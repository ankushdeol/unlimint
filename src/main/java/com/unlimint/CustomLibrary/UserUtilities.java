package com.unlimint.CustomLibrary;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.swing.text.Utilities;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.unlimint.CommanLocators.CommonLocator;
import com.unlimint.CustomLibrary.PropertyFile;
import com.unlimint.CustomLibrary.UserUtilities;
import com.unlimint.CustomReport.ListenerClass;

public class UserUtilities extends ListenerClass implements CommonLocator {

    private static int WAIT_FOR_ELEMENT_TIMEOUT = 15;

    public Utilities utils = new Utilities();
    public static WebDriver driver = null;
    public String filepath = "..\\Data\\data.xlsx";
    public String URL = getWebappUrl("advisor");
    public String Browser = PropertyFile.prop.getProperty("Browser");

    public static final boolean mobile = "mobile".equals(PropertyFile.prop.getProperty("mode"));

    // -------------------------------------------------------------------------

    // if true, the browser will be left open on failure
    // set this to true when creating the test scripts
    // set this to false when running in automated mode
    static boolean DEBUG = "true".equals(PropertyFile.prop.getProperty("debug"));

    // if true, the execution will be slowed down to allow
    // viewer to see what's going on. Set this to true
    // anywhere in your script when you want to slow it down.
    // put it back to false when
    private static boolean _slow = false;
    private static final long SLOW_TIME = 5000l;

    /**
     * Call this method in your script while debugging when you want
     * to slow down script execution to be able to better see what
     * is happening.
     *
     * You can resume with call to resumeNormalSpeed().
     * 
     * Note: This will only have effect in DEBUG mode.
     */
    public static void slowDown() {
        if (DEBUG)
            _slow = true;
        else
            warn("slowDown call left around in committed code");
    }

    public static void resumeNormalSpeed() {
        if (DEBUG)
            _slow = false;
        else
            warn("resumeNormalSpeed call left around in committed code");
    }

    // -------------------------------------------------------------------------

    public void chromeBrowser() {
        ChromeOptions options = new ChromeOptions();

        options.addArguments("--disable-extensions");
        // options.addArguments("chrome.switches","--disable-extensions");

        System.setProperty(
                PropertyFile.prop.getProperty("chromeDriverProperty"),
                PropertyFile.prop.getProperty("chromeDriverPath")
        );

        // LoggingPreferences logPrefs = new LoggingPreferences();
        // logPrefs.enable(LogType.BROWSER, Level.ALL);
        // DesiredCapabilities caps = DesiredCapabilities.chrome();
        // caps.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);

        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.Jdk14Logger"); 
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        if (mobile)
            driver.manage().window().setSize(new Dimension(400, 850));
        else
            driver.manage().window().maximize();
    }

	public void firefoxBrowser() {
		/* Firefox driver code */
		System.setProperty(
			PropertyFile.prop.getProperty("firefoxDriverProperty"),
			PropertyFile.prop.getProperty("firefoxDriverPath")
		);

        DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        capabilities.setCapability("marionette", false);
        FirefoxOptions opt = new FirefoxOptions();
        opt.merge(capabilities);
        driver = new FirefoxDriver(opt);
    }

    public void adminLogin() {
        wcmLogin(
                PropertyFile.prop.getProperty("username"),
                PropertyFile.prop.getProperty("password")
		);
	}

    public void fieldServiceLogin() {
        wcmLogin(
                PropertyFile.prop.getProperty("fieldusername"),
                PropertyFile.prop.getProperty("fieldpassword")
        );
    }

    private void wcmLogin(String username, String password) {
       //driver.get(getWebappUrl("advisor"));
        trace("fill username");
        sendKeys(USERNAME,username);
        trace("fill password");
        sendKeys(PASS,password);
        wait1Sec();
        trace("click login button");
        click(LoginButton);
    }

    // -------------------------------------------------------------------------

