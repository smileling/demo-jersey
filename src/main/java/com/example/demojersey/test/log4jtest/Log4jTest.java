package com.example.demojersey.test.log4jtest;

import com.example.demojersey.exception.UserSvcError;
import com.example.demojersey.exception.UserSvcException;
import org.apache.log4j.Logger;

public class Log4jTest {
    final static Logger logger = Logger.getLogger(Log4jTest.class);
    public String a;
    public int num;

    public Exception getException() {
        return new UserSvcException("Request Error", UserSvcError.INTERNAL_SERCICE_ERROR);
    }

    public Log4jTest(String a, int num) {
        this.a = a;
        this.num = num;
    }
    public void fun() {
        if(logger.isDebugEnabled()) {
            logger.debug("This is debug");
        }

        if(logger.isInfoEnabled()) {
            logger.info("This is info");
        }

        logger.warn("This is warn");

        logger.error("This is error : " + this.a);

        logger.error("This is error", getException());

        logger.fatal("This is fatal");

    }

    public static void main(String[] args) {
        new Log4jTest("Hello", 1).fun();
    }
}
