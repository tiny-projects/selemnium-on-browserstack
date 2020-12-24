#Sample Feature Definition Template
@Oscar
Feature: Getting detail of oscar winner

  Scenario: Checking oscar winner in specifc year with category
    Given I go to IMDb website
    And I navigate to webpage of Oscars
    When I search in following category
      | category                     | year |
      | Best Actor in a Leading Role | 1929 |
    Then I see winner is Emil Jannings
