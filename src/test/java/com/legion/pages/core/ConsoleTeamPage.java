package com.legion.pages.core;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.legion.pages.LoginPage;
import com.legion.pages.ProfileNewUIPage;
import com.legion.tests.core.TeamTest;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.legion.pages.BasePage;
import com.legion.pages.TeamPage;
import com.legion.tests.core.TeamTestKendraScott2.timeOffRequestAction;
import com.legion.utils.JsonUtil;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.support.ui.Select;

import static com.legion.utils.MyThreadLocal.*;

public class ConsoleTeamPage extends BasePage implements TeamPage{
	
	private static Map<String, String> propertyMap = SimpleUtils.getParameterMap();
	private static HashMap<String, String> searchDetails = JsonUtil.getPropertiesFromJsonFile("src/test/resources/searchDetails.json");
	int teamMemberRecordsCount=0;
	public static String pth = System.getProperty("user.dir");
	
	 @FindBy(css="[class='console-navigation-item-label Team']")
	 private WebElement goToTeamButton;
	 
	 @FindBy(css="div.sub-navigation div.row div:nth-child(2) div.sub-navigation-view-link")
	 private WebElement goToTeamCoverageTab;
	 
	 @FindBy(id="legion_cons_Team_Roster_Table")
	 private WebElement rosterBodyElement;
	 
	 @FindBy(className="coverage-view")
	 private WebElement coverageViewElement;
	 
	 @FindBy(css="[ng-if=\"canShowTodos()\"]")
	 private WebElement teamPageShowToDoButton;
	 
	 @FindBy(css="[ng-click=\"closeTodoPanelClick()\"]")
	 private WebElement teamPageHideToDoButton;
	 
	 @FindBy(className="todos-container")
	 private WebElement teamPageToDoContainer;
	 
	 @FindBy(css="[ng-show=\"subNavigation.canShowSelf\"]")
	 private WebElement teamPageSubNavigationBar;
	 
	 @FindBy(css="[ng-click=\"setMode('availability')\"]")
	 private WebElement teamPageCoverageAvailabilityButton;
	 
	 @FindBy(css="teamPageAvailabilityTeamMemberDataSection")
	 private WebElement teamPageAvailabilityTeamMemberDataSection;
	 
	 @FindBy(css="[ng-click=\"openProfileModal(w.worker)\"]")
	 private WebElement teamPageChangeInTeamMemberAvailability;
	 
	 @FindBy(className="coverage-info-row")
	 private WebElement teamPageCoverageLabelData;
	 
	 @FindBy(css="[ng-style=\"dropDownButtonStyle()\"]")
	 private WebElement teamPageCoverageJobTitleFilterButton;
	 
	 @FindBy(css="[ng-click=\"selectChoice($event, choice)\"]")
	 private WebElement teamPageCoverageDropdownList;
	 
	 @FindBy(className="coverage-info-row")
	 private WebElement teamPageCoverageLabelSection;
	 
	 @FindBy(css="[ng-click=\"setMode('timeoff')\"]")
	 private WebElement teamPageCoverageTimeOff;
	 
	 @FindBy(css="ul.dropdown-menu.dropdown-menu-right")
	 private WebElement teamPageCoverageDropdownMenu;
	 
	 @FindBy(className="sub-navigation-view-link")
	 private List<WebElement> TeamPageHeaderTabs;
	 
	 @FindBy (css = "div.console-navigation-item-label.Team")
	 private WebElement teamConsoleName;
	 @FindBy(css="[class='console-navigation-item-label Team']")
	 private WebElement goToTeamTab;
		
	 @FindBy(id="legion_cons_Team_Roster_Search_field")
	 private WebElement searchTextBox;
		
	 @FindBy(css=".search-icon")
	 private WebElement searchBtn;
		
	 @FindBy(css="[class='sub-navigation-view-link active']")
	 private WebElement activeTab;
		
	 @FindBy(xpath="//span[contains(text(),'Coverage')]")
	 private WebElement goToCoverageTab;
		
	 @FindBy(css="[class='tmroster ng-scope']")
	 private WebElement rosterLoading;
		
	 @FindBy(css=".title.ng-binding")
	  private List<WebElement> jobTitle;
		
	 @FindBy(css="div.coverage-view.ng-scope")
	 private WebElement coverageLoading;
		
	 @FindBy(css=".fa-angle-right.sch-calendar-navigation-arrow")
	 private  WebElement coverageNextArrow;
		
	 @FindBy(css=".fa-angle-left.sch-calendar-navigation-arrow")
	 private WebElement coveragePreviousArrow;
		
	 @FindBy(css=".coverage-title")
	 private WebElement coverageTitle;
		
	 @FindBy(css=".count.ng-binding")
	 private WebElement teamTabSize;	
	 
	 @FindBy(css="input[id=\"legion_cons_Team_Roster_Search_field\"]")
    private WebElement teamMemberSearchBox;

    @FindBy(css="span.name")
    private List<WebElement> teamMembersList;
    @FindBy(css="div.timeoff-requests-request.row-fx")
    private List<WebElement> timeOffRequestRows;

    @FindBy(css="span.request-buttons-approve")
    private WebElement timeOffApproveBtn;

    @FindBy(css="button.lgn-action-button-success")
    private WebElement timeOffRequestApprovalCOnfirmBtn;

    @FindBy(css="img[src*=\"img/legion/todos-none\"]")
    private WebElement toDoBtnToOpen;

    @FindBy(css="[src*=\"todos-selected\"]")
    private WebElement toDoBtnToClose;

    @FindBy(css="div[ng-show=\"show\"]")
    private WebElement toDoPopUpWindow;

    @FindBy(css="//div[@ng-show='show']//h1[contains(text(),'TEAM')]")
	private WebElement toDoPopUpWindowLabel;

    @FindBy(css="todo-card[todo-type=\"todoType\"]")
    private List<WebElement> todoCards;

    @FindBy(css="a[ng-click=\"goRight()\"]")
    private WebElement nextToDoCardArrow;

    @FindBy(css="button.lgn-action-button-success")
    private WebElement confirmTimeOffApprovalBtn;

    @FindBy(className = "day-week-picker-period-active")
	private WebElement currentWeek;

	 public ConsoleTeamPage() {
		PageFactory.initElements(getDriver(), this);
    }
    
    public void goToTeam() throws Exception
	{
		scrollToTop();
    	if(isElementLoaded(goToTeamButton, 5))
    	{
    		activeConsoleName = teamConsoleName.getText();
    		clickTheElement(goToTeamButton);
    	}else{
    		SimpleUtils.fail("Team button not present on the page",false);
    	}
	}
    
   //added by Gunjan
  //Check the presence of desired workrole in the TeamPage
  	public boolean checkRole(String key){
  		for (WebElement jobTitles : jobTitle){
  			if(key.equalsIgnoreCase(jobTitles.getText())){
  				return true;
  			}
  		}
  	    return true;
  	}
  	
  	
  	//Check the count of Team Members shown on Team Page matches no. of TM records
  	public void teamMemberCountEqualsNoOfTMRecord(){
  		String noOfMemberShown=teamTabSize.getText().replaceAll("\\p{P}","");
  		int countOfTMShown = Integer.parseInt(noOfMemberShown);
  		SimpleUtils.assertOnFail("Count of TM records exists on Team tab matches the count shown = " +jobTitle.size() , countOfTMShown==calcListLength(jobTitle), true);
  	}

  	@Override
  	public void performSearchRoster(List<String> list) throws Exception {
  		// TODO Auto-generated method stub
  		if(isElementLoaded(rosterLoading)){
  			waitForSeconds(3);
  			SimpleUtils.pass("Team ROASTER Page Loaded Successfully");
  			teamMemberCountEqualsNoOfTMRecord();
  			for(int j=0;j<list.size();j++){
  				//noOfMembers++;
  				int count=0;
  				String key=(String) list.get(j);
  				boolean bolCheckRole = checkRole(key);
  				if(bolCheckRole == true){
  					searchTextBox.sendKeys(key);
  					searchBtn.click();
  					int Size=jobTitle.size();
  						for (int i=0;i<jobTitle.size();i++){
  							if(key.equalsIgnoreCase(jobTitle.get(i).getText()) || (jobTitle.get(i).getText()).contains(key)){
  								count=count+1;
  							}else{ 
  								SimpleUtils.fail("Incorrect Search Result",false);
  							}	
  						}
  						if(Size>0){
  							if(Size==count){
  								SimpleUtils.pass("No. of Search Result for "+key+ " JobTitle is " +count);
  							}else{
  								SimpleUtils.fail("Search result is not correct",false);
  							}
  						}else{
  							SimpleUtils.report(key+" work role is not deployed to this environment");
  						}
  				}
  				searchTextBox.clear();
  			}	
  		}else{
  			SimpleUtils.fail("Page not loaded successfully",false);
  		}
  	}
  	
  	
		@Override
		public void coverage() {
			// TODO Auto-generated method stub
			try {
				if(isElementLoaded(goToCoverageTab, 10))
				{
					clickTheElement(goToCoverageTab);
					SimpleUtils.pass("Coverage tab present on Team Page");
					if(isElementLoaded(coverageLoading, 20)){
						SimpleUtils.pass("Coverage Loaded Successfully for current week "+ currentWeek.getText());
					}else{
						SimpleUtils.fail("Coverage not-loaded for "+ currentWeek.getText(),false);
					}

				}else{
					SimpleUtils.fail("Coverage tab not present on Team Tab",false);
				}
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				SimpleUtils.fail("Click on Coverage tab failed!", false);
			}
		}
  		
  		@Override
  		public void coverageViewToPastOrFuture(String nextWeekView, int weekCount)
  		{
  			for(int i = 0; i < weekCount; i++)
  			{
  				if(nextWeekView.toLowerCase().contains("next") || nextWeekView.toLowerCase().contains("future"))
  				{
  					try {
  						if(isElementLoaded(coverageNextArrow)){
  							coverageNextArrow.click();
  							SimpleUtils.pass("Coverage page for next week loaded successfully! "+coverageTitle.getText());
  						}
  					}
  					catch (Exception e) {
  						SimpleUtils.fail("Coverage page for Next Week Arrows Not Loaded/Clickable after ' "+coverageTitle.getText()+ "'",false);
  					}
  				}
  			}

  		}

  		public boolean rosterTeamLoading() throws Exception{
  			boolean flag=false;
  			goToTeam();
  			if(isElementLoaded(rosterBodyElement)){
					SimpleUtils.pass("Roster sub-tab of team tab loaded successfully");
					flag=true;
					return flag;
  			}else{
				SimpleUtils.fail("Roster sub-tab of team tab not loaded successfully",true);
  			}
			return flag;

  		}

  		public boolean coverageTeamLoading() throws Exception{
  			boolean flag=false;
  			if(isElementLoaded(goToCoverageTab))
  			{
  				SimpleUtils.pass("Coverage tab present on Team Page");
  				goToCoverageTab.click();
  				if(isElementLoaded(coverageLoading)){
  					SimpleUtils.pass("Coverage Sub Tab of Team tab Loaded Successfully");
  					flag=true;
  					return flag;
  				}else{
  					SimpleUtils.fail("Coverage Sub Tab of Team tab Not Loaded Successfully",true);
  				}

  			}else{
  				SimpleUtils.fail("Coverage tab not present on Team Tab",true);
  			}
			return flag;

  		}

		@Override
		public boolean loadTeamTab() throws Exception {
			// TODO Auto-generated method stub
			boolean flag=false;
			boolean resultrosterTeamLoading = rosterTeamLoading();
			boolean resultcoverageTeamLoading = coverageTeamLoading();
			if(resultrosterTeamLoading == true && resultcoverageTeamLoading == true){
				SimpleUtils.pass("Team tab loaded successfully");
				flag = true;
				return flag;
			}else {
				SimpleUtils.fail("Team tab not loaded successfully",true);
			}
			return flag;
		}

		@Override
		public String searchAndSelectTeamMemberByName(String username) throws Exception {
	 		String selectedName = "";
			boolean isTeamMemberFound = false;
			if(isElementLoaded(teamMemberSearchBox, 10)) {
				teamMemberSearchBox.sendKeys(Keys.CONTROL, "a");
				teamMemberSearchBox.sendKeys(Keys.DELETE);
				teamMemberSearchBox.sendKeys(username);
				waitForSeconds(3);
				int i = 0;
				while(teamMembers.size() == 0 && i< 5){
					teamMemberSearchBox.sendKeys(Keys.CONTROL, "a");
					teamMemberSearchBox.sendKeys(Keys.DELETE);
					teamMemberSearchBox.sendKeys(username);
					SimpleUtils.report("Input the TM name in search box and waiting for the result! ");
					waitForSeconds(5);
					i++;
				}
				if (teamMembers.size() > 0){
					for (WebElement teamMember : teamMembers){
//						WebElement tr = teamMember.findElement(By.className("tr"));
						if (teamMember != null) {
							WebElement name = teamMember.findElement(By.cssSelector("[data-testid=\"lg-table-name\"] span"));
							List<WebElement> titles = teamMember.findElements(By.cssSelector("[data-testid=\"lg-table-job-title\"] span"));
							WebElement status = teamMember.findElement(By.cssSelector("[data-testid=\"lg-table-legion-onboarding\"] span"));
							String title = "";
							if (name != null && titles != null && status != null && titles.size() > 0) {
								for (WebElement titleElement : titles) {
									title += titleElement.getText();
								}
								String nameJobTitleStatus = name.getText() + title + status.getText();
								if (nameJobTitleStatus.toLowerCase().contains(username.toLowerCase())) {
									selectedName = name.getText();
									clickTheElement(name);
									isTeamMemberFound = true;
									SimpleUtils.pass("Team Page: Team Member '" + username + "' selected Successfully.");
									break;
								}
							}else {
								SimpleUtils.fail("Failed to find the name, title and Status!", true);
							}
						}else {
							SimpleUtils.fail("Failed to find the tr element!", true);
						}
					}
				}
			}
			if(!isTeamMemberFound)
				SimpleUtils.report("Team Page: Team Member '"+username+"' not found.");
			LoginPage loginPage= new ConsoleLoginPage();
			loginPage.closeIntroductionMode();
			return selectedName;
		}


		@FindBy(xpath = "(//div[contains(@id,\"legion_cons_Team_Roster_Table\")]//span)[2]")
		private WebElement firstTeamMemberName;
		public void searchAndSelectTeamMember(String username) throws Exception {
			if(isElementLoaded(teamMemberSearchBox, 10)) {
				teamMemberSearchBox.clear();
				teamMemberSearchBox.sendKeys(username);
				if(isElementLoaded(firstTeamMemberName,10))
					click(firstTeamMemberName);
				else
					SimpleUtils.fail("There is no " + username,false);
			}else
				SimpleUtils.fail("TM search box load failed",false);

		}

		@Override
		public void approvePendingTimeOffRequest() throws Exception {
			String pendingStatusLabel = "PENDING";
			if(timeOffRequestRows.size() > 0) {
				for(WebElement timeOffRequestRow : timeOffRequestRows) {
					if(timeOffRequestRow.getText().toLowerCase().contains(pendingStatusLabel.toLowerCase())) {
						click(timeOffRequestRow);
						if(isElementLoaded(timeOffApproveBtn)) {
							WebElement timeOffRequestDuration = timeOffRequestRow.findElement(By.cssSelector("div.request-date"));
							click(timeOffApproveBtn);
							if(isElementLoaded(timeOffRequestApprovalCOnfirmBtn))
								click(timeOffRequestApprovalCOnfirmBtn);
							SimpleUtils.pass("Team Page: Time Off Request for the duration '"
									+timeOffRequestDuration.getText().replace("\n", "")+"' Approved.");
						}
					}
				}
			}
			else
				SimpleUtils.fail("Team Page: No Time off request found.", false);
		}

		@Override
		public int getPendingTimeOffRequestCount() throws Exception {
			String pendingStatusLabel = "PENDING";
			int pendingRequestCount = 0;
			if(timeOffRequestRows.size() > 0) {
				for(WebElement timeOffRequestRow : timeOffRequestRows) {
					if(timeOffRequestRow.getText().toLowerCase().contains(pendingStatusLabel.toLowerCase())) {
						pendingRequestCount = pendingRequestCount + 1;
					}
				}
			}
			return pendingRequestCount;
		}

		@Override
		public void openToDoPopupWindow() throws Exception {
	 	scrollToTop();
		waitForSeconds(2);
	 	if(isElementLoaded(toDoBtnToOpen,5)) {
				click(toDoBtnToOpen);
//				Thread.sleep(1000);
				if(isToDoWindowOpened())
					SimpleUtils.pass("Team Page: 'ToDo' popup window loaded successfully.");
				else
					SimpleUtils.fail("Team Page: 'ToDo' popup window not loaded.", false);
			}
		}

		@Override
		public void closeToDoPopupWindow() throws Exception {
			if(isElementLoaded(toDoBtnToClose, 5)) {
				waitForSeconds(3);
				moveToElementAndClick(toDoBtnToClose);
				waitForSeconds(3);
				if(!isToDoWindowOpened())
					SimpleUtils.pass("Team Page: 'ToDo' popup window closed successfully.");
				else
					SimpleUtils.fail("Team Page: 'ToDo' popup window not closed.", false);
			}
		}

		public boolean isToDoWindowOpened() throws Exception{
			if(isElementLoaded(toDoPopUpWindow,25) && areListElementVisible(todoCards,25)) {
				if(toDoPopUpWindow.getAttribute("class").contains("is-shown"))
					return true;
			}
			return false;
		}
		

		@Override
		public void approveOrRejectTimeOffRequestFromToDoList(String userName, String timeOffStartDuration,
				String timeOffEndDuration, String action) throws Exception{
			boolean isTimeOffRequestToDoCardFound = false;
			String timeOffRequestCardText = "TIME OFF REQUEST";
			String timeOffStartDate = timeOffStartDuration.split(", ")[1];
			String timeOffEndDate =  timeOffEndDuration.split(", ")[1];
			if(isElementLoaded(todoCards.get(0))) {
				for(WebElement todoCard :todoCards) {
					if(isElementLoaded(nextToDoCardArrow, 10) && !todoCard.isDisplayed())
						click(nextToDoCardArrow);
					if(todoCard.getText().toLowerCase().contains(timeOffRequestCardText.toLowerCase())) {
						if(todoCard.getText().toLowerCase().contains(timeOffStartDate.toLowerCase())
								&& todoCard.getText().toLowerCase().contains(timeOffEndDate.toLowerCase())) {
							isTimeOffRequestToDoCardFound = true;
							if(action.toLowerCase().contains(timeOffRequestAction.Approve.getValue().toLowerCase())) {
								WebElement timeOffApproveButton = todoCard.findElement(By.cssSelector("a[ng-click=\"askConfirm('approve')\"]"));
								if(isElementLoaded(timeOffApproveButton)) {
									click(timeOffApproveButton);
									if(isElementLoaded(confirmTimeOffApprovalBtn)) {
										click(confirmTimeOffApprovalBtn);
										SimpleUtils.pass("Team Page: Time Off Request 'Approved' successfully for ToDo list.");
									}
								}
								else
									SimpleUtils.fail("Team Page: ToDo list time off request 'Approve' button not found.", false);
							}
							else if(action.toLowerCase().contains(timeOffRequestAction.Reject.getValue().toLowerCase())) {
								WebElement timeOffRejectButton = todoCard.findElement(By.cssSelector("a[ng-click=\"askConfirm('reject')\"]"));
								if(isElementLoaded(timeOffRejectButton)) {
									click(timeOffRejectButton);
									WebElement confirmRejectRequestBtn = todoCard.findElement(By.cssSelector("a[ng-click=\"action('reject')\"]"));
									if(isElementLoaded(confirmRejectRequestBtn)) {
										click(confirmRejectRequestBtn);
										SimpleUtils.pass("Team Page: Time Off Request 'Rejected' successfully for ToDo list.");
									}
								}
								else
									SimpleUtils.fail("Team Page: ToDo list time off request 'Reject' button not found.", false);
							}
							break;
						}
					}
				}
				if(! isTimeOffRequestToDoCardFound)
					SimpleUtils.fail("Team Page: ToDo list Time Off Request not found with given details.", false);
			}
			else
				SimpleUtils.fail("Team Page: ToDo cards not loaded.", false);
		}

	//Added by Nora
	@FindBy (className = "loading-icon")
	private WebElement teamTabLoadingIcon;
	@FindBy(css="div[id=\"legion_cons_Team_Roster_Table\"] div[role=\"row\"]")
	private List<WebElement> teamMembers;
	@FindBy (id = "legion_cons_Team_Roster_AddTeamMember_button")
	private WebElement addNewMemberButton;
	@FindBy (className = "col-sm-6")
	private List<WebElement> sectionsOnAddNewTeamMemberTab;
	@FindBy (css = "label[for=\"dateHired\"] img")
	private WebElement calendarImage;
	@FindBy (css = "div.single-calendar-picker.ng-scope")
	private WebElement calendar;
	@FindBy (className = "ranged-calendar__month-name")
	private WebElement currentMonthYear;
	@FindBy (css = "div.is-today")
	private WebElement todayHighlighted;
	@FindBy (css = "[label=\"Transfer\"]")
	private WebElement transferButton;
	@FindBy (className = "location-card-image-name-container")
	private List<WebElement> locationCards;
	@FindBy (className = "location-card-image-box")
	private List<WebElement> locationImages;
	@FindBy (className = "lgnCheckBox")
	private WebElement temporaryTransferButton;
	@FindBy (className = "check-image")
	private WebElement checkImage;
	@FindBy (css="[data-testid=\"lg-table-name\"] span")
	private List<WebElement> teamMemberNames;
	@FindBy (css = "#legion_cons_Team_Roster_Table [role=\"row\"] [data-testid=\"lg-table-name\"] span")
	private List<WebElement> newTeamMemberNames;
	@FindBy (className = "transfer-heading")
	private List<WebElement> transferTitles;
	@FindBy (className = "lgncalendar")
	private List<WebElement> transferCalendars;
	@FindBy (css = "a.pull-left")
	private WebElement backArrow;
	@FindBy (css = "a.pull-right")
	private WebElement forwardArrow;
	@FindBy (css = "div.real-day")
	private List<WebElement> realDays;
	@FindBy (id = "dateHired")
	private WebElement dateHiredInput;
	@FindBy (className = "current-day")
	private WebElement currentDay;
	@FindBy (css = "div.day")
	private List<WebElement> daysOnCalendar;
	@FindBy (css = "div.loan-from-calendar div.day")
	private List<WebElement> startDaysOnCalendar;
	@FindBy (css = "div.loan-to-calendar div.day")
	private	List<WebElement> endDaysOnCalendar;
	@FindBy (css = "button.save-btn.pull-right")
	private WebElement applyOnTransfer;
	@FindBy (className = "lgn-alert-modal")
	private WebElement confirmPopupWindow;
	@FindBy (className = "lgn-action-button-success")
	private WebElement confirmButton;
	@FindBy (className = "lgn-action-button-default")
	private WebElement cancelButton;
	@FindBy (css = "span.lgn-alert-message")
	private List<WebElement> alertMessages;
	@FindBy (css = "div.lgn-alert-message")
	private WebElement popupMessage;
	//@FindBy (css = "div:nth-child(7) > div.value")
	@FindBy (css = ".quick-engagement div:nth-child(7) > div.value")
	private WebElement homeStoreLocation;
	@FindBy (css = "pre.change-location-msg")
	private WebElement changeLocationMsg;
	@FindBy (css = "div.badge-section div.profile-heading")
	private WebElement badgeTitle;
	@FindBy (css = "div.collapsible-title-open span.ng-binding")
	private WebElement profileTabTitle;
	@FindBy (className = "lgn-tm-manage-badges")
	private WebElement manageBadgesWindow;
	@FindBy (className = "lgnCheckBox")
	private List<WebElement> badgeCheckBoxes;
	@FindBy (className = "one-badge")
	private WebElement badgeIcon;
	@FindBy (css = "button[ng-switch-when=\"invite\"]")
	private List<WebElement> inviteButtons;
	@FindBy (className = "modal-content")
	private WebElement inviteTMWindow;
	@FindBy (id = "email")
	private WebElement emailInput;
	@FindBy (className = "help-block")
	private WebElement emailErrorMsg;
	@FindBy (css = "section[ng-form=\"inviteTm\"]")
	private WebElement inviteTMSection;
	@FindBy (css = "button.pull-left")
	private WebElement cancelInviteButton;
	@FindBy (css = "button.btn-success")
	private WebElement sendInviteButton;
	@FindBy (css = "input[placeholder=\"First\"]")
	private WebElement firstNameInput;
	@FindBy (css = "input[placeholder=\"Last\"]")
	private WebElement lastNameInput;
	@FindBy (css = "input[name=\"email\"]")
	private WebElement emailInputTM;
	@FindBy (css = "input[name=\"phone\"]")
	private WebElement phoneInput;
	@FindBy (css = "input[placeholder*=\"Employee\"]")
	private WebElement employeeIDInput;
	@FindBy (css = "select[ng-model*=\"role\"]")
	private WebElement jobTitleSelect;
	@FindBy (css = "select[ng-model*=\"Status\"]")
	private WebElement engagementStatusSelect;
	@FindBy (css = "select[ng-model*=\"hourly\"]")
	private WebElement hourlySelect;
	@FindBy (css = "select[ng-model*=\"salaried\"]")
	private WebElement salariedSelect;
	@FindBy (css = "select[ng-model*=\"exempt\"]")
	private WebElement exemptSelect;
	@FindBy (className = "current-location-text")
	private WebElement homeStoreLabel;
	@FindBy (className = "btn-success")
	private WebElement saveTMButton;
	@FindBy (className = "contact-error")
	private WebElement contactErrorMsg;
	@FindBy (className = "count")
	private WebElement tmCount;
	@FindBy (css = "[data-testid=\"table-filter-wrapper\"] span:nth-child(2)")
	private WebElement newTMCount;
	@FindBy (className = "pull-left")
	private WebElement cancelButtonAddTM;
	@FindBy (css = "span.invitationStatus")
	private List<WebElement> invitationStatus;
	@FindBy (css = "lgn-action-button.invite-button button")
	private WebElement inviteButton;
	@FindBy (css = "button[ng-switch-when=\"activate\"]")
	private List<WebElement> activateButtons;
	@FindBy (css = "profile-management div.collapsible-title")
	private WebElement profileTab;
	@FindBy (css = "img[src*=\"t-m-preferences\"]+[ng-bind-html=\"blockTitle\"]")
	private WebElement workPreferTab;
	@FindBy (css = "timeoff-management .collapsible-title-text")
	private WebElement timeOffTab;
	@FindBy (css = "lg-button[label = 'Create time off']")
	private WebElement newTimeOffBtn;
	@FindBy (css = ".user-profile-section")
	private List<WebElement> userProfileSections;
	@FindBy (css = "[class=\"inline-block ng-scope\"] [ng-click=\"actionClicked('Activate')\"]")
	private WebElement activateButton;
	@FindBy (css = "div.activate")
	private WebElement activateWindow;
	@FindBy (css = "button.save-btn.pull-right")
	private WebElement applyButton;
	@FindBy (css = "[ng-click=\"actionClicked('Deactivate')\"]")
	private WebElement deactivateButton;
	@FindBy (css = "[ng-click=\"actionClicked('Terminate')\"] button")
	private WebElement terminateButton;
	@FindBy (css = "[class=\"pull-left ng-isolate-scope\"][ng-click=\"actionClicked('CancelTerminate')\"]")
	private WebElement cancelTerminateButton;
	@FindBy (css = "div.legion-status div.invitation-status")
	private WebElement onBoardedDate;
	@FindBy (css = "div.legion-status>div:nth-child(2)")
	private WebElement tmStatus;
	@FindBy (css = "[class=\"pull-left ng-isolate-scope\"][ng-click=\"actionClicked('CancelDeactivate')\"]")
	private WebElement cancelActivateButton;
	@FindBy (className = "modal-content")
	private WebElement deactivateWindow;
	@FindBy (css = "button[ng-switch-when=\"update\"]")
	private List<WebElement> updateInfoButtons;
	@FindBy (className = "location-date-selector")
	private WebElement removeWindow;
	@FindBy (css = "lgn-tm-engagement-quick div:nth-child(5)>div")
	private WebElement employeeID;
	@FindBy (css = "i.next-month")
	private WebElement nextMonthArrow;
	@FindBy (css = "[ng-if=\"showManualOnboard()\"]")
	private WebElement manualOnBoardButton;
	@FindBy (css = "div.loan-to-calendar i.next-month")
	private WebElement endDateNextMonthArrow;
	@FindBy (css = "div.loan-from-calendar i.next-month")
	private WebElement startDateNextMonthArrow;
	@FindBy (css = "[ng-src*=\"home-location\"]")
	private WebElement homeStoreImg;
	@FindBy (css = "div.personal-details-panel div.invitation-status")
	private WebElement personalInvitationStatus;
	@FindBy (css = "[src*=\"TimeOff\"] h1")
	private WebElement timeOffDays;
	@FindBy (className = "day-week-picker-date")
	private WebElement calMonthYear;
	@FindBy (className = "day-week-picker-period")
	private List<WebElement> weekDurations;
	@FindBy (className = "day-week-picker-arrow-right")
	private WebElement nextWeekPickerArrow;
	@FindBy(css = "[timeoff=\"timeoff\"] .request-status-Approved")
	private List<WebElement> approvedTimeOffRequests;
	@FindBy(className = "request-buttons-reject")
	private WebElement timeOffRejectBtn;
	@FindBy(css = ".row.th div")
	private List<WebElement> columnsInRoster;
	@FindBy(css = "[role=\"columnheader\"]")
	private List<WebElement> newColumnsInRoster;
	@FindBy(css = ".tr .name")
	private List<WebElement> namesInRoster;
	@FindBy(css = ".tr [ng-if=\"showWorkerId\"]")
	private List<WebElement> employeeIDsInRoster;
	@FindBy(css = ".tr .lgn-xs-4 .title")
	private List<WebElement> jobTitlesInRoster;
	@FindBy(css = ".tr .employedStatus")
	private List<WebElement> employmentStatus;
	@FindBy(css = "[box-title=\"Actions\"]")
	private WebElement actionsSection;

