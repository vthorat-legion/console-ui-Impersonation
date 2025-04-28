package com.legion.tests.core;

import com.legion.pages.*;
import com.legion.tests.TestBase;
import com.legion.tests.annotations.Automated;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.annotations.Owner;
import com.legion.tests.annotations.TestName;
import com.legion.tests.data.CredentialDataProviderSource;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.*;

public class ScheduleAcknowledgementTest extends TestBase {
    @Override
    @BeforeMethod()
    public void firstTest(Method testMethod, Object[] params) throws Exception{
        try {
            this.createDriver((String) params[0], "69", "Window");
            visitPage(testMethod);
            loginToLegionAndVerifyIsLoginDone((String) params[1], (String) params[2], (String) params[3]);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate TM can acknowledge the notification after publish the schedule")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyTMCanAcknowledgeTheNotificationAfterPublishTheScheduleAsTeamMember(String browser, String username, String password, String location) throws Exception {
        try {
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            profileNewUIPage.clickOnUserProfileImage();
            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Profile");
            String tmFullName = profileNewUIPage.getUserProfileName().get("fullName");
            String firstName = tmFullName.split(" ")[0];
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            goToSchedulePageScheduleTab();
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            String smartCardName = "SCHEDULE ACKNOWLEDGEMENT";
            //Verify the SCHEDULE ACKNOWLEDGEMENT smart card will not display after generate but not publish schedule
            SimpleUtils.assertOnFail("The SCHEDULE ACKNOWLEDGEMENT smart card should not display before publish schedule! ",
                    !smartCardPage.isSpecificSmartCardLoaded(smartCardName), false);
            String workRole = shiftOperatePage.getRandomWorkRole();
            //Verify the SCHEDULE ACKNOWLEDGEMENT smart card will not display after edit but not publish schedule
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            createShiftsWithSpecificValues(workRole, "", "", "9:00am", "12:00pm",
                    1, Arrays.asList(1), ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(), "", firstName);
            scheduleMainPage.saveSchedule();
            //https://legiontech.atlassian.net/browse/SCH-8043
            SimpleUtils.assertOnFail("The SCHEDULE ACKNOWLEDGEMENT smart card should not display before publish schedule! ",
                    !smartCardPage.isSpecificSmartCardLoaded(smartCardName), false);
            //Verify the SCHEDULE ACKNOWLEDGEMENT smart card will display after publish schedule
            createSchedulePage.publishActiveSchedule();
            SimpleUtils.assertOnFail("The SCHEDULE ACKNOWLEDGEMENT smart card should display before publish schedule! ",
                    smartCardPage.isSpecificSmartCardLoaded(smartCardName), false);
            //Get count before acknowledge
            int pendingEmployeeCountBeforeAcknowledge = smartCardPage.getCountFromSmartCardByName(smartCardName);

            //Login as employee
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.TeamMember.getValue());
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();

            //check ACTION REQUIRED smart card display
            String acknowledgeNotificationMessage = "Please review and acknowledge receiving your schedule below.";
            SimpleUtils.assertOnFail("The SCHEDULE ACKNOWLEDGEMENT smart card should display before publish schedule! ",
                    smartCardPage.isSpecificSmartCardLoaded("ACTION REQUIRED")
                            && smartCardPage.isSmartCardAvailableByLabel(acknowledgeNotificationMessage), false);
            smartCardPage.clickOnAcknowledgeButtonOnAcknowledgeNotificationSmartCard();
            refreshPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("The SCHEDULE ACKNOWLEDGEMENT smart card should display before publish schedule! ",
                    !smartCardPage.isSpecificSmartCardLoaded("ACTION REQUIRED"), false);

            //Login as admin
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            goToSchedulePageScheduleTab();
            int pendingEmployeeCountAfterAcknowledge = smartCardPage.getCountFromSmartCardByName(smartCardName);
            SimpleUtils.assertOnFail("The pending employee count display incorrectly, the expected is: "
                            + (pendingEmployeeCountBeforeAcknowledge-1) + ". The actual is: "+pendingEmployeeCountAfterAcknowledge,
                    pendingEmployeeCountAfterAcknowledge == (pendingEmployeeCountBeforeAcknowledge-1) , false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate SM can acknowledge the notification after publish the schedule")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifySMCanAcknowledgeTheNotificationAfterPublishTheScheduleAsStoreManager(String browser, String username, String password, String location) throws Exception {
        try {
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            profileNewUIPage.clickOnUserProfileImage();
            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Profile");
            String tmFullName = profileNewUIPage.getUserProfileName().get("fullName");
            String firstName = tmFullName.split(" ")[0];
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            goToSchedulePageScheduleTab();
            scheduleCommonPage.navigateToNextWeek();
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            String smartCardName = "SCHEDULE ACKNOWLEDGEMENT";
            //Verify the SCHEDULE ACKNOWLEDGEMENT smart card will not display after generate but not publish schedule
            SimpleUtils.assertOnFail("The SCHEDULE ACKNOWLEDGEMENT smart card should not display before publish schedule! ",
                    !smartCardPage.isSpecificSmartCardLoaded(smartCardName), false);
            String workRole = shiftOperatePage.getRandomWorkRole();
            //Verify the SCHEDULE ACKNOWLEDGEMENT smart card will not display after edit but not publish schedule
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            createShiftsWithSpecificValues(workRole, "", "", "9:00am", "12:00pm",
                    1, Arrays.asList(1), ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(), "", firstName);
            scheduleMainPage.saveSchedule();
            //https://legiontech.atlassian.net/browse/SCH-8043
            SimpleUtils.assertOnFail("The SCHEDULE ACKNOWLEDGEMENT smart card should not display before publish schedule! ",
                    !smartCardPage.isSpecificSmartCardLoaded(smartCardName), false);
            //Verify the SCHEDULE ACKNOWLEDGEMENT smart card will display after publish schedule
            Thread.sleep(5000);
            createSchedulePage.publishActiveSchedule();
            SimpleUtils.assertOnFail("The SCHEDULE ACKNOWLEDGEMENT smart card should display after publish schedule! ",
                    smartCardPage.isSpecificSmartCardLoaded(smartCardName), false);
            //Get count before acknowledge
            int pendingEmployeeCountBeforeAcknowledge = smartCardPage.getCountFromSmartCardByName(smartCardName);

            //Login as employee
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.StoreManager.getValue());
            dashboardPage.clickOnProfileIconOnDashboard();
            dashboardPage.clickOnSwitchToEmployeeView();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.navigateToNextWeek();

            //check ACTION REQUIRED smart card display
            String acknowledgeNotificationMessage = "Please review and acknowledge receiving your schedule below.";
            SimpleUtils.assertOnFail("The SCHEDULE ACKNOWLEDGEMENT smart card should display before publish schedule! ",
                    smartCardPage.isSpecificSmartCardLoaded("ACTION REQUIRED")
                            && smartCardPage.isSmartCardAvailableByLabel(acknowledgeNotificationMessage), false);
            smartCardPage.clickOnAcknowledgeButtonOnAcknowledgeNotificationSmartCard();
            refreshPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.navigateToNextWeek();
            SimpleUtils.assertOnFail("The SCHEDULE ACKNOWLEDGEMENT smart card should display before publish schedule! ",
                    !smartCardPage.isSpecificSmartCardLoaded("ACTION REQUIRED"), false);

            //Login as admin
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            goToSchedulePageScheduleTab();
            scheduleCommonPage.navigateToNextWeek();
            int pendingEmployeeCountAfterAcknowledge = smartCardPage.getCountFromSmartCardByName(smartCardName);
            SimpleUtils.assertOnFail("The pending employee count display incorrectly, the expected is: "
                            + (pendingEmployeeCountBeforeAcknowledge-1) + ". The actual is: "+pendingEmployeeCountAfterAcknowledge,
                    pendingEmployeeCountAfterAcknowledge == (pendingEmployeeCountBeforeAcknowledge-1) , false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate employee can acknowledge the notification after edit shifts")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyEmployeeCanAcknowledgeTheNotificationAfterEditShiftsAsTeamMember(String browser, String username, String password, String location) throws Exception {
        try {
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            profileNewUIPage.clickOnUserProfileImage();
            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Profile");
            String tmFullName = profileNewUIPage.getUserProfileName().get("fullName");
            String firstName = tmFullName.split(" ")[0];
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            goToSchedulePageScheduleTab();
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated) {
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }
            String workRole = shiftOperatePage.getRandomWorkRole();
            //Check if TM has shift in the schedule
            scheduleMainPage.clickOnOpenSearchBoxButton();
            scheduleMainPage.searchShiftOnSchedulePage(tmFullName);
            if (scheduleShiftTablePage.getShiftsCount()<3){
                //If these is no shift for TM, create one for it
                scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
                createShiftsWithSpecificValues(workRole, "", "", "1:00pm", "3:00pm",
                        1, Arrays.asList(0,1,2,3), ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(), "", firstName);
                scheduleMainPage.saveSchedule();
            }
            createSchedulePage.publishActiveSchedule();
            //Login as TM and acknowledge the original notification
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.TeamMember.getValue());
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            if(smartCardPage.isSpecificSmartCardLoaded("ACTION REQUIRED")) {
                smartCardPage.clickOnAcknowledgeButtonOnAcknowledgeNotificationSmartCard();
            }
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            goToSchedulePageScheduleTab();
            //Verify the employee can receive and acknowledge the acknowledge notification after edit shift time
            String action = "edit shift time";
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleMainPage.clickOnOpenSearchBoxButton();
            scheduleMainPage.searchShiftOnSchedulePage(tmFullName);
            shiftOperatePage.editTheShiftTimeForSpecificShift(scheduleShiftTablePage.getTheShiftByIndex(0), "2pm", "4pm");
            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();
            int pendingEmployeeCountBeforeAcknowledge = smartCardPage.getCountFromSmartCardByName("SCHEDULE ACKNOWLEDGEMENT");
            loginAsTMAndAcknowledgeTheNotification(action);
            int pendingEmployeeCountAfterAcknowledge = smartCardPage.getCountFromSmartCardByName("SCHEDULE ACKNOWLEDGEMENT");
            SimpleUtils.assertOnFail("The pending employee count display incorrectly, the expected is: "
                            + (pendingEmployeeCountBeforeAcknowledge-1) + ". The actual is: "+pendingEmployeeCountAfterAcknowledge,
                    pendingEmployeeCountAfterAcknowledge == (pendingEmployeeCountBeforeAcknowledge-1) , false);

            //Verify the employee can receive and acknowledge the acknowledge notification after change shift work role
            action = "change shift role";
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleMainPage.clickOnOpenSearchBoxButton();
            scheduleMainPage.searchShiftOnSchedulePage(tmFullName);
            shiftOperatePage.clickOnProfileIcon();
            shiftOperatePage.changeWorkRoleInPromptOfAShift(true, scheduleShiftTablePage.getTheShiftByIndex(0));
            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();
            pendingEmployeeCountBeforeAcknowledge = smartCardPage.getCountFromSmartCardByName("SCHEDULE ACKNOWLEDGEMENT");
            loginAsTMAndAcknowledgeTheNotification(action);
            pendingEmployeeCountAfterAcknowledge = smartCardPage.getCountFromSmartCardByName("SCHEDULE ACKNOWLEDGEMENT");
            SimpleUtils.assertOnFail("The pending employee count display incorrectly, the expected is: "
                            + (pendingEmployeeCountBeforeAcknowledge-1) + ". The actual is: "+pendingEmployeeCountAfterAcknowledge,
                    pendingEmployeeCountAfterAcknowledge == (pendingEmployeeCountBeforeAcknowledge-1) , false);

            //Verify the employee can receive and acknowledge the acknowledge notification after assign other TM for the employee's shift
            action = "assign other TM for shift";
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleMainPage.clickOnOpenSearchBoxButton();
            scheduleMainPage.searchShiftOnSchedulePage(tmFullName);
            int shiftCountBeforeAssign = scheduleShiftTablePage.getShiftsCount();
            shiftOperatePage.clickOnProfileIcon();
            shiftOperatePage.clickonAssignTM();
            newShiftPage.selectTeamMembers();
            newShiftPage.clickOnOfferOrAssignBtn();
            int shiftCountAfterAssign = scheduleShiftTablePage.getShiftsCount();
            SimpleUtils.assertOnFail("The expected shift count is :"+(shiftCountBeforeAssign-1)
                    +". The actual shift count is:"+shiftCountAfterAssign, shiftCountBeforeAssign-1 == shiftCountAfterAssign, false);
            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();
            pendingEmployeeCountBeforeAcknowledge = smartCardPage.getCountFromSmartCardByName("SCHEDULE ACKNOWLEDGEMENT");
            loginAsTMAndAcknowledgeTheNotification(action);
            pendingEmployeeCountAfterAcknowledge = smartCardPage.getCountFromSmartCardByName("SCHEDULE ACKNOWLEDGEMENT");
            SimpleUtils.assertOnFail("The pending employee count display incorrectly, the expected is: "
                            + (pendingEmployeeCountBeforeAcknowledge-1) + ". The actual is: "+pendingEmployeeCountAfterAcknowledge,
                    pendingEmployeeCountAfterAcknowledge == (pendingEmployeeCountBeforeAcknowledge-1) , false);

            //Verify the employee can receive and acknowledge the acknowledge notification after convert employee's shift to open
            action = "convert shift to open";
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleMainPage.clickOnOpenSearchBoxButton();
            scheduleMainPage.searchShiftOnSchedulePage(tmFullName);
            shiftCountBeforeAssign = scheduleShiftTablePage.getShiftsCount();
            shiftOperatePage.clickOnProfileIcon();
            shiftOperatePage.clickOnConvertToOpenShift();
            shiftOperatePage.convertToOpenShiftDirectly();
            shiftCountAfterAssign = scheduleShiftTablePage.getShiftsCount();
            SimpleUtils.assertOnFail("The expected shift count is :"+(shiftCountBeforeAssign-1)
                    +". The actual shift count is:"+shiftCountAfterAssign, shiftCountBeforeAssign-1 == shiftCountAfterAssign, false);
            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();
            pendingEmployeeCountBeforeAcknowledge = smartCardPage.getCountFromSmartCardByName("SCHEDULE ACKNOWLEDGEMENT");
            loginAsTMAndAcknowledgeTheNotification(action);
            pendingEmployeeCountAfterAcknowledge = smartCardPage.getCountFromSmartCardByName("SCHEDULE ACKNOWLEDGEMENT");
            SimpleUtils.assertOnFail("The pending employee count display incorrectly, the expected is: "
                            + (pendingEmployeeCountBeforeAcknowledge-1) + ". The actual is: "+pendingEmployeeCountAfterAcknowledge,
                    pendingEmployeeCountAfterAcknowledge == (pendingEmployeeCountBeforeAcknowledge-1) , false);

            //Verify the employee can receive and acknowledge the acknowledge notification after edit shift note
            action = "edit shift note";
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleMainPage.clickOnOpenSearchBoxButton();
            scheduleMainPage.searchShiftOnSchedulePage(tmFullName);
            shiftOperatePage.clickOnProfileIcon();
            shiftOperatePage.clickOnEditShiftNotesOption();
            shiftOperatePage.addShiftNotesToTextarea("shift notes testing");
            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();
            pendingEmployeeCountBeforeAcknowledge = smartCardPage.getCountFromSmartCardByName("SCHEDULE ACKNOWLEDGEMENT");
            loginAsTMAndAcknowledgeTheNotification(action);
            pendingEmployeeCountAfterAcknowledge = smartCardPage.getCountFromSmartCardByName("SCHEDULE ACKNOWLEDGEMENT");
            SimpleUtils.assertOnFail("The pending employee count display incorrectly, the expected is: "
                            + (pendingEmployeeCountBeforeAcknowledge-1) + ". The actual is: "+pendingEmployeeCountAfterAcknowledge,
                    pendingEmployeeCountAfterAcknowledge == (pendingEmployeeCountBeforeAcknowledge-1) , false);

            //Verify the employee can receive and acknowledge the acknowledge notification after edit breaks of the shift
            action = "edit shift breaks";
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleMainPage.clickOnOpenSearchBoxButton();
            scheduleMainPage.searchShiftOnSchedulePage(tmFullName);
            shiftOperatePage.clickOnProfileIcon();
            shiftOperatePage.clickOnEditMeaLBreakTime();
            shiftOperatePage.moveMealOrRestBreak(true, 100); shiftOperatePage.clickOnOKBtnOnMealBreakDialog();
            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();
            pendingEmployeeCountBeforeAcknowledge = smartCardPage.getCountFromSmartCardByName("SCHEDULE ACKNOWLEDGEMENT");
            loginAsTMAndAcknowledgeTheNotification(action);
            pendingEmployeeCountAfterAcknowledge = smartCardPage.getCountFromSmartCardByName("SCHEDULE ACKNOWLEDGEMENT");
            SimpleUtils.assertOnFail("The pending employee count display incorrectly, the expected is: "
                            + (pendingEmployeeCountBeforeAcknowledge-1) + ". The actual is: "+pendingEmployeeCountAfterAcknowledge,
                    pendingEmployeeCountAfterAcknowledge == (pendingEmployeeCountBeforeAcknowledge-1) , false);

            //Verify the employee can receive and acknowledge the acknowledge notification after delete the shifts
            action = "delete shifts";
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstName);
            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();
            pendingEmployeeCountBeforeAcknowledge = smartCardPage.getCountFromSmartCardByName("SCHEDULE ACKNOWLEDGEMENT");
            loginAsTMAndAcknowledgeTheNotification(action);
            pendingEmployeeCountAfterAcknowledge = smartCardPage.getCountFromSmartCardByName("SCHEDULE ACKNOWLEDGEMENT");
//            SimpleUtils.assertOnFail("The pending employee count display incorrectly, the expected is: "
//                            + (pendingEmployeeCountBeforeAcknowledge-1) + ". The actual is: "+pendingEmployeeCountAfterAcknowledge,
//                     , false);
            if (pendingEmployeeCountAfterAcknowledge == (pendingEmployeeCountBeforeAcknowledge-1)) {
                SimpleUtils.pass("The pending employee count display correctly! ");
            } else
                SimpleUtils.fail("The pending employee count display incorrectly, the expected is: ", false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    private void loginAsTMAndAcknowledgeTheNotification(String action) throws Exception {
        LoginPage loginPage = pageFactory.createConsoleLoginPage();
        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
        ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
        ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
        TeamPage teamPage = pageFactory.createConsoleTeamPage();
        ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
        SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        loginPage.logOut();
        loginAsDifferentRole(AccessRoles.TeamMember.getValue());
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        if(smartCardPage.isSpecificSmartCardLoaded("ACTION REQUIRED")) {
            smartCardPage.clickOnAcknowledgeButtonOnAcknowledgeNotificationSmartCard();
            refreshPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("The SCHEDULE ACKNOWLEDGEMENT smart card should display before publish schedule! ",
                    !smartCardPage.isSpecificSmartCardLoaded("ACTION REQUIRED"), false);
        }else
            SimpleUtils.fail("The acknowledge notification smart card fail to load after "+action+"! ",false);
        loginPage.logOut();
        loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
        goToSchedulePageScheduleTab();
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify employee can acknowledge the notification after drag and drop shifts")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyEmployeeCanAcknowledgeTheNotificationAfterDragAndDropShiftsAsTeamMember(String browser, String username, String password, String location) throws Exception {
        try {
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            profileNewUIPage.clickOnUserProfileImage();
            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Profile");
            String tmFullName = profileNewUIPage.getUserProfileName().get("fullName");
            String firstName = tmFullName.split(" ")[0];
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            goToSchedulePageScheduleTab();
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated) {
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }
            String workRole = shiftOperatePage.getRandomWorkRole();
            //Check if TM has shift in the schedule
            scheduleMainPage.clickOnOpenSearchBoxButton();
            scheduleMainPage.searchShiftOnSchedulePage(tmFullName);
            if (scheduleShiftTablePage.getShiftsCount()>0) {
                scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
                scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstName);
                scheduleMainPage.saveSchedule();
            }
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            createShiftsWithSpecificValues(workRole, "", "", "1:00pm", "3:00pm",
                    1, Arrays.asList(0), ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(), "", firstName);
            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();
            //Login as TM and acknowledge the original notification
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.TeamMember.getValue());
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            if(smartCardPage.isSpecificSmartCardLoaded("ACTION REQUIRED")) {
                smartCardPage.clickOnAcknowledgeButtonOnAcknowledgeNotificationSmartCard();
            }
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            goToSchedulePageScheduleTab();
            //Verify the employee can receive and acknowledge the acknowledge notification after edit shift time
            String action = "drag and drop shift";
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            List<WebElement> shiftsOnOneDay = new ArrayList<>();
//            int dayIndex = 0;
//            for (int i = 0;i< 7;i++) {
//                shiftsOnOneDay = scheduleShiftTablePage.getOneDayShiftByName(i, firstName);
//                if (shiftsOnOneDay.size()>0) {
//                    dayIndex = i;
//                    break;
//                }
//            }
            scheduleShiftTablePage.dragOneShiftToAnotherDay(0, firstName, 6);
            scheduleShiftTablePage.selectCopyOrMoveByOptionName("move");
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
            if (scheduleShiftTablePage.ifMoveAnywayDialogDisplay()) {
                scheduleShiftTablePage.moveAnywayWhenChangeShift();
            }
            scheduleShiftTablePage.verifyShiftIsMovedToAnotherDay(0,firstName,6);
            scheduleMainPage.saveSchedule();
            scheduleShiftTablePage.verifyShiftIsMovedToAnotherDay(0,firstName,6);

            createSchedulePage.publishActiveSchedule();
            int pendingEmployeeCountBeforeAcknowledge = smartCardPage.getCountFromSmartCardByName("SCHEDULE ACKNOWLEDGEMENT");
            loginAsTMAndAcknowledgeTheNotification(action);
            int pendingEmployeeCountAfterAcknowledge = smartCardPage.getCountFromSmartCardByName("SCHEDULE ACKNOWLEDGEMENT");
            SimpleUtils.assertOnFail("The pending employee count display incorrectly, the expected is: "
                            + (pendingEmployeeCountBeforeAcknowledge-1) + ". The actual is: "+pendingEmployeeCountAfterAcknowledge,
                    pendingEmployeeCountAfterAcknowledge == (pendingEmployeeCountBeforeAcknowledge-1) , false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate employee can acknowledge the notification from non-home location when home location schedule is published")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyEmployeeCanAcknowledgeTheNotificationFromNonHomeLocationWhenHomeScheduleIsPublishedAsTeamMemberOtherLocation1(String browser, String username, String password, String location) throws Exception {
        try {
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            profileNewUIPage.clickOnUserProfileImage();
            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Profile");
            String tmFullName = profileNewUIPage.getUserProfileName().get("fullName");
            String firstName = tmFullName.split(" ")[0];
            String lastName = tmFullName.split(" ")[1];
            String homeLocation = location;
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(homeLocation);
            goToSchedulePageScheduleTab();
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated) {
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }
            scheduleMainPage.clickOnOpenSearchBoxButton();
            scheduleMainPage.searchShiftOnSchedulePage(tmFullName);
            if (scheduleShiftTablePage.getShiftsCount()>0) {
                scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
                scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstName);
                scheduleMainPage.saveSchedule();
            }
            createSchedulePage.publishActiveSchedule();
            //Login as TM and acknoledge the notification
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.TeamMemberOtherLocation1.getValue());
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            if(smartCardPage.isSpecificSmartCardLoaded("ACTION REQUIRED")) {
                smartCardPage.clickOnAcknowledgeButtonOnAcknowledgeNotificationSmartCard();
                refreshPage();
                scheduleCommonPage.clickOnScheduleConsoleMenuItem();
                scheduleCommonPage.navigateToNextWeek();
                scheduleCommonPage.navigateToNextWeek();
                scheduleCommonPage.navigateToNextWeek();
                scheduleCommonPage.navigateToNextWeek();
                SimpleUtils.assertOnFail("The SCHEDULE ACKNOWLEDGEMENT smart card should display before publish schedule! ",
                        !smartCardPage.isSpecificSmartCardLoaded("ACTION REQUIRED"), false);
            }
            //Login as admin and go to the schedule
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            goToSchedulePageScheduleTab();
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated) {
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }
            String workRole = shiftOperatePage.getRandomWorkRole();
            //Check the employee pending count on SCHEDULE ACKNOWLEDGEMENT smart card
            createSchedulePage.publishActiveSchedule();
            int pendingEmployeeCountBeforeEdit = smartCardPage.getCountFromSmartCardByName("SCHEDULE ACKNOWLEDGEMENT");
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            createShiftsWithSpecificValues(workRole, "", "", "1:00pm", "3:00pm",
                    1, Arrays.asList(0), ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(), "", firstName+" "+lastName);
            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();
            int pendingEmployeeCountAfterEdit = smartCardPage.getCountFromSmartCardByName("SCHEDULE ACKNOWLEDGEMENT");
            SimpleUtils.assertOnFail("The expected count is:"+ (pendingEmployeeCountBeforeEdit+1)
                            + ". The actual count is: "+pendingEmployeeCountAfterEdit,
                    pendingEmployeeCountBeforeEdit+1 == pendingEmployeeCountAfterEdit, false);
            //Login as TM and check the notification smart card
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.TeamMemberOtherLocation1.getValue());
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            if(smartCardPage.isSpecificSmartCardLoaded("ACTION REQUIRED")) {
                smartCardPage.clickOnAcknowledgeButtonOnAcknowledgeNotificationSmartCard();
                refreshPage();
                scheduleCommonPage.clickOnScheduleConsoleMenuItem();
                scheduleCommonPage.navigateToNextWeek();
                scheduleCommonPage.navigateToNextWeek();
                scheduleCommonPage.navigateToNextWeek();
                scheduleCommonPage.navigateToNextWeek();
                SimpleUtils.assertOnFail("The SCHEDULE ACKNOWLEDGEMENT smart card should display before publish schedule! ",
                        !smartCardPage.isSpecificSmartCardLoaded("ACTION REQUIRED"), false);
            }else
                SimpleUtils.fail("The acknowledge notification smart card fail to load after edit the shift on non-home location! ",false);

            //Login as admin and go to the schedule
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            goToSchedulePageScheduleTab();
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            int pendingEmployeeCountAfterAcknowledge = smartCardPage.getCountFromSmartCardByName("SCHEDULE ACKNOWLEDGEMENT");
            SimpleUtils.assertOnFail("The expected count is:"+ (pendingEmployeeCountAfterEdit-1)
                            + ". The actual count is: "+pendingEmployeeCountAfterAcknowledge,
                    pendingEmployeeCountAfterEdit-1 == pendingEmployeeCountAfterAcknowledge, false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate employee can acknowledge the notification from non-home location when home location schedule is not published")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyEmployeeCanAcknowledgeTheNotificationFromNonHomeLocationWhenHomeScheduleIsNotPublishedAsTeamMemberOtherLocation1(String browser, String username, String password, String location) throws Exception {
        try {
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            profileNewUIPage.clickOnUserProfileImage();
            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Profile");
            String tmFullName = profileNewUIPage.getUserProfileName().get("fullName");
            String firstName = tmFullName.split(" ")[0];
            String homeLocation = location;
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(homeLocation);
            goToSchedulePageScheduleTab();
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            boolean isSchedulePublished = createSchedulePage.isCurrentScheduleWeekPublished();
            if(isSchedulePublished) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated) {
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }
            scheduleMainPage.clickOnOpenSearchBoxButton();
            scheduleMainPage.searchShiftOnSchedulePage(tmFullName);
            if (scheduleShiftTablePage.getShiftsCount()>0) {
                scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
                scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstName);
                scheduleMainPage.saveSchedule();
            }
            createSchedulePage.publishActiveSchedule();
            //Login as TM and acknoledge the notification
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.TeamMemberOtherLocation1.getValue());
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            if(smartCardPage.isSpecificSmartCardLoaded("ACTION REQUIRED")) {
                smartCardPage.clickOnAcknowledgeButtonOnAcknowledgeNotificationSmartCard();
                refreshPage();
                scheduleCommonPage.clickOnScheduleConsoleMenuItem();
                scheduleCommonPage.navigateToNextWeek();
                scheduleCommonPage.navigateToNextWeek();
                scheduleCommonPage.navigateToNextWeek();
                scheduleCommonPage.navigateToNextWeek();
                SimpleUtils.assertOnFail("The SCHEDULE ACKNOWLEDGEMENT smart card should display before publish schedule! ",
                        !smartCardPage.isSpecificSmartCardLoaded("ACTION REQUIRED"), false);
            }
            //Login as admin and go to the schedule
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            goToSchedulePageScheduleTab();
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated) {
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }
            String workRole = shiftOperatePage.getRandomWorkRole();
            //Check the employee pending count on SCHEDULE ACKNOWLEDGEMENT smart card
            createSchedulePage.publishActiveSchedule();
            int pendingEmployeeCountBeforeEdit = smartCardPage.getCountFromSmartCardByName("SCHEDULE ACKNOWLEDGEMENT");
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            createShiftsWithSpecificValues(workRole, "", "", "1:00pm", "3:00pm",
                    1, Arrays.asList(0), ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(), "", firstName);
            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();
            int pendingEmployeeCountAfterEdit = smartCardPage.getCountFromSmartCardByName("SCHEDULE ACKNOWLEDGEMENT");
            SimpleUtils.assertOnFail("The expected count is:"+ (pendingEmployeeCountBeforeEdit+1)
                            + ". The actual count is: "+pendingEmployeeCountAfterEdit,
                    pendingEmployeeCountBeforeEdit+1 == pendingEmployeeCountAfterEdit, false);
            //Login as TM and check the notification smart card
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.TeamMemberOtherLocation1.getValue());
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            if(smartCardPage.isSpecificSmartCardLoaded("ACTION REQUIRED")) {
                smartCardPage.clickOnAcknowledgeButtonOnAcknowledgeNotificationSmartCard();
                refreshPage();
                scheduleCommonPage.clickOnScheduleConsoleMenuItem();
                scheduleCommonPage.navigateToNextWeek();
                scheduleCommonPage.navigateToNextWeek();
                scheduleCommonPage.navigateToNextWeek();
                scheduleCommonPage.navigateToNextWeek();
                SimpleUtils.assertOnFail("The SCHEDULE ACKNOWLEDGEMENT smart card should display before publish schedule! ",
                        !smartCardPage.isSpecificSmartCardLoaded("ACTION REQUIRED"), false);
            }else
                SimpleUtils.fail("The acknowledge notification smart card fail to load after edit the shift on non-home location! ",false);

            //Login as admin and go to the schedule
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            goToSchedulePageScheduleTab();
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            int pendingEmployeeCountAfterAcknowledge = smartCardPage.getCountFromSmartCardByName("SCHEDULE ACKNOWLEDGEMENT");
            SimpleUtils.assertOnFail("The expected count is:"+ (pendingEmployeeCountAfterEdit-1)
                            + ". The actual count is: "+pendingEmployeeCountAfterAcknowledge,
                    pendingEmployeeCountAfterEdit-1 == pendingEmployeeCountAfterAcknowledge, false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }
}
