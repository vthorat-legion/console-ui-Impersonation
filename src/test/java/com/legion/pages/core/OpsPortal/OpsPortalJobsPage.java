package com.legion.pages.core.OpsPortal;

import com.legion.pages.BasePage;
import com.legion.pages.OpsPortaPageFactories.JobsPage;
import com.legion.pages.SmartCardPage;
import com.legion.pages.core.schedule.ConsoleSmartCardPage;
import com.legion.utils.FileDownloadVerify;
import com.legion.utils.JsonUtil;
import com.legion.utils.MyThreadLocal;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.legion.tests.TestBase.propertyMap;
import static com.legion.utils.MyThreadLocal.*;


public class OpsPortalJobsPage extends BasePage implements JobsPage {

	public OpsPortalJobsPage() {
		PageFactory.initElements(getDriver(), this);
	}
	private static Map<String, String> newLocationParas = JsonUtil.getPropertiesFromJsonFile("src/test/resources/AddANewLocation.json");
	private static HashMap<String, String> parameterMap = JsonUtil.getPropertiesFromJsonFile("src/test/resources/envCfg.json");

	public enum jobSummarySmartCardData {
		jobsCompleted("jobsCompleted"),
		jobsInProgress("jobsInProgress"),
		notStarted("notStarted"),
		jobs("number");
		private final String value;

		jobSummarySmartCardData(final String newValue) {
			value = newValue;
		}

		public String getValue() {
			return value;
		}
	}
	// Added by Estelle
	@FindBy(css="[class='console-navigation-item-label Jobs']")
	private WebElement goToJobsButton;
	@FindBy(css="lg-button[label=\"Create New Job\"]")
	private WebElement createNewJobBtn;
	@FindBy(css="lg-filter[label=\"Filter\"]")
	private WebElement filterBtn;
	@FindBy(css="input[placeholder=\"You can search by Job title or type\"]")
	private WebElement searchInputBox;
	@FindBy (css = ".lg-pagination__arrow--left")
	private WebElement pageLeftBtn;
	@FindBy (css = ".lg-pagination__arrow--right")
	private WebElement pageRightBtn;
	@FindBy(css = "div.card-carousel")
	private WebElement smartCardPanel;
	@FindBy(css = "div.lg-filter__wrapper")
	private WebElement filterPopup;
	@FindBy(css = "[ng-repeat=\"(key, opts) in $ctrl.displayFilters\"]")
	private List<WebElement> filterElementsInJob;

	@Override
	public void iCanEnterJobsTab() {
		if (isElementEnabled(goToJobsButton,3)) {
			scrollToElement(goToJobsButton);
			clickTheElement(goToJobsButton);
			if (isElementEnabled(createNewJobBtn,5)) {
				SimpleUtils.pass("I can enter jobs page");
			}else
				SimpleUtils.fail("I can not enter jobs page",false);
		}else
			SimpleUtils.fail("Jobs button load failed",false);
	}

	@Override
	public void verifyJobLandingPageShowWell() {
		if (isElementEnabled(filterBtn,3)&& isElementEnabled(createNewJobBtn,3)&&
		isElementEnabled(searchInputBox,3)&& isElementEnabled(pageLeftBtn,3)&&isElementEnabled(pageRightBtn,3)
		&& isElementEnabled(smartCardPanel,3)) {
			SimpleUtils.pass("Jobs landing page show well and the placeholder of search field on the jobs landing page show well");
		}else
			SimpleUtils.fail("Jobs landing page load failed",false);
	}

	@FindBy(css = "input[aria-label=\"Job Title\"]")
	private WebElement jobTitleInputBox;
	@FindBy(css = "input[aria-label=\"Comments\"]")
	private WebElement jobCommentsInputBox;
	@FindBy(css = "input-field[options=\"jobTypeChoices\"]>ng-form >div >select")
	private WebElement jobTypeSelectBox;
	@FindBy(css = "div[ng-repeat=\"week in model.data\"]")
	private List<WebElement>  weekSelecters;
	@FindBy(css="lg-button[label=\"OK\"]")
	private WebElement okBtnInCreateNewJobPage;
	@FindBy(css="lg-button[label=\"Add Location\"]")
	private WebElement addLocationBtn;
	@FindBy(css="lg-button[label=\"Add\"]")
	private WebElement addBtn;

	@Override
	public void iCanEnterCreateNewJobPage() {
		if (isElementEnabled(createNewJobBtn,3) ) {
			click(createNewJobBtn);
			if (isElementEnabled(jobTypeSelectBox,3)) {
				SimpleUtils.pass("I can enter create new job page successfully");
			}else
				SimpleUtils.fail("I can not enter create new job page ",false);
		}else 
			SimpleUtils.fail("Create new job button load failed",false);
	}

	@Override
	public void selectJobType(String jobType) throws Exception {
		selectByVisibleText(jobTypeSelectBox,jobType);
		if (!jobTypeSelectBox.getAttribute("class").equalsIgnoreCase("ng-empty")) {
			SimpleUtils.pass("Select job type" + jobType + "successfully");
		}else
			SimpleUtils.fail("Select job type" + jobType + "failed",false);
	}

	@Override
	public void selectWeekForJobToTakePlace() {
		for (WebElement weekSelecter:weekSelecters
			 ) {
			if (!weekSelecter.getAttribute("class").contains("unselectable-week")) {
				click(weekSelecter);
				if (weekSelecter.getAttribute("class").contains("current-week")) {
					SimpleUtils.pass("Select Week for job to take place successfully");
					break;
				}else
					SimpleUtils.fail("Failed to select week for job",false);
			}

		}
	}

	@Override
	public void clickOkBtnInCreateNewJobPage() {
		if (isElementEnabled(okBtnInCreateNewJobPage,3)) {
			click(okBtnInCreateNewJobPage);
			if (isElementEnabled(jobTitleInputBox,3)) {
				SimpleUtils.pass("OK button is clickable and can enter job creation details page");
			}
		}else
			SimpleUtils.fail("Ok button load failed",false);

	}

	@Override
	public void inputJobTitle(String jobTitle) {
		if (isElementEnabled(jobTitleInputBox,3)) {
			jobTitleInputBox.sendKeys(jobTitle);
		}else
			SimpleUtils.fail("job Title InputBox load failed",false);

	}

	@Override
	public void inputJobComments(String commentText) {
		if (isElementEnabled(jobCommentsInputBox,3)) {
			jobCommentsInputBox.sendKeys(commentText);
		}else
			SimpleUtils.fail("job comment InputBox load failed",false);
	}

	@Override
	public void addLocationBtnIsClickable() {
		scrollToElement(addLocationBtn);
		click(addLocationBtn);
		if (isElementEnabled(selectALocationTitle,5)) {
			SimpleUtils.pass("Add location button is clickable and can enter select location page");
		}else
			SimpleUtils.fail("Add location button load failed",false);
	}
	@FindBy(css=".lg-modal__title-icon")
	private WebElement selectALocationTitle;
	@FindBy(css="div.lg-tab-toolbar__search >lg-search >input-field>ng-form>input")
	private WebElement searchInputInSelectALocation;
	@FindBy(css="tr[ng-repeat=\"item in $ctrl.currentPageItems track by $index\"]")
	private List<WebElement> locationRowsInSelectLocation;
	@FindBy(css="lg-button[label=\"Create\"]")
	private WebElement createBtn;

	@Override
	public void iCanSelectLocationsByAddLocation(String searchText, int index) {
		if (isElementEnabled(selectALocationTitle,5)) {
			searchInputInSelectALocation.sendKeys(searchText);
			searchInputInSelectALocation.sendKeys(Keys.ENTER);
			waitForSeconds(5);
			if (locationRowsInSelectLocation.size()>0) {
				WebElement firstRow = locationRowsInSelectLocation.get(index).findElement(By.cssSelector("input[type=\"checkbox\"]"));
				click(firstRow);
				scrollToElement(addBtn);
				click(addBtn);
				click(okBtnInCreateNewJobPage);
			}else
				SimpleUtils.report("Search location result is 0");

		}else
			SimpleUtils.fail("Select a location window load failed",true);
	}

