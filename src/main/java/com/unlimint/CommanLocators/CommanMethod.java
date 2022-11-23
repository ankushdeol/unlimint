package com.unlimint.CommanLocators;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.unlimint.CustomLibrary.UserUtilities;

public class CommanMethod extends UserUtilities {
	
	
	
	public void hoverSort(By hover,By item) {
		driver.findElement(hover).click();
		waitforElement();
		driver.findElement(item).click();
		waitForExpectedCondition();
	}
	public  void hoverAndSort(By hower,String sortBy,String path) {
		waitForExpectedCondition();
		WebElement node = driver.findElement(hower);
        WebDriverWait wait = new WebDriverWait(driver,1);      		
		for ( int i = 0; i < 5; i++ ) {
			List<WebElement> sortByList = new ArrayList<WebElement>();
			waitForExpectedCondition();
			new Actions(driver).moveToElement(node).build().perform();
			
			//wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(path)));
			waitForExpectedCondition();
			sortByList = driver.findElements(By.xpath(path));    			
   	         for(WebElement sort:sortByList){
   	        	if(sort.getText().equals(sortBy)){
   	        		sort.click();
   	        		waitForExpectedCondition();
   	        		break;
   	        	}
   	         }
   	         if(sortByList.size()>0){
   	        	 break;
   	         }
		}
	}
	
}
