package com.legion.pages;

import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ReportPage {

	public void clickOnConsoleReportMenu() throws Exception;

	public boolean exportReportForAllLocations(String reportType);
	public boolean verifyFileExtension(String fileExtensionExpected);
	public List<String> verifyExcelSheet() throws Exception;
	public boolean verifyColumnNameOnSheet(String s, List<String> expectedColumnNames) throws Exception;
	public boolean verifyInfoForOPHealthCheckExportedFile(Map<String, List<String>> dynamicGroupMap) throws Exception;
}