    public void openBrowser() {
        if (Browser.equalsIgnoreCase("Chrome")) {
            chromeBrowser();
        } else if (Browser.equalsIgnoreCase("Firefox")) {
            firefoxBrowser();
        } else {
            log("<<<<<<<<   Invalid Browser  >>>>>");
        }
    }

    private static String getWebappUrl(String webapp) {
        String skdUrl = PropertyFile.prop.getProperty("SKD_URL");
        if (skdUrl.endsWith("/"))
            return skdUrl + webapp;
        else
            return skdUrl + "/" + webapp;
    }

    /**
     * For opening the webpage.
     */
    public void loginAdminUser() {
      openBrowser();
		
		wait5Sec();
        adminLogin();
    }

    public void loginfieldServiceUser() {
        openBrowser();
        fieldServiceLogin();
    }

    public void studioAdminLogin() {
        openBrowser();
  
    }

    public void openWelcomePage() {
        openBrowser();
        driver.get(PropertyFile.prop.getProperty("WCM_URL"));
    }

    public void openAdvisor() {
        if (Browser.equalsIgnoreCase("Chrome")) {
            chromeBrowser();
            
        } else if (Browser.equalsIgnoreCase("Firefox")) {
            firefoxBrowser();
        }
        driver.get(getWebappUrl("advisor"));
    }

    public void closeBrowser() {
        if (driver != null)
            driver.quit();
    }

    // -------------------------------------------------------------------------

    public void fnHighlightMe(By element) {
        WebElement ele = driver.findElement(element);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        // for (int i = 0; i < 3; i++) {
        js.executeScript("arguments[0].setAttribute('style', arguments[1]);", ele,
                "color: Red;  border: 2px solid OrangeRed;");
        // js.executeScript("arguments[0].setAttribute('style',
        // arguments[1]);",ele, "");
        // }
    }

