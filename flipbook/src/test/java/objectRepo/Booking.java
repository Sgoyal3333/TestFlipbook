package objectRepo;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.relevantcodes.extentreports.LogStatus;

import config.FrameworkException;
import reusablecomponents.Report;
import reusablecomponents.TechnicalComponents;

public class Booking {

	private final WebDriver driver;
	WebDriverWait wait;

	public Booking(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		wait = new WebDriverWait(driver, 30);
	}
 
	@FindBy(xpath = "//input[@class='form-control search_query']")
	WebElement location_TxtBox;

	@FindBy(xpath = "(//div[@class='location_div']/span)[1]")
	WebElement suggestedCity;

	@FindBy(xpath = "//input[@id='checkindate']")
	WebElement checkIn;

	@FindBy(xpath = "//input[@id='checkoutdate']")
	WebElement checkOut;

	@FindBy(xpath = "//button[contains(text(),'Search')]")
	WebElement search_Btn;

	@FindBy(xpath = "//a[contains(text(),'Some Feature Here')]")
	WebElement feture_Btn;

	@FindBy(xpath = "//button[@data-id='rooms']")
	WebElement room_ddl;

	@FindBy(xpath = "//button[@data-id='adult1']")
	WebElement adult_ddl;

	@FindBy(xpath = "//button[@data-id='adult2']")
	WebElement child_ddl;

	@FindBy(xpath = "(//pre[contains(text(),'Envelope')])[1]")
	WebElement codeTest1;

	@FindBy(xpath = "(//pre[contains(text(),'Envelope')])[2]")
	WebElement codeTest2;

	public void book(String location, String checkInDate, String checkOutDate, String roomCount, String adultCount,
			String childCount) {
		int i = 1;
		TechnicalComponents.waitTill(location_TxtBox, "enable");
		TechnicalComponents.type(location_TxtBox, location, "location");
		wait.until(ExpectedConditions.visibilityOf(suggestedCity));
		TechnicalComponents.waitTill(4);
		TechnicalComponents.click(suggestedCity, "Suggested city");
		TechnicalComponents.waitTill(3);
		TechnicalComponents.scroll(search_Btn);
		TechnicalComponents.waitTill(2);
		if (!roomCount.isEmpty()) {
			TechnicalComponents.click(room_ddl, "rooms");
			TechnicalComponents.click(driver.findElement(By.xpath("//span[contains(text(),'" + roomCount + "')]")),
					"rooms");
		}
		/*TechnicalComponents.click(checkIn, "check in date");
		driver.findElement(By
				.xpath("(//div[@class='lightpick__day is-available ' and  contains(text(),'" + checkInDate + "')])[1]"))
				.click();
		TechnicalComponents.waitTill(2);
		List<WebElement> cellsOfArrivalDate = wait.until(ExpectedConditions
				.presenceOfAllElementsLocatedBy(By.cssSelector("section:nth-of-type(2) > .lightpick__days > div")));

		cellsOfArrivalDate.get(5).click();
		

		TechnicalComponents.waitTill(4);
		if (!roomCount.isEmpty()) {
			TechnicalComponents.click(room_ddl, "rooms");
			TechnicalComponents.click(driver.findElement(By.xpath("//span[contains(text(),'" + roomCount + "')]")),
					"rooms");
		}
		// Select adult
		if (!adultCount.isEmpty()) {
			TechnicalComponents.click(adult_ddl, "adult");
			TechnicalComponents.click(driver.findElement(
					By.xpath("//button[@data-id='adult1']/following-sibling::div/ul/li/a/span[contains(text(),'"
							+ adultCount + "')]")),
					"adult");
		}
		// Select Child
		if (!childCount.isEmpty()) {
			TechnicalComponents.click(child_ddl, "child");
			TechnicalComponents.click(driver.findElement(
					By.xpath("//button[@data-id='adult2']/following-sibling::div/ul/li/a/span[contains(text(),'"
							+ childCount + "')]")),
					"child");
		}*/
	}

	public void clickSearchBtn() {

		TechnicalComponents.click(search_Btn, "Search");
	}

	public void verifybookNow() {
		TechnicalComponents.waitForPageToLoad();
		
		try {
            wait.until(ExpectedConditions.visibilityOf(feture_Btn));
			TechnicalComponents.verifyElementState(feture_Btn, "enable", "book now button");
			getCode();
		} catch (Exception e) {
			getCode();
			throw new FrameworkException("Geeting error on listing page.");
		}
		
			
	}

	public void navigateURL(String url) {
		TechnicalComponents.navigatetoUrl(url);
	}

	public void getCode() {

		Report.infoStep("CCAMTH : MultiSingle REQ: -     " + codeTest1.getText());
		Report.infoStep("CCAMTH : MultiSingle RES : -     " + codeTest2.getText());
	}
}