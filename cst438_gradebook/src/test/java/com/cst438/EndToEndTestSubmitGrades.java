package com.cst438;

import static org.assertj.core.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;

/*
 * This example shows how to use selenium testing using the web driver 
 * with Chrome browser.
 * 
 *  - Buttons, input, and anchor elements are located using XPATH expression.
 *  - onClick( ) method is used with buttons and anchor tags.
 *  - Input fields are located and sendKeys( ) method is used to enter test data.
 *  - Spring Boot JPA is used to initialize, verify and reset the database before
 *      and after testing.
 *      
 *  In SpringBootTest environment, the test program may use Spring repositories to 
 *  setup the database for the test and to verify the result.
 */

@SpringBootTest
public class EndToEndTestSubmitGrades {

	public static final String CHROME_DRIVER_FILE_LOCATION = "C:/chromedriver_win32/chromedriver.exe";

	public static final String URL = "http://localhost:3000";
	public static final int SLEEP_DURATION = 1000; // 1 second.
	public static final String TEST_ASSIGNMENT_NAME = "db design";
	public static final String NEW_GRADE = "99";


	//Code to test adding an assignment 
	@Test
	public void addAssignmentTest() throws Exception{
		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
		WebDriver driver = new ChromeDriver();
		
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		driver.get(URL + "/assignment");
		Thread.sleep(SLEEP_DURATION);
		
		try {
			WebElement assignmentNameInput = driver.findElement(By.id("assignmentName"));
            WebElement dueDateInput = driver.findElement(By.id("dueDate"));
            WebElement submitButton = driver.findElement(By.id("add"));
            
            assignmentNameInput.sendKeys(TEST_ASSIGNMENT_NAME);
            dueDateInput.sendKeys("2023-10-20");
            
            submitButton.click();
            Thread.sleep(SLEEP_DURATION);
            
            WebElement successMessage = driver.findElement(message));
            assertThat(successMessage.getText()).isEqualTo("Assignment Saved");
			
		}catch (Exception ex) {
			throw ex;
		} finally {

			driver.quit();
		}
		
		
	}

	
	//Code to test updating an assignment 
	@Test
	public void updateAssignmentTest() throws Exception{
		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
		WebDriver driver = new ChromeDriver();
	
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	
		//get id? 
		driver.get(URL + "/assignment/{id}");
		Thread.sleep(SLEEP_DURATION);
		
		
		try {
			
			WebElement assignmentNameInput = driver.findElement(By.id("assignmentName"));
	        WebElement dueDateInput = driver.findElement(By.id("dueDate"));
	        WebElement updateButton = driver.findElement(By.id("updateButton"));
	        
	        assignmentNameInput.clear();
	        assignmentNameInput.sendKeys("Updated Assignment Name");
	        dueDateInput.clear();
	        dueDateInput.sendKeys("2023-10-20");
	        
	        updateButton.click();
	        Thread.sleep(SLEEP_DURATION);
	        
	        WebElement successMessage = driver.findElement(message);
	        assertThat(successMessage.getText()).isEqualTo("Assignment Updated Succesfully");

			
		}catch (Exception ex) {
			throw ex;
		} finally {

			driver.quit();
		}
	}


	//code to test deleting an assignment
	@Test
	public void deleteAssignmentTest() throws Exception{
		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
		WebDriver driver = new ChromeDriver();
		
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	
		//get id? 
		driver.get(URL + "/assignment/{id}");
		Thread.sleep(SLEEP_DURATION);
		
		try {
			
			WebElement deleteButton = driver.findElement(By.id("deleteButton"));
	        deleteButton.click();
	        Thread.sleep(SLEEP_DURATION);

	        WebElement successMessage = driver.findElement(message);
	        assertThat(successMessage.getText()).isEqualTo("Assignment deleted.");

			
		}catch (Exception ex) {
			throw ex;
		} finally {

			driver.quit();
		}
	}


	@Test
	public void addCourseTest() throws Exception {



		// set the driver location and start driver
		//@formatter:off
		// browser	property name 				Java Driver Class
		// edge 	webdriver.edge.driver 		EdgeDriver
		// FireFox 	webdriver.firefox.driver 	FirefoxDriver
		// IE 		webdriver.ie.driver 		InternetExplorerDriver
		//@formatter:on
		
		/*
		 * initialize the WebDriver and get the home page. 
		 */

		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
		WebDriver driver = new ChromeDriver();
		// Puts an Implicit wait for 10 seconds before throwing exception
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		driver.get(URL);
		Thread.sleep(SLEEP_DURATION);
		
		WebElement w;
		

		try {
			/*
			* locate the <td> element for assignment title 'db design'
			* 
			*/
			
			List<WebElement> elements  = driver.findElements(By.xpath("//td"));
			boolean found = false;
			for (WebElement we : elements) {
				if (we.getText().equals(TEST_ASSIGNMENT_NAME)) {
					found=true;
					we.findElement(By.xpath("..//a")).click();
					break;
				}
			}
			assertThat(found).withFailMessage("The test assignment was not found.").isTrue();

			/*
			 *  Locate and click Grade button to indicate to grade this assignment.
			 */
			
			Thread.sleep(SLEEP_DURATION);

			/*
			 *  enter grades for all students, then click save.
			 */
			ArrayList<String> originalGrades = new ArrayList<>();
			elements  = driver.findElements(By.xpath("//input"));
			for (WebElement element : elements) {
				originalGrades.add(element.getAttribute("value"));
				element.clear();
				element.sendKeys(NEW_GRADE);
				Thread.sleep(SLEEP_DURATION);
			}
			
			for (String s : originalGrades) {
				System.out.println("'"+s+"'");
			}

			/*
			 *  Locate submit button and click
			 */
			driver.findElement(By.id("sgrade")).click();
			Thread.sleep(SLEEP_DURATION);
			
			w = driver.findElement(By.id("gmessage"));
			assertThat(w.getText()).withFailMessage("After saving grades, message should be \"Grades saved.\"").startsWith("Grades saved");
			
			driver.navigate().back();  // back button to last page
			Thread.sleep(SLEEP_DURATION);
			
			// find the assignment 'db design' again.
			elements  = driver.findElements(By.xpath("//td"));
			found = false;
			for (WebElement we : elements) {
				if (we.getText().equals(TEST_ASSIGNMENT_NAME)) {
					found=true;
					we.findElement(By.xpath("..//a")).click();
					break;
				}
			}
			Thread.sleep(SLEEP_DURATION);
			assertThat(found).withFailMessage("The test assignment was not found.").isTrue();
			
			// verify the grades. Change grades back to original values

			elements  = driver.findElements(By.xpath("//input"));
			for (int idx=0; idx < elements.size(); idx++) {
				WebElement element = elements.get(idx);
				assertThat(element.getAttribute("value")).withFailMessage("Incorrect grade value.").isEqualTo(NEW_GRADE);
				
				// clear the input value by backspacing over the value
				while(!element.getAttribute("value").equals("")){
			        element.sendKeys(Keys.BACK_SPACE);
			    }
				if (!originalGrades.get(idx).equals("")) element.sendKeys(originalGrades.get(idx));
				Thread.sleep(SLEEP_DURATION);
			}
			driver.findElement(By.id("sgrade")).click();
			Thread.sleep(SLEEP_DURATION);
			
			w = driver.findElement(By.id("gmessage"));
			assertThat(w.getText()).withFailMessage("After saving grades, message should be \"Grades saved.\"").startsWith("Grades saved");


		} catch (Exception ex) {
			throw ex;
		} finally {

			driver.quit();
		}

	}
}
