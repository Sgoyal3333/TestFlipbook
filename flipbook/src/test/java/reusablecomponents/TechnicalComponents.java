package reusablecomponents;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.relevantcodes.extentreports.LogStatus;
import config.FrameworkException;
import config.TestSetup;


public class TechnicalComponents extends TestSetup {
	static InputStream input;
	XSSFSheet ExcelWSheet;
	XSSFWorkbook ExcelWBook;
	public String path;
	public FileInputStream fis = null;
	public FileOutputStream fileOut = null;


	public static void navigatetoUrl(String url) {
		try {
			if (url != "") {
				driver.get(url);
				Thread.sleep(2000);
				reportLogger(LogStatus.PASS, "Navigated to URL : " + url);
				reportLoggerForLog(LogStatus.INFO, "Navigated to URL : " + url);

			} else {
				throw new FrameworkException("Please enter the URL in config.");
			}

		} catch (Exception e) {
			throw new FrameworkException(
					"Unable to navigate to URL--- " + url + "---" + e.getClass() + "---" + e.getMessage());
		}
	}

	public static void type(WebElement element, Keys text, String desc) {
		try {
			if (element.isDisplayed()) {
				if (element.isEnabled()) {
					element.sendKeys(text);
				} else {
					throw new FrameworkException("Field " + desc + " is disabled.");
				}
			} else {
				throw new FrameworkException("Field " + desc + " is not displayed.");
			}
			driverWait = Long.parseLong(Utilities.getProperty("IMPLICIT_WAIT"));
			reportLoggerForLog(LogStatus.INFO, "Typed '" + text + "' to " + desc);
		} catch (NoSuchElementException e) {
			if (driverWait > 0) {
				driverWait--;
				waitTill(1);
				type(element, text, desc);
			} else {
				throw new FrameworkException("Field " + desc + " not found.");
			}
		}
	}



	public static String getWebPageText() {
		try {
			WebElement body = driver.findElement(By.tagName("body"));
			String bodyText = body.getText();
			return bodyText;
		} catch (Exception e) {
			throw new FrameworkException(
					"Unknown exception occured while typing on: " + e.getClass() + "---" + e.getMessage());
		}
	}



	public static void type(WebElement element, String text, String desc) {
		try {
			if (element.isDisplayed()) {
				if (element.isEnabled()) {
					try {
						element.clear();
					} catch (Exception e) {
					}
					try {
						clear(element, desc);
					} catch (Exception e) {
					}
					waitTill(4);
					element.sendKeys(text);
					waitTill(1);
				} else {
					if (driverWait > 0) {
						driverWait--;
						waitTill(1);
						type(element, text, desc);
					} else {
						throw new FrameworkException("Field " + desc + " is disabled.");
					}
				}
			} else {
				if (driverWait > 0) {
					driverWait--;
					waitTill(1);
					type(element, text, desc);
				} else {
					throw new FrameworkException("Field " + desc + " is not displayed.");
				}
			}
			driverWait = Long.parseLong(Utilities.getProperty("IMPLICIT_WAIT"));
			reportLoggerForLog(LogStatus.INFO, "Typed '" + text + "' to " + desc);
		} catch (NoSuchElementException e) {
			if (driverWait > 0) {
				driverWait--;
				waitTill(1);
				type(element, text, desc);
			} else {
				throw new FrameworkException("Field " + desc + " not found.");
			}
		}
	}


