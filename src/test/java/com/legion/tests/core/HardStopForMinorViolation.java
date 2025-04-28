package com.legion.tests.core;

import com.legion.api.cache.CacheAPI;
import com.legion.pages.*;
import com.legion.pages.OpsPortaPageFactories.ConfigurationPage;
import com.legion.pages.OpsPortaPageFactories.LocationsPage;
import com.legion.pages.core.OpsPortal.OpsPortalConfigurationPage;
import com.legion.tests.TestBase;
import com.legion.tests.annotations.Automated;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.annotations.Owner;
import com.legion.tests.annotations.TestName;
import com.legion.tests.core.OpsPortal.LocationsTest;
import com.legion.tests.data.CredentialDataProviderSource;
import com.legion.utils.JsonUtil;
import com.legion.utils.MyThreadLocal;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class HardStopForMinorViolation extends TestBase {

    private static HashMap<String, String> cinemarkSetting14N15 = JsonUtil.getPropertiesFromJsonFile("src/test/resources/CinemarkMinorSettings.json");
    private static HashMap<String, String> cinemarkSetting16N17 = JsonUtil.getPropertiesFromJsonFile("src/test/resources/CinemarkMinorSettings16N17.json");
    private static HashMap<String, String> cinemarkMinors = JsonUtil.getPropertiesFromJsonFile("src/test/resources/CinemarkMinorsData.json");
    private static HashMap<String, String> scheduleWorkRoles = JsonUtil.getPropertiesFromJsonFile("src/test/resources/WorkRoleOptions.json");
    private static String minorWorkRole = scheduleWorkRoles.get("TEAM_MEMBER_CORPORATE_THEATRE");

    @Override
    @BeforeMethod()
    public void firstTest(Method testMethod, Object[] params) throws Exception {
        try {
            this.createDriver((String) params[0], "69", "Window");
            visitPage(testMethod);
            loginToLegionAndVerifyIsLoginDone((String) params[1], (String) params[2], (String) params[3]);
            if (MyThreadLocal.getCurrentComplianceTemplate()==null || MyThreadLocal.getCurrentComplianceTemplate().equals("")){
                getAndSetDefaultTemplate((String) params[3]);
            }
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the functionality of setting 'Strictly enforce minor violations?'")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheFunctionalityOfSettingStrictlyEnforceMinorViolationsAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

            //Go to Control Center -> Configuration tab ,click on Compliance title
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(OpsPortalConfigurationPage.configurationLandingPageTemplateCards.Compliance.getValue());

            //Click on New Tempate button, input name and description, click on Continue button
            String templateName = "complianceTemplate-ForAuto";
            configurationPage.deleteTemplate(templateName);
            configurationPage.createNewTemplate(templateName);
            configurationPage.clickOnSpecifyTemplateName(templateName, "edit");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            Thread.sleep(5000);
            //Check the setting "Strictly enforce minor violations?", the default value is No
            SimpleUtils.assertOnFail("The 'Strictly enforce minor violations?' should be setting as No by default! ",
                    !configurationPage.isStrictlyEnforceMinorViolationSettingEnabled(), false);

            //Yes/No buttons are clickable
            configurationPage.setStrictlyEnforceMinorViolations("Yes");
            SimpleUtils.assertOnFail("The 'Strictly enforce minor violations?' should be setting as Yes! ",
                    configurationPage.isStrictlyEnforceMinorViolationSettingEnabled(), false);

            configurationPage.clickOnBackButton();
            configurationPage.deleteTemplate(templateName);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify Minor Violation will show in Action Required smart card")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyMinorViolationWillShowInActionRequiredSmartCardAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

            setStrictlyEnforceMinorViolationSetting("Yes");

            //Go to schedule and make one minor shift has violation
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()) , false);
            scheduleCommonPage.navigateToNextWeek();

            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            Thread.sleep(5000);
            createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("08:00AM", "08:00PM");
            if (smartCardPage.isRequiredActionSmartCardLoaded()) {
                shiftOperatePage.convertAllUnAssignedShiftToOpenShift();
            }
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            String minorName = cinemarkMinors.get("Minor15-2");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("minor");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();
            Thread.sleep(5000);
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            Thread.sleep(5000);
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(1);
            newShiftPage.moveSliderAtCertainPoint("2pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.selectWorkRole(minorWorkRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            MyThreadLocal.setAssignTMStatus(true);
            newShiftPage.searchTeamMemberByName(minorName);
//            shiftOperatePage.clickOnRadioButtonOfSearchedTeamMemberByName(minorName);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            List<WebElement> minorShifts = scheduleShiftTablePage.getAllShiftsOfOneTM(minorName.split(" ")[0]);
            SimpleUtils.assertOnFail("The minor shift fail to created! ", minorShifts.size()>0, false);
            shiftOperatePage.editTheShiftTimeForSpecificShift(minorShifts.get(0),
                    "8am", "8pm");
            Thread.sleep(5000);
            scheduleMainPage.saveSchedule();
            //ACTION REQUIRED smart card will show
            SimpleUtils.assertOnFail("Action Required smart card should be loaded! ",
                    smartCardPage.isRequiredActionSmartCardLoaded(), false);

            String minorMessage = smartCardPage.getMessageFromActionRequiredSmartCard().get("minorViolation").replace("\n", " ");
            String expectedMinorViolation = "1 shift Minor Violation";
            SimpleUtils.assertOnFail("The minor violation message display incorrect! The expected is: "+expectedMinorViolation
                            + " The actual is : " + minorMessage,
                    minorMessage.equalsIgnoreCase(expectedMinorViolation), false);


            //Publish button is disabled
            String tooltip = scheduleMainPage.getTooltipOfPublishButton();
            SimpleUtils.assertOnFail("The tooltip of publish button should display as: Please address required action(s)! But the actual tooltip is: "+ tooltip,
                    tooltip.equalsIgnoreCase("Please address required action(s)"), false);
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            minorShifts = scheduleShiftTablePage.getAllShiftsOfOneTM(minorName.split(" ")[0]);
            shiftOperatePage.editTheShiftTimeForSpecificShift(minorShifts.get(0),
                    "11am", "2pm");
            scheduleMainPage.saveSchedule();

            tooltip = scheduleMainPage.getTooltipOfPublishButton();
            SimpleUtils.assertOnFail("The tooltip of publish button should display as: Please address required action(s)! But the actual tooltip is: "+ tooltip,
                    tooltip.equalsIgnoreCase(""), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify hard stop when assigning the shift to the minor will trigger minor violation")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyHardStopWhenAssigningTheShiftToTheMinorWillTriggerMinorViolationAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

            // Go to Control Center -> Configuration tab ,click on Compliance title
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(OpsPortalConfigurationPage.configurationLandingPageTemplateCards.Compliance.getValue());
            configurationPage.clickOnSpecifyTemplateName(MyThreadLocal.getCurrentComplianceTemplate(), "edit");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();

            // Click the Yes buttons of setting "Strictly enforce minor violations?"
            configurationPage.setStrictlyEnforceMinorViolations("Yes");
            SimpleUtils.assertOnFail("The 'Strictly enforce minor violations?' should be setting as Yes! ",
                    configurationPage.isStrictlyEnforceMinorViolationSettingEnabled(), false);

            //Publish the template
            configurationPage.publishNowTheTemplate();
            locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.Console.getValue());
            Thread.sleep(3000);
            //Go to schedule and make one minor shift has violation
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()) , false);
            scheduleCommonPage.navigateToNextWeek();

            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("08:00AM", "08:00PM");

            if (smartCardPage.isRequiredActionSmartCardLoaded()) {
                shiftOperatePage.convertAllUnAssignedShiftToOpenShift();
            }
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            String minorName = cinemarkMinors.get("Minor15-2");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("minor");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectWeekDaysByDayName("Mon");
            newShiftPage.moveSliderAtCertainPoint("8pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("8am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.selectWorkRole(minorWorkRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchWithOutSelectTM(minorName);
            newShiftPage.selectTeamMembers();
//            newShiftPage.searchTeamMemberByName(minorName);
            //Hard stop dialog with OK button will show
            SimpleUtils.assertOnFail("The warning modal should be loaded! ",
                    newShiftPage.ifWarningModeDisplay(), false);

            //Related minor warning message will show.
            String messageFromWarningModal = scheduleShiftTablePage.getWarningMessageInDragShiftWarningMode().trim().replace("\\\n", "");
            String expectedMessage = "As a minor, "+minorName.split(" ")[0]+" should be scheduled from 8am - 4pm";
            SimpleUtils.assertOnFail("The message display incorrectly, the expected is : "+expectedMessage + " The actual is: "+messageFromWarningModal,
                    messageFromWarningModal.equalsIgnoreCase(expectedMessage), false);
            newShiftPage.clickOnOkButtonOnWarningModal();
            Thread.sleep(3000);
            SimpleUtils.assertOnFail("The warning modal should not be loaded! ",
                    !newShiftPage.ifWarningModeDisplay(), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify minor cannot claim the open shift if it will trigger minor violation")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyMinorCannotClaimOpenShiftWithMinorViolationAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

            // Go to Control Center -> Configuration tab ,click on Compliance title
            setStrictlyEnforceMinorViolationSetting("Yes");

            //Go to schedule and make one minor shift has violation
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()) , false);
            scheduleCommonPage.navigateToNextWeek();
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated){
                createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("08:00AM", "08:00PM");
            }

            if (smartCardPage.isRequiredActionSmartCardLoaded()) {
                shiftOperatePage.convertAllUnAssignedShiftToOpenShift();
            }
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            String minorName = cinemarkMinors.get("Minor16-2");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("minor");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();

            //Create open shift with no minor violation
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            Thread.sleep(5000);
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectWeekDaysByDayName("Mon");
            newShiftPage.moveSliderAtCertainPoint("2pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.selectWorkRole(minorWorkRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.ManualShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(minorName);
            newShiftPage.clickOnOfferOrAssignBtn();
            Thread.sleep(5000);
            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();
            List<WebElement> openShifts = scheduleShiftTablePage.getAllShiftsOfOneTM("open");
            int i = 0;
            String offerStatus = "";
            while (i< 10 && !offerStatus.contains("offered")) {
                scheduleShiftTablePage.clickProfileIconOfShiftByIndex(scheduleShiftTablePage.getAddedShiftIndexes("Open").get(0));
                scheduleShiftTablePage.clickViewStatusBtn();
                offerStatus = shiftOperatePage.getOfferStatusFromOpenShiftStatusList(minorName);
                shiftOperatePage.closeViewStatusContainer();
                Thread.sleep(60000);
                i++;
            }

            //Edit the open shift to has minor violation
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            shiftOperatePage.editTheShiftTimeForSpecificShift(openShifts.get(0),"8am", "8pm");
            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();

            //Login as TM to claim the open shift that has minor violation
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            loginPage.logOut();
            loginPage.loginToLegionWithCredential("nora+aminor16@legion.co", "legionco1");
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            SimpleUtils.assertOnFail("Open shift should not loaded!", !scheduleShiftTablePage.areShiftsPresent(), false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify Minor Violation will not show in smart card if changing the setting from Yes to No")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyMinorViolationWillNotShowInSmartCardIfChangingSettingFromYesTONoAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

            // Go to Control Center -> Configuration tab ,click on Compliance title
            setStrictlyEnforceMinorViolationSetting("Yes");

            //Go to schedule and make one minor shift has violation
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()) , false);
            scheduleCommonPage.navigateToNextWeek();
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("08:00AM", "08:00PM");
            if (!smartCardPage.isRequiredActionSmartCardLoaded()) {
                scheduleMainPage.goToToggleSummaryView();
                List<String> toCloseDays = new ArrayList<>();
                toCloseDays.add("Tuesday");
                newShiftPage.editOperatingHoursOnScheduleOldUIPage("8:00am", "8:00pm", toCloseDays);
                scheduleMainPage.goToToggleSummaryView();

            }
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            String minorName = cinemarkMinors.get("Minor16-2");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("minor");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();

            //Create open shift with no minor violation
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectWeekDaysByDayName("Mon");
            newShiftPage.moveSliderAtCertainPoint("2pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.selectWorkRole(minorWorkRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            MyThreadLocal.setAssignTMStatus(true);
            newShiftPage.searchTeamMemberByName(minorName);
            newShiftPage.clickOnOfferOrAssignBtn();
            Thread.sleep(3000);
            scheduleMainPage.saveSchedule();

            //Edit the open shift to has minor violation
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            List<WebElement> minorShifts = scheduleShiftTablePage.getAllShiftsOfOneTM(minorName.split(" ")[0]);
            SimpleUtils.assertOnFail("The minor shift fail to created! ",
                    minorShifts.size()>0, false);
//            shiftOperatePage.editTheShiftTimeForSpecificShift(minorShifts.get(0), "8am", "8pm");

            EditShiftPage editShiftPage = pageFactory.createEditShiftPage();
            HashSet<Integer> indexes = new HashSet<>();
            indexes.add(scheduleShiftTablePage.getShiftIndexById(minorShifts.get(0).getAttribute("id").toString()));
            scheduleShiftTablePage.rightClickOnSelectedShifts(indexes);
            String action = "Edit";
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!",
                    editShiftPage.isEditShiftWindowLoaded(), false);
            String inputStartTime = "8:00 AM";
            editShiftPage.inputStartOrEndTime(inputStartTime, true);
            String inputEndTime = "8:00 PM";
            editShiftPage.inputStartOrEndTime(inputEndTime, false);
            editShiftPage.clickOnUpdateButton();
            shiftOperatePage.clickOnAssignAnywayButton();

            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();
            String minorMessage = smartCardPage.getMessageFromActionRequiredSmartCard().get("minorViolation").replace("\n", " ");
            String expectedMinorViolation = "1 shift Minor Violation";
            SimpleUtils.assertOnFail("The minor violation message display incorrect! The expected is: "+expectedMinorViolation
                            + " The actual is : " + minorMessage,
                    minorMessage.equalsIgnoreCase(expectedMinorViolation), false);

            scheduleCommonPage.navigateToNextWeek();
            isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("08:00AM", "08:00PM");

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("minor");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("unassigned");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();

            //Create open shift with no minor violation
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectWeekDaysByDayName("Mon");
            newShiftPage.moveSliderAtCertainPoint("2pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.selectWorkRole(minorWorkRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(minorName);
            newShiftPage.clickOnOfferOrAssignBtn();
            Thread.sleep(3000);
            scheduleMainPage.saveSchedule();

            //Edit the open shift to has minor violation
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            minorShifts = scheduleShiftTablePage.getAllShiftsOfOneTM(minorName.split(" ")[0]);
//            shiftOperatePage.editTheShiftTimeForSpecificShift(minorShifts.get(0),"8am", "8pm");

            indexes.clear();
            indexes.add(scheduleShiftTablePage.getShiftIndexById(minorShifts.get(0).getAttribute("id").toString()));
            scheduleShiftTablePage.rightClickOnSelectedShifts(indexes);
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!",
                    editShiftPage.isEditShiftWindowLoaded(), false);
            editShiftPage.inputStartOrEndTime(inputStartTime, true);
            editShiftPage.inputStartOrEndTime(inputEndTime, false);
            editShiftPage.clickOnUpdateButton();
            shiftOperatePage.clickOnAssignAnywayButton();
            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();
            minorMessage = smartCardPage.getMessageFromActionRequiredSmartCard().get("minorViolation").replace("\n", " ");
            expectedMinorViolation = "1 shift Minor Violation";
            SimpleUtils.assertOnFail("The minor violation message display incorrect! The expected is: "+expectedMinorViolation
                            + " The actual is : " + minorMessage,
                    minorMessage.equalsIgnoreCase(expectedMinorViolation), false);

            //Change the setting "Strictly enforce minor violations?" from Yes to No
            setStrictlyEnforceMinorViolationSetting("No");
            int i = 0;
            while (i<5 && smartCardPage.isRequiredActionSmartCardLoaded()){
                Thread.sleep(10000);
                scheduleCommonPage.clickOnScheduleConsoleMenuItem();
                SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                        scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , false);
                scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
                SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                        scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()) , false);
                scheduleCommonPage.navigateToNextWeek();
