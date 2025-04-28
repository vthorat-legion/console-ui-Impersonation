package com.legion.pages.core;

import com.legion.pages.BasePage;
import com.legion.pages.RulePage;
import com.legion.utils.MyThreadLocal;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.legion.utils.MyThreadLocal.getDriver;

public class ConsoleRulePage extends BasePage implements RulePage {
    public ConsoleRulePage() {
        PageFactory.initElements(getDriver(), this);
    }


    @FindBy(css = "[id=\"legion_cons_Schedule_Schedule_Rules_button\"]")
    private WebElement rulesButton;
    @Override
    public void clickRuleButton() throws Exception {
        if (isElementLoaded(rulesButton, 10)) {
            scrollToElement(rulesButton);
            clickTheElement(rulesButton);
        }else
            SimpleUtils.fail("Rules button is not found!", false);
    }


    @FindBy(css = "[ng-if=\"controlPanel.isViewRulesMode\"] div img")
    private List<WebElement> shiftPatternsAssignments;
    @FindBy(css = ".MuiCardContent-root+div")
    private WebElement editAssignmentButton;
    @FindBy(css = "div.react-modal_assign-rules")
    private WebElement createOngoingAssignmentModal;
    @FindBy(css = "button svg[fill=\"none\"]")
    private List<WebElement> removeAssignmentButtons;
    @FindBy(css = "[id=\"legion_cons_Schedule_Schedule_AssignRules_Create_button\"]")
    private WebElement saveButtonOnCreateOngoingAssignmentModal;
    @Override
    public void removeAllShiftPatternsAssignmentsOnScheduleRulePage() throws Exception {
        if (areListElementVisible(shiftPatternsAssignments, 10)) {
            while (shiftPatternsAssignments.size()>0){
                click(shiftPatternsAssignments.get(0));
                click(editAssignmentButton);
                if (isElementLoaded(createOngoingAssignmentModal, 5)){
                    removeAllAssignmentsOnCreateOngoingAssignmentModal();
                    clickSaveButtonOnCreateOngoingAssignmentModal();
                }else
                    SimpleUtils.fail("The create ongoing assignment modal fail to load! ", false);
            }
        }else
            SimpleUtils.report("There is no shift pattern assignments on schedule rule page!");
    }


    @Override
    public boolean checkIfThereAreAssignmentOnRulePage() throws Exception {
        boolean isAassignmentDisplay = false;
        if (areListElementVisible(shiftPatternsAssignments, 10)
                && shiftPatternsAssignments.size()>0) {
            isAassignmentDisplay = true;
            SimpleUtils.pass("There is "+shiftPatternsAssignments.size()+" assignments display on rule page");
        }else
            SimpleUtils.report("There is no shift pattern assignments on schedule rule page!");
        return isAassignmentDisplay;
    }

    @Override
    public void removeAllAssignmentsOnCreateOngoingAssignmentModal() throws Exception {
        if (areListElementVisible(removeAssignmentButtons, 10)) {
            while(removeAssignmentButtons.size()>0){
                click(removeAssignmentButtons.get(0));
                SimpleUtils.pass("Remove one assignment successfully! ");
            }
            SimpleUtils.pass("Remove all assignments successfully! ");

        }else
            SimpleUtils.report("There is no assignment on create ongoing assignment modal!");
    }

    @Override
    public void clickSaveButtonOnCreateOngoingAssignmentModal() throws Exception {
        if (isElementLoaded(saveButtonOnCreateOngoingAssignmentModal, 10)) {
            clickTheElement(saveButtonOnCreateOngoingAssignmentModal);
            SimpleUtils.pass("Click save button on create ongoing assignment modal successfully! ");
        }else
            SimpleUtils.fail("There is no save button on create ongoing assignment modal!", false);
    }

    @Override
    public List<String> getAllShiftPatternsAssignmentsOnScheduleRulePage() throws Exception {
        List<String> employeeNames = new ArrayList<>();
        if (areListElementVisible(shiftPatternsAssignments, 10)) {
            for (WebElement shiftPatternsAssignment : shiftPatternsAssignments) {
                String employeeName = shiftPatternsAssignment.getAttribute("alt").trim();
                employeeNames.add(employeeName);
                SimpleUtils.pass("Get employee:" + employeeName + " successfully! ");
            }
        }else
            SimpleUtils.report("There is no shift pattern assignments on schedule rule page!");
        return employeeNames;
    }