	public static void click(WebElement element, String desc, boolean firstClick) {
		try {
			if (element.isDisplayed()) {
				highlightElement(element);
				if (element.isEnabled()) {
					js.executeScript("arguments[0].click();", element);
				} else {
					if (firstClick) {
						scroll(element);
						click(element, desc, false);
					} else {
						throw new FrameworkException("Field: " + desc + " is disabled.");
					}
				}
			} else {
				if (driverWait > 0) {
					driverWait--;
					waitTill(1);
					if (!firstClick) {
						highlightElement(element);
						js.executeScript("arguments[0].click();", element);
						reportLoggerForLog(LogStatus.INFO, desc + " clicked");
						driverWait = Long.parseLong(Utilities.getProperty("IMPLICIT_WAIT"));
						return;
					}
					scroll(element);
					click(element, desc, false);
				} else {
					throw new FrameworkException("Field: " + desc + " is not displayed on UI.");
				}
			}
			driverWait = Long.parseLong(Utilities.getProperty("IMPLICIT_WAIT"));
			reportLoggerForLog(LogStatus.INFO, desc + " clicked");
		} catch (NoSuchElementException e) {
			if (driverWait > 0) {
				driverWait--;
				waitTill(1);
				click(element, desc);
			} else {
				throw new FrameworkException("Field " + desc + " not found.");
			}
		}
	}

	

	
	public static void reportLoggerForLog(LogStatus logStatus, String desc) {
		try {
			loggerForLogs.log(logStatus, " >> " + desc);
			System.out.println(logStatus.toString() + " >> " + desc);
		} catch (FrameworkException e) {
			throw new FrameworkException(e.getMessage());
		} catch (Exception e) {
			throw new FrameworkException("Unknown exception occured while hovering to: " + desc + "---" + e.getClass()
					+ "---" + e.getMessage());
		}
	}

	public static void reportLogger(LogStatus logStatus, String desc) {
		try {
			logger.log(logStatus, " >> " + desc);
			System.out.println(logStatus.toString() + " >> " + desc);
		} catch (FrameworkException e) {
			throw new FrameworkException(e.getMessage());
		} catch (Exception e) {
			throw new FrameworkException("Unknown exception occured while hovering to: " + desc + "---" + e.getClass()
					+ "---" + e.getMessage());
		}
	}


	public static void waitTill(WebElement element, String state) {
		try {
			switch (state.toLowerCase()) {
			case "visible":
				wait.until(ExpectedConditions.visibilityOf(element));
				break;
			case "enable":
				wait.until(ExpectedConditions.elementToBeClickable(element));
				break;
			case "invisible":
				element.isDisplayed();
				wait.until(ExpectedConditions.invisibilityOf(element));
				break;
			default:
				wait.until(ExpectedConditions.visibilityOf(element));
			}
			reportLoggerForLog(LogStatus.INFO, "Element " + element.toString() + " " + state);
		} catch (StaleElementReferenceException e) {
			if (timeOut > 0) {
				timeOut--;
				waitTill(element, state);
			} else {
				throw new FrameworkException(
						"Page refreshed while waiting for element : *  '" + element.toString() + "'");
			}
		}
		}


	
	public static void waitTill(WebElement element, String state, String text) {
		try {
			if (state != "text") {
				waitTill(element, state);
			} else {
				wait.until(ExpectedConditions.textToBePresentInElement(element, text));
			}
			reportLoggerForLog(LogStatus.INFO, "Element(with text) " + element.toString() + " " + state);
		} catch (NoSuchElementException e) {
			if (timeOut > 0) {
				timeOut--;
				waitTill(element, state, text);
			} else {
				throw new FrameworkException(
						"Element : " + element.toString() + " not found within defined time limit.");
			}

		} 
		}

	public static String screenshot(WebDriver driver) {
		try {
			String src_path = Utilities.getProperty("SCREENSHOTS_LOCATION_FOR_RUN");

			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

			FileUtils.copyFile(scrFile,
					new File(src_path + Utilities.getTimeStamp("local").replace("-", "").replace(":", "") + ".png"));

			return "Screenshots/" + Utilities.getTimeStamp("local").replace("-", "").replace(":", "") + ".png";
		} catch (Exception e) {
			return "Not able to take screenshot.---" + e.getClass() + "---" + e.getMessage();
		}

	}