	@Override
	public void verifyTheButtonsInActions(List<String> buttons) throws Exception {
		if (isElementLoaded(actionsSection, 5)) {
			List<WebElement> availableButtons = actionsSection.findElements(By.cssSelector(".pull-left.ng-isolate-scope"));
			List<String> buttonNames = new ArrayList<>();
			for (WebElement name : availableButtons) {
				if (!name.getAttribute("class").contains("ng-hide") && !name.getText().isEmpty()) {
					buttonNames.add(name.getText());
				}
			}
			SimpleUtils.report("Get the buttons from Actions: " + buttonNames.toString());
			if (buttonNames.size() > 0 && buttons.size() > 0 && buttonNames.containsAll(buttons) && buttons.containsAll(buttonNames)) {
				SimpleUtils.pass("Buttons are correct: " + buttons.toString());
			} else {
				SimpleUtils.fail("Buttons are not expected, expected: " + buttons.toString() + ", But actual: " + buttonNames.toString(), false);
			}
		} else {
			SimpleUtils.fail("Actions section not loaded successfully!", false);
		}
	}

	@Override
	public void verifyTheSortFunctionInRosterByColumnName(String columnName) throws Exception {
		try {
			if (areListElementVisible(columnsInRoster, 5)) {
				for (WebElement column : columnsInRoster) {
					if (column.getText() != null && !column.getText().isEmpty() && column.getText().equals(columnName)) {
						clickTheElement(column);
						verifyTheSortFunction(columnName, column);
						clickTheElement(column);
						verifyTheSortFunction(columnName, column);
					}
				}
			}
		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	private void verifyTheSortFunction(String columnName, WebElement column) {
		boolean isSorted = true;
		List<String> targetList = new ArrayList<>();
		if (columnName.equals("NAME")) {
			targetList = getNameListInRoster();
		} else if (columnName.equals("EMPLOYEE ID")) {
			targetList = getEmployeeIDListInRoster();
		} else if (columnName.equals("JOB TITLE")) {
			targetList = getJobTitleListInRoster();
		} else if (columnName.equals("EMPLOYMENT")) {
			targetList = getEmploymentListInRoster();
		}
		String className = column.getAttribute("class");
		List<String> currentList = targetList;
		if (!className.contains("roster-sort-reverse")) {
			Collections.sort(targetList);
			for (int i = 0; i < currentList.size(); i++) {
				if (currentList.get(i) != targetList.get(i)) {
					isSorted = false;
					SimpleUtils.fail("Roster page column: " + columnName + " is not sorted asc!", false);
				}
			}
		} else {
			Collections.sort(targetList, Comparator.reverseOrder());
			for (int i = 0; i < currentList.size(); i++) {
				if (currentList.get(i) != targetList.get(i)) {
					isSorted = false;
					SimpleUtils.fail("Roster page column: " + columnName + " is not sorted desc!", false);
				}
			}
		}
		if (isSorted) {
			SimpleUtils.pass("Roster page column: " + columnName + " is sorted!");
		}
	}

	private List<String> getNameListInRoster() {
		List<String> employeeIDs = new ArrayList<>();
		if (areListElementVisible(employeeIDsInRoster, 5)) {
			for (WebElement id : employeeIDsInRoster) {
				employeeIDs.add(id.getText());
			}
		}
		return employeeIDs;
	}

	private List<String> getEmployeeIDListInRoster() {
		List<String> jobTitles = new ArrayList<>();
		if (areListElementVisible(jobTitlesInRoster, 5)) {
			for (WebElement title : jobTitlesInRoster) {
				jobTitles.add(title.getText());
			}
		}
		return jobTitles;
	}

	private List<String> getJobTitleListInRoster() {
		List<String> names = new ArrayList<>();
		if (areListElementVisible(namesInRoster, 5)) {
			for (WebElement name : namesInRoster) {
				names.add(name.getText());
			}
		}
		return names;
	}

	private List<String> getEmploymentListInRoster() {
		List<String> employments = new ArrayList<>();
		if (areListElementVisible(employmentStatus, 5)) {
			for (WebElement name : employmentStatus) {
				employments.add(name.getText());
			}
		}
		return employments;
	}

	@Override
	public void verifyTheColumnInRosterPage(boolean isLocationGroup) throws Exception {
		try {
			List<String> actualColumns = new ArrayList<>();
			List<String> expectedColumnsRegular = null;
			List<String> expectedColumnsLG = null;
			if (areListElementVisible(columnsInRoster, 5)) {
				for (WebElement column : columnsInRoster) {
					if (column.getText() != null && !column.getText().isEmpty()) {
						actualColumns.add(column.getText());
					}
				}
				expectedColumnsRegular = new ArrayList<>(Arrays.asList("NAME", "EMPLOYEE ID", "JOB TITLE", "EMPLOYMENT", "STATUS", "BADGES", "ACTION"));
				expectedColumnsLG = new ArrayList<>(Arrays.asList("NAME", "EMPLOYEE ID", "JOB TITLE", "EMPLOYMENT", "LOCATION", "STATUS", "BADGES", "ACTION"));
			} else if (areListElementVisible(newColumnsInRoster, 5)) {
				for (WebElement column : newColumnsInRoster) {
					if (column.getText() != null && !column.getText().isEmpty() && !column.getText().equalsIgnoreCase(" ")) {
						actualColumns.add(column.getText());
					}
				}
				expectedColumnsRegular = new ArrayList<>(Arrays.asList("NAME", "LEGION ONBOARDING", "JOB TITLE", "EMPLOYMENT", "MINOR", "EID", "BADGES"));
				expectedColumnsLG = new ArrayList<>(Arrays.asList("NAME", "LEGION ONBOARDING", "JOB TITLE", "EMPLOYMENT", "MINOR", "EID", "LOCATION", "BADGES"));
			} else {
				SimpleUtils.fail("Team Roster Page: Columns failed to load!", false);
			}
			if (isLocationGroup) {
				if (actualColumns.containsAll(expectedColumnsLG) && expectedColumnsLG.containsAll(actualColumns)) {
					SimpleUtils.pass("Team Roster: Verified the columns are correct for location group!");
				} else {
					SimpleUtils.fail("Team Roster: Verified the columns are incorrect for location group! Expected: "
							+ expectedColumnsLG.toString() + ". But actual columns: " + actualColumns.toString(), false);
				}
			} else {
				if (actualColumns.containsAll(expectedColumnsRegular) && expectedColumnsRegular.containsAll(actualColumns)) {
					SimpleUtils.pass("Team Roster: Verified the columns are correct for regular location!");
				} else {
					SimpleUtils.fail("Team Roster: Verified the columns are incorrect for regular location! Expected: "
							+ expectedColumnsRegular.toString() + ". But actual columns: " + actualColumns.toString(), false);
				}
			}
		} catch (Exception e) {
			SimpleUtils.fail("Team page: verify the columns in roster page failed!", false);
		}
	}

	@Override
	public void rejectAllTheTimeOffRequests() throws Exception {
		if(areListElementVisible(approvedTimeOffRequests,10) && approvedTimeOffRequests.size() > 0) {
			for(WebElement timeOffRequest : approvedTimeOffRequests) {
				clickTheElement(timeOffRequest);
				if(isElementLoaded(timeOffRejectBtn,5)) {
					scrollToElement(timeOffRejectBtn);
					clickTheElement(timeOffRejectBtn);
					SimpleUtils.pass("My Time Off: Time off request Reject button clicked.");
				}
			}
		}
	}

	@Override
	public int getTimeOffCountByStartAndEndDate(List<String> timeOffStartNEndDate) throws Exception {
		int timeOffCount = 0;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MMM dd");
		Date timeOffStartDate = dateFormat.parse(timeOffStartNEndDate.get(0));
		Date timeOffEndDate = dateFormat.parse(timeOffStartNEndDate.get(1));
		List<String> daysAndStatus = getTimeOffDaysAndStatus(timeOffStartDate, timeOffEndDate, timeOffCount, false, false);
		while (!Boolean.parseBoolean(daysAndStatus.get(1)) || !Boolean.parseBoolean(daysAndStatus.get(2))) {
			if (isElementLoaded(nextWeekPickerArrow, 5)) {
				click(nextWeekPickerArrow);
				daysAndStatus = getTimeOffDaysAndStatus(timeOffStartDate, timeOffEndDate, Integer.parseInt(daysAndStatus.get(0)),
						Boolean.parseBoolean(daysAndStatus.get(1)), Boolean.parseBoolean(daysAndStatus.get(2)));
			}
		}
		SimpleUtils.report("Time Off Days: " + daysAndStatus.get(0));
		return Integer.parseInt(daysAndStatus.get(0));
	}

	public List<String> getTimeOffDaysAndStatus(Date timeOffStartDate, Date timeOffEndDate, int timeOffCount,
												boolean isStartFound, boolean isEndFound) throws Exception {
		List<String> daysAndStatus = new ArrayList<>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MMM dd");
		if (areListElementVisible(weekDurations, 5)) {
			for (int i = 0; i < weekDurations.size(); i++) {
				click(weekDurations.get(i));
				List<String> years = getYearsFromCalendarMonthYearText();
				String activeWeek = getActiveWeekText(weekDurations.get(i));
				String[] items = activeWeek.split(" ");
				String weekStartText = years.get(0) + " " + items[3] + " " + items[4];
				String weekEndText = (years.size() == 2 ? years.get(1) : years.get(0)) + " " + items[6] + " " + items[7];
				Date weekStartDate = dateFormat.parse(weekStartText);
				Date weekEndDate = dateFormat.parse(weekEndText);
				if (!isStartFound) {
					isStartFound = SimpleUtils.isDateInTimeDuration(timeOffStartDate, weekStartDate, weekEndDate);
				}
				isEndFound = SimpleUtils.isDateInTimeDuration(timeOffEndDate, weekStartDate, weekEndDate);
				if (isStartFound && isEndFound) {
					if (isElementLoaded(timeOffDays, 5)) {
						// Wait for the time off days to be loaded
						waitForSeconds(5);
						timeOffCount += Integer.parseInt(timeOffDays.getText());
						break;
					}
				}
				if (isStartFound || isEndFound) {
					if (isElementLoaded(timeOffDays, 5)) {
						// Wait for the time off days to be loaded
						waitForSeconds(5);
						timeOffCount += Integer.parseInt(timeOffDays.getText());
					}
				}
			}
		}
		daysAndStatus.add(Integer.toString(timeOffCount));
		daysAndStatus.add(Boolean.toString(isStartFound));
		daysAndStatus.add(Boolean.toString(isEndFound));
		return daysAndStatus;
	}

	public List<String> getYearsFromCalendarMonthYearText() throws Exception {
		List<String> years = new ArrayList<>();
		if (isElementLoaded(calMonthYear, 5)) {
			if (calMonthYear.getText().contains("-")) {
				String[] monthAndYear = calMonthYear.getText().split("-");
				if (monthAndYear.length == 2) {
					if (monthAndYear[0].trim().length() > 4)
					    years.add(monthAndYear[0].trim().substring(monthAndYear[0].trim().length() - 4));
					if (monthAndYear[1].trim().length() > 4)
					    years.add(monthAndYear[1].trim().substring(monthAndYear[1].trim().length() - 4));
				}
			}else {
				years.add(calMonthYear.getText().trim().substring(calMonthYear.getText().trim().length() - 4));
			}
		}else {
			SimpleUtils.fail("Calendar month and year not loaded successfully!", false);
		}
		return years;
	}

	public String getActiveWeekText(WebElement element) throws Exception {
		String activeWeekText = "";
		if (isElementLoaded(element, 5)) {
			activeWeekText = element.getText().contains("\n") ? element.getText().replace("\n", " ") : element.getText();
		}
		return activeWeekText;
	}

	@Override
	public int verifyTimeOffRequestShowsOnToDoList(String userName, String timeOffStartDuration, String timeOffEndDuration) throws Exception {
		int timeOffDays = 0;
		boolean isTimeOffRequestToDoCardFound = false;
		String timeOffRequestCardText = "TIME OFF REQUEST";
		String timeOffStartDate = timeOffStartDuration.split(", ")[1];
		String timeOffEndDate =  timeOffEndDuration.split(", ")[1];
		if(isElementLoaded(todoCards.get(0))) {
			for (WebElement todoCard : todoCards) {
				if (isElementLoaded(nextToDoCardArrow, 10) && !todoCard.isDisplayed())
					click(nextToDoCardArrow);
				if (todoCard.getText().toLowerCase().contains(timeOffRequestCardText.toLowerCase())) {
					if (todoCard.getText().toLowerCase().contains(timeOffStartDate.toLowerCase())
							&& todoCard.getText().toLowerCase().contains(timeOffEndDate.toLowerCase())
							&& todoCard.getText().toLowerCase().contains(userName.toLowerCase())) {
						isTimeOffRequestToDoCardFound = true;
						SimpleUtils.pass("User: " + userName + " is visible on TODO list!");
						String headingText = todoCard.findElement(By.cssSelector("p.heading")).getText();
						String[] items = headingText.split(" ");
						for (String item : items) {
							if (SimpleUtils.isNumeric(item)) {
								timeOffDays = Integer.parseInt(item);
								SimpleUtils.report("Get Time Off Days: " + timeOffDays);
							}
						}
						break;
					}
				}
			}
		}
		if (!isTimeOffRequestToDoCardFound) {
			SimpleUtils.fail("Failed to find user: " + userName + " on TODO list!", false);
		}
		return timeOffDays;
	}

	@FindBy(className="roster-header")
	private WebElement rosterHeaderElement;
	@FindBy(css = "[data-testid=\"table-filter-wrapper\"]")
	private WebElement teamTableFilter;

	@Override
	public void verifyTeamPageLoadedProperlyWithNoLoadingIcon() throws Exception {
		waitUntilElementIsInVisible(teamTabLoadingIcon);
		if(isElementLoaded(rosterLoading, 60)
				&& areListElementVisible(TeamSubTabsElement, 60)
				&& isElementLoaded(rosterHeaderElement, 60)
				&& isElementLoaded(rosterBodyElement, 60)){
			SimpleUtils.pass("Team Page is Loaded Successfully!");
		}else if (isElementLoaded(teamTableFilter, 5)) {
			SimpleUtils.pass("Team Page is Loaded Successfully!");
		} else{
			SimpleUtils.fail("Team Page isn't Loaded Successfully", false);
		}
	}

	@Override
	public void verifyTheFunctionOfSearchTMBar(List<String> testStrings) throws Exception {
		if (isElementLoaded(searchTextBox, 5)){
			if (testStrings.size() > 0){
				for (String testString : testStrings){
					searchTextBox.sendKeys(testString);
					if (teamMembers.size() > 0){
						for (WebElement teamMember : teamMembers){
							if (teamMember.getText().toLowerCase().contains(testString.trim().toLowerCase())) {
								SimpleUtils.pass("Verified " + teamMember.getText() + " contains test string: " + testString);
							} else {
								SimpleUtils.fail("Team member: " + teamMember.getText() + " doesn't contain the test String: "
										+ testString, false);
							}

						}
					}else{
						SimpleUtils.report("Doesn't find the Team member that contains: " + testString);
					}
					searchTextBox.clear();
				}
			}
		}else {
			SimpleUtils.fail("Failed to find the search textbox element!", false);
		}
	}

	@Override
	public void verifyTheFunctionOfAddNewTeamMemberButton() throws Exception{
		final String personalDetails = "Personal Details";
		final String engagementDetails = "Engagement Details";
		final String titleClassName = "header-label";
		verifyTheVisibilityAndClickableOfPlusIcon();
		click(addNewMemberButton);
		if (areListElementVisible(sectionsOnAddNewTeamMemberTab, 10)){
			if (sectionsOnAddNewTeamMemberTab.size() == 2){
				SimpleUtils.pass("Two sections on Add New Team Member Tab loaded successfully!");
				WebElement personalElement = sectionsOnAddNewTeamMemberTab.get(0).findElement(By.className(titleClassName));
				WebElement engagementElement = sectionsOnAddNewTeamMemberTab.get(1).findElement(By.className(titleClassName));
				if (personalElement != null && engagementElement != null){
					if (personalDetails.equals(personalElement.getText()) && engagementDetails.equals(engagementElement.getText())){
						SimpleUtils.pass("Personal Details and Engagement Details sections loaded!");
					}else{
						SimpleUtils.fail("Personal Details and Engagement Details sections failed to load", true);
					}
				}else {
					SimpleUtils.fail("Failed to find the personal and engagement sections!", true);
				}
			}else{
				SimpleUtils.fail("Two sections on Add New Team Member Tab failed to load", false);
			}
		}
	}

	private void verifyTheVisibilityAndClickableOfPlusIcon() throws Exception {
		if (isElementLoaded(addNewMemberButton, 10)){
			SimpleUtils.pass("\"+\" icon is visible on team tab!");
			if (isClickable(addNewMemberButton, 10)){
				SimpleUtils.pass("\"+\" icon is clickable on team tab!");
			}else{
				SimpleUtils.fail("\"+\" icon isn't clickable on team tab!", true);
			}
		}else{
			SimpleUtils.fail("\"+\" icon is invisible on team tab!", false);
		}
	}

	@Override
	public void verifyTheMonthAndCurrentDayOnCalendar(String currentDateForSelectedLocation) throws Exception{
		String colorOnWeb = "#fb7800";
		if (isClickOnCalendarImageSuccessfully()){
			if (isElementLoaded(currentMonthYear, 5) && isElementLoaded(todayHighlighted, 5)){
				String currentDateOnCalendar = currentMonthYear.getText() + " " + todayHighlighted.getText();
				String color = todayHighlighted.getCssValue("color");
				/*
				 * color css value format: rgba(251, 120, 0, 1), need to convert it to Hex format
				 */
				if (color.contains("(") && color.contains(")") && color.contains(",")){
					String[] rgba = color.substring(color.indexOf("(") + 1, color.indexOf(")")).split(",");
					String colorHex = awtColorToWeb(rgba);
					if (colorHex.equals(colorOnWeb)){
						SimpleUtils.pass("Verified the color of current day is correct!");
					}else{
						SimpleUtils.fail("Failed to verify the color, expected is: " + colorOnWeb + " actual is: "
						+ colorHex, true);
					}
				}
				if (currentDateForSelectedLocation.equals(currentDateOnCalendar)){
					SimpleUtils.pass("It displays the calendar for current month and current day!");
				}else{
					SimpleUtils.fail("It doesn't display the calendar for current month and current day, current day is: "
							+ currentDateForSelectedLocation + ", but calendar displayed day is: " + currentDateOnCalendar, true);
				}
			}else {
				SimpleUtils.fail("Current month, year and today elements failed to load!", true);
			}
		}
	}

	@Override
	public String selectATeamMemberToTransfer() throws Exception {
		String transfer = "TRANSFER";
		String teamMember = null;
		if (areListElementVisible(teamMemberNames, 30)){
			Random random = new Random();
			int randomIndex = random.nextInt(teamMemberNames.size() - 1);
			teamMember = teamMemberNames.get(randomIndex).getText();
			click(teamMemberNames.get(randomIndex));
			if (isElementLoaded(transferButton, 5) && isElementLoaded(homeStoreImg, 10)) {
				if (transfer.equals(transferButton.getText())) {
					SimpleUtils.pass("Find a Team Member that can be transferred!");
					moveToElementAndClick(transferButton);
				} else {
					/*
					 * If the user already transferred, cancel transfer it.
					 */
					if (isCancelTransferSuccess()) {
						click(transferButton);
					}
				}
			}else {
				SimpleUtils.fail("Transfer button and home store image failed to load!", true);
			}
		}else{
			SimpleUtils.fail("Team Members didn't load successfully!", false);
		}
		return teamMember;
	}

	@Override
	public void isCancelTransferButtonLoadedAndClick() throws Exception {
		if (isElementLoaded(cancelTransferButton, 5)) {
			SimpleUtils.pass("CANCEL TRANSFER button loaded successfully!");
			waitForSeconds(3);
			moveToElementAndClick(cancelTransferButton);
		} else {
			SimpleUtils.fail("The cancel transfer button fail to load! ", false);
		}
	}

	@Override
	public boolean verifyCancelTransferWindowPopup() throws Exception {
		boolean isPopup = false;
		String expectedMessage = "Are you sure you want to cancel the transfer to the new location?";
		String actualMessage = null;
		if (isElementLoaded(confirmPopupWindow, 5) && isElementLoaded(popupMessage, 5)) {
			actualMessage = popupMessage.findElement(By.tagName("span")).getText();
			if (expectedMessage.trim().equals(actualMessage.trim())){
				isPopup = true;
				SimpleUtils.pass("Cancel Transfer window pops up!");
			}else {
				SimpleUtils.fail("The Message on Cancel Transfer window is incorrect!", true);
			}
		} else {
			SimpleUtils.fail("Cancel Transfer pop-up window doesn't show!", true);
		}
		return isPopup;
	}

	@Override
	public boolean verifyTransferButtonEnabledAfterCancelingTransfer() throws Exception {
		boolean isEnabled = false;
		String transfer = "TRANSFER";
		if (isElementLoaded(confirmButton, 10)) {
			click(confirmButton);
			if (isElementEnabled(transferButton, 10)) {
				if (transferButton.getText().equals(transfer)) {
					isEnabled = true;
					SimpleUtils.pass("TRANSFER button is enabled!");
				} else {
					SimpleUtils.fail("CANCEL TRANSFER button doesn't change to TRANSFER", true);
				}
			} else {
				SimpleUtils.fail("Cancel Transfer failed!", true);
			}
		}else {
			SimpleUtils.fail("Cancel transfer confirm button failed to load!", true);
		}
		return isEnabled;
	}

	@Override
	public void verifyHomeLocationAfterCancelingTransfer(String homeLocation) throws Exception {
		if (isElementLoaded(homeStoreLocation, 5)) {
			if (homeStoreLocation.getText().contains(homeLocation)){
				SimpleUtils.pass("Home Store location is the previous one!");
			}else {
				SimpleUtils.fail("Home Store location isn't the previous one!", true);
			}
		}else {
			SimpleUtils.fail("Home Location element failed to load!", false);
		}
	}

	@Override
	public boolean isProfilePageLoaded() throws Exception {
		boolean isLoaded = false;
		String profile = "Profile";
		if (isElementLoaded(profileTabTitle, 5)){
			if (profileTabTitle.getText().equals(profile)){
				isLoaded = true;
				SimpleUtils.pass("Profile Page loaded successfully!");
			} else {
				SimpleUtils.fail("Profile Page doesn't load successfully!", true);
			}
		}
		return isLoaded;
	}

	private boolean isCancelTransferSuccess() throws Exception {
		boolean isSuccess = false;
		if (isElementLoaded(cancelTransferButton, 5)) {
			click(cancelTransferButton);
			if (isElementLoaded(confirmButton, 10)) {
				click(confirmButton);
				if (isElementLoaded(transferButton, 10)){
					isSuccess = true;
					SimpleUtils.pass("Cancel Transfer Successfully!");
				}else {
					SimpleUtils.fail("Cancel Transfer failed!", true);
				}
			}else {
				SimpleUtils.fail("A pop-up window doesn't show!", true);
			}
		}else {
			SimpleUtils.fail("Cancel Transfer button doesn't Load!", true);
		}
		return isSuccess;
	}

	@Override
	public String verifyHomeLocationCanBeSelected() throws Exception {
		String selectedLocation = null;
		String attribute = "style";
		if (areListElementVisible(locationImages, 30) && areListElementVisible(locationCards, 30)) {
			Random random = new Random();
			int index = random.nextInt(locationCards.size() - 1);
			WebElement locationCard = locationCards.get(index);
			selectedLocation = locationCard.findElement(By.className("location-card-name-text")).getText();
			click(locationCard);
			if (locationCard.getAttribute(attribute) != null && !locationCard.getAttribute(attribute).isEmpty()){
				SimpleUtils.pass("Select one Location successfully!");
			}else{
				SimpleUtils.fail("Failed to select the Location!", false);
			}
		}else{
			SimpleUtils.fail("Location Cards Failed to load!", false);
		}
		return selectedLocation;
	}

	@Override
	public void verifyClickOnTemporaryTransferButton() throws Exception {
		if (isElementLoaded(temporaryTransferButton, 5)) {
			if (!temporaryTransferButton.getAttribute("class").contains("checked")) {
				click(temporaryTransferButton);
			}
			if (isElementLoaded(checkImage, 5)){
				SimpleUtils.pass("Temporary Transfer button is checked!");
			}else{
				SimpleUtils.fail("Temporary Transfer button isn't checked", true);
			}
		}else{
			SimpleUtils.fail("Temporary Transfer button doesn't load!", true);
		}
	}

	@Override
	public void verifyTwoCalendarsForCurrentMonthAreShown(String currentDate) throws Exception {
		String className = "month-header";
		if (areListElementVisible(transferTitles, 10) && areListElementVisible(transferCalendars, 10)){
			if (transferTitles.size() == 2 && transferCalendars.size() == 2){
				String monthYearLeft = transferCalendars.get(0).findElement(By.className(className)).getText().toLowerCase();
				String monthYearRight = transferCalendars.get(1).findElement(By.className(className)).getText().toLowerCase();
				if (currentDate.toLowerCase().contains(monthYearLeft) && currentDate.toLowerCase().contains(monthYearRight)) {
					SimpleUtils.pass("Two Calendars for current month are shown!");
				}else{
					SimpleUtils.fail("Two Calendars are not for current month!", true);
				}
			}else {
				SimpleUtils.fail("Calendar counts are incorrect!", true);
			}
		}else {
			SimpleUtils.fail("Calendars are failed to loaded!", true);
		}
	}

	@Override
	public void verifyTheCalendarCanNavToPreviousAndFuture() throws Exception {
		String monthAndYear = null;
		String attribute = "value";
		if (isClickOnCalendarImageSuccessfully()){
			if (isElementLoaded(backArrow, 5) && isElementLoaded(forwardArrow, 5)){
				navigateToPreviousAndFutureDate(backArrow);
				navigateToPreviousAndFutureDate(forwardArrow);
				navigateToPreviousAndFutureDate(forwardArrow);
				monthAndYear = currentMonthYear.getText();
				if (areListElementVisible(realDays)){
					/*
					 * Generate a random to select a day!
					 */
					Random random = new Random();
					WebElement realDay = realDays.get(random.nextInt(realDays.size()));
					String day = realDay.getText();
					String expectedDate = monthAndYear.substring(0,3) + " " + day + ", "
							+ monthAndYear.substring(monthAndYear.length() - 4);
					click(realDay);
					String selectedDate = dateHiredInput.getAttribute(attribute);
					if (expectedDate.equals(selectedDate)) {
						SimpleUtils.pass("Selected a day successfully!");
					}else {
						SimpleUtils.fail("Selected day is inconsistent with the date shown in Date Hired!", true);
					}
				}else {
					SimpleUtils.fail("Real days elements failed to load!", true);
				}
			}else {
				SimpleUtils.fail("Back and Forward arrows are failed to load!", true);
			}
		}else {
			SimpleUtils.fail("Click on Calendar image failed!", true);
		}
	}

	@Override
	public void verifyTheCurrentDateAndSelectOtherDateOnTransfer() throws Exception {
		String colorOnWeb = "#fb7800";
		if (areListElementVisible(transferCalendars, 10) && isElementLoaded(currentDay, 10)) {
			String color = currentDay.getCssValue("color");
			/*
			 * color css value format: rgba(251, 120, 0, 1), need to convert it to Hex format
			 */
			if (color.contains("(") && color.contains(")") && color.contains(",")){
				String[] rgba = color.substring(color.indexOf("(") + 1, color.indexOf(")")).split(",");
				String colorHex = awtColorToWeb(rgba);
				if (colorHex.equals(colorOnWeb)){
					SimpleUtils.pass("Current Day is Highlighted!");
				}else{
					SimpleUtils.fail("Current day isn't highlighted!", true);
				}
			}
			verifyDateCanBeSelectedOnTempTransfer();
		}else {
			SimpleUtils.fail("Calendar failed to load!", true);
		}
	}

	@Override
	public void verifyDateCanBeSelectedOnTempTransfer() throws Exception {
		String className = "selected-day";
		int currentDayIndex = 0;
		int maxIndex = 0;
		int randomIndex = 0;
		Random random = new Random();
		if (areListElementVisible(startDaysOnCalendar, 10)) {
			/*
			 * Select a start date to temp transfer, should start from today or future.
			 */
			currentDayIndex = getSpecificDayIndex(currentDay);
			maxIndex = startDaysOnCalendar.size() - 1;
			if (currentDayIndex < maxIndex) {
				randomIndex = currentDayIndex + 1;
			}else {
				if (isElementLoaded(endDateNextMonthArrow, 5)) {
					click(endDateNextMonthArrow);
					randomIndex = 7 + random.nextInt(startDaysOnCalendar.size() - 1 - 7);
				}
			}
			WebElement randomElement = startDaysOnCalendar.get(randomIndex);
			click(startDaysOnCalendar.get(randomIndex));
			if (randomElement.getAttribute("class").contains(className)) {
				SimpleUtils.pass("Select a start date successfully!");
			} else {
				SimpleUtils.fail("Failed to select a start date!", true);
			}
		}else {
			SimpleUtils.fail("Days on calendar failed to load!", true);
		}
		if (areListElementVisible(endDaysOnCalendar, 10)) {
			/*
			 * Select a end date to temp transfer.
			 */
			if (isElementLoaded(endDateNextMonthArrow, 5)) {
				click(endDateNextMonthArrow);
				click(endDateNextMonthArrow);
			}
			maxIndex = endDaysOnCalendar.size() - 1;
			randomIndex = 7 + random.nextInt(maxIndex - 7);
			WebElement randomElement = endDaysOnCalendar.get(randomIndex);
			click(randomElement);
			if (randomElement.getAttribute("class").contains(className)) {
				SimpleUtils.pass("Select an end date successfully!");
			} else {
				SimpleUtils.fail("Failed to select an end date!", true);
			}
		}else {
			SimpleUtils.fail("Days on calendar failed to load!", true);
		}
	}

	@Override
	public boolean isApplyButtonEnabled() throws Exception {
		boolean isEnabled = false;
		isEnabled = isElementEnabled(applyOnTransfer, 3);
		return isEnabled;
	}

	@Override
	public void verifyClickOnApplyButtonOnTransfer() throws Exception {
		if (isApplyButtonEnabled()) {
			SimpleUtils.pass("Apply Button on Transfer page is enabled!");
			click(applyOnTransfer);
		}else{
			SimpleUtils.fail("Apply Button on Transfer Page is still disabled!", true);
		}
	}

	@Override
	public void verifyTheMessageOnPopupWindow(String currentLocation, String selectedLocation, String teamMemberName) throws Exception {
		if (teamMemberName.contains("“") && teamMemberName.contains("”")){
			teamMemberName = teamMemberName.substring(teamMemberName.indexOf("“") + 1, teamMemberName.indexOf("”"));
		}else if (teamMemberName.contains(" ")){
			teamMemberName = teamMemberName.split(" ")[0];
		}
		String expectedShiftMessage = "from this date onwards will be converted to Open Shifts.";
		if (isElementLoaded(confirmPopupWindow, 10) && areListElementVisible(alertMessages, 10)) {
			if (alertMessages.size() == 2) {
				String transferMessage = alertMessages.get(0).getText();
				String shiftMessage = alertMessages.get(1).getText();
				if (transferMessage.contains(currentLocation) && transferMessage.contains(selectedLocation) &&
				shiftMessage.contains(teamMemberName) && shiftMessage.contains(expectedShiftMessage)){
					SimpleUtils.pass("Message on pop-up window shows correctly!");
				}else{
					SimpleUtils.fail("Message on pop-up window shows incorrectly", true);
				}
			}
		}else{
			SimpleUtils.fail("Pop-up window failed to show!", true);
		}
	}

	@Override
	public void verifyTheFunctionOfConfirmTransferButton() throws Exception {
		String successfulMessage = "Successfully transferred the Team Member";
		String cancelTransfer = "CANCEL TRANSFER";
		if (isElementLoaded(confirmPopupWindow, 10) && isElementLoaded(confirmButton, 10)) {
			click(confirmButton);
			if (isElementLoaded(popupMessage, 10)) {
				String message = popupMessage.getText();
				if (message.equals(successfulMessage)) {
					if (isElementLoaded(transferButton, 10)){
						if (transferButton.getText().equals(cancelTransfer)) {
							SimpleUtils.pass("Transfer Successfully!");
						}else {
							SimpleUtils.fail("Button doesn't change to CANCEL TRANSFER!", true);
						}
					}
				}else {
					SimpleUtils.fail("The pop-up message is incorrect!", true);
				}
			}
		}else {
			SimpleUtils.fail("Confirm pop up window failed to load!", true);
		}
	}

	@Override
	public void verifyTheHomeStoreLocationOnProfilePage(String location, String selectedLocation) throws Exception {
		String actualLocationMessage = null;
		String startDate = null;
		String endDate = null;
		boolean isCorrectFormat = false;
		SimpleDateFormat format = new SimpleDateFormat("dd/mm/yyyy");
		if (isElementLoaded(homeStoreLocation, 10) && isElementLoaded(changeLocationMsg, 10)) {
			if (homeStoreLocation.getText().contains(location)){
				SimpleUtils.pass("Home Store Location is not updating!");
			}else {
				SimpleUtils.fail("Home Store Location is changed!", true);
			}
			actualLocationMessage = changeLocationMsg.getText();
			if (actualLocationMessage.contains("-")) {
				String[] values = actualLocationMessage.split("-");
				if (values.length == 3) {
					startDate = values[1].trim().split("\n")[0];
					endDate = values[2].trim();
					/*
					 * Check whether the date format is correct, eg: 02/20/2020
					 */
					isCorrectFormat = SimpleUtils.isDateFormatCorrect(startDate, format) && SimpleUtils.isDateFormatCorrect(endDate, format);
				}
			}
			/*
			 * Since some string has two blank spaces, so remove the blank space.
			 */
			selectedLocation = selectedLocation.replaceAll("\\s*", "");
			actualLocationMessage = actualLocationMessage.replaceAll("\\s*", "");
			if (actualLocationMessage.contains(selectedLocation) && isCorrectFormat) {
				SimpleUtils.pass("Change Location Message is correct!");
			}else {
				SimpleUtils.fail("Change Location Message is incorrect!", true);
			}
		}else {
			SimpleUtils.fail("Home store location and change location message failed to load!", true);
		}
	}

	@FindBy(css = "form-section[on-action=\"editProfile()\"]")
	private WebElement profileSection;

	@Override
	public String verifyTheFunctionOfEditBadges() throws Exception {
		String badges = "BADGES";
		String badgeID = "";
		if (isElementLoaded(profileSection,5) && isElementLoaded(profileSection.findElement(By.cssSelector("lg-button[label=\"Edit\"]")),5)){
			click(profileSection.findElement(By.cssSelector("lg-button[label=\"Edit\"]")));
			SimpleUtils.pass("enter edit profile mode!");
			waitForSeconds(3);
			WebElement manageBadge = profileSection.findElement(By.cssSelector(".ManageButton"));
			scrollToElement(manageBadge);
			moveToElementAndClick(manageBadge);
			if (isManageBadgesLoaded()) {
				badgeID = selectTheBadgeByRandom();
				confirmButton.click();
				if (isElementLoaded(badgeIcon, 5)) {
					WebElement badge = badgeIcon.findElement(By.id(badgeID));
					if (badge != null) {
						SimpleUtils.pass("Select the badges successfully!");
					}else{
						SimpleUtils.fail("The selected badge doesn't show!", true);
					}
				}else {
					SimpleUtils.fail("Badges failed to load on Profile page!", true);
				}
			}
			moveToElementAndClick(profileSection.findElement(By.xpath("//span[text()=\"Save\"]")));
		} else {
			SimpleUtils.fail("Edit button is not loaded!",true);
		}


		/*if (isElementLoaded(badgeTitle, 5)) {
			if (badgeTitle.getText().equals(badges)) {
				WebElement editBadge = badgeTitle.findElement(By.tagName("i"));
				click(editBadge);
				if (isManageBadgesLoaded()) {
					badgeID = selectTheBadgeByRandom();
					confirmButton.click();
					if (isElementLoaded(badgeIcon, 5)) {
						WebElement badge = badgeIcon.findElement(By.id(badgeID));
						if (badge != null) {
							SimpleUtils.pass("Select the badges successfully!");
						}else{
							SimpleUtils.fail("The selected badge doesn't show!", true);
						}
					}else {
						SimpleUtils.fail("Badges failed to load on Profile page!", true);
					}
				}
			}else {
				SimpleUtils.fail("Failed to find the title: BADGES!", true);
			}
		}else{
			SimpleUtils.fail("BADGES failed to load!", true);
		} */
		return badgeID;
	}

	@Override
	public void verifyTheVisibleOfBadgesOnTeamRoster(String firstName, String badgeID) throws Exception {
		WebElement badge = null;
		if (isElementLoaded(searchTextBox, 5)) {
			searchTextBox.sendKeys(firstName);
			waitForSeconds(1);
			if (areListElementVisible(teamMemberNames, 5)) {
				if (isElementLoaded(badgeIcon, 5)) {
					badge = badgeIcon.findElement(By.id(badgeID));
				}
				if (badge != null) {
					SimpleUtils.pass("Badge: " + badgeID + " is visible on Team Roster!");
				} else {
					SimpleUtils.fail("Badge: " + badgeID + " failed to load on Team Roster!", true);
				}
			} else {
				SimpleUtils.fail("Failed to find the team member: " + firstName, true);
			}
		}else {
			SimpleUtils.fail("Search textbox failed to load!", true);
		}
	}

	@Override
	public boolean isInviteTeamMemberWindowLoaded() throws Exception {
		boolean isLoaded = false;
		if (isElementLoaded(inviteTMWindow, 5) && isElementLoaded(inviteTMSection, 5)) {
			isLoaded = true;
			SimpleUtils.pass("Invite Team Member Window sections are loaded!");
		}else {
			SimpleUtils.fail("Invite Team Member window failed to load!", false);
		}
		return isLoaded;
	}

	@Override
	public void verifyTheEmailFormatOnInviteWindow(List<String> testEmails) throws Exception {
		String regex = "^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}" +
				"\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,10}))$";
		String errorMessage = "Email is invalid.";
		if (isElementEnabled(emailInput, 5) && testEmails.size() > 0) {
			for (String testEmail : testEmails) {
				emailInput.clear();
				emailInput.sendKeys(testEmail);
				if (testEmail.matches(regex)) {
					if (isElementLoaded(emailErrorMsg, 5)){
						SimpleUtils.fail("Email: " + testEmail + "'s format is correct, but error message shows", true);
					}else {
						SimpleUtils.pass("Email: " + testEmail + "'s format is correct!");
					}
				}else{
					if (isElementLoaded(emailErrorMsg, 5)) {
						if (emailErrorMsg.getText().equals(errorMessage)) {
							SimpleUtils.pass("Email: " + testEmail + "'s format is incorrect, error message shows!");
						}
					}else {
						SimpleUtils.fail("Email: " + testEmail + "'s format is incorrect, but error message doesn't show!", true);
					}
				}
			}
		}else {
			SimpleUtils.fail("Email Input failed to load!", true);
		}
	}

	@Override
	public boolean isSendAndCancelLoadedAndEnabledOnInvite() throws Exception {
		boolean isEnabled = false;
		if (isElementLoaded(cancelInviteButton, 5) && isElementLoaded(sendInviteButton, 5)) {
			SimpleUtils.pass("Cancel and Send buttons are Loaded!");
			if (isElementEnabled(cancelInviteButton, 5) && isElementEnabled(sendInviteButton, 5)) {
				SimpleUtils.pass("Cancel and Send buttons are available!");
				isEnabled = true;
			} else {
				SimpleUtils.fail("Cancel and Send buttons are not available!", true);
			}
		}else {
			SimpleUtils.fail("Cancel and Send buttons are failed to load!", true);
		}
		return isEnabled;
	}

	@Override
	public String addANewTeamMemberToInvite(Map<String, String> newTMDetails) throws Exception {
		String firstName = null;
		String successfulMsg = "The team member has been added.";
		firstName = checkAndFillInTheFieldsToCreateInviteTM(newTMDetails);
		if (isElementEnabled(saveTMButton, 5)) {
			SimpleUtils.pass("Save button on new TM page is enabled!");
			scrollToBottom();
			click(saveTMButton);
			if (isElementLoaded(popupMessage, 5)) {
				if (popupMessage.getText().equals(successfulMsg)) {
					SimpleUtils.pass("The New Team member is added successfully");
				}else {
					SimpleUtils.fail("The message is incorrect!", true);
				}
			}
		} else {
			SimpleUtils.fail("Save Button is not enabled!", true);
		}
		return firstName;
	}

	@Override
	public void saveTheNewTeamMember() throws Exception {
		if (isSaveButtonOnNewTMPageEnabled()) {
			scrollToBottom();
			click(saveTMButton);
		}
	}

	@Override
	public String fillInMandatoryFieldsOnNewTMPage(Map<String, String> newTMDetails, String mandatoryField) throws Exception {
		String email = "Email";
		String phoneNumber = "Phone Number";
		String firstName = newTMDetails.get("FIRST_NAME") + new Random().nextInt(200) + new Random().nextInt(200);
		firstNameInput.sendKeys(firstName);
		lastNameInput.sendKeys(newTMDetails.get("LAST_NAME"));
		if (mandatoryField.equals(email)) {
			emailInputTM.sendKeys(newTMDetails.get("EMAIL"));
		}
		if (mandatoryField.equals(phoneNumber)) {
			phoneInput.sendKeys(newTMDetails.get("PHONE"));
		}
		click(dateHiredInput);
		if (areListElementVisible(realDays, 5) && isElementLoaded(todayHighlighted, 5)) {
			click(todayHighlighted);
		}
		employeeIDInput.sendKeys( "E" + new Random().nextInt(200) + new Random().nextInt(200) + new Random().nextInt(200));
		selectByVisibleText(jobTitleSelect, newTMDetails.get("JOB_TITLE"));
		selectByVisibleText(engagementStatusSelect, newTMDetails.get("ENGAGEMENT_STATUS"));
		selectByVisibleText(hourlySelect, newTMDetails.get("HOURLY"));
		selectByVisibleText(salariedSelect, newTMDetails.get("SALARIED"));
		selectByVisibleText(exemptSelect, newTMDetails.get("EXEMPT"));
		return firstName;
	}

	@Override
	public void checkAddATMMandatoryFieldsAreLoaded(String mandatoryField) throws Exception {
		String email = "Email";
		String phoneNumber = "Phone Number";
		isElementLoadedAndPrintTheMessage(firstNameInput, "FIRST NAME Input");
		isElementLoadedAndPrintTheMessage(lastNameInput, "LAST NAME Input");
		if (mandatoryField.equals(email)) {
			isElementLoadedAndPrintTheMessage(emailInputTM, "EMAIL Input");
		}
		if (mandatoryField.equals(phoneNumber)) {
			isElementLoadedAndPrintTheMessage(phoneInput, "PHONE Input");
		}
		isElementLoadedAndPrintTheMessage(dateHiredInput, "DATE HIRED Input");
		isElementLoadedAndPrintTheMessage(employeeIDInput, "EMPLOYEE ID Input");
		isElementLoadedAndPrintTheMessage(jobTitleSelect, "JOB TITLE SELECT");
		isElementLoadedAndPrintTheMessage(engagementStatusSelect, "ENGAGEMENT STATUS SELECT");
		isElementLoadedAndPrintTheMessage(hourlySelect, "HOURLY SELECT");
		isElementLoadedAndPrintTheMessage(salariedSelect, "SALARIED SELECT");
		isElementLoadedAndPrintTheMessage(exemptSelect, "EXEMPT SELECT");
		isElementLoadedAndPrintTheMessage(homeStoreLabel, "HOME STORE LOCATION");
	}

	private String checkAndFillInTheFieldsToCreateInviteTM(Map<String, String> newTMDetails) throws Exception {
		String firstName = "";
		if (getFirstNameForNewHire() == null || getFirstNameForNewHire().isEmpty() || getFirstNameForNewHire().equalsIgnoreCase("")) {
			firstName = newTMDetails.get("FIRST_NAME") + new Random().nextInt(200) + new Random().nextInt(200);
		} else {
			firstName = getFirstNameForNewHire();
		}
		setFirstNameForNewHire(firstName);
		isElementLoadedAndPrintTheMessage(firstNameInput, "FIRST NAME Input");
		isElementLoadedAndPrintTheMessage(lastNameInput, "LAST NAME Input");
		isElementLoadedAndPrintTheMessage(emailInputTM, "EMAIL Input");
		isElementLoadedAndPrintTheMessage(phoneInput, "PHONE Input");
		isElementLoadedAndPrintTheMessage(dateHiredInput, "DATE HIRED Input");
		isElementLoadedAndPrintTheMessage(employeeIDInput, "EMPLOYEE ID Input");
		isElementLoadedAndPrintTheMessage(jobTitleSelect, "JOB TITLE SELECT");
		isElementLoadedAndPrintTheMessage(engagementStatusSelect, "ENGAGEMENT STATUS SELECT");
		isElementLoadedAndPrintTheMessage(hourlySelect, "HOURLY SELECT");
		isElementLoadedAndPrintTheMessage(salariedSelect, "SALARIED SELECT");
		isElementLoadedAndPrintTheMessage(exemptSelect, "EXEMPT SELECT");
		isElementLoadedAndPrintTheMessage(homeStoreLabel, "HOME STORE LOCATION");
		firstNameInput.sendKeys(firstName);
		lastNameInput.sendKeys(newTMDetails.get("LAST_NAME"));
		setLastNameForNewHire(newTMDetails.get("LAST_NAME"));
		String[] email = newTMDetails.get("EMAIL").split("@");
		String emailInput = "";
		if (getEmailAccount() == null || getEmailAccount().isEmpty() || getEmailAccount().equals("")) {
			emailInput = email[0] + "+" + (char) (new Random().nextInt(26) + 96) + (char) (new Random().nextInt(26) + 96) + +new Random().nextInt(200) + "@" + email[1];
		} else {
			emailInput = getEmailAccount();
		}
		emailInputTM.sendKeys(emailInput);
		setEmailAccount(emailInput);
		phoneInput.sendKeys(newTMDetails.get("PHONE"));
		setPhoneForNewHire(newTMDetails.get("PHONE"));
		click(dateHiredInput);
		if (areListElementVisible(realDays, 5) && isElementLoaded(todayHighlighted, 5)) {
			click(todayHighlighted);
		}
		String employeeId = "";
		if (getEmployeeIdForNewHire() == null || getEmployeeIdForNewHire().isEmpty() || getEmployeeIdForNewHire().equals("")) {
			employeeId = "E" + new Random().nextInt(200) + new Random().nextInt(200) + new Random().nextInt(200);
		} else {
			employeeId = getEmployeeIdForNewHire();
		}
		setEmployeeIdForNewHire(employeeId);
		employeeIDInput.sendKeys(employeeId);
		selectByVisibleText(jobTitleSelect, newTMDetails.get("JOB_TITLE"));
		selectByVisibleText(engagementStatusSelect, newTMDetails.get("ENGAGEMENT_STATUS"));
		selectByVisibleText(hourlySelect, newTMDetails.get("HOURLY"));
		selectByVisibleText(salariedSelect, newTMDetails.get("SALARIED"));
		selectByVisibleText(exemptSelect, newTMDetails.get("EXEMPT"));
		return firstName;
	}

	@Override
	public void verifyThereSectionsAreLoadedOnInviteWindow() throws Exception {
		String contact = "Verify Contact Information";
		String availability = "Enter Committed Availability";
		String welcome = "Personalize Welcome Message";
		if (isElementLoaded(inviteTMSection, 5)) {
			List<WebElement> threeSections = inviteTMSection.findElements(By.tagName("h2"));
			if (areListElementVisible(threeSections, 10)) {
				if (threeSections.size() == 3) {
					if (threeSections.get(0).getText().equals(contact)) {
						SimpleUtils.pass(contact + " section loaded!");
					} else {
						SimpleUtils.fail(contact + " failed to load!", true);
					}
					if (threeSections.get(1).getText().equals(availability)) {
						SimpleUtils.pass(availability + " section loaded!");
					} else {
						SimpleUtils.fail(availability + " failed to load!", true);
					}
					if (threeSections.get(2).getText().equals(welcome)) {
						SimpleUtils.pass(welcome + " section loaded!");
					} else {
						SimpleUtils.fail(welcome + " failed to load!", true);
					}
				} else {
					SimpleUtils.fail("Should load 3 sections, but load " + threeSections.size() + " section(s).", true);
				}
			} else {
				SimpleUtils.fail("Three sections failed to load!", true);
			}
		} else {
			SimpleUtils.fail("Invite TM section failed to load!", true);
		}
	}

	@Override
	public boolean isSaveButtonOnNewTMPageEnabled() throws Exception {
		boolean isEnabled = false;
		if (isElementEnabled(saveTMButton, 5)) {
			isEnabled = true;
			SimpleUtils.pass("Save Button on Add New Team member page is enabled!");
		} else {
			SimpleUtils.fail("Save button isn't enabled!", true);
		}
		return isEnabled;
	}

	@Override
	public void verifyContactNumberFormatOnNewTMPage(List<String> contactNumbers) throws Exception {
		String regex = "^[(]{0,1}[0-9]{3}[)]{0,1}[-\\s\\.]{0,1}[0-9]{3}[-\\s\\.]{0,1}[0-9]{4}$";
		String phone = "Phone";
		if (isElementEnabled(phoneInput, 5) && contactNumbers.size() > 0) {
			for (String contactNumber : contactNumbers) {
				phoneInput.clear();
				phoneInput.sendKeys(contactNumber);
				if (contactNumber.matches(regex)) {
					if (isElementLoaded(contactErrorMsg, 5)) {
						if (contactErrorMsg.getText().contains(phone)) {
							SimpleUtils.fail("Phone: " + contactNumber + "'s format is correct, but error message shows", true);
						} else {
							SimpleUtils.pass("Phone: " + contactNumber + "'s format is correct!");
						}
					}else {
						SimpleUtils.pass("Phone: " + contactNumber + "'s format is correct!");
					}
				}else{
					if (isElementLoaded(contactErrorMsg, 5)) {
						if (contactErrorMsg.getText().contains(phone)) {
							SimpleUtils.pass("Phone: " + contactNumber + "'s format is incorrect, error message shows!");
						}
					}else {
						SimpleUtils.fail("Phone: " + contactNumber + "'s format is incorrect, but error message doesn't show!", true);
					}
				}
			}
		}else {
			SimpleUtils.fail("Phone Input failed to load!", true);
		}
	}

	@Override
	public int verifyTMCountIsCorrectOnRoster() throws Exception {
		int count = 0;
		if (areListElementVisible(teamMemberNames, 5) && isElementLoaded(tmCount, 5)) {
			String countOnRoster = tmCount.getText().substring(tmCount.getText().indexOf("(") + 1, tmCount.getText().indexOf(")"));
			try {
				count = Integer.parseInt(countOnRoster);
				if (count == teamMemberNames.size()) {
					SimpleUtils.pass("TM Count is correct On roster page!");
				} else {
					SimpleUtils.fail("TM Count is incorrect on roster page!", true);
				}
			}catch (Exception e){
				SimpleUtils.fail("Parse String to Integer failed!", false);
			}
		} else if (areListElementVisible(newTeamMemberNames, 5) && isElementLoaded(newTMCount, 5)) {
			String countOnRoster = newTMCount.getText().substring(newTMCount.getText().indexOf("(") + 1, newTMCount.getText().indexOf(")")).trim();
			try {
				count = Integer.parseInt(countOnRoster);
			}catch (Exception e){
				SimpleUtils.fail("Parse String to Integer failed!", false);
			}
		} else {
			SimpleUtils.fail("Team Members and team count failed to load!", true);
		}
		return count;
	}

	@Override
	public void verifyCancelButtonOnAddTMIsEnabled() throws Exception {
		if (isElementLoaded(cancelButtonAddTM, 5)) {
			SimpleUtils.pass("Cancel Button loaded successfully!");
			if (isElementEnabled(cancelButtonAddTM, 5)) {
				SimpleUtils.pass("Cancel Button is Enabled by default!");
			} else {
				SimpleUtils.fail("Cancel Button is not enabled!", true);
			}
		}else {
			SimpleUtils.fail("Cancel Button failed to load!", true);
		}
	}

	@Override
	public void clickCancelButton() throws Exception {
		if (isElementLoaded(cancelButtonAddTM, 5)) {
			click(cancelButtonAddTM);
		}
	}

	@Override
	public void verifyTheMandatoryFieldsCannotEmpty() throws Exception {
		if (isMandatoryElement(firstNameInput))
			SimpleUtils.pass("Checked First Name Input cannot be empty when adding a new TM!");
		else
			SimpleUtils.fail("First Name Input can be empty, but it is a mandatory field!", true);
		if (isMandatoryElement(lastNameInput))
			SimpleUtils.pass("Checked Last Name Input cannot be empty when adding a new TM!");
		else
			SimpleUtils.fail("Last Name Input can be empty, but it is a mandatory field!", true);
		if (isMandatoryElement(emailInputTM))
			SimpleUtils.pass("Checked Email Input cannot be empty when adding a new TM!");
		else
			SimpleUtils.fail("Email Input can be empty, but it is a mandatory field!", true);
		if (isMandatoryElement(dateHiredInput))
			SimpleUtils.pass("Checked Date Hired Input cannot be empty when adding a new TM!");
		else
			SimpleUtils.fail("Date Hired Input can be empty, but it is a mandatory field!", true);
		if (isMandatoryElement(employeeIDInput))
			SimpleUtils.pass("Checked Employee ID Input cannot be empty when adding a new TM!");
		else
			SimpleUtils.fail("Employee ID Input can be empty, but it is a mandatory field!", true);
		if (isMandatoryElement(jobTitleSelect))
			SimpleUtils.pass("Checked Job Title Select cannot be empty when adding a new TM!");
		else
			SimpleUtils.fail("Job Title Select can be empty, but it is a mandatory field!", true);
		if (isMandatoryElement(engagementStatusSelect))
			SimpleUtils.pass("Checked Engagement Status Select cannot be empty when adding a new TM!");
		else
			SimpleUtils.fail("Engagement Status Select can be empty, but it is a mandatory field!", true);
		if (isMandatoryElement(hourlySelect))
			SimpleUtils.pass("Checked Hourly Select cannot be empty when adding a new TM!");
		else
			SimpleUtils.fail("Hourly Select can be empty, but it is a mandatory field!", true);
		if (isMandatoryElement(salariedSelect))
			SimpleUtils.pass("Checked Salaried Select cannot be empty when adding a new TM!");
		else
			SimpleUtils.fail("Salaried Select can be empty, but it is a mandatory field!", true);
		if (isMandatoryElement(exemptSelect))
			SimpleUtils.pass("Checked Exempt Select cannot be empty when adding a new TM!");
		else
			SimpleUtils.fail("Exempt Select can be empty, but it is a mandatory field!", true);
		if (isElementLoaded(homeStoreLabel) && homeStoreLabel != null && !homeStoreLabel.getText().isEmpty())
			SimpleUtils.pass("Checked Home Store has value!");
		else
			SimpleUtils.fail("Home Store is empty, but it is a mandatory field!", true);
	}

	@Override
	public void verifyTMIsVisibleAndInvitedOnTODO(String name) throws Exception {
		boolean isVisible = false;
		String cardTitle = "ONBOARD NEW TEAM MEMBER";
		String tmMessage = name + " is a new Team Member";
		String inviteMessage = "Invite TM to create a Legion account";
		String dismiss = "DISMISS";
		String invite = "INVITE TO LEGION";
		if (areListElementVisible(todoCards, 10)) {
			for (WebElement todoCard : todoCards) {
				WebElement title = todoCard.findElement(By.cssSelector("div.todo-header"));
				WebElement teamMember = todoCard.findElement(By.cssSelector("p.heading"));
				WebElement inviteMsgElement = todoCard.findElement(By.cssSelector("span[ng-bind-html=\"p.text\"]"));
				if (isElementLoaded(title, 10) && isElementLoaded(teamMember, 10) && isElementLoaded(inviteMsgElement, 10)) {
					if (title.getText().contains(cardTitle) && teamMember.getText().equals(tmMessage) && inviteMsgElement.getText().equals(inviteMessage)) {
						WebElement dismissButton = todoCard.findElement(By.cssSelector("a.text-grey"));
						WebElement inviteButton = todoCard.findElement(By.cssSelector("a.text-green"));
						if (isElementLoaded(dismissButton, 10) && isElementLoaded(inviteButton, 10)) {
							if (dismissButton.getText().equals(dismiss) && inviteButton.getText().equals(invite)) {
								SimpleUtils.pass("Team member: " + name + " is visible and invited on TODO!");
								isVisible = true;
								break;
							}
						}
					} else {
						continue;
					}
				} else {
					continue;
				}
			}
		} else {
			SimpleUtils.fail("TODO cards failed to load!", false);
		}
		if (!isVisible) {
			SimpleUtils.fail("Failed to find the team member:" + name + " on TODO!", true);
		}
	}

	@Override
	public void	verifyInviteAndReInviteButtonThenInvite() throws Exception {
		boolean isAvailable = false;
		if (isInviteButtonAvailable()) {
			click(inviteButton);
			isAvailable = true;
		}
		if (!isAvailable) {
			SimpleUtils.fail("INVITE/REINVITE button is unavailable!", true);
		}
	}

	@Override
	public boolean isProfilePageSelected() throws Exception {
		boolean isProfile = false;
		String titleOpen = "collapsible-title-open";
		scrollToTop();
		if (isElementLoaded(profileTab, 10)) {
			String className = profileTab.getAttribute("class");
			if (className.contains(titleOpen)) {
				SimpleUtils.pass("Profile Tab is open!");
				isProfile = true;
			}
		} else {
			SimpleUtils.fail("Profile tab failed to load!", false);
		}
		return isProfile;
	}

	@Override
	public void navigateToProfileTab() throws Exception {
		if (isElementLoaded(profileTab, 10)) {
			click(profileTab);
			if (isProfilePageSelected()) {
				SimpleUtils.pass("Navigate to profile tab successfully!");
			} else {
				SimpleUtils.fail("Failed to navigate to the profile tab.", false);
			}
		} else {
			SimpleUtils.fail("Profile tab failed to load!", false);
		}
	}

	@Override
	public void navigateToWorkPreferencesTab() throws Exception {
		scrollToTop();
		if (isElementLoaded(workPreferTab, 10)) {
			click(workPreferTab);
			if (isWorkPreferencesPageLoaded()) {
				SimpleUtils.pass("Navigate to Work Preferences tab successfully!");
			} else {
				SimpleUtils.fail("Failed to navigate to the Work Preferences tab.", false);
			}
		} else {
			SimpleUtils.fail("Work Preferences tab failed to load!", false);
		}
	}

	@Override
	public boolean isWorkPreferencesPageLoaded() throws Exception {
		boolean isLoaded = false;
		if (areListElementVisible(userProfileSections, 10)) {
			isLoaded = true;
		}
		return isLoaded;
	}

	@Override
	public void navigateToTimeOffPage() throws Exception {
		if (isElementLoaded(timeOffTab, 5)) {
			click(timeOffTab);
			if (isElementLoaded(newTimeOffBtn, 15)) {
				SimpleUtils.pass("Navigate to Time Off page Successfully!");
			}else {
				SimpleUtils.fail("Time Off page not loaded Successfully!", true);
			}
		}else {
			SimpleUtils.fail("Time Off tab title not loaded Successfully!", true);
		}
	}

	@Override
	public void clickOnActivateButton() throws Exception {
		if (isElementLoaded(activateButton, 10)) {
			click(activateButton);
		} else {
			SimpleUtils.fail("Activate button failed to load on Profile Tab!", false);
		}
	}

	@Override
	public void isActivateWindowLoaded() throws Exception {
		if (isElementLoaded(removeWindow, 10)) {
			SimpleUtils.pass("Activate window loaded successfully!");
		} else {
			SimpleUtils.fail("Activate window failed to load!", false);
		}
	}

	@Override
	public void selectADateOnCalendarAndActivate() throws Exception {
		String successfulMsg = "Successfully scheduled activation of Team Member.";
		String actualMsg = null;
		if (isElementLoaded(currentDay, 10)) {
			click(currentDay);
			if (isElementEnabled(applyButton, 10)) {
				click(applyButton);
				if (isElementLoaded(popupMessage, 10)) {
					actualMsg = popupMessage.getText();
					if (actualMsg.equals(successfulMsg)) {
						SimpleUtils.pass("Activate the Team Member successfully!");
					} else {
						SimpleUtils.fail("Failed to activate the Team member", false);
					}
				}
			}else {
				SimpleUtils.fail("Activate button on activate window is disabled!", false);
			}
		}else {
			SimpleUtils.fail("Calendar failed to load!", false);
		}
	}

	@Override
	public void verifyDeactivateAndTerminateEnabled() throws Exception {
		if (isElementLoaded(deactivateButton, 10) && isElementLoaded(terminateButton, 10)) {
			if (isElementEnabled(deactivateButton, 5) && isElementEnabled(terminateButton, 5)) {
				SimpleUtils.pass("DEACTIVATE and TERMINATE button are enabled!");
			}else {
				SimpleUtils.fail("DEACTIVATE and TERMINATE button are not enabled!", false);
			}
		}else {
			SimpleUtils.fail("DEACTIVATE and TERMINATE button are not loaded!", false);
		}
	}

	@Override
	public String getOnBoardedDate() throws Exception {
		if (isElementLoaded(onBoardedDate, 10)) {
			return onBoardedDate.getText();
		}else {
			return null;
		}
	}

	@Override
	public void isOnBoardedDateUpdated(String previousDate) throws Exception {
		if (isElementLoaded(onBoardedDate, 10)) {
			if (!onBoardedDate.getText().equals(previousDate)) {
				SimpleUtils.pass("On Boarded Date is updated!");
			}else {
				SimpleUtils.fail("On Boarded Date is not updated!", true);
			}
		}else {
			SimpleUtils.fail("On boarded date failed to load!", false);
		}
	}

	@Override
	public void verifyTheStatusOfTeamMember(String expectedStatus) throws Exception {
		if (teamMembers.size() > 0){
			for (WebElement teamMember : teamMembers){
				if (teamMember != null) {
					WebElement status = teamMember.findElement(By.cssSelector("[data-testid=\"lg-table-legion-onboarding\"] span"));
					if (status != null) {
						if (expectedStatus.equals(status.getText())) {
							SimpleUtils.pass("Team member's status is correct!");
						}else {
							SimpleUtils.fail("Team member's status is incorrect!", true);
						}
					}else {
						SimpleUtils.fail("Failed to find the Status!", true);
					}
				}else {
					SimpleUtils.fail("Failed to find the tr element!", true);
				}
			}
		}
	}

	@Override
	public boolean isActivateButtonLoaded() throws Exception {
		boolean isLoaded = false;
		if (isElementLoaded(activateButton, 5)) {
			isLoaded = true;
		}
		return isLoaded;
	}

	@Override
	public void sendTheInviteViaEmail(String email) throws Exception {
		if (isSendAndCancelLoadedAndEnabledOnInvite()) {
			if (isElementLoaded(emailInput, 5)) {
				emailInput.clear();
				emailInput.sendKeys(email);
			}
			click(sendInviteButton);
			waitUntilElementIsInVisible(inviteTMWindow);
		} else {
			SimpleUtils.fail("Send Invitation Button failed to load!", false);
		}
	}

	@Override
	public void searchTheNewTMAndUpdateInfo(String firstName) throws Exception {
		if (isElementLoaded(searchTextBox, 5)) {
			searchTextBox.sendKeys(firstName);
			waitForSeconds(2);
			if (areListElementVisible(updateInfoButtons, 5)){
				if (updateInfoButtons.size() > 0) {
					// The newly created team member is at the last.
					click(updateInfoButtons.get(updateInfoButtons.size() - 1));
				} else {
					SimpleUtils.fail("Can't find the new team member!", false);
				}
			}else {
				SimpleUtils.fail("There is no Update Info Button loaded!", true);
			}
		}else {
			SimpleUtils.fail("Search textBox failed to load!", false);
		}
	}

	@Override
	public boolean isEmailOrPhoneNumberEmptyAndUpdate(Map<String, String> newTMDetails, String mandatoryField) throws Exception {
		boolean isEmpty = false;
		String email = "Email";
		String phone = "Phone Number";
		String emailValue = null;
		String phoneValue = null;
		if (isElementLoaded(phoneInput, 10) && isElementLoaded(emailInputTM, 10)) {
			emailValue = emailInputTM.getText();
			phoneValue = phoneInput.getText();
			if (email.equals(mandatoryField) && (phoneValue == null || (phoneValue != null && phoneValue.isEmpty()))) {
				isEmpty = true;
				SimpleUtils.pass("Email is a mandatory field, and phone number is empty, waiting for updated");
				phoneInput.sendKeys(newTMDetails.get("PHONE"));
			}
			if (phone.equals(mandatoryField) && (emailValue == null || (emailValue != null && emailValue.isEmpty()))) {
				isEmpty = true;
				SimpleUtils.pass("Phone Number is a mandatory field, and email is empty, waiting for updated");
				emailInputTM.sendKeys(newTMDetails.get("EMAIL"));
			}
		} else {
			SimpleUtils.fail("Phone and Email inputs failed to load!", true);
		}
		if (isEmpty) {
			scrollToBottom();
			click(saveTMButton);
			waitUntilElementIsInVisible(saveTMButton);
		}else {
			SimpleUtils.fail("Update Info button shows when email or phone is empty, but they are not empty!", false);
		}
		return isEmpty;
	}

	@Override
	public void searchTheTMAndCheckUpdateInfoNotShow(String firstName) throws Exception {
		if (isElementLoaded(searchTextBox, 5)) {
			searchTextBox.sendKeys(firstName);
			waitForSeconds(2);
			if (areListElementVisible(updateInfoButtons, 10)) {
				SimpleUtils.fail("Update Info button still shows after updating the info!", false);
			} else {
				SimpleUtils.pass("Update Info Button doesn't show after updating the info!");
			}
		}else {
			SimpleUtils.fail("Search textBox failed to load!", false);
		}
	}

	@Override
	public boolean isTerminateButtonLoaded() throws Exception {
		boolean isLoaded = false;
		if (isElementLoaded(terminateButton, 10)) {
			SimpleUtils.pass("Terminate Button is Loaded!");
			isLoaded = true;
		}else {
			SimpleUtils.fail("Terminate Button failed to load!", false);
		}
		return isLoaded;
	}

	@Override
	public boolean isCancelTerminateButtonLoaded() throws Exception {
		boolean isLoaded = false;
		if (isElementLoaded(cancelTerminateButton, 5)) {
			SimpleUtils.pass("Cancel Terminate Button is Loaded!");
			isLoaded = true;
		}
		return isLoaded;
	}

	@Override
	public void terminateTheTeamMember(boolean isCurrentDay) throws Exception {
		String removeMsg = "Successfully scheduled removal of Team Member from Roster.";
		String actualMsg = "";
		scrollToBottom();
		if (isElementLoaded(terminateButton, 10)) {
			click(terminateButton);
			isTerminateWindowLoaded();
			if (isElementLoaded(currentDay, 10) && isElementLoaded(applyButton)) {
				if (isCurrentDay) {
					click(currentDay);
				} else {
					selectAFutureDateFromCalendar();
				}
				click(applyButton);
				if (isElementLoaded(confirmPopupWindow, 15) && isElementLoaded(confirmButton, 15)) {
					click(confirmButton);
					if (isElementLoaded(popupMessage, 15)) {
						actualMsg = popupMessage.getText();
						if (removeMsg.equals(actualMsg)) {
							SimpleUtils.pass("Terminate the team member successfully!");
						} else {
							SimpleUtils.fail("The pop up message is incorrect!", false);
						}
					}
				} else {
					SimpleUtils.fail("Confirm window doesn't show!", false);
				}
			} else {
				SimpleUtils.fail("Current day and apply button doesn't show!", false);
			}
		} else {
			SimpleUtils.fail("Terminate button failed to load on Profile page!", false);
		}
	}

	@Override
	public String getEmployeeIDFromProfilePage() throws Exception {
		String employeeIDText = null;
		if (isElementLoaded(employeeID, 10)) {
			employeeIDText = employeeID.getText();
		}else {
			SimpleUtils.fail("EMPLOYEE ID failed to load!", false);
		}
		return employeeIDText;
	}

	@Override
	public void searchTheTeamMemberByEmployeeIDFromRoster(String employeeID, boolean isTerminated) throws Exception {
		if (isElementLoaded(searchTextBox, 5)) {
			searchTextBox.sendKeys(employeeID);
			waitForSeconds(2);
			if (isTerminated) {
				if (areListElementVisible(teamMemberNames, 10)) {
					SimpleUtils.fail("Team Member still shows after terminating it!", false);
				} else {
					SimpleUtils.pass("Team member can't be found from Roster after terminating it!");
				}
			}else {
				if (areListElementVisible(teamMemberNames, 10)) {
					SimpleUtils.pass("Team Member shows after cancel terminating it!");
				} else {
					SimpleUtils.fail("Team member should be found from Roster after cancel terminating it!", false);
				}
			}
		}else {
			SimpleUtils.fail("Search textBox failed to load!", false);
		}
	}

	@Override
	public List<String> getTMNameList() throws Exception {
		List<String> nameList = new ArrayList<>();
		if (areListElementVisible(teamMemberNames, 10)) {
			for (WebElement name : teamMemberNames) {
				nameList.add(name.getText());
			}
		} else {
			SimpleUtils.fail("Roster Page: Team members' name list not loaded Successfully!", false);
		}
		return nameList;
	}

	@Override
	public void verifyTheFunctionOfCancelTerminate() throws Exception {
		String cancelMsg = "Successfully cancelled removal of Team Member from Roster.";
		String actualMsg = "";
		isCancelTerminateButtonLoaded();
		click(cancelTerminateButton);
		if (isElementLoaded(confirmPopupWindow, 15) && isElementLoaded(confirmButton, 15)) {
			click(confirmButton);
			if (isElementLoaded(popupMessage, 15)) {
				actualMsg = popupMessage.getText();
				if (cancelMsg.equals(actualMsg)) {
					SimpleUtils.pass("Cancelled Terminated successfully!");
				}else {
					SimpleUtils.fail("Failed to cancel the termination!", false);
				}
			}
		}else{
			SimpleUtils.fail("Confirm pop up window failed to load!", false);
		}
	}

	@Override
	public boolean isManualOnBoardButtonLoaded() throws Exception {
		boolean isLoaded = false;
		if (isElementLoaded(manualOnBoardButton, 10)) {
			isLoaded = true;
			SimpleUtils.pass("Manual Onboard Button Loaded Successfully!");
		}else{
			SimpleUtils.report("Manual Onboard Button failed to load!");
		}
		return isLoaded;
	}

	@FindBy (css = "[label=\"okLabel()\"] button")
	private WebElement confirmBtn;
	@Override
	public void manualOnBoardTeamMember() throws Exception {
		String successfulMsg = "Team member successfully On-boarded.";
		String actualMsg = "";
		if (isElementLoaded(manualOnBoardButton, 5)) {
			click(manualOnBoardButton);
		}else {
			SimpleUtils.fail("Manual OnBoard button failed to load!", true);
		}
		if (isElementLoaded(confirmPopupWindow, 15) && isElementLoaded(confirmBtn, 15)) {
			click(confirmBtn);
			if (isElementLoaded(popupMessage, 15)) {
				actualMsg = popupMessage.getText();
				if (successfulMsg.equals(actualMsg)) {
					SimpleUtils.pass("Manual OnBoard the team member successfully!");
				}else {
					SimpleUtils.fail("Manual OnBoard message is incorrect! " + actualMsg, false);
				}
			}
		}else {
			SimpleUtils.fail("Manual OnBoard Pop up window doesn't show!", false);
		}
	}

	@Override
	public String selectATeamMemberToViewProfile() throws Exception {
		String teamMember = null;
		List<WebElement> names = null;
		if (areListElementVisible(teamMemberNames, 15)) {
			names = teamMemberNames;
		} else if (areListElementVisible(newTeamMemberNames, 15)) {
			names = newTeamMemberNames;
		}
		if (names.size() > 0) {
			Random random = new Random();
			int randomIndex = random.nextInt(names.size());
			teamMember = names.get(randomIndex).getText();
			clickTheElement(names.get(randomIndex));
		} else {
			SimpleUtils.fail("Team Members are failed to load!", false);
		}
		return teamMember;
	}

	// Added by Nora: For Work Preferences
	@FindBy (css = "div.adjust-hours div.value")
	private List<WebElement> quickPrefers;
	@FindBy (css = "[class=\"receiveOffers\"]>div.ng-scope")
	private WebElement vsl;
	@FindBy (css = "[class=\"receiveOffers\"]>[class=\"ng-binding\"]")
	private WebElement otherPreferredLocation;
	@FindBy (css = "work-preference-management [label=\"Edit\"]")
	private WebElement editShiftPreferButton;
	@FindBy (css = "[label=\"Cancel\"]")
	private WebElement cancelEditButton;
	@FindBy (css = "[label=\"Save\"]")
	private WebElement savePreferButton;
	@FindBy (className = "edit-pref-values")
	private List<WebElement> editPrefValues;
	@FindBy (className = "check-box-circle")
	private List<WebElement> shiftPrefChkes;
	@FindBy (css = "lgn-tm-edit-availability>div>div:nth-child(2) li")
	private List<WebElement> hoursPerWeek;
	@FindBy (css = "lgn-tm-edit-availability>div>div:nth-child(3) li")
	private List<WebElement> shiftsLength;
	@FindBy (css = "lgn-tm-edit-availability>div>div:nth-child(4) li")
	private List<WebElement> shiftsPerWeek;
	@FindBy (className = "rz-pointer-min")
	private List<WebElement> startNodes;
	@FindBy (className = "rz-pointer-max")
	private List<WebElement> endNodes;
	@FindBy (css = ".fa.fa-lock")
	private WebElement lockBtn;
	@FindBy (css = "availability-management [label=\"Edit\"]")
	private WebElement editAvailability;
	@FindBy (className = "lgn-action-button-success")
	private WebElement unLockButton;
	@FindBy (className = "modal-content")
	private WebElement unlockRemindWindow;
	@FindBy (css = "availability-management [label=\"Cancel\"]")
	private WebElement cancelAvailability;
	@FindBy (css = "availability-management [label=\"Save\"]")
	private WebElement saveAvailability;
	@FindBy (css = "div.tab")
	private List<WebElement> availabilityTabs;
	@FindBy (css = "i.fa-remove")
	private WebElement removeIcon;
	@FindBy (className = "availability-box-ghost")
	private List<WebElement> boxGhosts;

	public enum availabilityTabNames{
		Preferred(0),
		Busy(1),
		Committed(2);
		private final int value;
		availabilityTabNames(final int newValue) {
			value = newValue;
		}
		public int getValue() { return value; }
	}

	public enum availabilityColors{
		Green("green-zone"),
		Red("red-zone"),
		GreenColor("#74c479"),
		RedColor("#cc2f33");
		private final String value;
		availabilityColors(final String newValue) {
			value = newValue;
		}
		public String getValue() { return value; }
	}

	@Override
	public void rejectAllTeamMembersTimeOffRequest(ProfileNewUIPage profileNewUIPage, int index) throws Exception {
		if (areListElementVisible(teamMemberNames, 30)) {
			if (index < teamMemberNames.size()) {
				clickTheElement(teamMemberNames.get(index));
				String myTimeOffLabel = "Time Off";
				profileNewUIPage.selectProfilePageSubSectionByLabel(myTimeOffLabel);
				rejectAllTheTimeOffRequests();
				goToTeam();
				rejectAllTeamMembersTimeOffRequest(profileNewUIPage, index + 1);
			}
		} else {
			SimpleUtils.fail("Team Members are failed to load!", false);
		}
	}

	@Override
	public void changePreferredHours() throws Exception {
		if (availabilityTabNames.Preferred.getValue() != whichAvailabilityTabIsSelected()) {
			selectAvailabilityTabByIndex(availabilityTabNames.Preferred.getValue());
		}
		changeAvailabilityHours(availabilityColors.Green.getValue(), availabilityColors.GreenColor.getValue());
	}

	@Override
	public void changeBusyHours() throws Exception {
		if (availabilityTabNames.Busy.getValue() != whichAvailabilityTabIsSelected()) {
			selectAvailabilityTabByIndex(availabilityTabNames.Busy.getValue());
		}
		changeAvailabilityHours(availabilityColors.Red.getValue(), availabilityColors.RedColor.getValue());
	}

	private void changeAvailabilityHours(String zoneClassName, String expectedColor) throws Exception {
		scrollToBottom();
		waitForSeconds(2);
		List<WebElement> zones = getDriver().findElements(By.className(zoneClassName));
		if (areListElementVisible(zones, 5)) {
			for (int i = 0; i < zones.size(); i++) {
				try {
					WebElement span = zones.get(i).findElement(By.tagName("span"));
					if (span != null){
						mouseHoverDragandDrop(zones.get(i), span);
					}
				}catch (Exception e) {
					moveToElementAndClick(zones.get(i));
				}
				if (isElementLoaded(removeIcon, 5)) {
					click(removeIcon);
				}
			}
		}
		if (areListElementVisible(boxGhosts, 5)) {
			int index = (new Random()).nextInt(boxGhosts.size());
			List<WebElement> hourCells = boxGhosts.get(index).findElements(By.className("hour-cell-ghost"));
			if (hourCells.size() > 0) {
				int min = (new Random()).nextInt(hourCells.size() / 2);
				int max = (new Random()).nextInt(hourCells.size() / 2) + hourCells.size() / 2;
				mouseHoverDragandDrop(hourCells.get(min), hourCells.get(max));
			}
		}
		zones = getDriver().findElements(By.className(zoneClassName));
		if (areListElementVisible(zones, 5)) {
			for (WebElement zone : zones) {
				String actualColor = Color.fromString(zone.getCssValue("background-color")).asHex();
				if (expectedColor.equals(actualColor)) {
					SimpleUtils.pass("Select the hour successfully and the color is correct: " + expectedColor);
				} else {
					SimpleUtils.fail("The Color for the hour cells is incorrect, expected is: " + expectedColor + ", but actual is: " + actualColor, true);
				}
			}
		}else {
			SimpleUtils.fail("Failed to select the hours!", true);
		}
	}

	private int whichAvailabilityTabIsSelected() throws Exception {
		int index = 3;
		if (areListElementVisible(availabilityTabs, 5)) {
			for (int i = 0; i < availabilityTabs.size(); i++) {
				if (availabilityTabs.get(i).getAttribute("class").contains("select")) {
					index = i;
				}
			}
		}else {
			SimpleUtils.fail("Availability Tabs failed to load!", false);
		}
		return index;
	}

	private void selectAvailabilityTabByIndex(int index) throws Exception {
		if (areListElementVisible(availabilityTabs, 5)) {
			if (index < availabilityTabs.size()) {
				click(availabilityTabs.get(index));
			}else {
				SimpleUtils.fail("The index is out of bound!", true);
			}
		}else {
			SimpleUtils.fail("Availability Tabs failed to load!", false);
		}
	}

	@Override
	public List<String> getShiftPreferences() throws Exception {
		List<String> shiftPrefs = new ArrayList<>();
		// Element is loaded, but the data doesn't, so wait for seconds to wait for the data loaded
		waitForSeconds(5);
		if (areListElementVisible(quickPrefers, 5)) {
			if (quickPrefers.size() == 3) {
				for (WebElement quickPrefer : quickPrefers) {
					shiftPrefs.add(quickPrefer.getText());
				}
			}
		}
		if (isElementLoaded(vsl, 5)) {
			shiftPrefs.add(vsl.getText());
		}
		if (isElementLoaded(otherPreferredLocation, 5)) {
			shiftPrefs.add(otherPreferredLocation.getText());
		}
		if (shiftPrefs.size() < 3) {
			SimpleUtils.fail("Failed to get the shift preferences!", true);
		}
		return shiftPrefs;
	}

	@Override
	public void clickOnEditShiftPreference() throws Exception {
		if (isElementLoaded(editShiftPreferButton, 5)) {
			click(editShiftPreferButton);
		}else {
			SimpleUtils.fail("Edit Shift Preferences pencil button failed to load!", true);
		}
	}

	@Override
	public boolean isEditShiftPreferLayoutLoaded() throws Exception {
		boolean isLoaded = false;
		if (isElementLoaded(cancelEditButton, 5) && isElementLoaded(savePreferButton, 5)) {
			isLoaded = true;
		}
		return isLoaded;
	}

	@Override
	public List<String> setSliderForShiftPreferences() throws Exception {
		List<String> shiftPreferences = new ArrayList<>();
		if (areListElementVisible(startNodes, 5) && areListElementVisible(endNodes, 5)) {
			if (startNodes.size() == 3 && endNodes.size() == 3) {
				if (areListElementVisible(hoursPerWeek, 5)) {
					clickSliderAtSomePoint(startNodes.get(0), endNodes.get(0), hoursPerWeek);
				}
				if (areListElementVisible(shiftsLength, 5)) {
					clickSliderAtSomePoint(startNodes.get(1), endNodes.get(1), shiftsLength);
				}
				if (areListElementVisible(shiftsPerWeek, 5)) {
					clickSliderAtSomePoint(startNodes.get(2), endNodes.get(2), shiftsPerWeek);
				}
			}else {
				SimpleUtils.fail("The size of start nodes and end nodes is incorrect!", true);
			}
		}else {
			SimpleUtils.fail("Start nodes and end nodes not loaded Successfully!", true);
		}
		if (areListElementVisible(editPrefValues, 5)) {
			for (WebElement editPrefValue : editPrefValues) {
				shiftPreferences.add(editPrefValue.getText());
			}
		}else {
			SimpleUtils.fail("Edit preferences values failed to load!", true);
		}
		return shiftPreferences;
	}

	@Override
	public List<String> changeShiftPreferencesStatus() throws Exception {
		String enabled = "enabled";
		String otherLocation = "Other preferred locations: ";
		String vsl = "Voluntary Standby List: ";
		String yes = "Yes";
		String no = "No";
		String otherLocationText = "";
		List<String> status = new ArrayList<>();
		if (areListElementVisible(shiftPrefChkes, 5)) {
			for (WebElement shiftPrefChk : shiftPrefChkes) {
				click(shiftPrefChk);
				if (isElementEnabled(confirmButton, 5)) {
					click(confirmButton);
				}
				WebElement element = shiftPrefChk.findElement(By.xpath("./../following-sibling::div[1]"));
				if (element != null)
					otherLocationText = element.getText();
				String className = shiftPrefChk.getAttribute("class");
				if (className != null) {
					String result = className.contains(enabled) ? yes : no;
					if (otherLocationText.contains("other locations")) {
						status.add(otherLocation + result);
					}
					if (otherLocationText.contains("Voluntary")){
						status.add(vsl + result);
					}
				}
			}
		}else {
			SimpleUtils.fail("Shift Preferences checkboxes failed to load!", true);
		}
		return status;
	}

	@Override
	public void clickCancelEditShiftPrefBtn() throws Exception {
		if (isElementLoaded(cancelEditButton, 5)) {
			click(cancelEditButton);
		}else {
			SimpleUtils.fail("Cancel edit shit preferences button failed to load!", true);
		}
	}

	@Override
	public void clickSaveShiftPrefBtn() throws Exception {
		if (isElementLoaded(savePreferButton, 5)) {
			clickTheElement(savePreferButton);
		}else {
			SimpleUtils.fail("Save edit shit preferences button failed to load!", true);
		}
	}

	@Override
	public void verifyCurrentShiftPrefIsConsistentWithTheChanged(List<String> shiftPrefs, List<String> changedShiftPrefs,
																 List<String> status) throws Exception {
		boolean isConsistent = false;
		if (shiftPrefs != null && changedShiftPrefs != null && status != null) {
			if (shiftPrefs.size() == (changedShiftPrefs.size() + status.size())) {
				if (shiftPrefs.containsAll(changedShiftPrefs)) {
					if (shiftPrefs.size() == 4) {
						if (shiftPrefs.get(3).contains(status.get(0))) {
							isConsistent = true;
						}
					} else if (shiftPrefs.size() == 5) {
						if ((shiftPrefs.get(3).contains(status.get(0)) && shiftPrefs.get(4).contains(status.get(1))) ||
								(shiftPrefs.get(3).contains(status.get(1)) && shiftPrefs.get(4).contains(status.get(0)))) {
							isConsistent = true;
						}
					}
				}
				if (isConsistent) {
					SimpleUtils.pass("Current shift preferences are consistent with the changed one.");
				} else {
					SimpleUtils.fail("Current shift preferences are inconsistent with the changed one.", false);
				}
			}else {
				SimpleUtils.fail("Current shift preferences are inconsistent with the changed one.", false);
			}
		}else {
			SimpleUtils.fail("Shift preferences are null!", false);
		}
	}

	@Override
	public void editOrUnLockAvailability() throws Exception {
		scrollToBottom();
		if (isElementLoaded(lockBtn, 5)) {
			click(lockBtn);
			if (isElementLoaded(unLockButton, 5)) {
				click(unLockButton);
				if (isElementLoaded(unlockRemindWindow, 5) && isElementLoaded(unLockButton, 5)) {
					click(unLockButton);
				}
			}
		}
		if (isElementLoaded(editAvailability, 5)) {
			click(editAvailability);
		}
	}

	@Override
	public boolean areCancelAndSaveAvailabilityBtnLoaded() throws Exception {
		boolean areLoaded = false;
		if (isElementLoaded(cancelAvailability, 5) && isElementLoaded(saveAvailability, 5)) {
			areLoaded = true;
		}
		return areLoaded;
	}

	public void clickSliderAtSomePoint(WebElement startNode, WebElement endNode, List<WebElement> liElements) throws Exception {
		String startValue = "";
		String endValue = "";
		String maxValue = "";
		String minValue = "";
		if (isElementLoaded(startNode, 5) && isElementLoaded(endNode, 5)) {
			startValue = startNode.getAttribute("aria-valuenow");
			endValue = endNode.getAttribute("aria-valuenow");
			maxValue = endNode.getAttribute("aria-valuemax");
			minValue = startNode.getAttribute("aria-valuemin");
			if (Integer.parseInt(endValue) > Integer.parseInt(maxValue)) {
				endValue = maxValue;
			}
			if (Integer.parseInt(startValue) < Integer.parseInt(minValue)) {
				startValue = minValue;
			}
		}
		if (areListElementVisible(liElements, 5)) {
			int index = (new Random()).nextInt(liElements.size());
			String value = liElements.get(index).findElement(By.tagName("span")) == null ? "" : liElements.get(index).findElement(By.tagName("span")).getText();
			if (!startValue.equals(value) && !endValue.equals(value)) {
				click(liElements.get(index));
				startValue = startNode.getAttribute("aria-valuenow");
				endValue = endNode.getAttribute("aria-valuenow");
				if (Integer.parseInt(endValue) > Integer.parseInt(maxValue)) {
					endValue = maxValue;
				}
				if (Integer.parseInt(startValue) < Integer.parseInt(minValue)) {
					startValue = minValue;
				}
				SimpleUtils.report("Select value: " + value + " successfully!");
			}
		}else {
			SimpleUtils.fail("li elements failed to load!", true);
		}
	}

	private void selectAFutureDateFromCalendar() throws Exception {
		if (isElementLoaded(nextMonthArrow, 5)){
			click(nextMonthArrow);
			if (areListElementVisible(daysOnCalendar, 15)){
				/*
				 * Generate a random to select a day!
				 */
				Random random = new Random();
				WebElement realDay = daysOnCalendar.get(random.nextInt(daysOnCalendar.size()));
				click(realDay);
			}else {
				SimpleUtils.fail("Days on calendar failed to load!", false);
			}
		}else {
			SimpleUtils.fail("Back and Forward arrows are failed to load!", true);
		}
	}

	private boolean isTerminateWindowLoaded() throws Exception {
		boolean isLoaded = false;
		if (isElementLoaded(removeWindow)) {
			SimpleUtils.pass("Terminate window loaded successfully!");
			isLoaded = true;
		}else {
			SimpleUtils.fail("Terminate window failed to load!", false);
		}
		return isLoaded;
	}

	private boolean isInviteButtonAvailable() throws Exception {
		boolean isAvailable = false;
		String invite = "Invited to onboard";
		String notInvite = "(Not invited yet)";
		String inviteButtonName = "INVITE";
		String reInviteButtonName = "REINVITE";
		if (isElementLoaded(inviteButton, 5) && isElementLoaded(personalInvitationStatus, 5)) {
			if (personalInvitationStatus.getText().contains(invite)) {
				if (inviteButton.getText().equals(reInviteButtonName)) {
					isAvailable = true;
					SimpleUtils.pass("REINVITE button is available when the status is invited");
				}else {
					SimpleUtils.fail("When status is invited, button should be REINVITE, but actual is: " + inviteButton.getText(), true);
				}
			}else if (personalInvitationStatus.getText().equals(notInvite)) {
				if (inviteButton.getText().equals(inviteButtonName)) {
					isAvailable = true;
					SimpleUtils.pass("INVITE button is available when the status is Not invited");
				}else {
					SimpleUtils.fail("When status is Not invited, button should be INVITE, but actual is: " + inviteButton.getText(), true);
				}
			} else {
				SimpleUtils.fail("Failed to find the INVITE/REINVITE button!", true);
			}
		}else {
			SimpleUtils.fail("INVITE/REINVITE button is not shown!", true);
		}
		return isAvailable;
	}

	private boolean isMandatoryElement(WebElement element) throws Exception{
		boolean isCannotEmpty = false;
		String notEmptyClassName = "ng-invalid-required";
		if (isElementLoaded(element, 5)){
			if (element != null){
				String className = element.getAttribute("class");
				if (className.contains(notEmptyClassName)){
					isCannotEmpty = true;
				}
			}
		}
		return isCannotEmpty;
	}

	private boolean isElementLoadedAndPrintTheMessage(WebElement element, String elementName) throws Exception {
		boolean isLoaded = false;
		if (isElementEnabled(element, 5)) {
			isLoaded = true;
			SimpleUtils.pass(elementName + " loaded Successfully!");
		}else {
			SimpleUtils.fail(elementName + " failed to load!", false);
		}
		return isLoaded;
	}

	private boolean isManageBadgesLoaded() throws Exception {
		boolean isLoaded = false;
		if (isElementLoaded(manageBadgesWindow, 10)) {
			isLoaded = true;
			SimpleUtils.pass("Manage Badges Window Loaded successfully!");
		}else {
			SimpleUtils.fail("Manage Badges Window failed to load!", true);
		}
		return isLoaded;
	}

	private String selectTheBadgeByRandom() throws Exception {
		String badgeID = "";
		String checked = "checked";
		if (areListElementVisible(badgeCheckBoxes, 15)) {
			int randomIndex = (new Random()).nextInt(badgeCheckBoxes.size());
			click(badgeCheckBoxes.get(randomIndex));
			if (badgeCheckBoxes.get(randomIndex).getAttribute("class").contains(checked)) {
				SimpleUtils.pass("Check the Badge successfully!");
				WebElement parent = badgeCheckBoxes.get(randomIndex).findElement(By.xpath("./../.."));
				if (parent != null) {
					WebElement badge = parent.findElement(By.cssSelector("g#Symbols>g"));
					if (badge != null) {
						badgeID = badge.getAttribute("id");
					}
				}else {
					SimpleUtils.fail("Failed to find the parent element!", true);
				}
			}else {
				SimpleUtils.fail("Failed to select the badge!", true);
			}
		}else {
			SimpleUtils.fail("Badge checkboxes are failed to load!", true);
		}
		return badgeID;
	}

	private int getSpecificDayIndex(WebElement specificDay) {
		int index = 0;
		if (areListElementVisible(startDaysOnCalendar, 10)){
			for (int i = 0; i < startDaysOnCalendar.size(); i++) {
				String day = startDaysOnCalendar.get(i).getText();
				if (day.equals(specificDay.getText())){
					index = i;
					SimpleUtils.pass("Get current day's index successfully");
				}
			}
		}else {
			SimpleUtils.fail("Days on calendar failed to load!", true);
		}
		return index;
	}

	private void navigateToPreviousAndFutureDate(WebElement element) {
		SimpleDateFormat format = new SimpleDateFormat("MMMM yyyy");
		click(element);
		if (SimpleUtils.isDateFormatCorrect(currentMonthYear.getText(), format)){
			SimpleUtils.pass("Navigate to previous/future date successfully and date format is correct!");
		}else {
			SimpleUtils.fail("Date format is incorrect!", true);
		}
	}

	private String convertIntToHexString(int value){
		String hexString = Integer.toHexString(value);
		return hexString.length()== 1 ? "0"+ hexString : hexString;
	}

	private String awtColorToWeb(String[] rgba) {
		StringBuilder builder = new StringBuilder();
		try {
			if (rgba.length == 4) {
				builder.append("#");
				/*
				 * Need to convert the r, g, b.
				 */
				for (int i = 0; i < 3; i++) {
					builder.append(convertIntToHexString(Integer.parseInt(rgba[i].trim())));
				}
			}
		}catch (Exception e){
			SimpleUtils.fail("Convert failed!", false);
		}
		return builder.toString();
	}

	private boolean isClickOnCalendarImageSuccessfully() throws Exception {
		boolean isSuccess = false;
		if (isElementLoaded(calendarImage, 5)){
			click(calendarImage);
			if (isElementLoaded(calendar, 5)){
				isSuccess = true;
				SimpleUtils.pass("Click on Calendar Image, Calendar shows successfully!");
			}else{
				SimpleUtils.fail("Failed to show the Calendar", true);
			}
		}else{
			SimpleUtils.fail("Calendar Image failed to show.", true);
		}
		return isSuccess;
	}

	// Added by Nora: For Profile Section
	@FindBy (css = "work-preference-management .user-profile-section button")
	private WebElement editProfileButton;
	@FindBy (css = "[ng-click=\"editEngagement()\"]")
	private WebElement editEngagementBtn;
	@FindBy (css = "[ng-click=\"manageBadges()\"]")
	private WebElement editBadgeBtn;
	@FindBy (css = "span.phone")
	private WebElement phoneText;
	@FindBy (css = "span.email")
	private WebElement emailText;
	@FindBy (css = "div.col-xs-6.value")
	private List<WebElement> engagementTexts;
	@FindBy (css = "div.col-xs-12.value")
	private List<WebElement> nextEngagementTexts;
	@FindBy (css = "div.one-badge")
	private List<WebElement> badges;
	@FindBy (id = "uploadFormInput")
	private WebElement imageInput;
	@FindBy (id = "uploadBusinessFormInput")
	private WebElement businessImageInput;
	@FindBy (css = "lg-button[label=\"Save\"] button")
	private WebElement saveProfileBtn;

	@Override
	public void updateBusinessProfilePicture(String filePath) throws Exception {
		try {
			if (isElementLoaded(profileSection, 10) && isElementLoaded(profileSection.findElement(By.cssSelector("lg-button[label=\"Edit\"]")), 10)) {
				clickTheElement(profileSection.findElement(By.cssSelector("lg-button[label=\"Edit\"]")));
				if (isElementEnabled(getDriver().findElements(By.cssSelector("input[type=\"file\"]")).get(1), 5)
				&& isElementLoaded(saveProfileBtn, 5)) {
					File file = new File(filePath);
					getDriver().findElements(By.cssSelector("input[type=\"file\"]")).get(1).sendKeys(file.getCanonicalPath());
					// wait for the picture to be loaded
					waitForSeconds(6);
					clickTheElement(saveProfileBtn);
					waitForSeconds(5);
				} else {
					SimpleUtils.fail("Business Profile Image input element isn't enabled!", true);
				}
			} else {
				SimpleUtils.fail("Edit button is not loaded!", true);
			}
		} catch (Exception e) {
			SimpleUtils.fail(e.toString(), false);
		}
	}

	@Override
	public void updateProfilePicture(String filePath) throws Exception {
		if (isElementLoaded(editProfileButton, 5)) {
			click(editProfileButton);
			if (isElementEnabled(imageInput, 5)) {
				imageInput.sendKeys(filePath);
				// wait for the picture to be loaded
				waitForSeconds(5);
				clickTheSaveTMButton();
			}else {
				SimpleUtils.fail("Image input element isn't enabled!", true);
			}
		}else {
			SimpleUtils.fail("Edit Profile Button failed to load!", true);
		}
	}

	@Override
	public List<String> updateTheSelectedBadges() throws Exception {
		List<String> selectedBadgeIDs = new ArrayList<>();
		String checked = "checked";
		if (areListElementVisible(badgeCheckBoxes, 15)) {
			int randomIndex = (new Random()).nextInt(badgeCheckBoxes.size());
			click(badgeCheckBoxes.get(randomIndex));
			for (WebElement badgeCheckBox : badgeCheckBoxes) {
				if (badgeCheckBox.getAttribute("class").contains(checked)) {
					WebElement parent = badgeCheckBox.findElement(By.xpath("./../.."));
					if (parent != null) {
						WebElement badge = parent.findElement(By.cssSelector("g#Symbols>g"));
						if (badge != null) {
							selectedBadgeIDs.add(badge.getAttribute("id"));
							SimpleUtils.report("Get the badge id: " + badge.getAttribute("id") + " Successfully!");
						}else {
							SimpleUtils.fail("Failed to find the badge icon!", true);
						}
					} else {
						SimpleUtils.fail("Failed to find the parent element!", true);
					}
				}
			}
			clickConfirmButtonOnBadgeWindow();
		}else {
			SimpleUtils.fail("Badge checkboxes are failed to load!", true);
		}
		return selectedBadgeIDs;
	}

	private void clickConfirmButtonOnBadgeWindow() throws Exception {
		if (isElementLoaded(confirmButton, 5)) {
			confirmButton.click();
		}else {
			SimpleUtils.fail("Confirm button not loaded Successfully!", true);
		}
	}

	@Override
	public void clickOnEditBadgeButton() throws Exception {
		if (isElementLoaded(editBadgeBtn, 5)) {
			waitForSeconds(3);
			moveToElementAndClick(editBadgeBtn);
			isManageBadgesLoaded();
		}else {
			SimpleUtils.fail("Edit Badge button failed to load!", true);
		}
	}

	@Override
	public List<String> getCurrentBadgesOnEngagement() throws Exception {
		List<String> badgeIDs = new ArrayList<>();
		if (areListElementVisible(badges, 5)) {
			for (WebElement badge : badges) {
				WebElement icon = badge.findElement(By.cssSelector("g#Symbols>g"));
				if (icon != null) {
					badgeIDs.add(icon.getAttribute("id"));
					SimpleUtils.report("Get the badge id: " + icon.getAttribute("id") + " Successfully!");
				}else {
					SimpleUtils.fail("Failed to get the badge icon element!", true);
				}
			}
		}
		return badgeIDs;
	}

	@Override
	public void updatePhoneNumberAndEmailID(String phoneNumber, String emailID) throws Exception {
		if (isElementLoaded(editProfileButton, 5)) {
			click(editProfileButton);
			if (isElementLoaded(emailInputTM, 5) && isElementLoaded(phoneInput, 5)) {
				emailInputTM.clear();
				emailInputTM.sendKeys(emailID);
				phoneInput.clear();
				phoneInput.sendKeys(phoneNumber);
				clickTheSaveTMButton();
				if (isElementEnabled(phoneText, 5) && isElementLoaded(emailText, 5)) {
					if (phoneNumber.equalsIgnoreCase(phoneText.getText()) && emailID.equalsIgnoreCase(emailText.getText())) {
						SimpleUtils.pass("Phone number and email ID are updated successfully!");
					}else {
						SimpleUtils.fail("Phone number and email ID aren't updated successfully!", true);
					}
				}else {
					SimpleUtils.fail("Phone Text and Email Text not loaded Successfully!", true);
				}
			}else {
				SimpleUtils.fail("Email and Phone Inputs not loaded successfully!", true);
			}
		}else {
			SimpleUtils.fail("Edit Profile Button not loaded successfully!", true);
		}
	}

	@Override
	public void updateEngagementDetails(Map<String, String> tmDetails) throws Exception {
		String selectedDate = "";
		if (isElementLoaded(editEngagementBtn, 5)) {
			click(editEngagementBtn);
			isElementLoadedAndPrintTheMessage(dateHiredInput, "Date Hired Input");
			click(dateHiredInput);
			if (areListElementVisible(realDays, 5) && isElementLoaded(todayHighlighted, 5)) {
				click(todayHighlighted);
				// Wait for the selected date to be loaded
				waitForSeconds(2);
				selectedDate = dateHiredInput.getAttribute("value");
				SimpleUtils.report("Select the hired date: " + selectedDate);
			}
			isElementLoadedAndPrintTheMessage(employeeIDInput, "Employee ID Input");
			if (employeeIDInput.getAttribute("value").isEmpty()) {
				employeeIDInput.sendKeys("E" + new Random().nextInt(200) + new Random().nextInt(200) + new Random().nextInt(200));
			}
			isElementLoadedAndPrintTheMessage(engagementStatusSelect, "Engagement Status Select");
			selectByVisibleText(engagementStatusSelect, tmDetails.get("ENGAGEMENT_STATUS"));
			isElementLoadedAndPrintTheMessage(hourlySelect, "Hourly Select");
			selectByVisibleText(hourlySelect, tmDetails.get("HOURLY"));
			isElementLoadedAndPrintTheMessage(salariedSelect, "Salaried Select");
			selectByVisibleText(salariedSelect, tmDetails.get("SALARIED"));
			isElementLoadedAndPrintTheMessage(exemptSelect, "Exempt Select");
			selectByVisibleText(exemptSelect, tmDetails.get("EXEMPT"));
			clickTheSaveTMButton();
			verifyTheEngagementDetailsSavedSuccessfully(tmDetails, selectedDate);
		}else {
			SimpleUtils.fail("Edit Engagement Details button not loaded successfully!", true);
		}
	}

	public void verifyTheEngagementDetailsSavedSuccessfully(Map<String, String> tmDetails, String selectedDate) throws Exception {
		boolean areConsistent = true;
		SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat format2 = new SimpleDateFormat("MMM dd, yyyy");
		if (areListElementVisible(engagementTexts, 5) && areListElementVisible(nextEngagementTexts, 5)) {
			if (engagementTexts.size() >= 4 && nextEngagementTexts.size() >= 4) {
				String actualDate = engagementTexts.get(0).getText();
				String engagementStatus = nextEngagementTexts.get(0).getText();
				String hourly = nextEngagementTexts.get(1).getText();
				String salaried = nextEngagementTexts.get(2).getText();
				String exempt = nextEngagementTexts.get(3).getText();
				if (!SimpleUtils.isSameDayComparingTwoDays(actualDate, selectedDate, format1, format2)
				|| !engagementStatus.equalsIgnoreCase(tmDetails.get("ENGAGEMENT_STATUS").replaceAll(" ", "")) || !hourly.equalsIgnoreCase(tmDetails.get("HOURLY"))
				|| !salaried.equalsIgnoreCase(tmDetails.get("SALARIED")) || !exempt.equalsIgnoreCase(tmDetails.get("EXEMPT"))) {
					areConsistent = false;
				}
			}else {
				areConsistent = false;
			}
		}else {
			areConsistent = false;
		}
		if (areConsistent) {
			SimpleUtils.pass("Verified Engagement details are updated successfully!");
		}else {
			SimpleUtils.fail("Engagement details not updated successfully!", true);
		}
	}

	public void clickTheSaveTMButton() throws Exception {
		if (isElementLoaded(saveTMButton, 5) && isElementEnabled(saveTMButton, 5)) {
			scrollToBottom();
			moveToElementAndClick(saveTMButton);
		}else {
			SimpleUtils.fail("Save Team Member not loaded or enabled successfully!", true);
		}
	}

	// Added by Nora: for Coverage
	@FindBy(css = "[ng-click*=\"openFilter()\"]")
	private WebElement openFilterBtn;
	@FindBy(className = "lg-filter__wrapper")
	private WebElement filterLayout;
	@FindBy(className = "lg-filter__clear")
	private WebElement clearFilterBtn;
	@FindBy(css = "[ng-repeat=\"opt in opts\"]")
	private List<WebElement> jobTitles;
	@FindBy(css = "div.search-option div.lg-button-group>div")
	private List<WebElement> subTabsOnCoverage;
	@FindBy(css = "[class=\"coverage-row row-fx ng-scope coverage-timeoff\"]")
	private List<WebElement> timeOffRows;
	@FindBy(className = "coverage-no-heatmap")
	private WebElement noTimeOff;
	@FindBy(css = ".sch-calendar-day")
	private List<WebElement> weekDays;

	@Override
	public HashMap<Integer, String> generateIndexAndRelatedTimes(LinkedHashMap<String, List<String>> regularHours) throws Exception {
		HashMap<Integer, String> indexAndTime = null;
		List<String> startTimes = new ArrayList<>();
		List<String> endTimes = new ArrayList<>();
		for (Map.Entry<String, List<String>> entry : regularHours.entrySet()) {
			if (entry.getValue().size() == 2) {
				startTimes.add(entry.getValue().get(0));
				endTimes.add(entry.getValue().get(1));
			}
		}
		String minStartTime = getMinNMaxTimes(startTimes).get(0);
		String maxEndTime = getMinNMaxTimes(endTimes).get(1);
		SimpleUtils.report("Get the minimum start time: " + minStartTime);
		SimpleUtils.report("Get the maximum end time: " + maxEndTime);
		int count = getTimeDurationByStartNEndTime(minStartTime, maxEndTime);
		indexAndTime = generateIndexAndTime(count, minStartTime, maxEndTime);
		return indexAndTime;
	}

	public HashMap<Integer, String> generateIndexAndTime(int count, String minStartTime, String maxEndTime) throws ParseException {
		HashMap<Integer, String> indexAndTime = new HashMap<>();
		for (int i = 0; i < count; i++) {
			String time = (i == 0 ? minStartTime : timeAdd(minStartTime, i * 30));
			indexAndTime.put(i, time);
			SimpleUtils.report("Add the index: " + i + ", and related time: " + time);
		}
		if (indexAndTime.get(count - 1).equalsIgnoreCase(timeMinus(maxEndTime, 30))) {
			SimpleUtils.pass("Get the index and time Successfully!");
		}else {
			SimpleUtils.fail("The last time isn't equal to the maximum end time!", true);
		}
		return indexAndTime;
	}

	public String timeAdd(String oldTime, int addMinutes) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
		Date date = df.parse(oldTime);
		Date expireTime = new Date(date.getTime() + addMinutes * 60 * 1000);
		String newTime = df.format(expireTime);
		return newTime;
	}

	public String timeMinus(String oldTime, int minusMinutes) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
		Date date = df.parse(oldTime);
		Date expireTime = new Date(date.getTime() - minusMinutes * 60 * 1000);
		String newTime = df.format(expireTime);
		return newTime;
	}

