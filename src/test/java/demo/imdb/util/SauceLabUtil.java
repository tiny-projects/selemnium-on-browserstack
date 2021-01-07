package demo.imdb.util;

import java.net.URL;
import java.util.HashMap;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;

public class SauceLabUtil {

	public static final String USERNAME = "sayantandey";
	public static final String ACCESS_KEY = "9a527475-9232-44ff-a301-2bacb9abd27b";
	public static final String URL = "http://" + USERNAME + ":" + ACCESS_KEY + "@ondemand.saucelabs.com:80/wd/hub";

	private static int RETRY_COUNT = 10;

	/**
	 * initialize a driver for mobile device with retry attempts, if retry fails
	 * return backs the driver instance sent
	 * 
	 * @param device
	 */
	public static WebDriver setupDriver(WebDriver driver, String device) throws Exception {
		MutableCapabilities sauceOptions = new MutableCapabilities();
		sauceOptions.setCapability("username", USERNAME);
		sauceOptions.setCapability("accessKey", ACCESS_KEY);

		MutableCapabilities capabilities = new MutableCapabilities();
		capabilities.setCapability("browserName", "chrome");
		capabilities.setCapability("browserVersion", "87.0");
		capabilities.setCapability("platformName", "Windows 10");
		capabilities.setCapability("sauce:options", sauceOptions);
		String sauceUrl = String.format("https://ondemand.saucelabs.com/wd/hub");
		driver = new RemoteWebDriver(new URL(sauceUrl), capabilities);
		int attemptMade = 0;
		while (++attemptMade <= RETRY_COUNT) {
			try {
				driver = new RemoteWebDriver(new URL(sauceUrl), capabilities);
				if (driver != null) {
					System.out.println("Driver instantiated , starting test procedure ");
					return driver;
				}
				System.out.print("Null driver received , retrying in 10 seconnds...");
				Thread.sleep(10000);
			} catch (UnreachableBrowserException e) {
				System.err.printf("[Attempt %d ] Remote connection not established, retrying in 10 seconds ...\n",
						attemptMade);
				Thread.sleep(10000);
			}

		}
		return driver;

	}

	private static void conFigureDevice(DesiredCapabilities caps, String device, WebDriver driver) {
		switch (device) {
		case "PC":
			caps.setCapability("browser", "Edge");
			caps.setCapability("browserVersion", "latest");
			caps.setCapability("resolution", "1920x1080");
			HashMap<String, Object> pcOptions = new HashMap<String, Object>();
			pcOptions.put("os", "Windows");
			pcOptions.put("osVersion", "10");
			pcOptions.put("local", "false");
			pcOptions.put("seleniumVersion", "3.141.59");
			caps.setCapability("bstack:options", pcOptions);
			break;

		default:
			HashMap<String, Object> mobileOptions = new HashMap<String, Object>();
			mobileOptions.put("osVersion", "13");
			mobileOptions.put("deviceName", "iPhone 11 Pro Max");
			mobileOptions.put("realMobile", "true");
			mobileOptions.put("local", "false");
			caps.setCapability("bstack:options", mobileOptions);

		}
	}

	/**
	 * accepts the status, reason and WebDriver instance and marks the test on
	 * BrowserStack
	 * 
	 * @param status : Whether test passed or failed
	 * @param reason
	 * @param driver
	 */
	public static void markTestStatus(String status, String reason, WebDriver driver) {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \""
				+ status + "\", \"reason\": \"" + reason + "\"}}");
	}

}
