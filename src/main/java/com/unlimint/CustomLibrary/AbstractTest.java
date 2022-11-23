package com.unlimint.CustomLibrary;

import java.lang.reflect.Method;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;

import com.ptc.skd.CustomLibrary.UserUtilities;

public class AbstractTest extends UserUtilities {
    
	//-------------------------------------------------------------------------

	// leave browser open if the script ends as error
	private boolean _leaveOpen = false;

	//-------------------------------------------------------------------------

	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(Method method) {
		logMethodStart(method.getName());
	}

	/**
	 * Browser should not be closed if the test failed
	 */
	@AfterMethod(alwaysRun = true)
	public void afterMethod(ITestResult result) {
		_leaveOpen = (result.getStatus() == ITestResult.FAILURE);
		logMethodEnd(result.getName());
	}
	
	@AfterTest(alwaysRun = true)
	public void tearDown() {
		// close browser if not in debug mode  
		if (!TRACE) {
			closeBrowser();
			return;
		}
		// if not in debug mode and _leaveOpen is false, close it
		if (!_leaveOpen)
			closeBrowser();
	}
	
	//-------------------------------------------------------------------------
}
