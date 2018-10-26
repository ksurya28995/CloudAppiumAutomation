package IreffImplements;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import AppiumCore.CsvHandler;
import AppiumCore.ElementFinder;

public class ireff_Implements {
	private static CsvHandler csvObj = null;
	private Map<String, String> arrData = new HashMap<String, String>();
	private ElementFinder elem = new ElementFinder();
	private static SoftAssert asrt =new SoftAssert();
	
	/**
	 * Method is used as a constructor
	 * 
	 * @author SuryaRay
	 */
	public ireff_Implements() {
		csvObj = new CsvHandler();
	}
	
	/**
	 * Method is used to validate the operators list 
	 * 
	 * @param supportCSv - input csv Name
	 *  
	 * @author SuryaRay
	 */
	public void validNewOperatorsList(String supportCSV) throws Exception {
		arrData = csvObj.readCsvData(supportCSV);
		elem.resourceId("in.ireff.android:id/skipTextView").makeUiElement().tap();
		elem.resourceId("in.ireff.android:id/imageView1").makeUiElement().tap();
		String[] operList = elem.xpath(
				"//android.widget.LinearLayout[contains(@resource-id,'in.ireff.android:id/layout')]/android.widget.ImageView")
				.makeUiElement().getAttributeList("resourceId");
		String[] arrList = arrData.get("OperatorLists").split("@");
		int testCount = 0;
		for (String uiList : operList) {
			for (String csvList : arrList) {
				uiList = uiList.replaceAll("in.ireff.android:id/imageView", "");
				if (uiList.equalsIgnoreCase(csvList))
					testCount++;
			}
		}
		asrt.assertTrue(testCount == arrList.length);
		elem.contDesc("Navigate up").makeUiElement().tap();
		elem.resourceId("in.ireff.android:id/currentServiceLayout").makeUiElement().isDisplayed();
	}

	/**
	 * Method is used to validate the tab names of all the operators
	 * 
	 * @author SuryaRay
	 */
	public void validOperaTabs(String supportCSV) throws Exception {
		arrData = csvObj.readCsvData(supportCSV);
		elem.resourceId("in.ireff.android:id/skipTextView").makeUiElement().tap();
		String[] operaList = arrData.get("OperatorLists").split("@");
		for (int i = 0; i < operaList.length; i++) {
			System.out.println("");
			System.out.println("Checking for " + operaList[i].toUpperCase());
			elem.resourceId("in.ireff.android:id/imageView1").makeUiElement().tap();
			elem.resourceId("in.ireff.android:id/imageView" + operaList[i]).makeUiElement().tap();
			if (operaList[i].equalsIgnoreCase("MTNL"))
				elem.resourceId("in.ireff.android:id/textView").text("Mumbai").makeUiElement().tap();
			else
				elem.resourceId("in.ireff.android:id/textView").text("Tamil Nadu").makeUiElement().tap();
			if (arrData.get(operaList[i] + "TabNames").equals("")) {
				String uiMsg = elem.resourceId("in.ireff.android:id/noContentMessage").makeUiElement().getText();
				asrt.assertTrue(
						uiMsg.equalsIgnoreCase("No recharge plans for this operator and circle."));
				System.out.println(uiMsg);
			} else {
				String[] csvTabNames = arrData.get(operaList[i] + "TabNames").split("@");
				String tabPath = "//android.widget.HorizontalScrollView[@resource-id=\"in.ireff.android:id/sliding_tabs\"]";
				String uiTabName = "";
				for (int j = 0; j < csvTabNames.length; j++) {
					if (!elem.resourceId("android:id/text1").text(csvTabNames[j]).makeUiElement().isExist()) {
						elem.resourceId("android:id/text1").text(csvTabNames[j]).makeUiElement()
								.scrollLeftByCount(tabPath, 5);
					}
					uiTabName = elem.resourceId("android:id/text1").text(csvTabNames[j]).makeUiElement().getText();
					asrt.assertTrue( csvTabNames[j].contains(uiTabName));
					System.out.println(csvTabNames[j]);
				}
			}
		}
	}

