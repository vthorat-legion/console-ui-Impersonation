package com.legion.pages;

import static com.legion.utils.MyThreadLocal.*;
import static io.appium.java_client.touch.WaitOptions.waitOptions;
import static io.appium.java_client.touch.offset.ElementOption.element;
import static io.appium.java_client.touch.offset.PointOption.point;
import static java.time.Duration.ofSeconds;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.security.Key;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import org.apache.poi.ss.formula.functions.T;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.*;

import com.aventstack.extentreports.Status;
import com.legion.tests.testframework.ExtentTestManager;
import com.legion.utils.MyThreadLocal;
import com.legion.utils.SimpleUtils;

/**
 * Yanming
 */
public class BasePage {

    public static String activeConsoleName;

    public static Map<String, String> propertyMap = SimpleUtils.getParameterMap();

    protected T currentPage;

    protected Map<String,BasePage> resultList;

    public void click(WebElement element, boolean... shouldWait) {
        try {
            waitUntilElementIsVisible(element);
            element.click();
            waitForSeconds(2);
        } catch (TimeoutException te) {
            ExtentTestManager.getTest().log(Status.WARNING,te);
        }
    }

    public void doubleClick(WebElement element, boolean... shouldWait) {
        try {
            waitUntilElementIsVisible(element);
            Actions actions = new Actions(getDriver());
            actions.doubleClick(element).perform();
        } catch (TimeoutException te) {
            ExtentTestManager.getTest().log(Status.WARNING,te);
        }
    }

    public void moveToElementAndClick(WebElement element, boolean... shouldWait) {
        try {
            waitUntilElementIsVisible(element);
            Actions actions = new Actions(getDriver());
            actions.moveToElement(element).click().perform();
        } catch (TimeoutException te) {
            ExtentTestManager.getTest().log(Status.WARNING,te);
        }
    }

    public void openNewURLOnNewTab(String url) {
        String currentHandle = getDriver().getWindowHandle();
        ((JavascriptExecutor) getDriver()).executeScript("window.open(arguments[0]),\'_blank\'", url);
        Set<String> handles = getDriver().getWindowHandles();
        if (handles.size() > 1) {
            for (String handle : handles) {
                if (!handle.equals(currentHandle)) {
                    getDriver().switchTo().window(handle);
                    if (handle.equals(getDriver().getWindowHandle())) {
                        SimpleUtils.pass("Navigate to the New Tab successfully!");
                    }
                }
            }
        }else {
            SimpleUtils.fail("New window doesn't open!", false);
        }
    }

    public void scrollToTop() {
        ((JavascriptExecutor) getDriver()).executeScript("window.scrollTo(document.body.scrollHeight,0)");

    }

    public void scrollOneHeight() {
        ((JavascriptExecutor) getDriver()).executeScript("window.scrollTo(0,document.body.scrollHeight)");
    }

    public static void scrollToBottom() {
        ((JavascriptExecutor) getDriver()).executeScript("window.scrollTo(0,1000000)");
    }

