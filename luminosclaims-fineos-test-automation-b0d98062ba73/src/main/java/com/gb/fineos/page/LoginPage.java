package com.gb.fineos.page;

import com.gb.fineos.domain.PageRequest;
import com.gb.fineos.domain.TestCase;
import com.relevantcodes.extentreports.ExtentTest;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Class Name : LoginPage
 * Description : Contains Initializing WebElements, Methods to perform actions on Web Page
 */
public class LoginPage extends BasePage {
	private static final Logger LOG = Logger.getLogger(LoginPage.class);

	private static final String ACTION_LOGIN_PAGE = "LOGIN PAGE";

	//Initialize web element for  Logo field
	@FindBy(id="fineos_login_fineoslogo")
	private WebElement fineosLogo;

	//Initialize web element for User Name text field
	@FindBy(id="fineos_logindialog_nameinput_cell")
	private WebElement username;
	//Initialize web element for Password text field
	@FindBy(id="fineos_logindialog_nameinput_cell")
	private WebElement password;
	//Initialize web element for login button
	@FindBy(id="login")
	private WebElement btnLogin;
	//Initialize web element for Continue button
	@FindBy(xpath="//input[@name='ok']")
	private WebElement btnContinue;

	/**
	 * Method Name : LoginPage()
	 * Description : Parameterized Constructor - To initialize object
	 */
	public LoginPage(final WebDriver driver) {
		super(driver);
	}

	/**
	 * Method Name : doLogin()
	 * Description : To login into application
	 */
	public void doLogin(final LoginPageRequest pageRequest) {
		try {
			openApplication(pageRequest);
			getDriver().switchTo().frame(getDriver().findElement(By.id("login")));
			enterUsername(pageRequest);
			enterPassword(pageRequest);
			clickLogin(pageRequest);
			getDriver().switchTo().defaultContent();
			getDriver().switchTo().frame(getDriver().findElement(By.id("logininfo")));
			clickContinue(pageRequest);
		} catch (Exception e) {
			pageRequest.error(ACTION_LOGIN_PAGE, e);
			LOG.error("An error occurred when attempting to login.", e);
			throw new AssertionError(e.getMessage(), e);
		}
	}

	private void enterUsername(final LoginPageRequest pageRequest) {
		input(pageRequest.getUsername(), username);
		pageRequest.log(ACTION_LOGIN_PAGE, "Entered User Name:" + pageRequest.getUsername());
	}

	private void enterPassword(final LoginPageRequest pageRequest) {
		input(pageRequest.getPassword(), password);
		pageRequest.log(ACTION_LOGIN_PAGE, "Entered Password :" + pageRequest.getPassword());
	}

	private void clickLogin(final LoginPageRequest pageRequest) {
		click(btnLogin);
		pageRequest.log(ACTION_LOGIN_PAGE, "Clicked on Login Button");
	}

	private void clickContinue(final LoginPageRequest pageRequest) {
		click(btnContinue);
		pageRequest.log(ACTION_LOGIN_PAGE, "Clicked on Continue Button");
	}

	private void openApplication(final LoginPageRequest pageRequest) {
		try {
			getDriver().get(getProperty("Application_URL"));
			pageRequest.log("LAUNCH APPLICATION", "Navigating to Application: " + getProperty("Application_URL"));
		} catch (Exception e) {
			throw new AssertionError("***EXCEPTION OCCURRED WHILE LAUNCHING URL INTO BROWSER***");
		}
	}

	public static class LoginPageRequest extends PageRequest {

		public LoginPageRequest(final TestCase testCase, final ExtentTest extentTest) {
			super(testCase, extentTest);
		}

		public String getUsername() {
			return get("UserName");
		}

		public String getPassword() {
			return get("Password");
		}
	}
}
