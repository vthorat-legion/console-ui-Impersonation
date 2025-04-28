package com.legion.pages.core.OpCommons;

import com.legion.pages.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.legion.utils.MyThreadLocal.getDriver;

public class RightHeaderBarPage extends BasePage {
    public RightHeaderBarPage() {
        PageFactory.initElements(getDriver(), this);
        waitForPageLoaded(getDriver());
    }

    // Added by Sophia
    @FindBy(css = "img.modeSwitchIcon")
    private WebElement modeSwitchIcon;
    @FindBy(css = "div.header-mode-switch-menu>ul>li:nth-child(1)")
    private WebElement operationPortal;
    @FindBy(css = "div.header-mode-switch-menu>ul>li:nth-child(2)")
    private WebElement console;
    @FindBy(css = "div.header-mode-switch-menu>ul>li:nth-child(3)")
    private WebElement timeClock;

    //profile
    @FindBy(css = "div.header-avatar.ng-scope>img")
    private WebElement userAvatar;
    //@FindBy(css = "div.header-user-switch-menu>ul>li:nth-child(2)")
    @FindBy(id = "legion_Profile_MyProfile")
    private WebElement myProfile;
    //@FindBy(css = "div.header-user-switch-menu>ul>li:nth-child(3)")
    @FindBy(id = "legion_Profile_MyWorkPrefs")
    private WebElement workPreferences;
    //@FindBy(css = "div.header-user-switch-menu>ul>li:nth-child(4)")
    @FindBy(id = "legion_Profile_MyTimeOff")
    private WebElement timeOff;


    public void openModeSwitchMenu() {
        waitForSeconds(5);
        modeSwitchIcon.click();
        waitForSeconds(3);
    }

    public void switchToOpsPortal() {
        openModeSwitchMenu();
        operationPortal.click();
        switchToNewTab();
        waitForSeconds(10);
    }

    public void switchToConsole() {
        openModeSwitchMenu();
        console.click();
        switchToNewTab();
        waitForSeconds(10);
    }

    public void switchToTimeClock() {
        openModeSwitchMenu();
        timeClock.click();
        switchToNewTab();
        waitForSeconds(10);
    }

    public void switchToNewTab() {
        String currentWindow = getDriver().getWindowHandle();
        for (String handle : getDriver().getWindowHandles()) {
            if (!handle.equals(currentWindow)) {
                getDriver().switchTo().window(handle);
            }
        }
    }

    public void navigateToTimeOff() {
        try {
            userAvatar.click();
            timeOff.click();
        } catch (Exception NoSuchWebElement) {
            if (!timeOff.isDisplayed()) {
                System.out.println("'Can employees request time off ?' was disabled in template lever!");
            } else {
                System.out.println("Failed to load uer avatar!");
            }
        }
    }

    public void switchToMyProfile() {
        userAvatar.click();
        myProfile.click();
    }
}