    public void scrollToElement(WebElement element) {
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    //get current date by specific zoon
    public Date getCurrentTimeby(String timeZone){
        TimeZone zone = TimeZone.getTimeZone(timeZone);
        Calendar cal = Calendar.getInstance(zone);
        return cal.getTime();
        }

    //click method for mobile app

    public void clickOnMobileElement(WebElement element, boolean... shouldWait) {
    	try {
//            waitUntilElementIsVisibleOnMobile(element);
            element.click();
        } catch (TimeoutException te) {
        	ExtentTestManager.getTest().log(Status.WARNING,te);
        }
    }
    

    public int calcListLength(List<WebElement> listLength){
    	return listLength.size();
    }
    
    public void waitForElement(String element) {
  
		Wait<WebDriver> wait = new FluentWait<WebDriver>(
                 MyThreadLocal.getDriver()).withTimeout(ofSeconds(60))
                 .pollingEvery(5, TimeUnit.SECONDS)
                 .ignoring(NoSuchElementException.class);
         wait.until(new Function<WebDriver, WebElement>() {
             @Override
             public WebElement apply(WebDriver t) {
                 return t.findElement(By
                         .cssSelector(element));
             }
         });
    	
    }

    public static String getText(WebElement element) {
    	waitUntilElementIsVisible(element);
        return element.getText();
    }
    
    public void checkElementVisibility(WebElement element)
    {
        WebDriverWait wait = new WebDriverWait(MyThreadLocal.getDriver(), 90);
        try {
        	wait.until(ExpectedConditions.visibilityOf(element));
        }
        catch (NoSuchElementException e)
        {
            e.printStackTrace();
//            fail("Element is not present");
        }
    }
    
   
    public boolean isElementLoaded(WebElement element) throws Exception
    {
    	WebDriverWait tempWait = new WebDriverWait(MyThreadLocal.getDriver(), 30);
    	 
    	try {
    	    tempWait.until(ExpectedConditions.visibilityOf(element));
    	    return true;
    	}
    	catch (NoSuchElementException | TimeoutException te) {
    		return false;	
    	}
    	
    }
    
    // method for mobile application
    
    public boolean isElementLoadedOnMobile(MobileElement element) throws Exception
    {
    	WebDriverWait tempWait = new WebDriverWait(MyThreadLocal.getAndroidDriver(), 30);
    	 
    	try {
    	    tempWait.until(ExpectedConditions.visibilityOf(element)); 
    	    return true;
    	}
    	catch (NoSuchElementException | StaleElementReferenceException | TimeoutException te ) {
    		return false;	
    	}
    	
    }

    public boolean isElementLoadedOnMobile(MobileElement element, long timeOutInSeconds) throws Exception
    {
        WebDriverWait tempWait = new WebDriverWait(MyThreadLocal.getAndroidDriver(), timeOutInSeconds);

        try {
            tempWait.until(ExpectedConditions.visibilityOf(element));
            return true;
        }
        catch (NoSuchElementException | StaleElementReferenceException | TimeoutException te ) {
            return false;
        }

    }

    public boolean areListElementVisibleOnMobile(List<MobileElement> listElement, long timeOutInSeconds ){
        Wait<WebDriver> wait = new FluentWait<WebDriver>(
                MyThreadLocal.getAndroidDriver()).withTimeout(ofSeconds(timeOutInSeconds))
                .pollingEvery(ofSeconds(1))
                .ignoring(org.openqa.selenium.NoSuchElementException.class);
        Boolean element =false;
        try{
            element = wait.until(new Function<WebDriver, Boolean>() {
                @Override
                public Boolean apply(WebDriver t) {
                    int size = 0;
                    size = listElement.size();
                    if(size > 0 )
                        return true;
                    else
                        return false;
                }
            });
        }catch(NoSuchElementException | TimeoutException te){
            return element;
        }

        return element;
    }


    public boolean isElementEnabledOnMobile(WebElement enabledElement, long timeOutInSeconds){
        Wait<WebDriver> wait = new FluentWait<WebDriver>(
                MyThreadLocal.getAndroidDriver()).withTimeout(ofSeconds(timeOutInSeconds))
                .pollingEvery(ofSeconds(2))
                .ignoring(org.openqa.selenium.NoSuchElementException.class);
        Boolean element =false;

        try{
            element = wait.until(new Function<WebDriver, Boolean>() {
                @Override
                public Boolean apply(WebDriver t) {
                    boolean display = false;
                    display = enabledElement.isEnabled();
                    if(display )
                        return true;
                    else
                        return false;
                }
            });
        }catch(NoSuchElementException | TimeoutException te){
            return element;
        }
        return element;
    }



    public boolean isElementLoaded(WebElement element, long timeOutInSeconds) throws Exception
    {
    	WebDriverWait tempWait = new WebDriverWait(MyThreadLocal.getDriver(), timeOutInSeconds);
    	 
    	try {
    	    tempWait.until(ExpectedConditions.visibilityOf(element));
            return true;
    	}
    	catch (NoSuchElementException | TimeoutException te) {
    		return false;	
    	}
    	
    }

    public boolean waitForNotExists(WebElement element, long timeOutInSeconds) throws Exception
    {
        WebDriverWait tempWait = new WebDriverWait(MyThreadLocal.getDriver(), timeOutInSeconds);

        try {
            tempWait.until(ExpectedConditions.invisibilityOf(element));
            return true;
        }
        catch (NoSuchElementException | TimeoutException te) {
            return false;
        }
    }
    
    
    public static void waitUntilElementIsVisible(final WebElement element) {
        ExpectedCondition<Boolean> expectation = _driver -> element.isDisplayed();

        Wait<WebDriver> wait = new WebDriverWait(getDriver(), 60);
        try {
            wait.until(webDriver -> expectation);
        } catch (Throwable ignored) {
        }
    }
    
    // method created for mobile app
    
    public static void waitUntilElementIsVisibleOnMobile(final WebElement element) {
        ExpectedCondition<Boolean> expectation = _driver -> element.isDisplayed();

        Wait<WebDriver> wait = new WebDriverWait(MyThreadLocal.getAndroidDriver(), 60);
        try {
            wait.until(webDriver -> expectation);
        } catch (Throwable ignored) {
        }
    }

    public static void waitUntilElementIsInVisible(final WebElement element) {
        ExpectedCondition<Boolean> expectation = _driver -> !element.isDisplayed();

        Wait<WebDriver> wait = new WebDriverWait(getDriver(), 20);
        try {
            wait.until(webDriver -> expectation);
        } catch (Throwable ignored) {

        }
    }

    public void waitForPageLoaded(WebDriver driver) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, 10);
            wait.until(webDriver -> getDriver().executeScript("return document.readyState").equals("complete"));
        } catch (Exception error) {
            try {
                getDriver().navigate().refresh();
            } catch (TimeoutException ignored) {
            }
            
        }
    }

    public static void waitForSeconds(long waitSeconds) {
        waitSeconds = waitSeconds * 1000;
        Calendar currentTime = Calendar.getInstance();
        long currentTimeMillis = currentTime.getTimeInMillis();
        long secCounter = 0;
        while (secCounter < waitSeconds) {
            Calendar newTime = Calendar.getInstance();
            secCounter = (newTime.getTimeInMillis()) - (currentTimeMillis);
        }
    }
    
    public WebElement waitForElementPresence(String element) {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(
                MyThreadLocal.getDriver()).withTimeout(ofSeconds(60))
                .ignoring(NoSuchElementException.class);
        WebElement elementPresent = wait.until(new Function<WebDriver, WebElement>() {
            @Override
            public WebElement apply(WebDriver t) {
                return t.findElement(By
                        .xpath(element));
            }
        });
        return elementPresent;
    }
    
    public static String displayCurrentURL()
    {
        JavascriptExecutor executor = (JavascriptExecutor) getDriver();
        return (String) executor.executeScript("return document.location.href");
      
    }
    
    public void mouseHover(WebElement element)
    {
        Actions actions = new Actions(getDriver());
        actions.moveToElement(element,10,15).click().build().perform();
    }

    public void mouseHoverClick(WebElement element)
    {
        Actions actions = new Actions(getDriver());
        actions.click().build().perform();
    }
    public void mouseToElement(WebElement element){
        Actions actions = new Actions(getDriver());
        actions.moveToElement(element).perform();
    }

    public void mouseHoverDragandDrop(WebElement fromDestination, WebElement toDestination)
    {
        Actions actions = new Actions(getDriver());
        actions.dragAndDrop(fromDestination, toDestination).build().perform();
    }

    //added by Nishant for Optimization of code

    public boolean isElementEnabled(WebElement enabledElement){
        Wait<WebDriver> wait = new FluentWait<WebDriver>(
                MyThreadLocal.getDriver()).withTimeout(ofSeconds(60))
                .pollingEvery(ofSeconds(5))
                .ignoring(org.openqa.selenium.NoSuchElementException.class);
        Boolean element =false;

        try{
            element = wait.until(new Function<WebDriver, Boolean>() {
                @Override
                public Boolean apply(WebDriver t) {
                    boolean display = false;
                    display = enabledElement.isEnabled();
                    if(display )
                        return true;
                    else
                        return false;
                }
            });
        }catch(NoSuchElementException | TimeoutException te){
            return element;
        }
        return element;
    }

    //added by Gunjan
    public boolean isElementEnabled(WebElement enabledElement, long timeOutInSeconds){
        Wait<WebDriver> wait = new FluentWait<WebDriver>(
                MyThreadLocal.getDriver()).withTimeout(ofSeconds(timeOutInSeconds))
                .pollingEvery(ofSeconds(2))
                .ignoring(org.openqa.selenium.NoSuchElementException.class);
        Boolean element =false;

        try{
            element = wait.until(new Function<WebDriver, Boolean>() {
                @Override
                public Boolean apply(WebDriver t) {
                    boolean display = false;
                    display = enabledElement.isEnabled();
                    if(display )
                        return true;
                    else
                        return false;
                }
            });
        }catch(NoSuchElementException | TimeoutException te){
            return element;
        }
        return element;
    }





