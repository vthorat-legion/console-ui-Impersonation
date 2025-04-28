package com.legion.api.abSwitch;

public enum AbSwitches {
    TwentyFourHourTimeFormat("24hTimeFormat"),

    SkipUsernamePasswordLogin("SkipUsernamePasswordLogin"),

    OfferTM("OfferTM"),

    AutoEmailVerification("AutoEmailVerification"),

    ParallelPublishSchedule("ParallelPublishSchedule"),

    PostPublishQueue("PostPublishQueue"),

    WorkerTasks("WorkerTasks"),

    FTUnfairAllocation("FTUnfairAllocation"),

    RetailDesign("RetailDesign"),

    OperationManagement("OperationManagement"),

    RecommendationTester("RecommendationTester"),

    MultipleShiftsPerDay("MultipleShiftsPerDay"),

    ControlPage("ControlPage"),

    SchedulingLogs("SchedulingLogs"),

    SettingPage("SettingPage"),

    TwentyFourHoursAvailability("24HoursAvailability"),

    EMAIL_CC("EMAIL_CC"),

    ComplianceViolation("ComplianceViolation"),

    ShiftOfferBatchGeneration("ShiftOfferBatchGeneration"),

    Badges("Badges"),

    TrafficDetailKPIReport("TrafficDetailKPIReport"),

    ForecastDetailKPIReport("ForecastDetailKPIReport"),

    ScheduleDetailKPIReport("ScheduleDetailKPIReport"),

    OfferGenerationByLocationSubscription("OfferGenerationByLocationSubscription"),

    WorkerBusinessProfileApproval("WorkerBusinessProfileApproval"),

    TmRosterRemovalEmailCc("TmRosterRemovalEmailCc"),

    EmailForecastTaskFailedResult("EmailForecastTaskFailedResult"),

    Announcements("Announcements"),

    ForecastingService("ForecastingService"),

    ZeroTransaction("ZeroTransaction"),

    EAIntegrationTask("EAIntegrationTask"),

    Forecast2SchedulePage("Forecast2SchedulePage"),

    NewAnalytics("NewAnalytics"),

    SampleClock("SampleClock"),

    DGCustomFormat("DGCustomFormat"),

    NewTimesheetUX("NewTimesheetUX"),

    AllPayPeriods("AllPayPeriods"),

    ScheduleDisablePrintOptions("ScheduleDisablePrintOptions"),

    ScheduleEditMealBreaks("ScheduleEditMealBreaks"),

    ScheduleUseDragAndDrop("ScheduleUseDragAndDrop"),

    ScheduleGenerateCopyManagerView("ScheduleGenerateCopyManagerView"),

    PremiumCalculatorSwitch("PremiumCalculatorSwitch"),

    CustomizableDashboard("CustomizableDashboard"),

    ScheduleGeneratePopupFlow("ScheduleGeneratePopupFlow"),

    Activities("Activities"),

    NewLawsonMode("NewLawsonMode"),

    ToDo("ToDo"),

    OPView("OPView"),

    UseConfigTemplateSwitch("UseConfigTemplateSwitch"),

    ForecastDemandSingleSelect("ForecastDemandSingleSelect"),

    DownloadAllLocationReports("DownloadAllLocationReports"),

    TimesheetNewLocationSearch("TimesheetNewLocationSearch"),

    BlockClockinPendingQueueConsumer("BlockClockinPendingQueueConsumer"),

    EnableOMJobs("EnableOMJobs"),

    EnableLocationLifeCycle("EnableLocationLifeCycle"),

    TALimitAllLocations("TALimitAllLocations"),

    NewProfileUX("NewProfileUX"),

    ForecastEdit("ForecastEdit"),

    EnableLocationGroupV2("EnableLocationGroupV2"),

    ForecastAccuracy("ForecastAccuracy"),

    FullStory("FullStory"),

    Intercom("Intercom"),

    RealTimeTimeSheetApprovalKpi("RealTimeTimeSheetApprovalKpi"),

    WalkMe("WalkMe"),

    LimitLocationSearchSwitch("LimitLocationSearchSwitch"),

    WorkforceSharingForAutoSchedule("WorkforceSharingForAutoSchedule"),

    MoveIntegrationToOP("MoveIntegrationToOP"),

    UseShortURL("UseShortURL"),

    ScheduleDisableDayMode("ScheduleDisableDayMode"),

    DisableScheduleCopy("DisableScheduleCopy"),

    EnableOhTemplateSwitch("EnableOhTemplateSwitch"),

    ExcludeBudgetAdjustments("ExcludeBudgetAdjustments"),

    SchedulePartialCopy("SchedulePartialCopy"),

    PaylocityAsyncProcessing("PaylocityAsyncProcessing"),

    ScheduleRestBreaks("ScheduleRestBreaks"),

    EnableDynamicGroupForAssociation("EnableDynamicGroupForAssociation"),

    enableStaffingRuleNew("enableStaffingRuleNew"),

    UseOldNavigation("UseOldNavigation"),

    ScheduleAgreementPolicy("ScheduleAgreementPolicy"),

    EnableNewScheduleTemplates("enableNewScheduleTemplates"),

    UseLegionAccural("UseLegionAccural"),

    LocationStat("LocationStat"),

    ScheduleEditShiftTimeNew( "ScheduleEditShiftTimeNew"),

    NewCreateShift("NewCreateShift");
    private final String value;

    AbSwitches(final String newValue) {
        value = newValue;
    }

    public String getValue() {
        return value;
    }
}
