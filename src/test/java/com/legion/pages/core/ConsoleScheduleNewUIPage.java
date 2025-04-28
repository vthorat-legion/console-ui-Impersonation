package com.legion.pages.core;

import com.legion.pages.*;
import com.legion.tests.core.ScheduleTestKendraScott2.SchedulePageSubTabText;
import com.legion.tests.core.ScheduleTestKendraScott2.staffingOption;
import com.legion.utils.FileDownloadVerify;
import com.legion.utils.JsonUtil;
import com.legion.utils.MyThreadLocal;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.legion.utils.MyThreadLocal.*;

public class ConsoleScheduleNewUIPage extends BasePage implements SchedulePage {
    private  ConsoleScheduleOverviewPage overviewPage;
    private static HashMap<String, String> propertyCustomizeMap = JsonUtil.getPropertiesFromJsonFile("src/test/resources/ScheduleCustomizeNewShift.json");
    private static HashMap<String, String> propertySearchTeamMember = JsonUtil.getPropertiesFromJsonFile("src/test/resources/SearchTeamMember.json");
    private static HashMap<String, String> propertyWorkRole = JsonUtil.getPropertiesFromJsonFile("src/test/resources/WorkRoleOptions.json");
    private static HashMap<String, String> propertyBudgetValue = JsonUtil.getPropertiesFromJsonFile("src/test/resources/Budget.json");
    private static HashMap<String, String> parameterMap = JsonUtil.getPropertiesFromJsonFile("src/test/resources/envCfg.json");
    private static HashMap<String, String> parametersMap2 = JsonUtil.getPropertiesFromJsonFile("src/test/resources/ControlsPageLocationDetail.json");
    private static HashMap<String, String> propertyOperatingHours = JsonUtil.getPropertiesFromJsonFile("src/test/resources/operatingHours.json");
    private static HashMap<String, String> propertyOperatingHoursLG = JsonUtil.getPropertiesFromJsonFile("src/test/resources/operatingHoursLG.json");
    private static HashMap<String, String> propertyTimeZoneMap = JsonUtil.getPropertiesFromJsonFile("src/test/resources/LocationTimeZone.json");

    public enum scheduleHoursAndWagesData {
        scheduledHours("scheduledHours"),
        budgetedHours("budgetedHours"),
        otherHours("otherHours"),
        budgetedWages("budgetedWages"),
        scheduledWages("scheduledWages"),
        otherWages("otherWages"),
        wages("Wages"),
        hours("hours");
        private final String value;

        scheduleHoursAndWagesData(final String newValue) {
            value = newValue;
        }

        public String getValue() {
            return value;
        }
    }

    public enum scheduleGroupByFilterOptions {
        groupbyAll("Group by All"),
        groupbyWorkRole("Group by Work Role"),
        groupbyTM("Group by TM"),
        groupbyJobTitle("Group by Job Title"),
        groupbyLocation("Group by Location"),
        groupbyDayParts("Group by Day Parts"),
        groupbyPattern("Group by Pattern");

        private final String value;

        scheduleGroupByFilterOptions(final String newValue) {
            value = newValue;
        }

        public String getValue() {
            return value;
        }
    }

    public enum dayCount {
        Seven(7);
        private final int value;

        dayCount(final int newValue) {
            value = newValue;
        }

        public int getValue() {
            return value;
        }
    }

    public ConsoleScheduleNewUIPage() {
        PageFactory.initElements(getDriver(), this);
    }

    @FindBy(css = "div.console-navigation-item-label.Schedule")
    private WebElement goToScheduleButton;

    @FindBy(css = "div[helper-text*='Work in progress Schedule'] span.legend-label")
    private WebElement draft;

    @FindBy(css = "div[helper-text-position='top'] span.legend-label")
    private WebElement published;

    @FindBy(css = "div[helper-text*='final per schedule changes'] span.legend-label")
    private WebElement finalized;

    @FindBy(className = "overview-view")
    private WebElement overviewSectionElement;

    @FindBy(css = "div.sub-navigation-view-link.active")
    private WebElement activatedSubTabElement;

    @FindBy(xpath = "//div[contains(@class,'sub-navigation-view')]//span[contains(text(),'Schedule')]")
    private WebElement goToScheduleTab;

    @FindBy(css = "lg-button[label=\"Analyze\"]")
    private WebElement analyze;

    @FindBy(xpath = "//*[@class=\"version-label-container\"]/div")
    private List<WebElement> scheduleHistoryListInAnalyzePopUp;

    @FindBy(css = "lg-button[label=\"Edit\"]")
    private WebElement edit;

    @FindBy(css = "lg-button[data-tootik=\"Edit Schedule\"]")
    private WebElement newEdit;

    @FindBy(xpath = "//span[contains(text(),'Projected Sales')]")
    private WebElement goToProjectedSalesTab;

    @FindBy(css = "ui-view[name='forecastControlPanel'] span.highlight-when-help-mode-is-on")
    private WebElement salesGuidance;

    @FindBy(css = "[ng-click=\"controlPanel.fns.refreshConfirmation($event)\"]")
    private WebElement refresh;

    @FindBy(css = "button.btn.sch-publish-confirm-btn")
    private WebElement confirmRefreshButton;

    @FindBy(css = "button.btn.successful-publish-message-btn-ok")
    private WebElement okRefreshButton;

    @FindBy(xpath = "//div[contains(text(),'Guidance')]")
    private WebElement guidance;

    @FindBy(xpath = "//div[contains(text(),'Schedule History')]")
    private WebElement scheduleHistoryInAnalyzePopUp;

    @FindBy(xpath = "//div[contains(text(),'Details')]")
    private WebElement versionDetailsInAnalyzePopUp;

    @FindBy(css = "[ng-repeat=\"role in guidanceRoleDetails\"]")
    private List<WebElement> guidanceRoleDetails;

    @FindBy(css = "[ng-repeat=\"role in versionedRoleDetails\"]")
    private List<WebElement> versionRoleDetails;

    @FindBy(xpath = "//span[contains(text(),'Staffing Guidance')]")
    private WebElement goToStaffingGuidanceTab;

//	@FindBy(className="sch-calendar-day-dimension")
//	private List<WebElement> ScheduleCalendarDayLabels;

    @FindBy(css = "div.sub-navigation-view-link")
    private List<WebElement> ScheduleSubTabsElement;

    @FindBy(className = "day-week-picker-arrow-right")
    private WebElement calendarNavigationNextWeekArrow;

    @FindBy(className = "day-week-picker-arrow-left")
    private WebElement calendarNavigationPreviousWeekArrow;

    @FindBy(xpath= "//day-week-picker/div/div/div[3]")
    private WebElement calendarNavigationPreviousWeek;

    @FindBy(css = "[ng-click=\"regenerateFromOverview()\"] button")
    private WebElement generateSheduleButton;

    @FindBy(css = "[ng-click*=\"regenerateFromManagerView()\"]")
    private WebElement reGenerateScheduleButton;

    @FindBy(css = "[label='Generate Schedule']")
    private WebElement generateSheduleForEnterBudgetBtn;

    @FindBy(css = "lg-button[label*=\"ublish\"]")
    private WebElement publishSheduleButton;

    @FindBy(css = "lg-button[label*=\"ublish\"] span span")
    private WebElement txtPublishSheduleButton;

    @FindBy(css = "div.edit-budget span.header-text")
    private WebElement popUpGenerateScheduleTitleTxt;

    @FindBy(css = "span.ok-action-text")
    private WebElement btnGenerateBudgetPopUP;

    @FindBy(css = "div[ng-if='canEditHours(budget)']")
    private List<WebElement> editBudgetHrs;

    @FindBy(css = "span[ng-if='canEditWages(budget)']")
    private List<WebElement> editWagesHrs;

    @FindBy(css = "div.sch-view-dropdown-summary-content-item-heading.ng-binding")
    private WebElement analyzePopupLatestVersionLabel;

    @FindBy(css = "[ng-click=\"controlPanel.fns.analyzeAction($event)\"]")
    private WebElement scheduleAnalyzeButton;

    @FindBy(className = "sch-schedule-analyze-content")
    private WebElement scheduleAnalyzePopup;

    @FindBy(className = "sch-schedule-analyze-dismiss")
    private WebElement scheduleAnalyzePopupCloseButton;

    @FindBy(css = "lg-close.dismiss")
    private WebElement scheduleAnalyzePopupCloseButtonInKS2;

    @FindBy(css = "[ng-click=\"goToSchedule()\"]")
    private WebElement checkOutTheScheduleButton;

    @FindBy(css = "lg-button[label=\"Generate schedule\"]")
    private WebElement generateScheduleBtn;

    @FindBy(className = "console-navigation-item")
    private List<WebElement> consoleNavigationMenuItems;

    @FindBy(css = "[ng-click=\"callOkCallback()\"]")
    private WebElement editAnywayPopupButton;

    @FindBy(css = "[ng-if=\"canShowNewShiftButton()\"]")
    private WebElement addNewShiftOnDayViewButton;

    @FindBy(css = "[label=\"Cancel\"]")
    private WebElement scheduleEditModeCancelButton;

    @FindBy(css = "[label=\"Save\"]")
    private WebElement scheduleEditModeSaveButton;

    @FindBy(css = "[ng-click=\"regenerateFromOverview()\"]")
    private WebElement scheduleGenerateButton;

    @FindBy(css = "#legion-app navigation div:nth-child(4)")
    private WebElement analyticsConsoleName;

    //added by Nishant

//    @FindBy(css = ".modal-content")
//    private WebElement customizeNewShift;

    @FindBy(css = "div.sch-day-view-grid-header span:nth-child(1)")
    private WebElement shiftStartday;

//    @FindBy(css = "div.lgn-time-slider-notch-selector-start span.lgn-time-slider-label")
//    private WebElement customizeShiftStartdayLabelTimeFormat;

    @FindBy(xpath = "//div[contains(@class,'lgn-time-slider-notch-selector-start')]/following-sibling::div[1]")
    private WebElement customizeShiftStartdayLabel;

//    @FindBy(css = "div.lgn-time-slider-notch-selector-end span.lgn-time-slider-label")
//    private WebElement customizeShiftEnddayLabelTimeFormat;

    @FindBy(xpath = "//div[contains(@class,'lgn-time-slider-notch-selector-end')]/following-sibling::div[1]")
    private WebElement customizeShiftEnddayLabel;

    @FindBy(css = "div.lgn-time-slider-notch-selector-start")
    private WebElement sliderNotchStart;

    @FindBy(css = "div.lgn-time-slider-notch-selector-end")
    private WebElement sliderNotchEnd;

    @FindBy(css = "div.lgn-time-slider-notch.droppable")
    private List<WebElement> sliderDroppableCount;

    @FindBy(css = " [ng-click=\"selectChoice($event, choice)\"]")
    private List<WebElement> listWorkRoles;

    @FindBy(css = "button.lgn-dropdown-button:nth-child(1)")
    private WebElement btnWorkRole;

    @FindBy(xpath = "//div[contains(text(),'Open Shift: Auto')]")
    private WebElement textOpenShift;

    @FindBy(xpath = "//div[contains(text(),'Open Shift: Auto')]/parent::div/parent::div/div/div[@class='tma-staffing-option-outer-circle']")
    private WebElement radioBtnOpenShift;

    @FindBy(css = ".tma-staffing-option-text-title")
    private List<WebElement> radioBtnShiftTexts;

    @FindBy(css = ".tma-staffing-option-radio-button")
    private List<WebElement> radioBtnStaffingOptions;

    @FindBy(xpath = "//div[contains(@class,'sch-day-view-right-gutter-text')]//parent::div//parent::div/parent::div[contains(@class,'sch-shift-container')]//div[@class='break-container']")
    private List<WebElement> shiftContainer;

    @FindBy(css = "button.tma-action")
    private WebElement btnSave;

    @FindBy(css = "div.sch-day-view-shift-delete")
    private WebElement shiftDeleteBtn;

    @FindBy(xpath = "//div[contains(text(),'Delete')]")
    private List<WebElement> shiftDeleteGutterText;

    @FindBy(css = "div.sch-day-view-right-gutter-text")
    private List<WebElement> gutterText;

    @FindBy(css = "div.sch-day-view-right-gutter")
    private List<WebElement> gutterCount;

    @FindBy(css = "div.sch-day-view-grid-header.fill span")
    private List<WebElement> gridHeaderDayHour;

    @FindBy(xpath = "//div[contains(@class,'sch-day-view-grid-header fill')]/following-sibling::div//div[@data-tootik='TMs in Schedule']/parent::div")
    private List<WebElement> gridHeaderTeamCount;

    @FindBy(xpath = "//span[contains(text(),'Save')]")
    private WebElement scheduleSaveBtn;

    @FindBy(css = "lg-button[data-tootik=\"Save changes\"]")
    private WebElement newScheduleSaveButton;

    @FindBy(xpath = "//div[contains(@ng-if,'PostSave')]")
    private WebElement popUpPostSave;

    @FindBy(css = "button.btn.sch-ok-single-btn")
    private WebElement btnOK;

    @FindBy(xpath = "//div[contains(@ng-if,'PreSave')]")
    private WebElement popUpPreSave;

    @FindBy(css = "button.btn.sch-save-confirm-btn")
    private WebElement scheduleVersionSaveBtn;

    @FindBy(css = ".tma-search-field-input-text")
    private WebElement textSearch;

    @FindBy(css = "div.tab-label")
    private List<WebElement> btnSearchteamMember;

    @FindBy(css = ".sch-search")
    private WebElement searchIcon;

    @FindBy(css = ".table-row")
    private List<WebElement> tableRowCount;

    @FindBy(xpath = "//div[@class='worker-edit-availability-status']//span[contains(text(),'Available')]")
    private List<WebElement> availableStatus;

    @FindBy(xpath = "//div[@class='worker-edit-availability-status']")
    private List<WebElement> scheduleStatus;

    @FindBy(xpath="//div[@class='tma-search-action']/following-sibling::div[1]//div[@class='worker-edit-availability-status']")
    private List<WebElement> scheduleSearchTeamMemberStatus;

    @FindBy(xpath="//div[@class='tab-label']/span[text()='Search Team Members']")
    private WebElement btnSearchTeamMember;


    @FindBy(xpath="//span[contains(text(),'Best')]")
    private List<WebElement> scheduleBestMatchStatus;

    @FindBy(css="div.tma-empty-search-results")
    private WebElement scheduleNoAvailableMatchStatus;

    @FindBy(css = "div.worker-edit-search-worker-name")
    private List<WebElement> searchWorkerName;

    @FindBy(xpath="//div[@class='tma-search-action']/following-sibling::div[1]//div[@class='worker-edit-search-worker-name']")
    private List<WebElement> searchWorkerDisplayName;

    @FindBy(xpath="//div[@class='tma-search-action']/following-sibling::div[1]//div[@class='worker-edit-search-worker-name']/following-sibling::div")
    private List<WebElement> searchWorkerRole;

    @FindBy(xpath="//div[@class='tma-search-action']/following-sibling::div[1]//div[@class='worker-edit-search-worker-name']/following-sibling::div[2]")
    private List<WebElement> searchWorkerLocation;

    @FindBy(css="[ng-show=\"hasSearchResults()\"] tr.table-row.ng-scope")
    private List<WebElement> searchTMRows;

    @FindBy(xpath="//div[@class='sch-day-view-shift ng-scope']//div[contains(@class,'sch-day-view-shift-time')]")
    private WebElement searchWorkerSchShiftTime;

    @FindBy(xpath="//div[@class='sch-day-view-shift ng-scope']//div[contains(@class,'sch-day-view-worker-time')]")
    private WebElement searchWorkerSchShiftDuration;

    @FindBy(css = "td.table-field.action-field.tr>div")
    private List<WebElement> radionBtnSelectTeamMembers;

    @FindBy(xpath="//div[@class='tma-search-action']/following-sibling::div[1]//div[@ng-click='selectAction($event, worker)']")
    private List<WebElement> radionBtnSearchTeamMembers;

    @FindBy(css="button.tma-action.sch-save")
    private WebElement btnOffer;

    @FindBy(xpath = "//button[contains(text(),'UPDATE')]")
    private WebElement updateAndGenerateScheduleButton;


    @FindBy(css = ".group-by-select-box select")
    private WebElement groupBySelector;

    // Options of group by selectors

    @FindBy(xpath = "//option[contains(@value,'string:All')]")
    private WebElement groupByAll;

    @FindBy(xpath = "//option[contains(@value,'string:WorkRole')]")
    private WebElement groupByWorkRole;

    @FindBy(xpath = "//option[contains(@value,'string:TM')]")
    private WebElement groupByTM;

    @FindBy(xpath = "//option[contains(@value,'string:JobTitle')]")
    private WebElement groupByJobTitle;

    @FindBy(xpath = "//option[contains(@value,'string:Location')]")
    private WebElement groupByLocation;

    // Verify Group by work Role
    @FindBy(css = "div.week-schedule-shift-place.ng-scope.week-schedule-shift-place-ribbon")
    private WebElement workRoleDisaplayedOnScreen;

    @FindBy(css = "[ng-if=\"showColorLabel\"]")
    private List<WebElement> workRoleLabor;
    // Verify Group by TM
    @FindBy(css = "div.week-schedule-right-strip.ng-scope")
    private WebElement tmDetailsDisplayedOnScreen;

    @FindBy(css = "div.generate-modal-budget-step-container")
    private WebElement enterBudgetTable;






    //added by Naval

//    @FindBy(css = "input-field[placeholder='None'] ng-form.input-form.ng-pristine.ng-valid-pattern")
//    private WebElement filterButton;

    @FindBy(css = "[ng-click=\"$ctrl.openFilter()\"]")
    private WebElement filterButton;

    @FindBy(css = "[ng-repeat=\"(key, opts) in $ctrl.displayFilters\"]")
    private List<WebElement> scheduleFilterElements;

    @FindBy(css = "div.lg-filter__wrapper")
    private WebElement filterPopup;

    @FindBy(css = "div.sch-calendar-day-dimension")
    private List<WebElement> weekViewDaysAndDates;

    @FindBy(css = "div.sch-week-view-day-summary")
    private List<WebElement> weekDaySummeryHoursAndTeamMembers;

    @FindBy(css = ".shift-container.week-schedule-shift-wrapper")
    private List<WebElement> shiftsOnScheduleView;


    @FindBy(css = "div.sch-day-view-grid-header.fill")
    private List<WebElement> dayViewShiftsTimeDuration;

    @FindBy(css = "div.sch-day-view-grid-header.tm-count.guidance")
    private List<WebElement> dayViewbudgetedTMCount;

    @FindBy(xpath = "//div[contains(@class,'sch-day-view-grid-header tm-count') and not(contains(@class,'guidance'))]")
    private List<WebElement> dayViewScheduleTMsCount;

    @FindBy(css = "select.ng-valid-required")
    private WebElement scheduleGroupByButton;

    @FindBy(css = "div.tab.ng-scope")
    private List<WebElement> selectTeamMembersOption;

    @FindBy(css = ".tab-set .select .tab-label-text")
    private WebElement selectRecommendedOption;

    @FindBy(css = "div.tma-scroll-table tr")
    private List<WebElement> recommendedScrollTable;

    @FindBy(css = "button.btn-success")
    private WebElement upgradeAndGenerateScheduleBtn;

    @FindBy(css = "div.version-label")
    private List<WebElement> versionHistoryLabels;

    @FindBy(className = "sch-schedule-analyze-dismiss-button")
    private WebElement dismissanAlyzeButton;

    @FindBy(className = "sch-publish-confirm-btn")
    private WebElement publishConfirmBtn;

    @FindBy(css = "span.wm-close-link")
    private WebElement closeLegionPopUp;

    @FindBy(className = "successful-publish-message-btn-ok")
    private WebElement successfulPublishOkBtn;

    @FindBy(css = "div.holiday-logo-container")
    private WebElement holidayLogoContainer;

    @FindBy(css = "tr[ng-repeat=\"day in summary.workingHours\"]")
    private List<WebElement> guidanceWeekOperatingHours;

    @FindBy(className = "sch-group-header")
    private List<WebElement> scheduleShiftHeaders;

    @FindBy(css = "div.card-carousel-card.card-carousel-card-smart-card-required")
    private WebElement requiredActionCard;

    //@FindBy(className = "sch-day-view-shift-outer")
    @FindBy(css = ".sch-day-view-shift")
    private List<WebElement> dayViewAvailableShifts;

    @FindBy(css = "div.card-carousel-card")
    private List<WebElement> carouselCards;

    @FindBy(css = "div.sch-calendar-day-dimension.sch-calendar-day")
    private List<WebElement> ScheduleWeekCalendarDates;

    @FindBy(css = "div.card-carousel")
    private WebElement smartCardPanel;

    @FindBy(css = "div.sch-worker-popover")
    private WebElement shiftPopover;

    @FindBy(css = "button.sch-action.sch-save")
    private WebElement convertToOpenYesBtn;

    @FindBy(css = "[ng-click=\"selectManualOpenShiftAction($event)\"]")
    private WebElement offerToSpecificTMBtn;

    @FindBy(css = "div.card-carousel-arrow.card-carousel-arrow-right")
    private WebElement smartcardArrowRight;

    @FindBy(css = "div.card-carousel-arrow.card-carousel-arrow-left")
    private WebElement smartcardArrowLeft;

    @FindBy(css = "div.schedule-action-buttons")
    private List<WebElement> actionButtonDiv;

    @FindBy(css = "span.weather-forecast-temperature")
    private List<WebElement> weatherTemperatures;

    @FindBy(css = ".weather-forecast-day-name")
    private List<WebElement> weatherDaysOfWeek;

    @FindBy(xpath = "//*[contains(text(),'Weather - Week of')]")
    private WebElement weatherWeekSmartCardHeader;

    @FindBy(css = "input[ng-class='hoursFieldClass(budget)']")
    private List<WebElement> budgetEditHours;

    @FindBy(css = "div.sch-shift-container")
    private List<WebElement> scheduleShiftsRows;

    @FindBy(css = "div.sch-day-view-grid-header.fill")
    private List<WebElement> scheduleShiftTimeHeaderCells;


    @FindBy(css = "div.sch-calendar-date-label")
    private List<WebElement> projectedScheduleDatePeriod;

    @FindBy(css = "img[ng-if=\"hasViolateCompliance(line, scheduleWeekDay)\"]")
    private List<WebElement> complianceReviewDangerImgs;

    @FindBy(css = "lg-dropdown-menu[actions=\"moreActions\"]")
    private WebElement scheduleAdminDropDownBtn;

    @FindBy(css = "div[ng-repeat=\"action in actions\"]")
    private List<WebElement> scheduleAdminDropDownOptions;

    @FindBy(css = "button[ng-click=\"yesClicked()\"]")
    private WebElement unGenerateBtnOnPopup;

    //added by Nishant

    @FindBy(css = "div.lgn-alert-modal")
    private WebElement popUpScheduleOverlap;

    @FindBy(css = "button.lgn-action-button-success")
    private WebElement btnAssignAnyway;

    @FindBy(css = "div.lgn-alert-message")
    private WebElement alertMessage;

    @FindBy(css = "div.schedule-filter-label")
    private WebElement scheduleType;

    @FindBy(css = "lg-button-group[buttons='scheduleTypeOptions'] div.lg-button-group-first")
    private WebElement scheduleTypeSystem;

    @FindBy(css = "lg-button-group[buttons='scheduleTypeOptions'] div.lg-button-group-last")
    private WebElement scheduleTypeManager;

    @FindBy(css = "lg-button-group[buttons='scheduleTypeOptions'] div.lg-button-group-selected")
    private WebElement activScheduleType;

    //moved from ConsoleSchedulePage
    String dayWeekPicker;

    @FindBy(css = "span[ng-if='canEditEstimatedHourlyWage(budget)']")
    private List<WebElement> scheduleDraftWages;

    @FindBy (xpath = "//span[contains(text(),'Schedule')]")
    private WebElement ScheduleSubMenu;

    @FindBy(className="schedule-status-title")
    private List<WebElement> scheduleOverviewWeeksStatus;

    @FindBy(css = "div.fx-center.left-banner")
    private List<WebElement> overviewPageScheduleWeekDurations;

    @FindBy(css = "[ng-click=\"gotoPreviousWeek($event)\"]")
    private WebElement salesForecastCalendarNavigationPreviousWeekArrow;

    @FindBy(className = "left-banner")
    private List<WebElement> weeklyScheduleDateElements;

    @FindBy(css = "[ng-click=\"controlPanel.fns.publishConfirmation($event, false)\"]")
    private WebElement publishButton;

    @FindBy(css = "[ng-if='!loading']")
    private WebElement weeklyScheduleTableBodyElement;

    @FindBy(css = "[ng-if='!isLocationGroup()']")
    private List<WebElement> weeklyScheduleStatusElements;

    @FindBy(css = "[ng-click=\"confirmPublishAction()\"]")
    private WebElement schedulePublishButton;

    @FindBy(css = "[ng-click=\"OkAction()\"]")
    private WebElement successfullyPublishedOkOption;

    @FindBy(css = "span[ng-click='c.action()']")
    private WebElement enterBudgetLink;

    @FindBy(css = "span.header-text.fl-left.ng-binding")
    private WebElement budgetHeader;

    @FindBy(css = "div.day-week-picker-period-active")
    private WebElement daypicker;

    @FindBy(xpath = "//*[text()=\"Day\"]")
    private WebElement daypButton;

    @FindBy(xpath = "//*[text()=\"Week\"]")
    private WebElement weekButton;

    @FindBy (css = "div.day-week-picker-period")
    private List<WebElement> dayPickerAllDaysInDayView;

    @FindBy(css = ".day-week-picker-period fx-center ng-scope day-week-picker-period-active day-week-picker-period-week")
    private WebElement currentActiveWeeks;

    @FindBy(css = "day-week-picker > div > div > div:nth-child(3)>span")
    private WebElement postWeekNextToCurrentWeek;

    @FindBy(css = "day-week-picker > div > div > div:nth-child(5)>span")
    private WebElement futureWeekNextToCurrentWeek;

    @FindBy(css = "div.row-fx.schedule-table-row.ng-scope")
    private List<WebElement> schedulesForWeekOnOverview;

    @FindBy(css = ".cancel-action-text")
    private WebElement enterBudgetCancelButton;

    @FindBy(css = "div.col-sm-10.plr-0-0 > div:nth-child(1) > div > span")
    private WebElement returnToOverviewTab;

    @FindBy(css = "div.row-name-field.ng-binding.ng-scope")
    private WebElement sumOfBudgetHour;

    @FindBy(css = "table:nth-child(2) tr.table-row.ng-scope")
    private WebElement budgetPopUpRows;

    @FindBy(css = "table:nth-child(2) tr.table-row.ng-scope td:nth-child(3)")
    private List<WebElement> guidanceHour;

    @FindBy(css = "table:nth-child(2) tr.table-row.ng-scope td:nth-child(4)")
    private List<WebElement> guidanceWages;

    @FindBy(css = "table:nth-child(2) tr.table-row.ng-scope td:nth-child(5)")
    private List<WebElement> budgetHourWhenBudgetByWagesEnabled;

    @FindBy(css = "table td:nth-child(2)")
    private List<WebElement> budgetDisplayOnScheduleSmartcard;

    @FindBy (css = "table td:nth-child(3)")
    private List<WebElement> scheduleDisplayOnScheduleSmartcard;

    //add by Estelle to get Schedule smart card elements
//    @FindBy(xpath = "//table[@class=\"ng-scope\"]")
//    private WebElement scheduleSmartCard;

    @FindBy(xpath = "//table[@class=\"ng-scope\"]/tbody/tr[2]")
    private WebElement hoursColumn;

    @FindBy(xpath = "//table[@class=\"ng-scope\"]/tbody/tr[3]")
    private WebElement wagesColumn;

//    @FindBy(xpath = "ng-include[ng-repeat='c in cards']")
//    private WebElement budgetOnbudgetSmartCardWhenNoBudgetEntered;

    @FindBy(css = "ng-include[ng-repeat='c in cards']")
    private WebElement budgetOnbudgetSmartCardWhenNoBudgetEntered;

    @FindBy(xpath = "//div[@class='card-carousel-card card-carousel-card-default']//div[contains(text(),'')]/following-sibling::h1")
    private WebElement budgetOnbudgetSmartCard;

//    @FindBy (css = "div.console-navigation-item-label.Schedule")
//    private WebElement consoleSchedulePageTabElement;

    @FindBy (css = "week-view-detail[weekly-schedule-data='weeklyScheduleData']")
    private WebElement scheduleTableWeekView;

    @FindBy (css = "div.sch-day-view-grid")
    private WebElement scheduleTableDayView;

    @FindBy (css = "div.sch-day-view-shift-worker-detail")
    private List<WebElement> scheduleTableWeekViewWorkerDetail;

    @FindBy (css = "div.lg-button-group-first")
    private WebElement scheduleDayView;

    @FindBy (css = "div.lg-button-group-last")
    private WebElement scheduleWeekView;

    @FindBy (css = "div.card-carousel-carousel")
    private WebElement smartcard;

    @FindBy (css = "img.holiday-logo-image")
    private WebElement storeClosed;

    @FindBy (css = "div.day-week-picker-period-active")
    private WebElement currentActiveDay;

    @FindBy (css = "div.day-week-picker-period-week.day-week-picker-period-active")
    private WebElement currentActiveWeek;

    @FindBy (css = "div.sch-shift-transpose-second-row")
    private List<WebElement> scheduleWeekViewGrid;

    @FindBy (css = "[on-change=\"updateGroupBy(value)\"]")
    private WebElement groupByAllIcon;

    @FindBy(css = "[ng-click=\"printActionInProgress() || printAction($event)\"]")
    private WebElement printButton;

    @FindBy(xpath ="//*[text()=\"Portrait\"]")
    private WebElement PortraitButton;

    @FindBy(xpath ="//*[text()=\"Landscape\"]")
    private WebElement LandscapeButton;

    @FindBy(css = "[ng-click=\"showTodos($event)\"]")
    private WebElement todoButton;

    @FindBy (css = ".horizontal.is-shown")
    private WebElement todoSmartCard;

    @FindBy (css = "[ng-click=\"askConfirm('approve')\"]")
    private List<WebElement> toDosInTodoSmartCard;

    @FindBy(css = "[label=\"Print\"]")
    private WebElement printButtonInPrintLayout;

    @FindBy(css = "[label=\"Cancel\"]")
    private WebElement cannelButtonInPrintLayout;

    @FindBy(css = "div.sch-calendar-date-label>span")
    private List<WebElement> schCalendarDateLabel;


    //compliance elements
    @FindBy(css = "[ng-if=\"scheduleSmartCard.complianceViolations\"] div.card-carousel-card")
    private WebElement complianceSmartcardHeader;
//
//    @FindBy(css = ".fa-flag.sch-red")
//    private WebElement redFlagInCompliance;
//
//    @FindBy(css = "[ng-click=\"smartCardShiftFilter('Compliance Review')\"]")
//    private WebElement viewShiftBtn;

    @FindBy(css = "[ng-click=\"smartCardShiftFilter('Compliance Review')\"]")
    private WebElement clearShiftBtn;

    @FindBy(css = "[src=\"img/legion/schedule/shift-info-danger.png\"]")
    private List<WebElement> complianceShitShowIcon;

    //added by Nishant for schedule generation

    @FindBy(css = "div.modal-instance.generate-modal")
    private WebElement copySchedulePopUp;

    @FindBy(css = "div.modal-instance-header-title")
    private WebElement copyScheduleHeader;

    @FindBy(css = ".target-budget")
    private WebElement copyScheduleTargetBudget;

    @FindBy(css = ".target-budget")
    private WebElement copyScheduleLocationName;

    @FindBy(css = "div.suggested div.generate-modal-week-schedules-header-title")
    private WebElement copyScheduleSuggestedScheduleTxt;

    @FindBy(xpath = "//div[contains(text(),'Copy')]")
    private WebElement copyScheduleWindowCopyScheduleTxt;

    @FindBy(css = "div[ng-repeat='schedule in previousWeeksSchedules'] div.generate-modal-week-container")
    private List<WebElement> generateModalWeekContainerForPreviousWeeks;

    @FindBy(css = "div.confirm")
    private WebElement btnContinue;

    List<String> scheduleWeekDate = new ArrayList<String>();
    List<String> scheduleWeekStatus = new ArrayList<String>();

    Map<String, String> weeklyTableRowsDatesAndStatus = new LinkedHashMap<String, String>();


//    final static String consoleScheduleMenuItemText = "Schedule";
//
//
//    public void clickOnScheduleConsoleMenuItem() {
//        if (areListElementVisible(consoleNavigationMenuItems, 10) && consoleNavigationMenuItems.size() != 0) {
//            WebElement consoleScheduleMenuElement = SimpleUtils.getSubTabElement(consoleNavigationMenuItems, consoleScheduleMenuItemText);
//            clickTheElement(consoleScheduleMenuElement);
//            SimpleUtils.pass("'Schedule' Console Menu Loaded Successfully!");
//        } else {
//            SimpleUtils.fail("'Schedule' Console Menu Items Not Loaded Successfully!", false);
//        }
//    }
//
//    @Override
//    public void goToSchedulePage() throws Exception {
//
//        checkElementVisibility(goToScheduleButton);
//        activeConsoleName = analyticsConsoleName.getText();
//        click(goToScheduleButton);
//        SimpleUtils.pass("Schedule Page Loading..!");
//
//        if (isElementLoaded(draft)) {
//            SimpleUtils.pass("Draft is Displayed on the page");
//        } else {
//            SimpleUtils.fail("Draft not displayed on the page", true);
//        }
//
//        if (isElementLoaded(published)) {
//            SimpleUtils.pass("Published is Displayed on the page");
//        } else {
//            SimpleUtils.fail("Published not displayed on the page", true);
//        }
//
//        if (isElementLoaded(finalized)) {
//            SimpleUtils.pass("Finalized is Displayed on the page");
//        } else {
//            SimpleUtils.fail("Finalized not displayed on the page", true);
//        }
//    }
//
//
//    @Override
//    public boolean isSchedulePage() throws Exception {
//        if (isElementLoaded(overviewSectionElement)) {
//            return true;
//        } else {
//            return false;
//        }
//    }


//    @Override
//    public Boolean verifyActivatedSubTab(String SubTabText) throws Exception {
//        if (isElementLoaded(activatedSubTabElement,15)) {
//            if (activatedSubTabElement.getText().toUpperCase().contains(SubTabText.toUpperCase())) {
//                return true;
//            }
//        } else {
//            SimpleUtils.fail("Schedule Page not loaded successfully", true);
//        }
//        return false;
//    }
//
//    @Override
//    public void goToSchedule() throws Exception {
//
//        checkElementVisibility(goToScheduleTab);
//        activeConsoleName = analyticsConsoleName.getText();
//        click(goToScheduleTab);
//        SimpleUtils.pass("Schedule Page Loading..!");
//        CreateSchedulePage createSchedulePage = new ConsoleCreateSchedulePage();
//        if (createSchedulePage.isWeekGenerated()) {
//            if (isElementLoaded(analyze)) {
//                SimpleUtils.pass("Analyze is Displayed on Schdule page");
//            } else {
//                SimpleUtils.fail("Analyze not Displayed on Schedule page", true);
//            }
//            if (isElementLoaded(edit)) {
//                SimpleUtils.pass("Edit is Displayed on Schedule page");
//            } else {
//                SimpleUtils.fail("Edit not Displayed on Schedule page", true);
//            }
//        }
//    }
//
//
//    @Override
//    public void goToProjectedSales() throws Exception {
//        checkElementVisibility(goToProjectedSalesTab);
//        click(goToProjectedSalesTab);
//        SimpleUtils.pass("ProjectedSales Page Loading..!");
//
//        if(isElementLoaded(salesGuidance)){
//            SimpleUtils.pass("Sales Forecast is Displayed on the page");
//        }else{
//            SimpleUtils.fail("Sales Forecast not Displayed on the page",true);
//        }
//
//         verifyRefreshBtnOnSalesForecast();
//    }
//
//    public void verifyRefreshBtnOnSalesForecast() throws Exception {
//        if (getCurrentTestMethodName().contains("InternalAdmin")) {
//            if (isElementLoaded(refresh)) {
//                SimpleUtils.pass("Refresh button is Displayed on Sales Forecast for Legion Internal Admin");
//            } else {
//                SimpleUtils.fail("Refresh button not Displayed on Sales Forecast for Legion Internal Admin", true);
//            }
//        } else {
//            if (!isElementLoaded(refresh)) {
//                SimpleUtils.pass("Refresh button should not be Displayed on Sales Forecast for other than Legion Internal Admin");
//            } else {
//                SimpleUtils.fail("Refresh button Displayed on Sales Forecast for other than Internal Admin", true);
//            }
//        }
//    }
//
//
//    @Override
//    public void goToStaffingGuidance() throws Exception {
//        checkElementVisibility(goToStaffingGuidanceTab);
//        click(goToStaffingGuidanceTab);
//        SimpleUtils.pass("StaffingGuidance Page Loading..!");
//
//        if (isElementLoaded(guidance)) {
//            SimpleUtils.pass("Guidance is Displayed on Staffing Guidance page");
//        } else {
//            SimpleUtils.fail("Guidance not Displayed on Staffing Guidance page", true);
//        }
//
//        if (isElementLoaded(analyze)) {
//            SimpleUtils.pass("Analyze is Displayed on Staffing Guidance page");
//        } else {
//            SimpleUtils.fail("Analyze not Displayed on Staffing Guidance page", true);
//        }
//    }
//
//
//    @Override
//    public void clickOnWeekView() throws Exception {
//		/*WebElement scheduleWeekViewButton = MyThreadLocal.getDriver().
//			findElement(By.cssSelector("[ng-click=\"selectDayWeekView($event, 'week')\"]"));*/
//
//        WebElement scheduleWeekViewButton = MyThreadLocal.getDriver().
//                findElement(By.cssSelector("div.lg-button-group-last"));
//        if (isElementLoaded(scheduleWeekViewButton,15)) {
//            if (!scheduleWeekViewButton.getAttribute("class").toString().contains("selected"))//selected
//            {
//                clickTheElement(scheduleWeekViewButton);
//            }
//            SimpleUtils.pass("Schedule page week view loaded successfully!");
//        } else {
//            SimpleUtils.fail("Schedule Page Week View Button not Loaded Successfully!", true);
//        }
//    }
//
//
//    @Override
//    public void clickOnDayView() throws Exception {
//		/*WebElement scheduleDayViewButton = MyThreadLocal.getDriver().
//			findElement(By.cssSelector("[ng-click=\"selectDayWeekView($event, 'day')\"]"));*/
//        WebElement scheduleDayViewButton = MyThreadLocal.getDriver().
//                findElement(By.cssSelector("div.lg-button-group-first"));
//
//        if (isElementLoaded(scheduleDayViewButton)) {
//            if (!scheduleDayViewButton.getAttribute("class").toString().contains("enabled")) {
//                click(scheduleDayViewButton);
//            }
//            SimpleUtils.pass("Schedule Page day view loaded successfully!");
//        } else {
//            SimpleUtils.fail("Schedule Page Day View Button not Loaded Successfully!", true);
//        }
//    }
//
//
//    @FindBy(xpath = "//*[@class='shift-hover-seperator']/following-sibling::div[1]/div[1]")
//    private WebElement shiftSize;
//
    @FindBy(css = "img[ng-if*='hasViolation']")
    private List<WebElement> infoIcon;
//
//
//    public float calcTotalScheduledHourForDayInWeekView() throws Exception {
//        float sumOfAllShiftsLength = 0;
//        for (int i = 0; i < infoIcon.size(); i++) {
//            if (isElementEnabled(infoIcon.get(i))) {
//                click(infoIcon.get(i));
//                String[] TMShiftSize = shiftSize.getText().split(" ");
//                float shiftSizeInHour = Float.valueOf(TMShiftSize[0]);
//                sumOfAllShiftsLength = sumOfAllShiftsLength + shiftSizeInHour;
//
//            } else {
//                SimpleUtils.fail("Shift not loaded successfully in week view", false);
//            }
//        }
//        return (sumOfAllShiftsLength);
//
//    }

    //added by haya
    @FindBy(css = ".sch-shift-hover.visible")
    private WebElement shiftInfo;

    @FindBy(css = ".shift-container.week-schedule-shift-wrapper")
    private List<WebElement> shifts;
//
//    @FindBy(css = ".sch-calendar-day-summary")
//    private List<WebElement> daySummaries;
//
//    @Override
//    public float newCalcTotalScheduledHourForDayInWeekView() throws Exception {
//        float sumOfAllShiftsLength = 0;
//        if (areListElementVisible(daySummaries,10)){
//            for (int i=0; i<daySummaries.size();i++){
//                String[] TMShiftSize = daySummaries.get(i).findElement(By.cssSelector("span:nth-child(1)")).getText().split(" ");
//                float shiftSizeInHour = Float.valueOf(TMShiftSize[0]);
//                sumOfAllShiftsLength = sumOfAllShiftsLength + shiftSizeInHour;
//            }
//        } else {
//            SimpleUtils.fail("weekDaySummeryHoursAndTeamMembers are not loaded!", false);
//        }
//        return (sumOfAllShiftsLength);
//
//    }

//    @Override
//    public HashMap<String, Float> getScheduleLabelHoursAndWages() throws Exception {
//        HashMap<String, Float> scheduleHoursAndWages = new HashMap<String, Float>();
//        WebElement budgetedScheduledLabelsDivElement = MyThreadLocal.getDriver().findElement(By.cssSelector("[ng-if*=\"isTitleBasedBudget()\"] .card-carousel-card"));
//        if(isElementEnabled(budgetedScheduledLabelsDivElement,5))
//        {
////			Thread.sleep(2000);
//            String scheduleWagesAndHoursCardText = budgetedScheduledLabelsDivElement.getText();
//            String [] tmp =  scheduleWagesAndHoursCardText.split("\n");
//            String[] scheduleWagesAndHours = new String[5];
//            if (tmp.length>6) {
//                scheduleWagesAndHours[0] = tmp[0];
//                scheduleWagesAndHours[1] = tmp[1];
//                scheduleWagesAndHours[2] = tmp[2];
//                scheduleWagesAndHours[3] = tmp[3]+" "+tmp[4]+" "+tmp[5];
//                scheduleWagesAndHours[4] = tmp[6];
//            }else
//                scheduleWagesAndHours =tmp;
//            for(int i = 0; i < scheduleWagesAndHours.length; i++)
//            {
//                if(scheduleWagesAndHours[i].toLowerCase().contains(scheduleHoursAndWagesData.hours.getValue().toLowerCase()))
//                {
//                    if (scheduleWagesAndHours[i].split(" ").length == 4) {
//                        scheduleHoursAndWages = updateScheduleHoursAndWages(scheduleHoursAndWages , scheduleWagesAndHours[i].split(" ")[1],
//                                scheduleHoursAndWagesData.budgetedHours.getValue());
//                        scheduleHoursAndWages = updateScheduleHoursAndWages(scheduleHoursAndWages , scheduleWagesAndHours[i].split(" ")[2],
//                                scheduleHoursAndWagesData.scheduledHours.getValue());
//                        scheduleHoursAndWages = updateScheduleHoursAndWages(scheduleHoursAndWages , scheduleWagesAndHours[i].split(" ")[3],
//                                scheduleHoursAndWagesData.otherHours.getValue());
//                    } else {
//                        scheduleHoursAndWages = updateScheduleHoursAndWages(scheduleHoursAndWages, scheduleWagesAndHours[i].split(" ")[1],
//                                scheduleHoursAndWagesData.budgetedHours.getValue());
//                        scheduleHoursAndWages = updateScheduleHoursAndWages(scheduleHoursAndWages, scheduleWagesAndHours[i + 1],
//                                scheduleHoursAndWagesData.scheduledHours.getValue());
//                        scheduleHoursAndWages = updateScheduleHoursAndWages(scheduleHoursAndWages, scheduleWagesAndHours[i + 2],
//                                scheduleHoursAndWagesData.otherHours.getValue());
//                    }
//                    break;
//                }
//                else if(scheduleWagesAndHours[i].toLowerCase().contains(scheduleHoursAndWagesData.wages.getValue().toLowerCase()))
//                {
//                    if (scheduleWagesAndHours[i].split(" ").length == 4) {
//                        scheduleHoursAndWages = updateScheduleHoursAndWages(scheduleHoursAndWages , scheduleWagesAndHours[i].split(" ")[1]
//                                .replace("$", ""), scheduleHoursAndWagesData.budgetedWages.getValue());
//                        scheduleHoursAndWages = updateScheduleHoursAndWages(scheduleHoursAndWages , scheduleWagesAndHours[i].split(" ")[2]
//                                .replace("$", ""), scheduleHoursAndWagesData.scheduledWages.getValue());
//                        scheduleHoursAndWages = updateScheduleHoursAndWages(scheduleHoursAndWages , scheduleWagesAndHours[i].split(" ")[3]
//                                .replace("$", ""), scheduleHoursAndWagesData.otherWages.getValue());
//                    } else {
//                        scheduleHoursAndWages = updateScheduleHoursAndWages(scheduleHoursAndWages, scheduleWagesAndHours[i].split(" ")[1]
//                                .replace("$", ""), scheduleHoursAndWagesData.budgetedWages.getValue());
//                        scheduleHoursAndWages = updateScheduleHoursAndWages(scheduleHoursAndWages, scheduleWagesAndHours[i + 1]
//                                .replace("$", ""), scheduleHoursAndWagesData.scheduledWages.getValue());
//                        scheduleHoursAndWages = updateScheduleHoursAndWages(scheduleHoursAndWages, scheduleWagesAndHours[i + 1]
//                                .replace("$", ""), scheduleHoursAndWagesData.otherWages.getValue());
//                    }
//                    break;
//                }
//            }
//        }
//        return scheduleHoursAndWages;
//    }


//    public static HashMap<String, Float> updateScheduleHoursAndWages(HashMap<String, Float> scheduleHoursAndWages,
//                                                                     String hours, String hoursAndWagesKey) {
//        scheduleHoursAndWages.put(hoursAndWagesKey, Float.valueOf(hours.replaceAll(",","")));
//        return scheduleHoursAndWages;
//    }

//    @Override
//    public synchronized List<HashMap<String, Float>> getScheduleLabelHoursAndWagesDataForEveryDayInCurrentWeek() throws Exception {
//        List<HashMap<String, Float>> ScheduleLabelHoursAndWagesDataForDays = new ArrayList<HashMap<String, Float>>();
//        List<WebElement> ScheduleCalendarDayLabels = MyThreadLocal.getDriver().findElements(By.className("day-week-picker-period"));
//        if(isScheduleDayViewActive()) {
//            if(ScheduleCalendarDayLabels.size() != 0) {
//                for(WebElement ScheduleCalendarDayLabel: ScheduleCalendarDayLabels)
//                {
//                    click(ScheduleCalendarDayLabel);
//                    ScheduleLabelHoursAndWagesDataForDays.add(getScheduleLabelHoursAndWages());
//                }
//            }
//            else {
//                SimpleUtils.fail("Schedule Page Day View Calender not Loaded Successfully!", true);
//            }
//        }
//        else {
//            SimpleUtils.fail("Schedule Page Day View Button not Active!", true);
//        }
//        return ScheduleLabelHoursAndWagesDataForDays;
//    }


//    @Override
//    public void clickOnScheduleSubTab(String subTabString) throws Exception {
//        if (ScheduleSubTabsElement.size() != 0 && !verifyActivatedSubTab(subTabString)) {
//            for (WebElement ScheduleSubTabElement : ScheduleSubTabsElement) {
//                if (ScheduleSubTabElement.getText().equalsIgnoreCase(subTabString)) {
//                    waitForSeconds(5);
//                    clickTheElement(ScheduleSubTabElement);
//                    waitForSeconds(3);
//                    break;
//                }
//            }
//
//        }
//
//        if (verifyActivatedSubTab(subTabString)) {
//            SimpleUtils.pass("Schedule Page: '" + subTabString + "' tab loaded Successfully!");
//        } else {
//            SimpleUtils.fail("Schedule Page: '" + subTabString + "' tab not loaded Successfully!", true);
//        }
//    }

//    @Override
//    public void navigateWeekViewOrDayViewToPastOrFuture(String nextWeekViewOrPreviousWeekView, int weekCount) {
//        String currentWeekStartingDay = "NA";
//        List<WebElement> ScheduleCalendarDayLabels = MyThreadLocal.getDriver().findElements(By.className("day-week-picker-period"));
//        for (int i = 0; i < weekCount; i++) {
//            if (ScheduleCalendarDayLabels.size() != 0) {
//                currentWeekStartingDay = ScheduleCalendarDayLabels.get(0).getText();
//            }
//
//            int displayedWeekCount = ScheduleCalendarDayLabels.size();
//            for (WebElement ScheduleCalendarDayLabel : ScheduleCalendarDayLabels) {
//                if (ScheduleCalendarDayLabel.getAttribute("class").toString().contains("day-week-picker-period-active")) {
//                    if (nextWeekViewOrPreviousWeekView.toLowerCase().contains("next") || nextWeekViewOrPreviousWeekView.toLowerCase().contains("future")) {
//                        try {
//                            int activeWeekIndex = ScheduleCalendarDayLabels.indexOf(ScheduleCalendarDayLabel);
//                            if (activeWeekIndex < (displayedWeekCount - 1)) {
//                                click(ScheduleCalendarDayLabels.get(activeWeekIndex + 1));
//                            } else {
//                                click(calendarNavigationNextWeekArrow);
//                                click(ScheduleCalendarDayLabels.get(0));
//                            }
//                        } catch (Exception e) {
//                            SimpleUtils.report("Schedule page Calender Next Week Arrows Not Loaded/Clickable after '" + ScheduleCalendarDayLabel.getText().replace("\n", "") + "'");
//                        }
//                    } else {
//                        try {
//                            int activeWeekIndex = ScheduleCalendarDayLabels.indexOf(ScheduleCalendarDayLabel);
//                            if (activeWeekIndex > 0) {
//                                click(ScheduleCalendarDayLabels.get(activeWeekIndex - 1));
//                            } else {
//                                click(calendarNavigationPreviousWeekArrow);
//                                click(ScheduleCalendarDayLabels.get(displayedWeekCount - 1));
//                            }
//                        } catch (Exception e) {
//                            SimpleUtils.fail("Schedule page Calender Previous Week Arrows Not Loaded/Clickable after '" + ScheduleCalendarDayLabel.getText().replace("\n", "") + "'", true);
//                        }
//                    }
//                    break;
//                }
//            }
//        }
//    }


//    @Override
//    public Boolean isWeekGenerated() throws Exception {
//        if (isElementEnabled(generateSheduleButton, 10) && generateSheduleButton.getText().equalsIgnoreCase("Create schedule")) {
//            return false;
//        }else if(isElementEnabled(generateScheduleBtn, 10)){
//            return false;
//        }else if(isElementLoaded(publishSheduleButton, 10)) {
//            return true;
//        }else if(isElementLoaded(reGenerateScheduleButton, 10)) {
//            return true;
//        }else if(isElementLoaded(deleteScheduleButton, 10)) {
//            return true;
//        }
//        if(areListElementVisible(shiftsWeekView,3) || isElementLoaded(editScheduleButton,5)){
//            SimpleUtils.pass("Week: '" + getActiveWeekText() + "' Already Generated!");
//            return true;
//        }
//        return false;
//    }
//
//
//    @Override
//    public Boolean isWeekPublished() throws Exception {
//        if (isElementLoaded(publishSheduleButton,5)) {
//            if (publishSheduleButton.isEnabled()) {
//                return false;
//            } else {
//                clickOnScheduleAnalyzeButton();
//                if (isElementLoaded(analyzePopupLatestVersionLabel)) {
//                    String latestVersion = analyzePopupLatestVersionLabel.getText();
//                    latestVersion = latestVersion.split(" ")[1].split(".")[0];
//                    closeStaffingGuidanceAnalyzePopup();
//                    if (Integer.valueOf(latestVersion) < 1) {
//                        return false;
//                    }
//                }
//            }
//        } else if (isConsoleMessageError()) {
//            return false;
//        }
//        SimpleUtils.pass("Week: '" + getActiveWeekText() + "' Already Published!");
//        return true;
//
//    }
//
//
//
//    @Override
//    public void generateSchedule() throws Exception {
//        if (isElementLoaded(generateSheduleButton)) {
//            click(generateSheduleButton);
//            Thread.sleep(2000);
//            if (isElementLoaded(generateScheduleBtn))
//                click(generateScheduleBtn);
//            Thread.sleep(4000);
//            if (isElementLoaded(checkOutTheScheduleButton)) {
//                click(checkOutTheScheduleButton);
//                SimpleUtils.pass("Schedule Generated Successfuly!");
//            }
//
//            Thread.sleep(2000);
//            if (isElementLoaded(upgradeAndGenerateScheduleBtn)) {
//                click(upgradeAndGenerateScheduleBtn);
//                Thread.sleep(5000);
//                if (isElementLoaded(checkOutTheScheduleButton)) {
//                    click(checkOutTheScheduleButton);
//                    SimpleUtils.pass("Schedule Generated Successfuly!");
//                }
//            }
//        } else {
//            SimpleUtils.assertOnFail("Schedule Already generated for active week!", false, true);
//        }
//    }
//
//
//    public Boolean isScheduleWeekViewActive() {
//        WebElement scheduleWeekViewButton = MyThreadLocal.getDriver().
//                findElement(By.cssSelector("[ng-click=\"selectDayWeekView($event, 'week')\"]"));
//        if (scheduleWeekViewButton.getAttribute("class").toString().contains("enabled")) {
//            return true;
//        }
//        return false;
//    }
//

//    public Boolean isScheduleDayViewActive() {
////        WebElement scheduleDayViewButton = MyThreadLocal.getDriver().
////                findElement(By.cssSelector("[ng-click=\"selectDayWeekView($event, 'day')\"]"));
//        WebElement scheduleDayViewButton = MyThreadLocal.getDriver().
//                findElement(By.cssSelector("div.lg-button-group-first"));
//        if (scheduleDayViewButton.getAttribute("class").contains("selected")) {
//            return true;
//        }
//        return false;
//    }

//    public void clickOnScheduleAnalyzeButton() throws Exception {
//        if (isElementLoaded(analyze)) {
//            click(analyze);
//            if (isElementLoaded(scheduleHistoryInAnalyzePopUp,5)) {
//                SimpleUtils.pass("Analyze button is clickable and pop up page displayed");
//            }
//        } else {
//            SimpleUtils.fail("Schedule Analyze Button not loaded successfully!", false);
//        }
//    }
//
//    public void closeStaffingGuidanceAnalyzePopup() throws Exception {
//        if (isStaffingGuidanceAnalyzePopupAppear()) {
//            click(scheduleAnalyzePopupCloseButton);
//        }
//    }
//
//    public Boolean isStaffingGuidanceAnalyzePopupAppear() throws Exception {
//        if (isElementLoaded(scheduleAnalyzePopup)) {
//            return true;
//        }
//        return false;
//    }

//    public String getScheduleWeekStartDayMonthDate() {
//        String scheduleWeekStartDuration = "NA";
//        WebElement scheduleCalendarActiveWeek = MyThreadLocal.getDriver().findElement(By.className("day-week-picker-period-active"));
//        try {
//            if (isElementLoaded(scheduleCalendarActiveWeek)) {
//                scheduleWeekStartDuration = scheduleCalendarActiveWeek.getText().replace("\n", "");
//            }
//        } catch (Exception e) {
//            SimpleUtils.fail("Calender duration bar not loaded successfully", true);
//        }
//        return scheduleWeekStartDuration;
//    }

//    public void clickOnEditButton() throws Exception {
//        if (isElementEnabled(newEdit,2)) {
//            click(newEdit);
//            if (isElementLoaded(editAnywayPopupButton, 2)) {
//                click(editAnywayPopupButton);
//                SimpleUtils.pass("Schedule edit shift page loaded successfully!");
//            } else {
//                SimpleUtils.pass("Schedule edit shift page loaded successfully for Draft or Publish Status");
//            }
//        } else {
//            SimpleUtils.pass("Schedule Edit button is not enabled Successfully!");
//        }
//    }

//    public void clickOnSuggestedButton() throws Exception {
//        if (isElementEnabled(scheduleTypeSystem, 5)) {
//            clickTheElement(scheduleTypeSystem);
//            SimpleUtils.pass("legion button is clickable");
//        }else {
//            SimpleUtils.fail("the schedule is not generated, generated schedule firstly",true);
//        }
//    }
//
//    @FindBy(xpath = "//*[contains(@class,'day-week-picker-period-active')]/following-sibling::div[1]")
//    private WebElement immediateNextToCurrentActiveWeek;
//
//    @FindBy(xpath = "//*[contains(@class,'day-week-picker-period-active')]/preceding-sibling::div[1]")
//    private WebElement immediatePastToCurrentActiveWeek;
//
//    public void clickImmediateNextToCurrentActiveWeekInDayPicker() {
//        if (isElementEnabled(immediateNextToCurrentActiveWeek, 30)) {
//            click(immediateNextToCurrentActiveWeek);
//        } else {
//            SimpleUtils.report("This is a last week in Day Week picker");
//        }
//    }
//
//    public void clickImmediatePastToCurrentActiveWeekInDayPicker() {
//        if (isElementEnabled(immediatePastToCurrentActiveWeek, 30)) {
//            click(immediatePastToCurrentActiveWeek);
//        } else {
//            SimpleUtils.report("This is a last week in Day Week picker");
//        }
//    }


//    public Boolean isAddNewDayViewShiftButtonLoaded() throws Exception {
//        if (isElementLoaded(createNewShiftWeekView)) {
//            return true;
//        } else {
//            return false;
//        }
//
//    }

//    public void clickOnCancelButtonOnEditMode() throws Exception {
//        if (isElementLoaded(scheduleEditModeCancelButton)) {
//            click(scheduleEditModeCancelButton);
//            SimpleUtils.pass("Schedule edit shift page cancelled successfully!");
//        }
//    }
//
//    public Boolean isGenerateButtonLoaded() throws Exception {
//        if (isElementLoaded(scheduleGenerateButton,2)) {
//            return true;
//        }
//        return false;
//    }
//
//    public Boolean isGenerateButtonLoadedForManagerView() throws Exception {
//        if (isElementLoaded(generateScheduleBtn,2)) {
//            return true;
//        }
//        return false;
//    }
//
//    public Boolean isReGenerateButtonLoadedForManagerView() throws Exception {
//        if (isElementLoaded(reGenerateScheduleButton, 10)) {
//            return true;
//        }
//        return false;
//    }
//
//    public String getActiveWeekDayMonthAndDateForEachDay() throws Exception {
//        String activeWeekTimeDuration = "";
//        List<WebElement> ScheduleCalendarDayLabels = MyThreadLocal.getDriver().findElements(By.className("sch-calendar-day-dimension"));
//        if (ScheduleCalendarDayLabels.size() != 0) {
//            for (WebElement ScheduleCalendarDayLabel : ScheduleCalendarDayLabels) {
//                if (activeWeekTimeDuration != "")
//                    activeWeekTimeDuration = activeWeekTimeDuration + "," + ScheduleCalendarDayLabel.getText().replace("\n", " ");
//                else
//                    activeWeekTimeDuration = ScheduleCalendarDayLabel.getText().replace("\n", " ");
//            }
//        }
//        return activeWeekTimeDuration;
//    }
//
//    public Boolean validateScheduleActiveWeekWithOverviewCalendarWeek(String overviewCalendarWeekDate, String overviewCalendarWeekDays, String scheduleActiveWeekDuration) {
//        String[] overviewCalendarDates = overviewCalendarWeekDate.split(",");
//        String[] overviewCalendarDays = overviewCalendarWeekDays.split(",");
//        String[] scheduleActiveDays = scheduleActiveWeekDuration.split(",");
//        int index;
//        if (overviewCalendarDates.length == overviewCalendarDays.length && overviewCalendarDays.length == scheduleActiveDays.length) {
//            for (index = 0; index < overviewCalendarDates.length; index++) {
//                // Verify Days on Schedule Active week with Overview Calendar week
//                if (scheduleActiveDays[index].startsWith(overviewCalendarDays[index])) {
//                    // Verify Days on Schedule Active week with Overview Calendar week
//                    if (scheduleActiveDays[index].contains(overviewCalendarDays[index])) {
//                        SimpleUtils.pass("Schedule week dayAndDate matched with Overview calendar DayAndDate for '" + scheduleActiveDays[index] + "'");
//                    }
//                }
//            }
//            if (index != 0)
//                return true;
//
//        }
//        return false;
//    }

//    public boolean isCurrentScheduleWeekPublished() {
//        String scheduleStatus = "No Published Schedule";
//        try {
//            List<WebElement> noPublishedSchedule = MyThreadLocal.getDriver().findElements(By.className("holiday-text"));
//            if (noPublishedSchedule.size() > 0) {
//                if (noPublishedSchedule.get(0).getText().contains(scheduleStatus))
//                    return false;
//            } else if (isConsoleMessageError()) {
//                return false;
//            } else if (isElementLoaded(publishSheduleButton)) {
//                return false;
//            } else if (isElementLoaded(generateSheduleButton, 5)) {
//                return false;
//            }
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//        SimpleUtils.pass("Schedule is Published for current Week!");
//        return true;
//    }

//    public boolean isConsoleMessageError() throws Exception {
//        List<WebElement> carouselCards = MyThreadLocal.getDriver().findElements(By.cssSelector("div.card-carousel-card.card-carousel-card-default"));
//        WebElement activeWeek = MyThreadLocal.getDriver().findElement(By.className("day-week-picker-period-active"));
//        if (carouselCards.size() != 0) {
//            for (WebElement carouselCard : carouselCards) {
//                if (carouselCard.getText().toUpperCase().contains("CONSOLE MESSAGE")) {
//                    SimpleUtils.report("Week: '" + activeWeek.getText().replace("\n", " ") + "' Not Published because of Console Message Error: '" + carouselCard.getText().replace("\n", " ") + "'");
//                    return true;
//                }
//            }
//        }
//        return false;
//    }

//    public String getActiveWeekText() throws Exception {
//        if (isElementLoaded(MyThreadLocal.getDriver().findElement(By.className("day-week-picker-period-active")),15))
//            return MyThreadLocal.getDriver().findElement(By.className("day-week-picker-period-active")).getText().replace("\n", " ");
//        return "";
//    }

//    @Override
//    public void validatingRefreshButtononPublishedSchedule() throws Exception {
//        // TODO Auto-generated method stub
//
//    }
//
//    @Override
//    public void isGenerateScheduleButton() throws Exception {
//        // TODO Auto-generated method stub
//
//    }
//
//    @Override
//    public void validatingScheduleRefreshButton() throws Exception {
//        // TODO Auto-generated method stub
//
//    }

//    @Override
//    public void clickPublishBtn() throws Exception {
//        if(isElementEnabled(publishSheduleButton)){
//            click(publishSheduleButton);
//        } else {
//            SimpleUtils.fail("Publish button is not loaded!", false);
//        }
//    }
//
//    // Added by Nora
//    @FindBy (css = "span.wm-close-link")
//    private WebElement closeButton;
//
//    @Override
//    public void clickOnSchedulePublishButton() throws Exception {
//        // TODO Auto-generated method stub
//        if(isElementEnabled(publishSheduleButton)){
//            click(publishSheduleButton);
//            if(isElementEnabled(publishConfirmBtn))
//            {
////                WebElement switchIframe = getDriver().findElement(By.xpath("//iframe[@id='walkme-proxy-iframe']"));
////			    getDriver().switchTo().frame(switchIframe);
////			    if(isElementEnabled(closeLegionPopUp)){
////			        click(closeLegionPopUp);
////                }
////                getDriver().switchTo().defaultContent();
//                click(publishConfirmBtn);
////			    if(isElementLoaded(closeLegionPopUp)){
////			        click(closeLegionPopUp);
////                }
//                SimpleUtils.pass("Schedule published successfully for week: '"+ getActiveWeekText() +"'");
//                // It will pop up a window: Welcome to Legion!
//                if (isElementLoaded(closeButton, 5)) {
//                    click(closeButton);
//                }
//                if(isElementEnabled(successfulPublishOkBtn))
//                {
//                    click(successfulPublishOkBtn);
//                }
//            }
//        }
//    }

    //added by Nishant

//    public void navigateDayViewToPast(String PreviousWeekView, int dayCount)
//    {
//        String currentWeekStartingDay = "NA";
//        List<WebElement> ScheduleCalendarDayLabels = MyThreadLocal.getDriver().findElements(By.className("day-week-picker-period"));
//        for(int i = 0; i < dayCount; i++)
//        {
//
//            try {
//                click(ScheduleCalendarDayLabels.get(i));
//                clickOnEditButton();
//                clickOnCancelButtonOnEditMode();
//            } catch (Exception e) {
//                SimpleUtils.fail("Schedule page Calender Previous Week Arrows Not Loaded/Clickable", true);
//            }
//
//
//        }
//    }
//
//
//    //added by Nishant
//
//
//    public String clickNewDayViewShiftButtonLoaded() throws Exception
//    {
//        String textStartDay = null;
//        if(isElementEnabled(addNewShiftOnDayViewButton,5))
//        {
//            SimpleUtils.pass("User is allowed to add new shift for current or future week!");
//            if(isElementEnabled(shiftStartday,10)){
//                String[] txtStartDay = shiftStartday.getText().split(" ");
//                textStartDay = txtStartDay[0];
//            }else{
//                SimpleUtils.fail("Shift Start day not getting Loaded!",true);
//            }
//            click(addNewShiftOnDayViewButton);
//        }
//        else
//        {
//            SimpleUtils.fail("User is not allowed to add new shift for current or future week!",true);
//        }
//        return textStartDay;
//    }

//    public void customizeNewShiftPage() throws Exception
//    {
//        if(isElementLoaded(customizeNewShift,15))
//        {
//            SimpleUtils.pass("Customize New Shift Page loaded Successfully!");
//        }
//        else
//        {
//            SimpleUtils.fail("Customize New Shift Page not loaded Successfully!",false);
//        }
//    }
//
//
//    public void compareCustomizeStartDay(String textStartDay) throws Exception
//    {
//        if(isElementLoaded(customizeShiftStartdayLabel,10))
//        {
////			String[] actualTextStartDay = customizeShiftStartdayLabel.getText().split(":");
//            if(customizeShiftStartdayLabel.getText().equals(textStartDay)){
//                SimpleUtils.pass("Start time on Custimize New shift page "+customizeShiftStartdayLabel.getText()+" is same as "+textStartDay);
//            }else{
//                SimpleUtils.fail("Start time on Custimize New shift page "+customizeShiftStartdayLabel.getText()+" is not same as "+textStartDay,true);
//            }
//        }else
//        {
//            SimpleUtils.    fail("Customize New Shift Page not loaded Successfully!",false);
//        }
//
//    }
//
//
//    public void clickOnStartDay(String textStartDay) throws Exception
//    {
//        if(isElementLoaded(customizeShiftStartdayLabel))
//        {
////			String[] actualTextStartDay = customizeShiftStartdayLabel.getText().split(":");
//            if(customizeShiftStartdayLabel.getText().equals(textStartDay)){
//                SimpleUtils.pass("Start time on Customize New shift page "+customizeShiftStartdayLabel.getText()+" is same as "+textStartDay);
//            }else{
//                SimpleUtils.fail("Start time on Customize New shift page "+customizeShiftStartdayLabel.getText()+" is not same as "+textStartDay,true);
//            }
//        }else
//        {
//            SimpleUtils.fail("Customize New Shift Page not loaded Successfully!",false);
//        }
//
//    }

//    @FindBy(css = ".modal-dialog ")
//    WebElement popUpDialog;
//    @FindBy(css = ".modal-dialog .publish-confirm-modal-message-container-compliance")
//    WebElement complianceWarningMsgInConfirmModal;
//    @Override
//    public String getMessageForComplianceWarningInPublishConfirmModal() throws Exception {
//        if (isElementLoaded(popUpDialog, 5)){
//            return popUpDialog.findElement(By.cssSelector(".publish-confirm-modal-message-container-compliance")).getText();
//        } else {
//            SimpleUtils.fail("No dialog pop up.", false);
//        }
//        return null;
//    }
//
//    @Override
//    public boolean isComplianceWarningMsgLoad() throws Exception {
//        if (isElementLoaded(complianceWarningMsgInConfirmModal, 10)){
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public void clickConfirmBtnOnPublishConfirmModal() throws Exception {
//        if (isElementLoaded(publishConfirmBtn)) {
//            clickTheElement(publishConfirmBtn);
//            SimpleUtils.pass("Schedule published successfully for week: '" + getActiveWeekText() + "'");
//            // It will pop up a window: Welcome to Legion!
//            if (isElementLoaded(closeButton, 5)) {
//                clickTheElement(closeButton);
//            }
//            if (isElementLoaded(successfulPublishOkBtn)) {
//                clickTheElement(successfulPublishOkBtn);
//            }
//            if (isElementLoaded(publishSheduleButton, 5)) {
//                // Wait for the Publish button to disappear.
//                waitForSeconds(10);
//            }
//        } else {
//            SimpleUtils.fail("Comfirm button is not loaded successfully!", false);
//        }
//    }

//    public void moveSliderAtSomePoint(String shiftTime, int shiftStartingCount, String startingPoint) throws Exception
//    {
//        if(startingPoint.equalsIgnoreCase("End")){
//            if(isElementLoaded(sliderNotchEnd,10) && sliderDroppableCount.size()>0){
//                SimpleUtils.pass("Shift timings with Sliders loaded on page Successfully for End Point");
//                for(int i= shiftStartingCount; i<= sliderDroppableCount.size();i++){
//                    if(i == (shiftStartingCount + Integer.parseInt(shiftTime))){
//                        WebElement element = getDriver().findElement(By.cssSelector("div.lgn-time-slider-notch.droppable:nth-child("+(i+2)+")"));
//                        mouseHoverDragandDrop(sliderNotchEnd,element);
//                        WebElement ele = getDriver().findElement(By.xpath("//div[contains(@class,'lgn-time-slider-notch-selector-end')]/following-sibling::div[1]"));
//                        String txt = ele.getAttribute("innerHTML");
//                        if(customizeShiftEnddayLabel.getAttribute("class").contains("PM")){
//                            MyThreadLocal.setScheduleHoursEndTime(customizeShiftEnddayLabel.getText() + ":00PM");
//                            break;
//                        }else{
//                            MyThreadLocal.setScheduleHoursEndTime(customizeShiftEnddayLabel.getText() + ":00AM");
//                            break;
//                        }
//                    }
//                }
//
//            }else{
//                SimpleUtils.fail("Shift timings with Sliders not loading on page Successfully", false);
//            }
//        }else if(startingPoint.equalsIgnoreCase("Start")){
//            if(isElementLoaded(sliderNotchStart,10) && sliderDroppableCount.size()>0){
//                SimpleUtils.pass("Shift timings with Sliders loaded on page Successfully for Starting point");
//                for(int i= shiftStartingCount; i<= sliderDroppableCount.size();i++){
//                    if(i == (shiftStartingCount + Integer.parseInt(shiftTime))){
//                        WebElement element = getDriver().findElement(By.cssSelector("div.lgn-time-slider-notch.droppable:nth-child("+(i+2)+")"));
//                        mouseHoverDragandDrop(sliderNotchStart,element);
//                        if(customizeShiftStartdayLabel.getAttribute("class").contains("AM")){
//                            MyThreadLocal.setScheduleHoursStartTime(customizeShiftStartdayLabel.getText() + ":00AM");
//                            break;
//                        }else{
//                            MyThreadLocal.setScheduleHoursStartTime(customizeShiftStartdayLabel.getText() + ":00PM");
//                            break;
//                        }
//                    }
//                }
//
//            }else{
//                SimpleUtils.fail("Shift timings with Sliders not loading on page Successfully", false);
//            }
//        }
//    }
//
//    public HashMap<String, String> calculateHourDifference() throws Exception {
//        Float scheduleHoursDifference = 0.0f;
//        HashMap<String, String> shiftTimeSchedule = new HashMap<String, String>();
//        if(isElementLoaded(sliderNotchStart,10) && sliderDroppableCount.size()>0){
//            String scheduledHoursStartTime = MyThreadLocal.getScheduleHoursStartTime();
//            String scheduledHoursEndTime = MyThreadLocal.getScheduleHoursEndTime();
//            scheduleHoursDifference =  SimpleUtils.convertDateIntotTwentyFourHrFormat(scheduledHoursStartTime , scheduledHoursEndTime);
////			String[] customizeStartTimeLabel = customizeShiftStartdayLabel.getText().split(":");
////			String[] customizeEndTimeLabel = customizeShiftEnddayLabel.getText().split(":");
//            SimpleUtils.pass("Schedule hour difference is "+scheduleHoursDifference);
//            shiftTimeSchedule.put("ScheduleHrDifference",Float.toString(scheduleHoursDifference));
//            shiftTimeSchedule.put("CustomizeStartTimeLabel",customizeShiftStartdayLabel.getText());
//            shiftTimeSchedule.put("CustomizeEndTimeLabel",customizeShiftEnddayLabel.getText());
//        }
//        return shiftTimeSchedule;
//    }

//    public void selectWorkRole(String workRoles) throws Exception {
//        if (isElementLoaded(btnWorkRole, 20)) {
//            click(btnWorkRole);
//            SimpleUtils.pass("Work Role button clicked Successfully");
//        } else {
//            SimpleUtils.fail("Work Role button is not clickable", false);
//        }
//        if (listWorkRoles.size() > 0) {
//            for (WebElement listWorkRole : listWorkRoles) {
//                if (listWorkRole.getText().toLowerCase().contains(workRoles.toLowerCase())) {
//                    click(listWorkRole);
//                    SimpleUtils.pass("Work Role " + workRoles + "selected Successfully");
//                    break;
//                } else {
//                    SimpleUtils.report("Work Role " + workRoles + " not selected");
//                }
//            }
//
//        } else {
//            SimpleUtils.fail("Work Roles size are empty", false);
//        }
//
//    }
//
//    public void clickRadioBtnStaffingOption(String staffingOption) throws Exception {
//        boolean flag = false;
//        int index = -1;
//        if (radioBtnStaffingOptions.size() != 0 && radioBtnShiftTexts.size() != 0 &&
//                radioBtnStaffingOptions.size() == radioBtnShiftTexts.size()) {
//
//            for (WebElement radioBtnShiftText : radioBtnShiftTexts) {
//                index = index + 1;
//                if (radioBtnShiftText.getText().contains(staffingOption)) {
//                    click(radioBtnStaffingOptions.get(index));
//                    SimpleUtils.pass(radioBtnShiftText.getText() + "Radio Button clicked Successfully!");
//                    flag = true;
//                    break;
//                }
//            }
//
//            if (flag == false) {
//                SimpleUtils.fail("No Radio Button Selected!", false);
//            }
//
//        } else {
//            SimpleUtils.fail("Staffing option Radio Button is not clickable!", false);
//        }
//    }
//
//    public void clickOnCreateOrNextBtn() throws Exception {
//        if (isElementLoaded(btnSave, 20)) {
//            click(btnSave);
//            SimpleUtils.pass("Create or Next Button clicked Successfully on Customize new Shift page!");
//        } else {
//            SimpleUtils.fail("Create or Next Button not clicked Successfully on Customize new Shift page!", false);
//        }
//    }
//
//    public HashMap<List<String>, List<String>> calculateTeamCount() throws Exception {
//        HashMap<List<String>, List<String>> gridDayHourTeamCount = new HashMap<List<String>, List<String>>();
//        List<String> gridDayCount = new ArrayList<String>();
//        List<String> gridTeamCount = new ArrayList<String>();
//        if (gridHeaderDayHour.size() != 0 && gridHeaderTeamCount.size() != 0 &&
//                gridHeaderDayHour.size() == gridHeaderTeamCount.size()) {
//            for (int i = 0; i < gridHeaderDayHour.size(); i++) {
//                gridDayCount.add(gridHeaderDayHour.get(i).getText());
//                gridTeamCount.add(gridHeaderTeamCount.get(i).getText());
//                SimpleUtils.pass("Day Hour is " + gridHeaderDayHour.get(i).getText() + " And Team Count is " + gridHeaderTeamCount.get(i).getText());
//            }
//        } else {
//            SimpleUtils.fail("Team Count and Day hour does not match", false);
//        }
//        gridDayHourTeamCount.put(gridDayCount, gridTeamCount);
//        return gridDayHourTeamCount;
//    }
//
//    public List<String> calculatePreviousTeamCount(
//            HashMap<String, String> shiftTimeSchedule, HashMap<List<String>, List<String>>
//            gridDayHourPrevTeamCount) throws Exception {
//        int count = 0;
//        waitForSeconds(3);
//        List<String> gridDayHourTeamCount = new ArrayList<String>();
//        exit:
//        for (Entry<List<String>, List<String>> entry : gridDayHourPrevTeamCount.entrySet()) {
//            List<String> previousKeys = entry.getKey();
//            List<String> val = entry.getValue();
//            for (String previousKey : previousKeys) {
//                String[] arrayPreviousKey = previousKey.split(" ");
//                count = count + 1;
//                if (shiftTimeSchedule.get("CustomizeStartTimeLabel").equals(arrayPreviousKey[0])) {
//                    for (int j = 0; j < Float.parseFloat(shiftTimeSchedule.get("ScheduleHrDifference")); j++) {
//                        count = count + 1;
//                        gridDayHourTeamCount.add(val.get(count - 2));
//                    }
//                    break exit;
//                }
//            }
//        }
//        return gridDayHourTeamCount;
//    }
//
//    public List<String> calculateCurrentTeamCount(HashMap<String, String> shiftTiming) throws Exception {
//        int count = 0;
//        waitForSeconds(3);
//        HashMap<List<String>, List<String>> gridDayHourCurrentTeamCount = calculateTeamCount();
//        List<String> gridDayHourTeamCount = new ArrayList<String>();
//        exit:
//        for (Entry<List<String>, List<String>> entry : gridDayHourCurrentTeamCount.entrySet()) {
//            List<String> previousKeys = entry.getKey();
//            List<String> val = entry.getValue();
//            for (String previousKey : previousKeys) {
//                String[] arrayPreviousKey = previousKey.split(" ");
//                count = count + 1;
//                if (shiftTiming.get("CustomizeStartTimeLabel").equals(arrayPreviousKey[0])) {
//                    for (int j = 0; j < Float.parseFloat(shiftTiming.get("ScheduleHrDifference")); j++) {
//                        count = count + 1;
//                        gridDayHourTeamCount.add(val.get(count - 2));
//                    }
//                    break exit;
//                }
//            }
//        }
//        return gridDayHourTeamCount;
//    }

//    public void clickSaveBtn() throws Exception {
//        if (isElementLoaded(newScheduleSaveButton)) {
//            click(newScheduleSaveButton);
//            clickOnVersionSaveBtn();
//            clickOnPostSaveBtn();
//        } else {
//            SimpleUtils.fail("Schedule Save button not clicked Successfully!", false);
//        }
//    }
//
//    public void clickOnVersionSaveBtn() throws Exception {
//        if (isElementLoaded(popUpPreSave) && isElementLoaded(scheduleVersionSaveBtn)) {
//            click(scheduleVersionSaveBtn);
//            SimpleUtils.pass("Schedule Version Save button clicked Successfully!");
//            waitForSeconds(3);
//        } else {
//            SimpleUtils.fail("Schedule Version Save button not clicked Successfully!", false);
//        }
//    }
//
//    public void clickOnPostSaveBtn() throws Exception {
//        if (isElementLoaded(popUpPostSave) && isElementLoaded(btnOK)) {
//            click(btnOK);
//            SimpleUtils.pass("Schedule Ok button clicked Successfully!");
//            waitForSeconds(3);
//        } else {
//            SimpleUtils.fail("Schedule Ok button not clicked Successfully!", false);
//        }
//    }

//    public HashMap<String, ArrayList<WebElement>> getAvailableFilters() {
//        HashMap<String, ArrayList<WebElement>> scheduleFilters = new HashMap<String, ArrayList<WebElement>>();
//        try {
//            if (isElementLoaded(filterButton,15)) {
//                if (filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
//                    click(filterButton);
//                for (WebElement scheduleFilterElement : scheduleFilterElements) {
//                    WebElement filterLabel = scheduleFilterElement.findElement(By.className("lg-filter__category-label"));
//                    String filterType = filterLabel.getText().toLowerCase().replace(" ", "");
//                    List<WebElement> filters = scheduleFilterElement.findElements(By.cssSelector("input-field[type=\"checkbox\"]"/*"[ng-repeat=\"opt in opts\"]"*/));
//                    ArrayList<WebElement> filterList = new ArrayList<WebElement>();
//                    for (WebElement filter : filters) {
//                        filterList.add(filter);
//                    }
//                    scheduleFilters.put(filterType, filterList);
//                }
//            } else {
//                SimpleUtils.fail("Filters button not found on Schedule page!", false);
//            }
//        } catch (Exception e) {
//            SimpleUtils.fail("Filters button not loaded successfully on Schedule page!", true);
//        }
//        return scheduleFilters;
//    }

//    public void filterScheduleByWorkRoleAndShiftType(boolean isWeekView) throws Exception {
//        waitForSeconds(10);
//        String shiftTypeFilterKey = "shifttype";
//        String workRoleFilterKey = "workrole";
//        HashMap<String, ArrayList<WebElement>> availableFilters = getAvailableFilters();
//        if (availableFilters.size() > 1) {
//            ArrayList<WebElement> shiftTypeFilters = availableFilters.get(shiftTypeFilterKey);
//            ArrayList<WebElement> workRoleFilters = availableFilters.get(workRoleFilterKey);
//            for (WebElement workRoleFilter : workRoleFilters) {
//                if (filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
//                    click(filterButton);
//                unCheckFilters(workRoleFilters);
//                click(workRoleFilter);
//                SimpleUtils.report("Data for Work Role: '" + workRoleFilter.getText() + "'");
//                if (isWeekView)
//                    filterScheduleByShiftTypeWeekView(shiftTypeFilters);
//                else
//                    filterScheduleByShiftTypeDayView(shiftTypeFilters);
//            }
//        } else {
//            SimpleUtils.fail("Filters are not appears on Schedule page!", false);
//        }
//    }

//    public void filterScheduleByShiftTypeWeekView(ArrayList<WebElement> shiftTypeFilters) {
//        //String shiftType = "";
//        for (WebElement shiftTypeFilter : shiftTypeFilters) {
//            try {
//                Thread.sleep(1000);
//                if (filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
//                    click(filterButton);
//                unCheckFilters(shiftTypeFilters);
//                String shiftType = shiftTypeFilter.getText();
//                SimpleUtils.report("Data for Shift Type: '" + shiftType + "'");
//                click(shiftTypeFilter);
//                click(filterButton);
//                String cardHoursAndWagesText = "";
//                SmartCardPage smartCardPage = new ConsoleSmartCardPage();
//                HashMap<String, Float> hoursAndWagesCardData = smartCardPage.getScheduleLabelHoursAndWages();
//                for (Entry<String, Float> hoursAndWages : hoursAndWagesCardData.entrySet()) {
//                    if (cardHoursAndWagesText != "")
//                        cardHoursAndWagesText = cardHoursAndWagesText + ", " + hoursAndWages.getKey() + ": '" + hoursAndWages.getValue() + "'";
//                    else
//                        cardHoursAndWagesText = hoursAndWages.getKey() + ": '" + hoursAndWages.getValue() + "'";
//                }
//                SimpleUtils.report("Active Week Card's Data: " + cardHoursAndWagesText);
//                getHoursAndTeamMembersForEachDaysOfWeek();
//                SimpleUtils.assertOnFail("Sum of Daily Schedule Hours not equal to Active Week Schedule Hours!", verifyActiveWeekDailyScheduleHoursInWeekView(), true);
//
//                if (!getActiveGroupByFilter().toLowerCase().contains(scheduleGroupByFilterOptions.groupbyTM.getValue().toLowerCase())
//                        && !shiftType.toLowerCase().contains("open"))
//                    verifyActiveWeekTeamMembersCountAvailableShiftCount();
//            } catch (Exception e) {
//                SimpleUtils.fail("Unable to get Card data for active week!", true);
//            }
//        }
//    }


//    public void filterScheduleByShiftTypeDayView(ArrayList<WebElement> shiftTypeFilters) {
//        for (WebElement shiftTypeFilter : shiftTypeFilters) {
//            try {
//                if (filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
//                    click(filterButton);
//                unCheckFilters(shiftTypeFilters);
//                SimpleUtils.report("Data for Shift Type: '" + shiftTypeFilter.getText() + "'");
//                click(shiftTypeFilter);
//                click(filterButton);
//                String cardHoursAndWagesText = "";
//                SmartCardPage smartCardPage = new ConsoleSmartCardPage();
//                HashMap<String, Float> hoursAndWagesCardData = smartCardPage.getScheduleLabelHoursAndWages();
//                for (Entry<String, Float> hoursAndWages : hoursAndWagesCardData.entrySet()) {
//                    if (cardHoursAndWagesText != "")
//                        cardHoursAndWagesText = cardHoursAndWagesText + ", " + hoursAndWages.getKey() + ": '" + hoursAndWages.getValue() + "'";
//                    else
//                        cardHoursAndWagesText = hoursAndWages.getKey() + ": '" + hoursAndWages.getValue() + "'";
//                }
//                SimpleUtils.report("Active Week Card's Data: " + cardHoursAndWagesText);
//                String timeDurationText = "";
//                for (String timeDuration : getScheduleDayViewGridTimeDuration()) {
//                    if (timeDurationText == "")
//                        timeDurationText = timeDuration;
//                    else
//                        timeDurationText = timeDurationText + " | " + timeDuration;
//                }
//                SimpleUtils.report("Schedule Day View Shift Duration: " + timeDurationText);
//
//                String budgetedTeamMembersCount = "";
//                for (String budgetedTeamMembers : getScheduleDayViewBudgetedTeamMembersCount()) {
//                    if (budgetedTeamMembersCount == "")
//                        budgetedTeamMembersCount = budgetedTeamMembers;
//                    else
//                        budgetedTeamMembersCount = budgetedTeamMembersCount + " | " + budgetedTeamMembers;
//                }
//                SimpleUtils.report("Schedule Day View budgeted Team Members count: " + budgetedTeamMembersCount);
//
//                String scheduleTeamMembersCount = "";
//                for (String scheduleTeamMembers : getScheduleDayViewScheduleTeamMembersCount()) {
//                    if (scheduleTeamMembersCount == "")
//                        scheduleTeamMembersCount = scheduleTeamMembers;
//                    else
//                        scheduleTeamMembersCount = scheduleTeamMembersCount + " | " + scheduleTeamMembers;
//                }
//                SimpleUtils.report("Schedule Day View budgeted Team Members count: " + scheduleTeamMembersCount);
//            } catch (Exception e) {
//                SimpleUtils.fail("Unable to get Card data for active week!", true);
//            }
//        }
//    }


//    public ArrayList<String> getScheduleDayViewGridTimeDuration() {
//        ArrayList<String> gridTimeDurations = new ArrayList<String>();
//        if (dayViewShiftsTimeDuration.size() != 0) {
//            for (WebElement timeDuration : dayViewShiftsTimeDuration) {
//                gridTimeDurations.add(timeDuration.getText());
//            }
//        }
//
//        return gridTimeDurations;
//    }


//    public ArrayList<String> getScheduleDayViewBudgetedTeamMembersCount() {
//        ArrayList<String> BudgetedTMsCount = new ArrayList<String>();
//        if (dayViewbudgetedTMCount.size() != 0) {
//            for (WebElement BudgetedTMs : dayViewbudgetedTMCount) {
//                BudgetedTMsCount.add(BudgetedTMs.getText());
//            }
//        }
//
//        return BudgetedTMsCount;
//    }

//    public ArrayList<String> getScheduleDayViewScheduleTeamMembersCount() {
//        ArrayList<String> ScheduledTMsCount = new ArrayList<String>();
//        if (dayViewScheduleTMsCount.size() != 0) {
//            for (WebElement scheduleTMs : dayViewScheduleTMsCount) {
//                ScheduledTMsCount.add(scheduleTMs.getText());
//            }
//        }
//
//        return ScheduledTMsCount;
//    }

//
//    public void unCheckFilters(ArrayList<WebElement> filterElements) throws Exception {
//        if (filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
//            click(filterButton);
//        waitForSeconds(2);
//        for (WebElement filterElement : filterElements) {
//            if (isElementLoaded(filterElement, 5)) {
//                WebElement filterCheckBox = filterElement.findElement(By.cssSelector("input[type=\"checkbox\"]"));
//                String elementClasses = filterCheckBox.getAttribute("class").toLowerCase();
//                if (elementClasses.contains("ng-not-empty"))
//                    click(filterElement);
//            }
//        }
//    }
//
//    public void unCheckFilters() throws Exception {
//        if (filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
//            click(filterButton);
//        if (areListElementVisible(filters, 10)) {
//            for (WebElement filterElement : filters) {
//                waitForSeconds(2);
//                WebElement filterCheckBox = filterElement.findElement(By.cssSelector("input[type=\"checkbox\"]"));
//                String elementClasses = filterCheckBox.getAttribute("class").toLowerCase();
//                if (elementClasses.contains("ng-not-empty"))
//                    clickTheElement(filterCheckBox);
//            }
//        }
//    }

//    public void checkFilters(ArrayList<WebElement> filterElements) {
//        if (filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
//            click(filterButton);
//        waitForSeconds(2);
//        for (WebElement filterElement : filterElements) {
//            WebElement filterCheckBox = filterElement.findElement(By.cssSelector("input[type=\"checkbox\"]"));
//            String elementClasses = filterCheckBox.getAttribute("class").toLowerCase();
//            if (elementClasses.contains("ng-empty"))
//                click(filterElement);
//        }
//    }

//    public void getHoursAndTeamMembersForEachDaysOfWeek() {
//        String weekDaysAndDatesText = "";
//        String weekDaysHoursAndTMsCount = "";
//        try {
//            if (weekViewDaysAndDates.size() != 0) {
//                for (WebElement weekViewDayAndDate : weekViewDaysAndDates) {
//                    if (weekDaysAndDatesText != "")
//                        weekDaysAndDatesText = weekDaysAndDatesText + " | " + weekViewDayAndDate.getText();
//                    else
//                        weekDaysAndDatesText = weekViewDayAndDate.getText();
//                }
//                SimpleUtils.report("Active Week Days And Dates: " + weekDaysAndDatesText);
//            }
//            if (weekDaySummeryHoursAndTeamMembers.size() != 0) {
//                for (WebElement weekDayHoursAndTMs : weekDaySummeryHoursAndTeamMembers) {
//                    if (weekDaysHoursAndTMsCount != "")
//                        weekDaysHoursAndTMsCount = weekDaysHoursAndTMsCount + " | " + weekDayHoursAndTMs.getText();
//                    else
//                        weekDaysHoursAndTMsCount = weekDayHoursAndTMs.getText();
//                }
//                SimpleUtils.report("Active Week Hours And TeamMembers: " + weekDaysHoursAndTMsCount);
//            }
//        } catch (Exception e) {
//            SimpleUtils.fail("Unable to get Hours & Team Members for active Week!", true);
//        }
//    }

//    public boolean verifyActiveWeekDailyScheduleHoursInWeekView() {
//        Float weekDaysScheduleHours = (float) 0;
//        Float activeWeekScheduleHoursOnCard = (float) 0;
//        try {
//            SmartCardPage smartCardPage = new ConsoleSmartCardPage();
//            activeWeekScheduleHoursOnCard = smartCardPage.getScheduleLabelHoursAndWages().get(scheduleHoursAndWagesData.scheduledHours.getValue());
//            if (weekDaySummeryHoursAndTeamMembers.size() != 0) {
//                for (WebElement weekDayHoursAndTMs : weekDaySummeryHoursAndTeamMembers) {
//                    float dayScheduleHours = Float.parseFloat(weekDayHoursAndTMs.getText().split("HRs")[0]);
//                    weekDaysScheduleHours = (float) (weekDaysScheduleHours + Math.round(dayScheduleHours * 10.0) / 10.0);
//                }
//            }
//            float totalShiftSizeForWeek = calcTotalScheduledHourForDayInWeekView();
////            System.out.println("sum" + totalShiftSizeForWeek);
//            if (totalShiftSizeForWeek == activeWeekScheduleHoursOnCard) {
//                SimpleUtils.pass("Sum of all the shifts in a week equal to Week Schedule Hours!('" + totalShiftSizeForWeek + "/" + activeWeekScheduleHoursOnCard + "')");
//                return true;
//            } else {
//                SimpleUtils.fail("Sum of all the shifts in an week is not equal to Week scheduled Hour!('" + totalShiftSizeForWeek + "/" + activeWeekScheduleHoursOnCard + "')", false);
//            }
////            if(weekDaysScheduleHours.equals(activeWeekScheduleHoursOnCard))
////            {
////                SimpleUtils.pass("Sum of Daily Schedule Hours equal to Week Schedule Hours! ('"+weekDaysScheduleHours+ "/"+activeWeekScheduleHoursOnCard+"')");
////                return true;
////            }
//        } catch (Exception e) {
//            SimpleUtils.fail("Unable to Verify Daily Schedule Hours with Week Schedule Hours!", true);
//        }
//        return false;
//    }

//    //added by haya
//    private boolean newVerifyActiveWeekDailyScheduleHoursInWeekView() throws Exception {
//        Float weekDaysScheduleHours = 0.0f;
//        Float activeWeekScheduleHoursOnCard = 0.0f;
//        SmartCardPage smartCardPage = new ConsoleSmartCardPage();
//        activeWeekScheduleHoursOnCard = smartCardPage.getScheduleLabelHoursAndWages().get(scheduleHoursAndWagesData.scheduledHours.getValue())
//                + smartCardPage.getScheduleLabelHoursAndWages().get(scheduleHoursAndWagesData.otherHours.getValue());
//        if (weekDaySummeryHoursAndTeamMembers.size() != 0) {
//            for (WebElement weekDayHoursAndTMs : weekDaySummeryHoursAndTeamMembers) {
//                float dayScheduleHours = Float.parseFloat(weekDayHoursAndTMs.getText().split("HRs")[0]);
//                weekDaysScheduleHours = (float) (weekDaysScheduleHours + Math.round(dayScheduleHours * 10.0) / 10.0);
//            }
//        }
//        float totalShiftSizeForWeek = newCalcTotalScheduledHourForDayInWeekView();
//        if (activeWeekScheduleHoursOnCard - totalShiftSizeForWeek <= 0.06) {
//            SimpleUtils.pass("Sum of all the shifts in a week equal to Week Schedule Hours!('" + totalShiftSizeForWeek + "/" + activeWeekScheduleHoursOnCard + "')");
//            return true;
//        } else {
//            SimpleUtils.fail("Sum of all the shifts in an week is not equal to Week scheduled Hour!('" + totalShiftSizeForWeek + "/" + activeWeekScheduleHoursOnCard + "')", false);
//        }
//        return false;
//    }

//    public boolean verifyActiveWeekTeamMembersCountAvailableShiftCount() {
//        int weekDaysTMsCount = 0;
//        int weekDaysShiftsCount = 0;
//        try {
//            if (weekDaySummeryHoursAndTeamMembers.size() != 0) {
//                for (WebElement weekDayHoursAndTMs : weekDaySummeryHoursAndTeamMembers) {
//                    String TeamMembersCount = weekDayHoursAndTMs.getText().split("HRs")[1].replace("TMs", "").trim();
//                    weekDaysTMsCount = weekDaysTMsCount + Integer.parseInt(TeamMembersCount);
//                }
//            }
//
//
//            if (shiftsOnScheduleView.size() != 0) {
//                for (WebElement shiftOnScheduleView : shiftsOnScheduleView) {
//                    if (shiftOnScheduleView.getText().trim().length() > 0 && shiftOnScheduleView.isDisplayed()) {
//                        weekDaysShiftsCount = weekDaysShiftsCount + 1;
//                    }
//                }
//            }
//
//            if (weekDaysTMsCount == weekDaysShiftsCount) {
//                SimpleUtils.pass("Sum of Daily Team Members Count equal to Sum of Daily Shifts Count! ('" + weekDaysTMsCount + "/" + weekDaysShiftsCount + "')");
//                return true;
//            } else {
//                SimpleUtils.fail("Sum of Daily Team Members Count not equal to Sum of Daily Shifts Count! ('" + weekDaysTMsCount + "/" + weekDaysShiftsCount + "')", true);
//            }
//        } catch (Exception e) {
//            SimpleUtils.fail("Unable to Verify Daily Team Members Count with Daily Shifts Count!", true);
//        }
//        return false;
//    }
//
//    public void selectGroupByFilter(String optionVisibleText) {
//        Select groupBySelectElement = new Select(scheduleGroupByButton);
//        List<WebElement> scheduleGroupByButtonOptions = groupBySelectElement.getOptions();
//        groupBySelectElement.selectByIndex(1);
//        for (WebElement scheduleGroupByButtonOption : scheduleGroupByButtonOptions) {
//            if (scheduleGroupByButtonOption.getText().toLowerCase().contains(optionVisibleText.toLowerCase())) {
//                groupBySelectElement.selectByIndex(scheduleGroupByButtonOptions.indexOf(scheduleGroupByButtonOption));
//                SimpleUtils.report("Selected Group By Filter: '" + optionVisibleText + "'");
//            }
//        }
//    }

//    public void verifySpecificDayPartExists(String dayPart) throws Exception{
//        boolean flag = false;
//        for (WebElement dayPartTemp: dayPartTitlesOnSchedulePage){
//            System.out.println(dayPartTemp.getText());
//            if (dayPartTemp.getText().equalsIgnoreCase(dayPart)){
//                flag = true;
//                break;
//            }
//        }
//        if (flag){
//            SimpleUtils.pass(dayPart + " exists!");
//        } else {
//            SimpleUtils.fail(dayPart + " doesn't exists!", false);
//        }
//    }

//    public String getNextDayPart(String dayPart) throws Exception{
//        String nextDayPart = null;
//        for (int i = 0 ; i < dayPartTitlesOnSchedulePage.size(); i++){
//            if (dayPartTitlesOnSchedulePage.get(i).getText().equalsIgnoreCase(dayPart)){
//                if (i < dayPartTitlesOnSchedulePage.size()-1){
//                    nextDayPart = dayPartTitlesOnSchedulePage.get(i+1).getText();
//                }
//            }
//        }
//        return nextDayPart;
//    }


//    public int getIndexInAllShiftPlacesOfTheOnlyAddedOne(String name) throws Exception{
//        int index = 0;
//        for (int i = 0; i < shiftPlaces.size(); i++){
//            System.out.println(shiftPlaces.get(i).getText());
//            if (shiftPlaces.get(i).getText().toLowerCase().contains(name.toLowerCase())){
//                index = i;
//                break;
//            }
//        }
//        return index;
//    }

//    @FindBy(css = ".drag-target-place")
//    List<WebElement> shiftPlaces;
//    @FindBy(css = ".week-schedule-shift-title")
//    List<WebElement> dayPartTitlesOnSchedulePage;
//    @Override
//    public void verifyNewAddedShiftFallsInDayPart(String nameOfTheShift, String dayPart) throws Exception {
//        int index = 0;
//        int indexOfDayPart = 0;
//        int indexOfNextDayPart = 0;
//        String nextDayPart = getNextDayPart(dayPart);
//
//        if (areListElementVisible(shiftPlaces,15) && areListElementVisible(dayPartTitlesOnSchedulePage, 15)){
//            verifySpecificDayPartExists(dayPart);
//            index = getIndexInAllShiftPlacesOfTheOnlyAddedOne(nameOfTheShift);
//            for (int i = 0; i < shiftPlaces.size(); i++){
//                if (shiftPlaces.get(i).getText().toLowerCase().contains(dayPart.toLowerCase())){
//                    indexOfDayPart = i;
//                    continue;
//                }
//                if (nextDayPart != null && shiftPlaces.get(i).getText().toLowerCase().contains(nextDayPart.toLowerCase())){
//                    indexOfNextDayPart = i;
//                    break;
//                }
//            }
//            System.out.println(indexOfDayPart);
//            System.out.println(index);
//            System.out.println(indexOfNextDayPart);
//            if (indexOfNextDayPart == 0 && index > indexOfDayPart){
//                SimpleUtils.pass("successful!");
//            } else if (indexOfNextDayPart != 0 && index > indexOfDayPart && index < indexOfNextDayPart){
//                SimpleUtils.pass("successful!");
//            } else {
//                SimpleUtils.fail("fail!", false);
//            }
//        } else {
//
//        }
//    }

//    public ArrayList<WebElement> getAllAvailableShiftsInWeekView() {
//        ArrayList<WebElement> avalableShifts = new ArrayList<WebElement>();
//        if (shiftsOnScheduleView.size() != 0) {
//            for (WebElement shiftOnScheduleView : shiftsOnScheduleView) {
//                if (shiftOnScheduleView.getText().trim().length() > 0 && shiftOnScheduleView.isDisplayed()) {
//                    avalableShifts.add(shiftOnScheduleView);
//                }
//            }
//        }
//        return avalableShifts;
//    }

//    public ArrayList<HashMap<String, String>> getHoursAndShiftsCountForEachWorkRolesInWeekView() throws Exception {
//        String workRoleFilterKey = "workrole";
//        ArrayList<HashMap<String, String>> eachWorkRolesData = new ArrayList<HashMap<String, String>>();
//        HashMap<String, ArrayList<WebElement>> availableFilters = getAvailableFilters();
//        SmartCardPage smartCardPage = new ConsoleSmartCardPage();
//        if (availableFilters.size() > 1) {
//            ArrayList<WebElement> workRoleFilters = availableFilters.get(workRoleFilterKey);
//            for (WebElement workRoleFilter : workRoleFilters) {
//                if (filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
//                    click(filterButton);
//                unCheckFilters(workRoleFilters);
//                click(workRoleFilter);
//
//                //adding workrole name
//                HashMap<String, String> workRole = new HashMap<String, String>();
//                workRole.put("workRole", workRoleFilter.getText());
//
//                //Adding Card data (Hours & Wages)
//                for (Entry<String, Float> e : smartCardPage.getScheduleLabelHoursAndWages().entrySet())
//                    workRole.put(e.getKey(), e.getValue().toString());
//                // Adding Shifts Count
//                workRole.put("shiftsCount", "" + getAllAvailableShiftsInWeekView().size());
//
//                eachWorkRolesData.add(workRole);
//            }
//            unCheckFilters(workRoleFilters);
//            if (!filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
//                click(filterButton);
//        } else {
//            SimpleUtils.fail("Filters are not appears on Schedule page!", false);
//        }
//
//        return eachWorkRolesData;
//    }
//
//
//    public ArrayList<Float> getAllVesionLabels() throws Exception {
//        ArrayList<Float> allVersions = new ArrayList<Float>();
//        if (isElementLoaded(analyze)) {
//            click(analyze);
//            if (versionHistoryLabels.size() != 0) {
//                for (WebElement eachVersionLabel : versionHistoryLabels) {
//                    String LabelsText = eachVersionLabel.getText().split("\\(Created")[0];
//                    allVersions.add(Float.valueOf(LabelsText.split("Version")[1].trim()));
//                }
//            }
//            closeAnalyticPopup();
//        }
//
//
//        return allVersions;
//    }
//
//    public void closeAnalyticPopup() throws Exception {
//        if (isElementLoaded(dismissanAlyzeButton)) {
//            click(dismissanAlyzeButton);
//        }
//    }
//

//    @Override
//    public void publishActiveSchedule() throws Exception {
//        if (!isCurrentScheduleWeekPublished()) {
//            if (isConsoleMessageError())
//                SimpleUtils.fail("Schedule Can not be publish because of Action Require for week: '" + getActiveWeekText() + "'", false);
//            else {
//                clickTheElement(publishSheduleButton);
//                if (isElementLoaded(publishConfirmBtn)) {
//                    clickTheElement(publishConfirmBtn);
//                    SimpleUtils.pass("Schedule published successfully for week: '" + getActiveWeekText() + "'");
//                    // It will pop up a window: Welcome to Legion!
//                    if (isElementLoaded(closeButton, 5)) {
//                        clickTheElement(closeButton);
//                    }
//                    if (isElementLoaded(successfulPublishOkBtn)) {
//                        clickTheElement(successfulPublishOkBtn);
//                    }
//                    if (isElementLoaded(publishSheduleButton, 5)) {
//                        // Wait for the Publish button to disappear.
//                        waitForSeconds(10);
//                    }
//                }
//            }
//
//        }
//    }

//    public boolean isPublishButtonLoaded() {
//        try {
//            if (isElementLoaded(publishSheduleButton))
//                return true;
//            else
//                return false;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//
//    @Override
//    public boolean inActiveWeekDayClosed(int dayIndex) throws Exception {
//        CreateSchedulePage createSchedulePage = new ConsoleCreateSchedulePage();
//        if (createSchedulePage.isWeekGenerated()) {
//            ScheduleCommonPage scheduleCommonPage = new ConsoleScheduleCommonPage();
//            scheduleCommonPage.navigateDayViewWithIndex(dayIndex);
//            if (isElementLoaded(holidayLogoContainer))
//                return true;
//        } else {
//            if (guidanceWeekOperatingHours.size() != 0) {
//                if (guidanceWeekOperatingHours.get(dayIndex).getText().contains("Closed"))
//                    return true;
//            }
//        }
//        return false;
//
//    }

//    @Override
//    public void navigateDayViewWithIndex(int dayIndex) {
//        if (dayIndex < 7 && dayIndex >= 0) {
//            try {
//                clickOnDayView();
//                List<WebElement> ScheduleCalendarDayLabels = MyThreadLocal.getDriver().findElements(By.className("day-week-picker-period"));
//                if (ScheduleCalendarDayLabels.size() == 7) {
//                    click(ScheduleCalendarDayLabels.get(dayIndex));
//                }
//            } catch (Exception e) {
//                SimpleUtils.fail("Unable to navigate to in Day View", false);
//            }
//        } else {
//            SimpleUtils.fail("Invalid dayIndex value to verify Store is Closed for the day", false);
//        }
//
//    }

//    @Override
//    public void navigateDayViewWithDayName(String dayName) throws Exception {
//        // The day name should be: Fri, Sat, Sun, Mon, Tue, Wed, Thu
//        clickOnDayView();
//        List<WebElement> scheduleCalendarDayLabels = MyThreadLocal.getDriver().findElements(By.className("day-week-picker-period"));
//        if (scheduleCalendarDayLabels.size() == 7) {
//            boolean isDayNameExists = false;
//            for (WebElement day: scheduleCalendarDayLabels ){
//                if (day.getText().contains(dayName)) {
//                    click(day);
//                    isDayNameExists = true;
//                    break;
//                }
//            }
//            if(!isDayNameExists){
//                SimpleUtils.fail("The day name is not exists", false);
//            }
//        } else
//            SimpleUtils.fail("Week day picker display incorrectly! ", false);
//    }

//    @Override
//    public String getActiveGroupByFilter() throws Exception {
//        String selectedGroupByFilter = "";
//        if (isElementLoaded(scheduleGroupByButton)) {
//            Select groupBySelectElement = new Select(scheduleGroupByButton);
//            selectedGroupByFilter = groupBySelectElement.getFirstSelectedOption().getText();
//        } else {
//            SimpleUtils.fail("Group By Filter not loaded successfully for active Week/Day: '" + getActiveWeekText() + "'", false);
//        }
//        return selectedGroupByFilter;
//    }
//
//
//    @Override
//    public boolean isActiveWeekHasOneDayClose() throws Exception {
//        for (int index = 0; index < dayCount.Seven.getValue(); index++) {
//            if (inActiveWeekDayClosed(index))
//                return true;
//        }
//        return false;
//    }
//
//    @Override
//    public boolean isActiveWeekAssignedToCurrentUser(String userName) throws Exception {
//        ScheduleCommonPage scheduleCommonPage = new ConsoleScheduleCommonPage();
//        scheduleCommonPage.clickOnWeekView();
//        if (shiftsOnScheduleView.size() != 0) {
//            for (WebElement shiftOnScheduleView : shiftsOnScheduleView) {
//                if (shiftOnScheduleView.getText().trim().length() > 0 && shiftOnScheduleView.isDisplayed()
//                        && shiftOnScheduleView.getText().toLowerCase().contains(userName.toLowerCase())) {
//                    SimpleUtils.pass("Active Week/Day: '" + getActiveWeekText() + "' assigned to '" + userName + "'.");
//                    return true;
//                }
//            }
//        }
//        SimpleUtils.report("Active Week/Day: '" + getActiveWeekText() + "' not assigned to '" + userName + "'.");
//        return false;
//    }
//
//
//    @Override
//    public boolean isScheduleGroupByWorkRole(String workRoleOption) throws Exception {
//        ScheduleMainPage scheduleMainPage = new ConsoleScheduleMainPage();
//        if (scheduleMainPage.getActiveGroupByFilter().equalsIgnoreCase(workRoleOption)) {
//            String filterType = "workrole";
//            ArrayList<WebElement> availableWorkRoleFilters = scheduleMainPage.getAvailableFilters().get(filterType);
//            if (availableWorkRoleFilters.size() == scheduleShiftHeaders.size()) {
//                return true;
//            }
//
//        }
//        return false;
//    }

//    public void selectWorkRoleFilterByIndex(int index, boolean isClearWorkRoleFilters) throws Exception {
//        String filterType = "workrole";
//        ArrayList<WebElement> availableWorkRoleFilters = getAvailableFilters().get(filterType);
//        if (availableWorkRoleFilters.size() >= index) {
//            if (isClearWorkRoleFilters)
//                unCheckFilters(availableWorkRoleFilters);
//            click(availableWorkRoleFilters.get(index));
//            SimpleUtils.pass("Schedule Work Role:'" + availableWorkRoleFilters.get(index).getText() + "' Filter selected Successfully!");
//        }
//    }
//
//    @Override
//    public void selectWorkRoleFilterByText(String workRoleLabel, boolean isClearWorkRoleFilters) throws Exception {
//        String filterType = "workrole";
//        ArrayList<WebElement> availableWorkRoleFilters = getAvailableFilters().get(filterType);
//        if (isClearWorkRoleFilters)
//            unCheckFilters(availableWorkRoleFilters);
//        for (WebElement availableWorkRoleFilter : availableWorkRoleFilters) {
//            if (availableWorkRoleFilter.getText().contains(workRoleLabel)) {
//                click(availableWorkRoleFilter);
//                SimpleUtils.pass("Schedule Work Role:'" + availableWorkRoleFilter.getText() + "' Filter selected Successfully!");
//                break;
//            }
//        }
//        if (!filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
//            click(filterButton);
//    }
//
//    @Override
//    public ArrayList<String> getSelectedWorkRoleOnSchedule() throws Exception {
//        ScheduleMainPage scheduleMainPage = new ConsoleScheduleMainPage();
//        ArrayList<String> selectedScheduleTabWorkRoles = new ArrayList<String>();
//        String filterType = "workrole";
//        ArrayList<WebElement> availableWorkRoleFilters = scheduleMainPage.getAvailableFilters().get(filterType);
//        if (availableWorkRoleFilters.size() > 0) {
//            for (WebElement filterElement : availableWorkRoleFilters) {
//                WebElement filterCheckBox = filterElement.findElement(By.cssSelector("input[type=\"checkbox\"]"));
//                String elementClasses = filterCheckBox.getAttribute("class").toLowerCase();
//                if (elementClasses.contains("ng-not-empty")) {
//                    selectedScheduleTabWorkRoles.add(filterElement.getText());
//                    SimpleUtils.report("Selected Work Role: '" + filterElement.getText() + "'");
//                }
//            }
//            if (!filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
//                click(filterButton);
//        }
//        return selectedScheduleTabWorkRoles;
//    }
//
//
//    @Override
//    public boolean isRequiredActionUnAssignedShiftForActiveWeek() throws Exception {
//        String unAssignedShiftRequireActionText = "unassigned shift";
//        if (isElementLoaded(requiredActionCard)) {
//            if (requiredActionCard.getText().toLowerCase().contains(unAssignedShiftRequireActionText)) {
//                SimpleUtils.report("Required Action for Unassigned Shift found for the week: '" + getActiveWeekText() + "'");
//                return true;
//            }
//        }
//        return false;
//    }
//
//
//    public void clickOnRefreshButton() throws Exception {
//        if (isElementLoaded(refresh))
//            click(refresh);
//        if (isElementLoaded(confirmRefreshButton))
//            click(confirmRefreshButton);
//        if (isElementLoaded(okRefreshButton)) {
//            click(okRefreshButton);
//            SimpleUtils.pass("Active Week: '" + getActiveWeekText() + "' refreshed successfully!");
//        }
//    }

//    public void selectShiftTypeFilterByText(String filterText) throws Exception {
//        String shiftTypeFilterKey = "shifttype";
//        ArrayList<WebElement> shiftTypeFilters = getAvailableFilters().get(shiftTypeFilterKey);
//        unCheckFilters(shiftTypeFilters);
//        for (WebElement shiftTypeOption : shiftTypeFilters) {
//            if (shiftTypeOption.getText().toLowerCase().contains(filterText.toLowerCase())) {
//                click(shiftTypeOption);
//                break;
//            }
//        }
//        if (!filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
//            click(filterButton);
//    }


//    public List<WebElement> getAvailableShiftsInDayView() {
//        return dayViewAvailableShifts;
//    }
//
//    public void dragShiftToRightSide(WebElement shift, int xOffSet) {
//        WebElement shiftRightSlider = shift.findElement(By.cssSelector("div.sch-day-view-shift-pinch.right"));
//        moveDayViewCards(shiftRightSlider, xOffSet);
//    }
//
//    public void moveDayViewCards(WebElement webElement, int xOffSet) {
//        Actions builder = new Actions(MyThreadLocal.getDriver());
//        builder.moveToElement(webElement)
//                .clickAndHold()
//                .moveByOffset(xOffSet, 0)
//                .release()
//                .build()
//                .perform();
//    }
//

//    public boolean isSmartCardAvailableByLabel(String cardLabel) throws Exception {
//        waitForSeconds(4);
//        if (carouselCards.size() != 0) {
//            for (WebElement carouselCard : carouselCards) {
//                smartCardScrolleToLeft();
//                if (carouselCard.isDisplayed() && carouselCard.getText().toLowerCase().contains(cardLabel.toLowerCase())
//                        && isSmartcardContainText(carouselCard))
//                    return true;
//                else if (!carouselCard.isDisplayed()) {
//                    while (isSmartCardScrolledToRightActive() == true) {
//                        if (carouselCard.isDisplayed() && carouselCard.getText().toLowerCase().contains(cardLabel.toLowerCase())
//                                && isSmartcardContainText(carouselCard))
//                            return true;
//                    }
//                }
//            }
//        }
//        return false;
//    }
//
//    public boolean isSmartcardContainText(WebElement smartcardElement) throws Exception {
//        if (smartcardElement.getText().trim().length() > 0) {
//            return true;
//        } else {
//            SimpleUtils.fail("Schedule Page: Smartcard contains No Text or Spinning Icon on duration '" + getActiveWeekText() + "'.", true);
//            return false;
//        }
//    }
//
//
//    boolean isSmartCardScrolledToRightActive() throws Exception {
//        if (isElementLoaded(smartcardArrowRight, 10) && smartcardArrowRight.getAttribute("class").contains("available")) {
//            click(smartcardArrowRight);
//            return true;
//        }
//        return false;
//    }

//    void smartCardScrolleToLeft() throws Exception {
//        if (isElementLoaded(smartcardArrowLeft, 2)) {
//            while (smartcardArrowLeft.getAttribute("class").contains("available")) {
//                click(smartcardArrowLeft);
//            }
//        }
//
//    }
//
//
//    //added by Nishant
//
//    @Override
//    public HashMap<String, Float> getScheduleLabelHours() throws Exception {
//        HashMap<String, Float> scheduleHours = new HashMap<String, Float>();
//        WebElement budgetedScheduledLabelsDivElement = MyThreadLocal.getDriver().findElement(By.xpath("//div[@class='card-carousel-card card-carousel-card-primary card-carousel-card-table']"));
//        if (isElementLoaded(budgetedScheduledLabelsDivElement)) {
//            String scheduleWagesAndHoursCardText = budgetedScheduledLabelsDivElement.getText();
//            String[] scheduleWagesAndHours = scheduleWagesAndHoursCardText.split("\n");
//            for (String wagesAndHours : scheduleWagesAndHours) {
//                if (wagesAndHours.toLowerCase().contains(scheduleHoursAndWagesData.hours.getValue().toLowerCase())) {
//                    SmartCardPage smartCardPage = new ConsoleSmartCardPage();
//                    scheduleHours = smartCardPage.updateScheduleHoursAndWages(scheduleHours, wagesAndHours.split(" ")[2],
//                            scheduleHoursAndWagesData.scheduledHours.getValue());
//                    scheduleHours = smartCardPage.updateScheduleHoursAndWages(scheduleHours, wagesAndHours.split(" ")[3],
//                            scheduleHoursAndWagesData.otherHours.getValue());
//                }
//
//            }
//        }
//        return scheduleHours;
//    }
//
//
//    public int getgutterSize() {
//        int guttercount = 0;
//        try {
//            guttercount = gutterCount.size();
//        } catch (Exception e) {
//            SimpleUtils.fail("Gutter element not available on the page", false);
//        }
//        return guttercount;
//    }

//    public void verifySelectTeamMembersOption() throws Exception {
//        waitForSeconds(3);
//        if (isElementLoaded(selectRecommendedOption, 20)) {
//            clickTheElement(selectRecommendedOption);
//            waitForSeconds(3);
//            if (areListElementVisible(recommendedScrollTable, 5)) {
//                String[] txtRecommendedOption = selectRecommendedOption.getText().replaceAll("\\p{P}", "").split(" ");
//                if (Integer.parseInt(txtRecommendedOption[2]) == 0) {
//                    if (getDriver().getCurrentUrl().contains(parameterMap.get("KendraScott2_Enterprise"))) {
//                        searchText(propertySearchTeamMember.get("AssignTeamMember"));
//                    } else if (getDriver().getCurrentUrl().contains(parameterMap.get("Coffee_Enterprise"))) {
//                        searchText(propertySearchTeamMember.get("TeamLCMember"));
//                    }
//                    SimpleUtils.pass(txtRecommendedOption[0] + " Option selected By default for Select Team member option");
//                } else {
//                    boolean  scheduleBestMatchStatus = getScheduleBestMatchStatus();
//                    if(scheduleBestMatchStatus){
//                        SimpleUtils.pass(txtRecommendedOption[0] + " Option selected By default for Select Team member option");
//                    }else{
//                        if(areListElementVisible(btnSearchteamMember,5)){
//                            click(btnSearchteamMember.get(0));
//                            if (getDriver().getCurrentUrl().contains(parameterMap.get("KendraScott2_Enterprise"))) {
//                                searchText(propertySearchTeamMember.get("AssignTeamMember"));
//                            } else if (getDriver().getCurrentUrl().contains(parameterMap.get("Coffee_Enterprise"))) {
//                                searchText(propertySearchTeamMember.get("TeamLCMember"));
//                            }
//                        }
//
//                    }
//
//                }
//            } else if (areListElementVisible(btnSearchteamMember,5)) {
//                click(btnSearchteamMember.get(0));
//                if (getDriver().getCurrentUrl().contains(parameterMap.get("KendraScott2_Enterprise"))) {
//                    searchText(propertySearchTeamMember.get("AssignTeamMember"));
//                } else if (getDriver().getCurrentUrl().contains(parameterMap.get("Coffee_Enterprise"))) {
//                    searchText(propertySearchTeamMember.get("TeamLCMember"));
//                }
//            }
//        } else {
//            SimpleUtils.fail("Select Team member option and Recommended options are not available on page", false);
//        }
//    }

//    public String selectTeamMembers() throws Exception {
//        String newSelectedTM = null;
//        waitForSeconds(5);
//        if (areListElementVisible(recommendedScrollTable, 20)) {
//            if (isElementLoaded(selectRecommendedOption, 5)) {
//                String[] txtRecommendedOption = selectRecommendedOption.getText().replaceAll("\\p{P}", "").split(" ");
//                if (Integer.parseInt(txtRecommendedOption[2]) == 0) {
//                    SimpleUtils.report(txtRecommendedOption[0] + " Option no recommended TMs");
//                    click(btnSearchteamMember.get(0));
//                    newSelectedTM = searchAndGetTMName(propertySearchTeamMember.get("AssignTeamMember"));
//                } else {
//                    click(firstTableRow.findElement(By.cssSelector("td.table-field.action-field")));
//                    newSelectedTM = firstnameOfTM.getText();
//                }
//            } else {
//                click(btnSearchteamMember.get(0));
//                newSelectedTM = searchAndGetTMName(propertySearchTeamMember.get("AssignTeamMember"));
//                SimpleUtils.report("Recommended option not available on page");
//            }
//        } else if (isElementLoaded(textSearch, 10)) {
//            newSelectedTM = searchAndGetTMName(propertySearchTeamMember.get("AssignTeamMember"));
//        } else {
//            SimpleUtils.fail("Select Team member option and Recommended options are not available on page", false);
//        }
//        return newSelectedTM;
//    }

//    @FindBy(css = "tr.table-row.ng-scope")
//    List<WebElement> recommendedTMs;
//    public String selectTeamMembers(int numOfTM) throws Exception {
//        String newSelectedTMs = "";
//        waitForSeconds(5);
//        if (areListElementVisible(recommendedScrollTable, 20)) {
//            if (isElementLoaded(selectRecommendedOption, 5)) {
//                String[] txtRecommendedOption = selectRecommendedOption.getText().replaceAll("\\p{P}", "").split(" ");
//                int recommendedNum= Integer.parseInt(txtRecommendedOption[2]);
//                if (recommendedNum == 0) {
//                    SimpleUtils.report(txtRecommendedOption[0] + " Option no recommended TMs");
//                    click(btnSearchteamMember.get(0));
//                    newSelectedTMs = searchAndGetTMName(propertySearchTeamMember.get("AssignTeamMember"));
//                } else if (recommendedNum >= numOfTM){
//                    for (int i = 0; i < numOfTM; i++){
//                        click(recommendedTMs.get(i).findElement(By.cssSelector("td.table-field.action-field")));
//                        newSelectedTMs = newSelectedTMs + recommendedTMs.get(i).findElement(By.cssSelector(".worker-edit-search-worker-display-name")).getText() + " ";
//                    }
//                } else {
//                    for (int i = 0; i < recommendedNum; i++){
//                        click(recommendedTMs.get(i).findElement(By.cssSelector("td.table-field.action-field")));
//                        newSelectedTMs = newSelectedTMs + recommendedTMs.get(i).findElement(By.cssSelector(".worker-edit-search-worker-display-name")).getText() + " ";
//                    }
//                }
//            } else {
//                click(btnSearchteamMember.get(0));
//                newSelectedTMs = searchAndGetTMName(propertySearchTeamMember.get("AssignTeamMember"));
//                SimpleUtils.report("Recommended option not available on page");
//            }
//        } else if (isElementLoaded(textSearch, 5)) {
//            newSelectedTMs = searchAndGetTMName(propertySearchTeamMember.get("AssignTeamMember"));
//        } else {
//            SimpleUtils.fail("Select Team member option and Recommended options are not available on page", false);
//        }
//        return newSelectedTMs;
//    }
//
//
//    @FindBy(css = ".sch-day-view-shift-overtime-icon")
//    private List<WebElement> oTFlag;
//
//    @FindBy(css = ".ot-hours-text")
//    private List<WebElement> oTHoursText;
//
//    @Override
//    public int getOTShiftCount() {
//        int count =0;
//        if (areListElementVisible(oTFlag,5) && areListElementVisible(oTHoursText,5)) {
//            count = oTHoursText.size();
//        }
//        return count;
//    }

//    public void searchText(String searchInput) throws Exception {
//        String[] searchAssignTeamMember = searchInput.split(",");
//        if (isElementLoaded(textSearch, 10) && isElementLoaded(searchIcon, 10)) {
//            for (int i = 0; i < searchAssignTeamMember.length; i++) {
//                String[] searchTM = searchAssignTeamMember[i].split("\\.");
//                textSearch.sendKeys(searchTM[0]);
//                click(searchIcon);
//                if (getScheduleStatus()) {
//                    setTeamMemberName(searchAssignTeamMember[i]);
//                    break;
//                } else {
//                    textSearch.clear();
//                }
//            }
//
//        } else {
//            SimpleUtils.fail("Search text not editable and icon are not clickable", false);
//        }
//
//    }

//    public String searchAndGetTMName(String searchInput) throws Exception {
//        String[] searchAssignTeamMember = searchInput.split(",");
//        String selectedTMName = null;
//        if (isElementLoaded(textSearch, 10) && isElementLoaded(searchIcon, 10)) {
//            for (int i = 0; i < searchAssignTeamMember.length; i++) {
//                String[] searchTM = searchAssignTeamMember[i].split("\\.");
//                textSearch.sendKeys(searchTM[0]);
//                click(searchIcon);
//                waitForSeconds(5);
//                WebElement selectedTM = selectAndGetTheSelectedTM();
//                if (selectedTM != null) {
//                    selectedTMName = selectedTM.findElement(By.className("worker-edit-search-worker-display-name")).getText();
//                    break;
//                } else {
//                    textSearch.clear();
//                }
//            }
//
//            if (selectedTMName == null || (selectedTMName != null && selectedTMName.isEmpty())) {
//                SimpleUtils.fail("Not able to found Available TMs in SearchResult", false);
//            }
//
//        } else {
//            SimpleUtils.fail("Search text not editable and icon are not clickable", false);
//        }
//        return selectedTMName;
//    }

//    public WebElement selectAndGetTheSelectedTM() throws Exception {
//        WebElement selectedTM = null;
////		waitForSeconds(5);
//        if(areListElementVisible(scheduleSearchTeamMemberStatus,5) || isElementLoaded(scheduleNoAvailableMatchStatus,5)){
//            for(int i=0; i<scheduleSearchTeamMemberStatus.size();i++){
//                String statusText = scheduleSearchTeamMemberStatus.get(i).getText();
//                if((statusText.contains("Available") || statusText.contains("Unknown")) && !statusText.contains("Assigned to this shift")){
//                    click(radionBtnSearchTeamMembers.get(i));
//                    if (isElementEnabled(confirmWindow, 5)) {
//                        click(okBtnOnConfirm);
//                    }
//                    selectedTM = searchTMRows.get(i);
//                    break;
//                }
//            }
//            if (selectedTM == null) {
//                SimpleUtils.report("Not able to found Available TMs");
//            }
//        }else{
//            SimpleUtils.fail("Not able to found Available status in SearchResult", true);
//        }
//
//        return selectedTM;
//    }

//    public boolean getScheduleStatus() throws Exception {
//        boolean ScheduleStatus = false;
////		waitForSeconds(5);
//        if(areListElementVisible(scheduleSearchTeamMemberStatus,10) || isElementLoaded(scheduleNoAvailableMatchStatus,10)){
//            for(int i=0; i<scheduleSearchTeamMemberStatus.size();i++){
//                if(scheduleSearchTeamMemberStatus.get(i).getText().contains("Available")
//                        || scheduleSearchTeamMemberStatus.get(i).getText().contains("Unknown")){
//                    if(scheduleSearchTeamMemberStatus.get(i).getText().contains("Minor")){
//                        click(radionBtnSearchTeamMembers.get(i));
//                        ScheduleStatus = true;
//                        break;
//                    } else if(scheduleSearchTeamMemberStatus.get(i).getText().contains("Role Violation")){
//                        click(radionBtnSearchTeamMembers.get(i));
//                        displayAlertPopUpForRoleViolation();
//                        setWorkerRole(searchWorkerRole.get(i).getText());
//                        setWorkerLocation(searchWorkerLocation.get(i).getText());
////                        setWorkerShiftTime(searchWorkerSchShiftTime.getText());
////                        setWorkerShiftDuration(searchWorkerSchShiftDuration.getText());
//                        ScheduleStatus = true;
//                        break;
//                    } else if(scheduleSearchTeamMemberStatus.get(i).getText().contains("Will trigger")) {
//                        clickTheElement(radionBtnSearchTeamMembers.get(i));
//                        if (isElementLoaded(btnAssignAnyway, 10) && btnAssignAnyway.getText().toUpperCase().contains("ASSIGN ANYWAY")) {
//                            clickTheElement(btnAssignAnyway);
//                            waitUntilElementIsInVisible(btnAssignAnyway);
//                        }
//                        ScheduleStatus = true;
//                        break;
//                    } else {
//                        click(radionBtnSearchTeamMembers.get(i));
//                        setWorkerRole(searchWorkerRole.get(i).getText());
//                        setWorkerLocation(searchWorkerLocation.get(i).getText());
////					setWorkerShiftTime(searchWorkerSchShiftTime.getText());
////					setWorkerShiftDuration(searchWorkerSchShiftDuration.getText());
//                        ScheduleStatus = true;
//                        break;
//                    }
//                }
//            }
//        }else{
//            SimpleUtils.fail("Not able to found Available status in SearchResult", false);
//        }
//
//        return ScheduleStatus;
//    }

//    public boolean getScheduleBestMatchStatus()throws Exception {
//        boolean ScheduleBestMatchStatus = false;
//        if(areListElementVisible(scheduleStatus,5) || scheduleBestMatchStatus.size()!=0 && radionBtnSelectTeamMembers.size() == scheduleStatus.size() && searchWorkerName.size()!=0){
//            for(int i=0; i<scheduleStatus.size();i++){
//                if(scheduleBestMatchStatus.get(i).getText().contains("Best")
//                        || scheduleStatus.get(i).getText().contains("Unknown") || scheduleStatus.get(i).getText().contains("Available")){
//                    //if(searchWorkerName.get(i).getText().contains("Gordon.M") || searchWorkerName.get(i).getText().contains("Jayne.H")){
//                    click(radionBtnSelectTeamMembers.get(i));
//                    setTeamMemberName(searchWorkerName.get(i).getText());
//                    ScheduleBestMatchStatus = true;
//                    break;
//                    //}
//                }
//            }
//        }else{
//            SimpleUtils.fail("Not able to found Available status in SearchResult", false);
//        }
//
//        return ScheduleBestMatchStatus;
//    }

//    public void getAvailableStatus()throws Exception {
//        if(areListElementVisible(scheduleStatus) && availableStatus.size()!=0 && radionBtnSelectTeamMembers.size() == scheduleStatus.size()){
//            for(int i=0; i<scheduleStatus.size();i++){
//                if(scheduleStatus.get(i).getText().contains(availableStatus.get(i).getText())){
//                    click(radionBtnSelectTeamMembers.get(i));
//                    break;
//                }
//            }
//        }else{
//            SimpleUtils.fail("Not able to found Available status in SearchResult", false);
//        }
//    }

//    public void clickOnOfferOrAssignBtn() throws Exception{
//        if(isElementLoaded(btnOffer,5)){
//            scrollToElement(btnOffer);
//            waitForSeconds(3);
//            clickTheElement(btnOffer);
//            if (isElementLoaded(btnAssignAnyway, 5) && btnAssignAnyway.getText().toUpperCase().equals("ASSIGN ANYWAY")) {
//                clickTheElement(btnAssignAnyway);
//            }
//        }else{
//            SimpleUtils.fail("Offer Or Assign Button is not clickable", false);
//        }
//    }
//
//    public void clickOnShiftContainer(int index) throws Exception{
//        int guttercount = getgutterSize();
//        waitForSeconds(2);
//        if(shiftContainer.size()!=0 && index<=shiftContainer.size() && isElementLoaded(shiftContainer.get(0))){
//            for(int i=0;i<index;i++){
//                mouseHover(shiftContainer.get(i));
//                waitForSeconds(2);
////				click(shiftContainer.get(i));
//                deleteShift();
//                waitForSeconds(2);
//            }
//
//        }else{
//            SimpleUtils.report("Shift container does not any gutter text");
//        }
//    }
//
//
//    public void deleteShift() throws Exception {
//        if(isElementLoaded(shiftDeleteBtn, 10)){
//            clickTheElement(shiftDeleteBtn);
//        }else{
//            SimpleUtils.fail("Delete button is not available on Shift container",false);
//        }
//    }
//
//    public void deleteAllShiftsInDayView() throws Exception {
//        if (areListElementVisible(dayViewAvailableShifts,10)){
//            int count = dayViewAvailableShifts.size();
//            for (int i = 0; i < count; i++) {
//                List<WebElement> tempShifts = getDriver().findElements(By.cssSelector(".sch-day-view-shift-outer .right-shift-box"));
//                scrollToElement(tempShifts.get(i));
//                moveToElementAndClick(tempShifts.get(i));
//                deleteShift();
//            }
//        }
//    }
//
//    public void deleteShiftGutterText(){
//        if(shiftDeleteGutterText.size()!=0){
//            for(int i=0;i<shiftDeleteGutterText.size();i++){
//                if(shiftDeleteGutterText.get(i).equals("Delete")){
//                    SimpleUtils.pass(shiftDeleteGutterText.get(i)+" Option as gutter is present on shift container");
//                }
//
//            }
//        }else{
//            SimpleUtils.fail("Delete text is not present on Shift container gutter",true);
//        }
//    }
//
//    public void disableNextWeekArrow() throws Exception{
//        if(!calendarNavigationNextWeekArrow.isDisplayed()){
//            SimpleUtils.pass("Next week arrow is disabled");
//        }else{
//            SimpleUtils.fail("Next week arrow is not disabled",false);
//        }
//    }

//    @Override
//    public void clickScheduleDraftAndGuidanceStatus(
//            List<String> overviewScheduleWeeksStatus) {
//        // TODO Auto-generated method stub
//
//    }
//
//    public void editBudgetHours(){
//        if(budgetEditHours.size()>0){
//            for(int i=0; i< budgetEditHours.size();i++){
//                System.out.println(budgetEditHours.get(i).getText());
//            }
//        }
//    }
//
//    @Override
//    public void navigateScheduleDayWeekView(String nextWeekView, int weekCount) {
//
//    }
//
//    public ArrayList<String> getActiveWeekCalendarDates() throws Exception
//    {
//        ArrayList<String> scheduleWeekCalendarDates = new ArrayList<String>();
//        String catendarWeekDatesAsText = "";
//        for(WebElement ScheduleWeekCalendarDate : ScheduleWeekCalendarDates)
//        {
//            scheduleWeekCalendarDates.add(ScheduleWeekCalendarDate.getText().replace("\n", " "));
//            if(catendarWeekDatesAsText == "")
//                catendarWeekDatesAsText = ScheduleWeekCalendarDate.getText().replace("\n", " ");
//            else
//                catendarWeekDatesAsText = catendarWeekDatesAsText+ " | " +ScheduleWeekCalendarDate.getText().replace("\n", " ");
//        }
//        SimpleUtils.report("Active Week Calendar Dates: '" + catendarWeekDatesAsText + "'");
//        return scheduleWeekCalendarDates;
//    }
//
//
//    @Override
//    public void refreshBrowserPage() throws Exception {
//        MyThreadLocal.getDriver().navigate().refresh();
//        Thread.sleep(5000);
//        SimpleUtils.pass("Browser Refreshed Successfully for the Week/Day: '"+ getActiveWeekText() +"'!");
//
//    }

//    @Override
//    public void addOpenShiftWithDefaultTime(String workRole) throws Exception {
//        if (isElementLoaded(createNewShiftWeekView)) {
//            click(createNewShiftWeekView);
//            selectWorkRole(workRole);
//            clickRadioBtnStaffingOption(staffingOption.OpenShift.getValue());
//            clickOnCreateOrNextBtn();
//            Thread.sleep(2000);
//        } else
//            SimpleUtils.fail("Day View Schedule edit mode, add new shift button not found for Week Day: '" +
//                    getActiveWeekText() + "'", false);
//    }
//
//    @Override
//    public void addOpenShiftWithDefaultTime(String workRole, String location) throws Exception {
//        if (isElementLoaded(createNewShiftWeekView)) {
//            click(createNewShiftWeekView);
//            selectWorkRole(workRole);
//            clickRadioBtnStaffingOption(staffingOption.OpenShift.getValue());
//            if (isLocationLoaded())
//                selectLocation(location);
//            moveSliderAtSomePoint(propertyCustomizeMap.get("INCREASE_END_TIME_3"), ScheduleTestKendraScott2.sliderShiftCount.SliderShiftEndTimeCount.getValue(), ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
//            moveSliderAtSomePoint(propertyCustomizeMap.get("INCREASE_START_TIME_3"), ScheduleTestKendraScott2.sliderShiftCount.SliderShiftStartCount.getValue(), ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
//            clickOnCreateOrNextBtn();
//            if (ifWarningModeDisplay() && isElementLoaded(okBtnInWarningMode,5))
//                click(okBtnInWarningMode);
//            Thread.sleep(2000);
//        } else
//            SimpleUtils.fail("Day View Schedule edit mode, add new shift button not found for Week Day: '" +
//                    getActiveWeekText() + "'", false);
//    }



//    @Override
//    public void addOpenShiftWithFirstDay(String workRole) throws Exception {
//        if (isElementLoaded(createNewShiftWeekView, 10)) {
//            click(createNewShiftWeekView);
//            selectWorkRole(workRole);
//            clearAllSelectedDays();
//            if (areListElementVisible(weekDays, 5) && weekDays.size() > 0) {
//                clickTheElement(weekDays.get(0));
//            }
//            clickRadioBtnStaffingOption(staffingOption.OpenShift.getValue());
//            clickOnCreateOrNextBtn();
//        } else
//            SimpleUtils.fail("Day View Schedule edit mode, add new shift button not found for Week Day: '" +
//                    getActiveWeekText() + "'", false);
//    }
//
//    @Override
//    public boolean isNextWeekAvaibale() throws Exception {
//        if (!isElementLoaded(calendarNavigationNextWeekArrow)) {
//            List<WebElement> ScheduleCalendarDayLabels = MyThreadLocal.getDriver().findElements(By.className("day-week-picker-period"));
//            if (ScheduleCalendarDayLabels.get(ScheduleCalendarDayLabels.size() - 1).getAttribute("class").toLowerCase().contains("active"))
//                return false;
//        }
//        return true;
//    }
//
//    @Override
//    public boolean isSmartCardPanelDisplay() throws Exception {
//        if (isElementLoaded(smartCardPanel)) {
//            return true;
//        }
//        return false;
//    }

//    @Override
//    public void convertAllUnAssignedShiftToOpenShift() throws Exception {
//        ScheduleCommonPage scheduleCommonPage = new ConsoleScheduleCommonPage();
//        ScheduleMainPage scheduleMainPage = new ConsoleScheduleMainPage();
//        if (scheduleCommonPage.verifyActivatedSubTab(SchedulePageSubTabText.Schedule.getValue())) {
//            scheduleCommonPage.clickOnWeekView();
//            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
//            for (WebElement unAssignedShift : getUnAssignedShifts()) {
//                convertUnAssignedShiftToOpenShift(unAssignedShift);
//            }
//            saveSchedule();
//        } else {
//            SimpleUtils.fail("Unable to convert UnAssigned Shift to Open Shift as 'Schedule' tab not active.", false);
//        }
//
//    }

//    public void convertUnAssignedShiftToOpenShift(WebElement unAssignedShift) throws Exception {
////        By isUnAssignedShift = By.cssSelector("[ng-if=\"isUnassignedShift()\"]");
//        By isUnAssignedShift = By.cssSelector(".rows .week-view-shift-image-optimized span");
//        WebElement unAssignedPlusBtn = unAssignedShift.findElement(isUnAssignedShift);
//        if (isElementLoaded(unAssignedPlusBtn)) {
//            scrollToElement(unAssignedPlusBtn);
//            clickTheElement(unAssignedPlusBtn);
//            if (isElementLoaded(shiftPopover)) {
//                WebElement convertToOpenOption = shiftPopover.findElement(By.cssSelector("[ng-if=\"canConvertToOpenShift() && !isTmView()\"]"));
//                if (isElementLoaded(convertToOpenOption)) {
//                    scrollToElement(convertToOpenOption);
//                    click(convertToOpenOption);
//                    if (isElementLoaded(convertToOpenYesBtn)) {
//                        click(convertToOpenYesBtn);
//                        Thread.sleep(2000);
//                    }
//                }
//            }
//        }
//    }

//    private List<WebElement> getUnAssignedShifts() {
//        String unAssignedShiftsLabel = "unassigned";
//        List<WebElement> unAssignedShiftsObj = new ArrayList<WebElement>();
//        waitForSeconds(5);
//        if (areListElementVisible(shiftsOnScheduleView, 10) && shiftsOnScheduleView.size() != 0) {
//            for (WebElement shift : shiftsOnScheduleView) {
//                if (shift.getText().toLowerCase().contains(unAssignedShiftsLabel) && shift.isDisplayed())
//                    unAssignedShiftsObj.add(shift);
//            }
//        }
//        return unAssignedShiftsObj;
//    }

//    @Override
//    public void reduceOvertimeHoursOfActiveWeekShifts() throws Exception {
//        List<WebElement> ScheduleCalendarDayLabels = MyThreadLocal.getDriver().findElements(By.className("day-week-picker-period"));
//        for (WebElement activeWeekDay : ScheduleCalendarDayLabels) {
//            click(activeWeekDay);
//            List<WebElement> availableDayShifts = getAvailableShiftsInDayView();
//            if (availableDayShifts.size() != 0) {
//                clickOnEditButton();
//                for (WebElement shiftWithOT : getAvailableShiftsInDayView()) {
//                    WebElement shiftRightSlider = shiftWithOT.findElement(By.cssSelector("div.sch-day-view-shift-pinch.right"));
//                    String OTString = "hrs ot";
//                    int xOffSet = -50;
//                    while (shiftWithOT.getText().toLowerCase().contains(OTString)) {
//                        moveDayViewCards(shiftRightSlider, xOffSet);
//                    }
//                }
//                clickSaveBtn();
//                break;
//            }
//        }
//    }
//
//
//    @Override
//    public boolean isActionButtonLoaded(String actionBtnText) throws Exception {
//        if (actionButtonDiv.size() != 0) {
//            if (actionButtonDiv.get(0).getText().toLowerCase().contains(actionBtnText.toLowerCase()))
//                return true;
//        }
//        return false;
//    }

//    @Override
//    public void navigateToNextDayIfStoreClosedForActiveDay() throws Exception {
//        String dayType = "Next";
//        int dayCount = 1;
//        if (isStoreClosedForActiveWeek())
//            navigateWeekViewOrDayViewToPastOrFuture(dayType, dayCount);
//        if (!isStoreClosedForActiveWeek())
//            SimpleUtils.pass("Navigated to Next day successfully!");
//    }
//
//    @Override
//    public boolean isStoreClosedForActiveWeek() throws Exception {
//        if (isElementLoaded(holidayLogoContainer, 10)) {
//            SimpleUtils.report("Store is Closed for the Day/Week: '" + getActiveWeekText() + "'.");
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public void validateBudgetPopUpHeader(String nextWeekView, int weekCount) {
//        // TODO Auto-generated method stub
//        for (int i = 0; i < weekCount; i++) {
//            if (nextWeekView.toLowerCase().contains("next") || nextWeekView.toLowerCase().contains("future")) {
//                try {
//                    if (isElementLoaded(schedulesForWeekOnOverview.get(0))) {
//                        click(schedulesForWeekOnOverview.get(i));
//                        dayWeekPicker = daypicker.getText();
//                        String[] weekActiveArray = daypicker.getText().split("\n");
//                        String weekActiveDate = weekActiveArray[1];
//                        String budgetPopUpHeader = "Budget - Week of " + SimpleUtils.dateWeekPickerDateComparision(weekActiveDate);
//                        checkElementVisibility(enterBudgetLink);
//                        waitForSeconds(2);
//                        click(enterBudgetLink);
//                        if (budgetPopUpHeader.equalsIgnoreCase(budgetHeader.getText())) {
//                            SimpleUtils.pass("Budget pop-up header week duration " + budgetHeader.getText() + " matches " + weekActiveDate);
//                            checkElementVisibility(enterBudgetCancelButton);
//                            click(enterBudgetCancelButton);
//                            checkElementVisibility(returnToOverviewTab);
//                            click(returnToOverviewTab);
//                        } else {
//                            SimpleUtils.fail("Budget-PopUp opens up for " + SimpleUtils.dateWeekPickerDateComparision(weekActiveDate), false);
//                        }
//                    }
//                } catch (Exception e) {
//                    SimpleUtils.fail("Budget pop-up not opening ", false);
//                }
//            }
//
//
//        }
//    }
//
//    @Override
//    public void validatingGenrateSchedule() throws Exception {
//        // TODO Auto-generated method stub
//    }
//
//    @Override
//    public void noBudgetDisplayWhenBudgetNotEntered(String nextWeekView,
//                                                    int weekCount) {
//        // TODO Auto-generated method stub
//        String valueWhenBudgetNotEntered = "-- Hours";
//
//        for(int i = 0; i < weekCount; i++)
//        {
//            if(nextWeekView.toLowerCase().contains("next") || nextWeekView.toLowerCase().contains("future"))
//            {
//                try {
//                    if(isElementLoaded(schedulesForWeekOnOverview.get(0))){
//                        click(schedulesForWeekOnOverview.get(i));
//                        waitForSeconds(3);
//                        String budgetDisplayOnSmartCardSchedule = budgetOnbudgetSmartCard.getText();
//                        String[] budgetDisplayOnSmartCard = budgetOnbudgetSmartCard.getText().split(" ");
//                        String[] daypickers = daypicker.getText().split("\n");
//                        checkElementVisibility(enterBudgetLink);
//                        click(enterBudgetLink);
//                        waitForSeconds(3);
//                        String totalOfEnteredBudget = sumOfBudgetHour.getText();
//                        if(Float.parseFloat(totalOfEnteredBudget)==0 && budgetDisplayOnSmartCardSchedule.equalsIgnoreCase(valueWhenBudgetNotEntered)){
//                            SimpleUtils.pass("No Budget Entered for week "+daypickers[1]+", Budget SmartCard shows " + budgetDisplayOnSmartCard[0] );
//                        }
//                        else if(Float.parseFloat(totalOfEnteredBudget)>0 && Float.parseFloat(totalOfEnteredBudget)==Float.parseFloat(budgetDisplayOnSmartCard[0])){
//                            SimpleUtils.pass("value on Budget Smart Card "+budgetDisplayOnSmartCard[0]+ " for week "+daypickers[1]+ ", and entered Budget is " + Float.parseFloat(totalOfEnteredBudget) );
//                        }
//                        else{
//                            SimpleUtils.fail("Budget Smartcard shows wrong values "+Float.parseFloat(totalOfEnteredBudget),false);
//                        }
//                        checkElementVisibility(enterBudgetCancelButton);
//                        click(enterBudgetCancelButton);
//                        checkElementVisibility(returnToOverviewTab);
//                        click(returnToOverviewTab);
//                    }
//
//                }
//                catch (Exception e) {
//                    SimpleUtils.fail("Budget pop-up not opening ",false);
//                }
//            }
//
//
//        }
//
//    }
//
//    public Boolean verifyNoBudgetAvailableForWeek(String valueOfBudgetSmartcardWhenNoBudgetEntered, String weekDuration){
//        Boolean budgetAvailable = false;
//        if (valueOfBudgetSmartcardWhenNoBudgetEntered.contains(("-- Hours"))) {
//            SimpleUtils.pass(weekDuration + " week has no budget entered");
//            waitForSeconds(2);
//            checkElementVisibility(returnToOverviewTab);
//            click(returnToOverviewTab);
//            budgetAvailable = true;
//        }
//        return budgetAvailable;
//    }
//
//    public void calculateBudgetValueForScheduleAndBudgetSmartCardWhenBudgetByHour(String weekDuration, String budgetDisplayOnBudgetSmartCardByHours, String budgetOnScheduleSmartcard){
//        float totalBudgetedHourForBudgetSmartCard=0.0f;
//        float totalBudgetHourforScheduleSmartcardIfBudgetEntered=0.0f;
//        for (int j = 1; j < guidanceHour.size(); j++) {
//            totalBudgetedHourForBudgetSmartCard = totalBudgetedHourForBudgetSmartCard + Float.parseFloat(budgetEditHours.get(j - 1).getAttribute("value"));
//            if (((Float.parseFloat(budgetEditHours.get(j - 1).getAttribute("value"))) == 0)) {
//                totalBudgetHourforScheduleSmartcardIfBudgetEntered = totalBudgetHourforScheduleSmartcardIfBudgetEntered + Float.parseFloat(guidanceHour.get(j - 1).getText());
//
//            } else {
//                totalBudgetHourforScheduleSmartcardIfBudgetEntered = totalBudgetHourforScheduleSmartcardIfBudgetEntered + Float.parseFloat(budgetEditHours.get(j - 1).getAttribute("value"));
//            }
//        }
//        if (totalBudgetedHourForBudgetSmartCard == (Float.parseFloat(budgetDisplayOnBudgetSmartCardByHours))) {
//            SimpleUtils.pass("Budget " + (Float.parseFloat(budgetDisplayOnBudgetSmartCardByHours)) +" for week " +weekDuration + " on budget smartcard matches the budget entered " + totalBudgetedHourForBudgetSmartCard);
//        } else {
//            SimpleUtils.fail("Budget " + (Float.parseFloat(budgetDisplayOnBudgetSmartCardByHours))  +" for week " +weekDuration + " on budget smartcard doesn't match the budget entered " + totalBudgetedHourForBudgetSmartCard, true);
//        }
//
//        float finaltotalScheduledHourIfBudgetEntered = (float) (Math.round(totalBudgetHourforScheduleSmartcardIfBudgetEntered * 10) / 10.0);;
//        if (finaltotalScheduledHourIfBudgetEntered == (Float.parseFloat(budgetOnScheduleSmartcard))) {
//            SimpleUtils.pass("Budget " + (Float.parseFloat(budgetOnScheduleSmartcard))  +" for week " +weekDuration + " on schedule smartcard matches the budget calculated " + finaltotalScheduledHourIfBudgetEntered);
//        } else {
//            SimpleUtils.fail("Budget " + (Float.parseFloat(budgetOnScheduleSmartcard))  +" for week " +weekDuration + " on schedule smartcard doesn't match the budget calculated " + finaltotalScheduledHourIfBudgetEntered, true);
//        }
//        checkElementVisibility(enterBudgetCancelButton);
//        click(enterBudgetCancelButton);
//        checkElementVisibility(returnToOverviewTab);
//        click(returnToOverviewTab);
//    }
//
//
//    public void calculateBudgetValueForScheduleAndBudgetSmartCardWhenBudgetByWages(String weekDuration, String budgetDisplayOnSmartCardWhenByWages,String budgetedWagesOnScheduleSmartcard, String budgetOnScheduleSmartcard, int tolerance){
//        float totalBudgetedWagesForBudgetSmartCard=0.0f;
//        float totalScheduledHourIfBudgetEntered=0.0f;
//        float totalScheduledWagesIfBudgetEntered=0.0f;
//        for (int j = 1; j < guidanceHour.size(); j++) {
//            totalBudgetedWagesForBudgetSmartCard = totalBudgetedWagesForBudgetSmartCard + Float.parseFloat(budgetEditHours.get(j - 1).getAttribute("value"));
//            if (((Float.parseFloat(budgetEditHours.get(j - 1).getAttribute("value"))) == 0)) {
//                totalScheduledHourIfBudgetEntered = totalScheduledHourIfBudgetEntered + Float.parseFloat(guidanceHour.get(j - 1).getText());
//                totalScheduledWagesIfBudgetEntered = totalScheduledWagesIfBudgetEntered + Float.parseFloat(guidanceWages.get(j-1).getText());
//            } else {
//                totalScheduledHourIfBudgetEntered = totalScheduledHourIfBudgetEntered + Float.parseFloat(budgetHourWhenBudgetByWagesEnabled.get(j - 1).getText());
//                totalScheduledWagesIfBudgetEntered = totalScheduledWagesIfBudgetEntered + Float.parseFloat(budgetEditHours.get(j - 1).getAttribute("value"));
//            }
//        }
//        if (totalBudgetedWagesForBudgetSmartCard == (Float.parseFloat(budgetDisplayOnSmartCardWhenByWages.replaceAll(",","")))) {
//            SimpleUtils.pass("Budget " + (Float.parseFloat(budgetDisplayOnSmartCardWhenByWages.replaceAll(",",""))) +" for week " +weekDuration + " on budget smartcard matches the budget entered " + totalBudgetedWagesForBudgetSmartCard);
//        } else {
//            SimpleUtils.fail("Budget " + (Float.parseFloat(budgetDisplayOnSmartCardWhenByWages))  +" for week " +weekDuration + " on budget smartcard doesn't match the budget entered " + totalBudgetedWagesForBudgetSmartCard, false);
//        }
//
//        float finaltotalScheduledHourIfBudgetEntered = (float) (Math.round(totalScheduledHourIfBudgetEntered * 10) / 10.0);
//        float differenceBetweenBudInSCnCalcBudgbyHour = (Float.parseFloat(budgetOnScheduleSmartcard)) - finaltotalScheduledHourIfBudgetEntered;
//        if (finaltotalScheduledHourIfBudgetEntered == (Float.parseFloat(budgetOnScheduleSmartcard)) ||
//                (differenceBetweenBudInSCnCalcBudgbyHour <= Integer.parseInt(propertyBudgetValue.get("Tolerance_Value")))) {
//            SimpleUtils.pass("Budget " + (Float.parseFloat(budgetOnScheduleSmartcard))  +" for week " +weekDuration + " on schedule smartcard matches the budget calculated " + finaltotalScheduledHourIfBudgetEntered);
//        } else {
//            SimpleUtils.fail("Budget " + (Float.parseFloat(budgetOnScheduleSmartcard))  +" for week " +weekDuration + " on schedule smartcard doesn't match the budget calculated " + finaltotalScheduledHourIfBudgetEntered, true);
//        }
//        int finaltotalScheduledWagesIfBudgetEntered = (int) (Math.round(totalScheduledWagesIfBudgetEntered * 10) / 10.0);
//        int differenceBetweenBugInSCnCalcBudg = (Integer.parseInt(budgetedWagesOnScheduleSmartcard)) - finaltotalScheduledWagesIfBudgetEntered;
//        if (finaltotalScheduledWagesIfBudgetEntered == (Integer.parseInt(budgetedWagesOnScheduleSmartcard)) || (differenceBetweenBugInSCnCalcBudg <= tolerance)) {
//            SimpleUtils.pass("Budgeted Wages " + (Float.parseFloat(budgetedWagesOnScheduleSmartcard))  +" for week " +weekDuration + " on" +
//                    " schedule smartcard matches the budget wages calculated " + finaltotalScheduledWagesIfBudgetEntered);
//            setBudgetTolerance(1);
//        } else {
//            SimpleUtils.fail("Budget Wages" + (Float.parseFloat(budgetedWagesOnScheduleSmartcard))  +" for week " +weekDuration + " on schedule smartcard doesn't match the budget wages calculated " + finaltotalScheduledWagesIfBudgetEntered, true);
//        }
//        checkElementVisibility(enterBudgetCancelButton);
//        click(enterBudgetCancelButton);
//        checkElementVisibility(returnToOverviewTab);
//        click(returnToOverviewTab);
//    }
//
//
//    @Override
//    public void budgetInScheduleNBudgetSmartCard(String nextWeekView, int weekCount, int tolerance) {
//        // TODO Auto-generated method stub
//        waitForSeconds(3);
//        for(int i = 0; i < weekCount; i++)
//        {
//            float totalBudgetedHourForBudgetSmartCard=0.0f;
//            float totalBudgetHourforScheduleSmartcardIfBudgetEntered=0.0f;
//            float totalBudgetedWagesForBudgetSmartCard=0.0f;
//            float totalScheduledWagesIfBudgetEntered=0.0f;
//            if(nextWeekView.toLowerCase().contains("next") || nextWeekView.toLowerCase().contains("future"))
//            {
//                try {
//                    if(isElementLoaded(schedulesForWeekOnOverview.get(0))) {
//                        waitForSeconds(3);
//                        click(schedulesForWeekOnOverview.get(i));
//                        waitForSeconds(4);
//                        String[] daypickers = daypicker.getText().split("\n");
//                        String valueOfBudgetSmartcardWhenNoBudgetEntered = budgetOnbudgetSmartCardWhenNoBudgetEntered.getText();
//                        String[] budgetDisplayOnBudgetSmartcard = budgetOnbudgetSmartCard.getText().split(" ");
//                        String budgetDisplayOnSmartCardWhenByWages = budgetOnbudgetSmartCard.getText().substring(1);
//                        String budgetDisplayOnBudgetSmartCardByHours = budgetDisplayOnBudgetSmartcard[0];
//                        String budgetOnScheduleSmartcard = budgetDisplayOnScheduleSmartcard.get(0).getText();
//                        String budgetedWagesOnScheduleSmartcard = budgetDisplayOnScheduleSmartcard.get(1).getText().substring(1).replace(",","");
//                        String weekDuration = daypickers[1];
//                        if (verifyNoBudgetAvailableForWeek(valueOfBudgetSmartcardWhenNoBudgetEntered, weekDuration) == false) {
//                            click(enterBudgetLink);
//                            waitForSeconds(3);
//                            if(areListElementVisible(editBudgetHrs,5)){
//                                calculateBudgetValueForScheduleAndBudgetSmartCardWhenBudgetByHour(weekDuration, budgetDisplayOnBudgetSmartCardByHours, budgetOnScheduleSmartcard);
//                            }else if(areListElementVisible(editWagesHrs,5)){
//                                calculateBudgetValueForScheduleAndBudgetSmartCardWhenBudgetByWages(weekDuration, budgetDisplayOnSmartCardWhenByWages, budgetedWagesOnScheduleSmartcard, budgetOnScheduleSmartcard, tolerance);
//                            }
//
//                        }
//                    }
//                }
//                catch (Exception e) {
//                    SimpleUtils.fail("Budget pop-up not opening ",false);
//                }
//            }
//        }
//    }

//    @Override
//    public void budgetHourByWagesInScheduleNBudgetedSmartCard(String nextWeekView,
//                                                       int weekCount) {
//        // TODO Auto-generated method stub
//        waitForSeconds(3);
//        for(int i = 0; i < weekCount; i++)
//        {
//            float totalBudgetedWagesForBudgetSmartCard=0.0f;
//            float totalScheduledHourIfBudgetEntered=0.0f;
//            float totalScheduledWagesIfBudgetEntered=0.0f;
//            if(nextWeekView.toLowerCase().contains("next") || nextWeekView.toLowerCase().contains("future"))
//            {
//                try {
//                    if(isElementLoaded(schedulesForWeekOnOverview.get(0))) {
//                        waitForSeconds(3);
//                        click(schedulesForWeekOnOverview.get(i));
//                        waitForSeconds(4);
//                        String[] daypickers = daypicker.getText().split("\n");
//                        String valueOfBudgetSmartcardWhenNoBudgetEntered = budgetOnbudgetSmartCardWhenNoBudgetEntered.getText();
//                        String budgetDisplayOnSmartCard = budgetOnbudgetSmartCard.getText().substring(1);
//                        String budgetOnScheduleSmartcard = budgetDisplayOnScheduleSmartcard.get(0).getText();
//                        String budgetedWagesOnScheduleSmartcard = budgetDisplayOnScheduleSmartcard.get(1).getText().substring(1).replace(",","");
//                        if (valueOfBudgetSmartcardWhenNoBudgetEntered.contains(("-- Hours"))) {
//                            SimpleUtils.pass(daypickers[1] + " week has no budget entered");
//                            waitForSeconds(2);
//                            checkElementVisibility(returnToOverviewTab);
//                            click(returnToOverviewTab);
//                        } else {
//                            click(enterBudgetLink);
//                            waitForSeconds(3);
//                            for (int j = 1; j < guidanceHour.size(); j++) {
//                                totalBudgetedWagesForBudgetSmartCard = totalBudgetedWagesForBudgetSmartCard + Float.parseFloat(budgetEditHours.get(j - 1).getAttribute("value"));
//                                if (((Float.parseFloat(budgetEditHours.get(j - 1).getAttribute("value"))) == 0)) {
//                                    totalScheduledHourIfBudgetEntered = totalScheduledHourIfBudgetEntered + Float.parseFloat(guidanceHour.get(j - 1).getText());
//                                    totalScheduledWagesIfBudgetEntered = totalScheduledWagesIfBudgetEntered + Float.parseFloat(guidanceWages.get(j-1).getText());
//                                } else {
//                                    totalScheduledHourIfBudgetEntered = totalScheduledHourIfBudgetEntered + Float.parseFloat(budgetHourWhenBudgetByWagesEnabled.get(j - 1).getText());
//                                    totalScheduledWagesIfBudgetEntered = totalScheduledWagesIfBudgetEntered + Float.parseFloat(budgetEditHours.get(j - 1).getAttribute("value"));
//                                }
//                            }
//                            if (totalBudgetedWagesForBudgetSmartCard == (Float.parseFloat(budgetDisplayOnSmartCard))) {
//                                SimpleUtils.pass("Budget " + (Float.parseFloat(budgetDisplayOnSmartCard)) +" for week " +daypickers[1] + " on budget smartcard matches the budget entered " + totalBudgetedWagesForBudgetSmartCard);
//                            } else {
//                                SimpleUtils.fail("Budget " + (Float.parseFloat(budgetDisplayOnSmartCard))  +" for week " +daypickers[1] + " on budget smartcard doesn't match the budget entered " + totalBudgetedWagesForBudgetSmartCard, false);
//                            }
//
//                            float finaltotalScheduledHourIfBudgetEntered = (float) (Math.round(totalScheduledHourIfBudgetEntered * 10) / 10.0);
//                            if (finaltotalScheduledHourIfBudgetEntered == (Float.parseFloat(budgetOnScheduleSmartcard))) {
//                                SimpleUtils.pass("Budget " + (Float.parseFloat(budgetOnScheduleSmartcard))  +" for week " +daypickers[1] + " on schedule smartcard matches the budget calculated " + finaltotalScheduledHourIfBudgetEntered);
//                            } else {
//                                SimpleUtils.fail("Budget " + (Float.parseFloat(budgetOnScheduleSmartcard))  +" for week " +daypickers[1] + " on schedule smartcard doesn't match the budget calculated " + finaltotalScheduledHourIfBudgetEntered, true);
//                            }
//                            int finaltotalScheduledWagesIfBudgetEntered = (int) (Math.round(totalScheduledWagesIfBudgetEntered * 10) / 10.0);
//                            if (finaltotalScheduledWagesIfBudgetEntered == (Integer.parseInt(budgetedWagesOnScheduleSmartcard))) {
//                                SimpleUtils.pass("Budgeted Wages " + (Float.parseFloat(budgetedWagesOnScheduleSmartcard))  +" for week " +daypickers[1] + " on schedule smartcard matches the budget wages calculated " + finaltotalScheduledWagesIfBudgetEntered);
//                            } else {
//                                SimpleUtils.fail("Budget Wages" + (Float.parseFloat(budgetedWagesOnScheduleSmartcard))  +" for week " +daypickers[1] + " on schedule smartcard doesn't match the budget wages calculated " + finaltotalScheduledWagesIfBudgetEntered, true);
//                            }
//                            checkElementVisibility(enterBudgetCancelButton);
//                            click(enterBudgetCancelButton);
//                            checkElementVisibility(returnToOverviewTab);
//                            click(returnToOverviewTab);
//                        }
//                    }
//                }
//                catch (Exception e) {
//                    SimpleUtils.fail("Budget pop-up not opening ",false);
//                }
//            }
//        }
//    }


//    @Override
//    public String getsmartCardTextByLabel(String cardLabel) {
//        if (carouselCards.size() != 0) {
//            for (WebElement carouselCard : carouselCards) {
//                if (carouselCard.isDisplayed() && carouselCard.getText().toLowerCase().contains(cardLabel.toLowerCase()))
//                    return carouselCard.getText();
//            }
//        }
//        return null;
//    }


//    @Override
//    public String getWeatherTemperature() throws Exception {
//        String temperatureText = "";
//        if (weatherTemperatures.size() != 0)
//            for (WebElement weatherTemperature : weatherTemperatures) {
//                if (weatherTemperature.isDisplayed()) {
//                    if (temperatureText == "")
//                        temperatureText = weatherTemperature.getText();
//                    else
//                        temperatureText = temperatureText + " | " + weatherTemperature.getText();
//                } else if (!weatherTemperature.isDisplayed()) {
//                    while (isSmartCardScrolledToRightActive() == true) {
//                        if (temperatureText == "")
//                            temperatureText = weatherTemperature.getText();
//                        else
//                            temperatureText = temperatureText + " | " + weatherTemperature.getText();
//                    }
//                }
//            }
//
//        return temperatureText;
//    }


//    public String getWeatherDayOfWeek() throws Exception {
//        String daysText = "";
//        if (weatherDaysOfWeek.size() != 0)
//            for (WebElement weatherDay : weatherDaysOfWeek) {
//                if (weatherDay.isDisplayed()) {
//                    if (daysText == "")
//                        daysText = weatherDay.getText();
//                    else
//                        daysText = daysText + " | " + weatherDay.getText();
//                } else if (!weatherDay.isDisplayed()) {
//                    while (isSmartCardScrolledToRightActive() == true) {
//                        if (daysText == "")
//                            daysText = weatherDay.getText();
//                        else
//                            daysText = daysText + " | " + weatherDay.getText();
//                    }
//                }
//            }
//
//        return daysText;
//    }

//	@Override
//	public void generateOrUpdateAndGenerateSchedule() throws Exception {
//		if (isElementLoaded(generateSheduleButton)) {
//			click(generateSheduleButton);
//			waitForSeconds(4);
//			if(isElementLoaded(updateAndGenerateScheduleButton)){
//				click(updateAndGenerateScheduleButton);
//				SimpleUtils.pass("Schedule Update and Generate button clicked Successfully!");
//				checkOutGenerateScheduleBtn(checkOutTheScheduleButton);
//			}else if(isElementLoaded(checkOutTheScheduleButton)) {
//				checkOutGenerateScheduleBtn(checkOutTheScheduleButton);
//			}else{
//				SimpleUtils.fail("Not able to generate Schedule Successfully!",false);
//			}
//
//		} else {
//			SimpleUtils.assertOnFail("Schedule Already generated for active week!", false, true);
//		}
//	}

//    public void checkoutSchedule() {
//        clickTheElement(checkOutTheScheduleButton);
//        SimpleUtils.pass("Schedule Generated Successfuly!");
//    }
//
//    public void updateAndGenerateSchedule() {
//        if (isElementEnabled(updateAndGenerateScheduleButton)) {
//            click(updateAndGenerateScheduleButton);
//            SimpleUtils.pass("Schedule Update and Generate button clicked Successfully!");
//            if (isElementEnabled(checkOutTheScheduleButton)) {
//                checkoutSchedule();
//            }
//        } else {
//            SimpleUtils.fail("Not able to generate Schedule Successfully!", false);
//        }
//    }
//
//    @Override
//    public void generateOrUpdateAndGenerateSchedule() throws Exception {
//        if (isElementEnabled(generateSheduleButton,5)) {
//            clickTheElement(generateSheduleButton);
//            openBudgetPopUp();
////            openBudgetPopUpGenerateSchedule();
//            if (isElementLoaded(generateSheduleForEnterBudgetBtn, 5)) {
//                click(generateSheduleForEnterBudgetBtn);
//                if (isElementEnabled(checkOutTheScheduleButton, 20)) {
//                    checkoutSchedule();
//                    switchToManagerView();
//                } else if (isElementLoaded(updateAndGenerateScheduleButton, 5)) {
//                    updateAndGenerateSchedule();
//                    switchToManagerView();
//                } else {
//                    SimpleUtils.fail("Not able to generate Schedule Successfully!", false);
//                }
//            } else if (isElementLoaded(updateAndGenerateScheduleButton, 5)) {
//                updateAndGenerateSchedule();
//                switchToManagerView();
//            } else if (isElementEnabled(checkOutTheScheduleButton,20)) {
//                checkOutGenerateScheduleBtn(checkOutTheScheduleButton);
//                SimpleUtils.pass("Schedule Generated Successfully!");
//                switchToManagerView();
//            } else {
//                SimpleUtils.fail("Not able to generate Schedule Successfully!", false);
//            }
//
//        } else if(isElementEnabled(generateScheduleBtn,5)){
//            switchToManagerView();
//        }
//        else {
//            SimpleUtils.assertOnFail("Schedule Already generated for active week!", false, true);
//        }
//    }

//    // Added by Nora: for day view overtime
//    @Override
//    public void dragOneShiftToMakeItOverTime() throws Exception {
//        if (areListElementVisible(scheduleShiftsRows, 10) && scheduleShiftsRows.size() > 0) {
//            SimpleUtils.report("Schedule Day View: shifts loaded Successfully!");
//            int index = 0;
//            WebElement leftPinch = scheduleShiftsRows.get(index).findElement(By.cssSelector(".sch-day-view-shift-pinch.left"));
//            WebElement rightPinch = scheduleShiftsRows.get(index).findElement(By.cssSelector(".sch-day-view-shift-pinch.right"));
//            List<WebElement> gridCells = scheduleShiftsRows.get(index).findElements(By.cssSelector(".sch-day-view-grid-cell"));
//            if (leftPinch != null && rightPinch != null && gridCells != null && gridCells.size() > 0) {
//                WebElement firstCell = gridCells.get(0);
//                WebElement lastCell = gridCells.get(gridCells.size() - 1);
//                mouseHoverDragandDrop(leftPinch, firstCell);
//                waitForSeconds(2);
//                mouseHoverDragandDrop(rightPinch, lastCell);
//                waitForSeconds(2);
//                WebElement flag = scheduleShiftsRows.get(index).findElement(By.cssSelector(".sch-day-view-shift-overtime-icon"));
//                if (flag != null) {
//                    SimpleUtils.pass("Schedule Day View: day overtime icon shows correctly!");
//                } else {
//                    SimpleUtils.fail("Schedule Day View: day overtime icon failed to show!", false);
//                }
//            } else {
//                SimpleUtils.fail("Schedule Day View: Failed to find the left pinch, right pinch and grid cells elements!", false);
//            }
//        } else {
//            SimpleUtils.report("Schedule Day View: There is no shift for this day!");
//        }
//    }

    // Added by Nora: For non dg flow create schedule
    @FindBy (className = "generate-modal-subheader-title")
    private WebElement generateModalTitle;
    @FindBy (css = "[ng-click=\"next()\"]")
    private WebElement nextButtonOnCreateSchedule;
    @FindBy (className = "generate-modal-week-container")
    private List<WebElement> availableCopyWeeks;
    @FindBy (css = "generate-modal-operating-hours-step [label=\"Edit\"]")
    private WebElement operatingHoursEditBtn;
    @FindBy (css = ".operating-hours-day-list-item.ng-scope")
    private List<WebElement> operatingHoursDayLists;
    @FindBy (css = "generate-modal-budget-step [label=\"Edit\"]")
    private WebElement editBudgetBtn;
    @FindBy (css = "generate-modal-budget-step [ng-repeat=\"r in summary.staffingGuidance.roleHours\"]")
    private List<WebElement> roleHoursRows;
    @FindBy (className = "sch-calendar-day-dimension")
    private List<WebElement> weekDayDimensions;
    @FindBy (css = "tbody tr")
    private List<WebElement> smartCardRows;
    @FindBy (css = ".generate-modal-week")
    private List<WebElement> createModalWeeks;
    @FindBy (css = ".holiday-text")
    private WebElement storeClosedText;
    @FindBy (css = "[ng-repeat*=\"summary.workingHours\"]")
    private List<WebElement> summaryWorkingHoursRows;
    @FindBy (css = "span.loading-icon.ng-scope")
    private WebElement loadingIcon;
    @FindBy (css = ".operating-hours-day-list-item.ng-scope")
    private List<WebElement> currentOperatingHours;


//    @Override
//    public void verifyClosedDaysInToggleSummaryView(List<String> weekDaysToClose) throws Exception {
//        if (areListElementVisible(summaryWorkingHoursRows, 15) && summaryWorkingHoursRows.size() == 7) {
//            for (WebElement row : summaryWorkingHoursRows) {
//                List<WebElement> tds = row.findElements(By.tagName("td"));
//                if (tds != null && tds.size() == 2) {
//                    if (weekDaysToClose.contains(tds.get(0).getText())) {
//                        if (tds.get(1).getText().equals("Closed")) {
//                            SimpleUtils.pass("Verfied " + tds.get(0).getText() + " is \"Closed\"");
//                        } else {
//                            SimpleUtils.fail("Verified " + tds.get(0).getText() + " is not \"Closed\"", false);
//                        }
//                    }
//                } else {
//                    SimpleUtils.fail("Summary Operating Hours: Failed to find two td elements!", false);
//                }
//            }
//        } else {
//            SimpleUtils.fail("Summary Operating Hours rows not loaded Successfully!", false);
//        }
//    }
//
//    @Override
//    public boolean isCopyScheduleWindow() throws Exception {
//        if (areListElementVisible(createModalWeeks,10)){
//            return true;
//        }
//        return false;
//    }

//    @Override
//    public void verifyStoreIsClosedForSpecificWeekDay(List<String> weekDaysToClose) throws Exception {
//        if (weekDaysToClose != null && weekDaysToClose.size() > 0) {
//            for (String weekDayToClose : weekDaysToClose) {
//                if (areListElementVisible(dayPickerAllDaysInDayView, 5)) {
//                    for (WebElement dayPicker : dayPickerAllDaysInDayView) {
//                        if (dayPicker.getText().toLowerCase().contains(weekDayToClose.substring(0, 3).toLowerCase())) {
//                            clickTheElement(dayPicker);
//                            if (isElementLoaded(storeClosed, 10) && isElementLoaded(storeClosedText, 10) &&
//                                    storeClosedText.getText().equals("Store is closed.")) {
//                                SimpleUtils.pass("Verified 'Store is closed.' for week day:" + weekDayToClose);
//                            } else {
//                                SimpleUtils.fail("Verified 'Store is not closed.' for week day:" + weekDayToClose, false);
//                            }
//                            break;
//                        }
//                    }
//                } else {
//                    SimpleUtils.fail("Schedule Day View: Day pickers not loaded Successfully!", false);
//                }
//            }
//        }else {
//            SimpleUtils.report("There are no week days that need to close!");
//        }
//    }

//    @Override
//    public void verifyNoShiftsForSpecificWeekDay(List<String> weekDaysToClose) throws Exception {
//        if (areListElementVisible(weekDayDimensions, 10) && weekDayDimensions.size() == 7) {
//            for (WebElement weekDayDimension : weekDayDimensions) {
//                WebElement weekDay = weekDayDimension.findElement(By.className("sch-calendar-day-label"));
//                // Judge if the week day is in the list, if it is, this day is closed, there should be no shifts
//                if (weekDay != null && weekDaysToClose.contains(SimpleUtils.getFullWeekDayName(weekDay.getText()))) {
//                    List<WebElement> weekShiftWrappers = weekDayDimension.findElements(By.className("week-schedule-shift-wrapper"));
//                    if (weekShiftWrappers != null) {
//                        if (weekShiftWrappers.size() == 0) {
//                            SimpleUtils.pass("Verified for Week Day: " + weekDay.getText() + ", there are no shifts Loaded!");
//                        } else {
//                            SimpleUtils.fail("Verified for Week Day: " + weekDay.getText() + " failed, this day is closed, but there still have shifts!", false);
//                        }
//                    } else {
//                        SimpleUtils.pass("Verified for Week Day: " + weekDay.getText() + ", there are no shifts Loaded!");
//                    }
//                }
//            }
//        } else {
//            SimpleUtils.fail("Schedule Week View Page: Each week day dimension not loaded Successfully!", false);
//        }
//    }

    @FindBy (css = "[value=\"config.partialSchedule\"]")
    private WebElement copyPartialScheduleSwitch;

//    @FindBy (css = "[ng-repeat=\"assignment in assignments\"]")
//    private List<WebElement> copyShiftAssignments;
//
//    public void selectSpecificCopyShiftAssignments (List<String> specificShiftAssignments) {
//        if (areListElementVisible(copyShiftAssignments, 10) && copyShiftAssignments.size()>0) {
//            for (WebElement copyShiftAssignment: copyShiftAssignments){
//                if (specificShiftAssignments.contains(copyShiftAssignment.getText())){
//                    click(copyShiftAssignment);
//                    SimpleUtils.pass("Selected shift assignment: " + copyShiftAssignment.getText() +" successfully! ");
//                }
//            }
//        } else
//            SimpleUtils.fail("Copy shift assignments loaded fail! ", false);
//    }


//    @Override
//    public float createScheduleForNonDGByWeekInfo(String weekInfo, List<String> weekDaysToClose, List<String> copyShiftAssignments) throws Exception {
//        float budgetHours = 0;
//        String subTitle = "Confirm Operating Hours";
//        waitForSeconds(2);
//        if (isElementLoaded(generateSheduleButton,60)) {
//            clickTheElement(generateSheduleButton);
//            if (isElementLoaded(generateModalTitle, 20) && subTitle.equalsIgnoreCase(generateModalTitle.getText().trim())
//                    && isElementLoaded(nextButtonOnCreateSchedule, 20)) {
//                editTheOperatingHours(weekDaysToClose);
//                waitForSeconds(1);
//                clickTheElement(nextButtonOnCreateSchedule);
//                budgetHours = checkEnterBudgetWindowLoadedForNonDG();
//                selectWhichWeekToCopyFrom(weekInfo);
//                if(copyShiftAssignments !=null && copyShiftAssignments.size()>0){
//                    if (isElementLoaded(copyPartialScheduleSwitch, 10)){
//                        click(copyPartialScheduleSwitch);
//                    } else
//                        SimpleUtils.fail("Copy Partial Schedule Switch loaded fail! ", false);
//                    clickTheElement(nextButtonOnCreateSchedule);
//                    selectSpecificCopyShiftAssignments(copyShiftAssignments);
//                    clickTheElement(nextButtonOnCreateSchedule);
//                } else
//                    clickOnFinishButtonOnCreateSchedulePage();
//                if (isElementEnabled(checkOutTheScheduleButton, 20)) {
//                    checkoutSchedule();
//                }
//
//            } else {
//                SimpleUtils.fail("Not able to generate schedule Successfully!", false);
//            }
//        }else {
//            SimpleUtils.fail("Create Schedule button not loaded Successfully!", false);
//        }
//        return budgetHours;
//    }



//    @Override
//    public void selectWhichWeekToCopyFrom(String weekInfo) throws Exception {
//        boolean selectOtherWeek = false;
//        try{
//            if (areListElementVisible(createModalWeeks, 10)) {
//                SimpleUtils.pass("Copy Schedule page loaded Successfully!");
//                waitForSeconds(5);
//                for (WebElement createModalWeek : createModalWeeks) {
//                    WebElement weekName = createModalWeek.findElement(By.className("generate-modal-week-name"));
//                    if (!selectOtherWeek) {
//                        if (weekName != null && weekName.getText().toLowerCase().contains(weekInfo.toLowerCase())) {
//                            WebElement weekContainer = createModalWeek.findElement(By.className("generate-modal-week-container"));
//                            if (weekContainer != null) {
//                                WebElement scheduledHours = weekContainer.findElement(By.cssSelector("svg > g > g:nth-child(2) > text"));
//                                if (scheduledHours != null && !scheduledHours.getText().equals("0")) {
//                                    int i = 0;
//                                    while (isElementLoaded(loadingIcon)&& i<20){
//                                        waitForSeconds(3);
//                                        i = i+1;
//                                    }
//                                    clickTheElement(weekContainer);
//                                    waitForSeconds(3);
////                                SimpleUtils.pass("Create Schedule: Select the " + weekName.getText() + " with scheduled hour: " + scheduledHours.getText() + " Successfully!");
//                                    break;
//                                } else {
//                                    selectOtherWeek = true;
//                                    SimpleUtils.warn("Scheduled Hour is 0, due to bug PLT-1082: [RC]Creating schedule keeps spinning and shows 0 scheduled hours! Will select another week as a workaround");
//                                }
//                            }
//                        }
//                    } else {
//                        WebElement weekContainer = createModalWeek.findElement(By.className("generate-modal-week-container"));
//                        if (weekContainer != null) {
//                            WebElement scheduledHours = weekContainer.findElement(By.cssSelector("svg > g > g:nth-child(2) > text"));
//                            if (scheduledHours != null && !scheduledHours.getText().equals("0")) {
//                                clickTheElement(weekContainer);
//                                SimpleUtils.pass("Create Schedule: Select the " + weekName.getText() + "with scheduled hour: " + scheduledHours.getText() + " Successfully!");
//                                break;
//                            }
//                        }
//                    }
//                }
//            }
//        } catch (StaleElementReferenceException e){
//            SimpleUtils.report(e.getMessage());
//        }
//    }

//    @Override
//    public float checkEnterBudgetWindowLoadedForNonDG() throws Exception {
//        float budgetHour = 0;
//        String title = "Enter Budget";
//        waitForSeconds(2);
//        try {
//            if (isElementLoaded(generateModalTitle, 10) && title.equalsIgnoreCase(generateModalTitle.getText().trim())
//                    && isElementLoaded(nextButtonOnCreateSchedule, 10)) {
//                editTheBudgetForNondgFlow();
//                waitForSeconds(5);
//                try {
//                    List<WebElement> trs = enterBudgetTable.findElements(By.tagName("tr"));
//                    if (areListElementVisible(trs, 5) && trs.size() > 0) {
//                        WebElement budget = trs.get(trs.size() - 1).findElement(By.cssSelector("th:nth-child(4)"));
//                        budgetHour = Float.parseFloat(budget == null ? "" : budget.getText());
//                        SimpleUtils.report("Enter Budget Window: Get the budget hour: " + budgetHour);
//                    }
//                } catch (Exception e) {
//                    // Nothing
//                }
//                waitForSeconds(5);
//                clickTheElement(nextButtonOnCreateSchedule);
//            }
//            if (isElementEnabled(checkOutTheScheduleButton, 20)) {
//                checkoutSchedule();
//            }
//        } catch (Exception e) {
//            // do nothing
//        }
//        return budgetHour;
//    }

//    @Override
//    public void clickOnFinishButtonOnCreateSchedulePage() throws Exception {
//        if (isElementLoaded(nextButtonOnCreateSchedule, 5)) {
//            clickTheElement(nextButtonOnCreateSchedule);
//            WebElement element = (new WebDriverWait(getDriver(), 120))
//                    .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[ng-click=\"goToSchedule()\"]")));
//            waitForSeconds(3);
//            if (isElementLoaded(element, 5)) {
//                checkoutSchedule();
//                SimpleUtils.pass("Schedule Page: Schedule is generated within 2 minutes successfully");
//            } else {
//                SimpleUtils.fail("Schedule Page: Schedule isn't generated within 2 minutes", false);
//            }
//            if (areListElementVisible(shiftsWeekView, 60) && shiftsWeekView.size() > 0) {
//                SimpleUtils.pass("Create the schedule successfully!");
//            }else {
//                SimpleUtils.fail("Not able to generate the schedule successfully!", false);
//            }
//        }
//    }

//    @Override
//    public int getComplianceShiftCountFromSmartCard(String cardName) throws Exception {
//        int count = 0;
//        if (areListElementVisible(smartCards, 5)) {
//            for (WebElement smartCard : smartCards) {
//                WebElement title = smartCard.findElement(By.className("card-carousel-card-title"));
//                if (title != null && title.getText().trim().equalsIgnoreCase(cardName)) {
//                    WebElement header = smartCard.findElement(By.tagName("h1"));
//                    if (header != null && !header.getText().isEmpty()) {
//                        count = Integer.parseInt(header.getText().trim().substring(0, 1));
//                        SimpleUtils.report("Compliance Card: Get: " + count + " compliance shift(s).");
//                        break;
//                    }
//                }
//            }
//        }
//        if (count == 0) {
//            SimpleUtils.fail("Compliance Card: Failed to get the count of the shift(s)!", false);
//        }
//        return count;
//    }

//    @Override
//    public HashMap<String, String> getBudgetNScheduledHoursFromSmartCard() throws Exception {
//        HashMap<String, String> budgetNScheduledHours = new HashMap<>();
//        if (areListElementVisible(smartCardRows, 5) && smartCardRows.size() != 0) {
//            List<WebElement> ths = smartCardRows.get(0).findElements(By.tagName("th"));
//            List<WebElement> tds = smartCardRows.get(1).findElements(By.tagName("td"));
//            if (ths != null && tds != null && ths.size() == 4 && tds.size() == 4) {
//                budgetNScheduledHours.put(ths.get(1).getText(), tds.get(1).getText());
//                budgetNScheduledHours.put(ths.get(2).getText(), tds.get(2).getText());
//                SimpleUtils.report("Smart Card: Get the hour: " + tds.get(1).getText() + " for: " + ths.get(1).getText());
//                SimpleUtils.report("Smart Card: Get the hour: " + tds.get(2).getText() + " for: " + ths.get(2).getText());
//            } else {
//                SimpleUtils.fail("Schedule Week View Page: The format of the budget and Scheduled hours' smart card is incorrect!", false);
//            }
//        } else {
//            SimpleUtils.fail("Schedule Week View Page: Budget and Scheduled smart card not loaded Successfully!", false);
//        }
//        return budgetNScheduledHours;
//    }
//
//    @Override
//    public HashMap<String, String> getBudgetNScheduledHoursFromSmartCardOnDGEnv() throws Exception {
//        HashMap<String, String> budgetNScheduledHours = new HashMap<>();
//        if (areListElementVisible(smartCardRows, 5) && smartCardRows.size() != 0) {
//            List<WebElement> ths = smartCardRows.get(0).findElements(By.tagName("th"));
//            List<WebElement> tds = smartCardRows.get(1).findElements(By.tagName("td"));
//            List<WebElement> td2s = smartCardRows.get(2).findElements(By.tagName("td"));
//            if (ths != null && tds != null && ths.size() == 6 && tds.size() == 6) {
//                for (int i =1 ;i< ths.size(); i++){
//                    budgetNScheduledHours.put(tds.get(0).getText() + ths.get(i).getText(), tds.get(i).getText());
//                    SimpleUtils.report("Smart Card: Get the hour: " + tds.get(0) + ths.get(i).getText() + " for: " + tds.get(i).getText());
//                    budgetNScheduledHours.put(td2s.get(0).getText() + ths.get(i).getText(), td2s.get(i).getText());
//                    SimpleUtils.report("Smart Card: Get the hour: " + td2s.get(0) + ths.get(i).getText() + " for: " + td2s.get(i).getText());
//                }
//            } else {
//                SimpleUtils.fail("Schedule Week View Page: The format of the budget and Scheduled hours' smart card is incorrect!", false);
//            }
//        } else {
//            SimpleUtils.fail("Schedule Week View Page: Budget and Scheduled smart card not loaded Successfully!", false);
//        }
//        return budgetNScheduledHours;
//    }

//    @Override
//    public HashMap<String, List<String>> getTheContentOfShiftsForEachWeekDay() throws Exception {
//        HashMap<String, List<String>> shiftsForEachDay = new HashMap<>();
//        if (areListElementVisible(weekDayDimensions, 10) && weekDayDimensions.size() == 7) {
//            for (WebElement weekDayDimension : weekDayDimensions) {
//                WebElement weekDay = weekDayDimension.findElement(By.className("sch-calendar-day-label"));
//                List<WebElement> weekShiftWrappers = weekDayDimension.findElements(By.className("week-schedule-shift-wrapper"));
//                List<String> infos = new ArrayList<>();
//                if (weekShiftWrappers != null && weekShiftWrappers.size() > 0) {
//                    for (WebElement weekShiftWrapper : weekShiftWrappers) {
//                        WebElement shiftTime = weekShiftWrapper.findElement(By.className("week-schedule-shift-time"));
//                        WebElement workerName = weekShiftWrapper.findElement(By.className("week-schedule-worker-name"));
//                        WebElement jobTitle = weekShiftWrapper.findElement(By.className("week-schedule-role-name"));
//                        if (weekDay != null && shiftTime != null && workerName != null && jobTitle != null) {
//                            infos.add(shiftTime.getText() + "\n" + workerName.getText() + "\n" + jobTitle.getText());
//                        } else {
//                            SimpleUtils.fail("Schedule Week View Page: Failed to find the week day, shift time, worker name and job title elements!", false);
//                        }
//                    }
//                }
//                shiftsForEachDay.put(weekDay.getText(), infos);
//                SimpleUtils.report("Schedule Week View Page: Get the shifts for week day: " + weekDay.getText() + ", the shifts are: " + infos.toString());
//            }
//        } else {
//            SimpleUtils.fail("Schedule Week View Page: Each week day dimension not loaded Successfully!", false);
//        }
//        return shiftsForEachDay;
//    }

//    @Override
//    public HashMap<String, String> getTheHoursNTheCountOfTMsForEachWeekDays() throws Exception {
//        HashMap<String, String> hoursNTeamMembersCount = new HashMap<>();
//        if (areListElementVisible(weekDayDimensions, 10) && weekDayDimensions.size() == 7) {
//            for (WebElement weekDayDimension : weekDayDimensions) {
//                WebElement weekDay = weekDayDimension.findElement(By.className("sch-calendar-day-label"));
//                WebElement hoursNCount = weekDayDimension.findElement(By.className("sch-calendar-day-summary"));
//                if (weekDay != null && hoursNCount != null) {
//                    hoursNTeamMembersCount.put(weekDay.getText(), hoursNCount.getText());
//                    SimpleUtils.report("Schedule Week View Page: Get the week day: " + weekDay.getText() + " and the count of hours" +
//                            ", TMs are: " + hoursNCount.getText());
//                } else {
//                    SimpleUtils.fail("Schedule Week View Page: week day, hours and TMs are not loaded Successfully!", false);
//                }
//            }
//        } else {
//            SimpleUtils.fail("Schedule Week View Page: Each week day dimension not loaded Successfully!", false);
//        }
//        return hoursNTeamMembersCount;
//    }

//    @Override
//    public void createScheduleForNonDGFlowNewUI() throws Exception {
//        String subTitle = "Confirm Operating Hours";
//        waitForSeconds(3);
//        if (isElementLoaded(generateSheduleButton,120)) {
//            clickTheElement(generateSheduleButton);
//            openBudgetPopUp();
//            if (isElementLoaded(generateModalTitle, 15) && subTitle.equalsIgnoreCase(generateModalTitle.getText().trim())
//                    && isElementLoaded(nextButtonOnCreateSchedule, 15)) {
//                editTheOperatingHours(new ArrayList<>());
//                waitForSeconds(3);
//                clickTheElement(nextButtonOnCreateSchedule);
//                checkEnterBudgetWindowLoadedForNonDG();
//                selectWhichWeekToCopyFrom("SUGGESTED");
//                clickOnFinishButtonOnCreateSchedulePage();
//                switchToManagerViewToCheckForSecondGenerate();
//            }else if (isElementLoaded(generateSheduleForEnterBudgetBtn, 5)) {
//                click(generateSheduleForEnterBudgetBtn);
//                if (isElementEnabled(checkOutTheScheduleButton, 20)) {
//                    checkoutSchedule();
//                    switchToManagerViewToCheckForSecondGenerate();
//                } else if (isElementLoaded(updateAndGenerateScheduleButton, 5)) {
//                    updateAndGenerateSchedule();
//                    switchToManagerViewToCheckForSecondGenerate();
//                } else {
//                    SimpleUtils.fail("Not able to generate Schedule Successfully!", false);
//                }
//            } else if (isElementLoaded(updateAndGenerateScheduleButton, 5)) {
//                updateAndGenerateSchedule();
//                switchToManagerViewToCheckForSecondGenerate();
//            } else if (isElementEnabled(checkOutTheScheduleButton,20)) {
//                checkOutGenerateScheduleBtn(checkOutTheScheduleButton);
//                SimpleUtils.pass("Schedule Generated Successfully!");
//                switchToManagerViewToCheckForSecondGenerate();
//            } else {
//                SimpleUtils.fail("Not able to generate schedule Successfully!", false);
//            }
//        }else {
//            SimpleUtils.fail("Create Schedule button not loaded Successfully!", false);
//        }
//    }

//    @Override
//    public void clickNextBtnOnCreateScheduleWindow() throws Exception {
//        if (isElementLoaded(nextButtonOnCreateSchedule, 15)){
//            clickTheElement(nextButtonOnCreateSchedule);
//            checkEnterBudgetWindowLoadedForNonDG();
//        } else {
//            SimpleUtils.fail("There is not next button!", false);
//        }
//    }

//    public void editTheBudgetForNondgFlow() throws Exception {
//        if (isElementLoaded(editBudgetBtn, 20)) {
//            clickTheElement(editBudgetBtn);
//            // Cancel and Save buttons are consistent with operating hours
//            if (isElementLoaded(operatingHoursCancelBtn, 10) && isElementLoaded(operatingHoursSaveBtn, 10)) {
//                SimpleUtils.pass("Create Schedule - Enter Budget: Click on Edit button Successfully!");
//                if (areListElementVisible(roleHoursRows, 5)) {
//                    for (WebElement roleHoursRow : roleHoursRows) {
//                        try {
//                            WebElement forecastHour = roleHoursRow.findElement(By.cssSelector("td:nth-child(3)"));
//                            WebElement budgetHour = roleHoursRow.findElement(By.cssSelector("input[type=\"number\"]"));
//                            if (forecastHour != null && budgetHour != null) {
//                                String forecastHourString = "";
//                                if (forecastHour.getText().trim().contains(".")) {
//                                    forecastHourString = forecastHour.getText().trim().substring(0, forecastHour.getText().trim().indexOf("."));
//                                }
//                                budgetHour.clear();
//                                budgetHour.sendKeys(forecastHourString);
//                            }
//                        }catch (Exception e) {
//                            continue;
//                        }
//                    }
//                }
//                clickTheElement(operatingHoursSaveBtn);
//                waitForSeconds(3);
//                if (isElementEnabled(editBudgetBtn, 10)) {
//                    SimpleUtils.pass("Create Schedule: Save the budget hours Successfully!");
//                }else {
//                    SimpleUtils.fail("Create Schedule: Click on Save the budget hours button failed, Next button is not enabled!", false);
//                }
//            }
//        }else {
//            SimpleUtils.fail("Create Schedule - Enter Budget: Edit button not loaded Successfully!", false);
//        }
//    }
//
    @FindBy (css = ".generate-modal-operating-hours-step-container .lg-picker-input")
    private WebElement locationSelectorOnCreateSchedulePage;
//
//    @FindBy (css = ".modal-instance .lg-search-options__option")
//    private List<WebElement> locationsInLocationSelectorOnCreateSchedulePage;
//
//    @FindBy (css = "input[placeholder=\"Search Location\"]")
//    private WebElement searchLocationOnCreateSchedulePage;
//
//    @FindBy (css = ".generate-modal-subheader-location-name")
//    private WebElement selectedLocationOnCreateSchedulePage;
//
//    @FindBy (css = "[ng-click=\"openSearchDropDown()\"]")
//    private WebElement openSearchLocationBoxButton;
//
//    @FindBy (css = "[ng-click=\"closeSearchDropDown()\"]")
//    private WebElement closeSearchLocationBoxButton;
//
//    @FindBy (css = "input[placeholder=\"Search Locations\"]")
//    private WebElement searchLocationOnUngenerateSchedulePage;
//
//    @FindBy (css = "div[class=\"lg-picker-input__wrapper lg-ng-animate\"] .lg-search-options__option-wrapper")
//    private List<WebElement> locationsInLocationSelectorOnUngenerateSchedulePage;
//
//    @FindBy (css = "div.schedule-summary-location-picker span")
//    private WebElement selectedLocationOnUngenerateSchedulePage;
//
//    public void selectRandomOrSpecificLocationOnUngenerateScheduleEditOperatingHoursPage(String specificLocationName) throws Exception {
//        if (isElementLoaded(openSearchLocationBoxButton, 60)||(isElementLoaded(closeSearchLocationBoxButton, 60))){
//            if(isElementLoaded(openSearchLocationBoxButton, 5)){
//                click(openSearchLocationBoxButton);
//            }
//            if(isElementLoaded(searchLocationOnUngenerateSchedulePage, 5)){
//                click(searchLocationOnUngenerateSchedulePage);
//            } else
//                SimpleUtils.fail("Ungenerate schedule page: Search locations box fail to open! ", false);
//
//            if(areListElementVisible(locationsInLocationSelectorOnUngenerateSchedulePage, 5)
//                    && locationsInLocationSelectorOnUngenerateSchedulePage.size() >0){
//                String locationName = specificLocationName;
//                if (locationName == null){
//                    int randomLocations = (new Random()).nextInt(locationsInLocationSelectorOnUngenerateSchedulePage.size());
//                    locationName = locationsInLocationSelectorOnUngenerateSchedulePage.get(randomLocations).getText();
//                }
//
//                if(isElementLoaded(searchLocationOnUngenerateSchedulePage, 5)){
//                    searchLocationOnUngenerateSchedulePage.sendKeys(locationName);
//                }
//                waitForSeconds(3);
//                click(locationsInLocationSelectorOnUngenerateSchedulePage.get(0));
////                if(areListElementVisible(locationsInLocationSelectorOnUngenerateSchedulePage)){
////
////                    click(locationsInLocationSelectorOnUngenerateSchedulePage.get(randomLocations));
////                }
//
//                if(selectedLocationOnUngenerateSchedulePage.getText().equals(locationName)){
//                    SimpleUtils.pass("Ungenerate schedule page: Select locations on Edit Operating hours successfully! ");
//                } else
//                    SimpleUtils.fail("Ungenerate schedule page: Select locations on Edit Operating hours failed! ", false);
//
//            } else
//                SimpleUtils.fail("Ungenerate schedule page: Locations fail to list! ", false);
//        } else
//            SimpleUtils.fail("Ungenerate schedule page: Search location buttons fail to load! ", false);
//    }

//    public void selectRandomLocationOnCreateScheduleEditOperatingHoursPage() throws Exception {
//        int randomLocations = (new Random()).nextInt(locationsInLocationSelectorOnCreateSchedulePage.size());
//        String randomLocationName = locationsInLocationSelectorOnCreateSchedulePage.get(randomLocations).getText();
//        waitForSeconds(3);
//        if(isElementLoaded(searchLocationOnCreateSchedulePage, 5)){
//            searchLocationOnCreateSchedulePage.sendKeys(randomLocationName);
//        }
//        click(locationsInLocationSelectorOnCreateSchedulePage.get(0));
//        if(selectedLocationOnCreateSchedulePage.getText().equals(randomLocationName)){
//            SimpleUtils.pass("Select locations on Edit Operating hours successfully! ");
//        } else
//            SimpleUtils.fail("Select locations on Edit Operating hours failed! ", false);
//    }

//    @Override
//    public void editTheOperatingHours(List<String> weekDaysToClose) throws Exception {
//        try{
//            if (isElementLoaded(operatingHoursEditBtn, 10)) {
//                clickTheElement(operatingHoursEditBtn);
//                if (isElementLoaded(locationSelectorOnCreateSchedulePage, 5)
//                        && areListElementVisible(locationsInLocationSelectorOnCreateSchedulePage, 5)
//                        && locationsInLocationSelectorOnCreateSchedulePage.size() > 0) {
//                    click(locationSelectorOnCreateSchedulePage);
//                    selectRandomLocationOnCreateScheduleEditOperatingHoursPage();
//                }
//                if (isElementLoaded(operatingHoursCancelBtn, 10) && isElementLoaded(operatingHoursSaveBtn, 10)) {
//                    SimpleUtils.pass("Click on Operating Hours Edit button Successfully!");
//                    if (areListElementVisible(operatingHoursDayLists, 15)) {
//                        for (WebElement dayList : operatingHoursDayLists) {
//                            WebElement weekDay = dayList.findElement(By.cssSelector(".operating-hours-day-list-item-day"));
//                            if (weekDay != null) {
//                                WebElement checkbox = dayList.findElement(By.cssSelector("input[type=\"checkbox\"]"));
//                                if (!weekDaysToClose.contains(weekDay.getText())) {
//                                    if (checkbox.getAttribute("class").contains("ng-empty")) {
//                                        clickTheElement(checkbox);
//                                    }
//                                    String[] operatingHours = null;
//                                    if (isElementLoaded(locationSelectorOnCreateSchedulePage, 5)) {
//                                        operatingHours = propertyOperatingHoursLG.get(weekDay.getText()).split("-");
//                                    } else
//                                        operatingHours = propertyOperatingHours.get(weekDay.getText()).split("-");
//                                    List<WebElement> startNEndTimes = dayList.findElements(By.cssSelector("[ng-if*=\"day.isOpened\"] input"));
//                                    startNEndTimes.get(0).clear();
//                                    startNEndTimes.get(1).clear();
//                                    startNEndTimes.get(0).sendKeys(operatingHours[0].trim());
//                                    startNEndTimes.get(1).sendKeys(operatingHours[1].trim());
//                                } else {
//                                    if (!checkbox.getAttribute("class").contains("ng-empty")) {
//                                        clickTheElement(checkbox);
//                                    }
//                                }
//                            } else {
//                                SimpleUtils.fail("Failed to find weekday element!", false);
//                            }
//                        }
//                        clickTheElement(operatingHoursSaveBtn);
//                        if (isElementEnabled(operatingHoursEditBtn, 15)) {
//                            SimpleUtils.pass("Create Schedule: Save the operating hours Successfully!");
//                        } else {
//                            SimpleUtils.fail("Create Schedule: Click on Save the operating hours button failed, Next button is not enabled!", false);
//                        }
//                    }
//                } else {
//                    SimpleUtils.fail("Click on Operating Hours Edit button failed!", false);
//                }
//            }else {
//                SimpleUtils.fail("Operating Hours Edit button not loaded Successfully!", false);
//            }
//        } catch (StaleElementReferenceException e){
//            SimpleUtils.report(e.getMessage());
//        }
//    }
//
//    private boolean isOperatingHoursConsistentWithTheRequiredHours() throws Exception {
//        boolean isConsistent = true;
//        if (areListElementVisible(currentOperatingHours, 5) && currentOperatingHours.size() == 7) {
//            for (WebElement operatingHour : currentOperatingHours) {
//                WebElement weekDay = operatingHour.findElement(By.className("operating-hours-day-list-item-day"));
//                List<WebElement> times = operatingHour.findElements(By.cssSelector(".operating-hours-day-list-item-hours input"));
//                if (isElementLoaded(locationSelectorOnCreateSchedulePage, 5)){
//                    if (!propertyOperatingHoursLG.get(weekDay.getText()).contains(times.get(0).getAttribute("value")) ||
//                            !propertyOperatingHoursLG.get(weekDay.getText()).contains(times.get(1).getAttribute("value"))) {
//                        isConsistent = false;
//                        break;
//                    }
//                } else {
//                    if (!propertyOperatingHours.get(weekDay.getText()).contains(times.get(0).getAttribute("value")) ||
//                            !propertyOperatingHours.get(weekDay.getText()).contains(times.get(1).getAttribute("value"))) {
//                        isConsistent = false;
//                        break;
//                    }
//                }
//            }
//        } else {
//            isConsistent = false;
//        }
//        return isConsistent;
//    }

//    public void switchToManagerViewToCheckForSecondGenerate() throws Exception {
//        try {
//            String activeWeekText = getActiveWeekText();
//            if (isScheduleTypeLoaded()) {
//                if (activScheduleType.getText().equalsIgnoreCase("Suggested")) {
//                    click(scheduleTypeManager);
//                    waitForSeconds(3);
//                    if (isReGenerateButtonLoadedForManagerView()) {
//                        click(reGenerateScheduleButton);
//                        generateScheduleFromCreateNewScheduleWindow(activeWeekText);
//                        selectWhichWeekToCopyFrom("SUGGESTED");
//                        clickOnFinishButtonOnCreateSchedulePage();
//                    } else if (isElementLoaded(publishSheduleButton, 5)) {
//                        SimpleUtils.pass("Generate the schedule for week: " + activeWeekText + " Successfully!");
//                    } else if (areListElementVisible(weekShifts, 5)) {
//                        SimpleUtils.pass("Generate the schedule for week: " + activeWeekText + " Successfully!");
//                    } else {
//                        SimpleUtils.fail("Generate button or Publish Button not found on page", false);
//                    }
//                } else {
//                    if (isReGenerateButtonLoadedForManagerView()) {
//                        click(reGenerateScheduleButton);
//                        generateScheduleFromCreateNewScheduleWindow(activeWeekText);
//                    } else if (isElementLoaded(publishSheduleButton, 10)) {
//                        SimpleUtils.pass("Generate the schedule for week: " + activeWeekText + " Successfully!");
//                    } else if (areListElementVisible(weekShifts, 10)) {
//                        SimpleUtils.pass("Generate the schedule for week: " + activeWeekText + " Successfully!");
//                    } else {
//                        SimpleUtils.fail("Generate button or Publish not found on page", false);
//                    }
//                }
//            } else {
//                SimpleUtils.report("Schedule Type Suggested/Manager is disabled");
////                getDriver().navigate().refresh();
////                waitForSeconds(5);
//                if (isReGenerateButtonLoadedForManagerView()) {
//                    click(reGenerateScheduleButton);
//                    generateScheduleFromCreateNewScheduleWindow(activeWeekText);
//                } else if (isElementLoaded(publishSheduleButton, 5)) {
//                    SimpleUtils.pass("Generate the schedule for week: " + activeWeekText + " Successfully!");
//                } else if (areListElementVisible(weekShifts, 5)) {
//                    SimpleUtils.pass("Generate the schedule for week: " + activeWeekText + " Successfully!");
//                } else {
//                    SimpleUtils.fail("Generate button or Publish not found on page", false);
//                }
//            }
//            if (isElementEnabled(checkOutTheScheduleButton, 20)) {
//                checkoutSchedule();
//            }
//            waitForSeconds(5);
//            if (areListElementVisible(shiftsWeekView, 15) && shiftsWeekView.size() > 0) {
//                SimpleUtils.pass("Create the schedule successfully!");
//            } else {
//                SimpleUtils.fail("Not able to generate the schedule successfully!", false);
//            }
//        } catch (Exception e) {
//            // Do nothing
//        }
//    }

//    private boolean isScheduleTypeLoaded() {
//        try {
//            if (isElementEnabled(activScheduleType, 10)) {
//                return true;
//            } else {
//                return false;
//            }
//        } catch (Exception e) {
//            return false;
//        }
//    }

    //added by haya, create button for non-dg flow
//    @Override
//    public void clickCreateScheduleBtn() throws Exception {
//        if (isElementEnabled(generateSheduleButton,10)) {
//            click(generateSheduleButton);
//            openBudgetPopUp();
//
//        }else {
//            SimpleUtils.fail("Create Schedule button not loaded Successfully!", false);
//        }
//    }

//    @FindBy(css = "div[ng-click=\"back()\"]")
//    private WebElement backBtnOnCreateScheduleWindow;
//    @Override
//    public void clickBackBtnAndExitCreateScheduleWindow() throws Exception {
//        if (isElementEnabled(backBtnOnCreateScheduleWindow,10)) {
//            click(backBtnOnCreateScheduleWindow);
//            click(backBtnOnCreateScheduleWindow);
//            if (isElementEnabled(backBtnOnCreateScheduleWindow,10)) {
//                click(backBtnOnCreateScheduleWindow);
//            }
//        }else {
//            SimpleUtils.fail("Back button on create schedule popup window is not loaded Successfully!", false);
//        }
//    }
//
//    @Override
//    public void clickExitBtnToExitCreateScheduleWindow() throws Exception {
//        if (isElementEnabled(backBtnOnCreateScheduleWindow,10)) {
//            click(backBtnOnCreateScheduleWindow);
//        }else {
//            SimpleUtils.fail("Exit button on create schedule popup window is not loaded Successfully!", false);
//        }
//    }

    //added by haya, edit operating hours when create new schedule for non-dg flow.
    //e.g.: day: Sunday, startTime->09:00AM, endTime->05:00PM
//    @Override
//    public void editOperatingHoursWithGivingPrameters(String day, String startTime, String endTime) throws Exception {
//        if (isElementLoaded(operatingHoursEditBtn, 10)) {
//            clickTheElement(operatingHoursEditBtn);
//            if (isElementLoaded(operatingHoursCancelBtn, 10) && isElementLoaded(operatingHoursSaveBtn, 10)) {
//                SimpleUtils.pass("Click on Operating Hours Edit button Successfully!");
//                if (areListElementVisible(operatingHoursDayLists, 15)) {
//                    for (WebElement dayList : operatingHoursDayLists) {
//                        WebElement checkbox = dayList.findElement(By.cssSelector("input[type=\"checkbox\"]"));
//                        WebElement weekDay = dayList.findElement(By.cssSelector(".operating-hours-day-list-item-day"));
//                        List<WebElement> startNEndTimes = dayList.findElements(By.cssSelector("[ng-if*=\"day.isOpened\"] input"));
//                        if (checkbox != null && weekDay != null && startNEndTimes != null && startNEndTimes.size() == 2) {
//                            if (checkbox.getAttribute("class").contains("ng-empty")) {
//                                SimpleUtils.warn("editOperatingHoursWithGivingPrameters: All seven day of a week should be checked by default when create schedule.");
//                                clickTheElement(checkbox);
//                            }
//                            if (weekDay.getText().toLowerCase().contains(day.toLowerCase())){
//                                startNEndTimes = dayList.findElements(By.cssSelector("[ng-if*=\"day.isOpened\"] input"));
//                                String openTime = startNEndTimes.get(0).getAttribute("value");
//                                String closeTime = startNEndTimes.get(1).getAttribute("value");
//                                if (!openTime.equals(startTime) || !closeTime.equals(endTime)) {
//                                    startNEndTimes.get(0).clear();
//                                    startNEndTimes.get(1).clear();
//                                    startNEndTimes.get(0).sendKeys(startTime);
//                                    startNEndTimes.get(1).sendKeys(endTime);
//                                }
//
//                            }
//                        }else {
//                            SimpleUtils.fail("Failed to find the checkbox, weekday or start and end time elements!", false);
//                        }
//                    }
//                    clickTheElement(operatingHoursSaveBtn);
//                    if (isElementEnabled(operatingHoursEditBtn, 10)) {
//                        SimpleUtils.pass("Create Schedule: Save the operating hours Successfully!");
//                    }else {
//                        SimpleUtils.fail("Create Schedule: Click on Save the operating hours button failed, Next button is not enabled!", false);
//                    }
//                }
//            }else {
//                SimpleUtils.fail("Click on Operating Hours Edit button failed!", false);
//            }
//        }else {
//            SimpleUtils.fail("Operating Hours Edit button not loaded Successfully!", false);
//        }
//    }

    //added by haya, create schedule for non-dg flow with giving parameters.
    //e.g.: day: Sunday, startTime->09:00AM, endTime->05:00PM
//    @Override
//    public void createScheduleForNonDGFlowNewUIWithGivingParameters(String day, String startTime, String endTime) throws Exception {
//        String subTitle = "Confirm Operating Hours";
//        if (isElementLoaded(generateSheduleButton,10)) {
//            moveToElementAndClick(generateSheduleButton);
//            openBudgetPopUp();
//            if (isElementLoaded(generateModalTitle, 15) && subTitle.equalsIgnoreCase(generateModalTitle.getText().trim())
//                    && isElementLoaded(nextButtonOnCreateSchedule, 15)) {
//                editOperatingHoursWithGivingPrameters(day, startTime, endTime);
//                waitForSeconds(3);
//                clickTheElement(nextButtonOnCreateSchedule);
//                checkEnterBudgetWindowLoadedForNonDG();
//                selectWhichWeekToCopyFrom("SUGGESTED");
//                clickOnFinishButtonOnCreateSchedulePage();
//                switchToManagerViewToCheckForSecondGenerate();
//            }else if (isElementLoaded(generateSheduleForEnterBudgetBtn, 5)) {
//                click(generateSheduleForEnterBudgetBtn);
//                if (isElementEnabled(checkOutTheScheduleButton, 20)) {
//                    checkoutSchedule();
//                    switchToManagerViewToCheckForSecondGenerate();
//                } else if (isElementLoaded(updateAndGenerateScheduleButton, 5)) {
//                    updateAndGenerateSchedule();
//                    switchToManagerViewToCheckForSecondGenerate();
//                } else {
//                    SimpleUtils.fail("Not able to generate Schedule Successfully!", false);
//                }
//            } else if (isElementLoaded(updateAndGenerateScheduleButton, 5)) {
//                updateAndGenerateSchedule();
//                switchToManagerViewToCheckForSecondGenerate();
//            } else if (isElementEnabled(checkOutTheScheduleButton,20)) {
//                checkOutGenerateScheduleBtn(checkOutTheScheduleButton);
//                SimpleUtils.pass("Schedule Generated Successfully!");
//                switchToManagerViewToCheckForSecondGenerate();
//            } else {
//                SimpleUtils.fail("Not able to generate schedule Successfully!", false);
//            }
//        }else {
//            SimpleUtils.fail("Create Schedule button not loaded Successfully!", false);
//        }
//    }
//    @Override
//    public void createScheduleForNonDGFlowNewUIWithGivingTimeRange(String startTime, String endTime) throws Exception {
//        String subTitle = "Confirm Operating Hours";
//        if (isElementLoaded(generateSheduleButton,10)) {
//            moveToElementAndClick(generateSheduleButton);
//            openBudgetPopUp();
//            if (isElementLoaded(generateModalTitle, 15) && subTitle.equalsIgnoreCase(generateModalTitle.getText().trim())
//                    && isElementLoaded(nextButtonOnCreateSchedule, 15)) {
//                editOperatingHoursWithGivingPrameters("Sunday", startTime, endTime);
//                editOperatingHoursWithGivingPrameters("Monday", startTime, endTime);
//                editOperatingHoursWithGivingPrameters("Tuesday", startTime, endTime);
//                editOperatingHoursWithGivingPrameters("Wednesday", startTime, endTime);
//                editOperatingHoursWithGivingPrameters("Thursday", startTime, endTime);
//                editOperatingHoursWithGivingPrameters("Friday", startTime, endTime);
//                editOperatingHoursWithGivingPrameters("Saturday", startTime, endTime);
//                waitForSeconds(3);
//                clickTheElement(nextButtonOnCreateSchedule);
//                waitForSeconds(2);
//                checkEnterBudgetWindowLoadedForNonDG();
//                selectWhichWeekToCopyFrom("SUGGESTED");
//                clickOnFinishButtonOnCreateSchedulePage();
//                switchToManagerViewToCheckForSecondGenerate();
//            }else if (isElementLoaded(generateSheduleForEnterBudgetBtn, 5)) {
//                click(generateSheduleForEnterBudgetBtn);
//                if (isElementEnabled(checkOutTheScheduleButton, 20)) {
//                    checkoutSchedule();
//                    switchToManagerViewToCheckForSecondGenerate();
//                } else if (isElementLoaded(updateAndGenerateScheduleButton, 5)) {
//                    updateAndGenerateSchedule();
//                    switchToManagerViewToCheckForSecondGenerate();
//                } else {
//                    SimpleUtils.fail("Not able to generate Schedule Successfully!", false);
//                }
//            } else if (isElementLoaded(updateAndGenerateScheduleButton, 5)) {
//                updateAndGenerateSchedule();
//                switchToManagerViewToCheckForSecondGenerate();
//            } else if (isElementEnabled(checkOutTheScheduleButton,20)) {
//                checkOutGenerateScheduleBtn(checkOutTheScheduleButton);
//                SimpleUtils.pass("Schedule Generated Successfully!");
//                switchToManagerViewToCheckForSecondGenerate();
//            } else {
//                SimpleUtils.fail("Not able to generate schedule Successfully!", false);
//            }
//        }else {
//            SimpleUtils.fail("Create Schedule button not loaded Successfully!", false);
//        }
//    }

//    //added by Haya
//    @FindBy (css = "button.dropdown-toggle")
//    private WebElement dropdownToggle;
//    @FindBy (css = ".lg-dropdown-menu__option")
//    private List<WebElement> dropdownMenuFormDropdownToggle;
//    @Override
//    public void goToToggleSummaryView() throws Exception {
//        waitForSeconds(2);
//        if (isElementLoaded(dropdownToggle,10)){
//            click(dropdownToggle);
//            if (areListElementVisible(dropdownMenuFormDropdownToggle,10)&&dropdownMenuFormDropdownToggle.size()==3 ){
//                waitForSeconds(3);
//                click(dropdownMenuFormDropdownToggle.get(2));
//                SimpleUtils.pass("Toggle Summary View has been clicked!");
//            } else {
//                SimpleUtils.fail("After clicking dropdown toggle button, no menu drop down", false);
//            }
//        } else {
//            SimpleUtils.fail("There is no toggle drop down button in schedule page!", false);
//        }
//    }
//
//    @Override
//    public void clickToggleSummaryViewButton() throws Exception {
//        if (areListElementVisible(dropdownMenuFormDropdownToggle,10)&&dropdownMenuFormDropdownToggle.size()==3 ){
//            click(dropdownMenuFormDropdownToggle.get(2));
//            SimpleUtils.pass("Toggle Summary View has been clicked!");
//        } else {
//            if (isElementLoaded(dropdownToggle,10)){
//                click(dropdownToggle);
//            } else {
//                SimpleUtils.fail("There is no toggle drop down button in schedule page!", false);
//            }
//        }
//    }
//
//    //added by Haya
//    @FindBy (css = "div.generate-schedule-stats")
//    private WebElement scheduleSummary;
//    @Override
//    public void verifyOperatingHrsInToggleSummary(String day, String startTime, String endTime) throws Exception {
//        if (isElementLoaded(scheduleSummary) && isElementLoaded(scheduleSummary.findElement(By.cssSelector("div[ng-class=\"hideItem('projected.sales')\"] table")))){
//            List<WebElement> dayInSummary = scheduleSummary.findElements(By.cssSelector("div[ng-class=\"hideItem('projected.sales')\"] tr[ng-repeat=\"day in summary.workingHours\"]"));
//            for (WebElement e : dayInSummary){
//                if (e.getText().contains(day) && e.getText().contains(getTimeFormat(startTime)) && e.getText().contains(getTimeFormat(endTime))){
//                    SimpleUtils.pass("Operating Hours is consistent with setting!");
//                }
//            }
//        } else {
//            SimpleUtils.fail("schedule summary fail to load!", false);
//        }
//    }
//
//    //added by Haya. 09:00AM-->9am
//    private String getTimeFormat(String time) throws Exception{
//        String result = time.substring(0,2);
//        if (time.contains("AM") | time.contains("am")){
//            result = result.concat("am");
//        } else {
//            result = result.concat("pm");
//        }
//        if (result.indexOf("0")==0){
//            result = result.substring(1);
//        }
//        return result;
//    }

    @FindBy (css = ".sch-calendar-day-dimension")
    private List<WebElement> scheduleDays;
//    @Override
//    public void verifyDayHasShifts(String day) throws Exception {
//        if (areListElementVisible(scheduleDays,10)){
//            if (day.toLowerCase().contains("sunday")) {
//                for (WebElement e : scheduleDays) {
//                    if (e.getAttribute("class").contains("0")) {
//                        String data = e.getAttribute("data-day");
//                        if (areListElementVisible(MyThreadLocal.getDriver().findElements(By.cssSelector("div[data-day=\"" + data + "\"].week-schedule-shift")), 10))
//                            SimpleUtils.pass("On Sunday there are shifts!");
//                        else
//                            SimpleUtils.fail("There are no shifts on Sunday!", false);
//                        break;
//                    }
//                }
//            } else if (day.toLowerCase().contains("monday")){
//                for (WebElement e : scheduleDays){
//                    if (e.getAttribute("class").contains("1")){
//                        String data = e.getAttribute("data-day");
//                        if (areListElementVisible(MyThreadLocal.getDriver().findElements(By.cssSelector("div[data-day=\"" + data + "\"].week-schedule-shift")),10))
//                            SimpleUtils.pass("On Sunday there are shifts!");
//                        else
//                            SimpleUtils.fail("There are no shifts on Sunday!",false);
//                        break;
//                    }
//                }
//            } else if (day.toLowerCase().contains("tuesday")){
//                for (WebElement e : scheduleDays){
//                    if (e.getAttribute("class").contains("2")){
//                        String data = e.getAttribute("data-day");
//                        if (areListElementVisible(MyThreadLocal.getDriver().findElements(By.cssSelector("div[data-day=\"" + data + "\"].week-schedule-shift")),10))
//                            SimpleUtils.pass("On Sunday there are shifts!");
//                        else
//                            SimpleUtils.fail("There are no shifts on Sunday!",false);
//                        break;
//                    }
//                }
//            }
//        } else if (day.toLowerCase().contains("wednesday")){
//            for (WebElement e : scheduleDays){
//                if (e.getAttribute("class").contains("3")){
//                    String data = e.getAttribute("data-day");
//                    if (areListElementVisible(MyThreadLocal.getDriver().findElements(By.cssSelector("div[data-day=\"" + data + "\"].week-schedule-shift")),10))
//                        SimpleUtils.pass("On Sunday there are shifts!");
//                    else
//                        SimpleUtils.fail("There are no shifts on Sunday!",false);
//                    break;
//                }
//            }
//        } else if (day.toLowerCase().contains("thursday")){
//            for (WebElement e : scheduleDays){
//                if (e.getAttribute("class").contains("4")){
//                    String data = e.getAttribute("data-day");
//                    if (areListElementVisible(MyThreadLocal.getDriver().findElements(By.cssSelector("div[data-day=\"" + data + "\"].week-schedule-shift")),10))
//                        SimpleUtils.pass("On Sunday there are shifts!");
//                    else
//                        SimpleUtils.fail("There are no shifts on Sunday!",false);
//                    break;
//                }
//            }
//        } else if (day.toLowerCase().contains("friday")){
//            for (WebElement e : scheduleDays){
//                if (e.getAttribute("class").contains("5")){
//                    String data = e.getAttribute("data-day");
//                    if (areListElementVisible(MyThreadLocal.getDriver().findElements(By.cssSelector("div[data-day=\"" + data + "\"].week-schedule-shift")),10))
//                        SimpleUtils.pass("On Sunday there are shifts!");
//                    else
//                        SimpleUtils.fail("There are no shifts on Sunday!",false);
//                    break;
//                }
//            }
//        } else if (day.toLowerCase().contains("saturday")){
//            for (WebElement e : scheduleDays){
//                if (e.getAttribute("class").contains("6")){
//                    String data = e.getAttribute("data-day");
//                    if (areListElementVisible(MyThreadLocal.getDriver().findElements(By.cssSelector("div[data-day=\"" + data + "\"].week-schedule-shift")),10))
//                        SimpleUtils.pass("On Sunday there are shifts!");
//                    else
//                        SimpleUtils.fail("There are no shifts on Sunday!",false);
//                    break;
//                }
//            }
//        } else {
//            SimpleUtils.fail("No schedule day loaded in schedule page!",false);
//        }
//    }

//    @Override
//    public List<String> getDayShifts(String index) throws Exception {
//        List<String> result = new ArrayList<>();
//        if (areListElementVisible(scheduleDays,10)){
//            for (WebElement e : scheduleDays) {
//                if (e.getAttribute("class").contains(index)) {
//                    String data = e.getAttribute("data-day");
//                    if (areListElementVisible(MyThreadLocal.getDriver().findElements(By.cssSelector("div[data-day=\"" + data + "\"].week-schedule-shift")), 10)){
//                        List<WebElement> shifts = MyThreadLocal.getDriver().findElements(By.cssSelector("div[data-day=\"" + data + "\"].week-schedule-shift"));
//                        for (WebElement shift : shifts){
//                            result.add(shift.getText());
//                        }
//                        SimpleUtils.pass("On Sunday there are shifts!");
//                    }
//                    break;
//                }
//            }
//        } else {
//            SimpleUtils.fail("No schedule day loaded in schedule page!",false);
//        }
//        return result;
//    }
//
//    public void checkOutGenerateScheduleBtn(WebElement checkOutTheScheduleButton) {
//        Wait<WebDriver> wait = new FluentWait<WebDriver>(
//                MyThreadLocal.getDriver()).withTimeout(Duration.ofSeconds(60))
//                .pollingEvery(Duration.ofSeconds(5))
//                .ignoring(NoSuchElementException.class);
//        Boolean element = wait.until(new Function<WebDriver, Boolean>() {
//            @Override
//            public Boolean apply(WebDriver t) {
//                boolean display = false;
//                display = t.findElement(By.cssSelector("[ng-click=\"goToSchedule()\"]")).isEnabled();
//                if (display)
//                    return true;
//                else
//                    return false;
//            }
//        });
//        if (element) {
//            click(checkOutTheScheduleButton);
//            SimpleUtils.pass("Schedule Generated Successfully!");
//        } else {
//            SimpleUtils.fail("Not able to generate Schedule Successfully!", false);
//        }
//
//    }
//
//
//    @Override
//    public boolean loadSchedule() throws Exception {
//        // TODO Auto-generated method stub
//        boolean flag=false;
//        if(isElementLoaded(ScheduleSubMenu)){
//            click(ScheduleSubMenu);
//            SimpleUtils.pass("Clicked on Schedule Sub Menu... ");
//            if(isElementLoaded(scheduleDayView)){
//                click(scheduleDayView);
//                SimpleUtils.pass("Clicked on Day View of Schedule Tab");
//                if(isElementLoaded(smartcard)){
//                    flag = true;
//                    SimpleUtils.pass("Smartcard Section in Day View Loaded Successfully!");
//                }else{
//                    SimpleUtils.fail("Smartcard Section in Day View Not Loaded Successfully!", true);
//                }
//                if(isElementLoaded(scheduleTableDayView)){
//                    flag = true;
//                    SimpleUtils.pass("Schedule in Day View Loaded Successfully!");
//                }else{
//                    SimpleUtils.fail("Schedule in Day View Not Loaded Successfully!", true);
//                }
//            }else{
//                SimpleUtils.fail("Day View button not found in Schedule Sub Tab",false);
//            }
//            if(isElementLoaded(scheduleWeekView,10)){
//                click(scheduleWeekView);
//                SimpleUtils.pass("Clicked on Week View of Schedule Tab");
//                if(isElementLoaded(smartcard,10)){
//                    flag = true;
//                    SimpleUtils.pass("Smartcard Section in Week View Loaded Successfully!");
//                }else{
//                    SimpleUtils.fail("Smartcard Section in Week View Not Loaded Successfully!", true);
//                }
//                if(isElementLoaded(scheduleTableWeekView,10)){
//                    flag = true;
//                    SimpleUtils.pass("Schedule in Week View Loaded Successfully!");
//                }else{
//                    SimpleUtils.fail("Schedule in Week View Not Loaded Successfully!", false);
//                }
//            }else{
//                SimpleUtils.pass("Week View button not found in Schedule Sub Tab");
//            }
//        }else{
//            SimpleUtils.fail("Schedule Sub Menu Tab Not Found", false);
//        }
//        return false;
//    }
//
//
//    @Override
//    public HashMap<String, Integer> getScheduleBufferHours() throws Exception {
//        HashMap<String, Integer> schedulePageBufferHours = new HashMap<String, Integer>();
//        int gutterCellCount = 0;
//        int totalHoursCountForShift = scheduleShiftTimeHeaderCells.size();
//        int cellCountInAnHour = 2;
//        int openingBufferHours = 0;
//        int closingBufferHours = 0;
//        if (scheduleShiftsRows.size() > 0) {
//            for (WebElement scheduleShiftsRow : scheduleShiftsRows) {
//                List<WebElement> scheduleShiftRowCells = scheduleShiftsRow.findElements(By.cssSelector("div.sch-day-view-grid-cell"));
//                String backgroundColorToVerify = "";
//                String[] styles = scheduleShiftRowCells.get(0).getAttribute("style").split(";");
//                for (String styleAttr : styles) {
//                    if (styleAttr.toLowerCase().contains("background-color"))
//                        backgroundColorToVerify = styleAttr;
//                }
//                if (backgroundColorToVerify != "") {
//                    cellCountInAnHour = Integer.valueOf(scheduleShiftRowCells.size() / totalHoursCountForShift);
//                    for (WebElement scheduleShiftRowCell : scheduleShiftRowCells) {
//                        if (scheduleShiftRowCell.getAttribute("style").contains(backgroundColorToVerify))
//                            gutterCellCount++;
//                        else {
//                            if (openingBufferHours == 0) {
//                                openingBufferHours = gutterCellCount;
//                                gutterCellCount = 0;
//                            }
//
//                        }
//                    }
//                    closingBufferHours += gutterCellCount;
//                }
//                else
//                    SimpleUtils.fail("Schedule Page: Unable to fetch backgroung color of 'Gutter Area'.", false);
//
//                schedulePageBufferHours.put("closingBufferHours", (closingBufferHours / cellCountInAnHour));
//                schedulePageBufferHours.put("openingBufferHours", (openingBufferHours / cellCountInAnHour));
//                break;
//            }
//        } else
//            SimpleUtils.fail("Schedule Page: Shift Rows not loaded.", false);
//
//        return schedulePageBufferHours;
//    }
//
//    @FindBy(css = "[ng-class=\"{'active': config.partialSchedule}\"]")
//    WebElement partialCopyOption;
//    @Override
//    public boolean isPartialCopyOptionLoaded() throws Exception {
//        if (isElementLoaded(partialCopyOption, 10)){
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public boolean isComlianceReviewRequiredForActiveWeek() throws Exception {
//        if (complianceReviewDangerImgs.size() > 0) {
//            return true;
//        }
//        return false;
//    }

    @FindBy (css = "lg-button[ng-click=\"deleteSchedule()\"]")
    private WebElement deleteScheduleButton;

    @FindBy (css = "div.redesigned-modal")
    private WebElement deleteSchedulePopup;

    @FindBy (css = ".redesigned-modal input")
    private WebElement deleteScheduleCheckBox;

    @FindBy (css = ".redesigned-button-ok")
    private WebElement deleteButtonOnDeleteSchedulePopup;

    @FindBy (css = ".redesigned-button-cancel-gray")
    private WebElement cancelButtonOnDeleteSchedulePopup;

//    @Override
//    public void unGenerateActiveScheduleScheduleWeek() throws Exception {
//
//        if(isElementLoaded(deleteScheduleButton, 60)){
//            click(deleteScheduleButton);
//            if(isElementLoaded(deleteSchedulePopup, 15)
//                    && isElementLoaded(deleteScheduleCheckBox, 5)
//                    && isElementLoaded(deleteButtonOnDeleteSchedulePopup, 5)){
//                click(deleteScheduleCheckBox);
//                waitForSeconds(1);
//                click(deleteButtonOnDeleteSchedulePopup);
//                if (isElementLoaded(generateSheduleButton, 60)) {
//                    SimpleUtils.pass("Schedule Page: Active Week ('" + getActiveWeekText() + "') Ungenerated Successfully.");
//                } else {
//                    SimpleUtils.fail("Schedule Page: Active Week ('" + getActiveWeekText() + "') isn't deleted successfully!", false);
//                }
//            } else
//                SimpleUtils.fail("Schedule Page: Delete schedule popup or delete schedule Button not loaded for the week: '"
//                        + getActiveWeekText() + "'.", false);
//
//        } else
//            SimpleUtils.fail("Schedule Page: Delete schedule button not loaded to Ungenerate the Schedule for the Week : '"
//                    + getActiveWeekText() + "'.", false);
//    }
//
//
//
//    @Override
//    public int getScheduleShiftIntervalCountInAnHour() throws Exception {
//        int schedulePageShiftIntervalMinutes = 0;
//        float totalHoursCountForShift = 0;
//        if (scheduleShiftsRows.size() > 0) {
//            for (WebElement scheduleShiftTimeHeaderCell : scheduleShiftTimeHeaderCells) {
//                if (scheduleShiftTimeHeaderCell.getText().trim().length() > 0)
//                    totalHoursCountForShift++;
//                else
//                    totalHoursCountForShift = (float) (totalHoursCountForShift + 0.5);
//            }
//            List<WebElement> scheduleShiftRowCells = scheduleShiftsRows.get(0).findElements(By.cssSelector("div.sch-day-view-grid-cell"));
//            int shiftIntervalCounts = scheduleShiftRowCells.size();
//            schedulePageShiftIntervalMinutes = Math.round(shiftIntervalCounts / totalHoursCountForShift);
//        } else
//            SimpleUtils.fail("Schedule Page: Shift Rows not loaded.", false);
//
//        return schedulePageShiftIntervalMinutes;
//    }

//    @Override
//    public void toggleSummaryView() throws Exception {
//        String toggleSummaryViewOptionText = "Toggle Summary View";
//        if (isElementLoaded(scheduleAdminDropDownBtn, 10)) {
//            click(scheduleAdminDropDownBtn);
//            if (scheduleAdminDropDownOptions.size() > 0) {
//                for (WebElement scheduleAdminDropDownOption : scheduleAdminDropDownOptions) {
//                    if (scheduleAdminDropDownOption.getText().toLowerCase().contains(toggleSummaryViewOptionText.toLowerCase())) {
//                        click(scheduleAdminDropDownOption);
//                    }
//                }
//            } else
//                SimpleUtils.fail("Schedule Page: Admin dropdown Options not loaded to Toggle Summary View for the Week : '"
//                        + getActiveWeekText() + "'.", false);
//        } else
//            SimpleUtils.fail("Schedule Page: Admin dropdown not loaded to Toggle Summary View for the Week : '"
//                    + getActiveWeekText() + "'.", false);
//    }

//    @FindBy(css = "div[ng-if=\"showSummaryView\"]")
//    private WebElement summaryViewDiv;
//
//    @Override
//    public boolean isSummaryViewLoaded() throws Exception {
//        if (isElementLoaded(summaryViewDiv))
//            return true;
//        return false;
//    }

    @FindBy(css = "tr[ng-repeat=\"day in summary.workingHours\"]")
    private List<WebElement> operatingHoursRows;

    @FindBy(css = "div.lgn-time-slider-notch-selector-start")
    private WebElement scheduleOperatingStartHrsSlider;

    @FindBy(css = "div.lgn-time-slider-notch-selector-end")
    private WebElement scheduleOperatingEndHrsSlider;

    @FindBy(css = "lg-button[label=\"Save\"]")
    private WebElement operatingHoursSaveBtn;

    @FindBy(css = "lg-button[label=\"Cancel\"]")
    private WebElement operatingHoursCancelBtn;
//
//    public void updateScheduleOperatingHours(String day, String startTime, String endTime) throws Exception {
//        waitForSeconds(1);
//        String strScheduleOperatingStartHrsSlider = null;
//        String strScheduleOperatingEndHrsSlider = null;
//        boolean startHrsSlider = true;
//        if (operatingHoursRows.size() > 0) {
//            for (WebElement operatingHoursRow : operatingHoursRows) {
//                if (operatingHoursRow.getText().toLowerCase().contains(day.toLowerCase())) {
//                    WebElement editBtn = operatingHoursRow.findElement(By.cssSelector("span[ng-if=\"canEditWorkingHours\"]"));
//                    if (isElementLoaded(editBtn)) {
//                        click(editBtn);
//                        if(customizeShiftStartdayLabel.getAttribute("class").contains("AM")){
//                            strScheduleOperatingStartHrsSlider = customizeShiftStartdayLabel.getText() + ":00AM";
//                        }else{
//                            strScheduleOperatingStartHrsSlider = customizeShiftStartdayLabel.getText() + ":00PM";
//                        }
//                        if(customizeShiftEnddayLabel.getAttribute("class").contains("PM")){
//                            strScheduleOperatingEndHrsSlider = customizeShiftEnddayLabel.getText() + ":00PM";
//                        }else{
//                            strScheduleOperatingEndHrsSlider = customizeShiftEnddayLabel.getText() + ":00AM";
//                        }
//                        if (strScheduleOperatingStartHrsSlider.toLowerCase().contains(startTime.toLowerCase())
//                                && strScheduleOperatingEndHrsSlider.toLowerCase().contains(endTime.toLowerCase())) {
//                            SimpleUtils.pass("Operating Hours already updated for the day '" + day + "' with Start time '" + startTime
//                                    + "' and End time '" + endTime + "'.");
//                            if (isElementLoaded(operatingHoursCancelBtn)) {
//                                click(operatingHoursCancelBtn);
//                            }
//                        } else {
//                            dragRollerElementTillTextMatched(customizeShiftStartdayLabel, startTime, startHrsSlider);
//                            dragRollerElementTillTextMatched(customizeShiftEnddayLabel, endTime, false);
//                            if (isElementLoaded(operatingHoursSaveBtn)) {
//                                click(operatingHoursSaveBtn);
//                                SimpleUtils.pass("Operating Hours updated for the day '" + day + "' with Start time '" + startTime
//                                        + "' and End time '" + endTime + "'.");
//                            }
//                        }
//                    } else
//                        SimpleUtils.fail("Operating Hours Table 'Edit' button not loaded.", false);
//                }
//            }
//        } else
//            SimpleUtils.fail("Operating Hours Rows not loaded.", false);
//    }
//
//
//    @FindBy(xpath = "//div[@class='lgn-time-slider-notch-mark']/following-sibling::div[1]")
//    private List<WebElement> sliderNotchLabel;
//
//    @FindBy(css = "div.lgn-time-slider-notch.droppable")
//    private List<WebElement> sliderNotchDroppable;
//
//    @Override
//    public void dragRollerElementTillTextMatched(WebElement rollerElement, String textToMatch, boolean startHrsSlider) throws Exception {
//        String rollerElementTxt = null;
//        if(rollerElement.getAttribute("class").contains("AM")){
//            rollerElementTxt = rollerElement.getText() + ":00AM";
//        }else{
//            rollerElementTxt = rollerElement.getText() + ":00PM";
//        }
//
//        if(startHrsSlider){
//            Outerloop:
//            for(int i = 0;i < sliderNotchDroppable.size();i++){
//                for(int j= 0;j< sliderNotchLabel.size();j++){
//                    if(rollerElement.getText().equals(sliderNotchLabel.get(j).getText()) && !rollerElementTxt.toLowerCase().contains(textToMatch.toLowerCase())){
//                        mouseHoverDragandDrop(scheduleOperatingStartHrsSlider, sliderNotchDroppable.get(j+Integer.parseInt(propertyCustomizeMap.get("INCREASE_START_OPERATING_TIME"))));
//                        break Outerloop;
//                    }
//                }
//            }
//        }else{
//            Outerloop:
//            for(int i = 0;i < sliderNotchLabel.size();i++){
//                for(int j= 0;j< sliderNotchDroppable.size();j++){
//                    if(rollerElement.getText().equals(sliderNotchLabel.get(j).getText()) && !rollerElementTxt.toLowerCase().contains(textToMatch.toLowerCase())){
//                        mouseHoverDragandDrop(scheduleOperatingEndHrsSlider, sliderNotchDroppable.get((j*2)+Integer.parseInt(propertyCustomizeMap.get("INCREASE_END_OPERATING_TIME"))));
//                        break Outerloop;
//                    }
//                }
//            }
//        }

//        int hourOnSlider = Integer.valueOf(rollerElement.getText());
//        if (rollerElementTxt.toLowerCase().contains("pm"))
//            hourOnSlider = hourOnSlider + 12;
//        int openingHourOnJson = Integer.valueOf(textToMatch.split(":")[0]);
//        if (textToMatch.toLowerCase().contains("pm"))
//            openingHourOnJson = openingHourOnJson + 12;
//        int sliderOffSet = 2;
//        if (hourOnSlider > openingHourOnJson)
//            sliderOffSet = -2;
//        if(startHrsSlider){
//            moveDayViewCards(scheduleOperatingStartHrsSlider, sliderOffSet);
//        }else{
//            moveDayViewCards(scheduleOperatingEndHrsSlider, sliderOffSet);
//        }
//    }
//
//    @Override
//    public boolean isScheduleOperatingHoursUpdated(String startTime, String endTime) throws Exception {
//        String scheduleShiftHeaderStartTime = "";
//        float hoursBeforeStartTime = 0;
//        String scheduleShiftHeaderEndTime = "";
//        float hoursAfterEndTime = 0;
//        if (scheduleShiftTimeHeaderCells.get(0).getText().trim().length() > 0)
//            scheduleShiftHeaderStartTime = scheduleShiftTimeHeaderCells.get(0).getText().split(" ")[0];
//        else {
//            hoursBeforeStartTime = (float) 0.5;
//            scheduleShiftHeaderStartTime = scheduleShiftTimeHeaderCells.get(1).getText().split(" ")[0];
//        }
//        //System.out.println("hoursBeforeStartTime : "+hoursBeforeStartTime);
//
//        if (scheduleShiftTimeHeaderCells.get(scheduleShiftTimeHeaderCells.size() - 1).getText().trim().length() > 0)
//            scheduleShiftHeaderEndTime = scheduleShiftTimeHeaderCells.get(scheduleShiftTimeHeaderCells.size() - 1).getText().split(" ")[0];
//        else {
//            hoursAfterEndTime = (float) 0.5;
//            scheduleShiftHeaderEndTime = scheduleShiftTimeHeaderCells.get(scheduleShiftTimeHeaderCells.size() - 2).getText().split(" ")[0];
//        }
//
//        //System.out.println("hoursAfterEndTime : "+hoursAfterEndTime);
//
//        HashMap<String, Integer> scheduleBufferHours = getScheduleBufferHours();
//        for (Entry<String, Integer> bufferHours : scheduleBufferHours.entrySet()) {
//            //System.out.println(bufferHours.getKey() +" : "+bufferHours.getValue());
//        }
//
//        float startHours = Float.valueOf(scheduleShiftHeaderStartTime) + hoursBeforeStartTime + scheduleBufferHours.get("openingBufferHours");
//        System.out.println("startHours: " + startHours);
//        float endHours = Float.valueOf(scheduleShiftHeaderEndTime) - scheduleBufferHours.get("closingBufferHours") + 1;
//        System.out.println("scheduleShiftHeaderEndTime : " + endHours);
//        if (Integer.valueOf(startTime.split(":")[0]) == (int) startHours && Integer.valueOf(endTime.split(":")[0]) == (int) endHours)
//            return true;
//
//        return false;
//    }

//    @Override
//    public void verifyScheduledHourNTMCountIsCorrect() throws Exception {
//        getHoursAndTeamMembersForEachDaysOfWeek();
//        verifyActiveWeekDailyScheduleHoursInWeekView();
//        verifyActiveWeekTeamMembersCountAvailableShiftCount();
//    }

    @FindBy(css = "card-carousel-card[ng-if='compliance'] div.card-carousel-card-smart-card-required")
    private WebElement complianceSmartCard;

    @FindBy(css = "img[ng-if='hasViolateCompliance(line, scheduleWeekDay)'] ")
    private List<WebElement> complianceInfoIcon;

    @FindBy(css = "card-carousel-card[ng-if*='compliance'] span")
    private WebElement viewShift;
    @FindBy(css = "img[ng-if='hasViolateCompliance(shift)']")
    private List<WebElement> complianceInfoIconDayView;

    @FindBy(css = "div.sch-worker-display-name")
    private List<WebElement> workerName;

    @FindBy(css = "div.week-schedule-worker-name.ng-binding")
    private List<WebElement> workerNameList;

    @FindBy(xpath = "//*[contains(@class,'week-view-shift-hover-info-icon')]/preceding-sibling::div")
    private List<WebElement> shiftDurationInWeekView;

    @FindBy(xpath = "//*[contains(@class,'shift-hover-subheading')]/parent::div/div[1]")
    private WebElement workerNameInPopUp;

    @FindBy(xpath = "//*[contains(@class,'shift-hover-subheading')]/parent::div/div[2]")
    private WebElement workerRoleDetailsFromPopUp;

    @FindBy(xpath = "//*[@class='shift-hover-seperator']/preceding-sibling::div[1]/div[1]")
    private WebElement shiftDurationInPopUp;

    @FindBy(css = "card-carousel-card[ng-if='compliance'] h1")
    private WebElement numberOfComplianceShift;

    @FindBy(css = "div[ng-repeat*='getComplianceMessages'] span")
    private List<WebElement> complianceMessageInPopUp;

    @FindBy (css = "div.sch-day-view-shift-worker-name")
    private List<WebElement> workerStatus;
//
//
//    public String timeFormatter(String formattedTime) {
//        String UpdatedTime;
//        String returnValue = null;
//        if (formattedTime.contains(":00")) {
//            UpdatedTime = formattedTime.replace(":00", "");
//            returnValue = UpdatedTime;
//        } else {
//            returnValue = formattedTime;
//        }
//        return returnValue;
//    }
//
//    public void captureShiftDetails() {
////	    HashMap<String, String> shiftDetailsWeekView = new HashMap<>();
//        HashMap<List<String>, List<String>> shiftWeekView = new HashMap<>();
//        List<String> workerDetailsWeekView = new ArrayList<>();
//        List<String> shiftDurationWeekView = new ArrayList<>();
//        HashMap<List<String>, List<String>> shiftDetailsPopUpView = new HashMap<>();
//        List<String> workerDetailsPopUpView = new ArrayList<>();
//        List<String> shiftDurationPopUpView = new ArrayList<>();
//        List<String> complianceMessage = new ArrayList<>();
//
////        boolean flag=true;
//        int counter = 0;
//        if (areListElementVisible(infoIcon)) {
//            for (int i = 0; i < infoIcon.size(); i++) {
//                if (areListElementVisible(complianceInfoIcon)) {
//                    if (counter < complianceInfoIcon.size()) {
//                        if (infoIcon.get(i).getAttribute("ng-if").equals(complianceInfoIcon.get(counter).getAttribute("ng-if"))) {
//                            counter = counter + 1;
//                            workerDetailsWeekView.add(workerName.get(i).getText().toLowerCase());
////							String formattedTime = shiftDurationInWeekView.get(i).getText();
////							timeFormatter(shiftDurationInWeekView.get(i).getText());
//                            shiftDurationWeekView.add(shiftDurationInWeekView.get(i).getText());
//
//                        }
//                    }
//                } else {
//                    SimpleUtils.fail("Shift not loaded successfully in week view", true);
//                }
//            }
//            shiftWeekView.put(workerDetailsWeekView, shiftDurationWeekView);
//            if (isElementEnabled(viewShift, 5)) {
//                click(viewShift);
//                if (areListElementVisible(complianceInfoIcon)) {
//                    for (int i = 0; i < complianceInfoIcon.size(); i++) {
//                        click(complianceInfoIcon.get(i));
//                        workerDetailsPopUpView.add(workerNameInPopUp.getText().toLowerCase());
//                        shiftDurationPopUpView.add(timeFormatter(shiftDurationInPopUp.getText()));
//                        ;
//                        String str = "";
//                        String finalstr = "";
//                        for (int j = 0; j < complianceMessageInPopUp.size(); j++) {
//                            str = complianceMessageInPopUp.get(j).getText();
//                            finalstr = finalstr + " " + j + "." + str;
//                        }
//                        SimpleUtils.pass(workerNameInPopUp.getText() + " has following voilations " + finalstr);
//                    }
//                    shiftDetailsPopUpView.put(workerDetailsPopUpView, shiftDurationPopUpView);
//                } else {
//                    SimpleUtils.fail("Shift not loaded successfully in week view", true);
//                }
//            }
//        } else {
//            SimpleUtils.fail("Shift not loaded successfully in week view", true);
//        }
//        for (Entry<List<String>, List<String>> entry : shiftWeekView.entrySet()) {
//            List<String> keysShiftWeekView = entry.getKey();
//            List<String> valuesShiftWeekView = entry.getValue();
//            System.out.println(shiftWeekView.keySet());
//            for (Entry<List<String>, List<String>> entry1 : shiftDetailsPopUpView.entrySet()) {
//                List<String> keysShiftDetailsPopUpView = entry1.getKey();
//                List<String> valuesShiftDetailsPopUpView = entry1.getValue();
//                int index = 0;
//                int count = 0;
//                for (int i = 0; i < keysShiftWeekView.size(); i++) {
//                    for (int j = 0; j < keysShiftDetailsPopUpView.size(); j++) {
//                        if (keysShiftWeekView.get(i).equals(keysShiftDetailsPopUpView.get(j))) {
//                            if (valuesShiftWeekView.get(i).replace(" ", "").equals(valuesShiftDetailsPopUpView.get(j)))
//                                SimpleUtils.pass("TM " + keysShiftWeekView.get(i) + " has shift " + valuesShiftWeekView.get(i));
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    @Override
//    public void complianceShiftSmartCard() throws Exception {
//        if (isElementEnabled(complianceSmartCard)) {
//            String[] complianceShiftCountFromSmartCard = numberOfComplianceShift.getText().split(" ");
//            int noOfcomplianceShiftFromSmartCard = Integer.valueOf(complianceShiftCountFromSmartCard[0]);
//            int noOfComplianceShiftInWeekScheduleTable = complianceInfoIcon.size();
//            captureShiftDetails();
//        }
//
//    }
//
//    @FindBy(css = "div.allow-pointer-events.ng-scope")
//    private List<WebElement> imageSize;
//    //    @FindBy(css = "span.sch-worker-action-label")
////    private List<WebElement> viewProfile;
//    @FindBy(css = "div[ng-class*='ChangeRole'] span")
//    private WebElement changeRole;
//    @FindBy(css = "div[ng-class*='ConvertToOpen'] span")
//    private WebElement convertOpen;
//    @FindBy(css = "div[ng-class*='ViewProfile'] span")
//    private WebElement viewProfile;
//    @FindBy(css = "div[ng-class*='AssignTM'] span")
//    private WebElement assignTM;
//    @FindBy(xpath = "//span[contains(text(),'YES')]")
//    private WebElement openPopYesButton;
//    @FindBy(css = "div[ng-class*='OfferTMs']")
//    private WebElement OfferTMS;
//    @FindBy(css = "div[ng-class*='EditShiftNotes']")
//    private WebElement EditShiftNotes;
//
//    public void beforeEdit() {
//        if (areListElementVisible(imageSize, 5)) {
//            click(imageSize.get(5));
//            WebElement element = getDriver().findElement(By.cssSelector("div.sch-worker-popover.allow-pointer-events.ng-scope"));
//            JavascriptExecutor jse = (JavascriptExecutor) getDriver();
//            String txt = jse.executeScript("return arguments[0].innerHTML;", element).toString();
//            System.out.println(txt);
////		  	 	   		if(viewProfile.size()>0){
////				   		   click(viewProfile.get(1));
////			 	   		}
//
//        }
//    }
//

//    public void selectTeamMembersOptionForOverlappingSchedule() throws Exception{
//        if(isElementEnabled(btnSearchTeamMember,5)){
//            click(btnSearchTeamMember);
//            if(isElementLoaded(textSearch,5)) {
//                searchWorkerName(propertySearchTeamMember.get("AssignTeamMember"));
//            }
//        }else{
//            SimpleUtils.fail("Select Team member option not available on page",false);
//        }
//
//    }
//
//
//    public boolean getScheduleOverlappingStatus()throws Exception {
//        boolean ScheduleStatus = false;
//        if(areListElementVisible(scheduleSearchTeamMemberStatus,5)){
//            for(int i=0; i<scheduleSearchTeamMemberStatus.size();i++){
//                if(scheduleSearchTeamMemberStatus.get(i).getText().contains("Scheduled")){
//                    click(radionBtnSearchTeamMembers.get(i));
//                    String workerDisplayFirstNameText =(searchWorkerDisplayName.get(i).getText().replace(" ","").split("\n"))[0];
//                    String workerDisplayLastNameText =(searchWorkerDisplayName.get(i).getText().replace(" ","").split("\n"))[1];
//                    setTeamMemberName(workerDisplayFirstNameText + workerDisplayLastNameText);
//                    boolean flag = displayAlertPopUp();
//                    if (flag) {
//                        ScheduleStatus = true;
//                        break;
//                    }
//                }
//            }
//        }
//
//        return ScheduleStatus;
//    }

//    public void searchWorkerName(String searchInput) throws Exception {
//        String[] searchAssignTeamMember = searchInput.split(",");
//        if(isElementLoaded(textSearch,10) && isElementLoaded(searchIcon,10)){
//            for(int i=0; i<searchAssignTeamMember.length;i++){
//                textSearch.sendKeys(searchAssignTeamMember[i]);
//                click(searchIcon);
//                if(getScheduleOverlappingStatus()){
//                    break;
//                }else{
//                    textSearch.clear();
//                    if(i== searchAssignTeamMember.length-1){
//                        SimpleUtils.fail("There is no data found for given team member. Please provide some other input", false);
//                    }
//                }
//            }
//        }else{
//            SimpleUtils.fail("Search text not editable and icon are not clickable", false);
//        }
//
//    }

//    @Override
//    public boolean displayAlertPopUp() throws Exception{
//        boolean flag = true;
//        String msgAlert = null;
//        if(isElementLoaded(popUpScheduleOverlap,5)){
//            if(isElementLoaded(alertMessage,5)){
//                msgAlert = alertMessage.getText();
//                if(isElementLoaded(btnAssignAnyway,5)){
//                    if(btnAssignAnyway.getText().toLowerCase().equals("OK".toLowerCase())){
//                        click(btnAssignAnyway);
//                        flag = false;
//                    } else if (btnAssignAnyway.getText().toUpperCase().equals("ASSIGN ANYWAY")) {
//                        flag = true;
//                    } else {
//                        String startTime = ((msgAlert.split(": "))[1].split(" - "))[0];
//                        String startTimeFinal = SimpleUtils.convertTimeIntoHHColonMM(startTime);
//                        String endTime = (((msgAlert.split(": "))[1].split(" - "))[1].split(" "))[0];
//                        String endTimeFinal = SimpleUtils.convertTimeIntoHHColonMM(endTime);
//                        String timeDuration = startTimeFinal + "-" + endTimeFinal;
//                        setScheduleHoursTimeDuration(timeDuration);
//                        click(btnAssignAnyway);
//                        flag= true;
//                    }
//
//                }else{
//                    SimpleUtils.fail("Schedule Overlap Assign Anyway button not displayed on the page",false);
//                }
//            }else{
//                SimpleUtils.fail("Schedule Overlap alert message not displayed",false);
//            }
//        }else {
//            flag = false;
//        }
//        return flag;
//    }
//
//
//    public void verifyScheduleStatusAsOpen() throws Exception {
//        boolean flag = true;
//        if(areListElementVisible(infoIcon)){
//            for(int i=0; i<infoIcon.size();i++){
//                if(areListElementVisible(workerStatus,5)){
//                    if(workerStatus.get(i).getText().toLowerCase().contains("Open".toLowerCase())){
//                        click(infoIcon.get(i));
//                        flag = verifyShiftDurationInComplianceImageIconPopUp(true);
//                        if(flag){
//                            SimpleUtils.pass("Worker status " +workerStatus.get(i).getText() + " matches with the expected result");
//                            click(infoIcon.get(i));
//                            break;
//                        }
//                    }else{
//                        flag = false;
//                    }
//                }else{
//                    SimpleUtils.fail("Worker status not available on the page",true);
//                }
//            }
//        }else{
//            SimpleUtils.fail("There is no image icon available on the page",false);
//        }
//
//        if(!flag) {
//            SimpleUtils.report("Worker status does not match with the expected result");
//        }
//
//    }
//
//
//    public boolean verifyShiftDurationInComplianceImageIconPopUp(boolean openStatus) throws Exception{
//        boolean flag = true;
//        if(openStatus){
//            shiftDurationVerification(getScheduleHoursTimeDuration());
//        }else{
//            String scheduledHoursStartTime = MyThreadLocal.getScheduleHoursStartTime();
//            String scheduledHoursEndTime = MyThreadLocal.getScheduleHoursEndTime();
//            String scheduleTimeDuration = scheduledHoursStartTime + "-" + scheduledHoursEndTime;
//            shiftDurationVerification(scheduleTimeDuration);
//        }
//
//        return flag;
//    }
//
//
//
//    public void verifyScheduleStatusAsTeamMember() throws Exception {
//        boolean flag = true;
//        if(areListElementVisible(infoIcon)){
//            for(int i=0; i<infoIcon.size();i++){
//                if(areListElementVisible(workerStatus,5)){
//                    if(workerStatus.get(i).getText().replace(" ","").toLowerCase().contains(getTeamMemberName().toLowerCase())){
//                        click(infoIcon.get(i));
//                        flag = verifyShiftDurationInComplianceImageIconPopUp(false);
//                        if(flag){
//                            SimpleUtils.pass("Worker status " +workerStatus.get(i).getText() + " matches with the expected result");
//                            break;
//                        }
//                    }else{
//                        flag = false;
//                    }
//                }else{
//                    SimpleUtils.fail("Worker status not available on the page",true);
//                }
//            }
//        }else{
//            SimpleUtils.fail("There is no image icon available on the page",false);
//        }
//
//        if(!flag) {
//            SimpleUtils.report("Worker status does not match with the expected result");
//        }
//
//    }
//
//
//    public void shiftDurationVerification(String scheduleHourTimeDuration) throws Exception{
//        if (isElementLoaded(shiftDurationInPopUp,5)){
//            if(shiftDurationInPopUp.getText().toLowerCase().equals(scheduleHourTimeDuration.toLowerCase())){
//                SimpleUtils.pass("Shift Time Duration " + shiftDurationInPopUp.getText().toLowerCase() + " matches with "+getScheduleHoursTimeDuration().toLowerCase());
//            }else{
//                SimpleUtils.report("Shift Time Duration value is " + shiftDurationInPopUp.getText().toLowerCase());
//            }
//        }else{
//            SimpleUtils.fail("Compliance Image icon Pop up was unable to open Successfully",false);
//        }
//    }
//

    //added by Nishant

//    public String getActiveAndNextDay() throws Exception{
//        WebElement activeWeek = MyThreadLocal.getDriver().findElement(By.className("day-week-picker-period-active"));
//        String activeDay = "";
//        if(isElementLoaded(activeWeek)){
//            activeDay = activeWeek.getText().replace("\n", " ").substring(0,3);
//        }
//        return activeDay;
//    }
//
//    public Map<String, String> getActiveDayInfo() throws Exception{
//
//        Map<String, String> dayInfo = new HashMap<>();
//        WebElement activeWeek = MyThreadLocal.getDriver().findElement(By.className("day-week-picker-period-active"));
//        String[] activeDay = activeWeek.getText().replace("\n", " ").split(" ");
//
//        dayInfo.put("weekDay", activeDay[0].substring(0, 3));
//        dayInfo.put("month", activeDay[1]);
//        dayInfo.put("day", activeDay[2]);
//        dayInfo.put("year", getYearsFromCalendarMonthYearText().get(0));
//
//        return dayInfo;
//    }

    @FindBy(css = "tr[ng-repeat='day in summary.workingHours'] td:nth-child(1)")
    private List<WebElement> operatingHoursScheduleDay;

    @FindBy(css = "tr[ng-repeat='day in summary.workingHours'] td.text-right.ng-binding")
    private List<WebElement> scheduleOperatingHrsTimeDuration;


//    public HashMap<String, String> getOperatingHrsValue(String day) throws Exception {
//        String currentDay = null;
//        String nextDay = null;
//        String finalDay = null;
//        HashMap<String, String>  activeDayAndOperatingHrs = new HashMap<>();
//
//        if(areListElementVisible(operatingHoursRows,5) &&
//                areListElementVisible(operatingHoursScheduleDay,5) &&
//                areListElementVisible(scheduleOperatingHrsTimeDuration,5)){
//            for(int i =0; i<operatingHoursRows.size();i++){
//                if(i == operatingHoursRows.size()-1){
//                    if(operatingHoursScheduleDay.get(i).getText().substring(0,3).equalsIgnoreCase(day)){
//                        currentDay = day;
//                        nextDay = operatingHoursScheduleDay.get(0).getText().substring(0,3);
//                        activeDayAndOperatingHrs.put("ScheduleOperatingHrs" , currentDay + "-" + scheduleOperatingHrsTimeDuration.get(i).getText());
//                        SimpleUtils.pass("Current day in Schedule " + day + " matches" +
//                                " with Operation hours Table " + operatingHoursScheduleDay.get(i).getText().substring(0,3));
//                        break;
//                    }else{
//                        SimpleUtils.fail("Current day in Schedule " + day + " does not match" +
//                                " with Operation hours Table " + operatingHoursScheduleDay.get(i).getText().substring(0,3),false);
//                    }
//                }else if(operatingHoursScheduleDay.get(i).getText().substring(0,3).equalsIgnoreCase(day)){
//                    currentDay = day;
//                    nextDay = operatingHoursScheduleDay.get(i+1).getText().substring(0,3);
//                    activeDayAndOperatingHrs.put("ScheduleOperatingHrs" , currentDay + "-" + scheduleOperatingHrsTimeDuration.get(i).getText());
//                    SimpleUtils.pass("Current day in Schedule " + day + " matches" +
//                            " with Operation hours Table " + operatingHoursScheduleDay.get(i).getText().substring(0,3));
//                    break;
//                }
//
//            }
//        }else{
//            SimpleUtils.fail("Operating hours table not loaded Successfully",false);
//        }
//        return activeDayAndOperatingHrs;
//    }
//
//
//    @FindBy(css = "div.lgn-time-slider-notch-label")
//    private List<WebElement> scheduleOperatingHrsOnEditPage;
//
//    @Override
//    public List<String> getAllOperatingHrsOnCreateShiftPage() throws Exception {
//        List<String> allOperatingHrs = new ArrayList<>();
//        if (areListElementVisible(scheduleOperatingHrsOnEditPage, 15)) {
//            for (WebElement operatingHour : scheduleOperatingHrsOnEditPage) {
//                if (operatingHour.getAttribute("class").contains("am")) {
//                    allOperatingHrs.add(operatingHour.getText() + "am");
//                } else {
//                    allOperatingHrs.add(operatingHour.getText() + "pm");
//                }
//            }
//        } else
//            SimpleUtils.fail("The operating hours on create shift page fail to load! ", false);
//        return allOperatingHrs;
//    }
//
//    @FindBy(css = "div.noUi-value-large")
//    private List<WebElement> startAndEndTimeOnEditShiftPage;
//
//    @Override
//    public List<String> getStartAndEndOperatingHrsOnEditShiftPage() throws Exception {
//        List<String> startAndEndOperatingHrs = new ArrayList<>();
//        if (areListElementVisible(startAndEndTimeOnEditShiftPage, 15)) {
//            for (WebElement operatingHour : startAndEndTimeOnEditShiftPage) {
//                startAndEndOperatingHrs.add(operatingHour.getText());
//            }
//        } else
//            SimpleUtils.fail("The operating hours on edit shift page fail to load! ", false);
//        return startAndEndOperatingHrs;
//    }

//    @Override
//    public boolean isHourFormat24Hour() throws Exception {
//        boolean is24Hour = true;
//        if (areListElementVisible(operatingHours, 15)) {
//            for (WebElement operatingHour : operatingHours) {
//                if (operatingHour.getText().toLowerCase().contains("am") || operatingHour.getText().contains("pm")) {
//                    is24Hour = false;
//                    break;
//                }
//            }
//        }
//        return is24Hour;
//    }

//    public void moveSliderAtCertainPoint(String shiftTime, String startingPoint) throws Exception {
//        WebElement element = null;
//        String am = "am";
//        String pm = "pm";
//        if (shiftTime.length() > 2 && (shiftTime.contains(am) || shiftTime.contains(pm))) {
//            if(areListElementVisible(scheduleOperatingHrsOnEditPage, 15)
//                    && scheduleOperatingHrsOnEditPage.size() >0){
//                for (WebElement scheduleOperatingHour: scheduleOperatingHrsOnEditPage){
//                    if (scheduleOperatingHour.getAttribute("class").contains(shiftTime.substring(shiftTime.length() - 2))) {
//                        if(scheduleOperatingHour.getText().equals(shiftTime.substring(0, shiftTime.length() - 2))){
//                            element = scheduleOperatingHour;
//                            break;
//                        }
//                    }
//                }
//            }
//        } else {
//            if(areListElementVisible(scheduleOperatingHrsOnEditPage, 15)
//                    && scheduleOperatingHrsOnEditPage.size() >0){
//                for (WebElement scheduleOperatingHour: scheduleOperatingHrsOnEditPage){
//                    if(scheduleOperatingHour.getText().equals(shiftTime)){
//                        element = scheduleOperatingHour;
//                        break;
//                    }
//                }
//            }
//        }
//        if (element == null){
//            SimpleUtils.fail("Cannot found the operating hour on edit operating hour page! ", false);
//        }
//        if(startingPoint.equalsIgnoreCase("End")){
//            if(isElementLoaded(sliderNotchEnd,10) && sliderDroppableCount.size()>0){
//                SimpleUtils.pass("Shift timings with Sliders loaded on page Successfully for End Point");
//                mouseHoverDragandDrop(sliderNotchEnd,element);
//            } else{
//                SimpleUtils.fail("Shift timings with Sliders not loaded on page Successfully", false);
//            }
//        }else if(startingPoint.equalsIgnoreCase("Start")){
//            if(isElementLoaded(sliderNotchStart,10) && sliderDroppableCount.size()>0){
//                SimpleUtils.pass("Shift timings with Sliders loaded on page Successfully for End Point");
//                mouseHoverDragandDrop(sliderNotchStart,element);
//            } else{
//                SimpleUtils.fail("Shift timings with Sliders not loaded on page Successfully", false);
//            }
//        }
//    }


//    public void clickOnNextDaySchedule(String activeDay) throws Exception {
//        List<WebElement> activeWeek = MyThreadLocal.getDriver().findElements(By.className("day-week-picker-period"));
//        for(int i=0; i<activeWeek.size();i++){
//            String currentDay = activeWeek.get(i).getText().replace("\n", " ").substring(0,3);
//            if(currentDay.equalsIgnoreCase(activeDay)){
//                if(i== activeWeek.size()-1){
//                    navigateWeekViewOrDayViewToPastOrFuture(ScheduleTestKendraScott2.weekViewType.Next.getValue(),
//                            ScheduleTestKendraScott2.weekCount.One.getValue());
//                    waitForSeconds(3);
//                }else{
//                    click(activeWeek.get(i+1));
//                }
//            }
//        }
//
//    }
//
//    public void selectTeamMembersOptionForSchedule() throws Exception {
//        NewShiftPage newShiftPage = new ConsoleNewShiftPage();
//        if(isElementEnabled(btnSearchTeamMember,5)){
//            click(btnSearchTeamMember);
//            if(isElementLoaded(textSearch,5)) {
//                newShiftPage.searchText(propertySearchTeamMember.get("AssignTeamMember"));
//            }
//        }else{
//            SimpleUtils.fail("Select Team member option not available on page",false);
//        }
//
//    }
//
//    public void selectTeamMembersOptionForScheduleForClopening() throws Exception {
//        NewShiftPage newShiftPage = new ConsoleNewShiftPage();
//        if(isElementEnabled(btnSearchTeamMember,5)){
//            click(btnSearchTeamMember);
//            if(isElementLoaded(textSearch,5)) {
//                if(getTeamMemberName()!=null){
//                    newShiftPage.searchText(getTeamMemberName());
//                }else{
//                    newShiftPage.searchText(propertySearchTeamMember.get("AssignTeamMember"));
//                }
//            }
//        }else{
//            SimpleUtils.fail("Select Team member option not available on page",false);
//        }
//
//    }
//
//    @FindBy(css = ".week-view-shift-info-icon")
//    private List<WebElement> scheduleInfoIcon;
//
//    @FindBy(css = "button[ng-click*='confirmSaveAction']")
//    private WebElement saveOnSaveConfirmationPopup;
//
//    @FindBy(css = "button[ng-click*='okAction']")
//    private WebElement okAfterSaveConfirmationPopup;
//
//    public void clickViewProfile(int i) {
//        if (areListElementVisible(imageSize, 5)) {
//            click(imageSize.get(i));
//            if (isElementEnabled(viewProfile)) {
//                click(viewProfile);
//            }
//        }
//    }

//    public void viewProfile() throws Exception {
//        int counter = 0;
//        if (areListElementVisible(imageSize, 5)) {
//            for (int i = 0; i < imageSize.size(); i++) {
//                if (!workerName.get(i).getText().equalsIgnoreCase("open")) {
//                    clickViewProfile(i);
//                    saveSchedule();
//                    counter = i;
//                    break;
//                }
//            }
//        }
//        if (areListElementVisible(imageSize, 5)) {
//            click(imageSize.get(5));
//            click(viewProfile);
//            WebElement element = getDriver().findElement(By.cssSelector("div.sch-worker-popover.allow-pointer-events.ng-scope"));
//            JavascriptExecutor jse = (JavascriptExecutor) getDriver();
//            String txt = jse.executeScript("return arguments[0].innerHTML;", element).toString();
////		  	 	   		if(viewProfile.size()>0){
////				   		   click(viewProfile.get(1));
////			 	   		}
//
//        }
//    }
//
//    public void verifyOpenShift(String TMName, String workerRole, String shiftDuration, int counter) {
//        click(scheduleInfoIcon.get(counter));
//        String workerRoleOpenShift = workerRoleDetailsFromPopUp.getText();
//        String shiftDurationOpenShift = shiftDurationInPopUp.getText();
//        if (workerRoleOpenShift.equalsIgnoreCase(workerRole) && shiftDurationOpenShift.equalsIgnoreCase(shiftDuration) && workerName.get(counter).getText().equalsIgnoreCase("open")) {
//            SimpleUtils.pass(TMName + "'s " + workerRole + " shift of duration " + shiftDuration + " got converted to open shift successfully");
//        } else {
//            SimpleUtils.fail(TMName + "'s " + workerRole + " shift of duration " + shiftDuration + " was not converted to open shift successfully", false);
//        }
//    }
//
//    public void saveSchedule() throws Exception {
//        if (isElementEnabled(scheduleSaveBtn, 10)) {
//            scrollToTop();
//            waitForSeconds(3);
//            clickTheElement(scheduleSaveBtn);
//        } else {
//            SimpleUtils.fail("Schedule save button not found", false);
//        }
//        if (isElementEnabled(saveOnSaveConfirmationPopup, 3)) {
//            clickTheElement(saveOnSaveConfirmationPopup);
//            waitForSeconds(3);
//            try{
//                if (isElementLoaded(msgOnTop, 20) && msgOnTop.getText().contains("Success")) {
//                    SimpleUtils.pass("Save the Schedule Successfully!");
//                } else if (isElementLoaded(editScheduleButton, 10)) {
//                    SimpleUtils.pass("Save the Schedule Successfully!");
//                } else {
//                    SimpleUtils.fail("Save Schedule Failed!", false);
//                }
//            } catch(StaleElementReferenceException e){
//                SimpleUtils.report("stale element reference: element is not attached to the page document");
//            }
//            waitForSeconds(3);
//        } else {
//            SimpleUtils.fail("Schedule save button not found", false);
//        }
//    }
//
//    public void convertToOpen(int i) {
//        if (areListElementVisible(imageSize, 5)) {
//            click(imageSize.get(i));
//            if (isElementEnabled(convertOpen)) {
//                click(convertOpen);
//                if (isElementEnabled(openPopYesButton,5)) {
//                    click(openPopYesButton);
//                    waitForSeconds(3);
//                } else {
//                    SimpleUtils.fail("Open pop-up Yes button not found", false);
//                }
//            } else {
//                SimpleUtils.fail("Convert to open shift option not found", false);
//            }
//        } else {
//            SimpleUtils.fail("shift images not loaded successfully", false);
//        }
//    }
//
//    public void convertToOpenShift() throws Exception {
//        ScheduleMainPage scheduleMainPage = new ConsoleScheduleMainPage();
//        String TMWorkerRole = null;
//        String shiftDuration = null;
//        String TMName = null;
//        int counter = 0;
//        if (areListElementVisible(imageSize, 5)) {
//            for (int i = 0; i < imageSize.size(); i++) {
//                if (!workerNameList.get(i).getText().equalsIgnoreCase("open")) {
//                    click(profileIcons.get(i));
//                    waitForSeconds(3);
//                    String[] workerRole = workerRoleDetailsFromPopUp.getText().split("as ");
//                    TMName = workerNameInPopUp.getText();
//                    TMWorkerRole = workerRole[1];
//                    shiftDuration = shiftDurationInPopUp.getText();
//                    convertToOpen(i);
//                    scheduleMainPage.saveSchedule();
//                    counter = i;
//                    break;
//                }
//            }
//            verifyOpenShift(TMName, TMWorkerRole, shiftDuration, counter);
//
//        } else {
//            SimpleUtils.fail("shift images not loaded successfully", false);
//        }
//    }
//
//
//
//
//    @FindBy (css= "div[ng-style*='roleChangeStyle'] span")
//    private List<WebElement> changeRoleValues;
//
//    @FindBy (css = "div[ng-click*='changeRoleMoveRight'] i")
//    private WebElement rightarrow;
//
//    @FindBy (css = "button.sch-save")
//    private WebElement applyButtonChangeRole;
//
//    @FindBy (css = "button.sch-cancel")
//    private WebElement cancelButtonChangeRole;
//
//
//    public void verifyChangedRoleShift(String TMName, String workerRole, String shiftDuration, int counter) throws Exception {
////        selectWorkRoleFilterByText("FOOT", true);
//        for(int i=counter;i<imageSize.size(); i+=7){
//            click(scheduleInfoIcon.get(i));
//            String workerName = workerNameInPopUp.getText();
//            String[] workerRoleShift = workerRoleDetailsFromPopUp.getText().split("as ");
//            String shiftDurationShift = shiftDurationInPopUp.getText();
//            if (workerRoleShift[1].equalsIgnoreCase(propertyWorkRole.get("changeWorkRole")) && shiftDurationShift.equalsIgnoreCase(shiftDuration) && workerName.equalsIgnoreCase(TMName)) {
//                SimpleUtils.pass(TMName + "'s shift work role changed from" + workerRole + " to " + workerRoleShift[1] + " for shift duration " + shiftDuration +"  successfully");
//                break;
//            } else {
//                SimpleUtils.fail(TMName + "'s shift work role not got changed from" + workerRole + " to " + workerRoleShift[1] + " for shift duration " + shiftDuration +"  successfully", false);
//            }
//        }
//    }
//
//    public void changeRole(int i) {
//        if (areListElementVisible(imageSize, 5)) {
//            click(imageSize.get(i));
//            if (isElementEnabled(changeRole)) {
//                click(changeRole);
//
//                for (int j = 0; j < changeRoleValues.size(); j++) {
//                    if (changeRoleValues.get(j).getText().equalsIgnoreCase(propertyWorkRole.get("changeWorkRole"))) {
//                        click(changeRoleValues.get(j));
//                        break;
//                    } else {
//                        if (j == changeRoleValues.size() - 1) {
//                            click(rightarrow);
//                            j = 0;
//                        }
//
//                    }
//                }
//                if (isElementEnabled(applyButtonChangeRole)) {
//                    click(applyButtonChangeRole);
//                } else {
//                    SimpleUtils.fail("Apply button on change role flyout not found", false);
//                }
//            }
//        }
//    }

//    public void changeWorkerRole() throws Exception{
//        String TMWorkerRole = null;
//        String shiftDuration = null;
//        String TMName = null;
//        int counter = 0;
//        if (areListElementVisible(imageSize, 5)) {
//            for (int i = 0; i < imageSize.size(); i++) {
//                if (!workerName.get(i).getText().equalsIgnoreCase("open")) {
//                    click(scheduleInfoIcon.get(i));
//                    String[] workerRole = workerRoleDetailsFromPopUp.getText().split("as ");
//                    TMName = workerNameInPopUp.getText();
//                    TMWorkerRole = workerRole[1];
//                    shiftDuration = shiftDurationInPopUp.getText();
//                    changeRole(i);
//                    saveSchedule();
//                    counter = i;
//                    break;
//                }
//            }
//            verifyChangedRoleShift(TMName, TMWorkerRole, shiftDuration,counter );
//
//        }else {
//            SimpleUtils.fail("shift images not loaded successfully", false);
//        }
//    }
//
//    public void assignTM(int i) {
//        if (areListElementVisible(imageSize, 5)) {
//            click(imageSize.get(i));
//            if (isElementEnabled(assignTM)) {
//                click(assignTM);
//            }
//        }
//    }
//

//    public void assignTeamMember() throws Exception{
//        String TMWorkerRole = null;
//        String shiftDuration = null;
//        String TMName = null;
//        int counter = 0;
//        if (areListElementVisible(imageSize, 5)) {
//            for (int i = 0; i < imageSize.size(); i++) {
//                if (!workerName.get(i).getText().equalsIgnoreCase("open")) {
//                    click(scheduleInfoIcon.get(i));
//                    String[] workerRole = workerRoleDetailsFromPopUp.getText().split("as ");
//                    TMName = workerNameInPopUp.getText();
//                    TMWorkerRole = workerRole[1];
//                    shiftDuration = shiftDurationInPopUp.getText();
//                    assignTM(i);
//                    verifySelectTeamMembersOption();
//                    clickOnOfferOrAssignBtn();
//                    saveSchedule();
//                    counter = i;
//                    break;
//                }
//            }
////            verifyChangedRoleShift(TMName, TMWorkerRole, shiftDuration,counter );
//
//        }else {
//            SimpleUtils.fail("shift images not loaded successfully", false);
//        }
//    }
//
//    public void verifyActiveScheduleType() throws Exception{
//        if(isElementLoaded(scheduleType, 5)){
//            if(scheduleType.getText().equalsIgnoreCase("SCHEDULE TYPE")){
//                SimpleUtils.pass("Schedule Type Label is displaying Successfully on page!");
//                getActiveScheduleType();
//            }else{
//                SimpleUtils.fail("Schedule Type Label not displaying on page Successfully!",false);
//            }
//        }
//    }
//
//    public void getActiveScheduleType(){
//        if(isElementEnabled(activScheduleType,5)){
//            if(activScheduleType.getText().equalsIgnoreCase("Manager")){
//                SimpleUtils.pass("Schedule Type " + activScheduleType.getText() + " is enabled on page");
//            }else{
//                SimpleUtils.fail("Schedule Type " + activScheduleType.getText() + " is disabled",false);
//            }
//        }else{
//            SimpleUtils.fail("Schedule Type " + scheduleTypeManager.getText() + " is disabled",false);
//        }
//    }

//    public void switchToManagerView() throws Exception {
//        String activeWeekText = getActiveWeekText();
//        if(isElementEnabled(activScheduleType,5)){
//            if(activScheduleType.getText().equalsIgnoreCase("Suggested")){
//                click(scheduleTypeManager);
//                if(isGenerateButtonLoadedForManagerView()){
//                    click(generateScheduleBtn);
//                    generateScheduleFromCreateNewScheduleWindow(activeWeekText);
//                }else{
//                    SimpleUtils.fail("Generate button not found on page",false);
//                }
//            }else{
//                if(isGenerateButtonLoadedForManagerView()){
//                    click(generateScheduleBtn);
//                    generateScheduleFromCreateNewScheduleWindow(activeWeekText);
//                }else{
//                    SimpleUtils.fail("Generate button not found on page",false);
//                }
//            }
//        }else{
//            SimpleUtils.fail("Schedule Type " + scheduleTypeManager.getText() + " is disabled",false);
//        }
//    }

//    public void generateScheduleFromCreateNewScheduleWindow(String activeWeekText) throws Exception {
//        if(isElementEnabled(copySchedulePopUp,5)){
//            SimpleUtils.pass("Copy From Schedule Window opened for week " + activeWeekText);
//            if(isElementEnabled(btnContinue,2)){
//                JavascriptExecutor jse = (JavascriptExecutor) getDriver();
//                jse.executeScript("arguments[0].click();", btnContinue);
//            }else{
//                SimpleUtils.fail("Continue button was not present on page",false);
//            }
//        }else if (isElementEnabled(publishSheduleButton,5)){
//            SimpleUtils.pass("Copy From Schedule Window not opened for " + activeWeekText +" because there was no past week published schedule" + activeWeekText);
//        }
//        else{
//            SimpleUtils.fail("Copy From Schedule Window not opened for week " + activeWeekText,false);
//        }
//    }
    //added by Nishant for DM Test cases

    @FindBy(css = "div.analytics-new-table-group")
    private List<WebElement> DMtableRowCount;

    @FindBy(xpath = "//div[contains(@class,'analytics-new-table-group-row')]//span/img/following-sibling::span")
    private List<WebElement> locationName;

    @FindBy(xpath = "//div[contains(@class,'analytics-new-table-group-row')]//div[@class='ng-scope col-fx-1']")
    private List<WebElement> DMHours;
//
//    public List<Float> validateScheduleAndBudgetedHours() throws Exception {
//        HashMap<String,List<String>> budgetHours = new HashMap<>();
//        HashMap<String,List<String>> publishHours = new HashMap<>();
//        HashMap<String,List<String>> clockHours = new HashMap<>();
//        List<Float> totalHoursFromSchTbl = new ArrayList<>();
//        List<String> budgetHrs = new ArrayList<>();
//        List<String> publishedHrs = new ArrayList<>();
//        List<String> clockedHrs = new ArrayList<>();
//        int counter = 0;
//        if(areListElementVisible(DMtableRowCount,10) && DMtableRowCount.size()!=0){
//            for(int i=0; i<DMtableRowCount.size();i++){
//                if(areListElementVisible(DMHours,10) && DMHours.size()!=0){
////                    SimpleUtils.report("Budget Hours for " + locationName.get(i).getText() + " is : " + DMHours.get(counter).getText());
////                    SimpleUtils.report("Publish Hours for " + locationName.get(i).getText() + " is : " + DMHours.get(counter+1).getText());
////                    SimpleUtils.report("Clocked Hours for " + locationName.get(i).getText() + " is : " + DMHours.get(counter+2).getText());
//                    budgetHrs.add(DMHours.get(counter).getText());
//                    publishedHrs.add(DMHours.get(counter+1).getText());
//                    clockedHrs.add(DMHours.get(counter+2).getText());
//                    budgetHours.put("Budgeted Hours",budgetHrs);
//                    publishHours.put("Published Hours",publishedHrs);
//                    clockHours.put("Clocked Hours",clockedHrs);
//                    counter = (i + 1) * 3;
//                }
//            }
//            Float totalBudgetHoursFromSchTbl = calculateTotalHoursFromScheduleTable(budgetHours);
//            Float totalPublishedHoursFromSchTbl = calculateTotalHoursFromScheduleTable(publishHours);
//            Float totalClockedHoursFromSchTbl = calculateTotalHoursFromScheduleTable(clockHours);
//            totalHoursFromSchTbl.add(totalBudgetHoursFromSchTbl);
//            totalHoursFromSchTbl.add(totalPublishedHoursFromSchTbl);
//            totalHoursFromSchTbl.add(totalClockedHoursFromSchTbl);
//
//        }else{
//            SimpleUtils.fail("No data available on Schedule table in DM view",false);
//        }
//
//        return totalHoursFromSchTbl;
//    }
//
//
//    public Float calculateTotalHoursFromScheduleTable(HashMap<String,List<String>> hoursCalulationFromSchTbl){
//        Float totalActualHours = 0.0f;
//        Float totalActualHoursFromSchTbl = 0.0f;
//        for (Entry<String, List<String>> entry : hoursCalulationFromSchTbl.entrySet()) {
//            String key = entry.getKey();
//
//            List<String> value = entry.getValue();
//            for(String aString : value){
//                totalActualHours = Float.parseFloat(aString.replace(",",""));
//                totalActualHoursFromSchTbl = totalActualHoursFromSchTbl + totalActualHours;
//            }
//        }
//
//        return totalActualHoursFromSchTbl;
//    }
//
//    @FindBy(css = "span.dms-box-item-number-small")
//    private List<WebElement> hoursOnDashboardPage;
//    @FindBy(css = "div.dms-box-item-title")
//    private List<WebElement> titleOnDashboardPage;
//    @FindBy(xpath = "//*[name()='svg']//*[name()='text' and @text-anchor='middle']")
//    private List<WebElement> locationSummaryOnSchedule;
//    @FindBy(xpath = "//*[name()='svg']//*[name()='text' and @text-anchor='end']")
//    private List<WebElement> projectedOverBudget;
//    @FindBy(xpath = "//*[name()='svg']//*[name()='text' and @text-anchor='start']")
//    private List<WebElement> projectedUnderBudget;
//    @FindBy(css = "span.dms-box-item-unit-trend")
//    private WebElement projectedWithinOrOverBudget;
//    @FindBy(css = "div.dashboard-time div.text-right")
//    private WebElement dateOnDashboard;
//    @FindBy(css = "ng-include[src*=LocationSummary] div.dms-box-title")
//    private WebElement locationsSummaryTitleOnDashboard;
//    @FindBy(css = "div.card-carousel-card-title")
//    private WebElement locationsSummaryTitleOnSchedule;
//    @FindBy(css = "span.dms-box-item-title.dms-box-item-title-color-dark")
//    private List<WebElement> locationsSummarySmartCardOnDashboard;
//    @FindBy(css = "div.day-week-picker-period-active")
//    private WebElement dateOnSchedule;
//    @FindBy(css = "div.published-clocked-cols-summary-title")
//    private List<WebElement> locationsSummarySmartCardOnSchedule;
//
//    //added by Gunjan
//    @FindBy(xpath = "//div[contains(@class,'dms-row1')]//div[contains(text(),'View Schedule')]")
//    private WebElement viewScheduleLinkInlocationsSummarySmartCardDashboard;
//    @FindBy(xpath = "//div[contains(@class,'dms-row2')]//div[contains(text(),'View Schedule')]")
//    private WebElement viewScheduleLinkInPayRollProjectionSmartCardDashboard;
//    @FindBy(css = "div.card-carousel-card.card-carousel-card-primary")
//    private WebElement locationSummarySmartCardOnSchedule;
//    @FindBy(css = "div.analytics-new-table-group-row-open div.analytics-new-table-group-row-action")
//    private List<WebElement> DMtoSMNavigationArrow;
//    @FindBy(css = "lg-select[search-hint='Search District'] input-field[class='picker-input ng-isolate-scope'] div.input-faked")
//    private WebElement selectedDistrictSMView;
//    @FindBy(css="lg-select[search-hint='Search Location']  input-field[class='picker-input ng-isolate-scope']  div.input-faked")
//    private WebElement selectedLocationSMView;
//    @FindBy(css = "[search-hint=\"Search District\"] div.lg-search-options")
//    private WebElement districtDropDownButton;
//    @FindBy(css = "div.lg-search-options__option")
//    private List<WebElement> availableLocationCardsName;
//
//
//    public void districtSelectionSMView(String districtName) throws Exception {
//        waitForSeconds(4);
//        try {
//            Boolean isDistrictMatched = false;
//            if (isElementLoaded(selectedDistrictSMView)) {
//                click(selectedDistrictSMView);
//                if (isElementLoaded(districtDropDownButton)) {
//                    if (availableLocationCardsName.size() != 0) {
//                        for (WebElement locationCardName : availableLocationCardsName) {
//                            if (locationCardName.getText().contains(districtName)) {
//                                isDistrictMatched = true;
//                                click(locationCardName);
//                                SimpleUtils.pass("District '" + districtName + " selected successfully");
//                                break;
//                            }
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    // Added by Nora
//    @FindBy (css = "lg-button-group[buttons*=\"custom\"] div.lg-button-group-first")
//    private WebElement scheduleDayViewButton;
//    @FindBy (className = "period-name")
//    private WebElement periodName;
//    @FindBy (className = "card-carousel-container")
//    private WebElement cardCarousel;
//    @FindBy(css = "table tr:nth-child(1)")
//    private WebElement scheduleTableTitle;
//    @FindBy(css = "table tr:nth-child(2)")
//    private WebElement scheduleTableHours;
//    @FindBy(className = "week-day-multi-picker-day")
//    private List<WebElement> weekDays;
//    @FindBy(css = "[ng-show=\"hasSearchResults()\"] [ng-repeat=\"worker in searchResults\"]")
//    private List<WebElement> searchResults;
////    @FindBy(css = "img[src*=\"added-shift\"]")
////    private List<WebElement> addedShiftIcons;
//    @FindBy(css = "[label=\"Create New Shift\"]")
//    private WebElement createNewShiftWeekView;
//    @FindBy(className = "week-schedule-shift-wrapper")
//    private List<WebElement> shiftsWeekView;
//    @FindBy(css = "div.popover div:nth-child(3)>div.ng-binding")
//    private WebElement timeDuration;
//    @FindBy(className = "sch-calendar-day-label")
//    private List<WebElement> weekDayLabels;
//    @FindBy(className = "week-schedule-shift")
//    private List<WebElement> weekShifts;
////    @FindBy(css = ".schedule-summary-search-dropdown [icon*=\"search.svg'\"]")
////    private WebElement searchLocationBtn;
//    @FindBy(css = ".redesigned-modal-icon")
//    private WebElement deleteScheduleIcon;
//    @FindBy(css = ".redesigned-modal-title")
//    private WebElement deleteScheduleTitle;
//    @FindBy(css = ".redesigned-modal-text")
//    private WebElement deleteScheduleText;
//    @FindBy(css = "[label*=\"Delete Schedule\"]")
//    private WebElement deleteScheduleWeek;
//
//    @Override
//    public void verifyClickOnCancelBtnOnDeleteScheduleDialog() throws Exception {
//        try {
//            if (isElementLoaded(cancelButtonOnDeleteSchedulePopup, 5)) {
//                clickTheElement(cancelButtonOnDeleteSchedulePopup);
//                waitForSeconds(2);
//                if (!isElementLoaded(deleteSchedulePopup, 5)) {
//                    SimpleUtils.pass("Delete Schedule Dialog: Click on Cancel button successfully!");
//                } else {
//                    SimpleUtils.fail("Delete Schedule Dialog: Click on Cancel button failed!", false);
//                }
//            } else {
//                SimpleUtils.fail("Delete Schedule Dialog: Cancel button is not loaded successfully!", false);
//            }
//        } catch (Exception e) {
//            SimpleUtils.fail(e.getMessage(), false);
//        }
//    }
//
//    @Override
//    public void verifyDeleteBtnDisabledOnDeleteScheduleDialog() throws Exception {
//        try {
//            if (isElementLoaded(deleteButtonOnDeleteSchedulePopup, 5) &&
//                    deleteButtonOnDeleteSchedulePopup.getAttribute("disabled").equalsIgnoreCase("true")) {
//                SimpleUtils.pass("Delete Schedule Dialog: Delete button is disabled by default!");
//            } else {
//                SimpleUtils.fail("Delete Schedule Dialog: Delete button is not disabled by default!", false);
//            }
//        } catch (Exception e) {
//            SimpleUtils.fail(e.getMessage(), false);
//        }
//    }
//
//    @Override
//    public void verifyDeleteButtonEnabledWhenClickingCheckbox() throws Exception {
//        try {
//            if (isElementLoaded(deleteScheduleCheckBox, 5)) {
//                clickTheElement(deleteScheduleCheckBox);
//                if (isElementLoaded(deleteButtonOnDeleteSchedulePopup, 5) && deleteButtonOnDeleteSchedulePopup.getAttribute("disabled") == null) {
//                    SimpleUtils.pass("Delete Schedule Dialog: Delete Button is enabled when clicking the checkbox!");
//                } else {
//                    SimpleUtils.fail("Delete Schedule Dialog: Delete Button is not enabled when clicking the checkbox!", false);
//                }
//            } else {
//                SimpleUtils.fail("Delete Schedule Dialog: Check box is not loaded successfully!", false);
//            }
//        } catch (Exception e) {
//            SimpleUtils.fail(e.getMessage(), false);
//        }
//    }
//
//    @Override
//    public String getDeleteScheduleForWhichWeekText() throws Exception {
//        String scheduleWeekText = "";
//        if (isElementLoaded(weekPeriod, 5) && isElementLoaded(calMonthYear, 5)) {
//            String year = calMonthYear.getText().trim().substring(calMonthYear.getText().trim().length() - 4);
//            String [] items = weekPeriod.getText().split(" ");
//            if (items.length == 7) {
//                scheduleWeekText = "Delete " + items[0] + " " + items[1] + " " + items[2].substring(0, 3) + " " + (items[3].length() == 2 ? items[3] : ("0" + items[3]))
//                        + " " + items[4] + " " + items[5].substring(0, 3) + " " + (items[6].length() == 2 ? items[6] : ("0" + items[6])) + ", " + year;
//                SimpleUtils.report("Delete Schedule For Which Weeek Text: " + scheduleWeekText);
//            }
//        }
//        if (scheduleWeekText == "") {
//            SimpleUtils.fail("Failed to get the delete schedule for which week Text", false);
//        }
//        return scheduleWeekText;
//    }
//
//    @Override
//    public void verifyTheContentOnDeleteScheduleDialog(String confirmMessage, String week) throws Exception {
//        if (isElementLoaded(deleteSchedulePopup, 20)) {
//            if (isElementLoaded(deleteScheduleIcon, 5) && isElementLoaded(deleteScheduleTitle, 5)
//                    && deleteScheduleTitle.getText().equalsIgnoreCase("Delete Schedule") && isElementLoaded(deleteScheduleTitle, 5)
//                    && deleteScheduleText.getText().equalsIgnoreCase(confirmMessage) && isElementLoaded(deleteScheduleWeek, 5)
//                    && deleteScheduleWeek.getText().toLowerCase().contains(week.toLowerCase()) && isElementLoaded(cancelButtonOnDeleteSchedulePopup, 5)
//                    && isElementLoaded(deleteButtonOnDeleteSchedulePopup, 5) && isElementLoaded(deleteScheduleCheckBox, 5)) {
//                SimpleUtils.pass("Delete Schedule Dialog: Verified the content is correct!");
//            } else {
//                SimpleUtils.fail("Delete Schedule Dialog: The content is unexpected!", false);
//            }
//        } else {
//            SimpleUtils.fail("Delete Schedule Dialog failed to pop up!", false);
//        }
//    }
//
//    @Override
//    public void verifyClickOnDeleteScheduleButton() throws Exception {
//        try {
//            if (isElementLoaded(deleteScheduleButton, 10)) {
//                clickTheElement(deleteScheduleButton);
//                SimpleUtils.pass("Schedule: Click on Delete Schedule button Successfully!");
//            } else {
//                SimpleUtils.fail("Schedule: Delete Schedule button is not loaded Successfully!", false);
//            }
//        } catch (Exception e) {
//            SimpleUtils.fail(e.getMessage(), false);
//        }
//    }
//
//    @Override
//    public boolean isDeleteScheduleButtonLoaded() throws Exception {
//        try {
//            if (isElementLoaded(deleteScheduleButton, 5)) {
//                return true;
//            } else {
//                return false;
//            }
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    @Override
//    public void verifyUngenerateButtonIsRemoved() throws Exception {
//        String unGenerateScheduleOptionText = "Ungenerate Schedule";
//        boolean isRemoved = true;
//        try {
//            if (isElementLoaded(scheduleAdminDropDownBtn, 5)) {
//                click(scheduleAdminDropDownBtn);
//                if (scheduleAdminDropDownOptions.size() > 0) {
//                    for (WebElement scheduleAdminDropDownOption : scheduleAdminDropDownOptions) {
//                        if (scheduleAdminDropDownOption.getText().toLowerCase().contains(unGenerateScheduleOptionText.toLowerCase())) {
//                            isRemoved = false;
//                            break;
//                        }
//                    }
//                } else {
//                    isRemoved = false;
//                }
//            } else {
//                isRemoved = false;
//            }
//            if (isRemoved) {
//                SimpleUtils.pass("Schedule page: Ungenerate Schedule option is removed!");
//            } else {
//                SimpleUtils.fail("Schedule page: Ungenerate Schedule option still shows!", false);
//            }
//        } catch (Exception e) {
//            SimpleUtils.fail(e.getMessage(), false);
//        }
//    }
//
//    @Override
//    public boolean isLocationGroup() {
//        try {
//            if (isElementLoaded(searchLocationBtn, 10)) {
//                return true;
//            } else {
//                return false;
//            }
//        } catch (Exception e) {
//            return false;
//        }
//    }

//    @Override
//    public int getTheIndexOfCurrentDayInDayView() throws Exception {
//        int index = 7;
//        if (areListElementVisible(dayPickerAllDaysInDayView, 10)) {
//            for (int i = 0; i < dayPickerAllDaysInDayView.size(); i++) {
//                if (dayPickerAllDaysInDayView.get(i).getAttribute("class").contains("day-week-picker-period-active")) {
//                    index = i;
//                    SimpleUtils.pass("Schedule Day view: Get the current day index: " + index);
//                }
//            }
//        }
//        if (index == 7) {
//            SimpleUtils.fail("Schedule Day view: Failed to get the index of CurrentDay", false);
//        }
//        return index;
//    }


//    @Override
//    public List<String> getTheShiftInfoByIndex(int index) throws Exception {
//        waitForSeconds(3);
//        List<String> shiftInfo = new ArrayList<>();
//        if (areListElementVisible(weekShifts, 20) && index < weekShifts.size()) {
//            String firstName = weekShifts.get(index).findElement(By.className("week-schedule-worker-name")).getText();
//            if (!firstName.equalsIgnoreCase("Open") && !firstName.equalsIgnoreCase("Unassigned")) {
//                String dayIndex = weekShifts.get(index).getAttribute("data-day-index");
//                String lastName = getTMDetailNameFromProfilePage(weekShifts.get(index)).split(" ")[1].trim();
//                waitForSeconds(2);
//                String jobTitle = weekShifts.get(index).findElement(By.cssSelector(".rows .week-schedule-role-name")).getText();
//                String shiftTimeWeekView = weekShifts.get(index).findElement(By.className("week-schedule-shift-time")).getText();
//                WebElement infoIcon = weekShifts.get(index).findElement(By.className("week-schedule-shit-open-popover"));
//                clickTheElement(infoIcon);
//                String workRole = shiftJobTitleAsWorkRole.getText().split("as")[1].trim();
//                if (isElementLoaded(shiftDuration, 10)) {
//                    String shiftTime = shiftDuration.getText();
//                    shiftInfo.add(firstName);
//                    shiftInfo.add(dayIndex);
//                    shiftInfo.add(shiftTime);
//                    shiftInfo.add(jobTitle);
//                    shiftInfo.add(workRole);
//                    shiftInfo.add(lastName);
//                    shiftInfo.add(shiftTimeWeekView);
//                }
//                //To close the info popup
//                clickTheElement(weekShifts.get(index));
//            } else {
//                //SimpleUtils.report("This is an Open Shift");
//                //return shiftInfo;
//                //For open shift
//                String dayIndex = weekShifts.get(index).getAttribute("data-day-index");
//                String lastName = "";
//                if (firstName.equalsIgnoreCase("Unassigned")){
//                    lastName = "unassigned";
//                } else
//                    lastName = "open";
//                String jobTitle = "";
//                String shiftTimeWeekView = weekShifts.get(index).findElement(By.className("week-schedule-shift-time")).getText();
//                WebElement infoIcon = weekShifts.get(index).findElement(By.className("week-schedule-shit-open-popover"));
//                clickTheElement(infoIcon);
//                String workRole = shiftJobTitleAsWorkRole.getText().trim();
//                if (isElementLoaded(shiftDuration, 10)) {
//                    String shiftTime = shiftDuration.getText();
//                    shiftInfo.add(firstName);
//                    shiftInfo.add(dayIndex);
//                    shiftInfo.add(shiftTime);
//                    shiftInfo.add(jobTitle);
//                    shiftInfo.add(workRole);
//                    shiftInfo.add(lastName);
//                    shiftInfo.add(shiftTimeWeekView);
//                }
//                //To close the info popup
//                clickTheElement(weekShifts.get(index));
//            }
//        } else {
//            SimpleUtils.fail("Schedule Page: week shifts not loaded successfully!", false);
//        }
//        if (shiftInfo.size() != 7) {
//            SimpleUtils.fail("Failed to get the shift info!", false);
//        }
//        return shiftInfo;
//    }

//    @Override
//    public String getRandomWorkRole() throws Exception {
//        List<String> shiftInfo = new ArrayList<>();
//        while (shiftInfo.size() == 0) {
//            shiftInfo = getTheShiftInfoByIndex(0);
//        }
//        return shiftInfo.get(4);
//    }

//    @Override
//    public List<String> getTheShiftInfoInDayViewByIndex(int index) throws Exception {
//        List<String> shiftInfo = new ArrayList<>();
//        if (areListElementVisible(dayViewAvailableShifts, 20) && index < dayViewAvailableShifts.size()) {
//            String firstName = dayViewAvailableShifts.get(index).
//                    findElement(By.className("sch-day-view-shift-worker-name")).getText().split(" ")[0];
//            if (!firstName.equalsIgnoreCase("Open")) {
//                String lastName = getTMDetailNameFromProfilePage(dayViewAvailableShifts.get(index)).split(" ")[1].trim();
//                String shiftTimeWeekView = dayViewAvailableShifts.get(index).findElement(By.className("sch-day-view-shift-time")).getText();
//                WebElement infoIcon = dayViewAvailableShifts.get(index).findElement(By.className("day-view-shift-hover-info-icon"));
//                clickTheElement(infoIcon);
//                String workRole = shiftJobTitleAsWorkRole.getText().split("as")[1].trim();
//                String jobTitle = shiftJobTitleAsWorkRole.getText().split("as")[0].trim();
//                if (isElementLoaded(shiftDuration, 10)) {
//                    String shiftTime = shiftDuration.getText();
//                    shiftInfo.add(firstName);
//                    shiftInfo.add(String.valueOf(getTheIndexOfCurrentDayInDayView()));
//                    shiftInfo.add(shiftTime);
//                    shiftInfo.add(jobTitle);
//                    shiftInfo.add(workRole);
//                    shiftInfo.add(lastName);
//                    shiftInfo.add(shiftTimeWeekView);
//                }
//                //To close the info popup
//                clickTheElement(dayViewAvailableShifts.get(index));
//            } else {
//                //SimpleUtils.report("This is an Open Shift");
//                //return shiftInfo;
//                //For open shift
//                //String dayIndex = weekShifts.get(index).getAttribute("data-day-index");
//                String lastName = "";
//                if (firstName.equalsIgnoreCase("Unassigned")){
//                    lastName = "unassigned";
//                } else
//                    lastName = "open";
//                String jobTitle = "";
//                String shiftTimeWeekView = dayViewAvailableShifts.get(index).findElement(By.className("sch-day-view-shift-time")).getText();
//                WebElement infoIcon = dayViewAvailableShifts.get(index).findElement(By.className("day-view-shift-hover-info-icon"));
//                clickTheElement(infoIcon);
//                String workRole = shiftJobTitleAsWorkRole.getText().trim();
//                if (isElementLoaded(shiftDuration, 10)) {
//                    String shiftTime = shiftDuration.getText();
//                    shiftInfo.add(firstName);
//                    //shiftInfo.add(dayIndex);
//                    shiftInfo.add(String.valueOf(getTheIndexOfCurrentDayInDayView()));
//                    shiftInfo.add(shiftTime);
//                    shiftInfo.add(jobTitle);
//                    shiftInfo.add(workRole);
//                    shiftInfo.add(lastName);
//                    shiftInfo.add(shiftTimeWeekView);
//                }
//                //To close the info popup
//                clickTheElement(dayViewAvailableShifts.get(index));
//            }
//        } else {
//            SimpleUtils.fail("Schedule Page: week shifts not loaded successfully!", false);
//        }
//        if (shiftInfo.size() != 7) {
//            SimpleUtils.fail("Failed to get the shift info!", false);
//        }
//        return shiftInfo;
//    }

//    public String getTMDetailNameFromProfilePage(WebElement shift) throws Exception {
//        String tmDetailName = null;
//        if (isScheduleDayViewActive()) {
//            clickTheElement(shift.findElement(By.cssSelector(".sch-day-view-shift .sch-shift-worker-img-cursor")));
//        } else {
//            clickTheElement(shift.findElement(By.cssSelector(".rows .worker-image-optimized img")));
//        }
//
//        clickOnViewProfile();
//        if (isElementEnabled(tmpProfileContainer, 15)) {
//            if (isElementEnabled(personalDetailsName, 5)) {
//                tmDetailName = personalDetailsName.getText();
//            } else
//                SimpleUtils.fail("TM detail name fail to load!", false);
//        } else
//            SimpleUtils.fail("Profile page fail to load!", false);
//        clickTheElement(closeViewProfileContainer);
//        return tmDetailName;
//    }

//    @Override
//    public void unGenerateActiveScheduleFromCurrentWeekOnward(int loopCount) throws Exception {
//        if (areListElementVisible(currentWeeks, 10)) {
//            for (int i = 0; i < currentWeeks.size(); i++) {
//                // Current week is at the center by default, since we don't need to ungenerate the schedule for previous week
//                if (loopCount == 0) {
//                    if (i == 0) {
//                        continue;
//                    }
//                }
//                click(currentWeeks.get(i));
//                CreateSchedulePage createSchedulePage = new ConsoleCreateSchedulePage();
//                if (createSchedulePage.isWeekGenerated()) {
//                    unGenerateActiveScheduleScheduleWeek();
//                }
//                if (i == (currentWeeks.size() - 1) && isElementLoaded(calendarNavigationNextWeekArrow, 5)) {
//                    click(calendarNavigationNextWeekArrow);
//                    loopCount += 1;
//                    unGenerateActiveScheduleFromCurrentWeekOnward(loopCount);
//                }
//            }
//        }else {
//            SimpleUtils.fail("Current Weeks' elements not loaded Successfully!", false);
//        }
//    }

//    @Override
//    public void selectWorkingDaysOnNewShiftPageByIndex(int index) throws Exception {
//        clearAllSelectedDays();
//        if (areListElementVisible(weekDays, 5) && weekDays.size() == 7) {
//            if (index < weekDays.size()) {
//                if (!weekDays.get(index).getAttribute("class").contains("week-day-multi-picker-day-selected")) {
//                    click(weekDays.get(index));
//                    SimpleUtils.report("Select day: " + weekDays.get(index).getText() + " Successfully!");
//                }
//            }else {
//                SimpleUtils.fail("There is index that out of range: " + index + ", the max value is 6!", false);
//            }
//        }else{
//            SimpleUtils.fail("Weeks Days failed to load!", true);
//        }
//    }

//    @Override
//    public void addNewShiftsByNames(List<String> names, String workRole) throws Exception {
//        for(int i = 0; i < names.size(); i++) {
//            clickOnDayViewAddNewShiftButton();
//            customizeNewShiftPage();
//            if(areListElementVisible(listLocationGroup, 10)){
//                List<String> locations = getAllLocationGroupLocationsFromCreateShiftWindow();
//                selectChildLocInCreateShiftWindow(locations.get((new Random()).nextInt(locations.size()-1)+1));
//                moveSliderAtSomePoint("40", 0, ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
//                moveSliderAtSomePoint("20", 0, ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
//                selectWorkRole(workRole);
//            } else
//                selectWorkRole(workRole);
//            clearAllSelectedDays();
//            if (i == 0) {
//                selectDaysByIndex(2, 4, 6);
//            }else {
//                selectDaysByIndex(1, 3, 5);
//            }
//            clickRadioBtnStaffingOption(staffingOption.AssignTeamMemberShift.getValue());
//            clickOnCreateOrNextBtn();
//            if(isElementLoaded(btnAssignAnyway,5))
//                click(btnAssignAnyway);
//            searchTeamMemberByName(names.get(i));
//            if(isElementLoaded(btnAssignAnyway,5))
//                click(btnAssignAnyway);
//            clickOnOfferOrAssignBtn();
//        }
//    }

//    public void clearAllSelectedDays() throws Exception {
//        if (areListElementVisible(weekDays, 5) && weekDays.size() == 7) {
//            for (WebElement weekDay : weekDays) {
//                if (weekDay.getAttribute("class").contains("week-day-multi-picker-day-selected")) {
//                    click(weekDay);
//                }
//            }
//        }else{
//            SimpleUtils.fail("Weeks Days failed to load!", true);
//        }
//    }
//
//    public void selectDaysByIndex(int index1, int index2, int index3) throws Exception {
//        if (areListElementVisible(weekDays, 5) && weekDays.size() == 7) {
//            if (index1 < weekDays.size() && index2 < weekDays.size() && index3 < weekDays.size()) {
//                if (!weekDays.get(index1).getAttribute("class").contains("week-day-multi-picker-day-selected")) {
//                    click(weekDays.get(index1));
//                    SimpleUtils.report("Select day: " + weekDays.get(index1).getText() + " Successfully!");
//                }
//                if (!weekDays.get(index2).getAttribute("class").contains("week-day-multi-picker-day-selected")) {
//                    click(weekDays.get(index2));
//                    SimpleUtils.report("Select day: " + weekDays.get(index2).getText() + " Successfully!");
//                }
//                if (!weekDays.get(index3).getAttribute("class").contains("week-day-multi-picker-day-selected")) {
//                    click(weekDays.get(index3));
//                    SimpleUtils.report("Select day: " + weekDays.get(index3).getText() + " Successfully!");
//                }
//            }else {
//                SimpleUtils.fail("There is index that out of range: " + index1 + ", " + index2 + ", " + index3 + ", the max value is 6!", false);
//            }
//        }else{
//            SimpleUtils.fail("Weeks Days failed to load!", true);
//        }
//    }

//    @Override
//    public void verifyShiftsAreSwapped(List<String> swapData) throws Exception {
//        int swapRequestIndex1 = -1;
//        int swapRequestIndex2 = -1;
//        String[] swapData1 = swapData.get(0).split("\n");
//        String[] swapData2 = swapData.get(1).split("\n");
//        if (areListElementVisible(weekDayLabels, 10)) {
//            for (int i = 0; i < weekDayLabels.size(); i++) {
//                if (weekDayLabels.get(i).getText().equalsIgnoreCase(swapData1[3].substring(0, 3))) {
//                    swapRequestIndex1 = i;
//                    SimpleUtils.pass("Get the index of " + swapData1[3] + ", the index is: " + i);
//                }
//                if (weekDayLabels.get(i).getText().equalsIgnoreCase(swapData2[3].substring(0, 3))) {
//                    swapRequestIndex2 = i;
//                    SimpleUtils.pass("Get the index of " + swapData2[3] + ", the index is: " + i);
//                }
//            }
//
//            List<WebElement> workerNames1 = getDriver().findElements(By.cssSelector("[data-day-index=\"" + swapRequestIndex1 + "\"] .week-schedule-shift-wrapper .week-schedule-worker-name"));
//            List<WebElement> workerNames2 = getDriver().findElements(By.cssSelector("[data-day-index=\"" + swapRequestIndex2 + "\"] .week-schedule-shift-wrapper .week-schedule-worker-name"));
//            for (WebElement workerName1 : workerNames1) {
//                if (workerName1.getText().equals(swapData1[0])) {
//                    SimpleUtils.fail("Swap failed, still can find the swap Name: " + swapData1[0] + " at: " + swapData1[3], false);
//                }
//                if (workerName1.getText().equals(swapData2[0])) {
//                    SimpleUtils.pass("Swap Successfully, can find the swap Name: " + swapData2[0] + " at: " + swapData1[3]);
//                }
//            }
//            for (WebElement workerName2 : workerNames2) {
//                if (workerName2.getText().equals(swapData2[0])) {
//                    SimpleUtils.fail("Swap failed, still can find the swap Name: " + swapData2[0] + " at: " + swapData2[3], false);
//                }
//                if (workerName2.getText().equals(swapData1[0])) {
//                    SimpleUtils.pass("Swap Successfully, can find the swap Name: " + swapData1[0] + " at: " + swapData2[3]);
//                }
//            }
//        }else {
//            SimpleUtils.fail("Week Day Labels not loaded Successfully!", false);
//        }
//    }

//    @Override
//    public void verifyShiftsChangeToOpenAfterTerminating(List<Integer> indexes, String name, String currentTime) throws Exception {
//        String open = "Open";
//        String unAssigned = "Unassigned";
//        String shiftTime = null;
//        if (indexes.size() > 0 && areListElementVisible(shiftsWeekView, 5)) {
//            for (int index : indexes) {
//                WebElement workerName = shiftsWeekView.get(index).findElement(By.className("week-schedule-worker-name"));
//                if (workerName != null) {
//                    if (workerName.getText().contains(name)) {
//                        shiftTime = shiftsWeekView.get(index).findElement(By.className("week-schedule-shift-time")).getText();
//                        boolean isConvertToOpen = compareShiftTimeWithCurrentTime(shiftTime, currentTime);
//                        SimpleUtils.report("IsConvertToOpen: " + isConvertToOpen + " index is: " + index);
//                        if (!isConvertToOpen) {
//                            SimpleUtils.pass("Shift isn't change to open or unassigned since the current is earlier than the shift end time!");
//                        }else {
//                            SimpleUtils.fail("Shift doesn't change to open or unassigned, worker name is: " + workerName.getText(), true);
//                        }
//                    }else if (workerName.getText().equalsIgnoreCase(open) || workerName.getText().equalsIgnoreCase(unAssigned)) {
//                        SimpleUtils.report("Index is: " + index);
//                        SimpleUtils.pass("Shift is changed to open or unassigned!");
//                    }else {
//                        SimpleUtils.fail("Shift doesn't change to open or unassigned, worker name is: " + workerName.getText(), true);
//                    }
//                }else {
//                    SimpleUtils.fail("Failed to find the worker name element!", true);
//                }
//            }
//        }else {
//            SimpleUtils.fail("Shifts on week view failed to load!", false);
//        }
//    }
//
//    public boolean compareShiftTimeWithCurrentTime(String shiftTime, String currentTime) {
//        boolean isConvertToOpen = false;
//        int shiftStartMinutes = 0;
//        int currentMinutes = 0;
//        String[] startAndEndTime = shiftTime.split("-");
//        if (startAndEndTime.length == 2) {
//            String startTime = startAndEndTime[0].trim();
//            shiftStartMinutes = getMinutesFromTime(startTime);
//            currentMinutes = getMinutesFromTime(currentTime);
//            SimpleUtils.report(startTime);
//            SimpleUtils.report("Convert start time to Minute: " + shiftStartMinutes);
//            SimpleUtils.report(currentTime);
//            SimpleUtils.report("Convert current time to Minute: " + currentMinutes);
//        }
//        if (currentMinutes < shiftStartMinutes) {
//            isConvertToOpen = true;
//        }
//        return isConvertToOpen;
//    }

//    public int getMinutesFromTime(String time) {
//        int minutes = 0;
//        if (time.contains(":")) {
//            String minute = time.split(":")[1].substring(0, time.split(":")[1].length()-2).trim();
//            minutes = (Integer.parseInt(time.split(":")[0].trim())) * 60 + Integer.parseInt(minute);
//        }else {
//            minutes = Integer.parseInt(time.substring(0, time.length()-2)) * 60;
//        }
//        if (time.toLowerCase().endsWith("pm")) {
//            minutes += 12 * 60;
//        }
//        return minutes;
//    }

//    @Override
//    public void isScheduleForCurrentDayInDayView(String dateFromDashboard) throws Exception {
//        String tagName = "span";
//        if (isElementLoaded(scheduleDayViewButton, 5) && isElementLoaded(periodName, 5)) {
//            if (scheduleDayViewButton.getAttribute("class").contains("lg-button-group-selected")){
//                SimpleUtils.pass("The Schedule Day View Button is selected!");
//            }else{
//                SimpleUtils.fail("The Schedule Day View Button isn't selected!", true);
//            }
//            /*
//             * @periodName format "Schedule for Wednesday, February 12"
//             */
//            if (periodName.getText().contains(dateFromDashboard)) {
//                SimpleUtils.pass("The Schedule is for current day!");
//            }else{
//                SimpleUtils.fail("The Schedule isn't for current day!", true);
//            }
//        }else{
//            SimpleUtils.fail("The Schedule Day View Button isn't loaded!",true);
//        }
//    }

//    @Override
//    public HashMap<String, String> getHoursFromSchedulePage() throws Exception {
//        // Wait for the hours to load
//        waitForSeconds(5);
//        HashMap<String, String> scheduleHours = new HashMap<>();
//        if (isElementLoaded(scheduleTableTitle, 5) && isElementLoaded(scheduleTableHours, 5)) {
//            List<WebElement> titles = scheduleTableTitle.findElements(By.tagName("th"));
//            List<WebElement> hours = scheduleTableHours.findElements(By.tagName("td"));
//            if (titles != null && hours != null) {
//                if (titles.size() == 4 && hours.size() == 4) {
//                    for (int i = 1; i < titles.size(); i++) {
//                        scheduleHours.put(titles.get(i).getText(), hours.get(i).getText());
//                        SimpleUtils.pass("Get Title: " + titles.get(i).getText() + " and related Hours: " +
//                                hours.get(i).getText());
//                    }
//                }
//            } else {
//                SimpleUtils.fail("Failed to find the Schedule table elementes!", true);
//            }
//        }else {
//            SimpleUtils.fail("Elements are not Loaded!", true);
//        }
//        return scheduleHours;
//    }

//    @Override
//    public HashMap<String, String> getFourUpComingShifts(boolean isStartTomorrow, String currentTime) throws Exception {
//        HashMap<String, String> upComingShifts = new HashMap<>();
//        String activeDay = null;
//        ScheduleCommonPage scheduleCommonPage = new ConsoleScheduleCommonPage();
//        if (isStartTomorrow) {
//            activeDay = getActiveAndNextDay();
//            scheduleCommonPage.clickOnNextDaySchedule(activeDay);
//            upComingShifts = getAvailableShiftsForDayView(upComingShifts);
//        }else {
//            upComingShifts = getShiftsForCurrentDayIfStartingSoon(upComingShifts, currentTime);
//        }
//        while (upComingShifts.size() < 4) {
//            activeDay = getActiveAndNextDay();
//            scheduleCommonPage.clickOnNextDaySchedule(activeDay);
//            upComingShifts = getAvailableShiftsForDayView(upComingShifts);
//        }
//        if (upComingShifts.size() >= 4) {
//            SimpleUtils.pass("Get four shifts successfully!");
//        }else {
//            SimpleUtils.fail("Failed to get at least four shifts!", false);
//        }
//        return upComingShifts;
//    }

//    @Override
//    public void selectDaysFromCurrentDay(String currentDay) throws Exception {
//        int index = 7;
//        String[] items = currentDay.split(" ");
//        if (items.length == 3) {
//            currentDay = items[0].substring(0, 3) + items[1].substring(0, 3) + (items[2].length() == 1 ? "0"+items[2] : items[2]);
//        }
//        if (areListElementVisible(weekDays, 5) && weekDays.size() == 7) {
//            for (int i = 0; i < weekDays.size(); i++) {
//                String weekDay = weekDays.get(i).getText().replaceAll("\\s*", "");
//                if (weekDay.equalsIgnoreCase(currentDay)) {
//                    index = i;
//                }
//                if (i >= index) {
//                    if (!weekDays.get(i).getAttribute("class").contains("week-day-multi-picker-day-selected")) {
//                        click(weekDays.get(i));
//                        SimpleUtils.pass("Select the day: " + weekDays.get(i).getText() + " successfully!");
//                    }
//                }
//            }
//        }else{
//            SimpleUtils.fail("Weeks Days failed to load!", true);
//        }
//    }

//    @Override
//    public void verifyScheduledWarningWhenAssigning(String userName, String shiftTime) throws Exception {
//        String scheduled = "Scheduled";
//        boolean isWarningShown = false;
//        if (isElementLoaded(textSearch, 15) && isElementLoaded(searchIcon, 15)) {
//            textSearch.sendKeys(userName);
//            clickTheElement(searchIcon);
//            if (areListElementVisible(searchResults, 15)) {
//                for (WebElement searchResult : searchResults) {
//                    WebElement workerName = searchResult.findElement(By.className("worker-edit-search-worker-display-name"));
//                    WebElement status = searchResult.findElement(By.className("worker-edit-availability-status"));
//                    if (workerName != null && optionCircle != null && workerName.getText().toLowerCase().trim().contains(userName.trim().toLowerCase())) {
//                        if (status.getText().contains(scheduled) && status.getText().contains(shiftTime)) {
//                            SimpleUtils.pass("Assign TM Warning: " + status.getText() + " shows correctly!");
//                            isWarningShown = true;
//                            break;
//                        }
//                    }
//                }
//            }
//        }
//        if (!isWarningShown) {
//            SimpleUtils.fail("Assign TM Warning: Expected warning \"" + scheduled + " " + shiftTime + "\" not show!", false);
//        }
//    }

//    @Override
//    public void searchTeamMemberByName(String name) throws Exception {
//        if(areListElementVisible(btnSearchteamMember,15)) {
//            if (btnSearchteamMember.size() == 2) {
//                //click(btnSearchteamMember.get(1));
//                if (isElementLoaded(textSearch, 5) && isElementLoaded(searchIcon, 5)) {
//                    textSearch.clear();
//                    textSearch.sendKeys(name);
//                    click(searchIcon);
//                    if (areListElementVisible(searchResults, 30)) {
//                        for (WebElement searchResult : searchResults) {
//                            WebElement workerName = searchResult.findElement(By.className("worker-edit-search-worker-name"));
//                            WebElement optionCircle = searchResult.findElement(By.className("tma-staffing-option-outer-circle"));
//                            if (workerName != null && optionCircle != null) {
//                                if (workerName.getText().toLowerCase().trim().replaceAll("\n"," ").contains(name.trim().toLowerCase())) {
//                                    clickTheElement(optionCircle);
//                                    SimpleUtils.report("Select Team Member: " + name + " Successfully!");
//                                    waitForSeconds(2);
//                                    if (isElementLoaded(btnAssignAnyway, 5) && btnAssignAnyway.getText().toLowerCase().equalsIgnoreCase("assign anyway")) {
//                                        clickTheElement(btnAssignAnyway);
//                                        SimpleUtils.report("Assign Team Member: Click on 'ASSIGN ANYWAY' button Successfully!");
//                                    }
//                                    break;
//                                }
//                            }else {
//                                SimpleUtils.fail("Worker name or option circle not loaded Successfully!", false);
//                            }
//                        }
//                    }else {
//                        SimpleUtils.fail("Failed to find the team member!", false);
//                    }
//                }else {
//                    SimpleUtils.fail("Search text not editable and icon are not clickable", false);
//                }
//            }else {
//                SimpleUtils.fail("Search team member should have two tabs, failed to load!", false);
//            }
//        }
//    }

//    @Override
//    public void searchTeamMemberByNameNLocation(String name, String location) throws Exception {
//        if(areListElementVisible(btnSearchteamMember,5)) {
//            if (btnSearchteamMember.size() == 2) {
//                //click(btnSearchteamMember.get(1));
//                if (isElementLoaded(textSearch, 5) && isElementLoaded(searchIcon, 5)) {
//                    textSearch.clear();
//                    textSearch.sendKeys(name);
//                    click(searchIcon);
//                    if (areListElementVisible(searchResults, 15)) {
//                        for (WebElement searchResult : searchResults) {
//                            WebElement workerName = searchResult.findElement(By.className("worker-edit-search-worker-name"));
//                            WebElement optionCircle = searchResult.findElement(By.className("tma-staffing-option-outer-circle"));
//                            WebElement locationInfo = searchResult.findElement(By.className("tma-description-fields"));
//                            if (workerName != null && optionCircle != null) {
//                                if (workerName.getText().toLowerCase().trim().replaceAll("\n"," ").contains(name.trim().toLowerCase()) && locationInfo.getText().toLowerCase().trim().replaceAll("\n"," ").contains(location.trim().toLowerCase())) {
//                                    click(optionCircle);
//                                    SimpleUtils.report("Select Team Member: " + name + " Successfully!");
//                                    waitForSeconds(2);
//                                    if (isElementLoaded(btnAssignAnyway, 5) && btnAssignAnyway.getText().equalsIgnoreCase("ASSIGN ANYWAY")) {
//                                        click(btnAssignAnyway);
//                                        SimpleUtils.report("Assign Team Member: Click on 'ASSIGN ANYWAY' button Successfully!");
//                                    }
//                                    break;
//                                }
//                            }else {
//                                SimpleUtils.fail("Worker name or option circle not loaded Successfully!", false);
//                            }
//                        }
//                    }else {
//                        SimpleUtils.fail("Failed to find the team member!", false);
//                    }
//                }else {
//                    SimpleUtils.fail("Search text not editable and icon are not clickable", false);
//                }
//            }else {
//                SimpleUtils.fail("Search team member should have two tabs, failed to load!", false);
//            }
//        }
//    }

//    @Override
//    public void verifyNewShiftsAreShownOnSchedule(String name) throws Exception {
//        boolean isFound = false;
//        if (areListElementVisible(addedShiftIcons, 5)) {
//            for (WebElement addedShiftIcon : addedShiftIcons) {
//                WebElement parent = addedShiftIcon.findElement(By.xpath("./../../../.."));
//                if (parent != null) {
//                    WebElement teamMemberName = parent.findElement(By.className("week-schedule-worker-name"));
//                    if (teamMemberName != null && teamMemberName.getText().contains(name)) {
//                        isFound = true;
//                        SimpleUtils.pass("Added a New shift for: " + name + " is successful!");
//                    }
//                }else {
//                    SimpleUtils.fail("Failed to find the parent element for adding icon!", false);
//                }
//            }
//        }else {
//            SimpleUtils.fail("Failed to find the new added shift icons!", false);
//        }
//        if (!isFound) {
//            SimpleUtils.fail("Cannot find the new shift for team member: " + name, true);
//        }
//    }

//    @Override
//    public List<Integer> getAddedShiftIndexes(String name) throws Exception {
//        // Wait for the shifts to be loaded
//        waitForSeconds(5);
//        List<Integer> indexes = new ArrayList<>();
//        if (areListElementVisible(shiftsWeekView, 5)) {
//            for (int i = 0; i < shiftsWeekView.size(); i++) {
//                WebElement workerName = shiftsWeekView.get(i).findElement(By.className("week-schedule-worker-name"));
//                if (workerName != null) {
//                    if (workerName.getText().contains(name)) {
//                        indexes.add(i);
//                        SimpleUtils.pass("Get the index: " + i + " successfully!");
//                    }
//                }
//            }
//        }
//        if (indexes.size() == 0) {
//            SimpleUtils.fail("Failed to get the index of the newly added shifts!", false);
//        }
//        return indexes;
//    }

//    public HashMap<String, String> getAvailableShiftsForDayView(HashMap<String, String> upComingShifts) throws Exception {
//        String name = null;
//        String role = null;
//        if (areListElementVisible(dayViewAvailableShifts, 15)) {
//            for (WebElement dayViewAvailableShift : dayViewAvailableShifts) {
//                name = dayViewAvailableShift.findElement(By.className("sch-day-view-shift-worker-name")).getText().toLowerCase();
//                if (name.contains("(")) {
//                    name = name.substring(0, name.indexOf("(") - 1);
//                }
//                if (!name.contains("open") && !name.contains("unassigned")) {
//                    role = dayViewAvailableShift.findElement(By.className("sch-day-view-shift-worker-title-role")).getText().toLowerCase();
//                    upComingShifts.put(name, role);
//                }
//            }
//        } else {
//            SimpleUtils.fail("Day View Available shifts failed to load!", true);
//        }
//        return upComingShifts;
//    }

//    public HashMap<String, String> getShiftsForCurrentDayIfStartingSoon(HashMap<String, String> upComingShifts, String currentTime) throws Exception {
//        String name = null;
//        String role = null;
//        if (areListElementVisible(dayViewAvailableShifts, 15)) {
//            for (WebElement dayViewAvailableShift : dayViewAvailableShifts) {
//                WebElement hoverInfo = dayViewAvailableShift.findElement(By.className("day-view-shift-hover-info-icon"));
//                if (hoverInfo != null) {
//                    clickTheElement(hoverInfo);
//                    if (isElementLoaded(timeDuration, 5)) {
//                        String startTime = timeDuration.getText().split("-")[0];
//                        clickTheElement(hoverInfo);
//                        int shiftStartMinutes = getMinutesFromTime(startTime);
//                        int currentMinutes = getMinutesFromTime(currentTime);
//                        if (shiftStartMinutes > currentMinutes) {
//                            name = dayViewAvailableShift.findElement(By.className("sch-day-view-shift-worker-name")).getText().toLowerCase();
//                            if (name.contains("(")) {
//                                name = name.substring(0, name.indexOf("(") - 1);
//                            }
//                            if (!name.contains("open") && !name.contains("unassigned")) {
//                                role = dayViewAvailableShift.findElement(By.className("sch-day-view-shift-worker-title-role")).getText().toLowerCase();
//                                upComingShifts.put(name, role);
//                            }
//                        }
//                    }else {
//                        SimpleUtils.fail("Failed to get the time duration!", true);
//                    }
//                }else {
//                    SimpleUtils.fail("Failed to get the hover info element!", true);
//                }
//            }
//        }else {
//            SimpleUtils.fail("Day View Available shifts failed to load!", true);
//        }
//        return upComingShifts;
//    }

//    @Override
//    public void verifyUpComingShiftsConsistentWithSchedule(HashMap<String, String> dashboardShifts, HashMap<String, String> scheduleShifts) throws Exception {
//        if (scheduleShifts.entrySet().containsAll(dashboardShifts.entrySet())) {
//            SimpleUtils.pass("Up coming shifts from dashboard is consistent with the shifts in schedule!");
//        }else {
//            SimpleUtils.fail("Up coming shifts from dashboard isn't consistent with the shifts in schedule!", true);
//        }
//    }

//    @Override
//    public void clickOnCreateNewShiftButton() throws Exception {
//        if (isElementLoaded(createNewShiftWeekView, 5)) {
//            click(createNewShiftWeekView);
//            SimpleUtils.pass("Click on Create New Shift button successfully!");
//        }else {
//            SimpleUtils.fail("Create New Shift button failed to load on Week View!", false);
//        }
//    }
//
//    @Override
//    public void clickOnDayViewAddNewShiftButton() throws Exception {
//        if (isElementLoaded(createNewShiftWeekView, 10)) {
//            clickTheElement(createNewShiftWeekView);
//            SimpleUtils.pass("Click on Create New Shift button successfully!");
//        }else if (isElementLoaded(addNewShiftOnDayViewButton, 10)) {
//            click(addNewShiftOnDayViewButton);
//            SimpleUtils.pass("Click on Add New Shift '+' button successfully!");
//        }else {
//            SimpleUtils.report("Add New Shift '+' button and Create New Shift button not loaded!");
//        }
//    }

//    @Override
//    public void verifyTeamCount(List<String> previousTeamCount, List<String> currentTeamCount) throws Exception {
//        if (previousTeamCount.size() == currentTeamCount.size()) {
//            for (int i = 0; i < currentTeamCount.size(); i++) {
//                String currentCount = currentTeamCount.get(i);
//                String previousCount = previousTeamCount.get(i);
//                if (Integer.parseInt(currentCount) == Integer.parseInt(previousCount) + 1) {
//                    SimpleUtils.pass("Current Team Count is greater than Previous Team Count");
//                } else {
//                    SimpleUtils.fail("Current Team Count is not greater than Previous Team Count", true);
//                }
//            }
//        } else {
//            SimpleUtils.fail("Size of Current Team Count should be equal to Previous Team Count", false);
//        }
//    }

//    @Override
//    public void printButtonIsClickable() throws Exception {
//        if (isElementLoaded(printButton,10)){
//            scrollToTop();
//            click(printButton);
//            if(isElementLoaded(printButtonInPrintLayout, 5)) {
//                click(printButtonInPrintLayout);
//            }
//        }else{
//            SimpleUtils.fail("There is no print button",false);
//        }
//    }

//    @Override
//    public void todoButtonIsClickable() throws Exception {
//        if(isElementLoaded(todoButton,10)) {
//            scrollToTop();
//            click(todoButton);
//            if(isElementLoaded(todoSmartCard,5)) {
//                SimpleUtils.pass("Todo button is  clickable");
//            }else {
//                SimpleUtils.fail("click todo button failed",true);
//            }
//        }else {
//            SimpleUtils.fail("there is no todo button", true);
//        }
//    }

//    @Override
//    public void legionButtonIsClickableAndHasNoEditButton() throws Exception {
//        clickOnSuggestedButton();
//        if(!isElementLoaded(edit,5)){
//            SimpleUtils.pass("Legion schedule has no edit button");
//        }else{
//            SimpleUtils.fail("it's not in legion schedule page", true);
//        }
//    }
//
//
//    public void clickOnViewScheduleLocationSummaryDMViewDashboard() {
//        if (isElementEnabled(viewScheduleLinkInlocationsSummarySmartCardDashboard, 2)) {
//            click(viewScheduleLinkInlocationsSummarySmartCardDashboard);
//            SimpleUtils.pass("'View Schedule' link in location summary smartcard Loaded Successfully!");
//        } else {
//            SimpleUtils.fail("'View Schedule' link in location summary smartcard not Loaded!", false);
//        }
//    }
//
//    public void clickOnViewSchedulePayrollProjectionDMViewDashboard() {
//        if (isElementEnabled(viewScheduleLinkInPayRollProjectionSmartCardDashboard, 2)) {
//            click(viewScheduleLinkInPayRollProjectionSmartCardDashboard);
//            SimpleUtils.pass("'View Schedule' link in PayRoll Projection smartcard Loaded Successfully!");
//        } else {
//            SimpleUtils.fail("'View Schedule' link in PayRoll Projection smartcard not Loaded!", false);
//        }
//    }
//
//
//    public void loadingOfDMViewSchedulePage(String SelectedWeek) throws Exception {
//        if(isElementLoaded(locationSummarySmartCardOnSchedule, 2)){
//            SimpleUtils.pass("'Location Summary' smartcard on DM View Schedule Page Loaded Successfully! for week "+ SelectedWeek);
//        } else {
//            SimpleUtils.fail("'Location Summary' smartcard on DM View Schedule Page not Loaded! for week "  + SelectedWeek, true);
//        }
//
//        if(DMtableRowCount.size()>0){
//            SimpleUtils.pass("Locations Table on DM View Schedule Page Loaded Successfully! for week " +SelectedWeek );
//        } else {
//            SimpleUtils.fail("Location Table on DM View Schedule Page not Loaded! for week " + SelectedWeek, true);
//        }
//    }
//
//    public void validateCorrectnessOfDMToSMNavigation(String locationToSelect, String districtName, String selectedWeek, String selectedDistrict, String selectedLocation, String activeWeekSMView) throws Exception {
//        if(selectedDistrict.equalsIgnoreCase(districtName) && selectedLocation.equalsIgnoreCase(locationToSelect)){
//            SimpleUtils.pass("Navigation from DM to SM View Works fine. " + "\n"
//                    + "Expected selection of District from DM view i.e. " + districtName + " matches the selection in SM View i.e. " + selectedDistrict + ". \n"
//                    + "Expected selection of Location from DM view i.e. " + locationToSelect + " matches the selection in SM View i.e. " + selectedLocation + ".");
//            System.out.println("compareDMAndSMViewDatePickerText(selectedWeek) is "+compareDMAndSMViewDatePickerText(selectedWeek));
//            if(compareDMAndSMViewDatePickerText(selectedWeek) == true) {
//                if (areListElementVisible(carouselCards,10,1)) {
//                    SimpleUtils.pass("Smartcard in SM Schedule loaded successfully! for selected week i.e " + selectedWeek);
//                } else {
//                    SimpleUtils.fail("Smartcard in SM Schedule not loaded successfully! for selected week i.e " + selectedWeek, true);
//                }
//                if (areListElementVisible(shiftsOnScheduleView,10,1)) {
//                    SimpleUtils.pass("SM Schedule table loaded successfully! for selected week i.e " + selectedWeek);
//                } else {
//                    SimpleUtils.fail("SM Schedule table not loaded successfully! for selected week i.e " + selectedWeek, true);
//                }
//            }else{
//                SimpleUtils.fail("Wrong week selected in SM View, expected week is " +selectedWeek + " and selected week is "+activeWeekSMView, true);
//            }
//        }else{
//            SimpleUtils.fail("Navigation from DM to SM View is not correct. " + " \n"
//                    + "Expected selection of District from DM view i.e. " + districtName + " doesn't match the selection in SM View i.e. " + selectedDistrict + ". \n"
//                    + "Expected selection of Location from DM view i.e. " + locationToSelect + " doesn't match the selection in SM View i.e. " + selectedLocation + ". ", true);
//        }
//    }
//
//
//    public void checkNavDMtoSMScheduleNSMScheduleLoading(String locationToSelect, String districtName, String selectedWeek) throws Exception {
//        String selectedDistrict = null;
//        String selectedLocation = null;
//        String activeWeekSMView = null;
//        if (areListElementVisible(DMtableRowCount, 3) && DMtableRowCount.size() != 0) {
//            for (int i = 0; i < DMtableRowCount.size(); i++) {
//                if (DMtableRowCount.get(i).getText().contains(locationToSelect)) {
//                    click(DMtableRowCount.get(i));
//                    selectedDistrict = selectedDistrictSMView.getText();
//                    selectedLocation = selectedLocationSMView.getText();
//                    activeWeekSMView = getActiveWeekText();
//                    validateCorrectnessOfDMToSMNavigation(locationToSelect, districtName, selectedWeek, selectedDistrict, selectedLocation, activeWeekSMView);
////                    selectedDistrictSMView.click();
//                    districtSelectionSMView(districtName);
//                    if(compareDMAndSMViewDatePickerText(activeWeekSMView) == true){
//                        SimpleUtils.pass("Backward navigation from SM to DM view is working fine, week selected in SM View " + activeWeekSMView + " , active week in DM view on backward navigation is " + daypicker.getText().replace("\n"," "));
//                    }else{
//                        SimpleUtils.fail("Backward navigation from SM to DM view is not correct, week selected in SM View is " + activeWeekSMView + " , active week in DM view on backward navigation is " + daypicker.getText().replace("\n"," "), true);
//                    }
//                }
//            }
//        }
//    }
//
//    @Override
//    public void toNFroNavigationFromDMToSMSchedule(String CurrentWeek, String locationToSelectFromDMViewSchedule, String districtName, String nextWeekViewOrPreviousWeekView) throws Exception {
//        ScheduleCommonPage scheduleCommonPage = new ConsoleScheduleCommonPage();
//        String weekSelected = null;
//        loadingOfDMViewSchedulePage(CurrentWeek);
//        checkNavDMtoSMScheduleNSMScheduleLoading(locationToSelectFromDMViewSchedule, districtName, CurrentWeek);
//        if (nextWeekViewOrPreviousWeekView.toLowerCase().contains("next") || nextWeekViewOrPreviousWeekView.toLowerCase().contains("future")) {
//            scheduleCommonPage.clickImmediateNextToCurrentActiveWeekInDayPicker();
//            weekSelected = daypicker.getText().replace("\n", " ");
//            checkNavDMtoSMScheduleNSMScheduleLoading(locationToSelectFromDMViewSchedule, districtName, weekSelected);
//        } else {
//            scheduleCommonPage.clickImmediatePastToCurrentActiveWeekInDayPicker();
//            weekSelected = daypicker.getText().replace("\n", " ");
//            checkNavDMtoSMScheduleNSMScheduleLoading(locationToSelectFromDMViewSchedule, districtName, weekSelected);
//        }
//
//    }
//
//    public void toNFroNavigationFromDMDashboardToDMSchedule(String CurrentWeek) throws Exception{
//        String weekSelected = null;
//        loadingOfDMViewSchedulePage(CurrentWeek);
//    }
//
//
//
//    public void compareHoursFromScheduleAndDashboardPage(List<Float> totalHoursFromSchTbl) throws Exception{
//
//        List<Float> totalHoursFromDashboardTbl = new ArrayList<>();
//        if(areListElementVisible(hoursOnDashboardPage,10) && hoursOnDashboardPage.size()!=0){
//            for(int i =0; i < hoursOnDashboardPage.size();i++){
//                totalHoursFromDashboardTbl.add(Float.parseFloat(hoursOnDashboardPage.get(i).getText().replace(",","")));
//            }
//            for(int j=0; j < totalHoursFromSchTbl.size();j++){
//                if(totalHoursFromSchTbl.get(j).equals(totalHoursFromDashboardTbl.get(j))){
//                    SimpleUtils.pass(titleOnDashboardPage.get(j).getText() +
//                            " Hours from Dashboard page " + totalHoursFromDashboardTbl.get(j)
//                            + " matching with the hours present on Schedule Page " + totalHoursFromSchTbl.get(j));
//                }else{
//                    SimpleUtils.fail(titleOnDashboardPage.get(j).getText() +
//                            " Hours from Dashboard page " + totalHoursFromDashboardTbl.get(j)
//                            + " not matching with the hours present on Schedule Page " + totalHoursFromSchTbl.get(j),true);
//                }
//            }
//        }else{
//            SimpleUtils.fail("No data available for Hours on Dashboard page in DM view",false);
//        }
//    }
//
//
//    public List<Float> getHoursOnLocationSummarySmartCard() throws Exception{
//        List<Float> totalHoursFromDashboardTbl = new ArrayList<>();
//        if(areListElementVisible(locationSummaryOnSchedule,10) && locationSummaryOnSchedule.size()!=0){
//            totalHoursFromDashboardTbl.add(Float.parseFloat(locationSummaryOnSchedule.get(0).getText()));
//            totalHoursFromDashboardTbl.add(Float.parseFloat(locationSummaryOnSchedule.get(2).getText()));
//            totalHoursFromDashboardTbl.add(Float.parseFloat(locationSummaryOnSchedule.get(6).getText()));
//        }else{
//            SimpleUtils.fail("No data available on Location Summary section Smart Card in DM view",false);
//        }
//        return totalHoursFromDashboardTbl;
//    }
//
//
//    public void compareHoursFromScheduleSmartCardAndDashboardSmartCard(List<Float> totalHoursFromSchTbl) throws Exception{
//
//        List<Float> totalHoursFromDashboardTbl = new ArrayList<>();
//        if(areListElementVisible(hoursOnDashboardPage,10) && hoursOnDashboardPage.size()!=0){
//            for(int i =0; i < hoursOnDashboardPage.size();i++){
//                totalHoursFromDashboardTbl.add(Float.parseFloat(hoursOnDashboardPage.get(i).getText().replace(",","")));
//            }
//            for(int j=0; j < totalHoursFromSchTbl.size();j++){
//                if(totalHoursFromSchTbl.get(j).equals(totalHoursFromDashboardTbl.get(j))){
//                    SimpleUtils.pass(titleOnDashboardPage.get(j).getText().replace(",","") +
//                            " Hours from Dashboard page " + totalHoursFromDashboardTbl.get(j)
//                            + " matching with the hours present on Schedule Page " + totalHoursFromSchTbl.get(j));
//                }
//            }
//        }else{
//            SimpleUtils.fail("No data available for Hours on Dashboard page in DM view",false);
//        }
//    }
//
//
//    public float getProjectedUnderBudget(){
//        float totalCountProjectedUnderBudget = 0;
//        if(areListElementVisible(projectedUnderBudget,10) && projectedUnderBudget.size()!=0){
//            for(int i=0;i<projectedUnderBudget.size();i++){
//                float countProjectedUnderBudget = Float.parseFloat(projectedUnderBudget.get(i).getText().replace(",",""));
//                totalCountProjectedUnderBudget = totalCountProjectedUnderBudget + countProjectedUnderBudget;
//            }
//        }else{
//            SimpleUtils.fail("No data available for Projected Under Budget section on location specific date in DM view",false);
//        }
//        return totalCountProjectedUnderBudget;
//    }
//
//
//    public float getProjectedOverBudget(){
//        float totalCountProjectedOverBudget = 0.0f;
//        if(areListElementVisible(projectedOverBudget,10) && projectedOverBudget.size()!=0){
//            for(int i=0;i<projectedOverBudget.size();i++){
//                float countProjectedOverBudget = Float.parseFloat(projectedOverBudget.get(i).getText());
//                totalCountProjectedOverBudget = totalCountProjectedOverBudget + countProjectedOverBudget;
//            }
//        }else{
//            SimpleUtils.fail("No data available for Projected Over Budget section on location specific date in DM view",false);
//        }
//        return totalCountProjectedOverBudget;
//    }
//
//    public void compareProjectedWithinBudget(float totalCountProjectedOverBudget) throws Exception{
//        if(isElementLoaded(projectedWithinOrOverBudget,10)){
//            String ProjectedWithinOrOverBudget = (projectedWithinOrOverBudget.getText().split(" "))[0];
//            if(totalCountProjectedOverBudget == Float.parseFloat(ProjectedWithinOrOverBudget)){
//                SimpleUtils.pass("Count of Projected Over/Under Budget on Dashboard page" +
//                        " " + Float.parseFloat(ProjectedWithinOrOverBudget) + " is same as Schedule page " + totalCountProjectedOverBudget);
//            }else{
//                SimpleUtils.fail("Count of Projected Over/Under Budget on Dashboard page" +
//                        " " + Float.parseFloat(ProjectedWithinOrOverBudget) + " not matching with Schedule page " + totalCountProjectedOverBudget,false);
//            }
//        }else{
//            SimpleUtils.fail("No data available for Projected Over/Under Budget section on Dashboard in DM view",false);
//        }
//
//    }
//
//    public String getDateFromDashboard() throws Exception {
//        String DateOnDashboard = null;
//        if(isElementLoaded(dateOnDashboard,10)){
//            DateOnDashboard = dateOnDashboard.getText().substring(8);
//        }else{
//            SimpleUtils.fail("Week Date not available on Dashboard in DM view",false);
//        }
//
//        return DateOnDashboard;
//    }
//    @FindBy(css = "div.console-navigation-item.active")
//    private WebElement activeConsoleMenuItem;
//    public void compareDashboardAndScheduleWeekDate(String DateOnSchdeule, String DateOnDashboard) throws Exception {
//        activeConsoleName = activeConsoleMenuItem.getText();
//        String splitFirstDate = null;
//        String splitSecondDate = null;
//        String strDateOnSchedule = DateOnSchdeule.substring(9).trim();
//        String[] splitDateOnSchedule = strDateOnSchedule.split(" ");
//        if(splitDateOnSchedule[1].length()>1){
//            splitFirstDate = splitDateOnSchedule[1];
//        }else{
//            splitFirstDate = "0" + splitDateOnSchedule[1];
//        }
//        if(splitDateOnSchedule[4].length()>1){
//            splitSecondDate = splitDateOnSchedule[4];
//        }else{
//            splitSecondDate = "0" + splitDateOnSchedule[4];
//        }
//
//        String actualDateOnSchedule = splitDateOnSchedule[0] + " " + splitFirstDate
//                + " " + splitDateOnSchedule[2] + " " + splitDateOnSchedule[3] + " " + splitSecondDate;
//
//        if(actualDateOnSchedule.equals(DateOnDashboard)){
//            SimpleUtils.pass("Week Date on Dashboard " + DateOnDashboard + " matching with DM view of " + activeConsoleName + " date " + actualDateOnSchedule);
//        }else{
//            SimpleUtils.fail("Week Date on Dashboard " + DateOnDashboard + " not matching with Schedule date " + actualDateOnSchedule,true);
//        }
//
//    }
//
//
//    public List<String> getLocationSummaryDataFromDashBoard() throws Exception{
//        String locationSummaryTitleOnDashboard = null;
//        List<String> ListLocationSummaryOnDashboard = new ArrayList<>();
//        if(isElementLoaded(locationsSummaryTitleOnDashboard, 10)){
//            locationSummaryTitleOnDashboard = locationsSummaryTitleOnDashboard.getText();
//            ListLocationSummaryOnDashboard.add(locationSummaryTitleOnDashboard);
//        }else{
//            SimpleUtils.fail("Location Summary Title not available on Dashboard Page", true);
//        }
//
//        if(areListElementVisible(locationsSummarySmartCardOnDashboard,10) && locationsSummarySmartCardOnDashboard.size()!=0){
//            for(int i =0; i< locationsSummarySmartCardOnDashboard.size();i++){
//                ListLocationSummaryOnDashboard.add(locationsSummarySmartCardOnDashboard.get(i).getText());
//            }
//        }else{
//            SimpleUtils.fail("Location Summary Smart Card not available on Dashboard Page", true);
//        }
//
//        return ListLocationSummaryOnDashboard;
//    }
//

//    public List<String> getLocationSummaryDataFromSchedulePage() throws Exception{
//        String locationSummaryTitleOnSchedule = null;
//        List<String> ListLocationSummaryOnSchedule = new ArrayList<>();
//        if(isElementLoaded(locationsSummaryTitleOnSchedule, 10)){
//            locationSummaryTitleOnSchedule = locationsSummaryTitleOnSchedule.getText();
//            ListLocationSummaryOnSchedule.add(locationSummaryTitleOnSchedule);
//        }else{
//            SimpleUtils.fail("Location Summary Title not available on Dashboard Page", true);
//        }
//
//        if(areListElementVisible(locationsSummarySmartCardOnSchedule,10) && locationsSummarySmartCardOnSchedule.size()!=0){
//            for(int i =0; i< locationsSummarySmartCardOnSchedule.size();i++){
//                ListLocationSummaryOnSchedule.add(locationsSummarySmartCardOnSchedule.get(i).getText());
//            }
//        }else{
//            SimpleUtils.fail("Location Summary Smart Card not available on Dashboard Page", true);
//        }
//
//        return ListLocationSummaryOnSchedule;
//    }
//
//
//    public void compareLocationSummaryFromDashboardAndSchedule(List<String> ListLocationSummaryOnDashboard, List<String> ListLocationSummaryOnSchedule){
//        for(int i=0; i<ListLocationSummaryOnDashboard.size();i++){
//            if(ListLocationSummaryOnDashboard.get(i).equalsIgnoreCase(ListLocationSummaryOnSchedule.get(i))){
//                SimpleUtils.pass("Location Summary on Dashboard "
//                        + ListLocationSummaryOnDashboard.get(i) + " matches with location" +
//                        " summary on Schedule page " +ListLocationSummaryOnSchedule.get(i));
//            }else{
//                SimpleUtils.fail("Location Summary on Dashboard "
//                        + ListLocationSummaryOnDashboard.get(i) + " doesn't match with location" +
//                        " summary on Schedule page " +ListLocationSummaryOnSchedule.get(i),true);
//            }
//        }
//    }

    @FindBy(css = "div.edit-budget span.header-text")
    private List<WebElement> tblBudgetRow;
    @FindBy(css = "span[ng-if='canEditWages(budget)'] span")
    private List<WebElement> editListWagesHrs;

//    public void openBudgetPopUpGenerateSchedule() throws Exception{
//        if(isElementEnabled(btnGenerateBudgetPopUP,5)){
//            click(btnGenerateBudgetPopUP);
//        }else{
//            SimpleUtils.fail("Generate btn not clickable on Budget pop up", false);
//        }
//    }

//    public void openBudgetPopUp() throws Exception{
//        if(isElementLoaded(popUpGenerateScheduleTitleTxt,5)){
//            if(areListElementVisible(editBudgetHrs,5)){
//                fillBudgetValues(editBudgetHrs);
//                openBudgetPopUpGenerateSchedule();
//            }else if(areListElementVisible(editWagesHrs,5)){
//                fillBudgetValues(editWagesHrs);
//                openBudgetPopUpGenerateSchedule();
//            } else if(isElementLoaded(btnGenerateBudgetPopUP, 5)){
//                openBudgetPopUpGenerateSchedule();
//            }
//        }
//    }

//    @FindBy(css = "input[ng-class='hoursFieldClass(budget)']")
//    private List<WebElement> inputHrs;
//    @FindBy(css = "tr.table-row.ng-scope")
//    private List<WebElement> budgetTableRow;
//
//    public void fillBudgetValues(List<WebElement> element) throws Exception {
//        if(areListElementVisible(budgetTableRow,5)){
//            for(int i=0; i<budgetTableRow.size()-1;i++){
//                click(element.get(i));
//                int fillBudgetInNumbers = SimpleUtils.generateRandomNumbers();
//                inputHrs.get(i).clear();
//                inputHrs.get(i).sendKeys(String.valueOf(fillBudgetInNumbers));
//            }
//        }else{
//            SimpleUtils.fail("Not able to see Budget table row for filling up the data",false);
//        }
//    }
//
//    public void updatebudgetInScheduleNBudgetSmartCard(String nextWeekView, int weekCount) {
//        // TODO Auto-generated method stub
//        CreateSchedulePage createSchedulePage = new ConsoleCreateSchedulePage();
//        waitForSeconds(3);
//        for(int i = 0; i < weekCount; i++)
//        {
//            float totalBudgetedHourForBudgetSmartCard=0.0f;
//            float totalBudgetHourforScheduleSmartcardIfBudgetEntered=0.0f;
//            float totalBudgetedWagesForBudgetSmartCard=0.0f;
//            float totalScheduledWagesIfBudgetEntered=0.0f;
//            if(nextWeekView.toLowerCase().contains("next") || nextWeekView.toLowerCase().contains("future"))
//            {
//                try {
//                    if(isElementLoaded(schedulesForWeekOnOverview.get(0))) {
//                        waitForSeconds(3);
//                        click(schedulesForWeekOnOverview.get(i));
//                        waitForSeconds(4);
//                        String[] daypickers = daypicker.getText().split("\n");
//                        String valueOfBudgetSmartcardWhenNoBudgetEntered = budgetOnbudgetSmartCardWhenNoBudgetEntered.getText();
//                        String[] budgetDisplayOnBudgetSmartcard = budgetOnbudgetSmartCard.getText().split(" ");
//                        String budgetDisplayOnSmartCardWhenByWages = budgetOnbudgetSmartCard.getText().substring(1);
//                        String budgetDisplayOnBudgetSmartCardByHours = budgetDisplayOnBudgetSmartcard[0];
//                        String budgetOnScheduleSmartcard = budgetDisplayOnScheduleSmartcard.get(0).getText();
//                        String budgetedWagesOnScheduleSmartcard = budgetDisplayOnScheduleSmartcard.get(1).getText().substring(1).replace(",","");
//                        String weekDuration = daypickers[1];
//                        if (verifyNoBudgetAvailableForWeek(valueOfBudgetSmartcardWhenNoBudgetEntered, weekDuration) == false) {
//                            click(enterBudgetLink);
//                            waitForSeconds(3);
//                            if(areListElementVisible(editBudgetHrs,5)){
//                                createSchedulePage.fillBudgetValues(editBudgetHrs);
//                                compareBudgetValueForScheduleAndBudgetSmartCardWhenBudgetByHour(weekDuration);
//                            }else if(areListElementVisible(editWagesHrs,5)){
//                                createSchedulePage.fillBudgetValues(editWagesHrs);
//                                compareBudgetValueForScheduleAndBudgetSmartCardWhenBudgetByWages(weekDuration);
//                            }
//
//                        }
//                    }
//                }
//                catch (Exception e) {
//                    SimpleUtils.fail("Budget pop-up not opening ",false);
//                }
//            }
//        }
//    }
//
//
//    public void compareBudgetValueForScheduleAndBudgetSmartCardWhenBudgetByHour(String weekDuration){
//        float totalBudgetedHourForBudgetSmartCard=0.0f;
//        float totalBudgetHourforScheduleSmartcardIfBudgetEntered=0.0f;
//        String budgetOnScheduleSmartcard = null;
//        String budgetDisplayOnBudgetSmartCardByHours = null;
//        for (int j = 1; j < guidanceHour.size(); j++) {
//            totalBudgetedHourForBudgetSmartCard = totalBudgetedHourForBudgetSmartCard + Float.parseFloat(budgetEditHours.get(j - 1).getAttribute("value"));
//            if (((Float.parseFloat(budgetEditHours.get(j - 1).getAttribute("value"))) == 0)) {
//                totalBudgetHourforScheduleSmartcardIfBudgetEntered = totalBudgetHourforScheduleSmartcardIfBudgetEntered + Float.parseFloat(guidanceHour.get(j - 1).getText());
//
//            } else {
//                totalBudgetHourforScheduleSmartcardIfBudgetEntered = totalBudgetHourforScheduleSmartcardIfBudgetEntered + Float.parseFloat(budgetEditHours.get(j - 1).getAttribute("value"));
//            }
//        }
//
//        if(isElementEnabled(okAfterSaveConfirmationPopup,5)){
//            click(okAfterSaveConfirmationPopup);
//            SimpleUtils.pass("Apply Budget button is clickable");
//        }else{
//            SimpleUtils.fail("Apply Budget button is not clickable",false);
//        }
//
//        getDriver().navigate().refresh();
//        if(areListElementVisible(scheduleWeekViewGrid,10)){
//            budgetOnScheduleSmartcard = budgetDisplayOnScheduleSmartcard.get(0).getText();
//            String[] budgetDisplayOnBudgetSmartcard = budgetOnbudgetSmartCard.getText().split(" ");
//            budgetDisplayOnBudgetSmartCardByHours = budgetDisplayOnBudgetSmartcard[0];
//        }
//
//        if (totalBudgetedHourForBudgetSmartCard == (Float.parseFloat(budgetDisplayOnBudgetSmartCardByHours))) {
//            SimpleUtils.pass("Budget " + (Float.parseFloat(budgetDisplayOnBudgetSmartCardByHours)) +" for week " +weekDuration + " on budget smartcard matches the budget entered " + totalBudgetedHourForBudgetSmartCard);
//        } else {
//            SimpleUtils.fail("Budget " + (Float.parseFloat(budgetDisplayOnBudgetSmartCardByHours))  +" for week " +weekDuration + " on budget smartcard doesn't match the budget entered " + totalBudgetedHourForBudgetSmartCard, true);
//        }
//
//        float finaltotalScheduledHourIfBudgetEntered = (float) (Math.round(totalBudgetHourforScheduleSmartcardIfBudgetEntered * 10) / 10.0);;
//        if (finaltotalScheduledHourIfBudgetEntered == (Float.parseFloat(budgetOnScheduleSmartcard))) {
//            SimpleUtils.pass("Budget " + (Float.parseFloat(budgetOnScheduleSmartcard))  +" for week " +weekDuration + " on schedule smartcard matches the budget calculated " + finaltotalScheduledHourIfBudgetEntered);
//        } else {
//            SimpleUtils.fail("Budget " + (Float.parseFloat(budgetOnScheduleSmartcard))  +" for week " +weekDuration + " on schedule smartcard doesn't match the budget calculated " + finaltotalScheduledHourIfBudgetEntered, true);
//        }
//        if(isElementEnabled(returnToOverviewTab,5)){
//            click(returnToOverviewTab);
//        }else{
//            SimpleUtils.fail("Unable to click on Overview tab",false);
//        }
//
//    }
//
//    public void compareBudgetValueForScheduleAndBudgetSmartCardWhenBudgetByWages(String weekDuration){
//        float totalBudgetedWagesForBudgetSmartCard=0.0f;
//        float totalScheduledHourIfBudgetEntered=0.0f;
//        float totalScheduledWagesIfBudgetEntered=0.0f;
//        String budgetDisplayOnBudgetSmartCardByHours = null;
//        String budgetOnScheduleSmartcard = null;
//        String budgetDisplayOnSmartCardWhenByWages = null;
//        String budgetedWagesOnScheduleSmartcard = null;
//        String valueOfBudgetSmartcardWhenNoBudgetEntered = budgetOnbudgetSmartCardWhenNoBudgetEntered.getText();
//
//        for (int j = 1; j < guidanceHour.size(); j++) {
//            totalBudgetedWagesForBudgetSmartCard = totalBudgetedWagesForBudgetSmartCard + Float.parseFloat(budgetEditHours.get(j - 1).getAttribute("value"));
//            if (((Float.parseFloat(budgetEditHours.get(j - 1).getAttribute("value"))) == 0)) {
//                totalScheduledHourIfBudgetEntered = totalScheduledHourIfBudgetEntered + Float.parseFloat(guidanceHour.get(j - 1).getText());
//                totalScheduledWagesIfBudgetEntered = totalScheduledWagesIfBudgetEntered + Float.parseFloat(guidanceWages.get(j-1).getText());
//            } else {
//                totalScheduledHourIfBudgetEntered = totalScheduledHourIfBudgetEntered + Float.parseFloat(budgetHourWhenBudgetByWagesEnabled.get(j - 1).getText());
//                totalScheduledWagesIfBudgetEntered = totalScheduledWagesIfBudgetEntered + Float.parseFloat(budgetEditHours.get(j - 1).getAttribute("value"));
//            }
//        }
//
//        if(isElementEnabled(okAfterSaveConfirmationPopup,5)){
//            click(okAfterSaveConfirmationPopup);
//            SimpleUtils.pass("Apply Budget button is clickable");
//        }else{
//            SimpleUtils.fail("Apply Budget button is not clickable",false);
//        }
//
//        getDriver().navigate().refresh();
//        if(areListElementVisible(scheduleWeekViewGrid,10)){
//            budgetOnScheduleSmartcard = budgetDisplayOnScheduleSmartcard.get(0).getText();
//            String[] budgetDisplayOnBudgetSmartcard = budgetOnbudgetSmartCard.getText().split(" ");
//            budgetDisplayOnSmartCardWhenByWages = budgetOnbudgetSmartCard.getText().substring(1);
//            budgetedWagesOnScheduleSmartcard = budgetDisplayOnScheduleSmartcard.get(1).getText().substring(1).replace(",","");
//
//        }
//        if (totalBudgetedWagesForBudgetSmartCard == (Float.parseFloat(budgetDisplayOnSmartCardWhenByWages.replaceAll(",","")))) {
//            SimpleUtils.pass("Budget " + (Float.parseFloat(budgetDisplayOnSmartCardWhenByWages.replaceAll(",",""))) +" for week " +weekDuration + " on budget smartcard matches the budget entered " + totalBudgetedWagesForBudgetSmartCard);
//        } else {
//            SimpleUtils.fail("Budget " + (Float.parseFloat(budgetDisplayOnSmartCardWhenByWages))  +" for week " +weekDuration + " on budget smartcard doesn't match the budget entered " + totalBudgetedWagesForBudgetSmartCard, false);
//        }
//
//        float finaltotalScheduledHourIfBudgetEntered = (float) (Math.round(totalScheduledHourIfBudgetEntered * 10) / 10.0);
//        float differenceBetweenBudInSCnCalcBudgbyHour = (Float.parseFloat(budgetOnScheduleSmartcard)) - finaltotalScheduledHourIfBudgetEntered;
//        if (finaltotalScheduledHourIfBudgetEntered == (Float.parseFloat(budgetOnScheduleSmartcard)) ||
//                (differenceBetweenBudInSCnCalcBudgbyHour <= Integer.parseInt(propertyBudgetValue.get("Tolerance_Value")))) {
//            SimpleUtils.pass("Budget " + (Float.parseFloat(budgetOnScheduleSmartcard))  +" for week " +weekDuration + " on schedule smartcard matches the budget calculated " + finaltotalScheduledHourIfBudgetEntered);
//        } else {
//            SimpleUtils.fail("Budget " + (Float.parseFloat(budgetOnScheduleSmartcard))  +" for week " +weekDuration + " on schedule smartcard doesn't match the budget calculated " + finaltotalScheduledHourIfBudgetEntered, true);
//        }
//        int finaltotalScheduledWagesIfBudgetEntered = (int) (Math.round(totalScheduledWagesIfBudgetEntered * 10) / 10.0);
//        int differenceBetweenBugInSCnCalcBudg = (Integer.parseInt(budgetedWagesOnScheduleSmartcard)) - finaltotalScheduledWagesIfBudgetEntered;
//        waitForSeconds(3);
//        if (finaltotalScheduledWagesIfBudgetEntered == (Integer.parseInt(budgetedWagesOnScheduleSmartcard)) || (differenceBetweenBugInSCnCalcBudg <= Integer.parseInt(propertyBudgetValue.get("Tolerance_Value")))){
//            SimpleUtils.pass("Budgeted Wages " + (Float.parseFloat(budgetedWagesOnScheduleSmartcard))  +" for week " +weekDuration + " on schedule smartcard matches the budget wages calculated " + finaltotalScheduledWagesIfBudgetEntered);
//            setBudgetTolerance(0);
//        }else{
//            SimpleUtils.fail("Budget Wages" + (Float.parseFloat(budgetedWagesOnScheduleSmartcard))  +" for week " +weekDuration + " on schedule smartcard doesn't match the budget wages calculated " + finaltotalScheduledWagesIfBudgetEntered, true);
//        }
//        if(isElementEnabled(returnToOverviewTab,5)){
//            click(returnToOverviewTab);
//        }else{
//            SimpleUtils.fail("Unable to click on Overview tab",false);
//        }
//    }

    //added by Nishant

//    public void searchTextForClopeningHrs(String searchInput) throws Exception {
//        String[] searchAssignTeamMember = searchInput.split(",");
//        if (isElementLoaded(textSearch, 10) && isElementLoaded(searchIcon, 10)) {
//            for (int i = 0; i < searchAssignTeamMember.length; i++) {
//                String[] searchTM = searchAssignTeamMember[i].split("\\.");
//                textSearch.sendKeys(searchTM[0]);
//                click(searchIcon);
//                if (getScheduleStatus()) {
//                    setTeamMemberName(searchAssignTeamMember[i]);
//                    break;
//                } else {
//                    textSearch.clear();
//                }
//            }
//
//        } else {
//            SimpleUtils.fail("Search text not editable and icon are not clickable", false);
//        }
//
//    }
//
//    @FindBy(xpath = "//span[text()='Clopening']")
//    private WebElement clopeningFlag;
//
//    public void verifyClopeningHrs() throws Exception {
//        boolean flag = true;
//        if(areListElementVisible(infoIcon,5)){
//            for(int i=0; i<infoIcon.size();i++){
//                if(areListElementVisible(workerStatus,5)){
//                    if(workerStatus.get(i).getText().toLowerCase().contains(getTeamMemberName().toLowerCase())){
//                        click(infoIcon.get(i));
//                        if(isElementLoaded(clopeningFlag,5)){
//                            SimpleUtils.pass("Clopening Flag is present for team member " + getTeamMemberName());
//                            break;
//                        }else{
//                            SimpleUtils.fail("Clopening Flag is not present for team member " + getTeamMemberName(),false);
//                        }
//                    }else{
//                        flag = false;
//                    }
//                }else{
//                    SimpleUtils.fail("Worker status not available on the page",true);
//                }
//            }
//        }else{
//            SimpleUtils.fail("There is no image icon available on the page",false);
//        }
//
//        if(!flag) {
//            SimpleUtils.report("Worker status does not match with the expected result");
//        }
//    }
//

//    public void clickOnPreviousDaySchedule(String activeDay) throws Exception {
//        List<WebElement> activeWeek = MyThreadLocal.getDriver().findElements(By.className("day-week-picker-period"));
//        for(int i=0; i<activeWeek.size();i++){
//            String currentDay = activeWeek.get(i).getText().replace("\n", " ").substring(0,3);
//            if(currentDay.equalsIgnoreCase(activeDay)){
//                if(i== activeWeek.size()-1){
//                    navigateWeekViewOrDayViewToPastOrFuture(ScheduleTestKendraScott2.weekViewType.Previous.getValue(),
//                            ScheduleTestKendraScott2.weekCount.One.getValue());
//                    waitForSeconds(3);
//                }else{
//                    click(activeWeek.get(i));
//                }
//            }
//        }
//
//    }

//    public void legionIsDisplayingTheSchedul() throws Exception {
//        if(isElementLoaded(groupByAllIcon,10)){
//            SimpleUtils.pass("Legion schedule is displaying");
//        }else {
//            SimpleUtils.fail("Legion Schedule load failed", true);
//        }
//    }


//    public void currentWeekIsGettingOpenByDefault(String location) throws Exception {
//        String jsonTimeZoon = propertyTimeZoneMap.get(location);
//        TimeZone timeZone = TimeZone.getTimeZone(jsonTimeZoon);
//        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
//        dfs.setTimeZone(timeZone);
//        String currentTime =  dfs.format(new Date());
//        Date currentDate = dfs.parse(currentTime);
//        String weekBeginEndByCurrentDate = SimpleUtils.getThisWeekTimeInterval(currentDate);
//        String weekBeginEndByCurrentDate2 = weekBeginEndByCurrentDate.replace("-","").replace(",","");
//        String weekBeginBYCurrentDate = weekBeginEndByCurrentDate2.substring(6,8);
//        String weekEndBYCurrentDate = weekBeginEndByCurrentDate2.substring(weekBeginEndByCurrentDate2.length()-2);
//        SimpleUtils.report("weekBeginBYCurrentDate is : "+ weekBeginBYCurrentDate);
//        SimpleUtils.report("weekEndBYCurrentDate is : "+ weekEndBYCurrentDate);
//        String activeWeekText =getActiveWeekText();
//        String weekDefaultBegin = activeWeekText.substring(14,16);
//        SimpleUtils.report("weekDefaultBegin is :"+weekDefaultBegin);
//        String weekDefaultEnd = activeWeekText.substring(activeWeekText.length()-2);
//        SimpleUtils.report("weekDefaultEnd is :"+weekDefaultEnd);
//        if (SimpleUtils.isNumeric(weekBeginBYCurrentDate.trim()) && SimpleUtils.isNumeric(weekDefaultBegin.trim()) &&
//                SimpleUtils.isNumeric(weekEndBYCurrentDate.trim()) && SimpleUtils.isNumeric(weekDefaultEnd.trim())) {
//            if (Math.abs(Integer.parseInt(weekBeginBYCurrentDate.trim()) - Integer.parseInt(weekDefaultBegin.trim())) <= 1 &&
//                    Math.abs(Integer.parseInt(weekEndBYCurrentDate.trim()) - Integer.parseInt(weekDefaultEnd.trim())) <= 1 &&
//                    (Math.abs(Integer.parseInt(weekBeginBYCurrentDate.trim()) - Integer.parseInt(weekDefaultBegin.trim())) ==
//                            Math.abs(Integer.parseInt(weekEndBYCurrentDate.trim()) - Integer.parseInt(weekDefaultEnd.trim())))) {
//                SimpleUtils.pass("Current week is getting open by default");
//            } else {
//                SimpleUtils.fail("Current week is not getting open by default", false);
//            }
//        }else {
//            SimpleUtils.fail("The date is not the numeric format!", false);
//        }
//    }

//    public void goToScheduleNewUI() throws Exception {
//
//        if (isElementLoaded(goToScheduleButton,5)) {
//            click(goToScheduleButton);
//            click(ScheduleSubMenu);
//            if (isElementLoaded(todoButton,5)) {
//                SimpleUtils.pass("Schedule New UI load successfully");
//            }
//        }else{
//            SimpleUtils.fail("Schedule New UI load failed", true);
//        }
//
//    }


//    public void dayWeekPickerSectionNavigatingCorrectly()  throws Exception{
//        String weekIcon = "Sun - Sat";
//        String activeWeekText = getActiveWeekText();
//        if(activeWeekText.contains(weekIcon)){
//            SimpleUtils.pass("Week pick show correctly");
//        }else {
//            SimpleUtils.fail("it's not week mode", true);
//        }
//        click(daypButton);
//        if(isElementLoaded(daypicker,3)){
//            SimpleUtils.pass("Day pick show correctly");
//        }else {
//            SimpleUtils.fail("change to day pick failed", true);
//        }
//
//    }
//
//
//    public void landscapePortraitModeShowWellInWeekView() throws Exception {
//        if (isElementLoaded(printButton,10)) {
////            click(printButton);
//            waitForSeconds(5);
//            if(isElementLoaded(LandscapeButton)&isElementLoaded(PortraitButton)){
//                SimpleUtils.pass("Landscape and Portrait mode show well");
//            }else {
//                SimpleUtils.fail("Landscape and Portrait load failed", true);
//            }
//            click(PortraitButton);
//            click(LandscapeButton);
//            SimpleUtils.pass("In Week view should be able to change the mode between Landscape and Portrait ");
//            click(cannelButtonInPrintLayout);
//        } else {
//            SimpleUtils.fail("Print button can not work", true);
//        }
//    }
//
//    public void landscapeModeWorkWellInWeekView() throws Exception {
//        String currentWindow =getDriver().getWindowHandle();
//        if (isElementLoaded(printButton,5)) {
//            click(printButton);
//            click(LandscapeButton);
//            click(printButtonInPrintLayout);
//            if(!isElementLoaded(LandscapeButton,6)){
//                String downloadPath = SimpleUtils.fileDownloadPath;
//                Assert.assertTrue(FileDownloadVerify.isFileDownloaded_Ext(downloadPath, "WeekViewSchedulePdf"), "print successfully");
//                SimpleUtils.pass("Landscape print work well");
//            }else{
//                SimpleUtils.fail("Can not print by Landscape", true);
//            }
//            getDriver().switchTo().window(currentWindow);
//        } else {
//            SimpleUtils.fail("Print button is not clickable", true);
//        }
//
//    }
//
//    public void portraitModeWorkWellInWeekView() throws Exception {
//        String currentWindow =getDriver().getWindowHandle();
//
//        if (isElementLoaded(printButton,3)) {
//            click(printButton);
//            click(PortraitButton);
//            click(printButtonInPrintLayout);
//            if(!isElementLoaded(PortraitButton,6)){
//                String downloadPath = SimpleUtils.fileDownloadPath;
//                Assert.assertTrue(FileDownloadVerify.isFileDownloaded_Ext(downloadPath, "WeekViewSchedulePdf"), "print successfully");
//                SimpleUtils.pass("Portrait print work well");
//            }else{
//                SimpleUtils.fail("Can not print by portrait", true);
//            }
//            getDriver().switchTo().window(currentWindow);
//        } else {
//            SimpleUtils.fail("Print button is not clickable", true);
//        }
//
//    }
//
//    public void landscapeModeOnlyInDayView() throws Exception {
//        if (isElementLoaded(printButton,10)) {
//            scrollToTop();
//            waitForSeconds(5);
//            click(printButton);
//            if(isClickable(LandscapeButton,5)){
//
//            }else {
//                SimpleUtils.pass("print in Landscape mode only.");
//            }
//        }else {
//            SimpleUtils.fail("print button is not clickable",false);
//        }
//    }
//
//    public enum DayOfWeek {
//        Mon,
//        Tue,
//        Wed,
//        Thu,
//        Fri,
//        Sat,
//        Sun;
//    }

//    public void weatherWeekSmartCardIsDisplayedForAWeek() throws Exception {
//        if (isElementLoaded(smartcardArrowRight,5)) {
//            click(smartcardArrowRight);
//            String jsonTimeZoon = parametersMap2.get("Time_Zone");
//            TimeZone timeZone = TimeZone.getTimeZone(jsonTimeZoon);
//            SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
//            dfs.setTimeZone(timeZone);
//            String currentTime =  dfs.format(new Date());
//            int currentDay = Integer.valueOf(currentTime.substring(currentTime.length()-2));
//            try{
//                String firstDayInWeatherSmtCad2 = getDriver().findElement(By.xpath("//*[contains(text(),'Weather - Week of')]")).getText();
//                int firstDayInWeatherSmtCad = Integer.valueOf(firstDayInWeatherSmtCad2.substring(firstDayInWeatherSmtCad2.length() - 2));
//                System.out.println("firstDayInWeatherSmtCad:" + firstDayInWeatherSmtCad);
//                if ((firstDayInWeatherSmtCad + 7) > currentDay) {
//                    SimpleUtils.pass("The week smartcard is current week");
//                    if (areListElementVisible(weatherTemperatures, 8)) {
//                        String weatherWeekTest = getWeatherDayOfWeek();
//                        SimpleUtils.report("Weather smart card is displayed for a week from mon to sun" + weatherWeekTest);
//                        for (ScheduleTestKendraScott2.DayOfWeek e : ScheduleTestKendraScott2.DayOfWeek.values()) {
//                            if (weatherWeekTest.contains(e.toString())) {
//                                SimpleUtils.pass("Weather smartcard include one week weather");
//                            } else {
//                                SimpleUtils.fail("Weather Smart card is not one whole week", false);
//                            }
//                        }
//                    } else {
//                        SimpleUtils.fail("there is no week weather smartcard", false);
//                    }
//                } else {
//                    SimpleUtils.fail("This is not current week weather smartcard ", false);
//                }
//            } catch (Exception e){
//                SimpleUtils.report("there is no week weather smartcard!");
//            }
//        }else {
//            String jsonTimeZoon = parametersMap2.get("Time_Zone");
//            TimeZone timeZone = TimeZone.getTimeZone(jsonTimeZoon);
//            SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
//            dfs.setTimeZone(timeZone);
//            String currentTime =  dfs.format(new Date());
//            int currentDay = Integer.valueOf(currentTime.substring(currentTime.length()-2));
//            try{
//                String firstDayInWeatherSmtCad2 = getDriver().findElement(By.xpath("//*[contains(text(),'Weather - Week of')]")).getText();
//                int firstDayInWeatherSmtCad = Integer.valueOf(firstDayInWeatherSmtCad2.substring(firstDayInWeatherSmtCad2.length() - 2));
//                System.out.println("firstDayInWeatherSmtCad:" + firstDayInWeatherSmtCad);
//                if ((firstDayInWeatherSmtCad + 7) > currentDay) {
//                    SimpleUtils.pass("The week smartcard is current week");
//                    if (areListElementVisible(weatherTemperatures, 8)) {
//                        String weatherWeekTest = getWeatherDayOfWeek();
//                        SimpleUtils.report("Weather smart card is displayed for a week from mon to sun" + weatherWeekTest);
//                        for (ScheduleTestKendraScott2.DayOfWeek e : ScheduleTestKendraScott2.DayOfWeek.values()) {
//                            if (weatherWeekTest.contains(e.toString())) {
//                                SimpleUtils.pass("Weather smartcard include one week weather");
//                            } else {
//                                SimpleUtils.fail("Weather Smart card is not one whole week", false);
//                            }
//                        }
//
//                    } else {
//                        SimpleUtils.fail("there is no week weather smartcard", false);
//                    }
//
//                } else {
//                    SimpleUtils.fail("This is not current week weather smartcard ", false);
//                }
//            } catch (Exception e){
//                SimpleUtils.report("there is no week weather smartcard!");
//            }
//        }
//
//    }



//    public String getScheduleDayRange() throws Exception {
//        String dayRangeText = "";
//        if (schCalendarDateLabel.size() != 0)
//            for (WebElement scheCalDay : schCalendarDateLabel) {
//                if (scheCalDay.isDisplayed()) {
//                    if (dayRangeText == "")
//                        dayRangeText = scheCalDay.getText();
//                    else
//                        dayRangeText = dayRangeText + " | " + scheCalDay.getText();
//                } else if (!scheCalDay.isDisplayed()) {
//                    while (isSmartCardScrolledToRightActive() == true) {
//                        if (dayRangeText == "")
//                            dayRangeText = scheCalDay.getText();
//                        else
//                            dayRangeText = dayRangeText + " | " + scheCalDay.getText();
//                    }
//                }
//            }
//
//        return dayRangeText;
//    }

//    public void scheduleUpdateAccordingToSelectWeek() throws Exception {
//        if (isElementLoaded(calendarNavigationPreviousWeek,5) ) {
//            String preWeekText = calendarNavigationPreviousWeek.getText().replace("\n","").replace("-","");
//            String preWeekText2 = preWeekText.trim().substring(preWeekText.length()-2);
//            click(calendarNavigationPreviousWeek);
//            String scheCalDay = getScheduleDayRange().trim();
//            if (areListElementVisible(schCalendarDateLabel,10) && scheCalDay.trim().contains(preWeekText2.trim())) {
//                SimpleUtils.pass("data is getting updating on Schedule page according to corresponding week");
//            }else {
//                SimpleUtils.fail("schedule canlendar is not updating according to corresponding week",true);
//            }
//        }else {
//            SimpleUtils.fail("no next week calendar",true);
//        }
//    }

//
//
//    @Override
//    public boolean verifyRedFlagIsVisible() throws Exception {
//        if (isElementLoaded(redFlagInCompliance, 20)) {
//            SimpleUtils.report("red flag is visible ");
//            return true;
//        }
//        return false;
//    }

//    @Override
//    public boolean verifyComplianceShiftsSmartCardShowing() throws Exception {
//        if (isElementLoaded(complianceSmartcardHeader,15)) {
//            SimpleUtils.pass("Compliance smartcard is visible ");
//            return true;
//        } else {
//            SimpleUtils.report("there is no compliance smartcard this week");
//            return false;
//        }
//    }
//
//    @Override
//    public void verifyComplianceShiftsShowingInGrid() throws Exception {
//        if (isElementLoaded(complianceSmartcardHeader,15)) {
//            if (complianceShitShowIcon.size() > 0) {
//                SimpleUtils.pass("Compliance shift is showing in grid");
//            }else {
//                SimpleUtils.fail("compliance shifts display failed",false);
//            }
//        }else {
//            SimpleUtils.report("there is no compliance smartcard in current week");
//        }
//
//    }
//
//    @Override
//    public void verifyClearFilterFunction() throws Exception {
//        String clearFilterBtnTextDefault = "Clear Filter";
//
//        if (isElementLoaded(complianceSmartcardHeader,10) ) {
//            String clearFilterTxt =viewShift.getText();
//            SimpleUtils.report("clear filter is" + clearFilterTxt);
//            if (clearFilterBtnTextDefault.equals(clearFilterTxt)) {
//                click(viewShift);
//                SimpleUtils.pass("clear filter button is clickable");
//                String filterText = getDriver().findElement(By.cssSelector("lg-filter > div > input-field > ng-form > div")).getText();
//                if (filterText.equals("")) {
//                    SimpleUtils.pass("filter 'Compliance shifts' will be unselected after clicking clear filter");
//                }
//            }else
//                SimpleUtils.fail("clear filter  button can't clickable",true);
//        }else
//            SimpleUtils.report("there is no compliance shift this week");
//
//    }
//
//    @Override
//    public void clickOnFilterBtn() throws Exception {
//        waitForSeconds(10);
//        if (isElementLoaded(filterButton,30)) {
//            click(filterButton);
//            SimpleUtils.pass("filter button is clickable");
//        } else {
//            SimpleUtils.fail("filter button is not Loaded Successfully!", true);
//        }
//    }


//    @Override
//    public void verifyShiftSwapCoverRequestedIsDisplayInTo() {
////        if () {
////
////        }
//    }

//    @Override
//    public void verifyAnalyzeBtnFunctionAndScheduleHistoryScroll() throws Exception {
//        clickOnScheduleAnalyzeButton();
//        for (WebElement e:scheduleHistoryListInAnalyzePopUp
//        ) {
//            if(verifyScrollBarWorkingInAnalyzePopUP(e)){
//                SimpleUtils.report("Staffing Guidance Schedule History-Scrollbar is working correctly version x details");
//
//            }else {
//                SimpleUtils.fail("Staffing Guidance Schedule History-Scrollbar is not working correctly version x details",true);
//            }
//        }
//        if (isElementLoaded(scheduleAnalyzePopupCloseButtonInKS2,10)){
//            click(scheduleAnalyzePopupCloseButtonInKS2);
//            SimpleUtils.pass("close button is working");
//        } else if (isElementLoaded(scheduleAnalyzePopupCloseButton)){
//            click(scheduleAnalyzePopupCloseButton);
//            SimpleUtils.pass("close button is working");
//        } else{
//            SimpleUtils.fail("No close button on schedule analyse popup",true);
//        }
//
//    }

//    @Override
//    public HashMap<String, Float> getScheduleBudgetedHoursInScheduleSmartCard() throws Exception {
//            /*
//            wait schedule smart card data load
//            */
//        waitForSeconds(10);
//        if (isElementLoaded(scheduleSmartCard,20) ){
//            SmartCardPage smartCardPage = new ConsoleSmartCardPage();
//            HashMap<String, Float> hoursWagesText = smartCardPage.getScheduleLabelHoursAndWages();
//            return hoursWagesText;
//        }
//        return null;
//    }

//    public boolean verifyScrollBarWorkingInAnalyzePopUP(WebElement element) throws Exception {
//        if (areListElementVisible(scheduleHistoryListInAnalyzePopUp,10)&scheduleHistoryListInAnalyzePopUp.size()>4) {
//            SimpleUtils.report("versions are more enough and there is a scroll bar to check details");
//            scrollToElement(element);
//            click(element);
//            String versionNubScrollToText = versionDetailsInAnalyzePopUp.getText().trim().split(" ")[1];
//            if (versionNubScrollToText.equals(element.getText().trim().split(" ")[1])) {
//                SimpleUtils.pass("scroll bar can work normally");
//                return  true;
//            }else {
//                SimpleUtils.fail("scroll bar can not  work normally",true);
//            }
//        }else if(scheduleHistoryListInAnalyzePopUp.size()<=4){
//
//            SimpleUtils.report("there are some versions,but not scroll bar");
//            click(element);
//            String versionNubScrollToText = versionDetailsInAnalyzePopUp.getText().trim().split(" ")[1];
//            if (versionNubScrollToText.equals(element.getText().trim().split(" ")[1])) {
//                SimpleUtils.pass("schedule version work well");
//                return  true;
//            }else {
//                SimpleUtils.fail("schedule version doesn't work well",true);
//            }
//
//        }
//        return  false;
//
//
//    }
//    private void goToPostWeekNextToCurrentWeek() throws Exception {
//        if (isElementLoaded(postWeekNextToCurrentWeek,5)) {
//            click(postWeekNextToCurrentWeek);
//            SimpleUtils.pass("navigate to post week successfully");
//        }else {
//            SimpleUtils.fail("post week tab load failed",true);
//        }
//    }
//
//    private void goToFutureWeekNextToCurrentWeek() throws Exception {
//        if (isElementLoaded(futureWeekNextToCurrentWeek,5)) {
//            click(futureWeekNextToCurrentWeek);
//            SimpleUtils.pass("navigate to future week successfully");
//        }else {
//            SimpleUtils.fail("future week tab load failed",true);
//        }
//    }
//
//    @Override
//    public boolean clickViewShift() throws Exception {
//        if (isElementLoaded(viewShiftBtn, 15)) {
//            click(viewShiftBtn);
//            SimpleUtils.report("View shift button is visible ");
//            return true;
//        }else
//            SimpleUtils.report("No view shift button");
//        return false;
//    }

//    @Override
//    public void verifyComplianceFilterIsSelectedAftClickingViewShift() throws Exception {
//        String filterTextDefault =" Compliance Review\n" +
//                "    ";
//        if (clickViewShift() == true) {
//            String filterText = getDriver().findElement(By.cssSelector("lg-filter > div > input-field > ng-form > div")).getText();
//            if (filterText.equals(filterTextDefault)) {
//                SimpleUtils.report("Compliance filter is selected after clicking view shift button");
//            }
//        }else {
//            SimpleUtils.report("there is no compliance");
//        }
//    }

    //added by Nishant
//    @Override
//    public void displayAlertPopUpForRoleViolation() throws Exception{
//        String msgAlert = null;
//        if(isElementLoaded(popUpScheduleOverlap,5)){
//            if(isElementLoaded(alertMessage,5)){
//                msgAlert = alertMessage.getText();
//                if(isElementLoaded(btnAssignAnyway,5)){
//                    SimpleUtils.pass("Role violation messsage as such as " + msgAlert);
//                    click(btnAssignAnyway);
//                }else{
//                    SimpleUtils.fail("Assign Anyway button not displayed on the page",false);
//                }
//            }else{
//                SimpleUtils.fail("Alert message for not displayed",false);
//            }
//        }else{
//            SimpleUtils.fail("Role Violation pop up not displayed",false);
//        }
//    }

//    // Added by Nora: for Team Member View
//    @FindBy(className = "sch-day-view-shift-worker-detail")
//    private List<WebElement> tmIcons;
//    @FindBy(className = "sch-worker-popover")
//    private WebElement popOverLayout;
    @FindBy(css = "span.sch-worker-action-label")
    private List<WebElement> shiftRequests;
//    @FindBy(css = "div.lg-modal")
//    private WebElement popUpWindow;
//    @FindBy(className = "lg-modal__title-icon")
//    private WebElement popUpWindowTitle;
//    @FindBy(css = "[label=\"Cancel\"]")
//    private WebElement cancelButton;
//    @FindBy(css = "[label=\"Submit\"]")
//    private WebElement submitButton;
//    @FindBy(css = "[label=\"Back\"]")
//    private WebElement backButton;
//    @FindBy(css = "textarea[placeholder]")
//    private WebElement messageText;
//    @FindBy(className = "lgn-alert-modal")
//    private WebElement confirmWindow;
//    @FindBy(className = "lgn-action-button-success")
//    private WebElement okBtnOnConfirm;
//    @FindBy(css = "[ng-repeat*=\"shift in results\"]")
//    private List<WebElement> comparableShifts;
//    @FindBy(css = "[label=\"Next\"]")
//    private WebElement nextButton;
//    @FindBy(css = "[label=\"Cancel Cover Request\"]")
//    private WebElement cancelCoverBtn;
//    @FindBy(css = "[label=\"Cancel Swap Request\"]")
//    private WebElement cancelSwapBtn;
//    @FindBy(css = ".shift-swap-modal-table th.ng-binding")
//    private WebElement resultCount;
//    @FindBy(css = "td.shift-swap-modal-shift-table-select>div")
//    private List<WebElement> selectBtns;
//    @FindBy(css = ".modal-content .sch-day-view-shift-outer")
//    private List<WebElement> swapRequestShifts;
//    @FindBy(css = "[label=\"Accept\"] button")
//    private List<WebElement> acceptButtons;
//    @FindBy(css = "[label=\"I agree\"]")
//    private WebElement agreeButton;
//    @FindBy(css = "lg-close.dismiss")
//    private WebElement closeDialogBtn;
//    @FindBy(className = "shift-swap-offer-time")
//    private WebElement shiftOfferTime;
//    @FindBy(className = "shift-swap-modal-table-shift-status")
//    private List<WebElement> shiftStatus;
//
//    @Override
//    public void navigateToNextWeek() throws Exception {
//        int currentWeekIndex = -1;
//        if (areListElementVisible(currentWeeks, 10)) {
//            for (int i = 0; i < currentWeeks.size(); i++) {
//                String className = currentWeeks.get(i).getAttribute("class");
//                if (className.contains("day-week-picker-period-active")) {
//                    currentWeekIndex = i;
//                }
//            }
//            if (currentWeekIndex == (currentWeeks.size() - 1) && isElementLoaded(calendarNavigationNextWeekArrow, 5)) {
//                clickTheElement(calendarNavigationNextWeekArrow);
//                if (areListElementVisible(currentWeeks, 5)) {
//                    clickTheElement(currentWeeks.get(0));
//                    SimpleUtils.pass("Navigate to next week: '" + currentWeeks.get(0).getText() + "' Successfully!");
//                }
//            }else {
//                clickTheElement(currentWeeks.get(currentWeekIndex + 1));
//                SimpleUtils.pass("Navigate to next week: '" + currentWeeks.get(currentWeekIndex + 1).getText() + "' Successfully!");
//            }
//        }else {
//            SimpleUtils.fail("Current weeks' elements not loaded Successfully!", false);
//        }
//    }

//    @Override
//    public void verifyShiftRequestStatus(String expectedStatus) throws Exception {
//        if (areListElementVisible(shiftStatus, 10)) {
//            for (WebElement status : shiftStatus) {
//                if (expectedStatus.equalsIgnoreCase(status.getText())) {
//                    SimpleUtils.pass("Verified shift status: " + expectedStatus + " is correct!");
//                }else {
//                    SimpleUtils.fail("Shift status is incorrect, expected is: " + expectedStatus + ", but actual is: "
//                            + status.getText(), false);
//                }
//            }
//        }else {
//            SimpleUtils.fail("Shift Status not loaded Successfully!", false);
//        }
//    }

//    @FindBy(css = "button.sch-publish-cancel-btn")
//    WebElement cancelPublish;
//    @Override
//    public void clickOnCancelPublishBtn() throws Exception {
//        if (isElementLoaded(cancelPublish,10)){
//            clickTheElement(cancelPublish);
//            SimpleUtils.pass("cancel publish button clicked");
//        } else {
//            SimpleUtils.fail("Didn't find cancel button.", false);
//        }
//    }

//    @Override
//    public void verifyClickAcceptSwapButton() throws Exception {
//        String title = "Confirm Shift Swap";
//        String expectedMessage = "Success";
//        if (areListElementVisible(acceptButtons, 5) && acceptButtons.size() > 0) {
//            clickTheElement(acceptButtons.get(0));
//            SimpleUtils.assertOnFail(title + " not loaded Successfully!", isPopupWindowLoaded(title), false);
//            if (isElementLoaded(agreeButton, 5)) {
//                click(agreeButton);
//                verifyThePopupMessageOnTop(expectedMessage);
//                verifySwapRequestDeclinedDialogPopUp();
//                if (isElementLoaded(closeDialogBtn, 5)) {
//                    clickTheElement(closeDialogBtn);
//                }
//            }else {
//                SimpleUtils.fail("I Agree button not loaded Successfully!", false);
//            }
//        }else {
//            SimpleUtils.fail("Accept Button not loaded Successfully!", false);
//        }
//    }

//    private void verifySwapRequestDeclinedDialogPopUp() throws Exception {
//        try {
//            // Same elements sa Delete Schedule pop up
//            if (isElementLoaded(deleteScheduleTitle, 10) && deleteScheduleTitle.getText().equalsIgnoreCase("Swap Request Declined")) {
//                if (isElementLoaded(deleteButtonOnDeleteSchedulePopup, 10)) {
//                    clickTheElement(deleteButtonOnDeleteSchedulePopup);
//                    SimpleUtils.pass("Click on 'OK' button Successfully on 'Swap Request Declined' dialog!");
//                }
//            }
//        } catch (Exception e) {
//            // Do nothing
//        }
//    }
//
//    @Override
//    public void verifySwapRequestShiftsLoaded() throws Exception {
//        if (areListElementVisible(swapRequestShifts, 5)) {
//            SimpleUtils.pass("Swap Request Shifts loaded Successfully!");
//        }else {
//            SimpleUtils.fail("Swap Request Shifts not loaded Successfully!", false);
//        }
//    }

//    @Override
//    public void verifyTheRedirectionOfBackButton() throws Exception {
//        verifyBackNSubmitBtnLoaded();
//        click(backButton);
//        String title = "Find Shifts to Swap";
//        if (isPopupWindowLoaded(title)) {
//            SimpleUtils.pass("Redirect to Find Shifts to Swap windows Successfully!");
//        }else {
//            SimpleUtils.fail("Failed to redirect to Find Shifts to Swap window!", false);
//        }
//    }
//
//    @Override
//    public void verifyBackNSubmitBtnLoaded() throws Exception {
//        if (isElementLoaded(backButton, 5)) {
//            SimpleUtils.pass("Back button loaded Successfully on submit swap request!");
//        }else {
//            SimpleUtils.fail("Back button not loaded Successfully on submit swap request!", true);
//        }
//        if (isElementLoaded(submitButton, 5)) {
//            SimpleUtils.pass("Submit button loaded Successfully on submit swap request!");
//        }else {
//            SimpleUtils.fail("Submit button not loaded Successfully on submit swap request!", false);
//        }
//    }

//    @Override
//    public void verifyClickOnNextButtonOnSwap() throws Exception {
//        verifySelectOneShiftNVerifyNextButtonEnabled();
//        click(nextButton);
//    }
//
//    @Override
//    public void verifySelectMultipleSwapShifts() throws Exception {
//        String selected = "selected";
//        if (areListElementVisible(selectBtns, 5) && selectBtns.size() > 0) {
//            for (WebElement selectBtn : selectBtns) {
//                String className = selectBtn.getAttribute("class");
//                if (className.isEmpty()) {
//                    click(selectBtn);
//                    className = selectBtn.getAttribute("class");
//                    if (className.contains(selected)) {
//                        SimpleUtils.pass("Select one shift Successfully!");
//                    }else {
//                        SimpleUtils.fail("Failed to select the shift!", false);
//                    }
//                }
//            }
//        }else {
//            SimpleUtils.fail("Select Buttons not loaded Successfully!", false);
//        }
//    }
//
//    @Override
//    public void verifySelectOneShiftNVerifyNextButtonEnabled() throws Exception {
//        String selected = "selected";
//        if (areListElementVisible(selectBtns, 10) && selectBtns.size() > 0) {
//            if (selectBtns.get(0).getAttribute("class").isEmpty()) {
//                click(selectBtns.get(0));
//            }
//            if (selectBtns.get(0).getAttribute("class").contains(selected)) {
//                SimpleUtils.pass("Select one shift Successfully!");
//                if (isElementEnabled(nextButton, 5)) {
//                    SimpleUtils.pass("Next Button is enabled after selecting one shift!");
//                }else {
//                    SimpleUtils.fail("Next button is still disabled after selecting one shift!", false);
//                }
//            }else {
//                SimpleUtils.fail("Failed to select the shift!", false);
//            }
//        }else {
//            SimpleUtils.fail("Select Buttons not loaded Successfully!", false);
//        }
//    }
//
//    @Override
//    public void verifyNextButtonIsLoadedAndDisabledByDefault() throws Exception {
//        String notAllowed = "not-allowed";
//        if (isElementLoaded(nextButton, 5)) {
//            SimpleUtils.pass("Next button loaded Successfully!");
//            WebElement button = nextButton.findElement(By.tagName("button"));
//            String cursorAttribute = button == null ? "" : button.getCssValue("cursor");
//            if (notAllowed.equalsIgnoreCase(cursorAttribute)) {
//                SimpleUtils.pass("Next button is disabled by default!");
//            }else {
//                SimpleUtils.fail("Next button should be disabled by default, but it is enabled!", false);
//            }
//        }else {
//            SimpleUtils.fail("Next button not loaded Successfully!", false);
//        }
//    }

//    @Override
//    public void verifyTheSumOfSwapShifts() throws Exception {
//        int sum = 0;
//        if (isElementLoaded(resultCount, 5) && areListElementVisible(comparableShifts, 5)) {
//            String result = resultCount.getText();
//            if (result.length() > 7) {
//                sum = Integer.parseInt(result.substring(0, result.length() - 7).trim());
//            }
//            if (sum == comparableShifts.size()) {
//                SimpleUtils.pass("Verified Sum of swap shifts is correct!");
//            }else {
//                SimpleUtils.fail("Sum of swap shifts is incorrect, showing sum is: " + sum + ", but actual is: " + comparableShifts.size(), false);
//            }
//        }else {
//            SimpleUtils.fail("Sum results and comparable shifts not loaded Successfully!", false);
//        }
//    }
//
//    @Override
//    public void verifyTheDataOfComparableShifts() throws Exception {
//        if (areListElementVisible(comparableShifts, 5)) {
//            for (WebElement comparableShift : comparableShifts) {
//                WebElement name = comparableShift.findElement(By.className("shift-swap-modal-table-name"));
//                WebElement dateNTime = comparableShift.findElement(By.className("shift-swap-modal-table-shift-info"));
//                WebElement location = comparableShift.findElement(By.className("shift-swap-modal-table-home-location"));
//                if (name != null && dateNTime != null && location != null && !name.getText().isEmpty() &&
//                        !dateNTime.getText().isEmpty() && !location.getText().isEmpty()) {
//                    SimpleUtils.report("Verified name: " + name.getText() + ", date and time: " + dateNTime.getText() +
//                            ", location: " + location.getText() + " are loaded!");
//                }else {
//                    SimpleUtils.fail("The data of the comparable shift is incorrect!", false);
//                    break;
//                }
//            }
//        }else {
//            SimpleUtils.fail("Comparable shifts not loaded Successfully!", false);
//        }
//    }

//    @Override
//    public void clickCancelButtonOnPopupWindow() throws Exception {
//        if (isElementLoaded(cancelButton, 5)) {
//            click(cancelButton);
//            if (!isElementLoaded(popUpWindow, 5)) {
//                SimpleUtils.pass("Pop up window get closed after clicking cancel button!");
//            }else {
//                SimpleUtils.fail("Pop up window still shows after clicking cancel button!", false);
//            }
//        }else {
//            SimpleUtils.fail("Cancel Button not loaded Successfully on pop up window!", false);
//        }
//    }

//    @Override
//    public void verifyClickCancelSwapOrCoverRequest() throws Exception {
//        if (isElementLoaded(cancelCoverBtn, 5)) {
//            click(cancelCoverBtn);
//        }
//        if (isElementLoaded(cancelSwapBtn, 5)) {
//            click(cancelSwapBtn);
//        }
//        if (isElementLoaded(okBtnOnConfirm, 5)) {
//            click(okBtnOnConfirm);
//        }else {
//            SimpleUtils.fail("Confirm Button failed to load!", true);
//        }
//    }
//
//    @Override
//    public String selectOneTeamMemberToSwap() throws Exception {
//        String tmName = "";
//        if (areListElementVisible(comparableShifts, 5) && isElementLoaded(nextButton, 5)) {
//            int randomIndex = (new Random()).nextInt(comparableShifts.size());
//            WebElement selectBtn = comparableShifts.get(randomIndex).findElement(By.cssSelector("td.shift-swap-modal-shift-table-select>div"));
//            WebElement name = comparableShifts.get(randomIndex).findElement(By.className("shift-swap-modal-table-name"));
//            click(selectBtn);
//            tmName = name.getText();
//            SimpleUtils.pass("Select team member: " + tmName + " Successfully!");
//            click(nextButton);
//            verifyClickOnSubmitButton();
//        }else {
//            SimpleUtils.fail("Comparable Shifts not loaded Successfully!", false);
//        }
//        return tmName;
//    }
//
//    @Override
//    public void verifyComparableShiftsAreLoaded() throws Exception {
//        if (areListElementVisible(comparableShifts, 60)) {
//            SimpleUtils.pass("Comparable Shifts loaded Successfully!");
//        }else {
//            SimpleUtils.fail("Comparable Shifts not loaded Successfully!", false);
//        }
//    }

//    @Override
//    public boolean verifyShiftRequestButtonOnPopup(List<String> requests) throws Exception {
//        boolean isConsistent = false;
//        List<String> shiftRequestsOnUI = new ArrayList<>();
//        if (areListElementVisible(shiftRequests, 5)) {
//            for (WebElement shiftRequest : shiftRequests) {
//                shiftRequestsOnUI.add(shiftRequest.getText());
//            }
//        }
//        if (shiftRequestsOnUI.containsAll(requests) && requests.containsAll(shiftRequestsOnUI)) {
//            isConsistent = true;
//            SimpleUtils.pass("Shift Requests loaded Successfully!");
//        }
//        return isConsistent;
//    }
//
//    @Override
//    public void clickOnShiftByIndex(int index) throws Exception {
//        if (areListElementVisible(tmIcons, 5)) {
//            if (index < tmIcons.size()) {
//                moveToElementAndClick(tmIcons.get(index));
//            }else {
//                SimpleUtils.fail("Index: " + index + " is out of range, the total size is: " + tmIcons.size(), true);
//            }
//        } else if (areListElementVisible(shifts, 10)) {
//            clickTheElement(profileIcons.get(index));
//        } else {
//            SimpleUtils.fail("Shift Requests not loaded Successfully!", true);
//        }
//    }
//
//    @Override
//    public void verifyShiftInfoIsCorrectOnMealBreakPopUp(List<String> expectedShiftInfo) throws Exception {
//        try {
//            if (isElementLoaded(shiftInfoContainer, 10)) {
//                String actualShiftInfo = shiftInfoContainer.getText();
//                if (actualShiftInfo.contains(expectedShiftInfo.get(0)) && actualShiftInfo.contains(expectedShiftInfo.get(3)) &&
//                actualShiftInfo.contains(expectedShiftInfo.get(4)) && actualShiftInfo.contains(expectedShiftInfo.get(2))) {
//                    SimpleUtils.pass("Shift info on the Meal Break pop up is correct!");
//                } else {
//                    SimpleUtils.fail("Shift info on the Meal Break pop up is correct!", false);
//                }
//            } else {
//                SimpleUtils.fail("Shift container failed to load on meal break pop up!", false);
//            }
//        } catch (Exception e) {
//            SimpleUtils.fail(e.getMessage(), false);
//        }
//    }
//
//    @FindBy(css = ".noUi-connect.color_meal")
//    private List<WebElement> mealBreakDurations;
//    @FindBy(css = ".noUi-connect.color_rest")
//    private List<WebElement> restBreakDurations;
//    @FindBy(css = "[ng-click*=\"addBreak\"]")
//    private List<WebElement> addBreakBtns;
//    @FindBy(css = ".noUi-touch-area.color_meal")
//    private List<WebElement> mealStartEndAreas;
//    @FindBy(css = ".noUi-touch-area.color_rest")
//    private List<WebElement> restStartEndAreas;
//
//    @Override
//    public void verifyBreakTimesAreUpdated(List<String> expectedBreakTimes) throws Exception {
//        int count = 0;
//        if (areListElementVisible(mealBreakTimes, 5) && areListElementVisible(restBreakTimes, 5)) {
//            for (WebElement meal : mealBreakTimes) {
//                if (expectedBreakTimes.contains(meal.getText())) {
//                    count = count + 1;
//                }
//            }
//            for (WebElement rest : restBreakTimes) {
//                if (expectedBreakTimes.contains(rest.getText())) {
//                    count = count + 1;
//                }
//            }
//            if (count == expectedBreakTimes.size()) {
//                SimpleUtils.pass("Meal and rest break times are updated successfully!");
//            } else {
//                SimpleUtils.fail("Meal and rest break times are not updated successfully!", false);
//            }
//        } else {
//            SimpleUtils.fail("Meal and rest break times are not updated successfully!", false);
//        }
//    }
//
//    @Override
//    public void verifyMealBreakAndRestBreakArePlacedCorrectly() throws Exception {
//        try {
//            if (areListElementVisible(restBreakTimes, 5)) {
//                if (areListElementVisible(restBreakDurations, 5) && restBreakDurations.size() == restBreakTimes.size()) {
//                    SimpleUtils.pass("Rest breaks are shown!");
//                } else {
//                    SimpleUtils.fail("Rest breaks show incorrectly!", false);
//                }
//            }
//            if (areListElementVisible(mealBreakTimes, 5)) {
//                if (areListElementVisible(mealBreakDurations, 5) && mealBreakDurations.size() == mealBreakTimes.size()) {
//                    SimpleUtils.pass("Meal breaks are shown!");
//                } else {
//                    SimpleUtils.fail("Meal breaks show incorrectly!", false);
//                }
//            }
//        } catch (Exception e) {
//            // Do nothing
//        }
//    }

//    @Override
//    public List<String> verifyEditBreaks() throws Exception {
//        List<String> breakTimes = new ArrayList<>();
//        if (isMealBreakTimeWindowDisplayWell(true)) {
//            // Verify delete breaks functionality
//            while(deleteMealBreakButtons.size()>0){
//                click(deleteMealBreakButtons.get(0));
//            }
//            // Verify adding breaks functionality
//            if (areListElementVisible(addBreakBtns, 5)) {
//                for (WebElement addBreakBtn : addBreakBtns) {
//                    clickTheElement(addBreakBtn);
//                }
//            } else {
//                SimpleUtils.fail("Add meal & rest break buttons not loaded successfully!", false);
//            }
//            if (areListElementVisible(mealBreakDurations, 5) && areListElementVisible(restBreakDurations, 5) && mealBreakDurations.size() == 1
//            && restBreakDurations.size() == 1) {
//                moveDayViewCards(mealBreakDurations.get(0), 40);
//                moveDayViewCards(restBreakDurations.get(0), 40);
//            } else {
//                SimpleUtils.fail("Meal and rest breaks are not added after clicking the add button!", false);
//            }
//            if (areListElementVisible(mealStartEndAreas, 5) && areListElementVisible(restStartEndAreas, 5)) {
//                moveDayViewCards(mealStartEndAreas.get(0), 40);
//                moveDayViewCards(restStartEndAreas.get(0), 40);
//            } else {
//                SimpleUtils.fail("Meal and rest start/end area not loaded successfully!", false);
//            }
//            breakTimes.add(mealBreakTimes.get(0).getText());
//            breakTimes.add(restBreakTimes.get(0).getText());
//        }else
//            SimpleUtils.fail("Edit meal break window load failed",true);
//        return breakTimes;
//    }
//
//    @Override
//    public void verifySpecificShiftHaveEditIcon(int index) throws Exception {
//        if (areListElementVisible(shiftsWeekView, 5) && shiftsWeekView.size() > index) {
//            try {
//                if (isElementLoaded(shiftsWeekView.get(index).findElement(By.cssSelector("[src*=edited-shift-week]")))) {
//                    SimpleUtils.pass("The shift with index: " + index + " has edited - pencil icon!");
//                } else {
//                    SimpleUtils.fail("The shift with index: " + index + " doesn't have edited - pencil icon!", false);
//                }
//            } catch (Exception e) {
//                SimpleUtils.fail("The shift with index: " + index + " doesn't have edited - pencil icon!", false);
//            }
//        } else {
//            SimpleUtils.fail("Week view shifts failed to load!", false);
//        }
//    }

//    @Override
//    public void verifyClickOnSubmitButton() throws Exception {
//        if (isElementLoaded(submitButton, 10)) {
//            click(submitButton);
//            if (isElementLoaded(confirmWindow, 20) && isElementLoaded(okBtnOnConfirm, 20)) {
//                clickTheElement(okBtnOnConfirm);
//                SimpleUtils.pass("Confirm window loaded Successfully!");
//            }else {
//                SimpleUtils.fail("Confirm window not loaded Successfully!", true);
//            }
//        }else {
//            SimpleUtils.fail("Submit button on Submit Cover Request not loaded Successfully!", true);
//        }
//    }

//    @Override
//    public void verifyComponentsOnSubmitCoverRequest() throws Exception {
//        if (isElementLoaded(messageText, 5)) {
//            SimpleUtils.pass("Message textbox loaded Successfully!");
//        }else {
//            SimpleUtils.fail("Message textbox not loaded Successfully!", true);
//        }
//        if (isElementLoaded(cancelButton, 5)) {
//            SimpleUtils.pass("Cancel button on Submit Cover Request loaded Successfully!");
//        }else {
//            SimpleUtils.fail("Cancel button on Submit Cover Request not loaded Successfully!", true);
//        }
//        if (isElementLoaded(submitButton, 5)) {
//            SimpleUtils.pass("Submit button on Submit Cover Request loaded Successfully!");
//        }else {
//            SimpleUtils.fail("Submit button on Submit Cover Request not loaded Successfully!", true);
//        }
//    }

//    @Override
//    public void verifyTheContentOfMessageOnSubmitCover() throws Exception {
//        if (isElementLoaded(messageText, 5) && isElementLoaded(shiftOfferTime, 5)) {
//            String expectedText = "Hey, I have a commitment " + shiftOfferTime.getText() + " that conflicts with my shift, would you be able to help cover my shift?";
//            String actualText = messageText.getAttribute("value");
//            if (expectedText.equalsIgnoreCase(actualText)) {
//                SimpleUtils.pass("The content of Add Message text box is correct!");
//                messageText.clear();
//            }else {
//                SimpleUtils.fail("The content of Add Message text box is incorrect! Expected is: " + expectedText
//                        + " but actual is: " + actualText, false);
//            }
//        }else {
//            SimpleUtils.fail("Message text box not loaded Successfully!", true);
//        }
//    }

//    @Override
//    public boolean isPopupWindowLoaded(String title) throws Exception {
//        boolean isLoaded = false;
//        if (isElementLoaded(popUpWindow, 5) && isElementLoaded(popUpWindowTitle, 5)) {
//            if (title.equalsIgnoreCase(popUpWindowTitle.getText())) {
//                SimpleUtils.pass(title + " window loaded Successfully!");
//                isLoaded = true;
//            }
//        }
//        return isLoaded;
//    }

//    @Override
//    public void clickTheShiftRequestByName(String requestName) throws Exception {
//        waitForSeconds(2);
//        if (areListElementVisible(shiftRequests, 5)) {
//            for (WebElement shiftRequest : shiftRequests) {
//                if (shiftRequest.getText().trim().equalsIgnoreCase(requestName.trim())) {
//                    click(shiftRequest);
//                    SimpleUtils.pass("Click " + requestName + " button Successfully!");
//                    break;
//                }
//            }
//        }else {
//            SimpleUtils.fail("Shift Request buttons not loaded Successfully!", true);
//        }
//    }
//
//    @Override
//    public boolean areShiftsPresent() throws Exception {
//        boolean arePresent = false;
//        if (areListElementVisible(dayViewAvailableShifts, 5)) {
//            arePresent = true;
//        }
//        return arePresent;
//    }

//    @Override
//    public int verifyClickOnAnyShift() throws Exception {
//        List<String> expectedRequests = new ArrayList<>(Arrays.asList("Request to Swap Shift", "Request to Cover Shift"));
//        int index = 100;
//        if (areListElementVisible(tmIcons, 15) && tmIcons.size() > 1) {
//            for (int i = 0; i < tmIcons.size(); i++) {
//                scrollToElement(tmIcons.get(i));
//                waitForSeconds(1);
//                clickTheElement(tmIcons.get(i));
//                if (isPopOverLayoutLoaded()) {
//                    if (verifyShiftRequestButtonOnPopup(expectedRequests)) {
//                        index = i;
//                        break;
//                    }else {
//                        clickTheElement(tmIcons.get(i));
//                    }
//                }
//            }
//            if (index == 100) {
//                // Doesn't find any shift that can swap or cover, cancel the previous
//                index = cancelSwapOrCoverRequests(expectedRequests);
//            }
//        }else {
//            SimpleUtils.fail("Team Members' Icons not loaded Successfully!", false);
//        }
//        return index;
//    }

//    public int cancelSwapOrCoverRequests(List<String> expectedRequests) throws Exception {
//        List<String> swapRequest = new ArrayList<>(Arrays.asList("View Swap Request Status"));
//        List<String> coverRequest = new ArrayList<>(Arrays.asList("View Cover Request Status"));
//        int index = 100;
//        if (areListElementVisible(tmIcons, 10)) {
//            for (int i = 0; i < tmIcons.size(); i++) {
//                moveToElementAndClick(tmIcons.get(i));
//                if (isPopOverLayoutLoaded()) {
//                    if (verifyShiftRequestButtonOnPopup(swapRequest)) {
//                        cancelRequestByTitle(swapRequest, swapRequest.get(0).substring(5).trim());
//                    }
//                    if (verifyShiftRequestButtonOnPopup(coverRequest)) {
//                        cancelRequestByTitle(coverRequest, coverRequest.get(0).substring(5).trim());
//                    }
//                    moveToElementAndClick(tmIcons.get(i));
//                    if (verifyShiftRequestButtonOnPopup(expectedRequests)) {
//                        index = i;
//                        break;
//                    }
//                }
//            }
//        }else {
//            SimpleUtils.fail("Team Members' Icons not loaded Successfully!", false);
//        }
//        if (index == 100) {
//            SimpleUtils.fail("Failed to find a shift that can swap or cover!", false);
//        }
//        return index;
//    }

//    public void cancelRequestByTitle(List<String> requests, String title) throws Exception {
//        if (requests.size() > 0) {
//            clickTheShiftRequestByName(requests.get(0));
//            if (isPopupWindowLoaded(title)) {
//                verifyClickCancelSwapOrCoverRequest();
//            }
//        }
//    }
//
//    public boolean isPopOverLayoutLoaded() throws Exception {
//        boolean isLoaded = false;
//        if (isElementLoaded(popOverLayout, 15)) {
//            isLoaded = true;
//            SimpleUtils.pass("Pop over layout loaded Successfully!");
//        }
//        return isLoaded;
//    }

    // Added by Nora: for Team schedule option as team member
    @FindBy (css = ".week-schedule-shift .week-schedule-shift-wrapper")
    private List<WebElement> wholeWeekShifts;
    @FindBy (css = ".day-week-picker-period-week")
    private List<WebElement> currentWeeks;
    @FindBy (className = "period-name")
    private WebElement weekPeriod;
    @FindBy (className = "card-carousel-card")
    private List<WebElement> smartCards;
    @FindBy (className = "card-carousel-link")
    private List<WebElement> cardLinks;
    @FindBy (css = "[src*=\"print.svg\"]")
    private WebElement printIcon;
    @FindBy (css = "[src*=\"No-Schedule\"]")
    private WebElement noSchedule;
    @FindBy(css = "[ng-repeat=\"opt in opts\"]")
    private List<WebElement> filters;
    @FindBy(css = ".accept-shift")
    private WebElement claimShiftWindow;
    @FindBy(css = ".redesigned-button-ok")
    private WebElement agreeClaimBtn;
    @FindBy(css = ".redesigned-button-cancel-outline")
    private WebElement declineBtn;
    @FindBy(css = ".redesigned-modal")
    private WebElement popUpModal;
    @FindBy(css = "img[src*='shift-info']")
    private List<WebElement> infoIcons;
    @FindBy(css = ".sch-shift-hover div:nth-child(3)>div.ng-binding")
    private WebElement shiftDuration;
    @FindBy(css = ".shift-hover-subheading.ng-binding:not([ng-if])")
    private WebElement shiftJobTitleAsWorkRole;
    @FindBy(className = "accept-shift-shift-info")
    private WebElement shiftDetail;
    @FindBy(css = ".lg-toast")
    private WebElement msgOnTop;
    @FindBy(css = "[label=\"Yes\"]")
    private WebElement yesButton;
    @FindBy(css = "[label=\"No\"]")
    private WebElement noButton;

//    public enum monthsOfCalendar {
//        Jan("January"),
//        Feb("February"),
//        Mar("March"),
//        Apr("April"),
//        May("May"),
//        Jun("June"),
//        Jul("July"),
//        Aug("August"),
//        Sep("September"),
//        Oct("October"),
//        Nov("November"),
//        Dec("December");
//        private final String value;
//
//        monthsOfCalendar(final String newValue) {
//            value = newValue;
//        }
//
//        public String getValue() {
//            return value;
//        }
//    }
//
//    @Override
//    public void verifyTheFunctionalityOfClearFilter() throws Exception {
//        String linkName = "Clear Filter";
//        String open = "Open";
//        clickLinkOnSmartCardByName(linkName);
//        if (areListElementVisible(dayViewAvailableShifts, 5)) {
//            for (WebElement shift : dayViewAvailableShifts) {
//                WebElement workerName = shift.findElement(By.className("sch-day-view-shift-worker-name"));
//                if (workerName != null) {
//                    if (!workerName.getText().trim().equalsIgnoreCase(open)) {
//                        SimpleUtils.pass("Clear Filter Successfully, no open shift found!");
//                    }else {
//                        SimpleUtils.fail("Clear Filter not Successfully, still found the open shift!", false);
//                    }
//                }else {
//                    SimpleUtils.fail("Failed to find the worker name element!", false);
//                }
//            }
//        }else {
//            SimpleUtils.report("No shifts found after clearing the shift!");
//        }
//    }
//
//    @Override
//    public void verifyClickOnYesButton() throws Exception {
//        if (isElementLoaded(yesButton, 5)) {
//            click(yesButton);
//            String message = "Cancelled successfully";
//            verifyThePopupMessageOnTop(message);
//        }else {
//            SimpleUtils.fail("Yes Buttons not loaded Successfully!", false);
//        }
//    }

//    @Override
//    public void verifyClickNoButton() throws Exception {
//        if (isElementLoaded(noButton, 5)) {
//            click(noButton);
//            if (!isElementLoaded(popUpWindow, 5)) {
//                SimpleUtils.pass("Click on No Button Successfully!");
//            }else {
//                SimpleUtils.fail("Click on No Button not Successfully!", false);
//            }
//        }else {
//            SimpleUtils.fail("No Buttons not loaded Successfully!", false);
//        }
//    }

//    @Override
//    public void verifyReConfirmDialogPopup() throws Exception {
//        String title = "Are you sure you want to cancel your claim for this shift?";
//        if (isPopupWindowLoaded(title)) {
//            if (isElementLoaded(yesButton, 5) && isElementLoaded(noButton, 5)) {
//                SimpleUtils.pass("Yes and No Buttons loaded Successfully!");
//            }else {
//                SimpleUtils.fail("Yes and No Buttons not loaded Successfully!", false);
//            }
//        }else {
//            SimpleUtils.fail(title + " window not loaded Successfully!", false);
//        }
//    }

//    @Override
//    public void verifyTheColorOfCancelClaimRequest(String cancelClaim) throws Exception {
//        String red = "#ff0000";
//        String color = "";
//        if (areListElementVisible(shiftRequests, 5)) {
//            for (WebElement shiftRequest : shiftRequests) {
//                if (shiftRequest.getText().equalsIgnoreCase(cancelClaim)) {
//                    color = Color.fromString(shiftRequest.getCssValue("color")).asHex();
//                }
//            }
//        }else {
//            SimpleUtils.fail("Shift Requests not loaded Successfully!", false);
//        }
//        if (red.equalsIgnoreCase(color)) {
//            SimpleUtils.pass("Cancel Claim Request option is in red color");
//        }else {
//            SimpleUtils.fail("Cancel Claim Request option should be there in red color, but the actual color is: "
//                    + color + ", expected is red: " + red, false);
//        }
//    }

//    @Override
//    public void verifyClickCancelBtnOnClaimShiftOffer() throws Exception {
//        if (isElementLoaded(declineBtn, 5)) {
//            clickTheElement(declineBtn);
//            if (isElementLoaded(popUpModal, 10) && popUpModal.getText().contains("Open Shift Declined")) {
//                SimpleUtils.pass("Click on Decline Claim Button Successfully!");
//                if (isElementLoaded(agreeClaimBtn, 5) && agreeClaimBtn.getText().equalsIgnoreCase("OK")) {
//                    clickTheElement(agreeClaimBtn);
//                    SimpleUtils.report("Click on OK button successfully on \"Open Shift Declined\" pop up!");
//                } else {
//                    SimpleUtils.fail("OK button failed to load on \"Open Shift Declined\" pop up", false);
//                }
//            }else {
//                SimpleUtils.fail("Click on Cancel Claim Button failed!", false);
//            }
//        }else {
//            SimpleUtils.fail("Cancel Claim Button not loaded Successfully!", false);
//        }
//    }

//    @Override
//    public void verifyClickAgreeBtnOnClaimShiftOffer() throws Exception {
//        if (isElementLoaded(agreeClaimBtn, 5)) {
//            click(agreeClaimBtn);
//            String expectedMessage = "Your claim request has been received and sent for approval";
//            verifyThePopupMessageOnTop(expectedMessage);
//        }else {
//            SimpleUtils.fail("I Agree Button not loaded Successfully!", false);
//        }
//    }
//
//    @Override
//    public void verifyClickAgreeBtnOnClaimShiftOfferWhenDontNeedApproval() throws Exception {
//        if (isElementLoaded(agreeClaimBtn, 5)) {
//            click(agreeClaimBtn);
//            String expectedMessage = "Success! This shift is yours, and has been added to your schedule.";
//            verifyThePopupMessageOnTop(expectedMessage);
//        }else {
//            SimpleUtils.fail("I Agree Button not loaded Successfully!", false);
//        }
//    }

//    @Override
//    public void verifyThePopupMessageOnTop(String expectedMessage) throws Exception {
//        if (isElementLoaded(msgOnTop, 20)) {
//            String message = msgOnTop.getText();
//            if (message.contains(expectedMessage)) {
//                SimpleUtils.pass("Verified Message shows correctly!");
//            }else {
//                SimpleUtils.fail("Message on top is incorrect, expected is: " + expectedMessage + ", but actual is: " + message, false);
//            }
//        }else {
//            SimpleUtils.fail("Message on top not loaded Successfully!", false);
//        }
//    }

//    @Override
//    public String getSpecificShiftWeekDay(int index) throws Exception {
//        String weekDay = null;
//        if (areListElementVisible(tmIcons, 5) && index < tmIcons.size()) {
//            WebElement clickedShift = tmIcons.get(index);
//            WebElement parent = clickedShift.findElement(By.xpath("./../../../../../../../../.."));
//            if (parent != null) {
//                WebElement weekDayElement = parent.findElement(By.tagName("div"));
//                String currentWeekDay = weekDayElement == null ? null : weekDayElement.getText();
//                if (currentWeekDay != null && !currentWeekDay.isEmpty()) {
//                    // WeekDay format is: SATURDAY, MAY 02, need to convert 02 to 2
//                    String[] items = currentWeekDay.split(" ");
//                    if (items.length == 3 && SimpleUtils.isNumeric(items[2])) {
//                        items[2] = Integer.toString(Integer.parseInt(items[2]));
//                        weekDay = items[0] + " " + items [1] + " " + items[2];
//                        SimpleUtils.report("Get the Week day for clicked shift: " + weekDay);
//                    }else {
//                        SimpleUtils.fail("Split String: '" + currentWeekDay + "' failed!", false);
//                    }
//                }else {
//                    SimpleUtils.fail("Failed to get the week day for clicked shift!", false);
//                }
//            }else {
//                SimpleUtils.fail("Failed to find the parent Element for the clicked team Member!", false);
//            }
//        }else {
//            SimpleUtils.fail("Team Members' Icons not loaded Successfully!", false);
//        }
//        return weekDay;
//    }

//    @Override
//    public void verifyTheShiftHourOnPopupWithScheduleTable(String scheduleShiftTime, String weekDay) throws Exception {
//        if (isElementLoaded(shiftDetail, 5)) {
//            String details = shiftDetail.getText();
//            if (details.toLowerCase().replaceAll("\\s*", "").contains(scheduleShiftTime.toLowerCase().replaceAll("\\s*", "")) &&
//                    details.toLowerCase().replaceAll("\\s*", "").contains(weekDay.toLowerCase().replaceAll("\\s*", ""))) {
//                SimpleUtils.pass("Date and time in the Popup is match with the date and time in Schedule table: " + scheduleShiftTime);
//            }else {
//                SimpleUtils.fail("Date and time in the Popup is incorrect: " + details + ", expected week day is: "
//                        + weekDay + ", and expected schedule shift time is:" + scheduleShiftTime, false);
//            }
//        }else {
//            SimpleUtils.fail("Shift Details not loaded Successfully!", false);
//        }
//    }

//    @Override
//    public List<String> getShiftHoursFromInfoLayout() throws Exception {
//        List<String> shiftHours = new ArrayList<>();
//        waitForSeconds(2);
//        if (areListElementVisible(hoverIcons, 20)) {
//            for (WebElement hoverIcon : hoverIcons) {
//                waitForSeconds(5);
//                scrollToBottom();
//                clickTheElement(hoverIcon);
//                if (isElementLoaded(shiftDuration, 5)) {
//                    shiftHours.add(shiftDuration.getText());
//                    SimpleUtils.report("Get the Shift time: " + shiftDuration.getText() + " Successfully!");
//                    click(hoverIcon);
//                }else {
//                    SimpleUtils.fail("Shift time duration not loaded Successfully!", false);
//                }
//            }
//            if (shiftHours.size() != hoverIcons.size()) {
//                SimpleUtils.fail("Failed to get the shift hours, the count is incorrect!", false);
//            }
//        }else {
//            SimpleUtils.fail("Info Icons not loaded Successfully!", false);
//        }
//        return shiftHours;
//    }

//    @Override
//    public void verifyClaimShiftOfferNBtnsLoaded() throws Exception {
//        if (isElementLoaded(claimShiftWindow, 5)) {
//            if (isElementLoaded(agreeClaimBtn, 5) && isElementLoaded(declineBtn, 5)) {
//                SimpleUtils.pass("Accept and Decline Buttons loaded Successfully!");
//            }else {
//                SimpleUtils.fail("Accept and Decline Buttons not loaded Successfully!", false);
//            }
//        }else {
//            SimpleUtils.fail("Pop up Window: Open Shift not loaded Successfully!", false);
//        }
//    }

//    @Override
//    public int selectOneShiftIsClaimShift(List<String> claimShift) throws Exception {
//        int index = -1;
//        if (areListElementVisible(tmIcons, 5)) {
//            for (int i = 0; i < tmIcons.size(); i++) {
//                moveToElementAndClick(tmIcons.get(i));
//                if (isPopOverLayoutLoaded()) {
//                    if (verifyShiftRequestButtonOnPopup(claimShift)) {
//                        index = i;
//                        break;
//                    }
//                }
//            }
//            if (index == -1) {
//                // Doesn't find any shift that is Claim Shift, cancel the previous
//                index = cancelClaimRequest(claimShift);
//            }
//        }else {
//            SimpleUtils.fail("Team Members' Icons not loaded Successfully!", false);
//        }
//        return index;
//    }

//    public int cancelClaimRequest(List<String> expectedRequests) throws Exception {
//        List<String> claimStatus = new ArrayList<>(Arrays.asList("Claim Shift Approval Pending", "Cancel Claim Request"));
//        int index = -1;
//        if (areListElementVisible(tmIcons, 10)) {
//            for (int i = 0; i < tmIcons.size(); i++) {
//                moveToElementAndClick(tmIcons.get(i));
//                if (isPopOverLayoutLoaded()) {
//                    if (verifyShiftRequestButtonOnPopup(claimStatus)) {
//                        clickTheShiftRequestByName(claimStatus.get(1));
//                        verifyReConfirmDialogPopup();
//                        verifyClickOnYesButton();
//                        moveToElementAndClick(tmIcons.get(i));
//                        if (verifyShiftRequestButtonOnPopup(expectedRequests)) {
//                            index = i;
//                            break;
//                        }
//                    }
//                }
//            }
//        }else {
//            SimpleUtils.fail("Team Members' Icons not loaded Successfully!", false);
//        }
//        if (index == -1) {
//            SimpleUtils.fail("Failed to find a shift that can swap or cover!", false);
//        }
//        return index;
//    }

//    @Override
//    public void verifySelectedFilterPersistsWhenSelectingOtherWeeks(String selectedFilter) throws Exception {
//        if (areListElementVisible(currentWeeks, 10)) {
//            for (int i = 0; i < currentWeeks.size(); i++) {
//                click(currentWeeks.get(i));
//                if (isElementLoaded(filterButton, 15)) {
//                    String selectedValue = filterButton.findElement(By.cssSelector("input-field[placeholder=\"None\"] input")).getAttribute("value");
//                    if (selectedFilter.contains(selectedValue)) {
//                        SimpleUtils.pass("Selected Filter is persist on Week: " + currentWeeks.get(i).getText());
//                    }else {
//                        SimpleUtils.fail("Selected filter is changed on Week: " + currentWeeks.get(i).getText()
//                                + ", expected filter is: " + selectedFilter + ", but actual selected filter is: " + selectedValue, false);
//                    }
//                }else {
//                    SimpleUtils.fail("Filter Button not loaded Successfully!", false);
//                }
//                if (i == (currentWeeks.size() - 1) && isElementLoaded(calendarNavigationNextWeekArrow, 5)) {
//                    click(calendarNavigationNextWeekArrow);
//                    verifySelectedFilterPersistsWhenSelectingOtherWeeks(selectedFilter);
//                }
//            }
//        }else {
//            SimpleUtils.fail("Current weeks' elements not loaded Successfully!", false);
//        }
//    }

//    @Override
//    public String selectOneFilter() throws Exception {
//        String selectedFilter = null;
//        if (areListElementVisible(filters, 10)) {
//            unCheckFilters();
//            waitForSeconds(2);
//            WebElement filterCheckBox = filters.get(1).findElement(By.cssSelector("input[type=\"checkbox\"]"));
//            String elementClass = filterCheckBox.getAttribute("class").toLowerCase();
//            if (elementClass.contains("ng-empty")) {
//                clickTheElement(filterCheckBox);
//                waitForSeconds(2);
//                elementClass = filterCheckBox.getAttribute("class").toLowerCase();
//            }
//            selectedFilter = filters.get(1).findElement(By.className("input-label")) == null ? "" : filters.get(1).findElement(By.className("input-label")).getText();
//            if (elementClass.contains("ng-not-empty")) {
//                SimpleUtils.pass("Check the filter: " + selectedFilter + " Successfully!");
//            }else {
//                SimpleUtils.fail("Check the filter: " + selectedFilter + " not Successfully!", false);
//            }
//        }else {
//            SimpleUtils.fail("Filters on Schedule page not loaded Successfully!", false);
//        }
//        return selectedFilter;
//    }

//    @Override
//    public void filterScheduleByBothAndNone() throws Exception {
//        String shiftTypeFilterKey = "shifttype";
//        HashMap<String, ArrayList<WebElement>> availableFilters = getAvailableFilters();
//        ArrayList<WebElement> shiftTypeFilters = null;
//        int bothSize = 0;
//        int noneSize = 0;
//        if (availableFilters.size() > 0) {
//            shiftTypeFilters = availableFilters.get(shiftTypeFilterKey);
//            unCheckFilters(shiftTypeFilters);
//            waitForSeconds(2);
//            if (areListElementVisible(wholeWeekShifts, 5)) {
//                noneSize = wholeWeekShifts.size();
//            }
//            checkFilters(shiftTypeFilters);
//            waitForSeconds(2);
//            if (areListElementVisible(wholeWeekShifts, 5)) {
//                bothSize = wholeWeekShifts.size();
//            }
//            if (noneSize != 0 && bothSize != 0 && noneSize == bothSize) {
//                SimpleUtils.pass("Scheduled and open shifts are shown when applying both filters and none of them!");
//            }else {
//                SimpleUtils.fail("Applying both filters size is: " + bothSize + ", but applying none of them size is: " + noneSize
//                        + ", they are inconsistent!", false);
//            }
//        }else {
//            SimpleUtils.fail("Failed to get the available filters!", false);
//        }
//    }

//    @Override
//    public void checkAndUnCheckTheFilters() throws Exception {
//        if (areListElementVisible(filters, 10)) {
//            unCheckFilters();
//            for (WebElement filter : filters) {
//                String filterName = filter.findElement(By.className("input-label")) == null ? "" : filter.findElement(By.className("input-label")).getText();
//                WebElement filterCheckBox = filter.findElement(By.cssSelector("input[type=\"checkbox\"]"));
//                String elementClass = filterCheckBox.getAttribute("class").toLowerCase();
//                if (elementClass.contains("ng-not-empty")) {
//                    SimpleUtils.fail("Uncheck the filter: " + filterName + " not Successfully!", false);
//                }else {
//                    SimpleUtils.pass("Uncheck the filter: " + filterName + " Successfully!");
//                }
//                click(filterCheckBox);
//                elementClass = filterCheckBox.getAttribute("class").toLowerCase();
//                if (elementClass.contains("ng-not-empty")) {
//                    SimpleUtils.pass("Check the filter: " + filterName + " Successfully!");
//                }else {
//                    SimpleUtils.fail("Check the filter: " + filterName + " not Successfully!", false);
//                }
//            }
//        }else {
//            SimpleUtils.fail("Filters on Schedule page not loaded Successfully!", false);
//        }
//    }

//    @Override
//    public void verifyScheduledNOpenFilterLoaded() throws Exception {
//        String shiftTypeFilterKey = "shifttype";
//        String scheduled = "Scheduled";
//        String open = "Open";
//        if (areListElementVisible(shiftsWeekView, 5)) {
//            HashMap<String, ArrayList<WebElement>> availableFilters = getAvailableFilters();
//            if (availableFilters.size() > 0) {
//                ArrayList<WebElement> shiftTypeFilters = availableFilters.get(shiftTypeFilterKey);
//                if (shiftTypeFilters.size() == 2) {
//                    if (shiftTypeFilters.get(0).getText().contains(scheduled) && shiftTypeFilters.get(1).getText().contains(open)) {
//                        SimpleUtils.pass("Filter is enabled and it has two filters - Scheduled and Open");
//                    } else {
//                        SimpleUtils.fail("Two filters are incorrect, expected are Scheduled and Open, actual are: "
//                                + shiftTypeFilters.get(0).getText() + " and " + shiftTypeFilters.get(1).getText(), false);
//                    }
//                } else {
//                    SimpleUtils.fail("The size of Shift type filters are incorrect!", false);
//                }
//            } else {
//                SimpleUtils.fail("Filters not loaded Successfully!", false);
//            }
//        }else if (isElementLoaded(noSchedule, 5) && isElementLoaded(periodName, 5)) {
//            SimpleUtils.report("The Schedule of this Week: " + periodName.getText() + " isn't generated!");
//        }else {
//            SimpleUtils.fail("Shifts week view not loaded Successfully!", false);
//        }
//    }
//
//    @Override
//    public void verifyThePrintFunction() throws Exception {
//        try {
//            if (isPrintIconLoaded()) {
//                clickTheElement(printIcon);
//                // Wait for the schedule to be downloaded
//                if (isElementLoaded(printButtonInPrintLayout, 5)) {
//                    click(printButtonInPrintLayout);
//                }
//                waitForSeconds(10);
//                String downloadPath = SimpleUtils.fileDownloadPath;
//                SimpleUtils.assertOnFail("Failed to download the team schedule", FileDownloadVerify.isFileDownloaded_Ext(downloadPath, "WeekViewSchedulePdf"), false);
//            } else {
//                SimpleUtils.fail("Print icon not loaded Successfully on Schedule page!", false);
//            }
//        } catch (Exception e) {
//            SimpleUtils.fail(e.toString(), false);
//        }
//    }
//
//    @Override
//    public boolean isPrintIconLoaded() throws Exception {
//        boolean isLoaded = false;
//        if (isElementLoaded(printIcon, 5)) {
//            isLoaded = true;
//            SimpleUtils.pass("Print Icon loaded Successfully!");
//        }
//        return isLoaded;
//    }

//    @Override
//    public void filterScheduleByShiftTypeAsTeamMember(boolean isWeekView) throws Exception {
//        String shiftTypeFilterKey = "shifttype";
//        HashMap<String, ArrayList<WebElement>> availableFilters = getAvailableFilters();
//        if (availableFilters.size() > 0) {
//            ArrayList<WebElement> shiftTypeFilters = availableFilters.get(shiftTypeFilterKey);
//            if (isWeekView) {
//                filterScheduleByShiftTypeWeekViewAsTeamMember(shiftTypeFilters);
//            }else {
//                filterScheduleByShiftTypeDayViewAsTeamMember(shiftTypeFilters);
//            }
//        }
//    }

//    public void filterScheduleByShiftTypeWeekViewAsTeamMember(ArrayList<WebElement> shiftTypeFilters) throws Exception {
//        if (shiftTypeFilters.size() > 0) {
//            for (WebElement shiftTypeFilter : shiftTypeFilters) {
//                if (filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
//                    click(filterButton);
//                unCheckFilters(shiftTypeFilters);
//                String shiftType = shiftTypeFilter.getText();
//                SimpleUtils.report("Data for Shift Type: '" + shiftType + "'");
//                click(shiftTypeFilter);
//                click(filterButton);
//                if (areListElementVisible(wholeWeekShifts, 5)) {
//                    for (WebElement shift : wholeWeekShifts) {
//                        WebElement name = shift.findElement(By.className("week-schedule-worker-name"));
//                        if (shiftType.contains("Open")) {
//                            if (!name.getText().equalsIgnoreCase("Open")) {
//                                SimpleUtils.fail("Shift: " + name.getText() + " isn't for shift type: " + shiftType, false);
//                            }
//                        }else {
//                            if (name.getText().contains("Open")) {
//                                SimpleUtils.fail("Shift: " + name.getText() + " isn't for shift type: " + shiftType, false);
//                            }
//                        }
//                    }
//                }else {
//                    SimpleUtils.report("Didn't find shift for type: " + shiftType);
//                }
//            }
//        }else {
//            SimpleUtils.fail("Shift Type Filters not loaded Successfully!", false);
//        }
//    }
//
//    public void filterScheduleByShiftTypeDayViewAsTeamMember(ArrayList<WebElement> shiftTypeFilters) throws Exception {
//        if (shiftTypeFilters.size() > 0) {
//            for (WebElement shiftTypeFilter : shiftTypeFilters) {
//                if (filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
//                    click(filterButton);
//                unCheckFilters(shiftTypeFilters);
//                String shiftType = shiftTypeFilter.getText();
//                SimpleUtils.report("Data for Shift Type: '" + shiftType + "'");
//                click(shiftTypeFilter);
//                click(filterButton);
//                if (areListElementVisible(dayViewAvailableShifts, 5)) {
//                    for (WebElement shift : dayViewAvailableShifts) {
//                        WebElement name = shift.findElement(By.className("sch-day-view-shift-worker-name"));
//                        if (shiftType.equalsIgnoreCase("Open")) {
//                            if (!name.getText().contains("Open")) {
//                                SimpleUtils.fail("Shift: " + name.getText() + " isn't for shift type: " + shiftType, false);
//                            }
//                        }else {
//                            if (name.getText().contains("Open")) {
//                                SimpleUtils.fail("Shift: " + name.getText() + " isn't for shift type: " + shiftType, false);
//                            }
//                        }
//                    }
//                }else {
//                    SimpleUtils.report("Didn't find shift for type: " + shiftType);
//                }
//            }
//        }else {
//            SimpleUtils.fail("Shift Type Filters not loaded Successfully!", false);
//        }
//    }
//
//    @Override
//    public int getShiftsCount() throws Exception {
//        int count = 0;
//        if (areListElementVisible(wholeWeekShifts, 5)) {
//            count = wholeWeekShifts.size();
//        } else if (areListElementVisible(dayViewAvailableShifts, 5)){
//            count = dayViewAvailableShifts.size();
//        }
//        return count;
//    }

//    @Override
//    public void clickLinkOnSmartCardByName(String linkName) throws Exception {
//        if (areListElementVisible(cardLinks, 5)) {
//            for (WebElement cardLink : cardLinks) {
//                if (cardLink.getText().equalsIgnoreCase(linkName)) {
//                    clickTheElement(cardLink);
//                    SimpleUtils.pass("Click the link: " + linkName + " Successfully!");
//                    break;
//                }
//            }
//        }else {
//            SimpleUtils.report("There are no smart card links!");
//        }
//    }
//
//    @Override
//    public int getCountFromSmartCardByName(String cardName) throws Exception {
//        int count = -1;
//        if (areListElementVisible(smartCards, 5)) {
//            for (WebElement smartCard : smartCards) {
//                WebElement title = smartCard.findElement(By.className("card-carousel-card-title"));
//                if (title != null && title.getText().trim().equalsIgnoreCase(cardName)) {
//                    WebElement h1 = smartCard.findElement(By.tagName("h1"));
//                    String h1Title = h1 == null ? "" : h1.getText();
//                    if (h1Title.contains(" ")) {
//                        String[] items = h1Title.split(" ");
//                        for (String item : items) {
//                            if (SimpleUtils.isNumeric(item)) {
//                                count = Integer.parseInt(item);
//                                SimpleUtils.report("Get " + cardName + " count is: " + count);
//                                break;
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        if (count == -1) {
//            SimpleUtils.fail("Failed to get the count from " + cardName + " card!", false);
//        }
//        return count;
//    }
//
//    @Override
//    public boolean isSpecificSmartCardLoaded(String cardName) throws Exception {
//        boolean isLoaded = false;
//        waitForSeconds(15);
//        if (areListElementVisible(smartCards, 15)) {
//            for (WebElement smartCard : smartCards) {
//                WebElement title = smartCard.findElement(By.className("card-carousel-card-title"));
//                if (title != null && title.getText().trim().equalsIgnoreCase(cardName)) {
//                    isLoaded = true;
//                    break;
//                }
//            }
//        }
//        return isLoaded;
//    }
//
//    @Override
//    public void goToSchedulePageAsTeamMember() throws Exception {
//        if (isElementLoaded(goToScheduleButton, 5)) {
//            clickTheElement(goToScheduleButton);
//            if (areListElementVisible(ScheduleSubTabsElement, 5)) {
//                SimpleUtils.pass("Navigate to schedule page successfully!");
//            }else {
//                SimpleUtils.fail("Schedule page not loaded Successfully!", true);
//            }
//        }else {
//            SimpleUtils.fail("Go to Schedule button not loaded Successfully!", false);
//        }
//    }
//
//    @Override
//    public void gotoScheduleSubTabByText(String subTitle) throws Exception {
//        if (areListElementVisible(ScheduleSubTabsElement, 5)) {
//            for (WebElement scheduleSubTab : ScheduleSubTabsElement) {
//                if (isElementEnabled(scheduleSubTab, 5) && isClickable(scheduleSubTab, 5)
//                        && scheduleSubTab.getText().equalsIgnoreCase(subTitle)) {
//                    click(scheduleSubTab);
//                    break;
//                }
//            }
//            if (isElementLoaded(activatedSubTabElement, 5) && activatedSubTabElement.getText().equalsIgnoreCase(subTitle)) {
//                SimpleUtils.pass("Navigate to sub tab: " + subTitle + " Successfully!");
//            }else {
//                SimpleUtils.fail("Failed navigating to sub tab: " + subTitle, false);
//            }
//        }else {
//            SimpleUtils.fail("Schedule sub tab elements not loaded SUccessfully!", false);
//        }
//    }
//
//    @Override
//    public void verifyTeamScheduleInViewMode() throws Exception {
//        if (isElementLoaded(edit, 5)) {
//            SimpleUtils.fail("Team Member shouldn't see the Edit button!", false);
//        }else {
//            SimpleUtils.pass("Verified Team Schedule is in View Mode!");
//        }
//    }
//
//    @Override
//    public List<String> getWholeWeekSchedule() throws Exception {
//        List<String> weekShifts = new ArrayList<>();
//        if (areListElementVisible(wholeWeekShifts, 5)) {
//            for (WebElement shift : wholeWeekShifts) {
//                WebElement name = shift.findElement(By.className("week-schedule-worker-name"));
//                if (!name.getText().contains("Open")) {
//                    weekShifts.add(shift.getText());
//                }
//            }
//        }else {
//            SimpleUtils.fail("Whole Week Shifts not loaded Successfully!", true);
//        }
//        if (weekShifts.size() == 0) {
//            SimpleUtils.fail("Failed to get the whole week shifts!", false);
//        }
//        return weekShifts;
//    }
//
//    @Override
//    public String getSelectedWeek() throws Exception {
//        String selectedWeek = "";
//        if (isElementLoaded(currentActiveWeek, 5)) {
//            selectedWeek = currentActiveWeek.getText();
//            SimpleUtils.report("Get current active week: " + selectedWeek);
//        }else {
//            SimpleUtils.fail("Current Active Week not loaded Successfully!", false);
//        }
//        return selectedWeek;
//    }

//    @Override
//    public void verifySelectOtherWeeks() throws Exception {
//        String currentWeekPeriod = "";
//        String weekDate = "";
//        if (areListElementVisible(currentWeeks, 10)) {
//            for (int i = 0; i < currentWeeks.size(); i++) {
//                click(currentWeeks.get(i));
//                if (isElementLoaded(periodName, 5)) {
//                    currentWeekPeriod = periodName.getText().length() > 12 ? periodName.getText().substring(12) : "";
//                }
//                if (currentWeeks.get(i).getText().contains("\n")) {
//                    weekDate = currentWeeks.get(i).getText().split("\n").length >= 2 ? currentWeeks.get(i).getText().split("\n")[1] : "";
//                    if (weekDate.contains("-")) {
//                        String[] dates = weekDate.split("-");
//                        String shortMonth1 = dates[0].trim().substring(0, 3);
//                        String shortMonth2 = dates[1].trim().substring(0, 3);
//                        String fullMonth1 = getFullMonthName(shortMonth1);
//                        String fullMonth2 = getFullMonthName(shortMonth2);
//                        weekDate = weekDate.replaceAll(shortMonth1, fullMonth1);
//                        if (!shortMonth1.equalsIgnoreCase(shortMonth2)) {
//                            weekDate = weekDate.replaceAll(shortMonth2, fullMonth2);
//                        }
//                    }
//                }
//                if (weekDate.trim().equalsIgnoreCase(currentWeekPeriod.trim())) {
//                    SimpleUtils.pass("Selected week is: " + currentWeeks.get(i).getText() + " and current week is: " + currentWeekPeriod);
//                }else {
//                    SimpleUtils.fail("Selected week is: " + currentWeeks.get(i).getText() + " but current week is: " + currentWeekPeriod, false);
//                }
//                if (i == (currentWeeks.size() - 1) && isElementLoaded(calendarNavigationNextWeekArrow, 5)) {
//                    click(calendarNavigationNextWeekArrow);
//                    verifySelectOtherWeeks();
//                }
//            }
//        }else {
//            SimpleUtils.fail("Current weeks' elements not loaded Successfully!", false);
//        }
//    }

//    public String getFullMonthName(String shortName) {
//        String fullName = "";
//        monthsOfCalendar[] shortNames = monthsOfCalendar.values();
//        for (int i = 0; i < shortNames.length; i++) {
//            if (shortNames[i].name().equalsIgnoreCase(shortName)) {
//                fullName = shortNames[i].value;
//                SimpleUtils.report("Get the full name of " + shortName + ", is: " + fullName);
//                break;
//            }
//        }
//        return fullName;
//    }

    //added by Estelle
//    @Override
//    public void validateGroupBySelectorSchedulePage(boolean isLocationGroup) throws Exception {
//        waitForSeconds(3);
//        if(isElementLoaded(groupBySelector,10))
//        {
//            click( groupBySelector);
//            // Validate the each button on dropdown
//            if(isElementEnabled(groupByAll,3)
//                    && isElementEnabled(groupByWorkRole,3)
//                    && isElementEnabled(groupByTM,3)
//                    && isElementLoaded(groupByJobTitle,3)
//                    && (isLocationGroup? isElementLoaded(groupByLocation, 5):true))
//                if(isLocationGroup){
//                    SimpleUtils.pass("In Week view: 'Group by All' filter have 5 filters:1.Group by all 2. Group by work role 3. Group by TM 4.Group by job title 5 Group by location");
//                } else
//                    SimpleUtils.pass("In Week view: 'Group by All' filter have 4 filters:1.Group by all 2. Group by work role 3. Group by TM 4.Group by job title");
//            else SimpleUtils.fail("Group by All filter does not have 4 filters:1.Group by all 2. Group by work role 3. Group by TM 4.Group by job title",true);
//        }
//        else SimpleUtils.fail("Group By Selector is not available on screen ", false);
//
//    }

    @FindBy(css = "[class=\"week-schedule-shift-color\"]")
    private List<WebElement> workRoleIcons;

    @FindBy(css = "lg-button[ng-click=\"controlPanel.fns.editAction($event)\"]")
    private WebElement editScheduleButton;

    @FindBy(css = ".week-schedule-right-strip")
    private WebElement tMHourAndAverageShiftLengthColumn;

    @FindBy(css = "[class=\"week-schedule-shift-title\"]")
    private List<WebElement> scheduleShiftTitles;

    @FindBy(css = "div.week-schedule-ribbon-location-toggle")
    private List<WebElement> groupByLocationToggles;

//    @Override
//    public void validateScheduleTableWhenSelectAnyOfGroupByOptions(boolean isLocationGroup) throws Exception {
//        if(isElementLoaded(groupBySelector,5)) {
//            //validate the schedule table when group by Work Role
//            selectGroupByFilter(scheduleGroupByFilterOptions.groupbyWorkRole.getValue());
//            if (areListElementVisible(workRoleIcons, 10) && workRoleIcons.size() > 0) {
//                SimpleUtils.pass("In Week view: Shifts in schedule table are grouped by work role");
//            } else {
//                SimpleUtils.fail("In Week view: Shifts in schedule table are failed group by work role ", true);
//            }
//
//            //validate the schedule table when group by TM
//            selectGroupByFilter(scheduleGroupByFilterOptions.groupbyTM.getValue());
//            if (isElementEnabled(tMHourAndAverageShiftLengthColumn, 10)) {
//                SimpleUtils.pass("In Week view: Shifts in schedule table are grouped by TM ");
//            } else {
//                SimpleUtils.fail("In Week view: Shifts in schedule table are failed group by TM ", true);
//            }
//
//            //validate the schedule table when group by Job Title
//            selectGroupByFilter(scheduleGroupByFilterOptions.groupbyJobTitle.getValue());
//            if (areListElementVisible(scheduleShiftTitles, 10) && scheduleShiftTitles.size() > 0) {
//                SimpleUtils.pass("In Week view: Shifts in schedule table are grouped by job title");
//            } else {
//                SimpleUtils.fail("In Week view: Shifts in schedule table are failed group by job title ", false);
//            }
//
//            //validate the schedule table when group by Location
//            if(isLocationGroup){
//                selectGroupByFilter(scheduleGroupByFilterOptions.groupbyLocation.getValue());
//                if (areListElementVisible(scheduleShiftTitles, 10) && scheduleShiftTitles.size() > 0
//                        && areListElementVisible(groupByLocationToggles, 10) && groupByLocationToggles.size()> 0
//                        && groupByLocationToggles.size() == scheduleShiftTitles.size()) {
//                    SimpleUtils.pass("In Week view: Shifts in schedule table are grouped by Location");
//
//                    // Check the sub-location display in alphabetical order
//                    List<String>  locationsNamesInSchedule = new ArrayList<>();
//                    for(int i = 0; i< scheduleShiftTitles.size(); i++){
//                        locationsNamesInSchedule.add(scheduleShiftTitles.get(i).getText());
//                    }
//                    List<String>  locationsNamesBySorted = new ArrayList<>();
//                    locationsNamesBySorted.addAll(locationsNamesInSchedule);
//                    locationsNamesInSchedule.sort(null);
//
//                    if(locationsNamesBySorted.equals(locationsNamesInSchedule)){
//                        SimpleUtils.pass("In Week view: Sub-location display in alphabetical order when grouped by Location");
//                    } else
//                        SimpleUtils.fail("In Week view: Sub-location are not display in alphabetical order when grouped by Location", false);
//
//                    //Check the first location will be opened
//                    if(groupByLocationToggles.get(0).getAttribute("class").contains("open")){
//                        SimpleUtils.pass("In Week view: The first location is opened ");
//                    } else
//                        SimpleUtils.fail("In Week view: The first location is not opened ", false);
//
//                    //Check all locations can be expanded and closed
//                    int randomIndex = (new Random()).nextInt(groupByLocationToggles.size()-1)+1;
//
//                    if(groupByLocationToggles.get(randomIndex).getAttribute("class").contains("close")){
//                        scheduleShiftTitles.get(randomIndex).click();
//                        if(groupByLocationToggles.get(randomIndex).getAttribute("class").contains("open")){
//                            SimpleUtils.pass("In Week view: Location can be expanded ");
//                        } else
//                            SimpleUtils.fail("In Week view: Location cannot be expanded ", false);
//
//                        scheduleShiftTitles.get(randomIndex).click();
//                        if(groupByLocationToggles.get(randomIndex).getAttribute("class").contains("close")){
//                            SimpleUtils.pass("In Week view: Location can be closed ");
//                        } else
//                            SimpleUtils.fail("In Week view: Location cannot be closed ", false);
//                    } else if(groupByLocationToggles.get(randomIndex).getAttribute("class").contains("open")){
//                        scheduleShiftTitles.get(randomIndex).click();
//                        if(groupByLocationToggles.get(randomIndex).getAttribute("class").contains("close")){
//                            SimpleUtils.pass("In Week view: Location can be closed ");
//                        } else
//                            SimpleUtils.fail("In Week view: Location cannot be closed ", false);
//                    }
//                } else {
//                    SimpleUtils.fail("In Week view: Shifts in schedule table are failed group by Location ", false);
//                }
//            }
//
//            //change back to Group by All
//            selectGroupByFilter(scheduleGroupByFilterOptions.groupbyAll.getValue());
//        }
//        else SimpleUtils.fail("Group By Selector is not available on screen ", true);
//    }

//    @Override
//    public boolean checkEditButton() throws Exception {
//        if(isElementLoaded(editScheduleButton,10))
//        {
//
//            SimpleUtils.pass("Edit button is Editable");
//            return true;
//        }
//        else {
//            SimpleUtils.fail("Edit button is not Enable on screen", false);
//            return false;
//        }
//    }

    @FindBy(css = "div.modal-content")
    private WebElement popupAlertPremiumPay;

    @FindBy(css = "div.lgn-alert-title")
    private WebElement editFinalizedScheduleWarningTitle;

    @FindBy(css = "[class=\"lgn-alert-message warning\"]")
    private WebElement editFinalizedScheduleWarningMessage;

    @FindBy(css = "button.btn.lgn-action-button.lgn-action-button-success")
    private WebElement btnEditAnyway;

    @FindBy(css = "button.btn.lgn-action-button.lgn-action-button-default")
    private WebElement btnCancelOnAlertPopup;

//    public void clickOnEditButtonNoMaterScheduleFinalizedOrNot() throws Exception {
//        waitForSeconds(5);
//        if(checkEditButton())
//        {
//            // Validate what happens next to the Edit!
//            // When Status is finalized, look for extra popup.
//            clickTheElement(editScheduleButton);
//            waitForSeconds(3);
//            if(isElementLoaded(popupAlertPremiumPay,10) ) {
//                SimpleUtils.pass("Edit button is clickable and Alert(premium pay pop-up) is appeared on Screen");
//                // Validate CANCEL and EDIT ANYWAY Buttons are enabled.
//                if(isElementEnabled(btnEditAnyway,10) && isElementEnabled(btnCancelOnAlertPopup,10)){
//                    SimpleUtils.pass("CANCEL And EDIT ANYWAY Buttons are enabled on Alert Pop up");
//                    SimpleUtils.report("Click on EDIT ANYWAY button and check for next save and cancel buttons");
//                    clickTheElement(btnEditAnyway);
//                } else {
//                    SimpleUtils.fail("CANCEL And EDIT ANYWAY Buttons are not enabled on Alert Popup ",false);
//                }
//            }
//            waitForSeconds(5);
//            if(checkSaveButton() && checkCancelButton()) {
//                SimpleUtils.pass("Save and Cancel buttons are enabled ");
//            } else{
//                SimpleUtils.fail("Save and Cancel buttons are not enabled. ", false);
//            }
//        }else{
//            generateOrUpdateAndGenerateSchedule();
//        }
//
///*        if(checkEditButton())
//        {
//            // Validate what happens next to the Edit!
//            // When Status is finalized, look for extra popup.
//            if(isScheduleFinalized())
//            {
//                click(editScheduleButton);
//                String warningMessage1 = "Editing finalized schedule\n" + "Editing a finalized schedule after the ";
//                String warningMessage2 = "-day advance notice period may incur a schedule change premium.";
//                String editFinalizedScheduleWarning = editFinalizedScheduleWarningTitle.getText() + "\n" + editFinalizedScheduleWarningMessage.getText();
//                if(isElementLoaded(popupAlertPremiumPay,5) && editFinalizedScheduleWarning.contains(warningMessage1) &&
//                editFinalizedScheduleWarning.contains(warningMessage2)) {
//                    SimpleUtils.pass("Edit button is clickable and Alert(premium pay pop-up) is appeared on Screen");
//                    // Validate CANCEL and EDIT ANYWAY Buttons are enabled.
//                    if(isElementEnabled(btnEditAnyway,5) && isElementEnabled(btnCancelOnAlertPopup,5)){
//                        SimpleUtils.pass("CANCEL And EDIT ANYWAY Buttons are enabled on Alert Pop up");
//                        SimpleUtils.report("Click on EDIT ANYWAY button and check for next save and cancel buttons");
//                        click(btnEditAnyway);
//                        if(checkSaveButton() && checkCancelButton()) {
//                            SimpleUtils.pass("Save and Cancel buttons are enabled ");
//                        }
//                        else
//                            SimpleUtils.fail("Save and Cancel buttons are not enabled. ", false);
//                    }
//                    else
//                        SimpleUtils.fail("CANCEL And EDIT ANYWAY Buttons are not enabled on Alert Popup ",false);
//                }
//            }
//            else
//            {
//                clickTheElement(editScheduleButton);
//                // Validate Save and cancel buttons are enabled!
//                if(checkSaveButton() && checkCancelButton()) {
//                    SimpleUtils.pass("Save and Cancel buttons are enabled ");
//                }
//                else
//                    SimpleUtils.fail("Save and Cancel buttons are not enabled. ", false);
//            }
//        }else
//            generateOrUpdateAndGenerateSchedule(); */
//    }
//
//    @Override
//    public void clickOnOpenShitIcon() {
//        if (areListElementVisible(openShiftIcon,5) ) {
//            click(openShiftIcon.get(0));
//            SimpleUtils.pass("open shift is clickable");
//        }else
//            SimpleUtils.fail("there is no open shift",true);
//    }

//    @Override
//    public void verifyEditButtonFuntionality() throws Exception {
//
//        if(checkEditButton())
//        {
//            // Validate what happens next to the Edit!
//            // When Status is finalized, look for extra popup.
//            if(isScheduleFinalized())
//            {
//                click(editScheduleButton);
//                if(isElementLoaded(popupAlertPremiumPay,5)) {
//                    SimpleUtils.pass("Alert(premium pay pop-up) is appeared on Screen");
//                    // Validate CANCEL and EDIT ANYWAY Buttons are enabled.
//                    if(isElementEnabled(btnEditAnyway,5) && isElementEnabled(btnCancelOnAlertPopup,5)){
//                        SimpleUtils.pass("CANCEL And EDIT ANYWAY Buttons are enabled on Alert Pop up");
//                        click(btnEditAnyway);
//                        if(checkSaveButton() && checkCancelButton()) {
//                            SimpleUtils.pass("Save and Cancel buttons are enabled ");
//                            selectCancelButton();
//                        }
//                        else
//                            SimpleUtils.fail("Save and Cancel buttons are not enabled. ", false);
//
//                    }
//                    else
//                        SimpleUtils.fail("CANCEL And EDIT ANYWAY Buttons are not enabled on Alert Popup ",false);
//                }
//                else
//                    SimpleUtils.fail("Alert(premium pay pop-up) is not appeared on Screen",false);
//            }
//            else
//            {
//                click(editScheduleButton);
//                SimpleUtils.pass("Edit button is clickable");
//                // Validate Save and cancel buttons are enabled!
//                if(checkSaveButton() && checkCancelButton()) {
//                    SimpleUtils.pass("Save and Cancel buttons are enabled ");
//                    selectCancelButton();
//                }
//                else
//                    SimpleUtils.fail("Save and Cancel buttons are not enabled. ", false);
//            }
//        }
//    }


    @FindBy(css = "lg-button[ng-click=\"controlPanel.fns.saveConfirmation($event)\"]")
    private WebElement btnSaveOnSchedulePage;
    @FindBy(css = "lg-button[ng-click=\"controlPanel.fns.cancelAction($event)\"]")
    private WebElement btnCancelOnSchedulePage;

//    @Override
//    public boolean checkCancelButton() throws Exception {
//        if(isElementEnabled(btnCancelOnSchedulePage,10))
//        {
//            SimpleUtils.pass("Cancel button is enabled ");
//            return true;
//        }
//        else
//        {
//            SimpleUtils.fail("Cancel button is not enabled. ", true);
//            return false;
//        }
//    }

//    @Override
//    public void selectCancelButton() throws Exception {
//        if(checkCancelButton())
//        {
//            click(btnCancelOnSchedulePage);
//            SimpleUtils.pass("Cancel button is clicked ! ");
//        }
//        else
//        {
//            SimpleUtils.fail("Cancel Button cannot be clicked! ",false);
//        }
//    }

//    @Override
//    public boolean checkSaveButton() throws Exception {
//        if(isElementEnabled(btnSaveOnSchedulePage,10))
//        {
//            SimpleUtils.pass("Save button is enabled ");
//            return true;
//        }
//        else
//        {
//            SimpleUtils.fail("Save button is not enabled. ", true);
//            return false;
//        }
//    }

//    @Override
//    public void selectSaveButton() throws Exception {
//        if(checkCancelButton())
//        {
//            click(btnSaveOnSchedulePage);
//            SimpleUtils.pass("Save button is clicked ! ");
//        }
//        else
//        {
//            SimpleUtils.fail("Save Button cannot be clicked! ",false);
//        }
//
//    }


//    @Override
//    public boolean isScheduleFinalized() throws Exception {
//        if(isElementLoaded(publishButton,5))
//        {
//            SimpleUtils.report("Publish button is loaded on screen, Hence We don't expect Alert Popup.  ");
//            return false;  }
//        else {
//            SimpleUtils.report("Publish button is not loaded on screen, Hence We have to expect Alert Popup.  ");
//            return true;  }
//    }

    @FindBy(css = ".week-schedule-shift .shift-container .rows .worker-image-optimized img")
    private List<WebElement> profileIcons;

    @FindBy(css = "div.sch-open-shift")
    private List<WebElement> openShiftIcon;

    @FindBy(css = "div[ng-class=\"workerActionClass('ViewProfile')\"]")
    private WebElement viewProfileOnIcon;

    @FindBy(css = "div[ng-class=\"workerActionClass('ViewOpenShift')\"]")
    private WebElement viewOpenShift;

    @FindBy(css = "div[ng-click=\"editShiftTime($event, shift)\"]")
    private WebElement editShiftTime;

    @FindBy(css = "div[ng-click=\"editBreaksTime()\"]")
    private WebElement editMealBreakTime;

    @FindBy(css = "div.modal-instance-header-title")
    private WebElement editMealBreakTitle;

    @FindBy(css = "[data-tootik=\"Add a meal break\"] img")
    private WebElement addMealBreakButton;

    @FindBy(css = "[ng-click=\"closeModal()\"]")
    private WebElement cannelBtnInMealBreakButton;

    @FindBy(css = "[ng-click=\"confirm()\"]")
    private WebElement continueBtnInMealBreakButton;

    @FindBy(css = "day-part-weekday.ng-isolate-scope")
    private WebElement sliderInMealBreakButton;

    @FindBy(css = "[ng-click=\"removeBreak(b)\"]")
    private List<WebElement> deleteMealBreakButtons;

    @FindBy(css = "div.noUi-draggable")
    private List<WebElement> mealBreaks;

    @FindBy(css = "div.slider-section-description-break-time-item-meal")
    private List<WebElement> mealBreakTimes;

    @FindBy(css = "[id=\"unconstrained\"]")
    private WebElement mealBreakBar;

    @FindBy(css = "div[ng-click=\"deleteShift($event, shift)\"]")
    private WebElement deleteShift;

    @FindBy(css = "div.lgn-alert-title.ng-binding.warning")
    private WebElement titleInDeleteWindows;

    @FindBy(css = ".lgn-alert-message.ng-binding.ng-scope.warning")
    private WebElement alertMsgInDeleteWindows;

    @FindBy(css = "[label=\"cancelLabel()\"]")
    private WebElement cancelBtnInDeleteWindows;

    @FindBy(css = "[label=\"okLabel()\"]")
    private WebElement deleteBtnInDeleteWindows;

    @FindBy(css = "[src=\"img/legion/edit/deleted-shift-week.png\"]")
    private WebElement deleteShiftImg;

    //View Profile Personal details
    @FindBy(css="div.tmprofile.profile-container.ng-scope")
    private WebElement tmpProfileContainer;

    @FindBy(xpath ="//div[contains(@class,'day-week-picker-period-week')][3]")
    private WebElement pickNextWeekOnSchedule;

//    @Override
//    public void selectNextWeekSchedule() throws Exception {
//        if(isElementEnabled(pickNextWeekOnSchedule,5)){
//            click(pickNextWeekOnSchedule);
//            SimpleUtils.pass("Next Week On Schedule is picked ");
//            //   return true;
//        }
//        else {
//            SimpleUtils.fail("Next Week On Schedule is not available", true);
//            //   return false;
//        }
//    }

//    @FindBy(xpath = "//div[@ng-if=\"!forceShowOpen && showWorkerImage(shift)\"]/worker-image/div/div")
//    private List<WebElement> profileIconsDayView;
//
//    @Override
//    public boolean isProfileIconsEnable() throws Exception {
//        if(areListElementVisible(profileIcons,10)){
//            SimpleUtils.pass("Profile Icon is present for selected Employee");
//            return true;
//        } else if (areListElementVisible(profileIconsDayView,10)) {
//            SimpleUtils.pass("Profile Icon is present for selected Employee");
//            return true;
//        }
//        else {
//            SimpleUtils.fail("Profile Icon is not present for selected Employee", false);
//            return false;
//        }
//    }
//
//    @Override
//    public boolean isProfileIconsClickable() throws Exception {
//        if(areListElementVisible(profileIcons,10)){
//            int randomIndex = (new Random()).nextInt(profileIcons.size());
//            try{
//                click(profileIcons.get(randomIndex));
//                return true;
//            } catch (Exception e){
//                //
//            }
//        } else {
//            SimpleUtils.fail("Profile Icon is not present for selected Employee", false);
//        }
//        return false;
//    }

//    public WebElement clickOnProfileIcon() throws Exception {
//        WebElement selectedShift = null;
//        if(isProfileIconsEnable()&& areListElementVisible(shifts, 10)) {
//            int randomIndex = (new Random()).nextInt(profileIcons.size());
//            while (profileIcons.get(randomIndex).getAttribute("src").contains("openShiftImage")){
//                randomIndex = (new Random()).nextInt(profileIcons.size());
//            }
//            clickTheElement(profileIcons.get(randomIndex));
//            selectedShift = shifts.get(randomIndex);
//        } else if (areListElementVisible(scheduleTableWeekViewWorkerDetail, 10) && areListElementVisible(dayViewAvailableShifts, 10)) {
//            int randomIndex = (new Random()).nextInt(scheduleTableWeekViewWorkerDetail.size());
//            while (dayViewAvailableShifts.get(randomIndex).findElement(By.className("sch-day-view-shift-worker-name")).getText().contains("Open")){
//                randomIndex = (new Random()).nextInt(scheduleTableWeekViewWorkerDetail.size());
//            }
//            clickTheElement(scheduleTableWeekViewWorkerDetail.get(randomIndex));
//            selectedShift = dayViewAvailableShifts.get(randomIndex);
//        } else
//            SimpleUtils.fail("Can't Click on Profile Icon due to unavailability ",false);
//
//        return selectedShift;
//    }


//    @Override
//    public boolean isViewProfileEnable() throws Exception {
//        if(isElementEnabled(viewProfileOnIcon,5)){
//            SimpleUtils.pass("View Profile  is enable/available on Pop Over Style!");
//            return true;
//        }
//        else{
//            SimpleUtils.fail("View Profile option is not enable/available on Pop Over Style ",true);
//            return false;
//        }
//    }
//
//    @Override
//    public void clickOnViewProfile() throws Exception {
//        if(isViewProfileEnable())
//        {
//            clickTheElement(viewProfileOnIcon);
//            SimpleUtils.pass("View Profile Clicked on Pop Over Style!");
//        }
//        else {
//            SimpleUtils.fail("View Profile can not be clicked ",false);
//        }
//    }

//    @FindBy(css = "div.tm-address-container")
//    private WebElement personalDetailsContainer;
//    @FindBy(css = "span.tm-nickname.ng-binding")
//    private WebElement personalDetailsName;
//    @FindBy(css = "div.tm-phone")
//    private WebElement personalDetailsPhone;
//    @FindBy(css = "div.tm-email")
//    private WebElement personalDetailsEmailAddress;
//
//    @Override
//    public void verifyPersonalDetailsDisplayed() throws Exception {
//        if(isElementEnabled(personalDetailsContainer,5))
//        {
//            SimpleUtils.pass("Personal Details Container is Loaded in popup!");
//        }
//        else
//        {
//            SimpleUtils.fail("Personal Details Container is not Loaded in Popup!",false);
//        }
//        //
//        if(isElementLoaded(personalDetailsName,5))
//        {
//            SimpleUtils.pass("Personal Details Name is Loaded in popup!");
//        }
//        else
//        {
//            SimpleUtils.fail("Personal Details Name is not Loaded in Popup!",false);
//        }
//
//        if(isElementLoaded(personalDetailsPhone,8) || isElementLoaded(personalDetailsEmailAddress,5))
//        {
//            SimpleUtils.pass("Phone/Email details are Loaded in popup!");
//        }
//        else
//        {
//            SimpleUtils.report("Phone/Email details are not Loaded in Popup!");
//        }
//    }
//
//    //WorkDetails
//
//    @FindBy(css = "div[class=\"staffing-details-container\"]")
//    private WebElement workPreferenceContainer;
//    @FindBy(css = "div[class=\"tm-prefs-container\"]")
//    private WebElement workPrefValues;
//    @FindBy(css = "div[class=\"tm-additional-locations-container tm-details\"]")
//    private WebElement workPrefAdditionalDetails;
//
//
//    @Override
//    public void verifyWorkPreferenceDisplayed() throws Exception {
//
//        if(isElementLoaded(workPreferenceContainer,5)){
//            SimpleUtils.pass("Work Preference Details are displayed");
//        }else{
//            SimpleUtils.fail("Work Preference Details are not displayed", true);
//        }
//        if(isElementLoaded(workPrefValues,5)){
//            SimpleUtils.pass("Work Preference Values are displayed");
//        }else {
//            SimpleUtils.fail("Work Preference Values are not displayed", true);
//        }
//        if(isElementLoaded(workPrefAdditionalDetails,5)){
//            SimpleUtils.pass("Work Additional Detail are displayed");
//        }else{
//            SimpleUtils.fail("Work Additional Detail are not displayed", true);
//        }
//
//    }
//
//    @FindBy(css = "div.availability-container")
//    private WebElement availabilityText;
//
//    @FindBy(css = "availability.ng-isolate-scope") private WebElement availabilityWeeklyView;
//
//    @FindBy(css = "div.profile-close-button-container")
//    private WebElement closeViewProfileContainer;
//
//    @FindBy(css = "[ng-click=\"getLastWeekData()\"]")
//    private WebElement getLastWeekArrow;
//
//    @FindBy(css = "[ng-click=\"getNextWeekData()\"]")
//    private WebElement getNextWeekArrow;
//
//
//
//    @Override
//    public void closeViewProfileContainer() throws Exception{
//        if(isElementEnabled(closeViewProfileContainer,5)){
//            click(closeViewProfileContainer);
//            SimpleUtils.pass("Close button is available and clicked");
//        }
//        else
//        { SimpleUtils.fail("Close Button is not enabled ", true); }
//
//    }
//
//
//    @Override
//    public void verifyAvailabilityDisplayed() throws Exception {
//        if(isElementLoaded(availabilityText,5)){
//            SimpleUtils.pass("AvailabilityText is displayed");
//        }else {
//            SimpleUtils.fail("AvailabilityText is not displayed", true);
//        }
//        if(isElementLoaded(availabilityWeeklyView,5)){
//            SimpleUtils.pass("Availability Weekly View is displayed");
//        }else{
//            SimpleUtils.fail("Availability Weekly View ise not displayed", true);
//        }
//        if(isElementEnabled(getLastWeekArrow, 5) && isElementEnabled(getNextWeekArrow, 5)) {
//            click(getLastWeekArrow);
//            click(getNextWeekArrow);
//            SimpleUtils.pass("Go to last week and next week arrow buttons are clickable ");
//        }else{
//            SimpleUtils.fail("Go to last week and next week arrow buttons are not clickable ", true);
//        }
//
//    }


//    @Override
//    public boolean isViewOpenShiftEnable() throws Exception {
//        if(isElementEnabled(viewOpenShift,5)){
//            SimpleUtils.pass("View Open Shift  is enable/available on Pop Over Style!");
//            return true;
//        }
//        else{
//            SimpleUtils.fail("View Open Shift option is not enable/available on Pop Over Style ",true);
//            return false;
//        }
//    }
//
//    @Override
//    public boolean isChangeRoleEnable() throws Exception {
//        if(isElementEnabled(changeRole,5)){
//            SimpleUtils.pass("Change Role is available on Pop Over Style!");
//            return true;
//        }
//        else{
//            SimpleUtils.fail("Change Role option is not enable/available on Pop Over Style ",true);
//            return false;
//        }
//    }
//
//    public boolean isEditShiftTimeEnable() throws Exception {
//        if(isElementEnabled(editShiftTime,5)){
//            SimpleUtils.pass("Edit Shift Time is available on Pop Over Style!");
//            return true;
//        }
//        else{
//            SimpleUtils.fail("Edit Shift Time is not enable/available on Pop Over Style ",true);
//            return false;
//        }
//    }
//
//    public boolean isEditMealBreakTimeEnable() throws Exception {
//        if(isElementEnabled(editMealBreakTime,5)){
//            SimpleUtils.pass("Edit Meal Break Time is available on Pop Over Style!");
//            return true;
//        }
//        else{
//            SimpleUtils.fail("Edit Meal Break Time is not enable/available on Pop Over Style ",true);
//            return false;
//        }
//    }
//
//    public boolean isDeleteShiftEnable() throws Exception {
//        if(isElementEnabled(deleteShift,5)){
//            SimpleUtils.pass("Delete Shift is available on Pop Over Style!");
//            return true;
//        }
//        else{
//            SimpleUtils.fail("Delete Shift is not enable/available on Pop Over Style ",true);
//            return false;
//        }
//    }

//    @Override
//    public void  verifyDeleteShiftCancelButton() throws Exception {
//        ShiftOperatePage shiftOperatePage = new ConsoleShiftOperatePage();
//        WebElement shift = clickOnProfileIcon();
//        clickTheElement(deleteShift);
//        if (shiftOperatePage.isDeleteShiftShowWell ()) {
//            click(cancelBtnInDeleteWindows);
//            if (isElementEnabled(shift, 5)) {
//                SimpleUtils.pass("Shift not been deleted after click cancel button");
//            } else {
//                SimpleUtils.fail("Shift still been deleted after click cancel button", false);
//            }
//        }
//    }

//    @Override
//    public void  verifyDeleteShift() throws Exception {
//        int count1 = profileIcons.size();
//        System.out.println(count1);
//        clickOnProfileIcon();
//        clickTheElement(deleteShift);
//        if (isDeleteShiftShowWell ()) {
//            click(deleteBtnInDeleteWindows);
//            if (isElementLoaded(deleteShiftImg,5)) {
//                SimpleUtils.pass("delete shift draft successfully");
//            }else
//                SimpleUtils.fail("delete shift draft failed",false);
//        }
//        saveSchedule();
//        waitForSeconds(3);
//        int count2 = profileIcons.size();
//        System.out.println(count2);
//        if (count1 > count2) {
//            SimpleUtils.pass("delete shift successfully");
//        }else
//            SimpleUtils.fail("delete shift draft failed",false);
//    }

//    private boolean isDeleteShiftShowWell() throws Exception {
//        if (isElementLoaded(titleInDeleteWindows,3) && isElementLoaded(alertMsgInDeleteWindows,3)
//                && isElementLoaded(cancelBtnInDeleteWindows,3) && isElementLoaded(deleteBtnInDeleteWindows,3)) {
//            SimpleUtils.pass("delete shift pop up window show well");
//            return true;
//        }else
//            SimpleUtils.fail("delete shift pop up window load failed",true);
//        return false;
//    }


//    @FindBy(css = "div.sch-worker-change-role-title")
//    private WebElement schWorkerInfoPrompt;
//
//    @FindBy(xpath ="//div[contains(@ng-click,'changeRoleMoveLeft($event)')]")
//    private WebElement moveLeftWorkerInfoPrompt;
//
//    @FindBy(xpath ="//div[contains(@ng-click,'changeRoleMoveRight($event)')]")
//    private WebElement moveRightWorkerInfoPrompt;
//
//    @FindBy(xpath= "//span[contains(@class,'sch-worker-change-role-name')]")
//    private List<WebElement> schWrokersList;
//
//    @FindBy(css= "div.sch-worker-change-role-body")
//    private List<WebElement> shiftRoleList;
//
//    @FindBy(css= "div.lgn-alert-modal")
//    private WebElement roleViolationAlter;
//
//    @FindBy(css= "button.lgn-action-button-success")
//    private WebElement roleViolationAlterOkButton;
//
//    public boolean validateVariousWorkRolePrompt() throws Exception{
//        if(isElementEnabled(schWorkerInfoPrompt,5)){
//            SimpleUtils.pass("Various Work Role Prompt is displayed ");
//            if (areListElementVisible(shiftRoleList, 5) && shiftRoleList.size() >0) {
//                if (shiftRoleList.size() < 10){
//                    for (WebElement shiftRole : shiftRoleList) {
//                        click(shiftRole);
//                        SimpleUtils.pass("Role '"+ shiftRole.findElement(By.
//                                cssSelector("span.sch-worker-change-role-name")).getText() +"' is selected!");
//                    }
//                } else {
//                    for (int i =0; i< 9;i++) {
//                        click(shiftRoleList.get(i));
//                        SimpleUtils.pass("Role '"+ shiftRoleList.get(i).findElement(By.
//                                cssSelector("span.sch-worker-change-role-name")).getText() +"' is selected!");
//                    }
//                }
//            } else {
//                SimpleUtils.fail("Work roles are doesn't show well ", true);
//            }
//
//            if (isElementEnabled(applyButtonChangeRole, 5) && isElementEnabled(cancelButtonChangeRole, 5)) {
//                SimpleUtils.pass("Apply and Cancel buttons are enabled");
//                scrollToTop();
//                click(applyButtonChangeRole);
//                if (isElementEnabled(roleViolationAlter, 5)) {
//                    click(roleViolationAlterOkButton);
//                }
//            } else {
//                SimpleUtils.fail("Apply and Cancel buttons are doesn't show well ", false);
//            }
//            return true;
//        } else
//            return false;
//    }

//    public void changeWorkRoleInPrompt(boolean isApplyChange) throws Exception {
//        WebElement clickedShift = clickOnProfileIcon();
//        clickOnChangeRole();
//        if(isElementEnabled(schWorkerInfoPrompt,5)) {
//            SimpleUtils.pass("Various Work Role Prompt is displayed ");
//            String newSelectedWorkRoleName = null;
//            String originSelectedWorkRoleName = null;
//            if (areListElementVisible(shiftRoleList, 5) && shiftRoleList.size() > 0) {
//                if (shiftRoleList.size() == 1) {
//                    SimpleUtils.pass("There is only one Work Role in Work Role list ");
//                    return;
//                } else {
//                    for (WebElement shiftRole : shiftRoleList) {
//                        if (shiftRole.getAttribute("class").contains("sch-worker-change-role-body-selected")) {
//                            originSelectedWorkRoleName = shiftRole.findElement(By.cssSelector("span.sch-worker-change-role-name")).getText();
//                            SimpleUtils.pass("The original selected Role is '" + originSelectedWorkRoleName);
//                            break;
//                        }
//                    }
//                    for (WebElement shiftRole : shiftRoleList) {
//                        if (!shiftRole.getAttribute("class").contains("sch-worker-change-role-body-selected")) {
//                            click(shiftRole);
//                            newSelectedWorkRoleName = shiftRole.findElement(By.cssSelector("span.sch-worker-change-role-name")).getText();
//                            SimpleUtils.pass("Role '" + newSelectedWorkRoleName + "' is selected!");
//                            break;
//                        }
//                    }
//
//                }
//            } else {
//                SimpleUtils.fail("Work roles are doesn't show well ", true);
//            }
//
//            if (isElementEnabled(applyButtonChangeRole, 5) && isElementEnabled(cancelButtonChangeRole, 5)) {
//                SimpleUtils.pass("Apply and Cancel buttons are enabled");
//                if (isApplyChange) {
//                    click(applyButtonChangeRole);
//                    if (isElementEnabled(roleViolationAlter, 5)) {
//                        click(roleViolationAlterOkButton);
//                    }
//                    //to close the popup
//                    waitForSeconds(5);
//                    clickTheElement(clickedShift);
//
//                    clickTheElement(clickedShift.findElement(By.cssSelector(".rows .worker-image-optimized img")));
//                    SimpleUtils.pass("Apply button has been clicked ");
//                } else {
//                    click(cancelButtonChangeRole);
//                    SimpleUtils.pass("Cancel button has been clicked ");
//                }
//            } else {
//                SimpleUtils.fail("Apply and Cancel buttons are doesn't show well ", true);
//            }
//
//            //check the shift role
//            if (!isElementEnabled(changeRole, 5)) {
//                click(clickedShift.findElement(By.cssSelector(".rows .worker-image-optimized img")));
//            }
//            clickOnChangeRole();
//            if (areListElementVisible(shiftRoleList, 5) && shiftRoleList.size() >1) {
//                for (WebElement shiftRole : shiftRoleList) {
//                    if (shiftRole.getAttribute("class").contains("sch-worker-change-role-body-selected")) {
//                        if (isApplyChange) {
//                            if (shiftRole.findElement(By.cssSelector("span.sch-worker-change-role-name")).getText().equals(newSelectedWorkRoleName)) {
//                                SimpleUtils.pass("Shift role been changed successfully ");
//                            } else {
//                                SimpleUtils.fail("Shift role failed to change ", true);
//                            }
//                        } else {
//                            if (shiftRole.findElement(By.cssSelector("span.sch-worker-change-role-name")).getText().equals(originSelectedWorkRoleName)) {
//                                SimpleUtils.pass("Shift role is not change ");
//                            } else {
//                                SimpleUtils.fail("Shift role still been changed when click Cancel button ", true);
//                            }
//                        }
//                        break;
//                    }
//                }
//            } else {
//                SimpleUtils.fail("Shift roles are doesn't show well ", true);
//            }
//            if(isElementLoaded(cancelButtonChangeRole, 5)){
//                click(cancelButtonChangeRole);
//            }
//        }
//    }

//    public void clickOnChangeRole() throws Exception {
//        if(isElementLoaded(changeRole,5))
//        {
//            clickTheElement(changeRole);
//            SimpleUtils.pass("Change Role option is clicked");
//        }
//        else
//            SimpleUtils.fail("Change Role option is not enable",false);
//    }
//
//    @Override
//    public boolean isAssignTMEnable() throws Exception {
//        if(isElementEnabled(assignTM,5)){
//            SimpleUtils.pass("Assign TM is available on Pop Over Style!");
//            return true;
//        }
//        else{
//            SimpleUtils.fail("Assign TM option is not enable/available on Pop Over Style ",true);
//            return false;
//        }
//    }
//    @Override
//    public void clickonAssignTM() throws Exception{
//        if(isAssignTMEnable())
//        {
//            click(assignTM);
//            SimpleUtils.pass("Clicked on Assign TM ");
//        }
//        else
//            SimpleUtils.fail("Assign TM is disabled or not available to Click ", false);
//    }
//
//    @Override
//    public void clickOnConvertToOpenShift() throws Exception{
//        if(isConvertToOpenEnable())
//        {
//            clickTheElement(convertOpen);
//            SimpleUtils.pass("Clicked on Convert to open shift successfully ");
//        } else
//            SimpleUtils.fail(" Convert to open shift is disabled or not available to Click ", false);
//    }
//
//    @Override
//    public void verifyOfferTMOptionIsAvailable() throws Exception{
//        if(isConvertToOpenEnable())
//        {
//            clickTheElement(convertOpen);
//            SimpleUtils.pass("Clicked on Convert to open shift successfully ");
//        } else
//            SimpleUtils.fail(" Convert to open shift is disabled or not available to Click ", false);
//    }
//
//    @Override
//    public void clickOnEditMeaLBreakTime() throws Exception{
//        if(isElementLoaded(editMealBreakTime,5))
//        {
//            click(editMealBreakTime);
//            SimpleUtils.pass("Clicked on Edit Meal Break Time ");
//        }
//        else
//            SimpleUtils.fail("Edit Meal Break Timeis disabled or not available to Click ", false);
//    }



//    @Override
//    public void clickOnEditShiftTime() throws Exception{
//        if(isElementLoaded(editShiftTime,5))
//        {
//            clickTheElement(editShiftTime);
//            SimpleUtils.pass("Clicked on Edit Shift Time ");
//        }
//        else
//            SimpleUtils.fail("Edit Shift Time is disabled or not available to Click ", false);
//    }
//
//    @Override
//    public void clickOnOfferTMOption() throws Exception{
//        if(isElementLoaded(OfferTMS,5)) {
//            clickTheElement(OfferTMS);
//            SimpleUtils.pass("Clicked on Offer Team Members ");
//        } else {
//            SimpleUtils.fail("Offer Team Members is disabled or not available to Click ", false);
//        }
//    }
//
//    @Override
//    public void clickOnEditShiftNotesOption() throws Exception {
//        if(isElementLoaded(EditShiftNotes,5)) {
//            clickTheElement(EditShiftNotes);
//            SimpleUtils.pass("Clicked on EditShiftNotes option ");
//        } else {
//            SimpleUtils.fail("EditShiftNotes is disabled or not available to Click ", false);
//        }
//    }
//
//    @Override
//    public void verifyRecommendedTableHasTM() throws Exception{
//        if (areListElementVisible(recommendedScrollTable, 15)){
//            SimpleUtils.pass("There is a recommended list!");
//        } else {
//            SimpleUtils.fail("No recommended team members!", false);
//        }
//    }

    @FindBy(css="div.edit-breaks-time-modal")
    private WebElement editShiftTimePopUp;

    @FindBy(css="div.worker-shift-container")
    private WebElement shiftInfoContainer;

    @FindBy(css="div.slider-section.slider-section-old")
    private WebElement shiftStartAndEndTimeContainer;

    @FindBy(css="[ng-click=\"closeModal()\"]")
    private WebElement cancelButtonInEditShiftTimeWindow;

    @FindBy(css="[ng-click=\"confirm()\"]")
    private WebElement updateButtonInEditShiftTimeWindow;

    @FindBy(css="div.noUi-handle.noUi-handle-lower")
    private WebElement shiftStartTimeButton;

    @FindBy(css="div.noUi-handle.noUi-handle-upper")
    private WebElement shiftEndTimeButton;

    @FindBy(css="div.slider-section-description-break-time-item-blue")
    private WebElement shiftTimeInEditShiftWindow;

    @FindBy(css=".noUi-marker-large")
    private List<WebElement> shiftTimeLarges;

    @FindBy(css = ".noUi-marker")
    private List<WebElement> noUiMakers;

    @FindBy(css = ".noUi-value")
    private List<WebElement> noUiValues;

//    @Override
//    public void editShiftTimeToTheLargest() throws Exception {
//        if (isElementLoaded(shiftStartTimeButton, 10) && isElementLoaded(shiftEndTimeButton, 10)
//                && areListElementVisible(shiftTimeLarges, 10) && shiftTimeLarges.size() == 2) {
//            mouseHoverDragandDrop(shiftStartTimeButton, shiftTimeLarges.get(0));
//            mouseHoverDragandDrop(shiftEndTimeButton, shiftTimeLarges.get(1));
//        } else {
//            SimpleUtils.fail("Shift time elements failed to load!", false);
//        }
//    }


//    public void verifyEditShiftTimePopUpDisplay() throws Exception {
//        if (isElementEnabled(editShiftTimePopUp, 5)) {
//            if (isElementEnabled(shiftInfoContainer, 5) && isElementEnabled(shiftStartAndEndTimeContainer, 5)
//                    && isElementEnabled(cancelButtonInEditShiftTimeWindow, 5) && isElementEnabled(updateButtonInEditShiftTimeWindow, 5)) {
//                SimpleUtils.pass("Edit Shift Time PopUp window load successfully");
//            } else {
//                SimpleUtils.fail("Items in Edit Shift Time PopUp window load failed", false);
//            }
//        } else {
//            SimpleUtils.fail("Edit Shift Time PopUp window load failed", false);
//        }
//    }

//    public List<String> editShiftTime() throws Exception {
//        List<String> shiftTimes= new ArrayList<>();
//        if (isElementEnabled(shiftStartAndEndTimeContainer, 5) && isElementEnabled(shiftStartTimeButton, 5)
//                && isElementEnabled(shiftEndTimeButton, 5) && isElementEnabled(shiftTimeInEditShiftWindow, 5)) {
//
//            String shiftTimeBeforeUpdate = shiftTimeInEditShiftWindow.getText();
//            shiftTimes.add(0, shiftTimeBeforeUpdate);
//            if (areListElementVisible(noUiMakers, 5) && areListElementVisible(noUiValues, 5) && noUiMakers.size() == noUiValues.size()) {
//                String currentNow = shiftEndTimeButton.getAttribute("aria-valuenow");
//                int currentValue = Integer.parseInt(currentNow.substring(0, currentNow.indexOf('.')));
//                mouseHoverDragandDrop(shiftEndTimeButton, noUiMakers.get(currentValue - 1));
//                waitForSeconds(2);
//            }
//            String shiftTimeAfterUpdate = shiftTimeInEditShiftWindow.getText();
//            if (!shiftTimeBeforeUpdate.equals(shiftTimeAfterUpdate)) {
//                SimpleUtils.pass("Edit Shift Time successfully");
//                shiftTimes.add(1, shiftTimeAfterUpdate);
//            } else {
//                SimpleUtils.fail("Shift Time doesn't change", false);
//            }
//
//        } else {
//            SimpleUtils.fail("Edit Shift Time container load failed", false);
//        }
//        return shiftTimes;
//    }

//    public void verifyShiftTime(String shiftTime) throws Exception {
//        if (isElementEnabled(shiftStartAndEndTimeContainer, 5)) {
//            if (shiftTimeInEditShiftWindow.getText().equals(shiftTime)) {
//                SimpleUtils.pass("Edit Shift Time PopUp window load successfully");
//            }
//
//        } else {
//            SimpleUtils.fail("Edit Shift Time container load failed", false);
//        }
//    }
//
//    public String getShiftTime() {
//        String shiftTime = null;
//        if (isElementEnabled(shiftStartAndEndTimeContainer, 5) && isElementEnabled(shiftTimeInEditShiftWindow,  5)) {
//            shiftTime = shiftTimeInEditShiftWindow.getText();
//        } else {
//            SimpleUtils.fail("Edit Shift Time load failed", true);
//        }
//        return shiftTime;
//    }

//    @Override
//    public void clickOnCancelEditShiftTimeButton() throws Exception{
//        if(isElementLoaded(cancelButtonInEditShiftTimeWindow,5))
//        {
//            click(cancelButtonInEditShiftTimeWindow);
//            SimpleUtils.pass("Clicked on Cancel Edit Shift Time button");
//        }
//        else
//            SimpleUtils.fail("Cancel button is disabled or not available to Click ", false);
//    }

//    @Override
//    public void clickOnUpdateEditShiftTimeButton() throws Exception{
//        if(isElementLoaded(updateButtonInEditShiftTimeWindow,5))
//        {
//            click(updateButtonInEditShiftTimeWindow);
//            SimpleUtils.pass("Clicked on Update Edit Shift Time button");
//        }
//        else
//            SimpleUtils.fail("Update button is disabled or not available to Click ", false);
//    }

//    @Override
//    public void editAndVerifyShiftTime(boolean isSaveChange) throws Exception{
//        List<String> shiftTime;
//
//        WebElement selectedShift = clickOnProfileIcon();
//        clickOnEditShiftTime();
//        shiftTime = editShiftTime();
//        if (isSaveChange) {
//            clickOnUpdateEditShiftTimeButton();
//        } else {
//            clickOnCancelEditShiftTimeButton();
//        }
//
//        clickTheElement(selectedShift.findElement(By.cssSelector(".rows .worker-image-optimized img")));
//        clickOnEditShiftTime();
//
//        if (isSaveChange) {
//            verifyShiftTime(shiftTime.get(1));
//        } else {
//            verifyShiftTime(shiftTime.get(0));
//        }
//
//        clickOnCancelEditShiftTimeButton();
//    }


//    @Override
//    public boolean isConvertToOpenEnable() throws Exception {
//        if(isElementEnabled(convertOpen,5)){
//            SimpleUtils.pass("Convert To Open option is available on Pop Over Style!");
//            return true;
//        }
//        else{
//            SimpleUtils.fail("Convert To Open option is not enable/available on Pop Over Style ",true);
//            return false;
//        }
//    }
//
//    @Override
//    public boolean isOfferTMOptionVisible() throws Exception {
//        if(isElementEnabled(OfferTMS,5)){
//            return true;
//        } else{
//            return false;
//        }
//    }
//
//    @Override
//    public boolean isOfferTMOptionEnabled() throws Exception {
//        if(isElementEnabled(OfferTMS,5) && !OfferTMS.getAttribute("class").toLowerCase().contains("graded-out")){
//            return true;
//        } else{
//            return false;
//        }
//    }

//    @FindBy(css="div.modal-content")
//    private WebElement popupSelectTM;
//
//    @FindBy (css="div.tab.ng-scope")
//    private List<WebElement> subtabsSelectTeamMember;
//
//    @Override
//    public void verifyRecommendedAndSearchTMEnabled() throws Exception
//    {
//        if (isElementLoaded(profileInfoInSeletedTM,3) && isElementLoaded(tabSearchTM,5) && isElementLoaded(tabRecommendedTM,5) && isElementEnabled(closeBtnInSeletedTM, 5)) {
//            SimpleUtils.pass("Select TMs window is opened");
//            for (int i = 0; i <btnSearchteamMember.size() ; i++) {
//                click(btnSearchteamMember.get(i));
//                SimpleUtils.pass(btnSearchteamMember.get(i).getText() +" is enable");
//            }
//        }else
//            SimpleUtils.fail("Select TMs window load failed",true);
//        click(closeBtnInSeletedTM);
//        waitForSeconds(3);
//    }


//    @FindBy(css = "tr.table-row.ng-scope:nth-child(1)")
//    private WebElement firstTableRow;
//
//    @FindBy(css = "tr.table-row.ng-scope:nth-child(1) > td > div:nth-child(2) > div:nth-child(1) > span:nth-child(1)")
//    private WebElement firstnameOfTM;
//    @FindBy(css = "tr.table-row.ng-scope:nth-child(1) > td > div:nth-child(2) > div:nth-child(1) > span:nth-child(2)")
//    private WebElement lastInitialOfTM;
//
//    private String strNameOfTM ;//= firstnameOfTM+" "+lastInitialOfTM;
//
//    @FindBy(css = "tr.table-row.ng-scope:nth-child(1) > td:nth-child(4)>div")
//    private WebElement rdBtnFirstInList;
//
//    @FindBy(css = "span.sch-worker-h-view-last-initial")
//    private WebElement nameLastIntial;
//
//    @FindBy(css = "span.sch-worker-h-view-display-name")
//    private WebElement nameDisplayName;
//
//    @FindBy(css = "div.tab.ng-scope:nth-child(1)")
//    private WebElement tabSearchTM;
//    @FindBy(css = "div.tab.ng-scope:nth-child(2)")
//    private WebElement tabRecommendedTM;
//    @FindBy(css = "[ng-click=\"cancelAction()\"]")
//    private WebElement closeBtnInSeletedTM;
//    @FindBy(css = "div.break-container")
//    private WebElement profileInfoInSeletedTM;
//
//
//    @FindBy(css = "input.form-control.tma-search-field-input-text")
//    private   WebElement textInputBoxOnAssignTM;
//
//    @FindBy(xpath ="//button[contains(@ng-class,'assignActionClass()')]")
//    private WebElement btnAssign;
//    @FindBy(xpath ="//i[contains(@ng-click,'searchAction()')]")
//    private WebElement btnSearch;
//
//    @FindBy(css = "div.lgn-modal-small-title")
//    private WebElement titleOfConvertToOpenShiftPopup;
//
//    @FindBy(css = "div.lgn-modal-small-description")
//    private WebElement descriptionOfConvertToOpenShiftPopup;
//
//
//
//    @FindBy(css = "button.sch-action.sch-cancel")
//    private WebElement btnCancelOpenSchedule;
//
//    @FindBy(css="button.sch-action.sch-save")
//    private WebElement btnYesOpenSchedule;
//
//    @FindBy(xpath ="//div[contains(@ng-class,'selectManualOpenShiftActionClass()')]")
//    private WebElement radioBtnManualOpenShift;
//
//    @FindBy(css ="div.tma-open-shift-manual.ml-10")
//    private WebElement textOfManualOpenShift;
//
//    public boolean verifyConvertToOpenPopUpDisplay(String firstNameOfTM) throws Exception {
//
//        String textOnConvertToOpenPopUp = "Are you sure you want to make this an Open Shift?\n" +
//                firstNameOfTM + " will be losing this shift. Legion will automatically offer the shift to matching team members.\n" +
//                "I want to offer to specific team members";
//        if (isElementLoaded(titleOfConvertToOpenShiftPopup,10) && isElementLoaded(radioBtnManualOpenShift,10)
//                && isElementLoaded(btnCancelOpenSchedule,10) && isElementLoaded(btnYesOpenSchedule,10)
//                && textOnConvertToOpenPopUp.contains(titleOfConvertToOpenShiftPopup.getText().trim())
//                && textOnConvertToOpenPopUp.contains(descriptionOfConvertToOpenShiftPopup.getText().trim())
//                && textOnConvertToOpenPopUp.contains(textOfManualOpenShift.getText().trim())) {
//            SimpleUtils.pass("checkbox is available to offer the shift to any specific TM[optional] Cancel /yes");
//            return true;
//        }else {
//            SimpleUtils.fail("Convert To Open PopUp windows load failed", false);
//        }
//        return false;
//    }
//
//    public void convertToOpenShiftDirectly(){
//        clickTheElement(btnYesOpenSchedule);
//        waitForSeconds(3);
//        SimpleUtils.pass("can convert to open shift by yes button directly");
//
//    }

//    public String convertToOpenShiftAndOfferToSpecificTMs() throws Exception {
//        String selectedTMName = null;
//        if (isElementEnabled(radioBtnManualOpenShift, 5) && isElementEnabled(btnYesOpenSchedule)) {
//            click(radioBtnManualOpenShift);
//            click(btnYesOpenSchedule);
//            waitForSeconds(3);
//            selectedTMName = searchAndGetTMName(propertySearchTeamMember.get("AssignTeamMember"));
//            clickOnOfferOrAssignBtn();
//            SimpleUtils.pass("Shift been convert to open shift and offer to Specific TM successfully");
//        } else {
//            SimpleUtils.fail("Buttons on convert To Open PopUp windows load failed", false);
//        }
//        return selectedTMName;
//    }

//    @Override
//    public void selectConvertToOpenShiftAndOfferToASpecificTMOption() throws Exception {
//        if (isElementEnabled(radioBtnManualOpenShift, 5) && isElementEnabled(btnYesOpenSchedule)) {
//            click(radioBtnManualOpenShift);
//            click(btnYesOpenSchedule);
//            SimpleUtils.pass("convert to open shift and offer to Specific TM option selected!");
//        } else {
//            SimpleUtils.fail("Buttons on convert To Open PopUp windows load failed", false);
//        }
//    }

//    public boolean isEditMealBreakEnabled() throws Exception {
//        clickOnProfileIcon();
//        boolean isEditMealBreakEnabled = false;
//        if(isElementLoaded(editMealBreakTime,5) )
//        {
//            if(editMealBreakTime.getText().equalsIgnoreCase("Edit Meal Break Time")){
//                isEditMealBreakEnabled = true;
//                SimpleUtils.report("Edit Meal Break function is enabled! ");
//            } else{
//                SimpleUtils.report("We can only view breaks!");
//            }
//        }
//        else
//            SimpleUtils.fail("Edit Meal Break Time is disabled or not available to Click ", false);
//        return isEditMealBreakEnabled;
//    }
//
//    @Override
//    public void verifyMealBreakTimeDisplayAndFunctionality(boolean isEditMealBreakEnabled) throws Exception {
//        clickOnProfileIcon();
//        clickOnEditMeaLBreakTime();
//        if (isMealBreakTimeWindowDisplayWell(isEditMealBreakEnabled)) {
//            if (isEditMealBreakEnabled){
//                click(addMealBreakButton);
//                click(continueBtnInMealBreakButton);
//                SimpleUtils.pass("add meal break time successfully");
//            } else {
//                click(continueBtnInMealBreakButton);
//            }
//        }else
//            SimpleUtils.report("add meal break failed");
//    }
//
//    @Override
//    public void verifyDeleteMealBreakFunctionality() throws Exception {
//        WebElement selectedShift = clickOnProfileIcon();
//        clickOnEditMeaLBreakTime();
//        if (isMealBreakTimeWindowDisplayWell(true)) {
//            while (!areListElementVisible(deleteMealBreakButtons, 5) && deleteMealBreakButtons.size()>0) {
//                click(cannelBtnInMealBreakButton);
//                selectedShift = clickOnProfileIcon();
//            }
//            while(deleteMealBreakButtons.size()>0){
//                click(deleteMealBreakButtons.get(0));
//            }
//            click(continueBtnInMealBreakButton);
//            if (isElementEnabled(confirmWindow, 5)) {
//                click(okBtnOnConfirm);
//            }
//
//        }else
//            SimpleUtils.fail("Delete meal break window load failed",true);
//
//        click(selectedShift.findElement(By.cssSelector(".rows .worker-image-optimized img")));
//        clickOnEditMeaLBreakTime();
//        if (isMealBreakTimeWindowDisplayWell(true)) {
//            if (!areListElementVisible(deleteMealBreakButtons, 5)) {
//                SimpleUtils.pass("Delete meal break times successfully");
//            } else {
//                SimpleUtils.fail("Delete meal break failed",false);
//            }
//
//        }else
//            SimpleUtils.fail("Delete meal break window load failed",false);
//        click(cannelBtnInMealBreakButton);
//    }
//
//    public void verifyEditMealBreakTimeFunctionality(boolean isSavedChange) throws Exception {
//        String mealBreakTimeBeforeEdit = null;
//        String mealBreakTimeAfterEdit = null;
//
//        WebElement selectedShift = clickOnProfileIcon();
//        clickOnEditMeaLBreakTime();
//        if (isMealBreakTimeWindowDisplayWell(true)) {
//            if (mealBreakBar.getAttribute("class").contains("disabled")) {
//                click(addMealBreakButton);
//                click(continueBtnInMealBreakButton);
//                if (isElementEnabled(confirmWindow, 5)) {
//                    click(okBtnOnConfirm);
//                }
//                click(selectedShift.findElement(By.cssSelector(".rows .worker-image-optimized img")));
//                clickOnEditMeaLBreakTime();
//            }
//            mealBreakTimeBeforeEdit = mealBreakTimes.get(0).getText();
//            moveDayViewCards(mealBreaks.get(0), 40);
//            mealBreakTimeAfterEdit = mealBreakTimes.get(0).getText();
//            if (isSavedChange) {
//                click(continueBtnInMealBreakButton);
//                if (isElementEnabled(confirmWindow, 5)) {
//                    click(okBtnOnConfirm);
//                }
//            } else {
//                click(cannelBtnInMealBreakButton);
//            }
//        }else
//            SimpleUtils.fail("Meal break window load failed",true);
//
//        click(selectedShift.findElement(By.cssSelector(".rows .worker-image-optimized img")));
//        clickOnEditMeaLBreakTime();
//        if (isMealBreakTimeWindowDisplayWell(true)) {
//            if (isSavedChange) {
//                if (mealBreakTimes.get(0).getText().equals(mealBreakTimeAfterEdit)) {
//                    SimpleUtils.pass("Edit meal break times successfully");
//                } else
//                    SimpleUtils.fail("Edit meal break time failed",true);
//            } else {
//                if (mealBreakTimes.get(0).getText().equals(mealBreakTimeBeforeEdit)) {
//                    SimpleUtils.pass("Edit meal break times not been changed after click Cancel button");
//                } else
//                    SimpleUtils.fail("Edit meal break times still been changed after click Cancel button",true);
//            }
//        }else
//            SimpleUtils.fail("Meal break window load failed",true);
//        click(cannelBtnInMealBreakButton);
//    }
//
//    @Override
//    public boolean isMealBreakTimeWindowDisplayWell(boolean isEditMealBreakEnabled) throws Exception {
//        if (isEditMealBreakEnabled){
//            if (isElementLoaded(editMealBreakTitle,5) && isElementLoaded(addMealBreakButton,5) &&
//                    isElementLoaded(cannelBtnInMealBreakButton,5) && isElementLoaded(continueBtnInMealBreakButton,5)
//                    && isElementLoaded(sliderInMealBreakButton,5) && isElementEnabled(shiftInfoContainer, 5)) {
//                SimpleUtils.pass("the Edit Meal break windows is pop up which include: 1.profile info 2.add meal break button 3.Specify meal break time period 4 cancel ,continue button");
//                return  true;
//            }else
//                SimpleUtils.fail("edit meal break time windows load failed",true);
//            return false;
//        } else {
//            if (isElementLoaded(editMealBreakTitle,5) && isElementLoaded(continueBtnInMealBreakButton,5)
//                    && isElementLoaded(sliderInMealBreakButton,5) && isElementEnabled(shiftInfoContainer, 5)) {
//                SimpleUtils.pass("the Edit Meal break windows is pop up which include: 1.profile info 2.Specify meal break time period 3 continue button");
//                return  true;
//            }else
//                SimpleUtils.fail("edit meal break time windows load failed",true);
//            return false;
//        }
//    }

//    @FindBy(css = "div.sch-day-view-shift-delete")
//    private WebElement btnDelete ;
//
//    @FindBy(css = "div[ng-repeat=\"shift in filteredShifts\"]")
//    private List<WebElement> shiftsInDayViewNew;
//    @FindBy(css = ".sch-day-view-shift")
//    private List<WebElement> shiftsInDayView;
//
//    //update by haya
//    public void validateXButtonForEachShift() throws Exception{
//        ScheduleMainPage scheduleMainPage = new ConsoleScheduleMainPage();
//        String deletedInfo = "Deleted";
//        int shiftCount = 0;
//        if (areListElementVisible(shiftsInDayViewNew, 5)) {
//            shiftCount = shiftsInDayViewNew.size();
//            for (int i = 0; i < shiftsInDayViewNew.size(); i++) {
//                List<WebElement> tempShifts = getDriver().findElements(By.cssSelector("div[ng-repeat=\"shift in filteredShifts\"]"));
//                click(tempShifts.get(i));
//                if (isElementEnabled(btnDelete, 5)) {
//                    SimpleUtils.pass(": X button is present for selected Shift");
//                    clickTheElement(btnDelete);
//                    // To avoid stale element issue
//                    tempShifts = getDriver().findElements(By.cssSelector("div[ng-repeat=\"shift in filteredShifts\"]"));
//                    String deletedShiftInfo = tempShifts.get(i).findElement(By.cssSelector("div.sch-day-view-right-gutter-text")).getText();
//                    if (deletedShiftInfo.contains(deletedInfo)) {
//                        SimpleUtils.pass("can delete shift by X button");
//                        break;
//                    } else {
//                        SimpleUtils.fail("delete shift failed by X button, no deleted guter text!", true);
//                    }
//                    break;
//                } else SimpleUtils.fail("X button is not present for ", true);
//            }
//        } else if (areListElementVisible(shiftsInDayView, 5)) {
//            shiftCount = shiftsInDayView.size();
//            for (int i = 0; i < shiftsInDayView.size(); i++) {
//                List<WebElement> tempShifts = getDriver().findElements(By.cssSelector(".sch-day-view-shift"));
//                moveToElementAndClick(tempShifts.get(i));
//                if (isElementEnabled(btnDelete, 5)) {
//                    SimpleUtils.pass(": X button is present for selected Shift");
//                    clickTheElement(btnDelete);
//                    // To avoid stale element issue
//                    String deletedShiftInfo = getDriver().findElements(By.cssSelector("div.sch-day-view-right-gutter-text")).get(i).getText();
//                    if (deletedShiftInfo.contains(deletedInfo)) {
//                        SimpleUtils.pass("can delete shift by X button");
//                        break;
//                    } else {
//                        SimpleUtils.fail("delete shift failed by X button, no deleted guter text!", true);
//                    }
//                    break;
//                } else SimpleUtils.fail("X button is not present for ", true);
//            }
//        } else {
//            SimpleUtils.fail("There is no shifts in day view!", false);
//        }
//        scheduleMainPage.saveSchedule();
//        int shiftCountAftDelete =  scheduleShiftsRows.size();
//        if (shiftCountAftDelete < shiftCount) {
//            SimpleUtils.pass("delete shift successfully by X button");
//
//        }else
//            SimpleUtils.fail("delete shift failed by X button",false);
//    }

//    @Override
//    public boolean verifyContextOfTMDisplay() throws Exception {
//        clickOnProfileIcon();
//        if (isViewProfileEnable() &&  isChangeRoleEnable() && isAssignTMEnable() && isConvertToOpenEnable() && isEditShiftTimeEnable() && isEditMealBreakTimeEnable() && isDeleteShiftEnable()) {
//            SimpleUtils.pass("context of any TM show well and include: 1. View profile 2. Change shift role  3.Assign TM 4.  Convert to open shift is enabled for current and future week day 5.Edit meal break time 6. Delete shift");
//            return true;
//
//        }else
//            return false;
//    }

//    @Override
//    public void verifyChangeRoleFunctionality() throws Exception {
//
//        if (validateVariousWorkRolePrompt()) {
//            SimpleUtils.pass("various work role any one of them can be selected");
//        }else
//            SimpleUtils.fail("various work load failed",true);
//    }
//
//    @FindBy(css = "div.tma-time-interval.fl-right.ng-binding")
//    private WebElement timeDurationWhenCreateNewShift;
//
//    @Override
//    public String getTimeDurationWhenCreateNewShift() throws Exception {
//        if (isElementLoaded(timeDurationWhenCreateNewShift,5)) {
//            String timeDuration = timeDurationWhenCreateNewShift.getText();
//            return timeDuration;
//        }else
//            SimpleUtils.fail("time duration load failed",true);
//        return null;
//    }

//    @Override
//    public void verifyConvertToOpenShiftBySelectedSpecificTM() throws Exception {
//        clickOnProfileIcon();
//        if (isConvertToOpenEnable()) {
//            click(convertOpen);
//        }
//        if (isElementLoaded(offerToSpecificTMBtn,3) && isElementLoaded(convertToOpenYesBtn,3)) {
//            click(offerToSpecificTMBtn);
//            click(convertToOpenYesBtn);
//            verifySelectTeamMembersOption();
//            if(isElementLoaded(btnAssignAnyway,5))
//                click(btnAssignAnyway);
//            clickOnOfferOrAssignBtn();
//        }else
//            SimpleUtils.fail("offer to Specific tm button load failed",true);
//    }

//    @Override
//    public void selectSpecificWorkDay(int dayCountInOneWeek) {
//        if (areListElementVisible(weekDays, 5) && weekDays.size() == 7) {
//            for (int i = 0; i < dayCountInOneWeek; i++) {
//                if (!weekDays.get(i).getAttribute("class").contains("selected")) {
//                    click(weekDays.get(i));
//                }
//            }
//        }else
//            SimpleUtils.fail("week days load failed",true);
//    }
//
//
//    @FindBy(xpath = "//div/shift-hover/div/div[5]/div[1]")
//    private WebElement totalHoursInWeekFromPopUp;

//    @Override
//    public float getShiftHoursByTMInWeekView(String teamMember) {
//        Float timeDurationForTMInWeek = 0.0f;
//        waitForSeconds(5);
//        if (areListElementVisible(workerNameList,5) ) {
//            for (int i = 0; i <workerNameList.size() ; i++) {
//                if ( workerNameList.get(i).getText().trim().toLowerCase().contains(teamMember.toLowerCase())) {
//                    click(scheduleInfoIcon.get(i));
//                    String[] timeDurationForTMContext = totalHoursInWeekFromPopUp.getText().split(" ");
//                    timeDurationForTMInWeek = Float.valueOf(timeDurationForTMContext[3].trim());
//                    break;
//                }
//            }
//        }else
//            timeDurationForTMInWeek = 0.0f;
//        return timeDurationForTMInWeek;
//    }

//    @Override
//    public void selectSpecificTMWhileCreateNewShift(String teamMemberName) throws Exception {
//        if(areListElementVisible(btnSearchteamMember,5)){
//            click(btnSearchteamMember.get(1));
//            searchText(teamMemberName);
//        }
//
//    }

//    @FindBy (css = "[ng-repeat=\"message in getComplianceMessages()\"]")
//    private List<WebElement> otFlagandOThoursInWeekForTM;
//    @Override
//    public void verifyWeeklyOverTimeAndFlag(String teamMemberName) throws Exception {
//        boolean flag = false;
//        if (areListElementVisible(otFlagandOThoursInWeekForTM,10)){
//            for (WebElement e : otFlagandOThoursInWeekForTM){
//                if (e.getText().contains("week overtime")){
//                    flag = true;
//                    break;
//                }
//            }
//        } else {
//            SimpleUtils.fail("Flag is not present for team member " + propertySearchTeamMember.get("TeamMember"), false);
//        }
//        if (flag) {
//            SimpleUtils.pass("week overtime shifts created successfully");
//        } else {
//            SimpleUtils.fail("weekly overtime Flag is not present for team member " + propertySearchTeamMember.get("TeamMember"), false);
//        }
//    }

//    @FindBy(css = ".sch-worker-h-view")
//    private WebElement workerInfoFromShift;
//    @Override
//    public Map<String, String> getHomeLocationInfo() throws Exception {
//        Map<String, String> resultInfo = new HashMap<String, String>();
//        if (isElementLoaded(workerInfoFromShift.findElement(By.cssSelector(".sch-worker-h-view-display-name")), 10)){
//            resultInfo.put("worker name", workerInfoFromShift.findElement(By.cssSelector(".sch-worker-h-view-display-name")).getText().replace("\n", ""));
//
//        } else {
//            SimpleUtils.fail("Worker name info fail to load!", false);
//        }
//        if (isElementLoaded(workerInfoFromShift.findElement(By.cssSelector(".sch-worker-role")), 10)){
//            if (workerInfoFromShift.findElement(By.cssSelector(".sch-worker-role")).getText().split("\n").length == 2){
//                resultInfo.put("job title", workerInfoFromShift.findElement(By.cssSelector(".sch-worker-role")).getText().split("\n")[0]);
//                resultInfo.put("PTorFT", workerInfoFromShift.findElement(By.cssSelector(".sch-worker-role")).getText().split("\n")[1]);
//            } else {
//                SimpleUtils.fail("Work role and PT or FT info are not expected!", false);
//            }
//        } else {
//            SimpleUtils.fail("Work role info fail to load!", false);
//        }
//        if (isElementLoaded(workerInfoFromShift.findElement(By.cssSelector(".sch-worker-h-view-role-name")), 10)){
//            resultInfo.put("homeLocation", workerInfoFromShift.findElement(By.cssSelector(".sch-worker-h-view-role-name")).getText());
//        } else {
//            SimpleUtils.fail("Home location info fail to load!", false);
//        }
//        if (areListElementVisible(workerInfoFromShift.findElements(By.cssSelector(".one-badge")), 10)){
//            resultInfo.put("badgeSum", String.valueOf(workerInfoFromShift.findElements(By.cssSelector(".one-badge")).size()));
//            String badgeInfo = "";
//            for (WebElement element: workerInfoFromShift.findElements(By.cssSelector(".one-badge"))){
//                badgeInfo = badgeInfo + " " + element.getAttribute("data-original-title");
//            }
//            resultInfo.put("badgeInfo", badgeInfo);
//        } else {
//            resultInfo.put("badgeSum", "0");
//        }
//        return resultInfo;
//    }

//    @Override
//    public void deleteTMShiftInWeekView(String teamMemberName) throws Exception {
//        if (areListElementVisible(shiftsWeekView, 15)) {
//            for (WebElement shiftWeekView : shiftsWeekView) {
//                try {
//                    WebElement workerName = shiftWeekView.findElement(By.className("week-schedule-worker-name"));
//                    if (workerName != null) {
//                        if (workerName.getText().toLowerCase().trim().contains(teamMemberName.toLowerCase().trim())) {
//                            WebElement image = shiftWeekView.findElement(By.cssSelector(".rows .week-view-shift-image-optimized span"));
//                            //WebElement image = shiftWeekView.findElement(By.cssSelector(".sch-day-view-shift-worker-detail"));
//                            clickTheElement(image);
//                            waitForSeconds(3);
//                            if (isElementLoaded(deleteShift, 10)) {
//                                clickTheElement(deleteShift);
//                                waitForSeconds(4);
//                                if (isElementLoaded(deleteBtnInDeleteWindows, 30)) {
//                                    clickTheElement(deleteBtnInDeleteWindows);
//                                    SimpleUtils.pass("Schedule Week View: Existing shift: " + teamMemberName + " delete successfully");
//                                    waitForSeconds(1);
//                                } else
//                                    SimpleUtils.fail("delete confirm button load failed", false);
//                            } else
//                                SimpleUtils.fail("delete item for this TM load failed", false);
//                        }
//                    }
//                } catch (Exception e) {
//                    continue;
//                }
//            }
//        }else
//            SimpleUtils.report("Schedule Week View: shifts load failed or there is no shift in this week");
//    }

    //added by Estelle for job title filter functionality

//    public void filterScheduleByJobTitleWeekView(ArrayList<WebElement> jobTitleFilters, ArrayList<String> availableJobTitleList) throws Exception{
//
//        for (WebElement jobTitleFilter : jobTitleFilters) {
//            Thread.sleep(1000);
//            if (filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
//                click(filterButton);
//            unCheckFilters(jobTitleFilters);
//            String jobTitle = jobTitleFilter.getText();
//            SimpleUtils.report("Data for job title: '" + jobTitle + "' as bellow");
//            clickTheElement(jobTitleFilter.findElement(By.cssSelector("input[type=\"checkbox\"]")));
//            click(filterButton);
//            String cardHoursAndWagesText = "";
//            SmartCardPage smartCardPage = new ConsoleSmartCardPage();
//            HashMap<String, Float> hoursAndWagesCardData = smartCardPage.getScheduleLabelHoursAndWages();
//            for (Entry<String, Float> hoursAndWages : hoursAndWagesCardData.entrySet()) {
//                if (cardHoursAndWagesText != "")
//                    cardHoursAndWagesText = cardHoursAndWagesText + ", " + hoursAndWages.getKey() + ": '" + hoursAndWages.getValue() + "'";
//                else
//                    cardHoursAndWagesText = hoursAndWages.getKey() + ": '" + hoursAndWages.getValue() + "'";
//            }
//            SimpleUtils.report("Active Week Card's Data: " + cardHoursAndWagesText);
//            if (availableJobTitleList.contains(jobTitle.toLowerCase().trim())) {
//                SimpleUtils.assertOnFail("Sum of Daily Schedule Hours not equal to Active Week Schedule Hours!", newVerifyActiveWeekDailyScheduleHoursInWeekView(), true);
//            }else
//                SimpleUtils.report("there is no data for this job title: '" + jobTitle+ "'");
//        }
//    }
//
//    public void filterScheduleByJobTitleDayView(ArrayList<WebElement> jobTitleFilters,ArrayList<String> availableJobTitleList) throws Exception{
//        for (WebElement jobTitleFilter : jobTitleFilters) {
//            if (filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
//                click(filterButton);
//            unCheckFilters(jobTitleFilters);
//            String jobTitle = jobTitleFilter.getText();
//            SimpleUtils.report("Data for job title: '" + jobTitle + "' as bellow");
//            click(jobTitleFilter);
//            click(filterButton);
//            String cardHoursAndWagesText = "";
//            SmartCardPage smartCardPage = new ConsoleSmartCardPage();
//            HashMap<String, Float> hoursAndWagesCardData = smartCardPage.getScheduleLabelHoursAndWages();
//            for (Entry<String, Float> hoursAndWages : hoursAndWagesCardData.entrySet()) {
//                if (cardHoursAndWagesText != "")
//                    cardHoursAndWagesText = cardHoursAndWagesText + ", " + hoursAndWages.getKey() + ": '" + hoursAndWages.getValue() + "'";
//                else
//                    cardHoursAndWagesText = hoursAndWages.getKey() + ": '" + hoursAndWages.getValue() + "'";
//            }
//            SimpleUtils.report("Active Day Card's Data: " + cardHoursAndWagesText);
//            float activeDayScheduleHoursOnCard = smartCardPage.getScheduleLabelHoursAndWages().get(scheduleHoursAndWagesData.scheduledHours.getValue());
//            float totalShiftsWorkTime = getActiveShiftHoursInDayView();
//            SimpleUtils.report("Active Day Total Work Time Data: " + totalShiftsWorkTime);
//            if (availableJobTitleList.contains(jobTitle.toLowerCase().trim())) {
//                if (activeDayScheduleHoursOnCard - totalShiftsWorkTime <= 0.05) {
//                    SimpleUtils.pass("Schedule Hours in smart card  equal to total Active Schedule Hours by job title filter ");
//                }else
//                    SimpleUtils.fail("the job tile filter hours not equal to schedule hours in schedule samrtcard",false);
//            }else
//                SimpleUtils.report( "there is no data for this job title: '" + jobTitle + "'");
//        }
//    }
//
//
//    public void filterScheduleByJobTitle(boolean isWeekView) throws Exception{
//        ArrayList<String> availableJobTitleList = new ArrayList<>();
//        if (isWeekView == true) {
//            availableJobTitleList = getAvailableJobTitleListInWeekView();
//        }else
//            availableJobTitleList = getAvailableJobTitleListInDayView();
//
//        waitForSeconds(10);
//        String jobTitleFilterKey = "jobtitle";
//        HashMap<String, ArrayList<WebElement>> availableFilters = getAvailableFilters();
//        if (availableFilters.size() > 1) {
//            ArrayList<WebElement> jobTitleFilters = availableFilters.get(jobTitleFilterKey);
//            if (filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
//                click(filterButton);
//            unCheckFilters(jobTitleFilters);
//            if (isWeekView) {
//                filterScheduleByJobTitleWeekView(jobTitleFilters, availableJobTitleList);
//            }
//            else {
//                filterScheduleByJobTitleDayView(jobTitleFilters, availableJobTitleList);
//            }
//        } else {
//            SimpleUtils.fail("Filters are not appears on Schedule page!", false);
//        }
//
//    }

//    @FindBy(className = "week-schedule-shift-title")
//    private List<WebElement> availableJobTitleListInWeekView;
//
//    @FindBy(className = "sch-group-label")
//    private List<WebElement> availableJobTitleListInDayView;
//
//    public ArrayList<String> getAvailableJobTitleListInWeekView(){
//        ArrayList<String> availableJobTitleList = new ArrayList<>();
//        for (WebElement jobTitle:availableJobTitleListInWeekView
//        ) {
//            availableJobTitleList.add(jobTitle.getText().toLowerCase().trim());
//        }
//
//        return availableJobTitleList;
//    }
//
//    public ArrayList<String> getAvailableJobTitleListInDayView(){
//        ArrayList<String> availableJobTitleList = new ArrayList<>();
//        for (WebElement jobTitle:availableJobTitleListInDayView
//        ) {
//            availableJobTitleList.add(jobTitle.getText().toLowerCase().trim());
//        }
//
//        return availableJobTitleList;
//    }

    @FindBy(css = "div.day-view-shift-hover-info-icon")
    private List<WebElement> scheduleInfoIconInDayView;

    @FindBy(xpath = "//div/shift-hover/div/div[5]/div[1]")
    private WebElement  workHoursInDayViewFromPopUp;

//    public float getActiveShiftHoursInDayView() {
//        Float totalDayWorkTime = 0.0f;
//        if (areListElementVisible(scheduleTableWeekViewWorkerDetail,5) ) {
//            for (int i = 0; i <scheduleTableWeekViewWorkerDetail.size() ; i++) {
//                clickTheElement(scheduleInfoIconInDayView.get(i));
//                String[] timeDurationForTMContext = workHoursInDayViewFromPopUp.getText().split(" ");
//                float shiftSizeInHour = Float.valueOf(timeDurationForTMContext[0]);
//                totalDayWorkTime = totalDayWorkTime+shiftSizeInHour;
//            }
//        }else
//            totalDayWorkTime = 0.0f;
//        return totalDayWorkTime;
//    }
//
//
//    @Override
//    public void filterScheduleByWorkRoleAndJobTitle(boolean isWeekView) throws Exception{
//        waitForSeconds(10);
//        ArrayList<String> availableJobTitleList = new ArrayList<>();
//        if (isWeekView == true) {
//            availableJobTitleList = getAvailableJobTitleListInWeekView();
//        }else
//            availableJobTitleList = getAvailableJobTitleListInDayView();
//
//        String workRoleFilterKey = "workrole";
//        String jobTitleFilterKey = "jobtitle";
//
//        HashMap<String, ArrayList<WebElement>> availableFilters = getAvailableFilters();
//        if (availableFilters.size() > 1) {
//            ArrayList<WebElement> workRoleFilters = availableFilters.get(workRoleFilterKey);
//            ArrayList<WebElement> jobTitleFilters = availableFilters.get(jobTitleFilterKey);
//            for (WebElement workRoleFilter : workRoleFilters) {
//                if (filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
//                    click(filterButton);
//                unCheckFilters(workRoleFilters);
//                click(workRoleFilter.findElement(By.cssSelector("input[type=\"checkbox\"]")));
//                SimpleUtils.report("Data for Work Role: '" + workRoleFilter.getText() + "'");
//                if (isWeekView) {
//                    filterScheduleByJobTitleWeekView(jobTitleFilters, availableJobTitleList);
//                }else {
//                    filterScheduleByJobTitleDayView(jobTitleFilters, availableJobTitleList);
//                }
//            }
//        } else {
//            SimpleUtils.fail("Filters are not appears on Schedule page!", false);
//        }
//    }
//    @Override
//    public void filterScheduleByShiftTypeAndJobTitle(boolean isWeekView) throws Exception{
//        waitForSeconds(10);
//        ArrayList<String> availableJobTitleList = new ArrayList<>();
//        if (isWeekView == true) {
//            availableJobTitleList = getAvailableJobTitleListInWeekView();
//        }else
//            availableJobTitleList = getAvailableJobTitleListInDayView();
//
//        String shiftTypeFilterKey = "shifttype";
//        String jobTitleFilterKey = "jobtitle";
//
//        HashMap<String, ArrayList<WebElement>> availableFilters = getAvailableFilters();
//        if (availableFilters.size() > 1) {
//            ArrayList<WebElement> shiftTypeFilters = availableFilters.get(shiftTypeFilterKey);
//            ArrayList<WebElement> jobTitleFilters = availableFilters.get(jobTitleFilterKey);
//            for (WebElement shiftTypeFilter : shiftTypeFilters) {
//                if (filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
//                    click(filterButton);
//                unCheckFilters(shiftTypeFilters);
//                click(shiftTypeFilter);
//                SimpleUtils.report("Data for Work Role: '" + shiftTypeFilter.getText() + "'");
//                if (isWeekView) {
//                    filterScheduleByJobTitleWeekView(jobTitleFilters, availableJobTitleList);
//                }else {
//                    filterScheduleByJobTitleDayView(jobTitleFilters, availableJobTitleList);
//                }
//            }
//        } else {
//            SimpleUtils.fail("Filters are not appears on Schedule page!", false);
//        }
//    }
//
//    //Added by Julie
//    @FindBy(css = "div.rows")
//    private List<WebElement> weekScheduleShiftsOfWeekView;
//
//    @FindBy(css = ".sch-day-view-shift-time")
//    private List<WebElement> weekScheduleShiftsTimeOfMySchedule;
//
//    @FindBy(css = ".my-schedule-no-schedule")
//    private WebElement myScheduleNoSchedule;
//
//    @FindBy(className = "sch-grid-container")
//    private WebElement scheduleTable;
//
//    @FindBy(css = "div.lg-picker-input")
//    private WebElement currentLocationOnSchedulePage;
//
//    @FindBy(css = ".sub-navigation-view-link")
//    private List<WebElement> subMenusOnSchedulePage;
//
//    @FindBy(css = "[ng-repeat=\"opt in opts\"] input-field")
//    private List<WebElement> shiftTypes;
//
    @FindBy(css = "div[ng-attr-class^=\"sch-date-title\"]")
    private List<WebElement> weekScheduleShiftsDateOfMySchedule;
//
//    @FindBy(css = "div.sch-day-view-grid-header span")
//    private List<WebElement> scheduleShiftTimeOnHeader;
//
//    @FindBy(css = ".day-view-shift-hover-info-icon")
//    private List<WebElement> hoverIcons;
//
//    @FindBy(className = "popover-content")
//    private WebElement popOverContent;
//
//    @FindBy(css = ".card-carousel-card-default")
//    private WebElement openShiftCard;
//
//    @FindBy(className = "card-carousel-link")
//    private WebElement viewShiftsBtn;
//
//    @FindBy(css = "h1[ng-if=\"weeklyScheduleData.hasSchedule !== 'FALSE'\"]")
//    private WebElement openShiftData;
//
//    @FindBy(css = "img[src*=\"openShift\"]")
//    private List<WebElement> blueIconsOfOpenShift;
//
//    @FindBy(css = "[ng-if=\"isGenerateOverview()\"] h1")
//    private WebElement weekInfoBeforeCreateSchedule;
//
//    @FindBy (css = ".modal-instance-header-title")
//    private WebElement headerWhileCreateSchedule;
//
//    @FindBy (css = ".generate-modal-location")
//    private WebElement locationWhileCreateSchedule;
//
//    @FindBy (css = ".text-right[ng-if=\"hasBudget\"]")
//    private List<WebElement> budgetedHoursOnSTAFF;
//
//    @FindBy (xpath = "//div[contains(text(), \"Weekly Budget\")]/following-sibling::h1[1]")
//    private WebElement budgetHoursOnWeeklyBudget;
//
//    @FindBy (css = "[x=\"25\"]")
//    private List<WebElement> budgetHrsOnGraph;
//
//    @FindBy (xpath = "//p[contains(text(),\"Target Budget: \")]/span")
//    private WebElement targetBudget;
//
//    @FindBy (css = ".generate-modal-week-container.selected text[x=\"85\"]")
//    private WebElement scheduledHrsOnGraph;
//
//    @FindBy (xpath = "//div[contains(text(), \"Action Required\")]/following-sibling::h1[1]")
//    private WebElement changesOnActionRequired;
//
//    @FindBy(css = "img[ng-if=\"unpublishedDeleted && isOneAndOnlyShiftTypeSelected('Edited')\"]")
//    private WebElement tooltipIconOfUnpublishedDeleted;
//
//    @FindBy (className = "sch-calendar-day")
//    private List<WebElement> scheduleCalendarDays;
//
    @FindBy (className = "tma-header-text")
    private WebElement titleInSelectTeamMemberWindow;
//
//    @FindBy (className = "worker-edit-availability-status")
//    private WebElement messageInSelectTeamMemberWindow;
//
//    @FindBy (css = "[ng-repeat=\"worker in searchResults\"] .tma-staffing-option-outer-circle")
//    private WebElement optionCircle;
//
//    @FindBy (css = "[ng-click=\"cancelAction()\"]")
//    private WebElement closeButtonOnCustomize;
//
//    List<String> weekScheduleShiftTimeListOfWeekView = new ArrayList<String>();
//    List<String> weekScheduleShiftTimeListOfMySchedule = new ArrayList<String>();
//
//    @Override
//    public List<String> getWeekScheduleShiftTimeListOfWeekView(String teamMemberName) throws Exception {
//        //clickOnWeekView();
//        if (areListElementVisible(weekScheduleShiftsOfWeekView, 10) && weekScheduleShiftsOfWeekView.size() != 0) {
//            for (int i = 0; i < weekScheduleShiftsOfWeekView.size(); i++) {
//                if (weekScheduleShiftsOfWeekView.get(i).findElement(By.cssSelector(".week-schedule-worker-name")).getText().contains(teamMemberName)) {
//                    weekScheduleShiftTimeListOfWeekView.add(weekScheduleShiftsOfWeekView.get(i).findElement(By.cssSelector(".week-schedule-shift-time")).getText().replace(" ", "").toLowerCase());
//                }
//            }
//        } else if (weekScheduleShiftsOfWeekView.size() == 0) {
//            SimpleUtils.report("Schedule Week View Page: No shift available");
//        } else {
//            SimpleUtils.fail("Schedule Week View Page: Failed to load shifts", true);
//        }
//        return weekScheduleShiftTimeListOfWeekView;
//    }

//    public List<String> getWeekScheduleShiftTimeListOfMySchedule() throws Exception {
//        if (areListElementVisible(weekScheduleShiftsTimeOfMySchedule, 20)) {
//            if (weekScheduleShiftsTimeOfMySchedule.size() > 0) {
//                for (int i = 0; i < weekScheduleShiftsTimeOfMySchedule.size(); i++) {
//                    weekScheduleShiftTimeListOfMySchedule.add(weekScheduleShiftsTimeOfMySchedule.get(i).getText().trim().replace(":00", ""));
//                }
//            } else if (weekScheduleShiftsTimeOfMySchedule.size() == 0)
//                SimpleUtils.report("My Schedule Page: No shift hours in the schedule table");
//        } else if (isElementLoaded(myScheduleNoSchedule, 10)) {
//            SimpleUtils.report("My Schedule Page: Schedule has not been generated.");
//        } else
//            SimpleUtils.fail("My Schedule Page: Failed to load shifts", true);
//        return weekScheduleShiftTimeListOfMySchedule;
//    }
//
//    @Override
//    public void validateTheAvailabilityOfScheduleTable(String userName) throws Exception {
//        if (isElementLoaded(scheduleTable, 10)) {
//            SimpleUtils.pass("My Schedule Page: Schedule table is present");
//            if (scheduleShiftsRows.size() > 0) {
//                for (WebElement scheduleShift : scheduleShiftsRows) {
//                    if (scheduleShift.getText().toLowerCase().contains(userName.toLowerCase())) {
//                        SimpleUtils.pass("My Schedule Page: TM's Schedules show in schedule table");
//                        break;
//                    } else if (scheduleShift.getText() == null)
//                        SimpleUtils.report("My Schedule Page: TM's Schedules are empty");
//                    else
//                        SimpleUtils.fail("My Schedule Page: TM's Schedules don't show in schedule table", true);
//                }
//            }
//        } else if (isElementLoaded(myScheduleNoSchedule, 10)) {
//            SimpleUtils.report("My Schedule Page: Schedule has not been generated.");
//        } else {
//            SimpleUtils.fail("My Schedule Page: Failed to load shifts", true);
//        }
//    }
//
//    @FindBy (css = ".lg-picker-input__wrapper .lg-search-options")
//    private WebElement locationDropDown;
//
//    @Override
//    public void validateTheDisabilityOfLocationSelectorOnSchedulePage() throws Exception {
//        if (isElementLoaded(currentLocationOnSchedulePage, 10)) {
//            if (currentLocationOnSchedulePage.getCssValue("cursor").contains("not-allowed"))
//                SimpleUtils.pass("My Schedule Page: Location selector is in disable mode");
//            else if (getDriver().findElement(By.cssSelector("lg-upperfield-navigation div.lg-picker-input")).equals(currentLocationOnSchedulePage)) {
//                click(currentLocationOnSchedulePage);
//                if (isElementLoaded(locationDropDown,5))
//                    SimpleUtils.pass("My Schedule Page: Location selector can be clicked since upperfield is enabled");
//                else
//                    SimpleUtils.fail("My Schedule Page: Location selector cannot be clicked when upperfield is enabled", true);
//            } else
//                SimpleUtils.fail("My Schedule Page: Location selector is still in enable mode",false);
//        } else SimpleUtils.fail("My Schedule Page: Location failed to load", true);
//    }

//    @Override
//    public void goToConsoleScheduleAndScheduleSubMenu() throws Exception {
//        if (isElementLoaded(consoleSchedulePageTabElement, 5)) {
//            clickTheElement(consoleSchedulePageTabElement);
//            clickTheElement(ScheduleSubMenu);
//            ScheduleCommonPage scheduleCommonPage = new ConsoleScheduleCommonPage();
//            if (scheduleCommonPage.verifyActivatedSubTab("Schedule"))
//                SimpleUtils.pass("Schedule Page: 'Schedule' tab loaded Successfully!");
//            else
//                SimpleUtils.fail("Schedule Page: 'Schedule' tab not loaded", false);
//        }
//    }

//    @Override
//    public void validateTheAvailabilityOfScheduleMenu() throws Exception {
//        if (areListElementVisible(subMenusOnSchedulePage, 10)) {
//            if (subMenusOnSchedulePage.size() > 2) {
//                SimpleUtils.pass("Schedule Page: It has at least two sub menus successfully");
//            } else {
//                SimpleUtils.fail("Schedule Page: It doesn't have two sub menus", false);
//            }
//            for (WebElement subMenu : subMenusOnSchedulePage) {
//                if (subMenu.getText().trim().equals("My Schedule") || subMenu.getText().trim().equals("Team Schedule") || subMenu.getText().trim().equals("My Clocks") ) {
//                    SimpleUtils.pass("Schedule Page: It includes " + subMenu.getText());
//                } else {
//                    SimpleUtils.fail("Schedule Page: " + subMenu.getText() + " isn't expected in sub menu list", true);
//                }
//            }
//        } else {
//            SimpleUtils.fail("Schedule Page: Sub menu list failed to load", true);
//        }
//    }
//
//    @Override
//    public void validateTheFocusOfSchedule() throws Exception {
//        if (areListElementVisible(subMenusOnSchedulePage, 10) && subMenusOnSchedulePage.size() > 1) {
//            if (subMenusOnSchedulePage.get(0).getAttribute("class").contains("active") && subMenusOnSchedulePage.get(0).getText().contains("My Schedule")) {
//                SimpleUtils.pass("Schedule Page: My schedule is selected by default not the Team schedule successfully ");
//            } else
//                SimpleUtils.fail("Schedule Page: My schedule isn't selected by default", true);
//        } else SimpleUtils.fail("Schedule Page: Sub menus failed to load", true);
//    }
//
//    @Override
//    public void validateTheDefaultFilterIsSelectedAsScheduled() throws Exception {
//        if (isElementLoaded(filterButton, 5)) {
//            click(filterButton);
//            if (shiftTypes.size() > 0) {
//                for (WebElement shiftType : shiftTypes) {
//                    WebElement filterCheckBox = shiftType.findElement(By.tagName("input"));
//                    if (filterCheckBox.getAttribute("class").contains("ng-not-empty")) {
//                        if (shiftType.getText().contains("Scheduled"))
//                            SimpleUtils.pass("My Schedule Page: Scheduled filter is applied by default successfully");
//                        else
//                            SimpleUtils.fail("My Schedule Page: Scheduled filter isn't applied by default successfully", false);
//                    }
//                }
//            } else if (isElementLoaded(myScheduleNoSchedule, 10)) {
//                SimpleUtils.report("My Schedule Page: Schedule has not been generated.");
//            } else
//                SimpleUtils.fail("My Schedule Page: No schedule shift type can be applied", true);
//            //Click again to close the pop up menu
//            click(filterButton);
//        } else
//            SimpleUtils.fail("My Schedule Page: Filter button failed to load", true);
//    }
//
//    @Override
//    public void validateTheFocusOfWeek(String currentDate) throws Exception {
//        String date = null;
//        if (isScheduleWeekViewActive()) {
//            SimpleUtils.pass("My Schedule Page: It is in week view now");
//            if (currentDate.contains(",") && currentDate.contains(" ")) {
//                date = currentDate.split(",")[1].trim().split(" ")[1];
//                SimpleUtils.report("Current date is " + date);
//            }
//            //activeWeekText is Mon - Sun Apr 13 - Apr 19
//            String activeWeekText = getActiveWeekText();
//            SimpleUtils.report("activeWeekText is: " + activeWeekText);
//            String weekDefaultEnd = "";
//            String weekDefaultBegin = "";
//            if (activeWeekText.contains(" ") && activeWeekText.contains("-")) {
//                try {
//                    weekDefaultBegin = activeWeekText.split("-")[1].split(" ")[3];
//                    SimpleUtils.report("weekDefaultBegin is: " + weekDefaultBegin);
//                    weekDefaultEnd = activeWeekText.split("-")[2].split(" ")[2];
//                    SimpleUtils.report("weekDefaultEnd is: " + weekDefaultEnd);
//                } catch (Exception e) {
//                    SimpleUtils.fail("My Schedule Page: Active week text doesn't have enough length", true);
//                }
//            }
//            if ((Integer.parseInt(weekDefaultBegin) <= Integer.parseInt(date) && Integer.parseInt(date) <= Integer.parseInt(weekDefaultEnd))
//                    || (Integer.parseInt(date) <= Integer.parseInt(weekDefaultEnd) && (weekDefaultBegin.length() == 2 && date.length() == 1))
//                    || (Integer.parseInt(date) >= Integer.parseInt(weekDefaultBegin) && (weekDefaultBegin.length() == 2 && date.length() == 2))) {
//                SimpleUtils.pass("My Schedule Page: By default focus is on current week successfully");
//            } else {
//                SimpleUtils.fail("My Schedule Page: Current week isn't selected by default", true);
//            }
//        } else
//            SimpleUtils.fail("My Schedule Page: It isn't in week view", true);
//    }

//    @Override
//    public void validateForwardAndBackwardButtonClickable() throws Exception {
//        String activeWeekText = getActiveWeekText();
//        if (isElementLoaded(calendarNavigationNextWeekArrow, 10)) {
//            try {
//                navigateWeekViewOrDayViewToPastOrFuture(ScheduleTestKendraScott2.weekViewType.Next.getValue(), ScheduleTestKendraScott2.weekCount.Three.getValue());
//                navigateWeekViewOrDayViewToPastOrFuture(ScheduleTestKendraScott2.weekViewType.Previous.getValue(), ScheduleTestKendraScott2.weekCount.Three.getValue());
//                if (activeWeekText.equals(getActiveWeekText()))
//                    SimpleUtils.pass("My Schedule Page: Forward and backward button to view previous or upcoming week is clickable successfully");
//            } catch (Exception e) {
//                SimpleUtils.fail("My Schedule Page: Exception occurs when clicking forward and backward button", true);
//            }
//        } else if (isElementLoaded(calendarNavigationPreviousWeekArrow, 10)) {
//            try {
//                navigateWeekViewOrDayViewToPastOrFuture(ScheduleTestKendraScott2.weekViewType.Previous.getValue(), ScheduleTestKendraScott2.weekCount.Three.getValue());
//                navigateWeekViewOrDayViewToPastOrFuture(ScheduleTestKendraScott2.weekViewType.Next.getValue(), ScheduleTestKendraScott2.weekCount.Three.getValue());
//                if (activeWeekText.equals(getActiveWeekText()))
//                    SimpleUtils.pass("My Schedule Page: Forward and backward button to view previous or upcoming week is clickable successfully");
//            } catch (Exception e) {
//                SimpleUtils.fail("My Schedule Page: Exception occurs when clicking forward and backward button", true);
//            }
//        } else
//            SimpleUtils.fail("My Schedule Page: Forward and backward button failed to load to view previous or upcoming week", true);
//    }

//    @Override
//    public void validateTheDataAccordingToTheSelectedWeek() throws Exception {
//        if (isElementLoaded(calendarNavigationPreviousWeekArrow, 10)) {
//            navigateWeekViewOrDayViewToPastOrFuture(ScheduleTestKendraScott2.weekViewType.Previous.getValue(), ScheduleTestKendraScott2.weekCount.Two.getValue());
//        } else if (isElementLoaded(calendarNavigationNextWeekArrow, 10)) {
//            navigateWeekViewOrDayViewToPastOrFuture(ScheduleTestKendraScott2.weekViewType.Next.getValue(), ScheduleTestKendraScott2.weekCount.Two.getValue());
//        } else
//            SimpleUtils.fail("My Schedule Page: Forward and backward button failed to load to view previous or upcoming week", true);
//        verifySelectOtherWeeks();
//        validateTheScheduleShiftsAccordingToTheSelectedWeek();
//    }

//    public void validateTheScheduleShiftsAccordingToTheSelectedWeek() throws Exception {
//        if (areListElementVisible(weekScheduleShiftsDateOfMySchedule, 20) && weekScheduleShiftsDateOfMySchedule.size() == 7 && isElementLoaded(currentActiveWeek, 5)) {
//            String activeWeek = currentActiveWeek.getText();
//            String weekScheduleShiftStartDate = weekScheduleShiftsDateOfMySchedule.get(0).getText();
//            String weekScheduleShiftEndDate = weekScheduleShiftsDateOfMySchedule.get(6).getText();
//            if (activeWeek.contains("\n") && weekScheduleShiftStartDate.contains(",") && weekScheduleShiftStartDate.contains(" ") && weekScheduleShiftEndDate.contains(",") && weekScheduleShiftEndDate.contains(" ") && activeWeek.contains("-")) {
//                try {
//                    if (weekScheduleShiftStartDate.split(",")[1].trim().split(" ")[1].startsWith("0")) {
//                        weekScheduleShiftStartDate = weekScheduleShiftStartDate.split(",")[1].trim().split(" ")[0] + " " + weekScheduleShiftStartDate.split(",")[1].split(" ")[2].substring(1, 2);
//                    } else {
//                        weekScheduleShiftStartDate = weekScheduleShiftStartDate.split(",")[1].trim();
//                    }
//                    if (weekScheduleShiftEndDate.split(",")[1].trim().split(" ")[1].startsWith("0")) {
//                        weekScheduleShiftEndDate = weekScheduleShiftEndDate.split(",")[1].trim().split(" ")[0] + " " + weekScheduleShiftEndDate.split(",")[1].split(" ")[2].substring(1, 2);
//                    } else {
//                        weekScheduleShiftEndDate = weekScheduleShiftEndDate.split(",")[1].trim();
//                    }
//                    activeWeek = activeWeek.split("\n")[1];
//                    SimpleUtils.report("weekScheduleShiftStartDate is " + weekScheduleShiftStartDate);
//                    SimpleUtils.report("weekScheduleShiftEndDate is " + weekScheduleShiftEndDate);
//                    SimpleUtils.report("activeWeek is " + activeWeek);
//                    if (weekScheduleShiftStartDate.equalsIgnoreCase(activeWeek.split("-")[0].trim()) && weekScheduleShiftEndDate.equalsIgnoreCase(activeWeek.split("-")[1].trim())) {
//                        SimpleUtils.pass("My Schedule Page: The schedule shifts show according to the selected week successfully");
//                    } else
//                        SimpleUtils.fail("My Schedule Page: The schedule shifts failed to show according to the selected week", true);
//                } catch (Exception e) {
//                    SimpleUtils.fail("My Schedule Page: The schedule shifts texts don't have enough length ", true);
//                }
//            }
//        } else if (isElementLoaded(myScheduleNoSchedule, 10)) {
//            SimpleUtils.report("My Schedule Page: Schedule has not been generated.");
//        } else {
//            SimpleUtils.fail("My Schedule Page: Failed to load shifts", true);
//        }
//    }
//
//    @Override
//    public void validateTheSevenDaysIsAvailableInScheduleTable() throws Exception {
//        if (areListElementVisible(weekScheduleShiftsDateOfMySchedule, 20) && weekScheduleShiftsDateOfMySchedule.size() == 7 && isElementLoaded(currentActiveWeek, 5)) {
//            String activeWeek = currentActiveWeek.getText();
//            String weekScheduleShiftStartDay = weekScheduleShiftsDateOfMySchedule.get(0).getText();
//            String weekScheduleShiftEndDay = weekScheduleShiftsDateOfMySchedule.get(6).getText();
//            if (activeWeek.contains("-") && activeWeek.contains("\n") && weekScheduleShiftStartDay.contains(",") && weekScheduleShiftEndDay.contains(",")) {
//                try {
//                    activeWeek = activeWeek.split("\n")[0];
//                    weekScheduleShiftStartDay = weekScheduleShiftStartDay.split(",")[0].substring(0, 3);
//                    weekScheduleShiftEndDay = weekScheduleShiftEndDay.split(",")[0].substring(0, 3);
//                    SimpleUtils.report("weekScheduleShiftStartDay is " + weekScheduleShiftStartDay);
//                    SimpleUtils.report("weekScheduleShiftEndDay is " + weekScheduleShiftEndDay);
//                    SimpleUtils.report("activeWeek is " + activeWeek);
//                    if (weekScheduleShiftStartDay.equalsIgnoreCase(activeWeek.split("-")[0].trim()) && weekScheduleShiftEndDay.equalsIgnoreCase(activeWeek.split("-")[1].trim())) {
//                        SimpleUtils.pass("My Schedule Page: Seven days - Sunday to Saturday show in the schedule table successfully");
//                        //todo according to the operation hours
//                    } else
//                        SimpleUtils.fail("My Schedule Page: Seven days - Sunday to Saturday failed to show in the schedule table", true);
//                } catch (Exception e) {
//                    SimpleUtils.fail("My Schedule Page: The schedule shifts texts don't have enough length ", true);
//                }
//            }
//        } else if (isElementLoaded(myScheduleNoSchedule, 10)) {
//            SimpleUtils.report("My Schedule Page: Schedule has not been generated.");
//        } else {
//            SimpleUtils.fail("My Schedule Page: Failed to load shifts", true);
//        }
//    }

//    @Override
//    public String getTheEarliestAndLatestTimeInSummaryView(HashMap<String, Integer> schedulePoliciesBufferHours) throws Exception {
//        String day = null;
//        String shiftStartTime = null;
//        String shiftEndTime = null;
//        double shiftStartTimeDouble = 12.0;
//        double shiftEndTimeDouble = 0.0;
//        HashMap<String, String> activeDayAndOperatingHrs = new HashMap<>();
//        if (areListElementVisible(operatingHoursRows, 5) &&
//                areListElementVisible(operatingHoursScheduleDay, 5) &&
//                areListElementVisible(scheduleOperatingHrsTimeDuration, 5)) {
//            for (int i = 0; i < operatingHoursRows.size(); i++) {
//                if (scheduleOperatingHrsTimeDuration.get(i).getText().contains("Closed"))
//                    continue;
//                day = operatingHoursScheduleDay.get(i).getText().substring(0, 3);
//                activeDayAndOperatingHrs = getOperatingHrsValue(day);
//                shiftStartTime = (activeDayAndOperatingHrs.get("ScheduleOperatingHrs").split("-"))[1];
//                if (shiftStartTime.endsWith("pm"))
//                    continue;
//                shiftEndTime = (activeDayAndOperatingHrs.get("ScheduleOperatingHrs").split("-"))[2];
//                if (shiftStartTime.contains(":"))
//                    shiftStartTime = shiftStartTime.replace(":", ".");
//                if (shiftEndTime.contains(":"))
//                    shiftEndTime = shiftEndTime.replace(":", ".");
//                if (shiftStartTime.contains("am") && shiftStartTime.startsWith("12"))
//                    shiftStartTime = shiftStartTime.replace("12", "0").replaceAll("[a-zA-Z]", "");
//                if (shiftStartTime.contains("am") && !shiftStartTime.startsWith("12"))
//                    shiftStartTime = shiftStartTime.replaceAll("[a-zA-Z]", "");
//                if (shiftStartTime.contains("pm") && shiftStartTime.startsWith("12"))
//                    shiftStartTime = shiftStartTime.replaceAll("[a-zA-Z]", "");
//                if (shiftStartTime.contains("pm") && !shiftStartTime.startsWith("12"))
//                    shiftStartTime = String.valueOf(Double.valueOf(shiftEndTime.replaceAll("[a-zA-Z]", "")) + 12);
//                if (shiftEndTime.contains("am"))
//                    shiftEndTime = shiftEndTime.replaceAll("[a-zA-Z]", "");
//                if (shiftEndTime.contains("pm") && !shiftEndTime.startsWith("12"))
//                    shiftEndTime = String.valueOf(Double.valueOf(shiftEndTime.replaceAll("[a-zA-Z]", "")) + 12);
//                if (shiftStartTimeDouble > Double.valueOf(shiftStartTime))
//                    shiftStartTimeDouble = Double.valueOf(shiftStartTime);
//                if (shiftEndTimeDouble < Double.valueOf(shiftEndTime))
//                    shiftEndTimeDouble = Double.valueOf(shiftEndTime);
//            }
//        } else {
//            SimpleUtils.fail("Operating hours table not loaded Successfully", true);
//        }
//        return Integer.valueOf(((int) shiftStartTimeDouble) - schedulePoliciesBufferHours.get("openingBufferHours")).toString() + "-" +
//                Integer.valueOf(((int) shiftEndTimeDouble) + schedulePoliciesBufferHours.get("closingBufferHours")).toString();
//    }

//    @Override
//    public String getTheEarliestAndLatestTimeInScheduleTable() throws Exception {
//        String operationStartTimeInScheduleTable = null;
//        String operationEndTimeInScheduleTable = null;
//        if (areListElementVisible(scheduleShiftTimeOnHeader, 30)) {
//            if (scheduleShiftTimeOnHeader.size() >= 2) {
//                if (scheduleShiftTimeOnHeader.get(0).getText().contains("AM")) {
//                    operationStartTimeInScheduleTable = scheduleShiftTimeOnHeader.get(0).getText().replaceAll("[^0-9]", "");
//                    if (operationStartTimeInScheduleTable.equals("12"))
//                        operationStartTimeInScheduleTable = "0";
//                } else if (scheduleShiftTimeOnHeader.get(0).getText().contains("PM")) {
//                    operationStartTimeInScheduleTable = scheduleShiftTimeOnHeader.get(0).getText().replaceAll("[^0-9]", "");
//                    if (operationStartTimeInScheduleTable.equals("24"))
//                        operationStartTimeInScheduleTable = "12";
//                } else
//                    operationStartTimeInScheduleTable = scheduleShiftTimeOnHeader.get(1).getText().replaceAll("[^0-9]", "");
//                if (scheduleShiftTimeOnHeader.get(scheduleShiftTimeOnHeader.size() - 1).getText().contains("AM")) {
//                    operationEndTimeInScheduleTable = scheduleShiftTimeOnHeader.get(scheduleShiftTimeOnHeader.size() - 1).getText().replaceAll("[^0-9]", "");
//                    if (operationEndTimeInScheduleTable.equals("12"))
//                        operationEndTimeInScheduleTable = "0";
//                } else if (scheduleShiftTimeOnHeader.get(scheduleShiftTimeOnHeader.size() - 1).getText().contains("PM")) {
//                    operationEndTimeInScheduleTable = String.valueOf(Integer.valueOf(scheduleShiftTimeOnHeader.get(scheduleShiftTimeOnHeader.size() - 1).getText().replaceAll("[^0-9]", "")) + 12);
//                    if (operationEndTimeInScheduleTable.equals("24"))
//                        operationEndTimeInScheduleTable = "12";
//                } else
//                    operationEndTimeInScheduleTable = String.valueOf(Integer.valueOf(scheduleShiftTimeOnHeader.get(scheduleShiftTimeOnHeader.size() - 2).getText().replaceAll("[^0-9]", "")) + 12);
//            } else SimpleUtils.fail("My Schedule Page: The operation hours shows wrong", true);
//        } else if (isElementLoaded(myScheduleNoSchedule, 20))
//            SimpleUtils.report("My Schedule Page: Schedule has not been generated.");
//        else
//            SimpleUtils.fail("My Schedule Page: The operation hours failed to load", true);
//        return operationStartTimeInScheduleTable + "-" + operationEndTimeInScheduleTable;
//    }

//    @Override
//    public void compareOperationHoursBetweenAdminAndTM(String theEarliestAndLatestTimeInScheduleSummary, String theEarliestAndLatestTimeInScheduleTable) throws Exception {
//        if (theEarliestAndLatestTimeInScheduleSummary.contains("-") && theEarliestAndLatestTimeInScheduleTable.contains("-")) {
//            if ((Integer.valueOf(theEarliestAndLatestTimeInScheduleSummary.split("-")[0]) <= Integer.valueOf(theEarliestAndLatestTimeInScheduleTable.split("-")[0])) && (Integer.valueOf(theEarliestAndLatestTimeInScheduleSummary.split("-")[1]) >= Integer.valueOf(theEarliestAndLatestTimeInScheduleTable.split("-")[1]))) {
//                SimpleUtils.pass("My Schedule Page: Seven days - Sunday to Saturday show in the schedule table according to the operating hours");
//            } else
//                SimpleUtils.fail("My Schedule Page: Seven days - Sunday to Saturday don't show in the schedule table according to the operating hours", false);
//        } else
//            SimpleUtils.fail("My Schedule Page: Operation hours display wrong, please check whether the shift is generated and published", false);
//    }

//    @Override
//    public void validateThatHoursAndDateIsVisibleOfShifts() throws Exception {
//        if (areListElementVisible(weekScheduleShiftsTimeOfMySchedule, 20) && areListElementVisible(weekScheduleShiftsDateOfMySchedule, 20) && weekScheduleShiftsDateOfMySchedule.size() == 7) {
//            for (int i = 0; i < weekScheduleShiftsDateOfMySchedule.size(); i++) {
//                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMM dd");
//                try {
//                    String date = weekScheduleShiftsDateOfMySchedule.get(i).getText();
//                    sdf.parse(date.trim());
//                    SimpleUtils.pass("My Schedule Page: Result Shifts show with shift date " + date.trim() + " successfully");
//                } catch (Exception e) {
//                    SimpleUtils.fail("My Schedule Page: Shifts don't show a legal DateTime type", false);
//                }
//            }
//        } else if (weekScheduleShiftsTimeOfMySchedule.size() == 0) {
//            SimpleUtils.report("My Schedule Page: No shift hours in the schedule table");
//            for (int i = 0; i < weekScheduleShiftsTimeOfMySchedule.size(); i++) {
//                if (weekScheduleShiftsTimeOfMySchedule.get(i).getText().contains("am") || weekScheduleShiftsTimeOfMySchedule.get(i).getText().contains("pm"))
//                    SimpleUtils.pass("My Schedule Page: Result Shifts show with shift hours " + weekScheduleShiftsTimeOfMySchedule.get(i).getText() + " successfully");
//                else
//                    SimpleUtils.fail("My Schedule Page: Result Shifts failed to show with shift hours " + weekScheduleShiftsTimeOfMySchedule.get(i).getText(), true);
//            }
//        } else if (isElementLoaded(myScheduleNoSchedule, 10)) {
//            SimpleUtils.report("My Schedule Page: Schedule has not been generated.");
//        } else {
//            SimpleUtils.fail("My Schedule Page: Failed to load shifts", true);
//        }
//    }

//    @Override
//    public void validateProfilePictureInAShiftClickable() throws Exception {
//        Boolean isShiftDetailsShowed = false;
//        if (areListElementVisible(weekScheduleShiftsDateOfMySchedule, 20)) {
//            if (scheduleTableWeekViewWorkerDetail.size() != 0) {
//                int randomIndex = (new Random()).nextInt(scheduleTableWeekViewWorkerDetail.size());
//                moveToElementAndClick(scheduleTableWeekViewWorkerDetail.get(randomIndex));
//                SimpleUtils.pass("My Schedule Page: Profile picture is clickable successfully");
//                if (isPopOverLayoutLoaded()) {
//                    if (!popOverLayout.getText().isEmpty() && popOverLayout.getText() != null) {
//                        isShiftDetailsShowed = true;
//                    }
//                    moveToElementAndClick(scheduleTableWeekViewWorkerDetail.get(randomIndex));
//                }
//                if (isShiftDetailsShowed == true)
//                    SimpleUtils.pass("My Schedule Page: Profile picture shows details of TM successfully");
//                else
//                    SimpleUtils.fail("My Schedule Page: Profile picture failed to details of TM", true);
//            } else if (scheduleTableWeekViewWorkerDetail.size() == 0)
//                SimpleUtils.report("My Schedule Page: No shift hours in the schedule table");
//        } else if (isElementLoaded(myScheduleNoSchedule, 10))
//            SimpleUtils.report("My Schedule Page: Schedule has not been generated.");
//        else SimpleUtils.fail("My Schedule Page: Failed to load shifts", true);
//    }

//    @Override
//    public void validateTheDataOfProfilePopupInAShift() throws Exception {
//        Boolean isShiftDetailsShowed = false;
//        if (areListElementVisible(weekScheduleShiftsDateOfMySchedule, 20)) {
//            if (scheduleTableWeekViewWorkerDetail.size() != 0) {
//                int randomIndex = (new Random()).nextInt(scheduleTableWeekViewWorkerDetail.size());
//                moveToElementAndClick(scheduleTableWeekViewWorkerDetail.get(randomIndex));
//                if (isPopOverLayoutLoaded()) {
///*                    System.out.println("pop is " + popOverLayout.getAttribute("innerHTML"));
//                    if (popOverLayout.getAttribute("class").contains("sch-worker-h-view-name")
//                            && popOverLayout.getAttribute("class").contains("sch-worker-role")
//                            && popOverLayout.getAttribute("class").contains("sch-worker-h-sub-row")
//                            && popOverLayout.getAttribute("class").contains("sch-worker-h-badges-row")) {
//                        if (popOverLayout.findElement(By.className("sch-worker-h-view-name")).getText() != null
//                                && popOverLayout.findElement(By.className("sch-worker-role")).getText() != null
//                                && popOverLayout.findElement(By.className("sch-worker-h-sub-row")).getText() != null
//                                && popOverLayout.findElement(By.className("sch-worker-h-badges-row")).getText() != null) {
//                            //todo: LEG-10929
//                            isShiftDetailsShowed = true;
//                        }
//                    }*/
//                    moveToElementAndClick(scheduleTableWeekViewWorkerDetail.get(randomIndex));
//                }
//                if (isShiftDetailsShowed == true)
//                    SimpleUtils.pass("My Schedule Page: Profile popup contains location, badges, job title and name successfully");
//                else
//                    SimpleUtils.fail("My Schedule Page: Profile popup doesn't contain location, badges, job title and name", true);
//            } else if (scheduleTableWeekViewWorkerDetail.size() == 0)
//                SimpleUtils.report("My Schedule Page: No shift hours in the schedule table");
//        } else if (isElementLoaded(myScheduleNoSchedule, 10))
//            SimpleUtils.report("My Schedule Page: Schedule has not been generated.");
//        else SimpleUtils.fail("My Schedule Page: Failed to load shifts", true);
//    }
//
//    @Override
//    public void validateTheAvailabilityOfInfoIcon() throws Exception {
//        if (areListElementVisible(weekScheduleShiftsDateOfMySchedule, 20)) {
//            if (hoverIcons.size() != 0) {
//                if (getDriver().findElements(By.xpath("//*[@class=\"right-shift-box\"]/div/div[1]")).size() == hoverIcons.size())
//                    SimpleUtils.pass("My Schedule Page: Info icon is present at the right side of a shift successfully");
//                else
//                    SimpleUtils.fail("My Schedule Page: Info icon isn't present at the right side of a shift", true);
//            } else if (hoverIcons.size() == 0)
//                SimpleUtils.report("My Schedule Page: No shift hours in the schedule table");
//        } else if (isElementLoaded(myScheduleNoSchedule, 10))
//            SimpleUtils.report("My Schedule Page: Schedule has not been generated.");
//        else SimpleUtils.fail("My Schedule Page: Failed to load shifts", true);
//    }
//
//    @Override
//    public void validateInfoIconClickable() throws Exception {
//        if (areListElementVisible(weekScheduleShiftsDateOfMySchedule, 20)) {
//            if (hoverIcons.size() != 0) {
//                int randomIndex = (new Random()).nextInt(hoverIcons.size());
//                clickTheElement(hoverIcons.get(randomIndex));
//                if (isElementLoaded(popOverContent, 5)) {
//                    SimpleUtils.pass("My Schedule Page: Info icon is clickable successfully");
//                    List<WebElement> hoverSubContainers = popOverContent.findElements(By.className("hover-sub-container"));
//                    if (hoverSubContainers.size() == 3) {
//                        String shiftRole = hoverSubContainers.get(0).findElement(By.cssSelector(".shift-hover-subheading")).getText();
//                        String timing = hoverSubContainers.get(1).getText();
//                        String duration = hoverSubContainers.get(2).getText();
//                        if (shiftRole != null && (timing.contains("am") || timing.contains("pm")) && duration.contains("Hrs this week")) {
//                            SimpleUtils.pass("My Schedule Page: Info icon has the shift details like duration, timing and shift role");
//                        } else
//                            SimpleUtils.fail("My Schedule Page: Info icon has the shift details like duration, timing and shift role", true);
//                    }
//                    scrollToBottom();
//                    click(hoverIcons.get(randomIndex));
//                }
//            } else if (hoverIcons.size() == 0)
//                SimpleUtils.report("My Schedule Page: No shift hours in the schedule table");
//        } else if (isElementLoaded(myScheduleNoSchedule, 10))
//            SimpleUtils.report("My Schedule Page: Schedule has not been generated");
//        else SimpleUtils.fail("My Schedule Page: Failed to load shifts", true);
//    }

//    @Override
//    public void validateTheAvailabilityOfOpenShiftSmartcard() throws Exception {
//        if (areListElementVisible(carouselCards, 10)) {
//            if (isSmartCardAvailableByLabel("WANT MORE HOURS"))
//                SimpleUtils.pass("My Schedule Page: Open Shift Smartcard is present on Schedule Page successfully");
//            else SimpleUtils.fail("My Schedule Page: Open Shift Smartcard isn't present on Schedule Page", true);
//        } else SimpleUtils.fail("My Schedule Page: Smartcards failed to load", true);
//    }
//
//    public boolean isViewShiftsBtnPresent() throws Exception {
//        boolean isConsistent = false;
//        if (areListElementVisible(carouselCards, 10)) {
//            if (isSmartCardAvailableByLabel("WANT MORE HOURS")) {
//                if (isElementLoaded(viewShiftsBtn, 5)) {
//                    isConsistent = true;
//                    SimpleUtils.pass("My Schedule Page: 'View Shifts' button is present on Open Shift Smartcard successfully");
//                } else
//                    SimpleUtils.report("My Schedule Page: 'View Shifts' button isn't present on Open Shift Smartcard since there is 0 shift offer");
//            } else SimpleUtils.fail("My Schedule Page: Open Shift Smartcard isn't present on Schedule Page", true);
//        } else SimpleUtils.fail("My Schedule Page: Smartcards failed to load", true);
//        return isConsistent;
//    }

//    @Override
//    public void validateViewShiftsClickable() throws Exception {
//        if (isViewShiftsBtnPresent()) {
//            click(viewShiftsBtn);
//            SimpleUtils.pass("My Schedule Page: View shift is clickable and a filter for Open shift is applied after that successfully");
//            click(filterButton);
//            if (shiftTypes.size() > 0) {
//                for (WebElement shiftType : shiftTypes) {
//                    WebElement filterCheckBox = shiftType.findElement(By.tagName("input"));
//                    if (filterCheckBox.getAttribute("class").contains("ng-not-empty")) {
//                        if (shiftType.getText().equals("Open"))
//                            SimpleUtils.pass("My Schedule Page: only open shifts for the selected week should show successfully");
//                        else
//                            SimpleUtils.fail("My Schedule Page: Not only open shifts for the selected week show", true);
//                    }
//                }
//            }
//        }
//    }

//    @Override
//    public void validateTheNumberOfOpenShifts() throws Exception {
//        if (isViewShiftsBtnPresent()) {
//            if (areListElementVisible(dayViewAvailableShifts, 10)) {
//                if (dayViewAvailableShifts.size() == Integer.valueOf(openShiftData.getText().replaceAll("[^0-9]", "")))
//                    SimpleUtils.pass("My Schedule Page: The total number of open shifts in smartcard matches with the open shifts available in the schedule table successfully");
//                else
//                    SimpleUtils.fail("My Schedule Page: The total number of open shifts in smartcard doesn't match with the open shifts available in the schedule table", true);
//            } else SimpleUtils.fail("My Schedule Page: Open shifts failed to load in the schedule table", true);
//        }
//    }
//
//    @Override
//    public void verifyTheAvailabilityOfClaimOpenShiftPopup() throws Exception {
//        List<String> claimShift = new ArrayList<>(Arrays.asList("Claim Shift"));
//        if (isViewShiftsBtnPresent()) {
//            if (areListElementVisible(dayViewAvailableShifts, 10)) {
//                int randomIndex = (new Random()).nextInt(dayViewAvailableShifts.size());
//                moveToElementAndClick(dayViewAvailableShifts.get(randomIndex));
//                if (isPopOverLayoutLoaded()) {
//                    if (verifyShiftRequestButtonOnPopup(claimShift))
//                        SimpleUtils.pass("My Schedule Page: A popup to claim the open shift shows successfully");
//                    else SimpleUtils.fail("My Schedule Page: A popup to claim the open shift doesn't show", true);
//                } else SimpleUtils.fail("My Schedule Page: No popup appears", true);
//            } else SimpleUtils.fail("My Schedule Page: Open shifts failed to load in the schedule table", true);
//        }
//    }
//
//    public void closeButtonIsClickable() {
//        getDriver().close();
//        SimpleUtils.pass("close button is clickable");
//    }
//
//    public boolean isRequestUserNameOnPopup(String requestUserName) throws Exception {
//        boolean isRequestUserNameOnPopup = false;
//        if (areListElementVisible(shiftRequests, 5)) {
//            for (WebElement shiftRequest : shiftRequests) {
//                if (shiftRequest.getText().contains(requestUserName)){
//                    isRequestUserNameOnPopup = true;
//                    break;
//                }
//            }
//        }
//        return isRequestUserNameOnPopup;
//    }

//    @Override
//    public void clickTheShiftRequestToClaimShift(String requestName, String requestUserName) throws Exception {
//        int index = 0;
//        if (areListElementVisible(tmIcons, 15)) {
//            for (int i = 0; i < tmIcons.size(); i++) {
//                moveToElementAndClick(tmIcons.get(i));
//                if (isPopOverLayoutLoaded()) {
//                    if (popOverLayout.getText().contains(requestName) && popOverLayout.getText().contains(requestUserName)) {
//                        index = 1;
//                        clickTheElement(popOverLayout.findElement(By.cssSelector("span.sch-worker-action-label")));
//                        SimpleUtils.pass("Click " + requestName + " button Successfully!");
//                        break;
//                    }
//                }
//            }
//            if (index == 0) {
//                SimpleUtils.fail("Failed to select one shift to claim", false);
//            }
//        } else {
//            SimpleUtils.fail("Team Members' Icons not loaded", false);
//        }
//    }

//    @Override
//    public void addOpenShiftWithLastDay(String workRole) throws Exception {
//        if (isElementLoaded(createNewShiftWeekView,5)) {
//            click(createNewShiftWeekView);
//            selectWorkRole(workRole);
//            clickRadioBtnStaffingOption(staffingOption.OpenShift.getValue());
//            clearAllSelectedDays();
//            if (areListElementVisible(weekDays, 5) && weekDays.size() == 7) {
//                if (!weekDays.get(6).getAttribute("class").contains("week-day-multi-picker-day-selected")) {
//                    click(weekDays.get(6));
//                }
//            }
//            clickOnCreateOrNextBtn();
//        } else
//            SimpleUtils.fail("Day View Schedule edit mode, add new shift button not found for Week Day: '" +
//                    getActiveWeekText() + "'", false);
//    }

//    @Override
//    public void deleteLatestOpenShift() throws Exception {
//        boolean isDeleted = false;
//        if (isElementLoaded(createNewShiftWeekView, 5) && areListElementVisible(blueIconsOfOpenShift,5)) {
//            for (int i = blueIconsOfOpenShift.size() - 1; i >= 0; i--) {
//                moveToElementAndClick(blueIconsOfOpenShift.get(i));
//                if (isPopOverLayoutLoaded()) {
//                    clickTheElement(deleteShift);
//                    if (isDeleteShiftShowWell()) {
//                        click(deleteBtnInDeleteWindows);
//                        if (isElementLoaded(deleteShiftImg, 5)) {
//                            isDeleted = true;
//                            SimpleUtils.pass("Schedule Week View: Existing shift: \" + Open Shift + \" deleted successfully");
//                            break;
//                        }
//                    }
//                }
//            }
//        } else
//            SimpleUtils.fail("Delete shift button not found for Week Day: '" +
//                    getActiveWeekText() + "'", true);
//        if (!isDeleted)
//            SimpleUtils.fail("Failed to delete the open shift on Last Day!", false);
//    }
//
//    @Override
//    public void addManualShiftWithLastDay(String workRole, String tmName) throws Exception {
//        if (isElementLoaded(createNewShiftWeekView,5)) {
//            click(createNewShiftWeekView);
//            selectWorkRole(workRole);
//            if (weekDays.get(0).getAttribute("class").contains("week-day-multi-picker-day-selected"))
//                click(weekDays.get(0));
//            clickRadioBtnStaffingOption(staffingOption.ManualShift.getValue());
//            if (weekDays.size() == 7) {
//                for (int i = weekDays.size() - 1; i >= 0; i--) {
//                    if (weekDays.get(i).getAttribute("class").contains("week-day-multi-picker-day-disabled"))
//                        continue;
//                    else {
//                        click(weekDays.get(i));
//                        break;
//                    }
//                }
//            }
//            clickOnCreateOrNextBtn();
//            searchTeamMemberByName(tmName);
//            if(isElementLoaded(btnAssignAnyway,5))
//                click(btnAssignAnyway);
//            clickOnOfferOrAssignBtn();
//        } else
//            SimpleUtils.fail("Failed to load 'Create New Shift' label, maybe it is not in edit mode", false);
//    }
//
//    @Override
//    public String getWeekInfoBeforeCreateSchedule() throws Exception {
//        String weekInfo = "";
//        if (isElementLoaded(weekInfoBeforeCreateSchedule,10)){
//            weekInfo = weekInfoBeforeCreateSchedule.getText().trim();
//            if (weekInfo.contains("Week")) {
//                weekInfo = weekInfo.substring(weekInfo.indexOf("Week"));
//            }
//        }
//        return weekInfo;
//    }
//
//    @Override
//    public void verifyTheContentOnEnterBudgetWindow(String weekInfo, String location) throws Exception {
//        if (isElementLoaded(headerWhileCreateSchedule,5) && headerWhileCreateSchedule.getText().contains(weekInfo)){
//            SimpleUtils.pass("Create Schedule - Enter Budget: \"" + weekInfo +"\" as header displays correctly");
//        } else
//            SimpleUtils.fail("Enter Budget: Week information as header not loaded or displays incorrectly",true);
//        if (isElementLoaded(locationWhileCreateSchedule,5) && locationWhileCreateSchedule.getText().contains(location)){
//            SimpleUtils.pass("Create Schedule - Enter Budget: \"" + location +"\" as location displays correctly");
//        } else
//            SimpleUtils.fail("Enter Budget: Location not loaded or displays correctly",true);
//        if (isElementLoaded(generateModalTitle,5) && generateModalTitle.getText().contains("Enter Budget")){
//            SimpleUtils.pass("Create Schedule - Enter Budget: Enter Budget as subhead displays correctly");
//        } else
//            SimpleUtils.fail("Enter Budget: Enter Budget as subhead not loaded or displays incorrectly",true);
//        if (isElementLoaded(editBudgetBtn,5) && editBudgetBtn.getText().contains("Edit")){
//            SimpleUtils.pass("Create Schedule - Enter Budget: Edit button displays correctly");
//        } else
//            SimpleUtils.fail("Enter Budget: Edit button not loaded or displays incorrectly",true);
//        if (isElementLoaded(editBudgetBtn,5) && editBudgetBtn.getText().contains("Edit")){
//            SimpleUtils.pass("Create Schedule - Enter Budget: Edit button displays correctly");
//        } else
//            SimpleUtils.fail("Enter Budget: Edit button not loaded or displays incorrectly",true);
//        if (isElementLoaded(backButton,5) && backButton.getText().contains("Back")){
//            SimpleUtils.pass("Create Schedule - Enter Budget: Back button displays correctly");
//        } else
//            SimpleUtils.fail("Create Schedule - Enter Budget:  Back button not loaded or displays incorrectly",true);
//        if (isElementLoaded(nextButtonOnCreateSchedule,5) && nextButtonOnCreateSchedule.getText().contains("Next")){
//            SimpleUtils.pass("Create Schedule - Enter Budget: Next button displays correctly");
//        } else
//            SimpleUtils.fail("Create Schedule - Enter Budget: Next button not loaded or displays incorrectly",true);
//    }
//
//    @Override
//    public List<String> setAndGetBudgetForNonDGFlow() throws Exception {
//        List<String> budgetForNonDGFlow = new ArrayList<>();
//        Float sumOfBudgetHours = 0.00f;
//        if (isElementLoaded(editBudgetBtn, 5)) {
//            clickTheElement(editBudgetBtn);
//            if (isElementLoaded(operatingHoursCancelBtn, 10) && isElementLoaded(operatingHoursSaveBtn, 10)) {
//                SimpleUtils.pass("Create Schedule - Enter Budget: Click on Edit button Successfully!");
//                if (areListElementVisible(roleHoursRows, 5)) {
//                    for (WebElement roleHoursRow : roleHoursRows) {
//                        try {
//                            WebElement forecastHour = roleHoursRow.findElement(By.cssSelector("td:nth-child(3)"));
//                            WebElement budgetHour = roleHoursRow.findElement(By.cssSelector("input[type=\"number\"]"));
//                            if (forecastHour != null && budgetHour != null) {
//                                String forecastHourString = "";
//                                forecastHourString = forecastHour.getText().trim().replaceAll("[a-zA-Z]", "");
//                                float forecastHourFloat= Float.valueOf(forecastHourString);
//                                float random = (float) (Math.random() * forecastHourFloat);
//                                budgetHour.clear();
//                                DecimalFormat decimalFormat =new DecimalFormat("#.00");
//                                String value = decimalFormat.format(random);
//                                System.out.println(forecastHourString);
//                                System.out.println(forecastHourFloat);
//                                System.out.println(random);
//                                System.out.println(value);
//                                budgetHour.sendKeys(value);
//                                sumOfBudgetHours += Float.valueOf(value);
//                                budgetForNonDGFlow.add(value);
//                            }
//                        } catch (Exception e) {
//                            continue;
//                        }
//                    }
//                    clickTheElement(operatingHoursSaveBtn);
//                    if (isElementEnabled(editBudgetBtn, 5)) {
//                        SimpleUtils.pass("Create Schedule: Save the budget hours Successfully!");
//                    } else
//                        SimpleUtils.fail("Create Schedule: Click on Save the budget hours button failed, Next button is not enabled!", false);
//                    String totalBudget = MyThreadLocal.getDriver().findElement(By.xpath("//th[contains(text(), \"Total\")]/following-sibling::th[2]")).getText().trim();
//                    System.out.println("sumOfBudgetHours is " + sumOfBudgetHours);
//                    if (sumOfBudgetHours == Float.valueOf(totalBudget)) {
//                        budgetForNonDGFlow.add(sumOfBudgetHours.toString());
//                        SimpleUtils.pass("Create Schedule - Enter Budget: The total budget value is consistent with the summary of the edited value");
//                    } else
//                        SimpleUtils.fail("Create Schedule - Enter Budget: The total budget value is inconsistent with the summary of the edited value, please check",true);
//                }
//            }
//        } else
//            SimpleUtils.fail("Create Schedule - Enter Budget: Edit button not loaded Successfully!", false);
//        return budgetForNonDGFlow;
//    }

//    @Override
//    public HashMap<String, String> verifyNGetBudgetNScheduleWhileCreateScheduleForNonDGFlowNewUI(String weekInfo, String location) throws Exception {
//        String subTitle = "Confirm Operating Hours";
//        String totalBudget = "";
//        String targetBudgetHrs = "";
//        List<String> budgetForNonDGFlow = new ArrayList<>();
//        HashMap<String, String> budgetNSchedule = new HashMap<>();
//        if (isElementLoaded(generateSheduleButton, 10)) {
//            moveToElementAndClick(generateSheduleButton);
//            openBudgetPopUp();
//            if (isElementLoaded(generateModalTitle, 5) && subTitle.equalsIgnoreCase(generateModalTitle.getText().trim())
//                    && isElementLoaded(nextButtonOnCreateSchedule, 5)) {
//                editTheOperatingHours(new ArrayList<>());
//                waitForSeconds(3);
//                clickTheElement(nextButtonOnCreateSchedule);
//                verifyTheContentOnEnterBudgetWindow(weekInfo, location);
//                budgetForNonDGFlow = setAndGetBudgetForNonDGFlow();
//                clickTheElement(nextButtonOnCreateSchedule);
//                selectWhichWeekToCopyFrom("SUGGESTED");
//                targetBudgetHrs = targetBudget.getText().trim();
//                if (targetBudgetHrs.contains(" ")) {
//                    targetBudgetHrs = targetBudgetHrs.split(" ")[0];
//                }
//                if (budgetForNonDGFlow.size() > 1)
//                    totalBudget = budgetForNonDGFlow.get(budgetForNonDGFlow.size() - 1);
//                if (targetBudgetHrs.equals(totalBudget)) {
//                    budgetNSchedule.put("Budget", targetBudgetHrs);
//                    SimpleUtils.pass("Total budget in enter budget window and target budget in copy schedule window are consistent");
//                } else
//                    SimpleUtils.fail("Total budget in enter budget window and target budget in copy schedule window are inconsistent", false);
//                for (WebElement budgetHrs : budgetHrsOnGraph) {
//                    if (budgetHrs.getText().equals(targetBudgetHrs))
//                        SimpleUtils.pass("The budget in graph is consistent with the target budget in copy schedule window");
//                    else
//                        SimpleUtils.fail("The budget in graph is inconsistent with the target budget in copy schedule window", false);
//                }
//                if (isElementLoaded(scheduledHrsOnGraph,10))
//                    budgetNSchedule.put("Scheduled", scheduledHrsOnGraph.getText());
//                clickOnFinishButtonOnCreateSchedulePage();
//            } else if (isElementLoaded(generateSheduleForEnterBudgetBtn, 5)) {
//                click(generateSheduleForEnterBudgetBtn);
//                if (isElementEnabled(checkOutTheScheduleButton, 20)) {
//                    checkoutSchedule();
//                    switchToManagerViewToCheckForSecondGenerate();
//                } else if (isElementLoaded(updateAndGenerateScheduleButton, 5)) {
//                    updateAndGenerateSchedule();
//                    switchToManagerViewToCheckForSecondGenerate();
//                } else
//                    SimpleUtils.fail("Not able to generate Schedule Successfully!", false);
//            } else if (isElementLoaded(updateAndGenerateScheduleButton, 5)) {
//                updateAndGenerateSchedule();
//                switchToManagerViewToCheckForSecondGenerate();
//            } else if (isElementEnabled(checkOutTheScheduleButton, 20)) {
//                checkOutGenerateScheduleBtn(checkOutTheScheduleButton);
//                SimpleUtils.pass("Schedule Generated Successfully!");
//                switchToManagerViewToCheckForSecondGenerate();
//            } else
//                SimpleUtils.fail("Not able to generate schedule Successfully!", false);
//        } else
//            SimpleUtils.fail("Create Schedule button not loaded Successfully!", false);
//        return budgetNSchedule;
//    }

//    @Override
//    public List<String> getBudgetedHoursOnSTAFF() throws Exception {
//        List<String> budgetedHours = new ArrayList<>();
//        if (areListElementVisible(budgetedHoursOnSTAFF,10)) {
//            for (WebElement e : budgetedHoursOnSTAFF) {
//                budgetedHours.add(e.getText().trim());
//            }
//        } else
//            SimpleUtils.fail("Budgeted Hours On STAFF failed to load",true);
//        return budgetedHours;
//    }
//
//    @Override
//    public String getBudgetOnWeeklyBudget() throws Exception {
//        String budgetOnWeeklyBudget = "";
//        if (budgetHoursOnWeeklyBudget.getText().contains(" ")) {
//            budgetOnWeeklyBudget = budgetHoursOnWeeklyBudget.getText().split(" ")[0];
//        }
//        return budgetOnWeeklyBudget;
//    }
//
//    @Override
//    public String getChangesOnActionRequired() throws Exception {
//        String changes = "";
//        if (isElementLoaded(changesOnActionRequired, 10)) {
//            changes = changesOnActionRequired.getText().replaceAll("\"","").trim();
//        }
//        return changes;
//    }
//
//    @Override
//    public String getTooltipOfUnpublishedDeleted() throws Exception {
//        String tooltipOfUnpublishedDeleted = "";
//        if (isElementLoaded(tooltipIconOfUnpublishedDeleted,10)) {
//            mouseHover(tooltipIconOfUnpublishedDeleted);
//            tooltipOfUnpublishedDeleted = changesOnActionRequired.getAttribute("data-tootik");
//        }
//        return tooltipOfUnpublishedDeleted;
//    }

//    @Override
//    public void selectAShiftToAssignTM(String username) throws Exception {
//        boolean isFound = false;
//        if (areListElementVisible(scheduleCalendarDays,10)) {
//            for (WebElement day: scheduleCalendarDays) {
//                if (!day.getAttribute("class").contains("sch-calendar-day-past")) {
//                    WebElement dataDay = day.findElement(By.xpath("./../.."));
//                    String data = dataDay.getAttribute("data-day");
//                    List<WebElement> shifts = MyThreadLocal.getDriver().findElements(By.cssSelector("div[data-day=\"" + data + "\"].week-schedule-shift"));
//                    if (shifts.size() > 0) {
//                        int randomIndex = (new Random()).nextInt(shifts.size());
//                        WebElement shiftImg = shifts.get(randomIndex).findElement(By.cssSelector(".rows span img"));
//                        moveToElementAndClick(shiftImg);
//                        if (isPopOverLayoutLoaded()) {
//                            clickTheElement(popOverLayout.findElement(By.xpath("//span[contains(text(), \"Assign Team Member\")]")));
//                            if (isAssignTeamMemberShowWell()) {
//                                searchText(username);
//                                isFound = true;
//                                SimpleUtils.pass("Assign Team Member: Select a shift and search the team member successfully");
//                                break;
//                            }
//                        }
//                    }
//                }
//            }
//        } else
//            SimpleUtils.fail("Schedule Page: Failed to find the schedule days",false);
//        if (!isFound)
//            SimpleUtils.fail("Assign Team Member: Failed to select a shift and search the team member",false);
//    }

//    @Override
//    public void verifyInactiveMessageNWarning(String username, String date) throws Exception {
//        if (messageInSelectTeamMemberWindow.getText().contains("TM is inactive from " + date)) {
//            SimpleUtils.pass("Assign Team Member: 'Inactive' message shows successfully");
//        } else
//            SimpleUtils.fail("Assign Team Member: 'Inactive' message failed to show",false);
//        if (isElementLoaded(optionCircle, 5)) {
//            click(optionCircle);
//            if (isElementLoaded(alertMessage,5)) {
//                if (alertMessage.getText().trim().equals(username + " is inactive starting " + date + ". Please activate the team member before assigning.")) {
//                    SimpleUtils.pass("Assign Team Member: Warning shows correctly");
//                    click(okBtnOnConfirm);
//                    if (optionCircle.findElement(By.className("tma-staffing-option-inner-circle")).getAttribute("class").contains("ng-hide")) {
//                        SimpleUtils.pass("Assign Team Member: Click OK in warning window and nothing changes as expected");
//                        if (isElementLoaded(closeButtonOnCustomize, 5)) {
//                            click(closeButtonOnCustomize);
//                            if (isElementLoaded(scheduleEditModeCancelButton, 10)) {
//                                click(scheduleEditModeCancelButton);
//                            }
//                        }
//                    } else {
//                        SimpleUtils.fail("Assign Team Member: Click OK in warning window, the inactive TM is selected unexpectedly", false);
//                    }
//                } else {
//                    SimpleUtils.fail("Assign Team Member: Warning shows incorrectly", false);
//                }
//            } else {
//                SimpleUtils.fail("Assign Team Member: No warning when assign an inactive TM", false);
//            }
//        }
//    }
//
//    private boolean isAssignTeamMemberShowWell() throws Exception {
//        if (isElementLoaded(titleInSelectTeamMemberWindow,3) && areListElementVisible(btnSearchteamMember,3)
//                && isElementLoaded(textSearch, 5) && isElementLoaded(searchIcon, 5)) {
//            SimpleUtils.pass("Assign Team Member pop up window show well");
//            return true;
//        } else
//            SimpleUtils.fail("Assign Team Member pop up window load failed",false);
//        return false;
//    }
//
//    @Override
//    public List<String> getOpenShiftInfoByIndex(int index) throws Exception {
//        List<String> openShiftInfo = new ArrayList<>();
//        if (areListElementVisible(weekShifts, 20) && index < weekShifts.size()) {
//            String shiftTimeWeekView = weekShifts.get(index).findElement(By.className("week-schedule-shift-time")).getText();
//            WebElement infoIcon = weekShifts.get(index).findElement(By.className("week-schedule-shit-open-popover"));
//            clickTheElement(infoIcon);
//            String workRole = shiftJobTitleAsWorkRole.getText().trim();
//            if (isElementLoaded(shiftDuration, 10)) {
//                String shiftTime = shiftDuration.getText();
//                openShiftInfo.add(shiftTime);
//                openShiftInfo.add(workRole);
//                openShiftInfo.add(shiftTimeWeekView);
//            }
//            //To close the info popup
//            moveToElementAndClick(weekShifts.get(index));
//        } else {
//            SimpleUtils.fail("Schedule Page: week shifts not loaded successfully!", false);
//        }
//        if (openShiftInfo.size() != 3) {
//            SimpleUtils.fail("Failed to get open shift info!", false);
//        }
//        return openShiftInfo;
//    }
//
//    @Override
//    public void verifyAlertMessageIsExpected(String messageExpected) throws Exception {
//        if (isElementLoaded(alertMessage,5)){
//            if (alertMessage.getText() != null && !alertMessage.getText().equals("") && alertMessage.getText().contains(messageExpected)){
//                SimpleUtils.pass("There is the message you want to see: " + messageExpected);
//            } else {
//                SimpleUtils.fail("No message you expected! Actual message is " + alertMessage.getText(), false );
//            }
//        } else {
//            SimpleUtils.fail("The alert message for selecting TM failed to loaded", false);
//        }
//    }
//
//    @FindBy(css = ".card-carousel-arrow-right.available")
//    private WebElement arrowRightAvailable;
//
//    @Override
//    public void navigateToTheRightestSmartCard() throws Exception {
//        while (isElementLoaded(arrowRightAvailable, 5)) {
//            click(arrowRightAvailable);
//        }
//    }

    @FindBy(css = "[attr-id=\"location\"] button.lgn-dropdown-button")
    private WebElement btnLocation;

    @FindBy(css = "[attr-id=\"location\"] [ng-click=\"selectChoice($event, choice)\"]")
    private List<WebElement> listLocations;
//
//    @Override
//    public boolean isLocationLoaded() throws Exception {
//        if (isElementLoaded(btnLocation, 20))
//            return true;
//        else
//            return false;
//    }
//
//    @Override
//    public void selectLocation(String location) throws Exception {
//        if (isElementLoaded(btnLocation, 20)) {
//            click(btnLocation);
//            SimpleUtils.pass("Location button clicked Successfully");
//        } else {
//            SimpleUtils.fail("Work Role button is not clickable", false);
//        }
//        if (listLocations.size() > 1) {
//            for (WebElement listWorkRole : listLocations) {
//                if (listWorkRole.getText().toLowerCase().contains(location.toLowerCase())) {
//                    click(listWorkRole);
//                    SimpleUtils.pass("Location " + location + "selected Successfully");
//                    break;
//                } else {
//                    SimpleUtils.report("Location " + location + " not selected");
//                }
//            }
//
//        } else {
//            SimpleUtils.fail("Location size are empty", false);
//        }
//    }

//    @Override
//    public void selectLocationFilterByText(String filterText) throws Exception {
//        String locationFilterKey = "location";
//        ArrayList<WebElement> locationFilters = getAvailableFilters().get(locationFilterKey);
//        unCheckFilters(locationFilters);
//        for (WebElement locationOption : locationFilters) {
//            if (locationOption.getText().toLowerCase().contains(filterText.toLowerCase())) {
//                click(locationOption);
//                break;
//            }
//        }
//        if (!filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
//            click(filterButton);
//    }

//    @FindBy (className = "lg-filter__category-label")
//    private List<WebElement> filterLabels;
//
//    @Override
//    public void verifyFilterDropdownList(boolean isLG) throws Exception {
//        if (isElementLoaded(filterPopup,5)) {
//            HashMap<String, ArrayList<WebElement>> availableFilters = getAvailableFilters();
//            if (!availableFilters.get("location").isEmpty()) {
//                if (isLG)
//                    SimpleUtils.pass("Schedule Page: 'LOCATION' is one label when current env is LG");
//                else
//                    SimpleUtils.fail("Schedule Page: 'LOCATION' should not be one label when current env isn't LG", false);
//            } else
//                SimpleUtils.report("Schedule Page: 'LOCATION' isn't one label currently");
//            if (availableFilters.get("shifttype").size() == 7 && availableFilters.get("jobtitle").size() > 1 && availableFilters.get("workrole").size() > 1)
//                SimpleUtils.pass("Schedule Page: 'SHIFT TYPE'/'JOB TITLE/'WORK ROLE' display as expected");
//            else
//                SimpleUtils.fail("Schedule Page: 'SHIFT TYPE'/'JOB TITLE/'WORK ROLE' display unexpectedly", false);
//        } else
//            SimpleUtils.fail("Schedule Page: The drop down list does not pop up", false);
//    }
//
//    @Override
//    public void verifyLocationFilterInLeft(boolean isLG) throws Exception {
//        if (isElementLoaded(filterPopup,5)) {
//                if (isLG) {
//                    if (filterLabels.size() == 4 && filterLabels.get(0).getText().equals("LOCATION"))
//                        SimpleUtils.pass("Schedule Page: 'LOCATION' displays in left when current env is LG");
//                    else
//                        SimpleUtils.fail("Schedule Page: 'LOCATION' is not in the left when current env is LG",false);
//                } else {
//                    if (filterLabels.size() == 3 && !filterLabels.get(0).getText().equals("LOCATION"))
//                        SimpleUtils.pass("Schedule Page: 'LOCATION' doesn't display when current env isn't LG");
//                    else
//                        SimpleUtils.fail("Schedule Page: Filter displays unexpectedly when current env isn't LG", false);
//                }
//        } else
//            SimpleUtils.fail("Schedule Page: The drop down list does not pop up",false);
//    }
//
//    @Override
//    public String selectRandomChildLocationToFilter() throws Exception {
//        String randomLocation = "";
//        int randomIndex = 0;
//        if (isElementLoaded(filterPopup,5)) {
//            HashMap<String, ArrayList<WebElement>> availableFilters = getAvailableFilters();
//            if (!availableFilters.get("location").isEmpty()) {
//                randomIndex = (new Random()).nextInt(availableFilters.get("location").size());
//                randomLocation = availableFilters.get("location").get(randomIndex).getText();
//                randomLocation = randomLocation.contains(" ")? randomLocation.split(" ")[0]: "";
//            } else
//                SimpleUtils.report("Schedule Page: 'LOCATION' isn't one label currently");
//        } else
//            SimpleUtils.fail("Schedule Page: The drop down list does not pop up", false);
//        return randomLocation;
//    }

//    @Override
//    public void selectAllChildLocationsToFilter() throws Exception {
//        if (isElementLoaded(filterPopup,5)) {
//            String locationFilterKey = "location";
//            ArrayList<WebElement> locationFilters = getAvailableFilters().get(locationFilterKey);
//            unCheckFilters(locationFilters);
//            checkFilters(locationFilters);
//        } else
//            SimpleUtils.fail("Schedule Page: The drop down list does not pop up", false);
//    }
//
//    @Override
//    public int getDaysBetweenFinalizeDateAndScheduleStartDate(String finalizeByDate, String scheduleStartDate) throws Exception {
//        int days = 0;
//        String finalizeByMonth = "";
//        String finalizeByDay = "";
//        String scheduleStartMonth = "";
//        String scheduleStartDay = "";
//        if (finalizeByDate.contains(" ") && finalizeByDate.split(" ").length == 4) {
//            finalizeByMonth = finalizeByDate.split(" ")[2];
//            finalizeByDay = finalizeByDate.split(" ")[3];
//        }
//        if (scheduleStartDate.contains(" ") && scheduleStartDate.split(" ").length == 2) {
//            scheduleStartMonth = scheduleStartDate.split(" ")[0];
//            scheduleStartDay = scheduleStartDate.split(" ")[1];
//        }
//        if (finalizeByMonth.toUpperCase().equals(scheduleStartMonth))
//            days = Integer.valueOf(scheduleStartDay) - Integer.valueOf(finalizeByDay);
//        else
//            days = Integer.valueOf(scheduleStartDay) + 31 - Integer.valueOf(finalizeByDay);
//        return days;
//    }

//    @Override
//    public void verifyAllChildLocationsShiftsLoadPerformance() throws Exception {
//        if (isElementLoaded(filterPopup,5)) {
//            String locationFilterKey = "location";
//            ArrayList<WebElement> locationFilters = getAvailableFilters().get(locationFilterKey);
//            unCheckFilters(locationFilters);
//            for (WebElement locationOption: locationFilters) {
//                click(locationOption);
//                if (areListElementVisible(weekScheduleShiftsOfWeekView,3))
//                    SimpleUtils.pass("Schedule Page: The performance target is < 3 seconds to load");
//                else
//                    SimpleUtils.fail("Schedule Page: The performance target is more than 3 seconds to load",false);
//            }
//        } else
//            SimpleUtils.fail("Schedule Page: The drop down list does not pop up", false);
//    }

//    @Override
//    public void verifyChildLocationShiftsLoadPerformance(String childLocation) throws Exception {
//        String locationFilterKey = "location";
//        boolean isChildLocationPresent = false;
//        ArrayList<WebElement> locationFilters = getAvailableFilters().get(locationFilterKey);
//        unCheckFilters(locationFilters);
//        for (WebElement locationOption: locationFilters) {
//            if (locationOption.getText().contains(childLocation)) {
//                isChildLocationPresent = true;
//                click(locationOption);
//                if (areListElementVisible(weekScheduleShiftsOfWeekView,3))
//                    SimpleUtils.pass("Schedule Page: The performance target is < 3 seconds to load");
//                else
//                    SimpleUtils.fail("Schedule Page: The performance target is more than 3 seconds to load",false);
//                break;
//            }
//        }
//        if (!isChildLocationPresent)
//            SimpleUtils.fail("Schedule Page: The filtered child location does not exist",false);
//    }

//    @Override
//    public void verifyShiftsDisplayThroughLocationFilter(String childLocation) throws Exception {
//        String locationFilterKey = "location";
//        boolean isChildLocationPresent = false;
//        ArrayList<WebElement> locationFilters = getAvailableFilters().get(locationFilterKey);
//        unCheckFilters(locationFilters);
//        for (WebElement locationOption: locationFilters) {
//            if (locationOption.getText().contains(childLocation)) {
//                isChildLocationPresent = true;
//                click(locationOption);
//                if (areListElementVisible(scheduleShiftTitles,3)) {
//                    for (WebElement title: scheduleShiftTitles) {
//                        if (childLocation.toUpperCase().contains(title.getText())) {
//                            SimpleUtils.pass("Schedule Page: The shifts change successfully according to the filter");
//                            break;
//                        } else
//                            SimpleUtils.fail("Schedule Page: The shifts don't change according to the filter",false);
//                    }
//                } else
//                    SimpleUtils.fail("Schedule Page: The shifts of the child location failed to load or loaded slowly",false);
//                break;
//            }
//        }
//        if (!isChildLocationPresent)
//            SimpleUtils.fail("Schedule Page: The filtered child location does not exist",false);
//        if (!filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
//            click(filterButton);
//    }

    //added by haya.  return a List has 4 week's data including last week
    @FindBy (css = ".row-fx.schedule-table-row.ng-scope")
    private List<WebElement> rowDataInOverviewPage;
    @FindBy (xpath = "//div[contains(@class,\"background-current-week-legend-calendar\")]/preceding-sibling::div[1]")
    private WebElement lastWeekNavigation;
    @FindBy (css = "i.fa-angle-left")
    private WebElement leftAngle;
//    @Override
//    public List<String> getOverviewData() throws Exception {
//        List<String> resultList = new ArrayList<String>();
//        if(isElementLoaded(leftAngle,10)){
//            click(leftAngle);
//            if (isElementLoaded(lastWeekNavigation,10)){
//                click(lastWeekNavigation);// click on last in overview page
//            }
//        }
//        waitForSeconds(3);
//        if (areListElementVisible(rowDataInOverviewPage,10)){
//            for (int i=0;i<rowDataInOverviewPage.size();i++){
//                String[] temp1 = rowDataInOverviewPage.get(i).getText().split("\n");
//                String[] temp2 = Arrays.copyOf(temp1,8);
//                resultList.add(Arrays.toString(temp2));
//            }
//        } else {
//            SimpleUtils.fail("data on schedules widget fail to load!",false);
//        }
//        return resultList;
//    }

//    @Override
//    public void verifyChangesNotPublishSmartCard(int changesNotPublished) throws Exception {
//        boolean flag = false;
//        if (areListElementVisible(smartCards,15)){
//            for (WebElement e: smartCards) {
//                //findElement(By.cssSelector(".card-carousel-card-title"))
//                String s = e.getText();
//                if (changesNotPublished == 0) {
//                    if (e.getText().toLowerCase().contains("action required") && e.getText().toLowerCase().contains("schedule not") && e.getText().toLowerCase().contains("published")) {
//                        SimpleUtils.pass("Changes not published smart card loads successfully!");
//                        flag = true;
//                        break;
//                    }
//                } else {
//                    if (e.getText().toLowerCase().contains("action required") && e.getText().toLowerCase().contains(changesNotPublished + " change") && e.getText().toLowerCase().contains("not published")) {
//                        SimpleUtils.pass("Changes not published smart card with number of changes loads successfully!");
//                        flag = true;
//                        break;
//                    }
//                }
//            }
//            if (!flag){
//                SimpleUtils.fail("There is no expected smart card",false);
//            }
//        } else {
//            SimpleUtils.fail("No smart cards!", false);
//        }
//    }
//
//    @Override
//    public void verifyLabelOfPublishBtn(String labelExpected) throws Exception {
//        if (isElementLoaded(txtPublishSheduleButton,5)){
//            if (txtPublishSheduleButton.getText().equals(labelExpected)){
//                SimpleUtils.pass("Label on publish button is correct!");
//            } else {
//                SimpleUtils.fail("Label on publish button is incorrect!",false);
//            }
//        } else {
//            SimpleUtils.fail("publish button fail to load!",false);
//        }
//    }

//    @FindBy(css = ".worker-edit-availability-status")
//    private WebElement messageForSelectTM;
//    @Override
//    public void verifyMessageIsExpected(String messageExpected) throws Exception {
//        if (isElementLoaded(messageForSelectTM,5)){
//            if (messageForSelectTM.getText()!=null && !messageForSelectTM.getText().equals("") && messageForSelectTM.getText().toLowerCase().contains(messageExpected)){
//                SimpleUtils.pass("There is a message you want to see: "+messageExpected);
//            } else {
//                SimpleUtils.fail("No message you expected! Actual message is "+ messageForSelectTM.getText(), false );
//            }
//        } else {
//            SimpleUtils.fail("message for select TM is not loaded!", false);
//        }
//    }
//
//    @Override
//    public String getAllTheWarningMessageOfTMWhenAssign() throws Exception {
//        String messageOfTMScheduledStatus = "";
//        if (isElementLoaded(messageForSelectTM,5)){
//            messageOfTMScheduledStatus = messageForSelectTM.getText();
//        }
//        return messageOfTMScheduledStatus;
//    }

    @FindBy(css = ".modal-dialog.modal-lgn-md")
    private WebElement dialogWarningModel;
    @FindBy(css = ".tma-dismiss-button")
    private WebElement closeSelectTMWindowBtn;
    @FindBy(css = ".lgn-action-button-success")
    private WebElement okButton;
//
//    @Override
//    public void verifyWarningModelForAssignTMOnTimeOff(String nickName) throws Exception {
//        String expectedMessageOnWarningModel1 = nickName.toLowerCase()+" is approved for time off";
//        String expectedMessageOnWarningModel2 = "please cancel the approved time off before assigning";
//        waitForSeconds(1);
//        if (isElementLoaded(alertMessage,15)) {
//            String s = alertMessage.getText();
//            if (s.toLowerCase().contains(expectedMessageOnWarningModel1) && s.toLowerCase().contains(expectedMessageOnWarningModel2)
//                    && isElementLoaded(okButton,5) && okButton.getText().equalsIgnoreCase("OK")){
//                waitForSeconds(1);
//                clickTheElement(okButton);
//                SimpleUtils.pass("There is a warning model with one button labeled OK! and the message is expected!");
//                if (isElementLoaded(closeSelectTMWindowBtn,5)){
//                    click(closeSelectTMWindowBtn);
//                }
//            }
//        } else {
//            SimpleUtils.fail("There is no warning model and warning message!", false);
//        }
//    }


//    public void switchSearchTMAndRecommendedTMsTab() {
//        if (areListElementVisible(selectTeamMembersOption, 10)) {
//            if (selectTeamMembersOption.get(0).getAttribute("class").contains("select")) {
//                click(selectTeamMembersOption.get(1));
//                SimpleUtils.pass("Recommended TMs tab been selected");
//            } else {
//                click(selectTeamMembersOption.get(0));
//                SimpleUtils.pass("Search Team Members tab been selected");
//            }
//        } else {
//            SimpleUtils.fail("Select Team Member options are not available", false);
//        }
//    }

    @FindBy(css = "lg-button[label=\"Analyze\"]")
    private WebElement analyzeBtn;
    @FindBy(css="div[ng-click=\"selectedTab = 'history'\"]")
    private WebElement schedulelHistoryTab;
//    @Override
//    public void clickOnAnalyzeBtn() throws Exception {
//        if (isElementLoaded(analyzeBtn,15)){
//            click(analyzeBtn);
//            SimpleUtils.pass("Clicked analyze button!");
//            if (isElementLoaded(schedulelHistoryTab,15)){
//                click(schedulelHistoryTab);
//                SimpleUtils.pass("Clicked schedulelHistoryTab!");
//            } else {
//                SimpleUtils.fail("There is no schedulelHistoryTab!", false);
//            }
//        } else {
//            SimpleUtils.fail("There is no Analyze button!", false);
//        }
//    }
//
//    @FindBy(css = ".sch-schedule-analyze__grey tr")
//    private List<WebElement> scheduleVersionInfo;
//
//    @Override
//    public void verifyScheduleVersion(String version) throws Exception {
//        if (areListElementVisible(scheduleVersionInfo,15) && areListElementVisible(scheduleVersionInfo.get(scheduleVersionInfo.size()-1).findElements(By.tagName("td")),15)){
//            String versionText = scheduleVersionInfo.get(scheduleVersionInfo.size()-1).findElements(By.tagName("td")).get(0).getText().split("\n")[0];
//            if ("".equals(versionText)){
//                versionText = scheduleVersionInfo.get(scheduleVersionInfo.size()-1).findElements(By.tagName("td")).get(1).getText().split("\n")[0];
//            }
//            if(version.equalsIgnoreCase(versionText)){
//                SimpleUtils.pass("version info is correct!");
//            }else {
//                SimpleUtils.fail("There is schedule HistoryTab!", false);
//            }
//        } else {
//            SimpleUtils.fail("There is no schedule version info!", false);
//        }
//    }
//
//    @FindBy(css = "lg-close.dismiss")
//    private WebElement closeAnalyzeBtn;
//    @Override
//    public void closeAnalyzeWindow() throws Exception {
//        if (isElementLoaded(closeAnalyzeBtn,15)){
//            click(closeAnalyzeBtn);
//            SimpleUtils.pass("Clicked close button!");
//        } else {
//            SimpleUtils.fail("There is no close button!", false);
//        }
//    }

//    @FindBy(css = ".save-schedule-confirm-message2")
//    private WebElement saveMessage;
//    @Override
//    public void verifyVersionInSaveMessage(String version) throws Exception {
//        if (isElementEnabled(scheduleSaveBtn)) {
//            clickTheElement(scheduleSaveBtn);
//        } else {
//            SimpleUtils.fail("Schedule save button not found", false);
//        }
//        waitForSeconds(3);
//        String a= saveMessage.getText();
//        if (isElementLoaded(saveMessage,15) && a.contains(version)){
//            SimpleUtils.pass("version info is correct!");
//        } else {
//            SimpleUtils.fail("There is no save message or the version is incorrect!", false);
//        }
//        if (isElementEnabled(saveOnSaveConfirmationPopup)) {
//            clickTheElement(saveOnSaveConfirmationPopup);
//            if (isElementLoaded(msgOnTop, 30) && msgOnTop.getText().contains("Success")) {
//                SimpleUtils.pass("Save the Schedule Successfully!");
//            } else {
//                SimpleUtils.fail("Save Schedule Failed!", false);
//            }
//        } else {
//            SimpleUtils.fail("Schedule save button not found", false);
//        }
//    }

//    @FindBy(xpath = "//span[text()=\"Manager\"]")
//    private WebElement managerTab;
//    @Override
//    public void clickOnManagerButton() throws Exception {
//        if (isElementEnabled(managerTab, 5)) {
//            click(managerTab);
//            SimpleUtils.pass("Manager button is clickable");
//        }else {
//            SimpleUtils.fail("There is no Manager button!",true);
//        }
//    }
//
//    @Override
//    public void verifyAllShiftsAssigned() throws Exception {
//        if (areListElementVisible(blueIconsOfOpenShift,20)){
//            SimpleUtils.fail("There are shifts not assigned!",false);
//        } else {
//            SimpleUtils.pass("All shifts are assigned!");
//        }
//    }
//
//    @Override
//    public void clickProfileIconOfShiftByIndex(int index) throws Exception {
//        if(areListElementVisible(weekShifts, 15) && index < weekShifts.size()){
//            clickTheElement(weekShifts.get(index).findElement(By.cssSelector(".worker-image-optimized img")));
//            SimpleUtils.pass("clicked shift icon!");
//        } else {
//            SimpleUtils.fail("There is no shift you want",false);
//        }
//    }

//    @Override
//    public int getTheIndexOfEditedShift() throws Exception {
//        int index = -1;
//        if (areListElementVisible(weekShifts, 10)) {
//            for (int i = 0; i < weekShifts.size(); i++) {
//                try {
//                    WebElement editedShift = weekShifts.get(i).findElement(By.cssSelector("[src*=\"edited-shift-week.png\"]"));
//                    index = i;
//                    SimpleUtils.pass("Schedule Week View: Get the index of the edited shift successfully: " + i);
//                    break;
//                } catch (NoSuchElementException e) {
//                    continue;
//                }
//            }
//        } else if (areListElementVisible(shiftsInDayView,10)) {
//            for (int i = 0; i < shiftsInDayView.size(); i++) {
//                try {
//                    WebElement editedShift = shiftsInDayView.get(i).findElement(By.xpath("./../../preceding-sibling::div[1][@ng-if=\"isShiftBeingEdited(shift)\"]"));
//                    index = i;
//                    SimpleUtils.pass("Schedule Day View: Get the index of the edited shift successfully: " + i);
//                    break;
//                } catch (NoSuchElementException e) {
//                    continue;
//                }
//            }
//        } else {
//            SimpleUtils.fail("Schedule Page: There are no shifts loaded!", false);
//        }
//        return index;
//    }
//
//    @FindBy(xpath = "//span[text()=\"View Status\"]")
//    private WebElement viewStatusBtn;
//    @Override
//    public void clickViewStatusBtn() throws Exception {
//        if(isElementLoaded(viewStatusBtn,15)){
//            click(viewStatusBtn);
//            SimpleUtils.pass("clicked view status button!");
//        } else {
//            SimpleUtils.fail("view status button is not loaded!",false);
//        }
//    }
//
//    @FindBy(css = "div.tma-scroll-table tr")
//    private List<WebElement> numberOfOffersMade;
//    @Override
//    public void verifyListOfOfferNotNull() throws Exception {
//        if (areListElementVisible(numberOfOffersMade,20)){
//            SimpleUtils.pass("There is a offer list which is not null!");
//        } else {
//            SimpleUtils.fail("The offer list is null!",false);
//        }
//    }
//
//    @Override
//    public void verifyTMInTheOfferList(String firstName, String expectedStatus) throws Exception{
//        boolean flag = false;
//        if (areListElementVisible(numberOfOffersMade,20)){
//            for (WebElement element: numberOfOffersMade){
//                if (element.getText().toLowerCase().contains(firstName.toLowerCase()) && element.getText().toLowerCase().contains(expectedStatus.toLowerCase())){
//                    flag = true;
//                    break;
//                }
//            }
//            if (flag){
//                SimpleUtils.pass(firstName + " is in the offered list!");
//            } else {
//                SimpleUtils.fail(firstName + " is not in the offered list!", false);
//            }
//        } else {
//            SimpleUtils.fail("The offer list is null!",false);
//        }
//    }

//    @FindBy(css = "[ng-click=\"openSearchBox()\"]")
//    private WebElement openSearchBoxButton;
//
//    @FindBy(css = "[ng-click=\"closeSearchBox()\"]")
//    private WebElement closeSearchBoxButton;
//
//    @FindBy(css = "input[placeholder*=\"Search by Employee Name, Work Role")
//    private WebElement searchBox;
//
//    @FindBy(css = "div[ng-show=\"!forbidModeChange\"]")
//    private WebElement switchDayViewAndWeeKViewButton;
//
//
//
//    public void verifyGhostTextInSearchBox () throws Exception{
//        if (isElementEnabled(searchBox, 5)) {
//            String ghostText = "Search by Employee Name, Work Role or Title";
//            if (searchBox.getAttribute("placeholder").equals(ghostText)) {
//                SimpleUtils.pass("The ghost text in search box display correctly");
//            } else
//                SimpleUtils.fail("The ghost text in search box display incorrectly",true);
//
//        } else {
//            SimpleUtils.fail("Search box on schedule page load fail!",false);
//        }
//    }
//
//    public void clickOnOpenSearchBoxButton() throws Exception {
//        if (isElementEnabled(openSearchBoxButton, 5)) {
//            click(openSearchBoxButton);
//            if (isElementLoaded(searchBox, 15)) {
//                SimpleUtils.pass("Search box is opened successfully");
//            } else {
//                SimpleUtils.fail("Search box is not opened successfully", false);
//            }
//
//        }else {
//            SimpleUtils.fail("There is no Open search box button!",false);
//        }
//    }
//
//    public void clickOnCloseSearchBoxButton() throws Exception {
//        if (isElementEnabled(closeSearchBoxButton, 5)) {
//            click(closeSearchBoxButton);
//            if (!isElementEnabled(searchBox, 5)) {
//                SimpleUtils.pass("Search box is closed successfully");
//            } else {
//                SimpleUtils.fail("Search box is not closed successfully", true);
//            }
//        }else {
//            SimpleUtils.fail("There is no Close search box button!",true);
//        }
//    }
//
//    public List<WebElement> searchShiftOnSchedulePage(String searchText) throws Exception {
//        List<WebElement> searchResult = null;
//        if (isElementEnabled(searchBox, 5)) {
//            searchBox.clear();
//            waitForSeconds(3);
//            searchBox.sendKeys(searchText);
//            waitForSeconds(5);
//            if (areListElementVisible(weekShifts, 5) && weekShifts.size() >0) {
//                searchResult = weekShifts;
//            } else if (areListElementVisible(dayViewAvailableShifts, 5) && dayViewAvailableShifts.size() >0) {
//                searchResult = dayViewAvailableShifts;
//            } else
//                SimpleUtils.report("Cannot search on schedule page!");
//        } else {
//            SimpleUtils.fail("Search box on schedule page load fail!",false);
//        }
//        return searchResult;
//    }

//    public void verifySearchResult (String firstNameOfTM, String lastNameOfTM, String workRole, String jobTitle, List<WebElement> searchResults) throws Exception {
//        if (searchResults !=null && searchResults.size()>0) {
//            if (firstNameOfTM != null) {
//                for (int i=0; i< searchResults.size(); i++) {
//                    String[] tmDetailName = getTMDetailNameFromProfilePage(searchResults.get(i)).split(" ");
//                    if (firstNameOfTM.equals(tmDetailName[0])|| firstNameOfTM.equals(tmDetailName[1]) || tmDetailName[0].contains(firstNameOfTM)
//                            || tmDetailName[1].contains(firstNameOfTM)) {
//                        SimpleUtils.pass("The search result display correctly when search by TM first name");
//                    } else {
//                        SimpleUtils.fail("The search result incorrect when search by TM first name, the expected name is: " + firstNameOfTM+ ". The actual name is: " + tmDetailName[0] +" " +tmDetailName[1],false);
//                        break;
//                    }
//                }
//            } else if (lastNameOfTM != null) {
//                for (int i=0; i< searchResults.size(); i++) {
//                    String[] tmDetailName = getTMDetailNameFromProfilePage(searchResults.get(i)).split(" ");
//                    if (tmDetailName[0].contains(lastNameOfTM) || tmDetailName[1].contains(lastNameOfTM)) {
//                        SimpleUtils.pass("The search result display correctly when search by TM last name");
//                    } else {
//                        SimpleUtils.fail("The search result incorrect when search by TM last name",false);
//                        break;
//                    }
//                }
//            }
//            else if (workRole != null) {
//                String[] workRoleWords = workRole.split(" ");
//                for (int i=0; i <searchResults.size(); i++) {
//                    scrollToElement(searchResults.get(i));
//                    Map<String, String> shiftInfo= getShiftInfoFromInfoPopUp(searchResults.get(i));
//                    String shiftWorkRole = shiftInfo.get("WorkRole");
//                    String shiftJobTitle = shiftInfo.get("JobTitle");
//                    if (workRole.equals(shiftWorkRole)|| workRole.equals(shiftJobTitle)) {
//                        SimpleUtils.pass("The search result display correctly when search by Work Role");
//                    } else if(workRoleWords.length>1) {
//                        for (int j=0; j< workRoleWords.length; j++){
//                            if (shiftWorkRole.contains(workRoleWords[j])){
//                                SimpleUtils.pass("The search result display correctly when search by Work Role");
//                                break;
//                            }
//                        }
//                    } else {
//                        SimpleUtils.fail("The search result incorrect when search by Work Role, expected: " + workRole
//                                + ", actual is: " + getShiftInfoFromInfoPopUp(searchResults.get(i)).get("WorkRole"),false);
//                        break;
//                    }
//                }
//            } else if (jobTitle != null) {
//                String[] jobTitleWords = jobTitle.split(" ");
//                for (int i=0; i <searchResults.size(); i++) {
//                    scrollToElement(searchResults.get(i));
//                    Map<String, String> shiftInfo= getShiftInfoFromInfoPopUp(searchResults.get(i));
//                    String shiftWorkRole = shiftInfo.get("WorkRole");
//                    String shiftJobTitle = shiftInfo.get("JobTitle");
//                    if (jobTitle.equals(shiftJobTitle)|| jobTitle.equals(shiftWorkRole)) {
//                        SimpleUtils.pass("The search result display correctly when search by Job Title");
//                    } else if(jobTitleWords.length>1) {
//                        for (int j=0; j< jobTitleWords.length; j++){
//                            if (shiftWorkRole.contains(jobTitleWords[j])){
//                                SimpleUtils.pass("The search result display correctly when search by Job Title");
//                                break;
//                            }
//                        }
//                    } else {
//                        SimpleUtils.fail("The search result incorrect when search by Job Title",false);
//                        break;
//                    }
//                }
//            } else {
//                SimpleUtils.fail("Verify texts all are null!",false);
//            }
//        } else {
//            SimpleUtils.fail("There is no search result!",false);
//        }
//    }

//    public Map<String, String> getShiftInfoFromInfoPopUp(WebElement shift) {
//        Map<String, String> shiftInfo = new HashMap<String, String>();
//        if (shift != null) {
//            if(isScheduleDayViewActive()){
//                click(shift.findElement(By.className("day-view-shift-hover-info-icon")));
//                waitForSeconds(2);
//            } else
//                click(shift.findElement(By.className("week-schedule-shit-open-popover")));
//
//        } else {
//            SimpleUtils.fail("Selected shift is null!",true);
//        }
//        if (isElementEnabled(popOverContent, 5)) {
//            String[] jobTitleAndWorkRole = popOverContent.findElement(By.cssSelector(".shift-hover-subheading.ng-binding")).getText().split("as");
//            if (jobTitleAndWorkRole.length==1){
//                //add job title
//                shiftInfo.put("JobTitle", "");
//                //add work role
//                shiftInfo.put("WorkRole", jobTitleAndWorkRole[0].trim());
//            } else {
//                //add job title
//                shiftInfo.put("JobTitle", jobTitleAndWorkRole[0].trim());
//                //add work role
//                shiftInfo.put("WorkRole", jobTitleAndWorkRole[1].trim());
//            }
//
//        }
//        return shiftInfo;
//    }

//    public void verifySearchBoxNotDisplayInDayView() throws Exception {
//        if (!switchDayViewAndWeeKViewButton.getAttribute("class").contains("hide")) {
//            ScheduleCommonPage scheduleCommonPage = new ConsoleScheduleCommonPage();
//            scheduleCommonPage.clickOnDayView();
//            if (!isElementEnabled(openSearchBoxButton, 5) || !isElementEnabled(searchBox, 5)) {
//                SimpleUtils.pass("Search box is not display in Day View");
//            } else
//                SimpleUtils.fail("Search box should not display in Day View", true);
//        }
//    }
//
//    public int getRandomIndexOfShift() {
//        int randomIndex = 0;
//        if (areListElementVisible(weekShifts, 5) && weekShifts.size() >0 ){
//            randomIndex = (new Random()).nextInt(weekShifts.size());
//        } else if (areListElementVisible(shiftsInDayView, 5) && shiftsInDayView.size() >0) {
//            randomIndex = (new Random()).nextInt(shiftsInDayView.size());
//        } else
//            SimpleUtils.fail("There is no shift display on schedule page", true);
//        return randomIndex;
//    }

    // Added by Nora: Drag & Drop
    @FindBy (className = "day-week-picker-date")
    private WebElement calMonthYear;
//
//    @Override
//    public String getWeekDayTextByIndex(int index) throws Exception {
//        String weekDayText = null;
//        if (index < 0 || index > 6) {
//            SimpleUtils.fail("The parameter index: " + index + " is out of range!", false);
//        }
//        if (areListElementVisible(weekDayLabels, 10)) {
//            weekDayText = weekDayLabels.get(index).getText();
//        } else {
//            SimpleUtils.fail("Schedule Week View: week day labels failed to load!", false);
//        }
//        return weekDayText;
//    }

//    @Override
//    public void goToSpecificWeekByDate(String date) throws Exception {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MMM dd");
//        Date switchDate = dateFormat.parse(date);
//        if (areListElementVisible(currentWeeks, 10)) {
//            for (int i = 0; i < currentWeeks.size(); i++) {
//                clickTheElement(currentWeeks.get(i));
//                List<String> years = getYearsFromCalendarMonthYearText();
//                String activeWeek = getActiveWeekText();
//                String[] items = activeWeek.split(" ");
//                String weekStartText = years.get(0) + " " + items[3] + " " + items[4];
//                String weekEndText = (years.size() == 2 ? years.get(1) : years.get(0)) + " " + items[6] + " " + items[7];
//                Date weekStartDate = dateFormat.parse(weekStartText);
//                Date weekEndDate = dateFormat.parse(weekEndText);
//                boolean isBetween = SimpleUtils.isDateInTimeDuration(switchDate, weekStartDate, weekEndDate);
//                if (isBetween) {
//                    SimpleUtils.report("Schedule Page: Navigate to week: " + activeWeek + ", it contains the day: " + date);
//                    break;
//                } else {
//                    if (i == (currentWeeks.size() - 1) && isElementLoaded(calendarNavigationNextWeekArrow, 5)) {
//                        click(calendarNavigationNextWeekArrow);
//                        goToSpecificWeekByDate(date);
//                    }
//                }
//            }
//        }
//    }

//    @Override
//    public List<Integer> selectDaysByCountAndCannotSelectedDate(int count, String cannotSelectedDate) throws Exception {
//        List<Integer> indexes = new ArrayList<>();
//        int selectedCount = 0;
//        if (count > 7) {
//            SimpleUtils.fail("Create New Shift: There are total 7 days, the count: " + count + " is larger than 7", false);
//        }
//        if (areListElementVisible(weekDays, 15) && weekDays.size() == 7) {
//            for (int i = 0; i < 7; i++) {
//                if (weekDays.get(i).getAttribute("class").contains("week-day-multi-picker-day-disabled")) {
//                    SimpleUtils.report("Day: " + weekDays.get(i).getText() + " is disabled!");
//                } else {
//                    if (cannotSelectedDate == null || cannotSelectedDate == "") {
//                        if (!weekDays.get(i).getAttribute("class").contains("week-day-multi-picker-day-selected")) {
//                            click(weekDays.get(i));
//                            SimpleUtils.report("Select day: " + weekDays.get(i).getText() + " Successfully!");
//                        }
//                        selectedCount++;
//                        indexes.add(i);
//                    } else {
//                        int date = Integer.parseInt(weekDays.get(i).getText().substring(weekDays.get(i).getText().length() - 2).trim());
//                        int cannotDate = Integer.parseInt(cannotSelectedDate.substring(cannotSelectedDate.length() - 2).trim());
//                        if (date != cannotDate) {
//                            if (!weekDays.get(i).getAttribute("class").contains("week-day-multi-picker-day-selected")) {
//                                click(weekDays.get(i));
//                                SimpleUtils.report("Select day: " + weekDays.get(i).getText() + " Successfully!");
//                            }
//                            selectedCount++;
//                            indexes.add(i);
//                        }
//                    }
//                    if (selectedCount == count) {
//                        SimpleUtils.pass("Create New Shift: Select " + count + " days Successfully!");
//                        break;
//                    }
//                }
//            }
//            if (selectedCount != count) {
//                SimpleUtils.fail("Create New Shift: Failed to select " + count + " days! Actual is: " + selectedCount + " days!", false);
//            }
//        }else{
//            SimpleUtils.fail("Weeks Days failed to load!", false);
//        }
//        return indexes;
//    }

//    @Override
//    public void verifyShiftIsMovedToAnotherDay(int startIndex, String firstName, int endIndex) throws Exception {
//        boolean isMoved = false;
//        List<WebElement> startElements = getDriver().findElements(By.cssSelector("[data-day-index=\"" + startIndex + "\"] .week-schedule-shift-wrapper"));
//        List<WebElement> endElements = getDriver().findElements(By.cssSelector("[data-day-index=\"" + endIndex + "\"] .week-schedule-shift-wrapper"));
//        if (startElements != null && endElements != null && startElements.size() > 0 && endElements.size() > 0) {
//            for (WebElement start : startElements) {
//                WebElement startName = start.findElement(By.className("week-schedule-worker-name"));
//                if (startName != null) {
//                    if (startName.getText().equalsIgnoreCase(firstName)) {
//                        SimpleUtils.fail("Still can find the TM:" + firstName + " on " + startIndex, false);
//                    }
//                } else {
//                    SimpleUtils.fail("Failed to find the worker name elements!", false);
//                }
//            }
//            for (WebElement end : endElements) {
//                WebElement endName = end.findElement(By.className("week-schedule-worker-name"));
//                if (endName != null) {
//                    if (endName.getText().equalsIgnoreCase(firstName)) {
//                        isMoved = true;
//                        break;
//                    }
//                } else {
//                    SimpleUtils.fail("Failed to find the worker name elements!", false);
//                }
//            }
//            if (!isMoved) {
//                SimpleUtils.fail(firstName + " isn't moved to the day, which index is: " + endIndex, false);
//            }
//        } else {
//            SimpleUtils.fail("Schedule Page: Failed to find the shift elements for index: " + startIndex + " or " + endIndex, false);
//        }
//    }
//
//    @Override
//    public void verifyShiftIsCopiedToAnotherDay(int startIndex, String firstName, int endIndex) throws Exception {
//        boolean isCopied = false;
//        boolean isExisted = false;
//        List<WebElement> startElements = getDriver().findElements(By.cssSelector("[data-day-index=\"" + startIndex + "\"] .week-schedule-shift-wrapper"));
//        List<WebElement> endElements = getDriver().findElements(By.cssSelector("[data-day-index=\"" + endIndex + "\"] .week-schedule-shift-wrapper"));
//        if (startElements != null && endElements != null && startElements.size() > 0 && endElements.size() > 0) {
//            for (WebElement start : startElements) {
//                WebElement startName = start.findElement(By.className("week-schedule-worker-name"));
//                if (startName != null) {
//                    if (startName.getText().equalsIgnoreCase(firstName)) {
//                        SimpleUtils.pass("Can find the TM:" + firstName + " on " + startIndex);
//                        isCopied = true;
//                        break;
//                    }
//                } else {
//                    SimpleUtils.fail("Failed to find the worker name elements!", false);
//                }
//            }
//            for (WebElement end : endElements) {
//                WebElement endName = end.findElement(By.className("week-schedule-worker-name"));
//                if (endName != null) {
//                    if (endName.getText().equalsIgnoreCase(firstName)) {
//                        isCopied = true;
//                        break;
//                    }
//                } else {
//                    SimpleUtils.fail("Failed to find the worker name elements!", false);
//                }
//            }
//            if (!isCopied && !isExisted) {
//                SimpleUtils.fail(firstName + " isn't copied to the day correctly, which index is: " + endIndex, false);
//            }
//        } else {
//            SimpleUtils.fail("Schedule Page: Failed to find the shift elements for index: " + startIndex + " or " + endIndex, false);
//        }
//    }
//
//    @Override
//    public void dragOneAvatarToAnother(int startIndex, String firstName, int endIndex) throws Exception {
//        boolean isDragged = false;
//        List<WebElement> startElements = getDriver().findElements(By.cssSelector("[data-day-index=\"" + startIndex + "\"] .week-schedule-shift-wrapper"));
//        List<WebElement> endElements = getDriver().findElements(By.cssSelector("[data-day-index=\"" + endIndex + "\"] .week-schedule-shift-wrapper"));
//        if (startElements != null && endElements != null && startElements.size() > 0 && endElements.size() > 0) {
//            for (WebElement start : startElements) {
//                WebElement startName = start.findElement(By.className("week-schedule-worker-name"));
//                WebElement startAvatar = start.findElement(By.cssSelector(".rows .week-view-shift-image-optimized img"));
//                if (startName != null && startAvatar != null && startName.getText().equalsIgnoreCase(firstName)) {
//                    for (WebElement end : endElements) {
//                        WebElement endAvatar = end.findElement(By.cssSelector(".rows .week-view-shift-image-optimized img"));
//                        WebElement endName = end.findElement(By.className("week-schedule-worker-name"));
//                        if (endAvatar != null && endName != null && !endName.getText().equalsIgnoreCase(firstName) &&
//                                !endName.getText().equalsIgnoreCase("Open")) {
//                            mouseHoverDragandDrop(startAvatar, endAvatar);
//                            SimpleUtils.report("Drag&Drop: Drag " + firstName + " to " + endName.getText() + " Successfully!");
//                            verifyConfirmStoreOpenCloseHours();
//                            isDragged = true;
//                            break;
//                        }
//                    }
//                    break;
//                }
//            }
//            if (!isDragged) {
//                SimpleUtils.fail("Failed to drag the user: " + firstName + " to another Successfully!", false);
//            }
//        } else {
//            SimpleUtils.fail("Schedule Page: Failed to find the shift elements for index: " + startIndex + " or " + endIndex, false);
//        }
//    }

//    @Override
//    public int getTheIndexOfTheDayInWeekView(String date) throws Exception {
//        int index = -1;
//        if (areListElementVisible(schCalendarDateLabel, 10)) {
//            for (int i = 0; i < schCalendarDateLabel.size(); i++) {
//                if (Integer.parseInt(schCalendarDateLabel.get(i).getText().trim()) == Integer.parseInt(date.trim())) {
//                    index = i;
//                    SimpleUtils.pass("Get the index of Date" + date + ", the index is: " + i);
//                    break;
//                }
//            }
//        } else {
//            SimpleUtils.fail("Schedule Week View: Week day labels are failed to load!", false);
//        }
//        if (index == -1) {
//            SimpleUtils.fail("Failed to get the index of the day: " + date, false);
//        }
//        return index;
//    }

//    public List<String> getYearsFromCalendarMonthYearText() throws Exception {
//        List<String> years = new ArrayList<>();
//        if (isElementLoaded(calMonthYear, 5)) {
//            if (calMonthYear.getText().contains("-")) {
//                String[] monthAndYear = calMonthYear.getText().split("-");
//                if (monthAndYear.length == 2) {
//                    if (monthAndYear[0].trim().length() > 4)
//                        years.add(monthAndYear[0].trim().substring(monthAndYear[0].trim().length() - 4));
//                    if (monthAndYear[1].trim().length() > 4)
//                        years.add(monthAndYear[1].trim().substring(monthAndYear[1].trim().length() - 4));
//                }
//            }else {
//                years.add(calMonthYear.getText().trim().substring(calMonthYear.getText().trim().length() - 4));
//            }
//        }else {
//            SimpleUtils.fail("Calendar month and year not loaded successfully!", false);
//        }
//        return years;
//    }

//    @Override
//    public HashMap<String,Integer> dragOneAvatarToAnotherSpecificAvatar(int startIndexOfTheDay, String user1, int endIndexOfTheDay, String user2) throws Exception {
//        List<WebElement> startElements = getDriver().findElements(By.cssSelector("[data-day-index=\"" + startIndexOfTheDay + "\"] .week-schedule-shift-wrapper"));
//        List<WebElement> endElements = getDriver().findElements(By.cssSelector("[data-day-index=\"" + endIndexOfTheDay + "\"] .week-schedule-shift-wrapper"));
//        HashMap<String,Integer> shiftsSwaped =  new HashMap<String, Integer>();
//        WebElement startAvatar = null;
//        WebElement endAvatar = null;
//        int i = 0;
//        int j = 0;
//        waitForSeconds(5);
//        if (startElements != null && endElements != null && startElements.size() > 0 && endElements.size() > 0) {
//            for (WebElement start : startElements) {
//                i++;
//                WebElement name1 = start.findElement(By.className("week-schedule-worker-name"));
//                if (name1 != null && name1.getText().equalsIgnoreCase(user1)) {
//                    startAvatar = start.findElement(By.cssSelector(".rows .week-view-shift-image-optimized img"));
//                    shiftsSwaped.put(user1,i);
//                }
//            }
//            for (WebElement end : endElements) {
//                j++;
//                WebElement name2 = end.findElement(By.className("week-schedule-worker-name"));
//                if (name2 != null  && name2.getText().equalsIgnoreCase(user2)) {
//                    endAvatar = end.findElement(By.cssSelector(".rows .week-view-shift-image-optimized img"));
//                    shiftsSwaped.put(user2,j);
//
//                }
//            }
//            if (endAvatar != null && startAvatar != null) {
//                mouseHoverDragandDrop(startAvatar, endAvatar);
//            }
//        } else {
//            SimpleUtils.fail("No shifts on the day",false);
//        }
//        return shiftsSwaped;
//    }
//
//    @FindBy(css = "div[ng-repeat=\"error in swapError\"]")
//    private List<WebElement> errorMessagesInSwap;
//    @FindBy(css = "div[ng-repeat=\"error in assignError\"]")
//    private List<WebElement> errorMessagesInAssign;
//    //========
//    @FindBy(css = "div[ng-repeat=\"error in assignError\"]")
//    private WebElement errorMessageInAssign;
//    @FindBy(css = ".swap-modal-error")
//    private List<WebElement> copyMoveErrorMesgs;
//
//    @Override
//    public void verifyMessageInConfirmPage(String expectedMassageInSwap, String expectedMassageInAssign) throws Exception {
//        String errorMessageForSwap = null;
//        String errorMessageInAssign = null;
//        if (areListElementVisible(errorMessagesInSwap,15) && areListElementVisible(errorMessagesInAssign,15)){
//            for (WebElement element: errorMessagesInSwap){
//                errorMessageForSwap = errorMessageForSwap+element.getText();
//            }
//            for (WebElement element: errorMessagesInAssign){
//                errorMessageInAssign = errorMessageInAssign+element.getText();
//            }
//            if (errorMessageForSwap.contains(expectedMassageInSwap) && errorMessageInAssign.contains(expectedMassageInAssign)){
//                SimpleUtils.pass("errorMessageInSwap: "+errorMessageForSwap+"\nerrorMessageInAssign: "+errorMessageInAssign);
//            }else{
//                SimpleUtils.fail("warning message for overtime when drag and drop is not expected!",false);
//            }
//
//        } else {
//            SimpleUtils.fail("No warning message for overtime when drag and drop",false);
//        }
//    }
//
//    @Override
//    public void verifyMessageOnCopyMoveConfirmPage(String expectedMsgInCopy, String expectedMsgInMove) throws Exception {
//        int count = 0;
//        if (areListElementVisible(copyMoveErrorMesgs,15) && copyMoveErrorMesgs.size() > 0){
//            for (WebElement message : copyMoveErrorMesgs) {
//                if (message.getText().equalsIgnoreCase(expectedMsgInCopy) || message.getText().equalsIgnoreCase(expectedMsgInMove)) {
//                    count = count + 1;
//                }
//            }
//            if (count == 2) {
//                SimpleUtils.pass(expectedMsgInCopy + " shows correctly!");
//            } else {
//                SimpleUtils.fail("\"" + expectedMsgInCopy + "\"" + " is not show!", false);
//            }
//        } else {
//            SimpleUtils.fail("No warning message when drag and drop",false);
//        }
//    }
//
//    @Override
//    public void verifyConfirmBtnIsDisabledForSpecificOption(String optionName) throws Exception {
//        try {
//             selectCopyOrMoveByOptionName(optionName);
//             if (isElementLoaded(confirmBtnOnDragAndDropConfirmPage, 5) && confirmBtnOnDragAndDropConfirmPage.getAttribute("class").contains("disabled")) {
//                 SimpleUtils.pass("CONFIRM button is disabled!");
//             } else {
//                 SimpleUtils.fail("CONFIRM button is mot loaded or is not disabled!", false);
//             }
//        } catch (Exception e) {
//            SimpleUtils.fail(e.getMessage(), false);
//        }
//    }
//
//    @Override
//    public void selectCopyOrMoveByOptionName(String optionName) throws Exception {
//        try {
//            if (areListElementVisible(swapAndAssignOptions,15)&&swapAndAssignOptions.size()==2){
//                if (optionName.equalsIgnoreCase("Copy")){
//                    click(swapAndAssignOptions.get(0));
//                    waitForSeconds(1);
//                    if (!swapAndAssignOptions.get(0).findElement(By.cssSelector(".tma-staffing-option-inner-circle")).getAttribute("class").contains("ng-hide")){
//                        SimpleUtils.pass("Copy option selected successfully!");
//                    } else {
//                        SimpleUtils.fail("Copy option is not selected", false);
//                    }
//                }
//                if (optionName.equalsIgnoreCase("Move")){
//                    click(swapAndAssignOptions.get(1));
//                    if (!swapAndAssignOptions.get(1).findElement(By.cssSelector(".tma-staffing-option-inner-circle")).getAttribute("class").contains("ng-hide")){
//                        SimpleUtils.pass("Move option selected successfully!");
//                    } else {
//                        SimpleUtils.fail("Move option is not selected", false);
//                    }
//                }
//            } else {
//                SimpleUtils.fail("Copy and move options fail to load!",false);
//            }
//        } catch (Exception e) {
//            SimpleUtils.fail(e.getMessage(), false);
//        }
//    }
//
//    @FindBy(css = ".tma-staffing-option-outer-circle")
//    private List<WebElement> swapAndAssignOptions;
//    @Override
//    public void selectSwapOrAssignOption(String action) throws Exception {
//        if (areListElementVisible(swapAndAssignOptions,15)&&swapAndAssignOptions.size()==2){
//            if (action.equalsIgnoreCase("swap")){
//                click(swapAndAssignOptions.get(0));
//                waitForSeconds(1);
//                if (!swapAndAssignOptions.get(0).findElement(By.cssSelector(".tma-staffing-option-inner-circle")).getAttribute("class").contains("ng-hide")){
//                    SimpleUtils.pass("swap option selected successfully!");
//                } else {
//                    SimpleUtils.fail("swap option is not selected", false);
//                }
//            }
//            if (action.equalsIgnoreCase("assign")){
//                click(swapAndAssignOptions.get(1));
//                if (!swapAndAssignOptions.get(1).findElement(By.cssSelector(".tma-staffing-option-inner-circle")).getAttribute("class").contains("ng-hide")){
//                    SimpleUtils.pass("assign option selected successfully!");
//                } else {
//                    SimpleUtils.fail("assign option is not selected", false);
//                }
//            }
//        } else {
//            SimpleUtils.fail("swap and assign options fail to load!",false);
//        }
//    }
//
//    @FindBy(css = ".modal-instance-button.confirm.ng-binding")
//    private WebElement confirmBtnOnDragAndDropConfirmPage;
//    @Override
//    public void clickConfirmBtnOnDragAndDropConfirmPage() throws Exception {
//        waitForSeconds(3);
//        if (isElementLoaded(confirmBtnOnDragAndDropConfirmPage,15) && !confirmBtnOnDragAndDropConfirmPage.getAttribute("class").contains("disabled")){
//            click(confirmBtnOnDragAndDropConfirmPage);
//            SimpleUtils.pass("confirm button is clicked successfully!");
//        } else {
//            SimpleUtils.fail("confirm button is disabled!",false);
//        }
//    }
//

//    @FindBy(css=".swap-modal-shifts.swap-modal-shifts-swap")
//    private WebElement swapSectionInfo;
//    @FindBy(css=".swap-modal-shifts.swap-modal-shifts-assign")
//    private WebElement assignSectionInfo;
//    @Override
//    public List<String> getShiftSwapDataFromConfirmPage(String action) throws Exception {
//        List<String> swapData = new ArrayList<>();
//        List<WebElement> swapResults = new ArrayList<>();
//        if (isElementLoaded(swapSectionInfo, 5) && isElementLoaded(assignSectionInfo, 5)) {
//            if (action.equalsIgnoreCase("swap")){
//                swapResults = swapSectionInfo.findElements(By.cssSelector("swap-modal-shift"));
//            }
//            if (action.equalsIgnoreCase("assign")){
//                swapResults = assignSectionInfo.findElements(By.cssSelector("swap-modal-shift"));
//            }
//
//            if (swapResults != null && swapResults.size() > 0) {
//                for(WebElement swapResult : swapResults) {
//                    WebElement date = swapResult.findElement(By.className("swap-modal-shift-time"));
//                    WebElement nameAndTitle = swapResult.findElement(By.className("swap-modal-shift-person"));
//                    if (date != null && nameAndTitle != null) {
//                        List <String> temp1 = Arrays.asList(nameAndTitle.getText().split("\n"));
//                        List <String> temp2 = Arrays.asList(date.getText().replace(",", "").replace("\n", "").split("-"));
//                        swapData.add(temp1.get(0)+"\n"+ temp1.get(1) + "\n"+ temp2.get(0) + "\n"+ temp2.get(1)+" - "+temp2.get(1) );
//                        SimpleUtils.report("Get the swap date: " + date.getText() + " and swap name title: " + nameAndTitle.getText() + " Successfully!");
//                    }else {
//                        SimpleUtils.fail("Failed to find the date and name elements!", false);
//                    }
//                }
//            }else {
//                SimpleUtils.fail("Failed to find the swap elements!", false);
//            }
//        }
//        if (swapData.size() != 2) {
//            SimpleUtils.fail("Failed to get the swap data!", false);
//        }
//        return swapData;
//    }
//
//    @Override
//    public int verifyDayHasShiftByName(int indexOfDay, String name) throws Exception {
//        int count = 0;
//        List<WebElement> shifts = getDriver().findElements(By.cssSelector("[data-day-index=\"" + indexOfDay + "\"] .week-schedule-shift-wrapper"));
//        if (shifts != null && shifts.size() > 0) {
//            for (WebElement shift : shifts) {
//                WebElement name1 = shift.findElement(By.className("week-schedule-worker-name"));
//                if (name1 != null && name1.getText().equalsIgnoreCase(name)) {
//                    SimpleUtils.pass("shift exists on this day!");
//                    count++;
//                }
//            }
//        } else {
//            SimpleUtils.fail("No shifts on the day",false);
//        }
//        return count;
//    }

//    public WebElement getShiftById(String id) throws Exception {
//        waitForSeconds(5);
//        WebElement shift = null;
//        if (id != null && !id.equals("")) {
//            String css = "div[data-shift-id=\""+ id+"\"]";
//            shift = MyThreadLocal.getDriver().findElement(By.cssSelector(css));
//            if (isElementLoaded(shift, 5)) {
//                SimpleUtils.pass("Get one shift by the id successfully");
//            } else
//                SimpleUtils.fail("Cannot find shift by the id !",false);
//        } else {
//            SimpleUtils.fail("The shift id is null or empty!",false);
//        }
//        return shift;
//    }


    @FindBy(css = "div[ng-repeat=\"error in swapError\"]")
    private List<WebElement> warningMessagesInSwap;
    @FindBy(css = "div[ng-repeat=\"error in assignError\"]")
    private List<WebElement> warningMessagesInAssign;
//
//    @Override
//    public boolean verifySwapAndAssignWarningMessageInConfirmPage(String expectedMessage, String action) throws Exception {
//        boolean canFindTheExpectedMessage = false;
//        if (action.equalsIgnoreCase("swap")) {
//            if (areListElementVisible(warningMessagesInSwap, 15) && warningMessagesInSwap.size() > 0) {
//                for (int i = 0; i < warningMessagesInSwap.size(); i++) {
//                    if (warningMessagesInSwap.get(i).getText().contains(expectedMessage)) {
//                        canFindTheExpectedMessage = true;
//                        SimpleUtils.pass("The expected message can be find successfully");
//                        break;
//                    }
//                }
//            } else {
//                SimpleUtils.report("There is no warning message in swap section");
//            }
//        } else if (action.equalsIgnoreCase("assign")) {
//            if (areListElementVisible(warningMessagesInAssign, 15) && warningMessagesInAssign.size() > 0) {
//                for (int i = 0; i < warningMessagesInAssign.size(); i++) {
//                    if (warningMessagesInAssign.get(i).getText().contains(expectedMessage)) {
//                        canFindTheExpectedMessage = true;
//                        SimpleUtils.pass("The expected message can be find successfully");
//                        break;
//                    }
//                }
//            } else {
//                SimpleUtils.report("There is no warning message in assign section");
//            }
//        } else
//            SimpleUtils.fail("No this action on drag&drop confirm page", true);
//
//        return canFindTheExpectedMessage;
//    }

//    @FindBy(css = "div.modal-instance-button")
//    private WebElement cancelBtnOnDragAndDropConfirmPage;
//    @Override
//    public void clickCancelBtnOnDragAndDropConfirmPage() throws Exception {
//        if (isElementLoaded(cancelBtnOnDragAndDropConfirmPage,15) ){
//            click(cancelBtnOnDragAndDropConfirmPage);
//            SimpleUtils.pass("cancel button is clicked successfully!");
//        } else {
//            SimpleUtils.fail("cancel button is disabled!",false);
//        }
//    }
//
//    @Override
//    public List<WebElement> getOneDayShiftByName(int indexOfDay, String name) throws Exception {
//        int count = 0;
//        List<WebElement> shiftsOfOneTM = new ArrayList<>();;
//        List<WebElement> shifts = getDriver().findElements(By.cssSelector("[data-day-index=\"" + indexOfDay + "\"] .week-schedule-shift-wrapper"));
//        if (areListElementVisible(shifts, 5) && shifts != null && shifts.size() > 0) {
//            for (WebElement shift : shifts) {
//                WebElement name1 = shift.findElement(By.className("week-schedule-worker-name"));
//                if (name1 != null && name1.getText().equalsIgnoreCase(name)) {
//                    shiftsOfOneTM.add(shift);
//                    SimpleUtils.pass("shift exists on this day!");
//                    count++;
//                }
//            }
//            if(count==0){
//                SimpleUtils.report("No shifts on the day for the TM: " + name);
//            }
//        } else {
//            SimpleUtils.fail("No shifts on the day",false);
//        }
//        return shiftsOfOneTM;
//    }

    @FindBy(css = "span.ot-hours-text")
    private List<WebElement> complianceMessageInInfoIconPopup;

//    @Override
//    public List<String> getComplianceMessageFromInfoIconPopup(WebElement shift) throws Exception {
//        List<String> complianceMessages = new ArrayList<>();
//        if (isElementLoaded(shift, 5)){
//            waitForSeconds(3);
//            scrollToElement(shift);
//            if(isScheduleDayViewActive()){
//                click(shift.findElement(By.cssSelector(".day-view-shift-hover-info-icon img")));
//                waitForSeconds(2);
//            } else
//                click(shift.findElement(By.cssSelector("img.week-schedule-shit-open-popover")));
//            if (isElementLoaded(popOverContent, 5)){
//                if (areListElementVisible(complianceMessageInInfoIconPopup, 5) && complianceMessageInInfoIconPopup.size()>0){
//                    for (int i=0; i< complianceMessageInInfoIconPopup.size(); i++){
//                        complianceMessages.add(complianceMessageInInfoIconPopup.get(i).getText());
//                    }
//                } else
//                    SimpleUtils.report("There is no compliance message in info icon popup");
//            } else
//                SimpleUtils.fail("Info icon popup fail to load", false);
//        } else
//            SimpleUtils.fail("Shift fail to load", false);
//        return complianceMessages;
//    }

//    @FindBy(css = ".sch-shift-hover.visible")
//    private WebElement infoTextFromInfoIcon;
//    @Override
//    public String getIIconTextInfo(WebElement shift) throws Exception{
//        if (isElementLoaded(shift, 5)){
//            waitForSeconds(3);
//            scrollToElement(shift);
//            if(isScheduleDayViewActive()){
//                click(shift.findElement(By.cssSelector(".day-view-shift-hover-info-icon img")));
//                waitForSeconds(2);
//            } else
//                click(shift.findElement(By.cssSelector("img.week-schedule-shit-open-popover")));
//            if (isElementLoaded(infoTextFromInfoIcon, 5)){
//                return infoTextFromInfoIcon.getText();
//            } else
//                SimpleUtils.fail("Info icon popup fail to load", false);
//        } else
//            SimpleUtils.fail("Shift fail to load", false);
//        return null;
//    }


//    @Override
//    public void dragOneShiftToAnotherDay(int startIndex, String firstName, int endIndex) throws Exception {
//        waitForSeconds(3);
//        boolean isDragged = false;
//        List<WebElement> startElements = getDriver().findElements(By.cssSelector("[data-day-index=\"" + startIndex + "\"] .week-schedule-shift-wrapper"));
//        List<WebElement> endElements = getDriver().findElements(By.cssSelector("[data-day-index=\"" + endIndex + "\"]"));
//        WebElement weekDay = getDriver().findElement(By.cssSelector("[data-day-index=\""+endIndex+"\"] .sch-calendar-day-label"));
//        if (startElements != null && endElements != null && startElements.size() > 0 && endElements.size() > 0 && weekDay!=null) {
//            for (WebElement start : startElements) {
//                WebElement startName = start.findElement(By.className("week-schedule-worker-name"));
//                if (startName != null && startName.getText().equalsIgnoreCase(firstName)) {
//                    mouseHoverDragandDrop(start, endElements.get(0));
//                    SimpleUtils.report("Drag&Drop: Drag " + firstName + " to " + weekDay.getText() + " days Successfully!");
//                    //verifyConfirmStoreOpenCloseHours();
//                    isDragged = true;
//                    break;
//                }
//            }
//            if (!isDragged) {
//                SimpleUtils.fail("Failed to drag the user: " + firstName + " to another Successfully!", false);
//            }
//        } else {
//            SimpleUtils.fail("Schedule Page: Failed to find the shift elements for index: " + startIndex + " or " + endIndex, false);
//        }
//    }
//
//    @Override
//    public String getNameOfTheFirstShiftInADay(int dayIndex) throws Exception {
//        List<WebElement> elements = getDriver().findElements(By.cssSelector("[data-day-index=\"" + dayIndex + "\"] .week-schedule-shift-wrapper"));
//        if (areListElementVisible(elements, 10)){
//            return elements.get(0).findElement(By.className("week-schedule-worker-name")).getText();
//        }
//        return null;
//    }
//
//    @FindBy(css = "div.lgn-alert-modal")
//    private WebElement warningMode;
//
//
//    @FindBy(css = "span.lgn-alert-message")
//    private List<WebElement> warningMessagesInWarningMode;
//
//    @FindBy(className = "lgn-action-button-success")
//    private WebElement okBtnInWarningMode;

//    @Override
//    public boolean ifWarningModeDisplay() throws Exception {
//        if(isElementLoaded(warningMode, 5)) {
//            SimpleUtils.pass("Warning mode is loaded successfully");
//            return true;
//        } else {
//            SimpleUtils.report("Warning mode fail to load");
//            return false;
//        }
//    }
//
//    @Override
//    public String getWarningMessageInDragShiftWarningMode() throws Exception {
//        String warningMessage = "";
//        if(areListElementVisible(warningMessagesInWarningMode, 5) && warningMessagesInWarningMode.size()>0) {
//            for (WebElement warningMessageInWarningMode: warningMessagesInWarningMode){
//                warningMessage = warningMessage + warningMessageInWarningMode.getText()+"\n";
//            }
//        } else {
//            SimpleUtils.fail("Warning message fail to load", false);
//        }
//        return warningMessage;
//    }
//
//    @Override
//    public void clickOnOkButtonInWarningMode() throws Exception {
//        if(isElementLoaded(okBtnInWarningMode, 5)) {
//            click(okBtnOnConfirm);
//            SimpleUtils.pass("Click on Ok button on warning successfully");
//        } else {
//            SimpleUtils.fail("Ok button fail to load", false);
//        }
//    }
//
//    @FindBy(css = "div.week-day-multi-picker-day-selected")
//    private List<WebElement> selectedDaysOnCreateShiftPage;
//
//    @Override
//    public List<String> getSelectedDayInfoFromCreateShiftPage() throws Exception {
//        List<String> selectedDates = new ArrayList<>();
//        if (areListElementVisible(selectedDaysOnCreateShiftPage, 5) && selectedDaysOnCreateShiftPage.size()>0) {
//            for (WebElement selectedDate: selectedDaysOnCreateShiftPage){
//                selectedDates.add(selectedDate.getText());
//            }
//            SimpleUtils.pass("Get selected days info successfully");
//        }else
//            SimpleUtils.fail("Select days load failed",true);
//        return selectedDates;
//    }

    @FindBy(css=".modal-dialog.modal-lgn-md")
    private WebElement moveAnywayDialog;
//
//    @Override
//    public void verifyConfirmStoreOpenCloseHours() throws Exception {
//        try {
//            if (ifMoveAnywayDialogDisplay()) {
//                if (isElementLoaded(moveAnywayDialog.findElement(By.cssSelector(".lgn-action-button-success")), 10)) {
//                    if (moveAnywayDialog.findElement(By.cssSelector(".lgn-action-button-success")).getText().equals("OK")) {
//                        clickTheElement(moveAnywayDialog.findElement(By.cssSelector(".lgn-action-button-success")));
//                        SimpleUtils.pass("CONFIRM button clicked!");
//                    }
//                }
//            }
//        } catch (Exception e) {
//            // Do nothing
//        }
//    }
//
//    @Override
//    public boolean ifMoveAnywayDialogDisplay() throws Exception {
//        if (isElementLoaded(moveAnywayDialog,10)){
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public void moveAnywayWhenChangeShift() throws Exception {
//        if (isElementLoaded(moveAnywayDialog.findElement(By.cssSelector(".lgn-action-button-success")),10)){
//            if (moveAnywayDialog.findElement(By.cssSelector(".lgn-action-button-success")).getText().equals("MOVE ANYWAY")) {
//                click(moveAnywayDialog.findElement(By.cssSelector(".lgn-action-button-success")));
//                SimpleUtils.pass("move anyway button clicked!");
//            } else {
//                SimpleUtils.fail("move anyway button fail to load!",false);
//            }
//        } else {
//            SimpleUtils.fail("move anyway button fail to load!",false);
//        }
//    }
//
//    @FindBy(css = "[search-results=\"workerSearchResult\"] [ng-class=\"swapStatusClass(worker)\"]")
//    private WebElement tmScheduledStatus;
//
//    @Override
//    public String getTheMessageOfTMScheduledStatus() throws Exception {
//        String messageOfTMScheduledStatus = "";
//        if (isElementLoaded(tmScheduledStatus,5)){
//            if (tmScheduledStatus.getText()!=null && !tmScheduledStatus.getText().equals("")){
//                messageOfTMScheduledStatus = tmScheduledStatus.getText();
//                SimpleUtils.pass("TM scheduled status display as : "+ messageOfTMScheduledStatus);
//            } else {
//                SimpleUtils.fail("TM scheduled status message is empty ", false );
//            }
//        } else {
//            SimpleUtils.fail("TM scheduled status is not loaded!", false);
//        }
//        return messageOfTMScheduledStatus;
//    }
//
//
//    @Override
//    public void verifyWarningModelMessageAssignTMInAnotherLocWhenScheduleNotPublished() throws Exception {
//        String expectedMessageOnWarningModel = "cannot be assigned because the schedule has not been published yet at the home location";
//        if (isElementLoaded(alertMessage,15)) {
//            String s = alertMessage.getText();
//            if (s.toLowerCase().contains(expectedMessageOnWarningModel)
//                    && isElementLoaded(okButton,5)){
//                click(okButton);
//                SimpleUtils.pass("There is a warning model with one button labeled OK! and the message is expected!");
//                if (isElementLoaded(closeSelectTMWindowBtn,5)){
//                    click(closeSelectTMWindowBtn);
//                }
//            }
//        } else {
//            SimpleUtils.fail("There is no warning model and warning message!", false);
//        }
//    }
//
//    @Override
//    public void closeCustomizeNewShiftWindow() throws Exception {
//        if (isElementLoaded(closeSelectTMWindowBtn, 10)){
//            clickTheElement(closeSelectTMWindowBtn);
//            waitUntilElementIsInVisible(closeSelectTMWindowBtn);
//        } else {
//            SimpleUtils.fail("Customize New Shift window: Close button not loaded Successfully!", false);
//        }
//    }
//
//    @FindBy(css=".tma-table")
//    private WebElement TMResultsTable;
//    @Override
//    public void verifyTMNotSelected() throws Exception {
//        if (isElementLoaded(TMResultsTable,10)){
//            if (TMResultsTable.findElements(By.cssSelector(".tma-staffing-option-inner-circle")).size()>0
//                    && TMResultsTable.findElements(By.cssSelector(".tma-staffing-option-inner-circle")).get(0).getAttribute("class").contains("ng-hide")){
//                SimpleUtils.pass("TM is not selected!");
//            } else {
//                SimpleUtils.fail("TM is selected!",false);
//            }
//        }
//    }
//
//    @Override
//    public void clickOnRadioButtonOfSearchedTeamMemberByName(String name) throws Exception {
//        if (areListElementVisible(searchResults, 15)) {
//            for (WebElement searchResult : searchResults) {
//                WebElement workerName = searchResult.findElement(By.className("worker-edit-search-worker-name"));
//                WebElement optionCircle = searchResult.findElement(By.className("tma-staffing-option-outer-circle"));
//                if (workerName != null && optionCircle != null) {
//                    if (workerName.getText().toLowerCase().trim().replaceAll("\n"," ").contains(name.trim().toLowerCase())) {
//                        click(optionCircle);
//                        SimpleUtils.report("Select Team Member: " + name + " Successfully!");
//                    }
//                }else {
//                    SimpleUtils.fail("Worker name or option circle not loaded Successfully!", false);
//                }
//            }
//        }else {
//            SimpleUtils.fail("Failed to find the team member!", false);
//        }
//
//
//    }
//
//    @Override
//    public void clickOnAssignAnywayButton() throws Exception {
//        waitForSeconds(2);
//        if (isElementLoaded(btnAssignAnyway, 5) && btnAssignAnyway.getText().equalsIgnoreCase("ASSIGN ANYWAY")) {
//            click(btnAssignAnyway);
//            SimpleUtils.report("Assign Team Member: Click on 'ASSIGN ANYWAY' button Successfully!");
//        } else{
//            SimpleUtils.fail("Assign Team Member: 'ASSIGN ANYWAY' button fail to load!", false);
//        }
//    }
//
//
//    @Override
//    public WebElement getTheShiftByIndex(int index) throws Exception {
//        WebElement shift = null;
//        if (areListElementVisible(weekShifts, 20) && index < weekShifts.size()) {
//            shift = weekShifts.get(index);
//        } else if (areListElementVisible(shiftsInDayView, 20) && index < shiftsInDayView.size()) {
//            shift = shiftsInDayView.get(index);
//        } else
//            SimpleUtils.fail("Schedule Page: week or day shifts not loaded successfully!", false);
//         return shift;
//    }

//    @FindBy(css = ".modal-dialog")
//    private WebElement holidaySmartCardWindow;
//    @Override
//    public List<String> getHolidaysOfCurrentWeek() throws Exception {
//        List<String> holidays = new ArrayList<String>();
//        if (isElementLoaded(holidaySmartCardWindow,5) && holidaySmartCardWindow.findElements(By.cssSelector(".event-card span")).size()>0){
//            List<WebElement> holidayList = holidaySmartCardWindow.findElements(By.cssSelector(".event-card span"));
//            for (WebElement element: holidayList){
//                holidays.add(element.getText().replace("\n",""));
//            }
//        } else {
//            SimpleUtils.fail("Holiday popup window fail to load!", false);
//        }
//        return holidays;
//    }

//    @Override
//    public void selectWeekDaysByDayName(String dayName) throws Exception {
//        boolean isDayNameExist = false;
//        if (areListElementVisible(weekDays, 5) && weekDays.size() == 7) {
//            for(int i=0; i< weekDays.size(); i++){
//                String weekDayName = weekDays.get(i).getText().split("\n")[0];
//                if (weekDayName.equalsIgnoreCase(dayName)){
//                    click(weekDays.get(i));
//                    SimpleUtils.report("Select day: " + weekDays.get(i).getText() + " Successfully!");
//                    isDayNameExist = true;
//                    break;
//                }
//            }
//            if (!isDayNameExist) {
//                SimpleUtils.fail("This is a wrong day name: "+ dayName+ "The correct day names should be: Mon, TUE, WED, THU, FRI, SAT, SUN", true);
//            }
//        }else{
//            SimpleUtils.fail("Weeks Days failed to load!", true);
//        }
//    }

//    @FindBy(css = "[ng-repeat=\"day in summary.workingHours\"]")
//    private List<WebElement> operatingHours;
//
//    @FindBy(css = "[ng-class=\"{ 'switcher-closed': !value }\"]")
//    private WebElement openOrCloseWeekDayButton;
//
//    @FindBy(css = "[ng-click=\"$dismiss()\"] button[ng-click=\"$ctrl.onSubmit({type:'saveas',label:$ctrl.label})\"]")
//    private WebElement editOperatingHourCancelButton;
//
//    @FindBy(css = "[ng-click=\"save()\"] button")
//    private WebElement editOperatingHourSaveButton;
//
//
//    @Override
//    public void editOperatingHoursOnScheduleOldUIPage(String startTime, String endTime, List<String> weekDaysToClose) throws Exception {
//        waitForSeconds(6);
//        if (areListElementVisible(operatingHours, 20) && operatingHours.size()==7){
//            for (WebElement operatingHour : operatingHours){
//                WebElement weekDay = operatingHour.findElement(By.cssSelector("td[class=\"ng-binding\"]"));
//                WebElement editButton = operatingHour.findElement(By.cssSelector("span[ng-if=\"canEditWorkingHours\"]"));
//                WebElement openCloseHours = operatingHour.findElement(By.cssSelector("[ng-class=\"{dirty: day.isOverridden}\"]"));
//
//                if (isElementLoaded(weekDay, 5) && !weekDay.getText().equals("")
//                        && isElementLoaded(editButton, 5)
//                        && isElementLoaded(openCloseHours, 5) && !openCloseHours.getText().equals("")){
//                    if (weekDaysToClose.contains(weekDay.getText())){
//                        if (openCloseHours.getText().equalsIgnoreCase("Closed")) {
//                            SimpleUtils.report("Week day: "+weekDay.getText()+" is already closed");
//                        } else{
//                            click(editButton);
//                            if (isElementLoaded(openOrCloseWeekDayButton, 5)){
//                                if (!openOrCloseWeekDayButton.getAttribute("class").contains("switcher-closed")) {
//                                    click(openOrCloseWeekDayButton);
//                                    click(editOperatingHourSaveButton);
//                                    openCloseHours = operatingHour.findElement(By.cssSelector("[ng-class=\"{dirty: day.isOverridden}\"]"));
//                                    if (openCloseHours.getText().equalsIgnoreCase("Closed")){
//                                        SimpleUtils.report("Week day: "+weekDay.getText()+" been closed successfully!");
//                                    } else {
//                                        SimpleUtils.fail("Close week day: "+weekDay.getText()+" failed!", false);
//                                    }
//                                }
//                            } else{
//                                SimpleUtils.fail("Open Or Close week day button not loaded Successfully!", false);
//                            }
//                        }
//                    } else{
//                        if (!openCloseHours.getText().equalsIgnoreCase(startTime+"-"+endTime)){
//                            click(editButton);
//                            if (openOrCloseWeekDayButton.getAttribute("class").contains("switcher-closed")){
//                                click(openOrCloseWeekDayButton);
//                                SimpleUtils.report("Week day: "+weekDay.getText()+" been opened successfully!");
//                            }
//                            moveSliderAtCertainPoint(endTime, ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
//                            moveSliderAtCertainPoint(startTime, ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
//                            clickTheElement(editOperatingHourSaveButton);
//                            waitForSeconds(2);
//                            // If operating hours is consistent with the values wanted to change, then Save button is disabled
//                            if (isElementLoaded(operatingHoursCancelBtn, 5)) {
//                                clickTheElement(operatingHoursCancelBtn);
//                            }
//                            openCloseHours = operatingHour.findElement(By.cssSelector("[ng-class=\"{dirty: day.isOverridden}\"]"));
//                            if (openCloseHours.getText().equalsIgnoreCase(startTime+":00-"+endTime+":00")){
//                                SimpleUtils.report("Week day: "+weekDay.getText()+" been edited successfully!");
//                            } else  if (openCloseHours.getText().equalsIgnoreCase(startTime+"-"+endTime)){
//                                SimpleUtils.report("Week day: "+weekDay.getText()+" been edited successfully!");
//                            } else  if (openCloseHours.getText().equalsIgnoreCase(startTime+"am-"+endTime+"pm")){
//                                SimpleUtils.report("Week day: "+weekDay.getText()+" been edited successfully!");
//                            } else {
//                                SimpleUtils.fail("Edit week day: "+weekDay.getText()+" failed!", false);
//                            }
//                        } else {
//                            SimpleUtils.report("Week day: "+weekDay.getText()+"'s operating hours already been set as: " + openCloseHours.getText());
//                        }
//
//                    }
//                } else{
//                    SimpleUtils.fail("Week days not loaded Successfully!", false);
//                }
//
//            }
//        }else{
//            SimpleUtils.fail("Operating Hours not loaded Successfully!", false);
//        }
//    }
//
//
//
//    @FindBy(css = "[ng-class=\"{'schedule-view-small-padding': controlPanel.isGenerateSchedleView}\"]")
//    private WebElement tmSchedulePanel;
//    @Override
//    public void verifyTMSchedulePanelDisplay() throws Exception {
//        try{
//            if(isElementLoaded(tmSchedulePanel, 5)){
//                SimpleUtils.pass("TM schedule panel is loaded successfully! ");
//            } else
//                SimpleUtils.fail("TM schedule panel not loaded successfully! ", false);
//        }catch(Exception e){
//            SimpleUtils.fail(e.getMessage(), false);
//        }
//    }

//    @Override
//    public boolean suggestedButtonIsHighlighted() throws Exception {
//        if (isElementLoaded(scheduleTypeSystem, 5) && scheduleTypeSystem.getAttribute("class").contains("g-button-group-selected") ){
//            SimpleUtils.pass("The suggest button is high lighted");
//            return true;
//        }else {
//            SimpleUtils.fail("The suggest button load failed",true);
//        }
//        return false;
//    }
//
//    @Override
//    public boolean verifyWFSFunction() {
//        if (searchResults.size()!=0) {
//            SimpleUtils.pass("Can search team members in Workforce sharing group");
//            return true;
//        }else
//            SimpleUtils.fail("Workforce Sharing function work wrong",false);
//        return false;
//    }

    @FindBy(css = "div[ng-repeat=\"schedule in previousWeeksSchedules\"] div")
    private List<WebElement> previousWeeks;
    @FindBy(css = ".schedule-disabled-tooltip")
    private WebElement scheduleDisabledTooltip;
//    @Override
//    public void verifyPreviousWeekWhenCreateAndCopySchedule(String weekInfo, boolean shouldBeSelected) throws Exception {
//        //Need to prepare 2 previous week to check.
//        if (areListElementVisible(previousWeeks, 10) && previousWeeks.size()>=2){
//            for (WebElement element: previousWeeks){
//                String weekDayInfo = element.findElement(By.cssSelector(".generate-modal-week-name")).getText().split("\n")[1];
//                if (weekInfo.equalsIgnoreCase(weekDayInfo)){
//                    if (!element.getAttribute("class").contains("disabled")) {
//                        if (shouldBeSelected == !element.findElement(By.cssSelector(".generate-modal-week")).getAttribute("class").contains("disabled")){
//                            SimpleUtils.pass("Should the week:"+weekInfo+" be selected is correct!");
//                        } else {
//                            SimpleUtils.fail("Should the week:"+weekInfo+" be selected is not the expected!", false);
//                        }
//                    } else
//                        SimpleUtils.fail("This week is disbled and cannot be selected! ", false);
//                }
//            }
//        } else {
//            SimpleUtils.fail("There is no previous week to copy", false);
//        }
//    }

//    @Override
//    public String convertDateStringFormat(String dateString) throws Exception{
//        String result = dateString;
//        // dateString format: JAN 2 - JAN 9, will convert 2 to 02, 9 to 09, return JAN 02 - JAN 09
//        String[] items = dateString.split(" ");
//        if (items.length==5 && SimpleUtils.isNumeric(items[1]) && SimpleUtils.isNumeric(items[4])){
//            if (Integer.parseInt(items[1])<10 ){
//                items[1] = Integer.toString(Integer.parseInt(items[1]));
//                result = items[0] + " 0" + items[1] + " " + items[2] + " " + items[3];
//            } else {
//                result = items[0] + " " + items[1] + " " + items[2] + " " + items[3];
//            }
//            if (Integer.parseInt(items[4])<10){
//                items[4] = Integer.toString(Integer.parseInt(items[4]));
//                result = result + " 0" + items[4];
//            } else {
//                result = result + " " + items[4];
//            }
//        } else {
//            SimpleUtils.fail("week day info format is not expected! Split String: " + dateString + " failed!", false);
//        }
//        return result;
//    }
//
//    @FindBy(css = ".generate-schedule-staffing tr:not([ng-repeat]) th[class=\"text-right ng-binding\"]")
//    private WebElement staffingGuidanceHrs;
//    @Override
//    public float getStaffingGuidanceHrs() throws Exception {
//        float staffingGuidanceHours = 0;
//        if (isElementLoaded(staffingGuidanceHrs,20) && SimpleUtils.isNumeric(staffingGuidanceHrs.getText().replace("\n",""))){
//            staffingGuidanceHours = Float.parseFloat(staffingGuidanceHrs.getText().replace("\n",""));
//        } else {
//            SimpleUtils.fail("There is no Staffing guidance hours!", false);
//        }
//        return staffingGuidanceHours;
//    }
//
//    @Override
//    public void verifyTooltipForCopyScheduleWeek(String weekInfo) throws Exception {
//        //Need to prepare 2 previous week to check.
//        if (areListElementVisible(previousWeeks, 10) && previousWeeks.size()>=2){
//            for (WebElement element: previousWeeks){
//                String weekDayInfo = element.findElement(By.cssSelector(".generate-modal-week-name")).getText().split("\n")[1];
//                if (weekInfo.equalsIgnoreCase(weekDayInfo)){
//                    mouseHover(element);
//                    String tooltipText = "Policy: Max. 2 violations and 0% over budget";
//                    if (scheduleDisabledTooltip.getAttribute("style").contains("visible") && tooltipText.equalsIgnoreCase(scheduleDisabledTooltip.getText())){
//                        SimpleUtils.pass("Tooltip is expected!");
//                    } else {
//                        SimpleUtils.fail("Tooltip should display when mouse hover the week!", false);
//                    }
//                }
//            }
//        } else {
//            SimpleUtils.fail("There is no previous week to copy", false);
//        }
//    }

//    @FindBy(css = ".generate-modal-week-violations-different-hours")
//    private WebElement differrentOperatingHoursInfo;
//    @Override
//    public void verifyDifferentOperatingHours(String weekInfo) throws Exception {
//        if (areListElementVisible(previousWeeks, 10) && previousWeeks.size()>=2){
//            for (WebElement element: previousWeeks){
//                String weekDayInfo = element.findElement(By.cssSelector(".generate-modal-week-name")).getText().split("\n")[1];
//                if (weekInfo.equalsIgnoreCase(weekDayInfo)){
//                    String differentOperatingHrsInfo = "*Different operating hours";
//                    if (isElementLoaded(differrentOperatingHoursInfo,5) && differrentOperatingHoursInfo.getText().contains(differentOperatingHrsInfo)){
//                        SimpleUtils.pass("Differrent Operating Hours info is expected!");
//                    } else {
//                        SimpleUtils.fail("Differrent Operating Hours info is not loaded!", false);
//                    }
//                }
//            }
//        } else {
//            SimpleUtils.fail("There is no previous week to copy", false);
//        }
//    }
//
//    @FindBy(css = "div.analytics-new-table")
//    private WebElement analyticsTableInScheduleDMViewPage;
//    @Override
//    public boolean isScheduleDMView() throws Exception {
//        boolean result = false;
//        if (isElementLoaded(analyticsTableInScheduleDMViewPage, 60)) {
//            result = true;
//        }
//        return result;
//    }
//
//    @Override
//    public int getShiftsNumberByName(String name) throws Exception {
//        int result = 0;
//        if (areListElementVisible(shiftsWeekView, 15)) {
//            if (name == null && name.equals("")){
//                result = shiftsWeekView.size();
//            } else {
//                for (WebElement shiftWeekView : shiftsWeekView) {
//                    WebElement workerName = shiftWeekView.findElement(By.className("week-schedule-worker-name"));
//                    if (workerName != null) {
//                        if (workerName.getText().toLowerCase().contains(name.toLowerCase())) {
//                            result++;
//                        }
//                    }
//                }
//            }
//        }
//        return result;
//    }
//
//    @FindBy(css = "div.analytics-new-table-group-row-open")
//    private List<WebElement> locationsInTheList;
//    @Override
//    public List<String> getLocationsInScheduleDMViewLocationsTable() throws Exception {
//        waitForSeconds(3);
//        List<String> locations = new ArrayList<String>();
//        if (areListElementVisible(getDriver().findElements(By.cssSelector("div.analytics-new-table-group-row-open")),10)){
//            for (int i=0; i< getDriver().findElements(By.cssSelector("div.analytics-new-table-group-row-open")).size(); i++){
//                locations.add(getDriver().findElements(By.cssSelector("div.analytics-new-table-group-row-open")).get(i).findElement(By.cssSelector("img.analytics-new-table-location~span")).getText());
//            }
//        }
//        return locations;
//    }
//
//    @FindBy(css = "div.analytics-new-table-header")
//    private WebElement locationTableHeader;
//    @Override
//    public void verifySortByColForLocationsInDMView(int index) throws Exception {
//        List<String> listString = new ArrayList<String>();
//        List<Float> listFloat = new ArrayList<Float>();
//        if (index > 0 && index <= getNumOfColInDMViewTable()){
//            listString = getListByColInTimesheetDMView(index);
//            if (locationTableHeader.findElements(By.cssSelector("i.analytics-new-table-header-sorter")).size()==getNumOfColInDMViewTable()){
//                click(locationTableHeader.findElements(By.cssSelector("i.analytics-new-table-header-sorter")).get(index-1));
//                if (locationTableHeader.findElements(By.cssSelector("i.analytics-new-table-header-sorter")).get(index-1).getAttribute("class").contains("sorter-up")){
//                    if (transferStringToFloat(listString).size()==listString.size()){
//                        listFloat = transferStringToFloat(listString).stream().sorted(Float::compareTo).collect(Collectors.toList());
//                        if (Math.abs(transferStringToFloat(getListByColInTimesheetDMView(index)).get(listFloat.size()-1)-listFloat.get(listFloat.size()-1)) == 0){
//                            SimpleUtils.pass("Sort result is correct!");
//                        } else {
//                            SimpleUtils.fail("Sort result is incorrect!", false);
//                        }
//                    } else {
//                        listString = listString.stream().sorted(String::compareTo).collect(Collectors.toList());
//                        if (getListByColInTimesheetDMView(index).get(0).equals(listString.get(0))){
//                            SimpleUtils.pass("Sort result is correct!");
//                        } else {
//                            SimpleUtils.fail("Sort result is incorrect!", false);
//                        }
//                    }
//                } else {
//                    if (transferStringToFloat(listString).size()==listString.size()){
//                        listFloat = transferStringToFloat(listString).stream().sorted(Float::compareTo).collect(Collectors.toList());
//                        if (Math.abs(transferStringToFloat(getListByColInTimesheetDMView(index)).get(0)-listFloat.get(listFloat.size()-1)) == 0){
//                            SimpleUtils.pass("Sort result is correct!");
//                        } else {
//                            SimpleUtils.fail("Sort result is incorrect!", false);
//                        }
//                    } else {
//                        listString = listString.stream().sorted(String::compareTo).collect(Collectors.toList());
//                        if (getListByColInTimesheetDMView(index).get(0).equals(listString.get(listString.size()-1))){
//                            SimpleUtils.pass("Sort result is correct!");
//                        } else {
//                            SimpleUtils.fail("Sort result is incorrect!", false);
//                        }
//                    }
//                }
//            } else {
//                SimpleUtils.fail("Columns are not loaded correctly!", false);
//            }
//        } else {
//            SimpleUtils.fail("Index beyond range.", false);
//        }
//    }

//    @Override
//    public List<Float> transferStringToFloat(List<String> listString) throws Exception{
//        List<Float> result = new ArrayList<Float>();
//        boolean flag = true;
//        for (String s : listString){
//            if (!SimpleUtils.isNumeric(s)){
//                flag = false;
//                break;
//            }
//        }
//        if (flag){
//            for (String s : listString){
//                result.add(Float.parseFloat(s));
//            }
//        }
//        return result;
//    }

//    @Override
//    public void verifySearchLocationInScheduleDMView(String location) throws Exception {
//        boolean flag = true;
//        waitForSeconds(5);
//        if (isElementLoaded(analyticsTableInScheduleDMViewPage.findElement(By.cssSelector("[ng-class=\"{'ng-invalid': $ctrl.invalid}\"]")),60)){
//            analyticsTableInScheduleDMViewPage.findElement(By.cssSelector("[ng-class=\"{'ng-invalid': $ctrl.invalid}\"]")).clear();
//            analyticsTableInScheduleDMViewPage.findElement(By.cssSelector("[ng-class=\"{'ng-invalid': $ctrl.invalid}\"]")).sendKeys(location);
//            for (String s: getLocationsInScheduleDMViewLocationsTable()){
//                flag = flag && s.contains(location);
//            }
//            if (flag){
//                SimpleUtils.pass("Search result is correct!");
//            } else {
//                SimpleUtils.fail("Search result is incorrect!", false);
//            }
//        } else {
//            SimpleUtils.fail("Search box is not loaded!", false);
//        }
//    }

//    @Override
//    public void navigateToPreviousWeek() throws Exception {
//        int currentWeekIndex = -1;
//        if (areListElementVisible(currentWeeks, 10)) {
//            for (int i = 0; i < currentWeeks.size(); i++) {
//                String className = currentWeeks.get(i).getAttribute("class");
//                if (className.contains("day-week-picker-period-active")) {
//                    currentWeekIndex = i;
//                }
//            }
//            if (currentWeekIndex == 0 && isElementLoaded(calendarNavigationPreviousWeekArrow, 5)) {
//                clickTheElement(calendarNavigationPreviousWeekArrow);
//                if (areListElementVisible(currentWeeks, 5)) {
//                    clickTheElement(currentWeeks.get(currentWeeks.size()-1));
//                    SimpleUtils.pass("Navigate to previous week: '" + currentWeeks.get(currentWeeks.size()-1).getText() + "' Successfully!");
//                }
//            }else {
//                clickTheElement(currentWeeks.get(currentWeekIndex - 1));
//                SimpleUtils.pass("Navigate to previous week: '" + currentWeeks.get(currentWeekIndex - 1).getText() + "' Successfully!");
//            }
//        }else {
//            SimpleUtils.fail("Current weeks' elements not loaded Successfully!", false);
//        }
//    }
//
//    @Override
//    public void clickOnLocationNameInDMView(String location) throws Exception {
//        boolean flag = false;
//        if (areListElementVisible(locationsInTheList, 15)) {
//            for (WebElement element : locationsInTheList) {
//                if (element.findElement(By.cssSelector("img.analytics-new-table-location~span")).getText().contains(location)) {
//                    flag = true;
//                    click(element.findElement(By.cssSelector("img.analytics-new-table-location~span")));
//                    SimpleUtils.pass(location + "clicked!");
//                    break;
//                }
//            }
//            if (!flag) {
//                SimpleUtils.fail("No this location: " + location, false);
//            }
//        } else {
//            SimpleUtils.fail("No location displayed!", false);
//        }
//    }
//
//    @FindBy(css = "lg-button[label*=\"Republish\"]")
//    private WebElement republishButton;
//
//    public boolean isPublishButtonLoadedOnSchedulePage() throws Exception {
//        boolean isPublishButtonLoaded = false;
//        if (isElementLoaded(publishButton, 4)){
//            isPublishButtonLoaded = true;
//            SimpleUtils.report("Publish button loaded successfully on schedule page! ");
//        } else
//            SimpleUtils.report("Publish button loaded fail on schedule page! ");
//        return isPublishButtonLoaded;
//    }
//
//    public boolean isRepublishButtonLoadedOnSchedulePage() throws Exception {
//        boolean isRepublishButtonLoaded = false;
//        if (isElementLoaded(republishButton, 4)){
//            isRepublishButtonLoaded = true;
//            SimpleUtils.report("Republish button loaded successfully on schedule page! ");
//        } else
//            SimpleUtils.report("Republish button loaded fail on schedule page! ");
//        return isRepublishButtonLoaded;
//    }

//    public void clickOnRepublishButtonLoadedOnSchedulePage() throws Exception {
//        if (isElementLoaded(republishButton, 4)){
//            click(republishButton);
//            SimpleUtils.pass("Click Republish button successfully on schedule page! ");
//        } else
//            SimpleUtils.fail("Republish button loaded fail on schedule page! ", false);
//    }
//
//    public boolean isCreateScheduleBtnLoadedOnSchedulePage() throws Exception {
//        boolean isCreateScheduleBtnLoaded = false;
//        if (isElementLoaded(generateSheduleButton, 4)) {
//            isCreateScheduleBtnLoaded = true;
//            SimpleUtils.report("Create Schedule button loaded successfully on schedule page! ");
//        } else
//            SimpleUtils.report("Create Schedule button loaded fail on schedule page! ");
//        return isCreateScheduleBtnLoaded;
//    }
//
//    @FindBy(css = "div[class=\"card-carousel-card card-carousel-card-primary \"]")
//    private WebElement locationSummary;
//    @Override
//    public HashMap<String, Float> getValuesAndVerifyInfoForLocationSummaryInDMView(String upperFieldType, String weekType) throws Exception {
//        HashMap<String, Float> result = new HashMap<String, Float>();
//        if (isElementLoaded(locationSummary,10) && locationSummary.findElements(By.cssSelector("text")).size()>=6){
//            String upperFieldSummaryTitle = locationSummary.findElement(By.cssSelector(".card-carousel-card-title")).getText().toLowerCase();
//            if (upperFieldSummaryTitle.contains(upperFieldType.toLowerCase() + "s summary")
//                    || upperFieldSummaryTitle.contains(upperFieldType.toLowerCase() + " summary")){
//                SimpleUtils.pass("Location Summary smart title displays correctly!");
//                String numOfLocations = locationSummary.findElement(By.cssSelector(".card-carousel-card-title")).getText().split(" ")[0];
//                if (SimpleUtils.isNumeric(numOfLocations)){
//                    result.put("NumOfLocations", Float.valueOf(numOfLocations));
//                } else {
//                    SimpleUtils.fail("Location count in title fail to load!", false);
//                }
//            } else {
//                SimpleUtils.fail("Location Summary smart title diaplays incorrectly!", false);
//            }
//            if (SimpleUtils.isNumeric(locationSummary.findElements(By.cssSelector("text")).get(0).getText().replace(",","")) && SimpleUtils.isNumeric(locationSummary.findElements(By.cssSelector("text")).get(2).getText().replace(",",""))){
//                result.put(locationSummary.findElements(By.cssSelector("text")).get(1).getText(),
//                        Float.valueOf(locationSummary.findElements(By.cssSelector("text")).get(0).getText().replace(",","")));
//                result.put(locationSummary.findElements(By.cssSelector("text")).get(3).getText(),
//                        Float.valueOf(locationSummary.findElements(By.cssSelector("text")).get(2).getText().replace(",","")));
//            } else {
//                SimpleUtils.fail("Budget hours and Published hours display incorrectly!", false);
//            }
//            if (locationSummary.findElements(By.cssSelector("text")).size()==6
//                    && SimpleUtils.isNumeric(locationSummary.findElements(By.cssSelector("text")).get(4).getText().replace(" Hrs","").replace(",",""))){
//                result.put(locationSummary.findElements(By.cssSelector("text")).get(5).getText(), Float.valueOf(locationSummary.findElements(By.cssSelector("text")).get(4).getText().replace(" Hrs","").replace(",","")));
//                if (locationSummary.findElements(By.cssSelector("text")).get(5).getText().contains("▼")){
//                    if (locationSummary.findElements(By.cssSelector("text")).get(5).getAttribute("fill").contains("#50b83c")){
//                        SimpleUtils.pass("The color of the value is correct! -> green");
//                    } else {
//                        SimpleUtils.fail("The color of the value is incorrect! ->not green", false);
//                    }
//                } else if (locationSummary.findElements(By.cssSelector("text")).get(5).getText().contains("▲")){
//                    if (locationSummary.findElements(By.cssSelector("text")).get(5).getAttribute("fill").contains("#ff0000")){
//                        SimpleUtils.pass("The color of the value is correct! -> red");
//                    } else {
//                        SimpleUtils.fail("The color of the value is incorrect! ->not red", false);
//                    }
//                }
//            }
//            if (locationSummary.findElements(By.cssSelector("text")).size()==8
//                    && SimpleUtils.isNumeric(locationSummary.findElements(By.cssSelector("text")).get(4).getText().replace(" Hrs","").replace(",",""))
//                    && SimpleUtils.isNumeric(locationSummary.findElements(By.cssSelector("text")).get(6).getText().replace(" Hrs","").replace(",",""))){
//                result.put(locationSummary.findElements(By.cssSelector("text")).get(5).getText(), Float.valueOf(locationSummary.findElements(By.cssSelector("text")).get(4).getText().replace(" Hrs","").replace(",","")));
//                result.put(locationSummary.findElements(By.cssSelector("text")).get(7).getText(), Float.valueOf(locationSummary.findElements(By.cssSelector("text")).get(6).getText().replace(" Hrs","").replace(",","")));
//
//                if (locationSummary.findElements(By.cssSelector("text")).get(5).getText().contains("▼")){
//                    if (locationSummary.findElements(By.cssSelector("text")).get(5).getAttribute("fill").contains("#50b83c")){
//                        SimpleUtils.pass("The color of the value is correct! -> green");
//                    } else {
//                        SimpleUtils.fail("The color of the value is incorrect! ->not green", false);
//                    }
//                } else if (locationSummary.findElements(By.cssSelector("text")).get(5).getText().contains("▲")){
//                    if (locationSummary.findElements(By.cssSelector("text")).get(5).getAttribute("fill").contains("#ff0000")){
//                        SimpleUtils.pass("The color of the value is correct! -> red");
//                    } else {
//                        SimpleUtils.fail("The color of the value is incorrect! ->not red", false);
//                    }
//                }
//            }
//            if(weekType.toLowerCase().contains("current") || weekType.contains("previous")){
//                if (isElementLoaded(locationSummary.findElement(By.cssSelector(".published-clocked-cols-summary")),10)
//                        && locationSummary.findElement(By.cssSelector(".published-clocked-cols-summary")).getText().contains("Scheduled Within Budget")
//                        && locationSummary.findElement(By.cssSelector(".published-clocked-cols-summary")).getText().contains("Scheduled Over Budget")
//                        && getLocationSummaryDataFromSchedulePage().size() == 3){
//                    String numOfProjectedWithin = getLocationSummaryDataFromSchedulePage().get(1).split(" ")[0];
//                    String numOfProjectedOver = getLocationSummaryDataFromSchedulePage().get(2).split(" ")[0];
//                    if (SimpleUtils.isNumeric(numOfProjectedWithin.replace(",","")) && SimpleUtils.isNumeric(numOfProjectedOver.replace(",",""))){
//                        result.put("NumOfProjectedWithin", Float.valueOf(numOfProjectedWithin.replace(",","")));
//                        result.put("NumOfProjectedOver", Float.valueOf(numOfProjectedOver.replace(",","")));
//                    } else {
//                        SimpleUtils.fail("Scheduled Location count in title fail to load!", false);
//                    }
//                    SimpleUtils.pass("Scheduled locations info load successfully!");
//                } else {
//                    SimpleUtils.fail("Scheduled locations info fail to load!", false);
//                }
//            } else {
//                if (isElementLoaded(locationSummary.findElement(By.cssSelector(".published-clocked-cols-summary")),10)
//                        && locationSummary.findElement(By.cssSelector(".published-clocked-cols-summary")).getText().contains("Published Within Budget")
//                        && locationSummary.findElement(By.cssSelector(".published-clocked-cols-summary")).getText().contains("Published Over Budget")
//                        && getLocationSummaryDataFromSchedulePage().size() == 3){
//                    String numOfProjectedWithin = getLocationSummaryDataFromSchedulePage().get(1).split(" ")[0];
//                    String numOfProjectedOver = getLocationSummaryDataFromSchedulePage().get(2).split(" ")[0];
//                    if (SimpleUtils.isNumeric(numOfProjectedWithin.replace(",","")) && SimpleUtils.isNumeric(numOfProjectedOver.replace(",",""))){
//                        result.put("NumOfProjectedWithin", Float.valueOf(numOfProjectedWithin.replace(",","")));
//                        result.put("NumOfProjectedOver", Float.valueOf(numOfProjectedOver.replace(",","")));
//                    } else {
//                        SimpleUtils.fail("Projected Location count in title fail to load!", false);
//                    }
//                    SimpleUtils.pass("Projected locations info load successfully!");
//                } else {
//                    SimpleUtils.fail("Projected locations info fail to load!", false);
//                }
//            }
//
//        } else {
//            SimpleUtils.fail("Location summary smart card fail to load!", false);
//        }
//        return result;
//    }
//
//    @Override
//    public void verifyClockedOrProjectedInDMViewTable(String expected) throws Exception {
//        if (isElementLoaded(locationTableHeader, 10)){
//            if (locationTableHeader.getText().toLowerCase().contains(expected.toLowerCase())){
//                SimpleUtils.pass(expected + " displays!");
//            } else {
//                SimpleUtils.fail(expected + " doesn't display!", false);
//            }
//        } else {
//            SimpleUtils.fail("Table header fail to load!", false);
//        }
//    }
//
//    @Override
//    public int getIndexOfColInDMViewTable(String colName) throws Exception {
//        int index = 0;
//        boolean colExist = false;
//        if (isElementLoaded(locationTableHeader, 10)){
//            for (String s: locationTableHeader.getText().replace("\n(Hrs)","").split("\n")){
//                ++index;
//                if (s.toLowerCase().contains(colName.toLowerCase())){
//                    colExist = true;
//                    break;
//                }
//            }
//            if (!colExist) {
//                index = 0;
//            }
//        } else {
//            SimpleUtils.fail("Table header fail to load!", false);
//        }
//        return index;
//    }
//
//    private int getNumOfColInDMViewTable() throws Exception {
//        int num = 0;
//        if (isElementLoaded(locationTableHeader, 10)){
//            num = locationTableHeader.getText().split("\n").length;
//        } else {
//            SimpleUtils.fail("Table header fail to load!", false);
//        }
//        return num;
//    }
//
//    @FindBy(css = "div.card-carousel-container")
//    private WebElement cardContainerInDMView;
//    @Override
//    public HashMap<String, Integer> getValueOnUnplannedClocksSummaryCardAndVerifyInfo() throws Exception {
//        HashMap<String, Integer> result = new HashMap<String, Integer>();
//        if (isElementLoaded(cardContainerInDMView,10) && isElementLoaded(cardContainerInDMView.findElement(By.cssSelector("div[class*=\"card-carousel-card-analytics-card-color-\"]")),10)){
//            List<String> strList = Arrays.asList(cardContainerInDMView.findElement(By.cssSelector("div[class*=\"card-carousel-card-analytics-card-color-\"]")).getText().split("\n"));
//            if (strList.size()==4 && strList.get(1).toLowerCase().contains("unplanned") && strList.get(2).toLowerCase().contains("clocks") && SimpleUtils.isNumeric(strList.get(0)) && SimpleUtils.isNumeric(strList.get(3).replace(" total timesheets", ""))){
//                result.put("unplanned clocks", Integer.parseInt(strList.get(0)));
//                result.put("total timesheets", Integer.parseInt(strList.get(3).replace(" total timesheets", "")));
//                SimpleUtils.pass("All info on Unplanned Clocks Summary Card is expected!");
//            } else {
//                SimpleUtils.fail("Info on Unplanned Clocks Summary Card is not expected!", false);
//            }
//        } else {
//            SimpleUtils.fail("Unplanned clocks card fail to load!", false);
//        }
//        return result;
//    }

//    @Override
//    public List<String> getListByColInTimesheetDMView(int index) throws Exception{
//        List<String> list = new ArrayList<String>();
//        for (int i = 0; i < getDriver().findElements(By.cssSelector("div.analytics-new-table-group-row-open")).size(); i++){
//            List<WebElement> columns = getDriver().findElements(By.cssSelector("div.analytics-new-table-group-row-open")).get(i).findElements(By.cssSelector(".ng-scope.col-fx-1"));
//            if (index > 0 && index <= getNumOfColInDMViewTable() && columns.size()>=getNumOfColInDMViewTable()-1){
//                if (index == 1){
//                    list = getLocationsInScheduleDMViewLocationsTable();
//                } else {
//                    if (areListElementVisible(getDriver().findElements(By.cssSelector("div.analytics-new-table-group-row-open")),10)){
//                        list.add(columns.get(index-2).getText().replace("%","").replace(",",""));
//                    }
//                }
//            } else {
//                SimpleUtils.fail("Index beyond range.", false);
//            }
//        }
//        return list;
//    }
//
//    @Override
//    public HashMap<String, Integer> getValueOnUnplannedClocksSmartCardAndVerifyInfo() throws Exception {
//        HashMap<String, Integer> result = new HashMap<String, Integer>();
//        if (isElementLoaded(cardContainerInDMView,10) && isElementLoaded(cardContainerInDMView.findElement(By.cssSelector("div.card-carousel-card-card-carousel-card-yellow-top")),10)){
//            String info = cardContainerInDMView.findElement(By.cssSelector("div.card-carousel-card-card-carousel-card-yellow-top")).getText();
//            List<String> strList = Arrays.asList(info.split("\n"));
//            if (strList.size()==13 && strList.get(0).contains("UNPLANNED CLOCKS") && strList.get(2).contains("Early Clocks")&& strList.get(4).contains("Late Clocks")&& strList.get(6).contains("Incomplete Clocks")
//                    && strList.get(8).contains("Missed Meal")&& strList.get(10).contains("No Show")&& strList.get(12).contains("Unscheduled")){
//                SimpleUtils.pass("Title and info on Unplanned Clocks Smart Card are expected!");
//                if (SimpleUtils.isNumeric(strList.get(1).replace(",", ""))
//                        && SimpleUtils.isNumeric(strList.get(3).replace(",", ""))
//                        && SimpleUtils.isNumeric(strList.get(5).replace(",", ""))
//                        && SimpleUtils.isNumeric(strList.get(7).replace(",", ""))
//                        && SimpleUtils.isNumeric(strList.get(9).replace(",", ""))
//                        && SimpleUtils.isNumeric(strList.get(11).replace(",", ""))){
//                    result.put(strList.get(2), Integer.parseInt(strList.get(1).replace(",", "")));
//                    result.put(strList.get(4), Integer.parseInt(strList.get(3).replace(",", "")));
//                    result.put(strList.get(6), Integer.parseInt(strList.get(5).replace(",", "")));
//                    result.put(strList.get(8), Integer.parseInt(strList.get(7).replace(",", "")));
//                    result.put(strList.get(10), Integer.parseInt(strList.get(9).replace(",", "")));
//                    result.put(strList.get(12), Integer.parseInt(strList.get(11).replace(",", "")));
//                } else {
//                    SimpleUtils.fail("Datas on UNPLANNED CLOCKS smart card aren't numeric!", false);
//                }
//            } else {
//                SimpleUtils.fail("Info on Unplanned Clocks smart Card is not expected!", false);
//            }
//        } else {
//            SimpleUtils.fail("Unplanned clocks card fail to load!", false);
//        }
//        return result;
//    }
//
//    @Override
//    public void clickSpecificLocationInDMViewAnalyticTable(String location) throws Exception {
//        waitForSeconds(3);
//        if (areListElementVisible(locationsInTheList,10)){
//            for (WebElement element: locationsInTheList){
//                if (location.equalsIgnoreCase(element.findElement(By.cssSelector("img.analytics-new-table-location~span")).getText())){
//                    click(element);
//                    SimpleUtils.pass(location + " is clicked!");
//                    break;
//                }
//            }
//        } else {
//            SimpleUtils.fail("There is no location in the list!", false);
//        }
//    }
//
//    @Override
//    public boolean hasNextWeek() throws Exception {
//        int currentWeekIndex = -1;
//        if (areListElementVisible(currentWeeks, 10)) {
//            for (int i = 0; i < currentWeeks.size(); i++) {
//                String className = currentWeeks.get(i).getAttribute("class");
//                if (className.contains("day-week-picker-period-active")) {
//                    currentWeekIndex = i;
//                }
//            }
//            if (currentWeekIndex == (currentWeeks.size() - 1) && !isElementLoaded(calendarNavigationNextWeekArrow, 5)) {
//                return false;
//            }else {
//                return true;
//            }
//        } else {
//            return false;
//        }
//    }


    @FindBy(css = ".sch-calendar-day.week-view")
    private List<WebElement> scheduleCalendarDaysHeaderInWeekView;

//    @Override
//    public float getTotalProjectionOpenShiftsHoursForCurrentWeek() throws Exception {
//        float totalProjectionOpenShiftsHours = 0;
//        boolean isPastDay = true;
//        selectShiftTypeFilterByText("Open");
//        if (areListElementVisible(daySummaries,10)
//                && areListElementVisible(scheduleCalendarDaysHeaderInWeekView, 10)
//                && daySummaries.size() == scheduleCalendarDaysHeaderInWeekView.size()){
//            for (int i=0; i<daySummaries.size();i++){
//                boolean isToday = scheduleCalendarDaysHeaderInWeekView.get(i).getAttribute("class").contains("today");
//                if(isToday){
//                    isPastDay = false;
//                }
//                if(!isPastDay){
//                    String[] TMShiftSize = daySummaries.get(i).findElement(By.cssSelector("span:nth-child(1)")).getText().split(" ");
//                    float shiftSizeInHour = Float.valueOf(TMShiftSize[0]);
//                    totalProjectionOpenShiftsHours = totalProjectionOpenShiftsHours + shiftSizeInHour;
//                }
//
//            }
//        } else {
//            SimpleUtils.fail("Schedule Calendar Days Header In WeekView are not loaded!", false);
//        }
//        return totalProjectionOpenShiftsHours;
//    }
//
//
//    @FindBy(css = "span[ng-if=\"canEditWorkingHours\"]")
//    private List<WebElement> editOperatingHousButtonOnUngenerateSchedulePage;
//
//    public boolean checkIfEditOperatingHoursButtonsAreShown() throws Exception {
//        boolean areEditButtonShown = false;
//        if(areListElementVisible(editOperatingHousButtonOnUngenerateSchedulePage, 10)){
//            areEditButtonShown = true;
//            SimpleUtils.report("Edit operating hours buttons are shown on ungenerate schedule page! ");
//        } else if(isElementLoaded(operatingHoursEditBtn, 5)){
//            areEditButtonShown = true;
//            SimpleUtils.report("Edit operating hours button are shown on create schedule page! ");
//        } else
//            SimpleUtils.report("Edit operating hours buttons are not shown! ");
//        return areEditButtonShown;
//    }
//
//
//    @FindBy(css = "[ng-if=\"controlPanel.editMode !== 'edit' || controlPanel.isPublished\"] .card-carousel-card-smart-card-required")
//    private WebElement scheduleNotPublishedSmartCard;
//
//    @Override
//    public boolean isScheduleNotPublishedSmartCardLoaded() throws Exception {
//        if (isElementLoaded(scheduleNotPublishedSmartCard,15)) {
//            SimpleUtils.pass("Schedule Not Published SmartCard is show ");
//            return true;
//        } else {
//            SimpleUtils.report("There is no Schedule Not Published SmartCard this week");
//            return false;
//        }
//
//    }
//
//    @FindBy(css = "lgn-drop-down.tma-locations-dropdown button.lgn-dropdown-button")
//    private WebElement btnChildLocation;
//    @FindBy(css = ".sch-day-view-shift .sch-shift-worker-img-cursor")
//    private List<WebElement> profileIconsInDayView;
//    @FindBy(css = ".tma-dismiss-button")
//    private WebElement closeViewStatusBtn;
//
//    public void selectChildLocInCreateShiftWindow(String location) throws Exception {
//        if (isElementLoaded(btnChildLocation, 20)) {
//            click(btnChildLocation);
//            SimpleUtils.pass("Child location button clicked Successfully");
//        } else {
//            SimpleUtils.fail("Child location button is not clickable", false);
//        }
//        if (listWorkRoles.size() > 0) {
//            for (WebElement listWorkRole : listWorkRoles) {
//                if (listWorkRole.getText().toLowerCase().contains(location.toLowerCase())) {
//                    click(listWorkRole);
//                    SimpleUtils.pass("Child location " + location + "selected Successfully");
//                    break;
//                } else {
//                    SimpleUtils.report("Child location" + location + " not selected");
//                }
//            }
//
//        } else {
//            SimpleUtils.fail("Child location size are empty", false);
//        }
//
//    }

//    public void selectChildLocationFilterByText(String location) throws Exception {
//        String shiftTypeFilterKey = "location";
//        ArrayList<WebElement> shiftTypeFilters = getAvailableFilters().get(shiftTypeFilterKey);
//        unCheckFilters(shiftTypeFilters);
//        for (WebElement shiftTypeOption : shiftTypeFilters) {
//            if (shiftTypeOption.getText().toLowerCase().contains(location.toLowerCase())) {
//                click(shiftTypeOption);
//                break;
//            }
//        }
//        if (!filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
//            click(filterButton);
//    }

//    @Override
//    public void editTheOperatingHoursForLGInPopupWinodw(List<String> weekDaysToClose) throws Exception {
//        if (isElementLoaded(operatingHoursEditBtn, 10)) {
//            clickTheElement(operatingHoursEditBtn);
//            if (isElementLoaded(operatingHoursCancelBtn, 10) && isElementLoaded(operatingHoursSaveBtn, 10)) {
//                SimpleUtils.pass("Click on Operating Hours Edit button Successfully!");
//                if (areListElementVisible(operatingHoursDayLists, 15)) {
//                    for (WebElement dayList : operatingHoursDayLists) {
//                        WebElement weekDay = dayList.findElement(By.cssSelector(".operating-hours-day-list-item-day"));
//                        if (weekDay != null) {
//                            WebElement checkbox = dayList.findElement(By.cssSelector("input[type=\"checkbox\"]"));
//                            if (!weekDaysToClose.contains(weekDay.getText())) {
//                                if (checkbox.getAttribute("class").contains("ng-empty")) {
//                                    clickTheElement(checkbox);
//                                }
//                                String[] operatingHours = propertyOperatingHours.get(weekDay.getText()).split("-");
//                                List<WebElement> startNEndTimes = dayList.findElements(By.cssSelector("[ng-if*=\"day.isOpened\"] input"));
//                                startNEndTimes.get(0).clear();
//                                startNEndTimes.get(1).clear();
//                                startNEndTimes.get(0).sendKeys(operatingHours[0].trim());
//                                startNEndTimes.get(1).sendKeys(operatingHours[1].trim());
//                            } else {
//                                if (!checkbox.getAttribute("class").contains("ng-empty")) {
//                                    clickTheElement(checkbox);
//                                }
//                            }
//                        } else {
//                            SimpleUtils.fail("Failed to find weekday element!", false);
//                        }
//                    }
//                    clickTheElement(operatingHoursSaveBtn);
//                    if (isElementEnabled(operatingHoursEditBtn, 15)) {
//                        SimpleUtils.pass("Create Schedule: Save the operating hours Successfully!");
//                    }else {
//                        SimpleUtils.fail("Create Schedule: Click on Save the operating hours button failed, Next button is not enabled!", false);
//                    }
//                }
//            }else {
//                SimpleUtils.fail("Click on Operating Hours Edit button failed!", false);
//            }
//        }else {
//            SimpleUtils.fail("Operating Hours Edit button not loaded Successfully!", false);
//        }
//    }
//
//    @Override
//    public String getTheShiftInfoByIndexInDayview(int index) throws Exception {
//        String shiftInfo = "";
//        if (areListElementVisible(dayViewAvailableShifts, 20) && index < dayViewAvailableShifts.size()) {
//            shiftInfo = dayViewAvailableShifts.get(index).getText();
//        } else {
//            SimpleUtils.fail("Schedule Page: week shifts not loaded successfully!", false);
//        }
//        return shiftInfo;
//    }

//    public WebElement clickOnProfileIconOfOpenShift() throws Exception {
//        WebElement selectedShift = null;
//        if(isProfileIconsEnable()&& areListElementVisible(shifts, 10)) {
//            int i;
//            for (i=0; i<profileIcons.size(); i++){
//                if (profileIcons.get(i).getAttribute("src").contains("openShiftImage")){
//                    break;
//                }
//            }
//            clickTheElement(profileIcons.get(i));
//            selectedShift = shifts.get(i);
//        } else if (areListElementVisible(scheduleTableWeekViewWorkerDetail, 10) && areListElementVisible(dayViewAvailableShifts, 10)) {
//            int i;
//            for (i=0; i<dayViewAvailableShifts.size(); i++){
//                if (dayViewAvailableShifts.get(i).findElement(By.className("sch-day-view-shift-worker-name")).getText().contains("Open")){
//                    break;
//                }
//            }
//            clickTheElement(scheduleTableWeekViewWorkerDetail.get(i));
//            selectedShift = dayViewAvailableShifts.get(i);
//        } else {
//            SimpleUtils.fail("Can't Click on Profile Icon due to unavailability ",false);
//        }
//        return selectedShift;
//    }

//    @Override
//    public WebElement clickOnProfileOfUnassignedShift() throws Exception {
//        WebElement selectedShift = null;
//        if(isProfileIconsEnable()&& areListElementVisible(shifts, 10)) {
//            int randomIndex = (new Random()).nextInt(profileIcons.size());
//            while (!profileIcons.get(randomIndex).getAttribute("src").contains("unassignedShiftImage")){
//                randomIndex = (new Random()).nextInt(profileIcons.size());
//            }
//            clickTheElement(profileIcons.get(randomIndex));
//            selectedShift = shifts.get(randomIndex);
//        } else if (areListElementVisible(scheduleTableWeekViewWorkerDetail, 10) && areListElementVisible(dayViewAvailableShifts, 10)) {
//            int randomIndex = (new Random()).nextInt(scheduleTableWeekViewWorkerDetail.size());
//            while (!dayViewAvailableShifts.get(randomIndex).findElement(By.className("sch-day-view-shift-worker-name")).getText().contains("Open")){
//                randomIndex = (new Random()).nextInt(scheduleTableWeekViewWorkerDetail.size());
//            }
//            clickTheElement(scheduleTableWeekViewWorkerDetail.get(randomIndex));
//            selectedShift = dayViewAvailableShifts.get(randomIndex);
//        } else {
//            SimpleUtils.fail("Can't Click on Profile Icon due to unavailability ",false);
//        }
//        return selectedShift;
//    }

//    public WebElement clickOnProfileIconOfShiftInDayView(String openOrNot) throws Exception {
//        WebElement selectedShift = null;
//        if(isProfileIconsEnable()&& areListElementVisible(dayViewAvailableShifts, 10)) {
//            if (openOrNot.toLowerCase().contains("open")){
//                int randomIndex = (new Random()).nextInt(profileIconsInDayView.size());
//                while (!dayViewAvailableShifts.get(randomIndex).findElement(By.cssSelector(".sch-day-view-shift-worker-name")).getText().toLowerCase().contains("open")){
//                    randomIndex = (new Random()).nextInt(dayViewAvailableShifts.size());
//                }
//                clickTheElement(profileIconsInDayView.get(randomIndex));
//                selectedShift = dayViewAvailableShifts.get(randomIndex);
//            } else {
//                int randomIndex = (new Random()).nextInt(profileIconsInDayView.size());
//                while (dayViewAvailableShifts.get(randomIndex).findElement(By.cssSelector(".sch-day-view-shift-worker-name")).getText().toLowerCase().contains("open")){
//                    randomIndex = (new Random()).nextInt(profileIconsInDayView.size());
//                }
//                clickTheElement(profileIconsInDayView.get(randomIndex));
//                selectedShift = dayViewAvailableShifts.get(randomIndex);
//            }
//        } else {
//            SimpleUtils.fail("Can't Click on Profile Icon due to unavailability ",false);
//        }
//        return selectedShift;
//    }

//    @Override
//    public void closeViewStatusContainer() throws Exception{
//        if(isElementEnabled(closeViewStatusBtn,5)){
//            click(closeViewStatusBtn);
//            SimpleUtils.pass("Close button is available and clicked");
//        }
//        else {
//            SimpleUtils.fail("Close Button is not enabled ", true);
//        }
//
//    }

//    public void changeWorkRoleInPromptOfAShift(boolean isApplyChange, WebElement shift) throws Exception {
//        WebElement clickedShift = shift;
//        clickOnChangeRole();
//        if(isElementEnabled(schWorkerInfoPrompt,5)) {
//            SimpleUtils.pass("Various Work Role Prompt is displayed ");
//            String newSelectedWorkRoleName = null;
//            String originSelectedWorkRoleName = null;
//            if (areListElementVisible(shiftRoleList, 5) && shiftRoleList.size() > 0) {
//                if (shiftRoleList.size() == 1) {
//                    SimpleUtils.pass("There is only one Work Role in Work Role list ");
//                    return;
//                } else {
//                    for (WebElement shiftRole : shiftRoleList) {
//                        if (shiftRole.getAttribute("class").contains("sch-worker-change-role-body-selected")) {
//                            originSelectedWorkRoleName = shiftRole.findElement(By.cssSelector("span.sch-worker-change-role-name")).getText();
//                            SimpleUtils.pass("The original selected Role is '" + originSelectedWorkRoleName);
//                            break;
//                        }
//                    }
//                    for (WebElement shiftRole : shiftRoleList) {
//                        if (!shiftRole.getAttribute("class").contains("sch-worker-change-role-body-selected")) {
//                            click(shiftRole);
//                            newSelectedWorkRoleName = shiftRole.findElement(By.cssSelector("span.sch-worker-change-role-name")).getText();
//                            SimpleUtils.pass("Role '" + newSelectedWorkRoleName + "' is selected!");
//                            break;
//                        }
//                    }
//
//                }
//            } else {
//                SimpleUtils.fail("Work roles are doesn't show well ", true);
//            }
//
//            if (isElementEnabled(applyButtonChangeRole, 5) && isElementEnabled(cancelButtonChangeRole, 5)) {
//                SimpleUtils.pass("Apply and Cancel buttons are enabled");
//                if (isApplyChange) {
//                    click(applyButtonChangeRole);
//                    if (isElementEnabled(roleViolationAlter, 5)) {
//                        click(roleViolationAlterOkButton);
//                    }
//                    //to close the popup
//                    waitForSeconds(5);
//                    clickTheElement(clickedShift);
//
//                    clickTheElement(clickedShift.findElement(By.cssSelector(".rows .worker-image-optimized img")));
//                    SimpleUtils.pass("Apply button has been clicked ");
//                } else {
//                    click(cancelButtonChangeRole);
//                    SimpleUtils.pass("Cancel button has been clicked ");
//                }
//            } else {
//                SimpleUtils.fail("Apply and Cancel buttons are doesn't show well ", true);
//            }
//
//            //check the shift role
//            if (!isElementEnabled(changeRole, 5)) {
//                click(clickedShift.findElement(By.cssSelector(".rows .worker-image-optimized img")));
//            }
//            clickOnChangeRole();
//            if (areListElementVisible(shiftRoleList, 5) && shiftRoleList.size() >1) {
//                for (WebElement shiftRole : shiftRoleList) {
//                    if (shiftRole.getAttribute("class").contains("sch-worker-change-role-body-selected")) {
//                        if (isApplyChange) {
//                            if (shiftRole.findElement(By.cssSelector("span.sch-worker-change-role-name")).getText().equals(newSelectedWorkRoleName)) {
//                                SimpleUtils.pass("Shift role been changed successfully ");
//                            } else {
//                                SimpleUtils.fail("Shift role failed to change ", true);
//                            }
//                        } else {
//                            if (shiftRole.findElement(By.cssSelector("span.sch-worker-change-role-name")).getText().equals(originSelectedWorkRoleName)) {
//                                SimpleUtils.pass("Shift role is not change ");
//                            } else {
//                                SimpleUtils.fail("Shift role still been changed when click Cancel button ", true);
//                            }
//                        }
//                        break;
//                    }
//                }
//            } else {
//                SimpleUtils.fail("Shift roles are doesn't show well ", true);
//            }
//            if(isElementLoaded(cancelButtonChangeRole, 5)){
//                click(cancelButtonChangeRole);
//            }
//        }
//    }
//
//    public void changeWorkRoleInPromptOfAShiftInDayView(boolean isApplyChange, String shiftid) throws Exception {
//        clickOnChangeRole();
//        if(isElementEnabled(schWorkerInfoPrompt,5)) {
//            SimpleUtils.pass("Various Work Role Prompt is displayed ");
//            String newSelectedWorkRoleName = null;
//            String originSelectedWorkRoleName = null;
//            if (areListElementVisible(shiftRoleList, 5) && shiftRoleList.size() > 0) {
//                if (shiftRoleList.size() == 1) {
//                    SimpleUtils.pass("There is only one Work Role in Work Role list ");
//                    return;
//                } else {
//                    for (WebElement shiftRole : shiftRoleList) {
//                        if (shiftRole.getAttribute("class").contains("sch-worker-change-role-body-selected")) {
//                            originSelectedWorkRoleName = shiftRole.findElement(By.cssSelector("span.sch-worker-change-role-name")).getText();
//                            SimpleUtils.pass("The original selected Role is '" + originSelectedWorkRoleName);
//                            break;
//                        }
//                    }
//                    for (WebElement shiftRole : shiftRoleList) {
//                        if (!shiftRole.getAttribute("class").contains("sch-worker-change-role-body-selected")) {
//                            click(shiftRole);
//                            newSelectedWorkRoleName = shiftRole.findElement(By.cssSelector("span.sch-worker-change-role-name")).getText();
//                            SimpleUtils.pass("Role '" + newSelectedWorkRoleName + "' is selected!");
//                            break;
//                        }
//                    }
//
//                }
//            } else {
//                SimpleUtils.fail("Work roles are doesn't show well ", true);
//            }
//
//            if (isElementEnabled(applyButtonChangeRole, 5) && isElementEnabled(cancelButtonChangeRole, 5)) {
//                SimpleUtils.pass("Apply and Cancel buttons are enabled");
//                if (isApplyChange) {
//                    click(applyButtonChangeRole);
//                    if (isElementEnabled(roleViolationAlter, 5)) {
//                        click(roleViolationAlterOkButton);
//                    }
//                    //to close the popup
//                    waitForSeconds(5);
//                    clickTheElement(getShiftById(shiftid));
//
//                    clickTheElement(getShiftById(shiftid).findElement(By.cssSelector(".sch-shift-worker-img-cursor")));
//                    SimpleUtils.pass("Apply button has been clicked ");
//                } else {
//                    click(cancelButtonChangeRole);
//                    SimpleUtils.pass("Cancel button has been clicked ");
//                }
//            } else {
//                SimpleUtils.fail("Apply and Cancel buttons are doesn't show well ", true);
//            }
//
//            //check the shift role
//            if (!isElementEnabled(changeRole, 5)) {
//                click(getShiftById(shiftid).findElement(By.cssSelector(".sch-shift-worker-img-cursor")));
//            }
//            clickOnChangeRole();
//            if (areListElementVisible(shiftRoleList, 5) && shiftRoleList.size() >1) {
//                for (WebElement shiftRole : shiftRoleList) {
//                    if (shiftRole.getAttribute("class").contains("sch-worker-change-role-body-selected")) {
//                        if (isApplyChange) {
//                            if (shiftRole.findElement(By.cssSelector("span.sch-worker-change-role-name")).getText().equals(newSelectedWorkRoleName)) {
//                                SimpleUtils.pass("Shift role been changed successfully ");
//                            } else {
//                                SimpleUtils.fail("Shift role failed to change ", true);
//                            }
//                        } else {
//                            if (shiftRole.findElement(By.cssSelector("span.sch-worker-change-role-name")).getText().equals(originSelectedWorkRoleName)) {
//                                SimpleUtils.pass("Shift role is not change ");
//                            } else {
//                                SimpleUtils.fail("Shift role still been changed when click Cancel button ", true);
//                            }
//                        }
//                        break;
//                    }
//                }
//            } else {
//                SimpleUtils.fail("Shift roles are doesn't show well ", true);
//            }
//            if(isElementLoaded(cancelButtonChangeRole, 5)){
//                click(cancelButtonChangeRole);
//            }
//        }
//    }
//
//    public void verifyEditMealBreakTimeFunctionalityForAShift(boolean isSavedChange, WebElement shift) throws Exception {
//        String mealBreakTimeBeforeEdit = null;
//        String mealBreakTimeAfterEdit = null;
//
//        WebElement selectedShift = shift;
//        clickOnEditMeaLBreakTime();
//        if (isMealBreakTimeWindowDisplayWell(true)) {
//            if (mealBreakBar.getAttribute("class").contains("disabled")) {
//                click(addMealBreakButton);
//                click(continueBtnInMealBreakButton);
//                if (isElementEnabled(confirmWindow, 5)) {
//                    click(okBtnOnConfirm);
//                }
//                click(selectedShift.findElement(By.cssSelector(".rows .worker-image-optimized img")));
//                clickOnEditMeaLBreakTime();
//            }
//            mealBreakTimeBeforeEdit = mealBreakTimes.get(0).getText();
//            moveDayViewCards(mealBreaks.get(0), 40);
//            mealBreakTimeAfterEdit = mealBreakTimes.get(0).getText();
//            if (isSavedChange) {
//                click(continueBtnInMealBreakButton);
//                if (isElementEnabled(confirmWindow, 5)) {
//                    click(okBtnOnConfirm);
//                }
//            } else {
//                click(cannelBtnInMealBreakButton);
//            }
//        }else
//            SimpleUtils.fail("Meal break window load failed",true);
//
//        click(selectedShift.findElement(By.cssSelector(".rows .worker-image-optimized img")));
//        clickOnEditMeaLBreakTime();
//        if (isMealBreakTimeWindowDisplayWell(true)) {
//            if (isSavedChange) {
//                if (mealBreakTimes.get(0).getText().equals(mealBreakTimeAfterEdit)) {
//                    SimpleUtils.pass("Edit meal break times successfully");
//                } else
//                    SimpleUtils.fail("Edit meal break time failed",true);
//            } else {
//                if (mealBreakTimes.get(0).getText().equals(mealBreakTimeBeforeEdit)) {
//                    SimpleUtils.pass("Edit meal break times not been changed after click Cancel button");
//                } else
//                    SimpleUtils.fail("Edit meal break times still been changed after click Cancel button",true);
//            }
//        }else
//            SimpleUtils.fail("Meal break window load failed",true);
//        click(cannelBtnInMealBreakButton);
//    }
//
//    public void verifyEditMealBreakTimeFunctionalityForAShiftInDayView(boolean isSavedChange, String shiftid) throws Exception {
//        String mealBreakTimeBeforeEdit = null;
//        String mealBreakTimeAfterEdit = null;
//
//        WebElement selectedShift = getShiftById(shiftid);
//        clickOnEditMeaLBreakTime();
//        if (isMealBreakTimeWindowDisplayWell(true)) {
//            if (mealBreakBar.getAttribute("class").contains("disabled")) {
//                click(addMealBreakButton);
//                click(continueBtnInMealBreakButton);
//                if (isElementEnabled(confirmWindow, 5)) {
//                    click(okBtnOnConfirm);
//                }
//                click(getShiftById(shiftid).findElement(By.cssSelector(".sch-shift-worker-img-cursor")));
//                clickOnEditMeaLBreakTime();
//            }
//            mealBreakTimeBeforeEdit = mealBreakTimes.get(0).getText();
//            moveDayViewCards(mealBreaks.get(0), 40);
//            mealBreakTimeAfterEdit = mealBreakTimes.get(0).getText();
//            if (isSavedChange) {
//                click(continueBtnInMealBreakButton);
//                if (isElementEnabled(confirmWindow, 5)) {
//                    click(okBtnOnConfirm);
//                }
//            } else {
//                click(cannelBtnInMealBreakButton);
//            }
//        }else
//            SimpleUtils.fail("Meal break window load failed",true);
//
//        click(getShiftById(shiftid).findElement(By.cssSelector(".sch-shift-worker-img-cursor")));
//        clickOnEditMeaLBreakTime();
//        if (isMealBreakTimeWindowDisplayWell(true)) {
//            if (isSavedChange) {
//                if (mealBreakTimes.get(0).getText().equals(mealBreakTimeAfterEdit)) {
//                    SimpleUtils.pass("Edit meal break times successfully");
//                } else
//                    SimpleUtils.fail("Edit meal break time failed",true);
//            } else {
//                if (mealBreakTimes.get(0).getText().equals(mealBreakTimeBeforeEdit)) {
//                    SimpleUtils.pass("Edit meal break times not been changed after click Cancel button");
//                } else
//                    SimpleUtils.fail("Edit meal break times still been changed after click Cancel button",true);
//            }
//        }else
//            SimpleUtils.fail("Meal break window load failed",true);
//        click(cannelBtnInMealBreakButton);
//    }

//    public int getShiftIndexById(String id) throws Exception {
//        waitForSeconds(5);
//        WebElement shift = null;
//        int index = 0;
//        if (id != null && !id.equals("")) {
//            String css = "[data-shift-id=\""+ id+"\"]";
//            shift = MyThreadLocal.getDriver().findElement(By.cssSelector(css));
//            if (isElementLoaded(shift, 5) && areListElementVisible(weekShifts,10)) {
//                for (WebElement element: weekShifts){
//                    if (element.getText().equalsIgnoreCase(shift.getText())){
//                        return index;
//                    }
//                    index++;
//                }
//            } else if (isElementLoaded(shift, 5) && areListElementVisible(dayViewAvailableShifts,10)) {
//                for (WebElement element: dayViewAvailableShifts){
//                    if (element.getText().equalsIgnoreCase(shift.getText())){
//                        return index;
//                    }
//                    index++;
//                }
//            }else{
//                SimpleUtils.fail("Cannot find shift by the id !",false);
//            }
//        } else {
//            SimpleUtils.fail("The shift id is null or empty!",false);
//        }
//        return index;
//    }
//
//    @FindBy(css = ".modal-dialog .lg-picker-input")
//    private WebElement locationInput;
//    @FindBy(css = ".modal-dialog input[placeholder=\"Search Location\"]")
//    private WebElement searchInputForLocationInPopupWindow;
//    @FindBy (css = ".modal-dialog .lg-picker-input__wrapper.lg-ng-animate.ng-hide .input-faked")
//    private WebElement selectedLocationOnCreateScheduleWindow;
//    @Override
//    public void chooseLocationInCreateSchedulePopupWindow(String location) throws Exception{
//        if (isElementLoaded(locationInput, 60)){
//            click(locationInput);
//            if(areListElementVisible(locationsInLocationSelectorOnUngenerateSchedulePage, 5)
//                    && locationsInLocationSelectorOnUngenerateSchedulePage.size() >0){
//                if (location == null){
//                    int randomLocations = (new Random()).nextInt(locationsInLocationSelectorOnUngenerateSchedulePage.size());
//                    location = locationsInLocationSelectorOnUngenerateSchedulePage.get(randomLocations).getText();
//                }
//
//                if(isElementLoaded(searchInputForLocationInPopupWindow, 5)){
//                    searchInputForLocationInPopupWindow.sendKeys(location);
//                }
//                waitForSeconds(3);
//                click(locationsInLocationSelectorOnUngenerateSchedulePage.get(0));
//                Actions actions = new Actions(getDriver());
//                actions.moveByOffset(0, 0).click().build().perform();
//                if(selectedLocationOnCreateScheduleWindow.getAttribute("innerHTML").replace("\n","").trim().equalsIgnoreCase(location)){
//                    SimpleUtils.pass("Create schedule window: Select locations on Edit Operating hours successfully! ");
//                } else
//                    SimpleUtils.fail("Create schedule window: Select locations on Edit Operating hours failed! ", false);
//
//            } else {
//                SimpleUtils.fail("Create schedule window: Locations fail to list! ", false);
//            }
//        } else
//            SimpleUtils.fail("location input in popup window fail to load! ", false);
//    }

//    @FindBy(css = " [ng-if=\"isLocationGroup()\"] [ng-click=\"selectChoice($event, choice)\"]")
//    private List<WebElement> listLocationGroup;
//
//    public List<String> getAllLocationGroupLocationsFromCreateShiftWindow() throws Exception{
//        if (isElementLoaded(btnChildLocation, 20)) {
//            click(btnChildLocation);
//            SimpleUtils.pass("Child location button clicked Successfully");
//        } else {
//            SimpleUtils.fail("Child location button is not clickable", false);
//        }
//        List<String> locationGroupLocations = new ArrayList<>();
//        if(areListElementVisible(listLocationGroup, 10) && listLocationGroup.size()>0){
//            for (WebElement location: listLocationGroup){
//                locationGroupLocations.add(location.getText());
//            }
//            SimpleUtils.pass("Get location group locations from create shift window successfully! ");
//        }else
//            SimpleUtils.fail("Location group dropdown loaded fail! ", false);
//
//        //close the dropdown list
//        click(btnChildLocation);
//        return locationGroupLocations;
//    }


    @FindBy (css = "[ng-if=\"(scheduleSmartCard.unassignedShifts || scheduleSmartCard.outsideOperatingHoursShifts) && hasSchedule()\"] .card-carousel-card")
    private WebElement requiredActionSmartCard;
//    @FindBy (css = "[ng-click=\"smartCardShiftFilter('Requires Action')\"]")
//    private WebElement viewShiftsBtnOnRequiredActionSmartCard;
    @FindBy (css = ".lg-filter__clear")
    private WebElement clearFilterOnFilterDropdownPopup;
//
//    public boolean isRequiredActionSmartCardLoaded() throws Exception {
//        if (isElementLoaded(requiredActionSmartCard, 5)) {
//            return true;
//        } else
//            return false;
//    }
//
//    public void clickOnViewShiftsBtnOnRequiredActionSmartCard() throws Exception {
//        if (isElementLoaded(requiredActionSmartCard, 5) && isElementLoaded(viewShiftsBtnOnRequiredActionSmartCard, 5)) {
//            if (viewShiftsBtnOnRequiredActionSmartCard.getText().equalsIgnoreCase("View Shifts")){
//                click(viewShiftsBtnOnRequiredActionSmartCard);
//                SimpleUtils.pass("Click View Shifts button successfully! ");
//            } else if(viewShiftsBtnOnRequiredActionSmartCard.getText().equalsIgnoreCase("Clear Filter")){
//                SimpleUtils.report("View Shifts button alreay been clicked! ");
//            } else
//                SimpleUtils.fail("The button name on required action smart card display incorrectly! ", false);
//        } else
//            SimpleUtils.fail("The required action smard card or the view shifts button on it loaded fail! ", false);
//    }
//
//    public void clickOnClearShiftsBtnOnRequiredActionSmartCard() throws Exception {
//        if (isElementLoaded(requiredActionSmartCard, 5) && isElementLoaded(viewShiftsBtnOnRequiredActionSmartCard, 5)) {
//            if (viewShiftsBtnOnRequiredActionSmartCard.getText().equalsIgnoreCase("Clear Filter")){
//                click(viewShiftsBtnOnRequiredActionSmartCard);
//                SimpleUtils.pass("Click Clear Filter button successfully! ");
//            } else if(viewShiftsBtnOnRequiredActionSmartCard.getText().equalsIgnoreCase("View Shifts")){
//                SimpleUtils.report("Clear Filter button alreay been clicked! ");
//            } else
//                SimpleUtils.fail("The button name on required action smart card display incorrectly! ", false);
//        } else
//            SimpleUtils.fail("The required action smard card or the view shifts button on it loaded fail! ", false);
//    }
//
//    public void clickOnClearFilterOnFilterDropdownPopup() throws Exception {
//        if(isElementLoaded(clearFilterOnFilterDropdownPopup, 5)){
//            if(clearFilterOnFilterDropdownPopup.getAttribute("class").contains("active")){
//                scrollToElement(clearFilterOnFilterDropdownPopup);
//                click(clearFilterOnFilterDropdownPopup);
//                SimpleUtils.pass("Click Clear Filter button on Filter dropdown popup successfully! ");
//            } else
//                SimpleUtils.report("Clear filter button is disabled because there is no filters been selected! ");
//        } else
//            SimpleUtils.fail("Clear Filter button loaded fail! ", false);
//    }


//    @Override
//    public List<WebElement> getAllShiftsOfOneTM(String name) throws Exception{
//        List<WebElement> allShifts = new ArrayList<>();
//        if (areListElementVisible(shiftsWeekView, 15)) {
//            for (WebElement shiftWeekView : shiftsWeekView) {
//                WebElement workerName = null;
//                if(isScheduleDayViewActive()){
//                    workerName = shiftWeekView.findElement(By.className("sch-day-view-shift-worker-name"));
//                } else
//                    workerName = shiftWeekView.findElement(By.className("week-schedule-worker-name"));
//                if (workerName != null && workerName.getText().toLowerCase().contains(name)) {
//                    allShifts.add(shiftWeekView);
//                }
//            }
//        }else
//            SimpleUtils.fail("Schedule Week View: shifts load failed or there is no shift in this week", false);
//        return allShifts;
//    }
//
//
//    @Override
//    public String getWholeMessageFromActionRequiredSmartCard() throws Exception {
//        String message = "";
//        if (isElementLoaded(requiredActionSmartCard, 5)) {
//            message = requiredActionSmartCard.getText();
//        } else
//            SimpleUtils.fail("Required Action smart card fail to load! ", false);
//        return message;
//    }

//
//
//    @FindBy (css = "[ng-if=\"scheduleSmartCard.unassignedShifts && scheduleSmartCard.outsideOperatingHoursShifts\"] .col-fx-1")
//    private List<WebElement> unassignedAndOOOHMessageOnActionRequiredSmartCard;
//
//    @FindBy (css = "[ng-if=\"!scheduleSmartCard.outsideOperatingHoursShifts\"]")
//    private WebElement unassignedMessageOnActionRequiredSmartCard;
//
//    @FindBy (css = "[ng-if=\"!scheduleSmartCard.unassignedShifts\"]")
//    private WebElement oOOHMessageOnActionRequiredSmartCard;
//
//    @Override
//    public HashMap<String, String> getMessageFromActionRequiredSmartCard() throws Exception {
//        HashMap<String, String> unassignedAndOOOHMessage = new HashMap<String, String>();
//        if (isElementLoaded(requiredActionSmartCard, 5)) {
//            if (areListElementVisible(unassignedAndOOOHMessageOnActionRequiredSmartCard, 5)) {
//                unassignedAndOOOHMessage.put("unassigned", unassignedAndOOOHMessageOnActionRequiredSmartCard.get(0).getText());
//                unassignedAndOOOHMessage.put("OOOH", unassignedAndOOOHMessageOnActionRequiredSmartCard.get(1).getText());
//            } else if (isElementLoaded(unassignedMessageOnActionRequiredSmartCard, 5)) {
//                unassignedAndOOOHMessage.put("unassigned", unassignedMessageOnActionRequiredSmartCard.getText());
//                unassignedAndOOOHMessage.put("OOOH", "");
//            } else if (isElementLoaded(oOOHMessageOnActionRequiredSmartCard, 5)) {
//                unassignedAndOOOHMessage.put("OOOH", oOOHMessageOnActionRequiredSmartCard.getText());
//                unassignedAndOOOHMessage.put("unassigned", "");
//            } else
//                SimpleUtils.fail("No available message display on Action Required smart card! ", false);
//        } else
//            SimpleUtils.fail("Required Action smart card fail to load! ", false);
//        return unassignedAndOOOHMessage;
//    }
//
//    @Override
//    public String getTooltipOfPublishButton() throws Exception {
//        String tooltip = "";
//        if (isElementLoaded(publishSheduleButton, 5)) {
//            tooltip = publishSheduleButton.getAttribute("data-tootik");
//        } else
//            SimpleUtils.fail("Publish schedule button fail to load! ", false);
//        return tooltip;
//    }
//
//    @FindBy(css = ".swap-modal.modal-instance")
//    private WebElement dragAndDropConfirmPage;
//    @Override
//    public boolean isDragAndDropConfirmPageLoaded() throws Exception {
//        boolean isConfirmPageDisplay = false;
//        if (isElementLoaded(dragAndDropConfirmPage,15) ){
//            isConfirmPageDisplay = true;
//            SimpleUtils.report("Drag and Drop confirm page is display!");
//        } else {
//            SimpleUtils.report("Drag and Drop confirm page is not display!");
//        }
//        return isConfirmPageDisplay;
//    }


//    @Override
//    public void deleteAllOOOHShiftInWeekView() throws Exception {
//
//        if (areListElementVisible(shiftsWeekView, 15) && isRequiredActionSmartCardLoaded()) {
//            clickOnViewShiftsBtnOnRequiredActionSmartCard();
//            for (WebElement shiftWeekView : shiftsWeekView) {
//                try {
//                    if (getComplianceMessageFromInfoIconPopup(shiftWeekView).contains("Outside Operating hours")) {
//                        WebElement image = shiftWeekView.findElement(By.cssSelector(".rows .week-view-shift-image-optimized span"));
//                        clickTheElement(image);
//                        waitForSeconds(3);
//                        if (isElementLoaded(deleteShift, 5)) {
//                            clickTheElement(deleteShift);
//                            if (isElementLoaded(deleteBtnInDeleteWindows, 10)) {
//                                click(deleteBtnInDeleteWindows);
//                                SimpleUtils.pass("Schedule Week View: OOOH shift been deleted successfully");
//                            } else
//                                SimpleUtils.fail("delete confirm button load failed", false);
//                        } else
//                            SimpleUtils.fail("delete item for this OOOH shift load failed", false);
//                    }
//                } catch (Exception e) {
//                    continue;
//                }
//            }
//            clickOnClearShiftsBtnOnRequiredActionSmartCard();
//        }else
//            SimpleUtils.report("Schedule Week View: there is no shifts or Action Required smart card in this week");
//    }
//
//    @Override
//    public void deleteAllShiftsInWeekView() throws Exception {
//
//        if (areListElementVisible(shiftsWeekView, 15)) {
//            for (WebElement shiftWeekView : shiftsWeekView) {
//                WebElement image = shiftWeekView.findElement(By.cssSelector(".rows .week-view-shift-image-optimized span"));
//                clickTheElement(image);
//                waitForSeconds(3);
//                if (isElementLoaded(deleteShift, 5)) {
//                    clickTheElement(deleteShift);
//                    if (isElementLoaded(deleteBtnInDeleteWindows, 10)) {
//                        click(deleteBtnInDeleteWindows);
//                        SimpleUtils.pass("Schedule Week View: OOOH shift been deleted successfully");
//                    } else
//                        SimpleUtils.fail("delete confirm button load failed", false);
//                } else
//                    SimpleUtils.fail("delete item for this OOOH shift load failed", false);
//            }
//        }else
//            SimpleUtils.report("Schedule Week View: there is no shifts or Action Required smart card in this week");
//    }


//    @Override
//    public List<WebElement> getAllOOOHShifts() throws Exception {
//        List<WebElement> allOOOHShifts = new ArrayList<>();
//        WebElement iIcon = null;
//        if (areListElementVisible(shiftsWeekView, 15)) {
//            for (WebElement shiftWeekView : shiftsWeekView) {
//                scrollToElement(shiftWeekView);
//                if(isScheduleDayViewActive()){
//                    iIcon = shiftWeekView.findElement(By.cssSelector("div.day-view-shift-hover-info-icon img"));
//                    waitForSeconds(2);
//                } else
//                    iIcon = shiftWeekView.findElement(By.cssSelector("img.week-schedule-shit-open-popover"));
//                if(iIcon.getAttribute("src").contains("danger")) {
//                    click(iIcon);
//                    if (isElementLoaded(popOverContent, 5)){
//                        if (areListElementVisible(complianceMessageInInfoIconPopup, 5) && complianceMessageInInfoIconPopup.size()>0){
//                            List<String> complianceMessages = new ArrayList<>();
//                            for (WebElement message: complianceMessageInInfoIconPopup){
//                                complianceMessages.add(message.getText());
//                            }
//                            if(complianceMessages.contains("Outside Operating hours")) {
//                                allOOOHShifts.add(shiftWeekView);
//                            }
//                        } else
//                            SimpleUtils.report("There is no compliance message in info icon popup");
//                    } else
//                        SimpleUtils.fail("Info icon popup fail to load", false);
//                }
//            }
//        }else
//            SimpleUtils.fail("Schedule Week View: shifts load failed or there is no shift in this week", false);
//        return allOOOHShifts;
//    }


//    public void moveSliderAtCertainPointOnEditShiftTimePage(String shiftTime, String startingPoint) throws Exception {
//        WebElement element = null;
//        String time = "am";
//        if(areListElementVisible(noUiValues, 15) && noUiValues.size() >0){
//            for (WebElement noUiValue: noUiValues){
//                if (noUiValue.getAttribute("class").contains("pm")) {
//                    time = "pm";
//                } else if (noUiValue.getAttribute("class").contains("am")){
//                    time = "am";
//                }
//                if (time.equalsIgnoreCase(shiftTime.substring(shiftTime.length() - 2))) {
//                    if(noUiValue.getText().equals(shiftTime.substring(0, shiftTime.length() - 2))){
//                        element = noUiValue;
//                        break;
//                    }
//                }
//            }
//        }
//        if (element == null){
//            SimpleUtils.fail("Cannot found the operating hour on edit operating hour page! ", false);
//        }
//        if(startingPoint.equalsIgnoreCase("End")){
//            if(isElementLoaded(shiftEndTimeButton,10)){
//                SimpleUtils.pass("Shift timings with Sliders loaded on page Successfully for End Point");
//                mouseHoverDragandDrop(shiftEndTimeButton,element);
//            } else{
//                SimpleUtils.fail("Shift timings with Sliders not loaded on page Successfully", false);
//            }
//        }else if(startingPoint.equalsIgnoreCase("Start")){
//            if(isElementLoaded(shiftStartTimeButton,10)){
//                SimpleUtils.pass("Shift timings with Sliders loaded on page Successfully for End Point");
//                mouseHoverDragandDrop(shiftStartTimeButton,element);
//            } else{
//                SimpleUtils.fail("Shift timings with Sliders not loaded on page Successfully", false);
//            }
//        }
//    }


//    public void editTheShiftTimeForSpecificShift(WebElement shift, String startTime, String endTime) throws Exception {
//        By isUnAssignedShift = null;
//        if (!isScheduleDayViewActive())
//            isUnAssignedShift = By.cssSelector(".rows .week-view-shift-image-optimized span");
//        else
//            isUnAssignedShift = By.cssSelector(".sch-shift-worker-initials span");
//        WebElement shiftPlusBtn = shift.findElement(isUnAssignedShift);
//        if (isElementLoaded(shiftPlusBtn)) {
//            click(shiftPlusBtn);
//            if (isElementLoaded(shiftPopover)) {
//                WebElement editShiftTimeOption = shiftPopover.findElement(By.cssSelector("[ng-if=\"canEditShiftTime && !isTmView()\"]"));
//                if (isElementLoaded(editShiftTimeOption)) {
//                    scrollToElement(editShiftTimeOption);
//                    click(editShiftTimeOption);
//                    if (isElementEnabled(editShiftTimePopUp, 5)) {
//                        moveSliderAtCertainPointOnEditShiftTimePage(startTime, "Start");
//                        moveSliderAtCertainPointOnEditShiftTimePage(endTime, "End");
//                        waitForSeconds(2);
//                        click(confirmBtnOnDragAndDropConfirmPage);
//                    } else {
//                        SimpleUtils.fail("Edit Shift Time PopUp window load failed", false);
//                    }
//                }
//            }
//        }
//    }
//
//    @FindBy(css = ".modal-dialog .sch-day-view-shift-outer")
//    private WebElement shiftInViewStatusWindow;
//    @Override
//    public String getViewStatusShiftsInfo() throws Exception {
//        String result = "";
//        if (isElementLoaded(shiftInViewStatusWindow, 5)) {
//            result = shiftInViewStatusWindow.getAttribute("innerText");
//        }
//        return result;
//    }

//
//    @Override
//    public void clickOnCloseButtonOnCustomizeShiftPage() throws Exception {
//        if (isElementLoaded(closeButtonOnCustomize, 5)) {
//            click(closeButtonOnCustomize);
//        } else
//            SimpleUtils.fail("The close button on custimize shift page fail to load! ", false);
//    }
//
//
//    @Override
//    public void verifyShiftTypeInLeft(boolean isLG) throws Exception {
//        if (isElementLoaded(filterPopup,5)) {
//            if (isLG) {
//                if (isElementLoaded(filterPopup,5)) {
//                    if (filterLabels.size() == 4 && filterLabels.get(1).getText().equals("SHIFT TYPE"))
//                        SimpleUtils.pass("Schedule Page: 'SHIFT TYPE' displays in left expect Location");
//                    else
//                        SimpleUtils.fail("Schedule Page: 'SHIFT TYPE' is not in the left expect Location",false);
//                } else
//                    SimpleUtils.fail("Schedule Page: The drop down list does not pop up",false);
//            } else {
//                if (isElementLoaded(filterPopup,5)) {
//                    if (filterLabels.size() == 3 && filterLabels.get(0).getText().equals("SHIFT TYPE"))
//                        SimpleUtils.pass("Schedule Page: 'SHIFT TYPE' displays in left");
//                    else
//                        SimpleUtils.fail("Schedule Page: 'SHIFT TYPE' is not in the left",false);
//                } else
//                    SimpleUtils.fail("Schedule Page: The drop down list does not pop up",false);
//            }
//        } else
//            SimpleUtils.fail("Schedule Page: The drop down list does not pop up",false);
//    }
//
//    @Override
//    public void verifyShiftTypeFilters() throws Exception {
//        if (isElementLoaded(filterPopup,5)) {
//            String shiftTypeFilterKey = "shifttype";
//            ArrayList<WebElement> shiftTypeFilters = getAvailableFilters().get(shiftTypeFilterKey);
//            if (shiftTypeFilters.size() >= 7) {
//                if (shiftTypeFilters.get(0).getText().contains("Action Required")
//                        && shiftTypeFilters.get(1).getText().contains("Assigned")
//                        && shiftTypeFilters.get(2).getText().contains("Compliance Review")
//                        && shiftTypeFilters.get(3).getText().contains("Open")
//                        && shiftTypeFilters.get(4).getText().contains("Unavailable")
//                        && shiftTypeFilters.get(5).getText().contains("Swap/Cover Requested")
//                        && shiftTypeFilters.get(6).getText().contains("Unpublished changes")
////                        && shiftTypeFilters.get(7).getText().contains("New or Borrowed TM")
////                        && (shiftTypeFilters.size()> 7? (shiftTypeFilters.get(7).getText().contains("Minor (14-15)") ||
////                        shiftTypeFilters.get(7).getText().contains("Minor (16-17)")): true)
//                ){
//                    SimpleUtils.pass("The shift types display correctly in Filter dropdown list! ");
//                } else
//                    SimpleUtils.fail("The shift types display incorrectly in Filter dropdown list! ", false);
//            } else
//                SimpleUtils.fail("The shift types count display incorrectly in Filter dropdown list! ", false);
//        } else
//            SimpleUtils.fail("Schedule Page: The drop down list does not pop up",false);
//    }

//
//    @FindBy(css = "label.input-label.ng-binding")
//    private List<WebElement> allFilterText;
//
//    public int getSpecificFiltersCount (String filterText) throws Exception {
//        int count = 0;
//        if (areListElementVisible(allFilterText, 10) && allFilterText.size()>0){
//            for (WebElement filter: allFilterText){
//                String [] fullText= filter.getText().split("\\(");
//                String filterName = fullText[0].trim();
//                String filterCount = fullText[1].replace(")", "");
//                if (filterName.equalsIgnoreCase(filterText)) {
//                    count = Integer.parseInt(filterCount);
//                }
//            }
//        } else
//            SimpleUtils.fail("Filter text in schedule filter dropdown list fail to load! ", false);
//        return count;
//    }

//    @Override
//    public void deleteMealBreakForOneShift(WebElement shift) throws Exception {
//        click(shift.findElement(By.cssSelector(".rows .worker-image-optimized img")));
//        clickOnEditMeaLBreakTime();
//
//        if (areListElementVisible(deleteMealBreakButtons, 5)) {
//            while(deleteMealBreakButtons.size()>0){
//                click(deleteMealBreakButtons.get(0));
//            }
//
//            SimpleUtils.pass("Delete meal break times successfully");
//        } else {
//            SimpleUtils.report("Delete meal break fail to load! ");
//        }
//        click(continueBtnInMealBreakButton);
//        if (isElementEnabled(confirmWindow, 5)) {
//            click(okBtnOnConfirm);
//        }
//    }
//
//    @Override
//    public List<WebElement> getShiftsByNameOnDayView(String name) throws Exception {
//        int count = 0;
//        List<WebElement> shiftsOfOneTM = new ArrayList<>();
//        if (areListElementVisible(dayViewAvailableShifts, 5) && dayViewAvailableShifts != null && dayViewAvailableShifts.size() > 0) {
//            for (WebElement shift : dayViewAvailableShifts) {
//                WebElement name1 = shift.findElement(By.className("sch-day-view-shift-worker-name"));
//                if (name1 != null && name1.getText().contains(name)) {
//                    shiftsOfOneTM.add(shift);
//                    SimpleUtils.pass("shift exists on this day!");
//                    count++;
//                }
//            }
//            if(count==0){
//                SimpleUtils.report("No shifts on the day for the TM: " + name);
//            }
//        } else {
//            SimpleUtils.fail("No shifts on the day",false);
//        }
//        return shiftsOfOneTM;
//    }

    @FindBy (className = "week-schedule-shift-title")
    private List<WebElement> weekScheduleShiftTitles;

    @FindBy (className = "sch-group-label")
    private List<WebElement> dayScheduleGroupLabels;

    @FindBy(css = "[src=\"img/legion/edit/deleted-shift-week.png\"]")
    private List<WebElement> deleteShiftImgsInWeekView;

    @FindBy(css = "img[ng-src=\"img/legion/edit/deleted-shift-day@2x.png\"]")
    private List<WebElement> deleteShiftImgsInDayView;
//
//    @Override
//    public boolean isGroupByDayPartsLoaded() throws Exception {
//        Select groupBySelectElement = new Select(scheduleGroupByButton);
//        List<WebElement> scheduleGroupByButtonOptions = groupBySelectElement.getOptions();
//         for (WebElement scheduleGroupByButtonOption: scheduleGroupByButtonOptions) {
//            if (scheduleGroupByButtonOption.getText().contains("Group by Day Parts"))
//                return true;
//        }
//        return false;
//    }
//
//    @Override
//    public List<String> getWeekScheduleShiftTitles() throws Exception {
//        List<String> weekShiftTitles = new ArrayList<>();
//        if (areListElementVisible(weekScheduleShiftTitles, 10)) {
//            for (WebElement title: weekScheduleShiftTitles) {
//                weekShiftTitles.add(title.getText());
//            }
//        }
//        return weekShiftTitles;
//    }
//
//    @Override
//    public List<String> getDayScheduleGroupLabels() throws Exception {
//        List<String> dayGroupLabels = new ArrayList<>();
//        if (areListElementVisible(dayScheduleGroupLabels, 10)) {
//            for (WebElement label: dayScheduleGroupLabels) {
//                dayGroupLabels.add(label.getText());
//            }
//        }
//        return dayGroupLabels;
//    }
//
//    @Override
//    public boolean isShiftInDayPartOrNotInWeekView(int shiftIndex, String dayPart) throws Exception {
//        boolean isIn = false;
//        int index1 = -1;
//        int index2 = -1;
//        for (int i = 0; i < weekScheduleShiftTitles.size(); i++) {
//                if (weekScheduleShiftTitles.get(i).getText().equals(dayPart)) {
//                    for (int k = 7; k < 13; k++) {
//                        try {
//                            WebElement nextShift = weekScheduleShiftTitles.get(i).findElement(By.xpath("./../../following-sibling::div[" + k + "]/div"));
//                            if (isElementLoaded(nextShift,10)) {
//                                index1 = getTheIndexOfShift(nextShift);
//                                break;
//                            }
//                        } catch (NoSuchElementException e) {
//                            continue;
//                        }
//                    }
//                    if (i != weekScheduleShiftTitles.size() - 1) {
//                        for (int j = 7; j < 13; j++) {
//                            try {
//                                WebElement nextShift = weekScheduleShiftTitles.get(i + 1).findElement(By.xpath("./../../following-sibling::div[" + j + "]/div"));
//                                if (isElementLoaded(nextShift,10)) {
//                                    index2 = getTheIndexOfShift(nextShift);
//                                    break;
//                                }
//                            } catch (NoSuchElementException e) {
//                                continue;
//                            }
//                        }
//                    } else
//                        index2 = weekShifts.size() - 1;
//                    if (shiftIndex >= index1 && shiftIndex <= index2) {
//                        isIn = true;
//                    }
//                    break;
//                }
//            }
//        return isIn;
//    }
//
//    @Override
//    public boolean isShiftInDayPartOrNotInDayView(int shiftIndex, String dayPart) throws Exception {
//        boolean isIn = false;
//        int index2 = -1;
//        for (int i = 0; i < dayScheduleGroupLabels.size(); i++) {
//            if (dayScheduleGroupLabels.get(i).getText().equals(dayPart)) {
//                int index1 = getTheIndexOfShift(dayScheduleGroupLabels.get(i).findElement(By.xpath("./../../following-sibling::div[1]//worker-shift/div")));
//                if (i != dayScheduleGroupLabels.size() - 1)
//                    index2 = getTheIndexOfShift(dayScheduleGroupLabels.get(i+1).findElement(By.xpath("./../../following-sibling::div[1]//worker-shift/div")));
//                else
//                    index2 = shiftsInDayView.size() - 1;
//                if (shiftIndex >= index1 && shiftIndex <= index2) {
//                    isIn = true;
//                    break;
//                }
//            }
//        }
//        return isIn;
//    }
//
//    @Override
//    public int getTheIndexOfShift(WebElement shift) throws Exception {
//        int index = -1;
//        if (areListElementVisible(weekShifts, 10)) {
//            for (int i = 0; i < weekShifts.size(); i++) {
//                try {
//                    if (weekShifts.get(i).equals(shift)) {
//                        index = i;
//                        SimpleUtils.pass("Schedule Week View: Get the index of the shift successfully: " + i);
//                        break;
//                    }
//
//                } catch (NoSuchElementException e) {
//                    continue;
//                }
//            }
//        } else if (areListElementVisible(shiftsInDayView, 10)) {
//            for (int i = 0; i < shiftsInDayView.size(); i++) {
//                try {
//                    if (shiftsInDayView.get(i).equals(shift)) {
//                        index = i;
//                        SimpleUtils.pass("Schedule Day View: Get the index of the shift successfully: " + i);
//                        break;
//                    }
//
//                } catch (NoSuchElementException e) {
//                    continue;
//                }
//            }
//        } else {
//            SimpleUtils.fail("Schedule Week View: There are no shifts loaded!", false);
//        }
//        return index;
//    }
//
//    // added by Fiona
//    public List<String> verifyDaysHasShifts() throws Exception {
//        List<String> dayHasShifts = new ArrayList<String>();
//        if (areListElementVisible(scheduleDays, 10)) {
//            for(WebElement scheduleDay:scheduleDays){
//                String dayAbbr = scheduleDay.findElement(By.cssSelector("div.sch-calendar-day-label")).getText().trim();
//                String totalCalendarDaySummary = scheduleDay.findElement(By.cssSelector("div.sch-calendar-day-summary span")).getText().trim().split(" ")[0];
//                if(! totalCalendarDaySummary.equals("0")){
//                    dayHasShifts.add(dayAbbr);
//                    SimpleUtils.pass(dayAbbr + " has shifts!");
//                }
//            }
//        }
//        return dayHasShifts;
//    }
//
//    @FindBy(css=".ReactVirtualized__Grid__innerScrollContainer")
//    private WebElement shiftsTable;
//
//    @Override
//    public void verifyShiftTimeInReadMode(String index,String shiftTime) throws Exception{
//        String shiftTimeInShiftTable = null;
//        if (isElementEnabled(shiftsTable,5)) {
//            List<WebElement> shiftsTableList = shiftsTable.findElements(By.cssSelector("div[data-day-index=\"" + index + "\"].week-schedule-shift"));
//            for(WebElement shiftTable:shiftsTableList){
//                shiftTimeInShiftTable = shiftTable.findElement(By.cssSelector(".week-schedule-shift-time")).getText().trim();
//                if(shiftTimeInShiftTable.equals(shiftTime)){
//                    SimpleUtils.pass("The shift time on data-day-index: " + index + " is aligned with advance staffing rule");
//                }else {
//                    SimpleUtils.fail("The shift time is NOT aligned with advance staffing rule",false);
//                }
//            }
//        }else{
//            SimpleUtils.fail("There is no shifts generated.",false);
//        }
//    }
//
//    @Override
//    public List<String> getIndexOfDaysHaveShifts() throws Exception {
//        List<String> index = new ArrayList<String>();
//        String dataDayIndex = null;
//        if (areListElementVisible(scheduleDays, 10)) {
//            for(WebElement scheduleDay:scheduleDays){
//                String totalCalendarDaySummary = scheduleDay.findElement(By.cssSelector("div.sch-calendar-day-summary span")).getText().trim().split(" ")[0];
//                if(! totalCalendarDaySummary.equals("0")){
//                    dataDayIndex = scheduleDay.getAttribute("data-day-index").trim();
//                    index.add(dataDayIndex);
//                }
//            }
//        } else {
//            SimpleUtils.fail("Table header fail to load!", false);
//        }
//        return index;
//    }

//    @Override
//    public void deleteAllShiftsOfGivenDayPartInWeekView(String dayPart) throws Exception {
//        boolean isFound = false;
//        for (int i = 0; i < weekScheduleShiftTitles.size(); i++) {
//            if (weekScheduleShiftTitles.get(i).getText().equals(dayPart)) {
//                isFound = true;
//                if (i == weekScheduleShiftTitles.size() - 1) {
//                    List<WebElement> shifts = weekScheduleShiftTitles.get(i).findElements(By.xpath("./../../following-sibling::div//div[@class=\"rows\"]//span/span"));
//                    int count1 = shifts.size();
//                    for (int j = 0; j < count1; j++) {
//                        clickTheElement(shifts.get(j));
//                        if (isPopOverLayoutLoaded()) {
//                            clickTheElement(deleteShift);
//                            if (isDeleteShiftShowWell())
//                                click(deleteBtnInDeleteWindows);
//                        }
//                    }
//                    int count2 = deleteShiftImgsInWeekView.size();
//                    if (count1 == count2)
//                        SimpleUtils.pass("Schedule Page: Delete all the shifts in '" + dayPart + "' in week view successfully");
//                    else
//                        SimpleUtils.fail("Schedule Page: Failed to delete all the shifts in '\" + dayPart + \"' in week view",false);
//                } else {
//                    List<WebElement> shifts = weekScheduleShiftTitles.get(i).findElements(By.xpath("./../../following-sibling::div//div[@class=\"rows\"]//span/span"));
//                    List<WebElement> shiftsOfNextDayPart = weekScheduleShiftTitles.get(i + 1).findElements(By.xpath("./../../following-sibling::div//div[@class=\"rows\"]//span/span"));
//                    int count1 = shifts.size() - shiftsOfNextDayPart.size();
//                    for (int j = 0; j < count1; j++) {
//                        clickTheElement(shifts.get(j));
//                        if (isPopOverLayoutLoaded()) {
//                            clickTheElement(deleteShift);
//                            if (isDeleteShiftShowWell())
//                                click(deleteBtnInDeleteWindows);
//                        }
//                    }
//                    int count2 = deleteShiftImgsInWeekView.size();
//                    if (count1 == count2)
//                        SimpleUtils.pass("Schedule Page: Delete all the shifts in '" + dayPart + "' in week view successfully");
//                    else
//                        SimpleUtils.fail("Schedule Page: Failed to delete all the shifts in '" + dayPart + "' in week view",false);
//                }
//                break;
//            }
//        }
//        if (!isFound)
//            SimpleUtils.report("Schedule Page: Not find the given day part in week view");
//    }

//    @Override
//    public void deleteAllShiftsOfGivenDayPartInDayView(String dayPart) throws Exception {
//        boolean isFound = false;
//        int count1 = 0;
//        for (int i = 0; i < dayScheduleGroupLabels.size(); i++) {
//            if (dayScheduleGroupLabels.get(i).getText().equals(dayPart)) {
//                isFound = true;
//                for (int j = 0; j < dayScheduleGroupLabels.get(i).findElements(By.xpath("./../../following-sibling::div//worker-detail/div")).size(); j++) {
//                    List<WebElement> shifts = dayScheduleGroupLabels.get(i).findElements(By.xpath("./../../following-sibling::div//worker-detail/div"));
//                    count1 = shifts.size();
//                    click(shifts.get(j));
//                    if (isPopOverLayoutLoaded()) {
//                        clickTheElement(deleteShift);
//                        if (isDeleteShiftShowWell())
//                            click(deleteBtnInDeleteWindows);
//                    }
//                }
//                int count2 = deleteShiftImgsInDayView.size();
//                if (count1 == count2)
//                    SimpleUtils.pass("Schedule Page: Delete all the shifts in '" + dayPart + "' in day view successfully");
//                else
//                    SimpleUtils.fail("Schedule Page: Failed to delete all the shifts in '" + dayPart + "' in day view",false);
//            }
//            break;
//        }
//        if (!isFound)
//            SimpleUtils.report("Schedule Page: Not find the given day part in week view");
//    }
    
    @FindBy(css = "div.slider-section-description-break-time-item-rest")
    private List<WebElement> restBreakTimes;

//    @Override
//    public HashMap<String, String> getMealAndRestBreaksTime() throws Exception {
//        HashMap<String, String> mealAndRestBreaksTime = new HashMap<String, String>();
//        if (isElementEnabled(editMealBreakTitle,5)) {
//            for (WebElement mealBreakTime:mealBreakTimes){
//                String mealTime = mealBreakTime.getText().trim();
//                mealAndRestBreaksTime.put("Meal Break",mealTime);
//            }
//            for (WebElement restBreakTime:restBreakTimes){
//                String restTime = restBreakTime.getText().trim();
//                mealAndRestBreaksTime.put("Rest Break",restTime);
//            }
//        }else
//            SimpleUtils.report("Breaks edit page don't display");
//        return mealAndRestBreaksTime;
//    }
//
//    @FindBy(css = ".week-schedule-ribbon-group-toggle")
//    private List<WebElement> groupTitleList;
//    @Override
//    public void verifyGroupCanbeCollapsedNExpanded() throws Exception {
//        if (areListElementVisible(getDriver().findElements(By.cssSelector(".week-schedule-ribbon-group-toggle")),10)){
//            for (int i=0; i< getDriver().findElements(By.cssSelector(".week-schedule-ribbon-group-toggle")).size(); i++){
//                clickTheElement(getDriver().findElements(By.cssSelector(".week-schedule-ribbon-group-toggle")).get(i));
//                if (getDriver().findElements(By.cssSelector(".week-schedule-ribbon-group-toggle")).get(i).getAttribute("class").contains("closed")){
//                    clickTheElement(getDriver().findElements(By.cssSelector(".week-schedule-ribbon-group-toggle")).get(i));
//                    if (getDriver().findElements(By.cssSelector(".week-schedule-ribbon-group-toggle")).get(i).getAttribute("class").contains("open")){
//                        SimpleUtils.pass("Group can be expanded!!");
//                    } else {
//                        SimpleUtils.fail("Group is not expanded!", false);
//                    }
//                    clickTheElement(getDriver().findElements(By.cssSelector(".week-schedule-ribbon-group-toggle")).get(i));
//                    if (getDriver().findElements(By.cssSelector(".week-schedule-ribbon-group-toggle")).get(i).getAttribute("class").contains("closed")){
//                        SimpleUtils.pass("Group can be collapsed!");
//                    } else {
//                        SimpleUtils.fail("Group is not collapsed!", false);
//                    }
//                }
//                if (getDriver().findElements(By.cssSelector(".week-schedule-ribbon-group-toggle")).get(i).getAttribute("class").contains("open")){
//                    clickTheElement(getDriver().findElements(By.cssSelector(".week-schedule-ribbon-group-toggle")).get(i));
//                    if (getDriver().findElements(By.cssSelector(".week-schedule-ribbon-group-toggle")).get(i).getAttribute("class").contains("closed")){
//                        SimpleUtils.pass("Group can be collapsed!");
//                    } else {
//                        SimpleUtils.fail("Group is not collapsed!", false);
//                    }
//                }
//            }
//        } else {
//            SimpleUtils.fail("No group title show up!", false);
//        }
//    }
//
//    // Added by Nora
//    @FindBy (css = ".sch-worker-action")
//    private List<WebElement> shiftOptions;
//
//    /***
//     * Verify specific option is enabled on shift menu when clicking the avatar of the shift
//     * @param optionName - The name of the option
//     * @throws Exception
//     */
//    @Override
//    public void verifySpecificOptionEnabledOnShiftMenu(String optionName) throws Exception {
//        try {
//            boolean isEnabled = false;
//            if (areListElementVisible(shiftOptions, 15) && shiftOptions.size() > 0) {
//                for (WebElement option : shiftOptions) {
//                    if (option.getText().equalsIgnoreCase(optionName) && !option.getAttribute("class").contains("graded-out")) {
//                        isEnabled = true;
//                    }
//                }
//            }
//            SimpleUtils.assertOnFail("Shift option: " + optionName + " isn't enabled!", isEnabled,false);
//        } catch (Exception e) {
//            SimpleUtils.fail(e.getMessage(), false);
//        }
//    }
//
//    @FindBy(css = ".week-schedule-shift .shift-container .rows .worker-image-optimized")
//    private List<WebElement> profileIconsRingsInWeekView;
//    @FindBy(css = ".sch-day-view-shift-outer .allow-pointer-events")
//    private List<WebElement> profileIconsRingsInDayView;
//    @Override
//    public void verifyShiftsHasMinorsColorRing(String minorsType) throws Exception {
//        if (areListElementVisible(profileIconsRingsInDayView, 15)){
//            for (WebElement element: profileIconsRingsInDayView){
//                SimpleUtils.assertOnFail("No colered ring representing minors", element.getAttribute("class").contains(minorsType), false);
//            }
//        } else if (areListElementVisible(profileIconsRingsInWeekView, 15)){
//            for (WebElement element: profileIconsRingsInWeekView){
//                SimpleUtils.assertOnFail("No colered ring representing minors", element.getAttribute("class").contains(minorsType), false);
//            }
//        } else {
//            SimpleUtils.fail("No profile icons!", false);
//        }
//    }
//
//    @FindBy (className = "header-navigation-label")
//    private WebElement headerLabel;
//
//    @Override
//    public String getHeaderOnSchedule() throws Exception {
//        String header = "";
//        if (isElementLoaded(headerLabel,5))
//            header = headerLabel.getText();
//        return header;
//    }
//
//    @Override
//    public void verifyHeaderOnSchedule() throws Exception {
//        String header = getHeaderOnSchedule();
//        if (header.equals("Schedule"))
//            SimpleUtils.pass("Schedule Page: Header is \"Schedule\" as expected");
//        else
//            SimpleUtils.fail("Dashboard Page: Header isn't \"Schedule\"",true);
//    }
//
//    @FindBy(css = "div.modal-dialog div.edit-shift-notes")
//    private WebElement EditShiftNotesDialog;
//
//    @Override
//    public void verifyShiftNotesContent(String shiftNotes){
//        if (isElementEnabled(EditShiftNotesDialog, 10)){
//            //verify dialog title.
//            if (EditShiftNotesDialog.findElement(By.cssSelector("div.modal-instance-header-title")).getText().equalsIgnoreCase("Edit Shift Notes")){
//                SimpleUtils.pass("Edit shift notes dialog title is correct!");
//            } else {
//                SimpleUtils.fail("Edit shift notes dialog title is incorrect!", false);
//            }
//            //verify placeholder.
//            if (EditShiftNotesDialog.findElement(By.cssSelector("textarea")).getAttribute("placeholder").equalsIgnoreCase("Add note (Optional)")){
//                SimpleUtils.pass("Shift notes placeholder is expected!");
//            } else {
//                SimpleUtils.fail("Shift notes placeholder is incorrect!", false);
//            }
//            //verify shift notes content.
//            if (EditShiftNotesDialog.findElement(By.cssSelector("textarea")).getAttribute("value").equalsIgnoreCase(shiftNotes)){
//                SimpleUtils.pass("Shift notes is not expected!");
//            } else {
//                SimpleUtils.fail("Shift notes is not expected!", false);
//            }
//        } else {
//            SimpleUtils.fail("Edit shift notes dialog is not loaded!", false);
//        }
//    }
//
//    @Override
//    public void addShiftNotesToTextarea(String notes){
//        if (isElementEnabled(EditShiftNotesDialog.findElement(By.cssSelector("textarea")), 10)){
//            EditShiftNotesDialog.findElement(By.cssSelector("textarea")).clear();
//            EditShiftNotesDialog.findElement(By.cssSelector("textarea")).sendKeys(notes);
//            clickOnSaveBtnOnEditShiftNotesDialog();
//        } else {
//            SimpleUtils.fail("Edit shift notes dialog is not loaded!", false);
//        }
//    }
//
//    public void clickOnSaveBtnOnEditShiftNotesDialog(){
//        if (isElementEnabled(EditShiftNotesDialog.findElement(By.cssSelector("div.confirm")), 10)){
//            clickTheElement(EditShiftNotesDialog.findElement(By.cssSelector("div.confirm")));
//            SimpleUtils.pass("Update button is clicked!");
//        } else {
//            SimpleUtils.fail("Edit shift notes dialog is not loaded!", false);
//        }
//    }
//
//    @Override
//    public String getShiftInfoInEditShiftDialog() throws Exception {
//        if (isElementEnabled(EditShiftNotesDialog.findElement(By.cssSelector(".sch-day-view-shift-outer")), 10)){
//            return EditShiftNotesDialog.findElement(By.cssSelector(".sch-day-view-shift-outer .left-shift-box")).getText()+EditShiftNotesDialog.findElement(By.cssSelector(".sch-day-view-shift-outer .right-shift-box")).getText();
//        }
//        return null;
//    }
}

