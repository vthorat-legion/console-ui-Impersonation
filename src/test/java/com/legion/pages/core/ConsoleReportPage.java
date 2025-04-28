package com.legion.pages.core;

import com.legion.pages.BasePage;
import com.legion.pages.ReportPage;

import com.legion.tests.testframework.PropertyMap;
import com.legion.utils.FileDownloadVerify;
import com.legion.utils.SimpleUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;


import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.legion.utils.MyThreadLocal.getDriver;

public class ConsoleReportPage extends BasePage implements ReportPage{

	public ConsoleReportPage() {
		PageFactory.initElements(getDriver(), this);
	}

	@FindBy(css = "div.console-navigation-item-label.Report")
	private WebElement reportConsoleMenuDiv;


	@Override
	public void clickOnConsoleReportMenu() throws Exception {
		if(isElementLoaded(reportConsoleMenuDiv,5)) {
			click(reportConsoleMenuDiv);
			if (reportConsoleMenuDiv.findElement(By.xpath("./..")).getAttribute("class").contains("active"))
				SimpleUtils.pass("Report Page: Click on Compliance console menu successfully");
			else
				SimpleUtils.fail("Report Page: It doesn't navigate to Compliance console menu after clicking", false);
		} else
			SimpleUtils.fail("Report Console Menu not loaded Successfully!", false);


	}

	@FindBy(css = "[ng-if=\"canDownloadAllLocationReports\"] div[class*=\"ng-scope lg-button-group-last\"]")
	private WebElement allLocationsTab;
	@FindBy(css = "div.table-responsive tbody tr[class*=\"row ng-scope\"]")
	private List<WebElement> reportList;
	@Override
	public boolean exportReportForAllLocations(String reportType) {
		boolean flag = false;
		if(isElementEnabled(allLocationsTab)){
			click(allLocationsTab);
			for (WebElement report : reportList){
				if(report.findElement(By.cssSelector("div.sch-kpi-title-text")).getText().toLowerCase().contains(reportType.toLowerCase())){
					scrollToElement(report);
					mouseToElement(report);
					click(report.findElement(By.cssSelector("span.sch-kpi-action-text")));
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_hh-mm");
					dateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
					String partOfFileName = "LEG" + "-" + dateFormat.format(new Date());
					waitForSeconds(15);
					System.out.println("partOfFileName is: " +  partOfFileName);
					String downloadPath = propertyMap.get("Download_File_Default_Dir");
					System.out.println("downloadPath " + downloadPath);
					Assert.assertTrue(FileDownloadVerify.isFileDownloaded_Ext(downloadPath, partOfFileName), "Download successfully");
					flag = true;
					SimpleUtils.pass("Report Type " + reportType + "is found and exported successfully!");
				}
			}
			if(!flag){
				SimpleUtils.fail("Can not find the " + reportType, false);
			}
		}
		System.out.println("flag: " + flag);
		return flag;
	}

	@Override
	public boolean verifyFileExtension(String fileExtensionExpected) {
		boolean flag = false;
		String downloadDirPath = PropertyMap.get("Download_File_Default_Dir");
		File latestFile = SimpleUtils.getLatestFileFromDirectory(downloadDirPath);
		String fileName = latestFile.getName();
		String extension = fileName.split("\\.")[1];
		if (extension.equalsIgnoreCase(fileExtensionExpected)) {
			SimpleUtils.pass("The exported file " + fileName + " has the expected extension: " + fileExtensionExpected);
			flag = true;
		}
		return flag;
	}

	@Override
	public List<String> verifyExcelSheet() throws Exception{
		String downloadDirPath = PropertyMap.get("Download_File_Default_Dir");
		File latestFile = SimpleUtils.getLatestFileFromDirectory(downloadDirPath);
		FileInputStream inputStream = new FileInputStream(latestFile);
		Workbook workbook = null;
		List<String> sheetNameList = new ArrayList<>();


		if(verifyFileExtension("xlsx")){
			workbook = new XSSFWorkbook(inputStream);
		}
		else if (verifyFileExtension("xls")){
			workbook = new HSSFWorkbook(inputStream);
		}
		int sheetCount = workbook.getNumberOfSheets();

		String sheetName = "";
		for (int i = 0; i < sheetCount; i++){
			sheetName = workbook.getSheetName(i);
			System.out.println("sheetName: " + sheetName);
			sheetNameList.add(sheetName);
		}
		return sheetNameList;
	}

	@Override
	public boolean verifyColumnNameOnSheet(String sheetName, List<String> expectedColumnNames) throws Exception{
		boolean flag = false;
		String downloadDirPath = PropertyMap.get("Download_File_Default_Dir");
		File latestFile = SimpleUtils.getLatestFileFromDirectory(downloadDirPath);
		FileInputStream inputStream = new FileInputStream(latestFile);
		Workbook workbook = null;
		List<String> sheetNameList = new ArrayList<>();

		if(verifyFileExtension("xlsx")){
			workbook = new XSSFWorkbook(inputStream);
		}
		else if (verifyFileExtension("xls")){
			workbook = new HSSFWorkbook(inputStream);
		}

		Sheet sheet = workbook.getSheet(sheetName);
		Row row = sheet.getRow(sheet.getFirstRowNum());
		int cellCount = row.getLastCellNum();
		List<String> cellValues = new ArrayList<>();
		for (int i=0; i < cellCount; i++){
			cellValues.add(row.getCell(i).getStringCellValue());
		}
		cellValues.sort(Comparator.comparing(String::hashCode));
		expectedColumnNames.sort(Comparator.comparing(String::hashCode));
		if(cellValues.toString().equalsIgnoreCase(expectedColumnNames.toString())){
			flag = true;
		}
		return flag;
	}

	@Override
	public boolean verifyInfoForOPHealthCheckExportedFile(Map<String, List<String>> dynamicGroupMap) throws Exception{
		boolean flag = false;
		String downloadDirPath = PropertyMap.get("Download_File_Default_Dir");
		File latestFile = SimpleUtils.getLatestFileFromDirectory(downloadDirPath);
		FileInputStream inputStream = new FileInputStream(latestFile);
		Workbook workbook = null;
		List<String> sheetNameList = new ArrayList<>();

		if(verifyFileExtension("xlsx")){
			workbook = new XSSFWorkbook(inputStream);
		}
		else if (verifyFileExtension("xls")){
			workbook = new HSSFWorkbook(inputStream);
		}

		for (Map.Entry<String, List<String>> entry : dynamicGroupMap.entrySet()) {
			int checkIndex = 0;
			System.out.println(entry.getKey() + "," + entry.getValue());
			if (entry.getKey() != null && !entry.getKey().isEmpty()){
				String sheetName = entry.getKey();
				Sheet sheet = workbook.getSheet(sheetName);
				Row firstRow = sheet.getRow(sheet.getFirstRowNum());
				int columnCount = firstRow.getLastCellNum();
				// find the column index for "Template Type"
				for(int i = 0; i <= columnCount; i++){
					if(firstRow.getCell(i).getStringCellValue().equalsIgnoreCase("Template Type")){
						checkIndex = i;
						break;
					}
				}

				int rowCount = sheet.getLastRowNum();
				for (int j = 1; j <= rowCount; j++){
					Row row = sheet.getRow(j);
					if(entry.getValue().contains(row.getCell(checkIndex).getStringCellValue())){
						flag = true;
					}
				}
			}
		}
		return flag;
	}
}