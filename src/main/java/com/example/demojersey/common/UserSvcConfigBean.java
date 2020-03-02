package com.example.demojersey.common;

import com.example.demojersey.utils.CSVProcessorUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class UserSvcConfigBean {
    private static UserSvcConfigBean userSvcConfigBean = new UserSvcConfigBean();
    private Properties commonConfigProperties = null;
    private static final Logger logger = Logger.getLogger(UserSvcConfigBean.class);

    private UserSvcConfigBean() {
        try {
            commonConfigProperties = getConfigMapFromFile("src/main/resources/config/config.properties");
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static UserSvcConfigBean getInstance( ) {
        return userSvcConfigBean;
    }

    public String getPropertyByName(String propertyName) {
        return commonConfigProperties.getProperty(propertyName);
    }

    public String getCSVRootFilePath() {
        return getPropertyByName("csv.files.path");
    }

    public static Properties getConfigMapFromFile(String propertiesFile) throws IOException {
        Properties prop = new Properties();
        prop.load(new FileInputStream(propertiesFile));
        return prop;
    }
}
