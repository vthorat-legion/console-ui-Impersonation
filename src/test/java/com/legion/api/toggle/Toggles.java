package com.legion.api.toggle;

public enum Toggles {

    UseLegionAccrual("UseLegionAccrual"),
    MinorRulesTemplate("MinorRulesTemplate"),
    DynamicGroupV2("DynamicGroupV2"),
    ScheduleShowFullNames("ScheduleShowFullNames"),
    EnableDemandDriverTemplate("EnableDemandDriverTemplate"),
    MixedModeDemandDriverSwitch("MixedModeDemandDriverSwitch"),
    MealAndRestTemplate("MealAndRestTemplate"),
    ScheduleEditShiftTimeNew( "ScheduleEditShiftTimeNew"),
    EnableTahoeStorage("EnableTahoeStorage"),
    EnableLongTermBudgetPlan("EnableLongTermBudgetPlan"),
    ShowAnnouncementGroupOP("ShowAnnouncementGroupOP"),
    NewAnnouncements( "NewAnnouncements"),
    CommsAnnouncements("CommsAnnouncements"),
    Announcements("Announcements"),
    UseDemandDriverTemplateSwitch("UseDemandDriverTemplateSwitch"),
    EnableChangeLocationGroupSetting("EnableChangeLocationGroupSetting"),
    WorkRoleSettingsTemplateOP("WorkRoleSettingsTemplateOP"),
    TAScheduleCost("TAScheduleCost"),
    EnableMultiWorkRolePerShiftSCH("EnableMultiWorkRolePerShiftSCH"),
    UseAbsenceMgmtConfiguration("UseAbsenceMgmtConfiguration"),
    EnableSenioritySchedule("EnableSenioritySchedule"),
    EnableScheduleOnAccrualSCH("EnableScheduleOnAccrualSCH"),
    ReliefPool("ReliefPool"),
    EnableForce24hTimeFormat("EnableForce24hTimeFormat"),
    EnableSchTmView24hTimeFormat ("EnableSchTmView24hTimeFormat");

    private final String value;

    Toggles(final String newValue) {
        value = newValue;
    }

    public String getValue() {
        return value;
    }
}