	public List<String> getMinNMaxTimes(List<String> times) {
		List<String> minNMaxTimes = new ArrayList<>();
		int minIndex = 0;
		int maxIndex = 0;
		int minCount = 0;
		int maxCount = 0;
		if (times.size() > 0) {
			for (int i = 0; i < times.size(); i++) {
				int currentCount  = getMinutesFromTime(times.get(i));
				if (i == 0) {
					minCount = currentCount;
					maxCount = currentCount;
				}else {
					if (currentCount < minCount) {
						minCount = currentCount;
						minIndex = i;
					}
					if (currentCount > maxCount) {
						maxCount = currentCount;
						maxIndex = i;
					}
				}
			}
			minNMaxTimes.add(times.get(minIndex));
			minNMaxTimes.add(times.get(maxIndex));
		}else {
			SimpleUtils.fail("Times size is incorrect!", true);
		}
		return minNMaxTimes;
	}

	public int getTimeDurationByStartNEndTime(String startTime, String endTime) {
		int startHalfHourCount = 0;
		int endHalfHourCount = 0;
		startHalfHourCount = getMinutesFromTime(startTime)/30;
		endHalfHourCount = getMinutesFromTime(endTime)/30;
		return endHalfHourCount - startHalfHourCount;
	}

	public int getMinutesFromTime(String time) {
		int totalMinutes = 0;
		int hour = 0;
		int minute = 0;
		if (time.contains(":")) {
			String[] hourNMinute = time.split(":");
			if (hourNMinute.length == 2 && hourNMinute[1].length() > 2) {
				if (hourNMinute[1].contains("pm") && !hourNMinute[0].trim().equalsIgnoreCase("12")) {
					hour = Integer.parseInt(hourNMinute[0].trim()) + 12;
				}else {
					hour = Integer.parseInt(hourNMinute[0].trim());
				}
				minute = Integer.parseInt(hourNMinute[1].substring(0, hourNMinute[1].length() - 2).trim());
				totalMinutes = hour * 60 + minute;
			}
		}else {
			SimpleUtils.fail("Time format is incorrect!", false);
		}
		return totalMinutes;
	}

