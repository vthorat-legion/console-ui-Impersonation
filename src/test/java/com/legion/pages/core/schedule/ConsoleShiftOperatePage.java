package com.legion.pages.core.schedule;

import com.legion.pages.*;
import com.legion.tests.core.ScheduleTestKendraScott2;
import com.legion.utils.JsonUtil;
import com.legion.utils.MyThreadLocal;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.*;

import static com.legion.utils.MyThreadLocal.getDriver;

public class ConsoleShiftOperatePage extends BasePage implements ShiftOperatePage {
    public ConsoleShiftOperatePage() {
        PageFactory.initElements(getDriver(), this);
    }
    private static HashMap<String, String> propertySearchTeamMember = JsonUtil.getPropertiesFromJsonFile("src/test/resources/SearchTeamMember.json");

    @FindBy(css = "div.sch-day-view-shift-delete")
    private WebElement shiftDeleteBtn;

    @FindBy(css = ".sch-day-view-shift")
    private List<WebElement> dayViewAvailableShifts;

    @FindBy(css = "[ng-repeat=\"shift in shiftGroup\"]")
    private List<WebElement> dayViewShiftGroups;

    @FindBy(css = "div.sch-worker-popover")
    private WebElement shiftPopover;

    @FindBy(css = "button.sch-action.sch-save")
    private WebElement convertToOpenYesBtn;

    public void deleteShift() throws Exception {
        if(isElementLoaded(shiftDeleteBtn, 10)){
            clickTheElement(shiftDeleteBtn);
        }else{
            SimpleUtils.fail("Delete button is not available on Shift container",false);
        }
    }

