package AppiumCore;


import io.appium.java_client.android.AndroidKeyCode;
/**
 * Class is used to do android button actions
 * 
 * @author SuryaRay
 */
public class Actions {

	public void home() {
		Android.driver.pressKeyCode(AndroidKeyCode.HOME);
	}
	
	public void back() {
		Android.driver.pressKeyCode(AndroidKeyCode.BACK);
	}
	
	public void enter() {
		Android.driver.pressKeyCode(AndroidKeyCode.ENTER);
	}
	
}