	@Override
	public String selectAJobTitleByRandom() throws Exception {
		String selectedJobTitle = "";
		clickOnJobTitleFilter();
		clickOnClearFilterBtn();
		if (areListElementVisible(jobTitles, 5)) {
			int index = (new Random()).nextInt(jobTitles.size());
			WebElement checkbox = jobTitles.get(index).findElement(By.className("input-form"));
			WebElement title = jobTitles.get(index).findElement(By.tagName("label"));
			if (checkbox != null && title != null) {
				click(checkbox);
				selectedJobTitle = title.getText();
			}else {
				SimpleUtils.fail("Failed to find the checkbox and title element!", true);
			}
		}else {
			SimpleUtils.fail("Job titles not loaded Successfully!", true);
		}
		return selectedJobTitle;
	}

	@Override
	public void	selectTheJobTitleByName(String jobTitleName) throws Exception {
		clickOnJobTitleFilter();
		clickOnClearFilterBtn();
		if (areListElementVisible(jobTitles, 5)) {
			for (WebElement jobTitle : jobTitles) {
				WebElement checkbox = jobTitle.findElement(By.className("input-form"));
				WebElement title = jobTitle.findElement(By.tagName("label"));
				if (checkbox != null && title != null) {
					if (jobTitleName.equalsIgnoreCase(title.getText())) {
						click(checkbox);
						break;
					}
				}else {
					SimpleUtils.fail("Failed to find the checkbox and title element!", true);
				}
			}
		}else {
			SimpleUtils.fail("Job titles not loaded Successfully!", true);
		}
	}

