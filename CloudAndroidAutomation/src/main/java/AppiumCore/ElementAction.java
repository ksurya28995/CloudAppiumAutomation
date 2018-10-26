package AppiumCore;

import java.time.Duration;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

import io.appium.java_client.TouchAction;

public class ElementAction {

	public static String EAlocator;
	public static long startTime;
	public static Date time = new Date();

	/**
	 * Method is used as a constructor
	 * 
	 * @author SuryaRay
	 */
	public ElementAction(String _locator) {
		startTime = 0;
		EAlocator = _locator;
	}

	/**
	 * Method is used to set delay 
	 * 
	 * @param sleeptime - delaytime
	 * 
	 * @author SuryaRay
	 */
	public void threadSleep(int sleepTime) throws Exception {
		Thread.sleep(sleepTime);
	}

	/**
	 * Method is used to set delay of 2 secs
	 * 
	 * @param sleeptime - delaytime
	 * 
	 * @author SuryaRay
	 */
	public void threadSleep() throws Exception {
		Thread.sleep(2000);
	}

	/**
	 * Method is used to find the element based on the path provided
	 * 
	 * @return webelement 
	 * 
	 * @author SuryaRay
	 */
	public WebElement getElement() throws Exception {
		ElementFinder.locator = "new UiSelector()";
		waitForElement();
		if (EAlocator.contains("new UiSelector()")) {
			return Android.driver.findElementByAndroidUIAutomator(EAlocator);
		} else {
			return Android.driver.findElementByXPath(EAlocator);
		}
	}

	/**
	 * Method is used to find the element based on the xpath provided
	 * 
	 * @return webelement 
	 * 
	 * @author SuryaRay
	 */
	public WebElement getElementByXpath(String xpath) throws Exception {
		String tempEAlocator = EAlocator;
		EAlocator = xpath;
		WebElement xpathElem = getElement();
		EAlocator = tempEAlocator;
		return xpathElem;
	}
	
	/**
	 * Method is used to find the elements based on the path provided
	 * 
	 * @return webelement 
	 * 
	 * @author SuryaRay
	 */
	public List<WebElement> getElements() throws Exception {
		ElementFinder.locator = "new UiSelector()";
		if (EAlocator.contains("new UiSelector()")) {
			return Android.driver.findElementsByAndroidUIAutomator(EAlocator);
		} else {
			return Android.driver.findElementsByXPath(EAlocator);
		}
	}

	/**
	 * Method is used to tap on the element
	 * 
	 * @author SuryaRay
	 */
	public void tap() throws Exception {
		getElement().click();
	}

	/**
	 * Method is used to set the text to the element
	 * 
	 * @author SuryaRay
	 */
	public void enterText(String value) throws Exception {
		getElement().sendKeys("");
		getElement().sendKeys(value);
	}

	/**
	 * Method is used to get the attributes as an array
	 * 
	 * @param attribute whose values needed to be returned
	 * @return array of attribute values
	 * 
	 * @author SuryaRay
	 */
	public String[] getAttributeList(String attribute) throws Exception {
		String[] array = null;
		List<WebElement> elems = getElements();
		array = new String[elems.size()];
		for (int i = 0; i < elems.size(); i++)
			array[i] = elems.get(i).getAttribute(attribute);
		return array;
	}

	/**
	 * Method is used to get the list of text
	 * 
	 * @return array of texts 
	 * 
	 * @author SuryaRay
	 */
	public String[] getTextList() throws Exception {
		String[] array = null;
		List<WebElement> elems = getElements();
		array = new String[elems.size()];
		for (int i = 0; i < elems.size(); i++)
			array[i] = elems.get(i).getText();
		return array;

	}

	/**
	 * Method is used to get the text of the element
	 * 
	 * @author SuryaRay
	 */
	public String getText() throws Exception {
		return getElement().getText();
	}

	/**
	 * Method is used to check whether the element is displayed
	 * 
	 * @author SuryaRay
	 */
	public void isDisplayed() throws Exception {
		getElement().isDisplayed();
	}

	/**
	 * Method is used to clear the text of the element
	 * 
	 * @author SuryaRay
	 */
	public void clearText() throws Exception {
		getElement().clear();
	}

