package com.legion.pages.core;

import com.legion.pages.ActivityPage;
import com.legion.pages.AdminPage;
import com.legion.pages.BasePage;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;

import static com.legion.utils.MyThreadLocal.getDriver;

public class ConsoleAdminPage extends BasePage implements AdminPage {

	public ConsoleAdminPage() {
		PageFactory.initElements(getDriver(), this);
	}

	public enum CacheNames {
		ABSwitch("ABSwitch"),
		AssignmentRule("AssignmentRule"),
		DifferentialPay("DifferentialPay"),
		DynamicGroup("DynamicGroup"),
		Employee("Employee"),
		Engagement("Engagement"),
		Enterprise("Enterprise"),
		EnterpriseQueueConfig("EnterpriseQueueConfig"),
		HazardPayType("HazardPayType"),
		Holiday("Holiday"),
		LocationBrokerContainer("LocationBrokerContainer"),
		LocationGroup("LocationGroup"),
		LocationSubscription("LocationSubscription"),
		LoginAlias("LoginAlias"),
		NearByLocation("NearByLocation"),
		TAConfig("TAConfig"),
		TACorporateEmployeeLocations("TACorporateEmployeeLocations"),
		TAOTParams("TAOTParams"),
		Template("Template"),
		TemplateAssociation("TemplateAssociation"),
		TemplateUserAssociation("TemplateUserAssociation"),
		Translation("Translation"),
		WorkerBadge("WorkerBadge"),
		WorkerPreference("WorkerPreference"),
		WorkforceSharingGroup("WorkforceSharingGroup");
		private final String value;

		CacheNames(final String newValue) {
			value = newValue;
		}

		public String getValue() {
			return value;
		}
	}

		// added by Estelle

	@FindBy(css="div.console-navigation-item-label.Admin")
	private WebElement consoleAdminPageTabElement;

	@FindBy(className = "lgn-action-dropdown-button")
	private WebElement actionDropDownBtn;
	@Override
	public void goToAdminTab() throws Exception {
		if (isElementLoaded(consoleAdminPageTabElement,5) ) {
			clickTheElement(consoleAdminPageTabElement);
			if (isElementLoaded(actionDropDownBtn, 5)) {
				SimpleUtils.pass("Admin page load successfully");
			} else
				SimpleUtils.fail("Admin page load failed", false);
		}else
			SimpleUtils.fail("Admin tab is not clickable", false);
	}

	@FindBy(xpath = "//lgn-action-drop-down/div/ul/li[4]/a")
	private WebElement rebuildSearchIndexAction;

	@Override
	public void rebuildSearchIndex() throws Exception {
		if (isElementLoaded(actionDropDownBtn,5)) {
			clickTheElement(actionDropDownBtn);
			waitForSeconds(3);
			if (isElementLoaded(rebuildSearchIndexAction,5)) {
				SimpleUtils.pass("Action button is clickable");
				clickTheElement(rebuildSearchIndexAction);
				if (!isElementLoaded(rebuildSearchIndexAction,5)) {
					SimpleUtils.pass("Rebuild search index button is clickable");
				}
				waitForSeconds(30);
			}else
				SimpleUtils.fail("Action button is not clickable",false);

		}
	}

	@Override
	public void clickOnConsoleAdminMenu() throws Exception {
		if(isElementLoaded(consoleAdminPageTabElement,20)) {
			click(consoleAdminPageTabElement);
			if (consoleAdminPageTabElement.findElement(By.xpath("./..")).getAttribute("class").contains("active"))
				SimpleUtils.pass("Admin Page: Click on Admin console menu successfully");
			else
				SimpleUtils.fail("Admin Page: It doesn't navigate to Admin console menu after clicking", false);
		} else
			SimpleUtils.fail("Admin Console Menu not loaded Successfully!", false);
	}

	@FindBy(css = "[data-testid=\"console.legionadmin.inspector\"]")
	private WebElement inspectorTab;

//	@FindBy(css = ".lg-button-group-last")
	@FindBy(css = "div[id=\"legion_cons_Admin_Inspector_Cache_button\"]")
	private WebElement cacheTab;

	@FindBy(css = "[ng-repeat=\"cache in filteredCaches\"]")
	private List<WebElement> cachesInCacheList;

	@Override
	public void clickOnInspectorTab() throws Exception {
		if(isElementLoaded(inspectorTab,20)) {
			click(inspectorTab);
			SimpleUtils.pass("Admin Page: Click on Inspector tab successfully");
		} else
			SimpleUtils.fail("Admin: Inspector tab not loaded Successfully!", false);
	}

	@Override
	public void clickOnCacheTab() throws Exception {
		if(isElementLoaded(cacheTab,20)) {
			click(cacheTab);
			SimpleUtils.pass("Admin Page: Click on Cache tab successfully");
		} else
			SimpleUtils.fail("Admin: Cache tab not loaded Successfully!", false);
	}

	@Override
	public void refreshCacheStatus(String cacheName) {
		if(areListElementVisible(cachesInCacheList,20) && cachesInCacheList.size()>0) {
			boolean isTemplateExist = false;
			for (WebElement cache: cachesInCacheList) {
				String cacheNameInList = cache.findElement(By.cssSelector(":nth-child(2)")).getText();
				System.out.println("The cache name in list is:"+cacheNameInList);
				if (cacheNameInList.equalsIgnoreCase(cacheName)) {
					isTemplateExist = true;
					clickTheElement(cache.findElement(By.tagName("button")));
					waitForSeconds(2);
					SimpleUtils.pass("Click the Refresh button successfully! ");
					try{
						WebElement requestSendTime = cache.findElement(By.cssSelector("div[ng-if=\"hasPendingRequest(cache)\"]"));
						if (isElementLoaded(requestSendTime, 5)){
							SimpleUtils.pass("The cache been refresh successfully! ");
						}
					} catch (Exception e){
						SimpleUtils.fail("The cache fail to refresh! ", false);
					}
					break;
				}
			}
			if (!isTemplateExist) {
				SimpleUtils.fail("Admin: The cache "+cacheName+" is not exists! ", false);
			}
		} else
			SimpleUtils.fail("Admin: Caches in cache list not loaded Successfully!", false);
	}
}
