package com.legion.pages;

import org.openqa.selenium.WebElement;

import java.util.List;

public interface AdminPage {

    public void goToAdminTab() throws Exception;

    public void rebuildSearchIndex() throws Exception;

    public void clickOnConsoleAdminMenu() throws Exception;

    public void clickOnInspectorTab() throws Exception;

    public void clickOnCacheTab() throws Exception;

    public void refreshCacheStatus(String cacheName);
}
