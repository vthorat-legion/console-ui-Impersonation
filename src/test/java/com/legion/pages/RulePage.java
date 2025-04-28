package com.legion.pages;

import java.util.List;

public interface RulePage {
    public void clickRuleButton() throws Exception;
    public void removeAllShiftPatternsAssignmentsOnScheduleRulePage() throws Exception;
    public void removeAllAssignmentsOnCreateOngoingAssignmentModal() throws Exception;
    public void clickSaveButtonOnCreateOngoingAssignmentModal() throws Exception;
    public boolean checkIfThereAreAssignmentOnRulePage() throws Exception;
    public List<String> getAllShiftPatternsAssignmentsOnScheduleRulePage() throws Exception;
    public void clickBackButton() throws Exception;
    public void assignEmployeeToSpecificShiftPattern(String employeeName, String workRole, String shiftPatternName, String startDate, String endDate) throws Exception;
    public List<String> getAssignmentOfShiftPattern(String workRole, String shiftPatternName);
}
