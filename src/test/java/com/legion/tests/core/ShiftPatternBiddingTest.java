package com.legion.tests.core;

import com.legion.api.ShiftPatternBidding.AutoAssignmentTaskAPI;
import com.legion.pages.*;
import com.legion.pages.OpsPortaPageFactories.ConfigurationPage;
import com.legion.pages.OpsPortaPageFactories.LocationsPage;
import com.legion.tests.TestBase;
import com.legion.tests.annotations.Automated;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.annotations.Owner;
import com.legion.tests.annotations.TestName;
import com.legion.tests.core.OpsPortal.LocationsTest;
import com.legion.tests.data.CredentialDataProviderSource;
import com.legion.utils.SimpleUtils;
import org.junit.Rule;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

public class ShiftPatternBiddingTest extends TestBase {
    private DashboardPage dashboardPage;
    private CreateSchedulePage createSchedulePage;
    private ScheduleMainPage scheduleMainPage;
    private ScheduleShiftTablePage scheduleShiftTablePage;
    private ScheduleCommonPage scheduleCommonPage;
    private EditShiftPage editShiftPage;
    private NewShiftPage newShiftPage;
    private ShiftOperatePage shiftOperatePage;
    private ControlsPage controlsPage;
    private ControlsNewUIPage controlsNewUIPage;
    private MySchedulePage mySchedulePage;
    private BasePage basePage;
    private SmartCardPage smartCardPage;
    private NewShiftPatternBiddingPage newShiftPatternBiddingPage;
    private LocationsPage locationsPage;
    private ConfigurationPage configurationPage;
    private LoginPage loginPage;
    private BidShiftPatternBiddingPage bidShiftPatternBiddingPage;
    private ProfileNewUIPage profileNewUIPage;
    private RulePage rulePage;
    /*
    -Hourly rate: Mate: $50, Master:$30, Deckhand: $20, Chief Engineer:$10
    -Employee1-Adah's seniority by work role: Master-3, Mate-2, Chief Engineer-1, Deckhand-2
    -Employee2-Alvera's seniority by work role: Master-3, Mate-2, Chief Engineer-1, Deckhand-4
    -Employee3-Bennie's seniority by work role: Master-2, Mate-1, Chief Engineer-5, Deckhand-3
    -Employee4-Adele's seniority by work role: Master-1, Mate-4, Chief Engineer-3, Deckhand-2
    **/
    //Work role
    private String workRole1= "Mate";
    private String workRole2 = "Master";
    private String workRole3 = "Deckhand";
    private String workRole4 = "Engineer Chief";
    private String workRole1ShiftPattern1 = "Mate";
    private String workRole2ShiftPattern1 = "Master";
    private String workRole3ShiftPattern1 = "S12 - Crew 1";
    private String workRole4ShiftPattern1 = "S12 - Crew 1";
    private String workRole4ShiftPattern2 = "S12 - Crew 2";
    private String workRole4ShiftPattern3 = "CE-Test";


