package com.legion.pages;

import java.util.HashMap;
import java.util.Map;

public interface IntegrationPage {

	public void clickOnConsoleIntegrationPage() throws Exception;
	public void createConfig(Map<String, String> configInfo) throws Exception;
	public boolean checkIsConfigExists(String channel, String application) throws Exception;
	public void clickOnEditButtonByChannelAndApplication(String channel, String application) throws Exception;
	public boolean isEditConfigPageLoaded() throws Exception;
	public void editTheConfigByName(HashMap<String, String> names) throws Exception;
}