	@Override
	public HashMap<Integer, List<String>> getTimeOffWeekTableOnCoverage(HashMap<Integer, String> indexAndTimes,
																		LinkedHashMap<String, List<String>> regularHours) throws Exception {
		HashMap<Integer, List<String>> timeOffs = new HashMap<>();
		List<String> weekTimeOffCounts = null;
		try {
			if (isElementLoaded(noTimeOff, 5)) {
				timeOffs = generateTheDefaultTimeOffTableByRegularHours(indexAndTimes, regularHours);
			}
		} catch (Exception e) {
			// Do nothing
		}
		if (areListElementVisible(timeOffRows, 10)) {
			for (int i = 0; i < timeOffRows.size(); i++) {
				// Avoid stale element issue
				String className = getDriver().findElements(By.cssSelector("[class=\"coverage-row row-fx ng-scope coverage-timeoff\"]")).get(i).getAttribute("class");
				if (!className.contains("ng-hide")) {
					timeOffRows = getDriver().findElements(By.cssSelector("[class=\"coverage-row row-fx ng-scope coverage-timeoff\"]"));
					List<WebElement> timeOffCounts = timeOffRows.get(i).findElements(By.className("coverage-cell"));
					if (areListElementVisible(timeOffCounts, 5)) {
						weekTimeOffCounts = new ArrayList<>();
						timeOffCounts = timeOffRows.get(i).findElements(By.className("coverage-cell"));
						for (int j = 0; j < timeOffCounts.size(); j++) {
							weekTimeOffCounts.add(getDriver().findElements(By.cssSelector("[class=\"coverage-row row-fx ng-scope coverage-timeoff\"]")).
									get(i).findElements(By.className("coverage-cell")).get(j).getText());
						}
						timeOffs.put(i, weekTimeOffCounts);
					} else {
						SimpleUtils.fail("Coverage cell elements not loaded Successfully!", true);
					}
				}
			}
		}
		return timeOffs;
	}

