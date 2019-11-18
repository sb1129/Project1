package com.gb.fineos.page;

import com.gb.fineos.factory.PropertiesFactory;
import org.apache.log4j.Logger;
import org.codehaus.plexus.util.StringUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Properties;

public abstract class BasePage {
    private static final Logger LOG = Logger.getLogger(BasePage.class);

    private final WebDriver driver;
    private Properties config;

    public BasePage(final WebDriver driver) {
        this.driver = driver;
        this.config = PropertiesFactory.getInstance().getProperties();
    }

    protected WebDriver getDriver() {
        return driver;
    }

    protected String getProperty(final String key) {
        return config.getProperty(key);
    }

    protected void input(final String value, final WebElement webElement) {
        try {
            if (StringUtils.isNotBlank(value)) {
                waitForElementPresent(webElement);
                highlightWebElement(webElement);
                webElement.clear();
                webElement.sendKeys(value);
            }
        } catch (NoSuchElementException e) {
            handleNoSuchElementException(webElement, e);
        }
    }

    protected void click(final WebElement webElement) {
        try {
            waitForElementPresent(webElement);
            highlightWebElement(webElement);
            webElement.click();
        } catch (NoSuchElementException e) {
            handleNoSuchElementException(webElement, e);
        }
    }

    protected void waitForElementPresent(final WebElement xpath) {
        final WebDriverWait wait = new WebDriverWait(driver, 100);
        xpath.isDisplayed();
        wait.until(ExpectedConditions.visibilityOf(xpath));
    }

    protected void highlightWebElement(final WebElement xpath) {
        if (getProperty("isHighlight").equalsIgnoreCase(String.valueOf(true))) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('style', arguments[1]);", xpath, "border: 4px solid yellow;");
            ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('style', arguments[1]);", xpath, "");
        }
    }

    protected void handleNoSuchElementException(final WebElement webElement, final NoSuchElementException exception) {
        LOG.error("Unable to identify web element: " + webElement, exception);
        throw exception;
    }
}
