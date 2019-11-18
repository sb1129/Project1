package com.gb.fineos.script;

import com.gb.fineos.domain.PageRequest;
import com.gb.fineos.domain.TestCase;
import com.gb.fineos.page.LoginPage;
import com.gb.fineos.page.LoginPage.LoginPageRequest;
import com.gb.fineos.provider.TestDataProvider;
import org.openqa.selenium.support.PageFactory;
import org.testng.SkipException;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.fail;

/* Class Name : LoginToApplication 
 * Description :Flow for logging into the application
 */
public class LoginTest extends BaseTest {

	public LoginTest() {
		super("LOGIN");
	}

	/*
	 * Method Name : loginTest
	 * Description : Flow for logging into the application
	 */
	@Test(dataProviderClass = TestDataProvider.class, dataProvider = "loginDataProvider", priority = 0)
	public void loginTest(final Map<String, String> testData) {
		final TestCase testCase = new TestCase("loginTest", "LOGIN INTO APPLICATION", testData);

		try {
			start(testCase);

			final LoginPage loginPage = PageFactory.initElements(getDriver(), LoginPage.class);
			loginPage.doLogin(new LoginPageRequest(testCase, getExtentTest()));
			waitForPageToLoad(5000L);

			pass(testCase, "Logged in successfully...");
		} catch (SkipException e) {
			skip(testCase);
		} catch (Exception e) {
			fail(testCase, e);
		} finally {
			end();
		}
	}
}
