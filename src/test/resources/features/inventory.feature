Feature: Inventory page functionality after successful login

  This feature verifies the behaviour of the inventory page once a user successfully logs in.
  It covers handling of alerts, viewing product details, adding/removing items from the cart,
  and validating product display and sorting functionality.

  Background:
    Given the user has logged in successfully
    And the "Change your password" alert is accepted

  @inventory @positive
  Scenario: User adds a product to the cart
    When the user clicks the "Add to Cart" button on a product
    Then the product should be added to the cart
    And the button text should change to "Remove"

  @inventory @positive
  Scenario: User removes a product from the cart
    Given the user has added a product to the cart
    When the user clicks the "Remove" button
    Then the product should be removed from the cart
    And the button text should change back to "Add to Cart"

  @inventory @positive @dynamicPrice
  Scenario: User adds the most expensive product to the cart
    When the user views the list of available products
    And identifies the product with the highest price
    And clicks the "Add to Cart" button for that product
    Then the product should be added to the cart
    And the cart badge count should be incremented