	/**
	 * Method is used to validate the tab data of all the tabs
	 * 
	 * @author SuryaRay
	 */
	public void validTabData(String supportCSV) throws Exception {
		arrData = csvObj.readCsvData(supportCSV);
		String[] tabLists = arrData.get("TabNames").split("@");
		String eachPackPath = null;
		List<WebElement> eachPack = null;
		boolean checkFlag;
		boolean isScrollEnds;
		String[] csvPacks = null;
		elem.resourceId("in.ireff.android:id/skipTextView").makeUiElement().tap();
		elem.resourceId("in.ireff.android:id/imageView1").makeUiElement().tap();
		elem.resourceId("in.ireff.android:id/imageView" + arrData.get("Operator")).makeUiElement().tap();
		elem.resourceId("in.ireff.android:id/textView").text("Tamil Nadu").makeUiElement().tap();
		
		for (int i = 0; i < tabLists.length; i++) {
			isScrollEnds = false;
			elem.resourceId("android:id/text1").text(tabLists[i]).makeUiElement().tap();
			Thread.sleep(1000);
			csvPacks = arrData.get(tabLists[i] + "Packs").split("@");
			for (int j = 0; j < csvPacks.length; j++) {
				checkFlag = false;
				eachPackPath = "//android.widget.TextView[@resource-id=\"in.ireff.android:id/price\"][@text='"
						+ csvPacks[j]
						+ "']//parent::android.widget.LinearLayout//parent::android.widget.LinearLayout//parent::android.widget.LinearLayout//parent::android.widget.LinearLayout[@resource-id=\"in.ireff.android:id/priceValidityParentLayout\"]/following-sibling::android.widget.LinearLayout/android.widget.LinearLayout[1]//android.widget.TextView";
				eachPack = elem.xpath(eachPackPath).makeUiElement().getElements();
				for (WebElement eachElem : eachPack) {
					if (tabLists[i].equalsIgnoreCase("Topup")) {
						if (eachElem.getText().equalsIgnoreCase(tabLists[i])
								|| eachElem.getText().equalsIgnoreCase("FULL TT")) {
							checkFlag = true;
							break;
						}
					} else {
						if (eachElem.getText().toLowerCase().contains(tabLists[i].toLowerCase())) {
							checkFlag = true;
							break;
						}
					}
				}
				if (checkFlag)
					System.out.println("Passed: " + csvPacks[j]);
				else
					Assert.fail("Rs. " + csvPacks[j] + " pack is missing the category");
				if (!isScrollEnds) {
					isScrollEnds = elem.xpath(eachPackPath).makeUiElement()
							.scrollUpTillDisappears("//android.widget.ListView[@resource-id=\"android:id/list\"]");
				}
			}
		}
	}

