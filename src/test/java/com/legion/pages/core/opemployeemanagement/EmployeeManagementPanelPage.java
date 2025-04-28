package com.legion.pages.core.opemployeemanagement;

import com.legion.pages.BasePage;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import static com.legion.utils.MyThreadLocal.getDriver;

public class EmployeeManagementPanelPage extends BasePage {
    public EmployeeManagementPanelPage() {
        PageFactory.initElements(getDriver(), this);
    }

    // Added by Sophia
    @FindBy(css = "lg-dashboard-card[title='Time Off Management']")
    private WebElement timeOffManagement;

    public void goToTimeOffManagementPage() {
        try {
            if (timeOffManagement.isDisplayed()) {
                timeOffManagement.click();
                waitForSeconds(3);
            }
        } catch (NoSuchElementException e) {
            SimpleUtils.fail(e.getMessage(), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getDashboardCardContent() {
        return timeOffManagement.getText();
    }

}
