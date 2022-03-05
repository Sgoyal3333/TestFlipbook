/**
 * 
 */
package data;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.annotations.DataProvider;


import reusablecomponents.Utilities;

public class TestData {
	
	@DataProvider(name = "locations")
	public static Object[][] testLocations() throws FileNotFoundException, IOException {
		return Utilities.Read_Excel("TestData/locations.xlsx", "city");
	}
	
	@DataProvider(name="db")
	public static String[] testData() throws FileNotFoundException, IOException, ParseException {
		JSONParser parser= new JSONParser();
		FileReader reader=new FileReader("TestData/location.json");
		Object obj = parser.parse(reader);
		JSONObject user = 	(JSONObject) obj;
		JSONArray arr=(JSONArray)user.get("");
		String arr1[] = new String[arr.size()];
		for (int i=0; i<arr.size();i++) {
			
			 JSONObject data = (JSONObject)arr.get(i);
			 String value1 = (String) data.get("");
			 String value2 = (String) data.get("");
			 arr1[1]=  value1 + ","+ value2;
		}
		return arr1;
	}
	
}