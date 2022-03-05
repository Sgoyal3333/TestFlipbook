package config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Properties;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import reusablecomponents.TechnicalComponents;
import reusablecomponents.Utilities;

public class TestSetup {
	public static ExtentReports report, log;
	public static ExtentTest logger, loggerForLogs;
	public static WebDriver driver, secondaryDriver, testmailinatorDriver;
	String testName, browser, reportName, excelReport;
	public static int testCasePassed = 0, testCaseFailed = 0, testCaseExecuted = 0, testCaseSkipped = 0;
	public static WebDriverWait wait;
	public static JavascriptExecutor js;
	public static long timeOut, driverWait, emailTimeOut;
	public static HashMap<String, String> testCasesToBeExecuted = new HashMap<String, String>();
	public static HashMap<String, String> testCaseBrowser = new HashMap<String, String>();
	public static HashMap<String, String> testCaseCategory = new HashMap<String, String>();
	public static HashMap<Integer, String> testCaseResult = new HashMap<Integer, String>(),

			testCaseName = new HashMap<Integer, String>(), testCaseId = new HashMap<Integer, String>(),
			testCaseResults = new HashMap<Integer, String>();
	public static String[][] testCases;
	public static String strFileName = "config.properties";
	public static Properties props = new Properties();
	public boolean toBeTested = false;
	public boolean localSiteTest = false;
	public static boolean isMobile = false;

	public static String runMode = "local";
	public static String testDriverLocation, testDataLocation;
	public static int testCaseCount;
	
	public static String downloadChromeFolderPath = System.getProperty("user.dir") + "\\DownloadedFile";
	public static String globalTestData = System.getProperty("user.dir") + "\\DownloadedFile";

	@BeforeSuite
	public void beforeSuite() throws FrameworkException, Throwable {

		reportName = "LocalRun_";

		testDataLocation = Utilities.getProperty("TEST_DATA_LOCATION");
		testDriverLocation = Utilities.getProperty("TEST_DRIVER_LOCATION");

		reportName = Utilities.dateFormat("T+0", "MM_dd_yyyy") + "/" + reportName
				+ Utilities.dateFormat("T+0", "MM_dd_yyyy") + "_"
				+ Utilities.getTimeStamp("local").replace("-", "").replace(":", ""); 

		report = new ExtentReports("Reports/" + reportName + ".html");
		log = new ExtentReports("Logs/" + reportName + ".html");
		excelReport = "Reports/" + reportName + ".xlsx";

		Utilities.setProperty("SCREENSHOTS_LOCATION_FOR_RUN", System.getProperty("user.dir") + "/Reports/"
				+ Utilities.dateFormat("T+0", "MM_dd_yyyy") + "/Screenshots/");

		testCaseCount = 0;

	}


	@BeforeMethod
	public void beforeMethod(Method method) throws Throwable {

		testCaseCount++;
		driver = null;
		toBeTested = true;

		testName = method.getName();
		if (!testName.contains("testValidateURLRedirection")) {
			logger = report.startTest(testName, "");
			loggerForLogs = log.startTest(testName);
		} else {
			localSiteTest = true;
		}
		emailTimeOut = Long.parseLong(Utilities.getProperty("EMAIL_TIME_OUT"));
		testCaseName.put(testCaseCount, testName);
		try {
			System.out.println("================== Execution Started for ===================>> " + testName);
			String testCategory = Utilities.getProperty("TEST_CATEGORY").trim(),
					browser = Utilities.getProperty("EXECUTION_BROWSER").trim();

			if (!localSiteTest) {
				driver = OpenBrowser(testCategory, browser);
			}

		} catch (NullPointerException e) {
			logger.log(LogStatus.SKIP,
					testName + " not configured. Please check data file and function name for consistency.");
		}
	}

	@AfterMethod
	public void afterMethod(ITestResult result) throws InterruptedException {
		report.endTest(logger);
		report.flush();
		log.endTest(loggerForLogs);
		log.flush();

		String testCaseStatus = null;
		switch (result.getStatus()) {
		case ITestResult.SUCCESS:
			testCasePassed++;
			testCaseStatus = "PASS";
			break;
		case ITestResult.FAILURE:
			testCaseFailed++;
			testCaseStatus = "FAIL";
			break;

		case ITestResult.SKIP:
			testCaseSkipped++;
			testCaseStatus = "SKIP";
			break;
		}
		testCaseResult.put(testCaseCount, testCaseStatus);
		testCaseExecuted = testCasePassed + testCaseFailed;
		if (toBeTested) {
			if (Utilities.getProperty("TEST_CATEGORY").toLowerCase().contains("web")) {
				driver.quit();
			}
		}
	}