    @FindBy(css = "span.tm-info__breadcrumbs")
    private WebElement backButtonOnRulePage;
    @Override
    public void clickBackButton() throws Exception {
        if (isElementLoaded(backButtonOnRulePage, 10)) {
            clickTheElement(backButtonOnRulePage);
            SimpleUtils.pass("Click back button on rule page successfully! ");
        }else
            SimpleUtils.fail("There is no back button on rule page!", false);
    }

    @FindBy(xpath = "//div/table/tbody/tr")
    private List<WebElement> shiftPatternsOnRulePage;
    @FindBy(css = "div.MuiInputBase-root input")
    private WebElement searchInput;
    @FindBy(xpath = "//div[contains(@class, 'MuiBox-root')]/div/div[contains(@class, 'MuiBox-root')]/div/div[2]/div")
    private List<WebElement> searchResults;
    @FindBy(css = "button[aria-label=\"Choose date\"]")
    private List<WebElement> selectDateButtons;
    @FindBy(css = "button[role=\"gridcell\"]")
    private List<WebElement> calendarDates;
    @FindBy(css = "div.MuiPickersCalendarHeader-label")
    private WebElement calendarMonthAndYear;
    @FindBy(css = "div.MuiPickersCalendarHeader-label")
    private WebElement arrowRightIcon;
    @FindBy(css = "div.MuiPickersCalendarHeader-label")
    private WebElement arrowLeftIcon;


