package IMDbGenre.runner;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/java", glue = "demo.imdb.steps", tags = "@Oscar", plugin = {
		"html:test-results/imdb-html-report.html", "json:test-results/imdb.json", "pretty:test-results/imdb-pretty.txt",
		"usage:test-results/imdb-genre_results.json", "junit:test-results/imdb-results.xml" })
public class JUnitTestRunner {

}
