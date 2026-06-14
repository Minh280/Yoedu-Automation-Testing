package common;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeDriverService;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
//import org.openqa.selenium.opera.OperaDriver;
//import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.cucumber.java.After;
import io.cucumber.java.Before;
//import io.github.bonigarcia.wdm.WebDriverManager;

@SuppressWarnings("deprecation")
public class ContextSteps {
    public static WebDriver driver;
    public static AppiumDriver appiumDriver;

    CommonFunctions commonFunction = new CommonFunctions();

    @Before
    public void setUp() throws Exception {
        // Read properties files
        commonFunction.funcReadPropertiesFile("src/main/resources/application.properties");

        // ANDROID
        if (System.getProperty("osName").trim().toLowerCase().contains("android")) {
            DesiredCapabilities caps = new DesiredCapabilities();
            // caps.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);

            if (System.getProperty("appType").trim().toLowerCase().contains("webview")) {
                caps.setCapability("automationName", "UIAutomator2");
                caps.setCapability("platformName", System.getProperty("osName").trim());
                caps.setCapability("platformVersion", System.getProperty("androidVersion").trim());
                caps.setCapability("deviceName", System.getProperty("androidDeviceName").trim());
                caps.setCapability("browserName", System.getProperty("androidBrowser").trim());
                caps.setCapability("newCommandTimeout", "300");

                String chromeOnDevice = commonFunction.getChromeVersion();

                WebDriverManager wdm = WebDriverManager.chromedriver().driverVersion(chromeOnDevice).clearDriverCache();
                wdm.setup();

                String chromeDriverPath = wdm.getDownloadedDriverPath();

                caps.setCapability("chromedriverExecutable", chromeDriverPath);


//                if (System.getProperty("os.name").contains("Windows"))
//                    caps.setCapability("chromedriverExecutable", System.getProperty("user.dir") + "\\driver\\chromedriver_x64.exe");
//                else
//                    caps.setCapability("chromedriverExecutable", System.getProperty("user.dir") + "/driver/chromedriver");

//                WebDriverManager.chromedriver().browserVersion(System.getProperty("chromeVersion")).setup();

                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.setExperimentalOption("w3c", false);
                caps.merge(chromeOptions);

            }

            appiumDriver = new AndroidDriver(
                    new URL("http://" + System.getProperty("androidAppiumServerIP").trim() + ":"
                            + System.getProperty("appiumPort").trim() + "/"),
                    caps);
            appiumDriver.manage().timeouts().implicitlyWait(Integer.parseInt(System.getProperty("objectTimeout")),
                    TimeUnit.SECONDS);

            ThreadWebDriver.setAppium(appiumDriver);

            ContextSteps.appiumDriver = ThreadWebDriver.getAppium();
        } else if (System.getProperty("osName").trim().toLowerCase().contains("windows")) {
            switch (System.getProperty("browserName")) {
                case "chrome":
                     // WebDriverManager.chromedriver().browserVersion(System.getProperty("chromeVersion")).setup();
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments("--remote-allow-origins=*");

//                         chromeOptions.setHeadless(true);

                    ChromeDriverService driverService = ChromeDriverService.createDefaultService();
                    ChromeDriver driver = new ChromeDriver(driverService, chromeOptions);

                    ThreadWebDriver.set(driver);

                    ContextSteps.driver = ThreadWebDriver.get();

                    break;
            }

            // Set resolution
            if (System.getProperty("os.name").toLowerCase().contains("windows")
                    || System.getProperty("os.name").toLowerCase().contains("mac")) {
                driver.manage().window().maximize();

                Dimension initial_size = driver.manage().window().getSize();
//			int width = initial_size.getWidth();
                int height = initial_size.getHeight();

                if (height < 1080)
                    driver.manage().window().setSize(new Dimension(1920, 1080));
            }

            driver.manage().timeouts().implicitlyWait(Integer.parseInt(System.getProperty("objectTimeout")),
                    TimeUnit.SECONDS);

        } else if (System.getProperty("osName").trim().toLowerCase().contains("mac")) {
            switch (System.getProperty("browserName")) {
                case "chrome":
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments("--remote-allow-origins=*");

                    System.out.println(System.getProperty("os.name"));

//					chromeOptions.setHeadless(true);

                    WebDriverManager.chromedriver().browserVersion(System.getProperty("chromeVersion")).setup();

                    driver = new ChromeDriver(chromeOptions);
                    ThreadWebDriver.set(driver);

                    ContextSteps.driver = ThreadWebDriver.get();

                    break;
            }

            // Set resolution
            if (System.getProperty("os.name").toLowerCase().contains("windows")
                    || System.getProperty("os.name").toLowerCase().contains("mac")) {
                driver.manage().window().maximize();

                Dimension initial_size = driver.manage().window().getSize();
//			int width = initial_size.getWidth();
                int height = initial_size.getHeight();

                if (height < 1080)
                    driver.manage().window().setSize(new Dimension(1920, 1080));
            }

            driver.manage().timeouts().implicitlyWait(Integer.parseInt(System.getProperty("objectTimeout")),
                    TimeUnit.SECONDS);
        }
    }

    public WebDriver getDriver() {
        if (System.getProperty("osName").trim().toLowerCase().contains("windows")
                || System.getProperty("osName").trim().toLowerCase().contains("mac"))
            return ThreadWebDriver.get();
        else
            return ThreadWebDriver.getAppium();
    }

    @After
    public void tearDown() throws Exception {
        if (System.getProperty("osName").trim().toLowerCase().contains("windows")
                || System.getProperty("osName").trim().toLowerCase().contains("mac")) {

            ThreadWebDriver.get().quit();
            ThreadWebDriver.remove();

        } else {

            ThreadWebDriver.getAppium().quit();
            ThreadWebDriver.removeAppium();

        }
    }
}
