package com.gb.fineos.script;

import com.gb.fineos.domain.TestCase;
import com.gb.fineos.factory.PropertiesFactory;
import com.gb.fineos.util.XlsReader;
import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public abstract class BaseTest {
    private static final Logger LOG = Logger.getLogger(BaseTest.class);

    private static final String BROWSER = "Browser";
    private static final String BROWSER_MOZILLA = "Firefox";
    private static final String BROWSER_IE = "IE";
    private static final String BROWSER_CHROME = "Chrome";

    private static final String OPERATING_SYSTEM = "Operating_System";
    private static final String OPERATING_SYSTEM_WINDOWS = "windows";
    private static final String WEBDRIVER_GECKO_DRIVER = "webdriver.gecko.driver";
    private static final String WEBDRIVER_IE_DRIVER = "webdriver.ie.driver";
    private static final String WEBDRIVER_CHROME_DRIVER = "webdriver.chrome.driver";

    private static final String TESTCASE_SUITE = "TestCaseSuite";
    private static final String TESTCASESUITE_SHEET = "Suite";
    private static final String TESTCASEDATA_SHEET = "TestCases";
    private static final String TESTCASESUITE_SHEET_COLUMN1 = "TestSuiteName";
    private static final String TESTCASEDATA_TESTCASES_COLUMN1 = "TestCaseName";
    private static final String QC_TESTDATA = "TestCaseData_SI_Env";
    private static final String RUNMODE_COLUMN = "Runmode";
    private static final String RUNMODE_COLUMN_YES = "Y";

    private String testGroupName;
    private Properties config;
    private WebDriver driver;
    private ExtentReports extentReports;
    private ExtentTest parentExtentTest;
    private ExtentTest extentTest;

    protected BaseTest(String testGroupName) {
        this.testGroupName = testGroupName;
        this.config = PropertiesFactory.getInstance().getProperties();
        final String extentLocation = getProperty("ReportLocation") + "_" + new SimpleDateFormat("MM-dd-yyyy-HHmmss").format(new Date()) + ".html";

        extentReports = new ExtentReports("./Reports/Ex-Reports/" + extentLocation, true, DisplayOrder.OLDEST_FIRST);

        extentReports.loadConfig(ClassLoader.getSystemResource("extent-config.xml"));
        extentReports.addSystemInfo("User", "Gallagher Bassett Services Pty Ltd.");
    }

    @BeforeClass
    public void setParent(){
        parentExtentTest = extentReports.startTest(testGroupName);
    }

    @BeforeTest
    public void setUp() {
        try {
            final String webDriverProperty;
            final String browserDriver;

            switch (getProperty(BROWSER)) {
                case BROWSER_MOZILLA:
                    webDriverProperty = "Browser_" + getProperty(OPERATING_SYSTEM) + "_" + BROWSER_MOZILLA + "_Driver";
                    browserDriver = getProperty(webDriverProperty);

                    if (StringUtils.isNotBlank(browserDriver)) {
                        System.setProperty(WEBDRIVER_GECKO_DRIVER, ClassLoader.getSystemResource(browserDriver).getFile());
                    } else {
                        LOG.error("No property exists: " + webDriverProperty);
                        throw new IllegalArgumentException("No property exists: " + webDriverProperty);
                    }

                    final DesiredCapabilities capabilities = DesiredCapabilities.firefox();
                    capabilities.setBrowserName("firefox");
                    capabilities.setVersion("your firefox version");
                    capabilities.setPlatform(Platform.WINDOWS);
                    capabilities.setCapability("marionette", false);
                    driver = new FirefoxDriver(capabilities);
                    break;
                case BROWSER_IE:
                    webDriverProperty = "Browser_" + getProperty(OPERATING_SYSTEM) + "_" + BROWSER_IE + "_Driver";
                    browserDriver = getProperty(webDriverProperty);

                    if (StringUtils.isNotBlank(browserDriver)) {
                        System.setProperty(WEBDRIVER_IE_DRIVER, ClassLoader.getSystemResource(browserDriver).getFile());
                    } else {
                        LOG.error("No property exists: " + webDriverProperty);
                        throw new IllegalArgumentException("No property exists: " + webDriverProperty);
                    }

                    driver = new InternetExplorerDriver();
                    break;
                case BROWSER_CHROME:
                    webDriverProperty = "Browser_" + getProperty(OPERATING_SYSTEM) + "_" + BROWSER_CHROME + "_Driver";
                    browserDriver = getProperty(webDriverProperty);

                    if (StringUtils.isNotBlank(browserDriver)) {
                        System.setProperty(WEBDRIVER_CHROME_DRIVER, browserDriver);
                    } else {
                        LOG.error("No property exists: " + webDriverProperty);
                        throw new IllegalArgumentException("No property exists: " + webDriverProperty);
                    }

                    final ChromeOptions options = new ChromeOptions();
                    options.setExperimentalOption("useAutomationExtension", false);
                    options.addArguments("--headless");
                    driver = new ChromeDriver(options);
                    break;
                default:
                    break;
            }

            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Long.parseLong(getProperty("Default_WaitTime")), TimeUnit.SECONDS);
            LOG.info("Launching " + getProperty(BROWSER) + " browser.");
        } catch (Exception e) {
            throw new AssertionError("***EXCEPTION OCCURRED WHILE LAUNCHING THE BROWSER***", e);
        }
    }

    @AfterTest
    public void tearDown() {
        driver.quit();
    }

    protected String getProperty(final String key) {
        return config.getProperty(key);
    }

    protected WebDriver getDriver() {
        return driver;
    }

    protected ExtentTest getExtentTest() {
        return extentTest;
    }

    protected void start(final TestCase testCase) {
        extentTest = extentReports.startTest(testCase.getDesc());
        parentExtentTest.appendChild(extentTest);

        validateRunmodes(testCase);
    }

    protected void end() {
        try{
            extentReports.endTest(extentTest);
            extentReports.flush();
        } catch (Exception e){
            throw new AssertionError("***EXCEPTION OCCURRED WHILE FLUSHING EXTENT REPORTS...***", e);
        }
    }

    protected void testLog(final LogStatus logStatus, final String details) {
        getExtentTest().log(logStatus, details);
    }

    protected void testLog(final LogStatus logStatus, final String stepName, final String details) {
        getExtentTest().log(logStatus, stepName, details);
    }

    protected void pass(final TestCase testCase, final String stepName) {
        getExtentTest().log(LogStatus.PASS, stepName, capturePassScreenshot(testCase).getName());
    }

    protected void skip(final TestCase testCase) {
        getExtentTest().log(LogStatus.SKIP, testCase.getName());
    }

    protected void fail(final TestCase testCase, final Exception exception) {
        getExtentTest().log(LogStatus.FAIL, testCase.getName(), captureFailScreenshot(testCase).getName());
        LOG.error("Test " + testCase.getName() + "failed.", exception);
        Assert.fail("EXCEPTION CAUSED BY..." + exception.getMessage());
    }

    protected void waitForPageToLoad(final long timeOut) {
        try {
            long startTime = System.currentTimeMillis();
            long waitTime = startTime;
            long endTime = startTime + timeOut;

            while (System.currentTimeMillis() < endTime) {
                if (String.valueOf(((JavascriptExecutor) driver).executeScript("return document.readyState")).equals("complete")) {
                    waitTime = System.currentTimeMillis();
                    break;
                }
            }

            final DateFormat timeOnlyDateFormat = new SimpleDateFormat("hh:mm:ss");
            final Date start = timeOnlyDateFormat.parse(timeOnlyDateFormat.format(new Date(startTime)));
            final Date end = timeOnlyDateFormat.parse(timeOnlyDateFormat.format(new Date(waitTime)));

            LOG.info("PAGE LOAD TIME is : [" + (end.getTime() - start.getTime()) / 1000 + "] seconds");
        } catch (Exception e) {
            throw new AssertionError("***EXCEPTION OCCURRED WHILE PAGE IS LOADING...***", e);
        }
    }

    private void validateRunmodes(final TestCase testCase) {
        final boolean isSuiteRunmode = isSuiteExecutable(QC_TESTDATA, new XlsReader("test_case/" + TESTCASE_SUITE + ".xlsx"));

        if (isSuiteRunmode) {
            //Test Case Runmode
            final boolean isTestRunmode = isTestCaseExecutable(testCase.getName(), new XlsReader("test_case/" + QC_TESTDATA + ".xlsx"));

            if (isTestRunmode && testCase.getData().get(RUNMODE_COLUMN).equalsIgnoreCase(RUNMODE_COLUMN_YES)) {
                return;
            }
        }

        throw new SkipException("Test " + testCase.getName() + "has been SKIPPED.");
    }

    private boolean isSuiteExecutable(String suiteName, XlsReader xls) {
        final int rows = xls.getRowCount(TESTCASESUITE_SHEET);

        for (int rNum = 2; rNum <= rows; rNum++) {
            final String data = xls.getCellData(TESTCASESUITE_SHEET, TESTCASESUITE_SHEET_COLUMN1, rNum);

            if (data.equalsIgnoreCase(suiteName)) {
                final String runmode = xls.getCellData(TESTCASESUITE_SHEET, RUNMODE_COLUMN, rNum);
                LOG.info("RUN MODE SET TO " + runmode + " at " + suiteName + " file");

                if (runmode.equalsIgnoreCase(RUNMODE_COLUMN_YES)) {
                    return true;
                }
            }
        }

        return false;
    }

    /* Method Name : isTestCaseExecutable()
     * Description : Used to test TESTs are runnable or not
     */
    private boolean isTestCaseExecutable(final String testCase, final XlsReader xls) {
        final int rows = xls.getRowCount(TESTCASEDATA_SHEET);

        for (int rNum = 2; rNum <= rows; rNum++) {
            final String testNameXls = xls.getCellData(TESTCASEDATA_SHEET, TESTCASEDATA_TESTCASES_COLUMN1, rNum);

            if (testNameXls.equalsIgnoreCase(testCase)) {
                final String runmode = xls.getCellData(TESTCASEDATA_SHEET, RUNMODE_COLUMN, rNum);

                LOG.info("RUN MODE SET TO " + runmode + " at " + testCase + " file");

                if (runmode.equalsIgnoreCase(RUNMODE_COLUMN_YES)) {
                    return true;
                }
            }
        }

        return false;
    }

    private File capturePassScreenshot(final TestCase testCase) {
        try {
            if (Boolean.parseBoolean(getProperty("capturePassScreenshot"))) {
                final File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                final File destFile = new File(config.getProperty("passSnapshotPath")
                                                    + testCase.getName()
                                                    + "_"
                                                    + new SimpleDateFormat("yyyyMMddHHmm").format(new Date())
                                                    + "_PASSED.png");
                FileUtils.copyFile(scrFile, destFile);

                return destFile;
            } else {
                LOG.info("TAKING SCREENSHOT FOR PASS IS NOT ENABLED");
                return null;
            }
        } catch (Exception e) {
            throw new AssertionError();
        }
    }

    private File captureFailScreenshot(final TestCase testCase) {
        try {
            if (Boolean.parseBoolean(getProperty("captureFailScreenshot"))) {
                final File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                final File destFile = new File(config.getProperty("failSnapshotPath")
                                                    + testCase.getName()
                                                    + "_"
                                                    + new SimpleDateFormat("yyyyMMddHHmm").format(new Date())
                                                    + "_FAILED.png");
                FileUtils.copyFile(scrFile, destFile);

                return destFile;
            } else {
                LOG.info("TAKING SCREENSHOT FOR FAIL IS NOT ENABLED");

                return null;
            }
        } catch (Exception e) {
            throw new AssertionError();
        }
    }
}
