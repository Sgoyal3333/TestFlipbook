package reusablecomponents;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import config.FrameworkException;

public class Utilities extends TechnicalComponents {

	static String strValue;
	static Properties props = new Properties();
	static String strFileName = "config.properties";
	static String strFileName1 = "Ecomm.properties";
	static String strFileName2 = "sauceLabs.properties";
	static String strFileName3 = "cyrus.properties";
	static String content[][] = null;
	static FileInputStream file = null;
	static XSSFWorkbook workbook = null;
	static XSSFSheet sheet = null;

	public static String getProperty(String strKey) {
		try {
			Thread.sleep(10);
			File f = new File(strFileName);
			if (f.exists()) {
				FileInputStream in = new FileInputStream(f);
				props.load(in);
				strValue = props.getProperty(strKey);
				in.close();
			} else
				throw new FrameworkException("Configuration File not found.");
		} catch (Exception e) {
			throw new FrameworkException("Unknown Error encountered while reading " + strKey
					+ " from configuration file. ---" + e.getClass() + "---" + e.getMessage());
		}
		if (strValue != null) {
			return strValue;
		} else {
			throw new FrameworkException(
					"Value '" + strKey + "' not configured in config file. Contact automation team");
		}

	}


	public static String getTimeStamp(String timeZone) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
		if (timeZone.toLowerCase().equals("utc")) {
			dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		} else {
			dateFormat.setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
		}

		return dateFormat.format(new Date());
	}

	public static void setProperty(String strKey, String strValue) {
		try {
			File f = new File(strFileName);
			if (f.exists()) {
				FileInputStream in = new FileInputStream(f);
				props.load(in);
				props.setProperty(strKey, strValue);
				props.store(new FileOutputStream(strFileName), null);

				in.close();
			} else {
				throw new FrameworkException("Configuration File not found.");
			}
		} catch (Exception e) {
			throw new FrameworkException("Unknown Error encountered while writing " + strKey
					+ " from configuration file. ---" + e.getClass() + "---" + e.getMessage());
		}
	}

	public static String[][] Read_Excel(String fileName, String sheetName) {
		try {
			file = new FileInputStream(new File(fileName));

			workbook = new XSSFWorkbook(file);
			sheet = workbook.getSheet(sheetName);
			int rowNum = sheet.getLastRowNum();
			workbook.close();
			content = Read_Excel(fileName, sheetName, rowNum);
			return content;
		} catch (FileNotFoundException e) {
			throw new FrameworkException("File " + fileName + " not found for reading.");
		} catch (IOException e) {
			throw new FrameworkException("Exception occured while reading " + fileName);
		} catch (Exception e) {
			throw new FrameworkException("Unknown Exception while reading " + fileName + "&" + sheetName + "---"
					+ e.getClass() + "---" + e.getMessage());
		}
	}

	public static String[][] Read_Excel(String fileName, String sheetName, int rowNum) {
		try {
			DataFormatter df = new DataFormatter();
			String content[][] = null;
			FileInputStream file = null;
			XSSFWorkbook workbook = null;
			XSSFSheet sheet = null;
			int colNum = 0;

			file = new FileInputStream(new File(fileName));

			workbook = new XSSFWorkbook(file);
			sheet = workbook.getSheet(sheetName);
			colNum = sheet.getRow(0).getLastCellNum();
			content = new String[rowNum][colNum];

			for (int i = 0; i < rowNum; i++) {
				XSSFRow row = sheet.getRow(i + 1);
				for (int j = 0; j < colNum; j++) {
					XSSFCell cell = row.getCell(j);
					String value;
					if (cell != null) {
						// value = cell.getStringCellValue();
						value = df.formatCellValue(row.getCell(j));

						content[i][j] = value;
					} else {
						content[i][j] = "";
					}
				}
			}
			workbook.close();
			return content;
		} catch (FileNotFoundException e) {
			throw new FrameworkException("File " + fileName + " not found for reading.");
		} catch (IOException e) {
			throw new FrameworkException("Exception occured while reading " + fileName);
		} catch (Exception e) {
			throw new FrameworkException("Unknown Exception while reading " + fileName + "&" + sheetName + "---"
					+ e.getClass() + "---" + e.getMessage());
		}

	}

	public static String dateFormat(String DateToSelect, String dateFormat) {

		String formatters = "";
		try {
			if (!isNull_Empty_WhiteSpace(DateToSelect)) {
				LocalDate localDateT = LocalDate.now();
				LocalDate localDateT_1 = null;
				if (DateToSelect.toLowerCase().contains("+".toLowerCase())) {
					String[] DateToSelectArr = DateToSelect.split("\\+");
					String DayNum = DateToSelectArr[1];
					int DaysToIncrement = Integer.parseInt(DayNum.trim());
					localDateT_1 = localDateT.plus(DaysToIncrement, ChronoUnit.DAYS);
				} 
				if (!(isNull_Empty_WhiteSpace(dateFormat))) {
					formatters = DateTimeFormatter.ofPattern(dateFormat).format(localDateT_1);
				} else {
					formatters = DateTimeFormatter.ofPattern("MM/dd/yyyy").format(localDateT_1);
				}
			}
		} catch (Exception e) {
			System.out.println("Error occured dateFormat " + e.getMessage());
		}
		return formatters;
	}

	public static boolean isNull_Empty_WhiteSpace(String CmpVal) {
		try {
			if (CmpVal == null) {
				return true;
			} else {
				CmpVal = CmpVal.replaceAll("\u00a0", "");
				CmpVal = CmpVal.replaceAll("&nbsp", "").trim();
			}
		} catch (Exception e) {
			System.out.println("Error occured isNull_Empty_WhiteSpace " + e.getMessage());
		}
		if (CmpVal.trim() != "" && CmpVal != null && (CmpVal.isEmpty()) == false) {
			return false;
		} else {
			return true;
		}

	}

	public static String formatDate(String Date, String format) {
		int date_Day, date_Month, date_Year;
		try {
			date_Day = Integer.parseInt(Date.split("/")[1]);
			date_Month = Integer.parseInt(Date.split("/")[0]) - 1;
			date_Year = Integer.parseInt(Date.split("/")[2]) - 1900;

			Date convertedServiceDate = new Date(date_Year, date_Month, date_Day);
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			Date = sdf.format(convertedServiceDate);
			return Date;
		} catch (Exception e) {
			throw new FrameworkException(
					"Unknown Error encountered while formatting. ---" + e.getClass() + "---" + e.getMessage());
		}
	}

	public static String convertDateToYYYYMMDD(String date) {
		String updatedDate[] = date.split("/");
		return updatedDate[2] + "-" + updatedDate[0] + "-" + updatedDate[1];
	}

	public static String getTodaysDate(String format) {
		DateFormat dateFormat = new SimpleDateFormat(format);
		Calendar cal = Calendar.getInstance();
		return dateFormat.format(cal.getTime());
	}

	public static String getPropertyEcomm(String strKey) {
		try {
			File f = new File(strFileName1);
			if (f.exists()) {
				FileInputStream in = new FileInputStream(f);
				props.load(in);
				strValue = props.getProperty(strKey);
				in.close();
			} else
				throw new FrameworkException("Configuration File not found.");
		} catch (Exception e) {
			throw new FrameworkException("Unknown Error encountered while reading " + strKey
					+ " from configuration file. ---" + e.getClass() + "---" + e.getMessage());
		}
		return strValue;
	}

	public static int SystemTime() {
		int Myhours = LocalDateTime.now().getHour();
		return Myhours;
	}

}
