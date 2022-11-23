package com.unlimint.RegisterUser;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.unlimint.CustomLibrary.UserUtilities;

public class LoginPageTest extends UserUtilities {

	@BeforeTest(alwaysRun = true)
	public void setup() {
		openWelcomePage();
	}

	
	  @Test(priority = 0,groups={"sanity","regression"}) 
	  public void login() {
		//  loginAdminUser();
	  }
	 
	@AfterTest(alwaysRun = true)
	public void tearDown() {
		closeBrowser();
	}

}
