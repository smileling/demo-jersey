package com.example.demojersey.test.exception;

import javax.ws.rs.core.Response;

public class ExceptionTest extends RuntimeException {
    static void f() {
        try {
            throw new Exception();
        } catch (Exception e) {
            e.printStackTrace();
            for (StackTraceElement ste: e.getStackTrace())
                System.out.println(ste.getMethodName());
        }
    }

    static void g() { f(); }
    static void h() { g(); }
    public static void main(String[] args) {
        f();
        System.out.println("--------------------");
        g();
        System.out.println("--------------------");
        h();
    }
}
