package testRunner;


import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.CucumberOptions.SnippetType;
import io.cucumber.core.cli.Main;

@CucumberOptions(
		  features = "src/test/resources/features", // To include all feature files in the folder
		  glue = "StepDefinitions",
		  dryRun = true,
		  snippets = SnippetType.CAMELCASE
		)


public class example_runner extends AbstractTestNGCucumberTests{

}