//    public boolean isElementPresent(WebElement displayElement){
//        Wait<WebDriver> wait = new FluentWait<WebDriver>(
//                MyThreadLocal.getDriver()).withTimeout(Duration.ofSeconds(5))
//                .pollingEvery(Duration.ofSeconds(5))
//                .ignoring(org.openqa.selenium.NoSuchElementException.class);
//        try{
//            Boolean element = wait.until(new Function<WebDriver, Boolean>() {
//                @Override
//                public Boolean apply(WebDriver t) {
//                    boolean display = false;
//
//                    display = displayElement.isDisplayed();
//                    if(display )
//                        return true;
//                    else
//                        return false;
//                }
//            });
//        }catch()
//
//        return false;
//    }



    public boolean areListElementVisible(List<WebElement> listElement){
        Wait<WebDriver> wait = new FluentWait<WebDriver>(
                MyThreadLocal.getDriver()).withTimeout(ofSeconds(60))
                .pollingEvery(ofSeconds(5))
                .ignoring(org.openqa.selenium.NoSuchElementException.class);
        Boolean element =false;
        try{
            element = wait.until(new Function<WebDriver, Boolean>() {
                @Override
                public Boolean apply(WebDriver t) {
                    int size = 0;
                    size = listElement.size();
                    if(size > 0 )
                        return true;
                    else
                        return false;
                }
            });
        }catch(NoSuchElementException | TimeoutException te){
            return element;
        }

        return element;
    }


    public boolean areListElementVisible(List<WebElement> listElement, long timeOutInSeconds ){
        Wait<WebDriver> wait = new FluentWait<WebDriver>(
                MyThreadLocal.getDriver()).withTimeout(ofSeconds(timeOutInSeconds))
                .pollingEvery(ofSeconds(5))
                .ignoring(org.openqa.selenium.NoSuchElementException.class);
        Boolean element =false;
        try{
            element = wait.until(new Function<WebDriver, Boolean>() {
                @Override
                public Boolean apply(WebDriver t) {
                    int size = 0;
                    size = listElement.size();
                    if(size > 0 )
                        return true;
                    else
                        return false;
                }
            });
        }catch(NoSuchElementException | TimeoutException te){
            return element;
        }

        return element;
    }

    public boolean areListElementVisible(List<WebElement> listElement, long timeOutInSeconds, int listSize ){
        Wait<WebDriver> wait = new FluentWait<WebDriver>(
                MyThreadLocal.getDriver()).withTimeout(ofSeconds(timeOutInSeconds))
                .pollingEvery(ofSeconds(5))
                .ignoring(org.openqa.selenium.NoSuchElementException.class);
        Boolean element =false;
        try{
            element = wait.until(new Function<WebDriver, Boolean>() {
                @Override
                public Boolean apply(WebDriver t) {
                    int size = 0;
                    size = listElement.size();
                    if(size > listSize )
                        return true;
                    else
                        return false;
                }
            });
        }catch(NoSuchElementException | TimeoutException te){
            return element;
        }

        return element;
    }

    public void waitForElementLoaded(String css, long timeOutInSeconds) {
        Wait<WebDriver> wait = new FluentWait<WebDriver>(
                MyThreadLocal.getDriver()).withTimeout(ofSeconds(timeOutInSeconds))
                .pollingEvery(ofSeconds(10))
                .ignoring(org.openqa.selenium.NoSuchElementException.class);
        wait.until(new Function<WebDriver, WebElement>() {
            @Override
            public WebElement apply(WebDriver t) {
                return t.findElement(By.cssSelector(css));
            }
        });
    }

    public void selectByVisibleText(WebElement element, String text) throws Exception {
        if (isElementLoaded(element, 10)) {
            clickTheElement(element);
            Select select = new Select(element);
            List<WebElement> options = select.getOptions();
            List<String> optionTexts = new ArrayList<>();
            if (options.size() > 0) {
                for (WebElement option : options) {
                    optionTexts.add(option.getText());
                }
                if (optionTexts.contains(text)) {
                    select.selectByVisibleText(text);
                    SimpleUtils.pass("Select:" + text + " Successfully!");
                } else {
                    SimpleUtils.fail(text + " doesn't exist in options!", false);
                }
            } else {
                SimpleUtils.fail("Select options are empty!", false);
            }
        }else {
            SimpleUtils.fail("Select Element failed to load!", false);
        }
    }

    @FindBy(css = ".react-select__option")
    private List<WebElement> dropDownListOnReact;

    public void selectOptionByLabel(String option) throws Exception {
        if (dropDownListOnReact.size() > 0) {
            for (WebElement optionOnReact : dropDownListOnReact) {
                if (optionOnReact.getText().toLowerCase().trim().contains(option.toLowerCase().trim())) {
                    optionOnReact.click();
                    break;
                }
            }
        }
    }

    public void selectByIndex(WebElement element, int index) throws Exception {
        if (isElementLoaded(element, 5)) {
            click(element);
            Select select = new Select(element);
            List<WebElement> options = select.getOptions();
            if (options.size() > 0) {
               select.selectByIndex(index);
            } else {
                SimpleUtils.fail("Select options are empty!", true);
            }
        }else {
            SimpleUtils.fail("Select Element failed to load!", true);
        }
    }

    public void selectDate(int daysFromToday) {
        int numClicks = -1;
        LocalDate now = LocalDate.now();
        LocalDate wanted = LocalDate.now().plusDays(daysFromToday);
        WebElement btnNextMonth = null;
        List<String> listMonthText = new ArrayList<>();
        if (wanted.getYear() == now.getYear()) {
            numClicks = wanted.getMonthValue() - now.getMonthValue();
        } else {
            numClicks = 12 + wanted.getMonthValue() - now.getMonthValue();
        }
        if (numClicks < 0) {
            numClicks = daysFromToday / 30;
        }
        if (numClicks >0){
            try{
                btnNextMonth = getDriver().findElement(By.cssSelector("span.fa.fa-chevron-right"));
                List<WebElement> textMonthVal = getDriver().findElements(By.cssSelector("div.ranged-calendar__month-name"));
                for(int i =0;i<textMonthVal.size();i++){
                    String[] textMonthArr = textMonthVal.get(i).getText().split(" ");
                    listMonthText.add(textMonthArr[0]);
                }
            }catch(Exception e){
                SimpleUtils.fail("Not able to click Next month arrow",false);
            }
        }

        for (int i = 0; i < numClicks; i++) {
            if(!listMonthText.get(0).equalsIgnoreCase(wanted.getMonth().toString())){
                clickTheElement(btnNextMonth);
            }
        }

        List<WebElement> mCalendarDates = getDriver().findElements(By.cssSelector("div.ranged-calendar__day.ng-binding.ng-scope.real-day:not(.can-not-select)"));
        for (WebElement mDate : mCalendarDates) {
            if (Integer.parseInt(mDate.getText()) == wanted.getDayOfMonth()) {
                clickTheElement(mDate);
                return;
            }
        }
    }

    public void selectDateInTemplate(int daysFromToday) {
        int numClicks = -1;
        LocalDate now = LocalDate.now();
        LocalDate wanted = LocalDate.now().plusDays(daysFromToday);
        WebElement btnNextMonth = null;
        List<String> listMonthText = new ArrayList<>();
        if (wanted.getYear() == now.getYear()) {
            numClicks = wanted.getMonthValue() - now.getMonthValue();
        } else {
            numClicks = 12 + wanted.getMonthValue() - now.getMonthValue();
        }
        if (numClicks < 0) {
            numClicks = daysFromToday / 30;
        }
        if (numClicks >0){
            try{
                btnNextMonth = getDriver().findElements(By.cssSelector("lg-single-calendar div a")).get(1);
                WebElement textMonthVal = getDriver().findElement(By.cssSelector("span.lg-single-calendar-month"));
                String[] textMonthArr = textMonthVal.getText().split(" ");
                listMonthText.add(textMonthArr[0]);
            }catch(Exception e){
                SimpleUtils.fail("Not able to click Next month arrow",false);
            }
        }

        for (int i = 0; i < numClicks; i++) {
            if(!listMonthText.get(0).equalsIgnoreCase(wanted.getMonth().toString())){
                clickTheElement(btnNextMonth);
            }
        }

        List<WebElement> mCalendarDates = getDriver().findElements(By.cssSelector("div.lg-single-calendar-date.ng-scope span"));
        for (WebElement mDate : mCalendarDates) {
            if(mDate.getText()==null || mDate.getText().isEmpty()){
                continue;
            }else if(Integer.parseInt(mDate.getText()) == wanted.getDayOfMonth()){
                    clickTheElement(mDate);
//                    return;
                    break;
            }
        }
    }

    public String selectDateForTimesheet(int daysFromToday) {
        LocalDate now = LocalDate.now();
        LocalDate wanted = LocalDate.now().minusDays(daysFromToday);
        String dateWanted = wanted.toString();
//        LocalDate wanted1 = LocalDate.now().minusDays()
        WebElement btnPreviousMonth = null;
        int numClicks = now.getMonthValue() - wanted.getMonthValue();
        if (numClicks < 0) {
            numClicks = daysFromToday / 30;
        }
        if (numClicks >0){
            try{
                btnPreviousMonth = getDriver().findElement(By.cssSelector("a[ng-click='$ctrl.changeMonth(-1)']"));
            }catch(Exception e){
                SimpleUtils.fail("Not able to click Next month arrow",false);
            }
        }

        for (int i = 0; i < numClicks; i++) {
            click(btnPreviousMonth);
        }

        List<WebElement> mCalendarDates = getDriver().findElements(By.cssSelector("div[class='lg-single-calendar-date ng-scope']"));
        for (WebElement mDate : mCalendarDates) {
            if (Integer.parseInt(mDate.getText()) == wanted.getDayOfMonth()) {
                mDate.click();
                break;
            }
        }
        return dateWanted;
    }


    public HashMap<String,String> getTimeOffDate(int fromDate, int toDate) {
        HashMap<String, String> timeOffDate = new HashMap<>();
        String timeOffStartDate = getDateCalculation(fromDate);
        String timeOffEndDate = getDateCalculation(toDate);
        timeOffDate.put("startDateTimeOff", timeOffStartDate);
        timeOffDate.put("endDateTimeOff", timeOffEndDate);
        return timeOffDate;
    }

    public HashMap<String,String> getTimeOffDateWithYear(int fromDate, int toDate) {
        HashMap<String, String> timeOffDate = new HashMap<>();
        String timeOffStartDate = getDateWithYearCalculation(fromDate);
        String timeOffEndDate = getDateWithYearCalculation(toDate);
        timeOffDate.put("startDateWithYearTimeOff", timeOffStartDate);
        timeOffDate.put("endDateWithYearTimeOff", timeOffEndDate);
        return timeOffDate;
    }

    public String getDateWithYearCalculation(int daysFromToday){
        LocalDate now = LocalDate.now();
        LocalDate wanted = now.plusDays(daysFromToday);
        String monthName = wanted.getMonth().toString().substring(0,1) + wanted.getMonth().toString().substring(1,3).toLowerCase();
        String timeOffDate = wanted.getYear() + " " + monthName + " " + wanted.getDayOfMonth();
        return timeOffDate;
    }

    public String getDateCalculation(int daysFromToday){

        LocalDate now = LocalDate.now();
        LocalDate wanted = LocalDate.now().plusDays(daysFromToday);
        String dayOfWeek = wanted.getDayOfWeek().toString().substring(0,1) + wanted.getDayOfWeek().toString().substring(1,3).toLowerCase();
        String monthName = wanted.getMonth().toString().substring(0,1) + wanted.getMonth().toString().substring(1,3).toLowerCase();
        String timeOffDate = dayOfWeek + ", " + monthName + " " + wanted.getDayOfMonth();
        return timeOffDate;
    }


    public static boolean isClickable(WebElement element, long timeOutInSeconds ) {
        try {
            WebDriverWait wait = new WebDriverWait(MyThreadLocal.getDriver(), timeOutInSeconds);
            wait.until(ExpectedConditions.elementToBeClickable(element));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //added by Nishant

    public String searchDateForTimesheet(int daysFromToday) {
        LocalDate now = LocalDate.now();
        LocalDate wanted = LocalDate.now().minusDays(daysFromToday);
        String dateWanted = wanted.toString();
        return dateWanted;
    }

    //added by Nishant

    public void pressByElement (MobileElement element, long seconds) {
        new TouchAction(getAndroidDriver())
                .press(element(element))
                .waitAction(waitOptions(ofSeconds(seconds)))
                .release()
                .perform();
    }

    public void verticalSwipeByPercentages(double startPercentage, double endPercentage, double anchorPercentage, long seconds) {
        Dimension size = getAndroidDriver().manage().window().getSize();
        int anchor = (int) (size.width * anchorPercentage);
        int startPoint = (int) (size.height * startPercentage);
        int endPoint = (int) (size.height * endPercentage);

        new TouchAction(getAndroidDriver())
                .press(point(anchor, startPoint))
                .waitAction(waitOptions(ofSeconds(seconds)))
                .moveTo(point(anchor, endPoint))
                .release().perform();
    }

    public void longPressByElement (MobileElement element, MobileElement element1,long seconds) {
        new TouchAction(getAndroidDriver())
                .longPress(element).moveTo(element1.getCenter().x,element1.getCenter().y)
                .waitAction(waitOptions(ofSeconds(seconds)))
                .release()
                .perform();
    }

    public void swipeByElements (MobileElement startElement, MobileElement endElement, long seconds) {
        int startX = startElement.getLocation().getX() + (startElement.getSize().getWidth() / 2);
        int startY = startElement.getLocation().getY() + (startElement.getSize().getHeight() / 2);

        int endX = endElement.getLocation().getX() + (endElement.getSize().getWidth() / 2);
        int endY = endElement.getLocation().getY() + (endElement.getSize().getHeight() / 2);

        new TouchAction(getAndroidDriver())
                .press(point(startX,startY))
                .waitAction(waitOptions(ofSeconds(seconds)))
                .moveTo(point(endX, endY))
                .release().perform();
    }

    public void swipeLeftByElement(MobileElement element, long seconds){
        int screenWidth = (int) getAndroidDriver().manage().window().getSize().width;
        int elementLocation = element.getLocation().getY();
        int screenWidth90 = (int) (.9 * screenWidth);
        int screenWidth65 = (int) (.65 * screenWidth);
        int screenWidth40 = (int) (.4 * screenWidth);

        TouchAction touchAction = new TouchAction(getAndroidDriver());
        touchAction.press(screenWidth90, elementLocation)
                .moveTo(screenWidth65, elementLocation)
                .waitAction(waitOptions(ofSeconds(seconds)))
                .moveTo(screenWidth40, elementLocation)
                .release()
                .perform();
    }


    public void swipeLeftByElement(long seconds){
        TouchAction act=new TouchAction(getAndroidDriver());
        Dimension size = getAndroidDriver().manage().window().getSize();
        int anchor = (int) (size.height * 0.5);
        int startPoint = (int) (size.width * 0.9);
        int endPoint = (int) (size.width * 0.01);
//        new TouchAction(driver).press(startPoint, anchor).waitAction(duration).moveTo(endPoint, anchor).release().perform();
        act.press(startPoint, anchor).waitAction(waitOptions(ofSeconds(seconds))).moveTo(endPoint, anchor).release().perform();
    }

    public void scrollUsingTouchActions_ByElements(MobileElement startElement, MobileElement endElement) {
        TouchAction actions = new TouchAction(getAndroidDriver());
        actions.press(startElement).waitAction(Duration.ofSeconds(2)).moveTo(endElement).release().perform();
    }

    public void swipeHorizontalByElement(MobileElement startElement, long seconds){
        Point point = startElement.getLocation();
        int startY = point.y;
        int endY = point.y;

        int startX = (int) ((getAndroidDriver().manage().window().getSize().getWidth()) * 0.80);
        int endX = (int) ((getAndroidDriver().manage().window().getSize().getWidth()) * 0.20);

        TouchAction actions = new TouchAction(getAndroidDriver());
        actions.press(startX, startY).waitAction(Duration.ofSeconds(seconds)).moveTo(endX, endY).release().perform();
    }


    public void swipeUp(int howManySwipes,long seconds) {
        Dimension size = getAndroidDriver().manage().window().getSize();
        // calculate coordinates for vertical swipe
        int startVerticalY = (int) (size.height * 0.8);
        int endVerticalY = (int) (size.height * 0.21);
        int startVerticalX = (int) (size.width / 2.1);
        try {
            for (int i = 1; i <= howManySwipes; i++) {
                new TouchAction<>(getAndroidDriver()).press(point(startVerticalX, startVerticalY))
                        .waitAction(waitOptions(ofSeconds(seconds))).moveTo(point(startVerticalX, endVerticalY)).release()
                        .perform();
            }
        } catch (Exception e) {
            //print error or something
        }
    }

    public void swipeUp1(int howManySwipes,long seconds) {
        Dimension size = getAndroidDriver().manage().window().getSize();
        // calculate coordinates for vertical swipe
        int startVerticalY = (int) (size.height * 0.8);
        int endVerticalY = (int) (size.height * 0.0001);
        int startVerticalX = (int) (size.width / 2.1);
        try {
            for (int i = 1; i <= howManySwipes; i++) {
                new TouchAction<>(getAndroidDriver()).press(point(startVerticalX, startVerticalY))
                        .waitAction(waitOptions(ofSeconds(seconds))).moveTo(point(startVerticalX, endVerticalY)).release()
                        .perform();
            }
        } catch (Exception e) {
            //print error or something
        }
    }

    public void swipeUpUntilElementFound(int howManySwipes,long seconds, MobileElement element) {
        Dimension size = getAndroidDriver().manage().window().getSize();
        // calculate coordinates for vertical swipe
        int startVerticalY = (int) (size.height * 0.8);
        int endVerticalY = (int) (size.height * 0.0001);
        int startVerticalX = (int) (size.width / 2.1);
        try {
            for (int i = 1; i <= howManySwipes; i++) {
                new TouchAction<>(getAndroidDriver()).press(point(startVerticalX, startVerticalY))
                        .waitAction(waitOptions(ofSeconds(seconds))).moveTo(point(startVerticalX, endVerticalY)).release()
                        .perform();
                boolean display = getEnabledElement(element);
                if(display){
                    System.out.println("Count of swipe is " +i);
                    break;
                }

            }
        } catch (Exception e) {
            //print error or something
        }
    }

    public boolean getEnabledElement(MobileElement element){
        boolean display = element.isEnabled();
        if(display )
            return true;
        else
            return false;
    }

    //added by Nishant
    public String getActiveWeekText() throws Exception {
        WebElement activeWeek = MyThreadLocal.getDriver().findElement(By.className("day-week-picker-period-active"));
        if (isElementLoaded(activeWeek,10))
            return activeWeek.getText().replace("\n", " ");
        return "";
    }

    public boolean compareDMAndSMViewDatePickerText(String datePickerTxtDMView) throws Exception{
        String datePickerTxtSMView = getActiveWeekText();
        boolean result = false;
        if(datePickerTxtDMView.equals(datePickerTxtSMView)){
            result = true;
            SimpleUtils.pass("Date Picker Text from" +
                    " DM View " + datePickerTxtDMView + " matches with Date picker text from SM View " + datePickerTxtSMView);
        }else{
            SimpleUtils.fail("Date Picker Text from" +
                    " DM View " + datePickerTxtDMView + " not matches with Date picker text from SM View " + datePickerTxtSMView,true);
        }
        return result;
    }

    public void moveDayViewCards(WebElement webElement, int xOffSet)
    {
        Actions builder = new Actions(MyThreadLocal.getDriver());
        builder.moveToElement(webElement)
                .clickAndHold()
                .moveByOffset(xOffSet, 0)
                .release()
                .build()
                .perform();
    }

    public void clickTheElement(WebElement element) {
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", element);
        waitForSeconds(2);
    }

    public void clearTheText(WebElement element) {
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].value=\"\";", element);
        waitForSeconds(1);
    }

    public void moveElement(WebElement webElement, int yOffSet)
    {
        Actions builder = new Actions(MyThreadLocal.getDriver());
        builder.moveToElement(webElement)
                .clickAndHold()
                .moveByOffset(0, yOffSet)
                .release()
                .build()
                .perform();
    }

    public void closeAuditLogDialog() throws Exception {
        try {
            if (isElementLoaded(getDriver().findElement(By.cssSelector(".lg-slider-pop__title-dismiss")), 10)) {
                clickTheElement(getDriver().findElement(By.cssSelector(".lg-slider-pop__title-dismiss")));
                SimpleUtils.pass("Cilck on Close button on Audit log dialog successfully!");
            }
        } catch (Exception e) {
            // Do nothing
        }
    }

    public Boolean isElementDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception NoSuchElementException) {
            return false;
        }
    }
    public void removeHidden(WebElement element){
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].style.display='block';", element);
    }
    public ArrayList getWebElementsText(List<WebElement> webElementsList) {
        ArrayList<String> list = new ArrayList<>();
        webElementsList.forEach((e) -> {
            list.add(e.getText());
        });
        return list;
    }

    public void highlightElement(WebElement element){
        try{
            ((JavascriptExecutor) getDriver()).executeScript("arguments[0].setAttribute('style',arguments[1])",element,"background:yellow;border:2px solid red");
        }catch(Exception NoSuchElementException){
            System.out.println("element" +element+ "is not found");
        }
    }

    public static boolean isExist(WebElement element)
    {
        try {
            element.isDisplayed();
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public static boolean isElementExist(String cssLocator)
    {
        boolean flag;
        try {
            if(getDriver().findElements(By.cssSelector(cssLocator)).size()>0){
                flag = true;
            }else {
                flag = false;
            }
        } catch (NoSuchElementException e) {
            flag = false;
        }
        return flag;
    }

    public static boolean isEleExist(String xpathLocator)
    {
        boolean flag;
        try {
            if(getDriver().findElements(By.xpath(xpathLocator)).size()>0){
                flag = true;
            }else {
                flag = false;
            }
        } catch (NoSuchElementException e) {
            flag = false;
        }
        return flag;
    }

//
//
//     public void assertIsDisplay(Map<String,String> map){
//        Map<String,BasePage> elementMap = currentPage.getUniqueKeyElementsMap();
//        driver = driverConfig.getDriver();
//         for (String key : map.keySet():
//              ) {
//             if (!key.equals ("element name")) {
//                 System.out.println("get element:" + key);
//                 elementMap.get(key).waitElement(driver,20);
//                 Assert.assertTrue("the element is not display:" +key,elementMap.get(key).isDispaly);
//             }
//
//         }
//     }
//
//     public Map<String,BasePage> getUniqueKeyElementsMap(){
//        if(resultList == null){
//            resultList = new HashMap<>();
//            for (GherkinId gherkinId : gherkinIdList) {
//                if (StringUtils.isNotBlank(gherkinId.name()) ){
//                    resultList.put(gherkinId.name),getElementByGherkinId(gherkinId);
//                }
//            }
//        }
//        return  resultList;
//     }


    public static boolean isBelongPeriodTime(String compareTime, String periodTime) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("hh:mma");
        Date startTimeDate1;
        Date endTimeDate1;
        Date startTimeDate2;
        Date endTimeDate2;
        startTimeDate1 = df.parse(compareTime.split("-")[0].trim().replace(" ", ""));
        endTimeDate1 = df.parse(compareTime.split("-")[1].trim().replace(" ", ""));
        startTimeDate2 = df.parse(periodTime.split("-")[0].trim());
        endTimeDate2 = df.parse(periodTime.split("-")[1].trim());

        if (startTimeDate1.getHours() >= startTimeDate2.getHours()
                && endTimeDate1.getHours() <= endTimeDate2.getHours()) {
            SimpleUtils.report("The time :" + compareTime +" is belong to : "+ periodTime);
            return true;
        } else {
            SimpleUtils.report("The time :" + compareTime +" is not belong to : "+ periodTime);
            return false;
        }
    }

    public static boolean isSorted(List<String> dateStrings, boolean isAsc, String format) throws ParseException {
        boolean isSort = false;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        if (dateStrings != null && dateStrings.size() > 0) {
            for (int i = 0; i < dateStrings.size() - 1; i++) {
                try {
                    Date date1 = sdf.parse(dateStrings.get(i));
                    Date date2 = sdf.parse(dateStrings.get(i+1));
                    if (isAsc) {
                        //sorted by ascend
                        if (date2.before(date1)) {
                            isSort = true;
                        }
                    } else {
                        //sorted by descend
                        if (date2.after(date1)) {
                            isSort = false;
                        }
                    }
                } catch (ParseException pe) {
                    isSort = false;
                    break;
                }
            }
        } else {
            isSort = false;
        }
        return isSort;
    }
}
