#Sample Feature Definition Template
@IMDb
Feature: Searching by Genre to See List

  @movie
  Scenario Outline: Checking list of top   movies
    Given I go to IMDb
    And I navigate to webpage to select movies by genre
    When I click on this <genre> for movie
    And I select the compact view option
    And select sort by user rating
    And I extract the list as text and open that file
    Then I see top movie is <top_movie>
    And Top Movie has rating <top_movie_rating>
    And was released in <top_movie_year>

    Examples: 
      | genre       | top_movie                                          | top_movie_rating | top_movie_year |
      | 'Biography' | 'Lo spirito del serchio'                           | '9.7'            | '2009'         |
      | 'Sci-Fi'    | 'Return of the Robo-Mummy from Outer Space Part 7' | '9.5'            | '2017'         |