    @Override
    public void convertAllUnAssignedShiftToOpenShift() throws Exception {
        ScheduleCommonPage scheduleCommonPage = new ConsoleScheduleCommonPage();
        ScheduleMainPage scheduleMainPage = new ConsoleScheduleMainPage();
        ScheduleShiftTablePage scheduleShiftTablePage = new ConsoleScheduleShiftTablePage();
        if (scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue())) {
            scheduleCommonPage.clickOnWeekView();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            for (WebElement unAssignedShift : scheduleShiftTablePage.getUnAssignedShifts()) {
                convertUnAssignedShiftToOpenShift(unAssignedShift);
            }
            waitForSeconds(3);
            scheduleMainPage.saveSchedule();
        } else {
            SimpleUtils.fail("Unable to convert UnAssigned Shift to Open Shift as 'Schedule' tab not active.", false);
        }

    }

    public void convertUnAssignedShiftToOpenShift(WebElement unAssignedShift) throws Exception {
//        By isUnAssignedShift = By.cssSelector("[ng-if=\"isUnassignedShift()\"]");
        By isUnAssignedShift = By.cssSelector(".rows .week-view-shift-image-optimized span");
        WebElement unAssignedPlusBtn = unAssignedShift.findElement(isUnAssignedShift);
        if (isElementLoaded(unAssignedPlusBtn)) {
            scrollToElement(unAssignedPlusBtn);
            clickTheElement(unAssignedPlusBtn);
            if (isElementLoaded(shiftPopover)) {
                WebElement convertToOpenOption = shiftPopover.findElement(By.cssSelector("[ng-if=\"canConvertToOpenShift() && !isTmView()\"]"));
                if (isElementLoaded(convertToOpenOption)) {
                    clickTheElement(convertToOpenOption);
                    if (isElementLoaded(convertToOpenYesBtn)) {
                        clickTheElement(convertToOpenYesBtn);
                        Thread.sleep(2000);
                    }
                }
            }
        }
    }

    @FindBy(css = ".week-schedule-shift .shift-container .rows .worker-image-optimized img")
    private List<WebElement> profileIcons;

    @FindBy(css = "div[ng-click=\"deleteShift($event, shift)\"]")
    private WebElement deleteShift;

    @FindBy(css = "[label=\"okLabel()\"]")
    private WebElement deleteBtnInDeleteWindows;

    @FindBy(css = "[src=\"img/legion/edit/deleted-shift-week.png\"]")
    private WebElement deleteShiftImg;

    @FindBy(xpath ="//div[contains(@ng-class,'selectManualOpenShiftActionClass()')]")
    private WebElement radioBtnManualOpenShift;

    @FindBy(css="button.sch-action.sch-save")
    private WebElement btnYesOpenSchedule;

    @FindBy(css="[ng-src=\"img/legion/edit/deleted-shift-day@2x.png\"]")
    private List<WebElement> deleteIconsInDayView;

    @Override
    public void  verifyDeleteShift() throws Exception {
        ScheduleMainPage scheduleMainPage = new ConsoleScheduleMainPage();
        ScheduleCommonPage scheduleCommonPage = new ConsoleScheduleCommonPage();
        List<WebElement> shiftIcons = null;
        if (scheduleCommonPage.isScheduleDayViewActive()) {
            shiftIcons = profileIconsDayView;
        } else
            shiftIcons = profileIcons;
        int count1 = shiftIcons.size();
        System.out.println(count1);
        clickOnProfileIcon();
        clickTheElement(deleteShift);
        if (isDeleteShiftShowWell ()) {
            click(deleteBtnInDeleteWindows);
            if (scheduleCommonPage.isScheduleDayViewActive()) {
                if (areListElementVisible(deleteIconsInDayView,5)) {
                    SimpleUtils.pass("delete shift draft successfully");
                }else
                    SimpleUtils.fail("delete shift draft failed",false);
            } else {
                if (isElementLoaded(deleteShiftImg,5)) {
                    SimpleUtils.pass("delete shift draft successfully");
                }else
                    SimpleUtils.fail("delete shift draft failed",false);
            }
        }
        scheduleMainPage.saveSchedule();
        waitForSeconds(3);
        int count2 = shiftIcons.size();
        System.out.println(count2);
        if (count1 > count2) {
            SimpleUtils.pass("delete shift successfully");
        }else
            SimpleUtils.fail("delete shift draft failed",false);
    }

    public String convertToOpenShiftAndOfferToSpecificTMs() throws Exception {
        NewShiftPage newShiftPage = new ConsoleNewShiftPage();
        String selectedTMName = null;
        if (isElementEnabled(radioBtnManualOpenShift, 5) && isElementEnabled(btnYesOpenSchedule)) {
            click(radioBtnManualOpenShift);
            click(btnYesOpenSchedule);
            waitForSeconds(3);
            selectedTMName = newShiftPage.searchAndGetTMName(propertySearchTeamMember.get("AssignTeamMember"));
            newShiftPage.clickOnOfferOrAssignBtn();
            SimpleUtils.pass("Shift been convert to open shift and offer to Specific TM successfully");
        } else {
            SimpleUtils.fail("Buttons on convert To Open PopUp windows load failed", false);
        }
        return selectedTMName;
    }

    @FindBy(css = "div.lgn-alert-title.ng-binding.warning")
    private WebElement titleInDeleteWindows;

    @FindBy(css = ".lgn-alert-message.ng-binding.ng-scope.warning")
    private WebElement alertMsgInDeleteWindows;

    @FindBy(css = "[label=\"cancelLabel()\"]")
    private WebElement cancelBtnInDeleteWindows;
    public boolean isDeleteShiftShowWell() throws Exception {
        if (isElementLoaded(titleInDeleteWindows,3) && isElementLoaded(alertMsgInDeleteWindows,3)
                && isElementLoaded(cancelBtnInDeleteWindows,3) && isElementLoaded(deleteBtnInDeleteWindows,3)) {
            SimpleUtils.pass("delete shift pop up window show well");
            return true;
        }else
            SimpleUtils.fail("delete shift pop up window load failed",true);
        return false;
    }

    @FindBy(css = "[label=\"Create New Shift\"]")
    private WebElement createNewShiftWeekView;

    @FindBy(css = "img[src*=\"openShift\"]")
    private List<WebElement> blueIconsOfOpenShift;
    @Override
    public void deleteLatestOpenShift() throws Exception {
        boolean isDeleted = false;
        if (isElementLoaded(createNewShiftWeekView, 5) && areListElementVisible(blueIconsOfOpenShift,5)) {
            for (int i = blueIconsOfOpenShift.size() - 1; i >= 0; i--) {
                moveToElementAndClick(blueIconsOfOpenShift.get(i));
                if (isPopOverLayoutLoaded()) {
                    clickTheElement(deleteShift);
                    if (isDeleteShiftShowWell()) {
                        click(deleteBtnInDeleteWindows);
                        if (isElementLoaded(deleteShiftImg, 5)) {
                            isDeleted = true;
                            SimpleUtils.pass("Schedule Week View: Existing shift: \" + Open Shift + \" deleted successfully");
                            break;
                        }
                    }
                }
            }
        } else
            SimpleUtils.fail("Delete shift button not found for Week Day: '" +
                    getActiveWeekText() + "'", true);
        if (!isDeleted)
            SimpleUtils.fail("Failed to delete the open shift on Last Day!", false);
    }

    @FindBy(className = "sch-worker-popover")
    private WebElement popOverLayout;
    public boolean isPopOverLayoutLoaded() throws Exception {
        boolean isLoaded = false;
        if (isElementLoaded(popOverLayout, 15)) {
            isLoaded = true;
            SimpleUtils.pass("Pop over layout loaded Successfully!");
        }
        return isLoaded;
    }


    @FindBy (className = "sch-calendar-day")
    private List<WebElement> scheduleCalendarDays;
    @Override
    public void selectAShiftToAssignTM(String username) throws Exception {
        NewShiftPage newShiftPage = new ConsoleNewShiftPage();
        boolean isFound = false;
        if (areListElementVisible(scheduleCalendarDays,10)) {
            for (WebElement day: scheduleCalendarDays) {
                if (!day.getAttribute("class").contains("sch-calendar-day-past")) {
                    WebElement dataDay = day.findElement(By.xpath("./../.."));
                    String data = dataDay.getAttribute("data-day");
                    List<WebElement> shifts = MyThreadLocal.getDriver().findElements(By.cssSelector("div[data-day=\"" + data + "\"].week-schedule-shift"));
                    if (shifts.size() > 0) {
                        int randomIndex = (new Random()).nextInt(shifts.size());
                        WebElement shiftImg = shifts.get(randomIndex).findElement(By.cssSelector(".rows span img"));
                        moveToElementAndClick(shiftImg);
                        if (isPopOverLayoutLoaded()) {
                            clickTheElement(popOverLayout.findElement(By.xpath("//span[contains(text(), \"Assign Team Member\")]")));
                            if (isAssignTeamMemberShowWell()) {
                                newShiftPage.searchText(username);
                                isFound = true;
                                SimpleUtils.pass("Assign Team Member: Select a shift and search the team member successfully");
                                break;
                            }
                        }
                    }
                }
            }
        } else
            SimpleUtils.fail("Schedule Page: Failed to find the schedule days",false);
        if (!isFound)
            SimpleUtils.fail("Assign Team Member: Failed to select a shift and search the team member",false);
    }


    @FindBy (className = "tma-header-text")
    private WebElement titleInSelectTeamMemberWindow;

    @FindBy(css = "div.tab-label")
    private List<WebElement> btnSearchteamMember;

    @FindBy(css = ".tma-search-field-input-text")
    private WebElement textSearch;

    @FindBy(css = ".sch-search")
    private WebElement searchIcon;
    public boolean isAssignTeamMemberShowWell() throws Exception {
        if (isElementLoaded(titleInSelectTeamMemberWindow,3) && areListElementVisible(btnSearchteamMember,3)
                && isElementLoaded(textSearch, 5) && isElementLoaded(searchIcon, 5)) {
            SimpleUtils.pass("Assign Team Member pop up window show well");
            return true;
        } else
            SimpleUtils.fail("Assign Team Member pop up window load failed",false);
        return false;
    }

    @FindBy (className = "week-schedule-shift-title")
    private List<WebElement> weekScheduleShiftTitles;

    @FindBy(css = "[src=\"img/legion/edit/deleted-shift-week.png\"]")
    private List<WebElement> deleteShiftImgsInWeekView;
    @Override
    public void deleteAllShiftsOfGivenDayPartInWeekView(String dayPart) throws Exception {
        boolean isFound = false;
        for (int i = 0; i < weekScheduleShiftTitles.size(); i++) {
            if (weekScheduleShiftTitles.get(i).getText().equals(dayPart)) {
                isFound = true;
                if (i == weekScheduleShiftTitles.size() - 1) {
                    List<WebElement> shifts = weekScheduleShiftTitles.get(i).findElements(By.xpath("./../../following-sibling::div//div[@class=\"rows\"]//span/span"));
                    int count1 = shifts.size();
                    for (int j = 0; j < count1; j++) {
                        clickTheElement(shifts.get(j));
                        if (isPopOverLayoutLoaded()) {
                            clickTheElement(deleteShift);
                            if (isDeleteShiftShowWell())
                                click(deleteBtnInDeleteWindows);
                        }
                    }
                    int count2 = deleteShiftImgsInWeekView.size();
                    if (count1 == count2)
                        SimpleUtils.pass("Schedule Page: Delete all the shifts in '" + dayPart + "' in week view successfully");
                    else
                        SimpleUtils.fail("Schedule Page: Failed to delete all the shifts in '\" + dayPart + \"' in week view",false);
                } else {
                    List<WebElement> shifts = weekScheduleShiftTitles.get(i).findElements(By.xpath("./../../following-sibling::div//div[@class=\"rows\"]//span/span"));
                    List<WebElement> shiftsOfNextDayPart = weekScheduleShiftTitles.get(i + 1).findElements(By.xpath("./../../following-sibling::div//div[@class=\"rows\"]//span/span"));
                    int count1 = shifts.size() - shiftsOfNextDayPart.size();
                    for (int j = 0; j < count1; j++) {
                        clickTheElement(shifts.get(j));
                        if (isPopOverLayoutLoaded()) {
                            clickTheElement(deleteShift);
                            if (isDeleteShiftShowWell())
                                click(deleteBtnInDeleteWindows);
                        }
                    }
                    int count2 = deleteShiftImgsInWeekView.size();
                    if (count1 == count2)
                        SimpleUtils.pass("Schedule Page: Delete all the shifts in '" + dayPart + "' in week view successfully");
                    else
                        SimpleUtils.fail("Schedule Page: Failed to delete all the shifts in '" + dayPart + "' in week view",false);
                }
                break;
            }
        }
        if (!isFound)
            SimpleUtils.report("Schedule Page: Not find the given day part in week view");
    }

    @FindBy(css = ".tma-dismiss-button")
    private WebElement closeViewStatusBtn;
    @Override
    public void closeViewStatusContainer() throws Exception{
        if(isElementLoaded(closeViewStatusBtn,10)){
            clickTheElement(closeViewStatusBtn);
            SimpleUtils.pass("Close button is available and clicked");
        }
        else {
            SimpleUtils.fail("Close Button is not enabled ", true);
        }

    }


    @FindBy (className = "sch-group-label")
    private List<WebElement> dayScheduleGroupLabels;

    @FindBy(css = "img[ng-src=\"img/legion/edit/deleted-shift-day@2x.png\"]")
    private List<WebElement> deleteShiftImgsInDayView;
    @Override
    public void deleteAllShiftsOfGivenDayPartInDayView(String dayPart) throws Exception {
        boolean isFound = false;
        int count1 = 0;
        for (int i = 0; i < dayScheduleGroupLabels.size(); i++) {
            if (dayScheduleGroupLabels.get(i).getText().equals(dayPart)) {
                isFound = true;
                for (int j = 0; j < dayScheduleGroupLabels.get(i).findElements(By.xpath("./../../following-sibling::div//worker-detail/div")).size(); j++) {
                    List<WebElement> shifts = dayScheduleGroupLabels.get(i).findElements(By.xpath("./../../following-sibling::div//worker-detail/div"));
                    count1 = shifts.size();
                    click(shifts.get(j));
                    if (isPopOverLayoutLoaded()) {
                        clickTheElement(deleteShift);
                        if (isDeleteShiftShowWell())
                            click(deleteBtnInDeleteWindows);
                    }
                }
                int count2 = deleteShiftImgsInDayView.size();
                if (count1 == count2)
                    SimpleUtils.pass("Schedule Page: Delete all the shifts in '" + dayPart + "' in day view successfully");
                else
                    SimpleUtils.fail("Schedule Page: Failed to delete all the shifts in '" + dayPart + "' in day view",false);
            }
            break;
        }
        if (!isFound)
            SimpleUtils.report("Schedule Page: Not find the given day part in week view");
    }

    @FindBy(css = "div.sch-worker-change-role-title")
    private WebElement schWorkerInfoPrompt;

    @FindBy(xpath ="//div[contains(@ng-click,'changeRoleMoveLeft($event)')]")
    private WebElement moveLeftWorkerInfoPrompt;

    @FindBy(xpath ="//div[contains(@ng-click,'changeRoleMoveRight($event)')]")
    private WebElement moveRightWorkerInfoPrompt;

    @FindBy(xpath= "//span[contains(@class,'sch-worker-change-role-name')]")
    private List<WebElement> schWrokersList;

    @FindBy(css= "div.sch-worker-change-role-body")
    private List<WebElement> shiftRoleList;

    @FindBy(css= "div.lgn-alert-modal")
    private WebElement roleViolationAlter;

    @FindBy(css= "button.lgn-action-button-success")
    private WebElement roleViolationAlterOkButton;

    @FindBy (css= "div[ng-style*='roleChangeStyle'] span")
    private List<WebElement> changeRoleValues;

    @FindBy (css = "div[ng-click*='changeRoleMoveRight'] i")
    private WebElement rightarrow;

    @FindBy (css = ".worker-change-role-edit-actions .sch-save")
    private WebElement applyButtonChangeRole;

    @FindBy (css = "button.sch-cancel")
    private WebElement cancelButtonChangeRole;

    @FindBy(css = "div[ng-class*='ChangeRole'] span")
    private WebElement changeRole;
    public void changeWorkRoleInPrompt(boolean isApplyChange) throws Exception {
        ScheduleShiftTablePage scheduleShiftTablePage = new ConsoleScheduleShiftTablePage();
        scheduleShiftTablePage.clickProfileIconOfShiftByIndex(0);
        WebElement clickedShift = getShiftElementByIndex(0);
        ScheduleCommonPage scheduleCommonPage = new ConsoleScheduleCommonPage();
        if (isElementLoaded(cancelButtonChangeRole, 3)){
            click(cancelButtonChangeRole);
            SimpleUtils.pass("Quit the change role modal!");
        }
        clickOnChangeRole();
        if(isElementEnabled(schWorkerInfoPrompt,5)) {
            SimpleUtils.pass("Various Work Role Prompt is displayed ");
            String newSelectedWorkRoleName = null;
            String originSelectedWorkRoleName = null;
            if (areListElementVisible(shiftRoleList, 5) && shiftRoleList.size() > 0) {
                if (shiftRoleList.size() == 1) {
                    SimpleUtils.pass("There is only one Work Role in Work Role list ");
                    return;
                } else {
                    for (WebElement shiftRole : shiftRoleList) {
                        if (shiftRole.getAttribute("class").contains("sch-worker-change-role-body-selected")) {
                            originSelectedWorkRoleName = shiftRole.findElement(By.cssSelector("span.sch-worker-change-role-name")).getText();
                            SimpleUtils.pass("The original selected Role is '" + originSelectedWorkRoleName);
                            break;
                        }
                    }
                    for (WebElement shiftRole : shiftRoleList) {
                        if (!shiftRole.getAttribute("class").contains("sch-worker-change-role-body-selected")) {
                            click(shiftRole);
                            newSelectedWorkRoleName = shiftRole.findElement(By.cssSelector("span.sch-worker-change-role-name")).getText();
                            SimpleUtils.pass("Role '" + newSelectedWorkRoleName + "' is selected!");
                            break;
                        }
                    }

                }
            } else {
                SimpleUtils.fail("Work roles are doesn't show well ", true);
            }

            if (isElementEnabled(applyButtonChangeRole, 5) && isElementEnabled(cancelButtonChangeRole, 5)) {
                SimpleUtils.pass("Apply and Cancel buttons are enabled");
                if (isApplyChange) {
                    moveToElementAndClick(applyButtonChangeRole);
                    if (isElementEnabled(roleViolationAlter, 5)) {
                        click(roleViolationAlterOkButton);
                    }
                    //to close the popup
                    waitForSeconds(5);
                    if (isElementLoaded(shiftPopover, 5)) {
                        clickTheElement(clickedShift);
                    }

                    clickedShift = getShiftElementByIndex(0);
                    if (scheduleCommonPage.isScheduleDayViewActive()) {
                        clickTheElement(clickedShift.findElement(By.cssSelector(".sch-shift-worker-img-cursor")));
                    } else
                        clickTheElement(clickedShift.findElement(By.cssSelector(".rows .worker-image-optimized img")));
                    SimpleUtils.pass("Apply button has been clicked ");
                } else {
                    click(cancelButtonChangeRole);
                    SimpleUtils.pass("Cancel button has been clicked ");
                }
            } else {
                SimpleUtils.fail("Apply and Cancel buttons are doesn't show well ", true);
            }

            //check the shift role
            if (!isElementEnabled(changeRole, 5)) {
                if (scheduleCommonPage.isScheduleDayViewActive()) {
                    click(clickedShift.findElement(By.cssSelector(".sch-shift-worker-img-cursor")));
                } else
                    click(clickedShift.findElement(By.cssSelector(".rows .worker-image-optimized img")));
            }
            clickOnChangeRole();
            if (areListElementVisible(shiftRoleList, 5) && shiftRoleList.size() >1) {
                for (WebElement shiftRole : shiftRoleList) {
                    if (shiftRole.getAttribute("class").contains("sch-worker-change-role-body-selected")) {
                        if (isApplyChange) {
                            String actualWorkRole = shiftRole.findElement(By.cssSelector("span.sch-worker-change-role-name")).getText();
                            if (actualWorkRole.equals(newSelectedWorkRoleName)) {
                                SimpleUtils.pass("Shift role been changed successfully ");
                            } else {
                                SimpleUtils.fail("Shift role failed to change , the actual is:"+actualWorkRole
                                        +" the expected is:"+ newSelectedWorkRoleName, false);
                            }
                        } else {
                            if (shiftRole.findElement(By.cssSelector("span.sch-worker-change-role-name")).getText().equals(originSelectedWorkRoleName)) {
                                SimpleUtils.pass("Shift role is not change ");
                            } else {
                                SimpleUtils.fail("Shift role still been changed when click Cancel button ", true);
                            }
                        }
                        break;
                    }
                }
            } else {
                SimpleUtils.fail("Shift roles are doesn't show well ", true);
            }
            if(isElementLoaded(cancelButtonChangeRole, 5)){
                click(cancelButtonChangeRole);
            }
        } else
            SimpleUtils.fail("Change work role popup fail to display! ", false);
    }

    public void clickOnChangeRole() throws Exception {
        if(isElementLoaded(changeRole,5))
        {
            clickTheElement(changeRole);
            SimpleUtils.pass("Change Role option is clicked");
        }
        else
            SimpleUtils.fail("Change Role option is not enable",false);
    }


    @Override
    public void  verifyDeleteShiftCancelButton() throws Exception {
        ShiftOperatePage shiftOperatePage = new ConsoleShiftOperatePage();
        WebElement shift = clickOnProfileIcon();
        clickTheElement(deleteShift);
        if (shiftOperatePage.isDeleteShiftShowWell ()) {
            if (isElementLoaded(cancelBtnInDeleteWindows, 10)) {
                clickTheElement(cancelBtnInDeleteWindows);
            }
            if (isElementEnabled(shift, 5)) {
                SimpleUtils.pass("Shift not been deleted after click cancel button");
            } else {
                SimpleUtils.fail("Shift still been deleted after click cancel button", false);
            }
        }
    }


    @FindBy(css = ".shift-container.week-schedule-shift-wrapper")
    private List<WebElement> shifts;

    @FindBy (css = "div.sch-day-view-shift-worker-detail")
    private List<WebElement> scheduleTableWeekViewWorkerDetail;



    @FindBy(css = "[id=\"legion_cons_Schedule_Schedule_TMInitialsImage\"]")
    private List<WebElement> profileIconsDayView;

    @Override
    public WebElement clickOnProfileIcon() throws Exception {
        WebElement selectedShift = null;
        if(areListElementVisible(profileIcons, 10)&& areListElementVisible(shifts, 10)) {
            int randomIndex = (new Random()).nextInt(profileIcons.size());
            int i = 0;
            while (i < 100 && profileIcons.get(randomIndex).getAttribute("src").contains("openShiftImage")){
                randomIndex = (new Random()).nextInt(profileIcons.size());
                i++;
            }
            selectedShift = shifts.get(randomIndex);
            clickTheElement(profileIcons.get(randomIndex));
        } else if (areListElementVisible(scheduleTableWeekViewWorkerDetail, 10) && areListElementVisible(dayViewAvailableShifts, 10)) {
            int randomIndex = (new Random()).nextInt(scheduleTableWeekViewWorkerDetail.size());
            int i = 0;
            ScheduleShiftTablePage scheduleShiftTablePage = new ConsoleScheduleShiftTablePage();
            String dayViewShiftNames = scheduleShiftTablePage.getTheShiftInfoInDayViewByIndex(randomIndex).get(0);;
            while (i < 100 && (dayViewShiftNames.contains("Open") || dayViewShiftNames.contains("Unassigned"))){
                randomIndex = (new Random()).nextInt(scheduleTableWeekViewWorkerDetail.size());
                i++;
            }
            selectedShift = dayViewAvailableShifts.get(randomIndex);
            clickTheElement(scheduleTableWeekViewWorkerDetail.get(randomIndex));
        } else
            SimpleUtils.fail("Can't Click on Profile Icon due to unavailability ",false);

        return selectedShift;
    }

    @Override
    public int getTheIndexWhenClickingOnProfileIcon() throws Exception {
        int index = 0;
        if(isProfileIconsEnable()&& areListElementVisible(shifts, 10)) {
            int randomIndex = (new Random()).nextInt(profileIcons.size());
            int i = 0;
            while (i < 100 && profileIcons.get(randomIndex).getAttribute("src").contains("openShiftImage")){
                randomIndex = (new Random()).nextInt(profileIcons.size());
                i++;
            }
            clickTheElement(profileIcons.get(randomIndex));
            index = randomIndex;
        } else if (areListElementVisible(scheduleTableWeekViewWorkerDetail, 10) && areListElementVisible(dayViewAvailableShifts, 10)) {
            int randomIndex = (new Random()).nextInt(scheduleTableWeekViewWorkerDetail.size());
            int i = 0;
            String dayViewShiftNames = dayViewAvailableShifts.get(randomIndex).findElement(By.className("sch-day-view-shift-worker-name")).getText();
            while (i < 100 && (dayViewShiftNames.contains("Open") || dayViewShiftNames.contains("Unassigned"))){
                randomIndex = (new Random()).nextInt(scheduleTableWeekViewWorkerDetail.size());
                i++;
            }
            clickTheElement(scheduleTableWeekViewWorkerDetail.get(randomIndex));
            index = randomIndex;
        } else
            SimpleUtils.fail("Can't Click on Profile Icon due to unavailability ",false);

        return index;
    }

    @Override
    public WebElement getShiftElementByIndex(int index) throws Exception {
        WebElement selectedShift = null;
        if(areListElementVisible(shifts, 10)) {
            selectedShift = shifts.get(index);
        } else if (areListElementVisible(scheduleTableWeekViewWorkerDetail, 10) && areListElementVisible(dayViewAvailableShifts, 10)) {
            selectedShift = dayViewAvailableShifts.get(index);
        } else
            SimpleUtils.fail("Shifts are not available in week/day view!",false);

        return selectedShift;
    }

    @FindBy(css = "[ng-if = \"!hasWorker() && isOpenShift()\"]")
    private List<WebElement> profileIconsDayViewForOpen;
    @Override
    public boolean isProfileIconsEnable() throws Exception {
        if(areListElementVisible(profileIcons,10)){
            SimpleUtils.pass("Profile Icon is present for selected Employee");
            return true;
        } else if (areListElementVisible(profileIconsDayView,10)) {
            SimpleUtils.pass("Profile Icon is present for selected Employee");
            return true;
        } else if (areListElementVisible(profileIconsDayViewForOpen,10)){
            SimpleUtils.pass("Profile Icon is present for selected Employee");
            return true;
        }
        else {
            SimpleUtils.fail("Profile Icon is not present for selected Employee", false);
            return false;
        }
    }


    @FindBy(css = "div[ng-click=\"editBreaksTime()\"]")
    private WebElement editMealBreakTime;

    @FindBy(css = "div.modal-instance-header-title")
    private WebElement editMealBreakTitle;

    @FindBy(css = "[data-tootik=\"Add a meal break\"] img")
    private WebElement addMealBreakButton;

    @FindBy(css = "[ng-click=\"closeModal()\"]")
    private WebElement cannelBtnInMealBreakButton;

    @FindBy(css = "[ng-click=\"confirm()\"]")
    private WebElement continueBtnInMealBreakButton;

    @FindBy(css = "day-part-weekday.ng-isolate-scope")
    private WebElement sliderInMealBreakButton;

    @FindBy(css = "[ng-click=\"removeBreak(b)\"]")
    private List<WebElement> deleteMealBreakButtons;

    @FindBy(css = "div.noUi-draggable.color_meal")
    private List<WebElement> mealBreaks;

    @FindBy(css = "div.slider-section-description-break-time-item-meal")
    private List<WebElement> mealBreakTimes;

    @FindBy(css = "[id=\"unconstrained\"]")
    private WebElement mealBreakBar;

    @FindBy(className = "lgn-alert-modal")
    private WebElement confirmWindow;

    @FindBy(className = "lgn-action-button-success")
    private WebElement okBtnOnConfirm;

    @FindBy(css="div.worker-shift-container")
    private WebElement shiftInfoContainer;

    @FindBy(xpath ="//div[contains(@class,'day-week-picker-period-week')][3]")
    private WebElement pickNextWeekOnSchedule;

    @FindBy(css = "div[ng-click=\"editShiftTime($event, shift)\"]")
    private WebElement editShiftTime;

    @FindBy(css = "div[ng-class*='AssignTM'] span")
    private WebElement assignTM;

    @FindBy(css = "div[ng-class*='ConvertToOpen'] span")
    private WebElement convertOpen;

    @FindBy(css = "div[ng-class*='OfferTMs']")
    private WebElement OfferTMS;

    @FindBy(css = ".slider-section-description-break-time-items-editable")
    private List<WebElement> breakTimeSliders;

    public boolean isEditMealBreakEnabled() throws Exception {
        clickOnProfileIcon();
        boolean isEditMealBreakEnabled = false;
        if(isElementLoaded(editMealBreakTime,5) )
        {
            if(editMealBreakTime.getText().equalsIgnoreCase("Edit Breaks")){
                isEditMealBreakEnabled = true;
                SimpleUtils.report("Edit Meal Break function is enabled! ");
            } else{
                SimpleUtils.report("We can only view breaks!");
            }
        }
        else
            SimpleUtils.fail("Edit Meal Break Time is disabled or not available to Click ", false);
        return isEditMealBreakEnabled;
    }

    @Override
    public void verifyMealBreakTimeDisplayAndFunctionality(boolean isEditMealBreakEnabled) throws Exception {
        clickOnProfileIcon();
        clickOnEditMeaLBreakTime();
        waitForSeconds(2);
        if (isMealBreakTimeWindowDisplayWell(isEditMealBreakEnabled)) {
            if (isEditMealBreakEnabled){
                click(addMealBreakButton);
                click(continueBtnInMealBreakButton);
                if (isElementEnabled(confirmWindow, 5)) {
                    click(okBtnOnConfirm);
                }
                SimpleUtils.pass("add meal break time successfully");
            } else {
                click(continueBtnInMealBreakButton);
                if (isElementEnabled(confirmWindow, 5)) {
                    click(okBtnOnConfirm);
                }
            }
        }else
            SimpleUtils.report("add meal break failed");
    }

    @Override
    public void verifyDeleteMealBreakFunctionality() throws Exception {
        ScheduleCommonPage scheduleCommonPage = new ConsoleScheduleCommonPage();
        ScheduleShiftTablePage scheduleShiftTablePage = new ConsoleScheduleShiftTablePage();
        WebElement selectedShift = clickOnProfileIcon();
        String id = selectedShift.getAttribute("id");

        clickOnEditMeaLBreakTime();
        if (isMealBreakTimeWindowDisplayWell(true)) {
            while (!areListElementVisible(deleteMealBreakButtons, 5) && deleteMealBreakButtons.size()>0) {
                click(cannelBtnInMealBreakButton);
                selectedShift = clickOnProfileIcon();
            }
            while(deleteMealBreakButtons.size()>0){
                click(deleteMealBreakButtons.get(0));
            }
            click(continueBtnInMealBreakButton);
            if (isElementEnabled(confirmWindow, 5)) {
                click(okBtnOnConfirm);
            }

        }else
            SimpleUtils.fail("Delete meal break window load failed",true);

        if (scheduleCommonPage.isScheduleDayViewActive()) {
            selectedShift = scheduleShiftTablePage.getShiftById(id);
            clickTheElement(selectedShift.findElement(By.cssSelector(".sch-day-view-shift .sch-shift-worker-img-cursor")));
        } else
            clickTheElement(selectedShift.findElement(By.cssSelector(".rows .worker-image-optimized img")));
        clickOnEditMeaLBreakTime();
        if (isMealBreakTimeWindowDisplayWell(true)) {
            if (!areListElementVisible(deleteMealBreakButtons, 5)) {
                SimpleUtils.pass("Delete meal break times successfully");
            } else {
                SimpleUtils.fail("Delete meal break failed",false);
            }

        }else
            SimpleUtils.fail("Delete meal break window load failed",false);
        click(cannelBtnInMealBreakButton);
    }

    @Override
    public void deleteMealOrRestBreaks(boolean isMealBreak) throws Exception {
        WebElement slider = null;
        if (isMealBreakTimeWindowDisplayWell(true) && areListElementVisible(breakTimeSliders, 5) && breakTimeSliders.size() == 2) {
            if (isMealBreak) {
                slider = breakTimeSliders.get(1);
            } else {
                slider = breakTimeSliders.get(0);
            }
            while (slider.findElements(By.tagName("i")).size() > 0) {
                click(slider.findElements(By.tagName("i")).get(0));
            }
            click(continueBtnInMealBreakButton);
            if (isElementEnabled(confirmWindow, 5)) {
                click(okBtnOnConfirm);
            }
        }
    }

    @Override
    public boolean isMealBreaksLoaded() throws Exception {
        boolean mealBreakLoaded = false;
        if (isElementLoaded(editMealBreakTitle) && isElementLoaded(sliderInMealBreakButton) && isElementLoaded(shiftInfoContainer)){
            if(areListElementVisible(mealBreaks, 5) && mealBreaks.size() >= 1){
                SimpleUtils.report("The Meal Break block shows on the Editing Meal Break dialog!");
                mealBreakLoaded = true;
            }else{
                SimpleUtils.report("The Meal Break block is not displayed on the Editing Meal Break dialog!");
                mealBreakLoaded = false;
            }
        }else{
            SimpleUtils.fail("The Meal Break window is not displayed on the Editing Meal Break dialog!", false);
        }
        return mealBreakLoaded;
    }

    @Override
    public void clickCancelBtnOnMealBreakDialog() throws Exception {
        ConsoleShiftOperatePage ShiftOperatePage = new ConsoleShiftOperatePage();
        boolean mealBreakDialogLoaded = ShiftOperatePage.isMealBreakTimeWindowDisplayWell(true);
        if (mealBreakDialogLoaded){
            clickTheElement(cannelBtnInMealBreakButton);
            SimpleUtils.report("Click the cancel button successfully!");
        }else{
            SimpleUtils.fail("The Meal Break dialog is not loaded!", false);
        }
    }

    public void verifyEditMealBreakTimeFunctionality(boolean isSavedChange) throws Exception {
        ScheduleCommonPage scheduleCommonPage = new ConsoleScheduleCommonPage();
        ScheduleShiftTablePage scheduleShiftTablePage = new ConsoleScheduleShiftTablePage();
        String mealBreakTimeBeforeEdit = null;
        String mealBreakTimeAfterEdit = null;

        WebElement selectedShift = clickOnProfileIcon();
        String id = selectedShift.getAttribute("id");
        clickOnEditMeaLBreakTime();
        if (isMealBreakTimeWindowDisplayWell(true)) {
            if (isElementLoaded(mealBreakBar, 15)){
                SimpleUtils.pass("The breaks bar is loaded successfully! ");
                if (!areListElementVisible(mealBreakTimes, 5)
                        || !areListElementVisible(mealBreaks, 5)
                        || mealBreakBar.getAttribute("class").contains("disabled")) {
                    click(addMealBreakButton);
                    SimpleUtils.pass("Click Add Meal Break button successfully! ");
                    click(continueBtnInMealBreakButton);
                    SimpleUtils.pass("Click Continue button successfully! ");
                    if (isElementEnabled(confirmWindow, 5)) {
                        click(okBtnOnConfirm);
                        SimpleUtils.pass("Click OK button successfully! ");
                    }
                    if (scheduleCommonPage.isScheduleDayViewActive()) {
                        selectedShift = scheduleShiftTablePage.getShiftById(id);
                        clickTheElement(selectedShift.findElement(By.cssSelector(".sch-day-view-shift .sch-shift-worker-img-cursor")));
                        SimpleUtils.pass("Click the selected shift avatar in day view successfully! ");
                    } else {
                        clickTheElement(selectedShift.findElement(By.cssSelector(".rows .worker-image-optimized img")));
                        SimpleUtils.pass("Click the selected shift avatar in week view successfully! ");
                    }
                    clickOnEditMeaLBreakTime();
                }
            } else
                SimpleUtils.fail("The breaks bar is fail to load! ", false);
            mealBreakTimeBeforeEdit = mealBreakTimes.get(0).getText();
            moveDayViewCards(mealBreaks.get(0), 40);
            mealBreakTimeAfterEdit = mealBreakTimes.get(0).getText();
            if (isSavedChange) {
                click(continueBtnInMealBreakButton);
                SimpleUtils.pass("Click Continue button to save break successfully! ");
                if (isElementEnabled(confirmWindow, 15)) {
                    click(okBtnOnConfirm);
                    SimpleUtils.pass("Click OK button to save break successfully! ");
                }
            } else {
                click(cannelBtnInMealBreakButton);
                SimpleUtils.pass("Click Cancel Btn successfully! ");
            }
        }else
            SimpleUtils.fail("Meal break window load failed",false);

        if (scheduleCommonPage.isScheduleDayViewActive()) {
            selectedShift = scheduleShiftTablePage.getShiftById(id);
            clickTheElement(selectedShift.findElement(By.cssSelector(".sch-day-view-shift .sch-shift-worker-img-cursor")));
            SimpleUtils.pass("Click the selected shift avatar in day view successfully! ");
        } else {
            clickTheElement(selectedShift.findElement(By.cssSelector(".rows .worker-image-optimized img")));
            SimpleUtils.pass("Click the selected shift avatar in week view successfully! ");
        }
        try {
            if(!isElementLoaded(editMealBreakTime,5)) {
                if (isElementLoaded(selectedShift.findElement(By.cssSelector("div.sch-day-view-shift-worker-detail")), 5)) {
                    clickTheElement(selectedShift.findElement(By.cssSelector("div.sch-day-view-shift-worker-detail")));
                } else if (isElementLoaded(selectedShift.findElement(By.cssSelector(".week-schedule-shift .shift-container .rows .worker-image-optimized img")))) {
                    clickTheElement(selectedShift.findElement(By.cssSelector(".week-schedule-shift .shift-container .rows .worker-image-optimized img")));
                }
            }
        } catch (Exception e) {
            if (isElementLoaded(selectedShift.findElement(By.cssSelector(".week-schedule-shift .shift-container .rows .worker-image-optimized img")))) {
                clickTheElement(selectedShift.findElement(By.cssSelector(".week-schedule-shift .shift-container .rows .worker-image-optimized img")));
            }
        }
        clickOnEditMeaLBreakTime();
        if (isMealBreakTimeWindowDisplayWell(true)) {
            if (isSavedChange) {
                if (mealBreakTimes.get(0).getText().equals(mealBreakTimeAfterEdit)) {
                    SimpleUtils.pass("Edit meal break times successfully");
                } else
                    SimpleUtils.fail("Edit meal break time failed",false);
            } else {
                if (mealBreakTimes.get(0).getText().equals(mealBreakTimeBeforeEdit)) {
                    SimpleUtils.pass("Edit meal break times not been changed after click Cancel button");
                } else
                    SimpleUtils.fail("Edit meal break times still been changed after click Cancel button",true);
            }
        }else
            SimpleUtils.fail("Meal break window load failed",false);
        click(cannelBtnInMealBreakButton);
        SimpleUtils.pass("Click Cancel Button successfully! ");
    }

    @Override
    public boolean isMealBreakTimeWindowDisplayWell(boolean isEditMealBreakEnabled) throws Exception {
        if (isEditMealBreakEnabled){
            if (isElementLoaded(editMealBreakTitle,5) && isElementLoaded(addMealBreakButton,5) &&
                    isElementLoaded(cannelBtnInMealBreakButton,5) && isElementLoaded(continueBtnInMealBreakButton,5)
                    && isElementLoaded(sliderInMealBreakButton,5) && isElementEnabled(shiftInfoContainer, 5)) {
                SimpleUtils.pass("the Edit Meal break windows is pop up which include: 1.profile info 2.add meal break button 3.Specify meal break time period 4 cancel ,continue button");
                return  true;
            }else
                SimpleUtils.fail("edit meal break time windows load failed",true);
            return false;
        } else {
            if (isElementLoaded(editMealBreakTitle,5) && isElementLoaded(continueBtnInMealBreakButton,5)
                    && isElementLoaded(sliderInMealBreakButton,5) && isElementEnabled(shiftInfoContainer, 5)) {
                SimpleUtils.pass("the Edit Meal break windows is pop up which include: 1.profile info 2.Specify meal break time period 3 continue button");
                return  true;
            }else
                SimpleUtils.fail("edit meal break time windows load failed",true);
            return false;
        }
    }


    @Override
    public void clickOnEditMeaLBreakTime() throws Exception{
//        if (!isElementLoaded(editMealBreakTime, 5)) {
//            clickOnProfileIcon();
//            waitForSeconds(1);
//        }
        if(isElementLoaded(editMealBreakTime,5))
        {
            clickTheElement(editMealBreakTime);
            SimpleUtils.pass("Clicked on Edit Meal Break Time ");
        }
        else {
            SimpleUtils.fail("Edit Meal Break Timeis disabled or not available to Click ", false);
        }
    }


    @Override
    public void editAndVerifyShiftTime(boolean isSaveChange) throws Exception{
        List<String> shiftTime;
        ScheduleCommonPage scheduleCommonPage = new ConsoleScheduleCommonPage();
        WebElement selectedShift = clickOnProfileIcon();
        clickOnEditShiftTime();
        shiftTime = editShiftTime();
        if (isSaveChange) {
            clickOnUpdateEditShiftTimeButton();
        } else {
            clickOnCancelEditShiftTimeButton();
        }

        if (scheduleCommonPage.isScheduleDayViewActive()) {
            clickTheElement(selectedShift.findElement(By.cssSelector(".sch-day-view-shift .sch-shift-worker-img-cursor")));
        } else
            clickTheElement(selectedShift.findElement(By.cssSelector(".rows .worker-image-optimized img")));

        clickOnEditShiftTime();

        if (isSaveChange) {
            verifyShiftTime(shiftTime.get(1));
        } else {
            verifyShiftTime(shiftTime.get(0));
        }

        clickOnCancelEditShiftTimeButton();
    }

    @FindBy(css="div.slider-section.slider-section-old")
    private WebElement shiftStartAndEndTimeContainer;

    @FindBy(css="[ng-click=\"closeModal()\"]")
    private WebElement cancelButtonInEditShiftTimeWindow;

    @FindBy(css=".modal-instance-button.confirm")
    private WebElement updateButtonInEditShiftTimeWindow;

    @FindBy(css="div.noUi-handle.noUi-handle-lower")
    private WebElement shiftStartTimeButton;

    @FindBy(css="div.noUi-handle.noUi-handle-upper")
    private WebElement shiftEndTimeButton;

    @FindBy(css = ".noUi-marker")
    private List<WebElement> noUiMakers;

    @FindBy(css = ".noUi-value")
    private List<WebElement> noUiValues;

    @FindBy(css = "div[ng-class=\"workerActionClass('ViewProfile')\"]")
    private WebElement viewProfileOnIcon;

    @FindBy(css = "div[ng-class=\"workerActionClass('ViewOpenShift')\"]")
    private WebElement viewOpenShift;
    @Override
    public void clickOnEditShiftTime() throws Exception{
        if(isElementLoaded(editShiftTime,5))
        {
            clickTheElement(editShiftTime);
            SimpleUtils.pass("Clicked on Edit Shift Time ");
        }
        else
            SimpleUtils.fail("Edit Shift Time is disabled or not available to Click ", false);
    }

    public List<String> editShiftTime() throws Exception {
        List<String> shiftTimes= new ArrayList<>();
        if (isElementEnabled(shiftStartAndEndTimeContainer, 5)
                && (isElementEnabled(shiftStartTimeButton, 5) || isElementLoaded(shiftStartInput, 5))
                && isElementEnabled(shiftEndTimeButton, 5) || isElementLoaded(shiftEndInput, 5)
                && isElementEnabled(shiftCardOnEditShiftTimePage, 5)) {
            String shiftTimeBeforeUpdate = getInfoFromCardOnEditShiftTimePage().get("shiftTime");
            shiftTimes.add(0, shiftTimeBeforeUpdate);
            if (isElementEnabled(shiftEndTimeButton, 5)) {
                if (areListElementVisible(noUiMakers, 5) && areListElementVisible(noUiValues, 5) && noUiMakers.size() == noUiValues.size()) {
                    String currentNow = shiftEndTimeButton.getAttribute("aria-valuenow");
                    int currentValue = Integer.parseInt(currentNow.substring(0, currentNow.indexOf('.')));
                    mouseHoverDragandDrop(shiftEndTimeButton, noUiMakers.get(currentValue - 1));
                    waitForSeconds(2);
                }
            } else {
                String oldEndTime = shiftTimeBeforeUpdate.split("-")[1];
                int newEnd = Integer.parseInt(oldEndTime.split(":")[0]) == 11 ?
                        Integer.parseInt(oldEndTime.split(":")[0]) - 1 : Integer.parseInt(oldEndTime.split(":")[0]) + 1;
                String newEndTime = newEnd + ":" + oldEndTime.split(":")[1];
                clearTheText(shiftEndInput);
                shiftEndInput.sendKeys(newEndTime);
            }

            String shiftTimeAfterUpdate = getInfoFromCardOnEditShiftTimePage().get("shiftTime");
            if (!shiftTimeBeforeUpdate.equals(shiftTimeAfterUpdate)) {
                SimpleUtils.pass("Edit Shift Time successfully");
                shiftTimes.add(1, shiftTimeAfterUpdate);
            } else {
                SimpleUtils.fail("Shift Time doesn't change", false);
            }

        } else {
            SimpleUtils.fail("Edit Shift Time container load failed", false);
        }
        return shiftTimes;
    }


    @Override
    public void clickOnUpdateEditShiftTimeButton() throws Exception{
        if(isElementLoaded(updateButtonInEditShiftTimeWindow,15))
        {
            if (checkIfUpdateButtonEnabled()) {
                click(updateButtonInEditShiftTimeWindow);
            } else
                SimpleUtils.fail("The Update button is disabled! ", false);
            try {
                if (isElementLoaded(okButton, 10)) {
                    clickTheElement(okButton);
                }
            } catch (Exception e) {
                // Do nothing
            }
            SimpleUtils.pass("Clicked on Update Edit Shift Time button");
        }
        else
            SimpleUtils.fail("Update button is disabled or not available to Click ", false);
    }

    @Override
    public void clickOnCancelEditShiftTimeButton() throws Exception{
        if(isElementLoaded(cancelButtonInEditShiftTimeWindow,5))
        {
            click(cancelButtonInEditShiftTimeWindow);
            waitUntilElementIsInVisible(cancelButtonInEditShiftTimeWindow);
            SimpleUtils.pass("Clicked on Cancel Edit Shift Time button");
        }
        else
            SimpleUtils.fail("Cancel button is disabled or not available to Click ", false);
    }


    public void verifyShiftTime(String shiftTime) throws Exception {
        if (isElementEnabled(shiftStartAndEndTimeContainer, 5)) {
            if (getInfoFromCardOnEditShiftTimePage().get("shiftTime").equals(shiftTime)) {
                SimpleUtils.pass("Edit Shift Time PopUp window load successfully");
            }

        } else {
            SimpleUtils.fail("Edit Shift Time container load failed", false);
        }
    }


    @Override
    public boolean verifyContextOfTMDisplay() throws Exception {
        clickOnProfileIcon();
        if (isViewProfileEnable()
                && isEditShiftTimeEnable()
                && isChangeRoleEnable()
                && isAssignTMEnable()
                && isConvertToOpenEnable()
                && isEditShiftNotesEnable()
                && isEditMealBreakTimeEnable()
                && isDeleteShiftEnable()) {
            SimpleUtils.pass("context of any TM show well and include: 1. View profile 2. Edit shift time 3. Change shift role  4.Assign TM 5.  Convert to open shift is enabled for current and future week day 6. Edit shift notes 7.Edit meal break time 8. Delete shift");
            return true;

        }else
            return false;
    }


    @Override
    public boolean isViewProfileEnable() throws Exception {
        return true;
    }

    @Override
    public void clickOnViewProfile() throws Exception {
        // Do nothing
    }


    @Override
    public boolean isViewOpenShiftEnable() throws Exception {
        if(isElementEnabled(viewOpenShift,5)){
            SimpleUtils.pass("View Open Shift  is enable/available on Pop Over Style!");
            return true;
        }
        else{
            SimpleUtils.fail("View Open Shift option is not enable/available on Pop Over Style ",false);
            return false;
        }
    }

    @Override
    public boolean isChangeRoleEnable() throws Exception {
        if(isElementEnabled(changeRole,5)){
            SimpleUtils.pass("Change Role is available on Pop Over Style!");
            return true;
        }
        else{
            SimpleUtils.fail("Change Role option is not enable/available on Pop Over Style ",false);
            return false;
        }
    }

    public boolean isEditShiftTimeEnable() throws Exception {
        if(isElementEnabled(editShiftTime,5)){
            SimpleUtils.pass("Edit Shift Time is available on Pop Over Style!");
            return true;
        }
        else{
            SimpleUtils.fail("Edit Shift Time is not enable/available on Pop Over Style ",false);
            return false;
        }
    }

    public boolean isEditMealBreakTimeEnable() throws Exception {
        if(isElementEnabled(editMealBreakTime,5)){
            SimpleUtils.pass("Edit Meal Break Time is available on Pop Over Style!");
            return true;
        }
        else{
            SimpleUtils.fail("Edit Meal Break Time is not enable/available on Pop Over Style ",false);
            return false;
        }
    }

    public boolean isDeleteShiftEnable() throws Exception {
        if(isElementEnabled(deleteShift,5)){
            SimpleUtils.pass("Delete Shift is available on Pop Over Style!");
            return true;
        }
        else{
            SimpleUtils.fail("Delete Shift is not enable/available on Pop Over Style ",false);
            return false;
        }
    }


    @Override
    public boolean isAssignTMEnable() throws Exception {
        if(isElementEnabled(assignTM,5)){
            SimpleUtils.pass("Assign TM is available on Pop Over Style!");
            return true;
        }
        else{
            SimpleUtils.fail("Assign TM option is not enable/available on Pop Over Style ",false);
            return false;
        }
    }


    @Override
    public boolean isEditShiftNotesEnable() throws Exception {
        if(isElementEnabled(EditShiftNotes,5)){
            SimpleUtils.pass("Edit shift notes is available on Pop Over Style!");
            return true;
        }
        else{
            SimpleUtils.fail("Edit shift notes option is not enable/available on Pop Over Style ",false);
            return false;
        }
    }

    @Override
    public void clickonAssignTM() throws Exception{
        if(isAssignTMEnable())
        {
            clickTheElement(assignTM);
            MyThreadLocal.setAssignTMStatus(true);
            SimpleUtils.pass("Clicked on Assign TM ");
        }
        else
            SimpleUtils.fail("Assign TM is disabled or not available to Click ", false);
    }

    @Override
    public void clickOnConvertToOpenShift() throws Exception{
        if(isConvertToOpenEnable())
        {
            clickTheElement(convertOpen);
            SimpleUtils.pass("Clicked on Convert to open shift successfully ");
        } else
            SimpleUtils.fail(" Convert to open shift is disabled or not available to Click ", false);
    }

    @Override
    public void verifyOfferTMOptionIsAvailable() throws Exception{
        if(isConvertToOpenEnable())
        {
            clickTheElement(convertOpen);
            SimpleUtils.pass("Clicked on Convert to open shift successfully ");
        } else
            SimpleUtils.fail(" Convert to open shift is disabled or not available to Click ", false);
    }



    @Override
    public boolean isConvertToOpenEnable() throws Exception {
        if(isElementEnabled(convertOpen,5)){
            SimpleUtils.pass("Convert To Open option is available on Pop Over Style!");
            return true;
        }
        else{
            SimpleUtils.report("Convert To Open option is not enable/available on Pop Over Style ");
            return false;
        }
    }

    @Override
    public boolean isOfferTMOptionVisible() throws Exception {
        if(isElementEnabled(OfferTMS,5)){
            return true;
        } else{
            return false;
        }
    }

    @Override
    public boolean isOfferTMOptionEnabled() throws Exception {
        if(isElementEnabled(OfferTMS,10) && !OfferTMS.getAttribute("class").toLowerCase().contains("graded-out")){
            return true;
        } else{
            return true;
        }
    }


    public WebElement clickOnProfileIconOfOpenShift() throws Exception {
        WebElement selectedShift = null;
        if(areListElementVisible(profileIcons, 10)
                && areListElementVisible(shifts, 10)) {
            int i;
            for (i=0; i<profileIcons.size(); i++){
                if (profileIcons.get(i).getAttribute("src").contains("openShiftImage")){
                    if (isElementLoaded(profileIcons.get(i), 5)) {
                        scrollToElement(profileIcons.get(i));
                    }
                    break;
                }
            }
            clickTheElement(profileIcons.get(i));
            selectedShift = shifts.get(i);
        } else if (areListElementVisible(scheduleTableWeekViewWorkerDetail, 10) && areListElementVisible(dayViewAvailableShifts, 10)) {
            int i;
            for (i=0; i<dayViewAvailableShifts.size(); i++){
                if (dayViewAvailableShifts.get(i).findElement(By.className("sch-day-view-shift-worker-name")).getText().contains("Open")){
                    break;
                }
            }
            clickTheElement(scheduleTableWeekViewWorkerDetail.get(i));
            selectedShift = dayViewAvailableShifts.get(i);
        } else {
            SimpleUtils.fail("Can't Click on Profile Icon due to unavailability ",false);
        }
        return selectedShift;
    }

    @FindBy(xpath = "//span[text()=\"View Status\"]")
    private WebElement viewStatusBtn;

    @Override
    public void offerTMByOpenShiftsCount( int openShiftsCount, String firstNameOfTM, String location) throws Exception {
        if (isProfileIconsEnable() && areListElementVisible(shifts, 15)) {
            if (openShiftsCount == profileIcons.size()){
                for (WebElement elm : profileIcons) {
                    scrollToElement(elm);
                    clickTheElement(elm);
                    waitForSeconds(2);
                    if (isOfferTMOptionEnabled()) {
                        SimpleUtils.pass("Offer TM option is enabled!");
                    } else {
                        SimpleUtils.fail("Offer TM  is not enabled!", false);
                    }
                    clickOnOfferTMOption();
                    searchTeamMemberByNameNLocation(firstNameOfTM, location);
                    clickOnOfferOrAssignBtn();
                    scrollToElement(elm);
                    clickTheElement(elm);
                    if(isElementLoaded(viewStatusBtn,15)){
                        clickTheElement(viewStatusBtn);
                        waitForSeconds(2);
                        SimpleUtils.pass("clicked view status button!");
                    } else {
                        SimpleUtils.fail("view status button is not loaded!",false);
                    }
                    verifyTMInTheOfferList(firstNameOfTM, "offered");
                    closeViewStatusContainer();
                }
            } else {
                SimpleUtils.fail("The open shifts count doesn't match the count in list grid!", false);
            }
        } else {
            SimpleUtils.fail("Can't Click on Profile Icon due to unavailability ",false);
        }
    }

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
                    click(searchIcon);
                    if (areListElementVisible(searchResults, 15)) {
                        for (WebElement searchResult : searchResults) {
                            WebElement workerName = searchResult.findElement(By.className("worker-edit-search-worker-name"));
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

    @Override
    public void clickAssignBtnOnCreateShiftDialog(String name) throws Exception {
        if (areListElementVisible(searchResultsOnNewCreateShiftPage, 30)) {
            for (WebElement searchResult : searchResultsOnNewCreateShiftPage) {
                List<WebElement> tmInfo = searchResult.findElements(By.cssSelector("p.MuiTypography-body1"));
                String tmName = tmInfo.get(0).getText();
                List<WebElement> assignAndOfferButtons = searchResult.findElements(By.tagName("button"));
                WebElement assignButton = assignAndOfferButtons.get(0);
                if (tmName != null && assignButton != null) {
                    if (tmName.toLowerCase().trim().replaceAll("\n", " ").contains(name.split(" ")[0].trim().toLowerCase())) {
                        if (MyThreadLocal.getAssignTMStatus()) {
                            clickTheElement(assignButton);
                        } else
                            SimpleUtils.fail("Can't get the TM status!", false);
                    } else
                        SimpleUtils.fail("TM name not match!", false);
                } else
                    SimpleUtils.fail("TM name is null or assign button is unavailable!", false);
            }
        }else {
            SimpleUtils.fail("No matched TM is displayed!", false);
        }
    }

    @FindBy(css="button.tma-action.sch-save")
    private WebElement btnOffer;

    @FindBy(css = "[ng-click=\"handleNext()\"]")
    private WebElement btnSaveOnNewCreateShiftPage;

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
        }else{
            SimpleUtils.fail("Offer Or Assign Button is not clickable", false);
        }
    }

    @FindBy(css="div.tmprofile.profile-container.ng-scope")
    private WebElement tmpProfileContainer;

    @FindBy(css = "span.tm-nickname.ng-binding")
    private WebElement personalDetailsName;

    @FindBy(css = "div.profile-close-button-container")
    private WebElement closeViewProfileContainer;
    public String getTMDetailNameFromProfilePage(WebElement shift) throws Exception {
        ScheduleCommonPage scheduleCommonPage = new ConsoleScheduleCommonPage();
        String tmDetailName = null;
        if (scheduleCommonPage.isScheduleDayViewActive()) {
            clickTheElement(shift.findElement(By.cssSelector(".sch-day-view-shift .sch-shift-worker-img-cursor")));
            SimpleUtils.pass("Click shift avatar in day view successfully!");
        } else {
            clickTheElement(shift.findElement(By.cssSelector(".rows .worker-image-optimized img")));
            SimpleUtils.pass("Click shift avatar in week view successfully!");
        }

        clickOnViewProfile();
        waitForSeconds(8);
        if (isElementEnabled(tmpProfileContainer, 15)) {
            SimpleUtils.pass("The profile page loaded successfully! ");
            if (isElementEnabled(personalDetailsName, 25)) {
                waitForSeconds(4);
                tmDetailName = personalDetailsName.getText().trim();
                if (tmDetailName.length()!=0 && tmDetailName.split(" ").length>1){
                    SimpleUtils.pass("Get employee detail name successfully! The detail name is:"+tmDetailName);
                }else
                    SimpleUtils.fail("Fail to get the employee detail name on profile page! The actual name is:"
                            +tmDetailName, false);
            } else
                SimpleUtils.fail("TM detail name fail to load!", false);
        } else
            SimpleUtils.fail("Profile page fail to load!", false);
        clickTheElement(closeViewProfileContainer);
        return tmDetailName;
    }

    @FindBy(css = ".noUi-connect.color_meal")
    private List<WebElement> mealBreakDurations;
    @FindBy(css = ".noUi-connect.color_rest")
    private List<WebElement> restBreakDurations;
    @FindBy(css = "[ng-click*=\"addBreak\"]")
    private List<WebElement> addBreakBtns;
    @FindBy(css = ".noUi-touch-area.color_meal")
    private List<WebElement> mealStartEndAreas;
    @FindBy(css = ".noUi-touch-area.color_rest")
    private List<WebElement> restStartEndAreas;
    @FindBy(css = "div.slider-section-description-break-time-item-rest")
    private List<WebElement> restBreakTimes;
    @Override
    public List<String> verifyEditBreaks() throws Exception {
        List<String> breakTimes = new ArrayList<>();
        if (isMealBreakTimeWindowDisplayWell(true)) {
            // Verify delete breaks functionality
            while(deleteMealBreakButtons.size()>0){
                click(deleteMealBreakButtons.get(0));
            }
            // Verify adding breaks functionality
            if (areListElementVisible(addBreakBtns, 5)) {
                for (WebElement addBreakBtn : addBreakBtns) {
                    clickTheElement(addBreakBtn);
                }
            } else {
                SimpleUtils.fail("Add meal & rest break buttons not loaded successfully!", false);
            }
            if (areListElementVisible(mealBreakDurations, 5) && areListElementVisible(restBreakDurations, 5) && mealBreakDurations.size() == 1
                    && restBreakDurations.size() == 1) {
                moveDayViewCards(mealBreakDurations.get(0), 40);
                moveDayViewCards(restBreakDurations.get(0), 40);
            } else {
                SimpleUtils.fail("Meal and rest breaks are not added after clicking the add button!", false);
            }
            if (areListElementVisible(mealStartEndAreas, 5) && areListElementVisible(restStartEndAreas, 5)) {
                moveDayViewCards(mealStartEndAreas.get(0), 40);
                moveDayViewCards(restStartEndAreas.get(0), 40);
            } else {
                SimpleUtils.fail("Meal and rest start/end area not loaded successfully!", false);
            }
            breakTimes.add(mealBreakTimes.get(0).getText());
            breakTimes.add(restBreakTimes.get(0).getText());
        }else
            SimpleUtils.fail("Edit meal break window load failed",false);
        return breakTimes;
    }

    @Override
    public void moveMealOrRestBreak(boolean isMeal, int offset) throws Exception {
        List<WebElement> durations = null;
        if (areListElementVisible(mealBreakDurations, 5) || areListElementVisible(restBreakDurations, 5)) {
            if (isMeal) {
                durations = mealBreakDurations;
            } else {
                durations = restBreakDurations;
            }
            moveDayViewCards(durations.get(0), offset);
        } else {
            clickTheElement(addMealBreakButton);
        }
    }

    @Override
    public void shortenMealOrRestBreak(boolean isMealBreak) throws Exception {
        if (areListElementVisible(mealStartEndAreas, 5) || areListElementVisible(restStartEndAreas, 5)) {
            List<WebElement> areas = isMealBreak ? mealStartEndAreas : restStartEndAreas;
            moveDayViewCards(areas.get(0), 40);
        } else {
            SimpleUtils.fail("Meal break start/end area not loaded successfully!", false);
        }
    }

    @Override
    public void changeWorkRoleInPromptOfAShift(boolean isApplyChange, WebElement shift) throws Exception {
        WebElement clickedShift = shift;
        clickOnChangeRole();
        if(isElementEnabled(schWorkerInfoPrompt,5)) {
            SimpleUtils.pass("Various Work Role Prompt is displayed ");
            String newSelectedWorkRoleName = null;
            String originSelectedWorkRoleName = null;
            if (areListElementVisible(shiftRoleList, 5) && shiftRoleList.size() > 0) {
                if (shiftRoleList.size() == 1) {
                    SimpleUtils.pass("There is only one Work Role in Work Role list ");
                    return;
                } else {
                    for (WebElement shiftRole : shiftRoleList) {
                        if (shiftRole.getAttribute("class").contains("sch-worker-change-role-body-selected")) {
                            originSelectedWorkRoleName = shiftRole.findElement(By.cssSelector("span.sch-worker-change-role-name")).getText();
                            SimpleUtils.pass("The original selected Role is '" + originSelectedWorkRoleName);
                            break;
                        }
                    }
                    for (WebElement shiftRole : shiftRoleList) {
                        if (!shiftRole.getAttribute("class").contains("sch-worker-change-role-body-selected")) {
                            click(shiftRole);
                            newSelectedWorkRoleName = shiftRole.findElement(By.cssSelector("span.sch-worker-change-role-name")).getText();
                            SimpleUtils.pass("Role '" + newSelectedWorkRoleName + "' is selected!");
                            break;
                        }
                    }

                }
            } else {
                SimpleUtils.fail("Work roles are doesn't show well ", true);
            }

            if (isElementEnabled(applyButtonChangeRole, 5) && isElementEnabled(cancelButtonChangeRole, 5)) {
                SimpleUtils.pass("Apply and Cancel buttons are enabled");
                if (isApplyChange) {
                    click(applyButtonChangeRole);
                    if (isElementEnabled(roleViolationAlter, 5)) {
                        click(roleViolationAlterOkButton);
                    }
                    //to close the popup
                    waitForSeconds(5);
                    clickTheElement(clickedShift);

                    clickTheElement(clickedShift.findElement(By.cssSelector(".rows .worker-image-optimized img")));
                    SimpleUtils.pass("Apply button has been clicked ");
                } else {
                    click(cancelButtonChangeRole);
                    SimpleUtils.pass("Cancel button has been clicked ");
                }
            } else {
                SimpleUtils.fail("Apply and Cancel buttons are doesn't show well ", true);
            }

            //check the shift role
            if (!isElementEnabled(changeRole, 5)) {
                click(clickedShift.findElement(By.cssSelector(".rows .worker-image-optimized img")));
            }
            clickOnChangeRole();
            if (areListElementVisible(shiftRoleList, 5) && shiftRoleList.size() >1) {
                for (WebElement shiftRole : shiftRoleList) {
                    if (shiftRole.getAttribute("class").contains("sch-worker-change-role-body-selected")) {
                        if (isApplyChange) {
                            if (shiftRole.findElement(By.cssSelector("span.sch-worker-change-role-name")).getText().equals(newSelectedWorkRoleName)) {
                                SimpleUtils.pass("Shift role been changed successfully ");
                            } else {
                                SimpleUtils.fail("Shift role failed to change ", false);
                            }
                        } else {
                            if (shiftRole.findElement(By.cssSelector("span.sch-worker-change-role-name")).getText().equals(originSelectedWorkRoleName)) {
                                SimpleUtils.pass("Shift role is not change ");
                            } else {
                                SimpleUtils.fail("Shift role still been changed when click Cancel button ", true);
                            }
                        }
                        break;
                    }
                }
            } else {
                SimpleUtils.fail("Shift roles are doesn't show well ", true);
            }
            if(isElementLoaded(cancelButtonChangeRole, 5)){
                click(cancelButtonChangeRole);
            }
        }
    }

    @Override
    public void changeWorkRoleInPromptOfAShiftInDayView(boolean isApplyChange, String shiftid) throws Exception {
        ScheduleShiftTablePage scheduleShiftTablePage = new ConsoleScheduleShiftTablePage();
        clickOnChangeRole();
        if(isElementEnabled(schWorkerInfoPrompt,5)) {
            SimpleUtils.pass("Various Work Role Prompt is displayed ");
            String newSelectedWorkRoleName = null;
            String originSelectedWorkRoleName = null;
            if (areListElementVisible(shiftRoleList, 5) && shiftRoleList.size() > 0) {
                if (shiftRoleList.size() == 1) {
                    SimpleUtils.pass("There is only one Work Role in Work Role list ");
                    return;
                } else {
                    for (WebElement shiftRole : shiftRoleList) {
                        if (shiftRole.getAttribute("class").contains("sch-worker-change-role-body-selected")) {
                            originSelectedWorkRoleName = shiftRole.findElement(By.cssSelector("span.sch-worker-change-role-name")).getText();
                            SimpleUtils.pass("The original selected Role is '" + originSelectedWorkRoleName);
                            break;
                        }
                    }
                    for (WebElement shiftRole : shiftRoleList) {
                        if (!shiftRole.getAttribute("class").contains("sch-worker-change-role-body-selected")) {
                            click(shiftRole);
                            newSelectedWorkRoleName = shiftRole.findElement(By.cssSelector("span.sch-worker-change-role-name")).getText();
                            SimpleUtils.pass("Role '" + newSelectedWorkRoleName + "' is selected!");
                            break;
                        }
                    }

                }
            } else {
                SimpleUtils.fail("Work roles are doesn't show well ", true);
            }

            if (isElementEnabled(applyButtonChangeRole, 5) && isElementEnabled(cancelButtonChangeRole, 5)) {
                SimpleUtils.pass("Apply and Cancel buttons are enabled");
                if (isApplyChange) {
                    click(applyButtonChangeRole);
                    if (isElementEnabled(roleViolationAlter, 5)) {
                        click(roleViolationAlterOkButton);
                    }
                    //to close the popup
                    waitForSeconds(5);
                    clickTheElement(scheduleShiftTablePage.getShiftById(shiftid));

                    clickTheElement(scheduleShiftTablePage.getShiftById(shiftid).findElement(By.cssSelector(".sch-shift-worker-img-cursor")));
                    SimpleUtils.pass("Apply button has been clicked ");
                } else {
                    click(cancelButtonChangeRole);
                    SimpleUtils.pass("Cancel button has been clicked ");
                }
            } else {
                SimpleUtils.fail("Apply and Cancel buttons are doesn't show well ", true);
            }

            //check the shift role
            if (!isElementEnabled(changeRole, 5)) {
                click(scheduleShiftTablePage.getShiftById(shiftid).findElement(By.cssSelector(".sch-shift-worker-img-cursor")));
            }
            clickOnChangeRole();
            if (areListElementVisible(shiftRoleList, 5) && shiftRoleList.size() >1) {
                for (WebElement shiftRole : shiftRoleList) {
                    if (shiftRole.getAttribute("class").contains("sch-worker-change-role-body-selected")) {
                        if (isApplyChange) {
                            if (shiftRole.findElement(By.cssSelector("span.sch-worker-change-role-name")).getText().equals(newSelectedWorkRoleName)) {
                                SimpleUtils.pass("Shift role been changed successfully ");
                            } else {
                                SimpleUtils.fail("Shift role failed to change ", false);
                            }
                        } else {
                            if (shiftRole.findElement(By.cssSelector("span.sch-worker-change-role-name")).getText().equals(originSelectedWorkRoleName)) {
                                SimpleUtils.pass("Shift role is not change ");
                            } else {
                                SimpleUtils.fail("Shift role still been changed when click Cancel button ", true);
                            }
                        }
                        break;
                    }
                }
            } else {
                SimpleUtils.fail("Shift roles are doesn't show well ", true);
            }
            if(isElementLoaded(cancelButtonChangeRole, 5)){
                click(cancelButtonChangeRole);
            }
        }
    }

    @Override
    public void verifyEditMealBreakTimeFunctionalityForAShift(boolean isSavedChange, WebElement shift) throws Exception {
        String mealBreakTimeBeforeEdit = null;
        String mealBreakTimeAfterEdit = null;

        WebElement selectedShift = shift;
        clickOnEditMeaLBreakTime();
        if (isMealBreakTimeWindowDisplayWell(true)) {
            if (!areListElementVisible(mealBreakTimes, 5)
                    || !areListElementVisible(mealBreaks, 5)
                    || mealBreakBar.getAttribute("class").contains("disabled")) {
                click(addMealBreakButton);
                click(continueBtnInMealBreakButton);
                if (isElementEnabled(confirmWindow, 5)) {
                    click(okBtnOnConfirm);
                }
                click(selectedShift.findElement(By.cssSelector(".rows .worker-image-optimized img")));
                clickOnEditMeaLBreakTime();
            }
            mealBreakTimeBeforeEdit = mealBreakTimes.get(0).getText();
            moveDayViewCards(mealBreaks.get(0), 40);
            mealBreakTimeAfterEdit = mealBreakTimes.get(0).getText();
            if (isSavedChange) {
                click(continueBtnInMealBreakButton);
                if (isElementEnabled(confirmWindow, 5)) {
                    click(okBtnOnConfirm);
                }
            } else {
                click(cannelBtnInMealBreakButton);
            }
        }else
            SimpleUtils.fail("Meal break window load failed",true);

        ScheduleCommonPage scheduleCommonPage = new ConsoleScheduleCommonPage();
        if (scheduleCommonPage.isScheduleDayViewActive()) {
            click(selectedShift.findElement(By.cssSelector(".sch-day-view-shift .sch-shift-worker-img-cursor")));
        } else {
            click(selectedShift.findElement(By.cssSelector(".rows .worker-image-optimized img")));
        }
        clickOnEditMeaLBreakTime();
        if (isMealBreakTimeWindowDisplayWell(true)) {
            if (isSavedChange) {
                if (mealBreakTimes.get(0).getText().equals(mealBreakTimeAfterEdit)) {
                    SimpleUtils.pass("Edit meal break times successfully");
                } else
                    SimpleUtils.fail("Edit meal break time failed",true);
            } else {
                if (mealBreakTimes.get(0).getText().equals(mealBreakTimeBeforeEdit)) {
                    SimpleUtils.pass("Edit meal break times not been changed after click Cancel button");
                } else
                    SimpleUtils.fail("Edit meal break times still been changed after click Cancel button",true);
            }
        }else
            SimpleUtils.fail("Meal break window load failed",true);
        click(cannelBtnInMealBreakButton);
    }

    @Override
    public void verifyEditMealBreakTimeFunctionalityForAShiftInDayView(boolean isSavedChange, String shiftid) throws Exception {
        ScheduleShiftTablePage scheduleShiftTablePage = new ConsoleScheduleShiftTablePage();
        String mealBreakTimeBeforeEdit = null;
        String mealBreakTimeAfterEdit = null;

        WebElement selectedShift = scheduleShiftTablePage.getShiftById(shiftid);
        clickOnEditMeaLBreakTime();
        if (isMealBreakTimeWindowDisplayWell(true)) {
            if (!areListElementVisible(mealBreakTimes, 5)
                    || !areListElementVisible(mealBreaks, 5)
                    || mealBreakBar.getAttribute("class").contains("disabled")) {
                click(addMealBreakButton);
                click(continueBtnInMealBreakButton);
                if (isElementEnabled(confirmWindow, 5)) {
                    click(okBtnOnConfirm);
                }
                click(scheduleShiftTablePage.getShiftById(shiftid).findElement(By.cssSelector(".sch-shift-worker-img-cursor")));
                clickOnEditMeaLBreakTime();
            }
            mealBreakTimeBeforeEdit = mealBreakTimes.get(0).getText();
            moveDayViewCards(mealBreaks.get(0), 40);
            mealBreakTimeAfterEdit = mealBreakTimes.get(0).getText();
            if (isSavedChange) {
                click(continueBtnInMealBreakButton);
                if (isElementEnabled(confirmWindow, 5)) {
                    click(okBtnOnConfirm);
                }
            } else {
                click(cannelBtnInMealBreakButton);
            }
        }else
            SimpleUtils.fail("Meal break window load failed",true);

        click(scheduleShiftTablePage.getShiftById(shiftid).findElement(By.cssSelector(".sch-shift-worker-img-cursor")));
        clickOnEditMeaLBreakTime();
        if (isMealBreakTimeWindowDisplayWell(true)) {
            if (isSavedChange) {
                if (mealBreakTimes.get(0).getText().equals(mealBreakTimeAfterEdit)) {
                    SimpleUtils.pass("Edit meal break times successfully");
                } else
                    SimpleUtils.fail("Edit meal break time failed",true);
            } else {
                if (mealBreakTimes.get(0).getText().equals(mealBreakTimeBeforeEdit)) {
                    SimpleUtils.pass("Edit meal break times not been changed after click Cancel button");
                } else
                    SimpleUtils.fail("Edit meal break times still been changed after click Cancel button",true);
            }
        }else
            SimpleUtils.fail("Meal break window load failed",true);
        click(cannelBtnInMealBreakButton);
    }


    @FindBy(css="div.edit-breaks-time-modal")
    private WebElement editShiftTimePopUp;
    @FindBy(css = ".modal-instance-button.confirm.ng-binding")
    private WebElement confirmBtnOnDragAndDropConfirmPage;
    @FindBy(className = "slider-container")
    private WebElement sliderContainer;
    @FindBy(css = "[aria-label=\"Shift start\"]")
    private WebElement shiftStartInput;
    @FindBy(css = "[aria-label=\"Shift end\"]")
    private WebElement shiftEndInput;
    public void editTheShiftTimeForSpecificShift(WebElement shift, String startTime, String endTime) throws Exception {
        ScheduleCommonPage scheduleCommonPage = new ConsoleScheduleCommonPage();
        By isUnAssignedShift = null;
        if (!scheduleCommonPage.isScheduleDayViewActive())
            isUnAssignedShift = By.cssSelector(".rows .week-view-shift-image-optimized span");
        else
            isUnAssignedShift = By.cssSelector(".sch-day-view-shift-outer [class=\"allow-pointer-events \"]");
        WebElement shiftPlusBtn = shift.findElement(isUnAssignedShift);
        if (isElementLoaded(shiftPlusBtn)) {
            clickTheElement(shiftPlusBtn);
            if (isElementLoaded(shiftPopover, 10)) {
                WebElement editShiftTimeOption = shiftPopover.findElement(By.cssSelector("[ng-if=\"canEditShiftTime && !isTmView()\"]"));
                if (isElementLoaded(editShiftTimeOption, 20)) {
                    scrollToElement(editShiftTimeOption);
                    click(editShiftTimeOption);
                    waitForSeconds(3);
                    if (isElementEnabled(editShiftTimePopUp, 15)) {
                        if (isElementLoaded(sliderContainer, 10)) {
                            moveSliderAtCertainPointOnEditShiftTimePage(endTime, "End");
                            moveSliderAtCertainPointOnEditShiftTimePage(startTime, "Start");
                            waitForSeconds(2);
                            click(confirmBtnOnDragAndDropConfirmPage);
                        } else {
                            if (!startTime.contains(":")) {
                                startTime = startTime.replace("am", ":00am").replace("pm", ":00pm");
                            }
                            if (startTime.split(":")[0].replace("am", "").replace("pm", "").length()==1) {
                                startTime = "0"+startTime;
                            }
                            if (!endTime.contains(":")){
                                endTime = endTime.replace("am", ":00am").replace("pm", ":00pm");
                            }
                            if (endTime.split(":")[0].replace("am", "").replace("pm", "").length()==1) {
                                endTime = "0"+endTime;
                            }
                            shiftStartInput.clear();
                            shiftEndInput.clear();
                            clickTheElement(shiftCardOnEditShiftTimePage);
                            waitForSeconds(2);
                            clickTheElement(shiftStartInput);
                            shiftStartInput.sendKeys(startTime);
                            waitForSeconds(2);
                            click(shiftEndInput);
                            shiftEndInput.sendKeys(endTime);
                            clickOnUpdateEditShiftTimeButton();
                        }

                    } else {
                        SimpleUtils.fail("Edit Shift Time PopUp window load failed", false);
                    }
                }
            }
        }
    }

    //Elements on the set opening and closing shift times page on Control -> Working hours page

    @FindBy(css = ".ui-droppable")
    private List<WebElement> shiftTimeValuesOnSetOpeningOrClosingTimesPage;

    @FindBy(css = ".null-selector-start")
    private WebElement startSliderOnSetOpeningOrClosingTimesPage;

    @FindBy(css = ".null-selector-end")
    private WebElement endSliderOnSetOpeningOrClosingTimesPage;
    public void moveSliderAtCertainPointOnEditShiftTimePage(String shiftTime, String startingPoint) throws Exception {
        WebElement element = null;
        String time = "am";
        List<WebElement> shiftTimeValues = new ArrayList<>();
        WebElement startSlider = null;
        WebElement endSlider = null;
        if (areListElementVisible(shiftTimeValuesOnSetOpeningOrClosingTimesPage, 15) && shiftTimeValuesOnSetOpeningOrClosingTimesPage.size() >0) {
            shiftTimeValues = shiftTimeValuesOnSetOpeningOrClosingTimesPage;
            startSlider = startSliderOnSetOpeningOrClosingTimesPage;
            endSlider = endSliderOnSetOpeningOrClosingTimesPage;
        } else if (areListElementVisible(noUiValues, 10) && noUiValues.size() >0) {
            shiftTimeValues = noUiValues;
            startSlider = shiftStartTimeButton;
            endSlider = shiftEndTimeButton;
        }  else
            SimpleUtils.fail("Shift time values fail to loaded! ", false);
        if(areListElementVisible(shiftTimeValues, 15) && shiftTimeValues.size() >0){
            for (WebElement noUiValue: shiftTimeValues){
                if (noUiValue.getAttribute("class").contains("pm") || noUiValue.getText().contains("PM")) {
                    time = "pm";
                } else if (noUiValue.getAttribute("class").contains("am") || noUiValue.getText().contains("AM")){
                    time = "am";
                }
                if (time.equalsIgnoreCase(shiftTime.substring(shiftTime.length() - 2))) {
                    if(noUiValue.getText().equals(shiftTime.substring(0, shiftTime.length() - 2).split(":")[0])){
                        element = noUiValue;
                        break;
                    }
                }
            }
        }
        if (element == null){
            SimpleUtils.fail("Cannot found the operating hour on edit operating hour page! ", false);
        }
        if(startingPoint.equalsIgnoreCase("End")){
            if(isElementLoaded(endSlider,10)){
                SimpleUtils.pass("Shift timings with Sliders loaded on page Successfully for End Point");
                mouseHoverDragandDrop(endSlider,element);
            } else{
                SimpleUtils.fail("Shift timings with Sliders not loaded on page Successfully", false);
            }
        }else if(startingPoint.equalsIgnoreCase("Start")){
            if(isElementLoaded(startSlider,10)){
                SimpleUtils.pass("Shift timings with Sliders loaded on page Successfully for End Point");
                mouseHoverDragandDrop(startSlider,element);
            } else{
                SimpleUtils.fail("Shift timings with Sliders not loaded on page Successfully", false);
            }
        }
    }

    @Override
    public void deleteMealBreakForOneShift(WebElement shift) throws Exception {
        click(shift.findElement(By.cssSelector(".rows .worker-image-optimized img")));
        clickOnEditMeaLBreakTime();

        if (areListElementVisible(deleteMealBreakButtons, 5)) {
            while(deleteMealBreakButtons.size()>0){
                click(deleteMealBreakButtons.get(0));
            }

            SimpleUtils.pass("Delete meal break times successfully");
        } else {
            SimpleUtils.report("Delete meal break fail to load! ");
        }
        click(continueBtnInMealBreakButton);
        if (isElementEnabled(confirmWindow, 5)) {
            click(okBtnOnConfirm);
        }
    }

    @Override
    public String getRandomWorkRole() throws Exception {
        ScheduleShiftTablePage scheduleShiftTablePage = new ConsoleScheduleShiftTablePage();
        List<String> shiftInfo = new ArrayList<>();
        while (shiftInfo.size() == 0) {
            shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(0);
        }
        return shiftInfo.get(4);
    }

    @FindBy(className = "week-schedule-shift-wrapper")
    private List<WebElement> shiftsWeekView;
    @Override
    public void deleteAllOOOHShiftInWeekView() throws Exception {
        ScheduleMainPage scheduleMainPage = new ConsoleScheduleMainPage();
        SmartCardPage smartCardPage = new ConsoleSmartCardPage();
        ScheduleShiftTablePage scheduleShiftTablePage = new ConsoleScheduleShiftTablePage();
        if (areListElementVisible(shiftsWeekView, 15) && smartCardPage.isRequiredActionSmartCardLoaded()) {
            smartCardPage.clickOnViewShiftsBtnOnRequiredActionSmartCard();
            for (WebElement shiftWeekView : shiftsWeekView) {
                try {
                    if (scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(shiftWeekView).contains("Outside Operating hours")) {
                        WebElement image = shiftWeekView.findElement(By.cssSelector(".rows .week-view-shift-image-optimized span"));
                        clickTheElement(image);
                        waitForSeconds(3);
                        if (isElementLoaded(deleteShift, 5)) {
                            clickTheElement(deleteShift);
                            if (isElementLoaded(deleteBtnInDeleteWindows, 10)) {
                                click(deleteBtnInDeleteWindows);
                                SimpleUtils.pass("Schedule Week View: OOOH shift been deleted successfully");
                            } else
                                SimpleUtils.fail("delete confirm button load failed", false);
                        } else
                            SimpleUtils.fail("delete item for this OOOH shift load failed", false);
                    }
                } catch (Exception e) {
                    continue;
                }
            }
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
            scheduleMainPage.clickOnFilterBtn();
        }else
            SimpleUtils.report("Schedule Week View: there is no shifts or Action Required smart card in this week");
    }


    @FindBy(css=".noUi-marker-large")
    private List<WebElement> shiftTimeLarges;
    @Override
    public void editShiftTimeToTheLargest() throws Exception {
        if (isElementLoaded(shiftStartTimeButton, 10) && isElementLoaded(shiftEndTimeButton, 10)
                && areListElementVisible(shiftTimeLarges, 10) && shiftTimeLarges.size() == 2) {
            mouseHoverDragandDrop(shiftStartTimeButton, shiftTimeLarges.get(0));
            mouseHoverDragandDrop(shiftEndTimeButton, shiftTimeLarges.get(1));
        } else {
            SimpleUtils.fail("Shift time elements failed to load!", false);
        }
    }

    public boolean validateVariousWorkRolePrompt() throws Exception{
        ScheduleCommonPage scheduleCommonPage = new ConsoleScheduleCommonPage();
        if(isElementEnabled(schWorkerInfoPrompt,5)){
            SimpleUtils.pass("Various Work Role Prompt is displayed ");
            if (areListElementVisible(shiftRoleList, 5) && shiftRoleList.size() >0) {
                if (shiftRoleList.size() < 10){
                    for (WebElement shiftRole : shiftRoleList) {
                        click(shiftRole);
                        SimpleUtils.pass("Role '"+ shiftRole.findElement(By.
                                cssSelector("span.sch-worker-change-role-name")).getText() +"' is selected!");
                    }
                } else {
                    for (int i =0; i< 9;i++) {
                        click(shiftRoleList.get(i));
                        SimpleUtils.pass("Role '"+ shiftRoleList.get(i).findElement(By.
                                cssSelector("span.sch-worker-change-role-name")).getText() +"' is selected!");
                    }
                }
            } else {
                SimpleUtils.fail("Work roles are doesn't show well ", true);
            }

            if (isElementEnabled(applyButtonChangeRole, 5) && isElementEnabled(cancelButtonChangeRole, 5)) {
                SimpleUtils.pass("Apply and Cancel buttons are enabled");
                if (scheduleCommonPage.isScheduleDayViewActive()) {
                    scrollToBottom();
                    clickTheElement(applyButtonChangeRole);
                    SimpleUtils.pass("Click Apply button successfully! ");
                } else {
                    clickTheElement(applyButtonChangeRole);
                    SimpleUtils.pass("Click Apply button successfully! ");
                }

                if (isElementEnabled(roleViolationAlter, 5)) {
                    clickTheElement(roleViolationAlterOkButton);
                    SimpleUtils.pass("Click OK button successfully! ");
                }
            } else {
                SimpleUtils.fail("Apply and Cancel buttons are doesn't show well ", false);
            }
            return true;
        } else
            return false;
    }

    @Override
    public List<String> getWorkRoleListFromChangeShiftRoleOption() throws Exception {
        List<String> workRoles = new ArrayList<>();
        if (areListElementVisible(shiftRoleList, 5) && shiftRoleList.size() >0) {
            for (WebElement shiftRole : shiftRoleList) {
                workRoles.add(shiftRole.findElement(By.
                        cssSelector("span.sch-worker-change-role-name")).getText());
            }
        } else {
            SimpleUtils.fail("Work roles are doesn't show well ", true);
        }
        return workRoles;
    }

    @FindBy(css="div.modal-content")
    private WebElement popupSelectTM;

    @FindBy (css="div.tab.ng-scope")
    private List<WebElement> subtabsSelectTeamMember;

    @FindBy(css = "div.tab.ng-scope:nth-child(1)")
    private WebElement tabSearchTM;
    @FindBy(css = "div.tab.ng-scope:nth-child(2)")
    private WebElement tabRecommendedTM;
    @FindBy(css = "[ng-click=\"cancelAction()\"]")
    private WebElement closeBtnInSeletedTM;
    @FindBy(css = "div.break-container")
    private WebElement profileInfoInSeletedTM;

    @Override
    public void verifyRecommendedAndSearchTMEnabled() throws Exception
    {
        if (isElementLoaded(profileInfoInSeletedTM,3) && isElementLoaded(tabSearchTM,5) && isElementLoaded(tabRecommendedTM,5) && isElementEnabled(closeBtnInSeletedTM, 5)) {
            SimpleUtils.pass("Select TMs window is opened");
            for (int i = 0; i <btnSearchteamMember.size() ; i++) {
                click(btnSearchteamMember.get(i));
                SimpleUtils.pass(btnSearchteamMember.get(i).getText() +" is enable");
            }
        }else
            SimpleUtils.fail("Select TMs window load failed",true);
        click(closeBtnInSeletedTM);
        waitForSeconds(3);
    }


    @FindBy(css = "div.tm-address-container")
    private WebElement personalDetailsContainer;

    @FindBy(css = "div.tm-phone")
    private WebElement personalDetailsPhone;

    @FindBy(css = "div.tm-email")
    private WebElement personalDetailsEmailAddress;

    @Override
    public void verifyPersonalDetailsDisplayed() throws Exception {
        if(isElementEnabled(personalDetailsContainer,5))
        {
            SimpleUtils.pass("Personal Details Container is Loaded in popup!");
        }
        else
        {
            SimpleUtils.fail("Personal Details Container is not Loaded in Popup!",false);
        }
        //
        if(isElementLoaded(personalDetailsName,5))
        {
            SimpleUtils.pass("Personal Details Name is Loaded in popup!");
        }
        else
        {
            SimpleUtils.fail("Personal Details Name is not Loaded in Popup!",false);
        }

        if(isElementLoaded(personalDetailsPhone,8) || isElementLoaded(personalDetailsEmailAddress,5))
        {
            SimpleUtils.pass("Phone/Email details are Loaded in popup!");
        }
        else
        {
            SimpleUtils.report("Phone/Email details are not Loaded in Popup!");
        }
    }

    //WorkDetails

    @FindBy(css = "div[class=\"staffing-details-container\"]")
    private WebElement workPreferenceContainer;
    @FindBy(css = "div[class=\"tm-prefs-container\"]")
    private WebElement workPrefValues;
    @FindBy(css = "div[class=\"tm-additional-locations-container tm-details\"]")
    private WebElement workPrefAdditionalDetails;


    @Override
    public void verifyWorkPreferenceDisplayed() throws Exception {

        if(isElementLoaded(workPreferenceContainer,5)){
            SimpleUtils.pass("Work Preference Details are displayed");
        }else{
            SimpleUtils.fail("Work Preference Details are not displayed", true);
        }
        if(isElementLoaded(workPrefValues,5)){
            SimpleUtils.pass("Work Preference Values are displayed");
        }else {
            SimpleUtils.fail("Work Preference Values are not displayed", true);
        }
        if(isElementLoaded(workPrefAdditionalDetails,5)){
            SimpleUtils.pass("Work Additional Detail are displayed");
        }else{
            SimpleUtils.fail("Work Additional Detail are not displayed", true);
        }

    }

    @FindBy(css = "div.availability-container")
    private WebElement availabilityText;

    @FindBy(css = "availability.ng-isolate-scope")
    private WebElement availabilityWeeklyView;

    @FindBy(css = "[ng-click=\"getLastWeekData()\"]")
    private WebElement getLastWeekArrow;

    @FindBy(css = "[ng-click=\"getNextWeekData()\"]")
    private WebElement getNextWeekArrow;



    @Override
    public void closeViewProfileContainer() throws Exception{
        if(isElementEnabled(closeViewProfileContainer,5)){
            click(closeViewProfileContainer);
            SimpleUtils.pass("Close button is available and clicked");
        }
        else
        { SimpleUtils.fail("Close Button is not enabled ", true); }

    }


    @Override
    public void verifyAvailabilityDisplayed() throws Exception {
        if(isElementLoaded(availabilityText,5)){
            SimpleUtils.pass("AvailabilityText is displayed");
        }else {
            SimpleUtils.fail("AvailabilityText is not displayed", true);
        }
        if(isElementLoaded(availabilityWeeklyView,5)){
            SimpleUtils.pass("Availability Weekly View is displayed");
        }else{
            SimpleUtils.fail("Availability Weekly View ise not displayed", true);
        }
        if(isElementEnabled(getLastWeekArrow, 5) && isElementEnabled(getNextWeekArrow, 5)) {
            click(getLastWeekArrow);
            click(getNextWeekArrow);
            SimpleUtils.pass("Go to last week and next week arrow buttons are clickable ");
        }else{
            SimpleUtils.fail("Go to last week and next week arrow buttons are not clickable ", true);
        }

    }


    @Override
    public void verifyChangeRoleFunctionality() throws Exception {

        if (validateVariousWorkRolePrompt()) {
            SimpleUtils.pass("various work role any one of them can be selected");
        }else
            SimpleUtils.fail("various work load failed",true);
    }


    @Override
    public void deleteTMShiftInWeekView(String teamMemberName) throws Exception {
        if (areListElementVisible(shiftsWeekView, 15)) {
            for (WebElement shiftWeekView : shiftsWeekView) {
                try {
                    WebElement workerName = shiftWeekView.findElement(By.className("week-schedule-worker-name"));
                    if (workerName != null) {
                        if (workerName.getText().toLowerCase().trim().contains(teamMemberName.toLowerCase().trim())) {
                            WebElement image = shiftWeekView.findElement(By.cssSelector(".rows .week-view-shift-image-optimized span"));
                            //WebElement image = shiftWeekView.findElement(By.cssSelector(".sch-day-view-shift-worker-detail"));
                            scrollToElement(image);
                            click(image);
                            waitForSeconds(2);
                            if (isElementLoaded(deleteShift, 10)) {
                                scrollToElement(deleteShift);
                                clickTheElement(deleteShift);
                                waitForSeconds(3);
                                if (isElementLoaded(deleteBtnInDeleteWindows, 10)) {
                                    scrollToElement(deleteBtnInDeleteWindows);
                                    clickTheElement(deleteBtnInDeleteWindows);
                                    SimpleUtils.pass("Schedule Week View: Existing shift: " + teamMemberName + " delete successfully");
                                    waitForSeconds(2);
                                } else
                                    SimpleUtils.fail("delete confirm button load failed", false);
                            } else
                                SimpleUtils.fail("delete item for this TM load failed", false);
                        }
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        }else
            SimpleUtils.report("Schedule Week View: shifts load failed or there is no shift in this week");
    }

    @Override
    public synchronized int countShiftsByUserName(String teamMemberName) throws Exception {
        int numberOfShifts = 0;
        if (areListElementVisible(shiftsWeekView, 15)) {
            for (WebElement shiftWeekView : shiftsWeekView) {
                try {
                    WebElement workerName = shiftWeekView.findElement(By.cssSelector("[class=\"rows\"] .week-schedule-worker-name"));
                    if (workerName != null) {
                        if (workerName.getText().toLowerCase().trim().contains(teamMemberName.toLowerCase().trim())) {
                            WebElement image = shiftWeekView.findElement(By.cssSelector(".rows .week-view-shift-image-optimized span"));
                            scrollToElement(image);
                            numberOfShifts = numberOfShifts + 1;
                        }
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        }
        return numberOfShifts;
    }

    @FindBy(css = "tr.table-row.ng-scope:nth-child(1)")
    private WebElement firstTableRow;

    @FindBy(css = "tr.table-row.ng-scope:nth-child(1) > td > div:nth-child(2) > div:nth-child(1) > span:nth-child(1)")
    private WebElement firstnameOfTM;
    @FindBy(css = "tr.table-row.ng-scope:nth-child(1) > td > div:nth-child(2) > div:nth-child(1) > span:nth-child(2)")
    private WebElement lastInitialOfTM;

    private String strNameOfTM ;//= firstnameOfTM+" "+lastInitialOfTM;

    @FindBy(css = "tr.table-row.ng-scope:nth-child(1) > td:nth-child(4)>div")
    private WebElement rdBtnFirstInList;

    @FindBy(css = "span.sch-worker-h-view-last-initial")
    private WebElement nameLastIntial;

    @FindBy(css = "span.sch-worker-h-view-display-name")
    private WebElement nameDisplayName;

    @FindBy(css = "input.form-control.tma-search-field-input-text")
    private   WebElement textInputBoxOnAssignTM;

    @FindBy(xpath ="//button[contains(@ng-class,'assignActionClass()')]")
    private WebElement btnAssign;
    @FindBy(xpath ="//i[contains(@ng-click,'searchAction()')]")
    private WebElement btnSearch;

    @FindBy(css = "div.lgn-modal-small-title")
    private WebElement titleOfConvertToOpenShiftPopup;

    @FindBy(css = "div.lgn-modal-small-description")
    private WebElement descriptionOfConvertToOpenShiftPopup;

    @FindBy(css = "button.sch-action.sch-cancel")
    private WebElement btnCancelOpenSchedule;

    @FindBy(css ="div.tma-open-shift-manual.ml-10")
    private WebElement textOfManualOpenShift;

    public boolean verifyConvertToOpenPopUpDisplay(String firstNameOfTM) throws Exception {

        String textOnConvertToOpenPopUp = "Are you sure you want to make this an Open Shift?\n" +
                firstNameOfTM + " will be losing this shift. Legion will automatically offer the shift to matching team members.\n" +
                "I want to offer to specific team members";
        if (isElementLoaded(titleOfConvertToOpenShiftPopup,10) && isElementLoaded(radioBtnManualOpenShift,10)
                && isElementLoaded(btnCancelOpenSchedule,10) && isElementLoaded(btnYesOpenSchedule,10)
                && textOnConvertToOpenPopUp.contains(titleOfConvertToOpenShiftPopup.getText().trim())
                && textOnConvertToOpenPopUp.contains(descriptionOfConvertToOpenShiftPopup.getText().trim())
                && textOnConvertToOpenPopUp.contains(textOfManualOpenShift.getText().trim())) {
            SimpleUtils.pass("checkbox is available to offer the shift to any specific TM[optional] Cancel /yes");
            return true;
        }else {
            SimpleUtils.fail("Convert To Open PopUp windows load failed", false);
        }
        return false;
    }

    public void convertToOpenShiftDirectly(){
        clickTheElement(btnYesOpenSchedule);
        waitForSeconds(3);
        SimpleUtils.pass("can convert to open shift by yes button directly");

    }

    @Override
    public void searchTMOnAssignPage(String NameOfTM) throws Exception {
        ShiftOperatePage shiftOperatePage = new ConsoleShiftOperatePage();
        if (shiftOperatePage.isAssignTeamMemberShowWell()) {
            textSearch.clear();
            textSearch.sendKeys(NameOfTM);
            clickTheElement(searchIcon);
            if(isElementLoaded(firstTableRow) && firstnameOfTM.getText().trim().toLowerCase().contains(NameOfTM.toLowerCase())){
                SimpleUtils.pass("The searched TM is displayed correctly!");
            }else{
                SimpleUtils.fail("The searched TM is not displayed!", false);
            }
        }else {
            SimpleUtils.fail("The Assign TM dialog is not loaded correctly!", false);
        }
    }

    @Override
    public void clickOnAssignButton() throws Exception {
        waitForSeconds(2);
        if (isElementLoaded(btnAssign, 5) && btnAssign.getText().equalsIgnoreCase("ASSIGN")) {
            click(btnAssign);
            SimpleUtils.report("Assign Team Member: Click on 'ASSIGN' button Successfully!");
        }else{
            SimpleUtils.fail("Assign Team Member: 'ASSIGN' button fail to load!", false);
        }
    }


    @FindBy(css = ".worker-edit-availability-status")
    private WebElement messageForSelectTM;

    @FindBy(xpath = "//div[contains(@class,'MuiGrid-grid-xs-3')]/div[1]/p")
    private List<WebElement> tmScheduledStatusOnNewCreateShiftPage;
    @Override
    public void verifyMessageIsExpected(String messageExpected) throws Exception {
        if (isElementLoaded(messageForSelectTM,5)){
            if (messageForSelectTM.getText()!=null && !messageForSelectTM.getText().equals("") && messageForSelectTM.getText().toLowerCase().contains(messageExpected)){
                SimpleUtils.pass("There is a message you want to see: "+messageExpected);
            } else {
                SimpleUtils.fail("No message you expected! Actual message is "+ messageForSelectTM.getText(), false );
            }
        } else if (areListElementVisible(tmScheduledStatusOnNewCreateShiftPage, 5)){
            if (tmScheduledStatusOnNewCreateShiftPage.get(0).getText()!=null
                    && tmScheduledStatusOnNewCreateShiftPage.get(0).getText().toLowerCase().contains(messageExpected)){
                SimpleUtils.pass("There is a message you want to see: "+messageExpected);
            } else {
                SimpleUtils.fail("No message you expected! Actual message is "+ tmScheduledStatusOnNewCreateShiftPage.get(0).getText(), false );
            }
        } else {
            SimpleUtils.fail("message for select TM is not loaded!", false);
        }
    }

    @Override
    public String getAllTheWarningMessageOfTMWhenAssign() throws Exception {
        String messageOfTMScheduledStatus = "";
        if (isElementLoaded(messageForSelectTM,5)){
            messageOfTMScheduledStatus = messageForSelectTM.getText();
        }
        return messageOfTMScheduledStatus;
    }


    @FindBy(css = ".modal-dialog.modal-lgn-md")
    private WebElement dialogWarningModel;
    @FindBy(css = ".tma-dismiss-button")
    private WebElement closeSelectTMWindowBtn;
    @FindBy(css = ".lgn-action-button-success")
    private WebElement okButton;
    @FindBy(css = "div.lgn-alert-message")
    private WebElement alertMessage;

    @FindBy(css = ".MuiDialogContent-root p")
    private List<WebElement> warningMessagesInWarningModeOnNewCreaeShiftPage;
    @Override
    public void verifyWarningModelForAssignTMOnTimeOff(String nickName) throws Exception {
        String expectedMessageOnWarningModel1 = nickName.toLowerCase()+" is approved for time off";
        String expectedMessageOnWarningModel2 = "please cancel the approved time off before assigning";
        NewShiftPage newShiftPage = new ConsoleNewShiftPage();
        waitForSeconds(1);
        if (isElementLoaded(alertMessage,15)) {
            String s = alertMessage.getText();
            if (s.toLowerCase().contains(expectedMessageOnWarningModel1) && s.toLowerCase().contains(expectedMessageOnWarningModel2)
                    && isElementLoaded(okButton,5) && okButton.getText().equalsIgnoreCase("OK")){
                waitForSeconds(1);
                clickTheElement(okButton);
                SimpleUtils.pass("There is a warning model with one button labeled OK! and the message is expected!");
                if (isElementLoaded(closeSelectTMWindowBtn,5)){
                    click(closeSelectTMWindowBtn);
                }
            } else
                SimpleUtils.fail("The message on warning model is incorrectly, the expected is:"
                        +expectedMessageOnWarningModel1+ expectedMessageOnWarningModel2
                        + " The actual is:"+s.toLowerCase(), false);
        } else if (areListElementVisible(warningMessagesInWarningModeOnNewCreaeShiftPage, 5)) {
            String warningMessage = newShiftPage.getWarningMessageFromWarningModal().toLowerCase();
            SimpleUtils.assertOnFail("The message on warning model is incorrectly, the expected is:"
                    +expectedMessageOnWarningModel1+ expectedMessageOnWarningModel2
                    + " The actual is:"+warningMessage.toLowerCase(), warningMessage.contains(expectedMessageOnWarningModel1)
                    && warningMessage.contains(expectedMessageOnWarningModel2), false);
            waitForSeconds(1);
            newShiftPage.clickOnOkButtonOnWarningModal();
            SimpleUtils.pass("There is a warning model with one button labeled OK! and the message is expected!");
            if (isElementLoaded(closeSelectTMWindowBtn,5)){
                click(closeSelectTMWindowBtn);
            }
        }else {
            SimpleUtils.fail("There is no warning model and warning message!", false);
        }
    }

    @Override
    public void clickOnCloseBtnOfAssignDialog() throws Exception{
        if(isElementLoaded(closeSelectTMWindowBtn, 5)) {
            clickTheElement(closeSelectTMWindowBtn);
            SimpleUtils.pass("Clicked the close button successfully! ");
        }
        else
            SimpleUtils.fail("The close button on assign dialog is not loaded! ", false);
    }

    @Override
    public boolean isCloseBtnOfAssignDialogLoaded() throws Exception{
        if(isElementLoaded(closeSelectTMWindowBtn)) {
            SimpleUtils.report("the close button loaded on assign TM dialog! ");
            return true;
        }
        else{
            SimpleUtils.report("the close button is not loaded on assign TM dialog! ");
            return false;
        }
    }

    @FindBy (className = "worker-edit-availability-status")
    private WebElement messageInSelectTeamMemberWindow;

    @FindBy (css = "[ng-repeat=\"worker in searchResults\"] .tma-staffing-option-outer-circle")
    private WebElement optionCircle;

    @FindBy (css = "[ng-click=\"cancelAction()\"]")
    private WebElement closeButtonOnCustomize;

    @FindBy(css = "[label=\"Cancel\"]")
    private WebElement scheduleEditModeCancelButton;
    @Override
    public void verifyInactiveMessageNWarning(String username, String date) throws Exception {
        if (messageInSelectTeamMemberWindow.getText().contains("TM is inactive from " + date)) {
            SimpleUtils.pass("Assign Team Member: 'Inactive' message shows successfully");
        } else
            SimpleUtils.fail("Assign Team Member: 'Inactive' message failed to show",false);
        if (isElementLoaded(optionCircle, 5)) {
            click(optionCircle);
            if (isElementLoaded(alertMessage,5)) {
                if (alertMessage.getText().trim().equals(username + " is inactive starting " + date + ". Please activate the team member before assigning.")) {
                    SimpleUtils.pass("Assign Team Member: Warning shows correctly");
                    click(okBtnOnConfirm);
                    if (optionCircle.findElement(By.className("tma-staffing-option-inner-circle")).getAttribute("class").contains("ng-hide")) {
                        SimpleUtils.pass("Assign Team Member: Click OK in warning window and nothing changes as expected");
                        if (isElementLoaded(closeButtonOnCustomize, 5)) {
                            click(closeButtonOnCustomize);
                            if (isElementLoaded(scheduleEditModeCancelButton, 10)) {
                                click(scheduleEditModeCancelButton);
                            }
                        }
                    } else {
                        SimpleUtils.fail("Assign Team Member: Click OK in warning window, the inactive TM is selected unexpectedly", false);
                    }
                } else {
                    SimpleUtils.fail("Assign Team Member: Warning shows incorrectly", false);
                }
            } else {
                SimpleUtils.fail("Assign Team Member: No warning when assign an inactive TM", false);
            }
        }
    }

    @FindBy(css = "[ng-show=\"hasSearchResults()\"] [ng-repeat=\"worker in searchResults\"]")
    private List<WebElement> searchResults;
    @Override
    public void verifyScheduledWarningWhenAssigning(String userName, String shiftTime) throws Exception {
        String scheduled = "Scheduled";
        boolean isWarningShown = false;
        if (areListElementVisible(searchAndRecommendedTMTabs, 5)) {
            if (searchAndRecommendedTMTabs.size() == 2 && isElementLoaded(textSearchOnNewCreateShiftPage, 5)) {
                textSearchOnNewCreateShiftPage.clear();
                textSearchOnNewCreateShiftPage.sendKeys(userName);
                if (areListElementVisible(searchResultsOnNewCreateShiftPage, 15)) {
                    List<WebElement> allStatus =  getTMScheduledStatusElementsOnNewCreateShiftPage();
                    String statusMessage = "";
                    for (WebElement status: allStatus) {
                        statusMessage = statusMessage + status.getText() + "\n";
                    }
                    for (WebElement searchResult : searchResultsOnNewCreateShiftPage) {
                        List<WebElement> tmInfo = searchResult.findElements(By.cssSelector("p.MuiTypography-body1"));
                        String workerName = tmInfo.get(0).getText();
                        if (workerName != null && workerName.toLowerCase().trim().contains(userName.trim().toLowerCase())) {
                            if (statusMessage.contains(scheduled)
                                    && statusMessage.replaceAll(" ", "").toLowerCase().contains(shiftTime.toLowerCase())) {
                                SimpleUtils.pass("Assign TM Warning: " + statusMessage + " shows correctly!");
                                isWarningShown = true;
                                break;
                            }
                        }
                    }
                }
            }else {
                SimpleUtils.fail("Search team member should have two tabs, failed to load!", false);
            }
        } else if (isElementLoaded(textSearch, 15) && isElementLoaded(searchIcon, 15)) {
            textSearch.sendKeys(userName);
            clickTheElement(searchIcon);
            if (areListElementVisible(searchResults, 15)) {
                for (WebElement searchResult : searchResults) {
                    WebElement workerName = searchResult.findElement(By.className("worker-edit-search-worker-display-name"));
                    WebElement status = searchResult.findElement(By.className("worker-edit-availability-status"));
                    if (workerName != null && optionCircle != null && workerName.getText().toLowerCase().trim().contains(userName.trim().toLowerCase())) {
                        if (status.getText().contains(scheduled) && status.getText().replaceAll(" ", "")
                                .toLowerCase().contains(shiftTime.replaceAll(" ", "").toLowerCase())) {
                            SimpleUtils.pass("Assign TM Warning: " + status.getText() + " shows correctly!");
                            isWarningShown = true;
                            break;
                        }
                    }
                }
            }
        }
        if (!isWarningShown) {
            SimpleUtils.fail("Assign TM Warning: Expected warning \"" + scheduled + " " + shiftTime + "\" not show!", false);
        }
    }


    @FindBy(css = "div.tab.ng-scope")
    private List<WebElement> selectTeamMembersOption;
    public void switchSearchTMAndRecommendedTMsTab() {
        if (areListElementVisible(searchAndRecommendedTMTabs, 10)) {
            if (searchAndRecommendedTMTabs.get(0).getAttribute("class").contains("selected")) {
                click(searchAndRecommendedTMTabs.get(1));
                if (searchAndRecommendedTMTabs.get(1).getAttribute("class").contains("select")) {
                    SimpleUtils.pass("Recommended TMs tab been selected");
                } else
                    SimpleUtils.fail("Recommended TMs tab fail been selected", false);
            } else {
                click(searchAndRecommendedTMTabs.get(0));
                if (searchAndRecommendedTMTabs.get(0).getAttribute("class").contains("select")) {
                    SimpleUtils.pass("Search Team Members tab been selected");
                } else
                    SimpleUtils.fail("Search Team Members tab fail been selected", false);
            }
        } else if (areListElementVisible(selectTeamMembersOption, 10)) {
            if (selectTeamMembersOption.get(0).getAttribute("class").contains("select")) {
                click(selectTeamMembersOption.get(1));
                if (selectTeamMembersOption.get(1).getAttribute("class").contains("select")) {
                    SimpleUtils.pass("Recommended TMs tab been selected");
                } else
                    SimpleUtils.fail("Recommended TMs tab fail been selected", false);
            } else {
                click(selectTeamMembersOption.get(0));
                if (selectTeamMembersOption.get(0).getAttribute("class").contains("select")) {
                    SimpleUtils.pass("Search Team Members tab been selected");
                } else
                    SimpleUtils.fail("Search Team Members tab fail been selected", false);
            }
        } else {
            SimpleUtils.fail("Select Team Member options are not available", false);
        }
    }


    public void verifyEditShiftTimePopUpDisplay() throws Exception {
        if (isElementEnabled(editShiftTimePopUp, 5)) {
            if (isElementEnabled(shiftInfoContainer, 5) && isElementEnabled(shiftStartAndEndTimeContainer, 5)
                    && isElementEnabled(cancelButtonInEditShiftTimeWindow, 5) && isElementEnabled(updateButtonInEditShiftTimeWindow, 5)) {
                SimpleUtils.pass("Edit Shift Time PopUp window load successfully");
            } else {
                SimpleUtils.fail("Items in Edit Shift Time PopUp window load failed", false);
            }
        } else {
            SimpleUtils.fail("Edit Shift Time PopUp window load failed", false);
        }
    }


    @FindBy(css = "div.tma-scroll-table tr")
    private List<WebElement> numberOfOffersMade;
    @Override
    public boolean checkIfOfferListHasOffers() throws Exception {
        boolean hasOffer = false;
        if (areListElementVisible(numberOfOffersMade,20)){
            hasOffer = true;
            SimpleUtils.pass("There is a offer list which is not null!");
        } else {
            SimpleUtils.report("The offer list is null!");
        }
        return hasOffer;
    }


    @FindBy(css = "[search-results=\"workerSearchResult\"] [ng-class=\"swapStatusClass(worker)\"]")
    private List<WebElement> tmScheduledStatus;
    @FindBy(css = ".MuiTabs-root+div>div>div:nth-child(2)>div>div:nth-child(1) .MuiGrid-item")
    private List<WebElement> searchTableColumns;
    @Override
    public String getTheMessageOfTMScheduledStatus() throws Exception {
        String messageOfTMScheduledStatus = "";
        List<WebElement> tmScheduledStatusOnNewCreateShiftPage = getTMScheduledStatusElementsOnNewCreateShiftPage();
        if (MyThreadLocal.getMessageOfTMScheduledStatus()==null || MyThreadLocal.getMessageOfTMScheduledStatus().equals("")) {
            if (areListElementVisible(tmScheduledStatus,5)){
                for (WebElement status : tmScheduledStatus) {
                    messageOfTMScheduledStatus = messageOfTMScheduledStatus + status.getText() + "\n";
                }
            } else if (areListElementVisible(tmScheduledStatusOnNewCreateShiftPage, 5)) {
                String statusMessage = "";
                for (WebElement status: tmScheduledStatusOnNewCreateShiftPage) {
                    statusMessage = statusMessage + status.getText() + "\n";
                }
                messageOfTMScheduledStatus = statusMessage.replace(" AM", "am").replace(" PM", "pm").replace(":00", "").replace(" 0", " ").replace(".0", "");
                MyThreadLocal.setMessageOfTMScheduledStatus(statusMessage);
            }else {
                SimpleUtils.report("TM scheduled status is not loaded!");
            }
        } else {
            messageOfTMScheduledStatus = MyThreadLocal.getMessageOfTMScheduledStatus().replace(" AM", "am").replace(" PM", "pm").replace(":00", "").replace(".0", "");
        }
        return messageOfTMScheduledStatus;
    }

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

    @FindBy(css = "[class = \"worker-edit-availability-status\"]")
    private List<WebElement> tmStatusOnAssignedShift;
    @Override
    public String getTheMessageOfAssignedShiftToTM() throws Exception {
        String messageOfTMScheduledStatus = "";
        if (areListElementVisible(tmStatusOnAssignedShift,5)){
            for (WebElement message : tmStatusOnAssignedShift) {
                messageOfTMScheduledStatus = messageOfTMScheduledStatus + message.getText() + "\n";
            }
        }else {
            SimpleUtils.fail("The message of TM status is not loaded!", false);
        }
        return messageOfTMScheduledStatus;
    }

    @Override
    public void verifyWarningModelMessageAssignTMInAnotherLocWhenScheduleNotPublished() throws Exception {
        String expectedMessageOnWarningModel = "cannot be assigned because the schedule has not been published yet at the home location";
        if (isElementLoaded(alertMessage,15)) {
            String s = alertMessage.getText();
            if (s.toLowerCase().contains(expectedMessageOnWarningModel)
                    && isElementLoaded(okButton,5)){
                click(okButton);
                SimpleUtils.pass("There is a warning model with one button labeled OK! and the message is expected!");
                if (isElementLoaded(closeSelectTMWindowBtn,5)){
                    click(closeSelectTMWindowBtn);
                }
            }
        } else {
            SimpleUtils.fail("There is no warning model and warning message!", false);
        }
    }

    @FindBy(css=".tma-table")
    private WebElement TMResultsTable;
    @Override
    public void verifyTMNotSelected() throws Exception {
        if (isElementLoaded(TMResultsTable,10)){
            if (TMResultsTable.findElements(By.cssSelector(".tma-staffing-option-inner-circle")).size()>0
                    && TMResultsTable.findElements(By.cssSelector(".tma-staffing-option-inner-circle")).get(0).getAttribute("class").contains("ng-hide")){
                SimpleUtils.pass("TM is not selected!");
            } else {
                SimpleUtils.fail("TM is selected!",false);
            }
        }
    }
    @FindBy(css = ".MuiTabs-scroller button.MuiButtonBase-root")
    private List<WebElement> searchAndRecommendedTMTabs;

    @FindBy(css = "[placeholder=\"Search by Team Member, Role, Location or any combination.\"]")
    private WebElement textSearchOnNewCreateShiftPage;

    @FindBy(css = "div.MuiBox-root div.MuiBox-root div.MuiBox-root div div div div div div.MuiGrid-root.MuiGrid-container")
    private List<WebElement> searchResultsOnNewCreateShiftPage;

    @FindBy(css = ".MuiDialogContent-root button")
    private List<WebElement> buttonsOnWarningMode;
    @Override
    public void clickOnRadioButtonOfSearchedTeamMemberByName(String name) throws Exception {
        if (areListElementVisible(searchResults, 15)) {
            for (WebElement searchResult : searchResults) {
                WebElement workerName = searchResult.findElement(By.className("worker-edit-search-worker-name"));
                WebElement optionCircle = searchResult.findElement(By.className("tma-staffing-option-outer-circle"));
                if (workerName != null && optionCircle != null) {
                    if (workerName.getText().toLowerCase().trim().replaceAll("\n"," ").contains(name.trim().toLowerCase())) {
                        clickTheElement(optionCircle);
                        SimpleUtils.report("Select Team Member: " + name + " Successfully!");
                    }
                }else {
                    SimpleUtils.fail("Worker name or option circle not loaded Successfully!", false);
                }
            }
        }else if (areListElementVisible(searchResultsOnNewCreateShiftPage, 30)) {
            for (WebElement searchResult : searchResultsOnNewCreateShiftPage) {
                List<WebElement> tmInfo = searchResult.findElements(By.cssSelector("p.MuiTypography-body1"));
                String tmName = tmInfo.get(0).getText();
                List<WebElement> assignAndOfferButtons = searchResult.findElements(By.tagName("button"));
                WebElement assignButton = assignAndOfferButtons.get(0);
                WebElement offerButton = assignAndOfferButtons.get(1);
                if (tmName != null && assignButton != null && offerButton != null) {
                    if (tmName.toLowerCase().trim().replaceAll("\n"," ").contains(name.split(" ")[0].trim().toLowerCase())) {
                        if (MyThreadLocal.getAssignTMStatus()) {
                            clickTheElement(assignButton);
                        } else
                            clickTheElement(offerButton);
                        SimpleUtils.report("Select Team Member: " + name + " Successfully!");
                        waitForSeconds(2);
                        break;
                    }
                }else {
                    SimpleUtils.fail("Worker name or buttons not loaded Successfully!", false);
                }
            }
        } else {
            SimpleUtils.fail("Failed to find the team member!", false);
        }


    }


    @FindBy(css = "button.lgn-action-button-success")
    private WebElement btnAssignAnyway;
    @Override
    public void clickOnAssignAnywayButton() throws Exception {
        waitForSeconds(2);
        if (isElementLoaded(btnAssignAnyway, 5) && btnAssignAnyway.getText().equalsIgnoreCase("ASSIGN ANYWAY")) {
            click(btnAssignAnyway);
            SimpleUtils.report("Assign Team Member: Click on 'ASSIGN ANYWAY' button Successfully!");
        } else if (areListElementVisible(buttonsOnWarningMode, 10)) {
            click(buttonsOnWarningMode.get(1));
        }else{
            SimpleUtils.fail("Assign Team Member: 'ASSIGN ANYWAY' button fail to load!", false);
        }
    }


    @Override
    public void verifyAlertMessageIsExpected(String messageExpected) throws Exception {
        if (isElementLoaded(alertMessage,5)){
            if (alertMessage.getText() != null && !alertMessage.getText().equals("") && alertMessage.getText().contains(messageExpected)){
                SimpleUtils.pass("There is the message you want to see: " + messageExpected);
            } else {
                SimpleUtils.fail("No message you expected! Actual message is " + alertMessage.getText(), false );
            }
        } else {
            SimpleUtils.fail("The alert message for selecting TM failed to loaded", false);
        }
    }

    @Override
    public void verifyMultipleAlertMessageIsExpected(String messageExpected1, String messageExpected2, String messageExpected3) throws Exception {
        if (isElementLoaded(alertMessage,5)){
            if (alertMessage.getText() != null && !alertMessage.getText().equals("") && alertMessage.getText().contains(messageExpected1) && alertMessage.getText().contains(messageExpected2) && alertMessage.getText().contains(messageExpected3)){
                SimpleUtils.pass("The message is expected!");
            } else {
                SimpleUtils.fail("No message you expected! Actual message is " + alertMessage.getText(), false );
            }
        } else {
            SimpleUtils.fail("The alert message for selecting TM failed to loaded", false);
        }
    }

//    @FindBy(xpath = "(//*[@search-results=\"workerSearchResult\"]//table)[2]//tr")
    @FindBy(css = "div.sc-karCPZ")

    private List<WebElement> searchResultsNew;
    @Override
    public boolean verifyWFSFunction() {
        if (searchResultsNew.size()!=0) {
            SimpleUtils.pass("Can search team members in Workforce sharing group");
            return true;
        }else
            SimpleUtils.fail("Workforce Sharing function work wrong",false);
        return false;
    }

    @Override
    public void deleteAllShiftsInWeekView() throws Exception {

        if (areListElementVisible(shiftsWeekView, 15)) {
            for (WebElement shiftWeekView : shiftsWeekView) {
                WebElement image = shiftWeekView.findElement(By.cssSelector(".rows .week-view-shift-image-optimized span"));
                clickTheElement(image);
                waitForSeconds(3);
                if (isElementLoaded(deleteShift, 15)) {
                    clickTheElement(deleteShift);
                    waitForSeconds(1);
                    if (isElementLoaded(deleteBtnInDeleteWindows, 10)) {
                        click(deleteBtnInDeleteWindows);
                        SimpleUtils.pass("Schedule Week View: OOOH shift been deleted successfully");
                    } else
                        SimpleUtils.fail("delete confirm button load failed", false);
                } else
                    SimpleUtils.fail("delete item for this OOOH shift load failed", false);
            }
        }else
            SimpleUtils.report("Schedule Week View: there is no shifts or Action Required smart card in this week");
    }

    @Override
    public void clickOnOfferTMOption() throws Exception{
        if(isElementLoaded(OfferTMS,5)) {
            clickTheElement(OfferTMS);
            waitForSeconds(3);
            SimpleUtils.pass("Clicked on Offer Team Members ");
        } else {
            SimpleUtils.fail("Offer Team Members is disabled or not available to Click ", false);
        }
    }


    @FindBy(css = "div[class*=\"MuiAvatar-root MuiAvatar-circular\"]")
    private List<WebElement> recommendedScrollTable;
    @Override
    public void verifyRecommendedTableHasTM() throws Exception{
        if (areListElementVisible(recommendedScrollTable, 15)){
            SimpleUtils.pass("There is a recommended list!");
        } else {
            SimpleUtils.fail("No recommended team members!", false);
        }
    }

    @Override
    public void verifyTMInTheOfferList(String firstName, String expectedStatus) throws Exception{
        boolean flag = false;
        if (areListElementVisible(numberOfOffersMade,20)){
            for (WebElement element: numberOfOffersMade){
                if (element.getText().toLowerCase().contains(firstName.toLowerCase()) && element.getText().toLowerCase().contains(expectedStatus.toLowerCase())){
                    flag = true;
                    break;
                }
            }
            if (flag){
                SimpleUtils.pass(firstName + " is in the offered list!");
            } else {
                SimpleUtils.fail(firstName + " is not in the offered list!", false);
            }
        } else {
            SimpleUtils.fail("The offer list is null!",false);
        }
    }


    @FindBy(css = ".modal-dialog .sch-day-view-shift-outer")
    private WebElement shiftInViewStatusWindow;
    @Override
    public String getViewStatusShiftsInfo() throws Exception {
        String result = "";
        if (isElementLoaded(shiftInViewStatusWindow, 5)) {
            result = shiftInViewStatusWindow.getAttribute("innerText");
        }
        return result;
    }

    @FindBy(css = "div.noUi-value-large")
    private List<WebElement> startAndEndTimeOnEditShiftPage;

    @Override
    public List<String> getStartAndEndOperatingHrsOnEditShiftPage() throws Exception {
        List<String> startAndEndOperatingHrs = new ArrayList<>();
        if (areListElementVisible(startAndEndTimeOnEditShiftPage, 15)) {
            for (WebElement operatingHour : startAndEndTimeOnEditShiftPage) {
                startAndEndOperatingHrs.add(operatingHour.getText());
            }
        } else
            SimpleUtils.fail("The operating hours on edit shift page fail to load! ", false);
        return startAndEndOperatingHrs;
    }


    @Override
    public HashMap<String, String> getMealAndRestBreaksTime() throws Exception {
        HashMap<String, String> mealAndRestBreaksTime = new HashMap<String, String>();
        if (isElementEnabled(editMealBreakTitle,5)) {
            for (WebElement mealBreakTime:mealBreakTimes){
                String mealTime = mealBreakTime.getText().trim();
                mealAndRestBreaksTime.put("Meal Break",mealTime);
            }
            for (WebElement restBreakTime:restBreakTimes){
                String restTime = restBreakTime.getText().trim();
                mealAndRestBreaksTime.put("Rest Break",restTime);
            }
        }else
            SimpleUtils.report("Breaks edit page don't display");
        return mealAndRestBreaksTime;
    }


    // Added by Nora
    @FindBy (css = ".sch-worker-action")
    private List<WebElement> shiftOptions;

    /***
     * Verify specific option is enabled on shift menu when clicking the avatar of the shift
     * @param optionName - The name of the option
     * @throws Exception
     */
    @Override
    public void verifySpecificOptionEnabledOnShiftMenu(String optionName) throws Exception {
        try {
            boolean isEnabled = false;
            if (areListElementVisible(shiftOptions, 15) && shiftOptions.size() > 0) {
                for (WebElement option : shiftOptions) {
                    if (option.getText().equalsIgnoreCase(optionName) && !option.getAttribute("class").contains("graded-out")) {
                        isEnabled = true;
                    }
                }
            }
            SimpleUtils.assertOnFail("Shift option: " + optionName + " isn't enabled!", isEnabled,false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Override
    public void verifyShiftInfoIsCorrectOnMealBreakPopUp(List<String> expectedShiftInfo) throws Exception {
        try {
            if (isElementLoaded(shiftInfoContainer, 10)) {
                String actualShiftInfo = shiftInfoContainer.getText();
                if (actualShiftInfo.replaceAll(" ", "").contains(expectedShiftInfo.get(0).replaceAll(" ", ""))
                        && actualShiftInfo.replaceAll(" ", "").contains(expectedShiftInfo.get(3).replaceAll(" ", "")) &&
                        actualShiftInfo.replaceAll(" ", "").contains(expectedShiftInfo.get(4).replaceAll(" ", ""))
                        && actualShiftInfo.replaceAll(" ", "").contains(expectedShiftInfo.get(2).replaceAll(" ", ""))) {
                    SimpleUtils.pass("Shift info on the Meal Break pop up is correct!");
                } else {
                    SimpleUtils.fail("Shift info on the Meal Break pop up is incorrect!", false);
                }
            } else {
                SimpleUtils.fail("Shift container failed to load on meal break pop up!", false);
            }
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Override
    public void verifyMealBreakAndRestBreakArePlacedCorrectly() throws Exception {
        try {
            if (areListElementVisible(restBreakTimes, 5)) {
                if (areListElementVisible(restBreakDurations, 5) && restBreakDurations.size() == restBreakTimes.size()) {
                    SimpleUtils.pass("Rest breaks are shown!");
                } else {
                    SimpleUtils.fail("Rest breaks show incorrectly!", false);
                }
            }
            if (areListElementVisible(mealBreakTimes, 5)) {
                if (areListElementVisible(mealBreakDurations, 5) && mealBreakDurations.size() == mealBreakTimes.size()) {
                    SimpleUtils.pass("Meal breaks are shown!");
                } else {
                    SimpleUtils.fail("Meal breaks show incorrectly!", false);
                }
            }
        } catch (Exception e) {
            // Do nothing
        }
    }

    @Override
    public void verifySpecificShiftHaveEditIcon(int index) throws Exception {
        try {
            boolean isFound = false;
            if (areListElementVisible(shiftsWeekView, 15) && shiftsWeekView.size() > index) {
                try {
                    if (isElementLoaded(shiftsWeekView.get(index).findElement(By.cssSelector("[src*=edited-shift-week]")))) {
                        isFound = true;
                        SimpleUtils.pass("The shift with index: " + index + " has edited - pencil icon!");
                    }
                } catch (Exception e) {
                    isFound = false;
                }
            }
            if (!isFound) {
                try {
                    if (areListElementVisible(dayViewShiftGroups, 5) && dayViewShiftGroups.size() > index) {
                        if (isElementLoaded(dayViewShiftGroups.get(index).findElement(By.cssSelector(".sch-day-view-right-gutter-img.edit")))) {
                            isFound = true;
                            SimpleUtils.pass("The shift with index: " + index + " has edited icon in day view!");
                        }
                    }
                } catch (Exception e) {
                    isFound = false;
                }
            }
            if (!isFound) {
                SimpleUtils.fail("The shift with index: " + index + " doesn't have edited/pencil icon!", false);
            }
        } catch (Exception e) {
            SimpleUtils.fail("The shift with index: " + index + " doesn't have edited/pencil icon!", false);
        }
    }


    @Override
    public void verifyBreakTimesAreUpdated(List<String> expectedBreakTimes) throws Exception {
        int count = 0;
        if (areListElementVisible(mealBreakTimes, 5) && areListElementVisible(restBreakTimes, 5)) {
            for (WebElement meal : mealBreakTimes) {
                if (expectedBreakTimes.contains(meal.getText())) {
                    count = count + 1;
                }
            }
            for (WebElement rest : restBreakTimes) {
                if (expectedBreakTimes.contains(rest.getText())) {
                    count = count + 1;
                }
            }
            if (count == expectedBreakTimes.size()) {
                SimpleUtils.pass("Meal and rest break times are updated successfully!");
            } else {
                SimpleUtils.fail("Meal and rest break times are not updated successfully!", false);
            }
        } else {
            SimpleUtils.fail("Meal and rest break times are not updated successfully!", false);
        }
    }


    @FindBy(css = "div.modal-dialog div.edit-shift-notes")
    private WebElement EditShiftNotesDialog;

    @Override
    public void verifyShiftNotesContent(String shiftNotes){
        if (isElementEnabled(EditShiftNotesDialog, 10)){
            //verify dialog title.
            if (EditShiftNotesDialog.findElement(By.cssSelector("div.modal-instance-header-title")).getText().equalsIgnoreCase("Edit Shift Notes")){
                SimpleUtils.pass("Edit shift notes dialog title is correct!");
            } else {
                SimpleUtils.fail("Edit shift notes dialog title is incorrect!", false);
            }
            //verify placeholder.
            if (EditShiftNotesDialog.findElement(By.cssSelector("textarea")).getAttribute("placeholder").equalsIgnoreCase("Add note (Optional)")){
                SimpleUtils.pass("Shift notes placeholder is expected!");
            } else {
                SimpleUtils.fail("Shift notes placeholder is incorrect!", false);
            }
            //verify shift notes content.
            if (EditShiftNotesDialog.findElement(By.cssSelector("textarea")).getAttribute("value").equalsIgnoreCase(shiftNotes)){
                SimpleUtils.pass("Shift notes is not expected!");
            } else {
                SimpleUtils.fail("Shift notes is not expected!", false);
            }
        } else {
            SimpleUtils.fail("Edit shift notes dialog is not loaded!", false);
        }
    }

    @Override
    public void addShiftNotesToTextarea(String notes){
        if (isElementEnabled(EditShiftNotesDialog.findElement(By.cssSelector("textarea")), 10)){
            EditShiftNotesDialog.findElement(By.cssSelector("textarea")).clear();
            EditShiftNotesDialog.findElement(By.cssSelector("textarea")).sendKeys(notes);
            clickOnSaveBtnOnEditShiftNotesDialog();
        } else {
            SimpleUtils.fail("Edit shift notes dialog is not loaded!", false);
        }
    }

    public void clickOnSaveBtnOnEditShiftNotesDialog(){
        if (isElementEnabled(EditShiftNotesDialog.findElement(By.cssSelector("div.confirm")), 10)){
            clickTheElement(EditShiftNotesDialog.findElement(By.cssSelector("div.confirm")));
            SimpleUtils.pass("Update button is clicked!");
        } else {
            SimpleUtils.fail("Edit shift notes dialog is not loaded!", false);
        }
    }

    @Override
    public String getShiftInfoInEditShiftDialog() throws Exception {
        if (isElementEnabled(EditShiftNotesDialog.findElement(By.cssSelector(".sch-day-view-shift-outer")), 10)){
            return EditShiftNotesDialog.findElement(By.cssSelector(".sch-day-view-shift-outer .left-shift-box")).getText()+EditShiftNotesDialog.findElement(By.cssSelector(".sch-day-view-shift-outer .right-shift-box")).getText();
        }
        return null;
    }

    @FindBy(css = "div[ng-class*='EditShiftNotes']")
    private WebElement EditShiftNotes;
    @Override
    public void clickOnEditShiftNotesOption() throws Exception {
        if(isElementLoaded(EditShiftNotes,5)) {
            clickTheElement(EditShiftNotes);
            SimpleUtils.pass("Clicked on EditShiftNotes option ");
        } else {
            SimpleUtils.fail("EditShiftNotes is disabled or not available to Click ", false);
        }
    }

    @FindBy(css = "[ng-if=\"hasBestWorkers()\"] [ng-repeat=\"worker in searchResults\"]")
    private List<WebElement> recommendedTMs;
    @FindBy(xpath = "//div[contains(@class,'MuiBox-root')]/div[2]/div/div[2]/div/div/div[2]/div")
    private List<WebElement> recommendedTMsOnNewCreateShiftPage;

    public List<WebElement> getAllRecommendedTMs () {
        List<WebElement> tmsInRecommendedTab = new ArrayList<>();
        if (areListElementVisible(recommendedTMs, 10)) {
            tmsInRecommendedTab = recommendedTMs;
        } else if (areListElementVisible(recommendedTMsOnNewCreateShiftPage, 5)) {
            tmsInRecommendedTab = recommendedTMsOnNewCreateShiftPage;
        }else
            SimpleUtils.report("There is no TMs in recommended tab! ");

        return tmsInRecommendedTab;
    }

    public boolean checkIfTMExistsInRecommendedTab (String fullNameOfTM) throws Exception {
        NewShiftPage newShiftPage = new ConsoleNewShiftPage();
        boolean isTMExist = false;
        boolean isNewCreateShiftPageDisplay = newShiftPage.checkIfNewCreateShiftPageDisplay();
        List<WebElement> allRecommendedTMs = getAllRecommendedTMs();
        for (WebElement tm: allRecommendedTMs) {
            if (isNewCreateShiftPageDisplay) {
                String tmFullName = tm.findElements(By.cssSelector("p.MuiTypography-body1")).get(0).getText();
                if (tmFullName.equalsIgnoreCase(fullNameOfTM)) {
                    isTMExist = true;
                    SimpleUtils.pass("TM: "+ fullNameOfTM+" exists in recommended tab! ");
                    break;
                }
            } else {
                String tmFullName = tm.findElement(By.cssSelector(".worker-edit-search-worker-display-name")).getText();
                if (tmFullName.equalsIgnoreCase(fullNameOfTM)) {
                    isTMExist = true;
                    SimpleUtils.pass("TM: "+ fullNameOfTM+" exists in recommended tab! ");
                    break;
                }
            }
        }
        return isTMExist;
    }

    @Override
    public void moveMealAndRestBreaksOnEditBreaksPage(String breakTime, int index, boolean isMealBreak) throws Exception {
        if (areListElementVisible(mealBreakDurations, 5) || areListElementVisible(restBreakDurations, 5)) {
            List<WebElement> breaks = null;
            List<WebElement> breakTimes = null;
            if (isMealBreak) {
                breaks = mealBreakDurations;
                breakTimes = mealBreakTimes;
            } else {
                breaks = restBreakDurations;
                breakTimes = restBreakTimes;
            }
            if (areListElementVisible(noUiValues, 10) && noUiValues.size() >0) {
                //Move break to the start of time line
                waitForSeconds(3);
                mouseHoverDragandDrop(breaks.get(index),shiftTimeLarges.get(0));
                int timeLineLength = noUiValues.size();
                String mealBreakTimeAfterEdit = "";
                boolean moveBreakTimeSuccess = false;
                for (int i = 0; i< timeLineLength; i++) {
                    moveDayViewCards(mealBreaks.get(index), 10);
                    mealBreakTimeAfterEdit = breakTimes.get(index).getText().split("-")[0].trim().replace(" ","");
                    if (mealBreakTimeAfterEdit.equalsIgnoreCase(breakTime)) {
                        SimpleUtils.pass("Move breaks successfully! ");
                        moveBreakTimeSuccess = true;
                        break;
                    }
                }
                if (!moveBreakTimeSuccess) {
                    SimpleUtils.fail("Move breaks fail, cannot found the break time: "+ breakTime, false);
                }

            } else
                SimpleUtils.fail("The meal break time line fail to load! ", false);
        }else
            SimpleUtils.fail("There is no breaks! ", false);
    }

    public String getOfferStatusFromOpenShiftStatusList (String tmName) {
        String status = "";
        if (areListElementVisible(numberOfOffersMade,20)){
            for (WebElement element: numberOfOffersMade){
                if (element.getText().toLowerCase().contains(tmName.toLowerCase())){
                    status = element.getText().toLowerCase();
                }
            }
        } else {
            SimpleUtils.report("The offer list is null!");
        }
        return status;
    }

    @FindBy(css = "div.worker-shift-container")
    private WebElement shiftCardOnEditShiftTimePage;

    public HashMap<String, String> getInfoFromCardOnEditShiftTimePage () throws Exception {
        HashMap<String, String> shiftInfo = new HashMap<>();
        if (isElementLoaded(shiftCardOnEditShiftTimePage, 5)) {
            WebElement workNameAndWorkRole = shiftCardOnEditShiftTimePage.findElement(By.cssSelector("div.sch-day-view-shift-worker-name"));
            shiftInfo.put("workName",
                    workNameAndWorkRole.getText().split("\\(")[0]);
            shiftInfo.put("workRole",
                    workNameAndWorkRole.getText().split("\\(")[1].replace("(","").replace(")",""));
            shiftInfo.put("shiftTime",
                    shiftCardOnEditShiftTimePage.findElement(By.cssSelector(".sch-day-view-shift-time span")).getText());
            List<WebElement> shiftHrs = shiftCardOnEditShiftTimePage.findElements(By.cssSelector(".sch-day-view-worker-time span"));
            shiftInfo.put("workCurrentShiftHrs", shiftHrs.get(0).getText());
            shiftInfo.put("workWeekShiftsHrs", shiftHrs.get(1).getText().replace("| ",""));
            shiftInfo.put("jobTitle", shiftCardOnEditShiftTimePage.findElement(By.cssSelector(".sch-day-view-shift-worker-title-role")).getText());
        } else
            SimpleUtils.fail("The shift card on edit shift time page fail to load! ", false);
        return shiftInfo;
    }

    public boolean isEditShiftTimeNewUIDisplay () throws Exception {
        boolean isNewUIDisplay = false;
        if (isElementLoaded(editShiftTimePopUp, 15)
                &&isElementLoaded(shiftStartInput, 10)
                && isElementLoaded(shiftEndInput, 10)
                && !isElementLoaded(shiftEndTimeButton, 5)
                && !isElementLoaded(shiftStartTimeButton, 5)) {
            isNewUIDisplay = true;
            SimpleUtils.report("The new edit shift time page display correctly! ");
        } else {
            SimpleUtils.report("The new edit shift time page is not display! ");
        }
        return isNewUIDisplay;
    }


    public void setShiftTimesOnEditShiftTimePage (String startTime, String endTime, boolean checkTheNextDay) throws Exception {
        Thread.sleep(5000);
        if (isElementLoaded(editShiftTimePopUp, 15)
                &&isElementLoaded(shiftStartInput, 10)
                && isElementLoaded(shiftEndInput, 10)) {
            SimpleUtils.report("The new edit shift time page display correctly! ");
            shiftStartInput.clear();
            shiftEndInput.clear();
            clickTheElement(shiftCardOnEditShiftTimePage);
            waitForSeconds(2);
            clickTheElement(shiftStartInput);
            shiftStartInput.sendKeys(startTime);
            waitForSeconds(2);
            click(shiftEndInput);
            shiftEndInput.sendKeys(endTime);
            if (checkTheNextDay) {
                checkOrUnCheckNextDayOnEditShiftTimePage(true);
            } else
                checkOrUnCheckNextDayOnEditShiftTimePage(false);
            SimpleUtils.pass("Set the shift times successfully! ");
        } else if (isElementLoaded(editShiftTimePopUp, 15)
                && isElementLoaded(shiftEndTimeButton, 5)
                && isElementLoaded(shiftStartTimeButton, 5)){
            SimpleUtils.report("The old edit shift time page display correctly! ");
            moveSliderAtCertainPointOnEditShiftTimePage(endTime, "End");
            moveSliderAtCertainPointOnEditShiftTimePage(startTime, "Start");
            SimpleUtils.pass("Set the shift times successfully! ");
        } else
            SimpleUtils.fail("Edit shift times popup, inputs or buttons fail to load! ", false);
    }


    @FindBy(css=".edit-shift-time-toggle-next-day img")
    private WebElement nextDayImg;
    @FindBy(css=".popover.fade")
    private WebElement nextDayPopup;
    public void checkOrUnCheckNextDayOnEditShiftTimePage (boolean isCheck) throws Exception {
        if (isElementLoaded(editShiftTimePopUp, 15)) {
            SimpleUtils.report("The new edit shift time page display correctly! ");
            if (isCheck) {
                if (isElementLoaded(nextDayImg, 10)) {
                    if (nextDayImg.getAttribute("src").contains("next-day")) {
                        if (!isElementLoaded(nextDayPopup, 3)) {
                            clickTheElement(nextDayImg);
                        }
                        clickTheElement(nextDayPopup.findElement(By.cssSelector(".input-form input")));
                        if (!nextDayImg.getAttribute("src").contains("next-day")) {
                            SimpleUtils.pass("The next day checkbox been checked successfully! ");
                        } else
                            SimpleUtils.fail("The next day checkbox been checked fail! ", false);
                    } else
                        SimpleUtils.pass("The next day checkbox already checked! ");
                } else
                    SimpleUtils.fail("The next day img fail to load! ", false);

            } else {
                if (isElementLoaded(nextDayImg, 10)) {
                    if (!nextDayImg.getAttribute("src").contains("next-day")) {
                        if (!isElementLoaded(nextDayPopup, 3)) {
                            clickTheElement(nextDayImg);
                        }
                        clickTheElement(nextDayPopup.findElement(By.cssSelector(".input-form input")));
                        if (nextDayImg.getAttribute("src").contains("next-day")) {
                            SimpleUtils.pass("The next day checkbox been unchecked successfully! ");
                        } else
                            SimpleUtils.fail("The next day checkbox been unchecked fail! ", false);
                    } else
                        SimpleUtils.pass("The next day checkbox already unchecked! ");
                } else
                    SimpleUtils.report("The next day img is not loaded! ");
            }
        } else
            SimpleUtils.fail("Edit shift times popup fail to load! ", false);
    }

    @FindBy(css=".edit-time-compliance")
    private WebElement editShiftTimeCompliance;
    public String getEditShiftTimeCompliance() throws Exception {
        String compliance = "";
        if (isElementLoaded(editShiftTimeCompliance, 10)) {
            compliance = editShiftTimeCompliance.getText();
        } else
            SimpleUtils.report("Edit shift time compliance fail to load! ");
        return compliance;
    }

    public boolean checkIfUpdateButtonEnabled () throws Exception {
        boolean isEnabled = false;
        if (isElementLoaded(updateButtonInEditShiftTimeWindow, 10)) {
            if (!updateButtonInEditShiftTimeWindow.getAttribute("class").contains("disabled")) {
                isEnabled = true;
            }
        } else
            SimpleUtils.fail("Update button on edit shift time page fail to load! ", false);
        return isEnabled;
    }

    @Override
    public int getTheDurationOfBreaks(boolean isMealBreak) throws Exception {
        List<WebElement> breakDuration = null;
        int duration = 0;
        if (areListElementVisible(mealBreakTimes, 5) || areListElementVisible(restBreakTimes, 5)) {
            if (isMealBreak) {
                breakDuration = mealBreakTimes;
            } else {
                breakDuration = restBreakTimes;
            }
            String[] times = breakDuration.get(0).getText().split("-");
            int startTime = Integer.valueOf(times[0].split(":")[0].trim()) * 60 +
                    Integer.valueOf(times[0].split(":")[1].trim().replaceAll("am", "").replaceAll("pm", ""));
            int endTime = Integer.valueOf(times[1].split(":")[0].trim()) * 60 +
                    Integer.valueOf(times[1].split(":")[1].trim().replaceAll("am", "").replaceAll("pm", ""));
            duration = endTime - startTime;
        } else {
            SimpleUtils.fail("Meal and Rest Breaks are failed to load!", false);
        }
        return duration;
    }

    @Override
    public void clickOnOKBtnOnMealBreakDialog() throws Exception {
        if (isElementLoaded(continueBtnInMealBreakButton, 5)) {
            clickTheElement(continueBtnInMealBreakButton);
        } else {
            SimpleUtils.fail("OK button failed to load on Meal Break Dialog!", false);
        }
    }

    @FindBy(css = "[ng-if=\"badgesToShow && badgesToShow.length\"] .one-badge")
    private List<WebElement> badgeIconList;

    @Override
    public void clickOnProfileIconByIndex(int indexOfProfIcon) throws Exception {
        if(isProfileIconsEnable()&& areListElementVisible(shifts, 10)) {
            clickTheElement(profileIcons.get(indexOfProfIcon));
        } else {
            SimpleUtils.fail("Failed for loading profile icon!", false);
        }
    }

    @Override
    public void checkBadgeOnProfilePopup(String tmA, String tmB) throws Exception {
        if (areListElementVisible(shifts, 10)) {
            SimpleUtils.assertOnFail("Shifts number is:" + shifts.size() + " didn't match 2!", shifts.size() == 2, false);
            clickOnProfileIconByIndex(0);
            waitForSeconds(3);
            SimpleUtils.assertOnFail("TM " + tmA + " should have badge!", areListElementVisible(badgeIconList), false);
            SimpleUtils.assertOnFail("TM " + tmA + " should have 1 badge! But actual number is: " + badgeIconList.size(), badgeIconList.size() == 1, false);
            clickOnProfileIconByIndex(1);
            waitForSeconds(3);
            SimpleUtils.assertOnFail("TM " + tmB + "should have no badge!", !areListElementVisible(badgeIconList), false);
        } else {
            SimpleUtils.fail("Shifts are not listed!", false);
        }
    }

    @Override
    public void convertAllShiftsToOpenInDayView(String action) throws Exception {
        ScheduleShiftTablePage scheduleShiftTablePage = new ConsoleScheduleShiftTablePage();
        EditShiftPage editShiftPage = new ConsoleEditShiftPage();
        if (areListElementVisible(dayViewAvailableShifts,10)){
            int count = dayViewAvailableShifts.size();
            for (int i = 0; i < count; i++) {
                scheduleShiftTablePage.rightClickOnSelectedShiftInDayView(i);
//                scheduleShiftTablePage.clickProfileIconOfShiftByIndex(i);
                scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
                editShiftPage.clickOnAssignmentSelect();
                editShiftPage.selectSpecificOptionByText(ConsoleEditShiftPage.assignmentOptions.OpenShift.getOption());
                editShiftPage.clickOnUpdateButton();
//                if(isConvertToOpenEnable()){
//                    clickOnConvertToOpenShift();
//                    convertToOpenShiftDirectly();
//                    SimpleUtils.pass("The shift is converted to the Open Shift successfully!");
//                    waitForSeconds(2);
//                }else{
//                    continue;
//                }
            }
        }else{
            SimpleUtils.fail("No available shifts in the Day View", false);
        }
    }

    @FindBy(css = ".table-field.this-week-field ")
    private List<WebElement> totalShiftHrsAndShiftCountThisWeek;
    @FindBy(xpath = "//div[contains(@class,'MuiGrid-root MuiGrid-container')]/div[4]/div")
    private List<WebElement> totalShiftHrsAndShiftCountThisWeekOnNewCreateShiftPage;
    @Override
    public HashMap<String, Float> getTotalShiftHrsAndShiftCountThisWeek() throws Exception {
        HashMap<String, Float> totalShiftHrsAndShiftCount= new HashMap<String, Float>();
        if (areListElementVisible(totalShiftHrsAndShiftCountThisWeekOnNewCreateShiftPage, 5)) {
            waitForSeconds(5);
            try {
                WebElement totalShiftHrs = totalShiftHrsAndShiftCountThisWeekOnNewCreateShiftPage.get(0)
                        .findElement(By.xpath("./div/div[2]"));
                WebElement shiftCount = totalShiftHrsAndShiftCountThisWeekOnNewCreateShiftPage.get(0)
                        .findElement(By.xpath("./div/div[1]"));
                if (totalShiftHrs.getText()!=null
                        && !totalShiftHrs.getText().equals("")
                        && shiftCount.getText()!=null
                        && !shiftCount.getText().equals("")){
                    totalShiftHrsAndShiftCount.put("shiftHrs", Float.parseFloat(totalShiftHrs.getText().split(" ")[0]));
                    totalShiftHrsAndShiftCount.put("shiftCount", Float.parseFloat(shiftCount.getText().split(" ")[0]));
                } else
                    SimpleUtils.fail("Fail to get the value of total shift hrs or shift count on search TM page! ", false);

            } catch (Exception e) {
                SimpleUtils.fail("Fail to found the total shift hrs or shift count on search TM page! ", false);
            }
        }else if (areListElementVisible(totalShiftHrsAndShiftCountThisWeek, 5)) {
            WebElement totalShiftHrs = totalShiftHrsAndShiftCountThisWeek.get(0)
                    .findElements(By.cssSelector("worker-edit-this-week-hour")).get(0);
            WebElement shiftCount = totalShiftHrsAndShiftCountThisWeek.get(0)
                    .findElements(By.cssSelector("worker-edit-this-week-hour")).get(1);
            totalShiftHrsAndShiftCount.put("shiftHrs", Float.parseFloat(totalShiftHrs.getText()));
            totalShiftHrsAndShiftCount.put("shiftCount", Float.parseFloat(shiftCount.getText()));
        } else {
            SimpleUtils.fail("The total shift hrs and shift count this week section fail to load! ", false);
        }
        return totalShiftHrsAndShiftCount;
    }

    @Override
    public boolean isMealBreakBlockDisplayed(int index) throws Exception {
        boolean mealBreakBlockDisplay = true;
        if (areListElementVisible(dayViewAvailableShifts, 10)) {
            try {
                WebElement mealBreakBlock = dayViewAvailableShifts.get(index).findElement(By.cssSelector("[ng-repeat=\"break in breaks\"]"));
                if (isElementLoaded(mealBreakBlock)) {
                    SimpleUtils.report("The Meal Break is displayed in the shift box!");
                }
            } catch (Exception e) {
                SimpleUtils.report("The Meal Break is not displayed in the shift box!");
                mealBreakBlockDisplay = false;
            }
        }
        return mealBreakBlockDisplay;
    }


    public void deleteTMShiftsInDayView(String tmName) throws Exception {
        if (areListElementVisible(dayViewAvailableShifts,10)){
            int count = dayViewAvailableShifts.size();
            for (int i = 0; i < count; i++) {
                WebElement workerName = null;
                try{
                    workerName = dayViewAvailableShifts.get(i).findElement(By.cssSelector(".sch-day-view-shift-worker-name"));
                } catch (Exception e) {
                SimpleUtils.fail("Fail to shift work name in day view! ", false);
                }
                if(workerName!= null && workerName.getText().contains(tmName)) {
                    List<WebElement> tempShifts = getDriver().findElements(By.cssSelector(".sch-day-view-shift-outer .right-shift-box"));
                    scrollToBottom();
                    moveToElementAndClick(tempShifts.get(i));
                    deleteShift();
                }
            }
        }
    }
    @FindBy(css = ".header-field.seniority-field.tl.ng-binding")
    private WebElement seniorityTitleShownForAssign;
    @FindBy(css = ".MuiGrid-root.MuiGrid-item>span")
    private List<WebElement> titlesOnCreationDialog;
    @Override
    public boolean isSeniorityColumnLoaded() throws Exception {
        boolean seniorityColumnLoaded = true;
        if (isElementLoaded(seniorityTitleShownForAssign,10)){
            SimpleUtils.report("Seniority Column is displayed!");
        }else if (areListElementVisible(titlesOnCreationDialog,10)){
            String seniorityText = null;
            for(WebElement seniorityTitle : titlesOnCreationDialog){
                seniorityText = seniorityTitle.getText().trim();
                if(seniorityText.equalsIgnoreCase("Seniority")){
                    SimpleUtils.report("Seniority Column is displayed!");
                    break;
                }else
                    continue;
                }
            if(!(seniorityText.equalsIgnoreCase("Seniority"))){
                seniorityColumnLoaded = false;
            }
        }else{
            seniorityColumnLoaded = false;
        }
        return seniorityColumnLoaded;
    }


    @FindBy(css = ".table-field.seniority-field")
    private List<WebElement> seniorityValueForAssign;
    @FindBy(css = "[class=\"MuiGrid-root MuiGrid-item MuiGrid-grid-xs-1 css-1909xa1\"] [class=\"sc-bLBzly cUEDBZ\"] [class*=\"MuiTypography-root\"]")
    private List<WebElement> seniorityValueForOpen;

    @Override
    public ArrayList getTMSeniorityValues() throws Exception {
        List<Integer> seniorityValues  = new ArrayList<Integer>();
        if (areListElementVisible(seniorityValueForAssign,10)){
            int i = 0;
            for(WebElement element: seniorityValueForAssign){
                String seniorityValue = element.getText().trim();
                if(seniorityValue.equalsIgnoreCase("SENIORITY")) {
                    continue;
                }
                if(seniorityValue == null || seniorityValue.equals("-")){
                    seniorityValue = "0";
                }
                seniorityValues.add(i, Integer.parseInt(seniorityValue));
                i++;
            }
        }else if (areListElementVisible(seniorityValueForOpen, 10)){
            int j = 0;
            for(WebElement element: seniorityValueForOpen){
                String seniorityValue = element.getText().trim();
                if(seniorityValue.equalsIgnoreCase("SENIORITY")) {
                    continue;
                }
                if(seniorityValue == null || seniorityValue.equals("-")){
                    seniorityValue = "0";
                }
                seniorityValues.add(j, Integer.parseInt(seniorityValue));
                j++;
            }
        }else {
            SimpleUtils.report("Seniority values are not loaded!");
        }
        return (ArrayList) seniorityValues;
    }

    @FindBy(xpath = "//*[contains(@class,'MuiAvatar-circular')]/parent::div/parent::div/parent::div/parent::div/parent::div/parent::div/parent::div/div[1]/div/div/span")
    private List<WebElement> tmListHeaders;
    @FindBy(xpath = "//*[contains(@class,'MuiAvatar-circular')]/parent::div/parent::div/parent::div/parent::div")
    private List<WebElement> allTMsInSearchOrRecommendedList;
    @FindBy(xpath = "//*[contains(@class,'MuiAvatar-circular')]/following-sibling::div/p[1]")
    private List<WebElement> allTMNamesInSearchOrRecommendedList;
    @FindBy(xpath = "//*[contains(@class,'MuiAvatar-circular')]/following-sibling::div/p[2]")
    private List<WebElement> allTMJobTitlesInSearchOrRecommendedList;
    @FindBy(xpath = "//*[contains(@class,'MuiAvatar-circular')]/following-sibling::div/p[3]")
    private List<WebElement> allTMLocationsInSearchOrRecommendedList;

    @Override
    public HashMap<String, String> getTMAllInfoFromSearchOrRecommendedListOnNewCreateShiftPageByIndex(int index) throws Exception {
        HashMap<String, String> allInfo= new HashMap<>();
        if (areListElementVisible(allTMsInSearchOrRecommendedList, 5)
                && areListElementVisible(allTMNamesInSearchOrRecommendedList, 5)
                && areListElementVisible(allTMJobTitlesInSearchOrRecommendedList, 5)
                && areListElementVisible(allTMLocationsInSearchOrRecommendedList, 5)
                && allTMsInSearchOrRecommendedList.size() ==allTMNamesInSearchOrRecommendedList.size()
                && allTMsInSearchOrRecommendedList.size() == allTMJobTitlesInSearchOrRecommendedList.size()
                && allTMsInSearchOrRecommendedList.size() == allTMLocationsInSearchOrRecommendedList.size()) {
            waitForSeconds(5);
            for (int i =0; i< allTMsInSearchOrRecommendedList.size(); i++) {
                //Get TM name
                String tmFullName = allTMNamesInSearchOrRecommendedList.get(i).getText().replace("\"", "").replace("\\n", "").trim();
                allInfo.put("tmname", tmFullName);
                //Get TM job title
                String tmJobTitle = allTMNamesInSearchOrRecommendedList.get(i).getText().trim();
                allInfo.put("tmjobtitle", tmJobTitle);
                //Get TM Location
                String tmLocation = allTMLocationsInSearchOrRecommendedList.get(i).getText().trim();
                allInfo.put("tmlocation", tmLocation);
                //Get Employee ID
                if (areListElementVisible(tmListHeaders, 5)
                        && tmListHeaders.get(1).getText().equalsIgnoreCase("Employee ID")) {
                    String employeeId = allTMsInSearchOrRecommendedList.get(i).findElement(By.xpath("./div[2]")).getText();
                    allInfo.put("employeeid", employeeId);
                }
            }
        }else {
            SimpleUtils.fail("The TMs on search or recommended page fail to load! ", false);
        }
        return allInfo;
    }
}