	@FindBy(css=".lg-tabs__nav >.lg-tabs__nav-item:nth-child(4)")
	private WebElement dynamicGroupTabAftClickAddLocationBtn;
	@FindBy(css ="tr[ng-repeat=\"item in $ctrl.currentPageItems track by $index\"]")
	private List<WebElement> groupRows;
	@Override
	public void iCanSelectLocationsViaDynamicGroupInAddLocation(String searchText) throws Exception {
		if (isElementEnabled(selectALocationTitle,5)) {
			scrollToElement(dynamicGroupTabAftClickAddLocationBtn);
			click(dynamicGroupTabAftClickAddLocationBtn);
			if (groupRows.size()>0) {
				for (WebElement eachRow: groupRows) {
					WebElement groupNameInEachRow = eachRow.findElement(By.cssSelector("td:nth-child(2) div span"));
					WebElement checkBoxOfEachGroup = eachRow.findElement(By.cssSelector("td>div>input-field"));
					if (groupNameInEachRow.getText().equalsIgnoreCase(searchText) ||groupNameInEachRow.getText().contains(searchText)) {
						click(checkBoxOfEachGroup);
						scrollToElement(okBtnInCreateNewJobPage);
						click(okBtnInCreateNewJobPage);
						break;
					}else
						SimpleUtils.fail("No group mached to "+searchText,true);
				}
			}else
				createNewDynamicGroup(searchText,searchText,searchText,0);

		}else
			SimpleUtils.fail("Select a location window load failed",true);
	}
	@FindBy(css = "lg-button[label=\"Create Group\"]")
	private WebElement addDynamicGroupBtn;
	@FindBy(css = "fieldset.rule_container")
	private WebElement formOfCreateEditGroup;
	@FindBy(css = "lg-button[label=\"Test\"]")
	private  WebElement testBtn;
	@FindBy(css = "input[aria-label=\"Group Name\"]")
	private  WebElement groupNameInput;
	@FindBy(css = "input-field[value=\"$ctrl.dynamicGroup.description\"] >ng-form>input")
	private  WebElement groupDescriptionInput;
	@FindBy(css = "select.ng-pristine.ng-untouched.ng-valid")
	private  WebElement criteriaSelect;
	@FindBy(css = "lg-button[label=\"Add More\"]")
	private  WebElement addMoreBtn;
	@FindBy(css = "textarea[autocorrect = 'off']")
	private WebElement formulaInputBox;
	//@FindBy(css = "lg-picker-input[value=\"group.values\"]")
	@FindBy(css = "lg-multiple-select > div > lg-picker-input")
	private  WebElement criteriaValue;
	//@FindBy(css = "input-field[type=\"checkbox\"]")
	@FindBy(css ="div.select-list")
	private  List<WebElement> checkboxInCriteriaValue;
	@FindBy(css = "div.mappingLocation.mt-20 > span")
	private  WebElement testBtnInfo;
	@FindBy(css = "lg-button[label = 'Create']")
	private WebElement create;
	public void createNewDynamicGroup(String name,String distriction,String criteria,int index) throws Exception {

		if (isElementEnabled(addDynamicGroupBtn,5)) {
			click(addDynamicGroupBtn);
			if (isElementEnabled(formOfCreateEditGroup,5)) {
				groupNameInput.sendKeys(name);
				groupDescriptionInput.sendKeys(distriction);
				//selectByVisibleText(criteriaSelect,criteria);
				selectTheCriteria(criteria);
				if (!isElementEnabled(formulaInputBox)) {
					click(criteriaValue);
					click(getDriver().findElement(By.cssSelector("lg-picker-input > div > div > ng-transclude > div > div:nth-child(1) > input-field > ng-form>input")));
					click(criteriaValue);
					click(testBtn);
					String testInfo = testBtnInfo.getText().trim();
					click(create);
					waitForSeconds(3);
					if (groupRows.size()>0) {
						SimpleUtils.pass("Dynamic group create successfully");
					}else
						SimpleUtils.fail("Dynamic group create failed",false);

				}else
					formulaInputBox.sendKeys("Parent(1)");
				//click(okBtnInCreateNewJobPage);
				waitForSeconds(3);
			}else
				SimpleUtils.fail("Create or Edit Dynamic Group form load failed",false);
		}else
			SimpleUtils.fail("Add dynamic group button load failed",false);

	}
	@FindBy(css=".lg-tabs__nav >.lg-tabs__nav-item:nth-child(2)")
	private WebElement upperFieldTabAftClickAddLocationBtn;
	@FindBy(css = "input[placeholder=\"Search by upperfield name\"]")
	private WebElement searchTextOfUpperField;
	@Override
	public void iCanSelectUpperFieldByAddLocation(String searchText, int index) {
		if (isElementEnabled(selectALocationTitle,5)) {
			click(upperFieldTabAftClickAddLocationBtn);
			if (isElementEnabled(searchTextOfUpperField,5)) {
				SimpleUtils.pass("UpperField item in select a UpperField window show well");
				searchInputInSelectALocation.sendKeys(searchText);
				searchInputInSelectALocation.sendKeys(Keys.ENTER);
				waitForSeconds(5);
			}else
				SimpleUtils.fail("UpperField item in select a UpperField window load failed",false);

			if (locationRowsInSelectLocation.size()>0) {
				SimpleUtils.pass("Can select UpperField in select a location window");
				WebElement firstRow = locationRowsInSelectLocation.get(index).findElement(By.cssSelector("input[type=\"checkbox\"]"));
				clickTheElement(firstRow);
				clickTheElement(addBtn);
				clickTheElement(okBtnInCreateNewJobPage);
			}else
				SimpleUtils.report("Search location result is 0");

		}else
			SimpleUtils.fail("Select a location window load failed",true);
	}

	@Override
	public void createBtnIsClickable() {
		scrollToBottom();
		click(createBtn);
		SimpleUtils.pass("Job creation done");

		if (isElementEnabled(createNewJobBtn,5)) {
			SimpleUtils.pass("Create button is clickable and can enter select location page");
		}else
			SimpleUtils.fail("Create location button load failed",false);
	}

	@FindBy(css="tr[ng-repeat=\"job in searchFilteredJobs\"]:nth-of-type(even)")
	private List<WebElement> jobRows;

	@Override
	public void iCanSearchTheJobWhichICreated(String jobTitle) throws Exception {
		searchInputBox.clear();
		String[] searchJobCha = jobTitle.split(",");
		if (isElementLoaded(searchInputBox, 10) ) {
			for (int i = 0; i < searchJobCha.length; i++) {
				searchInputBox.sendKeys(searchJobCha[0]);
				searchInputBox.sendKeys(Keys.ENTER);
				waitForSeconds(3);
				if (jobRows.size()-1>0) {
					SimpleUtils.pass("Jobs: " + jobRows.size() + " job(s) found  "+ " by "+jobTitle);
					if (getDriver().findElement(By.cssSelector("table.lg-table >tbody> tr:nth-child(1)")).getText().trim().contains("Job Type")&&
						getDriver().findElement(By.cssSelector("table.lg-table >tbody> tr:nth-child(1)")).getText().trim().contains("Job Title") &&
							getDriver().findElement(By.cssSelector("table.lg-table >tbody> tr:nth-child(1)")).getText().trim().contains("Created By") &&
							getDriver().findElement(By.cssSelector("table.lg-table >tbody> tr:nth-child(1)")).getText().trim().contains("Date Created") &&
							getDriver().findElement(By.cssSelector("table.lg-table >tbody> tr:nth-child(1)")).getText().trim().contains("# of Locations") &&
							getDriver().findElement(By.cssSelector("table.lg-table >tbody> tr:nth-child(1)")).getText().trim().contains("Status") &&
							getDriver().findElement(By.cssSelector("table.lg-table >tbody> tr:nth-child(1)")).getText().trim().contains("Action")) {
						SimpleUtils.pass("On each row show Job Type, Name, Created By, Date Created, Status, Actions");
					}else
						SimpleUtils.fail("Search result table header load failed",false);
					break;
				} else {
					searchInputBox.clear();
				}
			}
			waitForSeconds(3);
		} else {
			SimpleUtils.fail("Search input is not clickable", true);
		}
	}

	@FindBy(css=".lg-sub-content-box-title")
	private List<WebElement> jobDetailsSubHeaders;
	@FindBy(css=".om-job-details h2")
	private WebElement jobDetails;
	@FindBy(css="sub-content-box[box-title=\"Job Details\"]")
	private WebElement jobDetailsBoxIn;
	@FindBy(css="sub-content-box[box-title=\"Week for job to take place\"]")
	private WebElement weekInfoBoxIn;
	@FindBy(css="sub-content-box[box-title=\"Create Schedule  Status\"]")
	private WebElement scheduleStatusBoxIn;
	@FindBy(css="sub-content-box[box-title=\"Notification\"]")
	private WebElement notificationBoxIn;
	@FindBy(css="lg-button[label=\"Export Result File\"]")
	private WebElement exportResultFileBtn;
	@FindBy(css="lg-button[label=\"Export Task Summary\"]")
	private WebElement exportResultSummaryBtn;


	@Override
	public void iCanGoToCreateScheduleJobDetailsPage(int index) {
		List details = new ArrayList<>();
		details.add("Job Details");
		details.add("Week for job to take place");
		details.add("Create Schedule Status");
		details.add("Locations Selected");
		details.add("Notification");
		if (jobRows.size() > 0) {
			List<WebElement> locationDetailsLinks = jobRows.get(index).findElements(By.cssSelector("button[type='button']"));
			click(locationDetailsLinks.get(index));
			waitForSeconds(5);
			for (int i = 0; i < jobDetailsSubHeaders.size(); i++) {
				if (jobDetailsSubHeaders.get(i).getText().contains(details.get(i).toString())
				) {
					SimpleUtils.pass("I can go to job details page successfully and the create schedule job details page show well");
					break;
				} else
					SimpleUtils.fail("Create schedule job details page load failed", false);
			}
		}else if (jobRows.size()==0)
			SimpleUtils.report("There are no jobs that match your criteria. ");
	}

	@Override
	public void iCanGoToReleaseScheduleJobDetailsPage(int index) {
		List details = new ArrayList<>();
		details.add("Job Details");
		details.add("Week for job to take place");
		details.add("Release Schedule Status");
		details.add("Locations Selected");
		details.add("Notification");
		if (jobRows.size()>0) {
			List<WebElement> locationDetailsLinks = jobRows.get(index).findElements(By.cssSelector("button[type='button']"));
			click(locationDetailsLinks.get(index));
			waitForSeconds(5);
			for (int i = 0; i <jobDetailsSubHeaders.size() ; i++) {
				if (jobDetailsSubHeaders.get(i).getText().contains(details.get(i).toString())
				) {
					SimpleUtils.pass("I can go to job details page successfully and the release schedule job details page show well");
					break;
				}else
					SimpleUtils.fail("Release schedule job details page load failed",false);
			}

		}else if (jobRows.size()==0)
			SimpleUtils.report("There are no jobs that match your criteria. ");
	}

