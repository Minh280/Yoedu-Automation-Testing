package common;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.AbstractTestNGCucumberTests;

@CucumberOptions(publish = true, features = {
//		"src/test/resources/feature",
//		"src/test/resources/feature/Yoedu_HappyCase.feature",
//		"src/test/resources/feature/Yoedu_UnHappyCase.feature",
// 		"src/test/resources/feature/API_RestAssured.feature",
}
		, glue = { "common", "stepsDefinition" }
		, tags = " @GradeLevel or @Room "
		// , dryRun = true
		, plugin = { "pretty",
				"summary",
				"junit:target/cucumber.xml",
				"html:target/cucumber/index.html",
				"json:target/cucumber.json",
		}, monochrome = true)

@Test
public class RunParallel extends AbstractTestNGCucumberTests {

}

