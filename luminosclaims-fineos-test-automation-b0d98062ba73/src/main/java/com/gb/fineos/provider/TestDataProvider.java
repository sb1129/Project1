package com.gb.fineos.provider;

import com.gb.fineos.util.XlsReader;
import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Class Name : TestDataProvider Class
 * Description : Used to declare & implement methods to read the Test Data from Test SUITE files
 */
public class TestDataProvider {
	private static final String TEST_CASE_DATA_SI_ENV_XLSX = "test_case/TestCaseData_SI_Env.xlsx";

	private static final String TESTDATA_LOGIN_SHEET = "Login";
	private static final String TESTDATA_NOTIFICATION_SHEET = "notification";
	private static final String TESTDATA_ADDPERSON_SHEET ="AddPerson";

	private TestDataProvider() {
		// Do nothing
	}

	/**
	 * Method Name : getDataFromLoginSheet()
	 * Description : Used to read the data from LOGIN sheet
	 */
	@DataProvider(name = "loginDataProvider")
	public static Object[][] getDataFromLoginSheet(final Method m) {
		final XlsReader xlsSuiteA = new XlsReader(TEST_CASE_DATA_SI_ENV_XLSX);
		return getData(m.getName(), xlsSuiteA, TESTDATA_LOGIN_SHEET);
	}

	/**
	 * Method Name : getDataFromNotificationSheet()
	 * Description : Used to read the data from Notification Claim sheet
	 */
	@DataProvider(name = "NotificationProvider")
	public static Object[][] getDataFromNotificationSheet(final Method m) {
		final XlsReader xlsSuiteA = new XlsReader(TEST_CASE_DATA_SI_ENV_XLSX);
		return getData(m.getName(), xlsSuiteA, TESTDATA_NOTIFICATION_SHEET);
	}

	/**
	 * Method Name : getDataFromAddPerson()
	 * Description : Used to read the data from Professional Claim sheet
	 */
	@DataProvider(name = "AddPerson")
	public static Object[][] getDataFromAddPerson(final Method m) {
		final XlsReader xlsSuiteA = new XlsReader(TEST_CASE_DATA_SI_ENV_XLSX);
		return getData(m.getName(), xlsSuiteA, TESTDATA_ADDPERSON_SHEET);
	}

	private static Object[][] getData(String testName, XlsReader xls, String sheetName) {
		final int rows = xls.getRowCount(sheetName);
		// Row number for test case
		int testCaseRowNumber = 1;
		for (testCaseRowNumber = 1; testCaseRowNumber <= rows; testCaseRowNumber++) {
			String testNameXls = xls.getCellData(sheetName, 0, testCaseRowNumber);
			if (testNameXls.equalsIgnoreCase(testName)) {
				break;
			}
		}

		int dataStartRowNumber = testCaseRowNumber + 2;
		int colStartRowNumber = testCaseRowNumber + 1;
		// Total number of rows in Test
		int totalRows = 0;
		while (!xls.getCellData(sheetName, 0, dataStartRowNumber + totalRows).equals("")) {
			totalRows++;
		}

		// Total number of rows in Test
		int totalCols = 1;
		while (!xls.getCellData(sheetName, totalCols, colStartRowNumber).equals("")) {
			totalCols++;
		}

		final Object[][] data = new Object[totalRows][1];
		int r = 0;
		// To read data under specific tests
		for (int rNum = dataStartRowNumber; rNum < (dataStartRowNumber + totalRows); rNum++) {
			final Map<String, String> map = new HashMap<>();
			for (int cNum = 0; cNum < totalCols; cNum++) {
				map.put(xls.getCellData(sheetName, cNum, colStartRowNumber), xls.getCellData(sheetName, cNum, rNum));
			}
			data[r][0] = map;
			r++;
		}
		return data;
	}
}
