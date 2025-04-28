Feature: As TA user I would like see premiums or extra pay are generated properly

  @Premium
  Scenario: As a Manager user I want to do an early or on time publish and see not premium (extra pay ) generated
    Given I have a location with Dolar general(DG) configuration.
    Given I have a location that schedule should be published at least 7 days before schedule week starts.
    Given I have a week without schedule generated.
    Given I login as 'AUTuserManager' password 'legion2020' in enterprise 'LegionCoffee' in TA.
    When I select in dashboard the location 'Carmel Club DG Oregon'.
    When I navigate to schedule option.
    When I click in schedule tab.
    When I select a week without schedule generated.
    And I click in 'Generate Schedule' button.
    And I enter all estimates hours.
    And I click in 'CHECK OUT THE SCHEDULE!' button.
    And I click in 'Publish' button.
    And I confirm publish of Schedule clicking on publish button.
    And I click in 'OK' confirmation pop-up button.
    And I navigate to timesheet option.
    And I select a generated schedule week.
    And I click in 'Add Timeclock' button.
    And I search for 'Rodolfo Wolff' employee.
    And I click in 'Add' button.
    And I add on time clocks.
    Then I verify no premium or extra pay is generated in timesheet table.
