package com.legion.pages.core.oplabormodel;

import com.legion.pages.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import static com.legion.utils.MyThreadLocal.getDriver;

public class LaborModelPanelPage extends BasePage {
    public LaborModelPanelPage() {
        PageFactory.initElements(getDriver(), this);
    }

    // Added by Sophia
    @FindBy(xpath = "(//lg-dashboard-card)[1]")
    private WebElement laborModelRepository;
    @FindBy(xpath = "(//lg-dashboard-card)[2]")
    private WebElement laborModel;

    public void goToLaborModelRepositoryPage() {
        laborModelRepository.click();
        waitForSeconds(5);
    }

    public void goToLaborModel() {
        laborModel.click();
        waitForSeconds(5);

    }

}

