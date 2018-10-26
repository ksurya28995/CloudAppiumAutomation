package ireff;

import java.net.MalformedURLException;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import AppiumCore.Android;
import IreffImplements.ireff_Implements;

public class OperatorsList_AppTest1 {
	
	@BeforeClass
	public static void init() throws MalformedURLException, InterruptedException {
		Android.initDriver("ireff");
	}

	@Test
	public void startingTest() throws Exception {
		ireff_Implements appObj = new ireff_Implements();
		System.err.println("TestCase: 1");
		appObj.validNewOperatorsList("validLists.csv");
	}

	@AfterClass
	public static void terminate() throws MalformedURLException {
		Android.exitDriver();	
	}
	
}
