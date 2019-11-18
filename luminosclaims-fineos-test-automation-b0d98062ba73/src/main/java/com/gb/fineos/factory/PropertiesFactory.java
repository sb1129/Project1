package com.gb.fineos.factory;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

public class PropertiesFactory {
    private static final Logger LOG = Logger.getLogger(PropertiesFactory.class);

    private static final PropertiesFactory instance = new PropertiesFactory();

    private Properties properties;

    private PropertiesFactory() {
        properties = new Properties();

        try {
            LOG.info("Loading properties file: config.properties");
            properties.load(ClassLoader.getSystemResourceAsStream("config.properties"));

            if (ClassLoader.getSystemResource("configOverride.properties") != null) {
                LOG.info("Loading properties file: configOverride.properties");
                properties.load(ClassLoader.getSystemResourceAsStream("configOverride.properties"));
            }
        } catch (IOException e) {
            LOG.error("Unable to load properties file.", e);
        }
    }

    public static PropertiesFactory getInstance() {
        return instance;
    }

    public Properties getProperties() {
        return properties;
    }
}
