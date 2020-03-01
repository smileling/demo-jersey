package com.example.demojersey.services;

import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;

@Service
@Path("/reverse")
public class ReverseService {

    @GET
//    @Produces("text/plain")
    @Produces("application/json")
    public String reverse(@QueryParam("data") @NotNull String data) {
        return new StringBuilder(data).reverse().toString();
    }

    @GET
//    @Produces("text/plain")
    @Produces("application/json")
    @Path("/{data}")
    public String reverse2(@PathParam("data") @NotNull String data) {
        return new StringBuilder(data).reverse().toString();
    }
}
