package demo.imdb.steps;

import static org.junit.Assert.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SearchByGenreTest {

	private static String IMDB_URL = "https://www.imdb.com/";
	private WebDriver driver;
	WebElement topMovie;
	WebElement list;
	WebDriverWait wait;

	@Before("@IMDb")
	public void openBrowser() {
		System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google Chrome Driver\\chromedriver.exe");
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--no-sandbox");
		driver = new ChromeDriver(options);
		wait = new WebDriverWait(driver, 20);
		driver.manage().window().maximize();
	}

	@Given("I go to IMDb website")
	public void gotoIMDbWebsite() {
		if (!driver.getCurrentUrl().equals(IMDB_URL))
			driver.get(IMDB_URL);
	}

	@Given("I navigate to webpage to select movies by genre")
	public void navigateToWebpageToSelectMoviesByGenre() {
		WebElement menuButton = driver.findElement(By.id("imdbHeader-navDrawerOpen--desktop"));
		menuButton.click();
		WebElement playerIframe = wait.until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id=\"imdbHeader\"]/div[2]/aside/div")));
		WebElement browseByGenreOption = playerIframe.findElement(By.xpath("./div[2]/div/div[1]/span/div/div/ul/a[5]"));
		browseByGenreOption.click();
		waitTillPageLoads();

	}

	@When("I click on this {string} for movie")
	public void iClickOnThisGenreForMovie(String genre) {
		WebElement genreTable = driver.findElement(By.xpath("//*[@id=\"main\"]/div[6]/span/div/div/div"));
		WebElement genreElement = genreTable.findElement(By.linkText(genre));
		genreElement.click();
		waitTillPageLoads();
	}

	@When("I select the compact view option")
	public void iSelectTheCompactViewOption() {
		driver.findElement(By.className("compact")).click();
		waitTillPageLoads();

	}

	@When("select sort by user rating")
	public void selectSortByUserRating() {
		driver.findElement(By.linkText("User Rating")).click();
		waitTillPageLoads();
		list = driver.findElement(By.className("lister-list"));
		topMovie = list.findElement(By.className("lister-col-wrapper"));
	}

	@When("I extract the list as text and open that file")
	public void iExtractTheListAsTextAndOpenTheeFile() {
		list.findElements(By.xpath("./div")).forEach(element -> {
			System.out.printf(", \t %-60s \t %4s \t %5s \t,\n",
					element.findElement(By.xpath("./div[2]/div/div[1]/span/span[2]/a")).getText(),
					element.findElement(By.xpath("./div[2]/div/div[1]/span/span[2]/span")).getText().replaceAll("[()]",
							""),
					element.findElement(By.xpath("./div[2]/div/div[2]/strong")).getText());
		});
	}

	@Then("I see top movie is {string}")
	public void iSeeTopMovieName(String movieTitle) {
		assertEquals(movieTitle, topMovie.findElement(By.xpath("./div[1]/span/span[2]/a")).getText());
	}

	@Then("Top Movie has rating {string}")
	public void topMovieHasRating(String movieRating) {
		assertEquals(movieRating, topMovie.findElement(By.className("col-imdb-rating")).getText());
	}

	@Then("was released in {string}")
	public void wasReleasedIn(String movieYear) {
		assertEquals(movieYear,
				topMovie.findElement(By.xpath("./div[1]/span/span[2]/span")).getText().replaceAll("[()]", ""),
				movieYear);
	}

	@After("@movie")
	public void embedScreenshot(Scenario scenario) throws Exception {
		if (scenario.isFailed()) {
			try {
				byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
				System.out.println("screenshot "+ scenario.getUri() +"attached");
				scenario.attach(screenshot, "image/png", scenario.getName());
			} catch (WebDriverException wde) {
				System.err.println(wde.getMessage());
			} catch (ClassCastException cce) {
				System.err.println(cce.getMessage());
			}
		}
		driver.quit();
	}

	private void waitTillPageLoads() {
		wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState")
				.equals("complete"));
	}

//	@BeforeMethod
//	public void setup() {
//		openBrowser();
//		gotoIMDbWebsite();
//	}
//
//	@Test(dataProvider = "topMovieByrating")
//	public void testTopRatedMovieName(String genreName, String movieName) {
//		navigateToWebpageToSelectMoviesByGenre();
//		iClickOnThisGenreForMovie(genreName);
//		iSelectTheCompactViewOption();
//		selectSortByUserRating();
//		iSeeTopMovieName(movieName);
//	}
//
//	@Test
//	public void testTopRatedMovieYear(String genreName, String releasedYear) {
//
//	}
//
//	@Test
//	public void testTopRatedMovieRatings(String genreName, String rating) {
//
//	}
//
//	@DataProvider(name = "topMovieByrating")
//	public Object[][] getTopRatedMovieForGenre(Method testMethod) {
//		String csv = "src/test/resources/data.csv";
//		String testName = testMethod.getName();
//		switch (testName) {
//		case "testTopRatedMovieName":
//			return readDataFromCSV(csv, 1);
//		case "testTopRatedMovieRatings":
//			return readDataFromCSV(csv, 2);
//		case "testTopRatedMovieYear":
//			return readDataFromCSV(csv, 3);
//		default:
//			System.out.println("Test Method Not registered in Data provider");
//			return null;
//		}
//	}
//
//	private Object[][] readDataFromCSV(String csv, int ind) {
//		try {
//			Scanner scanner = new Scanner(new File(csv));
//			scanner.nextLine();
//			List<String[]> params = new ArrayList<String[]>();
//			while (scanner.hasNextLine()) {
//				String[] param = scanner.nextLine().split(",");
//				params.add(new String[] { param[0], param[ind] });
//			}
//			String[][] matrix = new String[params.size()][];
//			return params.toArray(matrix);
//		} catch (Exception e) {
//			System.err.println("Error reading test datavv source file");
//			e.printStackTrace();
//		}
//		return null;
//	}

}
