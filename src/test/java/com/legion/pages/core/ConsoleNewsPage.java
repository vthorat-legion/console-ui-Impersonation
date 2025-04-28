package com.legion.pages.core;

import com.google.inject.Key;
import com.legion.pages.ActivityPage;
import com.legion.pages.BasePage;
import com.legion.pages.NewsPage;
import com.legion.utils.SimpleUtils;
import com.legion.pages.BasePage;
import com.legion.pages.NewsPage;
import com.legion.pages.ReportPage;
import com.legion.utils.SimpleUtils;
import cucumber.api.java.ro.Si;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.Keys;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import java.util.List;

import static com.legion.utils.MyThreadLocal.getDriver;

public class ConsoleNewsPage extends BasePage implements NewsPage {

	public ConsoleNewsPage() {
		PageFactory.initElements(getDriver(), this);
	}

	// Added by Nora
	@FindBy (css = "[data-testid=\"feed-list-wrapper\"] section div span")
	private WebElement createPostButton;

	@FindBy (css = "input[placeholder=\"Subject\"]")
	private WebElement postTitle;

	@FindBy (css = "div.lg-modal div.notranslate")
	private WebElement postMessage;

	@FindBy(css = "div.console-navigation-item-label.News")
	private WebElement consoleNavigationNews;

	@FindBy(css = "h2[data-testid=\"post-title\"]")
	private List<WebElement> postTitles;

	@FindBy(css = "[data-testid=\"feed-list-wrapper\"] section span svg")
	private WebElement searchPostButton;

	@FindBy(css = "[data-testid=\"feed-list-wrapper\"] input.MuiInputBase-input")
	private WebElement searchBox;

	@FindBy(css = "div[id=\"newsfeed-container\"]")
	private WebElement newsFeedSection;

	@FindBy(css = "lg-button[label=\"Post\"]")
	private WebElement saveButton;

	@FindBy(css = "[data-testid=\"feed-list-wrapper\"] article")
	private List<WebElement> posts;

	@FindBy(css = "[data-testid=\"feed-list-wrapper\"] section [type=\"button\"]")
	private WebElement deleteSearchTextButton;

	@FindBy(css = "div.sub-navigation-view-link")
	private List<WebElement> newsSubNavigations;



	@Override
	public List<String> createPost() throws Exception {
		List<String> postInfo = new ArrayList<>();
		String title = "TestPostTitle" + (new Random()).nextInt(100);
		String message = "TestPostMessage" + (new Random()).nextInt(100);
		if (isElementEnabled(createPostButton, 5)) {
			click(createPostButton);
			if (isElementEnabled(postTitle, 5) && isElementEnabled(postMessage, 5)
					&& isElementEnabled(saveButton, 5)){
				postTitle.clear();
				postTitle.sendKeys(title);
				postMessage.clear();
				postMessage.sendKeys(message);
				click(saveButton);
				postInfo.add(title);
				postInfo.add(message);
			} else
				SimpleUtils.fail("Post title or message input fail to load! ", false);
		} else
			SimpleUtils.fail("Create post button fail to load! ", false);
		return postInfo;
	}

	@Override
	public void clickOnNewsConsoleMenu() throws Exception {
		if(isElementLoaded(consoleNavigationNews))
			click(consoleNavigationNews);
		else
			SimpleUtils.fail("Unable to click on 'Analytics' console menu.", false);
	}

	@FindBy(css = ".console-navigation-item-label.News")
	private WebElement newsConsoleMenuDiv;
	@FindBy(className = "sub-navigation-view-link")
	private List<WebElement> newsSubTab;
	@FindBy(css = ".MuiButtonBase-root.MuiListItem-root")
	private List<WebElement> postOperations;

	
	@Override
	public void clickOnConsoleNewsMenu() throws Exception {
		if(isElementLoaded(newsConsoleMenuDiv,20)) {
			click(newsConsoleMenuDiv);
			if (newsConsoleMenuDiv.findElement(By.xpath("./..")).getAttribute("class").contains("active"))
				SimpleUtils.pass("News Page: Click on News console menu successfully");
			else
				SimpleUtils.fail("News Page: It doesn't navigate to News console menu after clicking", false);
		} else
			SimpleUtils.fail("News Console Menu not loaded Successfully!", false);
	}