//                minorMessage = smartCardPage.getMessageFromActionRequiredSmartCard().get("minorViolation");
                i++;
            }
//            SimpleUtils.assertOnFail("The minor violation message display incorrect! The expected is empty"
//                            + " The actual is : " + minorMessage,
//                    minorMessage.equals(""), false);
//
//            scheduleCommonPage.navigateToNextWeek();
            SimpleUtils.assertOnFail("The action required smart card should not display! ",
                    !smartCardPage.isRequiredActionSmartCardLoaded(), false);


        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }



    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify Minor Violation will show in smart card if changing the setting from No to Yes")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyMinorViolationWillShowInSmartCardIfChangingSettingFromNoTOYesAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

            // Go to Control Center -> Configuration tab ,click on Compliance title
            setStrictlyEnforceMinorViolationSetting("No");

            //Go to schedule and make one minor shift has violation
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()) , false);
            scheduleCommonPage.navigateToNextWeek();
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("08:00AM", "08:00PM");
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            String minorName = cinemarkMinors.get("Minor16-2");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("minor");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("unassigned");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();

            //Create open shift with no minor violation
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectWeekDaysByDayName("Mon");
            newShiftPage.moveSliderAtCertainPoint("2pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.selectWorkRole(minorWorkRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(minorName);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();

            //Edit the open shift to has minor violation
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            List<WebElement> minorShifts = scheduleShiftTablePage.getAllShiftsOfOneTM(minorName.split(" ")[0]);
            shiftOperatePage.editTheShiftTimeForSpecificShift(minorShifts.get(0), "8am", "8pm");
            scheduleMainPage.saveSchedule();
            int i=0;
            while (i<5 && smartCardPage.isRequiredActionSmartCardLoaded()) {
                scheduleCommonPage.clickOnScheduleConsoleMenuItem();
                SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                        scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , false);
                scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
                SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                        scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()) , false);
                scheduleCommonPage.navigateToNextWeek();
                Thread.sleep(10000);
                i++;
            }
            SimpleUtils.assertOnFail("The action required smart card should not display! ",
                    !smartCardPage.isRequiredActionSmartCardLoaded(), false);


            scheduleCommonPage.navigateToNextWeek();
            isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("08:00AM", "08:00PM");

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("minor");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("unassigned");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();

            //Create open shift with no minor violation
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectWeekDaysByDayName("Mon");
            newShiftPage.moveSliderAtCertainPoint("2pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.selectWorkRole(minorWorkRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(minorName);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();

            //Edit the open shift to has minor violation
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            minorShifts = scheduleShiftTablePage.getAllShiftsOfOneTM(minorName.split(" ")[0]);
            shiftOperatePage.editTheShiftTimeForSpecificShift(minorShifts.get(0),"8am", "8pm");
            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();
            SimpleUtils.assertOnFail("The action required smart card should not display! ",
                    !smartCardPage.isRequiredActionSmartCardLoaded(), false);

            //Change the setting "Strictly enforce minor violations?" from Yes to Yes
            setStrictlyEnforceMinorViolationSetting("Yes");
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()) , false);
            scheduleCommonPage.navigateToNextWeek();
            i=0;
            String tooltip = scheduleMainPage.getTooltipOfPublishButton();
            while (i < 10 && (!smartCardPage.isRequiredActionSmartCardLoaded() || tooltip.equals(""))) {
                Thread.sleep(10000);
                scheduleCommonPage.clickOnScheduleConsoleMenuItem();
                SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                        scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , false);
                scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
                SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                        scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()) , false);
                scheduleCommonPage.navigateToNextWeek();
                Thread.sleep(5000);
                tooltip = scheduleMainPage.getTooltipOfPublishButton();
                i++;
            }
            SimpleUtils.assertOnFail("The action required smart card should not display! ",
                    smartCardPage.isRequiredActionSmartCardLoaded(), false);
            tooltip = scheduleMainPage.getTooltipOfPublishButton();
            SimpleUtils.assertOnFail("The tooltip of publish button should display as: Please address required action(s)! But the actual tooltip is: "+ tooltip,
                    tooltip.equalsIgnoreCase("Please address required action(s)"), false);

            scheduleCommonPage.navigateToNextWeek();
            SimpleUtils.assertOnFail("The action required smart card should not display! ",
                    smartCardPage.isRequiredActionSmartCardLoaded(), false);

            SimpleUtils.assertOnFail("The Publish/Republish button should not display! "+ tooltip,
                    !createSchedulePage.isPublishButtonLoadedOnSchedulePage() && !createSchedulePage.isRepublishButtonLoadedOnSchedulePage(), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }




    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify Minor Violation will show in smart card if changing the setting of the minor template")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyMinorViolationWillShowInSmartCardIfChangingSettingOfMinorTemplateAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            LoginPage loginPage = pageFactory.createConsoleLoginPage();

            // Go to Control Center -> Configuration tab ,click on Compliance title
            setStrictlyEnforceMinorViolationSetting("Yes");

            //Go to schedule and make one minor shift has violation
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            goToSchedulePageScheduleTab();
            scheduleCommonPage.navigateToNextWeek();
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("08:00AM", "08:00PM");

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("minor");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("unassigned");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();

            //Create open shift with no minor violation
            String minorName = cinemarkMinors.get("Minor16-2");
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.clickCloseBtnForCreateShift();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectWeekDaysByDayName("Mon");
            newShiftPage.moveSliderAtCertainPoint("1pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("9am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.selectWorkRole(minorWorkRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(minorName);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();

            //There is no minor violations in schedule
            SimpleUtils.assertOnFail("The action required smart card should not display! ",
                    !smartCardPage.isRequiredActionSmartCardLoaded(), false);


            //Change the settings in minor template that will trigger the violations of the existing minor shifts
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(OpsPortalConfigurationPage.configurationLandingPageTemplateCards.MinorsRules.getValue());
            String minor15TemplateName = "Minor16Template-ForAuto";
            configurationPage.clickOnSpecifyTemplateName(minor15TemplateName, "edit");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            CinemarkMinorPage cinemarkMinorPage = pageFactory.createConsoleCinemarkMinorPage();
            String fromTimeBefore = cinemarkSetting14N15.get(CinemarkMinorTest.minorRuleDayType.SchoolToday_SchoolTomorrow.getValue()).split(",")[0];
            String fromTimeAfterChanged = Integer.parseInt(fromTimeBefore.split(":")[0])+2 + ":"+fromTimeBefore.split(":")[1];
            cinemarkMinorPage.setMinorRuleByDay(CinemarkMinorTest.minorRuleDayType.SchoolToday_SchoolTomorrow.getValue(), fromTimeAfterChanged, cinemarkSetting14N15.get(CinemarkMinorTest.minorRuleDayType.SchoolToday_SchoolTomorrow.getValue()).split(",")[1], cinemarkSetting14N15.get(CinemarkMinorTest.minorRuleDayType.SchoolToday_SchoolTomorrow.getValue()).split(",")[2]);
            configurationPage.publishNowTheTemplate();
            locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.Console.getValue());
//            int j = 0;
//            while (j<6) {
//                Thread.sleep(60000);
//                scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//                j++;
//            }
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()) , false);
            scheduleCommonPage.navigateToNextWeek();
            int i = 0;
            while (i<10 && !smartCardPage.isRequiredActionSmartCardLoaded()) {
//                loginPage.logOut();
                Thread.sleep(10000);
//                loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
                scheduleCommonPage.clickOnScheduleConsoleMenuItem();
                SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                        scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , false);
                scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
                SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                        scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()) , false);
                scheduleCommonPage.navigateToNextWeek();
                i++;
                Thread.sleep(60000);
            }
            //There has minor violations in schedule
            SimpleUtils.assertOnFail("The action required smart card should display! ",
                    smartCardPage.isRequiredActionSmartCardLoaded(), false);

            String minorMessage = smartCardPage.getMessageFromActionRequiredSmartCard().get("minorViolation").replace("\n", " ");
            String expectedMinorViolation = "1 shift Minor Violation";
            SimpleUtils.assertOnFail("The minor violation message display incorrect! The expected is: "+expectedMinorViolation
                            + " The actual is : " + minorMessage,
                    minorMessage.equalsIgnoreCase(expectedMinorViolation), false);

            //Publish button is disabled
            String tooltip = scheduleMainPage.getTooltipOfPublishButton();
            SimpleUtils.assertOnFail("The tooltip of publish button should display as: Please address required action(s)! But the actual tooltip is: "+ tooltip,
                    tooltip.equalsIgnoreCase("Please address required action(s)"), false);
