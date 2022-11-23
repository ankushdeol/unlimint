package com.unlimint.RegisterUser;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.unlimint.CustomLibrary.UserUtilities;

public class VerifyHomePageTest extends UserUtilities {

	@BeforeTest(alwaysRun = true)
	public void setup() {
		loginAdminUser();
	}

	@Test(priority = 0, groups = { "sanity", "regression" })
	public void VerifySKDHomePage() {

		verifyElementIsPresentOnPage(wcmLogo);

	}

	@AfterTest(alwaysRun = true)
	public void tearDown() {
		closeBrowser();
	}

}
