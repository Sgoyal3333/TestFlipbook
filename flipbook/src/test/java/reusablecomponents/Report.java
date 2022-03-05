package reusablecomponents;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.search.FlagTerm;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
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
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.relevantcodes.extentreports.LogStatus;

import config.FrameworkException;

import config.TestSetup;
import data.GlobalTestData;

/**
 * Class for technical components, this class is utilized to perform actions on
 * application.
 * 
 * @author jkhanuja
 */
public class Report extends TestSetup {

	public static void passStep(String stepDesc) {
		try {
			// create a temp file
			logger.log(LogStatus.PASS, stepDesc);
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, e.getMessage());
		}
	}

	
	public static void passStep(String stepDesc, WebDriver driver ) {
		try {
			logger.log(LogStatus.PASS, stepDesc,
					logger.addScreenCapture(TechnicalComponents.screenshot(driver)));
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, e.getMessage());
		}
	}

	
	public static void failStep(String stepDesc) {
		try {
			// create a temp file
			logger.log(LogStatus.FAIL, stepDesc);
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, e.getMessage());
		}
	}

	
	public static void failStep(String stepDesc, WebDriver driver ) {
		try {
			logger.log(LogStatus.FAIL, stepDesc,
					logger.addScreenCapture(TechnicalComponents.screenshot(driver)));
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, e.getMessage());
		}
	}
	
	
	public static void infoStep(String stepDesc) {
		try {
			// create a temp file
			logger.log(LogStatus.INFO, stepDesc);
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, e.getMessage());
		}
	}

	
	public static void infoStep(String stepDesc, WebDriver driver ) {
		try {
			logger.log(LogStatus.INFO, stepDesc,
					logger.addScreenCapture(TechnicalComponents.screenshot(driver)));
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, e.getMessage());
		}
	}
	
	public static void skiptep(String stepDesc) {
		try {
			// create a temp file
			logger.log(LogStatus.SKIP, stepDesc);
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, e.getMessage());
		}
	}

	
	public static void skipStep(String stepDesc, WebDriver driver ) {
		try {
			logger.log(LogStatus.SKIP, stepDesc,
					logger.addScreenCapture(TechnicalComponents.screenshot(driver)));
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, e.getMessage());
		}
	}
	
	
	public static void warningStep(String stepDesc) {
		try {
			// create a temp file
			logger.log(LogStatus.WARNING, stepDesc);
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, e.getMessage());
		}
	}

	
	public static void warningStep(String stepDesc, WebDriver driver ) {
		try {
			logger.log(LogStatus.WARNING, stepDesc,
					logger.addScreenCapture(TechnicalComponents.screenshot(driver)));
		} catch (FrameworkException e) {
			throw new FrameworkException(e.getMessage());
		}
	}
	
	
	public static void addScreenCapture(WebDriver driver ) {
		try {
			logger.addScreenCapture(TechnicalComponents.screenshot(driver));
		} catch (FrameworkException e) {
			throw new FrameworkException(e.getMessage());
		}
	}
	


}