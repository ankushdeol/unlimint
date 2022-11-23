package com.unlimint.CommanLocators;

import org.openqa.selenium.By;

public interface CommonLocator {

	/* Login page */
	public By wcmLogo = By.xpath("//h1[contains(text(),'iWarranty')]");
	public By USERNAME = By.name("SignOn");
	public By PASS = By.name("UserPW");
	public By LoginButton = By.xpath("//button[contains(text(),'Sign In')]");
}