	/**
	 * Method is used to validate the packs details
	 * 
	 * @author SuryaRay
	 */
	public void validPacksData(String supportCSV) throws Exception {
		arrData = csvObj.readCsvData(supportCSV);
		elem.resourceId("in.ireff.android:id/skipTextView").makeUiElement().tap();
		elem.resourceId("in.ireff.android:id/imageView1").makeUiElement().tap();
		elem.resourceId("in.ireff.android:id/imageView" + arrData.get("Operator")).makeUiElement().tap();
		elem.resourceId("in.ireff.android:id/textView").text("Tamil Nadu").makeUiElement().tap();
		elem.resourceId("android:id/text1").text(arrData.get("PackSession")).makeUiElement().tap();
		
		String validityPath = "//android.widget.TextView[@resource-id=\"in.ireff.android:id/price\"][@text=\""
				+ arrData.get("Pack")
				+ "\"]//parent::android.widget.LinearLayout//parent::android.widget.LinearLayout//following-sibling::android.widget.LinearLayout//android.widget.TextView[@text=\""
				+ arrData.get("Validity") + " days\"]";
		asrt.assertTrue(elem.xpath(validityPath).makeUiElement().isExist());
		String percent = elem.xpath("//android.widget.TextView[@resource-id=\"in.ireff.android:id/price\"][@text=\""
				+ arrData.get("Pack")
				+ "\"]//parent::android.widget.LinearLayout//parent::android.widget.LinearLayout//parent::android.widget.LinearLayout//parent::android.widget.LinearLayout/following-sibling::android.widget.LinearLayout//android.widget.TextView[@resource-id=\"in.ireff.android:id/valueIndicator\"]")
				.makeUiElement().getText();
		String talkTime = elem.xpath("//android.widget.TextView[@resource-id=\"in.ireff.android:id/price\"][@text=\""
				+ arrData.get("Pack")
				+ "\"]//parent::android.widget.LinearLayout//parent::android.widget.LinearLayout//parent::android.widget.LinearLayout//parent::android.widget.LinearLayout/following-sibling::android.widget.LinearLayout//android.widget.TextView[@resource-id=\"in.ireff.android:id/talktime\"]")
				.makeUiElement().getText();
		String detailsPath = "//android.widget.TextView[@resource-id=\"in.ireff.android:id/price\"][@text=\""+arrData.get("Pack")+"\"]//parent::android.widget.LinearLayout//parent::android.widget.LinearLayout//parent::android.widget.LinearLayout//parent::android.widget.LinearLayout/following-sibling::android.widget.LinearLayout//android.widget.TextView[@resource-id=\"in.ireff.android:id/desc\"]"; 
		
		asrt.assertTrue(percent.equals(arrData.get("Percentage")));
		System.out.println("Percentage check Passed");
		int packPrice = Integer.parseInt(arrData.get("Pack"));
		double percentage = Double.parseDouble(arrData.get("Percentage").replaceAll("%", ""));
		double calcTalktime = packPrice * (percentage / 100);
		asrt.assertTrue( talkTime.equals((int) Math.ceil(calcTalktime) + " Talktime"));
		System.out.println("Talktime check Passed");
		String csvDetail1 = arrData.get("PackDetails").split("@")[0];
		String packDetails = elem.xpath(detailsPath).makeUiElement().getText();
		asrt.assertTrue( packDetails.contains(csvDetail1));
		System.out.println("Pack Details check passed");
		
		elem.xpath(detailsPath).makeUiElement().tap();
		String uiPackPrice = elem.resourceId("in.ireff.android:id/price").makeUiElement().getText();
		asrt.assertTrue(uiPackPrice.equals(arrData.get("Pack")));
		System.out.println("Inside Pack Price check passed");
		String uiValidity = elem.resourceId("in.ireff.android:id/validity").makeUiElement().getText();
		asrt.assertTrue(uiValidity.equals(arrData.get("Validity")+" days"));
		System.out.println("Inside Pack validity check passed");
		String uiTalktime = elem.resourceId("in.ireff.android:id/talktime").makeUiElement().getText();
		asrt.assertTrue(uiTalktime.equals((int) Math.ceil(calcTalktime) + " Talktime"));
		System.out.println("Inside Pack talktime check passed");
		String uiPercent = elem.resourceId("in.ireff.android:id/valueIndicator").makeUiElement().getText();
		asrt.assertTrue(uiPercent.equals(arrData.get("Percentage")));
		System.out.println("Inside Pack percentage check passed");
		String uiDetailsPath = "//android.widget.LinearLayout[@resource-id=\"in.ireff.android:id/detailItemsLayout\"]/android.widget.TextView[@resource-id=\"in.ireff.android:id/detailEntry\"][3]";
		String uiDetails = elem.xpath(uiDetailsPath).makeUiElement().getText();
		String csvDetail2 = arrData.get("PackDetails").split("@")[1];
		csvDetail2 = csvDetail2.replaceAll("<rupees>", String.valueOf(Math.ceil(calcTalktime)));
		System.out.println(uiDetails);
		System.out.println(csvDetail2);
		asrt.assertTrue(uiDetails.equals(csvDetail2)); 
		System.out.println("Inside Pack details check passed");
	}

}
