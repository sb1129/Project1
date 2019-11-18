package com.gb.fineos.factory;

import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

public class PropertiesFactoryTest {

    @Test
    public void testGetInstance() {
        assertNotNull(PropertiesFactory.getInstance().getProperties());
    }
}
