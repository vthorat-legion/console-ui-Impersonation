package com.legion.pages.core.OpsPortal;

import com.legion.pages.BasePage;
import com.legion.pages.OpsPortaPageFactories.ConfigurationPage;
import com.legion.pages.LocationSelectorPage;
import com.legion.pages.core.ConsoleLocationSelectorPage;
import com.legion.tests.TestBase;
import com.legion.tests.testframework.PropertyMap;
import com.legion.utils.CsvUtils;
import com.legion.utils.MyThreadLocal;
import com.legion.utils.SimpleUtils;
import cucumber.api.java.ro.Si;
import org.apache.commons.collections.ListUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.server.handler.ClickElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.legion.utils.MyThreadLocal.driver;
import static com.legion.utils.MyThreadLocal.getDriver;
import static com.legion.tests.TestBase.*;
import static com.legion.utils.MyThreadLocal.*;
import static org.apache.commons.lang.math.RandomUtils.nextInt;


public class OpsPortalConfigurationPage extends BasePage implements ConfigurationPage {

	private WebElement startElement;

	public OpsPortalConfigurationPage() {
		PageFactory.initElements(getDriver(), this);
	}

	@FindBy(css = ".console-navigation-item-label.Configuration")
	private WebElement configurationTab;

	@FindBy(css = "div[ng-repeat=\"cat in module\"] h1.categoryName")
	private List<WebElement> categoryOfTemplateList;

	@FindBy(css = "[class=\"tb-wrapper ng-scope\"] lg-dashboard-card h1")
	private List<WebElement> configurationCardsList;

	@FindBy(css = ".lg-dashboard-card")
	private List<WebElement> configurationCards;

	@FindBy(css = "div.card-carousel-card")
	private List<WebElement> smartCardsList;

	@FindBy(css = "span[class=\"lg-paged-search__showing top-right-action-button ng-scope\"] button")
	private WebElement newTemplateBTN;

	@FindBy(css = "div.lg-tab-toolbar__search")
	private WebElement searchField;

	@FindBy(css = "[class=\"lg-table lg-templates-table-improved ng-scope\"] .lg-templates-table-improved__grid-row.ng-scope")
	private List<WebElement> templatesList;

	@FindBy(css = "[class=\"lg-table lg-templates-table-improved ng-scope\"] button span.ng-binding")
	private List<WebElement> templateNameList;
	@FindBy(css = "lg-eg-status[type='Draft']")
	private List<WebElement> templateDraftStatusList;
	@FindBy(css = "div.toggle i[class=\"fa fa-caret-right\"]")
	private WebElement templateToggleButton;

	@FindBy(css = "lg-button[label=\"Edit\"]")
	private WebElement editButton;

	@FindBy(css = "div[class=\"lg-modal\"]")
	private WebElement editTemplatePopupPage;

	@FindBy(css = "lg-button[label=\"OK\"] button")
	private WebElement okButton;

	@FindBy(css = "lg-button[label=\"Cancel\"]")
	private WebElement cancelButton;

	@FindBy(css = "a[ng-click=\"$ctrl.back()\"]")
	private WebElement backButton;

	@FindBy(css = "div.lg-page-heading h1")
	private WebElement templateTitleOnDetailsPage;

	@FindBy(css = "div.lg-tabs nav[class=\"lg-tabs__nav\"]")
	private WebElement templateDetailsAssociateTab;

	@FindBy(css = "lg-tab[tab-title=\"Details\"]")
	private WebElement templateDetailsTab;

	@FindBy(css = "lg-tab[tab-title=\"Association\"]")
	private WebElement templateAssociationTab;

	@FindBy(css = "form[name=\"$ctrl.generalForm\"]")
	private WebElement templateDetailsPageForm;

	@FindBy(css = "lg-button[label=\"Save as draft\"] i.fa.fa-sort-down")
	private WebElement dropdownArrowButton;

	@FindBy(css = "button.saveas-drop")
	private WebElement dropdownArrowBTN;

	@FindBy(css = "lg-button[label=\"Save as draft\"] h3[ng-click*=\"publishNow\"]")
	private WebElement publishNowButton;

	@FindBy(css = "lg-button[label=\"Save as draft\"] button.pre-saveas")
	private WebElement saveAsDraftButton;

	@FindBy(css = "lg-button[label=\"Save as draft\"] h3[ng-click*= publishLater]")
	private WebElement publishLaterButton;

	@FindBy(css = "lg-button[label=\"Close\"]")
	private WebElement closeBTN;

	@FindBy(css = "lg-template-rule-container img.settings-add-icon")
	private WebElement addIconOnRulesListPage;

	@FindBy(css = "li[ng-click*=\"'Staffing\"]")
	private WebElement addStaffingRuleButton;

	@FindBy(css = "li[ng-click*=\"AdvancedStaffing\"]")
	private WebElement addAdvancedStaffingRuleButton;

	@FindBy(css = "sub-content-box[box-title=\"Days of Week\"]")
	private WebElement daysOfWeekSection;

	@FindBy(css = "[box-title=\"Dynamic Group\"]")
	private WebElement dynamicGroupSection;

	@FindBy(css = "sub-content-box[box-title=\"Time of Day\"]")
	private WebElement timeOfDaySection;

	@FindBy(css = "sub-content-box[box-title=\"Meal and Rest Breaks\"]")
	private WebElement mealAndRestBreaksSection;

	@FindBy(css = "sub-content-box[box-title=\"Number of Shifts\"]")
	private WebElement numberOfShiftsSection;

	@FindBy(css = "sub-content-box[box-title=\"Badges\"]")
	private WebElement badgesSection;

	@FindBy(css = "lg-button[label=\"Save\"] button")
	private WebElement saveButton;


	public enum configurationLandingPageTemplateCards {
		OperatingHours("Operating Hours"),
		SchedulingPolicies("Scheduling Policies"),
		ScheduleCollaboration("Schedule Collaboration"),
		TimeAttendance("Time & Attendance"),
		Compliance("Compliance"),
		SchedulingRules("Scheduling Rules"),
		SchedulingPolicyGroups("Scheduling Policy Groups"),
		Communications("Communications"),
		MinorsRules("Minors Rules"),
		MealAndRest("Meal and Rest"),
		AdditionalPayRules("Additional Pay Rules");
		private final String value;

		configurationLandingPageTemplateCards(final String newValue) {
			value = newValue;
		}

		public String getValue() {
			return value;
		}
	}


	public enum DynamicEmployeeGroupLabels {

		MinorRule("Minor Rule"),
		MealAndRest("MealAndRest"),
		DifferentialPay("Differential Pay");

		private final String value;

		DynamicEmployeeGroupLabels(final String newValue) {
			value = newValue;
		}

		public String getValue() {
			return value;
		}
	}

	public enum DynamicEmployeeGroupCriteria {

		WorkRole("Work Role"),
		JobTitle("Job Title"),
		Country("Country"),
		State("State"),
		City("City"),
		EmploymentType("Employment Type"),
		EmploymentStatus("Employment Status"),
		Exempt("Exempt"),
		Minor("Minor"),
		Badge("Badge"),
		Custom("Custom");

		private final String value;

		DynamicEmployeeGroupCriteria(final String newValue) {
			value = newValue;
		}

		public String getValue() {
			return value;
		}
	}

	public enum DynamicEmployeeGroupMinorCriteria {

		LessThan14("<14"),
		Equals14("14"),
		Equals15("15"),
		Equals16("16"),
		Equals17("17"),
		OlderOrEqualTo18(">=18");

		private final String value;

		DynamicEmployeeGroupMinorCriteria(final String newValue) {
			value = newValue;
		}

		public String getValue() {
			return value;
		}
	}

	public enum DynamicEmployeeGroupWorkRoleCriteria {

		EventManager("Event Manager"),
		GeneralManager("General Manager"),
		TeamMember("Team Member Corporate-Theatre");

		private final String value;

		DynamicEmployeeGroupWorkRoleCriteria(final String newValue) {
			value = newValue;
		}

		public String getValue() {
			return value;
		}
	}

	@Override
	public void goToConfigurationPage() throws Exception {
		if (isElementEnabled(configurationTab, 15)) {
			click(configurationTab);
			waitForSeconds(20);
			if (categoryOfTemplateList.size() != 0) {
//				checkAllTemplateCards();
				SimpleUtils.pass("User can click configuration tab successfully");
			} else {
				SimpleUtils.fail("User can't click configuration tab", false);
			}
		} else
			SimpleUtils.fail("Configuration tab load failed", false);
	}

	@Override
	public void checkAllTemplateCards() throws Exception {
		List<String> opTemplateTypes = new ArrayList<String>() {{
			add("Operating Hours");
			add("Scheduling Policies");
			add("Schedule Collaboration");
			add("Time & Attendance");
			add("Compliance");
			add("Scheduling Rules");
		}};
		if (configurationCardsList.size() > 0) {
			for (String opTemplateType : opTemplateTypes) {
				for (WebElement configurationCard : configurationCardsList) {
					String configurationCardName = configurationCard.getText().trim();
					if (opTemplateType.equals(configurationCardName)) {
						SimpleUtils.pass(opTemplateType + " is shown.");
						break;
					} else {
						continue;
					}
				}
			}
		} else if (configurationCardsList.size() == 10) {
			for (WebElement configurationCard : configurationCardsList) {
				if (configurationCard.getText().equals(configurationLandingPageTemplateCards.OperatingHours.getValue())) {
					SimpleUtils.pass(configurationLandingPageTemplateCards.OperatingHours.getValue() + " card is showing.");
					continue;
				} else if (configurationCard.getText().equals(configurationLandingPageTemplateCards.SchedulingPolicies.getValue())) {
					SimpleUtils.pass(configurationLandingPageTemplateCards.SchedulingPolicies.getValue() + " card is showing.");
					continue;
				} else if (configurationCard.getText().equals(configurationLandingPageTemplateCards.ScheduleCollaboration.getValue())) {
					SimpleUtils.pass(configurationLandingPageTemplateCards.ScheduleCollaboration.getValue() + " card is showing.");
					continue;
				} else if (configurationCard.getText().equals(configurationLandingPageTemplateCards.TimeAttendance.getValue())) {
					SimpleUtils.pass(configurationLandingPageTemplateCards.TimeAttendance.getValue() + " card is showing.");
					continue;
				} else if (configurationCard.getText().equals(configurationLandingPageTemplateCards.Compliance.getValue())) {
					SimpleUtils.pass(configurationLandingPageTemplateCards.Compliance.getValue() + " card is showing.");
					continue;
				} else if (configurationCard.getText().equals(configurationLandingPageTemplateCards.SchedulingRules.getValue())) {
					SimpleUtils.pass(configurationLandingPageTemplateCards.SchedulingRules.getValue() + " card is showing.");
					continue;
				} else if (configurationCard.getText().equals(configurationLandingPageTemplateCards.Communications.getValue())) {
					SimpleUtils.pass(configurationLandingPageTemplateCards.Communications.getValue() + " card is showing.");
					continue;
				} else if (configurationCard.getText().equals(configurationLandingPageTemplateCards.MinorsRules.getValue())) {
					SimpleUtils.pass(configurationLandingPageTemplateCards.MinorsRules.getValue() + " card is showing.");
					continue;
				} else if (configurationCard.getText().equals(configurationLandingPageTemplateCards.MealAndRest.getValue())) {
					SimpleUtils.pass(configurationLandingPageTemplateCards.MealAndRest.getValue() + " card is showing.");
					continue;
				} else if (configurationCard.getText().equals(configurationLandingPageTemplateCards.AdditionalPayRules.getValue())) {
					SimpleUtils.pass(configurationLandingPageTemplateCards.AdditionalPayRules.getValue() + " card is showing.");
					continue;
				} else {
					SimpleUtils.fail("Configuration template cards are loaded incorrect", false);
				}
			}
		} else if (configurationCardsList.size() == 11) {
			for (WebElement configurationCard : configurationCardsList) {
				if (configurationCard.getText().equals(configurationLandingPageTemplateCards.OperatingHours.getValue())) {
					SimpleUtils.pass(configurationLandingPageTemplateCards.OperatingHours.getValue() + " card is showing.");
					continue;
				} else if (configurationCard.getText().equals(configurationLandingPageTemplateCards.SchedulingPolicies.getValue())) {
					SimpleUtils.pass(configurationLandingPageTemplateCards.SchedulingPolicies.getValue() + " card is showing.");
					continue;
				} else if (configurationCard.getText().equals(configurationLandingPageTemplateCards.ScheduleCollaboration.getValue())) {
					SimpleUtils.pass(configurationLandingPageTemplateCards.ScheduleCollaboration.getValue() + " card is showing.");
					continue;
				} else if (configurationCard.getText().equals(configurationLandingPageTemplateCards.TimeAttendance.getValue())) {
					SimpleUtils.pass(configurationLandingPageTemplateCards.TimeAttendance.getValue() + " card is showing.");
					continue;
				} else if (configurationCard.getText().equals(configurationLandingPageTemplateCards.Compliance.getValue())) {
					SimpleUtils.pass(configurationLandingPageTemplateCards.Compliance.getValue() + " card is showing.");
					continue;
				} else if (configurationCard.getText().equals(configurationLandingPageTemplateCards.SchedulingRules.getValue())) {
					SimpleUtils.pass(configurationLandingPageTemplateCards.SchedulingRules.getValue() + " card is showing.");
					continue;
				} else if (configurationCard.getText().equals(configurationLandingPageTemplateCards.Communications.getValue())) {
					SimpleUtils.pass(configurationLandingPageTemplateCards.Communications.getValue() + " card is showing.");
					continue;
				} else if (configurationCard.getText().equals(configurationLandingPageTemplateCards.SchedulingPolicyGroups.getValue())) {
					SimpleUtils.pass(configurationLandingPageTemplateCards.SchedulingPolicyGroups.getValue() + " card is showing.");
					continue;
				} else if (configurationCard.getText().equals(configurationLandingPageTemplateCards.MinorsRules.getValue())) {
					SimpleUtils.pass(configurationLandingPageTemplateCards.MinorsRules.getValue() + " card is showing.");
					continue;
				} else if (configurationCard.getText().equals(configurationLandingPageTemplateCards.MealAndRest.getValue())) {
					SimpleUtils.pass(configurationLandingPageTemplateCards.MealAndRest.getValue() + " card is showing.");
					continue;
				} else if (configurationCard.getText().equals(configurationLandingPageTemplateCards.AdditionalPayRules.getValue())) {
					SimpleUtils.pass(configurationLandingPageTemplateCards.AdditionalPayRules.getValue() + " card is showing.");
					continue;
				} else {
					SimpleUtils.fail("Configuration template cards are loaded incorrect", false);
				}
			}
		} else {
			SimpleUtils.fail("Configuration landing page was loading failed", false);
		}
	}

	@Override
	public boolean isTemplateListPageShow() throws Exception {
		boolean flag = false;
		if (areListElementVisible(templatesList, 10)
				&& templatesList.size() != 0
				&& isElementEnabled(newTemplateBTN, 10)
				&& isElementEnabled(searchField, 10)) {
			SimpleUtils.pass("Template landing page shows well");
			flag = true;
		} else {
			SimpleUtils.fail("Template landing page was NOT loading well", false);
			flag = false;
		}
		return flag;
	}

	// open the first one template on template list page
	@Override
	public void clickOnTemplateName(String templateType) throws Exception {
		if (isTemplateListPageShow()) {
			String classValue = templatesList.get(0).getAttribute("class");
			if (classValue != null && classValue.contains("hasChildren")) {
				clickTheElement(templateToggleButton);
				waitForSeconds(3);
				clickTheElement(getDriver().findElement(By.cssSelector("[ng-repeat=\"child in item.childTemplate\"] button")));
				waitForSeconds(20);
				if (isElementEnabled(templateTitleOnDetailsPage) && isElementEnabled(closeBTN) && isElementEnabled(templateDetailsAssociateTab)
						&& isElementEnabled(templateDetailsPageForm)) {
					SimpleUtils.pass("User can open one " + templateType + " template succseefully");
				} else {
					SimpleUtils.fail("User open one " + templateType + " template failed", false);
				}
			} else {
				clickTheElement(templateNameList.get(0));
				waitForSeconds(20);
				if (isElementEnabled(templateTitleOnDetailsPage) && isElementEnabled(closeBTN) && isElementEnabled(templateDetailsAssociateTab)
						&& isElementEnabled(templateDetailsPageForm)) {
					SimpleUtils.pass("User can open one " + templateType + " template succseefully");
				} else {
					SimpleUtils.fail("User open one " + templateType + " template failed", false);
				}
			}
		} else {
			SimpleUtils.pass("There is No " + templateType + "template now");
		}
	}

	@Override
	public void clickOnConfigurationCrad(String templateType) throws Exception {
		if (templateType != null) {
			waitForSeconds(10);
			if (configurationCardsList.size() != 0) {
				for (WebElement configurationCard : configurationCardsList) {
					if (configurationCard.getText().contains(templateType)) {
						clickTheElement(configurationCard);
						waitForSeconds(10);
						SimpleUtils.pass("User can click " + templateType + " configuration card successfully!");
						break;
					}
				}
			} else {
				SimpleUtils.fail("configuration landing page was loaded failed", false);
			}
		} else {
			SimpleUtils.fail("Please specify which type of template would you open?", false);
		}
	}

	@FindBy(css = "ng-include[ng-if=\"noOfPublished > 0\"]")
	private WebElement OHListSmartCard;
	@FindBy(css = "table.lg-table tbody tr")
	private List<WebElement> OHListData;
	@FindBy(css = "div.lg-pagination__pages")
	private List<WebElement> OHListTurnPage;
	@FindBy(css = "span.ml-10.fs-14 a")
	private WebElement OHStartDaylink;
	@FindBy(css = "ul.dropdown-menu.oh-start-day li input-field")
	private List<WebElement> OHStartDayOptions;
	@FindBy(css = "span.ml-10 a")
	private List<WebElement> OHScheduleTimeLink;
	@FindBy(css = "ul.dropdown-menu.oh-start-time li input-field")
	private List<WebElement> OHScheduleTimeOptions;
	@FindBy(css = "span[ng-click=\"$ctrl.manageDayparts()\"]")
	private WebElement OHmanageDayPartlink;
	@FindBy(css = "modal[modal-title=\"Manage Dayparts\"]")
	private WebElement OHmanageDayPartDialog;
	@FindBy(css = "table.lg-table tbody tr[ng-repeat*='currentPageItems']")
	private List<WebElement> OHmanageDayPartData;
	@FindBy(css = "table[ng-if=\"$ctrl.getSelectedDayparts().length\"] tbody tr")
	private List<WebElement> OHmanageDayPartsSelectedData;
	@FindBy(css = "div.buffer-hours.ng-scope div.option-item")
	private List<WebElement> OHOperateBufferHrsOptions;
	@FindBy(css = "input-field[value=\"$ctrl.openingBufferHours\"] ng-form input")
	private WebElement OHOperateOpenCloseOffsetStartTime;
	@FindBy(css = "input-field[value=\"$ctrl.closingBufferHours\"] ng-form input")
	private WebElement OHOperateOpenCloseOffsetEndTime;
	@FindBy(css = "span.startend-time")
	private WebElement OHOperateOpenCloseTimeEditLink;
	@FindBy(css = "div.modal-dialog ")
	private WebElement OHOperateOpenCloseTimeEditDialog;
	@FindBy(css = "div.lgn-time-slider-notch-selector.lgn-time-slider-notch-selector-start")
	private WebElement OHOperateOpenCloseStartTimeDrag;
	@FindBy(css = "div.lgn-time-slider-notch-selector.lgn-time-slider-notch-selector-end")
	private WebElement OHOperateOpenCloseEndTimeDrag;
	@FindBy(css = "a[ng-if*='ContinuousOperation']")
	private WebElement OHOperateOpenCloseContinuousTimeLink;
	@FindBy(css = "ul.dropdown-menu.oh-co-start-time li input-field")
	private List<WebElement> OHOperateOpenCloseContinuousTimeOptions;
	@FindBy(css = "div.row-fx.mt-15.ng-scope")
	private List<WebElement> OHBusinessHoursEntries;
	@FindBy(css = "input-field[label=\"All days\"] input")
	private WebElement OHOperateOpenCloseAllDayOption;
	@FindBy(css = "input-field[type=\"checkbox\"] input")
	private List<WebElement> OHBusinessHoursDays;
	@FindBy(css = "nav.lg-tabs__nav div:nth-child(2)")
	private WebElement OHOperateDayPartTab;
	@FindBy(css = "div.availability-box.availability-box-ghost div")
	private List<WebElement> OHBusinessHoursTimeCells;
	@FindBy(css = "div.col-sm-8 div[tooltip-class=\"operating-hour-daypart-tooltip\"]")
	private List<WebElement> OHBusinessHoursTimeDayParts;


	@Override
	public void OHListPageCheck() throws Exception {
		//verify the smart card show
		if (isElementLoaded(OHListSmartCard)) {
			SimpleUtils.pass("Publised smartcard show at operating hours list page");
			//continue to check the published count at listed data
			int publishedCountAtSmart = Integer.parseInt(OHListSmartCard.findElement(By.cssSelector("h1")).getText().trim().split("Published")[0].trim());
			//get all published template at listed data
			int pageLength = Integer.parseInt(OHListTurnPage.get(0).getText().trim().split("of")[1].trim());
			int listedPublisedTP = 0;
			for (int round = 0; round < pageLength; round++) {
				for (WebElement OHdata : OHListData) {
					if (OHdata.findElement(By.cssSelector("td>lg-eg-status")).getAttribute("type").equals("Published"))
						listedPublisedTP++;
				}
				if (pageLength > 1)
					//turn page to continue
					selectByVisibleText(OHListTurnPage.get(0).findElement(By.cssSelector("select")), round + "2");
				else
					break;
			}
			//check the data matched
			if (listedPublisedTP == publishedCountAtSmart)
				SimpleUtils.pass("Published OH templates is equal to the data in smart card");
			else
				SimpleUtils.report("Published OH templates is not equal to the data in smart card");

		} else
			SimpleUtils.report("No Publised smartcard show at operating hours list page");
	}

	@Override
	public void createOHTemplateUICheck(String OHTempTemplate) throws Exception {
		//check template existed or not
		//search and archive the template
		if (searchTemplate(OHTempTemplate))
			if (templateDraftStatusList.size() > 0)
				//Delete the temp
				archivePublishedOrDeleteDraftTemplate(OHTempTemplate, "Delete");
		//click the add new template
		clickTheElement(newTemplateBTN);
		waitForSeconds(1);
		if (isElementEnabled(createNewTemplatePopupWindow)) {
			SimpleUtils.pass("User can click new template button successfully!");
			clickTheElement(newTemplateName);
			newTemplateName.sendKeys(OHTempTemplate);
			clickTheElement(newTemplateDescription);
			newTemplateDescription.sendKeys(OHTempTemplate);
			clickTheElement(continueBTN);
			waitForSeconds(4);
			if (isElementEnabled(welcomeCloseButton)) {
				clickTheElement(welcomeCloseButton);
			}
			//check the start day
			if (isElementLoaded(OHStartDaylink)) {
				SimpleUtils.pass("Already in operating hours detail page");
				clickTheElement(OHStartDaylink);
				waitForSeconds(1);
				if (OHStartDayOptions.size() == 7) {
					SimpleUtils.pass("There are 7 options for start day!");
					int random = new Random().nextInt(OHStartDayOptions.size());
					WebElement randomDay = OHStartDayOptions.get(random);
					//get the current selection
					String currentStartDay = randomDay.getAttribute("label").trim();
					clickTheElement(randomDay.findElement(By.cssSelector("ng-form input")));
					waitForSeconds(1);
					//get the start day value at detail that selected
					String selectedDay = OHStartDaylink.getText().trim();
					if (currentStartDay.equals(selectedDay))
						SimpleUtils.pass("The start day of week at OH template detail page works well");
					else
						SimpleUtils.report("The selection of start day of week at OH template detail page not work!");
				}
			} else
				SimpleUtils.fail("Some options are missing for start day settings!", false);

			//check the Time for Schedule week
			if (isElementLoaded(OHScheduleTimeLink.get(1))) {
				SimpleUtils.pass("Schedule start time loaded in operating hours detail page");
				int randoms = new Random().nextInt(OHScheduleTimeOptions.size());
				WebElement randomSch = OHScheduleTimeOptions.get(randoms);
				clickTheElement(OHScheduleTimeLink.get(1));
				waitForSeconds(1);
				if (OHScheduleTimeOptions.size() == 48) {
					SimpleUtils.pass("Schedule start time loaded with 48 options");
					//select a rando schedule time
					String selectedTime = randomSch.getAttribute("label").trim();
					clickTheElement(randomSch.findElement(By.cssSelector("ng-form input")));
					waitForSeconds(1);
					//get the current start day at detail page
					String currentStartTime = OHScheduleTimeLink.get(1).getText().trim();
					if (currentStartTime.equals(selectedTime))
						SimpleUtils.pass("The schedule start time at OH template detail page works well");
					else
						SimpleUtils.report("The selection of schedule start time at OH template detail page not work!");

				} else
					SimpleUtils.fail("Schedule start time not loaded with all options", false);
			}

			//check the day parts setting
			String selectDayPart = null;
			clickTheElement(OHmanageDayPartlink);
			waitForSeconds(1);
			waitForSeconds(1);
			if (isElementLoaded(OHmanageDayPartDialog)) {
				SimpleUtils.pass("The manage day parts dialog pop up successfully");
				if (areListElementVisible(OHmanageDayPartData, 5)) {
					OHmanageDayPartData.get(0).findElement(By.cssSelector("input-field[type=\"checkbox\"]")).click();
					waitForSeconds(1);
					selectDayPart = OHmanageDayPartData.get(0).findElement(By.cssSelector("td:nth-child(3) span")).getText().trim();
					clickTheElement(saveButton);
					waitForSeconds(1);
				} else
					SimpleUtils.report("No day parts option showed!");
				if (areListElementVisible(OHmanageDayPartsSelectedData, 5)) {
					SimpleUtils.pass("Set day part for the template success!");
					//get the selected day part
					String selectedDayPart = OHmanageDayPartsSelectedData.get(0).findElement(By.cssSelector("td:nth-child(1)")).getText().trim();
					if (selectedDayPart.equals(selectDayPart))
						SimpleUtils.pass("The day part showed is the one user selected");
				} else
					SimpleUtils.report("Set day part for the template failed");
			} else
				SimpleUtils.report("The manage day parts dialog not pop up");

			//check business hours settings
			//check the options number
			scrollToElement(OHBusinessHoursEntries.get(0));
			if (areListElementVisible(OHBusinessHoursEntries, 5) && OHBusinessHoursEntries.size() == 7) {
				SimpleUtils.pass("Business Hours options loaded successfully");
				//check the first option status
				boolean enableStatus = OHBusinessHoursEntries.get(0).findElement(By.cssSelector("input[type=\"checkbox\"]")).getAttribute("class").contains("ng-not-empty") ? true : false;
				OHTempBusinessHoursDaySetting(enableStatus);
				OHTempBusinessHoursDaySetting(!enableStatus);
			} else
				SimpleUtils.fail("Business Hours options not loaded", false);

			//check the Operating / Buffer Hours
			OHTempOperatinBufferHoursCheck();
			//save the template as draft
			if (isElementEnabled(saveAsDraftButton, 5)) {
				SimpleUtils.pass("User can click continue button successfully!");
				clickTheElement(saveAsDraftButton);
				waitForSeconds(5);
			}

			//search and archive the template
			if (searchTemplate(OHTempTemplate))
				if (templateDraftStatusList.size() > 0)
					//Delete the temp
					archivePublishedOrDeleteDraftTemplate(OHTempTemplate, "Delete");


		} else
			SimpleUtils.fail("Add new template button not loaded", false);
	}

	private void OHTempOperatinBufferHoursCheck() throws Exception {
		if (areListElementVisible(OHOperateBufferHrsOptions, 5) && OHOperateBufferHrsOptions.size() == 4) {
			SimpleUtils.report("Operating / Buffer Hours options showed correct;y at OH template detail page");
			//set the option for Open / Close
			OHOperateBufferHrsOptions.get(1).findElement(By.cssSelector("input-field:nth-child(1)")).click();
			waitForSeconds(1);
			//check the operating hours shift time edit dialog.
			clickTheElement(OHOperateOpenCloseTimeEditLink);
			if (isElementLoaded(OHOperateOpenCloseTimeEditDialog)) {
				SimpleUtils.pass("The dialog of 'Set Operating Hours opening and closing shift times' pops up successflly ");
				String str1 = OHOperateOpenCloseTimeEditDialog.findElement(By.cssSelector("lg-tab[tab-title=\"Open/Close\"] span.setting-title")).getText();
				if (str1.contains("opening and closing shift times"))
					SimpleUtils.pass("The dialog title is correct!");
				//get the original start time
				String OHStartOrigin = openCloseTimeInputs.get(0).findElement(By.xpath("./following-sibling::div")).getAttribute("innerText").trim();
				//get the original end time
				String OHEndOrigin = openCloseTimeInputs.get(1).findElement(By.xpath("./following-sibling::div")).getAttribute("innerText").trim();

				//change the start time and end time
				OHTempSetOpenAndCloseTime("Open/Close", "7:00AM", "8:00PM");

				//get the changed start time and end time
				String OHStartCurrent = openCloseTimeInputs.get(0).findElement(By.xpath("./following-sibling::div")).getAttribute("innerText").trim();
				String OHEndCurrent = openCloseTimeInputs.get(1).findElement(By.xpath("./following-sibling::div")).getAttribute("innerText").trim();
				if (!OHStartOrigin.equals(OHStartCurrent) && !OHEndOrigin.equals(OHEndCurrent))
					SimpleUtils.pass("change Operating Hours opening and closing shift times successfully");
				//click save
				clickTheElement(saveButton);
				waitForSeconds(1);
			}
			//Continuous Operation option check
			OHOperateBufferHrsOptions.get(3).findElement(By.cssSelector("input-field:nth-child(1)")).click();
			waitForSeconds(1);
			clickTheElement(OHOperateOpenCloseContinuousTimeLink);
			if (areListElementVisible(OHOperateOpenCloseContinuousTimeOptions, 5) && OHOperateOpenCloseContinuousTimeOptions.size() == 48) {
				SimpleUtils.pass("The Operation time set options are loaded successfully");
				clickTheElement(OHOperateOpenCloseContinuousTimeOptions.get(0).findElement(By.cssSelector("input-field ng-form input")));
				waitForSeconds(1);
			}
			//check the selected time option
			String currentClospeningTime = OHOperateOpenCloseContinuousTimeLink.getText().trim();
			if (currentClospeningTime.equals("12:00AM"))
				SimpleUtils.pass("The Continuous Operation time setting works well! ");

			//check the  Offset and none action
			OHOperateBufferHrsOptions.get(2).findElement(By.cssSelector("input-field:nth-child(1)")).click();
			waitForSeconds(1);
			//Offset start and  end time setting
			OHOperateOpenCloseOffsetStartTime.clear();
			OHOperateOpenCloseOffsetStartTime.sendKeys("0.5");
			OHOperateOpenCloseOffsetEndTime.clear();
			OHOperateOpenCloseOffsetEndTime.sendKeys("2");
			waitForSeconds(2);
			//change to select option as :none
			OHOperateBufferHrsOptions.get(0).findElement(By.cssSelector("input-field:nth-child(1)")).click();
			waitForSeconds(1);
			//get the Offset start and  end time setting and assert
			String offSetStart = OHOperateBufferHrsOptions.get(2).findElement(By.cssSelector("input-field[value=\"$ctrl.openingBufferHours\"] div.input-faked")).getAttribute("disabled");
			String offSetEnd = OHOperateBufferHrsOptions.get(2).findElement(By.cssSelector("input-field[value=\"$ctrl.closingBufferHours\"] div.input-faked")).getAttribute("disabled");
			if (offSetStart.equalsIgnoreCase("disabled") && offSetEnd.equalsIgnoreCase("disabled"))
				SimpleUtils.pass("Change clos/open option to none will set the offset change as default");

		} else
			SimpleUtils.report("No Operating / Buffer Hours options or not enough options showed at OH template detail page");

	}

	@FindBy(css = "input-field.lg-new-time-input-text input")
	private List<WebElement> openCloseTimeInputs;
	@FindBy(css = "table.lg-table.daypart-open-close-config tr>th")
	private List<WebElement> columnsInDaypartsTab;

	private void OHTempSetOpenAndCloseTime(String settingTab, String openTime, String closeTime) throws Exception {
		if (isElementLoaded(OHOperateOpenCloseTimeEditDialog, 10)) {
			if (settingTab.contains("Open")) {
				openCloseTimeInputs.get(0).clear();
				openCloseTimeInputs.get(0).sendKeys(openTime);
				openCloseTimeInputs.get(1).clear();
				openCloseTimeInputs.get(1).sendKeys(closeTime);
			} else if (settingTab.contains("Dayparts")) {
				openCloseTimeInputs.get(2).clear();
				openCloseTimeInputs.get(2).sendKeys(openTime);
				openCloseTimeInputs.get(3).clear();
				openCloseTimeInputs.get(3).sendKeys(closeTime);
			} else {
				SimpleUtils.fail("Can not find the setting item!", false);
			}
		} else {
			SimpleUtils.fail("There should pop up a window before set open/close time!", false);
		}
	}

	private void OHTempBusinessHoursDaySetting(boolean status) throws Exception {
		if (status) {
			//disable the day
			SimpleUtils.report("The day of business hours option is opened");
			//Click to set it as disabled
			OHBusinessHoursEntries.get(0).findElement(By.cssSelector("label.switch span")).click();
			waitForSeconds(1);
			String currentSta = OHBusinessHoursEntries.get(0).findElement(By.cssSelector("div.col-sm-8")).getText().trim();
			if (currentSta.contains("Closed for the day"))
				SimpleUtils.pass("Set the day of as closed successfully!");
		} else {//enable the day
			SimpleUtils.report("The day of business hours option is closed");
			//Click to set it as enabled
			OHBusinessHoursEntries.get(0).findElement(By.cssSelector("label.switch span")).click();
			waitForSeconds(1);
			WebElement defaultDaypartSet = OHBusinessHoursEntries.get(0).findElement(By.cssSelector("div.col-sm-8 label"));
			if (isElementLoaded(defaultDaypartSet) && defaultDaypartSet.getText().trim().contains("Dayparts Not Set"))
				SimpleUtils.pass("Set the day of as opened successfully and default as no day parts set");
			//get default start time and end time
			String StartOrigin = OHBusinessHoursEntries.get(0).findElement(By.cssSelector("span.work-time")).getText().trim();
			//get the original end time
			String EndOrigin = OHBusinessHoursEntries.get(0).findElement(By.cssSelector("span.work-time.mr-2")).getText().trim();
			if (StartOrigin.equals("12:00am") && EndOrigin.equals("12:00am"))
				SimpleUtils.pass("The default start time and end time show correctly");
			//change the time
			OHBusinessHoursEntries.get(0).findElement(By.cssSelector("lg-button[label=\"Edit\"]")).click();
			waitForSeconds(1);
			//change the start time and end time
			OHTempSetOpenAndCloseTime("Open/Close", "6:00AM", "9:00PM");
			waitForSeconds(1);
			//check the all day selection
			boolean selection = true;
			if (OHBusinessHoursDays.get(OHBusinessHoursDays.size() - 1).getAttribute("class").contains("ng-empty")) {
				clickTheElement(OHOperateOpenCloseAllDayOption);
				waitForSeconds(1);
				for (WebElement days : OHBusinessHoursDays) {
					if (days.getAttribute("class").contains("ng-empty")) ;
					{
						selection = false;
						break;
					}
				}
				if (!selection)
					SimpleUtils.pass("Check 'All days' option select all options successfully");
				else
					SimpleUtils.report("Check 'All days' option select all options failed!");
			}
			//switch to day part tab
			clickTheElement(OHOperateDayPartTab);
			waitForSeconds(2);
			if (areListElementVisible(columnsInDaypartsTab) && columnsInDaypartsTab.size() == 4) {
				SimpleUtils.pass("Day part time cell show correctly");
				//select a day part
				OHTempSetOpenAndCloseTime("Dayparts", "6:00AM", "5:00PM");
				//click save
				clickTheElement(saveButton);
				waitForSeconds(1);
				//check all day has set day part
				if (areListElementVisible(OHBusinessHoursTimeDayParts) && OHBusinessHoursTimeDayParts.size() == 7)
					SimpleUtils.pass("Day part set for every day success!");
			}
			//get the start time and end time again and assert time was updated
			//get default start time and end time for the first day again
			String StartCurrent = OHBusinessHoursEntries.get(0).findElement(By.cssSelector("span.work-time")).getText().trim();
			//get the original end time
			String EndCurrent = OHBusinessHoursEntries.get(0).findElement(By.cssSelector("span.work-time.mr-2")).getText().trim();
			if (!StartOrigin.equals(StartCurrent) && !EndOrigin.equals(EndCurrent))
				SimpleUtils.pass("Operating hours start time and end time was changes successfully");


		}
	}


	@Override
	public void goToTemplateDetailsPage(String templateType) throws Exception {
		waitForSeconds(5);
		clickOnConfigurationCrad(templateType);
		clickOnTemplateName(templateType);
	}

	@FindBy(css = "input[placeholder=\"You can search by template name, status and creator.\"]")
	private WebElement searchTemplateInputBox;

	@Override
	public boolean searchTemplate(String templateName) throws Exception {
		boolean exsiting = false;
		if (isElementEnabled(searchTemplateInputBox, 5)) {
			clickTheElement(searchTemplateInputBox);
			searchTemplateInputBox.clear();
			searchTemplateInputBox.sendKeys(templateName);
			searchTemplateInputBox.sendKeys(Keys.ENTER);
			waitForSeconds(2);
			if (templateNameList.size() > 0)
				exsiting = true;

		}
		return exsiting;
	}

	@FindBy(css = ".slider")
	private List<WebElement> radios;

	public void changeOHtemp() throws Exception {
		if (radios.size() > 0)
			clickTheElement(radios.get(0));
		//tap ok
		if (isElementLoaded(editTemplatePopupPage, 5)) {
			clickTheElement(okButton);
			waitForSeconds(10);
			if (isElementLoaded(editTemplatePopupPage)) {
				SimpleUtils.fail("Edit pop up not disappear after click OK button", false);
			}
		}
		//click save as draft
		scrollToBottom();
		if (isElementEnabled(saveAsDraftButton)) {
			SimpleUtils.pass("User can click continue button successfully!");
			clickTheElement(saveAsDraftButton);
			waitForSeconds(3);
		}
	}

	@FindBy(css = "lg-button[icon=\"'img/legion/audithistory.svg'\"]")
	private WebElement historyButton;
	@FindBy(css = ".session li.new-audit-log")
	private List<WebElement> historyRecords;

	public int historyRecordLimitCheck(String templateName) throws Exception {
		int current = 0;
		searchTemplate(templateName);
		if (templateNameList.size() > 0) {
			clickTheElement(templateNameList.get(0));
//			wait(3);
			waitForSeconds(3);
			//click the history button ond detail
			clickTheElement(historyButton);
			waitForSeconds(10);
			if (areListElementVisible(historyRecords, 5)) {
				current = historyRecords.size();
			}
		} else
			SimpleUtils.fail("No searched results for the template", false);
		return current;

	}


	//open the specify template to edit or view details
	@Override
	public void clickOnSpecifyTemplateName(String templateName, String editOrViewMode) throws Exception {
		waitForSeconds(10);
		if (isTemplateListPageShow()) {
			searchTemplate(templateName);
			for (int i = 0; i < templateNameList.size(); i++) {
				if (templateNameList.get(i).getText() != null && templateNameList.get(i).getText().trim().equals(templateName)) {
					String classValue = templatesList.get(i).getAttribute("class");
					if (classValue != null && classValue.contains("hasChildren")) {
//						clickTheElement(templatesList.get(i).findElement(By.className("toggle")));
						clickTheElement(templatesList.get(i).findElement(By.cssSelector(".toggle i")));
						waitForSeconds(3);
						if (editOrViewMode != null && editOrViewMode.toLowerCase().contains("edit")) {
							clickTheElement(getDriver().findElement(By.cssSelector("[ng-repeat=\"child in item.childTemplate\"] button")));
						} else {
							clickTheElement(templatesList.get(i).findElement(By.tagName("button")));
						}
						waitForSeconds(15);
						if (isElementEnabled(templateTitleOnDetailsPage) && isElementEnabled(closeBTN) && isElementEnabled(templateDetailsAssociateTab)
								&& isElementEnabled(templateDetailsPageForm)) {
							SimpleUtils.pass("User can open " + templateName + " template succseefully");
						} else {
							SimpleUtils.fail("User open " + templateName + " template failed", false);
						}
					} else {
						clickTheElement(templatesList.get(i).findElement(By.cssSelector("button")));
						waitForSeconds(15);
						if (isElementEnabled(templateTitleOnDetailsPage) && isElementEnabled(closeBTN) && isElementEnabled(templateDetailsAssociateTab)
								&& isElementEnabled(templateDetailsPageForm)) {
							SimpleUtils.pass("User can open " + templateName + " template succseefully");
						} else {
							SimpleUtils.fail("User open " + templateName + " template failed", false);
						}
					}
					break;
				}
//				else if(i==templateNameList.size()-1){
//					SimpleUtils.fail("Can't find the specify template",false);
//				}
			}
		} else {
			SimpleUtils.fail("There is No template now", false);
		}
	}

	@Override
	public void clickOnEditButtonOnTemplateDetailsPage() throws Exception {
		if (isElementLoaded(editButton, 20)) {
			clickTheElement(editButton);
			waitForSeconds(3);
			if (isElementEnabled(editTemplatePopupPage, 5)) {
				SimpleUtils.pass("Click edit button successfully!");
				clickTheElement(okButton);
				if (isElementEnabled(dropdownArrowButton, 20)) {
					SimpleUtils.pass("Template is in edit mode now");
				} else {
					SimpleUtils.fail("Template is not in edit mode now", false);
				}
			} else {
				SimpleUtils.fail("Click edit button successfully!", false);
			}
		} else {
			SimpleUtils.fail("Template details page is loaded failed", false);
		}
	}


	@FindBy(css = "table.lg-table.ng-scope tbody")
	private List<WebElement> workRoleList;
	@FindBy(css = "input[placeholder='Search by Work Role']")
	private WebElement searchByWorkRoleInput;

	@Override
	public void selectWorkRoleToEdit(String workRole) throws Exception {
		if (isElementLoaded(searchByWorkRoleInput, 10)) {
			searchByWorkRoleInput.clear();
			searchByWorkRoleInput.sendKeys(workRole);
			waitForSeconds(1);
			if (areListElementVisible(workRoleList, 3) && workRoleList.size() != 0) {
				for (WebElement workRoleItem : workRoleList) {
					String workRoleName = workRoleItem.findElement(By.cssSelector("td.ng-binding")).getText().trim();
					//get first char of the work role name
					char fir = workRole.charAt(0);
					String newWorkRole = String.valueOf(fir).toUpperCase() + " " + workRole;
					if (workRoleName.equalsIgnoreCase(newWorkRole)) {
						WebElement staffingRulesAddButton = workRoleItem.findElement(By.cssSelector("lg-button"));
						clickTheElement(staffingRulesAddButton);
						waitForSeconds(5);
						if (isElementEnabled(addIconOnRulesListPage)) {
							SimpleUtils.pass("Successful to select " + workRole + " to edit");
						} else {
							SimpleUtils.fail("Failed to select " + workRole + " to edit", false);
						}
						break;
					}
				}
				int i = 0;
				while (i < 10 && areListElementVisible(paginationRightArrow, 5)
						&& !paginationRightArrow.get(0).getAttribute("class").contains("disabled")) {
					clickTheElement(paginationRightArrow.get(0));
					for (WebElement workRoleItem : workRoleList) {
						String workRoleName = workRoleItem.findElement(By.cssSelector("td.ng-binding")).getText().trim();
						//get first char of the work role name
						char fir = workRole.charAt(0);
						String newWorkRole = String.valueOf(fir).toUpperCase() + " " + workRole;
						if (workRoleName.equals(newWorkRole)) {
							WebElement staffingRulesAddButton = workRoleItem.findElement(By.cssSelector("lg-button"));
							clickTheElement(staffingRulesAddButton);
							waitForSeconds(5);
							if (isElementEnabled(addIconOnRulesListPage)) {
								SimpleUtils.pass("Successful to select " + workRole + " to edit");
							} else {
								SimpleUtils.fail("Failed to select " + workRole + " to edit", false);
							}
							break;
						}
					}
					i++;
				}
			} else {
				SimpleUtils.fail("There is no work role for enterprise now", false);
			}
		} else {
			SimpleUtils.fail("Search by Work Role input failed to load!", false);
		}
	}

	@FindBy(css = "[tab-title=\"Details\"] div.lg-pagination__arrow--right")
	private List<WebElement> paginationRightArrow;

	public String getCountOfStaffingRules(String workRole) throws Exception {
		String count = null;
		if (isElementLoaded(searchByWorkRoleInput, 10)) {
			searchByWorkRoleInput.sendKeys(workRole);
			waitForSeconds(1);
			if (areListElementVisible(workRoleList, 10) && workRoleList.size() != 0) {
				for (WebElement workRoleItem : workRoleList) {
					String workRoleName = workRoleItem.findElement(By.cssSelector("td.ng-binding")).getText().trim();
					//get first char of the work role name
					char fir = workRole.charAt(0);
					String newWorkRole = String.valueOf(fir).toUpperCase() + " " + workRole;
					if (workRoleName.equals(newWorkRole)) {
						String firstLetter = workRoleItem.findElement(By.cssSelector("lg-button span.ng-binding")).getText().trim().split(" ")[0];
						if (firstLetter.equals("+")) {
							count = "0";
							SimpleUtils.pass("There is no staffing rules for this work role");
						} else {
							count = firstLetter;
							SimpleUtils.pass(workRole + " have " + count + " staffing rules now!");
						}
						break;
					}
				}
				int i = 0;
				while (i < 10 && areListElementVisible(paginationRightArrow, 10)
						&& !paginationRightArrow.get(0).getAttribute("class").contains("disabled")) {
					clickTheElement(paginationRightArrow.get(0));
					for (WebElement workRoleItem : workRoleList) {
						String workRoleName = workRoleItem.findElement(By.cssSelector("td.ng-binding")).getText().trim();
						//get first char of the work role name
						char fir = workRole.charAt(0);
						String newWorkRole = String.valueOf(fir).toUpperCase() + " " + workRole;
						if (workRoleName.equals(newWorkRole)) {
							String firstLetter = workRoleItem.findElement(By.cssSelector("lg-button span.ng-binding")).getText().trim().split(" ")[0];
							if (firstLetter.equals("+")) {
								count = "0";
								SimpleUtils.pass("There is no staffing rules for this work role");
							} else {
								count = firstLetter;
								SimpleUtils.pass(workRole + " have " + count + " staffing rules now!");
							}
							break;
						}
					}
					i++;
				}
			} else {
				SimpleUtils.fail("There is no work role for enterprise now", false);
			}
		} else {
			SimpleUtils.fail("Search by Work Role input failed to load!", false);
		}
		return count;
	}


	@Override
	public void checkTheEntryOfAddAdvancedStaffingRule() throws Exception {
		waitForSeconds(5);
		if (isElementEnabled(addIconOnRulesListPage)) {
			clickTheElement(addIconOnRulesListPage);
			if (isElementEnabled(addAdvancedStaffingRuleButton)) {
				SimpleUtils.pass("Advance staffing rules tab is show");
				clickTheElement(addAdvancedStaffingRuleButton);
				if (isElementEnabled(dynamicGroupSection)) {
					SimpleUtils.pass("Advance staffing rules tab is clickable");
				} else {
					SimpleUtils.fail("Advance staffing rules tab is NOT clickable", false);
				}
			} else {
				SimpleUtils.pass("Advance staffing rules tab is NOT show");
			}
		} else {
			SimpleUtils.fail("Work role's staffing rules list page was loaded failed", false);
		}
	}

	@Override
	public void verifyAdvancedStaffingRulePageShowWell() throws Exception {
		if (isElementEnabled(dynamicGroupSection) && isElementEnabled(daysOfWeekSection) && isElementEnabled(timeOfDaySection)
				&& isElementEnabled(mealAndRestBreaksSection) && isElementEnabled(numberOfShiftsSection)
				&& isElementEnabled(badgesSection) && isElementEnabled(cancelButton)) {
			SimpleUtils.pass("New advanced staffing rule page shows well");
			scrollToBottom();
		} else {
			SimpleUtils.fail("New advanced staffing rule page doesn't show well", false);
		}
	}

	@FindBy(css = "sub-content-box[box-title=\"Days of Week\"] input")
	private List<WebElement> daysOfWeekCheckBoxList;

	@FindBy(css = "sub-content-box[box-title=\"Days of Week\"] label.input-label")
	private List<WebElement> daysOfWeekLabelList;

	@Override
	public boolean isDaysOfWeekFormulaCheckBoxChecked() {
		boolean flag = false;
		if (daysOfWeekCheckBoxList.size() != 0) {
			String classValueOfCheckBox = daysOfWeekCheckBoxList.get(0).getAttribute("class");
			if (classValueOfCheckBox.contains("ng-not-empty")) {
				SimpleUtils.pass("The formula check box of days of week is checked already!");
				flag = true;
			} else {
				SimpleUtils.pass("The formula check box of days of week is NOT checked yet!");
				flag = false;
			}
		}
		return flag;
	}

	@Override
	public void verifyCheckBoxOfDaysOfWeekSection() throws Exception {
		if (isDaysOfWeekFormulaCheckBoxChecked()) {
			if (daysOfWeekCheckBoxList.size() == 1) {
				SimpleUtils.pass("The select day sub-section is disappeared after check the formula checkbox");
			} else {
				SimpleUtils.fail("The select day sub-section is NOT disappeared after check the formula checkbox", true);
			}
		} else {
			//verify check box for each day section
			for (int i = 1; i < daysOfWeekCheckBoxList.size(); i++) {
				if (i < daysOfWeekCheckBoxList.size() - 1) {
					clickTheElement(daysOfWeekCheckBoxList.get(i));
				}
				if (daysOfWeekCheckBoxList.get(i).getAttribute("class").contains("ng-not-empty")) {
					SimpleUtils.pass(daysOfWeekLabelList.get(i).getText().trim() + " has been selected successfully!");
				} else {
					SimpleUtils.fail(daysOfWeekLabelList.get(i).getText().trim() + " has NOT been selected successfully!", false);
				}
			}
			//verify check box for Custom formula
			clickTheElement(daysOfWeekCheckBoxList.get(0));
			if (daysOfWeekCheckBoxList.get(0).getAttribute("class").contains("ng-not-empty") && daysOfWeekCheckBoxList.size() == 1) {
				SimpleUtils.pass(daysOfWeekLabelList.get(0).getText().trim() + " has been selected successfully!");
			} else {
				SimpleUtils.fail(daysOfWeekLabelList.get(0).getText().trim() + " has NOT been selected successfully!", false);
			}
		}
	}

	@FindBy(css = "sub-content-box[box-title=\"Days of Week\"] textarea")
	private WebElement formulaTextAreaOfDaysOfWeekSection;

	@Override
	public void inputFormulaInForDaysOfWeekSection(String formula) throws Exception {
		if (isDaysOfWeekFormulaCheckBoxChecked()) {
			clickTheElement(formulaTextAreaOfDaysOfWeekSection);
			formulaTextAreaOfDaysOfWeekSection.sendKeys(formula);
		} else {
			clickTheElement(daysOfWeekCheckBoxList.get(0));
			clickTheElement(formulaTextAreaOfDaysOfWeekSection);
			formulaTextAreaOfDaysOfWeekSection.sendKeys(formula);
		}

		String formulaValue = getDriver().findElement(By.cssSelector("sub-content-box[box-title=\"Days of Week\"] input-field[type=\"textarea\"] ng-form div")).getAttribute("innerText").trim();
		if (formulaValue.equals(formula)) {
			SimpleUtils.pass("User can input formula for days of week successfully!");
		} else {
			SimpleUtils.fail("User can NOT input formula for days of week successfully!", false);
		}

	}

	@FindBy(css = "sub-content-box[box-title=\"Days of Week\"] div.day-selector input-field")
	private List<WebElement> daysOfWeekList;

	@Override
	public void selectDaysForDaysOfWeekSection(List<String> days) throws Exception {
		if (daysOfWeekCheckBoxList.size() > 1) {
			for (String day : days) {
				for (WebElement daysOfWeek : daysOfWeekList) {
					String dayname = daysOfWeek.findElement(By.cssSelector("label.input-label")).getText().trim();
					WebElement checkBoxOfDay = daysOfWeek.findElement(By.cssSelector("input"));
					if (day.equals(dayname)) {
						clickTheElement(checkBoxOfDay);
						if (checkBoxOfDay.getAttribute("class").contains("ng-not-empty")) {
							SimpleUtils.pass(dayname + " has been selected successfully!");
						} else {
							SimpleUtils.fail("User failed to select " + dayname, false);
						}
						break;
					} else {
						continue;
					}
				}
			}
		} else {
			SimpleUtils.fail("The formula check box of days of week have been checked on.", false);
		}
	}

	//below are advanced staffing rule element
	@FindBy(css = "div[class=\"mt-20\"] input-field[value*=\"timeEventOffsetMinutes\"] input")
	private WebElement shiftStartOffsetMinutes;
	@FindBy(css = "div[class=\"mt-20\"] input-field[value*=\"timeEventOffsetMinutes\"] div")
	private WebElement shiftStartOffsetMinutesValue;
	@FindBy(css = "div[class=\"mt-20\"] input-field[options*=\"timeUnitOptions\"] select")
	private WebElement shiftStartTimeUnit;
	@FindBy(css = "div[class=\"mt-20\"] input-field[options*=\"eventPointOptions\"] select")
	private WebElement shiftStartEventPoint;
	@FindBy(css = "div[class=\"mt-20\"] input-field[options*=\"eventPointOptions\"] select option")
	private List<WebElement> shiftStartEventPointList;
	@FindBy(css = "div[class=\"mt-20\"] input-field[options*=\"timeEventOptions\"] select")
	private WebElement shiftStartTimeEvent;
	@FindBy(css = "div[class=\"mt-20\"] input-field[options*=\"timeEventOptions\"] select option")
	private List<WebElement> shiftStartTimeEventList;


	@Override
	public void inputOffsetTimeForShiftStart(String startOffsetTime, String startEventPoint) throws Exception {
		if (isElementEnabled(shiftStartOffsetMinutes)) {
			clickTheElement(shiftStartOffsetMinutes);
			shiftStartOffsetMinutes.clear();
			shiftStartOffsetMinutes.sendKeys(startOffsetTime);
		} else {
			selectByVisibleText(shiftStartEventPoint, startEventPoint);
			waitForSeconds(3);
			clickTheElement(shiftStartOffsetMinutes);
			shiftStartOffsetMinutes.clear();
			shiftStartOffsetMinutes.sendKeys(startOffsetTime);
		}
		String offsetTimeValue = shiftStartOffsetMinutesValue.getAttribute("innerText").trim();
		if (offsetTimeValue.equals(startOffsetTime)) {
			SimpleUtils.pass("User can set shift Start Offset Minutes as " + startOffsetTime + " successfully!");
		} else {
			SimpleUtils.fail("User can't input value for shift Start Offset Minutes field", true);
		}
	}

	@FindBy(css = "div[class=\"mt-20\"] input-field[options*=\"timeUnitOptions\"] select option")
	private List<WebElement> shiftStartTimeUnitList;
	List<String> unitList = new ArrayList<String>() {{
		add("minutes");
		add("am");
		add("pm");
	}};

	//verify list of start shift time unit
	@Override
	public void validateShiftStartTimeUnitList() throws Exception {
		if (isElementEnabled(shiftStartTimeUnit)) {
			List<String> startTimeUnitList = new ArrayList<String>();
			clickTheElement(shiftStartTimeUnit);
			if (shiftStartTimeUnitList.size() != 0) {
				for (WebElement shiftStartTimeUnit : shiftStartTimeUnitList) {
					if (shiftStartTimeUnit != null) {
						String startTimeUnit = shiftStartTimeUnit.getText().trim();
						SimpleUtils.report("shift start time unit list: " + startTimeUnit);
						startTimeUnitList.add(startTimeUnit);
					}
				}
				if (ListUtils.isEqualList(unitList, startTimeUnitList)) {
					SimpleUtils.pass("The list of start time unit is correct");
				} else {
					SimpleUtils.fail("The list of start time unit is NOT correct", true);
				}
			}
		} else {
			SimpleUtils.fail("Shift start time unit isn't shown", false);
		}
	}

	@FindBy(css = "div[class=\"mt-20\"] input-field[options*=\"timeUnitOptions\"] div.input-faked")
	private WebElement startTimeUnitValue;

	@Override
	public void selectShiftStartTimeUnit(String startTimeUnit) throws Exception {
		if (isElementEnabled(shiftStartTimeUnit)) {
			selectByVisibleText(shiftStartTimeUnit, startTimeUnit);
			waitForSeconds(2);
			String startTimeUnitVal = startTimeUnitValue.getAttribute("innerText").trim();
			if (startTimeUnitVal.equals(startTimeUnit)) {
				SimpleUtils.pass("User successfully select start event: " + startTimeUnitVal);
			} else {
				SimpleUtils.fail("User failed to select start event: " + startTimeUnitVal, false);
			}
		}
	}

	//below are operating hour element
	@FindBy(css = "div.dayparts span.add-circle")
	private WebElement addDayPartsBTNInOH;
	@FindBy(css = "modal[modal-title=\"Manage Dayparts\"] div[class*=\"lg-modal__title-icon\"]")
	private WebElement manageDaypartsPageTitle;
	@FindBy(css = "div.lg-paged-search div.lg-paged-search__pagination select")
	private WebElement dayPartsPagination;
	@FindBy(css = "div.lg-paged-search div.lg-paged-search__pagination select option")
	private List<WebElement> dayPartsPaginationList;
	@FindBy(css = ".dayparts table tbody tr")
	private List<WebElement> dayPartsList;

	//verify list of Shift Start Time Event
	@Override
	public List<String> getShiftStartTimeEventList() throws Exception {
		List<String> startTimeEventList = new ArrayList<String>();
		if (isElementEnabled(shiftStartTimeEvent)) {
			clickTheElement(shiftStartTimeEvent);
			if (shiftStartTimeEventList.size() != 0) {
				for (WebElement shiftStartTimeEvent : shiftStartTimeEventList) {
					String startTimeEvent = shiftStartTimeEvent.getText().trim();
					if (startTimeEvent != null) {
						SimpleUtils.report("shift start time event list: " + startTimeEvent);
						startTimeEventList.add(startTimeEvent);
					}
				}
			}
		} else {
			SimpleUtils.fail("Shift start time event isn't shown", false);
		}
		return startTimeEventList;
	}

	@FindBy(css = "div[class=\"mt-20\"] input-field[options*=\"timeEventOptions\"] div.input-faked")
	private WebElement shiftStartTimeEventValue;

	@Override
	public void selectShiftStartTimeEvent(String startEvent) throws Exception {
		if (isElementEnabled(shiftStartTimeEvent)) {
			selectByVisibleText(shiftStartTimeEvent, startEvent);
			waitForSeconds(2);
			String shiftStartTimeEventVal = shiftStartTimeEventValue.getAttribute("innerText").trim();
			for (WebElement timeEvent : shiftStartTimeEventList) {
				String eventName = timeEvent.getText().trim();
				String eventValue = timeEvent.getAttribute("value").trim();
				if (startEvent.equals(eventName) && eventValue.contains(shiftStartTimeEventVal)) {
					SimpleUtils.pass("User can select start time event successfully.");
					break;
				}
			}
		}
	}

	@FindBy(css = "div.dif.duartion input-field[type=\"radio\"] ng-form")
	private WebElement shiftDuartionRadioButton;
	@FindBy(css = "div.dif.duartion input-field[type=\"number\"] input")
	private WebElement shiftDuartionMinutes;
	@FindBy(css = "div.dif.duartion input-field[type=\"number\"] div")
	private WebElement shiftDuartionMinutesValue;
	@FindBy(css = "div.dif.end-shift input-field[type=\"radio\"] ng-form")
	private WebElement shiftEndRadioButton;
	@FindBy(css = "div.dif.end-shift input-field[value*=\"timeEventOffsetMinutes\"] input")
	private WebElement shiftEndOffsetMinutes;
	@FindBy(css = "div.dif.end-shift input-field[value*=\"timeEventOffsetMinutes\"] div")
	private WebElement shiftEndOffsetMinutesValue;
	@FindBy(css = "div.dif.end-shift input-field[options=\"$ctrl.timeUnitOptions\"] select")
	private WebElement shiftEndTimeUnit;
	@FindBy(css = "div.dif.end-shift input-field[options=\"$ctrl.timeUnitOptions\"] select option")
	private List<WebElement> shiftEndTimeUnitList;
	@FindBy(css = "div.dif.end-shift input-field[options=\"$ctrl.eventPointOptions\"] select")
	private WebElement shiftEndEventPoint;
	@FindBy(css = "div.dif.end-shift input-field[options=\"$ctrl.eventPointOptions\"] select option")
	private List<WebElement> shiftEndEventPointList;
	@FindBy(css = "div.dif.end-shift input-field[options=\"$ctrl.timeEventOptions\"] select")
	private WebElement shiftEndTimeEvent;
	@FindBy(css = "div.dif.end-shift input-field[options=\"$ctrl.timeEventOptions\"] select option")
	private List<WebElement> shiftEndTimeEventList;
	@FindBy(css = "div.dif.duartion input-field[type=\"number\"]+span")
	private WebElement shiftDuartionMinutesUnit;
	@FindBy(css = "div.dif.end-shift input-field[options=\"$ctrl.timeEventOptions\"] div.input-faked")
	private WebElement shiftEndTimeEventValue;
	@FindBy(css = "button.saveas-drop")
	private WebElement templateSaveDrop;
	@FindBy(css = "div.saveas-list h3")
	private List<WebElement> templateSaveOptions;

	@Override
	public void selectShiftEndTimeEvent(String endEvent) throws Exception {
		if (isElementEnabled(shiftEndTimeEvent)) {
			selectByVisibleText(shiftEndTimeEvent, endEvent);
			waitForSeconds(2);
			String shiftEndTimeEventVal = shiftEndTimeEventValue.getAttribute("innerText").trim();
			for (WebElement timeEvent : shiftEndTimeEventList) {
				String eventName = timeEvent.getText().trim();
				String eventValue = timeEvent.getAttribute("value").trim();
				if (endEvent.equals(eventName) && eventValue.contains(shiftEndTimeEventVal)) {
					SimpleUtils.pass("User can select end time event successfully.");
					break;
				}
			}
		}
	}

	@FindBy(css = "div[class=\"mt-20 dif\"] input-field[options*=\"timeUnitOptions\"] div.input-faked")
	private WebElement endTimeUnitValue;

	@Override
	public void selectShiftEndTimeUnit(String endTimeUnit) throws Exception {
		if (isElementEnabled(shiftEndTimeUnit)) {
			selectByVisibleText(shiftEndTimeUnit, endTimeUnit);
			waitForSeconds(2);
			String endTimeUnitVal = endTimeUnitValue.getAttribute("innerText").trim();
			if (endTimeUnitVal.equals(endTimeUnit)) {
				SimpleUtils.pass("User successfully select start event: " + endTimeUnitVal);
			} else {
				SimpleUtils.fail("User failed to select start event: " + endTimeUnitVal, false);
			}
		}
	}

	@Override
	public void verifyRadioButtonInTimeOfDayIsSingletonSelect() throws Exception {
		if (isElementEnabled(shiftDuartionRadioButton) && isElementEnabled(shiftEndRadioButton)) {
			if (shiftDuartionRadioButton.getAttribute("class").trim().contains("ng-valid-parse")) {
				SimpleUtils.pass("shift Duartion Radio Button is selected now");
				shiftEndRadioButton.click();
				waitForSeconds(3);
				if (shiftEndRadioButton.getAttribute("class").trim().contains("ng-valid-parse")) {
					if (shiftDuartionRadioButton.getAttribute("class").trim().contains("ng-valid-parse")) {
						SimpleUtils.pass("Both Duartion Radio Button and End Radio Button are selected");
					} else {
						SimpleUtils.pass("End Radio Button is selected, Duartion Radio Button is dis-selected automatically");
					}
				} else {
					SimpleUtils.fail("User click shift End Radio Button failed", false);
				}
			} else {
				SimpleUtils.pass("shift Duartion Radio Button is NOT selected now");
				shiftDuartionRadioButton.click();
				waitForSeconds(3);
				if (shiftDuartionRadioButton.getAttribute("class").trim().contains("ng-valid-parse")) {
					if (shiftEndRadioButton.getAttribute("class").trim().contains("ng-valid-parse")) {
						SimpleUtils.pass("Both Duartion Radio Button and End Radio Button are selected");
					} else {
						SimpleUtils.pass("Duartion Radio Button is selected, End Radio Button is dis-selected automatically");
					}
				} else {
					SimpleUtils.fail("User click shift Duartion Radio Button failed", false);
				}
			}
		}
	}

	@Override
	public void inputShiftDurationMinutes(String duringTime) throws Exception {
		waitForSeconds(5);
		clickTheElement(shiftDuartionMinutes);
		shiftDuartionMinutes.clear();
		shiftDuartionMinutes.sendKeys(duringTime);
		String duringTimeValue = shiftDuartionMinutesValue.getAttribute("innerText").trim();
		if (duringTimeValue.equals(duringTime)) {
			SimpleUtils.pass("User can set shift during Minutes as " + duringTimeValue + " successfully!");
		} else {
			SimpleUtils.fail("User can't input value for shift during Minutes field", true);
		}
	}

	@Override
	public void validateShiftDurationTimeUnit() throws Exception {
		String unit = "minutes";
		if (shiftDuartionMinutesUnit.getText().trim() != null && shiftDuartionMinutesUnit.getText().equals(unit)) {
			SimpleUtils.pass("The shift Duration Minutes Unit is: " + shiftDuartionMinutesUnit.getText().trim());
		} else {
			SimpleUtils.fail("The The shift Duration Minutes Unit is not correct.", false);
		}

	}

	@Override
	public void inputOffsetTimeForShiftEnd(String endOffsetTime, String endEventPoint) throws Exception {
		if (isElementEnabled(shiftEndOffsetMinutes)) {
			clickTheElement(shiftEndOffsetMinutes);
			shiftEndOffsetMinutes.clear();
			shiftEndOffsetMinutes.sendKeys(endOffsetTime);
		} else {
			selectByVisibleText(shiftEndEventPoint, endEventPoint);
			waitForSeconds(3);
			clickTheElement(shiftEndOffsetMinutes);
			shiftEndOffsetMinutes.clear();
			shiftEndOffsetMinutes.sendKeys(endOffsetTime);
		}
		String offsetTimeValue = shiftEndOffsetMinutesValue.getAttribute("innerText").trim();
		if (offsetTimeValue.equals(endOffsetTime)) {
			SimpleUtils.pass("User can set shift End Offset Minutes as " + offsetTimeValue + " successfully!");
		} else {
			SimpleUtils.fail("User can't input value for shift End Offset Minutes field", true);
		}
	}

	//verify list of end shift time unit
	@Override
	public void validateShiftEndTimeUnitList() throws Exception {
		if (isElementEnabled(shiftEndTimeUnit)) {
			List<String> endTimeUnitList = new ArrayList<String>();
			clickTheElement(shiftEndTimeUnit);
			if (shiftEndTimeUnitList.size() != 0) {
				for (WebElement shiftEndTimeUnit : shiftEndTimeUnitList) {
					if (shiftEndTimeUnit != null) {
						String endTimeUnit = shiftEndTimeUnit.getText().trim();
						SimpleUtils.report("shift end time unit list: " + endTimeUnit);
						endTimeUnitList.add(endTimeUnit);
					}
				}
				if (ListUtils.isEqualList(unitList, endTimeUnitList)) {
					SimpleUtils.pass("The list of end time unit is correct");
				} else {
					SimpleUtils.fail("The list of end time unit is NOT correct", true);
				}
			}
		} else {
			SimpleUtils.fail("Shift end time unit isn't shown", false);
		}
	}

	@FindBy(css = "sub-content-box[box-title=\"Time of Day\"] input-field[label=\"Custom Formula?\"] input")
	private WebElement checkBoxOfTimeOfDay;
	@FindBy(css = "div[ng-if=\"$ctrl.isTimeOfTheDayFormula\"] textarea")
	private WebElement formulaTextAreaOfTimeOfDay;

	public boolean isTimeOfDayFormulaCheckBoxChecked() {
		boolean flag = false;
		if (isElementEnabled(checkBoxOfTimeOfDay)) {
			String classValueOfCheckBox = checkBoxOfTimeOfDay.getAttribute("class");
			if (classValueOfCheckBox.contains("ng-not-empty")) {
				flag = true;
			} else {
				flag = false;
			}
		}
		return flag;
	}

	@Override
	public void tickOnCheckBoxOfTimeOfDay() throws Exception {
		if (!isTimeOfDayFormulaCheckBoxChecked()) {
			clickTheElement(checkBoxOfTimeOfDay);
			waitForSeconds(3);
		}
		if (isElementEnabled(formulaTextAreaOfTimeOfDay)) {
			SimpleUtils.pass("User checked on check box of time of day successfully!");
		} else {
			SimpleUtils.fail("User checked on check box of time of day successfully!", false);
		}
	}

	@Override
	public void inputFormulaInTextAreaOfTimeOfDay(String formulaOfTimeOfDay) {
		String placeHolder = "Enter your custom formula here for the time of the day. The formula must evaluate to be an integer minutes.";
		//verify formula text area show well or not?
		if (formulaTextAreaOfTimeOfDay.getAttribute("placeholder").trim().equals(placeHolder)) {
			SimpleUtils.pass("formula Text Area Of Time Of Day shows well.");
		} else {
			SimpleUtils.fail("formula Text Area Of Time Of Day shows well.", false);
		}
		//input formula in text area
		formulaTextAreaOfTimeOfDay.sendKeys(formulaOfTimeOfDay);
		String formulaValue = getDriver().findElement(By.cssSelector("sub-content-box[box-title=\"Time of Day\"] input-field[type=\"textarea\"] ng-form div")).getAttribute("innerText").trim();
		if (formulaValue.equals(formulaOfTimeOfDay)) {
			SimpleUtils.pass("User can input formula for time of day successfully!");
		} else {
			SimpleUtils.fail("User can NOT input formula for time of day!", false);
		}
	}

	@FindBy(css = "sub-content-box.breaks")
	private WebElement mealAndRestBreakSection;
	@FindBy(css = "span[ng-click=\"$ctrl.addBreak('M')\"] span.ml-5.ng-binding")
	private WebElement addMealBreakButton;
	@FindBy(css = "sub-content-box.breaks div.col-sm-5:nth-child(1) table tr[ng-repeat]")
	private List<WebElement> mealBreakList;
	@FindBy(css = "sub-content-box.breaks div.col-sm-1.plr-0-0+div table tr[ng-repeat]")
	private List<WebElement> restBreakList;
	@FindBy(css = "span[ng-click=\"$ctrl.addBreak('R')\"] span.ml-5.ng-binding")
	private WebElement addRestBreakButton;

	@Override
	public void addNewMealBreak(List<String> mealBreakValue) throws Exception {
		if (isElementEnabled(mealAndRestBreaksSection)) {
			clickTheElement(addMealBreakButton);
			waitForSeconds(2);
			if (mealBreakList.size() != 0) {
				SimpleUtils.pass("User click add button of Meal Break successfully");
			} else {
				SimpleUtils.fail("User click add button of Meal Break failed", false);
			}
			int index = mealBreakList.size() - 1;
			List<WebElement> startOffsetAndBreakDuration = mealBreakList.get(index).findElements(By.cssSelector("input-field"));
			for (int i = 0; i <= 1; i++) {
				WebElement startOffsetAndBreakDurationInput = startOffsetAndBreakDuration.get(i).findElement(By.cssSelector("input"));
				startOffsetAndBreakDurationInput.click();
				startOffsetAndBreakDurationInput.clear();
				startOffsetAndBreakDurationInput.sendKeys(mealBreakValue.get(i));
				String startOffsetValue = startOffsetAndBreakDuration.get(i).findElement(By.cssSelector("div")).getAttribute("innerText").trim();
				if (startOffsetValue.equals(mealBreakValue.get(i))) {
					SimpleUtils.pass("User can add Meal Break successfully!");
				} else {
					SimpleUtils.fail("User can NOT add Meal Break successfully!", false);
				}
				waitForSeconds(2);
			}
		}
	}

	@Override
	public void addMultipleMealBreaks(List<String> mealBreakValue) throws Exception {
		int count = mealBreakList.size();
		for (int i = 0; i <= 9; i++) {
			scrollToBottom();
			addNewMealBreak(mealBreakValue);
		}
		count = count + 10;
		if (mealBreakList.size() == count) {
			SimpleUtils.pass("User can add multiple Meal Breaks successfully!");
			if (isElementEnabled(addMealBreakButton) && isElementEnabled(addRestBreakButton)) {
				verifyAdvancedStaffingRulePageShowWell();
				SimpleUtils.pass("The page shows well after adding multiple Meal Breaks");
			} else {
				SimpleUtils.fail("The page shows well after adding multiple Meal Breaks", false);
			}
		} else {
			SimpleUtils.fail("User can NOT add multiple Meal Breaks successfully!", false);
		}
	}

	@Override
	public void deleteMealBreak() throws Exception {
		int index = mealBreakList.size();
		if (index != 0) {
			WebElement removeButton = mealBreakList.get(index - 1).findElement(By.cssSelector("i"));
			if (isElementEnabled(removeButton)) {
				clickTheElement(removeButton);
				waitForSeconds(1);
				if (mealBreakList.size() == index - 1) {
					SimpleUtils.pass("User can remove meal break successfully!");
				} else {
					SimpleUtils.fail("User can NOT remove meal break successfully!", false);
				}
			} else {
				SimpleUtils.fail("remove meal breaks button is not available.", false);
			}
		} else {
			SimpleUtils.fail("Still have no meal break info!", false);
		}
	}

	@Override
	public void addNewRestBreak(List<String> restBreakValue) throws Exception {
		if (isElementEnabled(mealAndRestBreaksSection)) {
			clickTheElement(addRestBreakButton);
			waitForSeconds(2);
			if (restBreakList.size() != 0) {
				SimpleUtils.pass("User click add button of Rest Break successfully");
			} else {
				SimpleUtils.fail("User click add button of Rest Break failed", false);
			}
			int index = restBreakList.size() - 1;
			List<WebElement> startOffsetAndBreakDuration = restBreakList.get(index).findElements(By.cssSelector("input-field"));
			for (int i = 0; i <= 1; i++) {
				WebElement startOffsetAndBreakDurationInput = startOffsetAndBreakDuration.get(i).findElement(By.cssSelector("input"));
				startOffsetAndBreakDurationInput.click();
				startOffsetAndBreakDurationInput.clear();
				startOffsetAndBreakDurationInput.sendKeys(restBreakValue.get(i));
				String startOffsetValue = startOffsetAndBreakDuration.get(i).findElement(By.cssSelector("div")).getAttribute("innerText").trim();
				if (startOffsetValue.equals(restBreakValue.get(i))) {
					SimpleUtils.pass("User can add Rest Break successfully!");
				} else {
					SimpleUtils.fail("User can NOT add Rest Break successfully!", false);
				}
				waitForSeconds(2);
			}
		}
	}

	@Override
	public void addMultipleRestBreaks(List<String> restBreakValue) throws Exception {
		int count = restBreakList.size();
		for (int i = 0; i <= 9; i++) {
			scrollToBottom();
			addNewRestBreak(restBreakValue);
		}
		count = count + 10;
		if (restBreakList.size() == count) {
			SimpleUtils.pass("User can add multiple Rest Breaks successfully!");
			if (isElementEnabled(addMealBreakButton) && isElementEnabled(addRestBreakButton)) {
				verifyAdvancedStaffingRulePageShowWell();
				SimpleUtils.pass("The page shows well after adding multiple Rest Breaks");
			} else {
				SimpleUtils.fail("The page shows well after adding multiple Rest Breaks", false);
			}
		} else {
			SimpleUtils.fail("User can NOT add multiple Rest Breaks successfully!", false);
		}
	}

	@Override
	public void deleteRestBreak() throws Exception {
		int index = restBreakList.size();
		if (index != 0) {
			WebElement removeButton = restBreakList.get(index - 1).findElement(By.cssSelector("i"));
			if (isElementEnabled(removeButton)) {
				clickTheElement(removeButton);
				waitForSeconds(1);
				if (restBreakList.size() == index - 1) {
					SimpleUtils.pass("User can remove rest break successfully!");
				} else {
					SimpleUtils.fail("User can NOT remove rest break successfully!", false);
				}
			} else {
				SimpleUtils.fail("remove rest breaks button is not available.", false);
			}
		} else {
			SimpleUtils.fail("Still have no rest break info!", false);
		}
	}

	@FindBy(css = "sub-content-box.num-shifts input-field[type=\"number\"] input")
	private WebElement shiftsNumberInputField;
	@FindBy(css = "sub-content-box.num-shifts input-field[type=\"number\"] div")
	private WebElement valueOfShiftsNumber;
	@FindBy(css = "sub-content-box.num-shifts input-field[type=\"checkbox\"] input")
	private WebElement checkBoxOfNumberOfShifts;
	@FindBy(css = "sub-content-box.num-shifts input-field[type=\"checkbox\"] input")
	private WebElement checkBoxStatusOfNumberOfShifts;
	@FindBy(css = "sub-content-box.num-shifts input-field[type=\"textarea\"] textarea")
	private WebElement formulaTextAreaOfNumberOfShifts;
	@FindBy(css = "sub-content-box.num-shifts input-field[type=\"textarea\"] div")
	private WebElement formulaOfNumberOfShifts;

	@Override
	public void inputNumberOfShiftsField(String shiftsNumber) throws Exception {
		if (isElementEnabled(shiftsNumberInputField)) {
			shiftsNumberInputField.click();
			shiftsNumberInputField.clear();
			shiftsNumberInputField.sendKeys(shiftsNumber);
			waitForSeconds(2);
			String shiftsNumberValue = valueOfShiftsNumber.getAttribute("innerText").trim();
			if (shiftsNumberValue.equals(shiftsNumber)) {
				SimpleUtils.pass("User can input value in shifts number field successfully!");
			} else {
				SimpleUtils.fail("User can NOT input value in shifts number field successfully!", false);
			}
		}
	}

	@Override
	public void validCheckBoxOfNumberOfShiftsIsClickable() throws Exception {
		if (isElementEnabled(checkBoxOfNumberOfShifts)) {
			clickTheElement(checkBoxOfNumberOfShifts);
			if (isElementEnabled(formulaTextAreaOfNumberOfShifts)) {
				SimpleUtils.pass("User can click the check box of Number Of Shifts successfully!");
			} else {
				SimpleUtils.fail("User failed to click the check box of Number Of Shifts!", false);
			}
		} else {
			SimpleUtils.fail("The check box of Number Of Shifts is not shown.", false);
		}
	}

	@Override
	public void inputFormulaInFormulaTextAreaOfNumberOfShifts(String shiftNumberFormula) throws Exception {
		if (isElementEnabled(formulaTextAreaOfNumberOfShifts)) {
			clickTheElement(formulaTextAreaOfNumberOfShifts);
			formulaTextAreaOfNumberOfShifts.sendKeys(shiftNumberFormula);
			if (formulaOfNumberOfShifts.getAttribute("innerText").trim().equals(shiftNumberFormula)) {
				SimpleUtils.pass("User can input formula for number of shifts successfully!");
			} else {
				SimpleUtils.fail("User can NOT input formula for number of shifts successfully!", false);
			}
		} else {
			SimpleUtils.fail("Formula text area of Number Of Shifts is not showing yet.", false);
		}
	}

	@FindBy(css = "div.badges-edit-wrapper div.lg-button-group div")
	private List<WebElement> badgeOptions;
	@FindBy(css = "div.badges-edit-wrapper tbody tr")
	private List<WebElement> badgesList;

	@Override
	public void selectBadgesForAdvanceStaffingRules() throws Exception {
		if (isElementEnabled(badgesSection)) {
			for (WebElement badgeOption : badgeOptions) {
				if (isElementEnabled(badgeOption)) {
					clickTheElement(badgeOption);
					waitForSeconds(2);
					String classValue = badgeOption.getAttribute("class").trim();
					if (classValue.contains("lg-button-group-selected")) {
						SimpleUtils.pass("User can click badge option successfully!");
					} else {
						SimpleUtils.fail("User failed to click badge option successfully!", false);
					}
				} else {
					SimpleUtils.fail(badgeOption.findElement(By.cssSelector("span")).getText().trim() + " is not shown for user!", false);
				}
			}
			if (badgesList.size() != 0) {
				WebElement checkBoxInputField = badgesList.get(0).findElement(By.cssSelector("input"));
				checkBoxInputField.click();
				String classValue = checkBoxInputField.getAttribute("class").trim();
				if (classValue.contains("ng-not-empty")) {
					String badgeName = badgesList.get(0).findElement(By.cssSelector("td")).getText().trim();
					SimpleUtils.pass("User can select " + badgeName + " Successfully!");
				} else {
					SimpleUtils.fail("User failed to select badge.", false);
				}
			} else {
				SimpleUtils.fail("There is no bage info in system so far!", false);
			}
		}
	}

	@FindBy(css = "div.settings-work-rule-delete-icon")
	private WebElement crossButton;
	@FindBy(css = "div.settings-work-rule-save-icon")
	private WebElement checkMarkButton;
	@FindBy(css = "div.settings-work-role-detail-edit-rules div.settings-work-rule-container")
	private List<WebElement> staffingRulesList;

	@Override
	public void verifyCrossButtonOnAdvanceStaffingRulePage() throws Exception {
		if (isElementEnabled(crossButton)) {
			clickTheElement(crossButton);
			String classValue = staffingRulesList.get(staffingRulesList.size() - 1).getAttribute("class").trim();
			if (classValue.contains("deleted")) {
				SimpleUtils.pass("User successfully to click the cross button.");
			} else {
				SimpleUtils.fail("User failed to click the cross button.", false);
			}
		} else {
			SimpleUtils.fail("The cross button is not shown on page now", false);
		}
	}

	@Override
	public void verifyCheckMarkButtonOnAdvanceStaffingRulePage() throws Exception {
		if (isElementEnabled(checkMarkButton)) {
			clickTheElement(checkMarkButton);
			String classValue = staffingRulesList.get(staffingRulesList.size() - 1).getAttribute("class").trim();
			if (classValue.contains("settings-work-rule-container-border")) {
				SimpleUtils.pass("User successfully to click the checkmark button.");
			} else {
				SimpleUtils.fail("User failed to click the checkmark button.", false);
			}
		} else {
			SimpleUtils.fail("The checkmark button is not shown on page now", false);
		}
	}

	@FindBy(css = "lg-button[label=\"Save\"] button")
	WebElement saveButtonOnAdvanceStaffingRulePage;
	@FindBy(css = "div.banner-container")
	WebElement templateDescription;

	public void clickOnSaveButtonOfAdvanceStaffingRule() {
		if (isElementEnabled(saveButtonOnAdvanceStaffingRulePage)) {
			clickTheElement(saveButtonOnAdvanceStaffingRulePage);
			waitForSeconds(2);
			if (isElementEnabled(templateDescription)) {
				SimpleUtils.pass("User can click save button successfully!");
			} else {
				SimpleUtils.fail("User can NOT click save button successfully!", false);
			}
		} else {
			SimpleUtils.fail("Save button is not shown well now.", false);
		}
	}

	@Override
	public void saveOneAdvanceStaffingRule(String workRole, List<String> days) throws Exception {
		//get the staffing rules count before add one new rule
		int countBeforeSaving = Integer.valueOf(getCountOfStaffingRules(workRole));
		selectWorkRoleToEdit(workRole);
		checkTheEntryOfAddAdvancedStaffingRule();
		verifyAdvancedStaffingRulePageShowWell();
		selectDaysForDaysOfWeekSection(days);
		clickOnSaveButtonOfAdvanceStaffingRule();
		//get the staffing rules count after add one new rule
		int countAfterSaving = Integer.valueOf(getCountOfStaffingRules(workRole));

		if ((countAfterSaving - countBeforeSaving) == 1) {
			SimpleUtils.pass("User have successfully save one new staffing rule.");
		} else {
			SimpleUtils.fail("User failed to save one new staffing rule.", false);
		}
	}

	@FindBy(css = "lg-button[label=\"Cancel\"] button")
	WebElement cancelButtonOnAdvanceStaffingRulePage;
	@FindBy(css = "lg-button[label=\"Leave this page\"] button")
	WebElement leaveThisPageButton;

	public void clickOnCancelButtonOfAdvanceStaffingRule() {
		if (isElementEnabled(cancelButtonOnAdvanceStaffingRulePage)) {
			clickTheElement(cancelButtonOnAdvanceStaffingRulePage);
			waitForSeconds(2);
			if (isElementEnabled(leaveThisPageButton)) {
				clickTheElement(leaveThisPageButton);
				waitForSeconds(2);
				if (isElementEnabled(templateDescription)) {
					SimpleUtils.pass("User can click cancel button successfully!");
				}
			} else {
				SimpleUtils.fail("User can NOT click cancel button successfully!", false);
			}
		} else {
			SimpleUtils.fail("Cancel button is not shown well now.", false);
		}
	}

	@Override
	public void cancelSaveOneAdvanceStaffingRule(String workRole, List<String> days) throws Exception {
		//get the staffing rules count before add one new rule
		int countBeforeSaving = Integer.valueOf(getCountOfStaffingRules(workRole));
		selectWorkRoleToEdit(workRole);
		checkTheEntryOfAddAdvancedStaffingRule();
		verifyAdvancedStaffingRulePageShowWell();
		selectDaysForDaysOfWeekSection(days);
		clickOnCancelButtonOfAdvanceStaffingRule();
		//get the staffing rules count after add one new rule
		int countAfterSaving = Integer.valueOf(getCountOfStaffingRules(workRole));

		if ((countAfterSaving - countBeforeSaving) == 0) {
			SimpleUtils.pass("User have successfully cancel save one new staffing rule.");
		} else {
			SimpleUtils.fail("User failed to cancel save one new staffing rule.", false);
		}
	}

	@Override
	public void addMultipleAdvanceStaffingRule(String workRole, List<String> days) throws Exception {
		//get the staffing rules count before add one new rule
		int countBeforeSaving = Integer.valueOf(getCountOfStaffingRules(workRole));
		selectWorkRoleToEdit(workRole);
		checkTheEntryOfAddAdvancedStaffingRule();
		verifyAdvancedStaffingRulePageShowWell();
		selectDaysForDaysOfWeekSection(days);
		verifyCheckMarkButtonOnAdvanceStaffingRulePage();
		checkTheEntryOfAddAdvancedStaffingRule();
		verifyAdvancedStaffingRulePageShowWell();
		selectDaysForDaysOfWeekSection(days);
		verifyCheckMarkButtonOnAdvanceStaffingRulePage();
		int countAfterSaving = staffingRulesList.size();
		if (countAfterSaving - countBeforeSaving == 2) {
			SimpleUtils.pass("User can add multiple advance staffing rule successfully!");
		} else {
			SimpleUtils.fail("User can NOT add multiple advance staffing rule successfully!", false);
		}
	}

	@FindBy(css = "div.settings-work-role-detail-edit-rules div[ng-if=\"ifAdvancedStaffingRule()\"]")
	private List<WebElement> advancedStaffingRuleList;

	@Override
	public void editAdvanceStaffingRule(String shiftsNumber) throws Exception {
		if (advancedStaffingRuleList.size() != 0) {
			WebElement editButton = advancedStaffingRuleList.get(0).findElement(By.cssSelector("i.fa-pencil"));
			clickTheElement(editButton);
			waitForSeconds(2);
			inputNumberOfShiftsField(shiftsNumber);
			if (isElementEnabled(checkMarkButton)) {
				clickTheElement(checkMarkButton);
				waitForSeconds(2);
				String shiftNumberValueInRule = advancedStaffingRuleList.get(0).findElement(By.cssSelector("div.rule-label span:nth-child(2)")).getText().trim();
				if (shiftNumberValueInRule.equals(shiftsNumber)) {
					SimpleUtils.pass("User can edit advance staffing rule successfully!");
				} else {
					SimpleUtils.fail("User can't edit advance staffing rule successfully!", false);
				}
			}
		} else {
			SimpleUtils.fail("There is no advanced staffing rule.", false);
		}
	}

	@FindBy(css = "div.modal-dialog button[ng-click=\"confirmDeleteAction()\"]")
	WebElement deleteButtonOnDialogPage;

	@Override
	public void deleteAdvanceStaffingRule() throws Exception {
		int countOfAdvancedStaffingRule = advancedStaffingRuleList.size();
		if (countOfAdvancedStaffingRule != 0) {
			WebElement deleteButton = advancedStaffingRuleList.get(0).findElement(By.cssSelector("span.settings-work-rule-edit-delete-icon"));
			clickTheElement(deleteButton);
			if (isElementEnabled(deleteButtonOnDialogPage, 2)) {
				clickTheElement(deleteButtonOnDialogPage);
			}
			waitForSeconds(2);
			if (advancedStaffingRuleList.get(0).findElements(By.cssSelector("div[ng-if=\"$ctrl.isViewMode()\"]>div")).size() == 1) {
				SimpleUtils.pass("User can delete advance staffing rule successfully!");
			} else {
				SimpleUtils.fail("User can't delete advance staffing rule successfully!", false);
			}
		} else {
			SimpleUtils.fail("There is no advanced staffing rule.", false);
		}
	}

	@FindBy(css = "div.settings-work-role-detail-edit-rules div[ng-if=\"ifStaffingRule()\"]")
	private List<WebElement> basicStaffingRuleList;

	@Override
	public void deleteBasicStaffingRule() throws Exception {
		int countOfAdvancedStaffingRule = basicStaffingRuleList.size();
		if (countOfAdvancedStaffingRule != 0) {
			WebElement deleteButton = basicStaffingRuleList.get(0).findElement(By.cssSelector("span.settings-work-rule-edit-delete-icon"));
			clickTheElement(deleteButton);
			if (isElementEnabled(deleteButtonOnDialogPage, 2)) {
				clickTheElement(deleteButtonOnDialogPage);
			}
			waitForSeconds(2);
			if (basicStaffingRuleList.get(0).findElements(By.cssSelector("div[ng-if=\"$ctrl.isViewMode()\"]>div")).size() == 1) {
				SimpleUtils.pass("User can delete basic staffing rule successfully!");
			} else {
				SimpleUtils.fail("User can't delete advance staffing rule successfully!", false);
			}
		} else {
			SimpleUtils.fail("There is no advanced staffing rule.", false);
		}
	}

	@Override
	public void saveBtnIsClickable() throws Exception {
		scrollToBottom();
		if (isElementLoaded(saveButton, 5)) {
			click(saveButton);
		} else
			SimpleUtils.fail("Save button load failed", false);
	}

	@FindBy(css = "div.settings-work-rule-container")
	private WebElement scheduleRulesList;

	@Override
	public void deleteAllScheduleRules() throws Exception {
		if (staffingRulesList.size() != 0) {
			for (WebElement staffingRule : staffingRulesList) {
				WebElement deleteButton = staffingRule.findElement(By.cssSelector("span.settings-work-rule-edit-delete-icon"));
				if (isElementEnabled(deleteButton, 2)) {
					clickTheElement(deleteButton);
					waitForSeconds(2);
					clickTheElement(deleteButtonOnDialogPage);
					if (staffingRule.findElements(By.cssSelector("div[ng-if=\"$ctrl.isViewMode()\"]>div")).size() == 1) {
						SimpleUtils.pass("User can delete staffing rules successfully!");
					} else {
						SimpleUtils.fail("User failed to delete staffing rules.", false);
					}
				}
			}
		} else {
			SimpleUtils.report("There is not staffing rule so far.");
		}
	}

	@Override
	public void clickOnSaveButtonOnScheduleRulesListPage() throws Exception {
		if (isElementEnabled(saveButton, 5)) {
			clickTheElement(saveButton);
			waitForSeconds(30);
			if (isElementEnabled(dropdownArrowBTN, 5) || isElementEnabled(dropdownArrowBTN, 5)) {
				SimpleUtils.pass("User click on save button on schedule rule list page successfully!");
			} else
				SimpleUtils.fail("User failed to click on save button on schedule rule list page!", false);
		} else {
			SimpleUtils.fail("No save button displayed on page", false);
		}
	}

	//added by Estelle to verify ClockIn
	@FindBy(css = "[value*=\"ClockInGroup\"] select")
	private WebElement clockInSelector;
	@FindBy(css = "form-section[form-title=\"Clock in Group\"")
	private WebElement clockInForm;

	@Override
	public void verifyClockInDisplayAndSelect(List<String> clockInGroup) throws Exception {
		scrollToBottom();
		if (isElementLoaded(clockInForm, 5) && clockInForm.getText().contains("Locations that employees can clock-in\n") && isElementLoaded(clockInSelector, 5)) {
			SimpleUtils.pass("Clock in form show well");
			click(clockInSelector);
			List<WebElement> clockInOptionList = clockInSelector.findElements(By.cssSelector("option"));
			List<String> clockInOptionListText = new ArrayList<>();
			for (WebElement option : clockInOptionList) {
				if (!option.getText().equalsIgnoreCase("")) {
					clockInOptionListText.add(option.getText());
				}
			}
			if (clockInOptionListText.size() != clockInGroup.size()) {
				SimpleUtils.fail("Clock-in list size in TA is not as same as Clock-in list size in Global dynamic group", false);

			} else
				for (Object object : clockInOptionListText) {
					if (!clockInGroup.contains(object)) {
						SimpleUtils.fail("Clock-in list in TA is not as same as Clock-in list in Global dynamic group", false);
						break;
					} else
						SimpleUtils.pass("Clock-in list size in TA is as same as Clock-in list size in Global dynamic group");
				}
			for (int i = 0; i < Integer.valueOf(clockInOptionListText.size()); i++) {
				selectByVisibleText(clockInSelector, clockInOptionListText.get(i));
			}
		} else
			SimpleUtils.fail("Clock-in form load failed", false);
	}

	@FindBy(css = "[question-title=\"Enable the Work Force Sharing Group?\"] .lg-button-group div")
	private List<WebElement> yesNoForWFS;
	@FindBy(css = "[question-title=\"Do you want to send Shift Offers to other locations?\"] .lg-button-group div")
	private List<WebElement> yesNoForWFSControls;

	@Override
	public void setWFS(String wfsMode) {
		List<WebElement> yesNo = null;
		if (areListElementVisible(yesNoForWFS, 5)) {
			yesNo = yesNoForWFS;
		} else if (areListElementVisible(yesNoForWFSControls, 15)) {
			yesNo = yesNoForWFSControls;
		}
		for (WebElement yesNoOption : yesNo) {
			if (yesNoOption.getText().equalsIgnoreCase(wfsMode)) {
				click(yesNoOption);
				if (wfsMode.equalsIgnoreCase("yes")) {
					setWFSStatus(true);
				} else
					setWFSStatus(false);
					break;
				}
			}
		SimpleUtils.report("Do you want to send Shift Offers to other locations?  to " + wfsMode);
	}

	@Override
	public boolean isWFSEnabled() {
		boolean isWFSEnabled = false;
		if (areListElementVisible(yesNoForWFS, 5)) {
			if (yesNoForWFS.get(0).getAttribute("class").contains("selected")) {
				isWFSEnabled = true;
			}
		} else
			SimpleUtils.fail("Workforce sharing group settings fail to load! ", false);
		setWFSStatus(true);
		return isWFSEnabled;
	}

	@FindBy(css = "input-field[options=\"$ctrl.groupOptions\"]>ng-form>div:nth-child(3)>select")
	private WebElement wfsSelector;

	@Override
	public void selectWFSGroup(String wfsName) throws Exception {
		if (isElementLoaded(wfsSelector, 5)) {
			selectByVisibleText(wfsSelector, wfsName);
		} else
			SimpleUtils.fail("Workforce sharing group selector load failed", false);

	}

	@FindBy(css = "[ng-if=\"$ctrl.saveAsLabel\"] button.pre-saveas")
	private WebElement publishTemplateButton;

	@FindBy(css = "div.modal-dialog")
	private WebElement publishTemplateConfirmModal;

	@FindBy(css = "[ng-click=\"$ctrl.submit(true)\"]")
	private WebElement okButtonOnPublishTemplateConfirmModal;

	@FindBy(css = "div.lg-toast")
	private WebElement successMsg;

	public void displaySuccessMessage() throws Exception {
		if (isElementLoaded(successMsg, 20)) {
			if (successMsg.getText().contains("Success")) {
				SimpleUtils.pass("Success message displayed successfully." + successMsg.getText());
				waitForSeconds(2);
			} else
				SimpleUtils.report("The message should include 'Success', but the actual is: " + successMsg.getText());
		} else {
			SimpleUtils.report("Success pop up not displayed successfully.");
			waitForSeconds(3);
		}
	}

	@Override
	public void commitTypeCheck() throws Exception {
		String[] supportedType = {"Save as draft", "Publish now", "Publish later"};
		//scroll to the bottom of page
		scrollToBottom();
		clickTheElement(templateSaveDrop);
		if (areListElementVisible(templateSaveOptions)) {
			SimpleUtils.pass("Save and publish options loaded successfully");
			waitForSeconds(2);
			for (String sp : supportedType) {
				for (WebElement optionele : templateSaveOptions) {
					if (optionele.getText().trim().equals(sp)) {
						SimpleUtils.pass("Option" + sp + "showed in save and publish options list");
						continue;
					}
				}
			}
		} else
			SimpleUtils.fail("No save and publish options loaded!", false);
		scrollToTop();
	}

	@FindBy(css = "lg-button[on-submit=\"$ctrl.submit(label,type)\"] button.pre-saveas")
	private WebElement publishBTN;

	@Override
	public void publishNowTheTemplate() throws Exception {
		if (isElementLoaded(dropdownArrowButton, 10)) {
			scrollToElement(dropdownArrowButton);
			click(dropdownArrowButton);
			click(publishNowButton);
			click(publishTemplateButton);
			if (isElementLoaded(publishTemplateConfirmModal, 10)) {
				click(okButtonOnPublishTemplateConfirmModal);
				waitForSeconds(3);
				displaySuccessMessage();
			} else
				SimpleUtils.fail("Publish template confirm modal fail to load", false);
		} else

			SimpleUtils.fail("Publish template dropdown button load failed", false);
	}

	@FindBy(css = "[ng-if=\"$ctrl.saveAsLabel\"] button")
	private WebElement publishNowBTN;

	@Override
	public void publishNowTemplate() throws Exception {
		if (isElementLoaded(dropdownArrowBTN, 5)) {
			clickTheElement(dropdownArrowBTN);
			waitForSeconds(2);
			clickTheElement(publishNowButton);
			waitForSeconds(2);
			clickTheElement(publishNowBTN);
			waitForSeconds(5);
			if (isElementLoaded(publishTemplateConfirmModal, 5)) {
				click(okButtonOnPublishTemplateConfirmModal);
				displaySuccessMessage();
			}
		} else
			SimpleUtils.fail("Publish template dropdown button load failed", false);
	}

	@FindBy(css = "lg-button[label=\"Save as draft\"] h3[ng-click*= publishReplace]")
	private WebElement publishReplaceButton;
	@FindBy(css = "[ng-if=\"$ctrl.saveAsLabel\"] div h3:nth-child(1)")
	private WebElement saveAsDraftButtonInButtonList;
	@FindBy(css = "[ng-if=\"$ctrl.saveAsLabel\"] div h3:nth-child(2)")
	private WebElement publishNowButtonInButtonList;
	@FindBy(css = "[ng-if=\"$ctrl.saveAsLabel\"] div h3:nth-child(3)")
	private WebElement publishLaterButtonInButtonList;


	@Override
	public void chooseSaveOrPublishBtnAndClickOnTheBtn(String button) throws Exception {
		if (isElementLoaded(dropdownArrowButton, 5)) {
			scrollToElement(dropdownArrowButton);
			click(dropdownArrowButton);
			if (button.toLowerCase().contains("save")) {
				clickTheElement(saveAsDraftButtonInButtonList);
			} else if (button.toLowerCase().contains("publish now")) {
				clickTheElement(publishNowButton);
			} else if (button.toLowerCase().contains("different time")) {
				clickTheElement(publishLaterButton);
			} else if (button.toLowerCase().contains("replacing")) {
				clickTheElement(publishReplaceButton);
			}
//			click(publishTemplateButton);
			clickTheElement(publishBTN);
			if(isElementEnabled(replacingExistingPublishedStatusPopup,2)){
				clickTheElement(okButton);
			}
		} else {
			SimpleUtils.fail("Publish template dropdown button load failed", false);
		}
	}

	@Override
	public void validateAdvanceStaffingRuleShowing(String startEvent, String startOffsetTime, String startEventPoint, String startTimeUnit,
												   String endEvent, String endOffsetTime, String endEventPoint, String endTimeUnit,
												   List<String> days, String shiftsNumber) throws Exception {
		selectShiftStartTimeEvent(startEvent);
		inputOffsetTimeForShiftStart(startOffsetTime, startEventPoint);
		selectShiftStartTimeUnit(startTimeUnit);
		selectShiftEndTimeEvent(endEvent);
		inputOffsetTimeForShiftEnd(endOffsetTime, endEventPoint);
		selectShiftEndTimeUnit(endTimeUnit);
		selectDaysForDaysOfWeekSection(days);
		inputNumberOfShiftsField(shiftsNumber);
		verifyCheckMarkButtonOnAdvanceStaffingRulePage();
		List<WebElement> staffingRuleText = staffingRulesList.get(staffingRulesList.size() - 1).findElements(By.cssSelector("span[ng-bind-html=\"$ctrl.ruleLabelText\"] span"));
		List<String> daysInRule = Arrays.asList(staffingRuleText.get(2).getText().trim().split(","));
		List<String> newDaysInRule = new ArrayList<>();
		for (String dayInRules : daysInRule) {
			String newDayInRule = dayInRules.trim().toLowerCase();
			newDaysInRule.add(newDayInRule);
		}
		List<String> newDays = new ArrayList<>();
		for (String day : days) {
			String newDay = day.substring(0, 3).toLowerCase();
			newDays.add(newDay);
		}
		Collections.sort(newDaysInRule);
		Collections.sort(newDays);
		if (ListUtils.isEqualList(newDaysInRule, newDays)) {
			SimpleUtils.pass("The days info in rule are correct");
		} else {
			SimpleUtils.fail("The days info in rule are NOT correct", false);
		}
		String shiftsNumberInRule = staffingRuleText.get(1).getText().trim();
		if (shiftsNumberInRule.equals(shiftsNumber)) {
			SimpleUtils.pass("The shifts number info in rule is correct");
		} else {
			SimpleUtils.fail("The shifts number info in rule is NOT correct", false);
		}

		String startTimeInfo = staffingRuleText.get(0).getText().trim().split(",")[0];
		String endTimeInfo = staffingRuleText.get(0).getText().trim().split(",")[1];
		String startEventInRule = "";
		for (int i = 5; i < startTimeInfo.split(" ").length; i++) {
			startEventInRule = startEventInRule + startTimeInfo.split(" ")[i] + " ";
		}
		startEventInRule.trim();
		if (startEventInRule.contains(startEvent)) {
			SimpleUtils.pass("The start event info in rule is correct");
		} else {
			SimpleUtils.fail("The start event info in rule is NOT correct", false);
		}
		String startOffsetTimeInRule = startTimeInfo.split(" ")[2].trim();
		if (startOffsetTimeInRule.equals(startOffsetTime)) {
			SimpleUtils.pass("The start Offset Time info in rule is correct");
		} else {
			SimpleUtils.fail("The start Offset Time info in rule is NOT correct", false);
		}
		String startEventPointInRule = startTimeInfo.split(" ")[4].trim();
		if (startEventPointInRule.equals(startEventPoint)) {
			SimpleUtils.pass("The start Event Point info in rule is correct");
		} else {
			SimpleUtils.fail("The start Event Point info in rule is NOT correct", false);
		}
		String startTimeUnitInRule = startTimeInfo.split(" ")[3].trim();
		if (startTimeUnitInRule.equals(startTimeUnit)) {
			SimpleUtils.pass("The start Time Unit info in rule is correct");
		} else {
			SimpleUtils.fail("The start Time Unit info in rule is NOT correct", false);
		}
		String endEventInRule = "";
		for (int i = 5; i < endTimeInfo.split(" ").length; i++) {
			endEventInRule = endEventInRule + endTimeInfo.split(" ")[i] + " ";
		}
		endEventInRule.trim();
		if (endEventInRule.contains(endEvent)) {
			SimpleUtils.pass("The end Event info in rule is correct");
		} else {
			SimpleUtils.fail("The end Event info in rule is NOT correct", false);
		}
		String endOffsetTimeInRule = endTimeInfo.split(" ")[3].trim();
		if (endOffsetTimeInRule.contains(endOffsetTime)) {
			SimpleUtils.pass("The end Offset Time in rule is correct");
		} else {
			SimpleUtils.fail("The end Offset Time in rule is NOT correct", false);
		}
		String endEventPointInRule = endTimeInfo.split(" ")[5].trim();
		if (endEventPointInRule.contains(endEventPoint)) {
			SimpleUtils.pass("The end Event Point in rule is correct");
		} else {
			SimpleUtils.fail("The end Event Point in rule is NOT correct", false);
		}
		String endTimeUnitInRule = endTimeInfo.split(" ")[4].trim();
		if (endTimeUnitInRule.contains(endTimeUnit)) {
			SimpleUtils.pass("The end Time Unit in rule is correct");
		} else {
			SimpleUtils.fail("The end Time Unit in rule is NOT correct", false);
		}
	}

	@FindBy(css = "div.lg-modal")
	private WebElement createNewTemplatePopupWindow;
	@FindBy(css = "input-field[label=\"Name this template\"] input")
	private WebElement newTemplateName;
	@FindBy(css = "input-field[label=\"Description\"] textarea")
	WebElement newTemplateDescription;
	@FindBy(css = "lg-button[label=\"Continue\"] button")
	private WebElement continueBTN;
	@FindBy(css = "span.wm-close-link")
	private WebElement welcomeCloseButton;
	@FindBy(css = "question-input[question-title=\"How many minutes late can employees clock in to scheduled shifts?\"]")
	private WebElement taTemplateSpecialField;

	@Override
	public void createNewTemplate(String templateName) throws Exception {
		if (isElementLoaded(newTemplateBTN, 10)) {
			clickTheElement(newTemplateBTN);
			waitForSeconds(1);
			if (isElementEnabled(createNewTemplatePopupWindow, 10)) {
				SimpleUtils.pass("User can click new template button successfully!");
				clickTheElement(newTemplateName);
				newTemplateName.sendKeys(templateName);
				clickTheElement(newTemplateDescription);
				newTemplateDescription.sendKeys(templateName);
				clickTheElement(continueBTN);
				waitForSeconds(5);
				if (isElementEnabled(welcomeCloseButton, 5)) {
					clickTheElement(welcomeCloseButton);
				}
//				if(isElementEnabled(taTemplateSpecialField, 5)){
//					clickTheElement(taTemplateSpecialField.findElement(By.cssSelector("input")));
//					taTemplateSpecialField.findElement(By.cssSelector("input")).clear();
//					taTemplateSpecialField.findElement(By.cssSelector("input")).sendKeys("5");
//				}
				scrollToBottom();
				if (isElementEnabled(saveAsDraftButton, 10)
						&& isElementLoaded(templateDetailsAssociateTab, 10)
						&& isElementLoaded(templateDetailsBTN, 10)
						&& isElementLoaded(templateExternalAttributesBTN, 10)) {
					SimpleUtils.pass("User can click continue button successfully!");
					waitForSeconds(3);
					clickTheElement(saveAsDraftButton);
					waitForSeconds(5);
				} else {
					SimpleUtils.fail("User can't click continue button successfully!", false);
				}
			} else {
				SimpleUtils.fail("User can't click new template button successfully!", false);
			}
		}
		searchTemplate(templateName);
		String newTemplateName = templateNameList.get(0).getText().trim();
		if (newTemplateName.contains(templateName)) {
			SimpleUtils.pass("User can add new template successfully!");
		} else {
			SimpleUtils.fail("User can't add new template successfully", false);
		}
	}

	@FindBy(css = "lg-button[ng-click=\"deleteTemplate()\"] button")
	private WebElement deleteTemplateButton;

	@FindBy(css = "modal[modal-title=\"Deleting Template\"]")
	private WebElement deleteTemplateDialog;

	@FindBy(css = "modal[modal-title=\"Archive Template\"]")
	private WebElement archiveTemplateDialog;

	@FindBy(css = "modal[modal-title=\"Deleting Template\"] lg-button[label=\"OK\"] button")
	private WebElement okButtonOnDeleteTemplateDialog;

	@Override
	public void deleteNewCreatedTemplate(String templateName) throws Exception {
		if (areListElementVisible(templateNameList, 5) && templateNameList.size() > 0) {
			for (WebElement templateNameElement : templateNameList) {
				if (templateName.equalsIgnoreCase(templateNameElement.getText())) {
					clickTheElement(templateNameElement);
					waitForSeconds(5);

					if (isElementEnabled(deleteTemplateButton, 3)) {
						clickTheElement(deleteTemplateButton);
						if (isElementEnabled(deleteTemplateDialog, 3)) {
							clickTheElement(okButtonOnDeleteTemplateDialog);
							waitForSeconds(5);
							String firstTemplateName = templateNameList.get(0).getText().trim();
							if (!firstTemplateName.equals(templateName)) {
								SimpleUtils.pass("User has deleted new created template successfully!");
							} else {
								SimpleUtils.fail("User failed to delete new created template!", false);
							}
						}
					} else {
						SimpleUtils.fail("Clicking the template failed.", false);
					}
					break;
				}
			}
		} else {
			SimpleUtils.fail("Create new template failed.", false);
		}

	}

	@Override
	public void addAllTypeOfTemplate(String templateName) throws Exception {
		for (int i = 0; i <= 5; i++) {
			clickTheElement(configurationCardsList.get(i));
			waitForSeconds(1);
			createNewTemplate(templateName);
			deleteNewCreatedTemplate(templateName);
			goToConfigurationPage();
		}
	}

	//Added by Mary to check 'Automatically convert unassigned shifts to open shifts when creating a new schedule?' and 'Automatically convert unassigned shifts to open shifts when coping a schedule?'
	@FindBy(css = "question-input[question-title=\"Automatically convert unassigned shifts to open shifts when creating a new schedule?\"]")
	private WebElement convertUnassignedShiftsToOpenWhenCreatingScheduleSetting;

	@FindBy(css = "question-input[question-title=\"Automatically convert unassigned shifts to open shifts when creating a new schedule?\"] .lg-question-input__text")
	private WebElement convertUnassignedShiftsToOpenWhenCreatingScheduleSettingMessage;

	@FindBy(css = "question-input[question-title=\"Automatically convert unassigned shifts to open shifts when creating a new schedule?\"] select[ng-change=\"$ctrl.handleChange()\"]")
	private WebElement convertUnassignedShiftsToOpenWhenCreatingScheduleSettingDropdown;

	@FindBy(css = "question-input[question-title=\"Automatically convert unassigned shifts to open shifts when copying a schedule?\"]")
	private WebElement convertUnassignedShiftsToOpenWhenCopyingScheduleSetting;

	@FindBy(css = "question-input[question-title=\"Automatically convert unassigned shifts to open shifts when copying a schedule?\"] .lg-question-input__text")
	private WebElement convertUnassignedShiftsToOpenWhenCopyingScheduleSettingMessage;

	@FindBy(css = "question-input[question-title=\"Automatically convert unassigned shifts to open shifts when copying a schedule?\"] select[ng-change=\"$ctrl.handleChange()\"]")
	private WebElement convertUnassignedShiftsToOpenWhenCopyingScheduleSettingDropdown;

	@Override
	public void verifyConvertUnassignedShiftsToOpenSetting() throws Exception {
		if (isElementLoaded(convertUnassignedShiftsToOpenWhenCreatingScheduleSetting, 10)
				&& isElementLoaded(convertUnassignedShiftsToOpenWhenCreatingScheduleSettingMessage, 10)
				&& isElementLoaded(convertUnassignedShiftsToOpenWhenCreatingScheduleSettingDropdown, 10)
				&& isElementLoaded(convertUnassignedShiftsToOpenWhenCopyingScheduleSetting, 10)
				&& isElementLoaded(convertUnassignedShiftsToOpenWhenCopyingScheduleSettingMessage, 10)
				&& isElementLoaded(convertUnassignedShiftsToOpenWhenCopyingScheduleSettingDropdown, 10)) {

			//Check the message
			String createScheduleMessage = "Automatically convert unassigned shifts to open shifts when creating a new schedule?";
			String copyScheduleMessage = "Automatically convert unassigned shifts to open shifts when copying a schedule?";
			if (convertUnassignedShiftsToOpenWhenCreatingScheduleSettingMessage.getText().equalsIgnoreCase(createScheduleMessage)) {
				SimpleUtils.pass("OP - Schedule Collaboration: Open Shift : Convert unassigned shifts to open settings creating schedule settings message display correctly! ");
			} else
				SimpleUtils.fail("OP - Schedule Collaboration: Open Shift : Convert unassigned shifts to open when creating schedule settings message display incorrectly!  Expected message is :'"
						+ createScheduleMessage + "'. Actual message is : '" + convertUnassignedShiftsToOpenWhenCreatingScheduleSettingMessage.getText() + "'", false);

			if (convertUnassignedShiftsToOpenWhenCopyingScheduleSettingMessage.getText().equalsIgnoreCase(copyScheduleMessage)) {
				SimpleUtils.pass("OP - Schedule Collaboration: Open Shift : Convert unassigned shifts to open when coping schedule settings message display correctly! ");
			} else
				SimpleUtils.fail("OP - Schedule Collaboration: Open Shift : Convert unassigned shifts to open when coping schedule settings message display incorrectly!  Expected message is :'"
						+ createScheduleMessage + "'. Actual message is : '" + convertUnassignedShiftsToOpenWhenCopyingScheduleSettingMessage.getText() + "'", false);


			List<String> convertUnassignedShiftsToOpenSettingOptions = new ArrayList<>();
			convertUnassignedShiftsToOpenSettingOptions.add("Yes, all unassigned shifts");
			convertUnassignedShiftsToOpenSettingOptions.add("Yes, except opening/closing shifts");
			convertUnassignedShiftsToOpenSettingOptions.add("No, keep as unassigned");

			//Check the options
			Select dropdown = new Select(convertUnassignedShiftsToOpenWhenCreatingScheduleSettingDropdown);
			List<WebElement> dropdownOptions = dropdown.getOptions();
			for (int i = 0; i < dropdownOptions.size(); i++) {
				if (dropdownOptions.get(i).getText().equalsIgnoreCase(convertUnassignedShiftsToOpenSettingOptions.get(i))) {
					SimpleUtils.pass("OP - Schedule Collaboration: Open Shift : Convert unassigned shifts to open when creating schedule settings option: '" + dropdownOptions.get(i).getText() + "' display correctly! ");
				} else
					SimpleUtils.fail("OP - Schedule Collaboration: Open Shift : Convert unassigned shifts to open when creating schedule settings option display incorrectly, expected is : '" + convertUnassignedShiftsToOpenSettingOptions.get(i) +
							"' , the actual is : '" + dropdownOptions.get(i).getText() + "'. ", false);
			}

			//Check the options
			dropdown = new Select(convertUnassignedShiftsToOpenWhenCopyingScheduleSettingDropdown);
			dropdownOptions = dropdown.getOptions();
			for (int i = 0; i < dropdownOptions.size(); i++) {
				if (dropdownOptions.get(i).getText().equalsIgnoreCase(convertUnassignedShiftsToOpenSettingOptions.get(i))) {
					SimpleUtils.pass("OP - Schedule Collaboration: Open Shift : Convert unassigned shifts to open when coping schedule settings option: '" + dropdownOptions.get(i).getText() + "' display correctly! ");
				} else
					SimpleUtils.fail("OP - Schedule Collaboration: Open Shift : Convert unassigned shifts to open when coping schedule settings option display incorrectly, expected is : '" + convertUnassignedShiftsToOpenSettingOptions.get(i) +
							"' , the actual is : '" + dropdownOptions.get(i).getText() + "'. ", false);
			}

		} else
			SimpleUtils.fail("OP Configuration Page: Schedule Collaboration: Open Shift : Convert unassigned shifts to open when coping schedule settings not loaded.", false);
	}


	@Override
	public void updateConvertUnassignedShiftsToOpenWhenCreatingScheduleSettingOption(String option) throws Exception {
		if (isElementLoaded(convertUnassignedShiftsToOpenWhenCreatingScheduleSettingDropdown, 10)) {
			Select dropdown = new Select(convertUnassignedShiftsToOpenWhenCreatingScheduleSettingDropdown);
			dropdown.selectByVisibleText(option);
			SimpleUtils.pass("OP Page: Schedule Collaboration: Open Shift : Convert unassigned shifts to open when creating schedule settings been changed successfully");
		} else {
			SimpleUtils.fail("OP - Schedule Collaboration: Open Shift : Convert unassigned shifts to open when creating schedule settings dropdown list not loaded.", false);
		}
	}

	@Override
	public void updateConvertUnassignedShiftsToOpenWhenCopyingScheduleSettingOption(String option) throws Exception {
		if (isElementLoaded(convertUnassignedShiftsToOpenWhenCopyingScheduleSettingDropdown, 10)) {
			Select dropdown = new Select(convertUnassignedShiftsToOpenWhenCopyingScheduleSettingDropdown);
			dropdown.selectByVisibleText(option);
			SimpleUtils.pass("OP Page: Schedule Collaboration: Open Shift : Convert unassigned shifts to open when copying schedule settings been changed successfully");
		} else {
			SimpleUtils.fail("OP - Schedule Collaboration: Open Shift : Convert unassigned shifts to open when copying schedule settings dropdown list not loaded.", false);
		}
	}

	@FindBy(css = "input-field[value=\"$ctrl.bufferHourMode\"]")
	private List<WebElement> operatingBufferHoursOptions;

	@FindBy(css = "[value=\"$ctrl.openingBufferHours\"] input")
	private WebElement openingBufferHours;

	@FindBy(css = "[value=\"$ctrl.closingBufferHours\"] input")
	private WebElement closingBufferHours;
	@FindBy(css = "span[ng-if*='getSelectedHolidays']")
	private WebElement selectHolidayLink;
	@FindBy(css = "modal[modal-title=\"Manage Holidays\"]")
	private WebElement holidayDialog;
	@FindBy(css = "modal[modal-title=\"Manage Holidays\"] h1 div")
	private WebElement holidayDialogTitle;
	@FindBy(css = "select[aria-label=\"Country\"]")
	private WebElement holidayDialogCountrySelection;
	@FindBy(css = "tr[ng-style*='item.selected']")
	private List<WebElement> holidayItems;
	@FindBy(css = "input[placeholder=\"You can search by holiday name.\"]")
	private WebElement holidaySearchInput;
	@FindBy(css = "tr[ng-repeat*=\"customHolidays\"] input-field[ng-if=\"item.isEditing\"] input")
	private List<WebElement> customerHolidayName;
	@FindBy(css = "tr[ng-repeat*=\"customHolidays\"] lg-calendar-input div.lg-picker-input>input-field")
	private List<WebElement> calendarPicker;
	@FindBy(css = "tr[ng-repeat*=\"customHolidays\"] input[type=\"checkbox\"]")
	private List<WebElement> customerHolidayCheckbox;
	@FindBy(css = "i.fa.fa-check-circle")
	private List<WebElement> customerHolidaySaveIcon;
	@FindBy(css = "tr[ng-repeat*=\"customHolidays\"] span[ng-if=\"!item.isEditing\"].edit")
	private WebElement customerHolidayEdit;
	@FindBy(css = "tr[ng-repeat*=\"customHolidays\"] span[ng-if=\"!item.isEditing\"].remove")
	private WebElement customerHolidayRemove;
	@FindBy(css = "i.fa.fa-times-circle")
	private WebElement customerHolidayCacelIcon;
	@FindBy(css = "tr[ng-repeat*=\"customHolidays\"] span[ng-if='!item.isEditing'].fs-14")
	private WebElement customerHolidayEditedName;
	@FindBy(css = "tr[ng-repeat='holiday in $ctrl.getSelectedHolidays()']")
	private List<WebElement> selectedHolidaysInTemplate;


	private void createAcustomerHoliday(String holidaName) {
		//set a customer name and save
		customerHolidayName.get(0).sendKeys(holidaName);
		waitForSeconds(2);
		clickTheElement(calendarPicker.get(0));
		waitForSeconds(2);
		selectDateForTimesheet(3);
		clickTheElement(customerHolidayCheckbox.get(0));
		clickTheElement(customerHolidaySaveIcon.get(0));
		waitForSeconds(2);
	}

	public void holidaysDataCheckAndSelect(String custoHolyName) throws Exception {
		String checkBoxCss = "td>div>input-field[type=\"checkbox\"] input";
		String selectHoliday = null;
		//click the Select Holidays link
		clickTheElement(selectHolidayLink);
		waitForSeconds(2);
		if (isElementLoaded(holidayDialog) && getText(holidayDialogTitle).trim().equals("Manage Holidays")) {
			SimpleUtils.pass("Select holiday dialog pop up successfully");
			//filter holidays
			holidaySearchInput.sendKeys("Memorial");
			waitForSeconds(2);
			if (areListElementVisible(holidayItems))
				SimpleUtils.report("Holiday search with resulted");
			holidaySearchInput.clear();
//			selectByVisibleText(holidayDialogCountrySelection,"United States");
//			waitForSeconds(2);
			//select a holiday
			if (areListElementVisible(holidayItems)) {
				SimpleUtils.pass("Holidays options loaded successfully");
				//select the first holiday
				clickTheElement(holidayItems.get(0).findElement(By.cssSelector(checkBoxCss)));
				waitForSeconds(2);
				//get the holiday name
				selectHoliday = holidayItems.get(0).findElement(By.cssSelector("span.fs-14")).getText().trim();
			} else
				SimpleUtils.fail("Holidays options loaded fail", false);
			//create a customer holiday
			createAcustomerHoliday(custoHolyName);
			//edit the holiday name to check button works or not
			clickTheElement(customerHolidayEdit);
			waitForSeconds(2);
			//modify the holiday name
			customerHolidayName.get(0).clear();
			customerHolidayName.get(0).sendKeys(custoHolyName + " Modified");
			//save the change
			clickTheElement(customerHolidaySaveIcon.get(0));
			waitForSeconds(2);
			//get the modified holiday name
			String modifiedName = customerHolidayEditedName.getText().trim();
			if (modifiedName.equals(custoHolyName + " Modified"))
				SimpleUtils.pass("Customer holiday name modified successfully");
			else
				SimpleUtils.report("Customer holiday name modified Failed");
			//remove the customer holiday
			clickTheElement(customerHolidayRemove);
			//create the customer holiday again
			createAcustomerHoliday(custoHolyName);
			//save
			clickTheElement(saveBtnInManageDayparts);
			waitForSeconds(2);
			//check the selected or created customer holiday show on template page or not
			if (areListElementVisible(selectedHolidaysInTemplate)) {
				SimpleUtils.pass("Selected holidays shows on template detail page");
				//check the customer selected holiday name
				for (WebElement es : selectedHolidaysInTemplate) {
					if (es.findElement(By.cssSelector(" td >span")).getText().trim().equals(custoHolyName + " Modified"))
						SimpleUtils.pass("The customer holiday show on the page successfully");
					else if (selectHoliday != null && es.findElement(By.cssSelector(" td >span")).getText().trim().equals(selectHoliday))
						SimpleUtils.pass("The specified selected holiday show on the page successfully");
				}

			} else
				SimpleUtils.fail("Selected holidays not show on template detail page", true);
			//back to customer holiday to remove the customer holiday and unselected specified holiday
			clickTheElement(selectHolidayLink);
			waitForSeconds(2);
			//unselect the specified holiday
			if (areListElementVisible(holidayItems)) {
				SimpleUtils.pass("Holidays options loaded successfully");
				//select the first holiday
				clickTheElement(holidayItems.get(0).findElement(By.cssSelector(checkBoxCss)));
			}
			//remove the customer holiday
			clickTheElement(customerHolidayRemove);
			//click save
			clickTheElement(saveBtnInManageDayparts);
			waitForSeconds(2);
			//check if holiday show on template detail or not
			if (!areListElementVisible(selectedHolidaysInTemplate, 20))
				SimpleUtils.pass("Unselect the specified holiday and remove the customer holiday is successfully!");
			//save the template as draft again
			if (isElementEnabled(saveAsDraftButton)) {
				SimpleUtils.pass("User can click save as draft button!");
				clickTheElement(saveAsDraftButton);
				waitForSeconds(3);
			}
		} else
			SimpleUtils.fail("Select holiday dialog not pop up", false);


	}


	// Option: None, StartEnd, BufferHour, ContinuousOperation
	public void selectOperatingBufferHours(String option) throws Exception {
		if (areListElementVisible(operatingBufferHoursOptions, 10) && operatingBufferHoursOptions.size() == 4) {
			for (WebElement operatingBufferHours : operatingBufferHoursOptions) {
				if (operatingBufferHours.getAttribute("assigned-value").contains(option)) {
					WebElement inputButton = operatingBufferHours.findElement(By.className("input-form"));
					if (!inputButton.getAttribute("class").contains("ng-valid-parse")) {
						click(inputButton);
						SimpleUtils.pass("OP Page: Operating Hours: Operating / Buffer Hours : The '" + option + "' option been selected successfully! ");
					} else
						SimpleUtils.pass("OP Page: Operating Hours: Operating / Buffer Hours : The '" + option + "' option has been selected! ");
					break;
				}
			}
		} else {
			SimpleUtils.fail("OP - Operating Hours: Operating / Buffer Hours : Operating hours options not loaded.", false);
		}
	}

	public void setOpeningAndClosingBufferHours(int openingBufferHour, int closingBufferHour) throws Exception {

		if (isElementLoaded(openingBufferHours, 5) && isElementLoaded(closingBufferHours, 5)) {
			openingBufferHours.clear();
			closingBufferHours.clear();
			openingBufferHours.sendKeys(String.valueOf(openingBufferHour));
			closingBufferHours.sendKeys(String.valueOf(closingBufferHour));
		} else
			SimpleUtils.fail("OP - Operating Hours: Operating / Buffer Hours : Operating buffer hours and closing buffer hours are not loaded.", false);
	}


	@FindBy(css = "question-input[question-title=\"Enable schedule copy restrictions\"] > div > div.lg-question-input__wrapper > ng-transclude > yes-no > ng-form > lg-button-group >div>div")
	private List<WebElement> yesNoForScheduleCopyRestrictions;

	@Override
	public void setScheduleCopyRestrictions(String yesOrNo) throws Exception {
		if (areListElementVisible(yesNoForScheduleCopyRestrictions, 5)) {
			for (WebElement option : yesNoForScheduleCopyRestrictions) {
				if (option.getText().equalsIgnoreCase(yesOrNo)) {
					click(option);
					break;
				}
			}
			SimpleUtils.pass("Set copy restriction to " + yesOrNo + " successfully! ");
		} else
			SimpleUtils.fail("Set copy restriction setting fail to load!  ", false);
	}

	// Added by Julie
	@FindBy(css = ".dayparts span.manage-action-wrapper")
	private WebElement manageDaypartsBtn;

	@FindBy(css = "[ng-if=\"!$ctrl.getSelectedDayparts().length\"]")
	private WebElement selectDapartsBtn;

	@FindBy(css = "table div input.ng-not-empty")
	private List<WebElement> checkedBoxes;

	@FindBy(css = "table div.lg-select-list__thumbnail-wrapper input-field[type=\"checkbox\"]")
	private List<WebElement> allCheckBoxes;

	@FindBy(css = "div.lg-select-list__table-wrapper table tbody tr")
	private List<WebElement> rowsInManageDayparts;

	@FindBy(xpath = "//body/div[1]//lg-daypart-weekday/div[@class=\"availability-row availability-row-active ng-scope\"]")
	private List<WebElement> rowsWhenSetDaypart;

	@FindBy(css = ".availability-row-time [ng-repeat=\"r in $ctrl.hoursRange\"]")
	private List<WebElement> timeRangeWhenSetDaypart;

	@FindBy(css = ".daypart-legend .item")
	private List<WebElement> itemsOfDaypart;

	@FindBy(css = "lg-button[label=\"Save\"] button")
	private WebElement saveBtnInManageDayparts;

	@FindBy(css = ".business-hours lg-button[label=\"Edit\"]")
	private List<WebElement> editBtnsOfBusinessHours;

	@FindBy(css = ".location-working-hours nav.lg-tabs__nav div")
	private List<WebElement> tabsWhenEditBusinessHours;

	@FindBy(xpath = "//body/div[1]//input-field/ng-form")
	private List<WebElement> daysCheckBoxes;

	@FindBy(className = "col-sm-4")
	private List<WebElement> daysOfDayParts;

	@FindBy(className = "row-fx")
	private List<WebElement> rowsOfDayParts;

	@Override
	public void disableAllDayparts() throws Exception {
		if (areListElementVisible(dayPartsList, 10)) {
			click(manageDaypartsBtn);
			if (areListElementVisible(allCheckBoxes, 10)) {
				for (int i = 0; i < allCheckBoxes.size(); i++) {
					if (allCheckBoxes.get(i).findElement(By.tagName("input")).getAttribute("class").contains("ng-not-empty"))
						click(allCheckBoxes.get(i));
				}
				if (checkedBoxes.size() == 0)
					SimpleUtils.pass("Operation Hours: Day parts have been disabled");
				else
					SimpleUtils.fail("Operation Hours: Day parts have been not disabled", false);
				scrollToBottom();
				clickTheElement(saveBtnInManageDayparts);
			} else
				SimpleUtils.fail("Operation Hours: Checked boxes failed to load in day parts", false);
		} else if (isElementLoaded(selectDapartsBtn, 10))
			SimpleUtils.pass("Operation Hours: No Dayparts Available");
		else
			SimpleUtils.fail("Operation Hours: Day parts failed to load", false);
	}

	@Override
	public void selectDaypart(String dayPart) throws Exception {
		boolean isDayPartPresent = false;
		if (isElementEnabled(selectDapartsBtn, 10)) {
			clickTheElement(selectDapartsBtn);
			if (areListElementVisible(rowsInManageDayparts, 10)) {
				for (int i = 0; i < rowsInManageDayparts.size(); i++) {
					WebElement daypartName = rowsInManageDayparts.get(i).findElement(By.xpath("./td[3]//span"));
					WebElement daypartCheckBox = rowsInManageDayparts.get(i).findElement(By.xpath("./td[1]//input"));
					if (daypartName.getText().equals(dayPart)) {
						if (daypartCheckBox.getAttribute("class").contains("ng-empty"))
							click(daypartCheckBox);
						break;
					}
				}
				scrollToBottom();
				clickTheElement(saveBtnInManageDayparts);
			} else
				SimpleUtils.fail("Operation Hours: Rows failed to load in manage day parts", false);
		}
		if (areListElementVisible(dayPartsList, 10)) {
			for (WebElement row : dayPartsList) {
				WebElement daypartName = row.findElement(By.xpath("./td[1]"));
				if (daypartName.getText().equals(dayPart)) {
					isDayPartPresent = true;
					break;
				}
			}
			if (!isDayPartPresent) {
				click(manageDaypartsBtn);
				if (areListElementVisible(rowsInManageDayparts, 10)) {
					for (int i = 0; i < rowsInManageDayparts.size(); i++) {
						WebElement daypartName = rowsInManageDayparts.get(i).findElement(By.xpath("./td[3]//span"));
						WebElement daypartCheckBox = rowsInManageDayparts.get(i).findElement(By.xpath("./td[1]//input"));
						if (daypartName.getText().equals(dayPart)) {
							if (daypartCheckBox.getAttribute("class").contains("ng-empty"))
								click(daypartCheckBox);
							break;
						}
					}
					scrollToBottom();
					clickTheElement(saveBtnInManageDayparts);
				} else
					SimpleUtils.fail("Operation Hours: Rows failed to load in manage day parts", false);
				waitForSeconds(3);
				for (WebElement row : dayPartsList) {
					WebElement daypartName = row.findElement(By.xpath("./td[1]"));
					if (daypartName.getText().equals(dayPart)) {
						isDayPartPresent = true;
						break;
					}
				}
			}
		} else
			SimpleUtils.fail("Operation Hours: Day parts failed to load", false);
		if (isDayPartPresent)
			SimpleUtils.pass("Operation Hours: Operation Hours: '" + dayPart + "' is selected successfully");
		else
			SimpleUtils.fail("Operation Hours: Operation Hours: '" + dayPart + "' doesn't exist", false);
	}

	@Override
	public void setDaypart(String day, String dayPart, String startTime, String endTime) throws Exception {
		// Please set the start time and end time's format like 11am, 2pm
		String daypartColor = "";
		List<WebElement> hourCells = null;
		WebElement colorInRow = null;
		if (areListElementVisible(daysOfDayParts, 10)) {
			for (int h = 0; h < daysOfDayParts.size(); h++) {
				if (day.equals("All days")) {
					clickTheElement(editBtnsOfBusinessHours.get(h));
					break;
				}
				if (daysOfDayParts.get(h).getText().toUpperCase().equals(day.toUpperCase())) {
					clickTheElement(editBtnsOfBusinessHours.get(h));
					break;
				}
			}
		}
		int l = 0;
		int k = 0;
		if (areListElementVisible(tabsWhenEditBusinessHours, 10))
			click(tabsWhenEditBusinessHours.get(1));
		int j = 0;
		int i = 0;
		if (areListElementVisible(itemsOfDaypart, 10)) {
			for (WebElement item : itemsOfDaypart) {
				WebElement itemName = item.findElement(By.className("ng-binding"));
				if (itemName.getText().equals(dayPart)) {
					WebElement itemColor = item.findElement(By.className("icon"));
					daypartColor = itemColor.getAttribute("style");
					break;
				}
			}
		} else
			SimpleUtils.fail("Operation Hours: Daypart legend failed to load when editing daypart", false);
		if (areListElementVisible(rowsWhenSetDaypart, 10)) {
			for (i = 0; i < rowsWhenSetDaypart.size(); i++) {
				try {
					colorInRow = rowsWhenSetDaypart.get(i).findElement(By.cssSelector("day-part-color>div>div"));
				} catch (Exception e) {
					continue;
				}
				String color = colorInRow.getAttribute("style");
				if (color.contains(daypartColor)) {
					hourCells = rowsWhenSetDaypart.get(i).findElements(By.cssSelector(".availability-box div"));
					break;
				}
			}
		} else
			SimpleUtils.fail("Operation Hours: Daypart rows failed to load when editing daypart", false);
		if (areListElementVisible(timeRangeWhenSetDaypart, 10)) {
			for (int m = 0; m < timeRangeWhenSetDaypart.size(); m++) {
				String hourRange = timeRangeWhenSetDaypart.get(m).getText().trim().replace("\n", "");
				if (hourRange.contains("12PM")) {
					j = m;
					break;
				}
			}
			if (startTime.contains("am")) {
				for (k = 0; k < j; k++) {
					String hourRange = timeRangeWhenSetDaypart.get(k).getText().trim().replace("\n", "");
					if (hourRange.equals(startTime.replace("am", ""))) {
						break;
					}
				}
			} else if (startTime.contains("pm")) {
				for (k = j; k < timeRangeWhenSetDaypart.size(); k++) {
					String hourRange = timeRangeWhenSetDaypart.get(k).getText().trim().replace("\n", "");
					if (hourRange.equals(startTime.replace("pm", ""))) {
						break;
					}
				}
			}
			if (endTime.contains("am")) {
				for (l = 0; l < j; l++) {
					String hourRange = timeRangeWhenSetDaypart.get(l).getText().trim().replace("\n", "");
					if (hourRange.equals(endTime.replace("am", ""))) {
						break;
					}
				}
			} else if (endTime.contains("pm")) {
				for (l = j; l < timeRangeWhenSetDaypart.size(); l++) {
					String hourRange = timeRangeWhenSetDaypart.get(l).getText().trim().replace("\n", "");
					if (hourRange.equals(endTime.replace("pm", ""))) {
						break;
					}
				}
			}
			mouseHoverDragandDrop(hourCells.get(k), hourCells.get(l - 1));
			if (day.equals("All days"))
				click(daysCheckBoxes.get(daysCheckBoxes.size() - 1));
			WebElement dayPartMap = rowsWhenSetDaypart.get(i).findElement(By.cssSelector("[ng-if=\"$ctrl.daypartMap[daypart.objectId]\"] span"));
			if (dayPartMap.getText().equals(startTime + " - " + endTime))
				SimpleUtils.pass("Operation Hours: Day Part with '" + startTime + " - " + endTime + "' has been set");
			else
				SimpleUtils.fail("Operation Hours: Actual Day Part is '" + dayPartMap.getText() + "', expected Day Part is '" + startTime + " - " + endTime + "'", false);
			click(saveBtnInManageDayparts);
		} else
			SimpleUtils.fail("Operation Hours: Daypart rows failed to load when editing daypart", false);
	}

	@Override
	public HashMap<String, List<String>> getDayPartsDataFromBusinessHours() throws Exception {
		HashMap<String, List<String>> dataFromBusinessHours = new HashMap<>();
		List<String> nameColorDuration = new ArrayList<>();
		if (areListElementVisible(rowsOfDayParts, 10)) {
			for (WebElement row : rowsOfDayParts) {
				WebElement day = row.findElement(By.className("col-sm-4"));
				List<WebElement> progressBars = row.findElements(By.className("progress-bar"));
				for (WebElement bar : progressBars) {
					click(bar);
					String progress = bar.getAttribute("innerHTML");
					System.out.println(progress);
					String tool = bar.getAttribute("data-tootik");
					System.out.println(tool);

					nameColorDuration.add(progress);
				}
				dataFromBusinessHours.put(day.getText(), nameColorDuration);
			}
		} else
			SimpleUtils.fail("Operation Hours: Business Hours rows failed to load ", false);
		return dataFromBusinessHours;
	}

	@Override
	public void goToBusinessHoursEditPage(String workkday) throws Exception {
		boolean isEnabled = false;
		String day = "";
		WebElement editButton = null;

		if (areListElementVisible(OHBusinessHoursEntries, 5) && OHBusinessHoursEntries.size() == 7) {
			for (WebElement OHBusinessHour : OHBusinessHoursEntries) {
				isEnabled = OHBusinessHour.findElement(By.cssSelector("input[type=\"checkbox\"]")).getAttribute("class").contains("ng-not-empty");
				day = OHBusinessHour.findElement(By.cssSelector("div.col-sm-4")).getAttribute("innerText").replace("\n", "").trim();
				editButton = OHBusinessHour.findElement(By.cssSelector("lg-button[label=\"Edit\"]"));
				if (day.toLowerCase().equals(workkday) && isEnabled) {
					clickTheElement(editButton);
					if (areListElementVisible(tabsWhenEditBusinessHours)) {
						SimpleUtils.pass("Business hours edit page load successfully!");
						break;
					} else {
						SimpleUtils.fail("Business hours edit page not load as expected!", false);
					}
				}
			}
		} else {
			SimpleUtils.fail("Business hours section failed to load!", false);
		}
	}

	@FindBy(css = "span[class*=\"next-day-icon-trigger\"]")
	private List<WebElement> nextDayIcon;
	@FindBy(css = "div.next-day-menu input-field[label=\"Next Day\"]")
	private WebElement nextDayRadio;

	@Override
	public void checkOpenAndCloseTime() throws Exception {
		String timeBeforeEdit = "";
		String timeAfterEdit = "";
		int numberOfTimeInput = openCloseTimeInputs.size();

		if (areListElementVisible(openCloseTimeInputs, 2)) {
			for (int i = 0; i < openCloseTimeInputs.size(); i++) {
				//Check if text input
				if (openCloseTimeInputs.get(i).getAttribute("type").equals("text")) {
					//Check if up and down keys work fine
					if (i > 1) {
						//switch to day part tab
						clickTheElement(OHOperateDayPartTab);
						waitForSeconds(2);
					}
					int clickCount = 1;
					Actions action = new Actions(getDriver());
					//Locate the hour: click for once
					//Locate the minute: click for twice
					//Locate the AM/PM: click for three times
					while (clickCount <= 3) {
						for (int j = 0; j < clickCount; j++) {
							openCloseTimeInputs.get(i).click();
						}
						clickCount++;

						for (int j = 0; j < 3; j++) {
							timeBeforeEdit = openCloseTimeInputs.get(i).findElement(By.xpath("./following-sibling::div")).getAttribute("innerText").trim();
							openCloseTimeInputs.get(i).sendKeys(Keys.ARROW_UP);
							timeAfterEdit = openCloseTimeInputs.get(i).findElement(By.xpath("./following-sibling::div")).getAttribute("innerText").trim();
							if (timeBeforeEdit.equals(timeAfterEdit)) {
								SimpleUtils.fail("Time not change by pressing UP key!", false);
							}
						}
						for (int j = 0; j < 5; j++) {
							timeBeforeEdit = openCloseTimeInputs.get(i).findElement(By.xpath("./following-sibling::div")).getAttribute("innerText").trim();
							openCloseTimeInputs.get(i).sendKeys(Keys.ARROW_DOWN);
							timeAfterEdit = openCloseTimeInputs.get(i).findElement(By.xpath("./following-sibling::div")).getAttribute("innerText").trim();
							if (timeBeforeEdit.equals(timeAfterEdit)) {
								SimpleUtils.fail("Time not change by pressing UP key!", false);
							}
						}
					}
					//Check if next day exist.
					if ((i + 1) % 2 == 0 && isElementEnabled(nextDayIcon.get((i - 1) / 2))) {
						clickTheElement(nextDayIcon.get((i - 1) / 2));
						if (isElementLoaded(nextDayRadio)) {
							SimpleUtils.pass("Next Day is correctly displayed!");
						} else {
							SimpleUtils.fail("Next day failed to load!", false);
						}
					}
				} else {
					SimpleUtils.fail("Time input should be text box!", false);
				}
			}
		}

	}

	@Override
	public void clickOpenCloseTimeLink() throws Exception {
		if (isElementLoaded(OHOperateOpenCloseTimeEditLink)) {
			clickTheElement(OHOperateOpenCloseTimeEditLink);
			if (isElementLoaded(OHOperateOpenCloseTimeEditDialog)) {
				SimpleUtils.pass("Edit open/close time dialog pops up successfully");
			} else {
				SimpleUtils.fail("Edit open/close time dialog not pops up as expected!", false);
			}
		}
	}

	public boolean checkNextDayForCloseTime(WebElement nextDayIconToClick, String crossNextDay) throws Exception {
		boolean isNextDaySelected = false;

		if ((crossNextDay.equalsIgnoreCase("yes") && nextDayIconToClick.findElement(By.cssSelector("img")).getAttribute("src").contains("next-day-small"))
				|| (crossNextDay.equalsIgnoreCase("no") && nextDayIconToClick.findElement(By.cssSelector("img")).getAttribute("src").contains("plus-1-b"))) {
			clickTheElement(nextDayIconToClick);
			if (isElementLoaded(nextDayRadio)) {
				clickTheElement(nextDayRadio.findElement(By.cssSelector("input")));
				isNextDaySelected = nextDayRadio.findElement(By.cssSelector("input")).getAttribute("class").contains("ng-not-empty");
				if (isNextDaySelected && crossNextDay.equalsIgnoreCase("yes")) {
					SimpleUtils.pass("Next day is selected!");
				} else if (!isNextDaySelected && crossNextDay.equalsIgnoreCase("no")) {
					SimpleUtils.pass("Next day cancel to select!");
				} else {
					SimpleUtils.fail("The selection for Next day is different from provided parameter", false);
				}
			} else {
				SimpleUtils.fail("Next day failed to load!", false);
			}
		}
		return isNextDaySelected;
	}

	public int calculateOpenCloseTime(String type, String openOrCloseTime, boolean isNextDaySelected) {
		int abstHour = 0;
		int absMinute = 0;
		int distance = 0;
		int absTime = 0;

		String[] opentimeArray = openOrCloseTime.replace("PM", "").replace("AM", "").split(":");
		abstHour = Integer.parseInt(opentimeArray[0]);
		absMinute = Integer.parseInt(opentimeArray[1]);

		if (openOrCloseTime.contains("PM") & !openOrCloseTime.equals("12:00PM")) {
			abstHour += 12;
		}

		if (type.toLowerCase().contains("start")) {
			if (openOrCloseTime.equals("12:00AM")) {
				abstHour = 0;
				absMinute = 0;
			}
			absTime = abstHour * 60 + absMinute;
		} else if (type.toLowerCase().contains("end")) {
			if (openOrCloseTime.equals("12:00AM")) {
				abstHour = 24;
				absMinute = 0;
			}
			if (isNextDaySelected) {
				distance += 24;
			}
			absTime = (abstHour + distance) * 60 + absMinute;
		}
		return absTime;
	}

	public boolean verifyOpenAndCloseTime(String settingTab, int absOpenTime, int absCloseTime, boolean isNextDaySelected) throws Exception {
		boolean isvalidTime = false;
		if ((absCloseTime - absOpenTime) > 1440) {
			if (settingTab.toLowerCase().contains("open") && isElementLoaded(warningMsg)) {
				if (warningMsg.getAttribute("innerText").replace("\n", "").trim().contains("cannot exceed 24 hours") &&
						saveButton.getAttribute("disabled").equals("true")) {
					SimpleUtils.pass("Warning Message is correct!");
				} else {
					SimpleUtils.fail("There should be an exceeding 24 hours warning!", false);
				}
			} else if (settingTab.toLowerCase().contains("daypart") && isElementLoaded(dayPartsWarnigIcon)) {
				Actions builder = new Actions(MyThreadLocal.getDriver());
				builder.moveToElement(dayPartsWarnigIcon).build().perform();
				if (popUpWarningMsg.getAttribute("innerText").replace("\n", "").trim().contains("cannot exceed 24 hours") &&
						saveButton.getAttribute("disabled").equals("true")) {
					SimpleUtils.pass("Warning Message is correct!");
				} else {
					SimpleUtils.fail("There should be an exceeding 24 hours warning!", false);
				}
			}
		} else if (!isNextDaySelected && absOpenTime > absCloseTime) {
			if (settingTab.toLowerCase().contains("open") && isElementLoaded(warningMsg)) {
				if (warningMsg.getAttribute("innerText").replace("\n", "").trim().contains("Open time should be before close time") &&
						saveButton.getAttribute("disabled").equals("true")) {
					SimpleUtils.pass("Warning Message is correct!");
				} else {
					SimpleUtils.fail("There should be an open time should before close time warning!", false);
				}
			} else if (settingTab.toLowerCase().contains("daypart") && isElementLoaded(dayPartsWarnigIcon)) {
				Actions builder = new Actions(MyThreadLocal.getDriver());
				builder.moveToElement(dayPartsWarnigIcon).build().perform();
				if (popUpWarningMsg.getAttribute("innerText").replace("\n", "").trim().contains("Open time should be before close time") &&
						saveButton.getAttribute("disabled").equals("true")) {
					SimpleUtils.pass("Warning Message is correct!");
				} else {
					SimpleUtils.fail("There should be open time should before close time warning!", false);
				}
			}
		} else {
			isvalidTime = true;
		}
		return isvalidTime;
	}

	@FindBy(css = "div[class*=\"validationMessage\"]")
	private WebElement warningMsg;
	@FindBy(css = "i.fa-exclamation-circle")
	private WebElement dayPartsWarnigIcon;
	@FindBy(css = "div.group-type-pop")
	private WebElement popUpWarningMsg;

	@Override
	public void setOpenCloseTime(String settingTab, String openTime, String closeTime, String crossNextDay) throws Exception {
		int absOpenTime = 0;
		int absCloseTime = 0;
		boolean isNextDaySelected = false;
		WebElement nextDaySign = null;
		WebElement nextDayIconToClick = null;

		if (settingTab.toLowerCase().contains("open")) {
			nextDayIconToClick = nextDayIcon.get(0);
		} else if (settingTab.toLowerCase().contains("daypart")) {
			nextDayIconToClick = nextDayIcon.get(1);
			clickTheElement(OHOperateDayPartTab);
		}
		//Set Open and Close time
		OHTempSetOpenAndCloseTime(settingTab, openTime, closeTime);
		//Check if next day
		isNextDaySelected = checkNextDayForCloseTime(nextDayIconToClick, crossNextDay);
		//Calculate absolute time for Open and Close time
		absOpenTime = calculateOpenCloseTime("start", openTime, false);
		absCloseTime = calculateOpenCloseTime("end", closeTime, isNextDaySelected);
		//Verify if Open and Close time are valid
		verifyOpenAndCloseTime(settingTab, absOpenTime, absOpenTime, isNextDaySelected);
	}

	@Override
	public boolean verifyStartEndTimeForDays(String startTime, String endTime, String day) throws Exception {
		boolean isEnabled = false;
		boolean flag = false;

		//get the start time and end time for a certain day
		if (areListElementVisible(OHBusinessHoursEntries, 5) && OHBusinessHoursEntries.size() == 7) {
			for (int i = 0; i < OHBusinessHoursEntries.size(); i++) {
				isEnabled = OHBusinessHoursEntries.get(i).findElement(By.cssSelector("input[type=\"checkbox\"]")).getAttribute("class").contains("ng-not-empty");
				String dayInEntry = OHBusinessHoursEntries.get(i).findElement(By.cssSelector("div.col-sm-4")).getAttribute("innerText").replace("\n", "").trim();
				editButton = OHBusinessHoursEntries.get(i).findElement(By.cssSelector("lg-button[label=\"Edit\"]"));
				if (dayInEntry.equalsIgnoreCase(day) && isEnabled) {
					String StartOrigin = OHBusinessHoursEntries.get(i).findElement(By.cssSelector("span.work-time")).getText().trim();
					String EndOrigin = OHBusinessHoursEntries.get(i).findElement(By.cssSelector("span.work-time.mr-2")).getText().trim();
					if (StartOrigin.equals(startTime) && EndOrigin.equals(endTime)) {
						flag = true;
						SimpleUtils.pass("The start time and end time show as expected!");
					}
					break;
				}
			}
		}
		return flag;
	}

	@FindBy(css = "div.each-day-selector")
	private List<WebElement> eachDaySelector;

	@Override
	public void selectDaysForOpenCloseTime(List<String> dayOfWeek) throws Exception {
		if (areListElementVisible(eachDaySelector)) {
			for (String day : dayOfWeek) {
				for (WebElement dayToSelect : eachDaySelector) {
					if (day.equalsIgnoreCase(dayToSelect.findElement(By.cssSelector("input-field")).getAttribute("label")) &&
							dayToSelect.findElement(By.cssSelector("input-field input")).getAttribute("class").contains("ng-empty")) {
						clickTheElement(dayToSelect.findElement(By.cssSelector("input-field input")));
						break;
					}
				}
			}
		}
	}

	@Override
	public void editBasicStaffingRules() throws Exception {
		if (isElementLoaded(editButtonOfStaffingRule, 2)) {
			clickTheElement(editButtonOfStaffingRule);
			if (isElementLoaded(staffingRuleFields, 2)) {
				SimpleUtils.pass("User can click pencil edit button successfully for basic staffing rule!");
			} else {
				SimpleUtils.fail("User can NOT click edit pencil button successfully for basic staffing rule!", false);
			}
		} else {
			SimpleUtils.fail("pencil button is not showing", false);
		}
	}

	@FindBy(css = ".console-navigation-item-label.User.Management")
	private WebElement userManagementTab;

	@Override
	public void goToUserManagementPage() throws Exception {
		if (isElementEnabled(userManagementTab, 10)) {
			click(userManagementTab);
			waitForSeconds(20);
			if (categoryOfTemplateList.size() != 0) {
				SimpleUtils.pass("User can click user management tab successfully");
			} else {
				SimpleUtils.fail("User can't click user management tab", false);
			}
		} else
			SimpleUtils.fail("User management tab load failed", false);
	}

	@FindBy(css = "question-input[question-title=\"Is approval required by Manager when an employee claims an Open Shift in a home location?\"] yes-no")
	private WebElement approveShiftInHomeLocationSetting;

	@FindBy(css = "question-input[question-title=\"Is approval required by Manager and non-home Manager when an employee claims an Open Shift in a non-home location?\"] yes-no")
	private WebElement approveShiftInNonHomeLocationSetting;

	@Override
	public void enableOrDisableApproveShiftInHomeLocationSetting(String yesOrNo) throws Exception {
		if (isElementLoaded(approveShiftInHomeLocationSetting, 10)) {
			scrollToElement(approveShiftInHomeLocationSetting);
			if (yesOrNo.equalsIgnoreCase("yes")) {
				if (isElementLoaded(approveShiftInHomeLocationSetting.findElement(By.cssSelector(".lg-button-group-first")), 10)) {
					click(approveShiftInHomeLocationSetting.findElement(By.cssSelector(".lg-button-group-first")));
					SimpleUtils.pass("Turned on 'Is approval required by Manager when an employee claims an Open Shift in a home location?!' setting successfully! ");
				} else {
					SimpleUtils.fail("Yes button fail to load!", false);
				}
			} else if (yesOrNo.equalsIgnoreCase("no")) {
				if (isElementLoaded(approveShiftInHomeLocationSetting.findElement(By.cssSelector(".lg-button-group-last")), 10)) {
					click(approveShiftInHomeLocationSetting.findElement(By.cssSelector(".lg-button-group-last")));
					SimpleUtils.pass("Turned off 'Is approval required by Manager when an employee claims an Open Shift in a home location?!' setting successfully! ");
				} else {
					SimpleUtils.fail("No button fail to load!", false);
				}
			} else {
				SimpleUtils.warn("You have to input the right command: yes or no");
			}
		} else {
			SimpleUtils.fail("'Is approval required by Manager when an employee claims an Open Shift in a home location?!' setting is not loaded!", false);
		}
	}


	@Override
	public boolean checkIfApproveShiftInHomeLocationSettingEnabled() throws Exception {
		boolean isApproveShiftInHomeLocationSettingEnabled = false;
		if (isElementLoaded(approveShiftInHomeLocationSetting, 10)) {
			scrollToElement(approveShiftInHomeLocationSetting);
			if (approveShiftInHomeLocationSetting.findElement(By.cssSelector(".lg-button-group-first")).getAttribute("class").contains("selected")) {
				isApproveShiftInHomeLocationSettingEnabled = true;
				SimpleUtils.pass("'Is approval required by Manager when an employee claims an Open Shift in a home location?!' setting is enabled! ");
			} else {
				SimpleUtils.report("'Is approval required by Manager when an employee claims an Open Shift in a home location?!' setting is disabled! ");
			}
		} else {
			SimpleUtils.fail("'Is approval required by Manager when an employee claims an Open Shift in a home location?!' setting is not loaded!", false);
		}
		return isApproveShiftInHomeLocationSettingEnabled;
	}

	@Override
	public void enableOrDisableApproveShiftInNonHomeLocationSetting(String yesOrNo) throws Exception {
		if (isElementLoaded(approveShiftInNonHomeLocationSetting, 10)) {
			scrollToElement(approveShiftInNonHomeLocationSetting);
			if (yesOrNo.equalsIgnoreCase("yes")) {
				if (isElementLoaded(approveShiftInNonHomeLocationSetting.findElement(By.cssSelector(".lg-button-group-first")), 10)) {
					click(approveShiftInNonHomeLocationSetting.findElement(By.cssSelector(".lg-button-group-first")));
					SimpleUtils.pass("Turned on 'Is approval required by Manager and non-home Manager when an employee claims an Open Shift in a non-home location?' setting successfully! ");
				} else {
					SimpleUtils.fail("Yes button fail to load!", false);
				}
			} else if (yesOrNo.equalsIgnoreCase("no")) {
				if (isElementLoaded(approveShiftInNonHomeLocationSetting.findElement(By.cssSelector(".lg-button-group-last")), 10)) {
					click(approveShiftInNonHomeLocationSetting.findElement(By.cssSelector(".lg-button-group-last")));
					SimpleUtils.pass("Turned off 'Is approval required by Manager and non-home Manager when an employee claims an Open Shift in a non-home location?' setting successfully! ");
				} else {
					SimpleUtils.fail("No button fail to load!", false);
				}
			} else {
				SimpleUtils.warn("You have to input the right command: yes or no");
			}
		} else {
			SimpleUtils.fail("'Is approval required by Manager and non-home Manager when an employee claims an Open Shift in a non-home location?' setting is not loaded!", false);
		}
	}

	@FindBy(css = "tbody[ng-repeat=\"workRole in $ctrl.sortedRows\"]>tr>td:nth-child(2)>lg-button>button[type='button']")
	private List<WebElement> staffingRulesForWorkRoleInSchedulingRoles;

	@Override
	public void goToWorkRolesWithStaffingRules() {
		if (areListElementVisible(staffingRulesForWorkRoleInSchedulingRoles, 5)) {
			for (WebElement s : staffingRulesForWorkRoleInSchedulingRoles) {
				if (!s.getText().contains("Add")) {
					click(s);
					break;
				}
			}
		} else
			SimpleUtils.fail("staffing rules link show wrong for each work role", false);

	}

	@Override
	public void validateAdvanceStaffingRuleShowingAtLocationLevel(String startEvent, String startOffsetTime, String startEventPoint, String startTimeUnit,
																  String endEvent, String endOffsetTime, String endEventPoint, String endTimeUnit,
																  List<String> days, String shiftsNumber) throws Exception {
		List<WebElement> staffingRuleText = staffingRulesList.get(staffingRulesList.size() - 1).findElements(By.cssSelector("span[ng-bind-html=\"$ctrl.ruleLabelText\"] span"));
		List<String> daysInRule = Arrays.asList(staffingRuleText.get(2).getText().trim().split(","));
		List<String> newDaysInRule = new ArrayList<>();
		for (String dayInRules : daysInRule) {
			String newDayInRule = dayInRules.trim().toLowerCase();
			newDaysInRule.add(newDayInRule);
		}
		List<String> newDays = new ArrayList<>();
		for (String day : days) {
			String newDay = day.substring(0, 3).toLowerCase();
			newDays.add(newDay);
		}
		Collections.sort(newDaysInRule);
		Collections.sort(newDays);
		if (ListUtils.isEqualList(newDaysInRule, newDays)) {
			SimpleUtils.pass("The days info in rule are correct");
		} else {
			SimpleUtils.fail("The days info in rule are NOT correct", false);
		}
		String shiftsNumberInRule = staffingRuleText.get(1).getText().trim();
		if (shiftsNumberInRule.equals(shiftsNumber)) {
			SimpleUtils.pass("The shifts number info in rule is correct");
		} else {
			SimpleUtils.fail("The shifts number info in rule is NOT correct", false);
		}

		String startTimeInfo = staffingRuleText.get(0).getText().trim().split(",")[0];
		String endTimeInfo = staffingRuleText.get(0).getText().trim().split(",")[1];
		String startEventInRule = "";
		for (int i = 5; i < startTimeInfo.split(" ").length; i++) {
			startEventInRule = startEventInRule + startTimeInfo.split(" ")[i] + " ";
		}
		startEventInRule.trim();
		if (startEventInRule.contains(startEvent)) {
			SimpleUtils.pass("The start event info in rule is correct");
		} else {
			SimpleUtils.fail("The start event info in rule is NOT correct", false);
		}
		String startOffsetTimeInRule = startTimeInfo.split(" ")[2].trim();
		if (startOffsetTimeInRule.equals(startOffsetTime)) {
			SimpleUtils.pass("The start Offset Time info in rule is correct");
		} else {
			SimpleUtils.fail("The start Offset Time info in rule is NOT correct", false);
		}
		String startEventPointInRule = startTimeInfo.split(" ")[4].trim();
		if (startEventPointInRule.equals(startEventPoint)) {
			SimpleUtils.pass("The start Event Point info in rule is correct");
		} else {
			SimpleUtils.fail("The start Event Point info in rule is NOT correct", false);
		}
		String startTimeUnitInRule = startTimeInfo.split(" ")[3].trim();
		if (startTimeUnitInRule.equals(startTimeUnit)) {
			SimpleUtils.pass("The start Time Unit info in rule is correct");
		} else {
			SimpleUtils.fail("The start Time Unit info in rule is NOT correct", false);
		}
		String endEventInRule = "";
		for (int i = 5; i < endTimeInfo.split(" ").length; i++) {
			endEventInRule = endEventInRule + endTimeInfo.split(" ")[i] + " ";
		}
		endEventInRule.trim();
		if (endEventInRule.contains(endEvent)) {
			SimpleUtils.pass("The end Event info in rule is correct");
		} else {
			SimpleUtils.fail("The end Event info in rule is NOT correct", false);
		}
		String endOffsetTimeInRule = endTimeInfo.split(" ")[3].trim();
		if (endOffsetTimeInRule.contains(endOffsetTime)) {
			SimpleUtils.pass("The end Offset Time in rule is correct");
		} else {
			SimpleUtils.fail("The end Offset Time in rule is NOT correct", false);
		}
		String endEventPointInRule = endTimeInfo.split(" ")[5].trim();
		if (endEventPointInRule.contains(endEventPoint)) {
			SimpleUtils.pass("The end Event Point in rule is correct");
		} else {
			SimpleUtils.fail("The end Event Point in rule is NOT correct", false);
		}
		String endTimeUnitInRule = endTimeInfo.split(" ")[4].trim();
		if (endTimeUnitInRule.contains(endTimeUnit)) {
			SimpleUtils.pass("The end Time Unit in rule is correct");
		} else {
			SimpleUtils.fail("The end Time Unit in rule is NOT correct", false);
		}
	}

	@FindBy(css = "lg-search input[placeholder=\"You can search by name and description\"]")
	private WebElement searchAssociateFiled;
	@FindBy(css = "lg-tabs.ng-isolate-scope nav div:nth-child(1)")
	private WebElement templateDetailsBTN;
	@FindBy(css = "lg-tabs.ng-isolate-scope nav div:nth-child(2)")
	private WebElement templateExternalAttributesBTN;
	//	@FindBy(css="lg-tabs.ng-isolate-scope nav div:nth-child(3)")
	@FindBy(css = "nav.lg-tabs__nav>div:nth-last-child(2)")
	private WebElement templateAssociationBTN;
	@FindBy(css = "lg-button[ng-click=\"$ctrl.removeDynamicGroup(group.id,'remove')\"]")
	private WebElement dynamicGroupRemoveBTN;
	@FindBy(css = "div[ng-if*=showAction] lg-button[label=\"Edit\"]")
	private WebElement dynamicGroupEditBTN;
	@FindBy(css = "modal[modal-title=\"Remove Dynamic Location Group\"] lg-button[label=\"Remove\"]")
	private WebElement dynamicGroupRemoveBTNOnDialog;

	//	@FindBy(css="lg-search input[placeholder=\"You can search by name, label and description\"]")
//	private WebElement searchDynamicEmployeeGroupsField;
	@Override
	public void clickOnAssociationTabOnTemplateDetailsPage() throws Exception {
		if (isElementEnabled(templateAssociationBTN, 10)) {
			scrollToElement(templateAssociationBTN);
			clickTheElement(templateAssociationBTN);
			if (isElementEnabled(searchDynamicEmployeeGroupsField, 2)) {
				SimpleUtils.pass("Click Association Tab successfully!");
			} else {
				SimpleUtils.fail("Failed to Click Association Tab!", false);
			}
		}

	}

	@FindBy(css = "table.templateAssociation_table tr[ng-repeat=\"group in filterdynamicGroups\"]")
	private List<WebElement> templateAssociationRows;

	public boolean searchOneDynamicGroup(String dynamicGroupName) throws Exception {
		boolean dataExist = false;
		clickOnAssociationTabOnTemplateDetailsPage();
		waitForSeconds(2);
		if (isElementLoaded(searchDynamicEmployeeGroupsField, 10)) {
			searchDynamicEmployeeGroupsField.clear();
			searchDynamicEmployeeGroupsField.sendKeys(dynamicGroupName);
		}
		waitForSeconds(5);
		if (areListElementVisible(getDriver().findElements(By.cssSelector("[ng-repeat*=\"filterdynamicGroups\"]")), 5)
				&& (getDriver().findElements(By.cssSelector("[ng-repeat*=\"filterdynamicGroups\"]"))).size() > 0) {
			dataExist = true;
			SimpleUtils.pass("User can search out association named: " + dynamicGroupName);
		} else {
			SimpleUtils.report("User can NOT search out association named: " + dynamicGroupName);
		}
		return dataExist;
	}

	@Override
	public void deleteOneDynamicGroup(String dyname) throws Exception {
		if (searchOneDynamicGroup(dyname)) {
			//remove the dynamic group
			waitForSeconds(2);
			clickTheElement(dynamicGroupRemoveBTN);
			waitForSeconds(2);
			clickTheElement(dynamicGroupRemoveBTNOnDialog);
			waitForSeconds(1);
		}
		if (!searchOneDynamicGroup(dyname))
			SimpleUtils.pass("Dynamic group removed successfully");
		else
			SimpleUtils.fail("Dynamic group removed failed", false);

	}

	@FindBy(css="table.templateAssociation_table tr.ng-scope")
	private List<WebElement> dynamicGroupList;
	@FindBy(css="table.templateAssociation_table tr.ng-scope td:nth-child(1)")
	private List<WebElement> dynamicGroupNameList;
	@Override
	public void editADynamicGroup(String dyname) throws Exception {
		if (searchOneDynamicGroup(dyname)) {
			//edit the dynamic group
			clickTheElement(dynamicGroupEditBTN);
			waitForSeconds(2);
			if (isElementLoaded(manageDynamicGroupPopupTitle)) {
				SimpleUtils.pass("The edit dynamic group dialog pop up successfully!");
				//cancel
				clickTheElement(cancelButtonOnManageDynamicGroupPopup);
				waitForSeconds(1);
			} else
				SimpleUtils.fail("The edit dynamic group dialog not pop up!", true);
		}

	}

	@FindBy(css = "lg-tab[tab-title=\"Association\"] lg-button[label=\"Save\"] button")
	private WebElement saveBTNOnAssociationPage;

	@Override
	public void selectOneDynamicGroup(String dynamicGroupName) throws Exception {
		searchOneDynamicGroup(dynamicGroupName);
		for (WebElement templateAssociationRow : templateAssociationRows) {
			String associationName = templateAssociationRow.findElement(By.cssSelector("td:nth-child(2)")).getText().trim();
			if (associationName.equals(dynamicGroupName)) {
				clickTheElement(templateAssociationRow.findElement(By.cssSelector("td:nth-child(1) input")));
				break;
			}
		}
		clickTheElement(saveBTNOnAssociationPage);
		waitForSeconds(10);
	}

	@Override
	public void verifySpecificAssociationIsSaved(String name) throws Exception {
		boolean isSelected = false;
		searchOneDynamicGroup(name);
		if (areListElementVisible(templateAssociationRows, 5) && templateAssociationRows.size() > 0) {
			for (WebElement row : templateAssociationRows) {
				String associationName = row.findElement(By.cssSelector("td:nth-child(2)")).getText().trim();
				if (associationName.equals(name)) {
					WebElement radioBtn = row.findElement(By.cssSelector("[type=\"radio\"]"));
					if (radioBtn.getAttribute("checked").equals("true")) {
						isSelected = true;
						SimpleUtils.pass("Dynamic Group: " + name + " is selected successfully!");
					}
				}
			}
		}
		if (!isSelected) {
			SimpleUtils.fail("Dynamic Group: " + name + " is not selected!", false);
		}
	}

	@FindBy(css = "lg-button[ng-click*=\"addDynamicGroup()\"] button")
	private WebElement addDynamicGroupButton;
	@FindBy(css = "div.lg-modal h1.lg-modal__title div")
	private WebElement manageDynamicGroupPopupTitle;
	@FindBy(css = "input-field[label=\"Group Name\"] input")
	private WebElement dynamicGroupName;
	@FindBy(css = "input-field[label=\"Group Name\"] ng-form")
	private WebElement manageDynamicGroupPopupEditTitle;
	@FindBy(css = "i.fa-exclamation-circle")
	private WebElement dynamicGroupNameRequiredMsg;
	@FindBy(css = "input-field[value=\"$ctrl.dynamicGroup.description\"] input")
	private WebElement dynamicGroupDescription;
	@FindBy(css = "input-field[placeholder=\"Select one\"]")
	private WebElement dynamicGroupCriteria;
	@FindBy(css = "div.lg-search-options .lg-search-options__option.lg-search-options__subLabel")
	private List<WebElement> dynamicGroupCriteriaOptions;
	@FindBy(xpath = "//input-field[contains(@placeholder,'...')]")
	private List<WebElement> dynamicGroupCriteriaValueInputs;
	@FindBy(xpath = "(//input-field[contains(@placeholder,'...')])[2]//ng-form")
	private WebElement dynamicGroupCriteriaValueInput;
	@FindBy(css = ".lg-search-options__option[title='IN']")
	private WebElement dynamicGroupCriteriaINOption;
	@FindBy(css = ".lg-search-options__option[title='NOT IN']")
	private WebElement dynamicGroupCriteriaINotNOption;
	@FindBy(css = "input[placeholder=\"Search\"]")
	private WebElement dynamicGroupCriteriaSearchInput;
	@FindBy(css = "input-field[type=\"checkbox\"] ng-form input")
	private List<WebElement> dynamicGroupCriteriaResults;
	@FindBy(css = "lg-button[label=\"Add More\"]")
	private WebElement dynamicGroupCriteriaAddMoreLink;
	@FindBy(css = "i.deleteRule")
	private List<WebElement> dynamicGroupCriteriaAddDelete;
	@FindBy(css = "lg-button[label=\"Test\"]")
	private WebElement dynamicGroupTestButton;
	@FindBy(css = "span.testInfo")
	private WebElement dynamicGroupTestInfo;
	@FindBy(css = "div.CodeMirror textarea")
	private WebElement formulaTextAreaOfDynamicGroup;
	@FindBy(css = "lg-button[label=\"OK\"] button")
	private WebElement okButtonOnManageDynamicGroupPopup;
	@FindBy(css = "modal[modal-title=\"Manage Dynamic Location Group\"] lg-button[label=\"Cancel\"]")
	private WebElement cancelButtonOnManageDynamicGroupPopup;
	@FindBy(css = "input-field[placeholder=\"Search\"] input")
	private WebElement searchCriteriaOptionInput;

	@Override
	public void createDynamicGroup(String name, String criteria, String formula) throws Exception {
		waitForSeconds(3);
		clickOnAssociationTabOnTemplateDetailsPage();
		if (isElementLoaded(addDynamicGroupButton, 2)) {
			clickTheElement(addDynamicGroupButton);
		}
		if (isElementEnabled(manageDynamicGroupPopupTitle, 5)) {
			SimpleUtils.pass("User click add DynamicGroup button successfully!");
			clickTheElement(dynamicGroupName);
			dynamicGroupName.sendKeys(name);
			waitForSeconds(5);
			//select a criteria type
			clickTheElement(dynamicGroupCriteria);
			waitForSeconds(1);
			String optionLoc = ".lg-search-options__option[title='" + criteria + "']";
			getDriver().findElement(By.cssSelector(optionLoc)).click();
			waitForSeconds(2);
			if (criteria.equals("Custom")) {
				formulaTextAreaOfDynamicGroup.sendKeys(Keys.TAB);
				formulaTextAreaOfDynamicGroup.sendKeys(formula);
			} else {
				//select a criteria value
				//set up value
				dynamicGroupCriteriaValueInput.click();
				waitForSeconds(2);

				if (formula != null && !formula.equals("")) {
					searchCriteriaOptionInput.sendKeys(formula);
					waitForSeconds(3);
					if (dynamicGroupCriteriaResults != null && dynamicGroupCriteriaResults.size() > 0) {
						clickTheElement(dynamicGroupCriteriaResults.get(0));
					}
				} else if (areListElementVisible(dynamicGroupCriteriaResults, 5)) {
					SimpleUtils.pass("The current selected Criteria has value options");
					System.out.println("--- is: " + formula);
					if (dynamicGroupCriteriaValueInput.getAttribute("class").contains("ng-invalid")) {
						searchCriteriaOptionInput.clear();
						clickTheElement(dynamicGroupCriteriaResults.get(0));
						waitForSeconds(3);
					}
				}

			}
			clickTheElement(okButtonOnManageDynamicGroupPopup);
			waitForSeconds(3);
		} else {
			SimpleUtils.fail("User failed to clicking add DynamicGroup button!", false);
		}
	}

	@Override
	public void createTmpAndPublishAndArchive(String tempType, String tempName, String dygpname) throws Exception {
		//create a new template
		publishNewTemplate(tempName, dygpname, "Custom", "AutoCreatedDynamicTodelete---Foremat Script");
		//check if created successfully
		if (searchTemplate(tempName)) {
			archivePublishedOrDeleteDraftTemplate(tempName, "Archive");
		} else
			SimpleUtils.fail("Create and Publish" + tempType + "template failed", true);
	}


	@Override
	public void dynamicGroupDialogUICheck(String name) throws Exception {
		clickOnAssociationTabOnTemplateDetailsPage();
		waitForSeconds(3);
		//check if the dynamic group existing or not
		deleteOneDynamicGroup(name);
		if (isElementLoaded(addDynamicGroupButton, 5)) {
			SimpleUtils.pass("The " + " icon for adding dynamic group button show as expected");
			clickTheElement(addDynamicGroupButton);
			if (manageDynamicGroupPopupTitle.getText().trim().equalsIgnoreCase("Manage Dynamic Location Group")) {
				SimpleUtils.pass("Dynamic group dialog title show as expected");
				//check the group name is required
				if (dynamicGroupName.getAttribute("required").equals("true")) {
					SimpleUtils.pass("Group name is required");
					//input group name
					dynamicGroupName.sendKeys(name);
					//clear group name
					dynamicGroupName.clear();
					//get the required message
					if (isElementLoaded(dynamicGroupNameRequiredMsg) && dynamicGroupNameRequiredMsg.getText().contains("Group Name is required"))
						SimpleUtils.pass("group name is required message displayed if not input");
					dynamicGroupName.sendKeys(name);
					waitForSeconds(2);
					String[] criteriaOps = {"Custom", "District", "Country", "State", "City", "Location Name",
							"Location Id", "Location Type", "UpperField", "Config Type"};
					for (String ss : criteriaOps) {
						//check every criteria options is selectable
						clickTheElement(dynamicGroupCriteria);
						waitForSeconds(4);
						String optionType = ".lg-search-options__option[title='" + ss + "']";
						getDriver().findElement(By.cssSelector(optionType)).click();
						SimpleUtils.pass("The criteria " + ss + " was selected!");
						waitForSeconds(3);
					}
					//set up value
					dynamicGroupCriteriaValueInput.click();
					waitForSeconds(2);
					if (areListElementVisible(dynamicGroupCriteriaResults, 5)) {
						SimpleUtils.pass("The current selected Criteria has value options");
						clickTheElement(dynamicGroupCriteriaResults.get(0));
						waitForSeconds(3);
						//click add more link//click add more link
						clickTheElement(dynamicGroupCriteriaAddMoreLink);
						waitForSeconds(2);
						//Check the delete icon showed
						if (areListElementVisible(dynamicGroupCriteriaAddDelete) && dynamicGroupCriteriaAddDelete.size() > 1) {
							clickTheElement(dynamicGroupCriteriaAddDelete.get(1));
							waitForSeconds(2);
							//select a criteria type
							clickTheElement(dynamicGroupCriteria);
							waitForSeconds(1);
							String optionCountry = ".lg-search-options__option[title='Country']";
							getDriver().findElement(By.cssSelector(optionCountry)).click();
							waitForSeconds(2);
							//set up criteria relationship
							clickTheElement(dynamicGroupCriteriaValueInputs.get(0));
							// check IN and NOTIN options supported
							if (isElementLoaded(dynamicGroupCriteriaINOption) && isElementLoaded(dynamicGroupCriteriaINotNOption))
								SimpleUtils.pass("The IN and NOt IN relation are supported for Criteria relationship.");
							//set up criteria value
							clickTheElement(dynamicGroupCriteriaValueInputs.get(1));
							//choose the last value from drop down
							clickTheElement(dynamicGroupCriteriaValueInputs.get(0));
							clickTheElement(dynamicGroupCriteriaResults.get(dynamicGroupCriteriaResults.size() - 1));
							waitForSeconds(2);
							clickTheElement(dynamicGroupCriteriaResults.get(0));
							waitForSeconds(2);
							//click the test button to chek value
							clickTheElement(dynamicGroupTestButton);
							waitForSeconds(3);
							//get the result
							if (isElementLoaded(dynamicGroupTestInfo)) {
								SimpleUtils.pass("Get results for the dynamic group");
								String mappedRes = dynamicGroupTestInfo.getText().split("Location")[0].trim();
								if (Integer.parseInt(mappedRes) > 0)
									SimpleUtils.pass("Get mapped location for the dynamic group");
							} else
								SimpleUtils.fail("No result get for the dynamic group", true);
							//click save
							clickTheElement(okButtonOnManageDynamicGroupPopup);
							waitForSeconds(3);
						} else
							SimpleUtils.fail("The delete criteria icon is not displayed!", false);
					} else
						SimpleUtils.fail("The current selected Criteria has no options can be selected", true);
				} else
					SimpleUtils.fail("Group name is not required on UI", true);
			} else
				SimpleUtils.fail("Dynamic group dialog title is not show as designed!", true);
		} else
			SimpleUtils.fail("The " + " icon for adding dynamic group missing!", false);
	}


	@Override
	public void publishNewTemplate(String templateName, String dynamicGName, String criteria, String formula) throws Exception {
		LocationSelectorPage locationSelectorPage = new ConsoleLocationSelectorPage();
		if (isTemplateListPageShow()) {
			//check if template existing or not
			if (searchTemplate(templateName)) {
				if (templateDraftStatusList.size() > 0)
					//Delete the temp
					archivePublishedOrDeleteDraftTemplate(templateName, "Delete");
				else
//					archive the temp
					archivePublishedOrDeleteDraftTemplate(templateName, "Archive");
			}
			clickTheElement(newTemplateBTN);
			waitForSeconds(1);
			if (isElementEnabled(createNewTemplatePopupWindow)) {
				SimpleUtils.pass("User can click new template button successfully!");
				clickTheElement(newTemplateName);
				newTemplateName.sendKeys(templateName);
				clickTheElement(newTemplateDescription);
				newTemplateDescription.sendKeys(templateName);
				clickTheElement(continueBTN);
				waitForSeconds(4);
				if (isElementEnabled(welcomeCloseButton, 5)) {
					clickTheElement(welcomeCloseButton);
				}
				//change to association tan
				clickTheElement(templateAssociationBTN);
				waitForSeconds(3);
				if (searchOneDynamicGroup(dynamicGName)) {
					selectOneDynamicGroup(dynamicGName);
				} else {
					createDynamicGroup(dynamicGName, criteria, formula);
					selectOneDynamicGroup(dynamicGName);
				}
				waitForSeconds(4);
//				if(isElementEnabled(taTemplateSpecialField,20)){
//					clickTheElement(taTemplateSpecialField.findElement(By.cssSelector("input")));
//					taTemplateSpecialField.findElement(By.cssSelector("input")).clear();
//					taTemplateSpecialField.findElement(By.cssSelector("input")).sendKeys("5");
//				}
				clickOnTemplateDetailTab();
				scrollToBottom();
				publishNowTemplate();
			} else {
				SimpleUtils.fail("User can't click new template button successfully!", false);
			}
		}
		searchTemplate(templateName);
		String newTemplateName = templateNameList.get(0).getText().trim();
		if (newTemplateName.contains(templateName)) {
			SimpleUtils.pass("User can add new template successfully!");
		} else {
			SimpleUtils.fail("User can't add new template successfully", false);
		}
	}

	//added by Estelle to edit operating hours
	@FindBy(className = "lgn-time-slider-notch-selector")
	private List<WebElement> startEndSliderInBusinessHoursPopUp;
	@FindBy(css = "div[ng-repeat=\"notch in notches\"]")
	private List<WebElement> sliderScaleInBusinessHoursPopUp;

	@Override
	public void moveSliderAtSomePoint(int moveCount, String slideType) throws Exception {
		try {
			int startSelectTime = Integer.parseInt(startEndSliderInBusinessHoursPopUp.get(0).getText().trim().split(":")[0]);
			int endSelectTime = Integer.parseInt(startEndSliderInBusinessHoursPopUp.get(1).getText().trim().split(":")[0]) + 12;
			String startSliderTimeText = sliderScaleInBusinessHoursPopUp.get(0).getText().trim();
			int startSliderTime = Integer.parseInt(startSliderTimeText.split("\n")[0]);
			;

			if (startSliderTime == 12) {
				startSliderTime = 0;
			} else
				startSliderTime = startSliderTime;

			String endSliderTimeText = sliderScaleInBusinessHoursPopUp.get(96).getText().trim();
			int endSliderTime = Integer.parseInt(endSliderTimeText.split("\n")[0]);
			if (endSliderTimeText.contains("AM") && endSliderTime > 11) {
				endSliderTime = endSliderTime + 12;
			} else
				endSliderTime = endSliderTime;


			if (slideType.equalsIgnoreCase("End")) {
				if (isElementLoaded(startEndSliderInBusinessHoursPopUp.get(1), 5) && endSelectTime < endSliderTime) {
					SimpleUtils.pass("Business hours with Sliders loaded on page Successfully for End Point");
					if (endSelectTime < endSliderTime) {
						for (int i = endSelectTime * 4 + moveCount; i < sliderScaleInBusinessHoursPopUp.size(); i++) {
							WebElement element = getDriver().findElement(By.cssSelector("div.lgn-time-slider-notch.droppable:nth-child(" + (i + 2) + ")"));
							mouseHoverDragandDrop(startEndSliderInBusinessHoursPopUp.get(1), element);
							break;
						}
					} else if (endSelectTime == endSliderTime) {
						for (int i = endSelectTime * 4 - moveCount; i > sliderScaleInBusinessHoursPopUp.size(); i++) {
							WebElement element = getDriver().findElement(By.cssSelector("div.lgn-time-slider-notch.droppable:nth-child(" + (i + 2) + ")"));
							mouseHoverDragandDrop(startEndSliderInBusinessHoursPopUp.get(1), element);
							break;
						}
					}


				} else {
					SimpleUtils.fail("Business hours with End Sliders load failed", false);
				}
			} else if (slideType.equalsIgnoreCase("Start")) {
				if (isElementLoaded(startEndSliderInBusinessHoursPopUp.get(0), 10) && startEndSliderInBusinessHoursPopUp.size() > 0) {
					SimpleUtils.pass("Business hours with Sliders loaded on page Successfully for Starting point");
					if (startSelectTime > endSliderTime) {
						for (int i = endSelectTime * 4 - moveCount; i < sliderScaleInBusinessHoursPopUp.size(); i++) {
							WebElement element = getDriver().findElement(By.cssSelector("div.lgn-time-slider-notch.droppable:nth-child(" + (i + 2) + ")"));
							mouseHoverDragandDrop(startEndSliderInBusinessHoursPopUp.get(1), element);
							break;
						}
					} else if (startSelectTime == endSliderTime) {
						for (int i = endSelectTime * 4 + moveCount; i > sliderScaleInBusinessHoursPopUp.size(); i++) {
							WebElement element = getDriver().findElement(By.cssSelector("div.lgn-time-slider-notch.droppable:nth-child(" + (i + 2) + ")"));
							mouseHoverDragandDrop(startEndSliderInBusinessHoursPopUp.get(1), element);
							break;
						}
					}
				} else {
					SimpleUtils.fail("Business hours with Start Sliders load failed", false);
				}
			}
		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}

	}

	@Override
	public void saveADraftTemplate() throws Exception {
		//click Details to back detail tab
		clickTheElement(templateDetailsBTN);
		waitForSeconds(3);
		if (isElementEnabled(saveAsDraftButton)) {
			SimpleUtils.pass("User can click save as draft button!");
			clickTheElement(saveAsDraftButton);
			waitForSeconds(3);
		} else
			SimpleUtils.fail("Not stayed at template detail page!", false);

	}

	@Override
	public void archivePublishedOrDeleteDraftTemplate(String templateName, String action) throws Exception {

		if (templatesList.size() > 0) {
			SimpleUtils.report("There are :" + templatesList.size() + " found");

			//expand published template with draft version
			if (isItMultipVersion()) {
				expandTemplate();
			}
			clickTheElement(templateNameList.get(0));
			waitForSeconds(5);
			List<WebElement> deleteArchiveBtn = getDriver().findElements(By.xpath("//button/span/span"));
			for (WebElement e : deleteArchiveBtn) {
				if (e.getText().contains(action)) {
					click(e);
					break;
				}
			}

			//verify deleting / archive pop up
			if (isElementEnabled(archiveTemplateDialog, 3) || isElementLoaded(deleteTemplateDialog, 3)) {
				clickTheElement(okButton);
				waitForSeconds(5);

				searchTemplate(templateName);
				if (templateNameList.size() == 0) {
					SimpleUtils.pass("User has " + action + "  template successfully!");
				} else {
					SimpleUtils.fail("User has " + action + "  template failed!", false);
				}
			} else
				SimpleUtils.fail(action + " template dialog pop up window load failed.", false);
		} else
			SimpleUtils.fail("There are no template that match your criteria", false);
	}

	private boolean isItMultipVersion() {
		String classValue = templatesList.get(0).getAttribute("class");
		if (classValue != null && classValue.contains("hasChildren")) {
			return true;
		} else
			return false;
	}

	public void archiveOrDeleteTemplate(String templateName) throws Exception {
		if (areListElementVisible(templateNameList, 20)
				&& areListElementVisible(templatesList, 20)
				&& templateNameList.size() == templatesList.size()
				&& templateNameList.size() > 0) {
			for (int i = 0; i < templateNameList.size(); i++) {
				if (templateNameList.get(i).getText().equalsIgnoreCase(templateName)) {
					String classValue = templatesList.get(i).getAttribute("class");
					if (classValue != null && classValue.contains("hasChildren")) {
						//expand the template.
						clickTheElement(templateToggleButton);
						waitForSeconds(3);
						clickTheElement(templateNameList.get(i));
						if (isElementLoaded(templateDetailsBTN, 20)) {
							SimpleUtils.pass("Go to template detail page successfully! ");
						} else
							SimpleUtils.fail("Go to template detail page fail! ", false);
						if (isElementLoaded(archiveBtn, 10)) {
							clickTheElement(archiveBtn);
							if (isElementEnabled(archiveTemplateDialog, 10)) {
								clickTheElement(okButton);
								displaySuccessMessage();
							} else
								SimpleUtils.fail("Archive template dialog pop up window load failed.", false);
						} else if (isElementLoaded(deleteTemplateButton, 10)) {
							clickTheElement(deleteTemplateButton);
							if (isElementEnabled(deleteTemplateDialog, 10)) {
								clickTheElement(okButton);
								displaySuccessMessage();
							} else {
								SimpleUtils.fail("Delete template dialog pop up window load failed.", false);
							}
						} else {
							SimpleUtils.fail("Archive and delete button fail to load! ", false);
						}
						clickTheElement(templateNameList.get(i));
						if (isElementLoaded(templateDetailsBTN, 20)) {
							SimpleUtils.pass("Go to template detail page successfully! ");
						} else {
							SimpleUtils.fail("Go to template detail page fail! ", false);
						}
					} else {
						clickTheElement(templateNameList.get(i));
						if (isElementLoaded(templateDetailsBTN, 20)) {
							SimpleUtils.pass("Go to template detail page successfully! ");
						} else
							SimpleUtils.fail("Go to template detail page fail! ", false);
					}
					//delete the draft version.
					if (isElementLoaded(archiveBtn, 10)) {
						clickTheElement(archiveBtn);
						if (isElementEnabled(archiveTemplateDialog, 10)) {
							clickTheElement(okButton);
							displaySuccessMessage();
						} else
							SimpleUtils.fail("Archive template dialog pop up window load failed.", false);
					} else if (isElementLoaded(deleteTemplateButton, 10)) {
						clickTheElement(deleteTemplateButton);
						if (isElementEnabled(deleteTemplateDialog, 10)) {
							clickTheElement(okButton);
							displaySuccessMessage();
						} else
							SimpleUtils.fail("Delete template dialog pop up window load failed.", false);
					} else
						SimpleUtils.fail("Archive and delete button fail to load! ", false);
					break;
				}
			}

		} else
			SimpleUtils.warn("There is no template in the list! ");
	}

	@FindBy(css = "question-input[question-title=\"Move existing shifts to Open when transfers occur within the Workforce Sharing Group.\"] > div > div.lg-question-input__wrapper > ng-transclude > yes-no > ng-form > lg-button-group >div>div")
	private List<WebElement> yesNoForMoveExistingShiftsToOpenWhenTransfer;

	@Override
	public void setMoveExistingShiftWhenTransfer(String yesOrNo) throws Exception {
		if (areListElementVisible(yesNoForMoveExistingShiftsToOpenWhenTransfer, 5)) {
			for (WebElement option : yesNoForMoveExistingShiftsToOpenWhenTransfer) {
				if (option.getText().equalsIgnoreCase(yesOrNo)) {
					click(option);
					break;
				}
			}
			SimpleUtils.pass("Set 'Move existing shifts to Open when transfers occur within the Workforce Sharing Group' to " + yesOrNo + " successfully! ");
		} else
			SimpleUtils.fail("Set 'Move existing shifts to Open when transfers occur within the Workforce Sharing Group' setting fail to load!  ", false);
	}

	@Override
	public boolean isMoveExistingShiftWhenTransferSettingEnabled() throws Exception {
		boolean isMoveExistingShiftWhenTransferSettingEnabled = false;
		if (areListElementVisible(yesNoForMoveExistingShiftsToOpenWhenTransfer, 5)) {
			if (yesNoForMoveExistingShiftsToOpenWhenTransfer.get(0).getAttribute("class").contains("selected")) {
				isMoveExistingShiftWhenTransferSettingEnabled = true;
			}
		}
		return isMoveExistingShiftWhenTransferSettingEnabled;
	}

	@Override
	public void deleteTemplate(String templateName) throws Exception {
		clearSearchTemplateBox();
		if (isTemplateListPageShow()) {
			searchTemplate(templateName);
			for (int i = 0; i < templateNameList.size(); i++) {
				if (templateNameList.get(i).getText() != null && templateNameList.get(i).getText().trim().equals(templateName)) {
					String classValue = templatesList.get(i).getAttribute("class");
					if (classValue != null && classValue.contains("hasChildren")) {
						clickTheElement(templatesList.get(i).findElement(By.className("toggle")));
						waitForSeconds(3);
						clickTheElement(templateNameList.get(i));
						waitForSeconds(15);
						if (isElementEnabled(templateTitleOnDetailsPage) && isElementEnabled(closeBTN) && isElementEnabled(templateDetailsAssociateTab)
								&& isElementEnabled(templateDetailsPageForm)) {
							SimpleUtils.pass("User can open " + templateName + " template succseefully");
						} else {
							SimpleUtils.fail("User open " + templateName + " template failed", false);
						}
					} else {
						clickTheElement(templatesList.get(i).findElement(By.cssSelector("button")));
						waitForSeconds(15);
						if (isElementEnabled(templateTitleOnDetailsPage) && isElementEnabled(closeBTN) && isElementEnabled(templateDetailsAssociateTab)
								&& isElementEnabled(templateDetailsPageForm)) {
							SimpleUtils.pass("User can open " + templateName + " template succseefully");
						} else {
							SimpleUtils.fail("User open " + templateName + " template failed", false);
						}
					}
				} else if (i == templateNameList.size() - 1) {
					SimpleUtils.fail("Can't find the specify template", false);
				}

				if (isElementEnabled(deleteTemplateButton, 3)) {
					clickTheElement(deleteTemplateButton);
					if (isElementEnabled(deleteTemplateDialog, 3)) {
						clickTheElement(okButtonOnDeleteTemplateDialog);
						waitForSeconds(5);
						String firstTemplateName = templateNameList.get(0).getText().trim();
						if (!firstTemplateName.equals(templateName)) {
							SimpleUtils.pass("User has deleted new created template successfully!");
							break;
						} else {
							SimpleUtils.fail("User failed to delete new created template!", false);
						}
					}
				} else {
					SimpleUtils.fail("Clicking the template failed.", false);
				}
			}
		} else {
			SimpleUtils.fail("There is No template now", false);
		}
	}

	@Override
	public void clearSearchTemplateBox() throws Exception {
		if (isElementEnabled(searchTemplateInputBox, 5)) {
			clickTheElement(searchTemplateInputBox);
			searchTemplateInputBox.clear();
			waitForSeconds(2);
		}
	}


	@FindBy(css = "textarea[placeholder=\"http://...\"]")
	private WebElement companyMobilePolicyURL;

	@Override
	public boolean hasCompanyMobilePolicyURLOrNotOnOP() throws Exception {
		boolean hasCompanyMobilePolicyURL = false;
		waitForSeconds(10);
		if (isElementLoaded(companyMobilePolicyURL, 5)) {
			String url = companyMobilePolicyURL.getAttribute("value");
			if (!url.equals("") && !url.equals("http://...")) {
				hasCompanyMobilePolicyURL = true;
			} else
				SimpleUtils.report("The company mobile policy URL is empty");
		} else
			SimpleUtils.fail("The company mobile policy fail to load! ", false);
		setCompanyPolicy(hasCompanyMobilePolicyURL);
		return hasCompanyMobilePolicyURL;
	}

	@FindBy(css = "lg-global-dynamic-group-table[dynamic-groups=\"newsFeedDg\"]")
	private WebElement dynamicEmployeeGroup;
	@FindBy(css = "[title=\"Dynamic Employee Groups\"] .lg-dashboard-card")
	private WebElement dynamicEmployeeGroupSection;
	@Override
	public void goToDynamicEmployeeGroupPage() {
		if (isElementEnabled(dynamicEmployeeGroupSection, 20)) {
			clickTheElement(dynamicEmployeeGroupSection);
			if (isElementEnabled(dynamicEmployeeGroup, 20)) {
				SimpleUtils.pass("Can go to dynamic group page successfully");
			} else
				SimpleUtils.fail("Go to dynamic group page failed", false);
		} else
			SimpleUtils.fail("The dynamic group section fail to load! ", false);
	}

	@FindBy(css = "[ng-click=\"$ctrl.removeDynamicGroup(group.id,'remove')\"]")
	private List<WebElement> deleteIconsDynamicEmployeeGroupList;

	@FindBy(css = "[ng-repeat=\"group in filterdynamicGroups\"]")
	private List<WebElement> groupRowsInDynamicEmployeeGroupList;

	@FindBy(css = "lg-button[label=\"Remove\"]")
	private WebElement removeBtnInRemoveDGPopup;

	@FindBy(css = "lg-global-dynamic-group-table lg-search input")
	private WebElement searchDynamicEmployeeGroupsField;

	@Override
	public void deleteAllDynamicEmployeeGroupsInList() throws Exception {
		OpsPortalLocationsPage opsPortalLocationsPage = new OpsPortalLocationsPage();
		if (areListElementVisible(groupRowsInDynamicEmployeeGroupList, 20) && groupRowsInDynamicEmployeeGroupList.size() > 0) {
			if (areListElementVisible(deleteIconsDynamicEmployeeGroupList, 30)) {
				int i = 0;
				while (deleteIconsDynamicEmployeeGroupList.size() > 0 && i < 50) {
					click(deleteIconsDynamicEmployeeGroupList.get(0));
					if (opsPortalLocationsPage.isRemoveDynamicGroupPopUpShowing()) {
						click(removeBtnInRemoveDGPopup);
						displaySuccessMessage();
					} else
						SimpleUtils.fail("loRemove dynamic group page load failed ", false);
				}
			} else
				SimpleUtils.report("There is not dynamic group yet");
		} else
			SimpleUtils.report("There is no groups which selected");


	}


	@Override
	public void deleteSpecifyDynamicEmployeeGroupsInList(String groupName) throws Exception {
		OpsPortalLocationsPage opsPortalLocationsPage = new OpsPortalLocationsPage();
		if (areListElementVisible(groupRowsInDynamicEmployeeGroupList, 20) && groupRowsInDynamicEmployeeGroupList.size() > 0) {
			if (isElementLoaded(searchDynamicEmployeeGroupsField, 5)) {
				searchDynamicEmployeeGroupsField.clear();
				searchDynamicEmployeeGroupsField.sendKeys(groupName);
				int i = 0;
				while (deleteIconsDynamicEmployeeGroupList.size() > 0 && i < 50) {
					click(deleteIconsDynamicEmployeeGroupList.get(0));
					if (opsPortalLocationsPage.isRemoveDynamicEmployeeGroupPopUpShowing()) {
						click(removeBtnInRemoveDGPopup);
						displaySuccessMessage();
					} else
						SimpleUtils.fail("loRemove dynamic group page load failed ", false);
					searchDynamicEmployeeGroupsField.clear();
					searchDynamicEmployeeGroupsField.sendKeys(groupName);
				}
			} else
				SimpleUtils.fail("Search Dynamic Employee Groups Field fail to load! ", false);
		} else
			SimpleUtils.report("There is no groups in group list! ");
	}

	@FindBy(css = "lg-button[icon=\"'img/legion/add.png'\"]")
	private WebElement addDynamicGroupBtn;
	@FindBy(css = "[label=\"Labels\"] .lg-picker-input ng-form [placeholder=\"Select...\"]")
	private WebElement labelsSelector;
	@FindBy(css = ".item.ng-scope")
	private List<WebElement> labelsItems;
	@FindBy(css = "[class=\"input-form ng-pristine ng-invalid ng-invalid-required ng-valid-pattern ng-valid-maxlength\"] input[placeholder=\"Select...\"]")
	private List<WebElement> subCriteriaSelector;
	@FindBy(css = ".select-list-item")
	private List<WebElement> subCriteriaSelectorItems;
	@FindBy(css = "input[placeholder=\"Search Label\"]")
	private WebElement searchLabelBox;
	@FindBy(css = "div.new-label")
	private WebElement newLabel;
	@FindBy(css = ".lg-search-options__option")
	private List<WebElement> criteriaSelectorItems;
	@FindBy(css = "[class=\"lg-picker-input__wrapper lg-ng-animate\"]")
	private WebElement pickerPopup;
	@FindBy(css = "input[placeholder=\"Search\"]")
	private WebElement searchInput;

	@Override
	public void createNewDynamicEmployeeGroup(String groupTitle, String description, String groupLabels, List<String> groupCriteria) throws Exception {
		if (isElementLoaded(addDynamicGroupBtn, 15)) {
			clickTheElement(addDynamicGroupBtn);
			if (isManagerDGpopShowWell()) {
				//Send the group title
				groupNameInput.sendKeys(groupTitle);
				//Send the group description
				groupDescriptionInput.sendKeys(description);
				//Select the label
				clickTheElement(labelsSelector);
				waitForSeconds(3);
				if (isElementLoaded(searchLabelBox, 10)) {
					searchLabelBox.clear();
					searchLabelBox.sendKeys(groupLabels);
				} else
					SimpleUtils.fail("Search label box fail to load! ", false);

				if (isElementLoaded(newLabel, 5)) {
					clickTheElement(newLabel);
				} else {
					for (WebElement item : labelsItems) {
						if (item.getText().equalsIgnoreCase(groupLabels)) {
							clickTheElement(item.findElement(By.tagName("input")));
							break;
						}
					}
				}
				//To close the label pick popup
				if (isElementLoaded(pickerPopup, 5)) {
					clickTheElement(newLabel);
				}

				//Add more criteria if criteria more than 1
				if (groupCriteria.size() > 1) {
					for (int i = 0; i < groupCriteria.size() - 2; i++) {
						clickTheElement(addMoreBtn);
					}
				}
				waitForSeconds(3);
				//Select criteria and sub-criteria
				if (criteriaSelectors.size() == groupCriteria.size()) {
					for (int i = 0; i < groupCriteria.size(); i++) {
						String criteria = "";
						String subCriteria = "";
						if (groupCriteria.get(i).contains("&")) {
							criteria = groupCriteria.get(i).split("&")[0];
							subCriteria = groupCriteria.get(i).split("&")[1];
						} else if (groupCriteria.get(i).contains("-")) {
							criteria = groupCriteria.get(i).split("-")[0];
							subCriteria = groupCriteria.get(i).split("-")[1];
						}
						//Select criteria
						clickTheElement(criteriaSelectors.get(i));
						if (areListElementVisible(criteriaSelectorItems, 15)) {
							for (WebElement item : criteriaSelectorItems) {
								if (item.getText().equalsIgnoreCase(criteria)) {
									clickTheElement(item);
									break;
								}
							}
						} else
							SimpleUtils.fail("Criteria selector items fail to load! ", false);
//						selectByVisibleText(criteriaSelectors.get(i), criteria);
						waitForSeconds(3);
						//Select sub-criteria
						clickTheElement(subCriteriaSelector.get(i));
						/*if (isElementLoaded(searchInput, 5)) {
							searchInput.sendKeys(subCriteria);
							waitForSeconds(1);
							subCriteriaSelectorItems = getDriver().findElements(By.cssSelector(".select-list-item"));
						}*/
						for (WebElement item : subCriteriaSelectorItems) {
							if (item.getText().equalsIgnoreCase(subCriteria)) {
								clickTheElement(item.findElement(By.tagName("input")));
								break;
							} else {
								SimpleUtils.report("Expected: " + subCriteria + ", actual is: " + item.getText() + ".");
							}
						}
					}
				} else
					SimpleUtils.fail("Criteria selector fail to load! ", false);

				//Click on OK button
				clickTheElement(okButton);
				waitForSeconds(15);
				displaySuccessMessage();
				if (isManagerDGpopShowWell()) {
					SimpleUtils.fail("Fail to save the Dynamic Employee Group! ", false);
				}
			} else
				SimpleUtils.fail("Manage Dynamic Group window load failed", false);
		} else
			SimpleUtils.fail("Add dynamic group button load failed", false);
	}


	@FindBy(css = "modal[modal-title=\"Manage Dynamic Employee Group\"]>div")
	private WebElement managerDGpop;
	@FindBy(css = "input[aria-label=\"Group Name\"]")
	private WebElement groupNameInput;
	@FindBy(css = "input-field[value=\"$ctrl.dynamicGroup.description\"] >ng-form>input")
	private WebElement groupDescriptionInput;

	@FindBy(css = "input[placeholder=\"Select one\"]")
	private List<WebElement> criteriaSelectors;

	@FindBy(css = "lg-button[label=\"Test\"]")
	private WebElement testBtn;

	@FindBy(css = "lg-button[label=\"Add More\"]")
	private WebElement addMoreBtn;

	private boolean isManagerDGpopShowWell() throws Exception {
		if (isElementEnabled(managerDGpop, 15) && isElementEnabled(groupNameInput, 15) &&
				isElementEnabled(groupDescriptionInput, 15)
				&& isElementLoaded(labelsSelector, 15)
				&& areListElementVisible(criteriaSelectors, 15)
				&& isElementEnabled(testBtn, 15) && isElementEnabled(addMoreBtn, 15)) {
			SimpleUtils.pass("Manager Dynamic Group win show well");
			return true;
		} else
			return false;
	}

	private void expandTemplate() {
		clickTheElement(templatesList.get(0).findElement(By.className("toggle")));
		waitForSeconds(3);
	}

	@Override
	public void archiveOrDeleteAllTemplates() throws Exception {
		if (isElementLoaded(newTemplateBTN, 15) && isElementLoaded(searchTemplateInputBox, 10)) {
			SimpleUtils.pass("Labor model template list is showing now");
			if (areListElementVisible(templateNameList, 20) && templatesList.size() > 0) {
				int j = 0;
				while (templateNameList.size() > 0 && j < 10) {
					String templateName = templateNameList.get(0).getText();
					archiveOrDeleteTemplate(templateName);
					j++;
				}

			} else
				SimpleUtils.report("There are no template in the list");
		} else {
			SimpleUtils.fail("Template list page is not loaded well", false);
		}
	}

	@Override
	public void clickOnTemplateDetailTab() throws Exception {
		if (isElementLoaded(templateDetailsBTN, 10)) {
			clickTheElement(templateDetailsBTN);
			waitForSeconds(3);
		} else
			SimpleUtils.fail("The template detail tab fail to load! ", false);
	}

	//added by Estelle for archive template part

	@FindBy(css = "lg-button[label=\"Archive\"]")
	private WebElement archiveBtn;

	@Override
	public void archiveIsClickable() throws Exception {
		if (isElementLoaded(archiveBtn, 3)) {
			SimpleUtils.pass("Archive button show well in publish template");
		} else
			SimpleUtils.fail("Archive button load failed ", false);
	}

	@Override
	public void verifyArchivePopUpShowWellOrNot() throws Exception {
		click(archiveBtn);
		waitForSeconds(3);
		if (isElementLoaded(archiveTemplateDialog, 3) && isElementLoaded(okButton, 3) && isElementLoaded(cancelButton, 3)) {
			SimpleUtils.pass("Archive Template dialog load well");
		} else
			SimpleUtils.fail("Archive Template dialog load failed", false);
	}

	@Override
	public void cancelArchiveDeleteWorkWell(String templateName) throws Exception {
		click(cancelButton);
		click(backButton);
		searchTemplate(templateName);
		if (templateNameList.size() > 0) {
			SimpleUtils.pass("Cancel archive template successfully");
		} else
			SimpleUtils.fail("Published template was archived", false);
	}

	@Override
	public void clickOnBackBtnOnTheTemplateDetailAndListPage() throws Exception {
		if (isElementLoaded(backButton, 10)) {
			clickTheElement(backButton);
			SimpleUtils.pass("Back button is clicked!");
		} else {
			SimpleUtils.fail("Back button fail to load!", false);
		}
	}

	@FindBy(css = "lg-button[label=\"Edit\"]")
	private WebElement editBtn;

	@Override
	public void clickEdit() throws Exception {
		if (isElementLoaded(editBtn, 3)) {
			click(editBtn);
			SimpleUtils.pass("Edit button is clicked");
		} else
			SimpleUtils.fail("Edit button load failed ", false);
	}

	@FindBy(css = "lg-button[label=\"OK\"]")
	private WebElement OKBtn;

	@Override
	public void clickOK() throws Exception {
		if (isElementLoaded(OKBtn, 3)) {
			click(OKBtn);
			SimpleUtils.pass("OK button is clicked");
		} else
			SimpleUtils.fail("OKBtn button load failed ", false);
	}

	@FindBy(css = "form-section[form-title = 'Time Off']")
	private WebElement timeOffText;
	@FindBy(css = "div.lg-question-input__wrapper h3")
	private WebElement maxNumEmployeesText;

	public void verifyTimeOff() throws Exception {
		scrollToElement(timeOffText);
		if (isElementLoaded(timeOffText, 5) && isElementLoaded(maxNumEmployeesText, 5)) {
			SimpleUtils.pass("Time off loaded successfully");
			if (timeOffText.getText().equals("Time Off") && maxNumEmployeesText.getText().equals("Max number employees can request time off on the same day.")) {
				SimpleUtils.pass("Time off text is correct");
			}
		} else {
			SimpleUtils.fail("Time off loaded failed", false);
		}
	}

	@FindBy(xpath = "//lg-policies-form-template-details/form-section[5]/ng-transclude/content-box/ng-transclude/div/lg-property-meta-field/div/div/question-input/div/div[1]/ng-transclude/input-field/ng-form/input")
	private WebElement maxNumEmployeesInput;

	public void verifymaxNumEmployeesInput(String num) throws Exception {
		if (isElementLoaded(maxNumEmployeesInput, 5)) {
			maxNumEmployeesInput.clear();
			maxNumEmployeesInput.sendKeys(num);
			if (!num.contains("-") && !num.contains(".")) {
				if (isClickable(saveAsDraftButton, 5)) {
					SimpleUtils.pass("Positive integer is valid");
				} else {
					SimpleUtils.fail("Positive integer should be valid", false);
				}
			} else {
				if (!isClickable(saveAsDraftButton, 5)) {
					SimpleUtils.pass("Negative interger or decimal is invalid");
				} else {
					SimpleUtils.fail("Negative interger or decimal should be invalid", false);
				}
			}
		} else {
			SimpleUtils.fail("maxNumEmployeesInput loaded failed", false);
		}
	}

	public void switchToControlWindow() throws Exception {
		TestBase.switchToNewWindow();
	}

	//Added by Fiona
	@FindBy(tagName = "lg-eg-status")
	private List<WebElement> allTemplateStatus;
	@FindBy(css = "lg-eg-status[type=\"Published\"]")
	private List<WebElement> publishedTemplateStatus;

	private boolean isMultiplePublishVersion() {
		String classValue = templatesList.get(0).getAttribute("class");
		if (classValue != null && classValue.contains("hasChildren")) {
			expandTemplate();
			if (areListElementVisible(allTemplateStatus, 3) && publishedTemplateStatus.size() >= 2) {
				SimpleUtils.pass("This is a multiple version template");
			}
			return true;
		} else
			return false;
	}

	@FindBy(css = ".lg-templates-table-improved__grid-row--header~div")
	private List<WebElement> multipleTemplateList;

	@Override
	public void verifyMultipleTemplateListUI(String templateName) throws Exception {
		if (searchTemplate(templateName)) {
			if (isMultiplePublishVersion()) {
				//expand all future template that has draft template
				if (areListElementVisible(multipleTemplateList, 2)) {
					List<String> effectiveDates = new ArrayList<>();

					//expand template with multiple version
					for (WebElement multipleTemplate : multipleTemplateList) {
						WebElement toggleBTN = multipleTemplate.findElement(By.cssSelector(".toggle i"));
						if (toggleBTN.getAttribute("class").trim().equals("fa fa-caret-right")) {
							clickTheElement(toggleBTN);
						}
					}
					//Check each template can show well or not
					for (WebElement multipleTemplate1 : multipleTemplateList) {
						WebElement name = multipleTemplate1.findElement(By.cssSelector("button"));
						WebElement status = multipleTemplate1.findElement(By.cssSelector("lg-eg-status"));
						WebElement creator = multipleTemplate1.findElement(By.cssSelector(".creator"));
						WebElement effectiveDate = multipleTemplate1.findElement(By.cssSelector(".date"));
						WebElement lastModifiedDate = multipleTemplate1.findElement(By.cssSelector(".date+div"));
						if (isElementLoaded(name, 2) && isElementLoaded(status, 2)
								&& isElementLoaded(creator, 2) && isElementLoaded(effectiveDate, 2)
								&& isElementLoaded(lastModifiedDate, 2)) {
							SimpleUtils.pass("Template can show well in template list page");
						} else {
							SimpleUtils.fail("Template can't show well in template list page", false);
						}
						//get all effectiveDate
						if (effectiveDate.getText().trim() != null && effectiveDate.getText().trim() != "" && !effectiveDate.getText().trim().isEmpty()) {
							effectiveDates.add(effectiveDate.getText().trim());
						}
					}
					List<Date> dates = new ArrayList<Date>();
					List<Date> dates1 = new ArrayList<Date>();
					//Verify the effective date should be order by Ascending
					//format the date and add to list
					SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH);
					for (String effectiveDate : effectiveDates) {
						Date date = sdf.parse(effectiveDate);
						dates.add(date);
						dates1.add(date);
					}
					Collections.sort(dates1);

					if (ListUtils.isEqualList(dates, dates1)) {
						SimpleUtils.pass("The date is ordered by ascending");
					} else {
						SimpleUtils.fail("The date is not ordered by ascending", false);
					}
				}
			}
		}
	}


	@FindBy(css = "modal[modal-title=\"Date of Publish\"]")
	private WebElement dateOfPublishPopup;
//	@FindBy(css = "input-field[placeholder=\"Select...\"]")
    @FindBy(css = "input-field input")
	private WebElement effectiveDate;
	@FindBy(css = "lg-button[label=\"OK\"] button")
	private WebElement okButtonOnFuturePublishConfirmDialog;

	private void setEffectiveDate(int effectiveDate) throws Exception {
		selectDateInTemplate(effectiveDate);
	}

	@Override
	public void publishAtDifferentTimeTemplate(String templateName, String dynamicGName, String criteria, String formula, String button, int date) throws Exception {
		waitForSeconds(2);
		if (isElementLoaded(newTemplateBTN, 2)) {
			clickTheElement(newTemplateBTN);
			waitForSeconds(1);
			if (isElementEnabled(createNewTemplatePopupWindow)) {
				SimpleUtils.pass("User can click new template button successfully!");
				clickTheElement(newTemplateName);
				newTemplateName.sendKeys(templateName);
				clickTheElement(newTemplateDescription);
				newTemplateDescription.sendKeys(templateName);
				clickTheElement(continueBTN);
				waitForSeconds(4);
				if (isElementEnabled(welcomeCloseButton, 5)) {
					clickTheElement(welcomeCloseButton);
				}
				//change to association tan
				clickTheElement(templateAssociationBTN);
				waitForSeconds(3);
				if (searchOneDynamicGroup(dynamicGName)) {
					selectOneDynamicGroup(dynamicGName);
				} else {
					createDynamicGroup(dynamicGName, criteria, formula);
					selectOneDynamicGroup(dynamicGName);
				}
				waitForSeconds(4);
				if (isElementEnabled(taTemplateSpecialField, 20)) {
					clickTheElement(taTemplateSpecialField.findElement(By.cssSelector("input")));
					taTemplateSpecialField.findElement(By.cssSelector("input")).clear();
					taTemplateSpecialField.findElement(By.cssSelector("input")).sendKeys("5");
				}
				clickOnTemplateDetailTab();
				chooseSaveOrPublishBtnAndClickOnTheBtn(button);
				if (isElementLoaded(dateOfPublishPopup, 2)) {
					clickTheElement(effectiveDate);
					setEffectiveDate(date);
					clickTheElement(okButtonOnFuturePublishConfirmDialog);
				} else {
					SimpleUtils.fail("The future publish template confirm dialog is not displayed.", false);
				}
			} else {
				SimpleUtils.fail("User can't click new template button successfully!", false);
			}
		}
		searchTemplate(templateName);
		String newTemplateName = templateNameList.get(0).getText().trim();
		if (newTemplateName.contains(templateName)) {
			SimpleUtils.pass("User can add new template successfully!");
		} else {
			SimpleUtils.fail("User can't add new template successfully", false);
		}
	}

	public int getPublishedTemplateCountInMultipleVersion() {
		int count = 0;
		if (areListElementVisible(templatesList, 2)) {
			count = publishedTemplateStatus.size();
		}
		return count;
	}

	public int getAllTemplateCountInMultipleVersion() {
		int count = 0;
		if (areListElementVisible(templatesList, 2)) {
			count = allTemplateStatus.size();
		}
		return count;
	}

	@Override
	public void createFutureTemplateBasedOnExistingTemplate(String templateName, String button, int date, String editOrViewMode) throws Exception {
		int beforeCount = 0;
		int afterCount = 0;
		waitForSeconds(2);
		//create future published version template
		if (areListElementVisible(templateNameList, 3)) {
			beforeCount = publishedTemplateStatus.size();
			clickOnSpecifyTemplateName(templateName, editOrViewMode);
			clickOnEditButtonOnTemplateDetailsPage();
			scrollToBottom();
			chooseSaveOrPublishBtnAndClickOnTheBtn(button);
			if (isElementLoaded(dateOfPublishPopup, 2)) {
				clickTheElement(effectiveDate);
				setEffectiveDate(date);
				clickTheElement(okButtonOnFuturePublishConfirmDialog);
			} else {
				SimpleUtils.fail("The future publish template confirm dialog is not displayed.", false);
			}
		} else {
			SimpleUtils.fail("Template list is not displayed!", false);
		}
		//Check whether the future template is created successfully or not?
		expandMultipleVersionTemplate(templateName);
		afterCount = publishedTemplateStatus.size();

		if (afterCount - beforeCount == 1) {
			SimpleUtils.pass("User create new future template successfully!");
		} else {
			SimpleUtils.fail("User failed to create new future template!", false);
		}
	}

	@Override
	public void expandMultipleVersionTemplate(String templateName) throws Exception {
		waitForSeconds(2);
		searchTemplate(templateName);
		if (isMultiplePublishVersion()) {
			//expand all future template that has draft template
			if (areListElementVisible(multipleTemplateList, 2)) {
				List<String> effectiveDates = new ArrayList<>();
				//expand template with multiple version
				for (WebElement multipleTemplate : multipleTemplateList) {
					WebElement toggleBTN = multipleTemplate.findElement(By.cssSelector(".toggle i"));
					if (toggleBTN.getAttribute("class").trim().equals("fa fa-caret-right")) {
						clickTheElement(toggleBTN);
					}
				}
			}
		}
	}

	@FindBy(css = "div[ng-repeat=\"child in item.childTemplate\"] div.child-row:nth-child(1)")
	private List<WebElement> AllChildrenOfCurrentPublishedTemplate;

	@Override
	public void createDraftForEachPublishInMultipleTemplate(String templateName, String button, String editOrViewMode) throws Exception {
		expandMultipleVersionTemplate(templateName);
		if (areListElementVisible(multipleTemplateList, 2)) {
			int beforeCount = getAllTemplateCountInMultipleVersion();
//				String tempName = multipleTemplate.findElement(By.cssSelector("div:nth-child(2) button span.ng-binding")).getText().trim();
//				clickOnSpecifyTemplateName(tempName,editOrViewMode);
			//Create draft version for current published template
			clickTheElement(currentPublishedTemplate.findElement(By.cssSelector(" button")));
			clickOnEditButtonOnTemplateDetailsPage();
			scrollToBottom();
			chooseSaveOrPublishBtnAndClickOnTheBtn(button);
			waitForSeconds(2);
			expandMultipleVersionTemplate(templateName);
			int afterCount = getAllTemplateCountInMultipleVersion();
			if (afterCount - beforeCount == 1) {
				SimpleUtils.pass("User create draft version template for current published template successfully!");
			} else {
				SimpleUtils.fail("User failed to create draft version for current published template!", false);
			}

			//create draft version for each future publish version
			for (WebElement futurePublish : AllChildrenOfCurrentPublishedTemplate) {
				if (futurePublish.findElement(By.cssSelector("lg-eg-status")).getAttribute("type").trim().equalsIgnoreCase("Published")) {
					clickTheElement(futurePublish.findElement(By.cssSelector(" button")));
					clickOnEditButtonOnTemplateDetailsPage();
					scrollToBottom();
					chooseSaveOrPublishBtnAndClickOnTheBtn(button);
					waitForSeconds(2);
					expandMultipleVersionTemplate(templateName);
					int afterCount1 = getAllTemplateCountInMultipleVersion();
					if (afterCount1 - afterCount == 1) {
						SimpleUtils.pass("User create draft version for future published template successfully!");
					} else {
						SimpleUtils.fail("User failed to create draft version for future published template!", false);
					}
				}
			}
		}
	}

	@FindBy(css = "div.lg-templates-table-improved__grid-row.ng-scope.hasChildren")
	private WebElement currentPublishedTemplate;
	@FindBy(css = "div[ng-repeat=\"child in item.childTemplate\"] div.child-row:nth-child(1) lg-eg-status[type=\"Draft\"]")
	private WebElement draftFromCurrentPublished;
	@FindBy(css = "div.lg-templates-table-improved__grid-row.ng-scope.hasChildren+div[ng-repeat*=\"childTemplate\"] div.ml-25 button")
	private WebElement draftTemNameFromCurrentPublished;
	@FindBy(css = "div.lg-templates-table-improved__grid-row.child-row")
	private List<WebElement> allFutureTemplatesList;
	@FindBy(css = "div[ng-repeat=\"child in item.childTemplate\"] div.child-row:nth-child(1) lg-eg-status[type=\"Published\"]")
	private List<WebElement> allFuturePublishedTemplatesList;
	@FindBy(css = "div.lg-templates-table-improved__grid-row.child-row.ng-scope")
	private List<WebElement> allFutureDraftTemplatesList;
	@FindBy(css = "lg-button[is-save-as=\"$ctrl.isSaveAs\"] h3")
	private List<WebElement> menusList;
	@FindBy(css = "lg-button[label=\"Yes\"] button")
	private WebElement yesButtonOnCancelEditPopup;
	@FindBy(css = "modal[modal-title=\"Cancel Editing?\"]")
	private WebElement cancelEditPopup;

	public List<String> getMenuListOnTemplateDetailsPage() throws Exception {
		List<String> MenuList = new ArrayList<String>();
		try {
			clickOnEditButtonOnTemplateDetailsPage();
			clickTheElement(dropdownArrowButton);
			for (WebElement menu : menusList) {
				String name = menu.getText().trim();
				MenuList.add(name);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return MenuList;
	}

	@Override
	public HashMap<String, List<String>> verifyMenuListForMultipleTemplate(String templateName) throws Exception {
		HashMap<String, List<String>> allMenuInfo = new HashMap<>();
		List<String> currentTemplateMenuNameList = new ArrayList<String>();
		List<String> futureTemplateMenuNameList = new ArrayList<String>();
		expandMultipleVersionTemplate(templateName);
		//Open the draft template that from current published
		if (isElementLoaded(draftFromCurrentPublished, 3)) {
			clickTheElement(draftTemNameFromCurrentPublished);
			waitForSeconds(5);
			currentTemplateMenuNameList = getMenuListOnTemplateDetailsPage();
			allMenuInfo.put("current", currentTemplateMenuNameList);
			clickTheElement(cancelButton);
			if (isElementLoaded(cancelEditPopup, 3)) {
				clickTheElement(yesButtonOnCancelEditPopup);
			}
		} else {
			SimpleUtils.fail("draft template From Current Published is not loaded", false);
		}
		expandMultipleVersionTemplate(templateName);
		////Open the draft template that from future published
		if (areListElementVisible(allFutureDraftTemplatesList, 3)) {
			WebElement draftNameFromFuture = allFutureDraftTemplatesList.get(0).findElement(By.cssSelector("div.childrenName button"));
			clickTheElement(draftNameFromFuture);
			waitForSeconds(5);
			futureTemplateMenuNameList = getMenuListOnTemplateDetailsPage();
			allMenuInfo.put("future", futureTemplateMenuNameList);
		} else {
			SimpleUtils.fail("draft template From future Published is not loaded", false);
		}
		return allMenuInfo;
	}

	//	@FindBy(css="lg-button[label=\"History\"] button")
//	private WebElement historyButton;
	@FindBy(css = "lg-button[label=\"Archive\"] button")
	private WebElement archiveButton;
	@FindBy(css = "lg-button[ng-click=\"editTemplate()\"] button")
	private WebElement editTemplateButton;
	@FindBy(css = "lg-button[label=\"Close\"] button")
	private WebElement closeButton;

	@Override
	public void verifyButtonsShowingOnPublishedTemplateDetailsPage() throws Exception {
		String draftOfCurrentPublishLocator = "div[ng-repeat=\"child in item.childTemplate\"] div.child-row:nth-child(1) lg-eg-status[type=\"Draft\"]";
		String editLocator = "lg-button[ng-click=\"editTemplate()\"] button";
		//Check the buttons on current published template
		if (isElementExist(draftOfCurrentPublishLocator)) {
			clickTheElement(currentPublishedTemplate.findElement(By.cssSelector("button")));
			waitForSeconds(5);
			if (isElementLoaded(historyButton, 2) && isElementLoaded(archiveButton) && !isElementExist(editLocator)) {
				SimpleUtils.pass("Buttons on Published template details page can show well.");
			} else {
				SimpleUtils.fail("Buttons on Published template details page is not correctly!", false);
			}
		} else {
			clickTheElement(currentPublishedTemplate.findElement(By.cssSelector("button")));
			waitForSeconds(5);
			if (isElementLoaded(historyButton, 2) && isElementLoaded(archiveButton) && isElementLoaded(editTemplateButton)) {
				SimpleUtils.pass("Buttons on Published template details page can show well.");
			} else {
				SimpleUtils.fail("Buttons on Published template details page is not correctly!", false);
			}
		}
		clickTheElement(closeButton);
		waitForSeconds(5);
	}

	@Override
	public void verifyButtonsShowingOnDraftTemplateDetailsPage() throws Exception {
		String draftOfCurrentPublishLocator = "div[ng-repeat=\"child in item.childTemplate\"] div.child-row:nth-child(1) lg-eg-status[type=\"Draft\"]";
		String futureDraftTemplate = "div.lg-templates-table-improved__grid-row.child-row.ng-scope";
		//Check the buttons on draft status
		if (isElementExist(draftOfCurrentPublishLocator)) {
			clickTheElement(draftTemNameFromCurrentPublished);
			waitForSeconds(5);
			if (isElementLoaded(historyButton, 2) && isElementLoaded(deleteTemplateButton) && isElementLoaded(editTemplateButton)) {
				SimpleUtils.pass("Buttons on draft template details page can show well.");
			} else {
				SimpleUtils.fail("Buttons on draft template details page is not correct.", false);
			}
		} else if (isElementExist(futureDraftTemplate)) {
			clickTheElement(allFutureDraftTemplatesList.get(0).findElement(By.cssSelector("button")));
			waitForSeconds(5);
			if (isElementLoaded(historyButton, 2) && isElementLoaded(deleteTemplateButton) && isElementLoaded(editTemplateButton)) {
				SimpleUtils.pass("Buttons on draft template details page can show well.");
			} else {
				SimpleUtils.fail("Buttons on draft template details page is not correct.", false);
			}
		} else {
			SimpleUtils.fail("There are no any draft template showing", false);
		}
	}

	@Override
	public void createMultipleTemplateForAllTypeOfTemplate(String templateName, String dynamicGpName, String criteriaType, String criteriaValue, String button, int date, String editOrViewMode) throws Exception {
		for (int i = 0; i < 6; i++) {
			clickTheElement(configurationCardsList.get(i));
			waitForSeconds(3);
			publishNewTemplate(templateName, dynamicGpName, criteriaType, criteriaValue);
			createFutureTemplateBasedOnExistingTemplate(templateName, button, date, editOrViewMode);
			archiveMultipleTemplate(templateName);
			goToConfigurationPage();
		}
	}

	@Override
	public void archiveMultipleTemplate(String templateName) throws Exception {
		if (areListElementVisible(multipleTemplateList, 3)) {
			for (WebElement multipleTemplate : multipleTemplateList) {
				SimpleUtils.report("1111");
//				WebElement templateNameButton = multipleTemplate.findElement(By.cssSelector("button"));
				WebElement templateNameButton = getDriver().findElement(By.cssSelector(".lg-templates-table-improved__grid-row--header~div button"));
				SimpleUtils.report("222");
				String templateStatus = getDriver().findElement(By.cssSelector(".lg-templates-table-improved__grid-row--header~div lg-eg-status")).getAttribute("type").trim();
				SimpleUtils.report("333");
				if (templateStatus.equalsIgnoreCase("Published")) {

					clickTheElement(templateNameButton);
					waitForSeconds(5);
					if (isElementLoaded(archiveButton, 2)) {
						clickTheElement(archiveButton);
						waitForSeconds(2);
						if (isElementLoaded(okButton)) {
							clickTheElement(okButton);
						} else {
							SimpleUtils.fail("archive/delete template popup is not showing", false);
						}

					} else {
						SimpleUtils.fail("Template details page doesn't show well", false);
					}
				} else {
					clickTheElement(templateNameButton);
					waitForSeconds(5);
					if (isElementLoaded(deleteTemplateButton, 2)) {
						clickTheElement(deleteTemplateButton);
						waitForSeconds(2);
						if (isElementLoaded(okButton)) {
							clickTheElement(okButton);
						} else {
							SimpleUtils.fail("archive/delete template popup is not showing", false);
						}

					} else {
						SimpleUtils.fail("Template details page doesn't show well", false);
					}
				}
				searchTemplate(templateName);
				List<WebElement> multipleTemplateList = getDriver().findElements(By.cssSelector(".lg-templates-table-improved__grid-row--header~div"));
			}
		}
	}

	@FindBy(xpath = "//*[@type=\"'AdvancedStaffingRule'\"]")
	private List<WebElement> advanceStaffRules;
	@FindBy(css = "lg-template-advanced-staffing-rule div.settings-work-rule-number")
	private WebElement advanceStaffRulesStatus;

	@Override
	public void verifyAdvanceStaffRuleFromLocationLevel(List<String> advanceStaffingRule) throws Exception {
		if (advanceStaffRules.size() != 0)
			for (WebElement advanceStaffRule : advanceStaffRules) {
				List<WebElement> advanceStaffRuleContent = advanceStaffRule.findElements(By.xpath("//*[@class=\"highlight\"]"));
				for (WebElement content : advanceStaffRuleContent) {
					if (advanceStaffingRule.get(advanceStaffRules.indexOf(advanceStaffRule)).contains(content.getText())) {
						SimpleUtils.pass("AdvancedStaffingRule aligned with template level");
					} else {
						SimpleUtils.fail("AdvancedStaffingRule does not aligned with template level", false);
					}
				}
			}
		else {
			SimpleUtils.fail("no AdvancedStaffingRule in the template", false);
		}
	}

	@FindBy(css = "lg-template-advanced-staffing-rule div.settings-work-rule-number")
	private List<WebElement> advanceStaffRuleStatues;

	@Override
	public void verifyAdvanceStaffRuleStatusFromLocationLevel(List<String> advanceStaffingRuleStatus) throws Exception {
		if (advanceStaffRuleStatues.size() != 0)
			for (WebElement statues : advanceStaffRuleStatues) {
				if (advanceStaffingRuleStatus.get(advanceStaffRuleStatues.indexOf(statues)).equalsIgnoreCase(statues.getAttribute("data-tootik"))) {
					SimpleUtils.pass("This rule is enabled/disable for this location");
				} else {
					SimpleUtils.fail("This rule status is not exist", false);
				}
			}
		else {
			SimpleUtils.fail("no AdvancedStaffingRule in the template", false);
		}
	}


	@FindBy(css = "lg-template-advanced-staffing-rule div.settings-work-rule-assignment-container")
	private WebElement advanceStaffRuleEditStatues;

	@Override
	public void changeAdvanceStaffRuleStatusFromLocationLevel(int i) throws Exception {
		if (isElementLoaded(advanceStaffRuleStatues.get(i), 3)) {
			if (!isElementLoaded(advanceStaffRuleEditStatues)) {
				advanceStaffRuleStatues.get(i).click();
			}
			advanceStaffRuleStatues.get(i).click();
		} else {
			SimpleUtils.fail("no AdvancedStaffingRule in the template", false);
		}
	}

	@Override
	public void verifyCanNotAddAdvancedStaffingRuleFromTemplateLevel() throws Exception {
		if (isElementLoaded(addIconOnRulesListPage)) {
			clickTheElement(addIconOnRulesListPage);
			if (isElementLoaded(addAdvancedStaffingRuleButton)) {
				SimpleUtils.fail("Advance staffing rules tab is show", false);
			} else {
				SimpleUtils.pass("Advance staffing rules tab is NOT show");
			}
		} else {
			SimpleUtils.fail("Work role's staffing rules list page was loaded failed", false);
		}
	}

	@Override
	public void verifyCanNotEditDeleteAdvancedStaffingRuleFromTemplateLevel() throws Exception {
		List<WebElement> advancedStaffingRules = getDriver().findElements(By.cssSelector("lg-template-advanced-staffing-rule"));
		if (advancedStaffingRules.size() != 0)
			for (WebElement advancedStaffingRule : advancedStaffingRules) {
				if ((advancedStaffingRule.findElements(By.cssSelector("span.settings-work-rule-edit-edit-icon")).size() > 0
						|| (advancedStaffingRule.findElements(By.cssSelector("span.settings-work-rule-edit-delete-icon")).size() > 0))) {
					SimpleUtils.fail("This AdvancedStaffingRule can be edited/deleted", false);
				} else {
					SimpleUtils.pass("This AdvancedStaffingRule cannot be edited/deleted");
				}
			}
		else {
			SimpleUtils.fail("no AdvancedStaffingRule in the template", false);
		}
	}

	//added by Fiona
	@FindBy(css = "div.lg-table div[ng-repeat*=\"childTemplate\"]:last-child")
	private WebElement lastGroupInFutureTemplate;
	@FindBy(css = "div.lg-table div[ng-repeat*=\"childTemplate\"]:last-child>div")
	private List<WebElement> lastGroupInFutureTemplates;
	@FindBy(css = "div.lg-table div[ng-repeat*=\"childTemplate\"]:last-child>div:last-child button")
	private WebElement draftNameInLastGroupOfFutureTemplates;
	@FindBy(css = "div.lg-table div[ng-repeat*=\"childTemplate\"]:last-child>div button")
	private WebElement publishedNameInLastGroupOfFutureTemplates;
	@FindBy(css = "modal[modal-title=\"Replacing Existing Published Status\"]")
	private WebElement replacingExistingPublishedStatusPopup;

	@Override
	public String updateEffectiveDateOfFutureTemplate(String templateName, String button, int date) throws Exception {
		List<String> effectiveDates = new ArrayList<String>();
		String effectDate = null;
		//update existing future published version template
		waitForSeconds(2);
		expandMultipleVersionTemplate(templateName);
		if (areListElementVisible(multipleTemplateList, 3)) {
			if (lastGroupInFutureTemplates.size() > 1) {
				//click the last group future's draft template
				clickTheElement(draftNameInLastGroupOfFutureTemplates);
				waitForSeconds(5);
				clickOnEditButtonOnTemplateDetailsPage();
				scrollToBottom();
				chooseSaveOrPublishBtnAndClickOnTheBtn(button);
				if (isElementLoaded(dateOfPublishPopup, 2)) {
					clickTheElement(effectiveDate);
					setEffectiveDate(date);
					clickTheElement(okButtonOnFuturePublishConfirmDialog);
				} else {
					SimpleUtils.fail("The future publish template confirm dialog is not displayed.", false);
				}
				if (isElementLoaded(replacingExistingPublishedStatusPopup, 3)) {
					clickTheElement(okButtonOnFuturePublishConfirmDialog);
				}
			} else {
				//click the last group future's published template
				clickTheElement(publishedNameInLastGroupOfFutureTemplates);
				waitForSeconds(5);
				clickOnEditButtonOnTemplateDetailsPage();
				scrollToBottom();
				chooseSaveOrPublishBtnAndClickOnTheBtn(button);
				if (isElementLoaded(dateOfPublishPopup, 2)) {
					clickTheElement(effectiveDate);
					setEffectiveDate(date);
					clickTheElement(okButtonOnFuturePublishConfirmDialog);
				} else {
					SimpleUtils.fail("The future publish template confirm dialog is not displayed.", false);
				}
				if (isElementLoaded(replacingExistingPublishedStatusPopup, 3)) {
					clickTheElement(okButtonOnFuturePublishConfirmDialog);
				}
			}
		} else {
			SimpleUtils.fail("Template list is not displayed!", false);
		}
		effectiveDates = getEffectiveDateForTemplate(templateName);
		effectDate = effectiveDates.get(effectiveDates.size() - 1);
		return effectDate;
	}

	@Override
	public List<String> getEffectiveDateForTemplate(String templateName) throws Exception {
		List<String> effectiveDates = new ArrayList<String>();
		expandMultipleVersionTemplate(templateName);
		if (areListElementVisible(multipleTemplateList, 3)) {
			for (WebElement multipleTemplate : multipleTemplateList) {
				String effectiveDate = multipleTemplate.findElement(By.cssSelector(".date")).getText().trim();
				//get all effectiveDate
				if (!effectiveDate.isEmpty() || effectiveDate != null) {
					effectiveDates.add(effectiveDate);
				}
			}
		} else {
			SimpleUtils.fail("Multiple template doesn't show well! ", false);
		}
		return effectiveDates;
	}

	@FindBy(css = "ul.staffing-dropdown-menu li:nth-child(1)")
	private WebElement staffingRuleButton;
	@FindBy(css = "ul.staffing-dropdown-menu li")
	private List<WebElement> ruleButtons;
	@FindBy(css = ".constraint-box")
	private WebElement staffingRuleFields;

	@Override
	public void checkTheEntryOfAddBasicStaffingRule() throws Exception {
		waitForSeconds(5);
		if (isElementEnabled(addIconOnRulesListPage)) {
			clickTheElement(addIconOnRulesListPage);
			if (isElementEnabled(staffingRuleButton)) {
				SimpleUtils.pass("Staffing rules tab is show");
				clickTheElement(staffingRuleButton);
				if (isElementEnabled(staffingRuleFields)) {
					SimpleUtils.pass("Staffing rules tab is clickable");
				} else {
					SimpleUtils.fail("Staffing rules tab is NOT clickable", false);
				}
			} else {
				SimpleUtils.pass("Advance staffing rules tab is NOT show");
			}
		} else {
			SimpleUtils.fail("Work role's staffing rules list page was loaded failed", false);
		}
	}

	@Override
	public void checkTheEntryOfAddShiftPatternRule() throws Exception {
		if (isElementLoaded(addIconOnRulesListPage, 5)) {
			clickTheElement(addIconOnRulesListPage);
			if (areListElementVisible(ruleButtons, 5)) {
				for (WebElement rule : ruleButtons) {
					if (rule.getText().equalsIgnoreCase("Shift Pattern")) {
						clickTheElement(rule);
						break;
					}
				}
			} else {
				SimpleUtils.fail("Shift Pattern option button is not loaded!", false);
			}
		} else {
			SimpleUtils.fail("Work role's staffing rules list page was loaded failed", false);
		}
	}

	//added by Jane
	@FindBy(css = ".lg-toast span.lg-toast__simple-text")
	private WebElement warningMsgToast;

	@Override
	public boolean verifyWarningInfoForDemandDriver(String warningMsg) throws Exception {
		boolean isWarningMsgExisting = false;
		clickTheElement(publishBTN);
		if (isElementLoaded(warningMsgToast, 10)) {
			if (warningMsgToast.getText().toLowerCase().contains(warningMsg.toLowerCase())) {
				isWarningMsgExisting = true;
			}
		}
		return isWarningMsgExisting;
	}

	@FindBy(css = "tr[ng-repeat*=\"item in $ctrl.forecastSourceData.aggregated\"]")
	private List<WebElement> aggregatedDriverOptions;
	@FindBy(css = "lg-button[label=\"+ Add\"] button")
	private WebElement addBtn;
	@FindBy(css = "div.lg-search-options>div")
	private List<WebElement> searchOptions;
	@FindBy(css = "div.lg-search-options>div")
	private List<WebElement> scaleFactor;

	public boolean addAggregatedOptions(String options) throws Exception {
		boolean ifSuccess = false;
		int optionsCount = 0;
		String[] optionsToAdd;
		ConcurrentHashMap<String, String> driverAndFactors = new ConcurrentHashMap<String, String>();

		if (options != null) {
			optionsToAdd = options.split(";");
			optionsCount = optionsToAdd.length;
			while ((optionsCount - 1) > 0) {
				clickTheElement(addBtn);
				optionsCount--;
			}
			for (String optionToAdd : optionsToAdd) {
				driverAndFactors.put(optionToAdd.split(",")[0], optionToAdd.split(",")[1]);
			}
			for (WebElement driverOption : aggregatedDriverOptions) {
				for (Map.Entry<String, String> entry : driverAndFactors.entrySet()) {
					if (driverOption.findElement(By.cssSelector("input")).getAttribute("class").contains("ng-empty") ||
							!driverOption.findElement(By.cssSelector("div")).getAttribute("innerText").replaceAll("\n", "").trim().equals(entry.getKey())) {
						if (isElementExist("div[ng-repeat*=\"option in $ctrl.displayOptions\"]")) {
							List<WebElement> optionList = driverOption.findElements(By.cssSelector("div[ng-repeat*=\"option in $ctrl.displayOptions\"]"));
							for (WebElement value : optionList) {
								if (!value.getAttribute("class").contains("disabled") &&
										value.findElement(By.cssSelector("div")).getAttribute("innerText").replaceAll("\n", "").trim().equals(entry.getKey())) {
									clickTheElement(driverOption.findElement(By.cssSelector("input")));
									clickTheElement(value.findElement(By.cssSelector("div")));
									break;
								}
							}
							if (!driverOption.findElement(By.cssSelector("input")).getAttribute("class").contains("ng-empty")) {
								WebElement scaleFactor = driverOption.findElement(By.cssSelector("input-field[type=\"number\"] input"));
								String factorValue = scaleFactor.getText();
								if (!factorValue.equals(entry.getValue())) {
									scaleFactor.clear();
									scaleFactor.sendKeys(entry.getValue());
								}
								driverAndFactors.remove(entry.getKey(), entry.getValue());
								if (driverAndFactors.size() == 0){
									ifSuccess = true;
								}
								break;
							} else {
								if (saveButton.getAttribute("disabled").equals("true")) {
									clickTheElement(cancelButton);
									setLeaveThisPageButton();
									SimpleUtils.report("No available options, all are disabled!");
								} else {
									SimpleUtils.fail("Save button should be disabled!", false);
								}
								return false;
							}
						} else {
							if (saveButton.getAttribute("disabled").equals("true")) {
								clickTheElement(cancelButton);
								setLeaveThisPageButton();
								SimpleUtils.report("No options can be selected, add some basic drivers first!");
							} else {
								SimpleUtils.fail("Save button should be disabled!", false);
							}
							return false;
						}
					}
				}
			}
		}
		return ifSuccess;
	}

	@FindBy(css = "lg-button[label=\"Add\"] button")
	private WebElement addBtnForDriver;
	@FindBy(css = "form-section[ng-repeat=\"item in $ctrl.propertyMeta\"]")
	private List<WebElement> configurationSection;
	@FindBy(css = "div[ng-repeat=\"field in item.propertyMetas\"]")
	private List<WebElement> fieldInputList;
	@FindBy(css = "lg-button[label=\"Save\"] button")
	private WebElement saveBtn;
	@FindBy(css = "div.modal-content")
	private WebElement warningToast;
	@FindBy(css = "button[class*=\"btn lgn-action-button\"]")
	private WebElement okBtn;
	@FindBy(css = "tr[ng-repeat=\"rule in $ctrl.sortedRows\"] lg-button[label=\"Edit\"] button")
	private WebElement editBtnForDriver;
	@FindBy(css = "input-field[options=\"$ctrl.remoteOptions\"]")
	private WebElement remoteOption;

	@Override
	public void addOrEditDemandDriverInTemplate(HashMap<String, String> driverSpecificInfo) throws Exception {
		Select forecastSourceSelect = null;
		String forecastSource = driverSpecificInfo.get("Forecast Source");
		Select remoteOptionSelect = null;
		boolean isContinue = true;

		if (forecastSource != null) {
			forecastSourceSelect = new Select(forecastConfigurations.get(0).findElement(By.cssSelector("select")));
			forecastSourceSelect.selectByVisibleText(forecastSource);
			if (forecastSource.equals("Remote")) {
				remoteOptionSelect = new Select(remoteOption.findElement(By.cssSelector("select")));
				remoteOptionSelect.selectByVisibleText(driverSpecificInfo.get("Specify Location"));
			} else if (forecastSource.equals("Aggregated")) {
				isContinue = addAggregatedOptions(driverSpecificInfo.get("Options"));
			}
		}
		if (isContinue) {
			if (areListElementVisible(fieldInputList)) {
				setDriverConfiguration(fieldInputList, driverSpecificInfo);
			} else {
				SimpleUtils.fail("No fields found in demand driver creation/Edit page!", false);
			}
		} else {
			return;
		}
	}

	public void setDriverConfiguration(List<WebElement> forecastFieldInputList, HashMap<String, String> driverSpecificInfo) throws Exception {
		String childTag = "";
		String fieldType = "";
		List<String> derivedSources = new ArrayList<>(Arrays.asList("Distributed", "Remote", "Aggregated"));
		Select forecastSelect = null;
		List<WebElement> questionInputs = null;
		List<WebElement> yesOrNoOptions = null;
		for (int i = 0; i < forecastFieldInputList.size() - 1; i++) {
			if (derivedSources.contains(driverSpecificInfo.get("Forecast Source")) && i == forecastFieldInputList.size() - 3) {
				break;
			}
			if (isElementLoaded(forecastFieldInputList.get(i).findElement(By.cssSelector("question-input")))) {
				questionInputs = forecastFieldInputList.get(i).findElements(By.cssSelector("question-input"));
				for (WebElement questionInput : questionInputs) {
					for (Map.Entry<String, String> entry : driverSpecificInfo.entrySet()) {
						if (questionInput.getAttribute("question-title").contains(entry.getKey())) {
							childTag = questionInput.findElement(By.cssSelector("ng-transclude.lg-question-input__input :first-child")).getTagName();
							if (childTag.equals("input-field")) {
								if (childTag.equals("input-field")) {
									fieldType = questionInput.findElement(By.cssSelector("input-field")).getAttribute("type");
									if (fieldType.equals("number") || fieldType.equals("text")) {
										if (!questionInput.findElement(By.cssSelector("input-field input")).getText().equals(entry.getValue())) {
											questionInput.findElement(By.cssSelector("input-field input")).clear();
											questionInput.findElement(By.cssSelector("input-field input")).sendKeys(entry.getValue());
										}
										break;
									} else if (fieldType.equals("select")) {
										forecastSelect = new Select(questionInput.findElement(By.cssSelector("select")));
										if (questionInput.findElement(By.cssSelector("select")).getAttribute("class").contains("ng-empty") ||
												!forecastSelect.getFirstSelectedOption().getText().equals(entry.getValue())) {
											forecastSelect.selectByVisibleText(entry.getValue());
										}
										break;
									} else if (fieldType.equals("choose")) {
										clickTheElement(questionInput.findElement(By.cssSelector("span")));
										selectALocation(entry.getValue());
										break;
									}
								}
							} else if (childTag.equals("yes-no")) {
								yesOrNoOptions = questionInput.findElements(By.cssSelector("div[ng-repeat=\"button in $ctrl.buttons\"]"));
								for (WebElement choose : yesOrNoOptions) {
									if (choose.findElement(By.cssSelector("span")).getText().equals(entry.getValue()) &&
											!choose.getAttribute("class").contains("lg-button-group-selected")) {
										clickTheElement(choose);
										break;
									}
								}
							} else if (childTag.equals("lg-select")) {
								if (isElementExist("div[ng-repeat*=\"option in $ctrl.displayOptions\"]")) {
									if (questionInput.findElement(By.cssSelector("input")).getAttribute("class").contains("ng-empty") ||
											!questionInput.findElement(By.cssSelector("div.input-faked")).getAttribute("innerText").equals(entry.getValue())) {
										List<WebElement> options = questionInput.findElements(By.cssSelector("div[ng-repeat*=\"option in $ctrl.displayOptions\"]"));
										for (WebElement option : options) {
											if (!option.getAttribute("class").contains("disabled") &&
													option.findElement(By.cssSelector("div")).getAttribute("innerText").replaceAll("\n", "").trim().equals(entry.getValue())) {
												clickTheElement(questionInput.findElement(By.cssSelector("input")));
												clickTheElement(option.findElement(By.cssSelector("div")));
											}
										}
										if (questionInput.findElement(By.cssSelector("input")).getAttribute("class").contains("ng-empty")) {
											if (saveButton.getAttribute("disabled").equals("true")) {
												clickTheElement(cancelButton);
												setLeaveThisPageButton();
												SimpleUtils.report("No available options!");
												return;
											} else {
												SimpleUtils.fail("Save button should be disabled!", false);
											}
										}
									}
								} else {
									if (saveButton.getAttribute("disabled").equals("true")) {
										clickTheElement(cancelButton);
										setLeaveThisPageButton();
										SimpleUtils.report("No options can be selected, add some basic drivers first!");
										return;
									} else {
										SimpleUtils.fail("Save button should be disabled!", false);
									}
								}
							}
						}
					}
				}
			}
		}
		clickOnSaveForDriver();
	}

	@FindBy(css = ".lg-modal__title-icon")
	private WebElement selectALocationTitle;
	@FindBy(css = "div.lg-tab-toolbar__search >lg-search >input-field>ng-form>input")
	private WebElement searchInputInSelectALocation;
	@FindBy(css = "tr[ng-repeat=\"item in $ctrl.currentPageItems track by $index\"]")
	private List<WebElement> locationRowsInSelectLocation;

	private void selectALocation(String searchText) {
		if (isElementEnabled(selectALocationTitle, 5)) {
			searchInputInSelectALocation.sendKeys(searchText);
			searchInputInSelectALocation.sendKeys(Keys.ENTER);
			waitForSeconds(10);
			if (areListElementVisible(locationRowsInSelectLocation, 30) && locationRowsInSelectLocation.size() > 0) {
				WebElement firstRow = locationRowsInSelectLocation.get(0).findElement(By.cssSelector("input[type=\"radio\"]"));
				clickTheElement(firstRow);
				clickTheElement(okButton);
			} else
				SimpleUtils.report("Search location result is 0");

		} else
			SimpleUtils.fail("Select a location window load failed", true);
	}

	public void clickOnSaveForDriver() throws Exception {
		if (isElementLoaded(saveBtn)) {
			clickTheElement(saveBtn);
			if (isElementLoaded(warningToast, 5)) {
				if (warningToast.getText().contains("name duplicates with existing demand drivers")) {
					SimpleUtils.report("Create a driver with a duplicated name is not allowed!");
				} else if (warningToast.getText().contains("type, channel and category duplicate with existing demand drivers")) {
					SimpleUtils.report("Create a driver with a duplicated type& channel& category is not allowed!");
				}
				//click the ok and cancel
				clickTheElement(okBtn);
				clickTheElement(cancelButton);
				waitForSeconds(3);
			}
		} else {
			SimpleUtils.fail("Save button load failed!", false);
		}
	}

	@Override
	public boolean verifyWarningForDemandDriver(String warningMsg) throws Exception {
		boolean warningMsgExist = false;
		if (isElementLoaded(warningToast, 3)) {
			if (warningToast.getText().toLowerCase().contains(warningMsg.toLowerCase())) {
				warningMsgExist = true;
			}
			clickTheElement(okBtn);
		}
		return warningMsgExist;
	}

	@FindBy(css = "lg-button[label=\"View\"] button")
	private WebElement viewBtn;

	@Override
	public boolean verifyDriverInViewMode(HashMap<String, String> driverToCheck) throws Exception {
		if (isElementLoaded(viewBtn, 5))
			clickTheElement(viewBtn);
		if (areListElementVisible(fieldInputList)) {
			String childTag = "";
			String fieldType = "";
			List<String> derivedSources = new ArrayList<>(Arrays.asList("Distributed", "Remote", "Aggregated"));
			Select forecastSelect = null;
			List<WebElement> questionInputs = null;
			List<WebElement> yesOrNoOptions = null;

			for (int i = 0; i < fieldInputList.size() - 1; i++) {
				if (derivedSources.contains(driverToCheck.get("Forecast Source")) && i == (fieldInputList.size() - 3)) {
					break;
				}
				if (isElementLoaded(fieldInputList.get(i).findElement(By.cssSelector("question-input")))) {
					questionInputs = fieldInputList.get(i).findElements(By.cssSelector("question-input"));
					for (WebElement questionInput : questionInputs) {
						for (Map.Entry<String, String> entry : driverToCheck.entrySet()) {
							if (questionInput.getAttribute("question-title").contains(entry.getKey())) {
								childTag = questionInput.findElement(By.cssSelector("ng-transclude.lg-question-input__input :first-child")).getTagName();
								if (childTag.equals("input-field")) {
									if (childTag.equals("input-field")) {
										fieldType = questionInput.findElement(By.cssSelector("input-field")).getAttribute("type");
										if (fieldType.equals("number") || fieldType.equals("text")) {
											if (!questionInput.findElement(By.cssSelector("input-field div")).getAttribute("innerText").replaceAll("\n", "").trim().equals(entry.getValue()) ||
													!questionInput.findElement(By.cssSelector("input-field input")).getAttribute("disabled").equals("true")) {
												return false;
											}
											break;
										} else if (fieldType.equals("select")) {
											forecastSelect = new Select(questionInput.findElement(By.cssSelector("select")));
											if (!forecastSelect.getFirstSelectedOption().getText().equals(entry.getValue()) ||
													!questionInput.findElement(By.cssSelector("select")).getAttribute("disabled").equals("true")) {
												return false;
											}
											break;
										} else if (fieldType.equals("choose")) {
											if (!questionInput.findElements(By.cssSelector("span")).get(1).getText().equals(entry.getValue()) ||
													!questionInput.findElement(By.cssSelector("div.input-faked")).getAttribute("disabled").equals("true"))
												return false;
											break;
										}
									}
								} else if (childTag.equals("yes-no")) {
									yesOrNoOptions = questionInput.findElements(By.cssSelector("div[ng-repeat=\"button in $ctrl.buttons\"]"));
									for (WebElement choose : yesOrNoOptions) {
										if (choose.findElement(By.cssSelector("span")).getText().equals(entry.getValue()) &&
												!choose.getAttribute("class").contains("lg-button-group-selected")) {
											return false;
										}
										break;
									}
								} else if (childTag.equals("lg-select")) {
									if (!questionInput.findElement(By.cssSelector("input")).getAttribute("disabled").contains("true") ||
											!questionInput.findElement(By.cssSelector("div.input-faked")).getAttribute("innerText").replaceAll("\n", "").trim().equals(entry.getValue())) {
										return false;
									}
									break;
								}
							}
						}
					}
				}
			}
		} else {
			SimpleUtils.fail("No fields found in demand driver creation/Edit page!", false);
		}
		return true;
	}

	@Override
	public void verifyPublishedTemplateAfterEdit(String templateName) throws Exception {
		if (searchTemplate(templateName)) {
			if (isItMultipVersion()) {
				if (!isClickable(templatesList.get(0).findElement(By.cssSelector("button")), 5)) {
					if (publishedTemplateStatus.size() == 1) {
						expandTemplate();
						clickTheElement(getDriver().findElement(By.cssSelector(".child-row button")));
						if (!isElementLoaded(templateDetailsTab))
							SimpleUtils.fail("Failed to enter the template details page!", false);
					} else {
						SimpleUtils.fail("There should be only one published version!", false);
					}

				} else {
					SimpleUtils.fail("Template name should not be clickable for multiple version!", false);
				}
			} else {
				SimpleUtils.fail("This template should be multiple version!", false);
			}
		} else {
			SimpleUtils.fail("Can not find the template you search!", false);
		}


	}

	@FindBy(css = "input[placeholder=\"Search by demand driver name\"]")
	private WebElement searchDriverInputBox;
	@FindBy(css = "tr[ng-repeat=\"rule in $ctrl.sortedRows\"]")
	private List<WebElement> driverList;

	@Override
	public boolean searchDriverInTemplateDetailsPage(String driverName) throws Exception {
		boolean existing = false;
		if (isElementEnabled(searchDriverInputBox, 5)) {
			clickTheElement(searchDriverInputBox);
			searchDriverInputBox.clear();
			searchDriverInputBox.sendKeys(driverName);
			searchDriverInputBox.sendKeys(Keys.ENTER);
			waitForSeconds(2);
			if (driverList.size() > 0)
				existing = true;
		}
		return existing;
	}

	@FindBy(css = "lg-button[label=\"Remove\"]")
	private WebElement removeBTN;

	@Override
	public void clickRemove() throws Exception {
		if (isElementLoaded(removeBTN)) {
			clickTheElement(removeBTN);
		} else {
			SimpleUtils.fail("Can not find the remove button!", false);
		}
	}

	@FindBy(tagName = "work-role-badges-edit")
	private WebElement badgeSection;

	@Override
	public void verifyStaffingRulePageShowWell() throws Exception {
		if (isElementEnabled(badgeSection) && isElementEnabled(staffingRuleFields)) {
			SimpleUtils.pass("Staffing rule page shows well");
		} else {
			SimpleUtils.fail("Staffing rule page doesn't show well", false);
		}
	}

	@FindBy(css = ".limit-constraint select")
	private WebElement conditionMaxMinExactly;
	@FindBy(css = "input-field[type=\"number\"] input")
	private WebElement numberInput;
	@FindBy(css = "input-field.workRoleSelect select")
	private WebElement workRoleSelect;
	@FindBy(css = "input-field[options*=\"UnitOptions\"] select")
	private WebElement unitOptions;
	@FindBy(css = "div[ng-if*=\"showTimeConstraint()\"] input-field[type=\"number\"] input")
	private WebElement startOffsetMinutes;
	@FindBy(css = "div[ng-if*=\"isDuring()\"] input-field[type=\"number\"] input")
	private WebElement endOffsetMinutes;
	@FindBy(css = "div[ng-if^=\"$ctrl.showTimeConstraint()\"] input-field[options*=\"getEventPointOptions\"] select")
	private WebElement startEventPointOptions;
	@FindBy(css = "div[ng-if*=\"isDuring()\"] input-field[options*=\"getEventPointOptions\"] select")
	private WebElement endEventPointOptions;
	@FindBy(css = "div[ng-if^=\"$ctrl.showTimeConstraint()\"] lg-select[options*=\"timeEventOptions\"] input-field")
	private WebElement startTimeEventOptions;
	@FindBy(css = "div[ng-if^=\"$ctrl.showTimeConstraint()\"] lg-select[options*=\"timeEventOptions\"] input-field div")
	private WebElement startTimeEventSelected;
	@FindBy(css = "div[ng-if*=\"isDuring()\"] lg-select[options*=\"timeEventOptions\"]")
	private WebElement endTimeEventOptions;
	@FindBy(css = "div[ng-if*=\"isDuring()\"] lg-select[options*=\"timeEventOptions\"] input-field div")
	private WebElement endTimeEventSelected;
	@FindBy(css = "div[ng-if^=\"$ctrl.showTimeConstraint()\"]  .lg-search-options .lg-search-options__option")
	private List<WebElement> startTimeEventOptionsList;
	@FindBy(css = "div[ng-if*=\"isDuring()\"]  .lg-search-options .lg-search-options__option")
	private List<WebElement> endTimeEventOptionsList;

	@Override
	public void verifyConditionAndNumberFiledCanShowWell() throws Exception {
		boolean flag = true;
		List<String> targets = new ArrayList<>(Arrays.asList("A Maximum", "A Minimum", "Exactly"));
		List<String> optionNames = new ArrayList<>();
		Select select = new Select(conditionMaxMinExactly);
		//verify the conditionMaxMinExactly field options list
		if (isElementLoaded(conditionMaxMinExactly, 2) && isElementLoaded(numberInput, 2)) {
			List<WebElement> options = select.getOptions();
			for (WebElement option : options) {
				String optionName = option.getText().trim();
				optionNames.add(optionName);
			}
			for (String optionName : optionNames) {
				for (String option : targets) {
					if (optionName.equalsIgnoreCase(option)) {
						flag = true;
						SimpleUtils.pass(option + " is showing in list.");
						break;
					} else {
						flag = false;
					}
				}
			}
			if (flag) {
				SimpleUtils.pass("conditionMaxMinExactly field options list can show well");
			} else {
				SimpleUtils.pass("conditionMaxMinExactly field options list is Not Correct.");
			}

			//Verify Exactly option is disabled by default
			if (options.get(2).getAttribute("disabled").equalsIgnoreCase("true")) {
				SimpleUtils.pass("Exactly option is disabled by default!");
			} else {
				SimpleUtils.fail("Exactly option is Not disabled by default!", false);
			}
			//verify the Exactly option will only be enabled when user select start event filed as Specified Hours
			selectStartTimeEvent("Specified Hours");
			if (select.getFirstSelectedOption().getText().trim().equalsIgnoreCase("Exactly")) {
				SimpleUtils.pass("Exactly option will only be enabled when user select start event filed as Specified Hours.");
			} else {
				SimpleUtils.fail("Exactly option is not selected by default after user selected start event filed as Specified Hours.", false);
			}
		}
	}

	//select Start Time Event
	@Override
	public void selectStartTimeEvent(String startTimeEvent) throws Exception {
		if (isElementLoaded(conditionMaxMinExactly, 2)) {
			clickTheElement(startTimeEventOptions);
			if (areListElementVisible(startTimeEventOptionsList, 3)) {
				for (WebElement w : startTimeEventOptionsList) {
					if (w.getAttribute("innerText").trim().equalsIgnoreCase(startTimeEvent)) {
						clickTheElement(w);
						break;
					}
				}
			}
			if (startTimeEventSelected.getAttribute("innerText").trim().equalsIgnoreCase(startTimeEvent)) {
				SimpleUtils.pass("User select start Time Event successfully!");
			} else {
				SimpleUtils.fail("User failed to select start Time Event!", false);
			}
		}
	}

	@FindBy(css = "lg-button[label=\"Save\"] button")
	private WebElement saveButtonOnBasicStaffingRule;
	@FindBy(className = "settings-work-rule-save-icon")
	private WebElement saveRuleIcon;

	@Override
	public void verifyNumberInputFieldOfBasicStaffingRule() throws Exception {
		if (isElementLoaded(numberInput, 3)) {
			clickTheElement(numberInput);
			numberInput.clear();
			numberInput.sendKeys("5");
			if (saveRuleIcon.getAttribute("class").contains("enabled")) {
				SimpleUtils.pass("User can input number successfully!");
			} else {
				SimpleUtils.fail("User can not input number successfully!", false);
			}
		}
	}

	@Override
	public List<String> verifyWorkRoleListOfBasicStaffingRule() throws Exception {
		List<String> workRoleNames = new ArrayList<>();
		if (isElementLoaded(workRoleSelect, 3)) {
			Select select = new Select(workRoleSelect);
			List<WebElement> workRoleList = select.getOptions();
			for (WebElement workRole : workRoleList) {
				String workRoleName = workRole.getText().trim();
				workRoleNames.add(workRoleName);
			}
		}
		return workRoleNames;
	}

	@Override
	public void verifyUnitOptionsListOfBasicStaffingRule() throws Exception {
		List<String> unitOptionsValues = new ArrayList<>();
		List<String> optionValues = new ArrayList<>(Arrays.asList("Shifts", "Hours"));
		if (isElementLoaded(unitOptions, 2)) {
			Select select = new Select(unitOptions);
			List<WebElement> unitOptionsList = select.getOptions();
			for (WebElement unitOption : unitOptionsList) {
				unitOptionsValues.add(unitOption.getText().trim());
			}
		}
		boolean flag = true;
		for (String unitOptionsValue : unitOptionsValues) {
			for (String optionValue : optionValues) {
				if (unitOptionsValue.equalsIgnoreCase(optionValue)) {
					SimpleUtils.pass(optionValue + " is showing in Unit Options List");
					flag = true;
					break;
				} else {
					flag = false;
				}
			}
		}
		if (flag) {
			SimpleUtils.pass("unitOptions can show well");
		} else {
			SimpleUtils.fail("unitOptions can NOT show well", false);
		}
	}

	@Override
	public void verifyStartEndOffsetMinutesShowingByDefault() throws Exception {
		Select select = new Select(startEventPointOptions);
		WebElement selected = select.getFirstSelectedOption();
		if (selected.getText().trim().equalsIgnoreCase("during")) {
			if (!isElementExist("div[ng-if*=\"showTimeConstraint()\"] input-field[type=\"number\"] input") && !isElementExist("div[ng-if*=\"isDuring()\"] input-field[type=\"number\"] input")) {
				SimpleUtils.pass("Start/End offset time is not showing when start Event Point Option is selected during");
			} else {
				SimpleUtils.fail("Start/End offset time is showing when start Event Point Option is selected during", false);
			}
		}
		selectStartTimeEvent("Opening Operating Hours");
		select.selectByVisibleText("after");
		if (isElementExist("div[ng-if*=\"showTimeConstraint()\"] input-field[type=\"number\"] input") && isElementExist("div[ng-if*=\"isDuring()\"] input-field[type=\"number\"] input")) {
			SimpleUtils.pass("Start/End offset time is showing when start Event Point Option is selected after");
		} else {
			SimpleUtils.fail("Start/End offset time is NOT showing when start Event Point Option is selected after", false);
		}
		if (isElementLoaded(startOffsetMinutes, 3)) {
			clickTheElement(startOffsetMinutes);
			startOffsetMinutes.clear();
			startOffsetMinutes.sendKeys("5");
			if (saveRuleIcon.getAttribute("class").contains("enabled")) {
				SimpleUtils.pass("User can input number successfully!");
			} else {
				SimpleUtils.fail("User can not input number successfully!", false);
			}
		}
		if (isElementLoaded(endOffsetMinutes, 3)) {
			clickTheElement(endOffsetMinutes);
			endOffsetMinutes.clear();
			endOffsetMinutes.sendKeys("10");
			if (saveRuleIcon.getAttribute("class").contains("enabled")) {
				SimpleUtils.pass("User can input number successfully!");
			} else {
				SimpleUtils.fail("User can not input number successfully!", false);
			}
		}
	}

	@Override
	public void verifyStartEndEventPointOptionsList() throws Exception {
		List<String> eventPoints = new ArrayList<>(Arrays.asList("before", "after", "during"));
		List<String> eventPointList = new ArrayList<>();
		boolean flag = true;
		Select select = new Select(startEventPointOptions);
		List<WebElement> eventPointOptions = select.getOptions();
		for (WebElement eventPointOption : eventPointOptions) {
			eventPointList.add(eventPointOption.getText().trim());
		}
		for (String eventPointName : eventPointList) {
			for (String eventPoint : eventPoints) {
				if (eventPointName.equalsIgnoreCase(eventPoint)) {
					flag = true;
					SimpleUtils.pass(eventPointName + " is showing in start Event Point Options list.");
					break;
				} else {
					flag = false;
				}
			}
		}
		if (flag) {
			SimpleUtils.pass("start Event Point Options list can show correctly!");
		} else {
			SimpleUtils.fail("start Event Point Options list can NOT show correctly!", false);
		}
	}

	@Override
	public List<String> verifyStartEndTimeEventOptionsList() throws Exception {
		List<String> startEndTimeEventOptions = new ArrayList<>();
		if (isElementLoaded(startTimeEventOptions, 3)) {
			clickTheElement(startTimeEventOptions);
			if (areListElementVisible(startTimeEventOptionsList, 2)) {
				for (WebElement startTimeEventOption : startTimeEventOptionsList) {
					startEndTimeEventOptions.add(startTimeEventOption.getAttribute("innerText").trim());
				}
			} else {
				SimpleUtils.fail("startTimeEventOptionsList is not showing", false);
			}
		}
		return startEndTimeEventOptions;
	}

	@FindBy(css = ".days-select input-field")
	private WebElement daysOfWeekOfBasicRule;
	@FindBy(css = ".days-select input-field[type=\"checkbox\"]")
	private List<WebElement> daysOptionList;

	@Override
	public void verifyDaysListShowWell() throws Exception {
		List<String> days = new ArrayList<>();
		List<String> daysInfo = new ArrayList<>(Arrays.asList("Fri", "Mon", "Sat", "Sun", "Thu", "Tue", "Wed"));
		boolean flag = true;
		if (isElementLoaded(daysOfWeekOfBasicRule, 2)) {
			clickTheElement(daysOfWeekOfBasicRule);
			if (areListElementVisible(daysOptionList, 2)) {
				for (WebElement daysOption : daysOptionList) {
					days.add(daysOption.findElement(By.cssSelector(" label")).getText().trim());
				}
			} else {
				SimpleUtils.fail("days Option List is not showing", false);
			}
		}
		for (String dayInfo : daysInfo) {
			for (String day : days) {
				if (day.equalsIgnoreCase(dayInfo)) {
					flag = true;
					SimpleUtils.pass(day + " is showing in days of week Option List");
				} else {
					flag = false;
				}
			}
		}
		if (flag) {
			SimpleUtils.pass("Days of week Option List are correct");
		} else {
			SimpleUtils.fail("Days of week Option List are NOT correct", false);
		}
	}

	@Override
	public void verifyDefaultValueAndSelectDaysForBasicStaffingRule(String day) throws Exception {
		//verify all days are selected by default
		for (WebElement daysOption : daysOptionList) {
			if (daysOption.findElement(By.cssSelector(" input")).getAttribute("class").trim().contains("ng-not-empty")) {
				SimpleUtils.pass(daysOption.findElement(By.cssSelector(" label")).getText().trim() + " is selected by default!");
			} else {
				SimpleUtils.report(daysOption.findElement(By.cssSelector(" label")).getText().trim() + " is NOT selected by default!");
			}
		}
		//select specified days
		//de-selected all checkbox first
		for (WebElement daysOption : daysOptionList) {
			if (daysOption.findElement(By.cssSelector(" input")).getAttribute("class").trim().contains("ng-not-empty")) {
				clickTheElement(daysOption.findElement(By.cssSelector(" input")));
			}
		}
		//then select specified days
		for (WebElement daysOption : daysOptionList) {
			if (daysOption.findElement(By.cssSelector(" label")).getText().trim().equals(day)) {
				clickTheElement(daysOption.findElement(By.cssSelector(" input")));
				if (daysOption.findElement(By.cssSelector(" input")).getAttribute("class").trim().contains("ng-not-empty")) {
					SimpleUtils.pass("User can select " + day + " successfully!");
				} else {
					SimpleUtils.fail("User can NOT select " + day + " successfully!", false);
				}
				break;
			} else {
				continue;
			}
		}
	}

	@FindBy(css = "span[ng-if=\"$ctrl.isFixedTime()\"]")
	private WebElement fixedTime;
	@FindBy(css = "lg-time-select-modal[dismiss=\"$dismiss\"]")
	private WebElement startAndEndTime;
	@FindBy(css = "lg-new-time-input[label=\"Open\"] input")
	private WebElement startTime;
	@FindBy(css = "lg-new-time-input[label=\"Close\"] input")
	private WebElement endTime;

	@Override
	public void setSpecifiedHours(String start, String end) throws Exception {
		selectStartTimeEvent("Specified Hours");
		if (isElementLoaded(fixedTime, 2)) {
			SimpleUtils.pass("fixed Time field is showing after select Specified Hours");
			clickTheElement(fixedTime);
			if (isElementLoaded(startAndEndTime, 2)) {
				SimpleUtils.pass("User can click fixed time field successfully");
				clickTheElement(startTime);
				startTime.clear();
				startTime.sendKeys(start);
				clickTheElement(endTime);
				endTime.clear();
				endTime.sendKeys(end);
				clickTheElement(saveButton);
				waitForSeconds(2);
				if (isElementLoaded(fixedTime, 2)) {
					if (fixedTime.getText().trim().contains(start) && fixedTime.getText().trim().contains(end)) {
						SimpleUtils.pass("User can set fixed Time successfully");
					} else {
						SimpleUtils.fail("User can NOT set fixed Time successfully", false);
					}
				}
			} else {
				SimpleUtils.fail("User can click fixed time field successfully", false);
			}
		}
	}

	@Override
	public void selectEventPointForBasicStaffingRule(String startEventPoint, String endEventPoint) throws Exception {
		clickTheElement(startEventPointOptions);
		Select select1 = new Select(startEventPointOptions);
		select1.selectByVisibleText(startEventPoint);
		clickTheElement(endEventPointOptions);
		Select select2 = new Select(endEventPointOptions);
		select2.selectByVisibleText(endEventPoint);
	}

	@Override
	public void verifyBeforeAndAfterDayPartsShouldBeSameWhenSetAsDayParts(String dayParts1, String dayParts2, String startEventPoint, String endEventPoint) throws Exception {
		selectStartTimeEvent(dayParts1);
		selectEventPointForBasicStaffingRule(startEventPoint, endEventPoint);
		selectStartTimeEvent(dayParts2);
		String bb = endTimeEventSelected.getAttribute("innerText").trim();
		if (endTimeEventSelected.getAttribute("innerText").trim().equalsIgnoreCase(dayParts2)) {
			SimpleUtils.pass("The end time event will changed to same with start time event after changing start time event when set as day-parts");
		} else {
			SimpleUtils.fail("The end time event will NOT changed to same with start time event after changing start time event when set as day-parts", false);
		}
	}

	@FindBy(css = "div:nth-child(1) > ng-include > div > question-input > div > div.lg-question-input__wrapper > h3")
	private WebElement workforceSharingGroup;
	@FindBy(css = "div.ng-scope.lg-button-group-last")
	private WebElement noBtn;

	public void verifyWorkforceSharingGroup() throws Exception {
		click(noBtn);
		if (!isElementLoaded(workforceSharingGroup, 5)) {
			SimpleUtils.pass("Workforce Sharing Group doesn't display");
		} else
			SimpleUtils.fail("Workforce Sharing Group display", false);
	}

	@FindBy(css = "div.settings-work-rule-footer-edit div:first-child")
	private WebElement crossButtonOfBasicStaffingRule;
	@FindBy(css = "div.settings-work-rule-footer-edit div:last-child")
	private WebElement checkButtonOfBasicStaffingRule;
	@FindBy(css = ".settings-work-rule-edit-edit-icon i.fa-pencil")
	private WebElement editButtonOfStaffingRule;
	@FindBy(css = "div[ng-repeat=\"rule in roleDetails\"]")
	private List<WebElement> staffingRuleList;

	public void verifyCrossAndCheckButtonOfBasicStaffingRule() throws Exception {
		if (isElementLoaded(checkButtonOfBasicStaffingRule, 2)) {
			clickTheElement(checkButtonOfBasicStaffingRule);
			if (areListElementVisible(staffingRuleList, 2) && isElementExist("div.settings-work-rule-edit-icon-container")) {
				SimpleUtils.pass("User can click check button successfully for basic staffing rule!");
			} else {
				SimpleUtils.fail("User can NOT click check button successfully for basic staffing rule!", false);
			}
		}
		waitForSeconds(2);
		if (isElementLoaded(editButtonOfStaffingRule, 2)) {
			clickTheElement(editButtonOfStaffingRule);
			if (isElementLoaded(crossButtonOfBasicStaffingRule, 2)) {
				clickTheElement(crossButtonOfBasicStaffingRule);
				if (areListElementVisible(staffingRuleList, 2) && isElementExist("div.settings-work-rule-edit-icon-container")) {
					SimpleUtils.pass("User can click cross button successfully for basic staffing rule!");
				} else {
					SimpleUtils.fail("User can NOT click cross button successfully for basic staffing rule!", false);
				}
			}
		} else {
			SimpleUtils.fail("pencil button is not showing", false);
		}
	}

	@Override
	public void clickCheckButtonOfBasicStaffingRule() throws Exception {
		if (isElementLoaded(checkButtonOfBasicStaffingRule, 2)) {
			clickTheElement(checkButtonOfBasicStaffingRule);
			if (areListElementVisible(staffingRuleList, 2) && isElementExist("div.settings-work-rule-edit-icon-container")) {
				SimpleUtils.pass("User can click check button successfully for basic staffing rule!");
			} else {
				SimpleUtils.fail("User can NOT click check button successfully for basic staffing rule!", false);
			}
		}
	}

	@FindBy(css = "div.lg-button-group div")
	private List<WebElement> badgeOption;
	@FindBy(css = "div.lg-button-group div:first-child")
	private WebElement noBadge;
	@FindBy(css = "div.lg-button-group div:nth-child(2)")
	private WebElement badgeRequired;

	@Override
	public void defaultSelectedBadgeOption() throws Exception {
		if (areListElementVisible(badgeOption, 2)) {
			for (WebElement badge : badgeOption) {
				String badgeLabel = badge.findElement(By.cssSelector(" span")).getText().trim();
				waitForSeconds(2);
				if (badge.getAttribute("class").contains("lg-button-group-selected") && badgeLabel.equalsIgnoreCase("No badge")) {
					SimpleUtils.pass(badgeLabel + " is selected by default!");
					break;
				} else {
					SimpleUtils.fail("The default selected badge option is not correct!", false);
				}
			}
		}
	}

	@FindBy(css = "lg-search[placeholder=\"Search badges\"] input")
	private WebElement searchBadgesField;
	@FindBy(css = "div.badges-list-scroll tr")
	private WebElement badgeList;

	//hasBadgeOrNot field's value is yes or no
	@Override
	public void selectBadgesOfBasicStaffingRule(String hasBadgeOrNot, String badgeName) throws Exception {
		String badgeIcon = null;
		if (areListElementVisible(badgeOption, 2)) {
			//select badgeRequired or noBadge firstly
			if (hasBadgeOrNot.equalsIgnoreCase("yes")) {
				clickTheElement(badgeRequired);
				//select the details badge for rule
				if (badgeRequired.getAttribute("class").trim().contains("lg-button-group-selected") && isElementLoaded(searchBadgesField, 2)) {
					clickTheElement(searchBadgesField);
					searchBadgesField.clear();
					searchBadgesField.sendKeys(badgeName);
					searchBadgesField.sendKeys(Keys.ENTER);
					if (areListElementVisible(badgesList, 2) && badgesList.size() > 0) {
						WebElement checkBox = badgesList.get(0).findElement(By.cssSelector("td:first-child input"));
						badgeIcon = badgesList.get(0).findElement(By.cssSelector("td:first-child svg-image g#Symbols g")).getAttribute("id").trim();
						clickTheElement(checkBox);
					}
				}
				clickCheckButtonOfBasicStaffingRule();
				waitForSeconds(2);
				if (areListElementVisible(staffingRuleList, 2)) {
					String badgeInRuleList = staffingRuleList.get(0).findElement(By.cssSelector("svg-image g#Symbols g")).getAttribute("id").trim();
					if (badgeInRuleList.equalsIgnoreCase(badgeIcon)) {
						SimpleUtils.pass("User can select badge successfully");
					} else {
						SimpleUtils.fail("User can NOT select badge successfully", false);
					}
				}
			} else {
				clickTheElement(noBadge);
			}
		}
	}

	public void verifyHistoryButtonNotDisplay() throws Exception {
		if (!isElementLoaded(historyButton, 5)) {
			SimpleUtils.pass("History button doesn't display");
		} else
			SimpleUtils.fail("History button display", false);
	}

	public void verifyHistoryButtonDisplay() throws Exception {
		if (isElementLoaded(historyButton, 5)) {
			SimpleUtils.pass("History button display");
		} else
			SimpleUtils.fail("History button doesn't display", false);
	}

	public void verifyHistoryButtonIsClickable() throws Exception {
		if (isClickable(historyButton, 2)) {
			SimpleUtils.pass("History button is clickable");
		} else
			SimpleUtils.fail("History button is not clickable", false);
	}

	public void verifyCloseIconNotDisplayDefault() throws Exception {
		if (!isElementLoaded(closeIcon, 5)) {
			SimpleUtils.pass("Close icon doesn't display default");
		} else
			SimpleUtils.fail("Close icon display default", false);
	}

	@FindBy(css = "img.lg-slider-pop__title-dismiss")
	private WebElement closeIcon;

	public void clickHistoryAndClose() throws Exception {
		click(historyButton);
		if (isElementLoaded(closeIcon, 5)) {
			click(closeIcon);
			if (!isElementLoaded(closeIcon, 5)) {
				SimpleUtils.pass("Close history successfully");
			} else
				SimpleUtils.fail("Close history failed", false);
		} else
			SimpleUtils.fail("Close icon load failed", false);
	}

	public void goToItemInConfiguration(String item) throws Exception {
		scrollToElement(getDriver().findElement(By.cssSelector("lg-dashboard-card[title = \"" + item + "\"]")));
		click(getDriver().findElement(By.cssSelector("lg-dashboard-card[title = \"" + item + "\"]")));
		waitForSeconds(5);
	}

	@Override
	public void setLeaveThisPageButton() throws Exception {
		if (isElementLoaded(warningToast) && isElementLoaded(leaveThisPageButton))
			clickTheElement(leaveThisPageButton);
		waitForSeconds(5);
	}

	//added by Fiona
	@Override
	public void selectConditionMaxMinExactly(String condition) throws Exception {
		Select select = new Select(conditionMaxMinExactly);
		select.selectByVisibleText(condition);
	}

	@Override
	public void selectWorkRoleOfBasicStaffingRule(String workRoleName) throws Exception {
		if (isElementLoaded(workRoleSelect, 3)) {
			Select select = new Select(workRoleSelect);
			List<WebElement> workRoleList = select.getOptions();
			for (WebElement work : workRoleList) {
				String workRole = work.getText().trim();
				if (workRole.equalsIgnoreCase(workRoleName)) {
					select.selectByVisibleText(workRoleName);
				}
			}
		}
	}

	@Override
	public void addSkillCoverageBasicStaffingRule() throws Exception {
		selectWorkRoleOfBasicStaffingRule("Any");
		Select select = new Select(conditionMaxMinExactly);
		WebElement selected = select.getFirstSelectedOption();
		if (selected.getText().trim().equalsIgnoreCase("A Minimum")) {
			SimpleUtils.pass("When select work role as Any, it will selected as A Minimum automatically!");
		} else {
			SimpleUtils.fail("When select work role as Any, it will NOT selected as A Minimum automatically!", false);
		}
	}

	//select End Time Event
	@Override
	public void selectEndTimeEvent(String endTimeEvent) throws Exception {
		if (isElementLoaded(conditionMaxMinExactly, 2)) {
			clickTheElement(endTimeEventOptions);
			if (areListElementVisible(endTimeEventOptionsList, 3)) {
				for (WebElement w : endTimeEventOptionsList) {
					if (w.getAttribute("innerText").trim().equalsIgnoreCase(endTimeEvent)) {
						clickTheElement(w);
						break;
					}
				}
			}
			if (endTimeEventSelected.getAttribute("innerText").trim().equalsIgnoreCase(endTimeEvent)) {
				SimpleUtils.pass("User select end Time Event successfully!");
			} else {
				SimpleUtils.fail("User failed to select end Time Event!", false);
			}
		}
	}

	@Override
	public void selectUnitOptionsOfBasicStaffingRule(String unit) throws Exception {
		Select select = new Select(unitOptions);
		select.selectByVisibleText(unit);
	}

	public void inputNumberOfWorkRoleForBasicStaffingRule(String number) throws Exception {
		if (isElementLoaded(numberInput, 2)) {
			numberInput.click();
			numberInput.clear();
			numberInput.sendKeys(number);
		} else {
			SimpleUtils.fail("Number of Work Role For Basic Staffing Rule is not showing.", false);
		}
	}

	@Override
	public void inputStartOffsetMinutesOfBasicStaffingRule(String startOffset) throws Exception {
		if (isElementLoaded(startOffsetMinutes, 3)) {
			clickTheElement(startOffsetMinutes);
			startOffsetMinutes.clear();
			startOffsetMinutes.sendKeys(startOffset);
			if (saveRuleIcon.getAttribute("class").contains("enabled")) {
				SimpleUtils.pass("User can input number successfully!");
			} else {
				SimpleUtils.fail("User can not input number successfully!", false);
			}
		}
	}

	@Override
	public void inputEndOffsetMinutesOfBasicStaffingRule(String endOffset) throws Exception {
		if (isElementLoaded(endOffsetMinutes, 3)) {
			clickTheElement(endOffsetMinutes);
			endOffsetMinutes.clear();
			endOffsetMinutes.sendKeys(endOffset);
			if (saveRuleIcon.getAttribute("class").contains("enabled")) {
				SimpleUtils.pass("User can input number successfully!");
			} else {
				SimpleUtils.fail("User can not input number successfully!", false);
			}
		}
	}

	@Override
	public void selectStartEventPointForBasicStaffingRule(String startEventPoint) throws Exception {
		clickTheElement(startEventPointOptions);
		Select select1 = new Select(startEventPointOptions);
		select1.selectByVisibleText(startEventPoint);
	}

	@Override
	public void selectEndEventPointForBasicStaffingRule(String endEventPoint) throws Exception {
		clickTheElement(endEventPointOptions);
		Select select2 = new Select(endEventPointOptions);
		select2.selectByVisibleText(endEventPoint);
	}

	@Override
	public void selectDaysForBasicStaffingRule(List<String> days) throws Exception {
		//select specified days
		//de-selected all checkbox first
		for (WebElement daysOption : daysOptionList) {
			if (daysOption.findElement(By.cssSelector(" input")).getAttribute("class").trim().contains("ng-not-empty")) {
				clickTheElement(daysOption.findElement(By.cssSelector(" input")));
			}
		}
		//then select specified days
		for (String day : days) {
			for (WebElement daysOption : daysOptionList) {
				String dayValue = daysOption.findElement(By.cssSelector(" label:first-child")).getAttribute("innerText").trim();
				if (dayValue.equalsIgnoreCase(day)) {
					clickTheElement(daysOption.findElement(By.cssSelector(" input")));
					if (daysOption.findElement(By.cssSelector(" input")).getAttribute("class").trim().contains("ng-not-empty")) {
						SimpleUtils.pass("User can select " + day + " successfully!");
					} else {
						SimpleUtils.fail("User can NOT select " + day + " successfully!", false);
					}
					break;
				} else {
					continue;
				}
			}
		}
	}


	@Override
	public void createBasicStaffingRule(String startTimeEvent, String endTimeEvent, String startEventPoint, String endEventPoint,
										String workRoleName, String unit, String condition, List<String> days, String number,
										String startOffset, String endOffset) throws Exception {
		selectStartTimeEvent(startTimeEvent);
		selectStartEventPointForBasicStaffingRule(startEventPoint);
		selectEndTimeEvent(endTimeEvent);
		selectEndEventPointForBasicStaffingRule(endEventPoint);
		selectWorkRoleOfBasicStaffingRule(workRoleName);
		selectUnitOptionsOfBasicStaffingRule(unit);
		selectConditionMaxMinExactly(condition);
		selectDaysForBasicStaffingRule(days);
		inputNumberOfWorkRoleForBasicStaffingRule(number);
		inputStartOffsetMinutesOfBasicStaffingRule(startOffset);
		inputEndOffsetMinutesOfBasicStaffingRule(endOffset);
		clickCheckButtonOfBasicStaffingRule();
	}

	@FindBy(css = "div[ng-repeat=\"rule in roleDetails\"] div[ng-if=\"ifStaffingRule()\"]")
	private List<WebElement> basicStaffingRulesInList;
	@FindBy(css = "div[ng-if=\"ifStaffingRule()\"] span.setting-work-rule-staffing-time-constraint:first-child")
	private WebElement timeConstraint;
	@FindBy(css = "div[ng-if=\"ifStaffingRule()\"] span.setting-work-rule-staffing-limit-constraint:nth-child(2)")
	private WebElement limitConstraint;
	@FindBy(css = "div[ng-if=\"ifStaffingRule()\"] span.setting-work-rule-staffing-numeric-value:nth-child(4)")
	private WebElement workRoleNumbers;
	@FindBy(css = "div[ng-if=\"ifStaffingRule()\"] span.setting-work-rule-staffing-text")
	private WebElement workRoleAndUnit;
	@FindBy(css = "div[ng-if=\"ifStaffingRule()\"] span.setting-work-rule-staffing-limit-constraint:nth-child(7)")
	private WebElement timeUnit;
	@FindBy(css = "div[ng-if=\"ifStaffingRule()\"] span.setting-work-rule-staffing-limit-constraint:nth-child(9)")
	private WebElement daysValue;

	@Override
	public void verifyBasicStaffingRuleIsCorrectInRuleList(String startTimeEvent, String endTimeEvent, String startEventPoint, String endEventPoint,
														   String workRoleName, String unit, String condition, List<String> days, String number,
														   String startOffset, String endOffset) throws Exception {
		if (areListElementVisible(basicStaffingRulesInList, 3)) {
			//Start at 30 minutes after Opening Operating Hours,end at 40 minutes after Opening Operating Hours
			String[] timeConstraintStr = timeConstraint.getText().trim().split(",");

			//Start at 30 minutes after Opening Operating Hours
			String[] startTimeConstraintStr = timeConstraintStr[0].split(" ");
			String startTimeEventStr = timeConstraintStr[0].substring(26);
			if (startTimeConstraintStr[2].equalsIgnoreCase(startOffset) && startTimeConstraintStr[4].equalsIgnoreCase(startEventPoint)
					&& startTimeEventStr.equalsIgnoreCase(startTimeEvent)) {
				SimpleUtils.pass("Start offSet, start Event Point and start Time Event is correct!");
			} else {
				SimpleUtils.fail("Start offSet, start Event Point and start Time Event is NOT correct!", false);
			}

			//end at 40 minutes after Opening Operating Hours
			String[] endTimeConstraintStr = timeConstraintStr[1].split(" ");
			String endTimeEventStr = timeConstraintStr[1].substring(26);
			if (endTimeConstraintStr[3].equalsIgnoreCase(endOffset) && endTimeConstraintStr[5].equalsIgnoreCase(endEventPoint)
					&& endTimeEventStr.equalsIgnoreCase(endTimeEvent)) {
				SimpleUtils.pass("End offSet, end Event Point and end Time Event is correct!");
			} else {
				SimpleUtils.fail("End offSet, end Event Point and end Time Event is NOT correct!", false);
			}

			//limitConstraintStr is such as a maximum
			String limitConstraintStr = limitConstraint.getText().trim();
			if (limitConstraintStr.equalsIgnoreCase(condition)) {
				SimpleUtils.pass("Condition is correct in rule list");
			} else {
				SimpleUtils.fail("Condition is NOT correct in rule list", false);
			}

			String workerNumber = workRoleNumbers.getText().trim();
			if (workerNumber.equalsIgnoreCase(number)) {
				SimpleUtils.pass("Number of work role is correct in rule list");
			} else {
				SimpleUtils.fail("Number of work role is NOT correct in rule list", false);
			}

			//ANY/workRoleName shifts should be scheduled
			String workRoleNameStr = workRoleAndUnit.getText().trim().split(" ")[0];
			String unitStr = workRoleAndUnit.getText().trim().split(" ")[1];
			if (workRoleNameStr.equalsIgnoreCase(workRoleName) && unitStr.equalsIgnoreCase(unit)) {
				SimpleUtils.pass("work Role Name and unit can show correctly in rule list");
			} else {
				SimpleUtils.fail("work Role Name and unit can NOT show correctly in rule list", false);
			}

			//Sun, Mon, Tue, Wed, Thu, Fri, Sat
			String[] daysStr = daysValue.getText().trim().split(",");
			String[] daysStr1 = new String[2];
			for (int i = 0; i < daysStr.length; i++) {
				daysStr1[i] = daysStr[i].trim();
			}
			List<String> daysStr2 = new ArrayList<>(Arrays.asList(daysStr1));
			if (ListUtils.isEqualList(daysStr2, days)) {
				SimpleUtils.pass("Days is correct in rule list");
			} else {
				SimpleUtils.fail("Days is NOT correct in rule list", false);
			}
		}
	}

	@Override
	public void verifySkillCoverageBasicStaffingRuleInList() throws Exception {
		if (areListElementVisible(basicStaffingRulesInList, 3)) {
			String workRoleNameStr = workRoleAndUnit.getText().trim().split(" ")[0];
			if (workRoleNameStr.equalsIgnoreCase("any")) {
				SimpleUtils.pass("Skill coverage rule can show correctly in rule list");
			} else {
				SimpleUtils.fail("Skill coverage rule can NOT show correctly in rule list", false);
			}
		}

	}

	@FindBy(css = "img.setting-rule-delete-icon")
	private WebElement ruleDeleteIcon;
	@FindBy(css = "span.settings-work-rule-edit-edit-icon")
	private List<WebElement> editButtonListInRuleList;

	@Override
	public void verifySkillCoverageBasicStaffingRule(String workRole1, String workRole2) throws Exception {
		//add skill coverage rule for one work role, other work role will show
		int beforeBasicStaffingRuleCount = 0;
		int afterBasicStaffingRuleCount = 0;
		selectWorkRoleToEdit(workRole1);
		checkTheEntryOfAddBasicStaffingRule();
		verifyStaffingRulePageShowWell();
		addSkillCoverageBasicStaffingRule();
		clickCheckButtonOfBasicStaffingRule();
		verifySkillCoverageBasicStaffingRuleInList();
		beforeBasicStaffingRuleCount = editButtonListInRuleList.size();
		clickTheElement(saveButtonOnBasicStaffingRule);
		waitForSeconds(3);
		selectWorkRoleToEdit(workRole2);
		verifySkillCoverageBasicStaffingRuleInList();
		clickTheElement(ruleDeleteIcon);
		waitForSeconds(2);
		clickTheElement(saveButtonOnBasicStaffingRule);
		selectWorkRoleToEdit(workRole1);
		afterBasicStaffingRuleCount = editButtonListInRuleList.size();
		if (beforeBasicStaffingRuleCount - afterBasicStaffingRuleCount == 1) {
			SimpleUtils.pass("User can add/delete Skill Coverage Basic Staffing Rule successfully!");
		} else {
			SimpleUtils.fail("User can NOT add/delete Skill Coverage Basic Staffing Rule successfully!", false);
		}
	}

	@Override
	public void removeAllDemandDriverTemplates() throws Exception {
		int templateCount = templateNameList.size();
		if (isTemplateListPageShow()) {
			SimpleUtils.pass("Demand Driver template list is showing now");
			for (int i = 0; i < templateCount; i++) {
				archiveOrDeleteTemplate(templateNameList.get(0).getText());
			}
		} else {
			SimpleUtils.fail("Demand Driver Template list is not loaded well", false);
		}
	}

	@Override
	public void clickAddOrEditForDriver(String addOrEdit) throws Exception {
		if ("Add".equalsIgnoreCase(addOrEdit)) {
			if (isElementLoaded(addBtnForDriver, 5))
				clickTheElement(addBtnForDriver);
		} else if ("Edit".equalsIgnoreCase(addOrEdit)) {
			if (isElementLoaded(editBtnForDriver, 5))
				click(editBtnForDriver);
		} else {
			SimpleUtils.fail("Please choose add or edit mode!", false);
		}
		if (isElementLoaded(warningToast) && isElementLoaded(leaveThisPageButton))
			clickTheElement(leaveThisPageButton);
	}

	@FindBy(css = "input-field[options=\"$ctrl.inputStreamOptions\"]")
	private WebElement inputStreamSelect;

	@Override
	public List<String> getInputStreamInDrivers() throws Exception {
		List<String> streamNameList = new ArrayList<>();
		Select select = null;

		if (isElementLoaded(inputStreamSelect)) {
			scrollToElement(inputStreamSelect);
			select = new Select(inputStreamSelect.findElement(By.cssSelector("select")));
			for (int i = 0; i < select.getOptions().size(); i++) {
				if (!select.getOptions().get(i).getText().equals("")) {
					streamNameList.add(select.getOptions().get(i).getText());
				}
			}
		} else {
			SimpleUtils.fail("No input stream select show up!", false);
		}
		return streamNameList;
	}


	@FindBy(css = "[title=\"Minors Rules\"] div")
	private WebElement minorRulesTile;

	@Override
	public void verifyMinorRulesTileIsLoaded() throws Exception {
		if (isElementLoaded(minorRulesTile, 10)) {
			String textOnTile1 = "Scheduling Rules for Minors";
			String textOnTile2 = "Min/Max Hours for school days and non-school days";
			String textOnTile3 = "Min/Max Hours for school weeks and non-school weeks";
			String textOnTile4 = "Meal and Rest break rules for minors";
			String messageOnTile = minorRulesTile.findElement(By.className("lg-dashboard-card__body")).getText();
			if (messageOnTile.contains(textOnTile1)
					&& messageOnTile.contains(textOnTile2)
					&& messageOnTile.contains(textOnTile3)
					&& messageOnTile.contains(textOnTile4)) {
				SimpleUtils.pass("The message on the Minor Rule tile display correctly! ");
			} else
				SimpleUtils.fail("The message on the Minor Rule tile display incorrectly! ", false);
		} else
			SimpleUtils.fail("Minor Rules tile fail to loaded! ", false);
	}


	@FindBy(css = "[form-title=\"Minor Schedule by Week\"]")
	private WebElement minorScheduleByWeekSection;

	@FindBy(css = "[form-title=\"Minor Schedule by Day\"]")
	private WebElement minorScheduleByDaySection;

	public boolean checkIfMinorSectionsLoaded() throws Exception {
		boolean ifSectionLoaded = false;
		if (isElementLoaded(minorScheduleByWeekSection, 5)
				&& isElementLoaded(minorScheduleByDaySection, 5)) {
			ifSectionLoaded = true;
			SimpleUtils.pass("The sections display correctly on the minor template page! ");
		} else
			SimpleUtils.report("The sections display incorrectly on the minor template page! ");
		return ifSectionLoaded;
	}

	public void clickOnBackButton() throws Exception {
		if (isElementLoaded(backButton, 5)) {
			clickTheElement(backButton);
			if (isElementEnabled(leaveThisPageButton)) {
				clickTheElement(leaveThisPageButton);
				waitForSeconds(2);
			}
			SimpleUtils.pass("Click back button successfully! ");
		} else
			SimpleUtils.fail("Back button fail to loaded! ", false);
	}

	@Override
	public void verifyTheContentOnSpecificCard(String cardName, List<String> content) throws Exception {
		if (areListElementVisible(configurationCards, 5) && configurationCards.size() > 0) {
			for (WebElement card : configurationCards) {
				if (card.findElement(By.tagName("h1")).getText().equalsIgnoreCase(cardName)) {
					WebElement body = card.findElement(By.className("lg-dashboard-card__body"));
					String bodyText = body.getText();
					String[] temp = bodyText.split("\n");
					List<String> actualContent = Arrays.asList(temp);
					if (actualContent.containsAll(content) && content.containsAll(actualContent)) {
						SimpleUtils.pass("The content on card: " + cardName + " is correct!");
					} else {
						SimpleUtils.fail("The content on card: " + cardName + " is incorrect!", false);
					}
					break;
				}
			}
		} else {
			SimpleUtils.fail("Cards failed to load on Configuration page!", false);
		}
	}

	@FindBy(css = "[question-title=\"Strictly enforce minor violations?\"] yes-no")
	private WebElement yesNoForStrictlyEnforceMinorViolations;

	@Override
	public void setStrictlyEnforceMinorViolations(String yesOrNo) throws Exception {
		if (isElementLoaded(yesNoForStrictlyEnforceMinorViolations, 10)) {
			scrollToElement(yesNoForStrictlyEnforceMinorViolations);
			if (yesOrNo.equalsIgnoreCase("yes")) {
				if (isElementLoaded(yesNoForStrictlyEnforceMinorViolations.findElement(By.cssSelector(".lg-button-group-first")), 10)) {
					click(yesNoForStrictlyEnforceMinorViolations.findElement(By.cssSelector(".lg-button-group-first")));
					SimpleUtils.pass("Turned on 'Strictly enforce minor violations?' setting successfully! ");
				} else {
					SimpleUtils.fail("Yes button fail to load!", false);
				}
			} else if (yesOrNo.equalsIgnoreCase("no")) {
				if (isElementLoaded(yesNoForStrictlyEnforceMinorViolations.findElement(By.cssSelector(".lg-button-group-last")), 10)) {
					click(yesNoForStrictlyEnforceMinorViolations.findElement(By.cssSelector(".lg-button-group-last")));
					SimpleUtils.pass("Turned off 'Strictly enforce minor violations?' setting successfully! ");
				} else {
					SimpleUtils.fail("No button fail to load!", false);
				}
			} else {
				SimpleUtils.warn("You have to input the right command: yes or no");
			}
		} else {
			SimpleUtils.fail("'Strictly enforce minor violations?' setting is not loaded!", false);
		}
	}


	@Override
	public boolean isStrictlyEnforceMinorViolationSettingEnabled() throws Exception {
		boolean isStrictlyEnforceMinorViolationSettingEnabled = false;
		if (isElementLoaded(yesNoForStrictlyEnforceMinorViolations, 25)) {
			if (yesNoForStrictlyEnforceMinorViolations.
					findElement(By.cssSelector(".lg-button-group-first")).getAttribute("class").contains("selected")) {
				isStrictlyEnforceMinorViolationSettingEnabled = true;
			}
		} else
			SimpleUtils.fail("Strictly enforce minor violation setting fail to load! ", false);
		return isStrictlyEnforceMinorViolationSettingEnabled;
	}

	@FindBy(css = "question-input[question-title=\"Can a manager add another locations' employee in schedule before the employee's home location has published the schedule?\"] input-field")
	private WebElement canManagerAddAnotherLocationsEmployeeInSchedule;

	@Override
	public void updateCanManagerAddAnotherLocationsEmployeeInScheduleBeforeTheEmployeeHomeLocationHasPublishedTheSchedule(String option) throws Exception {

		WebElement confSelect = canManagerAddAnotherLocationsEmployeeInSchedule.findElement(By.cssSelector("select"));
		if (isElementLoaded(confSelect, 5)) {
			selectByVisibleText(confSelect, option);
			displaySuccessMessage();
		} else {
			SimpleUtils.fail("Can a manager add another locations' employee in schedule before the employee's home location has published the schedule? input field not loaded.", false);
		}
	}

	@FindBy(css = "question-input[question-title=\"Labor Preferences for Forecast Summary Smartcard\"] select[ng-change=\"$ctrl.handleChange()\"]")
	private WebElement laborPreferencesForForecastSummarySmartcardSettingDropdown;

	@Override
	public void updateLaborPreferencesForForecastSummarySmartcardSettingDropdownOption(String option) throws Exception {
		if (isElementLoaded(laborPreferencesForForecastSummarySmartcardSettingDropdown, 10)) {
			Select dropdown = new Select(laborPreferencesForForecastSummarySmartcardSettingDropdown);
			dropdown.selectByVisibleText(option);
			SimpleUtils.pass("OP Page: Global Configuration: Schedules : Labor Preferences for Forecast Summary Smartcard settings been changed successfully");
		} else {
			SimpleUtils.fail("OP Page: Global Configuration: Schedules : Labor Preferences for Forecast Summary Smartcard settings dropdown list not loaded.", false);
		}
	}

	@FindBy(css = "lg-template-forecast-source question-input")
	private List<WebElement> forecastConfigurations;
	@FindBy(css = "tr[ng-repeat*=\"item in $ctrl.forecastSourceData.aggregated\"]")
	private List<WebElement> aggregatedFields;

	@Override
	public void verifyForDerivedDemandDriverUI(String derivedType, String remoteType) throws Exception {
		Select sourceSelect = null;
		Select remoteTypeSelect = null;

		if (derivedType.equals("Legion ML") || derivedType.equals("Imported")) {
			SimpleUtils.fail("It's not a derived demand driver type!", false);
		}
		if (areListElementVisible(forecastConfigurations)) {
			if (forecastConfigurations.get(0).getAttribute("question-title").contains("Forecast Source")) {
				sourceSelect = new Select(forecastConfigurations.get(0).findElement(By.cssSelector("select")));
				sourceSelect.selectByVisibleText(derivedType);

				if (forecastConfigurations.size() > 1
						&& forecastConfigurations.get(1).getAttribute("question-title").contains("Input Stream")) {
					SimpleUtils.fail("Input Stream should not show up for Derived demand driver!", false);
				}
				if (derivedType.equals("Remote")) {
					if (forecastConfigurations.get(1).getAttribute("question-title").contains("Remote Location")
							&& forecastConfigurations.get(1).getAttribute("question-title").contains("Parent Location")) {
						remoteTypeSelect = new Select(forecastConfigurations.get(1).findElement(By.cssSelector("select")));
						remoteTypeSelect.selectByVisibleText(remoteType);
						if (remoteType.equals("Remote Location")
								&& forecastConfigurations.get(2).getAttribute("question-title").contains("Remote Location")) {
							SimpleUtils.pass("Remote:Remote Location demand driver UI is correct!");
						} else if (remoteType.equals("Parent Location")
								&& forecastConfigurations.get(2).getAttribute("question-title").contains("Parent Level")) {
							SimpleUtils.pass("Remote:Parent Location demand driver UI is correct!");
						} else {
							SimpleUtils.fail("Please check the remote type or the UI field!", false);
						}
					} else {
						SimpleUtils.fail("Remote demand driver UI is not correct!", false);
					}
				} else if (derivedType.equals("Distributed")) {
					if (forecastConfigurations.get(1).getAttribute("question-title").equals("Source Demand Driver")
							&& forecastConfigurations.get(2).getAttribute("question-title").equals("Distribution of Demand Driver")) {
						SimpleUtils.pass("Distributed demand driver UI is correct!");
					} else {
						SimpleUtils.fail("Distributed demand driver UI is not correct!", false);
					}
				} else if (derivedType.equals("Aggregated")) {
					if (aggregatedFields != null && aggregatedFields.size() > 0) {
						SimpleUtils.pass("Aggregated demand driver UI is correct!");
					} else {
						SimpleUtils.fail("Aggregated demand driver UI is not correct!", false);
					}
				} else {
					SimpleUtils.fail("Derived demand driver type not exist!", false);
				}
			}
		}
	}

	@Override
	public void clickOnCancelButton() throws Exception {
		if (isElementLoaded(cancelButton)) {
			clickTheElement(cancelButton);
			setLeaveThisPageButton();
		}
	}

	@Override
	public void clickHistoryButton() throws Exception {
		if (isElementLoaded(historyButton, 3)) {
			clickTheElement(historyButton);
			waitForSeconds(5);
			if (isElementLoaded(closeIcon, 2)) {
				SimpleUtils.pass("User can click history Button successfully");
			} else {
				SimpleUtils.fail("User can't click history Button", false);
			}
		}
	}

	@FindBy(css = "div.lg-slider-pop__content li.new-audit-log")
	private List<WebElement> historyRecordsList;

	@Override
	public void verifyRecordIsClickable() throws Exception {
		if (areListElementVisible(historyRecordsList, 3)) {
			WebElement templateName = historyRecordsList.get(0).findElement(By.cssSelector("div.templateInfo"));
			clickTheElement(templateName);
			waitForSeconds(4);
			if (isElementEnabled(templateTitleOnDetailsPage) && isElementEnabled(closeBTN) && isElementEnabled(templateDetailsAssociateTab)
					&& isElementEnabled(templateDetailsPageForm) && !isElementEnabled(closeIcon)) {
				SimpleUtils.pass("User can navigate to template details page via history panel");
			} else {
				SimpleUtils.fail("User can't navigate to template details page via history panel", false);
			}
		}
	}

	@FindBy(css = "h1.lg-slider-pop__title")
	private WebElement historyPanelHeader;
	@FindBy(css = "div.lg-slider-pop")
	private WebElement historyPanel;

	@Override
	public void verifyTemplateHistoryUI() throws Exception {
		if (isElementEnabled(historyPanelHeader, 2) && areListElementVisible(historyRecordsList, 2)
				&& isElementEnabled(historyPanel, 2)) {
			SimpleUtils.pass("Template history panel can show well");
		} else {
			SimpleUtils.fail("Template history panel can't show well", false);
		}
	}

	@FindBy(css = "question-input[question-title=\"Maximum Number of shifts an employee can have in one day.\"] input")
	private WebElement maximumNumberOfShiftsPerDay;

	@Override
	public void updateMaximumNumberOfShiftsPerDay(int maximumNumber) throws Exception {
		if (isElementLoaded(maximumNumberOfShiftsPerDay, 10)) {
			maximumNumberOfShiftsPerDay.clear();
			maximumNumberOfShiftsPerDay.sendKeys(String.valueOf(maximumNumber));
			SimpleUtils.pass("OP Page: Set maximum number of shifts per day successfully");
		} else {
			SimpleUtils.fail("OP Page: Maximum number of shifts per day fail to load! ", false);
		}
	}

	@Override
	public void verifyTemplateHistoryContent() throws Exception {
		if (areListElementVisible(historyRecordsList, 2)) {
			String content1 = historyRecordsList.get(0).findElement(By.cssSelector("div.templateInfo")).getText().trim();
			String content2 = historyRecordsList.get(0).findElement(By.cssSelector("p")).getText().trim();
			if ((content1.contains("Edited") || content1.contains("Deleted") || content1.contains("Published") || content1.contains("Created")
					|| content1.contains("set to publish on")) && content1.contains("Version") && content2.contains("at") && content2.contains("/2023")) {
				SimpleUtils.pass("History Content can show well");
			} else {
				SimpleUtils.fail("History Content can't show well", false);
			}
		}
	}

	@Override
	public void verifyOrderOfTheTemplateHistory() throws Exception {
		String format = "hh:mm:ss a','MM/dd/yyyy";
		boolean isAsc = true;
		List<String> dateStrings = new ArrayList<>();
		if (areListElementVisible(historyRecordsList, 2)) {
			for (WebElement historyRecord : historyRecordsList) {
				String str = historyRecord.findElement(By.cssSelector("p")).getText().trim();
				String strBefore = str.substring(0, str.indexOf("at"));
				//date is 19:00:37 PM,07/22/2022
				String dateTime = str.substring(strBefore.length() + 3, str.length());
				dateStrings.add(dateTime);
			}
		}

		if (isSorted(dateStrings, isAsc, format)) {
			SimpleUtils.pass("The template history are in ascend order");
		} else {
			SimpleUtils.fail("The template history are in descend order", false);
		}
	}

	@Override
	public void verifyNewTemplateIsClickable() throws Exception {
		if (isElementLoaded(newTemplateBTN, 10)) {
			if (isClickable(newTemplateBTN, 10)) {
				SimpleUtils.pass("can create a new template");
			} else {
				SimpleUtils.fail("can not create a new template", false);
			}
		}
	}

	@Override
	public void verifyNewCreatedTemplateHistoryContent(String option, String userName, String time) throws Exception {
		//Create new template's history checking
		if (areListElementVisible(historyRecordsList, 2) && option.contains("Created")) {
			String format = "hh:mm:ss a','MM/dd/yyyy";
			//content1  Template Edited ( Vrsion 1 )
			String content1 = historyRecordsList.get(0).findElement(By.cssSelector("div.templateInfo")).getText().trim();
			//content2   auto6 AD6 at 01:27:30 AM,07/28/2022
			String content2 = historyRecordsList.get(0).findElement(By.cssSelector("p")).getText().trim();
			String strBefore = content2.substring(0, content2.indexOf("at"));
			//date is 19:00:37 PM,07/22/2022
			String dateTime = content2.substring(strBefore.length() + 3, content2.length());
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			Date date = sdf.parse(dateTime);
			Date date1 = sdf.parse(time);

			if ((content1.contains("Edited") && content1.contains("Version 1") && content2.contains(userName) && content2.contains("at"))) {
				SimpleUtils.pass("Create new template history Content can show well");
			} else {
				SimpleUtils.fail("Create new template history Content can't show well", false);
			}
			//content3  Template Created ( Version 1 )
			String content3 = historyRecordsList.get(1).findElement(By.cssSelector("div.templateInfo")).getText().trim();
			if (historyRecordsList.size() == 2 && content3.contains("Version 1") && content3.contains("Created")) {
				SimpleUtils.pass("Created new template will create " + historyRecordsList.size() + " history records");
			} else {
				SimpleUtils.fail("Created new template's history record is not correct", false);
			}
		} else {
			SimpleUtils.report("There is no record for now.");
		}
	}

	@Override
	public void closeTemplateHistoryPanel() {
		if (isElementEnabled(closeIcon, 2)) {
			clickTheElement(closeIcon);
		}
		waitForSeconds(2);
	}

	@FindBy(css = "question-input[question-title*=\"Schedule Planning Window\"] input")
	WebElement firstFieldInSchedulePolicyTemplate;

	@Override
	public void updateSchedulePolicyTemplateFirstField(String count) {
		if (isElementEnabled(firstFieldInSchedulePolicyTemplate, 2)) {
			clickTheElement(firstFieldInSchedulePolicyTemplate);
			firstFieldInSchedulePolicyTemplate.clear();
			firstFieldInSchedulePolicyTemplate.sendKeys(count);
		}
	}

	//Published template history checking
	public void verifyPublishTemplateHistoryContent(String option, String userName) throws Exception {
		if (areListElementVisible(historyRecordsList, 2) && option.contains("Published")) {
			//content1  Template Published ( Version 1 )
			String content1 = historyRecordsList.get(0).findElement(By.cssSelector("div.templateInfo")).getText().trim();
			//content2   auto6 AD6 at 01:27:30 AM,07/28/2022
			String content2 = historyRecordsList.get(0).findElement(By.cssSelector("p")).getText().trim();
			if ((content1.contains(option) && content1.contains("Version 1") && content2.contains(userName))) {
				SimpleUtils.pass("Publish template history can show well");
			} else {
				SimpleUtils.fail("Publish template history can't show well", false);
			}
			if (historyRecordsList.size() == 3) {
				SimpleUtils.pass("Publish template history record can show well");
			} else {
				SimpleUtils.fail("Publish template history record can't show well", false);
			}
		} else {
			SimpleUtils.report("There is no record for now.");
		}
	}

	@FindBy(css = "question-input[question-title*=\"Schedule Planning Window\"] ng-form div")
	private WebElement firstFieldValueInSchedulePolicyTemplate;

	@Override
	public void verifyUpdatedSchedulePolicyTemplateFirstFieldCorrectOrNot(String count) {
		if (isElementEnabled(firstFieldValueInSchedulePolicyTemplate, 2)) {
			String value = firstFieldValueInSchedulePolicyTemplate.getAttribute("innerText").trim();
			if (value.equals(count)) {
				SimpleUtils.pass("User can go to correct template via template history list and template value is correct");
			} else {
				SimpleUtils.fail("User can go to correct template via template history list", false);
			}
		}
	}

	@Override
	public void publishAtDifferentTimeForTemplate(String button, int date) throws Exception {
		waitForSeconds(2);
		scrollToBottom();
		chooseSaveOrPublishBtnAndClickOnTheBtn(button);
		if (isElementLoaded(dateOfPublishPopup, 2)) {
			clickTheElement(effectiveDate);
			setEffectiveDate(date);
			clickTheElement(okButtonOnFuturePublishConfirmDialog);
		} else {
			SimpleUtils.fail("The future publish template confirm dialog is not displayed.", false);
		}
	}

	@Override
	public void openCurrentPublishInMultipleTemplate(String templateName) throws Exception {
		expandMultipleVersionTemplate(templateName);
		if (areListElementVisible(multipleTemplateList, 2)) {
			//Create draft version for current published template
			clickTheElement(currentPublishedTemplate.findElement(By.cssSelector(" button")));
			waitForSeconds(10);
			if (isElementEnabled(templateTitleOnDetailsPage) && isElementEnabled(closeBTN) && isElementEnabled(templateDetailsAssociateTab)
					&& isElementEnabled(templateDetailsPageForm)) {
				SimpleUtils.pass("User can open " + templateName + " template succseefully");
			} else {
				SimpleUtils.fail("User open " + templateName + " template failed", false);
			}
		}
	}

	@Override
	//Published at different time - template history checking
	public void verifyPublishFutureTemplateHistoryContent(String option, String userName) throws Exception {
		if (areListElementVisible(historyRecordsList, 2) && option.contains("set to publish on")) {
			//publish future history content
			//content1  Template set to publish on 08/09/2022 ( Version 2 )
			String content1 = historyRecordsList.get(0).findElement(By.cssSelector("div.templateInfo")).getText().trim();
			//content2   FionaUsing Feng at 14:57:36 PM,08/01/2022
			String content2 = historyRecordsList.get(0).findElement(By.cssSelector("p")).getText().trim();
			if ((content1.contains(option) && content1.contains("Version 2") && content2.contains(userName))) {
				SimpleUtils.pass("Publish at different time template history can show well");
			} else {
				SimpleUtils.fail("Publish at different time template history can't show well", false);
			}
			//click edit button from published template of version 1
			//content3  Template Edited ( Version 2 )
			String content3 = historyRecordsList.get(1).findElement(By.cssSelector("div.templateInfo")).getText().trim();
			//content4   FionaUsing Feng at 15:21:08 PM,08/01/2022
			String content4 = historyRecordsList.get(1).findElement(By.cssSelector("p")).getText().trim();
			if ((content3.contains("Edit") && content3.contains("Version 2") && content4.contains(userName))) {
				SimpleUtils.pass("Histroy of clicking edit button from published template can show well");
			} else {
				SimpleUtils.fail("Histroy of clicking edit button from published template can't show well", false);
			}
			if (historyRecordsList.size() == 5) {
				SimpleUtils.pass("Publish at different time for a template - action history record can show well");
			} else {
				SimpleUtils.fail("Publish at different time for a template - action history record can't show well", false);
			}
		} else {
			SimpleUtils.report("There is no record for now.");
		}
	}

	@Override
	public void clickOnArchiveButton() throws Exception {
		verifyArchivePopUpShowWellOrNot();
		clickTheElement(okButton);
		waitForSeconds(3);
	}

	@Override
	//Archive - template history checking
	public void verifyArchiveTemplateHistoryContent(String option, String userName) throws Exception {
		if (areListElementVisible(historyRecordsList, 2) && option.contains("Archived")) {
			//archive template history content
			//content1  Template Archived ( Version 1 )
			String content1 = historyRecordsList.get(0).findElement(By.cssSelector("div.templateInfo")).getText().trim();
			//content2   FionaUsing Feng at 16:38:27 PM,08/01/2022
			String content2 = historyRecordsList.get(0).findElement(By.cssSelector("p")).getText().trim();
			if ((content1.contains(option) && content1.contains("Version 1") && content2.contains(userName))) {
				SimpleUtils.pass("Archive template history can show well");
			} else {
				SimpleUtils.fail("Archive template history can't show well", false);
			}

			if (historyRecordsList.size() == 6) {
				SimpleUtils.pass("Archive template - action history record can show well");
			} else {
				SimpleUtils.fail("Archive template - action history record can't show well", false);
			}
		} else {
			SimpleUtils.report("There is no record for now.");
		}
	}

	@FindBy(css = "div.lg-slider-pop__content li.not-allow")
	private List<WebElement> deleteHistoryRecordsList;

	@Override
	//Delete - template history checking
	public void verifyDeleteTemplateHistoryContent(String option, String userName) throws Exception {
		if (areListElementVisible(deleteHistoryRecordsList, 2) && option.contains("Deleted")) {
			//delete template history content
			//content1  Template Deleted ( Version 3 )
			String content1 = deleteHistoryRecordsList.get(0).findElement(By.cssSelector("div.templateInfo")).getText().trim();
			//content2   FionaUsing Feng at 16:38:27 PM,08/01/2022
			String content2 = deleteHistoryRecordsList.get(0).findElement(By.cssSelector("p")).getText().trim();
			waitForSeconds(3);
			if ((content1.contains(option) && content1.contains("Version 3") && content2.contains(userName))) {
				SimpleUtils.pass("Delete template history can show well");
			} else {
				SimpleUtils.fail("Delete template history can't show well", false);
			}

			if ((historyRecordsList.size() + deleteHistoryRecordsList.size()) == 8) {
				SimpleUtils.pass("Delete template - action history record can show well");
			} else {
				SimpleUtils.fail("Delete template - action history record can't show well", false);
			}
		} else {
			SimpleUtils.report("There is no record for now.");
		}
	}

	@Override
	public void clickOnDeleteButtonOnTemplateDetailsPage() throws Exception {
		if (isElementEnabled(deleteTemplateButton, 3)) {
			clickTheElement(deleteTemplateButton);
			if (isElementEnabled(deleteTemplateDialog, 3)) {
				clickTheElement(okButtonOnDeleteTemplateDialog);
				waitForSeconds(5);
			}
		} else {
			SimpleUtils.fail("There is no delete button displayed", false);
		}
	}

	@Override
	public void verifyAllTemplateTypeHasAuditLog() throws Exception {
		List<WebElement> allTemplateTypes = getDriver().findElements(By.cssSelector("h1.lg-dashboard-card__title"));
		if (areListElementVisible(allTemplateTypes, 5)) {
			for (int i = 0; i < 7; i++) {
				//go to each template type
				clickTheElement(allTemplateTypes.get(i));
				waitForSeconds(3);
				//click the first template to check audit log
				if (areListElementVisible(templatesList, 2)) {
					String classValue = templatesList.get(0).getAttribute("class");
					if (classValue != null && classValue.contains("hasChildren")) {
						clickTheElement(templatesList.get(0).findElement(By.cssSelector(".toggle i")));
						waitForSeconds(2);
						clickTheElement(getDriver().findElement(By.cssSelector("[ng-repeat=\"child in item.childTemplate\"] button")));
					} else {
						clickTheElement(templatesList.get(0).findElement(By.cssSelector("button")));
					}
					waitForSeconds(15);
					if (isElementEnabled(templateTitleOnDetailsPage) && isElementEnabled(closeBTN) && isElementEnabled(templateDetailsAssociateTab)
							&& isElementEnabled(templateDetailsPageForm)) {
						SimpleUtils.pass("User can open template successfully");
					} else {
						SimpleUtils.fail("User open template failed", false);
					}
				} else {
					SimpleUtils.report("There is no templates in list");
					continue;
				}
				clickHistoryButton();
				verifyTemplateHistoryUI();
				goToConfigurationPage();
				allTemplateTypes = getDriver().findElements(By.cssSelector("h1.lg-dashboard-card__title"));
			}
		} else {
			SimpleUtils.fail("Configuration landing page load failed", false);
		}
	}

	@Override
	public void verifyLocationLevelTemplateNoHistoryButton() throws Exception {
		if (!isElementEnabled(historyButton, 2)) {
			SimpleUtils.pass("There is no history button at location level template");
		} else {
			SimpleUtils.fail("There is history button at location level template", false);
		}
	}

	@Override
	public void verifyTheLayoutOfTemplateDetailsPage() throws Exception {
		if (isElementLoaded(templateDetailsTab, 2)) {
			SimpleUtils.pass("Details Tab is showing");
		} else {
			SimpleUtils.fail("Details Tab is not showing", false);
		}
		if (isElementLoaded(backButton, 2) && isElementLoaded(closeButton, 2)) {
			SimpleUtils.pass("Details and Association Tab is showing");
		} else {
			SimpleUtils.fail("Details and Association Tab is not showing", false);
		}
	}

	public int getMaximumNumberOfShiftsPerDay() throws Exception {
		int maximumNumber = 0;
		if (isElementLoaded(maximumNumberOfShiftsPerDay, 10)) {
			maximumNumber = Integer.parseInt(maximumNumberOfShiftsPerDay.getText());
			SimpleUtils.pass("OP Page: Set maximum number of shifts per day successfully");
		} else {
			SimpleUtils.fail("OP Page: Maximum number of shifts per day fail to load! ", false);
		}
		return maximumNumber;
	}

	@FindBy(css = "question-input[question-title=\"Minimum time (in minutes) required between shifts.\"] input")
	private WebElement minimumTimeBetweenShifts;
	@Override
	public void updateMinimumTimeBetweenShifts(int minimumTime) throws Exception {
		if (isElementLoaded(minimumTimeBetweenShifts, 10)) {
			minimumTimeBetweenShifts.clear();
			minimumTimeBetweenShifts.sendKeys(String.valueOf(minimumTime));
			SimpleUtils.pass("OP Page: Set Minimum time (in minutes) required between shifts successfully");
		} else {
			SimpleUtils.fail("OP Page: Minimum time (in minutes) required between shifts fail to load! ", false);
		}
	}

	@Override
	public void verifyTheLayoutOfTemplateAssociationPage() throws Exception {
		if (isElementLoaded(templateAssociationBTN, 2)) {
			SimpleUtils.pass("Association Tab is showing");
			clickTheElement(templateAssociationBTN);
		} else {
			SimpleUtils.fail("Association Tab is not showing", false);
		}
		if (isElementLoaded(addDynamicGroupButton, 2)) {
			SimpleUtils.pass("addDynamicGroupButton is showing");
		} else {
			SimpleUtils.fail("addDynamicGroupButton is not showing", false);
		}
	}

	@Override
	public void verifyCriteriaTypeOfDynamicGroup() throws Exception {
		clickTheElement(addDynamicGroupButton);
		List<String> criteriaTypes = new ArrayList<String>() {{
			add("Country");
			add("Sate");
			add("City");
		}};
		for (String criteriaType : criteriaTypes) {
			for (WebElement criteriaOption : dynamicGroupCriteriaOptions) {
				if (criteriaOption.getText().trim().equalsIgnoreCase(criteriaType)) {
					SimpleUtils.pass("criteriaType is showing");
					break;
				} else {
					continue;
				}
			}
		}
		cancelButtonOnManageDynamicGroupPopup.click();
	}

	@FindBy(css="[tooltip-class=\"template-waning-tooltip\"]")
	private WebElement warningIcon;
	@FindBy(css="div[class*=\"tooltip ng-isolate-scope\"] div.tooltip-inner ul>li")
	private List<WebElement> warningTooltipMsgList = new ArrayList<>();
	@Override
	public boolean verifyWarningIconsDisplay(String templateName, String warningMsg) throws Exception {
		boolean isWarningExist = false;
		if (searchTemplate(templateName)){
			if(isElementLoaded(warningIcon, 5)){
				mouseToElement(warningIcon);
				for(WebElement warningTooltip : warningTooltipMsgList){
					if (isElementLoaded(warningTooltip) && warningTooltip.getText().contains(warningMsg)){
						isWarningExist = true;
						SimpleUtils.pass("warning tooltip exist and the message is correct!");
					}else {
						SimpleUtils.fail("Warning tooltip not exist or message is not correct!", false);
					}
				}
			}else {
				SimpleUtils.fail("there should be warning icons in the template list!", false);
			}
		} else {
			SimpleUtils.fail("Can not find the template!", false);
		}
		return isWarningExist;
	}

	public WebElement getSmartCard(String smartCardName) throws Exception {
		WebElement smartCardToFind = null;
		int count = 0;
		boolean isFound = false;

		if (areListElementVisible(smartCardsList, 5)){
			for (WebElement smartCard : smartCardsList){
				count++;
				if(smartCardName.equalsIgnoreCase(smartCard.findElement(By.cssSelector("div.card-carousel-card-title")).getText())){
					smartCardToFind = smartCard;
					SimpleUtils.pass(smartCardName + " Smart Card is loaded!");
					isFound = true;
					break;
				}else if(count == smartCardsList.size() && !isFound){
					SimpleUtils.fail(smartCardName + " Smart Card is not loaded!", false);
				}
			}
		} else {
			SimpleUtils.fail("Smart Card is not showing up!", false);
		}
		return smartCardToFind;
	}

	@Override
	public int getUnassignedNumber() throws Exception {
		WebElement smartCardElement = getSmartCard("Action Required");
		String unassignedString = "";
		int unassignedNumber = 0;

		if(smartCardElement != null){
			unassignedString = smartCardElement.findElement(By.cssSelector("h1.ng-binding.ng-scope")).getText();
			unassignedNumber = Integer.parseInt(unassignedString.split(" ")[0]);
		}else{
			SimpleUtils.fail("Unassigned Smart Card is not loaded!", false);
		}
		return unassignedNumber;
	}

	@FindBy(css = "div.card-carousel-full span[ng-if=\"noOfUnassigned\"]")
	private WebElement exportUnassignedFile;
	@Override
	public boolean verifyUnassignedSmartCardDownloadFile(String fileName, Map<String, String> criteriaAndValue) throws Exception {
		boolean flag = true;
		String downloadDirPath = PropertyMap.get("Download_File_Default_Dir");
		int number = criteriaAndValue.size();

		if (isElementLoaded(exportUnassignedFile)) {
			click(exportUnassignedFile);
			waitForSeconds(5);

			File latestFile = SimpleUtils.getLatestFileFromDirectory(downloadDirPath);
			String fileFullPathAndName = latestFile.toString();
			System.out.println("fileFullPathAndName: " + fileFullPathAndName);
			System.out.println("latestFile filename: " + latestFile.getName());
			System.out.println("fileName parameter: " + fileName);

			if (!latestFile.getName().toLowerCase().contains(fileName.toLowerCase())) {
				SimpleUtils.fail("the expected file name is not correct!", false);
			}
			ArrayList<HashMap<String, String>> unassignedDataList = CsvUtils.getDataFromCSVFileWithHeader(fileFullPathAndName);

			for (HashMap<String, String> unassignedData : unassignedDataList) {
				for (Map.Entry<String, String> unassignedEntry : unassignedData.entrySet()) {
					for (Map.Entry<String, String> criteriaEntry : criteriaAndValue.entrySet()) {

						if (criteriaEntry.getKey().equalsIgnoreCase(unassignedEntry.getKey()) && !"Any".equalsIgnoreCase(criteriaEntry.getValue())) {
							if (criteriaEntry.getValue().equalsIgnoreCase(unassignedEntry.getValue())) {
								flag = false;
								System.out.println("this data meets criteria, should not show up in the file: x " + unassignedEntry.getKey() + ": " + unassignedEntry.getValue());
								break;
							} else {
								System.out.println("this data does not meet criteria, show up in the file as expected: √ " + unassignedEntry.getKey() + ": " + unassignedEntry.getValue());
							}
						}
					}
				}
			}
		}
		return flag;
	}

	@FindBy(css="lg-switch[value*=\"overrideViaIntegration\"] label[class=\"switch\"]")
	private WebElement overrideViaIntegrationBTN;

	@Override
	public void verifyDefaultValueOfOverrideViaIntegrationButton(){
		if(isElementEnabled(overrideViaIntegrationBTN,2)){
			if(!overrideViaIntegrationBTN.findElement(By.cssSelector("input")).getAttribute("class").trim().contains("not-empty")){
				SimpleUtils.pass("The overrideViaIntegration button is disabled by default");
			}else {
				SimpleUtils.fail("The overrideViaIntegration button is enabled by default",false);
			}
		}
	}

	public int getMinimumTimeBetweenShifts() throws Exception {
		int minimumTime = 0;
		if (isElementLoaded(minimumTimeBetweenShifts, 10)) {
			minimumTime = Integer.parseInt(minimumTimeBetweenShifts.getText());
			SimpleUtils.pass("OP Page: Set Minimum time (in minutes) required between shifts successfully");
		} else {
			SimpleUtils.fail("OP Page: Minimum time (in minutes) required between shifts fail to load! ", false);
		}
		return minimumTime;
	}

	@Override
	public boolean verifyTemplateCardExist(String templateType) throws Exception {
		boolean flag = false;
		if (areListElementVisible(configurationCardsList)) {
			for (WebElement configurationCard : configurationCardsList) {
				if (configurationCard.getText().equals(templateType)) {
					SimpleUtils.pass(templateType + " card is showing.");
					flag = true;
					break;
				}
			}
		}else{
			SimpleUtils.fail("Configuration card failed to load!", false);
		}
		return flag;
	}

	@FindBy(css="input-field[options=\"$ctrl.forecastSourceOptions\"] select")
	private WebElement forecastSourceSelect;
	@Override
	public List<String> getAllForecastSourceType() throws Exception {
		List<String> forecastSourceName = new ArrayList<>();

		if (isElementLoaded(forecastSourceSelect, 5)){
			Select typeSelect = new Select(forecastSourceSelect);
			for (WebElement option : typeSelect.getOptions()){
				forecastSourceName.add(option.getText());
			}
		}
		return forecastSourceName;
	}

	@FindBy(css="lg-predictability-score>question-input[question-title=\"Predictability Score\"]")
	private WebElement predictabilityScoreItem;
	@Override
	public boolean verifyPredictabilityScoreExist() throws Exception {
		boolean flag = false;

		if (isElementLoaded(predictabilityScoreItem, 5)){
			flag = true;
		}
		return flag;
	}

	@Override
	public boolean verifyOverrideViaIntegrationButtonShowingOrNot(){
		boolean flag = false;
		if(isElementEnabled(overrideViaIntegrationBTN,2)){
			flag = true;
		}else {
			flag = false;
		}
		return flag;
  }

	@FindBy(css="lg-button[label=\"Get Predictability Score\"] button")
	private WebElement getScoreBtn;
	@Override
	public boolean isGetPredictabilityScoreEnabled() throws Exception {
		boolean isEnabled = true;
		if (!isElementExist("i[ng-if*=\"hasPublishedVersion\"]")){
			if (isElementLoaded(getScoreBtn, 5)){
				if (getScoreBtn.getAttribute("disabled") != null && getScoreBtn.getAttribute("disabled").equals("true")){
					isEnabled = false;
				}
			}else{
				SimpleUtils.fail("Failed to load 'Get Predictability Score button'!", false);
			}
		}

		return isEnabled;
	}

	@FindBy(css="i[ng-if*=\"hasPublishedVersion\"]")
	private WebElement spinButton;
	@Override
	public void clickGetPredictabilityScore() throws Exception {
		if (isElementLoaded(spinButton)){
			waitForNotExists(spinButton, 300);
		}
		if (isElementEnabled(getScoreBtn, 10)){
			clickTheElement(getScoreBtn);
			if (isElementLoaded(spinButton)){
				SimpleUtils.pass("The Predictability Score can be requested again by click the button!");
			}
		}else{
			SimpleUtils.fail("Get Predictability Score button should NOT be disabled!", false);
		}
	}

	@Override
	public void turnOnOffOverrideViaIntegrationButton(){
		if(isElementEnabled(overrideViaIntegrationBTN,2)){
			clickTheElement(overrideViaIntegrationBTN);
			waitForSeconds(2);
      }
   }
   
	@FindBy(xpath = "//input-field[@type='text']//input")
	private List<WebElement> inputFields;

	@Override
	public void verifyEachFieldsWithInvalidTexts() {
		List<String> invalidTexts = new ArrayList<String>() {{
			add("m");
			add("$");
		}};
		for (WebElement inputField : inputFields) {
			for (String invalidText : invalidTexts) {
				inputField.clear();
				inputField.sendKeys(invalidText);

			}
		}
	}

	@Override
	public void inputTemplateName(String templateName) throws Exception {
		if (isElementLoaded(newTemplateBTN, 10)) {
			clickTheElement(newTemplateBTN);
			waitForSeconds(1);
			if (isElementEnabled(createNewTemplatePopupWindow, 10)) {
				SimpleUtils.pass("User can click new template button successfully!");
				clickTheElement(newTemplateName);
				newTemplateName.sendKeys(templateName);
				clickTheElement(newTemplateDescription);
				newTemplateDescription.sendKeys(templateName);
				clickTheElement(continueBTN);
				waitForSeconds(5);
				if (isElementEnabled(welcomeCloseButton, 5)) {
					clickTheElement(welcomeCloseButton);
				}
			}
		}
	}

	@FindBy(css="sub-content-box[box-title=\"Dynamic Group\"] span.ng-binding")
	private WebElement addButtonOfAdvancedStaffingRuleDynamicGroup;
	@FindBy(css="h1.lg-modal__title")
	private WebElement manageDynamicLocationGroupPopUp;
	@Override
	public void verifyAddButtonOfDynamicLocationGroupOfAdvancedStaffingRuleIsClickable() throws Exception {
		if (isElementEnabled(dynamicGroupSection,3)) {
			SimpleUtils.pass("There is dynamic location group on new advance staffing rule page");
			isClickable(addButtonOfAdvancedStaffingRuleDynamicGroup,2);
		} else {
			SimpleUtils.fail("There is Not dynamic location group on new advance staffing rule page", false);
		}
	}

	@Override
	public void clickOnAddButtonOfDynamicLocationGroupOfAdvancedStaffingRule() throws Exception {
		if (isElementEnabled(dynamicGroupSection,3)) {
			SimpleUtils.pass("There is dynamic location group on new advance staffing rule page");
			clickTheElement(addButtonOfAdvancedStaffingRuleDynamicGroup);
			if(isElementEnabled(manageDynamicLocationGroupPopUp,2)){
				SimpleUtils.pass("User can click add button Dynamic Location Group Of Advanced Staffing Rule successfully");
			}else {
				SimpleUtils.fail("User can click add button Dynamic Location Group Of Advanced Staffing Rule successfully",false);
			}
		} else {
			SimpleUtils.fail("There is Not dynamic location group on new advance staffing rule page", false);
		}
	}

	@FindBy(css="input-field[type=\"checkbox\"] ng-form")
	private List<WebElement> dynamicGroupCriteriaResultsList;
	@Override
	public void advanceStaffingRuleDynamicGroupDialogUICheck(String name) throws Exception {
		if (isElementLoaded(addDynamicGroupButton, 5)) {
			SimpleUtils.pass("The " + " icon for adding dynamic group button show as expected");
			clickTheElement(addDynamicGroupButton);
			if (manageDynamicGroupPopupTitle.getText().trim().equalsIgnoreCase("Manage Dynamic Location Group")) {
				SimpleUtils.pass("Dynamic group dialog title show as expected");
				//check the group name is required
				if (dynamicGroupName.getAttribute("required").equals("true")) {
					SimpleUtils.pass("Group name is required");
					//input group name
					dynamicGroupName.sendKeys(name);
					//clear group name
					dynamicGroupName.clear();
					//get the required message
					if (isElementLoaded(dynamicGroupNameRequiredMsg) && dynamicGroupNameRequiredMsg.getText().contains("Group Name is required"))
						SimpleUtils.pass("group name is required message displayed if not input");
					dynamicGroupName.sendKeys(name);
					waitForSeconds(2);
					groupDescriptionInput.clear();
					groupDescriptionInput.sendKeys("description_qaz123@_");
					waitForSeconds(2);
					groupDescriptionInput.clear();
					String[] criteriaOps = {"Custom", "District", "Country", "State", "City", "Location Name",
							"Location Id", "Location Type", "UpperField", "Config Type"};
					for (String ss : criteriaOps) {
						//check every criteria options is selectable
						clickTheElement(dynamicGroupCriteria);
						waitForSeconds(4);
						String optionType = ".lg-search-options__option[title='" + ss + "']";
						getDriver().findElement(By.cssSelector(optionType)).click();
						SimpleUtils.pass("The criteria " + ss + " was selected!");
						waitForSeconds(3);
					}
					//set up value
					clickTheElement(dynamicGroupCriteriaValueInputs.get(1));
					waitForSeconds(2);
					if (areListElementVisible(dynamicGroupCriteriaResultsList, 5)) {
						SimpleUtils.pass("The current selected Criteria has value options");
						clickTheElement(dynamicGroupCriteriaResultsList.get(0));
						waitForSeconds(3);
						//click add more link//click add more link
						clickTheElement(dynamicGroupCriteriaAddMoreLink);
						waitForSeconds(2);
						//Check the delete icon showed
						if (areListElementVisible(dynamicGroupCriteriaAddDelete) && dynamicGroupCriteriaAddDelete.size() > 1) {
							clickTheElement(dynamicGroupCriteriaAddDelete.get(1));
							waitForSeconds(2);
							//select a criteria type
							clickTheElement(dynamicGroupCriteria);
							waitForSeconds(1);
							String optionCountry = ".lg-search-options__option[title='Country']";
							getDriver().findElement(By.cssSelector(optionCountry)).click();
							waitForSeconds(2);
							//set up criteria relationship
							clickTheElement(dynamicGroupCriteriaValueInputs.get(0));
							// check IN and NOTIN options supported
							if (isElementLoaded(dynamicGroupCriteriaINOption) && isElementLoaded(dynamicGroupCriteriaINotNOption))
								SimpleUtils.pass("The IN and NOt IN relation are supported for Criteria relationship.");
							//set up criteria value
							clickTheElement(dynamicGroupCriteriaValueInputs.get(1));
							//choose the last value from drop down
							clickTheElement(dynamicGroupCriteriaValueInputs.get(0));
							clickTheElement(dynamicGroupCriteriaResultsList.get(dynamicGroupCriteriaResultsList.size() - 1));
							waitForSeconds(2);
							clickTheElement(dynamicGroupCriteriaResultsList.get(0));
							waitForSeconds(2);
							//click the test button to chek value
							clickTheElement(dynamicGroupTestButton);
							waitForSeconds(3);
							//get the result
							if (isElementLoaded(dynamicGroupTestInfo)) {
								SimpleUtils.pass("Get results for the dynamic group");
								String mappedRes = dynamicGroupTestInfo.getText().split("Location")[0].trim();
								if (Integer.parseInt(mappedRes) > 0)
									SimpleUtils.pass("Get mapped location for the dynamic group");
							} else
								SimpleUtils.fail("No result get for the dynamic group", true);
							//click save
							clickTheElement(okButtonOnManageDynamicGroupPopup);
							waitForSeconds(3);
						} else
							SimpleUtils.fail("The delete criteria icon is not displayed!", false);
					} else
						SimpleUtils.fail("The current selected Criteria has no options can be selected", true);
				} else
					SimpleUtils.fail("Group name is not required on UI", true);
			} else
				SimpleUtils.fail("Dynamic group dialog title is not show as designed!", true);
		} else
			SimpleUtils.fail("The " + " icon for adding dynamic group missing!", false);
	}


	@FindBy(css="table.lg-table.templateAssociation_table tr.ng-scope")
	private List<WebElement> advanceStaffingRuleDynamicGroupList;
	@Override
	public void advanceStaffingRuleEditDeleteADynamicGroup(String dyname) throws Exception {
		if (areListElementVisible(advanceStaffingRuleDynamicGroupList,5)) {
			int beforeSize = advanceStaffingRuleDynamicGroupList.size();
			for(WebElement advanceStaffingRuleDynamicGroup:advanceStaffingRuleDynamicGroupList){
				String groupName = advanceStaffingRuleDynamicGroup.findElement(By.cssSelector("td:nth-child(1)")).getText().trim();
				WebElement editButton =advanceStaffingRuleDynamicGroup.findElement(By.cssSelector("td:nth-child(3) lg-button[label=\"Edit\"] button"));
				WebElement removeButton =advanceStaffingRuleDynamicGroup.findElement(By.cssSelector("td:nth-child(3) lg-button[label=\"Remove\"] button"));
				if(groupName.equalsIgnoreCase(dyname)){
					//edit the dynamic group
					clickTheElement(editButton);
					waitForSeconds(2);
					if (isElementLoaded(manageDynamicGroupPopupTitle)) {
						SimpleUtils.pass("The edit dynamic group dialog pop up successfully!");
						//cancel
						clickTheElement(cancelButtonOnManageDynamicGroupPopup);
						waitForSeconds(1);
					} else
						SimpleUtils.fail("The edit dynamic group dialog not pop up!", true);
					//remove the dynamic group
					clickTheElement(removeButton);
					waitForSeconds(2);
					clickTheElement(dynamicGroupRemoveBTNOnDialog);
					waitForSeconds(1);
					int afterSize = getDriver().findElements(By.cssSelector("table.lg-table.templateAssociation_table tr.ng-scope")).size();
					if(beforeSize-afterSize==1){
						SimpleUtils.pass("User can delete dynamic group in advance staffing rule successfully");
					}else {
						SimpleUtils.fail("User can't delete dynamic group in advance staffing rule successfully",false);
					}
				}
			}
		}
	}

	@FindBy(css="input-field[placeholder=\"Select one\"]")
	private List<WebElement> criteriaList;
	@FindBy(css="div.CodeMirror textarea")
	private WebElement customerFormatScript;
	@FindBy(css="lg-select input-field[placeholder=\"Select...\"]")
	private List<WebElement> dynamicGroupCriteriaInOrNotIn;
	@FindBy(css="lg-multiple-select input-field[placeholder=\"Select...\"]")
	private List<WebElement> dynamicGroupCriteriaResultField;
	@Override
	public void createAdvanceStaffingRuleDynamicGroup(String name) throws Exception {
		if (isElementLoaded(addDynamicGroupButton, 5)) {
			SimpleUtils.pass("The " + " icon for adding dynamic group button show as expected");
			clickTheElement(addDynamicGroupButton);
			if (manageDynamicGroupPopupTitle.getText().trim().equalsIgnoreCase("Manage Dynamic Location Group")) {
				SimpleUtils.pass("Dynamic group dialog title show as expected");
				//check the group name is required
				if (dynamicGroupName.getAttribute("required").equals("true")) {
					SimpleUtils.pass("Group name is required");
					//input group name
					dynamicGroupName.sendKeys(name);
					waitForSeconds(2);
					groupDescriptionInput.clear();
					groupDescriptionInput.sendKeys("description_qaz123@_");
					waitForSeconds(2);
					groupDescriptionInput.clear();
					String[] criteriaOps = {"Config Type","District","City","Custom"};
					for (String ss : criteriaOps) {
						//select criteria option
						clickTheElement(criteriaList.get(criteriaList.size()-1));
						waitForSeconds(2);
						String optionType = ".lg-search-options__option[title='" + ss + "']";
						List<WebElement> options = getDriver().findElements(By.cssSelector(optionType));
						clickTheElement(options.get(options.size()-1));
						SimpleUtils.pass("The criteria " + ss + " was selected!");
						waitForSeconds(3);
						//set up value
						waitForSeconds(2);
						if(!ss.equalsIgnoreCase("Custom")) {
							if (isElementEnabled(dynamicGroupCriteriaResultField.get(dynamicGroupCriteriaResultField.size()-1), 5)) {
								clickTheElement(dynamicGroupCriteriaResultsList.get(dynamicGroupCriteriaResultsList.size()-1));
								waitForSeconds(3);
								//click add more link
								clickTheElement(dynamicGroupCriteriaAddMoreLink);
								waitForSeconds(2);
							} else
								SimpleUtils.fail("The current selected Criteria has no options can be selected", true);
						}else {
							if(isElementEnabled(customerFormatScript,2)){
								clickTheElement(customerFormatScript);
								customerFormatScript.sendKeys(Keys.TAB);
								customerFormatScript.sendKeys("customer");
							}
						}
					}
				} else
					SimpleUtils.fail("Group name is not required on UI", true);
			} else
				SimpleUtils.fail("Dynamic group dialog title is not show as designed!", true);
		} else
			SimpleUtils.fail("The " + " icon for adding dynamic group missing!", false);
		//click save
		clickTheElement(okButtonOnManageDynamicGroupPopup);
		waitForSeconds(3);
	}

	@FindBy(css="div.condition_line>lg-cascade-select")
	private List<WebElement> conditionsList;

	@Override
	public void advanceStaffingRuleDynamicGroupCriteriaListChecking(String name) throws Exception {
		if (isElementLoaded(addDynamicGroupButton, 5)) {
			SimpleUtils.pass("The " + " icon for adding dynamic group button show as expected");
			clickTheElement(addDynamicGroupButton);
			if (manageDynamicGroupPopupTitle.getText().trim().equalsIgnoreCase("Manage Dynamic Location Group")) {
				SimpleUtils.pass("Dynamic group dialog title show as expected");
				//check the group name is required
				if (dynamicGroupName.getAttribute("required").equals("true")) {
					SimpleUtils.pass("Group name is required");
					//input group name
					dynamicGroupName.sendKeys(name);
					waitForSeconds(2);
					groupDescriptionInput.clear();
					groupDescriptionInput.sendKeys("description_qaz123@_");
					waitForSeconds(2);
					String criteriaOp = "Config Type";
					//select criteria option
					clickTheElement(criteriaList.get(0));
					waitForSeconds(2);
					String optionType = ".lg-search-options__option[title='" + criteriaOp + "']";
					getDriver().findElement(By.cssSelector(optionType)).click();
					SimpleUtils.pass("The criteria " + criteriaOp + " was selected!");
					waitForSeconds(3);
					//click add more link
					clickTheElement(dynamicGroupCriteriaAddMoreLink);
					clickTheElement(criteriaList.get(criteriaList.size()-1));
					List<WebElement> optionList = conditionsList.get(conditionsList.size()-1).findElements(By.cssSelector(" lg-select:nth-child(1) div.lg-search-options div[ng-repeat]"));
					for(WebElement option:optionList){
						String optionName = option.findElement(By.cssSelector("div")).getText().trim();
						if(optionName.equalsIgnoreCase("Config Type")){
							String classValue = option.getAttribute("class").trim();
							if(classValue.contains("disabled")){
								SimpleUtils.pass("The config type option is disabled after it was selected in another criteria");
							}else {
								SimpleUtils.fail("The config type option is NOT disabled after it was selected in another criteria",false);
							}
							break;
						}
					}
				} else
					SimpleUtils.fail("Group name is not required on UI", true);
			} else
				SimpleUtils.fail("Dynamic group dialog title is not show as designed!", true);
		} else
			SimpleUtils.fail("The " + " icon for adding dynamic group missing!", false);
	}

	@Override
	public List<String> getStaffingRules() throws Exception {
		List<String> staffingRules = new ArrayList<>();
		if (areListElementVisible(staffingRulesList, 5)) {
			for (WebElement rule : staffingRulesList) {
				staffingRules.add(rule.getText());
			}
		}
		return staffingRules;
	}

	@FindBy(css="pre.CodeMirror-placeholder.CodeMirror-line-like")
	private WebElement customFormulaDescription;
	@Override
	public void advanceStaffingRuleDynamicGroupCustomFormulaDescriptionChecking() throws Exception {
		//click add more link
		clickTheElement(dynamicGroupCriteriaAddMoreLink);
		//check custom script format description
		String criteriaOp = "Custom";
		//select criteria option
		clickTheElement(criteriaList.get(0));
		waitForSeconds(2);
		String optionType = ".lg-search-options__option[title='" + criteriaOp + "']";
		getDriver().findElement(By.cssSelector(optionType)).click();
		SimpleUtils.pass("The criteria " + criteriaOp + " was selected!");
		waitForSeconds(3);
		if(isElementEnabled(customerFormatScript,2)){
			String str = customFormulaDescription.getText().trim();
			if(str.equalsIgnoreCase("Enter your expression. The dynamic location group will" +
					" only be created if the expresion evaluates to be true.")){
				SimpleUtils.pass("The formula description is correct");
			}else
				SimpleUtils.fail("The formula description is incorrect",false);
		}
	}

	@Override
	public void verifyDynamicGroupOfAdvanceStaffingRuleIsOptional(String workRole, List<String> days) throws Exception {
		//get the staffing rules count before add one new rule
		int countBeforeSaving = Integer.valueOf(getCountOfStaffingRules(workRole));
		selectWorkRoleToEdit(workRole);
		checkTheEntryOfAddAdvancedStaffingRule();
		verifyAdvancedStaffingRulePageShowWell();
		selectDaysForDaysOfWeekSection(days);
		waitForSeconds(2);
		//get the status of mark button
		String classValue = checkMarkButton.getAttribute("class").trim();
		if(classValue.contains("enabled")){
			SimpleUtils.pass("Dynamic Group Of Advance Staffing Rule Is Optional");
		}else {
			SimpleUtils.fail("Dynamic Group Of Advance Staffing Rule Is NOT Optional",false);
		}
	}

	@FindBy(css = "question-input[question-title=\"Granularity\"] select")
	private WebElement granularityOption;
	@Override
	public String getGranularityForCertainDriver() throws Exception {
		String granularityValue = "";
		Select granularitySelect = new Select(granularityOption);
		granularityValue = granularitySelect.getFirstSelectedOption().getText();
		clickTheElement(cancelButton);
		searchDriverInTemplateDetailsPage( "");
		waitForSeconds(25);
		if (granularityValue.isEmpty()){
			SimpleUtils.fail("Failed to get granularity value!", false);
		}
		return granularityValue;
	}
	
	@FindBy(css="div.card-carousel-card-title")
	private List<WebElement> smartCards;
	@FindBy(css="input[placeholder=\"Search by Work Role\"]")
	private WebElement searchWorkRoleField;
	@FindBy(css="table tbody[ng-repeat=\"workRole in $ctrl.sortedRows\"]")
	private List<WebElement> workRoleListOnWorkRoleSettingTemplateDetailsPage;

	@Override
	public void verifyWorkRoleSettingsTemplateListUIAndDetailsUI(String templateName,String mode) throws Exception {
		if(isElementEnabled(newTemplateBTN,2) && areListElementVisible(smartCards,2) && isElementEnabled(searchField,2)
				&& areListElementVisible(templatesList,2)){
			SimpleUtils.pass("Work role settings template list can show well");
			clickOnSpecifyTemplateName(templateName,mode);
			if(isElementEnabled(historyButton,2)&&isElementEnabled(editButton,2)
					&& isElementEnabled(templateDetailsTab,2)&&isElementEnabled(templateAssociationTab)&&isElementEnabled(searchWorkRoleField,2)
					&&areListElementVisible(workRoleListOnWorkRoleSettingTemplateDetailsPage,2)){
				SimpleUtils.pass("The work role setting template details page can show well");
			}else {
				SimpleUtils.fail("The work role setting template details page can NOT show well",false);
			}
		}else {
			SimpleUtils.fail("Work role settings template list page is not showing well",false);
		}
	}

	@FindBy(css="lg-dashboard-card[title=\"Work Role Settings\"]")
	private WebElement workRoleSettingsTile;
	@FindBy(css="lg-dashboard-card[title=\"Work Role Settings\"] h1")
	private WebElement workRoleSettingsButton;

	@Override
	public void goToWorkRoleSettingsTile(){
		if(isElementEnabled(workRoleSettingsTile,2)){
			SimpleUtils.pass("The work role settings tile is showing");
			clickTheElement(workRoleSettingsButton);
			waitForSeconds(3);
			if(isElementEnabled(newTemplateBTN,2)){
				SimpleUtils.pass("User can go to work role settings template successfully");
			}else {
				SimpleUtils.fail("User can't go to work role settings template successfully",false);
			}
		}else {
			SimpleUtils.fail("There is no work role settings tile",false);
		}
	}

	@FindBy(css="table tbody[ng-repeat=\"workRole in $ctrl.sortedRows\"]")
	private List<WebElement> workRoles;
	@FindBy(css="div.lg-pagination div.lg-pagination__arrow--right")
	private WebElement rightPaginationBTN;
	@FindBy(css="div.lg-pagination div.lg-pagination__pages")
	private WebElement workRolePageNumber;

	@Override
	public void checkWorkRoleListShowingWell(int workRoleCount){
		int totalWorkRoles = 0;

		String classValue=rightPaginationBTN.getAttribute("class").trim();
		while(!classValue.contains("disabled")){
			clickTheElement(rightPaginationBTN);
			classValue=rightPaginationBTN.getAttribute("class").trim();
		}
		int totalPages = Integer.parseInt(workRolePageNumber.getText().trim().split(" ")[1]);
		if(totalPages>=2){
			totalWorkRoles = (totalPages-1)*15+workRoles.size();
		}else {
			totalWorkRoles = workRoles.size();
		}
		if(workRoleCount==totalWorkRoles){
			SimpleUtils.pass("Work role list can show well in WRS template");
		}else {
			SimpleUtils.fail("Work role list can NOT show well in WRS template",false);
		}
	}

	public void searchWorkRoleInWRSTemplateDetails(String workRole){
		if(isElementEnabled(searchWorkRoleField,2)){
			clickTheElement(searchWorkRoleField);
			searchWorkRoleField.clear();
			searchWorkRoleField.sendKeys(workRole);
			waitForSeconds(2);
			if(workRoleList.size()>0){
				SimpleUtils.pass("User can search out this work role");
			}else {
				SimpleUtils.report("There is no this work role");
			}
		}
	}

	@Override
	public HashMap<String,String> getDefaultHourlyRate(List<String> workRoles){
		HashMap<String,String> workRolesAndValues = new HashMap<String,String>();
		for(String workRole:workRoles){
			searchWorkRoleInWRSTemplateDetails(workRole);
			String hourlyRate = workRoleList.get(0).findElement(By.cssSelector("td[ng-if=\"$ctrl.mode === 'view'\"].ng-scope")).getText().trim().substring(1);
			if(hourlyRate == null || hourlyRate.isEmpty()){
				hourlyRate = "0";
			}
			workRolesAndValues.put(workRole,hourlyRate);
		}
		waitForSeconds(3);
		return workRolesAndValues;
	}

	@FindBy(css="td[ng-if=\"$ctrl.mode === 'edit'\"] input")
	private WebElement hourlyRateInputField;
	@Override
	public void updateWorkRoleHourlyRate(String workRole,String updateValue){
		searchWorkRoleInWRSTemplateDetails(workRole);
		if(isElementEnabled(hourlyRateInputField,2)){
			clickTheElement(hourlyRateInputField);
			hourlyRateInputField.clear();
			hourlyRateInputField.sendKeys(updateValue);
			waitForSeconds(3);
			if(getDriver().findElement(By.cssSelector("td[ng-if=\"$ctrl.mode === 'edit'\"] div")).getAttribute("innerText").trim().equalsIgnoreCase(updateValue)){
				SimpleUtils.pass("User can update hourly rate successfully");
			}else {
				SimpleUtils.fail("User can NOT update hourly rate successfully",false);
			}
		}
	}

	@Override
	public void createFutureWRSTemplateBasedOnExistingTemplate(String templateName, String button, int date, String editOrViewMode) throws Exception {
		int beforeCount = 0;
		int afterCount = 0;
		waitForSeconds(2);
		//create future published version template
		if (areListElementVisible(templateNameList, 3)) {
			expandMultipleVersionTemplate(templateName);
			beforeCount = publishedTemplateStatus.size();
			clickOnSpecifyTemplateName(templateName, editOrViewMode);
			clickOnEditButtonOnTemplateDetailsPage();
			scrollToBottom();
			chooseSaveOrPublishBtnAndClickOnTheBtn(button);
			if (isElementLoaded(dateOfPublishPopup, 2)) {
				clickTheElement(effectiveDate);
				setEffectiveDate(date);
				clickTheElement(okButtonOnFuturePublishConfirmDialog);
			} else {
				SimpleUtils.fail("The future publish template confirm dialog is not displayed.", false);
			}
		} else {
			SimpleUtils.fail("Template list is not displayed!", false);
		}
		//Check whether the future template is created successfully or not?
		expandMultipleVersionTemplate(templateName);
		afterCount = publishedTemplateStatus.size();

		if (afterCount - beforeCount == 1) {
			SimpleUtils.pass("User create new future template successfully!");
		} else {
			SimpleUtils.fail("User failed to create new future template!", false);
		}
	}

	@FindBy(css = "question-input[question-title=\"Input budget as labor hours or total wages?\"] select[ng-change=\"$ctrl.handleChange()\"]")
	private WebElement inputBudgetSettingDropdown;
	@Override
	public void updateInputBudgetSettingDropdownOption(String option) throws Exception {
		if (isElementLoaded(inputBudgetSettingDropdown, 10)) {
			Select dropdown = new Select(inputBudgetSettingDropdown);
			dropdown.selectByVisibleText(option);
			SimpleUtils.pass("OP Page: Global Configuration: Input budget as labor hours or total wages settings been changed successfully");
		} else {
			SimpleUtils.fail("OP Page: Global Configuration: Input budget as labor hours or total wages settings dropdown list not loaded.", false);
		}
	}


	@FindBy(css = "question-input[question-title=\"Are employees required to acknowledge their schedule?\"] yes-no")
	private WebElement requireEmployeeAcknowledgeSetting;

	@Override
	public void enableOrDisableRequiredEmployeeAcknowledgeSetting(String yesOrNo) throws Exception {
		if (isElementLoaded(requireEmployeeAcknowledgeSetting, 10)) {
			scrollToElement(requireEmployeeAcknowledgeSetting);
			if (yesOrNo.equalsIgnoreCase("yes")) {
				if (isElementLoaded(requireEmployeeAcknowledgeSetting.findElement(By.cssSelector(".lg-button-group-first")), 10)) {
					click(requireEmployeeAcknowledgeSetting.findElement(By.cssSelector(".lg-button-group-first")));
					SimpleUtils.pass("Turned on 'Are employees required to acknowledge their schedule?' setting successfully! ");
				} else {
					SimpleUtils.fail("Yes button fail to load!", false);
				}
			} else if (yesOrNo.equalsIgnoreCase("no")) {
				if (isElementLoaded(requireEmployeeAcknowledgeSetting.findElement(By.cssSelector(".lg-button-group-last")), 10)) {
					click(requireEmployeeAcknowledgeSetting.findElement(By.cssSelector(".lg-button-group-last")));
					SimpleUtils.pass("Turned off 'Are employees required to acknowledge their schedule?' setting successfully! ");
				} else {
					SimpleUtils.fail("No button fail to load!", false);
				}
			} else {
				SimpleUtils.warn("You have to input the right command: yes or no");
			}
		} else {
			SimpleUtils.fail("'Are employees required to acknowledge their schedule?' setting is not loaded!", false);
		}
	}


	@FindBy(css = "div[ng-class=\"getClassForRuleContainer()\"]")
	private List<WebElement> basicScheduleRuleList;
	@Override
	public void deleteScheduleRules() throws Exception {
		int countOfScheduleRules = basicScheduleRuleList.size();
		if (countOfScheduleRules != 0) {
			for(int i=0; i<countOfScheduleRules; i++) {
				WebElement deleteButton = basicScheduleRuleList.get(i).findElement(By.cssSelector("span.settings-work-rule-edit-delete-icon"));
				clickTheElement(deleteButton);
				if (isElementEnabled(deleteButtonOnDialogPage, 2)) {
					clickTheElement(deleteButtonOnDialogPage);
				}
				waitForSeconds(2);
				if (!isElementDisplayed(deleteButton)) {
					SimpleUtils.pass("User can delete basic staffing rule successfully!");
				} else {
					SimpleUtils.fail("User can't delete schedule rule successfully!", false);
				}
			}
		}else
			SimpleUtils.report("No any schedule rules!");
	}

	@FindBy(xpath = "//lg-tab-toolbar//lg-search//input")
	private WebElement workRoleSearchInput;
	@FindBy(css = "tbody[ng-repeat=\"workRole in $ctrl.sortedRows\"]>tr>td:nth-child(2)>lg-button>button[type='button']")
	private WebElement staffingRulesForWorkRoleInSchedulingRole;
	@Override
	public void DeleteStaffingRulesForParticularRule(String workRole) throws Exception {
		if (isElementLoaded(workRoleSearchInput, 5)) {
			workRoleSearchInput.clear();
			workRoleSearchInput.sendKeys(workRole);
			if (isElementLoaded(staffingRulesForWorkRoleInSchedulingRole, 5)) {
					if (!staffingRulesForWorkRoleInSchedulingRole.getText().contains("Add")) {
						click(staffingRulesForWorkRoleInSchedulingRole);
						deleteScheduleRules();
						saveBtnIsClickable();
					}
			}
		} else
			SimpleUtils.fail("Work role search box is not loaded!", false);
	}

}

