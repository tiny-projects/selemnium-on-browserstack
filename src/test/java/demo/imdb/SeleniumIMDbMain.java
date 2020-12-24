package demo.imdb;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class SeleniumIMDbMain {

	public static void main(String[] args) {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--no-sandbox");
		WebDriver driver = new ChromeDriver(options);
		System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google Chrome Driver\\chromedriver.exe");
		driver.manage().window().maximize();
		driver.get("www.youtube.com");
		openNewTab(driver);
		scrollPageTillEnd(driver);
	}

	private static void openNewTab(WebDriver driver) {
		driver.findElement(By.tagName("body")).sendKeys(Keys.CONTROL + "t");
		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(tabs.get(1)); // switches to new tab

	}

	private static void scrollPageTillEnd(WebDriver driver) {
		while (footerIsNotPresent()) {
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("window.scrollBy(0, 100)");
		}

	}

	private static boolean footerIsNotPresent() {
		return true;
	}
}