	@Override
	public HashMap<Integer, List<String>> getTimeOffWeekTableByDateNTime(HashMap<Integer, List<String>> previousTimeOffs,
																		 HashMap<String, List<String>> selectedDateNTime, HashMap<Integer, String> indexAndTimes) throws Exception {
		String selectedDate = "";
		if (previousTimeOffs != null && previousTimeOffs.size() > 0 && selectedDateNTime != null && selectedDateNTime.size() > 0
		&& indexAndTimes != null && indexAndTimes.size() > 0) {
			Set<String> mapSet = selectedDateNTime.keySet();
			Iterator<String> iterator = mapSet.iterator();
			while (iterator.hasNext()) {
				selectedDate = iterator.next();
				break;
			}
			if (!selectedDate.contains(",")) {
				SimpleUtils.fail("Selected date's format is incorrect!", false);
			}
			int index = getWeekDayIndexByTitle(selectedDate.split(",")[0]);
			List<String> selectedTimes = selectedDateNTime.get(selectedDate);
			List<Integer> timeIndexes = getRowIndexBySelectedTime(selectedTimes, indexAndTimes);
			if (timeIndexes.size() == 1) {
				String currentValue = previousTimeOffs.get(timeIndexes.get(0)).get(index);
				if (!currentValue.isEmpty()) {
					int value = Integer.parseInt(currentValue);
					previousTimeOffs.get(timeIndexes.get(0)).set(index, Integer.toString(value + 1));
				}else {
					previousTimeOffs.get(timeIndexes.get(0)).set(index, "1");
				}
			} else if (timeIndexes.size() == 2) {
				previousTimeOffs = updateTimeOffTableByIndexes(previousTimeOffs, index, timeIndexes);
			} else {
				SimpleUtils.fail("Time indexes are incorrect!", true);
			}
		}else {
			SimpleUtils.fail("Previous time off graph table, selected date and time, or indexes and times are incorrect!", true);
		}
		return previousTimeOffs;
	}

	public HashMap<Integer, List<String>> updateTimeOffTableByIndexes(HashMap<Integer, List<String>> previousTimeOffs, int index, List<Integer> timeIndexes) {
		if (timeIndexes.size() != 2) {
			SimpleUtils.fail("Time Indexes are incorrect!", false);
		}
		int startIndex = timeIndexes.get(0);
		int endIndex = timeIndexes.get(1);
		for (int i = startIndex; i <= endIndex; i++) {
			String currentValue = previousTimeOffs.get(i).get(index);
			if (!currentValue.isEmpty()) {
				int value = Integer.parseInt(currentValue);
				previousTimeOffs.get(i).set(index, Integer.toString(value + 1));
			}else {
				previousTimeOffs.get(i).set(index, "1");
			}
		}
		return previousTimeOffs;
	}

	public List<Integer> getRowIndexBySelectedTime(List<String> selectedTimes, HashMap<Integer, String> indexAndTimes) throws ParseException {
		List<Integer> timeIndexes = new ArrayList<>();
		int startIndex = 0;
		int endIndex = 0;
		if (selectedTimes.size() == 2 && indexAndTimes.size() > 2) {
			startIndex = SimpleUtils.getHashMapKeyByValue(indexAndTimes, selectedTimes.get(0));
			endIndex = SimpleUtils.getHashMapKeyByValue(indexAndTimes, timeMinus(selectedTimes.get(1), 30));
			SimpleUtils.report("Get the start index: " + startIndex + ", for start time: " + selectedTimes.get(0));
			SimpleUtils.report("Get the end index: " + endIndex + ", for end time: " + selectedTimes.get(1));
		}
		if (startIndex != endIndex) {
			timeIndexes.add(startIndex);
			timeIndexes.add(endIndex);
		}else {
			timeIndexes.add(startIndex);
		}
		return timeIndexes;
	}



	public int getWeekDayIndexByTitle(String selectedWeekDay) throws Exception {
		int index = 7;
		if (areListElementVisible(weekDays, 5)) {
			for (int i = 0; i < weekDays.size(); i++) {
				if (weekDays.get(i).getText().contains(selectedWeekDay)) {
					index = i;
					SimpleUtils.report("Get the index of " + selectedWeekDay + " is: " + index);
					break;
				}
			}
		}else {
			SimpleUtils.fail("Week days elements not loaded Successfully!", true);
		}
		if (index == 7) {
			SimpleUtils.fail("Failed to get the index!", true);
		}
		return index;
	}

	public HashMap<Integer, List<String>> generateTheDefaultTimeOffTableByRegularHours(HashMap<Integer, String> indexAndTimes,
																					   LinkedHashMap<String, List<String>> regularHours) throws Exception {
		HashMap<Integer, List<String>> timeOffs = new HashMap<>();
		List<String> weekTimeOffCounts = null;
		for (int i = 0; i < indexAndTimes.size(); i++) {
			weekTimeOffCounts = new ArrayList<>();
			for (Map.Entry<String, List<String>> entry : regularHours.entrySet()) {
				weekTimeOffCounts.add("");
			}
			timeOffs.put(i, weekTimeOffCounts);
		}
		if (timeOffs.size() == indexAndTimes.size()) {
			SimpleUtils.pass("Get the Default time off table Successfully!");
		}else {
			SimpleUtils.fail("Failed to get the default time off table!", true);
		}
		return timeOffs;
	}

	@Override
	public void navigateToSubTabOnCoverage(String subTabName) throws Exception {
		if (areListElementVisible(subTabsOnCoverage, 5)) {
			for (WebElement subTab : subTabsOnCoverage) {
				WebElement titleLabel = subTab.findElement(By.tagName("span"));
				if (titleLabel != null && subTabName.equalsIgnoreCase(titleLabel.getText())) {
					click(subTab);
					SimpleUtils.pass("Navigate to sub tab: " + subTabName + " Successfully!");
				}
			}
		}else {
			SimpleUtils.fail("Sub tabs not loaded Successfully!", true);
		}
	}

	@Override
	public void clickOnJobTitleFilter() throws Exception {
		if (isElementLoaded(openFilterBtn, 5)) {
			clickTheElement(openFilterBtn);
			if (isFilterLayoutLoaded()) {
				SimpleUtils.pass("Click the open filter button Successfully!");
			}else {
				SimpleUtils.fail("Filter Layout not loaded Successfully", true);
			}
		}else {
			SimpleUtils.fail("Open Filter Button not loaded Successfully!", true);
		}
	}

	public boolean isFilterLayoutLoaded() throws Exception {
		boolean isLoaded = false;
		if (isElementLoaded(filterLayout, 5)) {
			isLoaded = true;
		}
		return isLoaded;
	}

	@Override
	public void clickOnClearFilterBtn() throws Exception {
		if (isElementLoaded(clearFilterBtn, 15)) {
			click(clearFilterBtn);
			if (isClearFilterSuccessFully()) {
				SimpleUtils.pass("Clear Filter Successfully!");
			}else {
				SimpleUtils.fail("Clear Filter not successfully!", false);
			}
		}else {
			SimpleUtils.fail("Clear Filter button not loaded Successfully!", false);
		}
	}

	public boolean isClearFilterSuccessFully() throws Exception {
		boolean isClear = true;
		if (areListElementVisible(jobTitles, 5)) {
			for (WebElement jobTitle : jobTitles) {
				WebElement checkbox = jobTitle.findElement(By.tagName("input"));
				if (checkbox != null) {
					String className = checkbox.getAttribute("class");
					if (className.contains("ng-not-empty")) {
						isClear = false;
					}
				}else {
					SimpleUtils.fail("Failed to find the input element!", true);
				}
			}
		}else {
			SimpleUtils.fail("Job Title Filters not loaded Successfully!", true);
		}
		return isClear;
	}

@FindBy(css = "div[ng-if=\"showLocation\"]")
private List<WebElement> locationColumn;
	@Override
	public boolean verifyThereIsLocationColumnForMSLocationGroup() throws Exception {
		if (areListElementVisible(locationColumn,5)) {
			return true;
		}else
			return  false;
	}

	@Override
	public boolean isColumnExisted(String colName) throws Exception {
		if (areListElementVisible(columnsInRoster, 10)){
			for (WebElement element: columnsInRoster){
				if (colName.equalsIgnoreCase(element.getText())){
					return true;
				}
			}
		}
		return false;
	}

	@FindBy(xpath = "//span[contains(text(),'School Calendars')]")
	private WebElement schoolCalendarTab;
	@Override
	public boolean isCalendarTabLoad() throws Exception {
		if (isElementLoaded(schoolCalendarTab, 10)) {
			return true;
		}
		return false;
	}
	// Added by Julie
	@FindBy(css = ".sub-navigation-view-link")
	private List<WebElement> TeamSubTabsElement;

	@FindBy(css = ".sub-navigation-view-link.active")
	private WebElement activatedSubTabElement;

	@FindBy(xpath = "//label[text()=\"School Session Start*\"]/../div[@class=\"session-information-date-input\"]")
	private WebElement schoolSessionStartInput;

	@FindBy(xpath = "//label[text()=\"School Session End*\"]/../div[@class=\"session-information-date-input\"]")
	private WebElement schoolSessionEndInput;

	@FindBy(css = "[value=\"calendarName\"] input")
	private WebElement calendarNameInput;

	@FindBy(css = "[value=\"schoolCalendarUrl\"] input")
	private WebElement schoolCalendarURLInput;

	@FindBy(css = ".summer-day")
	private List<WebElement> summerDays;

	@FindBy(css = ".non-school-day")
	private List<WebElement> nonSchoolDays;

	@FindBy(css = ".set-session-modal")
	private WebElement setSessionStartAndEndTimeWindow;

	@FindBy(css = "div.big-calendar")
	private WebElement bigCalendar;

	@FindBy(css = "[month=\"sessionStart\"] .real-day")
	private List<WebElement> daysInSessionStart;

	@FindBy(css = "[month=\"sessionEnd\"] .real-day")
	private List<WebElement> daysInSessionEnd;

	@FindBy(css = ".in-range")
	private List<WebElement> daysInRange;

	@FindBy(css = ".ranged-calendar__month-name")
	private List<WebElement> rangedCalendars;

	@FindBy(css = ".confirm")
	private WebElement saveBtnInSessionStartEnd;

	@FindBy(css = ".lg-toast")
	private WebElement popMessage;

	@FindBy(css = ".set-session-modal-body-times-time-start span")
	private WebElement startDateInSessionStartEnd;

	@FindBy(xpath = "//div[@class=\"set-session-modal-body-times\"]/div[2]/span")
	private WebElement endDateInSessionStartEnd;

	@FindBy(css = ".calendar-month-name")
	private List<WebElement> calendarMonths;

	@FindBy(css = "i.fa-chevron-right[ng-click=\"changeCalendarYear(1)\"]")
	private WebElement nextYearArrow;

	@FindBy(css = "i.fa-chevron-left[ng-click=\"changeCalendarYear(-1)\"]")
	private WebElement priorYearArrow;

	@FindBy(css = ".school-calendars-year-switcher")
	private WebElement schoolCalendarYearSwitcher;

	@Override
	public void clickOnTeamSubTab(String subTabString) throws Exception {
		waitForSeconds(3);
		if (areListElementVisible(TeamSubTabsElement,10) && TeamSubTabsElement.size() != 0) {
			for (WebElement TeamSubTabElement : TeamSubTabsElement) {
				if (TeamSubTabElement.getText().equalsIgnoreCase(subTabString)) {
					click(TeamSubTabElement);
					waitForSeconds(3);
				}
			}
		}
		if (verifyActivatedSubTab(subTabString)) {
			SimpleUtils.pass("Team Page: '" + subTabString + "' tab loaded Successfully!");
		} else {
			SimpleUtils.fail("Team Page: '" + subTabString + "' tab not loaded Successfully!", true);
		}
	}

	@Override
	public boolean verifyActivatedSubTab(String SubTabText) throws Exception {
		if (isElementLoaded(activatedSubTabElement,30)) {
			if (activatedSubTabElement.getText().trim().equalsIgnoreCase(SubTabText)) {
				return true;
			}
		} else {
			SimpleUtils.fail("Team Page not loaded successfully", true);
		}
		return false;
	}

	@Override
	public void verifyCreateCalendarLoaded() throws Exception {
		if (isElementLoaded(bigCalendar,5) && areListElementVisible(nonSchoolDays,5) && nonSchoolDays.size() > 103)
			SimpleUtils.pass("School Calendars Page: A new calendar appears with school days and non school day, weekend is non school day and holiday is non school day");
		else
			SimpleUtils.fail("School Calendars Page: Calendar not loaded or loaded unexpectedly",false);
	}

	@Override
	public void verifySessionStartNEndIsMandatory() throws Exception {
		if (verifyMandatoryElement(schoolSessionStartInput) && verifyMandatoryElement(schoolSessionEndInput))
			SimpleUtils.pass("School Calendars Page: School Session Start field and School Session End field are mandatory fields");
		else
			SimpleUtils.fail("School Calendars Page: School Session Start field and School Session End field are not mandatory fields",false);
	}

	@Override
	public void clickOnSchoolSessionStart() throws Exception {
		if (isElementLoaded(schoolSessionStartInput,5)) {
			clickTheElement(schoolSessionStartInput);
			if (isElementLoaded(setSessionStartAndEndTimeWindow,5))
				SimpleUtils.pass("School Calendars Page: Click on School Session Start input successfully");
			else
				SimpleUtils.fail("School Calendars Page: Failed to click on School Session Start input",false);
		} else
			SimpleUtils.fail("School Calendars Page: School Session Start input field failed to load",false);
	}

	@Override
	public void clickOnSchoolSessionEnd() throws Exception {
		if (isElementLoaded(schoolSessionEndInput,5)) {
			clickTheElement(schoolSessionEndInput);
			if (isElementLoaded(setSessionStartAndEndTimeWindow,5))
				SimpleUtils.pass("School Calendars Page: Click on School Session End input successfully");
			else
				SimpleUtils.fail("School Calendars Page: Failed to click on School Session Start input",false);
		} else
			SimpleUtils.fail("School Calendars Page:School Session End input field failed to load",false);
	}

	@Override
	public void inputCalendarName(String calendarName) throws Exception {
		if (isElementLoaded(calendarNameInput,5)) {
			calendarNameInput.sendKeys(calendarName);
			if (calendarNameInput.getAttribute("value").equals(calendarName))
				SimpleUtils.pass("School Calendars Page: Input customized calendar name " + calendarName + " successfully");
			else
				SimpleUtils.fail("School Calendars Page: Failed to input customized calendar name",false);
		} else
			SimpleUtils.fail("School Calendars Page: Calendar Name input field failed to load",false);
	}

	@Override
	public String selectRandomDayInSessionStart() throws Exception {
		String startDate = "";
		if (areListElementVisible(daysInSessionStart,5)) {
			int index = (new Random()).nextInt(daysInSessionStart.size());
			clickTheElement(daysInSessionStart.get(index));
			if (daysInSessionStart.get(index).getAttribute("class").contains("in-range") && isElementLoaded(startDateInSessionStartEnd,5)) {
				SimpleUtils.pass("School Calendars Page: Session start random day is selected successfully");
				startDate = startDateInSessionStartEnd.getText();
			} else
				SimpleUtils.fail("School Calendars Page: Session start random day failed to select",false);
		} else
			SimpleUtils.fail("School Calendars Page: Session start days failed to load",false);
		return startDate;
	}

