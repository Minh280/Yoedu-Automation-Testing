package common;

import org.openqa.selenium.WebDriver;

import io.appium.java_client.AppiumDriver;

public class ThreadWebDriver {
	private static final ThreadLocal<WebDriver> threadWebDriver = new InheritableThreadLocal<WebDriver>();
	private static final ThreadLocal<AppiumDriver> threadAppiumDriver = new InheritableThreadLocal<AppiumDriver>();

	private ThreadWebDriver() {
	}

	//=====================================================================================
	public static WebDriver get() {
		return threadWebDriver.get();
	}

	public static AppiumDriver getAppium() {
		return threadAppiumDriver.get();
	}

	//=====================================================================================
	public static void set(WebDriver driver) {
		threadWebDriver.set(driver);
	}

	public static void setAppium(AppiumDriver driver) {
		threadAppiumDriver.set(driver);
	}

	//=====================================================================================
	public static void remove() {
		threadWebDriver.remove();
	}

	public static void removeAppium() {
		threadAppiumDriver.remove();
	}

}