	public static void scroll(WebElement element) {
		try {
			js.executeScript("arguments[0].scrollIntoView(true);", element);
			js.executeScript("window.scrollBy(0,-250)", "");
			reportLoggerForLog(LogStatus.INFO, "Scrolled to element " + element.toString());
		} catch (NoSuchElementException e) {
			if (driverWait > 0) {
				driverWait--;
				waitTill(1);
				scroll(element);
			} else {
				throw new FrameworkException(element.toString() + " not found while scrolling to element.");
			}

		} 
	}


	public static void verifyElementState(WebElement element, String expectedState, String fieldDesc) {
		try {
			scroll(element);
			if (expectedState.equalsIgnoreCase("enable")) {
				if (element.isEnabled()) {
					reportLogger(LogStatus.PASS,
							"Field '" + fieldDesc + "' verified successfully and is " + expectedState + ".");
				} else {
					highlightElement(element);
					throw new FrameworkException("Field '" + fieldDesc
							+ "' is not present in desired state. Expected State is: " + expectedState);
				}
			} else {
				if (element.isEnabled()) {
					highlightElement(element);
					throw new FrameworkException("Field '" + fieldDesc
							+ "' is not present in desired state. Expected State is: " + expectedState);
				} else {
					reportLogger(LogStatus.PASS,
							"Field '" + fieldDesc + "' verified successfully and is " + expectedState + ".");
				}
			}
			driverWait = Long.parseLong(Utilities.getProperty("IMPLICIT_WAIT"));
		} catch (FrameworkException e) {
			if (driverWait > 0) {
				driverWait--;
				waitTill(1);
				verifyElementState(element, expectedState, fieldDesc);

			} else {
				throw new FrameworkException(e.getMessage());
			}
		} catch (Exception e) {
			throw new FrameworkException("Unknown exception occured while verifying states for: " + fieldDesc + "---"
					+ e.getClass() + "---" + e.getMessage());
		}
	}

	
	public static void switchToDefaultContent() {
		driver.switchTo().defaultContent();
	}

	public static void closeCurrentwindow() {
		driver.close();
	}


	public static void waitTill(int time) {
		try {
			Thread.sleep(time * 1000);
		} catch (InterruptedException e) {

		}
	}

	public static boolean cleanFolder(String FolderPath) {
		boolean result = false;
		try {
			FileUtils.cleanDirectory(new File(FolderPath));
			waitTill(3);
		} catch (Exception e) {
			System.out.println("Error while creating the specific folder. Location " + FolderPath + ". Error message "
					+ e.getMessage());
		}
		return result;
	}
	
	public static void clear(WebElement element, String desc) {
		try {
			js.executeScript("arguments[0].setAttribute('text', '')", element);
			js.executeScript("arguments[0].setAttribute('value', '')", element);
			type(element, Keys.END, desc);
			int length = Math.max(TechnicalComponents.getAttribute(element, "text", desc).length(),
					TechnicalComponents.getAttribute(element, "value", desc).length());
			do {
				type(element, Keys.BACK_SPACE, desc);
				type(element, Keys.DELETE, desc);
				length--;
			} while (length > 0);
			driverWait = Long.parseLong(Utilities.getProperty("IMPLICIT_WAIT"));
			reportLoggerForLog(LogStatus.INFO, desc + " cleared.");
		} catch (NoSuchElementException e) {
			if (driverWait > 0) {
				driverWait--;
				waitTill(1);
				clear(element, desc);
			} else {
				throw new FrameworkException("Element " + desc + " not found while clearing element.");
			}
		} catch (Exception e) {
			throw new FrameworkException("Unknown exception encountered while clearing " + desc + "---" + e.getClass()
					+ "---" + e.getMessage());
		}
	}
	