	@Override
	public String selectRandomDayInSessionEnd() throws Exception {
		String endDate = "";
		if (areListElementVisible(daysInSessionEnd,5)) {
			int index = (new Random()).nextInt(daysInSessionEnd.size());
			click(daysInSessionEnd.get(index));
			if (daysInSessionEnd.get(index).getAttribute("class").contains("in-range") && isElementLoaded(endDateInSessionStartEnd,5)) {
				SimpleUtils.pass("School Calendars Page: Session end random day is selected successfully");
				endDate = endDateInSessionStartEnd.getText();
			} else
				SimpleUtils.fail("School Calendars Page: Session start random day failed to select",false);
		} else
			SimpleUtils.fail("School Calendars Page: Session start days failed to load",false);
		return endDate;
	}

	@Override
	public void clickOnSaveCalendar() throws Exception {
		if (isElementLoaded(savePreferButton,5)) {
			clickTheElement(savePreferButton);
			if (isElementLoaded(popMessage,120) && popMessage.getText().contains("Success"))
				SimpleUtils.pass("School Calendars Page: School Calendar is saved successfully");
			else
				SimpleUtils.fail("School Calendars Page: School Calendar failed to save",false);
		} else
			SimpleUtils.fail("School Calendars Page: School Session End input field failed to load",false);
	}

	@Override
	public void verifyDatesInCalendar(String startDate, String EndDate) throws Exception {
		if (areListElementVisible(summerDays,5)) {
			DateFormat df = new SimpleDateFormat( "MM/dd/yyyy");
			Date start = df.parse(startDate);
			Date end = df.parse(EndDate);
		    Long betweenDays = (end.getTime() - start.getTime()) / (1000L*3600L*24L);
			if ((summerDays.size() == 364 - betweenDays) || (summerDays.size() == 365 - betweenDays))
				SimpleUtils.pass("School Calendars Page: Summer days are consistent with user entered dates");
			else
				SimpleUtils.fail("School Calendars Page: Summer days are inconsistent with user entered dates",false);
		} else
			SimpleUtils.fail("School Calendars Page: Summer days failed to load",false);
	}

	@Override
	public void checkNextYearInEditMode() throws Exception {
		if (isElementLoaded(nextYearArrow,5)) {
           clickTheElement(nextYearArrow);
           if (schoolSessionStartInput.getText().isEmpty() && schoolSessionEndInput.getText().isEmpty() && !calendarNameInput.getAttribute("value").isEmpty()
				   && calendarNameInput.getTagName().equals("input"))
			   SimpleUtils.pass("School Calendars Page: Calendar for the next year in edit mode will only show calendar name, the calendar is editable");
		   else
			   SimpleUtils.fail("School Calendars Page: Calendar for the next year in edit mode is incorrect",false);
		} else
			SimpleUtils.fail("School Calendars Page: Next year arrow failed to load",false);
	}

	@Override
	public void checkPriorYearInEditMode() throws Exception {
		Calendar calder = Calendar.getInstance();
		calder.setTime(new Date());
		int month = calder.get(Calendar.MONTH);
		// If month is before August, previous year should show
		if (month < 7) {
			if (isElementLoaded(priorYearArrow, 5)) {
				SimpleUtils.pass("School Calendars Page: Prior year arrow is visible in edit mode when it is before August current year");
			} else {
				SimpleUtils.fail("School Calendars Page: Prior year arrow is invisible when it is before August current year, which is unexpected!", false);
			}
		} else {
			if (priorYearArrow.getAttribute("class").contains("invisible")) {
				SimpleUtils.fail("School Calendars Page: Prior year arrow is invisible in edit mode unexpectedly", false);
			} else
				SimpleUtils.pass("School Calendars Page: Prior year arrow is displayed expectedly");
		}
	}

	@Override
	public void clickOnPriorYearInEditMode() throws Exception {
		if (isElementLoaded(priorYearArrow,5)) {
			String yearStart = schoolCalendarYearSwitcher.getText().contains("-")? schoolCalendarYearSwitcher.getText().split("-")[0].trim():schoolCalendarYearSwitcher.getText();
			clickTheElement(priorYearArrow);
			String yearEnd = schoolCalendarYearSwitcher.getText().contains("-")? schoolCalendarYearSwitcher.getText().split("-")[1].trim().replaceAll("\\D+", ""):schoolCalendarYearSwitcher.getText();
			if (yearStart.equals(yearEnd))
				SimpleUtils.pass("School Calendars Page: Go to prior year successfully ");
			else
				SimpleUtils.fail("School Calendars Page: Failed to go to prior year",false);
		} else
			SimpleUtils.fail("School Calendars Page: Prior year arrow failed to load",false);
	}

	@Override
	public boolean verifyMandatoryElement(WebElement element) throws Exception {
		boolean isMandatory = false;
		if (isElementLoaded(element, 5)) {
			if (element != null) {
				if (element.getText().isEmpty() && savePreferButton.getAttribute("ng-attr-disabled").equals("!canSaveCalendar()")) {
					isMandatory= true;
				}
			}
		}
		return isMandatory;
	}

	@Override
	public void createNewCalendarByName(String calendarName) throws Exception {
		clickOnCreateNewCalendarButton();
        clickOnSchoolSessionStart();
		selectRandomDayInSessionStart();
		selectRandomDayInSessionEnd();
		clickOnSaveSchoolSessionCalendarBtn();
		inputCalendarName(calendarName);
		clickOnSaveCalendar();
		clickOnTeamSubTab(TeamTest.TeamPageSubTabText.SchoolCalendars.getValue());
	}

	@Override
	public boolean isCalendarDisplayedByName(String calendarName) throws Exception {
		boolean isCalendarDisplayed = false;
		if (areListElementVisible(calendarTitles, 60)) {
			for (WebElement title : calendarTitles) {
				if (title.getText().trim().equalsIgnoreCase(calendarName)) {
					isCalendarDisplayed = true;
					SimpleUtils.pass("School Calendars Page: The calendar " + calendarName + " displays in the calendar list");
					break;
				}
			}
		} else
			SimpleUtils.fail("School Calendar: There is no calendars!",false);
		return isCalendarDisplayed;
	}

	@Override
	public void clickOnCancelEditCalendarBtn() throws Exception {
		if (isElementLoaded(cancelEditButton, 5)) {
			click(cancelEditButton);
			if (isElementLoaded(schoolCalendarHeader,5))
				SimpleUtils.pass("School Calendars Page: Click on 'Cancel' button successfully!");
			else
				SimpleUtils.fail("School Calendars Page: Click on 'Cancel' button does not return to calendar list page",false);
		} else {
			SimpleUtils.fail("School Calendars Page: Failed to load Cancel button", false);
		}
	}

	// Added by Nora: For Cinemark Minors
	@FindBy(css = "[label=\"Create New Calendar\"]")
	private WebElement createNewCalendarBtn;
	@FindBy(css = ".set-session-modal-body-arrow-right")
	private List<WebElement> rightArrows;
	@FindBy(css = ".modal-instance-button.confirm")
	private WebElement saveSchoolSessionBtn;
	@FindBy(css = "div[ng-click=\"dismissPopup()\"]")
	private WebElement cancelSchoolSessionBtn;
	@FindBy(css = "[label=\"Delete\"] span.ng-binding")
	private WebElement deleteCalendarBtn;
	@FindBy(css = "[label=\"Edit\"]")
	private WebElement editCalendarBtn;
	@FindBy(css = ".calendar-overview-title")
	private List<WebElement> calendarTitles;
	@FindBy(className = "school-calendars-header-title")
	private WebElement schoolCalendarHeader;
	@FindBy(css = ".calendar-cell")
	private List<WebElement> calendarCells;
	@FindBy(css = "[options=\"schoolCalendars\"] select")
	private WebElement schoolCalendarSelect;
	@FindBy(css = "[label=\"Save\"]")
	private WebElement saveButton;
	@FindBy(css = ".school-day")
	private List<WebElement> schoolDays;
	@FindBy(css = ".session-information-and-calendar-wrapper")
	private WebElement calendarWrapper;
	@FindBy(css = ".calendar-overview")
	private List<WebElement> calendarList;
	@FindBy(css = ".school-calendars-year-switcher")
	private WebElement schoolYear;
	@FindBy(css = ".calendar-block")
	private List<WebElement> calendarBlocks;
	@FindBy(className = "school-calendars-linkback")
	private WebElement schoolCalendarsBackBtn;
	@FindBy(css = "span.lgn-alert-message.warning")
	private WebElement warningOnPopup;

	@Override
	public void clickOnSchoolSchedulesButton() throws Exception {
		if (isElementLoaded(schoolCalendarsBackBtn, 5)) {
			clickTheElement(schoolCalendarsBackBtn);
		} else {
			SimpleUtils.fail("Calendar page: School Calendars Back button not loaded Successfully!", false);
		}
	}

	@Override
	public void verifyTheContentOnDetailedCalendarPage() throws Exception {
		if (isElementLoaded(schoolCalendarHeader, 5)) {
			SimpleUtils.pass("Calendar: " + schoolCalendarHeader.getText() + " page loaded Successfully!");
		} else {
			SimpleUtils.fail("Calendar title failed to load!", false);
		}
		if (isElementLoaded(schoolYear,  5)) {
			SimpleUtils.pass("Calendar: " + schoolCalendarHeader.getText() + ", school year: " + schoolYear.getText() + " loaded Successfully!");
		} else {
			SimpleUtils.fail("Calendar: School Year element not loaded Successfully!", false);
		}
		if (isElementLoaded(deleteCalendarBtn, 5) && isElementLoaded(editCalendarBtn, 5)) {
			SimpleUtils.pass("Calendar: Delete and Edit buttons loaded Successfully!");
		} else {
			SimpleUtils.fail("Calendar: Delete and Edit buttons not loaded Successfully!", false);
		}
		if (areListElementVisible(calendarBlocks, 5) && calendarBlocks.size() == 12) {
			SimpleUtils.pass("Calendar: 12 Months loaded Successfully!");
		} else {
			SimpleUtils.fail("Calendar: 12 Months not loaded Successfully!", false);
		}
	}

	@Override
	public void verifyTheContentOnEachCalendarList() throws Exception {
		if (areListElementVisible(calendarList, 10)) {
			SimpleUtils.pass("Calendars are loaded Successfully!");
			for (WebElement calendar : calendarList) {
				WebElement title = calendar.findElement(By.cssSelector(".calendar-overview-title"));
				List<WebElement> infos = calendar.findElements(By.cssSelector(".calendar-overview-info-title"));
				WebElement lastUpdateInfo = calendar.findElement(By.cssSelector(".calendar-overview-info-img"));
				if (title != null && infos != null && lastUpdateInfo != null) {
					if (infos.size() == 3 && infos.get(0).getText().equals("SCHOOL SESSION START") &&
					infos.get(1).getText().equals("SCHOOL SESSION END") && infos.get(2).getText().equals("SCHOOL CALENDAR URL")) {
						SimpleUtils.pass("School Calendar: " + title.getText() + ", verified the content is correct!");
					} else {
						SimpleUtils.fail("School Calendar: " + title.getText() + ", the content is incorrect!", false);
					}
				} else {
					SimpleUtils.fail("School Calendar: the content is incorrect!", false);
				}
			}
		} else {
			SimpleUtils.report("School Calendars: There is no calendars on this page!");
		}
	}

	@Override
	public void verifyTheCalendarListLoaded() throws Exception {
		if (areListElementVisible(calendarList, 10)) {
			SimpleUtils.pass("Calendars are loaded Successfully!");
		} else {
			SimpleUtils.report("School Calendars: There is no calendars on this page!");
		}
	}

	@Override
	public void verifyClickedDayIsHighlighted() throws Exception {
		if (areListElementVisible(schoolDays, 10)) {
			WebElement firstSchoolDay = schoolDays.get(0);
			moveToElementAndClick(firstSchoolDay);
			waitForSeconds(2);
			if (firstSchoolDay.getAttribute("class").contains("non-school-day")) {
				SimpleUtils.pass("Edit Calendar: Clicked Day is highlighted!");
			} else {
				SimpleUtils.fail("Edit Calendar: Clicked Day is not highlighted!", false);
			}
		}
	}

	@Override
	public boolean isEditCalendarModeLoaded() throws Exception {
		boolean isLoaded = false;
		if (isElementLoaded(cancelEditButton, 5) && isElementLoaded(saveButton, 5)) {
			isLoaded = true;
		}
		return isLoaded;
	}

	@Override
	public void clickOnEditAnywayButton() throws Exception {
		if (isElementLoaded(confirmButton, 5) && confirmButton.getText().equalsIgnoreCase("EDIT ANYWAY")) {
			clickTheElement(confirmButton);
		} else {
			SimpleUtils.fail("EDIT ANYWAY button failed to load!", false);
		}
	}

	@Override
	public void verifyEditCalendarAlertModelPopsUp() throws Exception {
		String expectedMessage = "Please note: Editing this school calendar will affect schedules of all Team Members that are currently assigned to this calendar.";
		if (isElementLoaded(confirmPopupWindow, 10) && isElementLoaded(popupMessage, 10)) {
			if (popupMessage.getText().trim().equals(expectedMessage) && isElementLoaded(cancelButton, 5) && isElementLoaded(confirmButton, 5)
			&& confirmButton.getText().equalsIgnoreCase("EDIT ANYWAY")) {
				SimpleUtils.pass("Click On Edit Calendar button successfully, Alert message and buttons loaded Successfully!");
			} else {
				SimpleUtils.fail("Edit Calendar: Alert message or buttons are incorrect!", false);
			}
		} else {
			SimpleUtils.fail("Edit Calendar: Alert model failed to load!", false);
		}
	}

	@Override
	public void verifySchoolSessionPageLoaded() throws Exception {
		if (isElementLoaded(calendarWrapper, 10)) {
			SimpleUtils.pass("School Session Calendar page loaded Successfully!");
		} else {
			SimpleUtils.fail("School Session Calendar page not loaded Successfully!", false);
		}
	}

	@Override
	public void clickOnEditCalendarButton() throws Exception {
		if (isElementLoaded(editCalendarBtn, 10)) {
			clickTheElement(editCalendarBtn);
		} else {
			SimpleUtils.fail("School Calendars page: Edit button failed to load!", false);
		}
	}

	@Override
	public void clickTheCalendarByRandom() throws Exception {
		if (areListElementVisible(calendarTitles, 10)) {
			int randomIndex = (new Random()).nextInt(calendarTitles.size());
			String calendarName = calendarTitles.get(randomIndex).getText();
			SimpleUtils.report("School Calendars Page: select calendar: " + calendarName);
			clickTheElement(calendarTitles.get(randomIndex));
			if (isElementLoaded(deleteCalendarBtn, 10) && isElementLoaded(editCalendarBtn, 10)) {
				SimpleUtils.pass("Click on the calendar: " + calendarName + " Successfully!");
			}
		} else {
			SimpleUtils.report("School Calendar: There is no calendars!");
		}
	}

	@Override
	public void setTheCalendarForMinors(List<String> minorNames, String calendarName, ProfileNewUIPage profileNewUIPage) throws Exception {
		if (minorNames != null && minorNames.size() > 0) {
			for (String minorName : minorNames) {
				searchAndSelectTeamMemberByName(minorName);
				profileNewUIPage.selectAGivenCalendarForMinor(calendarName);
				goToTeam();
				verifyTeamPageLoadedProperlyWithNoLoadingIcon();
			}
		}
	}

	@Override
	public void clickOnCancelButtonOnPopup() throws Exception {
		if (isElementLoaded(cancelButton, 5)) {
			clickTheElement(cancelButton);
			SimpleUtils.pass("Click the Cancel button on Popup Successfully!");
			waitUntilElementIsInVisible(cancelButton);
		} else {
			SimpleUtils.fail("Cancel button not loaded Successfully on popup!", false);
		}
	}

	@Override
	public void	clickOnDeleteCalendarButton() throws Exception {
		String warningMessage = "Please note: Deleting this school calendar will affect schedules of all Team Members that are currently assigned to this calendar.";
		if (isElementLoaded(deleteCalendarBtn, 10)) {
			clickTheElement(deleteCalendarBtn);
			if (isElementLoaded(confirmButton, 10) && confirmButton.getText().trim().equalsIgnoreCase("DELETE ANYWAY") && isElementLoaded(cancelButton, 5)
			&& isElementLoaded(warningOnPopup, 5) && warningOnPopup.getText().contains(warningMessage)) {
				SimpleUtils.pass("Click on DELETE calendar button Successfully, warning message pops up!");
			}
		} else {
			SimpleUtils.fail("Delete Calendar button not loaded Successfully!", false);
		}
	}

	@Override
	public void clickOnDELETEANYWAYButton() throws Exception {
		if (isElementLoaded(confirmButton, 10) && confirmButton.getText().trim().equalsIgnoreCase("DELETE ANYWAY")) {
			clickTheElement(confirmButton);
			waitForSeconds(3);
			if (isElementLoaded(schoolCalendarHeader, 10)) {
				SimpleUtils.pass("Delete the school calendar Successfully!");
			} else {
				SimpleUtils.fail("Failed to delete the school calendar!", false);
			}
		} else {
			SimpleUtils.fail("School Calendar: DELETE ANYWAY button not loaded Successfully!", false);
		}
	}

	@Override
	public void deleteCalendarByName(String calendarName) throws Exception {
		if (areListElementVisible(calendarTitles, 20)) {
			for (WebElement title : calendarTitles) {
				if (title.getText().trim().contains(calendarName)) {
					clickTheElement(title);
					waitForSeconds(3);
					if (areListElementVisible(calendarCells,  10) && isElementLoaded(deleteCalendarBtn, 10)) {
						waitForSeconds(3);
						clickTheElement(deleteCalendarBtn);
						waitForSeconds(2);
						if (isElementLoaded(confirmButton, 10) && confirmButton.getText().trim().equalsIgnoreCase("DELETE ANYWAY")) {
							clickTheElement(confirmButton);
							waitForSeconds(3);
							if (isElementLoaded(schoolCalendarHeader, 10)) {
								SimpleUtils.pass("Delete the school calendar Successfully!");
								break;
							} else {
								SimpleUtils.fail("Failed to delete the school calendar: " + calendarName, false);
							}
						} else {
							SimpleUtils.fail("School Calendar: DELETE ANYWAY button not loaded Successfully!", false);
						}
					} else {
						SimpleUtils.fail("Delete Calendar button not loaded Successfully!", false);
					}
				}
			}
		} else {
			SimpleUtils.report("School Calendar: There is no calendars!");
		}
	}

	@Override
	public void clickOnSaveSchoolCalendarBtn() throws Exception {
		if (isElementLoaded(savePreferButton, 5) && isElementEnabled(savePreferButton, 5)) {
			clickTheElement(savePreferButton);
			if (isElementLoaded(deleteCalendarBtn, 20) && isElementLoaded(editCalendarBtn, 20)) {
				SimpleUtils.pass("School Calendar: Save the calendar Successfully!");
			} else {
				SimpleUtils.fail("School Calendar: Failed to save the school calendar!", false);
			}
		} else {
			SimpleUtils.fail("School Calendars Page: Save Calendar button not loaded Successfully!", false);
		}
	}

	@Override
	public void clickOnCreateNewCalendarButton() throws Exception {
		if (isElementLoaded(createNewCalendarBtn, 10)) {
			clickTheElement(createNewCalendarBtn);
			if (isElementLoaded(cancelEditButton, 5) && isElementLoaded(savePreferButton, 5)) {
				SimpleUtils.pass("School Calendars Page: Click on 'Create New Calendar' button successfully!");
			} else {
				SimpleUtils.fail("School Calendars Page: Click on 'Create New Calendar' button failed, Cancel and Save buttons are not loaded!", false);
			}
		} else {
			SimpleUtils.fail("School Calendars Page: 'Create New Calendar' not loaded Successfully!", false);
		}
	}

	@Override
	public void selectSchoolSessionStartNEndDate() throws Exception {
		if (areListElementVisible(realDays, 10) && realDays.size() > 57) {
			clickTheElement(realDays.get(0));
			waitForSeconds(1);
			clickTheElement(realDays.get(realDays.size() - 1));
		} else {
			SimpleUtils.fail("School Calendar: Session start and end date calendar failed to loade!", false);
		}
	}

	@FindBy (css = ".school-calendars-year-switcher .fa-chevron-left")
	private WebElement yearSwitchLeft;

