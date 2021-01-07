package demo.imdb.steps;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import demo.imdb.util.BrowserStackUtil;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class OscarByCategoryTest {

	private static String IMDB_URL = "https://www.imdb.com/";
	private WebDriver driver;
	WebElement topMovie;
	WebElement list;
	WebDriverWait wait;
	private String device = "PC";

	@Before("@Oscar")
	public void openBrowser() {
		try {
			driver = BrowserStackUtil.setupDriver(driver, device);
			driver.manage().window().setSize(new Dimension(1280, 960));
			wait = new WebDriverWait(driver, 20);
			if (driver == null) {
				System.out.println("Driver is null exiting");
				System.exit(1);
			}
		} catch (Exception e) {
			System.err.println("Error while initializing driver " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Given("I go to IMDb")
	public void i_go_to_imdb_website() {
		System.out.println("Launching IMDb website in resolution " + driver.manage().window().getSize());
		if (!driver.getCurrentUrl().equals(IMDB_URL))
			driver.get(IMDB_URL);
		waitTillPageLoads();
	}

	@Given("I navigate to webpage of Oscars")
	public void i_navigate_to_webpage_of_oscars() {
		System.out.println("Currently in web page " + driver.getCurrentUrl());
		WebElement menuButton;
		if (device == "PC")
			menuButton = driver.findElement(By.xpath("//*[@id='imdbHeader-navDrawerOpen--desktop']"));
		else
			menuButton = driver.findElement(By.xpath("//*[@id='imdbHeader-navDrawerOpen']"));
		menuButton.click();
		WebElement playerIframe = wait.until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id=\"imdbHeader\"]/div[2]/aside/div")));
		if (device == "Mobile")
			// expand Awards and events
			driver.findElement(By.xpath("//*[@id='imdbHeader']/div[2]/aside/div/div[2]/div/div[3]/span/label")).click();
		WebElement oscarOption = playerIframe.findElement(By.xpath("./div[2]/div/div[3]/span/div/div/ul/a[1]"));
		oscarOption.click();
		waitTillPageLoads();
	}

	@When("I search in following category")
	public void i_search_in_following_category(io.cucumber.datatable.DataTable dataTable) {
		Map<String, String> data = dataTable.asMaps().get(0);
		String year = data.get("year");
		wait.until(ExpectedConditions.presenceOfElementLocated(By.className("event-history-widget__years")));
		WebElement yearDiv = driver.findElement(By.className("event-history-widget__years"));
		yearDiv.findElement(By.linkText(year)).click();
		waitTillPageLoads();

	}

	@Then("I see Oscar Winner Page")
	public void i_see_oscar_winne_page() {
		List<WebElement> oscarCategory = driver
				.findElements(By.xpath("//*[@id=\"center-3-react\"]/div/div/div/h3/div"));
		oscarCategory.forEach(category -> {
			category.findElement(By.className("event-widgets__award-category-nominations"));

		});

	}

	private void waitTillPageLoads() {
		wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState")
				.equals("complete"));
	}
}