    @Override
    @BeforeMethod()
    public void firstTest(Method testMethod, Object[] params) throws Exception{
        try {
            this.createDriver((String) params[0], "83", "Window");
            visitPage(testMethod);
            loginToLegionAndVerifyIsLoginDone((String) params[1], (String) params[2], (String) params[3]);
            dashboardPage = pageFactory.createConsoleDashboardPage();
            createSchedulePage = pageFactory.createCreateSchedulePage();
            scheduleMainPage = pageFactory.createScheduleMainPage();
            scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            scheduleCommonPage = pageFactory.createScheduleCommonPage();
            editShiftPage = pageFactory.createEditShiftPage();
            newShiftPage = pageFactory.createNewShiftPage();
            shiftOperatePage = pageFactory.createShiftOperatePage();
            controlsPage = pageFactory.createConsoleControlsPage();
            controlsNewUIPage = pageFactory.createControlsNewUIPage();
            mySchedulePage = pageFactory.createMySchedulePage();
            basePage = new BasePage();
            smartCardPage = pageFactory.createSmartCardPage();
            newShiftPatternBiddingPage = pageFactory.createNewShiftPatternBiddingPage();
            locationsPage = pageFactory.createOpsPortalLocationsPage();
            configurationPage = pageFactory.createOpsPortalConfigurationPage();
            loginPage = pageFactory.createConsoleLoginPage();
            bidShiftPatternBiddingPage = pageFactory.createBidShiftPatternBiddingPage();
            profileNewUIPage = pageFactory.createProfileNewUIPage();
            rulePage = pageFactory.createRulePage();
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the end to end flow of shift bidding")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyTheEndToEndFlowOfShiftBiddingAsTeamMember(String browser, String username, String password, String location) throws Exception {
        try {
            profileNewUIPage.clickOnUserProfileImage();
            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Profile");
            String tmFullName = profileNewUIPage.getUserProfileName().get("fullName");
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());

            goToSchedulePageScheduleTab();
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if(!isActiveWeekGenerated){
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }

            //Remove all assignment on schedule rule page
            rulePage.clickRuleButton();
            rulePage.removeAllShiftPatternsAssignmentsOnScheduleRulePage();
            SimpleUtils.assertOnFail("Fail to delete all assignments on rule page! ",
                    !rulePage.checkIfThereAreAssignmentOnRulePage(), false);
            //Go to shift pattern bidding template and create new shift bidding
            locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
            locationsPage.clickOnLocationsTab();
            locationsPage.goToSubLocationsInLocationsPage();
            locationsPage.searchLocation(location);
            SimpleUtils.assertOnFail("Locations not searched out Successfully!",  locationsPage.verifyUpdateLocationResult(location), false);
            locationsPage.clickOnLocationInLocationResult(location);
            locationsPage.clickOnConfigurationTabOfLocation();
            HashMap<String, String> templateTypeAndName = locationsPage.getTemplateTypeAndNameFromLocation();

            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad("Shift Pattern Bidding");
            configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Shift Pattern Bidding"), "edit");
//            configurationPage.clickOnSpecifyTemplateName("SeaspanRegularLocation2", "edit");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();

            //Cancel or delete all current shift biddings
            if (newShiftPatternBiddingPage.getCurrentShiftBiddingsCount()>0){
                newShiftPatternBiddingPage.removeOrCancelAllCurrentShiftBidding();
                configurationPage.publishNowTheTemplate();
                configurationPage.clickOnSpecifyTemplateName("SeaspanRegularLocation2", "edit");
                configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            }

            //Verify new bidding page will display after click Add button on bidding list
            newShiftPatternBiddingPage.clickAddShiftBiddingButton();

            //Verify Shift bidding window start date/time and end date/time
            newShiftPatternBiddingPage.setShiftBiddingWindowStartAndEndDateAndTime();

            //Verify select Schedule start week
            newShiftPatternBiddingPage.selectTheFirstScheduleStartWeek();

            //Verify save the new shift bidding
            newShiftPatternBiddingPage.clickSaveButtonOnCreateShiftBiddingPage();
            //Verify publish the shift bidding template
            configurationPage.publishNowTemplate();
            locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.Console.getValue());
            refreshCachesAfterChangeTemplate();
            Thread.sleep(60000);
            refreshCachesAfterChangeTemplate();
            Thread.sleep(60000);
            refreshCachesAfterChangeTemplate();
            //Verify employee can see the bidding after bidding started
            loginPage.logOut();
            Thread.sleep(60000);
            loginAsDifferentRole(AccessRoles.TeamMember.getValue());
            Thread.sleep(60000);
            profileNewUIPage.clickOnUserProfileImage();
            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Profile");
            String tm1FullName = profileNewUIPage.getUserProfileName().get("fullName");
            String tm1FirstName = tm1FullName.split(" ")[0];
            dashboardPage.clickOnDashboardConsoleMenu();
            int i=0;
            while (!bidShiftPatternBiddingPage.checkIfTheShiftBiddingWidgetLoaded()&& i<10){
                Thread.sleep(30000);
                dashboardPage.clickOnDashboardConsoleMenu();
                i++;
            }
            SimpleUtils.assertOnFail("The shift bidding widget fail to load! ",
                    bidShiftPatternBiddingPage.checkIfTheShiftBiddingWidgetLoaded(), false);
            bidShiftPatternBiddingPage.clickSubmitBidButton();
            bidShiftPatternBiddingPage.clickNextButton();
            //Verify employee can select shift patterns
            bidShiftPatternBiddingPage.addAllShiftPatterns();
            bidShiftPatternBiddingPage.clickNextButton();
            bidShiftPatternBiddingPage.clickNextButton();
            //Verify employee can submit shift bidding
            bidShiftPatternBiddingPage.clickSubmitButton();
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            //Verify employee can be assign to the shift pattern after run auto assignment task API
            AutoAssignmentTaskAPI.runAutoAssignmentTaskAPI(getUserNameNPwdForCallingAPI().get(0),
                    getUserNameNPwdForCallingAPI().get(1));
            //Verify employee can be assign to the shifts after generated schedule
            goToSchedulePageScheduleTab();
            scheduleCommonPage.navigateToNextWeek();
            rulePage.clickRuleButton();

            //There is one assignment on rule page
            SimpleUtils.assertOnFail("Fail to delete all assignments on rule page! ",
                    rulePage.checkIfThereAreAssignmentOnRulePage(), false);

            //Check the employee display on rule page
            List<String> employeeNames = rulePage.getAllShiftPatternsAssignmentsOnScheduleRulePage();
            SimpleUtils.assertOnFail("The employee not display on rule page, the all assignment are:"+employeeNames
                            +", the employee name is:"+tmFullName,
                    employeeNames.contains(tmFullName), false);

            //Back to schedule page
            rulePage.clickBackButton();

            //Regenerate schedule and check the employee has shifts in the schedule
            createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            SimpleUtils.assertOnFail("The employee "+tmFullName+ " should display in schedule! ",
                    scheduleShiftTablePage.getAllShiftsOfOneTM(tmFullName.split(" ")[0]).size()>0, false);


        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify auto assign shift pattern to employee by seniority by work role")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyAutoAssignShiftPatternToEmployeeBySeniorityWorkRoleAsTeamMember5(String browser, String username, String password, String location) throws Exception {
        try {
            profileNewUIPage.clickOnUserProfileImage();
            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Profile");
            String tm5FullName = profileNewUIPage.getUserProfileName().get("fullName");
            String tm5FirstName = tm5FullName.split(" ")[0];
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());

            goToSchedulePageScheduleTab();
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if(!isActiveWeekGenerated){
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }

            //Remove all assignment on schedule rule page
            rulePage.clickRuleButton();
            rulePage.removeAllShiftPatternsAssignmentsOnScheduleRulePage();
            SimpleUtils.assertOnFail("Fail to delete all assignments on rule page! ",
                    !rulePage.checkIfThereAreAssignmentOnRulePage(), false);

            //Add employee to one shift pattern
            rulePage.assignEmployeeToSpecificShiftPattern(tm5FullName, workRole4,
                    workRole4ShiftPattern1, "","");

            //Go to shift pattern bidding template and create new shift bidding
            locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
            locationsPage.clickOnLocationsTab();
            locationsPage.goToSubLocationsInLocationsPage();
            locationsPage.searchLocation(location);
            SimpleUtils.assertOnFail("Locations not searched out Successfully!",  locationsPage.verifyUpdateLocationResult(location), false);
            locationsPage.clickOnLocationInLocationResult(location);
            locationsPage.clickOnConfigurationTabOfLocation();
            HashMap<String, String> templateTypeAndName = locationsPage.getTemplateTypeAndNameFromLocation();

            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad("Shift Pattern Bidding");
            configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Shift Pattern Bidding"), "edit");
//            configurationPage.clickOnSpecifyTemplateName("SeaspanRegularLocation2", "edit");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();

            //Cancel or delete all current shift biddings
            if (newShiftPatternBiddingPage.getCurrentShiftBiddingsCount()>0){
                newShiftPatternBiddingPage.removeOrCancelAllCurrentShiftBidding();
                configurationPage.publishNowTheTemplate();
                configurationPage.clickOnSpecifyTemplateName("SeaspanRegularLocation2", "edit");
                configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            }

            //Verify new bidding page will display after click Add button on bidding list
            newShiftPatternBiddingPage.clickAddShiftBiddingButton();

            //Verify Shift bidding window start date/time and end date/time
            newShiftPatternBiddingPage.setShiftBiddingWindowStartAndEndDateAndTime();

            //Verify select Schedule start week
            newShiftPatternBiddingPage.selectTheFirstScheduleStartWeek();

            //Verify save the new shift bidding
            newShiftPatternBiddingPage.clickSaveButtonOnCreateShiftBiddingPage();
            //Verify publish the shift bidding template
            configurationPage.publishNowTemplate();
            locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.Console.getValue());
            refreshCachesAfterChangeTemplate();
            //Verify employee can see the bidding after bidding started
            loginPage.logOut();
            Thread.sleep(60000);
            loginAsDifferentRole(AccessRoles.TeamMember.getValue());
            Thread.sleep(60000);
            profileNewUIPage.clickOnUserProfileImage();
            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Profile");
            String tm1FullName = profileNewUIPage.getUserProfileName().get("fullName");
            String tm1FirstName = tm1FullName.split(" ")[0];
                dashboardPage.clickOnDashboardConsoleMenu();
                int i=0;
                while (!bidShiftPatternBiddingPage.checkIfTheShiftBiddingWidgetLoaded()&& i<10){
                    Thread.sleep(30000);
                    dashboardPage.clickOnDashboardConsoleMenu();
                    i++;
                }
                SimpleUtils.assertOnFail("The shift bidding widget fail to load! ",
                        bidShiftPatternBiddingPage.checkIfTheShiftBiddingWidgetLoaded(), false);
            bidShiftPatternBiddingPage.clickSubmitBidButton();
            bidShiftPatternBiddingPage.clickNextButton();
            bidShiftPatternBiddingPage.addSpecificShiftPattern(workRole1, workRole1ShiftPattern1);
            bidShiftPatternBiddingPage.addSpecificShiftPattern(workRole2, workRole2ShiftPattern1);
            bidShiftPatternBiddingPage.addSpecificShiftPattern(workRole3, workRole3ShiftPattern1);
            //Verify employee can select shift patterns
            bidShiftPatternBiddingPage.clickNextButton();
            bidShiftPatternBiddingPage.clickNextButton();
            //Verify employee can submit shift bidding
            bidShiftPatternBiddingPage.clickSubmitButton();
            loginPage.logOut();

