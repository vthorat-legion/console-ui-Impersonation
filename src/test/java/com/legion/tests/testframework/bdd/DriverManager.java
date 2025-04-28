package com.legion.tests.testframework.bdd;

import com.legion.tests.testframework.PropertyMap;
import com.legion.utils.MyThreadLocal;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.legion.utils.MyThreadLocal.*;
import static com.legion.utils.MyThreadLocal.setOS;

public class DriverManager {


    public  void initDriver (String browser, String version, String os){
        WebDriverManager.chromedriver().setup();
        setBrowserNeeded(true);
        if (getBrowserNeeded() && browser != null) {

            setDriverType(browser);
            setVersion(version);
            setOS(os);
            createDriver();
        }
    }

    public void finishDriver(){
        MyThreadLocal.getDriver().close();
        MyThreadLocal.getDriver().quit();
        MyThreadLocal.setDriver(null);

    }

    private  void createDriver(){
        if (getDriverType().equalsIgnoreCase(PropertyMap.get("CHROME"))) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            if(PropertyMap.get("isHeadlessBrowser").equalsIgnoreCase("true")){
                options.addArguments("headless");
                options.addArguments("window-size=1200x600");
                createChrome(options);
            }else{
                createChrome(options);
            }
        }

    }

    private void createChrome(ChromeOptions options){
        options.addArguments("disable-infobars");
        options.addArguments("test-type", "new-window", "disable-extensions","start-maximized");
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("disable-logging", "silent", "ignore-certificate-errors");
        options.setExperimentalOption("useAutomationExtension", false);
        options.setExperimentalOption("excludeSwitches",
                Collections.singletonList("enable-automation"));
        options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        options.setCapability(ChromeOptions.CAPABILITY, options);
        options.setCapability("chrome.switches", Arrays.asList("--disable-extensions", "--disable-logging",
                "--ignore-certificate-errors", "--log-level=0", "--silent"));
        options.setCapability("silent", true);
        System.setProperty("webdriver.chrome.silentOutput", "true");
        setDriver(new ChromeDriver(options));
    }

}
