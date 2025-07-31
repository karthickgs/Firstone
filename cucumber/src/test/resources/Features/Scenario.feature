Feature: Remittance for agents

  @tag1
  
  Scenario: Remmitance in NOVA CBS
  
    Given I want agent and brokers with CD/CN amount
    And  CD/CN amount should not be less than 2 lakhs
    When In pending, Remittance amount is transferred from Customer to SGI via Agent/brokers
    Then If not, block the particular user id