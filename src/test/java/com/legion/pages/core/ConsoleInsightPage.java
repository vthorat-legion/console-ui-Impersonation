package com.legion.pages.core;

import com.legion.pages.BasePage;
import com.legion.pages.InsightPage;
import com.legion.utils.SimpleUtils;
import cucumber.api.java.ro.Si;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

import static com.legion.utils.MyThreadLocal.getDriver;


public class ConsoleInsightPage extends BasePage implements InsightPage {

	public ConsoleInsightPage() {
		PageFactory.initElements(getDriver(), this);
	}

	@FindBy(css = ".console-navigation-item-label.Insights")
	private WebElement insightConsoleMenuDiv;
	@FindBy(css = "input-field[value=\"selectedDashboardOption\"]")
	private WebElement calendarForDashboard;


	@Override
	public void clickOnConsoleInsightPage() throws Exception {
		if(isElementLoaded(insightConsoleMenuDiv,5)) {
			click(insightConsoleMenuDiv);
			if (insightConsoleMenuDiv.findElement(By.xpath("./..")).getAttribute("class").contains("active"))
				SimpleUtils.pass("Insights Page: Click on Insights console menu successfully");
			else
				SimpleUtils.fail("Insights Page: It doesn't navigate to Insights console menu after clicking", false);
		} else
			SimpleUtils.fail("Insights Console Menu not loaded Successfully!", false);
	}

	@Override
	public boolean isInsightsPageDisplay() throws Exception {
		if (isElementLoaded(calendarForDashboard,5) ) {
			SimpleUtils.pass("Insight page load successfully");
			return true;
		}else
			SimpleUtils.fail("Insight page load failed",false);
			return false;
	}
}