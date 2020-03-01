package com.example.demojersey;

import com.example.demojersey.services.HelloService;
import com.example.demojersey.services.ReverseService;
import com.example.demojersey.services.UserService;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.Path;

@Configuration
//@Path("/")
public class ApplicationConfig extends ResourceConfig {

    public ApplicationConfig() {

        register(HelloService.class);
        register(ReverseService.class);
        register(UserService.class);
        register(MultiPartFeature.class);
    }
}