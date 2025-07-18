package StepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class saucedemo_StepDefinitions {

		  @When("I add {int} of {string} to cart")
		  public void addItemToCart(int quantity, String product) {
		    // Implement the logic to add the specified quantity of the product to the cart
		  }

		  @Then("I should see {int} in cart")
		  public void verifyTotalInCart(int total) {
		    // Implement the logic to verify that the total in the cart matches the expected value
		  }
		}