	public static String getAttribute(WebElement element, String attribute, String desc) {
		try {
			String value;
			if (attribute.equals("text")) {
				if (element.getText() != null) {
					value = element.getText();
				} else {
					value = "";
				}
			} else {
				if (element.getAttribute(attribute) != null) {
					value = element.getAttribute(attribute);
				} else {
					value = "";
				}
			}
			driverWait = Long.parseLong(Utilities.getProperty("IMPLICIT_WAIT"));
			reportLoggerForLog(LogStatus.INFO, "Attribute " + attribute + " for " + desc + " returned " + value);
			return value;
		} catch (NoSuchElementException e) {
			if (driverWait > 0) {
				driverWait--;
				waitTill(1);
				return getAttribute(element, attribute, desc);
			} else {
				throw new FrameworkException("Field " + desc + " not found.");
			}
		} catch (Exception e) {
			throw new FrameworkException("Unknown exception occured while retrieving Attribute: "
					+ attribute.toUpperCase() + " value for " + desc + "---" + e.getClass() + "---" + e.getMessage());
		}
	}


	public static void setParametersPerTestCase(String testcase_Id, String testDesc, String complexity) {

		StackTraceElement ste = Thread.currentThread().getStackTrace()[2];

		logger.getTest()
				.setName("TestCase # " + testCaseCount + "---" + ste.getClassName() + "--------" + ste.getMethodName()
						+ "========" + logger.getTest().getName() + "--------" + testcase_Id + "--------" + testDesc);
		System.out.println("TestCase # " + testCaseCount + "---" + ste.getClassName() + "--------" + ste.getMethodName()
				+ "========" + logger.getTest().getName() + "--------" + testcase_Id + "--------" + testDesc
				+ "  ======== Started ======== >>>>>");
		loggerForLogs.getTest()
				.setName("TestCase # " + testCaseCount + "---" + ste.getClassName() + "--------" + ste.getMethodName()
						+ "========" + logger.getTest().getName() + "--------" + testcase_Id + "--------" + testDesc);

		testCaseName.put(testCaseCount, logger.getTest().getName());
		testCaseId.put(testCaseCount, testcase_Id);

		String browserVersion = "", browserName = "";
		try {
			Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
			if (driver instanceof ChromeDriver) {
				browserVersion = String.format("Browser Version: %s", cap.getCapability("version"));
			} else if (driver instanceof FirefoxDriver) {
				browserVersion = String.format("Browser Version: %s", cap.getVersion());
			} else if (driver instanceof EdgeDriver) {
				browserVersion = String.format("Browser Version: %s", cap.getVersion());
			} else if (driver instanceof InternetExplorerDriver) {
				browserVersion = String.format("Browser Version: %s", cap.getVersion());
			} else if (driver instanceof RemoteWebDriver) {
				browserVersion = "";
			}
			browserName = String.format("Browser Name: %s", cap.getCapability("browserName"));

			if (browserVersion.equals("") || browserVersion == null || browserVersion.equals("Browser Version: ")) {
				browserVersion = String.format("Browser Version: %s", cap.getCapability("browserVersion"));

			}
		} catch (Exception e) {

		}

		if (driver != null) {
			if (complexity.toLowerCase().equals("high")) {
				TestSetup.timeOut = Long.parseLong(Utilities.getProperty("TIME_OUT"));
			} else if (complexity.toLowerCase().equals("medium")) {
				TestSetup.timeOut = Long.parseLong(Utilities.getProperty("TIME_OUT_MEDIUM"));
			} else if (complexity.toLowerCase().equals("low")) {
				TestSetup.timeOut = Long.parseLong(Utilities.getProperty("TIME_OUT_LOW"));
			}

			wait = new WebDriverWait(driver, timeOut);
		}
		logger.setDescription("TestCase # " + testCaseCount + "----" + testcase_Id + ">>>" + testDesc + "---"
				+ complexity + "---" + timeOut + "---" + browserName + "---" + browserVersion);
		System.out.println("TestCase # " + testCaseCount + "----" + testcase_Id + ">>>" + testDesc + "---" + complexity
				+ "---" + timeOut + "---" + browserName + "---" + browserVersion);
		loggerForLogs.setDescription("TestCase # " + testCaseCount + "---" + testDesc + "---" + complexity + "---"
				+ timeOut + "---" + browserName + "---" + browserVersion);
	}