            loginAsDifferentRole(AccessRoles.TeamMember2.getValue());
            profileNewUIPage.clickOnUserProfileImage();
            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Profile");
            String tm2FullName = profileNewUIPage.getUserProfileName().get("fullName");
            String tm2FirstName = tm2FullName.split(" ")[0];
            dashboardPage.clickOnDashboardConsoleMenu();
            i=0;
            while (!bidShiftPatternBiddingPage.checkIfTheShiftBiddingWidgetLoaded()&& i<10){
                Thread.sleep(30000);
                dashboardPage.clickOnDashboardConsoleMenu();
                i++;
            }
            SimpleUtils.assertOnFail("The shift bidding widget fail to load! ",
                    bidShiftPatternBiddingPage.checkIfTheShiftBiddingWidgetLoaded(), false);
            bidShiftPatternBiddingPage.clickSubmitBidButton();
            bidShiftPatternBiddingPage.clickNextButton();
            bidShiftPatternBiddingPage.addSpecificShiftPattern(workRole2, workRole2ShiftPattern1);
            bidShiftPatternBiddingPage.addSpecificShiftPattern(workRole3, workRole3ShiftPattern1);
            //Verify employee can select shift patterns
            bidShiftPatternBiddingPage.clickNextButton();
            bidShiftPatternBiddingPage.clickNextButton();
            //Verify employee can submit shift bidding
            bidShiftPatternBiddingPage.clickSubmitButton();
            loginPage.logOut();

