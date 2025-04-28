package com.legion.pages.core.OpCommons;

import com.legion.pages.BasePage;
import com.legion.tests.TestBase;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

import static com.legion.utils.MyThreadLocal.getDriver;

public class ActivityPage extends BasePage{
    public ActivityPage() {
        PageFactory.initElements(getDriver(), this);
    }

    //activity
    @FindBy(css = "div.bell-container i")
    private WebElement Activity;

    public boolean verifyActivityDisplay() throws Exception{
        if(isElementLoaded(Activity,5)){
            return true;
        }else
            return false;
    }

    //activity box
    @FindBy(css = "div.notification-bell-popup-container.ng-scope")
    private WebElement ActivityBox;

    public boolean verifyActivityBoxDisplay() throws Exception{
        boolean flag = verifyActivityDisplay();
        if(flag){
            clickTheElement(Activity);
            if(isElementLoaded(ActivityBox,5)){
                return true;
            }
        }
        return false;
    }

    //time off
    @FindBy(css = "img[src='img/legion/icon/notifications/filter-icon/time-off.svg']")
    private WebElement activityTimeOff;

    public void clickTimeOff() throws Exception{
        if(isElementLoaded(activityTimeOff,5)){
            clickTheElement(activityTimeOff);
            SimpleUtils.pass("Time off displayed successfully");
        }else
            SimpleUtils.fail("Time off loaded failed",false);
    }

    //time off item
    @FindBy(css = "div.notification-container.ng-scope.unread")
    private List<WebElement> activityTimeOffItems;

    private int getActvityTimeOffSize(){
        return activityTimeOffItems.size();
    }

    //time off detail
    @FindBy(css = "div[ng-click='toggleDetails()']")
    private List<WebElement> activityTimeOffDetail;

    public void clickTimeOffDetail() throws Exception{
        Integer size = getActvityTimeOffSize();
        for(int i=0; i<size-1; i++){
            clickTheElement(activityTimeOffDetail.get(i));
        }
    }

    //time off approve
    @FindBy(css = "div.notification-buttons-button.ng-binding.ng-scope")
    private List<WebElement> activityTimeOffApprove;

    public void verifyApproveIsClickable() throws Exception{
        int size = getActvityTimeOffSize();
        for(int i=0; i<size; i++) {
            if (isElementLoaded(activityTimeOffApprove.get(i), 5)) {
                if (isClickable(activityTimeOffApprove.get(i),3)) {
                    SimpleUtils.pass("Approve button is clickable");
                }
                else
                    SimpleUtils.fail("Approve button is not clickable",false);
            }else
                SimpleUtils.fail("Approve button loaded failed",false);
        }
    }

    //time off reject
    @FindBy(css = "div.notification-buttons-button.reject.ng-binding.ng-scope")
    private List<WebElement> activityTimeOffReject;

    public void verifyRejectIsClickable() throws Exception{
        int size = getActvityTimeOffSize();
        for(int i=0; i<size; i++) {
            if (isElementLoaded(activityTimeOffReject.get(i), 5)) {
                if (isClickable(activityTimeOffReject.get(i),3)) {
                    SimpleUtils.pass("Reject button is clickable");
                }
                else
                    SimpleUtils.fail("Reject button is not clickable",false);
            }else
                SimpleUtils.fail("Reject button loaded failed",false);
        }
    }

    // activity title
    @FindBy(css = "div.notification-bell-popup-header-container span")
    private WebElement acivityTitle;

    public void verifyActivityTitle() throws Exception{
        if(isElementLoaded(acivityTitle,5)){
            int size = getActvityTimeOffSize();
            if(size == 0){
                if(acivityTitle.getText().equals("Activities")){
                    SimpleUtils.pass("Activity title displayed correctly");
                }else
                    SimpleUtils.fail("Activity title displayed wrong",false);
            }else{
                if(acivityTitle.getText().equals("Activities: " + size + " Pending")){
                    SimpleUtils.pass("Activity title displayed correctly");
                }else
                    SimpleUtils.fail("Activity title displayed wrong",false);
            }
        }else
            SimpleUtils.fail("Activity title loaded falied",false);
    }

    // pending status
    @FindBy(css = "span.request-status.request-status-Pending")
    private List<WebElement> activityTimeOffStatus;

    public void verifyActivityTimeOffStatus() throws Exception{
        int size = getActvityTimeOffSize();
        for(int i=1; i < size-1; i++){
            if(isElementLoaded(activityTimeOffStatus.get(i),5)){
                if(activityTimeOffStatus.get(i).getText().equals("PENDING")){
                    SimpleUtils.pass("Activity time off status is correct");
                }else
                    SimpleUtils.fail("Activity time off status is wrong",false);
            }else
                SimpleUtils.fail("Activity time off status doesn't display",false);
        }
    }

    public void approveActivityTimeOff() throws Exception{
        waitForSeconds(2);
        clickTheElement(activityTimeOffApprove.get(0));
    }

    public void rejectActivityTimeOff() throws Exception{
        clickTheElement(activityTimeOffReject.get(0));
    }

    public void switchToNewWindow() throws Exception{
        TestBase.switchToNewWindow();
    }

    // approve/reject text
    @FindBy(css = "div.notification-buttons div")
    private List<WebElement> approveRejectText;

    public void verifyCancel() throws Exception{
        if(isElementLoaded(approveRejectText.get(0),5)) {
            if (approveRejectText.get(0).getAttribute("innerText").contains("Reject")) {
                SimpleUtils.pass("Cancelled successfully");
            }else{
                SimpleUtils.fail("Cancelled failed",false);
            }
        }else{
            SimpleUtils.fail("Cancelled text doesn't display",false);
        }
    }

    public void verifyApprove() throws Exception{
        if(isElementLoaded(approveRejectText.get(0),5)) {
            if (approveRejectText.get(0).getAttribute("innerText").contains("Approve")) {
                SimpleUtils.pass("Approved successfully");
            }else{
                SimpleUtils.fail("Approved failed",false);
            }
        }else{
            SimpleUtils.fail("Approved text doesn't display",false);
        }
    }

    public void verifyReject() throws Exception{
        if(isElementLoaded(approveRejectText.get(1),5)) {
            if (approveRejectText.get(1).getAttribute("innerText").contains("Rejected")) {
                SimpleUtils.pass("Rejected successfully");
                click(Activity);
            }else{
                SimpleUtils.fail("Rejected failed",false);
            }
        }else{
            SimpleUtils.fail("Rejected text doesn't display",false);
        }
    }

}
