package com.legion.pages.core;

import static com.legion.utils.MyThreadLocal.getDriver;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.aventstack.extentreports.Status;
import com.legion.pages.BasePage;
import com.legion.pages.UserAuthorizationPage;
import com.legion.tests.testframework.ExtentTestManager;

public class ConsoleUserAuthorizationPage extends BasePage implements UserAuthorizationPage {

	@FindBy(css=".console-navigation-item-label")
	private List<WebElement> menuElements;
	
	public ConsoleUserAuthorizationPage() {
//    	super(driver);
		PageFactory.initElements(getDriver(), this);
	}

	@Override
	public void findAllVisibleMenu() throws Exception {
		
//		List<WebElement> menuElements = driver.findElements(By.className("console-navigation-item"));
    	int index = 0;
    	for(WebElement menuElement: menuElements)
    	{
    		
    		if(menuElement.isDisplayed())
    		{
        		
    			ExtentTestManager.extentTest.get().log(Status.PASS,menuElement.getText() +" present on console page");
    			index = index +1;
    		}
    	}
//    	if(driver.findElements(By.className("ion-ios-person")).size() != 0)
//    	{
//    		driver.findElement(By.className("ion-ios-person")).click();
//    		Thread.sleep(5000);
//    	}
	}

	
}
