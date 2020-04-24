package com.example.demojersey.common;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class UserSvcConfigBean {
    private static final Logger logger = Logger.getLogger(UserSvcConfigBean.class);
    private static UserSvcConfigBean userSvcConfigBean = new UserSvcConfigBean();
    private Properties commonConfigProperties = null;

    private UserSvcConfigBean() {
//        try {
////            InputStream in = UserSvcConfigBean.class.getResourceAsStream("/config/config.properties");
////            FileInputStream fin = (FileInputStream)in;
////            System.out.println("Path: " + UserSvcConfigBean.class.getResource("/config/config.properties").getPath());
//
//            logger.info("Path: " + UserSvcConfigBean.class.getResource("/config/config.properties").getPath());
//            commonConfigProperties = getConfigMapFromFile( UserSvcConfigBean.class.getResource("/config/config.properties").getPath());
//        } catch (FileNotFoundException e) {
//            logger.error(e.getMessage(), e);
//        } catch (IOException e) {
//            logger.error(e.getMessage(), e);
//        }
    }

    public static UserSvcConfigBean getInstance( ) {
        return userSvcConfigBean;
    }

    public String getCSVRootFilePath() {
//        return commonConfigProperties.getProperty("csv.files.path");
//        return "/root/demo/";
        return "/tmp/demo/";
    }

    public static Properties getConfigMapFromFile(String propertiesFile) throws IOException {
        Properties prop = new Properties();
        prop.load(new FileInputStream(propertiesFile));
        return prop;
    }
}
