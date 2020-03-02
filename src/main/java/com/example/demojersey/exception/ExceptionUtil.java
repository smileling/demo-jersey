package com.example.demojersey.exception;

import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.ws.rs.core.Response;

public class ExceptionUtil {
    public static void checkArgument(boolean expression,
                                     String errorMsg,
                                     UserSvcError userSvcError) {
        if(expression) {
            throw new UserSvcException(errorMsg, userSvcError);
        }
    }

    public static Response toResponse(Throwable exception) {
        if (exception instanceof UserSvcException) {
            UserSvcException se = (UserSvcException) exception;
            return Response.status(se.getReturnCode()).entity(se.getEntity()).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ExceptionUtils.getStackTrace(exception)).build();
    }
}
