package demo.imdb.steps;

import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class OscarByCategory {

	private static String IMDB_URL = "https://www.imdb.com/";
	private WebDriver driver;
	WebElement topMovie;
	WebElement list;
	WebDriverWait wait;

	@Before("@Oscar")
	public void openBrowser() {
		System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google Chrome Driver\\chromedriver.exe");
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--no-sandbox");
		driver = new ChromeDriver(options);
		wait = new WebDriverWait(driver, 20);
		driver.manage().window().maximize();
	}

	@Given("I go to IMDb")
	public void i_go_to_imdb_website() {
		if (!driver.getCurrentUrl().equals(IMDB_URL))
			driver.get(IMDB_URL);
	}

	@Given("I navigate to webpage of Oscars")
	public void i_navigate_to_webpage_of_oscars() {
		WebElement menuButton = driver.findElement(By.id("imdbHeader-navDrawerOpen--desktop"));
		menuButton.click();
		WebElement playerIframe = wait.until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id=\"imdbHeader\"]/div[2]/aside/div")));
		WebElement browseByGenreOption = playerIframe.findElement(By.xpath("./div[2]/div/div[3]/span/div/div/ul/a[1]"));
		browseByGenreOption.click();
		waitTillPageLoads();
	}

	@When("I search in following category")
	public void i_search_in_following_category(io.cucumber.datatable.DataTable dataTable) {

		// For other transformations you can register a DataTableType.
		Map<String, String> data = dataTable.asMaps().get(0);
		String year = data.get("year");
		WebElement yearDiv = driver.findElement(By.className("event-history-widget__years"));
		yearDiv.findElement(By.linkText(year)).click();
		waitTillPageLoads();

	}

	@Then("I see winner is Emil Jannings")
	public void i_see_winner_is_emil_jannings() {

	}

	private void waitTillPageLoads() {
		wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState")
				.equals("complete"));
	}
}