	public static void highlightElement(WebElement element) {
		js.executeScript("arguments[0].setAttribute('style', 'border: 4px solid red;');", element);
		js.executeScript("arguments[0].setAttribute('style','border: solid 4px white');", element);
		js.executeScript("arguments[0].setAttribute('style', 'border: 4px solid red;');", element);
		js.executeScript("arguments[0].setAttribute('style','border: solid 4px white');", element);
		js.executeScript("arguments[0].setAttribute('style', 'border: 4px solid red;');", element);
		js.executeScript("arguments[0].setAttribute('style','border: solid 4px white');", element);
		js.executeScript("arguments[0].setAttribute('style', 'border: 4px solid red;');", element);
		js.executeScript("arguments[0].setAttribute('style','border: solid 4px white');", element);
		js.executeScript("arguments[0].setAttribute('style','border: solid 0px white');", element);
	}


	public static void click(WebElement element, String desc) {
		click(element, desc, true);
		waitForPageToLoad();
	}


	public static void waitForPageToLoad() {
		try {
			String pageLoadStatus;
			do {
				JavascriptExecutor js = (JavascriptExecutor) driver;
				pageLoadStatus = (String) js.executeScript("return document.readyState");
				Thread.sleep(100);
				System.out.print(".");
			} while (!pageLoadStatus.equals("complete"));
			System.out.println("Page Loaded.");
		} catch (Exception e) {
			e.getMessage();
		}
	}
	
	

	
	public void setCellValue(String path, String sheetName, String data, int rowIndex, String columnValue) {
		int row = 0;
		int column = 0;

		try {
			fis = new FileInputStream(path);
			ExcelWBook = new XSSFWorkbook(fis);
			ExcelWSheet = ExcelWBook.getSheet(sheetName);

			row = rowIndex;
			column = getColumnIndex(columnValue);

			Row r = ExcelWSheet.getRow(row); // 10-1
			Cell c = r.getCell(column); // 4-1
			if (c == null) {
				c = r.createCell(column);
			}

			c.setCellValue(data);

			FileOutputStream out = new FileOutputStream(new File(path));
			ExcelWBook.write(out);
			out.close();
		} catch (Exception e) {
			throw new FrameworkException("Not able to set cell row value" + e.getMessage());
		}

	}

	private int getColumnIndex(String columnValue) {
		int columnIndex = 0;
		try {
			int totalCols = ExcelWSheet.getRow(1).getLastCellNum();
			for (int i = 0; i <= totalCols; i++) {
				String cellValue = getCellValue(0, i);
				if (cellValue.equals(columnValue)) {
					columnIndex = i;
				}
			}
		} catch (Exception e) {
			throw new FrameworkException("Not able to get cell column index" + e.getMessage());
		}
		return columnIndex;
	}

