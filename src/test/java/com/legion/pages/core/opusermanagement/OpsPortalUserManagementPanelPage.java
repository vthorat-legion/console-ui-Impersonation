package com.legion.pages.core.opusermanagement;

import com.legion.pages.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import static com.legion.utils.MyThreadLocal.getDriver;

public class OpsPortalUserManagementPanelPage extends BasePage {
    public OpsPortalUserManagementPanelPage() {
        PageFactory.initElements(getDriver(), this);
    }

    // Added by Sophia
    @FindBy(css = "[title='Work Roles']")
    private WebElement workRoles;
    @FindBy(css = "[title='Users and Roles']")
    private WebElement usersAndRoles;
    @FindBy(css = "lg-dashboard-card[title='Dynamic Employee Groups']")
    private WebElement dynamicGroups;

    public void goToWorkRolesPage() {
        workRoles.click();
        waitForSeconds(5);
    }

    public void goToUsersAndRoles() {
        usersAndRoles.click();
        waitForSeconds(5);
    }

    public void goToDynamicGroups() {
        dynamicGroups.click();
    }

}
