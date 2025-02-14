package org.apache.clusterbr.zupportl5.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Get properties from: "application.properties" file.
 * 
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/PropertiesProvider_class.png" alt="UML CLASS Diagram" class="class"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
public class PropertiesProvider {
    
    private static final Logger logger = LoggerFactory.getLogger(PropertiesProvider.class);

    public static String DEV = "dev", DEVGCP = "devgcp", PROD = "prod";

    private static final String APPLICATION_PROPERTIES = "application.properties";

    private static PropertiesProvider instance;

    private Properties properties;

    private PropertiesProvider() {
        properties = new Properties();
        loadProperties();
    }

    private void loadProperties() {

        String msg;
        String methodName = "PropertiesProvider:loadProperties";
        
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(APPLICATION_PROPERTIES)) {

            if (input == null) {
                msg = String.format("[Error] (%s) Unable to find %s file.", methodName, APPLICATION_PROPERTIES);
                logger.error(msg);
                return;
            }
            properties.load(input);
            
            msg = String.format("[Info] (%s) Properties file <%s> loaded successfully.", methodName, APPLICATION_PROPERTIES);
            logger.info(msg);
            logger.info("(PropertiesProvider::loadProperties()) property.application.name=" + properties.getProperty("property.application.name"));

        } catch (IOException ex) {
            ex.printStackTrace();
            msg = String.format("[Exception] (%s)", methodName);
            logger.error(msg, ex);
        }
    }

    public static PropertiesProvider getInstance() {
        if (instance == null) {
            instance = new PropertiesProvider();
        }
        return instance;
    }

    
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getPropertyOrDefault(String key, String defaultValue) {
        String propValue = properties.getProperty(key);
        return (propValue != null) ? propValue : defaultValue;
    }    


    /**
     * valid profiles as defined in POM, are: [dev|devgcp|prod]
     * @return a word indicating active profile
     */
    public String getActiveProfile() {
        String profile;
        
        //--> String activeProfiles = System.getProperty("spring.profiles.active");
        String activeProfiles = System.getenv("SPRING_PROFILES_ACTIVE");
            
        if(activeProfiles != null && activeProfiles.contains(PROD)) {
            profile = PROD;            
        } else if(activeProfiles != null && activeProfiles.contains(DEVGCP)) {
            profile = DEVGCP;            
        } else if(activeProfiles != null && activeProfiles.contains(DEV)) {
            profile = DEV;            
        } else {
            profile = (activeProfiles != null) ? activeProfiles : DEV;
        }

        return profile;
    }
}