            loginAsDifferentRole(AccessRoles.TeamMember3.getValue());
            profileNewUIPage.clickOnUserProfileImage();
            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Profile");
            String tm3FullName = profileNewUIPage.getUserProfileName().get("fullName");
            String tm3FirstName = tm3FullName.split(" ")[0];
            dashboardPage.clickOnDashboardConsoleMenu();
            i=0;
            while (!bidShiftPatternBiddingPage.checkIfTheShiftBiddingWidgetLoaded()&& i<10){
                Thread.sleep(30000);
                dashboardPage.clickOnDashboardConsoleMenu();
                i++;
            }
            SimpleUtils.assertOnFail("The shift bidding widget fail to load! ",
                    bidShiftPatternBiddingPage.checkIfTheShiftBiddingWidgetLoaded(), false);
            bidShiftPatternBiddingPage.clickSubmitBidButton();
            bidShiftPatternBiddingPage.clickNextButton();
            bidShiftPatternBiddingPage.addSpecificShiftPattern(workRole1, workRole1ShiftPattern1);
            //Verify employee can select shift patterns
            bidShiftPatternBiddingPage.clickNextButton();
            bidShiftPatternBiddingPage.clickNextButton();
            //Verify employee can submit shift bidding
            bidShiftPatternBiddingPage.clickSubmitButton();
            loginPage.logOut();