//            setStrictlyEnforceMinorViolationSetting("No");
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }

    private void setStrictlyEnforceMinorViolationSetting(String yesOrNo) throws Exception {
        LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
        locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
        SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
        ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
        configurationPage.goToConfigurationPage();
        configurationPage.clickOnConfigurationCrad(OpsPortalConfigurationPage.configurationLandingPageTemplateCards.Compliance.getValue());
        configurationPage.clickOnSpecifyTemplateName(MyThreadLocal.getCurrentComplianceTemplate(), "edit");
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();

        //Get 'Strictly enforce minor violations?' value before edit
//        boolean statusBeforeEdit = configurationPage.isStrictlyEnforceMinorViolationSettingEnabled();

        // Click the Yes buttons of setting "Strictly enforce minor violations?"
        configurationPage.setStrictlyEnforceMinorViolations(yesOrNo);
        if (yesOrNo.equalsIgnoreCase("Yes")) {
            SimpleUtils.assertOnFail("The 'Strictly enforce minor violations?' should be setting as Yes! ",
                    configurationPage.isStrictlyEnforceMinorViolationSettingEnabled(), false);
        } else
            SimpleUtils.assertOnFail("The 'Strictly enforce minor violations?' should be setting as No! ",
                    !configurationPage.isStrictlyEnforceMinorViolationSettingEnabled(), false);

//        boolean statusAfterEdit = configurationPage.isStrictlyEnforceMinorViolationSettingEnabled();
        //Publish the template
        configurationPage.publishNowTheTemplate();
        locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.Console.getValue());
        refreshCachesAfterChangeTemplate();
        Thread.sleep(5000);
        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//        if (statusAfterEdit != statusBeforeEdit) {
