package com.example.demojersey.services;

import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Service
@Path("/hello")
public class HelloService {

    @GET
    @Path("/hello1")
    @Produces("text/plain")
    public String hello() {
        return "Hello from Spring";
    }

    @GET
    @Path("/hello2")
    @Produces("application/json")
    public Response reverse(@QueryParam("data") String data) {
        return Response.status(Response.Status.OK).entity("hello").build();
    }
}
