package com.legion.pages.core.OpCommons;

import com.legion.pages.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import static com.legion.utils.MyThreadLocal.getDriver;

public class OpsPortalNavigationPage extends BasePage {
    public OpsPortalNavigationPage() {
        PageFactory.initElements(getDriver(), this);
    }

    // Added by Sophia
    @FindBy(css = "i.fa.fa-tachometer.fs-22")
    private WebElement dashboard;
    @FindBy(css = "div.console-navigation-item-icon>i.fa-user-plus")
    private WebElement userManagement;
    @FindBy(css = "i.fa.fa-file-text.fs-22")
    private WebElement configuration;
    @FindBy(css = "i.fa.fa-user.fs-22")
    private WebElement laborModel;
    @FindBy(css = "i.fa.fa-map-marker.fs-22")
    private WebElement location;
    @FindBy(css = "i.fa.fa-tasks.fs-20")
    private WebElement jobs;
    @FindBy(css = "[title=\"Employee Management\"]")
    private WebElement employeeManagement;
    @CacheLookup
    @FindBy(css = "i.fa.fa-sign-out.fs-24")
    private WebElement logout;


    public void navigateToDashboardPage() {
        dashboard.click();
    }

    public void navigateToUserManagement() {
        userManagement.click();
    }

    public void navigateToConfiguration() {
        configuration.click();
    }

    public void navigateToLaborModelPage() {
        laborModel.click();
    }

    public void navigateToLocationPage() {
        location.click();
    }

    public void navigateToJobsPage() {
        jobs.click();
    }

    public void navigateToEmployeeManagement() {
        try {
            if (isElementLoaded(employeeManagement, 20)) {
                clickTheElement(employeeManagement);
            } else {
                System.out.println("EmployeeManagement Module is not enabled!");
            }
        } catch (Exception NoSuchElementException) {
        }
    }

    public void logout() {
        scrollToElement(logout);
        logout.click();
    }

}