	@Override
	public void iCanGoToAdjustBudgetJobDetailsPage(int index) {
		List details = new ArrayList<>();
		details.add("Job Details");
		details.add("Week for job to take place");
		details.add("Adjust Budget Status");
		details.add("Locations Selected");
		details.add("Notification");
		if (jobRows.size()>0) {
			List<WebElement> locationDetailsLinks = jobRows.get(index).findElements(By.cssSelector("button[type='button']"));
			click(locationDetailsLinks.get(index));
			waitForSeconds(5);
			for (int i = 0; i <jobDetailsSubHeaders.size() ; i++) {
				if (jobDetailsSubHeaders.get(i).getText().contains(details.get(i).toString())
				) {
					SimpleUtils.pass("I can go to job details page successfully and the adjust budget job details page show well");
					break;
				}else
					SimpleUtils.fail("Adjust budget job details page load failed",false);
			}

		}else if (jobRows.size()==0)
			SimpleUtils.report("There are no jobs that match your criteria. ");
	}

	@Override
	public void iCanGoToAdjustForecastJobDetailsPage(int index) {
		List details = new ArrayList<>();
		details.add("Job Details");
		details.add("Week for job to take place");
		details.add("Adjust Forecast Status");
		details.add("Locations Selected");
		details.add("Notification");
		if (jobRows.size()>0) {
			List<WebElement> locationDetailsLinks = jobRows.get(index).findElements(By.cssSelector("button[type='button']"));
			click(locationDetailsLinks.get(index));
			waitForSeconds(5);
			String adbcdf  = jobDetails.getText();
			for (int i = 0; i <jobDetailsSubHeaders.size() ; i++) {
				if (jobDetailsSubHeaders.get(i).getText().contains(details.get(i).toString())
				) {
					SimpleUtils.pass("I can go to job details page successfully and the adjust forecast job details page show well");
					break;
				}else
					SimpleUtils.fail("Adjust forecast job details page load failed",false);
			}

		}else if (jobRows.size()==0)
			SimpleUtils.report("There are no jobs that match your criteria. ");
	}

	@FindBy(css = "div[ng-click=\"gotoJobs()\"]")
	private WebElement backBtnInJobDetailsPage;
	@FindBy(css = "lg-button[label=\"Close\"]")
	private WebElement closeBtnInJobDetailsPage;
	@Override
	public void iCanBackToJobListPage() {
		if (isElementEnabled(backBtnInJobDetailsPage,3)) {
			click(backBtnInJobDetailsPage);
			waitForSeconds(3);
			if (isElementEnabled(createNewJobBtn,3)) {
				SimpleUtils.pass("I can back to job list page successfully via clicking back button in details page");
			}
		}else
			SimpleUtils.fail("Back button load failed",false);
	}

	@Override
	public void iCanClickCloseBtnInJobDetailsPage() {
		if (isElementEnabled(closeBtnInJobDetailsPage,5)) {
			scrollToBottom();
			click(closeBtnInJobDetailsPage);
			waitForSeconds(3);
			if (isElementEnabled(createNewJobBtn,3)) {
				SimpleUtils.pass("I can back to job list page successfully");
			}
		}else
			SimpleUtils.fail("Close button load failed",false);
	}

	@Override
	public void iCanDownloadExportResultFile() {
		if (isElementEnabled(exportResultFileBtn,5)) {
			click(exportResultFileBtn);
			waitForSeconds(10);
			String downloadPath = propertyMap.get("Download_File_Default_Dir");//when someone run ,need to change this path
			Assert.assertTrue(FileDownloadVerify.isFileDownloaded_Ext(downloadPath, "Task Summary"), "Download successfully");
		}else
			SimpleUtils.fail("Export result file button load failed ",false);
	}

	@Override
	public void iCanDownloadExportTaskSummary() {
		if (isElementEnabled(exportResultSummaryBtn,5)) {
			click(exportResultSummaryBtn);
			waitForSeconds(10);
			String downloadPath = propertyMap.get("Download_File_Default_Dir");//when someone run ,need to change this path
			Assert.assertTrue(FileDownloadVerify.isFileDownloaded_Ext(downloadPath, "Task Summary"), "Download successfully");
		}else
			SimpleUtils.fail("Export result summary button load failed ",false);
	}


	@Override
	public ArrayList<HashMap<String, String>> iCanGetJobInfo(String jobTitle) {
		ArrayList<HashMap<String,String>> jobInfo = new ArrayList<>();
		if (isElementEnabled(searchInputBox, 10)) {
			searchInputBox.clear();
			searchInputBox.sendKeys(jobTitle);
			searchInputBox.sendKeys(Keys.ENTER);
			waitForSeconds(5);
			if (jobRows.size()> 0) {
//				for (WebElement row : jobRows) {
					HashMap<String, String> jobInfoInEachRow = new HashMap<>();
					jobInfoInEachRow.put("jobType", jobRows.get(0).findElement(By.cssSelector("td:nth-child(2)")).getText());
					jobInfoInEachRow.put("jobTitle", jobRows.get(0).findElement(By.cssSelector("td:nth-child(3)>lg-button>button[type=\"button\"]")).getText());
					jobInfoInEachRow.put("createdBy", jobRows.get(0).findElement(By.cssSelector("td:nth-child(4)")).getText());
					jobInfoInEachRow.put("dateCreated", jobRows.get(0).findElement(By.cssSelector("td:nth-child(5) ")).getText());
					jobInfoInEachRow.put("#ofLocations", jobRows.get(0).findElement(By.cssSelector("td:nth-child(6) ")).getText());
					jobInfoInEachRow.put("status", jobRows.get(0).findElement(By.cssSelector("td:nth-child(7) ")).getText());
					jobInfoInEachRow.put("action", jobRows.get(0).findElement(By.cssSelector("td:nth-child(8) ")).getText());
					jobInfo.add(jobInfoInEachRow);
//				}
				return jobInfo;
			}else
				SimpleUtils.report("There is no data ");
		}

		return jobInfo;
	}


