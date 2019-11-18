package com.gb.fineos.domain;

import com.gb.fineos.factory.PropertiesFactory;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Abstract Page Request domain object. Contains useful methods for testing of pages.
 *
 * Extend this class in a specific 'page' implementation (e.g. com.gb.fineos.page.LoginPage.LoginPageRequest).
 */
public abstract class PageRequest {
    private final TestCase testCase;
    private final ExtentTest extentTest;
    private final boolean captureExtentLog;

    public PageRequest(final TestCase testCase, final ExtentTest extentTest) {
        this.testCase = testCase;
        this.extentTest = extentTest;
        this.captureExtentLog = Boolean.parseBoolean(PropertiesFactory.getInstance().getProperties().getProperty("captureExtentLog"));
    }

    protected String get(final String key) {
        return testCase.getData().get(key);
    }

    public void log(final String action, final String message) {
        if (captureExtentLog) {
            extentTest.log(LogStatus.INFO, action, message);
        }
    }

    public void error(final String action, final Exception exception) {
        if (captureExtentLog) {
            extentTest.log(LogStatus.ERROR, action, exception);
        }
    }
}
