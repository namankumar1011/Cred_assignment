package WebAutomation;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TestScenarios {
	
//	private WebDriver driver;
//
//	
//	@BeforeMethod
//    public void setUp() {
//        // Setup ChromeDriver using WebDriverManager
//    	
////    	ChromeOptions options = new ChromeOptions();
////      options.addArguments("--headless");
//		
//		Map<String, Object> prefs = new HashMap<>();
//        prefs.put("download.default_directory", "F://");
//        ChromeOptions options = new ChromeOptions();
//        options.setExperimentalOption("prefs", prefs);
//        WebDriverManager.chromedriver().setup();
//        
//        // Create a new instance of the ChromeDriver
//        driver = new ChromeDriver(options);//	pass options for headless
//        driver.manage().window().maximize();
//
//    }
	
	
	 	private static ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();
	    private static ThreadLocal<WebDriverWait> waitThread = new ThreadLocal<>();

	    @BeforeMethod
	    @Parameters("browser")
	    public void setUp(@Optional("chrome") String browser) {
	        // Set up WebDriver based on the browser parameter
	        WebDriver driver;
	        WebDriverWait wait;

	        if (browser.equalsIgnoreCase("chrome")) {
	        	Map<String, Object> prefs = new HashMap<>();
		          prefs.put("download.default_directory", "F://");
		          ChromeOptions options = new ChromeOptions();
		          options.setExperimentalOption("prefs", prefs);
		          WebDriverManager.chromedriver().setup();
		          
		          // Create a new instance of the ChromeDriver
		          driver = new ChromeDriver(options);
	            
	            
	        } else if (browser.equalsIgnoreCase("firefox")) {
	          
	            driver = new FirefoxDriver();
	        } else {
	            throw new IllegalArgumentException("Invalid browser parameter");
	        }

	        driver.manage().window().maximize();
	        driverThread.set(driver);

	        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	        waitThread.set(wait);
	    }
	
	@Test
	public void testTitle() {
		WebDriver driver = driverThread.get();
		driver.get("https://qavbox.github.io/demo/");
		Assert.assertEquals(driver.getTitle(), "QAVBOX Demo");
	}
	
	@Test
	public void testSignUpForm() throws InterruptedException {
		WebDriver driver = driverThread.get();
		driver.get("https://qavbox.github.io/demo/");
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		
		//navigate to signup page
		WebElement signup_button=driver.findElement(By.xpath("//a[normalize-space()='SignUp Form']"));
		signup_button.click();
		
		//check signup title
		Assert.assertEquals(driver.getTitle(), "Registration Form");
		
		//verify the hidden element
		WebElement hidden_element=driver.findElement(By.xpath("//body//form//div[@class='container']//div//div[1]"));
		Assert.assertEquals(hidden_element.isDisplayed(), false);
		
		//input name
		WebElement full_name=driver.findElement(By.xpath("//input[@class='EnterText']"));
		full_name.sendKeys("Naman Baderia");
		
		//input email
		WebElement email=driver.findElement(By.xpath("//div[3]//input[1]"));
		email.sendKeys("naman@gmail.com");
		
		//input contact
		WebElement contact=driver.findElement(By.xpath("//div[4]//input[1]"));
		contact.sendKeys("123456789");
		
		//fax check
		WebElement fax=driver.findElement(By.xpath("//input[@disabled='disabled']"));
		Assert.assertEquals(fax.isEnabled(), false);
		
		//file upload
		WebElement file=driver.findElement(By.xpath("//input[@type='file']"));
		file.sendKeys("C:\\Users\\Naman\\Desktop\\Logoimage.jpg");
		
		
		//selecting gender
		WebElement gender_dropdown=driver.findElement(By.xpath("//div[@class='container']//div//div//select"));
		Select gender_obj=new Select(gender_dropdown);
		gender_obj.selectByVisibleText("Male");
		
		
		//selecting years of exp
		WebElement year_exp=driver.findElement(By.xpath("//input[@value='two']"));
		year_exp.click();
		
		
		//selecting skills 
		WebElement skill1=driver.findElement(By.xpath("//input[@value='manualtesting']"));
		WebElement skill2=driver.findElement(By.xpath("//input[@value='automationtesting']"));
		skill1.click();
		skill2.click();
		
		
		//select automation tools
		WebElement tools_dropdown=driver.findElement(By.xpath("//select[@multiple='multiple']"));
		Select tools_obj=new Select(tools_dropdown);
		tools_obj.selectByVisibleText("Selenium");
		tools_obj.selectByVisibleText("Cypress");
		
		
		
		
		//submit 
		WebElement submit=driver.findElement(By.xpath("//input[@type='submit']"));
		submit.click();
		
		//verify submit
		Alert alert=driver.switchTo().alert();
		Assert.assertEquals(alert.getText(), "Registration Done!");
    	alert.accept();  	

	}
	
	
	@Test
	public void testWebTable(){
		WebDriver driver = driverThread.get();
		driver.get("https://qavbox.github.io/demo/");
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		
		//navigate to web table page
		WebElement webtable_button=driver.findElement(By.xpath("//a[normalize-space()='WebTable']"));
		webtable_button.click();
		
		//deleting a table row
		WebElement delete_button=driver.findElement(By.xpath("//tbody/tr[2]/td[5]/input[1]"));
		delete_button.click();
		Assert.assertEquals(delete_button.getAttribute("value"), "Deleted");
		
		
		//clicking on link inside the table
		WebElement selenium_link=driver.findElement(By.xpath("//tbody/tr[2]/td[3]/a"));
		selenium_link.click();
		driver.navigate().back();
		
		
		//traversing first row of table 2
		int cols=driver.findElements(By.xpath("//div[@class='container']//div//table//th")).size();
		for(int c=1;c<=cols;c++) {
			String data=driver.findElement(By.xpath("//div[@class='container']//div//table//tr[1]/td["+c+"]")).getText();
			System.out.print(data+"    ");
		}
		
	}
	
	
	@Test
	public void testLists(){
		WebDriver driver = driverThread.get();
		driver.get("https://qavbox.github.io/demo/");
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		
		//navigate to lists page
		WebElement list_button=driver.findElement(By.xpath("//a[normalize-space()='List Items']"));
		list_button.click();
		
		//printing the value of sublist using parent-child
		WebElement sublist=driver.findElement(By.xpath("//div[@class='container']//div//ul/li/child::ul"));
		System.out.print(sublist.getText()+"    ");
		
	}
	
	
	@Test
	public void testIFrames() throws InterruptedException{
		WebDriver driver = driverThread.get();
		driver.get("https://qavbox.github.io/demo/");
		//driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		
		//navigate to lists page
		WebElement iframe_button=driver.findElement(By.xpath("//a[normalize-space()='iFrames']"));
		iframe_button.click();
		
		//printing the value of sublist using parent-child
		WebElement name_input=driver.findElement(By.xpath("//input[@type='text']"));
		name_input.sendKeys("Naman");
		
		//frame1
		driver.switchTo().frame("Frame1");
		WebElement frame1_text=driver.findElement(By.xpath("//body//p"));
		System.out.print(frame1_text.getText());
		driver.switchTo().defaultContent();
		
		driver.switchTo().frame("Frame2");
		WebElement input_frame2=driver.findElement(By.xpath("//input[@type='text']"));
		input_frame2.sendKeys("Naman");
		driver.switchTo().defaultContent();
		Thread.sleep(2000);
		
	}
	
	@Test
	public void testAlerts() throws InterruptedException{
		WebDriver driver = driverThread.get();
		driver.get("https://qavbox.github.io/demo/");
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		
		//navigate to lists page
		WebElement alerts_button=driver.findElement(By.xpath("//a[normalize-space()='Alerts']"));
		alerts_button.click();
		
		//printing the value of sublist using parent-child
		WebElement simple_alert_button=driver.findElement(By.xpath("//input[@value='Submit']"));
		simple_alert_button.click();
		driver.switchTo().alert().accept();
		
		
		WebElement delay_alert_button=driver.findElement(By.xpath("//input[@value='SubmitDelay']"));
		delay_alert_button.click();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		Alert alert = wait.until(ExpectedConditions.alertIsPresent());
		alert.accept();
		
		
		WebElement prompt_alert_button=driver.findElement(By.xpath("//input[@value='PromptMe']"));
		prompt_alert_button.click();
		alert=driver.switchTo().alert();
		alert.sendKeys("Naman");
		alert.accept();
		WebElement prompt_text=driver.findElement(By.xpath("//div[@class='container']//p[@align='center']"));
		Assert.assertEquals(prompt_text.getText(), "Hello Naman! How are you today?");
		
		WebElement confirm_alert_button=driver.findElement(By.xpath("//input[@value='Confirm']"));
		confirm_alert_button.click();
		driver.switchTo().alert().accept();
		WebElement confirm_text=driver.findElement(By.xpath("//div[@class='container']//p[@align='center']"));
		Assert.assertEquals(confirm_text.getText(), "You pressed OK!");
		
		
	}
	
	
	@Test
	public void testLinks() throws InterruptedException{
		WebDriver driver = driverThread.get();
		driver.get("https://qavbox.github.io/demo/");
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		
		//navigate to lists page
		WebElement links_button=driver.findElement(By.xpath("//a[normalize-space()='Links']"));
		links_button.click();
		
		//printing the value of sublist using parent-child
		WebElement new_pagelink_button=driver.findElement(By.xpath("//input[@value='New Tab']"));
		new_pagelink_button.click();
		String mainWindowHandle = driver.getWindowHandle();
		
		Set<String> allWindowHandles=driver.getWindowHandles();
		for (String windowHandle : allWindowHandles) {
            if (!windowHandle.equals(mainWindowHandle)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }
		System.out.println(driver.getTitle());
		driver.close();
        driver.switchTo().window(mainWindowHandle);
        
        
        WebElement new_windowlink_button=driver.findElement(By.xpath("//input[@value='Multi Window']"));
        new_windowlink_button.click();
		mainWindowHandle = driver.getWindowHandle();
		
		allWindowHandles=driver.getWindowHandles();
		for (String windowHandle : allWindowHandles) {
            if (!windowHandle.equals(mainWindowHandle)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }
		System.out.println(driver.getTitle());
		driver.close();
        driver.switchTo().window(mainWindowHandle);
        
        
    	Thread.sleep(2000);
		
    	
    	
    	WebElement download_button=driver.findElement(By.xpath("//input[@value='Download']"));
    	download_button.click();
    	
    	
    	boolean isDownloaded = false;
    	int maxWaitTimeInSeconds = 60;
    	for (int i = 0; i < maxWaitTimeInSeconds; i++) {
    	    if (isFileDownloaded("F://", "qav_test.zip")) {
    	        isDownloaded = true;
    	        break;
    	    }
    	    Thread.sleep(1000); // Wait for 1 second
    	}

    	// Assert or handle the result
    	Assert.assertTrue(isDownloaded, "File was not downloaded within the expected time.");

		
	}
	
	public boolean isFileDownloaded(String downloadPath, String fileName) {
	    File dir = new File(downloadPath);
	    File[] dirContents = dir.listFiles();

	    for (File file : dirContents) {
	        if (file.getName().equals(fileName)) {
	            return true;
	        }
	    }
	    return false;
	}
	
	
	@Test
	public void testDragNDrop() throws InterruptedException{
		WebDriver driver = driverThread.get();
		driver.get("https://qavbox.github.io/demo/");
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		
		//navigate to lists page
		WebElement dnd_button=driver.findElement(By.xpath("//a[normalize-space()='DragnDrop']"));
		dnd_button.click();
		
		//printing the value of sublist using parent-child
		WebElement source=driver.findElement(By.xpath("//div[@class='ui-widget-content ui-draggable ui-draggable-handle']"));
		WebElement target=driver.findElement(By.xpath("//div[@class='ui-widget-header ui-droppable']"));
		Actions actions=new Actions(driver);
		actions.dragAndDrop(source, target).perform();
		WebElement dnd_text=driver.findElement(By.xpath("//div[@class='ui-widget-header ui-droppable ui-state-highlight']//p"));
		Assert.assertEquals(dnd_text.getText(), "Dropped!");
		Thread.sleep(2000);
		
		
		WebElement slider = driver.findElement(By.xpath("//input[@value='0']"));

        // Get the width of the slider
        int sliderWidth = slider.getSize().getWidth();

        // Calculate the desired position (e.g., move to the middle of the slider)
        int xOffset = sliderWidth / 4;

        // Use Actions class to perform slider movement
        
        actions.clickAndHold(slider)
                .moveByOffset(xOffset, 0)  // Adjust the offset based on your requirements
                .release()
                .perform();
        
        
        WebElement slider_value = driver.findElement(By.xpath("//div[@class='slider1']//span"));
        System.out.println(slider_value.getText());
        
        
	}
	

	@Test
	public void testDelays() throws InterruptedException{
		WebDriver driver = driverThread.get();
		driver.get("https://qavbox.github.io/demo/");
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));

		WebElement delay_button=driver.findElement(By.xpath("//a[normalize-space()='Delay']"));
		delay_button.click();
		driver.findElement(By.xpath("//input[@value='Try me!']")).click();
		System.out.println(driver.findElement(By.id("delay")).getText());
		
		driver.findElement(By.xpath("//input[@value='Start']")).click();
		System.out.println(driver.findElement(By.id("loaderdelay")).getText());
		
		
	}
	
	
	@Test
	public void testShadowDom() throws InterruptedException{
		WebDriver driver = driverThread.get();
		driver.get("https://qavbox.github.io/demo/");
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		//navigate to lists page
		WebElement shadow_button=driver.findElement(By.xpath("//a[normalize-space()='Shadow DOM']"));
		shadow_button.click();
		
		JavascriptExecutor js=(JavascriptExecutor) driver;
		js.executeScript("document.querySelector('my-open-component').shadowRoot.querySelector('input').value=\"Naman\"");
		js.executeScript("document.querySelector('my-close-component')._root.querySelector('input').value=\"Naman\"");
		
		Thread.sleep(2000);
		
	}
	
	
	@Test
		public void testAutoComplete() throws InterruptedException{
		WebDriver driver = driverThread.get();
			driver.get("https://qavbox.github.io/demo/");
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
			//navigate to lists page
			WebElement auto_button=driver.findElement(By.xpath("//a[normalize-space()='Auto Suggestions']"));
			auto_button.click();
			
			WebElement input=driver.findElement(By.xpath("//input[@placeholder='Country']"));
			input.sendKeys("india");
			driver.findElement(By.xpath("//div[@class='autocomplete-items']//div")).click();
			Thread.sleep(2000);
			
		}
		
		
	
	
	
//	@AfterMethod
//    public void tearDown() {
//        // Close the browser
//        if (driver != null) {
//            driver.quit();
//        }
//    }
//	
	
	
	
	
	 @AfterMethod
	    public void tearDown() {
	        // Close the browser
	        WebDriver driver = driverThread.get();
	        if (driver != null) {
	            driver.quit();
	        }

	        driverThread.remove();
	        waitThread.remove();
	    }
	
}