	@Override
	public void selectSchoolYear() throws Exception {
		try {
			Calendar calder = Calendar.getInstance();
			calder.setTime(new Date());
			int month = calder.get(Calendar.MONTH)+1;
			// If month is before August, need to switch to the previous year
			if (month < 7) {
				if (isElementLoaded(yearSwitchLeft, 5)) {
					clickTheElement(yearSwitchLeft);
					waitForSeconds(2);
					SimpleUtils.pass("School Calendar: Click on previous year switch successfully!");
				} else {
					SimpleUtils.fail("School Calendar: Previous Year Switch button not loaded Successfully!", false);
				}
			}
		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Override
	public void clickOnSaveSchoolSessionCalendarBtn() throws Exception {
		if (isElementLoaded(saveSchoolSessionBtn, 5)) {
			clickTheElement(saveSchoolSessionBtn);
			waitUntilElementIsInVisible(saveSchoolSessionBtn);
		} else {
			SimpleUtils.fail("School Calendar Page: Save School Session button not loaded Successfully!", false);
		}
	}

	@Override
	public void clickOnCancelSchoolSessionCalendarBtn() throws Exception {
		if (isElementLoaded(cancelSchoolSessionBtn, 5)) {
			clickTheElement(cancelSchoolSessionBtn);
			waitUntilElementIsInVisible(cancelSchoolSessionBtn);
		} else {
			SimpleUtils.fail("School Calendar Page: Cancel School Session button not loaded Successfully!", false);
		}
	}

	private void goToTheCurrentMonth() throws Exception {
		while (!isElementLoaded(todayHighlighted, 5)) {
			if (areListElementVisible(rightArrows, 5) && rightArrows.size() > 0) {
				clickTheElement(rightArrows.get(0));
				goToTheCurrentMonth();
			}
		}
	}

	@Override
	public boolean isCreateCalendarBtnLoaded() throws Exception {
		if (isElementLoaded(createNewCalendarBtn, 10)) {
			return true;
		}
		return false;
	}

	

	//	{
//    	if(isElementLoaded(rosterBodyElement))
//    	{
//    		return true;
//    	}else{
//    		return false;
//    	}
//    	
//	}
//    
//    public void goToCoverage() throws Exception
//    {
//    	if(isElementLoaded(TeamPageHeaderTabs.get(0)))
//    	{
//    		for(WebElement teamPageHeaderTabElement : TeamPageHeaderTabs)
//    		{
//    			if(teamPageHeaderTabElement.getText().contains("COVERAGE"))
//    			{
//        			click(teamPageHeaderTabElement);
//    			}
//    		}
//    	}
//    }
//    
//    public boolean isCoverage() throws Exception
//    {
//    	if(isElementLoaded(coverageViewElement))
//    	{
//    		return true;
//    	}else{
//    		return false;
//    	}
//    	
//    }
//    
//    public void verifyTeamPage(boolean isTeamPage){
//    	if(isTeamPage){
//    		SimpleUtils.pass("Team Page Loaded Successfully!");
//    	}else{
//    		SimpleUtils.fail("Team Page not loaded Successfully!",true);
//    	}
//    }
//    
//    public void verifyCoveragePage(boolean isCoveragePage){
//    	if(isCoveragePage){
//    		SimpleUtils.pass("Team Page - Coverage Section Loaded Successfully!");
//    	}else{
//    		SimpleUtils.fail("Team Page - Coverage Section not Loaded Successfully for LegionTech Environment! (Jira Ticket :4978) ",false);
//    	}
//    }
//    


	@Override
	public void selectARandomOnboardedOrNotTeamMemberToViewProfile(boolean selectOnboardedTM) throws Exception {
		WebElement teamMember = null;
		String teamMemberStatus = "";
		boolean isTMFound = false;
		if (areListElementVisible(teamMembers, 15) && areListElementVisible(teamMemberNames)
				&&teamMembers.size() == teamMemberNames.size()) {
			Random random = new Random();
			while(!isTMFound){
				int randomIndex = random.nextInt(teamMembers.size());
				teamMember = teamMembers.get(randomIndex);
				teamMemberStatus = teamMember.findElement(By.className("status-wrapper")).getText();
				if(selectOnboardedTM){
					if(teamMemberStatus.equalsIgnoreCase("Active")){
						clickTheElement(teamMemberNames.get(randomIndex));
						String onBoardedDate = getOnBoardedDate();
						if(onBoardedDate == null || onBoardedDate.equals("")){
							isTMFound = true;
						} else{
							goToTeam();
							verifyTeamPageLoadedProperlyWithNoLoadingIcon();
						}
					}
				} else {
					if(!teamMemberStatus.equalsIgnoreCase("Active")){
						clickTheElement(teamMemberNames.get(randomIndex));
						isTMFound = true;
					}
				}
			}
		} else {
			SimpleUtils.fail("Team Members are failed to load!", false);
		}
	}



	@FindBy (css = "span.month-header")
	private WebElement monthHeader;

	@Override
	public void terminateOrDeactivateTheTeamMemberFromSpecificDate(Boolean isTerminate,String fromDate) throws Exception {
		String removeMsg = "Successfully scheduled removal of Team Member from Roster.";
		String deactivationMsg = "Successfully scheduled deactivation of Team Member.";
		String actualMsg = "";
		scrollToBottom();
		if (isElementLoaded(cancelActivateButton, 5) || isElementLoaded(cancelTerminateButton, 5)){
			cancelTMDeactivate();
			cancelTMTerminate();
		}

		if (isTerminate) {
			if (isElementLoaded(terminateButton, 10)) {
				click(terminateButton);
				isTerminateWindowLoaded();
			} else
				SimpleUtils.fail("Terminate button fail to loaded! ", false);

		} else {
			if (isElementLoaded(deactivateButton, 10)) {
				click(deactivateButton);
				isActivateWindowLoaded();
			} else
				SimpleUtils.fail("Deactivate button fail to loaded! ", false);

		}

		if (isElementLoaded(monthHeader, 5)
				&& isElementLoaded(nextMonthArrow, 5)
				&& areListElementVisible(daysOnCalendar, 5)) {
			String[] dates = fromDate.split(" ");
			String year = dates[0];
			String month = dates[1];
			String day = dates[2];

			String monthInCalendar = monthHeader.getText().split(" ")[0].substring(0, 3);
			String yearInCalendar = monthHeader.getText().split(" ")[1];

			int i =0;
			while (i<10 && (!year.equalsIgnoreCase(yearInCalendar) || !month.equalsIgnoreCase(monthInCalendar))){
				click(nextMonthArrow);
				i ++;
				monthInCalendar = monthHeader.getText().split(" ")[0].substring(0, 3);
			}
			boolean isCurrentWeek = false;
			for (WebElement dayOnCalendar: daysOnCalendar){
				if(!isCurrentWeek) {
					if (dayOnCalendar.getText().equalsIgnoreCase("1")){
						isCurrentWeek = true;
						if (dayOnCalendar.getText().equalsIgnoreCase(day)) {
							click (dayOnCalendar);
							break;
						}
					}
				} else {
					if (dayOnCalendar.getText().equalsIgnoreCase(day)) {
						click (dayOnCalendar);
						break;
					}
				}
			}
			click(applyButton);
			if (isElementLoaded(confirmPopupWindow, 15) && isElementLoaded(confirmButton, 15)) {
				click(confirmButton);
				if (isElementLoaded(popupMessage, 15))
				{
					actualMsg = popupMessage.getText();
					if (isTerminate) {
						if (removeMsg.equals(actualMsg)) {
							SimpleUtils.pass("Terminate the team member successfully!");
						}else {
							SimpleUtils.fail("The pop up message is incorrect!", false);
						}
					} else {
						if (deactivationMsg.equals(actualMsg)) {
							SimpleUtils.pass("Deactivate the team member successfully!");
						}else {
							SimpleUtils.fail("The pop up message is incorrect!", false);
						}
					}
				}
			} else {
				SimpleUtils.fail("Confirm window doesn't show!", false);
			}
		} else
			SimpleUtils.fail("The items on calendar loaded fail!", false);

	}


	public boolean isCancelDeactivateButtonLoaded () throws Exception {
		boolean isLoaded = false;
		if (isElementLoaded(cancelActivateButton, 5)) {
			SimpleUtils.pass("Cancel deactivate button is Loaded!");
			isLoaded = true;
		}
		return isLoaded;
	}

	public void cancelTMDeactivate() throws Exception {
		scrollToBottom();
		if(isElementLoaded(cancelActivateButton, 5)){
			click(cancelActivateButton);
			if(isElementLoaded(confirmBtn, 5)) {
				click(confirmBtn);
//				if (isElementLoaded(popupMessage, 10)) {
//					String actualMessage = popupMessage.getText();
//					if (actualMessage.equals("Successfully cancelled deactivation of Team Member.")) {
//						SimpleUtils.pass("Cancel terminate the Team Member successfully!");
//					} else {
//						SimpleUtils.fail("Failed to activate the Team member", false);
//					}
//				}
			}
			if (isElementLoaded(deactivateButton, 5)) {
				SimpleUtils.report("Cancel deactivate successfully! ");
			} else
				SimpleUtils.fail("Cancel deactivate failed! ", false);

		} else
			SimpleUtils.report("Cancel deactivate button loaded fail! ");
	}


	public void cancelTMTerminate() throws Exception {
		scrollToBottom();
		if(isElementLoaded(cancelTerminateButton, 5)){
			click(cancelTerminateButton);
			if(isElementLoaded(confirmBtn, 5)) {
				click(confirmBtn);
//				if (isElementLoaded(popupMessage, 10)) {
//					String actualMessage = popupMessage.getText();
//					if (actualMessage.equals("Successfully cancelled removal of Team Member from Roster.")) {
//						SimpleUtils.pass("Cancel terminate the Team Member successfully!");
//					} else {
//						SimpleUtils.fail("Failed to activate the Team member", false);
//					}
//				}
			}
			if (isElementLoaded(terminateButton, 5)) {
				SimpleUtils.report("Cancel terminate successfully! ");
			} else
				SimpleUtils.fail("Cancel terminate failed! ", false);
		} else
			SimpleUtils.report("Cancel terminate button loaded fail! ");
	}

	public boolean checkIfTMExists(String tmName) throws Exception {
		boolean isTMExists = false;
		if(isElementLoaded(teamMemberSearchBox, 20) && areListElementVisible(teamMembers, 20)) {
			teamMemberSearchBox.sendKeys(Keys.CONTROL, "a");
			teamMemberSearchBox.sendKeys(Keys.DELETE);
			teamMemberSearchBox.sendKeys(tmName);
			waitForSeconds(4);
			int i = 0;
			while(teamMembers.size() == 0 && i< 3){
				teamMemberSearchBox.sendKeys(Keys.CONTROL, "a");
				teamMemberSearchBox.sendKeys(Keys.DELETE);
				teamMemberSearchBox.sendKeys(tmName);
				waitForSeconds(3);
				i++;
			}
			if (teamMembers.size() > 0){
				for (WebElement teamMember : teamMembers){
					if (teamMember != null) {
						WebElement name = teamMember.findElement(By.cssSelector("[data-testid=\"lg-table-name\"] span"));
						List<WebElement> titles = teamMember.findElements(By.cssSelector("[data-testid=\"lg-table-job-title\"] span"));
						WebElement status = teamMember.findElement(By.cssSelector("[data-testid=\"lg-table-legion-onboarding\"] span"));
						String title = "";
						if (name != null && titles != null && status != null && titles.size() > 0) {
							for (WebElement titleElement : titles) {
								title += titleElement.getText();
							}
							String nameJobTitleStatus = name.getText() + title + status.getText();
							if (nameJobTitleStatus.contains(tmName)) {
								isTMExists = true;
								SimpleUtils.pass("Team Page: Team Member '" + tmName + "' can be found Successfully.");
								break;
							}
						}else {
							SimpleUtils.fail("Failed to find the name, title and Status!", true);
						}
					}else {
						SimpleUtils.fail("Failed to find the tr element!", true);
					}
				}
			}
		} else
			SimpleUtils.fail("The search TM box or team members fail to load! ", false);
		return isTMExists;
	}



	@FindBy (css = "[ng-click=\"changeMonth(sessionStart, -1, true)\"]")
	private WebElement sessionStartLeftArrow;

	@FindBy (css = "[ng-click=\"changeMonth(sessionStart, 1)\"]")
	private WebElement sessionStartRightArrow;

	@FindBy (css = "[ng-click=\"changeMonth(sessionEnd, -1)\"]")
	private WebElement sessionEndLeftArrow;

	@FindBy (css = "[ng-click=\"changeMonth(sessionEnd, 1, true)\"]")
	private WebElement sessionEndRightArrow;

	// Date like: 2021 Jan 1
	public void selectSchoolSessionStartAndEndDate(String startDate, String endDate) throws Exception{
		String[] fullStartDate = startDate.split(" ");
		String[] fullEndDate = endDate.split(" ");

		String startYear = fullStartDate[0];
		String startMonth = fullStartDate[1];
		String startDay = fullStartDate[2];
		String endYear = fullEndDate[0];
		String endMonth = fullEndDate[1];
		String endDay = fullEndDate[2];

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MMM");
		String startYearAndMonth =startYear +" " +startMonth;
		String endYearAndMonth = endYear +" "+endMonth;

		String startYearAndMonthInCalendar= rangedCalendars.get(0).getText();
		String startYearInCalendar = startYearAndMonthInCalendar.split(" ")[1];
		String startMonthInCalendar = startYearAndMonthInCalendar.split(" ")[0].substring(0,3);
		while(!startYearAndMonth.equalsIgnoreCase(startYearInCalendar+ " "+startMonthInCalendar)){
			Date to = dateFormat.parse(startYearAndMonth);
			Date from = dateFormat.parse(startYearInCalendar+ " "+startMonthInCalendar);
			boolean needMoveForward = to.after(from);
			if(needMoveForward && isElementLoaded(sessionStartRightArrow, 5)){
				click(sessionStartRightArrow);
			} else if (!needMoveForward && isElementLoaded(sessionStartLeftArrow, 5)) {
				click(sessionStartLeftArrow);
			} else
				SimpleUtils.fail("The Session Start Arrows fail to load! ", false);
			startYearAndMonthInCalendar= rangedCalendars.get(0).getText();
			startYearInCalendar = startYearAndMonthInCalendar.split(" ")[1];
			startMonthInCalendar = startYearAndMonthInCalendar.split(" ")[0].substring(0,3);
		}

		if (areListElementVisible(daysInSessionStart, 5)) {
			boolean isDayExist = false;
			for (WebElement day: daysInSessionStart) {
				if (day.getText().equals(startDay)){
					click(day);
					isDayExist = true;
					break;
				}
			}
			if (!isDayExist){
				SimpleUtils.fail("Cannot find the session start day! ", false);
			}
		} else
			SimpleUtils.fail("Days in Session Start panel fail to load! ", false);

		String endYearAndMonthInCalendar= rangedCalendars.get(1).getText();
		String endYearInCalendar = endYearAndMonthInCalendar.split(" ")[1];
		String endMonthInCalendar = endYearAndMonthInCalendar.split(" ")[0].substring(0,3);
		while(!endYearAndMonth.equalsIgnoreCase(endYearInCalendar+ " "+endMonthInCalendar)){
			Date to = dateFormat.parse(endYearAndMonth);
			Date from = dateFormat.parse(endYearInCalendar+ " "+endMonthInCalendar);
			boolean needMoveForward = to.after(from);
			if(needMoveForward && isElementLoaded(sessionEndRightArrow, 5)){
				click(sessionEndRightArrow);
			} else if (!needMoveForward && isElementLoaded(sessionEndLeftArrow, 5)) {
				click(sessionEndLeftArrow);
			} else
				SimpleUtils.fail("The Session End Arrows fail to load! ", false);
			endYearAndMonthInCalendar= rangedCalendars.get(1).getText();
			endYearInCalendar = endYearAndMonthInCalendar.split(" ")[1];
			endMonthInCalendar = endYearAndMonthInCalendar.split(" ")[0].substring(0,3);
		}

		if (areListElementVisible(daysInSessionEnd, 5)) {
			boolean isDayExist = false;
			for (WebElement day: daysInSessionEnd) {
				if (day.getText().equals(endDay)){
					click(day);
					isDayExist = true;
					break;
				}
			}
			if (!isDayExist){
				SimpleUtils.fail("Cannot find the session end day! ", false);
			}
		} else
			SimpleUtils.fail("Days in Session end panel fail to load! ", false);
	}

	public void activeTMAndRejectOrApproveAllAvailabilityAndTimeOff(String firstName) throws Exception{

		ProfileNewUIPage profileNewUIPage = new ConsoleProfileNewUIPage();
		String workPreferencesLabel = "Work Preferences";
		goToTeam();

		if (checkIfTMExists(firstName)) {
			searchAndSelectTeamMemberByName(firstName);
			if(isManualOnBoardButtonLoaded()) {
				manualOnBoardTeamMember();
			}
			if (isActivateButtonLoaded()) {
				clickOnActivateButton();
				isActivateWindowLoaded();
				selectADateOnCalendarAndActivate();
			}
			if (isCancelTerminateButtonLoaded()) {
				cancelTMTerminate();
			}
			if (isCancelDeactivateButtonLoaded()) {
				cancelTMDeactivate();
			}

			profileNewUIPage.selectProfilePageSubSectionByLabel(workPreferencesLabel);
			profileNewUIPage.approveAllPendingAvailabilityRequest();

			profileNewUIPage.selectProfilePageSubSectionByLabel("Time Off");
			profileNewUIPage.rejectAllTimeOff();
			profileNewUIPage.cancelAllTimeOff();

		} else
			SimpleUtils.fail("The team member '"+ firstName +"' is not exists! ", false);
	}

	public boolean checkIsInviteButtonExists() {
		boolean isInviteButtonExists = false;
		if (areListElementVisible(teamMembers, 10)
				&& areListElementVisible(inviteButtons, 5)) {
			isInviteButtonExists = true;
			SimpleUtils.pass("The Invite buttons display correctly! ");
		} else
			SimpleUtils.report("The Invite buttons fail to load! ");
		return isInviteButtonExists;
	}

	@Override
	public int getLocationName() {
		int numberOfLocation = 0;
		if (areListElementVisible(locationColumn,8)) {
			for (int i = 1; i < locationColumn.size(); i++) {
				if (!(locationColumn.get(i).getText().equalsIgnoreCase(locationColumn.get(i-1).getText()))) {
					numberOfLocation= numberOfLocation+1;

				}
			}
			return numberOfLocation;
		}else

			return 0;
	}

	@Override
	public List<String> getCalendarCurrentStartAndEndTime() {
		List<String> calendarCurrentStartAndEndTime = new ArrayList<>();
		if (areListElementVisible(rangedCalendars,8)) {
			for (int i = 0; i < rangedCalendars.size(); i++) {
				calendarCurrentStartAndEndTime.add(rangedCalendars.get(i).getText());
				SimpleUtils.pass("Get calendar current start and end time successfully! ");
			}
		}else
			SimpleUtils.fail("Calendar current start and end time fail to load! ", false);
		return  calendarCurrentStartAndEndTime;
	}

	@Override
	public List<String> getAllCalendarMonthNames() {
		List<String> calendarMonthNames = new ArrayList<>();
		if (areListElementVisible(calendarMonths,8)) {
			for (int i = 0; i < calendarMonths.size(); i++) {
				calendarMonthNames.add(calendarMonths.get(i).getText());
				SimpleUtils.pass("Get calendar month names successfully! ");
			}
		}else
			SimpleUtils.fail("Calendar month names fail to load! ", false);
		return  calendarMonthNames;
	}



	@FindBy (xpath = "//div[contains(@class,'calendar-cell ng-binding ng-scope non-school-day day-bold')]/preceding-sibling::div")
	private List<WebElement> theNonSummerDaysInTheLastSchoolMonth;

	public void setNonSchoolDaysForNonSchoolWeek () {
		if (areListElementVisible(theNonSummerDaysInTheLastSchoolMonth, 10)
				&& theNonSummerDaysInTheLastSchoolMonth.size() > 8) {
			//Click the school days in the last school week to change to non-school day
			waitForSeconds(3);
			click(theNonSummerDaysInTheLastSchoolMonth.get(theNonSummerDaysInTheLastSchoolMonth.size() - 1));
			SimpleUtils.pass("Set the non-school week successfully! ");

		} else if (areListElementVisible(schoolDays, 10)){
			//Click the school days in the last school week to change to non-school day
			waitForSeconds(3);
			click(schoolDays.get(schoolDays.size() - 1));
			SimpleUtils.pass("Set the non-school week successfully! ");
		} else {
			SimpleUtils.report("The school days fail to load! ");
		}

	}

	@FindBy (css = "[label=\"Cancel Transfer\"]")
	private WebElement cancelTransferButton;

	public void cancelTransfer() throws Exception {
		if (isElementLoaded(cancelTransferButton, 5)) {
			clickTheElement(cancelTransferButton);
			String expectedMessage = "Are you sure you want to cancel the transfer to the new location?";
			String actualMessage = "";
			if (isElementLoaded(confirmPopupWindow, 5) && isElementLoaded(popupMessage, 5)) {
				actualMessage = popupMessage.findElement(By.tagName("span")).getText();
				if (expectedMessage.trim().equals(actualMessage.trim())){
					SimpleUtils.pass("Cancel Transfer window pops up!");
				}else {
					SimpleUtils.fail("The Message on Cancel Transfer window is incorrect!", true);
				}
				clickTheElement(confirmButton);
				if (isElementLoaded(transferButton, 15)) {
					SimpleUtils.pass("Cancelled Transfer successfully!");
				}else {
				SimpleUtils.fail("Failed to cancel the Transfer!", false);
			 	}
			} else {
				SimpleUtils.fail("Cancel Transfer pop-up window doesn't show!", true);
			}
		} else
			SimpleUtils.report("The cancel transfer button is not loaded! ");
	}

	@FindBy (className = "modal-content")
	private WebElement transferTMWindow;

	@FindBy (css = "div.location-carousel-next")
	private WebElement locationCarouselNext;
	public void transferTheTeamMemberOnSpecificDay(String transferLocation, String transferFromDate) throws Exception {
		if (isElementLoaded(transferButton, 5)) {
			clickTheElement(transferButton);
			boolean isTransferLocationExists = false;
			if (isElementLoaded(transferTMWindow, 5)) {
				String transferMsg = "Successfully transferred the Team Member";
				String actualMsg = "";
				if (areListElementVisible(locationCards, 5)) {
					while (!isTransferLocationExists) {
						for (WebElement locationCard: locationCards) {
							if (locationCard.findElement(By.cssSelector("div.location-card-name-text")).
									getText().equalsIgnoreCase(transferLocation)) {
								isTransferLocationExists = true;
								clickTheElement(locationCard);
							}
						}
						if (!isTransferLocationExists && isElementLoaded(locationCarouselNext, 5)) {
							clickTheElement(locationCarouselNext);
						}
					}

				}
				if (isElementLoaded(monthHeader, 5)
						&& isElementLoaded(nextMonthArrow, 5)
						&& areListElementVisible(daysOnCalendar, 5)) {
					String[] dates = transferFromDate.split(" ");
					String year = dates[0];
					String month = dates[1];
					String day = dates[2];

					String monthInCalendar = monthHeader.getText().split(" ")[0].substring(0, 3);
					String yearInCalendar = monthHeader.getText().split(" ")[1];

					int i =0;
					while (i<10 && (!year.equalsIgnoreCase(yearInCalendar) || !month.equalsIgnoreCase(monthInCalendar))){
						click(nextMonthArrow);
						i ++;
						monthInCalendar = monthHeader.getText().split(" ")[0].substring(0, 3);
					}
					boolean isCurrentWeek = false;
					for (WebElement dayOnCalendar: daysOnCalendar){
						if(!isCurrentWeek) {
							//There may be have several days from last month in this calendar, like: 26, 27, 28 , 29 , 30
							//So need to get the first day of this month, if the day is < 20th, the day must belong to the current month
							if (Integer.parseInt(dayOnCalendar.getText())< 20){
								isCurrentWeek = true;
								if (dayOnCalendar.getText().equalsIgnoreCase(day)) {
									click (dayOnCalendar);
									break;
								}
							}
						} else {
							if (dayOnCalendar.getText().equalsIgnoreCase(day)) {
								click (dayOnCalendar);
								break;
							}
						}
					}
					click(applyButton);
					if (isElementLoaded(confirmPopupWindow, 15) && isElementLoaded(confirmButton, 15)) {
						click(confirmButton);
						if (isElementLoaded(popupMessage, 15))
						{
							actualMsg = popupMessage.getText();
							if (transferMsg.equals(actualMsg)) {
								SimpleUtils.pass("Transfer the team member successfully!");
							}else {
								SimpleUtils.fail("The pop up message is incorrect!", false);
							}
						}
					} else {
						SimpleUtils.fail("Confirm window doesn't show!", false);
					}
				} else
					SimpleUtils.fail("The items on calendar loaded fail!", false);
			} else
				SimpleUtils.fail("The transfer TM window fail to load! ", false);

		} else
			SimpleUtils.fail("The transfer button is fail to loaded! ", false);
	}

	@FindBy (css = "[ng-click=\"viewProfile(worker)\"]")
	private List<WebElement> filteredTMs;

	@Override
	public void clickTheTMByName(String tmName) throws Exception {
		if (areListElementVisible(filteredTMs, 10)) {
			for (WebElement elm : filteredTMs) {
				if (elm.getText().trim().equalsIgnoreCase(tmName.trim())) {
					click(elm);
					waitForSeconds(3);
					SimpleUtils.pass("TM has been clicked!");
				}
			}
		} else {
			SimpleUtils.fail("No vailed TM filtered! TM name is: " + tmName, false);
		}
	}

	@FindBy (css = "[ng-click=\"editUserProfile()\"] [type=\"button\"]")
	private WebElement editProfileBtn;

	@Override
	public void clickEditProfileBtn() throws Exception {
		if (isElementLoaded(editProfileBtn, 10)) {
			clickTheElement(editProfileBtn);
			waitForSeconds(3);
			SimpleUtils.pass("Enter edit profile mode!");
		} else {
			SimpleUtils.fail("Profile edit button is failed for loading!", false);
		}
	}

	@FindBy (css = "#Symbols")
	private List<WebElement> badgesIcon;

	@FindBy (css = ".lg-badges-add")
	private WebElement addBadgeIcon;

	@FindBy (css = ".badge-section [type=\"button\"]")
	private WebElement manageBadgesOrAddABadgeBtn;

	@FindBy (css = "[type=\"submit\"]")
	private List<WebElement> saveEditUserProfileBtn;

	@Override
	public boolean isWithBadges() {
		return areListElementVisible(badgesIcon, 10);
	}

	@Override
	public void deleteBadges() throws Exception {
		if (areListElementVisible(badgesIcon, 15)) {
			if (isElementLoaded(manageBadgesOrAddABadgeBtn, 10) && manageBadgesOrAddABadgeBtn.getText().equalsIgnoreCase("Manage Badges")) {
				scrollToElement(manageBadgesOrAddABadgeBtn);
				clickTheElement(manageBadgesOrAddABadgeBtn);
				waitForSeconds(2);
			} else {
				SimpleUtils.fail("Manage badge button is not loaded!", false);
			}
			if (isManageBadgesLoaded()) {
				String isChecked = "checked";
				if (areListElementVisible(badgeCheckBoxes, 15)) {
					for (WebElement elm : badgeCheckBoxes) {
						if (elm.getAttribute("class").contains("checked")) {
							scrollToElement(elm);
							clickTheElement(elm);
							waitForSeconds(2);
							if (!elm.getAttribute("class").contains("checked")) {
								SimpleUtils.pass("Badge is removed!");
							} else {
								SimpleUtils.fail("Failed for removing badge!", false);
							}
						}
					}
				} else {
					SimpleUtils.fail("Failed for loading badge selection popup!", false);
				}
			}
			scrollToElement(confirmButton);
			clickTheElement(confirmButton);
			waitForSeconds(3);
			SimpleUtils.assertOnFail("Failed for delete badge!", !areListElementVisible(badgesIcon, 10),false);
		}
	}

	@FindBy (css = "[ng-repeat=\"oneBadge in badges\"]")
	private List<WebElement> badgeRowsInfo;

	@FindBy (css = ".badge-title")
	private List<WebElement> badgeTitleListOnPopup;

	@Override
	public void selectBadgeByName(String badgeName) throws Exception {
		if (isElementLoaded(manageBadgesOrAddABadgeBtn, 15)) {
			scrollToElement(manageBadgesOrAddABadgeBtn);
			clickTheElement(manageBadgesOrAddABadgeBtn);
			SimpleUtils.pass("Edit badge mode start!");
			waitForSeconds(3);
		} else {
			SimpleUtils.fail("Badge edit button is not loaded!", false);
		}
		if (areListElementVisible(badgeRowsInfo, 15) && areListElementVisible(badgeTitleListOnPopup, 10)) {
			for (WebElement elm : badgeRowsInfo) {
				WebElement targetBadgeNameElm = elm.findElement(By.cssSelector(".badge-title"));
				WebElement targetCheckBox = elm.findElement(By.className("lgnCheckBox"));
				if (targetBadgeNameElm.getText().equalsIgnoreCase(badgeName)) {
					scrollToElement(targetCheckBox);
					clickTheElement(targetCheckBox);
					waitForSeconds(2);
					if (targetCheckBox.getAttribute("class").contains("checked")) {
						SimpleUtils.pass("Check the Badge successfully!");
					} else {
						SimpleUtils.fail("Failed for selecting the checkbox for " + badgeName, true);
					}
				}
			}
			clickBadgeSaveBtn();
		} else {
			SimpleUtils.fail("Badge list in the popup is not loaded!", false);
		}
	}

	@FindBy (css = "button.lgn-action-button-success")
	private WebElement badgeSaveBtn;

	@Override
	public void clickBadgeSaveBtn() throws Exception {
		if (isElementLoaded(badgeSaveBtn, 10)) {
			scrollToElement(badgeSaveBtn);
			click(badgeSaveBtn);
			waitForSeconds(3);
			SimpleUtils.pass("Badge selection done!");
		} else {
			SimpleUtils.fail("Save badge button on the popup is not loaded!", false);
		}
	}

	@Override
	public void saveEditProfileBtn() {
		if (areListElementVisible(saveEditUserProfileBtn, 10)) {
			scrollToElement(saveEditUserProfileBtn.get(0));
			click(saveEditUserProfileBtn.get(0));
			waitForSeconds(3);
			SimpleUtils.pass("All badges have been removed!");
		} else {
			SimpleUtils.fail("Save edit user profile button is not loaded!", false);
		}
	}

	@FindBy (css = "[class*=\"select-wrapper ng-scope\"] select")
	private WebElement averageAgreementList;
	@FindBy (css = "[class=\"receiveOffers\"] [class*=\"averagingagreement\"]")
	private WebElement averageAgreementText;
	@Override
	public void selectAverageAgreement(String optionValue) throws Exception {
		if (isElementLoaded(averageAgreementList, 10)){
			Select selectedAverageAgreement = new Select(averageAgreementList);
			selectedAverageAgreement.selectByVisibleText(optionValue);
			SimpleUtils.report("Select '" + optionValue + "' as the budget group");
			waitForSeconds(2);
		} else {
			SimpleUtils.fail("Average Agreement section fail to load!", false);
		}
	}

	@Override
	public String getTextOfAverageAgreement() throws Exception {
		String textOfAverage = null;
		if (isElementLoaded(averageAgreementText, 10)){
			textOfAverage = averageAgreementText.getText().trim();
		} else {
			SimpleUtils.fail("Average Agreement text is not displayed!", true);
		}return textOfAverage;
	}

	@FindBy(css="[label=\"Show Rate\"]>button")
	private WebElement showRateBtn;
	@FindBy(css="[ng-if=\"canViewHourlyRate\"] [class*=\"value\"]")
	private WebElement hourlyRate;
	@Override
	public String getTextOfHourlyRate() throws Exception {
		String textOfHourlyRate = null;
		if (isElementLoaded(showRateBtn, 10))
			click(showRateBtn);
		if(isElementLoaded(hourlyRate, 10)){
			textOfHourlyRate = hourlyRate.getText().trim();
		}else
			SimpleUtils.fail("Hourly Rate is not displayed!",false);
		return textOfHourlyRate;
	}
}
