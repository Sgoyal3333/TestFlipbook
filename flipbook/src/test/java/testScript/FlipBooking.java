package testScript;

import java.io.FileReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.LogStatus;
import objectRepo.Booking;
import reusablecomponents.BusinessComponents;
import reusablecomponents.TechnicalComponents;

public class FlipBooking extends TechnicalComponents {

	@Test(dataProvider = "locations", dataProviderClass = data.TestData.class)
	public void bookings(String location, String checkInDate, String checkOutDate, String rooms, String adult,
			String child) {
		try {
			String homePage = "http://int-pdt-1.testfb.ca/";
			
			JSONParser parser= new JSONParser();
			FileReader reader=new FileReader(".\\jsonFiles\\data.json");
			Object obj = parser.parse(reader);
			JSONObject user = 	(JSONObject) obj;
			String location1 =  (String) user.get("location");
			String room1 =  (String) user.get("room");
			JSONArray login=(JSONArray)user.get("userlogins");
			String arr[] = new String[login.size()];
			for (int i=0; i<login.size();i++) {
				
				 JSONObject users1 = (JSONObject)login.get(i);
				 String username = (String) users1.get("username");
				 String password = (String) users1.get("password");
				 System.out.println("User name is :" +username);
				 System.out.println("Password is :" +password);
				 System.out.println("-------------------------------------");
				 arr[i]=  username + ","+ password;
			}
		
			logger.log(LogStatus.INFO, "Location is:" + location1);
		    //logger.log(LogStatus.INFO, "Check in date is:" + checkInDate);
			//logger.log(LogStatus.INFO, "Check out date is:" + checkOutDate);
			/*logger.log(LogStatus.INFO, "No of Rooms are:" + rooms);
			logger.log(LogStatus.INFO, "No of adults are:" + adult);
			logger.log(LogStatus.INFO, "No of child:" + child);*/
			logger.log(LogStatus.INFO, "No of Rooms are:"+room1);
			logger.log(LogStatus.INFO, "No of adults are: 1");
			logger.log(LogStatus.INFO, "No of child: 0");
			Booking book = new Booking(driver);
			book.navigateURL(homePage);
			logger.log(LogStatus.PASS, logger.addScreenCapture(screenshot(driver)));
			book.book(location1, checkInDate, checkOutDate, room1, adult, child);
			book.clickSearchBtn();
			waitForPageToLoad();
			logger.log(LogStatus.PASS, logger.addScreenCapture(screenshot(driver)));
			book.verifybookNow();
			
			logger.log(LogStatus.PASS, logger.addScreenCapture(screenshot(driver)));

		} catch (Exception e) {
			logger.log(LogStatus.FAIL,  logger.addScreenCapture(screenshot(driver)));
			logger.log(LogStatus.INFO, "Failed log :"+driver.findElement(By.xpath("//div[@class='tab-content']")).getText());
			
		}
	}

	
	
}
