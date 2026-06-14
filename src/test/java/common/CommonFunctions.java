package common;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.Properties;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.appium.java_client.AppiumDriver;

@SuppressWarnings("deprecation")
public class CommonFunctions {
    WebDriverWait wait;
    int intCount = 0;

    private static AppiumDriver appiumDriver;
    
    java.io.StringWriter sw = new java.io.StringWriter();
    java.io.PrintWriter pw = new java.io.PrintWriter(sw);

    public CommonFunctions() {
        super();
    }

    // Read properties file
    public String funcReadPropertiesFile(String fileProperties) {
        String strValue = null;
        Properties prop = new Properties();
        InputStream input = null;

        try {

            input = new FileInputStream(fileProperties);
            prop.load(new InputStreamReader(input, Charset.forName("UTF-8")));

            // load a properties file
            prop.load(input);

            for (String key : prop.stringPropertyNames()) {
                String val = prop.getProperty(key);
                System.setProperty(key, val);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return strValue;
    }

    // Generate random number
    public int funcRandomIntegerBetweenRange(int min, int max) {
        int x = (int) ((Math.random() * ((max - min) + 1)) + min);
        return x;
    }

    public boolean isElementPresent(WebDriver driver, By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    public void waitUntilElementLocated(WebDriver driver, By locator, int timeOutInSec) {
        try {
            WebDriverWait driverWait = new WebDriverWait(driver, Duration.ofSeconds(timeOutInSec));
            driverWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (Exception e) {
            Assert.fail("Unable to locate web element: " + e.getMessage());
        }
    }

    public void waitTime(int intSecond) {
        try {
            Thread.sleep(intSecond * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getChromeVersion() {
        try {
            Process process = new ProcessBuilder(
                    "adb", "shell", "dumpsys", "package", "com.android.chrome"
            ).start();

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().startsWith("versionName=")) {
                    return line.replace("versionName=", "").trim();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to get Chrome version from device", e);
        }

        throw new RuntimeException("Chrome is not installed on device");
    }

}