	public void unCheckFilters(ArrayList<WebElement> filterElements) {
		if (filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
			click(filterBtn);
		waitForSeconds(2);
		for (WebElement filterElement : filterElements) {
			WebElement filterCheckBox = filterElement.findElement(By.cssSelector("input[type=\"checkbox\"]"));
			String elementClasses = filterCheckBox.getAttribute("class").toLowerCase();
			if (elementClasses.contains("ng-not-empty"))
				click(filterElement);

		}
	}
	@Override
	public void filterJobsByJobTypeAndStatus() throws Exception {
		waitForSeconds(10);
		String jobTypeFilterKey = "jobtype";
		String statusFilterKey = "status";
		HashMap<String, ArrayList<WebElement>> availableFilters = getAvailableFilters();
		if (availableFilters.size() > 1) {
			ArrayList<WebElement> jobTypeFilters = availableFilters.get(jobTypeFilterKey);
			ArrayList<WebElement> statusFilters = availableFilters.get(statusFilterKey);
			for (WebElement statusFilter : statusFilters) {
				if (filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
				unCheckFilters(statusFilters);
				click(statusFilter);
				SimpleUtils.report("Data for job status: '" + statusFilter.getText() + "'");
				filterJobByJobType(jobTypeFilters);
			}
		} else {
			SimpleUtils.fail("Filters are not appears on job page!", false);
		}
	}

	@Override
	public void filterJobsByJobType() throws Exception {
		waitForSeconds(10);
		String jobTypeFilterKey = "jobtype";
		String statusFilterKey = "status";
		HashMap<String, ArrayList<WebElement>> availableFilters = getAvailableFilters();
		if (availableFilters.size() > 1) {
			ArrayList<WebElement> jobTypeFilters = availableFilters.get(jobTypeFilterKey);
			filterJobsByJobType(jobTypeFilters);
		} else {
			SimpleUtils.fail("Filters are not appears on job page!", false);
		}
	}

	@Override
	public void filterJobsByJobStatus() throws Exception {
		waitForSeconds(10);
		String jobTypeFilterKey = "jobtype";
		String statusFilterKey = "status";
		HashMap<String, ArrayList<WebElement>> availableFilters = getAvailableFilters();
		if (availableFilters.size() > 1) {
			ArrayList<WebElement> statusFilters = availableFilters.get(statusFilterKey);
			filterJobsByJobStatus(statusFilters);

		} else {
			SimpleUtils.fail("Filters are not appears on job page!", false);
		}
	}

	@Override
	public void filterClearFilterFunction() {

	}
	@FindBy(css = "lg-button[label=\"OK\"]")
	private WebElement okBtnInJobPopUpPage;
	@FindBy(css = "lg-button[label=\"Cancel\"]")
	private WebElement cancelBtnInJobPopUpPage;
	@FindBy(css = "lg-close[ng-if=\"!$ctrl.hideClose\"]")
	private WebElement closeBtnInJobPopUpPage;
	@Override
	public boolean verifyCreatNewJobPopUpWin() {
		if (isElementEnabled(okBtnInJobPopUpPage,5) && isElementEnabled(cancelBtnInJobPopUpPage,5) &&
				isElementEnabled(closeBtnInJobPopUpPage,5)) {
			return true;
		}
		return false;
	}

	@Override
	public void iCanCloseJobCreatePopUpWindowByCloseBtn() {
		if (isElementEnabled(closeBtnInJobPopUpPage,5)) {
			click(closeBtnInJobPopUpPage);
			if (isElementEnabled(createNewJobBtn,5) ) {
				SimpleUtils.pass("Can close job pop up page by close button");
			}

		}else
			SimpleUtils.fail("Close job pop up page failed",false);
	}

	@Override
	public void iCanCancelJobCreatePopUpWindowByCancelBtn() {
		if (isElementEnabled(cancelBtnInJobPopUpPage,5)) {
			click(cancelBtnInJobPopUpPage);
			if (isElementEnabled(createNewJobBtn,5) ) {
				SimpleUtils.pass("Can close job pop up page by close button");
			}

		}else
			SimpleUtils.fail("Close job pop up page failed",false);
	}

	@Override
	public void iCanCancelJobInJobCreatPageByCancelBtn() {
		if (isElementEnabled(cancelBtnInJobPopUpPage,5)) {
			scrollToBottom();
			click(cancelBtnInJobPopUpPage);
			if (isElementEnabled(createNewJobBtn,5) ) {
				SimpleUtils.pass("Can close job pop up page by close button");
			}

		}else
			SimpleUtils.fail("Close job pop up page failed",false);
	}
	@FindBy(css = "input[type=\"number\"]")
	private WebElement daysBeforeRelease;
	@FindBy(css = "select[ng-attr-id=\"{{$ctrl.inputName}}\"]")
	private WebElement releaseTimeSelector;
	@FindBy(css = "input[aria-label=\"Also create and release schedules for locations that do not have schedules created yet\"]")
	private WebElement createAndReleaseCheckbox;
	@Override
	public void iCanSetUpDaysBeforeRelease(String releaseDay) {
		if (isElementEnabled(daysBeforeRelease,5) ) {
			daysBeforeRelease.clear();
			daysBeforeRelease.sendKeys(releaseDay);
			SimpleUtils.pass("Release schedule " +releaseDay+" days before the Schedule week");
		}else
			SimpleUtils.fail("Days before release load failed",false);
	}

	@Override
	public void iCanSetUpTimeOfRelease(String timeForRelease) throws Exception {
		if (isElementEnabled(releaseTimeSelector,5) ) {
			selectByVisibleText(releaseTimeSelector,timeForRelease);
			SimpleUtils.pass("Release schedule at " +timeForRelease+" o'clock");
		}else
			SimpleUtils.fail("Time for  release load failed",false);
	}

	@Override
	public void iCanClickOnCreatAndReleaseCheckBox() {
		if (isElementEnabled(createAndReleaseCheckbox,15)) {
			click(createAndReleaseCheckbox);
			SimpleUtils.pass("Click on Also create and release schedules for locations that do not have schedules created yet successfully" );
		}else
			SimpleUtils.fail("Also create and release schedules for locations that do not have schedules created yet check box load failed",false);
	}


	public HashMap<String, ArrayList<WebElement>> getAvailableFilters() {
		HashMap<String, ArrayList<WebElement>> jobFilters = new HashMap<String, ArrayList<WebElement>>();
		try {
			if (isElementLoaded(filterBtn,10)) {
				if (filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
					click(filterBtn);
				for (WebElement filterElementInJob : filterElementsInJob) {
					WebElement filterLabel = filterElementInJob.findElement(By.className("lg-filter__category-label"));
					String filterType = filterLabel.getText().toLowerCase().replace(" ", "");
					List<WebElement> filters = filterElementInJob.findElements(By.cssSelector("input-field[type=\"checkbox\"]"/*"[ng-repeat=\"opt in opts\"]"*/));
					ArrayList<WebElement> filterList = new ArrayList<WebElement>();
					for (WebElement filter : filters) {
						filterList.add(filter);
					}
					jobFilters.put(filterType, filterList);
				}
			} else {
				SimpleUtils.fail("Filters button not found on Jobs page!", false);
			}
		} catch (Exception e) {
			SimpleUtils.fail("Filters button not loaded successfully on jobs page!", true);
		}
		return jobFilters;
	}



	public HashMap<String, Float> getSummaryComplateInprogressAndNotStartNum() throws Exception {
		HashMap<String, Float> summaryComplateInprogressAndNotStartNums = new HashMap<String, Float>();
		SmartCardPage smartCardPage = new ConsoleSmartCardPage();
		WebElement smartCardElement = MyThreadLocal.getDriver().findElement(By.xpath("//div[@class='card-carousel-card card-carousel-card-primary card-carousel-card-table ']"));
		if (isElementLoaded(smartCardElement,5)) {
			String sumarySmartCardText = smartCardElement.getText();
			String[] complateInprogressAndNotStartNums = sumarySmartCardText.split("\n");
			for (String complateInprogressAndNotStartNum: complateInprogressAndNotStartNums) {

				if(complateInprogressAndNotStartNum.toLowerCase().contains(jobSummarySmartCardData.jobsCompleted.getValue().toLowerCase()))
				{
					summaryComplateInprogressAndNotStartNums = smartCardPage.updateScheduleHoursAndWages(summaryComplateInprogressAndNotStartNums , complateInprogressAndNotStartNum.split(" ")[1],
							"ComplatedNum");
				}
				else if(complateInprogressAndNotStartNum.toLowerCase().contains(jobSummarySmartCardData.jobsInProgress.getValue().toLowerCase()))
				{
					summaryComplateInprogressAndNotStartNums = smartCardPage.updateScheduleHoursAndWages(summaryComplateInprogressAndNotStartNums , complateInprogressAndNotStartNum.split(" ")[1]
							.replace("$", ""), "InProgressNum");
				}
				else if(complateInprogressAndNotStartNum.toLowerCase().contains(jobSummarySmartCardData.notStarted.getValue().toLowerCase()))
				{
					summaryComplateInprogressAndNotStartNums = smartCardPage.updateScheduleHoursAndWages(summaryComplateInprogressAndNotStartNums , complateInprogressAndNotStartNum.split(" ")[1]
							.replace("$", ""), "NotStartNum");
				}
			}
		}else {
			SimpleUtils.fail("there is no summary smart card",false);
		}
		return summaryComplateInprogressAndNotStartNums;
	}


	public void filterJobsByJobType(ArrayList<WebElement> jobTypeFilters) throws Exception {

		for (WebElement jobTypeFilter : jobTypeFilters) {
			try {
				Thread.sleep(1000);
				if (filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
					click(filterBtn);
				unCheckFilters(jobTypeFilters);
				String jobType = jobTypeFilter.getText();
				click(jobTypeFilter);
				SimpleUtils.report("Data for Job Type: '" + jobType + "'" +" selected");
				click(filterBtn);
				if (jobRows.size()<0) {
					SimpleUtils.report("There is no data with this filter: " + jobTypeFilter );
				}else
					SimpleUtils.pass("Jobs: " + jobRows.size() + " job(s) found  ");

			} catch (Exception e) {
				SimpleUtils.fail("Unable to get data", true);
			}
			}
		unCheckFilters(jobTypeFilters);
		}
     //this is for job type combination filter
	public void filterJobByJobType(ArrayList<WebElement> jobTypeFilters) throws Exception {

		for (WebElement jobTypeFilter : jobTypeFilters) {
			try {
				Thread.sleep(1000);
				if (filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
					click(filterBtn);
				unCheckFilters(jobTypeFilters);
				String jobType = jobTypeFilter.getText();
				click(jobTypeFilter);
				SimpleUtils.report("Data for Job Type: '" + jobType + "'" +" selected");
				click(filterBtn);
				if (jobRows.size()<0) {
					SimpleUtils.report("There is no data with this filter: " + jobTypeFilter );
				}else
					SimpleUtils.pass("Jobs: " + jobRows.size() + " job(s) found  ");

			} catch (Exception e) {
				SimpleUtils.fail("Unable to get data", true);
			}
		}
	}

	public void filterJobsByJobStatus(ArrayList<WebElement> statusFilters) throws Exception {

		for (WebElement jobTypeFilter : statusFilters) {
			try {
				Thread.sleep(1000);
				if (filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
					click(filterBtn);
				unCheckFilters(statusFilters);
				String jobType = jobTypeFilter.getText();
				click(jobTypeFilter);
				SimpleUtils.report("Data for Job Type: '" + jobType + "'" +" selected");
				click(filterBtn);
				if (jobRows.size()<0) {
					SimpleUtils.report("There is no data with this filter: " + jobTypeFilter );
				}else
					SimpleUtils.pass("Jobs: " + jobRows.size() + " job(s) found  ");

			} catch (Exception e) {
				SimpleUtils.fail("Unable to get data", true);
			}
		}
		unCheckFilters(statusFilters);

	}
		@FindBy(css="span[ng-click=\"applyAction(5,job)\"]")
		private WebElement copyBtnInJobListPage;
		@FindBy(css="span[ng-click=\"applyAction(6,job)\"]")
		private WebElement archiveBtnInJobListPage;
		@FindBy(css="span[ng-click=\"applyAction(7,job)\"]")
		private WebElement stopBtnInJobListPage;
		@FindBy(css=".lgn-alert-modal")
		private WebElement stopJobPopUpWins;
		@FindBy(css=".lgn-alert-title")
		private WebElement stopJobPopUpWinsWarningTitle;
		@FindBy(css=".lgn-alert-message")
		private WebElement stopJobPopUpWinsWarningDesc;
		@FindBy(css="div.cancel-button > div > button")
		private WebElement cancelBtnInStopJobPopUpWins;
		@FindBy(css="div.ok-button > div > button")
		private WebElement confirmBtnInStopJobPopUpWins;
		@FindBy(css="span[ng-click=\"applyAction(2,job)\"]")
		private WebElement resumeBtnInJobListPage;
		@Override
		public void iCanStopJob(String jobTitle) {
			if (isElementEnabled(stopBtnInJobListPage,5) ) {
				clickTheElement(stopBtnInJobListPage);
				if (verifyJobActionsWarningPageShowWell()) {
					clickTheElement(confirmBtnInStopJobPopUpWins);
					waitForSeconds(20);
					ArrayList<HashMap<String,String>> jobInfo = iCanGetJobInfo(jobTitle);
					if (jobInfo.size()>0 && jobInfo.get(0).get("status").equalsIgnoreCase("Stopped")) {
						SimpleUtils.pass("The job:" +jobTitle + " was stopped successfully");
					}else
						SimpleUtils.fail("The job:" +jobTitle + " failed to stop",false);
					}
			}else
				SimpleUtils.report("There is no stop button and the job is not in progress");
		}
		@FindBy(css=".modal-content")
		private WebElement copyJobPopUpWins;
		@FindBy(css=".lg-modal__content > div:nth-child(1)")
		private WebElement descForTypeOfJobCreating;
		@FindBy(css=".lg-modal__content > div:nth-child(3)")
		private WebElement descForWeekOfJobCreating;
		@FindBy(css="lg-button[label=\"Cancel\"]")
		private WebElement cancelBtnInCopyJobPopUpWins;
		@FindBy(css="lg-button[label=\"Create\"]")
		private WebElement createBtnInCopyJob;
		@Override
		public void iCanCopyJob(String jobTitle) throws Exception {
			ArrayList<HashMap<String,String>> jobInfoBeforeCopy = iCanGetJobInfo(jobTitle);
			if (isElementEnabled(copyBtnInJobListPage,5) ) {
				clickTheElement(copyBtnInJobListPage);
				if (verifyJobCopyPopUpWinsShowWell()) {
					selectWeekForJobToTakePlace();
					click(okBtnInCreateNewJobPage);
					scrollToBottom();
					click(createBtnInCopyJob);
					ArrayList<HashMap<String,String>> jobInfoAfterCopy = iCanGetJobInfo(jobTitle);
					if (!jobInfoAfterCopy.get(0).get("jobTitle").equalsIgnoreCase(jobInfoBeforeCopy.get(0).get("jobTitle"))) {
						SimpleUtils.pass("Job was copied successfully");
					}
				}
			}else {
				SimpleUtils.fail("Copy button load failed",false);
			}

		}

	private boolean verifyJobActionsWarningPageShowWell() {
		if (isElementEnabled(stopJobPopUpWins,5)) {
			if (stopJobPopUpWinsWarningTitle.getText().contains("Are you sure you want to") &&
					stopJobPopUpWinsWarningDesc.getText().contains("Please confirm that you want to perform the above action.")
			&& isElementEnabled(cancelBtnInStopJobPopUpWins,5) && isElementEnabled(confirmBtnInStopJobPopUpWins,5)) {
				SimpleUtils.pass("Job pop up window for each action show well");
				waitForSeconds(2);
				return true;
			}
		}else
			SimpleUtils.fail("Job pop up window load failed",false);
			return false;
	}

	private boolean verifyJobCopyPopUpWinsShowWell() {
		if (isElementEnabled(copyJobPopUpWins,5)) {
			if (descForTypeOfJobCreating.getText().contains("What type of job are you creating") &&
					descForWeekOfJobCreating.getText().contains("Select week for job to take place")
					&& isElementEnabled(cancelBtnInCopyJobPopUpWins,5) && isElementEnabled(okBtnInCreateNewJobPage,5)) {
				SimpleUtils.pass("Copy job pop up window for each action show well");
				return true;
			}
		}else
			SimpleUtils.fail("Copy job  pop up window load failed",false);
		return false;
	}

	@Override
		public void iCanResumeJob(String jobTitle) throws Exception {
			waitForSeconds(5);
		if (isElementEnabled(resumeBtnInJobListPage,5) ) {
			clickTheElement(resumeBtnInJobListPage);
			if (verifyJobActionsWarningPageShowWell()) {
				clickTheElement(confirmBtnInStopJobPopUpWins);
				iCanSearchTheJobWhichICreated(jobTitle);
				if (isElementEnabled(stopBtnInJobListPage,5)) {
					SimpleUtils.pass("Job was resumed successfully");
				}
			}
		}else {
			SimpleUtils.report("Resume button load failed or the status of job has completed");
		}
	}

		@Override
		public void iCanArchiveJob(String jobTitle) throws Exception {
			if (isElementEnabled(archiveBtnInJobListPage,5) ) {
				clickTheElement(archiveBtnInJobListPage);
				if (verifyJobActionsWarningPageShowWell()) {
					clickTheElement(confirmBtnInStopJobPopUpWins);
					if (isElementEnabled(searchInputBox, 10)) {
						searchInputBox.clear();
						searchInputBox.sendKeys(jobTitle);
						searchInputBox.sendKeys(Keys.ENTER);
						waitForSeconds(5);
						if (jobRows.size() <= 0) {
							SimpleUtils.pass("The job:" +jobTitle + " was archived successfully");
						}else
							SimpleUtils.fail("The job:" +jobTitle + " failed to archive",false);
					}
				}
			}else {
				SimpleUtils.fail("Archive button load failed",false);
			}
		}

	@FindBy(css = "select[ng-attr-aria-label=\"{{$ctrl.label}}\"]")
	private WebElement pageNumSelector;
	@FindBy(css = ".lg-pagination__pages.ng-binding")
	private WebElement pageNumberText;
	@FindBy (css = ".lg-pagination__arrow--left")
	private WebElement pageLeftBtnInDistrict;
	@FindBy (css = ".lg-pagination__arrow--right")
	private WebElement pageRightBtnInDistrict;
	@Override
	public void verifyPaginationFunctionInJob() throws Exception {
		if (isElementLoaded(pageNumSelector,3)) {
			int minPageNum = 1;
			String iniPageText = pageNumberText.getText().trim();
			String[] maxPageNumberOri = iniPageText.split("of");
			int maxPageNumber = Integer.valueOf(maxPageNumberOri[1].trim());
			if (maxPageNumber == minPageNum && pageLeftBtnInDistrict.getAttribute("class").contains("disabled")
					&& pageRightBtnInDistrict.getAttribute("class").contains("disabled")) {
				SimpleUtils.pass("There is only one page");
			}else {
				for (int i = 1; i <= Integer.valueOf(maxPageNumber); i++) {
					selectByVisibleText(pageNumSelector,String.valueOf(i));
					if (i <= Integer.valueOf(maxPageNumber)) {
						SimpleUtils.pass("Page Select work well");
					}else
						SimpleUtils.fail("Page select doesn't work",true);
				}

				String firstLineText = jobRows.get(0).getText();
				click(pageLeftBtnInDistrict);
				String firstLineTextAftLeft = jobRows.get(0).getText();
				if (!firstLineTextAftLeft.equalsIgnoreCase(firstLineText) ) {
					SimpleUtils.pass("Left pagination button work well" );
				}else
					SimpleUtils.fail("Left pagination button work wrong",false);
				click(pageRightBtnInDistrict);
				String firstLineTextAftRight = jobRows.get(0).getText();
				if (!firstLineTextAftRight.equalsIgnoreCase(firstLineTextAftLeft) ) {
					SimpleUtils.pass("Right pagination button work well");
				}else
					SimpleUtils.fail("Right pagination button work wrong",false);

			}

		}else
			SimpleUtils.fail("Page select load failed",true);

	}
	@FindBy(css = "om-job-details__title")
	private WebElement jobDetailsTitle;


	@FindBy(css = "sub-content-box[box-title=\"Budget Adjustment\"]")
	private WebElement subContentBudgetAdjustment;
	@FindBy(css = "input-field[ng-attr-disabled=\"adjustBudgetDisabled\"]>ng-form>div.select-wrapper>select")
	private List<WebElement> decreaseSelector;
	@FindBy(css = "input[type=\"number\"]")
	private WebElement budgetAdjustNum;
	@FindBy(css = "sub-content-box[box-title=\"What tasks would you like to apply this budget to?\"]")
	private WebElement subContentTask;
	@FindBy(css = "lg-button[label=\"Add Task\"]")
	private WebElement addTaskButton;
	@FindBy(css = "sub-content-box[box-title=\"What work roles would you like to apply this budget to?\"]")
	private WebElement subContentWorkRole;
	@FindBy(css = "lg-button[label=\"Add Work Role\"]")
	private WebElement addWorkRoleButton;
	@FindBy(css="tr[ng-repeat=\"task in filteredTasks track by $index\"]")
	private List<WebElement> taskRowsInAddTasks;
	@FindBy(css = "input[placeholder=\"Search by task...\"]")
	private WebElement searchTaskInputBox;
	@FindBy(css = "modal[modal-title=\"Add Task For Adjust Budget\"]")
	private WebElement addTaskForAdjustBudgetPopUp;
	@FindBy(css = "lg-button[label=\"OK\"]")
	private WebElement okBtnInAddTaskForAdjustBudgetPopUp;

	@FindBy(css="tr[ng-repeat=\"wr in filteredWorkRoles\"]")
	private List<WebElement> workRoleRowsInAddWorkRoles;
	@FindBy(css = "modal[modal-title=\"Add Work Role For Adjust Budget\"]")
	private WebElement addWorkRolesTitleForAdjustBudgetPopUp;
	@FindBy(css = "input[placeholder=\"Search by work role...\"]")
	private WebElement searchWorkRolesInputBox;

	//adjust budget confirmation
	@FindBy(css = "modal[modal-title=\"Adjust Budget Confirmation\"]")
	private WebElement adjustBudgetConfirmationTitle;
	@FindBy(css = "lg-button[label=\"Execute\"]")
	private WebElement executeButton;

	@Override
	public boolean verifyLayoutOfAdjustBudget() {
		if (isElementEnabled(jobTitleInputBox,5)
		&& isElementEnabled(jobCommentsInputBox,5) && isElementEnabled(addLocationBtn,5)
		&& isElementEnabled(subContentBudgetAdjustment,5) &&  areListElementVisible(decreaseSelector,5)
				&& isElementEnabled(subContentTask,5) && isElementEnabled(subContentWorkRole,5)
		) {
			SimpleUtils.pass("Adjust budget page show well");
			return true;
		}
		return false;
	}

	@Override
	public void iCanSetUpBudgetAssignmentNum(String budgetAssignmentNum) {

		if (isElementEnabled(budgetAdjustNum,5)) {
			budgetAdjustNum.clear();
			budgetAdjustNum.sendKeys(budgetAssignmentNum);
		}else
			SimpleUtils.fail("Adjust budget input box load failed",false);

	}

	@Override
	public void addTaskButtonIsClickable() {
		if ( isElementEnabled(addTaskButton,5)) {
			click(addTaskButton);
			if (areListElementVisible(taskRowsInAddTasks,5)) {
				SimpleUtils.pass("Add task button in adjust budget is clickable");
			}else
				SimpleUtils.fail("Add task button load failed",false);
		}

	}

	@Override
	public void iCanAddTasks(String searchText, int index, String taskName) {
		waitForSeconds(3);
		if (isElementEnabled(addTaskForAdjustBudgetPopUp,5)) {
			searchTaskInputBox.sendKeys(taskName);
			searchTaskInputBox.sendKeys(Keys.ENTER);
			waitForSeconds(5);
			if (taskRowsInAddTasks.size()>0) {
				WebElement firstRow = taskRowsInAddTasks.get(index).findElement(By.cssSelector("input[type=\"checkbox\"]"));
				click(firstRow);
				click(okBtnInCreateNewJobPage);
			}else
				SimpleUtils.report("Search task result is 0");

		}else
			SimpleUtils.fail("Select a task window load failed",true);
	}

	@Override
	public void addWorkRoleButtonIsClickable() {
		if ( isElementEnabled(addWorkRoleButton,5)) {
			click(addWorkRoleButton);
			if (isElementEnabled(addWorkRolesTitleForAdjustBudgetPopUp,5)) {
				SimpleUtils.pass("Add work role button in adjust budget is clickable");
			}else
				SimpleUtils.fail("Add work role button load failed",false);
		}
	}

	@Override
	public void iCanAddWorkRoles(String searchText, int index, String workRole) {
		if (isElementEnabled(addWorkRoleButton,5)) {
			searchWorkRolesInputBox.sendKeys(workRole);
			searchWorkRolesInputBox.sendKeys(Keys.ENTER);
			waitForSeconds(5);
			if (workRoleRowsInAddWorkRoles.size()>0) {
				WebElement firstRow = workRoleRowsInAddWorkRoles.get(index).findElement(By.cssSelector("input[type=\"checkbox\"]"));
				click(firstRow);
				click(okBtnInCreateNewJobPage);
			}else
				SimpleUtils.report("Search work role result is 0");

		}else
			SimpleUtils.fail("Select work role window load failed",true);
	}

	@Override
	public void executeBtnIsClickable() {
		scrollToBottom();
		click(createBtn);
		if ( isElementEnabled(executeButton,5)) {
			click(executeButton);
			if (isElementEnabled(createNewJobBtn,10)) {
				SimpleUtils.pass("Execute button in adjust budget is clickable");
			}else
				SimpleUtils.fail("Execute button load failed",false);
		}
	}

	@Override
	public void createBtnIsClickableInAdjustBudgetJob() throws Exception {
			scrollToBottom();
		if (isElementLoaded(createBtn,10)){
			click(createBtn);
			if(isElementLoaded(executeButton, 5)) {
				SimpleUtils.pass("Create button in adjust budget job is clickable");
			}
		}else{
			SimpleUtils.fail("Create button in adjust budget job load failed",false);
		}
	}


	@FindBy(css = "modal[modal-title=\"Adjust Budget Confirmation\"]")
	private WebElement adjustBudgetPopUp;
	@Override
	public void verifyAdjustBudgetConfirmationPage(String jobTitle, String budgetAssignmentNum, String taskName, String workRole) {
		String confirmationText = adjustBudgetConfirmationTitle.getText();

		if (isElementEnabled(adjustBudgetConfirmationTitle,5)) {
			if (confirmationText.contains(jobTitle)&& confirmationText.contains(budgetAssignmentNum)&&
			confirmationText.contains(taskName)&& confirmationText.contains(workRole) && isElementEnabled(cancelBtnInJobPopUpPage,5)
			&& isElementEnabled(executeButton,3)) {
				SimpleUtils.pass("Adjust budget confirmation page show well");
			}
		}else
			SimpleUtils.fail("Adjust budget confirmation page load failed",false);
				
	}

	@Override
	public void cancelBthInAdjustBudgetConfirmationPageIsClickable(){
		if (isElementEnabled(cancelBtnInJobPopUpPage,3)) {
			click(cancelBtnInCopyJobPopUpWins);
			if (isElementEnabled(createBtn,3)) {
				SimpleUtils.pass("Cancel button in adjust budget confirmation page is clickable");
			}
		}else
			SimpleUtils.fail("Cancel button in adjust budget confirmation page load failed",false);

	}

	@FindBy(css = "modal[modal-title=\"Adjust Forecast Confirmation\"]")
	private WebElement adjustForecastConfirmation;
	@FindBy(css = "div[data-content=\"Decrease\"]>select")
	private WebElement directionChoicesSelector;
	@FindBy(css = "input-field[value=\"job.forecastCategoryType\"]>ng-form>div:nth-child(3)>select")
	private WebElement categoryTypeSelector;
	@FindBy(css = "input-field[value=\"job.adjustmentValue\"]>ng-form>input")
	private WebElement adjustmentValueInput;
	@FindBy(css = "input-field[options=\"adjustmentTypes\"]>ng-form>div:nth-child(3)>select")
	private WebElement adjustmentTypeSelector;

	@Override
	public boolean verifyLayoutOfAdjustForecast() {
		if (isElementEnabled(jobTitleInputBox,5)
				&& isElementEnabled(jobCommentsInputBox,5) && isElementEnabled(addLocationBtn,5)
				&& isElementEnabled(directionChoicesSelector,5) &&  isElementEnabled(categoryTypeSelector,5)
				&& isElementEnabled(adjustmentValueInput,5) && isElementEnabled(adjustmentTypeSelector,5)
		) {
			SimpleUtils.pass("Adjust forecast page show well");
			return true;
		}
		return false;
	}

	@Override
	public void selectDirectionChoices(String directionChoices) throws Exception {
		if (isElementEnabled(directionChoicesSelector,5)) {
			selectByVisibleText(directionChoicesSelector,directionChoices);
			SimpleUtils.pass("Direction Choices is " + directionChoices);
		}else
			SimpleUtils.fail("Direction Choices in adjust forecast  load failed",false);
	}

	@Override
	public void selectCategoryTypes(String categoryType) throws Exception {
		if (isElementEnabled(categoryTypeSelector,5)) {
			selectByVisibleText(categoryTypeSelector,categoryType);
			SimpleUtils.pass("Direction Choices is " +categoryType);
		}else
			SimpleUtils.fail("Category Type Selector in adjust forecast  load failed",false);
	}

	@Override
	public void inputAdjustmentValue(String adjustmentValue) {
		if (isElementEnabled(adjustmentValueInput,5)) {
			adjustmentValueInput.clear();
			adjustmentValueInput.sendKeys(adjustmentValue);
			SimpleUtils.pass("Adjustment Value is " +adjustmentValue);
		}else
			SimpleUtils.fail("Adjustment Value in adjust forecast  load failed",false);
	}

	@Override
	public void selectAdjustmentType(String adjustmentType) throws Exception {
		if (isElementEnabled(adjustmentTypeSelector,5)) {
			selectByVisibleText(adjustmentTypeSelector,adjustmentType);
			SimpleUtils.pass("Adjustment Type is " +adjustmentType);
		}else
			SimpleUtils.fail("Adjustment Type selector in adjust forecast  load failed",false);
	}

	@Override
	public void verifyAdjustForecastConfirmationPage(String jobTitle, String adjustmentValue, String directionChoices, String categoryType, String searchTaskText) {
		String confirmationText = adjustForecastConfirmation.getText();

		if (isElementEnabled(adjustForecastConfirmation,5)) {
			if (confirmationText.contains(jobTitle)&& confirmationText.contains(adjustmentValue)&&
					confirmationText.contains(directionChoices.toLowerCase())&& confirmationText.contains(categoryType) && confirmationText.contains(searchTaskText) &&isElementEnabled(cancelBtnInJobPopUpPage,5)
					&& isElementEnabled(executeButton,3)) {
				SimpleUtils.pass("Adjust forecast confirmation page show well");
			}
		}else
			SimpleUtils.fail("Adjust forecast confirmation page load failed",false);

	}

	@Override
	public void verifyExportResultFunction() {
		scrollToBottom();
		if (isElementEnabled(exportResultFileBtn,3) ) {
			click(exportResultFileBtn);
			SimpleUtils.pass("Export Result File button is clickable and can download file");
		}else
			SimpleUtils.fail("Export Result File button load failed",false);

	}

	@Override
	public void verifyExportTaskSummaryFunction() {
		if (isElementEnabled(exportResultSummaryBtn,3) ) {
			click(exportResultSummaryBtn);
			SimpleUtils.pass("Export Task Summary button is clickable and can download file");
		}else
			SimpleUtils.fail("Export Task Summary button load failed",false);

	}

	@FindBy(css = ".lg-tabs__nav")
	private WebElement navForAddLocation;
	@Override
	public void verifyDynamicGroupName() throws Exception {
		if (isElementLoaded(navForAddLocation, 10)){
			if (navForAddLocation.getText().contains("Dynamic Location Group")){
				SimpleUtils.pass("Dynamic Group name is expected!");
			} else {
				SimpleUtils.fail("Dynamic Group name is not expected! actual is: "+ navForAddLocation.getText(), false);
			}
		} else {
			SimpleUtils.fail("Navigation tab are not loaded!", false);
		}
	}

	@FindBy(css = "select.ng-pristine")
	private WebElement jobTypeSelect;
	@FindBy(css ="div.calendar-body > div:nth-child(7)")
	private WebElement lastWeek;
	@FindBy(css = "nav.lg-tabs__nav > div:nth-child(4)")
	private WebElement dynamicGroup;
	@FindBy(css = "lg-button[label = 'Create Group']")
	private WebElement createGroupBtn;

	public void verifyDynamicGroupDisplayInSpecifyJobType(String type) throws Exception{
		click(createNewJobBtn);
		jobTypeSelect.sendKeys(type);
		lastWeek.click();
		click(okBtnInCreateNewJobPage);
		click(addLocationBtn);
		verifyDynamicGroupName();
		click(dynamicGroup);
		if(isElementLoaded(createGroupBtn,5)){
			SimpleUtils.pass("Dynamic group load successfully");
		}else
			SimpleUtils.fail("Dynamic group load failed",false);
		click(cancelBtnInJobPopUpPage);
		click(backBtnInJobDetailsPage);
	}

	@FindBy(css = "lg-button[label = 'Remove']")
	private WebElement remove;
	public void createDynamicGroup(String type) throws Exception{
		click(createNewJobBtn);
		jobTypeSelect.sendKeys(type);
		lastWeek.click();
		click(okBtnInCreateNewJobPage);
		click(addLocationBtn);
		verifyDynamicGroupName();
		click(dynamicGroup);
		SimpleDateFormat dfs = new SimpleDateFormat("MMddHHmmss ");
		String currentDisTime = dfs.format(new Date());
		createNewDynamicGroup("AutoCreate" + currentDisTime,"AutoCreateDescription","City",0);
		click(getDriver().findElement(By.cssSelector("td>div>input-field")));
		removeFirstDynamicGroup();
	}

	@FindBy(css = "div:nth-child(4) > div.condition_line > div > i")
	private WebElement closeIcon;

	public void addWorkforceSharingDGWithMutiplyCriteria() throws Exception {
		String jobName = createNewJob("Create Schedule");
		String testInfo = "";
		click(createNewJobBtn);
		jobTypeSelect.sendKeys("Adjust Forecast");
		lastWeek.click();
		click(okBtnInCreateNewJobPage);
		waitForSeconds(2);
		click(addLocationBtn);
		verifyDynamicGroupName();
		verifyNoDistrictInTitle();

		verifyRecentJobLocation(jobName);

		click(dynamicGroup);

		verifyPageFunction();

		verifyExistingGroupDetails();

		click(addDynamicGroupBtn);

		verifyCriteriaList();

		groupNameInput.sendKeys("RecentLocation");
		selectTheCriteria("Location Name");
		click(criteriaValue);
		searchValue("OMLocation16 -NO touch!!!");
		waitForSeconds(2);
		click(getDriver().findElement(By.cssSelector("input-field[label = 'OMLocation16 -NO touch!!!']>ng-form>input")));
		click(criteriaValue);
		click(create);
		click(getDriver().findElement(By.cssSelector("td>div>input-field")));
		click(remove);
		click(remove);

		click(addDynamicGroupBtn);
		groupNameInput.sendKeys("AutoCreateMutiply");
		selectTheCriteria("District");
		click(criteriaValue);
		click(getDriver().findElement(By.cssSelector("lg-picker-input > div > div > ng-transclude > div > div:nth-child(1) > input-field > ng-form>input")));
		click(criteriaValue);

		click(addMoreBtn);
		click(getDriver().findElement(By.cssSelector("div:nth-child(4) > div.condition_line > lg-cascade-select > lg-select > div > lg-picker-input > div > input-field")));
		click(getDriver().findElement(By.cssSelector("div:nth-child(4) > div.condition_line > lg-cascade-select > lg-select > div > lg-picker-input > div > div > ng-transclude > lg-search-options > div > div > div:nth-child(5) > div")));
		click(getDriver().findElement(By.cssSelector("div:nth-child(4) > div.condition_line > lg-cascade-select > lg-cascade-select > lg-multiple-select > div > lg-picker-input > div > input-field")));
		click(getDriver().findElement(By.cssSelector("div:nth-child(4) > div.condition_line > lg-cascade-select > lg-cascade-select > lg-multiple-select > div > lg-picker-input > div > div > ng-transclude > div > div:nth-child(1) > input-field")));

		click(addMoreBtn);
		click(getDriver().findElement(By.cssSelector("div:nth-child(5) > div.condition_line > lg-cascade-select > lg-select > div > lg-picker-input > div > input-field")));
		click(getDriver().findElement(By.cssSelector("div:nth-child(5) > div.condition_line > lg-cascade-select > lg-select > div > lg-picker-input > div > div > ng-transclude > lg-search-options > div > div > div:nth-child(10) > div")));
		scrollToElement(formulaInputBox);
		System.out.println(getDriver().findElement(By.cssSelector("div.CodeMirror")).getAttribute("innerText"));
		if(getDriver().findElement(By.cssSelector("div.CodeMirror")).getAttribute("innerText").contains("Enter your expression. The dynamic location group will only be created if the expresion evaluates to be true.")){
			SimpleUtils.pass("Custom formula description info is correct");
		}else
			SimpleUtils.fail("Custom formula description info is wrong",false);
		formulaInputBox.sendKeys("Parent(1)");

		click(create);

		waitForSeconds(2);
	}

	@FindBy(css = "span.lg-toast__simple-text")
	private WebElement errorMessage;
	public void verifyDuplicatedDGErrorMessage() throws Exception{
		click(addDynamicGroupBtn);
		groupNameInput.sendKeys("AutoCreateMutiply1");
		selectTheCriteria("Location Id");
		click(criteriaValue);
		click(getDriver().findElement(By.cssSelector("lg-picker-input > div > div > ng-transclude > div > div:nth-child(1) > input-field > ng-form>input")));
		click(criteriaValue);
		click(create);
		if(isExist(errorMessage)){
			System.out.println(errorMessage.getAttribute("innerText"));
			if(errorMessage.getAttribute("innerText").contains("Existing group can not create")){
				SimpleUtils.pass("DynamicGroup duplicated error meaasge is correct");
			}else
				SimpleUtils.fail("DynamicGroup duplicated error meaasge is wrong",false);

		}else
			SimpleUtils.fail("DynamicGroup duplicated error meaasge doesn't display",false);
	}

	@FindBy(css = "lg-button[label = 'Edit']")
	private WebElement Edit;
	@FindBy(css = "lg-button[label = 'Done']")
	private WebElement Done;

	public void editFirstDynamicGroup() throws Exception{
		click(getDriver().findElement(By.cssSelector("td>div>input-field")));
		click(Edit);
		groupNameInput.clear();
		groupNameInput.sendKeys("AutoCreateMutiplyUpdate");
		selectTheCriteria("Country");
		click(criteriaValue);
		click(getDriver().findElement(By.cssSelector("lg-picker-input > div > div > ng-transclude > div > div:nth-child(1) > input-field > ng-form>input")));
		click(criteriaValue);
		click(Done);
	}

	public void removeFirstDynamicGroup() throws Exception{
		click(remove);
		click(remove);

		click(cancelBtnInJobPopUpPage);
		click(backBtnInJobDetailsPage);
	}

	@FindBy(css = ".picker-input")
	private WebElement criteriaSelect0;
	@FindBy(css = ".lg-search-options__option-wrapper")
	private List<WebElement> criteriaOptions;
	private void selectTheCriteria(String criteria) throws Exception {
		if (isElementLoaded(criteriaSelect0, 5)) {
			clickTheElement(criteriaSelect0);
			if (areListElementVisible(criteriaOptions, 10)) {
				for (WebElement option : criteriaOptions) {
					if (option.getText().equalsIgnoreCase(criteria)) {
						clickTheElement(option);
						SimpleUtils.report("Select the option: " + criteria);
						break;
					}
				}
			} else {
				SimpleUtils.fail("Criteria options failed to load!", false);
			}
		} else {
			SimpleUtils.fail("Criteria Select failed to load!", false);
		}
	}

	public void verifyNoDistrictInTitle() throws Exception{
		System.out.println(navForAddLocation);
		if(!navForAddLocation.getAttribute("innerText").contains("District")){
			SimpleUtils.pass("Thers is no district in location title collect table");
		}else
			SimpleUtils.fail("Thers is  district in location title collect table",false);
	}

	@FindBy(css = "div.legendTitle")
	private WebElement groupNameInDetailPage;
	@FindBy(css ="label.input-label")
	private WebElement groupNameLable;
	@FindBy(css = "label.description_Title")
	private WebElement decriptionLabel;
	@FindBy(css = "div.formulaTitle")
	private WebElement criteriaLabel;

	public void verifyExistingGroupDetails() throws Exception{
		click(getDriver().findElement(By.cssSelector("td>div>input-field")));
		scrollToElement(okBtnInCreateNewJobPage);

		if(isElementLoaded(groupNameInDetailPage) && isElementLoaded(groupNameLable) && isElementLoaded(decriptionLabel) && isElementLoaded(criteriaLabel)
		&& isElementLoaded(testBtn) && isElementLoaded(Edit) && isElementLoaded(remove) && isElementLoaded(groupNameInput) && isElementLoaded(criteriaValue)){
			if(groupNameLable.getAttribute("innerText").equals("Group Name*")
			&& decriptionLabel.getAttribute("innerText").contains("Description") && criteriaLabel.getAttribute("innerText").equals("Criteria")){
				SimpleUtils.pass("Group detail page text is correct");
			}else
				SimpleUtils.fail("Group detail page text is wrong",false);
		}else
			SimpleUtils.fail("Some item don't display for group detail page",false);
	}

	@FindBy(css = "h1>lg-close")
	private WebElement closeBtn;
	public void verifyCriteriaList() throws Exception {
		if (criteriaOptions.get(0).getAttribute("innerText").contains("Config Type") && criteriaOptions.get(1).getAttribute("innerText").contains("District") && criteriaOptions.get(2).getAttribute("innerText").contains("Country")
				&& criteriaOptions.get(3).getAttribute("innerText").contains("State") && criteriaOptions.get(4).getAttribute("innerText").contains("City") && criteriaOptions.get(5).getAttribute("innerText").contains("Location Name")
				&& criteriaOptions.get(6).getAttribute("innerText").contains("Location Id") && criteriaOptions.get(7).getAttribute("innerText").contains("Location Type") && criteriaOptions.get(8).getAttribute("innerText").contains("UpperField")
				&& criteriaOptions.get(9).getAttribute("innerText").contains("Custom")) {
			SimpleUtils.pass("Criteria list is correct");
			selectTheCriteria("Config Type");
			click(addMoreBtn);
			click(getDriver().findElement(By.cssSelector("div:nth-child(4) > div.condition_line > lg-cascade-select > lg-select > div > lg-picker-input > div > input-field")));
			if(getDriver().findElement(By.cssSelector("div.lg-search-options__option-wrapper.ng-scope.lg-search-options__option-wrapper--disabled > div")).getCssValue("color").equals("rgba(211, 211, 211, 1)")) {
				SimpleUtils.pass("Selected criteria is gray out");
				click(closeIcon);
			}else
				SimpleUtils.fail("Selected criteria is not gray out",false);
		}else
			SimpleUtils.fail("Criteria list is wrong",false);
	}

	@FindBy(css = "input[placeholder = 'Search']")
	private WebElement searchOption;

	private void searchValue(String Name) throws Exception{
		if(isElementLoaded(searchOption,5)){
			searchOption.sendKeys(Name);
			SimpleUtils.pass("Search criteria successfully");
		}else
			SimpleUtils.fail("Search critiria input field loaded failed",false);
	}

	private void verifyPageFunction() throws Exception{
		if(isElementLoaded(pageLeftBtn,5) && isElementLoaded(pageRightBtn,5) && isElementLoaded(pageNumberText,5)){
			SimpleUtils.pass("Previous page button and next page button loaded successfully");
			SimpleUtils.pass("Page text is correct");
			click(pageLeftBtn);
			click(pageRightBtn);
			SimpleUtils.pass("Previous and next page button is clickable");
		}else
			SimpleUtils.fail("Previous page button and next page button loaded failed",false);
	}

	@FindBy(css = "nav.lg-tabs__nav > div:nth-child(3)")
	private WebElement recentJob;
	@FindBy(css="input[placeholder = 'Search by job title']")
	private WebElement searchRecentJob;
	@FindBy(css = "input[type = 'radio']")
	private WebElement firstJobTitle;
	@FindBy(css = "legend.ng-binding")
	private WebElement locationNum;

	public void verifyRecentJobLocation(String jobTitle) throws Exception {
		click(recentJob);
		searchRecentJob.sendKeys(jobTitle);
		click(firstJobTitle);
		scrollToElement(addBtn);
		click(addBtn);
		if (locationNum.getText().equals("1 Locations Added")) {
			SimpleUtils.pass("Location number is correct");
		} else
			SimpleUtils.fail("Location number is wrong", false);
	}

	@Override
	public void archiveOrStopSpecificJob(String jobTitle) throws Exception {
		if (isElementEnabled(searchInputBox, 10)) {
			searchInputBox.clear();
			searchInputBox.sendKeys(jobTitle);
			searchInputBox.sendKeys(Keys.ENTER);
			waitForSeconds(5);
			if (jobRows.size() <= 0) {
				SimpleUtils.report("There is no "+jobTitle+" job in Jobs! ");
			}else if (isElementLoaded(archiveBtnInJobListPage, 5)){
				for (WebElement row: jobRows) {
					WebElement archiveBtn = row.findElement(By.cssSelector("span[ng-click=\"applyAction(6,job)\"]"));
					clickTheElement(archiveBtn);
					SimpleUtils.pass("Click Archive job button successfully! ");
					if (verifyJobActionsWarningPageShowWell()) {
						clickTheElement(confirmBtnInStopJobPopUpWins);
						SimpleUtils.pass("Click confirm button in stop job popup windows successfully! ");
					}
				}
			} else if (isElementLoaded(stopBtnInJobListPage, 5)){
				clickTheElement(stopBtnInJobListPage);
				if (verifyJobActionsWarningPageShowWell()) {
					clickTheElement(confirmBtnInStopJobPopUpWins);
					waitForSeconds(15);
					ArrayList<HashMap<String,String>> jobInfo = iCanGetJobInfo(jobTitle);
					if (jobInfo.size()>0 && jobInfo.get(0).get("status").equalsIgnoreCase("Stopped")) {
						SimpleUtils.pass("The job:" +jobTitle + " was stopped successfully");
					}else
						SimpleUtils.fail("The job:" +jobTitle + " failed to stop",false);
				}
			}
		} else
			SimpleUtils.fail("The search input fail to load on Jobs page! ", false);
	}

	@FindBy(css = "[class=\"calendar-week select-week\"]")
	private List<WebElement> selectableWeekSelectors;
	@FindBy(css = "i.fa-chevron-right")
	private WebElement navigateToNextMonthButton;

	@Override
	public void selectWeeksForJobToTakePlaceByIndex(int index) {
		if (areListElementVisible(selectableWeekSelectors, 5)) {
			if (selectableWeekSelectors.size() > index) {
				clickTheElement(selectableWeekSelectors.get(index));
				SimpleUtils.pass("Click the " + index + " week in Select week for job to take place calendar successfully! ");
			} else {
				index = index - selectableWeekSelectors.size()+1;
				click(navigateToNextMonthButton);
				SimpleUtils.pass("Click Next month button successfully! ");
				clickTheElement(selectableWeekSelectors.get(index));
				SimpleUtils.pass("Click the " + index + " week in Select week for job to take place calendar successfully! ");
			}
		} else
			SimpleUtils.fail("There is no selectable week selectors load! ", false);
	}

	@FindBy(css = "input[aria-label = 'Job Title']")
	private WebElement jobTitle;
	@FindBy(css = "input[placeholder = 'Search by location name, id, district, state, city etc']")
	private WebElement locationSearchBox;
	@FindBy(css = "input[type = 'checkbox']")
	private WebElement locationCheckBox;

	public String createNewJob(String type){
		SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmss ");
		String currentTime = dfs.format(new Date());
		String jobName = "AutoCreate" + currentTime;
		click(createNewJobBtn);
		jobTypeSelect.sendKeys(type);
		lastWeek.click();
		click(okBtnInCreateNewJobPage);
		jobTitle.sendKeys(jobName);
		click(addLocationBtn);
		locationSearchBox.sendKeys("NancyTest");
		locationSearchBox.sendKeys(Keys.ENTER);
		waitForSeconds(2);
		click(locationCheckBox);
		scrollToElement(addBtn);
		click(addBtn);
		click(okBtnInCreateNewJobPage);
		click(createBtn);
		return jobName;
	}
}