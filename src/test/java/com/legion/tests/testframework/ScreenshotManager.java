package com.legion.tests.testframework;

import static com.legion.utils.MyThreadLocal.getCurrentTestMethodName;
import static com.legion.utils.MyThreadLocal.getDriver;
import static com.legion.utils.MyThreadLocal.getSessionTimestamp;

import com.legion.utils.SimpleUtils;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.legion.utils.MyThreadLocal;
import org.openqa.selenium.WebDriverException;

import static com.legion.utils.MyThreadLocal.*;

public class ScreenshotManager {
	
	private static Map<String, String> propertyMap = SimpleUtils.getParameterMap();
	
	public static String takeScreenShot() {
		File targetFile;
		File screenshotFile;
		File file = getScreenshotDir();
        String threadIdStr = String.valueOf(Thread.currentThread().getId());
        Date date = new Date();
        String screenShotName = date.toString().replace(":", "_").replace(" ", "_") + ".png";
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
        String strDate = formatter.format(date);
        String strDateFinal = strDate.replaceAll(" ", "_");
        String screenshotFinalLocation = file + File.separator + strDateFinal +
                File.separator + getSessionTimestamp() +
                File.separator + getCurrentTestMethodName() +
                File.separator + threadIdStr + File.separator + getScreenshotConsoleName();
        targetFile = new File(screenshotFinalLocation, screenShotName);
        MyThreadLocal.setScreenshotLocation(screenshotFinalLocation + File.separator + screenShotName);
       
//        File screenshotFile = ((TakesScreenshot) getAndroidDriver()).getScreenshotAs(OutputType.FILE);
        try {
            if(MyThreadLocal.getPlatformName()!=null && MyThreadLocal.getPlatformName().equalsIgnoreCase("mobile")){
                screenshotFile = ((TakesScreenshot) getAndroidDriver()).getScreenshotAs(OutputType.FILE);
                MyThreadLocal.setPlatformName("");
            }else{
                screenshotFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
            }
            FileUtils.copyFile(screenshotFile, targetFile);
        }catch (WebDriverException webDriverException) {
            webDriverException.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return targetFile.toString();
        }
    }

    public synchronized static void createScreenshotDirIfNotExist() {
        File file = getScreenshotDir();
        if (!file.exists()) {
            file.mkdir();
        }
    }

    private static File getScreenshotDir() {
        return new File("Screenshots" + File.separator + "Results");
    }
	

}
