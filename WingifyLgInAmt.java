package assignment1;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import io.github.bonigarcia.wdm.WebDriverManager;



public class WingifyLgInAmt {
	
	String url="https://sakshingp.github.io/assignment/login.html";
	WebDriver driver;
	boolean done=false;
	ExtentHtmlReporter htmlreporter;
	ExtentReports extent;
	ExtentTest test;
	
	@BeforeTest
	public void openUrl() throws InterruptedException {
		WebDriverManager.chromedriver().setup();
		driver=new ChromeDriver();
		driver.get(url);
		driver.manage().window().maximize();
		Thread.sleep(1000);
		
		htmlreporter=new ExtentHtmlReporter("extentReport.html");
		extent=new ExtentReports();
		extent.attachReporter(htmlreporter);
	}
	
	@Test(priority = 0, groups = {"Positive test case"})
	public void logIn() throws InterruptedException {
//		System.out.println("opened");
		test=extent.createTest("Wingify Assignment by Hariharamanikandan K");
		
		driver.findElement(By.id("username")).sendKeys("abcd");
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		test.pass("Entered the Username");
		
		driver.findElement(By.id("password")).sendKeys("1234");
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		test.pass("Entered the Password");
		
		driver.findElement(By.className("form-check-input")).click();
		test.pass("Clicked 'Remember Me'");
		Thread.sleep(1000);
		
		driver.findElement(By.id("log-in")).click();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		test.pass("Clicked the Login button");
		System.out.println("logged in");
	}
	
	@Test(priority = 1, groups = {"Positive test case"})
	public void amt() throws InterruptedException {
		driver.findElement(By.id("amount")).click();
		test.pass("Clicked on Amount header");
		Thread.sleep(3000);
	}
	
	@Test(priority = 2, groups = {"Positive test case"})
	public void checkOrder() throws InterruptedException {
		WebElement topAmt=driver.findElement(By.xpath("(//*[@class='text-success'])[1]"));
		String strTopAmt = topAmt.getText();
//		System.out.println(strTopAmt);
		float firstNum=Float.parseFloat(strTopAmt.replaceAll("[^\\d.]", ""));
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		WebElement secAmt=driver.findElement(By.xpath("(//*[@class='text-success'])[2]"));
		String strSecAmt=secAmt.getText();
		float secondNum=Float.parseFloat(strSecAmt.replaceAll("[^\\d.]", ""));
		WebElement thAmt=driver.findElement(By.xpath("(//*[@class='text-success'])[3]"));
		String strThAmt=thAmt.getText();
		float thirdNum=Float.parseFloat(strThAmt.replaceAll("[^\\d.]", ""));
		
		if(firstNum>=secondNum&&secondNum>=thirdNum) {
			done=true;
			System.out.println("Amount Sorted in decending order");
		}
		else if (firstNum<=secondNum&&secondNum<=thirdNum) {
			driver.findElement(By.id("amount")).click();
			System.out.println("Amount Sorted in Ascending order");
			done=true;
		}
		test.pass("Compared the amounts for assertion");
		
		Assert.assertEquals(true, done);
		test.pass("Asserted that the amount is in order");
		System.out.println("Sorted!!!");
		
		driver.navigate().back();
		test.pass("Navigated back to login page");
		Thread.sleep(2000);
	}
	
	@Test(priority = 3, groups = {"Negative test case"})
	public void negTest() throws InterruptedException {
		driver.findElement(By.id("username")).clear();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		test.pass("Cleared the Username for Negative test case");
		
		driver.findElement(By.id("password")).clear();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		test.pass("Cleared the Password for Negative test case");
		
		driver.findElement(By.className("form-check-input")).click();
		test.pass("Clicked 'Remember Me'");
		
		driver.findElement(By.id("log-in")).click();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		test.pass("Clicked the Login button without credentials");
		
		WebElement err=driver.findElement(By.xpath("//*[@class='alert alert-warning']"));
		String errText=err.getText();
		String actualText1="Both Username and Password must be present";
		String actualText2="Password must be present";
		String actualText3="Username must be present";
		boolean negDone=false;
		
		if (errText.equals(actualText1)||errText.equals(actualText2)||errText.equals(actualText3)) {
			negDone=true;
		}
		else {
			System.out.println("logged in without credentials!!!");
		}

		Assert.assertEquals(negDone, true);
		test.pass("Asserted the Negative test case");
		System.out.println("Negative test Done");
		
		extent.flush();
		Thread.sleep(2000);
	}
	
	@AfterTest
	public void closing() {
		driver.quit();
	}
}