	@Override
	public boolean checkIfPostExists(String postTitle) throws Exception{
		boolean ifPostExists = false;
		if (isElementEnabled(searchPostButton, 5)) {
			click(searchPostButton);
		}
		if (isElementEnabled(searchBox, 5)){
			if (isElementEnabled(deleteSearchTextButton, 5)) {
				click(deleteSearchTextButton);
			}
			searchBox.sendKeys(postTitle);
			searchBox.sendKeys(Keys.ENTER);
		}
		waitForSeconds(5);
		if (areListElementVisible(postTitles, 10) && postTitles.size()>0){
			for (int i=0; i< postTitles.size(); i++) {
				if (postTitles.get(i).getText().equalsIgnoreCase(postTitle)){
					ifPostExists = true;
					SimpleUtils.report("The post : "+ postTitle+ " is exists!");
					break;
				}
			}
		}
		return ifPostExists;
	}

	@Override
	public boolean checkIfNewsPageLoaded() throws Exception{
		boolean ifNewsLoaded = false;
		if (areListElementVisible(newsSubNavigations, 10)
				&& newsSubNavigations.size() == 2
				&& newsSubNavigations.get(0).getText().equalsIgnoreCase("Newsfeed")
				&& newsSubNavigations.get(1).getText().equalsIgnoreCase("Moderation")
				&& isElementEnabled(newsFeedSection, 10)) {
			ifNewsLoaded = true;
		} else
			SimpleUtils.report("The News page fail to load! ");
		return ifNewsLoaded;
	}


	@Override
	public void deletePost(String postTitle) throws Exception{
		if (isElementEnabled(searchPostButton, 5)) {
			click(searchPostButton);
		}
		if (isElementEnabled(searchBox, 5)){
			if (isElementEnabled(deleteSearchTextButton, 5)) {
				click(deleteSearchTextButton);
			}
			searchBox.sendKeys(postTitle);
			searchBox.sendKeys(Keys.ENTER);
		}
		waitForSeconds(5);
		if (areListElementVisible(posts, 10) && posts.size()>0){
			for (WebElement post: posts) {
				click(post.findElement(By.cssSelector("[type=\"button\"]")));
				if (postOperations.size() == 1){
					click(postOperations.get(0));
				} else
					click(postOperations.get(1));
			}
		} else
			SimpleUtils.fail("The post: "+ postTitle+" is not exists! ", false);
	}

	@Override
	public void clickNewsfeedTab() throws Exception{
		if (areListElementVisible(newsSubNavigations, 5)) {
			click(newsSubNavigations.get(0));
		} else
			SimpleUtils.fail("The Newsfeed tabs fail to load! ", false);
	}

	@Override
	public void clickModerationTab() throws Exception{
		if (areListElementVisible(newsSubNavigations, 5)) {
			click(newsSubNavigations.get(1));
		} else
			SimpleUtils.fail("The Newsfeed tabs fail to load! ", false);
	}

	public boolean isNewsTabLoadWell() throws Exception {
		if (newsSubTab.size()>0) {
//			click(newsSubTab.get(0));
//			if (isElementLoaded(createPostPanel,5)) {
//				SimpleUtils.pass("News tab load successfully");
//			}
			return true;
		}else
			SimpleUtils.fail("News tab load failed",false);
		return false;
	}


	@FindBy(css = "div.MuiAlert-message button")
	private WebElement enableViewingButton;

	@FindBy(css = "[role=\"presentation\"] [role=\"button\"]")
	private List<WebElement> confirmButtonsOnEnablePopupPage;

	public void enableViewing() throws Exception {
		if (isElementEnabled(enableViewingButton, 5)) {
			click(enableViewingButton);
			if (areListElementVisible(confirmButtonsOnEnablePopupPage, 5)
					&& confirmButtonsOnEnablePopupPage.size() ==2){
				click(confirmButtonsOnEnablePopupPage.get(1));
			}
		}
	}
}