    /*
    Date format: 2023 April 21
     */
    @Override
    public void assignEmployeeToSpecificShiftPattern(String employeeName, String workRole, String shiftPatternName, String startDate, String endDate) throws Exception {
        if (areListElementVisible(shiftPatternsOnRulePage, 10)) {
            for (WebElement shiftPattern: shiftPatternsOnRulePage){
                //Get work role name and shift pattern name
                String  shiftPatternNameOnRulePage = shiftPattern.findElement(By.xpath("./td/div/div[2]/div[1]/div[1]")).getText().trim();
                String workRoleNameOnRulePage = shiftPattern.findElement(By.xpath("./td/div/div[2]/div[2]/span")).getText().split("-")[0].trim();
                if (workRoleNameOnRulePage.equalsIgnoreCase(workRole)
                        && shiftPatternNameOnRulePage.equalsIgnoreCase(shiftPatternName)){
                    if (isElementLoaded(shiftPattern.findElement(By.xpath("./td[2]/div/div[2]/div/span[contains(text(),'Auto')]")))){
                         click(shiftPattern.findElement(By.xpath("./td[2]/div/div[2]/div/span[contains(text(),'Auto')]")));
                        if (isElementLoaded(createOngoingAssignmentModal, 5)){
                            //Search employee
                            searchInput.clear();
                            searchInput.sendKeys(employeeName);
                            waitForSeconds(3);
                            //Select employee
                            if (areListElementVisible(searchResults, 30)) {
                                for (WebElement searchResult : searchResults) {
                                    WebElement workerName = searchResult.findElement(By.xpath("//p[1]"));
                                    WebElement assignButton = searchResult.findElement(By.xpath("//button"));
                                    if (workerName != null && assignButton != null) {
                                        if (workerName.getText().toLowerCase().trim().contains(employeeName.toLowerCase())) {
                                            clickTheElement(assignButton);
                                            SimpleUtils.report("Select Team Member: " + employeeName + " Successfully!");
                                            waitForSeconds(2);
                                        }
                                    }else {
                                        SimpleUtils.fail("Worker name or assign button not loaded Successfully!", false);
                                    }
                                }
                            }else {
                                SimpleUtils.fail("Failed to find the team member!", false);
                            }
                            //Select start date and end date
                            clickTheElement(selectDateButtons.get(0));
                            if (!startDate.equals("")){
                                //select month
                                String day =  startDate.split(" ")[2];
                                String monthAndYear = startDate.split(" ")[1]+" "+startDate.split(" ")[0];
                                String monthAndYearInCalendar = calendarMonthAndYear.getText();
                                while(!monthAndYear.equalsIgnoreCase(monthAndYearInCalendar)){
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MMM");
                                    Date exceptDate = dateFormat.parse(monthAndYear);
                                    Date actualDate = dateFormat.parse(monthAndYearInCalendar);
                                    boolean needMoveForward = exceptDate.after(actualDate);
                                    if(needMoveForward && isElementLoaded(arrowRightIcon, 5)){
                                        click(arrowRightIcon);
                                    } else if (!needMoveForward && isElementLoaded(arrowLeftIcon, 5)) {
                                        click(arrowLeftIcon);
                                    } else
                                        SimpleUtils.fail("The Calender right/left Arrows fail to load! ", false);
                                }
                                //select date
                                for (WebElement date: calendarDates){
                                    String dateInCalendar = date.getText();
                                    if (day.equalsIgnoreCase(dateInCalendar)){
                                        clickTheElement(date);
                                        break;
                                    }
                                }
                            }else{
                                clickTheElement(calendarDates.get(0));
                            }

                            clickTheElement(selectDateButtons.get(0));
                            if (!endDate.equals("")){
                                //select month
                                String day =  endDate.split(" ")[2];
                                String monthAndYear = endDate.split(" ")[1]+" "+endDate.split(" ")[0];
                                String monthAndYearInCalendar = calendarMonthAndYear.getText();
                                while(!monthAndYear.equalsIgnoreCase(monthAndYearInCalendar)){
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MMM");
                                    Date exceptDate = dateFormat.parse(monthAndYear);
                                    Date actualDate = dateFormat.parse(monthAndYearInCalendar);
                                    boolean needMoveForward = exceptDate.after(actualDate);
                                    if(needMoveForward && isElementLoaded(arrowRightIcon, 5)){
                                        click(arrowRightIcon);
                                    } else if (!needMoveForward && isElementLoaded(arrowLeftIcon, 5)) {
                                        click(arrowLeftIcon);
                                    } else
                                        SimpleUtils.fail("The Calender right/left Arrows fail to load! ", false);
                                }
                                //select date
                                for (WebElement date: calendarDates){
                                    String dateInCalendar = date.getText();
                                    if (day.equalsIgnoreCase(dateInCalendar)){
                                        clickTheElement(date);
                                        break;
                                    }
                                }
                            }
                            clickSaveButtonOnCreateOngoingAssignmentModal();
                        }else
                            SimpleUtils.fail("The create ongoing assignment modal fail to load! ", false);
                    } else
                        SimpleUtils.fail("The shift pattern has been assigned! ", false);
                    break;
                }

            }
        }else
            SimpleUtils.fail("There is no shift pattern on rule page!", false);
    }



    public List<String> getAssignmentOfShiftPattern(String workRole, String shiftPatternName){
        List<String> employeeNames = new ArrayList<>();
        if (areListElementVisible(shiftPatternsOnRulePage, 5)
                &&shiftPatternsOnRulePage.size()>0){
            for (WebElement shiftPattern:shiftPatternsOnRulePage){
                String  shiftPatternNameOnRulePage = shiftPattern.findElement(By.xpath("./td/div/div[2]/div[1]/div[1]")).getText().trim();
                String workRoleNameOnRulePage = shiftPattern.findElement(By.xpath("./td/div/div[2]/div[2]/span")).getText().split("-")[0].trim();
                if (workRoleNameOnRulePage.equalsIgnoreCase(workRole)
                        && shiftPatternNameOnRulePage.equalsIgnoreCase(shiftPatternName)){
                    List<WebElement> assignments = shiftPattern.findElements(By.xpath(".//span/span/span[1]"));
                    if (assignments.size()>0){
                        for(WebElement assignment: assignments){
                            employeeNames.add(assignment.getText());
                            SimpleUtils.pass("Get one employee name successfully! ");
                        }
                    }else
                        SimpleUtils.report("There is no assignment for work role:"+workRole+"'s shift pattern:"+shiftPatternName);
                    break;
                }
            }
        }else
            SimpleUtils.fail("There is no shift pattern on rule page!", false);
        return employeeNames;
    }
}