	/**
	 * Method is used to long press the element
	 * 
	 * @author SuryaRay
	 */
	public void longPress() throws Exception {
		TouchAction action = new TouchAction(Android.driver);
		action.longPress(getElement()).perform();
	}

	/**
	 * Method is used to long press the element
	 * 
	 * @param sec - long pressed for the given secs
	 * 
	 * @author SuryaRay
	 */
	public void longPress(Duration sec) throws Exception {
		TouchAction action = new TouchAction(Android.driver);
		action.longPress(getElement(), sec).perform();
	}

	/**
	 * Method is used to check the element exist 
	 * 
	 * @return true if exist, false if not exist
	 * 
	 * @author SuryaRay
	 */
	public boolean isExist() throws Exception {
		try {
			if (EAlocator.contains("new UiSelector()"))
				return Android.driver.findElementByAndroidUIAutomator(EAlocator).isDisplayed();
			else
				return Android.driver.findElementByXPath(EAlocator).isDisplayed();
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	/**
	 * Method is used to start the timer
	 * 
	 * @return start time of the element search
	 * 
	 * @author SuryaRay
	 */
	public static long startTime() {
		time = new Date();
		if (startTime == 0)
			startTime = time.getTime();
		return startTime;
	}

	/**
	 * Method is used to check whether the time expired for the element search wait
	 * 
	 * @return true, if time expired
	 * 
	 * @author SuryaRay
	 */
	public static boolean expired() {
		time = new Date();
		boolean isExpired = (time.getTime() - startTime()) > Android.waitTime;
		return isExpired;
	}

	/**
	 * Method is used to wait for the element to appear in the screen
	 * 
	 * @author SuryaRay
	 */
	public void waitForElement() throws Exception {
		boolean elemNotFound = true;
		while (!expired()) {
			if (isExist()) {
				elemNotFound = false;
				break;
			} else {
				Thread.sleep(500);
			}
		}
		if (elemNotFound) {
			throw new Exception("Element not found: " + EAlocator);
		}
	}

	/**
	 * Method is used to hover on the ekement
	 * 
	 * @author SuryaRay
	 */
	public void hover() throws Exception {
		TouchAction action = new TouchAction(Android.driver);
		action.moveTo(getElement()).perform();
	}

	/**
	 * Method is used to scroll left for the element by count
	 * 
	 * @param scrollElemXpath - xpath of element on which it is pressed to scroll
	 * @param noOfScroll - no of scroll to be made
	 * 
	 * @author SuryaRay
	 */
	public void scrollLeftByCount(String scrollElemXpath, int noOfScroll) throws Exception {
		TouchAction act = new TouchAction(Android.driver);
		Point pt = getElementByXpath(scrollElemXpath).getLocation();
		pt.x = pt.getX() + 10;
		pt.y = pt.getY() + 10;
		while (!isExist() && noOfScroll > 0) {
			System.err.println("scrolling to find the element...");
			act.press(pt.getX() + 250, pt.getY()).waitAction(Duration.ofMillis(1000)).moveTo(pt.getX(), pt.getY())
					.release().perform();
			noOfScroll--;
		}
	}
	
	/**
	 * Method is used to scrollup untill the element disappears on the screen 
	 * 
	 * @param scrollElemXpath - xpath of element on which it is pressed to scroll
	 * 
	 * @author SuryaRay
	 */
	public boolean scrollUpTillDisappears(String scrollElemXpath) throws Exception {
		int noOfScroll = 7;
		TouchAction act = new TouchAction(Android.driver);
		Point pt = getElementByXpath(scrollElemXpath).getLocation();
		pt.x = pt.getX() + 20;
		pt.y = pt.getY() + 20;
		while (isExist() && noOfScroll > 0) {
			System.err.println("scrolling till the element disappears...");
			act.press(pt.getX(), pt.getY()+ 150).waitAction(Duration.ofMillis(300)).moveTo(pt.getX(), pt.getY())
					.release().perform();
			noOfScroll--;
		}
		if (isExist()) {
			return true;
		} else {
			return false;
		}
	}
	
}
