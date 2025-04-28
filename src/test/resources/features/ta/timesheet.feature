Feature: As TA user I would like to traverse for timesheet page

  @Legion
  Scenario: Navigate to Timesheet
    Given I login as 'userAdmin1' password 'legion2020' in enterprise 'LegionCoffee' in TA
    When I click in Timesheet tab
    Then The client verify that results are shown properly
