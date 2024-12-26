package steps;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.*;
import java.net.*;
import java.util.*;

public class ConnectionJenkins {
    private static Properties loadProperties() {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("src/test/resources/config.properties")) {
            props.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Can't load file config.properties", e);
        }
        return props;
    }

    public static WebDriver createDriver() throws MalformedURLException {
        Properties props = loadProperties();
        String runType = System.getProperty("type.run", props.getProperty("type.run"));
        String browser = System.getProperty("type.browser", props.getProperty("type.browser"));

        if ("local".equalsIgnoreCase(runType)) {

            switch (browser.toLowerCase()) {
                case "firefox":
                    System.setProperty("webdriver.gecko.driver", "/src/test/resources/geckodriver.exe");
                    return new FirefoxDriver();

                case "chrome":
                    System.setProperty("webdriver.chrome.driver", "/src/test/resources/chromedriver.exe");
                    return new ChromeDriver();
                default:
                    throw new IllegalArgumentException("Неизвестный локальный браузер: " + browser);
            }

        } else {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            Map<String, Object> selenoidOptions = new HashMap<>();
            capabilities.setCapability("browserName", browser);
            switch (browser.toLowerCase()) {
                case "chrome":
                    capabilities.setCapability("browserVersion", "109.0");
                    selenoidOptions.put("enableVNC", true);
                    selenoidOptions.put("enableVideo", false);
                    capabilities.setCapability("selenoid:options", selenoidOptions);
                    return new RemoteWebDriver(
                            URI.create("http://jenkins.applineselenoid.fvds.ru:4444/wd/hub").toURL(),
                            capabilities
                    );

                case "firefox":
                    capabilities.setCapability("browserVersion", "108.0");
                    selenoidOptions.put("enableVNC", true);
                    selenoidOptions.put("enableVideo", false);
                    capabilities.setCapability("selenoid:options", selenoidOptions);
                    return new RemoteWebDriver(
                            URI.create("http://jenkins.applineselenoid.fvds.ru:4444/wd/hub").toURL(),
                            capabilities
                    );

                default:
                    throw new IllegalArgumentException("Uncorrect browser" + browser);
            }
        }
    }
}