    /**
     * Draws a red border around the found element. Does not set it back
     * anyhow.
     */
    public static WebElement findElement(By by) {
        WebElement elem = driver.findElement(by);
        // draw a border around the found element
        if (driver instanceof JavascriptExecutor) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].style.border='3px solid  yellow'", elem);
        }
        return elem;
    }

    public void HandleAlertWithText(String text) {
        try {
            String str = driver.switchTo().alert().getText().trim();
            if (str.equals(text)) {
                log("String Matched  <  " + str + "   > ---pass");
                driver.switchTo().alert().accept();
            } else {
                log("String Not Matched --fail");
            }
        } catch (UnhandledAlertException e) {
            e.printStackTrace();
        }
    }

    public void acceptAlertIfAvailable(long timeout) {
        long waitForAlert = System.currentTimeMillis() + timeout;
        boolean boolFound = false;
        do {
            try {
                Alert alert = UserUtilities.driver.switchTo().alert();
                if (alert != null) {
                    alert.accept();
                    boolFound = true;
                }
            } catch (NoAlertPresentException ex) {
            }
        } while ((System.currentTimeMillis() < waitForAlert) && (!boolFound));
    }

    public String getcelldata(String sheet, int row, int cell) {
        String value = "";
        try {
            FileInputStream fis = new FileInputStream("..\\Data\\data.xlsx");

            Workbook wb = WorkbookFactory.create(fis);
            Sheet s = wb.getSheet(sheet);
            Row r = s.getRow(row);
            Cell c = r.getCell(cell);
            value = c.getStringCellValue().trim();
            System.out.println(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;

    }

    /**
     * Purpose:Selecting the dropdown using visible text
     */
    public void SelectDropdownMenuUsingVisibleText(By locator, String locatortext) {

        try {
            WebElement element = driver.findElement(locator);
            highlightOnLocator(locator);
            Select se = new Select(element);
            se.selectByVisibleText(locatortext);
            log("Selecting the value from dropdown and value is < " + locatortext + ">");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Selecting the dropdown using index
     */
    public void selectDropdownMenuUsingIndex(By locator, int locatorIndex) {
        try {
            log("Selecting the value from dropdown and value is < " + locatorIndex + ">");
            WebElement element = driver.findElement(locator);
            highlightOnLocator(locator);
            Select se = new Select(element);
            se.selectByIndex(locatorIndex);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Selecting the dropdown using value
     */
    public void selectDropdownMenuUsingValue(By locator, String locatortext) {
        try {
            WebElement element = driver.findElement(locator);
            highlightOnLocator(locator);
            Select se = new Select(element);
            se.selectByValue(locatortext);
            log("Selecting the value from dropdown and value is < " + locatortext + ">");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    

    public static boolean exists(By selector) {

        return !driver.findElements(selector).isEmpty();
    }

    public static boolean isDisplayed(By selector) {
        if (!exists(selector))
            return false;

        WebElement element = driver.findElement(selector);
        if (element == null)
            return false;
        return element.isDisplayed();
    }

    public static void assertElementExists(By selector) {
        if (!exists(selector))
            Assert.fail("The specified element doesn't exist: " + selector);
    }

    /**
     * Returns true if at least one element matched by
     * the selector is visible, false otherwise
     * 
     * @param selector
     * @return
     */
    public boolean atLeastOneVisible(By selector) {
        List<WebElement> elements = driver.findElements(selector);
        if (elements.isEmpty())
            return false;
        for (WebElement element : elements) {
            if (element.isDisplayed())
                return true;
        }
        return false;
    }

    /**
     * Returns true if at no element is matched by
     * the selector or none of them are visible,
     * false otherwise
     * 
     * @param selector
     * @return
     */
    public boolean noneVisible(By selector) {
        return !atLeastOneVisible(selector);
    }

    /**
     * Makes sure there is at least one element matched by the selector that
     * has content starting with the specified text.
     * 
     * @param selector
     * @param withText
     * @param message
     */
    public static void assertElementStartingWithTextExists(By selector, String withText, String message) {
        List<WebElement> elements = driver.findElements(selector);
        boolean found = false;
        for (WebElement element : elements) {
            found |= element.getText().startsWith(withText);
            trace("  *** -> " + element.getText());
        }
        Assert.assertTrue(found, message);
    }

    public static void assertElementDoesntExists(By selector) {
        try {
            driver.findElement(selector);
        } catch (NoSuchElementException ex) {
            return;
        }
        Assert.fail("The specified element should not have existed: " + selector);
    }

    // -------------------------------------------------------------------------

    /**
     * inserting value to field
     */
    public void input(By locator, String str) {
        try {
            highlightOnLocator(locator);
            WebElement element = driver.findElement(locator);
            element.clear();
            element.sendKeys(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Verify that webelement Present on page
     */
    public void verifyElementIsPresentOnPage(By locatorKey) {
        try {
            driver.findElement(locatorKey).isDisplayed();
            highlightOnLocator(locatorKey);
            // WebElement ele=driver.findElement(locatorKey);
            log("Element is present on page --- pass  [  " + locatorKey.toString().substring(10) + "]");

        } catch (NoSuchElementException e) {
            e.printStackTrace();
            log("Fail - Element is absent from page < " + locatorKey.toString());
        }
    }

    /**
     * Highlight the element to visible at time of execution
     */
    public void highlightOnLocator(By element) {
        WebElement ele = driver.findElement(element);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].setAttribute('style', arguments[1]);", ele,
                "color: blue; border: 2px solid Magenta;");
    }

    // -------------------------------------------------------------------------

    public final static boolean INFO = true;
    public final static boolean TRACE = true;

    public static void warn(String message) {
        System.out.println(" WARN " + message);
    }

    public static void info(String message) {
        if (INFO)
            System.out.println(" INFO " + message);
    }

    public static void logTestCase(String testCaseFile, int testCaseId, String description) {
        if (INFO)
            System.out.println("***" + testCaseFile + " - " + testCaseId + " - " + description);
    }

    public static void logMethodStart(String methodName) {
        if (INFO)
            System.out.println(" ** START METHOD: " + methodName);
    }

    public static void logMethodEnd(String methodName) {
        if (INFO)
            System.out.println(" ** END METHOD: " + methodName);
    }

    public static void trace(String message) {
        if (TRACE)
            System.out.println("TRACE " + message);
    }

    // -------------------------------------------------------------------------

    @Deprecated
    public static void waitSeconds(int l) {
        log("waiting for " + l + " seconds");
        try {
            Thread.sleep(l * 1000);
            slow();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    public static void waitforElement() {
        try {
            Thread.sleep(3000);
            slow();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    public static void waitForExpectedCondition() {
        try {
            Thread.sleep(10000);
            slow();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    public static void longWait() {
        try {
            Thread.sleep(30000);
            slow();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // -------------------------------------------------------------------------

    public static void waitHalfSec() {
        try {
            Thread.sleep(500);
            slow();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void wait1Sec() {
        try {
            Thread.sleep(1000);
            slow();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void wait2Sec() {
        try {
            Thread.sleep(2000);
            slow();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void wait3Sec() {
        try {
            Thread.sleep(3000);
            slow();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void wait5Sec() {
        try {
            Thread.sleep(5000);
            slow();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void slow() {
        try {
            if (_slow)
                Thread.sleep(SLOW_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // -------------------------------------------------------------------------

    // Consumer<Long> warnOfSlowness = (x) => {
    // warn(x + " > " + WAIT_FOR_ELEMENT_TIMEOUT);
    // };

    public static void waitForOneOfElementsToBeClickable(By... selectors) {
        waitForOneOfElementsToBeClickable(WAIT_FOR_ELEMENT_TIMEOUT, selectors);
    }

    public static void waitForOneOfElementsToBeClickable(int maxWaitTimeSec, By... selectors) {
        long start = System.currentTimeMillis();

        ExpectedCondition<WebElement>[] conditions = new ExpectedCondition[selectors.length];
        for (int i = 0; i < selectors.length; ++i) {
            conditions[i] = ExpectedConditions.elementToBeClickable(selectors[i]);
        }
        new WebDriverWait(driver, maxWaitTimeSec);
        long end = System.currentTimeMillis();
        if (end - start > 5000) {
            // TODO
        }

        slow();
    }

    public static void waitForOneOfElementsToAppear(By... selectors) {
        waitForOneOfElementsToAppear(WAIT_FOR_ELEMENT_TIMEOUT, selectors);
    }

    public static void waitForOneOfElementsToAppear(int maxWaitTimeSec, By... selectors) {
        ExpectedCondition<WebElement>[] conditions = new ExpectedCondition[selectors.length];
        for (int i = 0; i < selectors.length; ++i) {
            conditions[i] = ExpectedConditions.visibilityOfElementLocated(selectors[i]);
        }
   //     new WebDriverWait(driver, maxWaitTimeSec).until(ExpectedConditions.or(conditions));
        // until(ExpectedConditions.presenceOfElementLocated(selector));
        slow();
    }

    public static WebElement waitForElementToExist(By selector) {
        //new WebDriverWait(driver, WAIT_FOR_ELEMENT_TIMEOUT).until(ExpectedConditions.presenceOfElementLocated(selector));
        slow();
        return driver.findElement(selector);
    }

    public static WebElement waitForElementToAppear(By selector) {
		/*
		 * new WebDriverWait(driver, WAIT_FOR_ELEMENT_TIMEOUT)
		 * .until(ExpectedConditions.visibilityOfElementLocated(selector));
		 */
        slow();
        return driver.findElement(selector);
    }

	public static void waitForElementToDisappear(
			By selector) {/*
							 * new WebDriverWait(driver, WAIT_FOR_ELEMENT_TIMEOUT)
							 * .until(ExpectedConditions.invisibilityOfElementLocated(selector));
							 */
        slow();
    }

    // -------------------------------------------------------------------------

    public static void click(By locator) {
        driver.findElement(locator).click();
    }

    /**
     * Wait for the element to appear, and click on it when it did
     * 
     * @param locator
     */
    public static void waitAndClick(By locator) {
        StaleElementReferenceException staleException = null;
        for (int retry = 0; retry < 3; retry++) {
            try {
                waitForElementToAppear(locator).click();
                return;
            }
            catch (StaleElementReferenceException stale) {
                staleException = stale;
            }
        }
        throw staleException;
    }

    public static void clickNthElement(int nth, By locator) {
        List<WebElement> elements = driver.findElements(locator);
        if (elements.size() < nth)
            Assert.fail(nth + "th element not found for " + locator + " only " + elements.size()
                    + " found.");
        else
            elements.get(nth - 1).click();
    }

    public static String getText(By locator) {
        return driver.findElement(locator).getText();
    }

    /**
     * Wait for the element to appear, and return it's text content
     * 
     * @param locator
     */
    public static String waitAndGetText(By locator) {
        return waitForElementToAppear(locator).getText();
    }

    public static void sendKeys(By locator, CharSequence keys) {
        driver.findElement(locator).sendKeys(keys);
    }

    /**
     * Wait for the element to appear, and return it's text content
     * 
     * @param locator
     */
    public static void waitAndSendKeys(By locator, CharSequence keys) {
        waitForElementToAppear(locator).sendKeys(keys);
    }

    // -------------------------------------------------------------------------

    /**
     * Double click on the element
     */
    public void doubleClick(By element) {
        fnHighlightMe(element);
        try {
            Actions action = new Actions(driver).doubleClick();
            action.build().perform();
            log("Double clicked the element" + element.toString().substring(10));
        } catch (StaleElementReferenceException e) {
            log("Element is not attached to the page document " + e.getStackTrace());
        } catch (NoSuchElementException e) {
            log("Element " + element + " was not found in DOM " + e.getStackTrace());
        } catch (Exception e) {
            log("Element " + element + " was not clickable " + e.getStackTrace());
        }
    }

    // public String getDataFromExcel(String Sheetname, int rowcount, int
    // columncount) {
    // String st="";
    // try {
    // FileInputStream input = new FileInputStream(new File(filepath));
    // XSSFWorkbook wb = new XSSFWorkbook(input);
    // XSSFSheet sh = wb.getSheet(Sheetname);
    // XSSFRow row = sh.getRow(rowcount);
    // st = row.getCell(columncount).getStringCellValue();
    // }
    // catch(Exception e) {
    // System.out.println(e);
    // }

    // return st;
    // }

    // public String getExcelData(String sheet, int rnum, int cnum) {
    // try {
    // FileInputStream fis = new FileInputStream(filepath);
    // Workbook wb = WorkbookFactory.create(fis);
    // Sheet sh = wb.getSheet(sheet);
    // String value = sh.getRow(rnum).getCell(cnum).getStringCellValue().trim();
    // log(value);
    // } catch (Exception e) {
    // }
    // return null;
    // }

    // public int writeDataInExcel(int rowcount, int columncount, String Sheetname,
    // String value) {
    // try {
    // FileInputStream input = new FileInputStream(new File(filepath));
    // XSSFWorkbook wb = new XSSFWorkbook(input);
    // XSSFSheet sh = wb.getSheet(Sheetname);
    // XSSFRow row = sh.getRow(rowcount);
    // FileOutputStream webdata = new FileOutputStream(filepath);
    // row.createCell(columncount).setCellValue(value);
    // wb.write(webdata);
    // }
    // catch(Exception e) {
    // }
    // return columncount;
    // }

    public static void captureScreenshots(String filename) throws IOException {
        TakesScreenshot objScreenshot = ((TakesScreenshot) driver);
        File objSrcFile = objScreenshot.getScreenshotAs(OutputType.FILE);

        File objDestFile = new File("F:\\Selenium\\Snapshots\\" + filename + ".jpg"); // * Provide any path on
        // your system.

        FileUtils.copyFile(objSrcFile, objDestFile);
    }
}