            loginAsDifferentRole(AccessRoles.TeamMember4.getValue());
            profileNewUIPage.clickOnUserProfileImage();
            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Profile");
            String tm4FullName = profileNewUIPage.getUserProfileName().get("fullName");
            String tm4FirstName = tm4FullName.split(" ")[0];
            dashboardPage.clickOnDashboardConsoleMenu();
            i=0;
            while (!bidShiftPatternBiddingPage.checkIfTheShiftBiddingWidgetLoaded()&& i<10){
                Thread.sleep(30000);
                dashboardPage.clickOnDashboardConsoleMenu();
                i++;
            }
            SimpleUtils.assertOnFail("The shift bidding widget fail to load! ",
                    bidShiftPatternBiddingPage.checkIfTheShiftBiddingWidgetLoaded(), false);
            bidShiftPatternBiddingPage.clickSubmitBidButton();
            bidShiftPatternBiddingPage.clickNextButton();
            bidShiftPatternBiddingPage.addSpecificShiftPattern(workRole4, workRole4ShiftPattern1);
            bidShiftPatternBiddingPage.addSpecificShiftPattern(workRole4, workRole4ShiftPattern2);
            bidShiftPatternBiddingPage.addSpecificShiftPattern(workRole4, workRole4ShiftPattern3);
            //Verify employee can select shift patterns
            bidShiftPatternBiddingPage.clickNextButton();
            bidShiftPatternBiddingPage.clickNextButton();
            bidShiftPatternBiddingPage.rankSelectedShiftPattern(workRole4, workRole4ShiftPattern1, 3);
            bidShiftPatternBiddingPage.rankSelectedShiftPattern(workRole4, workRole4ShiftPattern2, 2);
            bidShiftPatternBiddingPage.rankSelectedShiftPattern(workRole4, workRole4ShiftPattern3, 1);
            //Verify employee can submit shift bidding
            bidShiftPatternBiddingPage.clickSubmitButton();
            loginPage.logOut();

            loginAsDifferentRole(AccessRoles.TeamMember6.getValue());
            profileNewUIPage.clickOnUserProfileImage();
            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Profile");
            String tm6FullName = profileNewUIPage.getUserProfileName().get("fullName");
            String tm6FirstName = tm6FullName.split(" ")[0];
            dashboardPage.clickOnDashboardConsoleMenu();
            i=0;
            while (!bidShiftPatternBiddingPage.checkIfTheShiftBiddingWidgetLoaded()&& i<10){
                Thread.sleep(30000);
                dashboardPage.clickOnDashboardConsoleMenu();
                i++;
            }
            SimpleUtils.assertOnFail("The shift bidding widget fail to load! ",
                    bidShiftPatternBiddingPage.checkIfTheShiftBiddingWidgetLoaded(), false);
            bidShiftPatternBiddingPage.clickSubmitBidButton();
            bidShiftPatternBiddingPage.clickNextButton();
            bidShiftPatternBiddingPage.addSpecificShiftPattern(workRole4, workRole4ShiftPattern1);
            //Verify employee can select shift patterns
            bidShiftPatternBiddingPage.clickNextButton();
            bidShiftPatternBiddingPage.clickNextButton();
            //Verify employee can submit shift bidding
            bidShiftPatternBiddingPage.clickSubmitButton();

//        String tm1FirstName = "Adah";
//        String tm2FirstName = "Alvera";
//        String tm3FirstName = "Bennie";
//        String tm4FirstName = "Adele";
//        String tm5FirstName = "Berniece";
//        String tm6FirstName = "Cecelia";

            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            //Verify employee can be assign to the shift pattern after run auto assignment task API
            AutoAssignmentTaskAPI.runAutoAssignmentTaskAPI(getUserNameNPwdForCallingAPI().get(0),
                    getUserNameNPwdForCallingAPI().get(1));
            //Verify employee can be assign to the shifts after generated schedule
            goToSchedulePageScheduleTab();
            rulePage.clickRuleButton();

