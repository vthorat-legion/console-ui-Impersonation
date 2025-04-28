package com.legion.pages.core;

import static com.legion.utils.MyThreadLocal.getDriver;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.legion.pages.BasePage;
import com.legion.pages.ProjectedSalesPage;
import com.legion.utils.SimpleUtils;

public class ConsoleProjectedSalesPage extends BasePage implements ProjectedSalesPage{

	public ConsoleProjectedSalesPage(){
		PageFactory.initElements(getDriver(), this);
	}
	
	@FindBy(css ="div[helper-text=\"Week view\"]")
	private WebElement weekViewBtn;
	
	@FindBy(css = "sch-control-kpi")
	private List<WebElement> projectedSalesKPIDivs;
	
	@Override
	public void clickOnWeekView() throws Exception {
		if(isElementLoaded(weekViewBtn))
			click(weekViewBtn);
		else
			SimpleUtils.fail("Projected Sales Page: Week view button not loaded.", false);
	}
	
	@Override
	public Float getProjectedSalesGuidanceItems()
	{
		String saleGuidanceUnitLabel = "ITEMS";
		if(projectedSalesKPIDivs.size() > 0)
		{
			for(WebElement projectedSalesKPIDiv : projectedSalesKPIDivs)
			{
				if(projectedSalesKPIDiv.getText().toLowerCase().contains(saleGuidanceUnitLabel.toLowerCase()))
					return Float.valueOf(projectedSalesKPIDiv.getText().split(" ")[0]);
			}
		}
		return (float) 0;
	}
}
