package com.legion.pages;


public interface PlanPage {
    
    public void clickOnPlanConsoleMenuItem() throws Exception;
    public void createANewPlan(String planName) throws Exception;
    public void verifyScenarioPlanAutoCreated(String planName,String scenarioName) throws Exception;
    public boolean searchAPlan(String keywords) throws Exception;
    public void takeOperationToPlan(String parentPlanName, String scenarioPlanName, String status) throws Exception;
    public boolean verifyCreatePlanButtonAvail(String upperfieldName) throws Exception;
    public void verifyCreatePlanLandingPage(String planName) throws Exception;
    public void verifyCreatePlanDialog(String planName) throws Exception;
    public void verifyCreatePlanDetailUICheck(String planName, String scplan,String newSCplan) throws Exception;
    public String getCurrentLocationsForCreatePlan() throws Exception;
    public String getScenarioPlanStatus() throws Exception;
    public void verifyPlanDetail(String planName,String scplan) throws Exception;
    public void verifyRunBTNInPlanDetail(String planName,String scplan) throws Exception;
    public boolean archiveAPlan(String planName,String scplanName) throws Exception;
    public void checkCompleteForecastPlan(String planName,String scplanName) throws Exception;
    public void editAScenarioPlan(String planName,String scplanName) throws Exception;
//    public int getAllPlansInList() throws Exception;
    public boolean verifyPlanConsoleTabShowing() throws Exception;
    public boolean verifyCreatePlanButtonShowing() throws Exception;
    public void verifyPlanStatus(String planName) throws Exception;
    public void verifySetInEffectPopup(String planName,String scplanName) throws Exception;
    public String verifyScenarioDetailPageUsingWorkRoleOrJobTitle(String planName, String scplan) throws Exception;


}