            //Check the assignment of work role 1 + shift pattern 1
            List<String> employeeNames = rulePage.getAssignmentOfShiftPattern(workRole2, workRole2ShiftPattern1);
            boolean containSpecificEmployee = false;
            for(String name: employeeNames){
                if (name.contains(tm1FirstName)){
                    containSpecificEmployee = true;
                    SimpleUtils.pass("The employee:"+tm1FirstName+" been assign to shift pattern:"+workRole2ShiftPattern1+" successfully!");
                }
            }
            SimpleUtils.assertOnFail( "The employee is not contains in "+employeeNames,
                    containSpecificEmployee, false);

                //Check the assignment of work role 2 + shift pattern 2
                employeeNames.clear();
                employeeNames = rulePage.getAssignmentOfShiftPattern(workRole3, workRole3ShiftPattern1);
                containSpecificEmployee = false;
                for(String name: employeeNames){
                    if (name.contains(tm2FirstName)){
                        containSpecificEmployee = true;
                        SimpleUtils.pass("The employee:"+tm2FirstName+" been assign to shift pattern:"+workRole3ShiftPattern1+" successfully!");
                    }
                }
                SimpleUtils.assertOnFail( "The employee is not contains in "+employeeNames,
                        containSpecificEmployee, false);

                //Check the assignment of work role 3 + shift pattern 3
                employeeNames.clear();
                employeeNames = rulePage.getAssignmentOfShiftPattern(workRole1, workRole1ShiftPattern1);
                containSpecificEmployee = false;
                for(String name: employeeNames){
                    if (name.contains(tm3FirstName)){
                        containSpecificEmployee = true;
                        SimpleUtils.pass("The employee:"+tm3FirstName+" been assign to shift pattern:"+workRole1ShiftPattern1+" successfully!");
                    }
                }
                SimpleUtils.assertOnFail( "The employee is not contains in "+employeeNames,
                        containSpecificEmployee, false);


                //Check the assignment of work role 4 + shift pattern 4
                employeeNames.clear();
                employeeNames = rulePage.getAssignmentOfShiftPattern(workRole4, workRole4ShiftPattern3);
                containSpecificEmployee = false;
                for(String name: employeeNames){
                    if (name.contains(tm4FirstName)){
                        containSpecificEmployee = true;
                        SimpleUtils.pass("The employee:"+tm4FirstName+" been assign to shift pattern:"+workRole4ShiftPattern3+" successfully!");
                    }
                }
                SimpleUtils.assertOnFail( "The employee is not contains in "+employeeNames,
                        containSpecificEmployee, false);

                //Check the assignment of work role 5 + shift pattern 5
                employeeNames.clear();
                employeeNames = rulePage.getAssignmentOfShiftPattern(workRole4, workRole4ShiftPattern1);
                containSpecificEmployee = false;
                for(String name: employeeNames){
                    if (name.contains(tm5FirstName)){
                        containSpecificEmployee = true;
                        SimpleUtils.pass("The employee:"+tm5FirstName+" been assign to shift pattern:"+workRole4ShiftPattern1+" successfully!");
                    }
                }
                SimpleUtils.assertOnFail( "The employee should not contains in "+employeeNames,
                        !containSpecificEmployee, false);


                //Check the assignment of work role 6 + shift pattern 6
                employeeNames.clear();
                employeeNames = rulePage.getAssignmentOfShiftPattern(workRole4, workRole4ShiftPattern1);
                containSpecificEmployee = false;
                for(String name: employeeNames){
                    if (name.contains(tm6FirstName)){
                        containSpecificEmployee = true;
                        SimpleUtils.pass("The employee:"+tm6FirstName+" been assign to shift pattern:"+workRole4ShiftPattern1+" successfully!");
                    }
                }
                SimpleUtils.assertOnFail( "The employee is not contains in "+employeeNames,
                        containSpecificEmployee, false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }
}