//            int i = 0;
//            while (i< 20) {
//                //Wait for the timed cache
//                scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//                Thread.sleep(60000);
//                CacheAPI.refreshTemplateCache(getUserNameNPwdForCallingAPI().get(0), getUserNameNPwdForCallingAPI().get(1));
//                i++;
//            }
//        }
//        LoginPage loginPage = pageFactory.createConsoleLoginPage();
//        loginPage.logOut();
//        Thread.sleep(5000);
//        loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
//        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
    }

    public void getAndSetDefaultTemplate(String currentLocation) throws Exception{
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

        //Go to OP page
        LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
        locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
        SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
        locationsPage.clickOnLocationsTab();
        locationsPage.goToSubLocationsInLocationsPage();
        locationsPage.searchLocation(currentLocation);               ;
        SimpleUtils.assertOnFail("Locations not searched out Successfully!",  locationsPage.verifyUpdateLocationResult(currentLocation), false);
        locationsPage.clickOnLocationInLocationResult(currentLocation);
        locationsPage.clickOnConfigurationTabOfLocation();
        HashMap<String, String> templateTypeAndName = locationsPage.getTemplateTypeAndNameFromLocation();
        MyThreadLocal.setCurrentComplianceTemplate(templateTypeAndName.get("Compliance"));
        //back to console.
        switchToConsoleWindow();
    }
}