	public int getRowIndex(String rowValue) {
		int rowIndex = 0;
		try {
			int totalRows = ExcelWSheet.getLastRowNum();
			if (!rowValue.contains(";")) {
				for (int i = 1; i <= totalRows; i++) {
					String cellValue = getCellValue(i, 0);
					if (cellValue.equals(rowValue)) {
						rowIndex = i;
						break;
					}
				}
			} else {
				String[] parts = rowValue.split(";");
				outerloop: for (int i = 1; i <= totalRows; i++) {
					String cellValue = getCellValue(i, 0);
					if (cellValue.trim().equals(parts[0].trim())) {
						for (int j = i; j <= totalRows; j++) {
							String cellValue2 = getCellValue(j, 1);
							if (cellValue2.trim().equals(parts[1].trim())) {
								rowIndex = j;
								break outerloop;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			throw new FrameworkException("Not able to get cell row index" + e.getMessage());
		}
		return rowIndex;
	}

	public String getCellValue(int RowNum, int ColNum) throws Exception {

		try {
			DataFormatter df = new DataFormatter();
			Row row = ExcelWSheet.getRow(RowNum);
			Cell cell = row.getCell(ColNum);
			if (cell == null)
				return "";
			if (cell.getCellType() == Cell.CELL_TYPE_STRING)
				return cell.getStringCellValue();
			else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC || cell.getCellType() == Cell.CELL_TYPE_FORMULA) {

				String cellText = df.formatCellValue(row.getCell(ColNum));
				String formatstring = cell.getCellStyle().getDataFormatString();
				if (cellText.contains("AM") || cellText.contains("PM") || cellText.contains("/")) {
					return cellText;
				} else if (formatstring.equals("[hh]:mm") || formatstring.equals("[h]:mm")) {
					if (cellText.substring(0, 1).equals("-")) {
						double timeInMinutes = Double.parseDouble(cellText) * 24 * (-1) * 60;
						int time = (int) Math.round(timeInMinutes);
						String timeInHHMM = convertMinsToHours(time);
						return "-" + timeInHHMM; 
					} else {
						if (!(Integer.parseInt(cellText.split(":")[0]) > 24)) {
							Date time = cell.getDateCellValue();
							Date nearestMinute = DateUtils.round(time, Calendar.MINUTE);
							cellText = convertDateToString(nearestMinute, "HH:mm");
							return cellText;
						} else {
							return cellText;
						}
					}
				}
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					String[] split = cellText.split(":", 2);
					int mins = Integer.valueOf(split[0]) * 60 + Integer.valueOf(split[1]);
					cellText = String.format("%02d", (int) (mins / 60)) + ":" + String.format("%02d", (mins % 60));
				}
				return cellText;
			} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK)
				return "";
			else
				return String.valueOf(cell.getBooleanCellValue());

		} catch (Exception e) {
			System.out.println("Read Excel inside" + e.getMessage());
			throw (e);
		}
	}

	public static String convertMinsToHours(int timeInMins) {

		if (timeInMins < 0) {
			if (((int) (timeInMins / 60) <= 0) & ((int) (timeInMins / 60) >= -9)) {
				return "-" + (String.format("%02d", -(int) (timeInMins / 60))) + ":"
						+ (String.format("%02d", -(timeInMins % 60)));
			} else {
				return (String.format("%02d", (int) (timeInMins / 60))) + ":"
						+ (String.format("%02d", -(timeInMins % 60)));
			}
		} else {
			return (String.format("%02d", (int) (timeInMins / 60))) + ":" + (String.format("%02d", (timeInMins % 60)));
		}

	}

	public static String convertDateToString(Date date, String format) {

		DateFormat df = new SimpleDateFormat(format);
		String reportDate = df.format(date);
		return reportDate;
	}

	
	public static void closeWindow(String windowHandle) {
		driver.switchTo().window(windowHandle);
		driver.close();
	}

	public static void clearField(WebElement element) {
		element.clear();
	}


	public static void scrollBottomJS(String desc) {
		try {
			((JavascriptExecutor) driver).executeScript("window.scrollTo(0,document.body.scrollHeight)");
			waitTill(2);
		} catch (Exception e) {
			throw new FrameworkException(
					"Unknown exception occured while typing on: " + desc + e.getClass() + "---" + e.getMessage());
		}
	}
	
	public static void KillDrivers() {
		try {
			String batchFilePath = System.getProperty("user.dir") + "\\utilities\\KillDriver.bat";
			executeBatchFile(batchFilePath);
			waitTill(1);
			InetAddress ip = InetAddress.getLocalHost();

				String batchBrowserFilePath = System.getProperty("user.dir") + "\\utilities\\KillBrowser.bat";
				executeBatchFile(batchBrowserFilePath);
			waitTill(3);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void executeBatchFile(String filePath) {
		System.out.println("In Function >>>  executeBatchFile");
		String BatchFilePath = filePath; // Runtime
		Runtime.getRuntime();
		try {
			String[] command = { "cmd.exe", "/C", "Start", BatchFilePath };
			Process p = Runtime.getRuntime().exec(command);
			waitTill(3);
		} catch (IOException e) {
			System.out.println(e);
		} catch (Exception e) {
			System.out.println(" Exception >> " + e.getMessage());
		}

	}
	
	
}