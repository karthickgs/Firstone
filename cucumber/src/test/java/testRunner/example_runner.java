package testRunner;

import org.junit.runner.RunWith;
import io.cucumber.core.cli.Main;
import io.cucumber.junit.Cucumber;

@RunWith(Cucumber.class)
@io.cucumber.junit.CucumberOptions(
		  features = "src/test/resources/features/configuration/saucelabs.feature", // To include all feature files in the folder
		  glue = "StepDefinitions.saucelabs.feature",
		  dryRun = true,
		  snippets = io.cucumber.junit.CucumberOptions.SnippetType.CAMELCASE
		)


public class example_runner{

}
