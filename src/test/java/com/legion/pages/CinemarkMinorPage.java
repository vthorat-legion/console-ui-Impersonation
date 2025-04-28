package com.legion.pages;

public interface CinemarkMinorPage {
    public void clickConfigurationTabInOP() throws Exception;
    public void newTemplate(String templateName) throws Exception;
    public void minorRuleToggle(String action, String witchOne) throws Exception;
    public void verifyDefaultMinorRuleIsOff(String witchOne) throws Exception;
    public void saveOrPublishTemplate(String action) throws Exception;
    public void clickOnBtn(String button) throws Exception;
    public void findDefaultTemplate(String templateName) throws Exception;
//    public void setMinorRuleByWeek(String minorType, String weekType, String maxOfDay, String maxOfHrs) throws Exception;
//    public void setMinorRuleByDay(String minorType, String dayType, String from, String to, String maxOfHrs) throws Exception;
    public void setMinorRuleByWeek(String weekType, String maxOfDay, String maxOfHrs) throws Exception;
    public void setMinorRuleByDay(String dayType, String from, String to, String maxOfHrs) throws Exception;
    public void turnOnOrOffSharingCalendars(String option) throws Exception;
}
