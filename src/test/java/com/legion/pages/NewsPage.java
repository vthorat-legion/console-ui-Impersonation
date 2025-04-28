package com.legion.pages;

import org.openqa.selenium.WebElement;

import java.util.List;

public interface NewsPage {
    public List<String> createPost()  throws Exception;
    public void clickOnNewsConsoleMenu() throws Exception;
    public boolean checkIfPostExists(String postTitle)  throws Exception;
    public boolean checkIfNewsPageLoaded() throws Exception;
    public void deletePost(String postTitle) throws Exception;
    public void clickNewsfeedTab() throws Exception;
    public void clickModerationTab() throws Exception;
    public void clickOnConsoleNewsMenu() throws Exception;
    public boolean isNewsTabLoadWell() throws Exception;
    public void enableViewing() throws Exception;
}
