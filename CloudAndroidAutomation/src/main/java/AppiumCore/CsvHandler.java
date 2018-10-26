package AppiumCore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.testng.asserts.SoftAssert;

public class CsvHandler {

	private static SoftAssert asrt = new SoftAssert();
	URI filePath;
	/**
	 * Method is used to read the csv file and map the values to a mapping object
	 * 
	 * @param csvName Name of the CSV
	 * 
	 * @author SuryaRay
	 */
	public Map<String, String> readCsvData(String csvName) throws Exception {
		Map<String, String> tabArray = new HashMap<String, String>();
		filePath = getFilepath(csvName);
		tabArray = getCSVData(filePath);
		return tabArray;
	}

	/**
	 * Method is used to get the csv file path in the URI format
	 * 
	 * @param csvName Name of the CSV
	 * 
	 * @author SuryaRay
	 */
	public URI getFilepath(String csvName) throws Exception {
		URI uri;
		uri = (URI) (new File("src/test/resources/InputCsvs/" + csvName).exists()
				? new File("src/test/resources/InputCsvs/" + csvName)
				: null).toURI();
		System.out.println("Initialized filepath==" + uri);
		if (uri == null)
			assert uri != null : "Input CSV file missing";
		return uri;
	}

	/**
	 * Method is used to break the csv line and map the values to the map object
	 * 
	 * @param filePath CSV file path in URI format
	 * 
	 * @author SuryaRay
	 */
	public Map<String, String> getCSVData(URI filePath) {
		Map<String, String> tabArray = new HashMap<String, String>();
		try {
			File file = new File(filePath);
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line1 = br.readLine();
			String line2 = br.readLine();
			br.close();
			String[] headers = line1.split(";", -1);
			String[] values = line2.split(";", -1);
			asrt.assertTrue(headers.length == values.length);
			for (int i = 0; i < headers.length; i++) {
				tabArray.put(headers[i], values[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tabArray;
	}

	/**
	 * Method is used to set the values to any column in the csv file
	 * 
	 * @param csvName Name of the CSV
	 * @param clmnName Name of the column to which data to be updated
	 * @param value value to be added to the column
	 * 
	 * @author SuryaRay
	 */
	public void setCsvData(String csvName, String clmnName, String value) {
		try {
			String headers = "";
			String values = "";
			// for same order of csv columns @last
			filePath = getFilepath(csvName);
			File file = new File(filePath);
			BufferedReader br = new BufferedReader(new FileReader(file));
			String[] keysArray = br.readLine().split(";", -1);
			br.close();
			// replace the value in the column
			Map<String, String> arrData = readCsvData(csvName);
			asrt.assertTrue(arrData.containsKey(clmnName));
			arrData.replace(clmnName, value);
			// get ready the data to write in csv
			for (int i = 0; i < keysArray.length; i++) {
				headers = headers + keysArray[i];
				values = values + arrData.get(keysArray[i]);
				if (i < keysArray.length - 1) {
					headers = headers + ";";
					values = values + ";";
				}
			}
			// write data to csv
			PrintWriter pWrite = new PrintWriter(new FileWriter(file));
			pWrite.println(headers);
			pWrite.print(values);
			pWrite.close();
			for (int i = 0; i < keysArray.length; i++) {
				System.out.println("Head/Value: " + keysArray[i] + " | " + arrData.get(keysArray[i]));
			}
			Thread.sleep(3000);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
