package AppiumCore;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;

public class Android {

	public static AndroidDriver driver;
	public static int waitTime = 30000;

	/**
	 * Method is used to initialize the android driver
	 * 
	 * @param appName
	 *            name of the app
	 * 
	 * @author SuryaRay
	 */
	public static AndroidDriver initDriver(String appName) throws MalformedURLException, InterruptedException {
		driver = new AndroidDriver(new URL(PropertyManager.getPropertyValue("androiddriverurl")),
				getDevCapabilities(appName));
		return driver;
	}

	/**
	 * Method is used to set the desired capabilities of the device
	 * 
	 * @param appName
	 *            name of the app
	 * 
	 * @author SuryaRay
	 */
	public static DesiredCapabilities getDevCapabilities(String appName) {
		DesiredCapabilities capability = new DesiredCapabilities();
		capability.setCapability(MobileCapabilityType.DEVICE_NAME, PropertyManager.getPropertyValue("devicename"));
		capability.setCapability(MobileCapabilityType.PLATFORM_VERSION,
				PropertyManager.getPropertyValue("platformversion"));
		capability.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.ANDROID);
		capability.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, "60");
		capability.setCapability("noReset", "false");
		capability.setCapability("browserstack.debug", "true");
		capability.setCapability("app", PropertyManager.getPropertyValue("app"));
		return capability;
	}

	/**
	 * Method is used to uninstall the appium support apps and terminate the driver
	 * 
	 * @author SuryaRay
	 */
	public static void exitDriver() throws MalformedURLException {
		driver.pressKeyCode(AndroidKeyCode.HOME);
		System.err.println("Uninstalling appium support apps on the device..");
		driver.removeApp(PropertyManager.getPropertyValue("appiumsettingspackage"));
		System.out.println("Uninstall Appium Setting app : done");
		driver.removeApp(PropertyManager.getPropertyValue("appiumunlockpackage"));
		System.out.println("uninstall Appium Unlock app : done");
		driver.quit();
		System.out.println("Quiting the Application");
	}
}