	@AfterSuite
	public void afterSuite() throws FrameworkException, Throwable {
		try {
		//	writeLogs();
		} finally {
			System.out.println("Test Cases Executed: " + testCaseExecuted);
			System.out.println("Test Cases Passed: " + testCasePassed);
			System.out.println("Test Cases Failed: " + testCaseFailed);
			System.out.println("Test Cases Skipped: " + testCaseSkipped);
		}

	}

	public static void loggerLogReportFail(String failDesc) {
		try {
			System.out.println(" FAIL :: " + failDesc);
			logger.log(LogStatus.FAIL, failDesc);
		} catch (Exception e) {
			System.out.println(" Exception while writing the log " + e.getMessage());
		}
	}

	public static void loggerLogReportPass(String passDesc) {
		try {
			System.out.println(" Pass :: " + passDesc);
			logger.log(LogStatus.PASS, passDesc);
		} catch (Exception e) {
			System.out.println(" Exception while writing the log " + e.getMessage());
		}
	}

	public static void loggerLogReportSkip(String skipDesc) {
		try {
			System.out.println(" Skip :: " + skipDesc);
			logger.log(LogStatus.SKIP, skipDesc);
		} catch (Exception e) {
			System.out.println(" Exception while writing the log " + e.getMessage());
		}
	}

	public static void loggerLogReportInfo(String infoDesc) {
		try {
			System.out.println(" Info :: " + infoDesc);
			logger.log(LogStatus.INFO, infoDesc);
		} catch (Exception e) {
			System.out.println(" Exception while writing the log " + e.getMessage());
		}
	}

	public static void loggerLogReportWarning(String warningDesc) {
		try {
			System.out.println(" Warning !!! " + warningDesc);
			logger.log(LogStatus.WARNING, warningDesc);
		} catch (Exception e) {
			System.out.println(" Exception while writing the log " + e.getMessage());
		}
	}

	public static WebDriver OpenBrowser(String testCategory, String browser) {
		WebDriver localDriver = null;
		if (testCategory.toLowerCase().equalsIgnoreCase("web")) {

			switch (browser.toLowerCase()) {
			case "chrome":
				String platform = System.getProperty("os.name");
					ChromeOptions options = new ChromeOptions();
					System.setProperty("webdriver.chrome.driver", "Drivers/" + "chromedriver.exe");
					HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
					chromePrefs.put("profile.default_content_settings.popups", 0);
					chromePrefs.put("download.default_directory", downloadChromeFolderPath);
					chromePrefs.put("download.prompt_for_download", false);
					chromePrefs.put("deviceName", "Google Nexus 5");
					options.setExperimentalOption("prefs", chromePrefs);
					options.addArguments("--test-type");
					options.addArguments("--disable-extensions"); // to disable browser extension popup
					System.setProperty(ChromeDriverService.CHROME_DRIVER_SILENT_OUTPUT_PROPERTY, "true");
					DesiredCapabilities cap = DesiredCapabilities.chrome();
					cap.setCapability(ChromeOptions.CAPABILITY, options);
					cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
					cap.setCapability(ChromeOptions.CAPABILITY, options);
					cap.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
					localDriver = new ChromeDriver(cap);
				break;
			default:
				throw new FrameworkException("Browser not configured.");
			}
			if (!TestSetup.isMobile) {
				localDriver.manage().window().maximize();
			}

		}
		if (localDriver != null) {
			timeOut = Long.parseLong(Utilities.getProperty("TIME_OUT_MEDIUM"));
			driverWait = Long.parseLong(Utilities.getProperty("IMPLICIT_WAIT"));
			wait = new WebDriverWait(localDriver, timeOut);
			wait = new WebDriverWait(localDriver, timeOut);

			js = ((JavascriptExecutor) localDriver);
			try {
				localDriver.manage().deleteAllCookies();
			} catch (Exception e) {
				localDriver.navigate().refresh();
			}

		}

		return localDriver;
	}

	public void switchToBrowser(WebDriver driver) {
		if (driver != null) {

			wait = new WebDriverWait(driver, timeOut);
			wait = new WebDriverWait(driver, timeOut);
			js = ((JavascriptExecutor) driver);
		}
	}

	public static void closeBrowser(WebDriver driver) {
		if (driver != null) {
			driver.quit();

		}
	}

}