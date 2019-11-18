package com.gb.fineos.page;

import com.gb.fineos.domain.TestCase;
import com.gb.fineos.page.LoginPage.LoginPageRequest;
import com.relevantcodes.extentreports.ExtentTest;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertEquals;

public class LoginPageRequestTest {

    @Test
    public void testGetters() {
        final Map<String, String> data = new HashMap<>();
        data.put("UserName", "John_Smith");
        data.put("Password", "Password10");

        final TestCase testCase = new TestCase("LoginPageRequestTest", "LoginPageRequestTestDesc", data);
        final ExtentTest extentTest = new ExtentTest(testCase.getName(), testCase.getDesc());

        final LoginPageRequest loginPageRequest = new LoginPageRequest(testCase, extentTest);

        assertEquals(loginPageRequest.getUsername(), "John_Smith");
        assertEquals(loginPageRequest.getPassword(), "Password10");
    }
}
