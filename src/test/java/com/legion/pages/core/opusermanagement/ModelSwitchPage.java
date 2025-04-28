package com.legion.pages.core.opusermanagement;

import com.legion.pages.BasePage;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.legion.tests.TestBase.switchToNewWindow;
import static com.legion.utils.MyThreadLocal.getDriver;

public class ModelSwitchPage extends BasePage {
    public ModelSwitchPage() {
        PageFactory.initElements(getDriver(), this);
        waitForPageLoaded(getDriver());
    }

    // Added by Sophia
    @FindBy(css = "img.modeSwitchIcon")
    private WebElement modeSwitchIcon;
    @FindBy(css = "li.header-mode-switch-menu-item")
    private List<WebElement> modelSwitchOption;
    @FindBy(css = "p.ng-binding.menu-item-op_title.mt-18")
    private WebElement operationPortal;
    @FindBy(css = "p.ng-binding.menu-item-console_title")
    private WebElement console;
    @FindBy(css = "div.console-item>p.ng-binding.menu-item-op_title")
    private WebElement timeClock;

    public void openModeSwitchMenu() {
        waitForSeconds(5);
        modeSwitchIcon.click();
        waitForSeconds(3);
    }

    public void switchToOpsPortal() {
        waitForSeconds(3);
        if (isElementEnabled(modeSwitchIcon, 20)) {
            clickTheElement(modeSwitchIcon);
            waitForSeconds(5);
            if (modelSwitchOption.size() != 0) {
                for (WebElement subOption : modelSwitchOption) {
                    if (subOption.getText().equalsIgnoreCase("Control Center")) {
                        click(subOption);
                        waitForSeconds(5);
                    }
                }

            }
            switchToNewWindow();

        } else
            SimpleUtils.fail("mode switch img load failed", false);


    }

    private void switchToOpsPortalForSophia() {
        openModeSwitchMenu();
        operationPortal.click();
        waitForSeconds(5);
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

}
