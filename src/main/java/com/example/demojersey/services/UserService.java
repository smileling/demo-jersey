package com.example.demojersey.services;

import com.example.demojersey.bean.User;
import com.example.demojersey.exception.DemoError;
import com.example.demojersey.exception.ExceptionUtil;
import com.example.demojersey.utils.CSVProcessor;
import org.apache.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.List;

@Service
@Path("/user")
public class UserService {
//    final static Logger logger = Logger.getLogger(UserService.class);
    @GET
    @Produces("application/json")
    @Path("/first")
    public Response getFirstUser(@QueryParam("file") String file) {
        try {
//            User user = CSVProcessor.fetchFirstUser("users.csv");
            User user = CSVProcessor.fetchFirstUser(file);
            return Response.status(Response.Status.OK).entity(user.toJson()).build();
        } catch (Exception e) {
            return ExceptionUtil.toResponse(e);
        }
    }

    @GET
    @Produces("application/json")
    @Path("/{name}")
    public Response getUser(@PathParam("name") @NotNull String name) {
        try {
            User user = CSVProcessor.fetchUser(name, "users.csv");
            return Response.status(Response.Status.OK).entity(user.toJson()).build();
        } catch (Exception e) {
            return ExceptionUtil.toResponse(e);
        }
    }

    @GET
    @Produces("application/json")
    public Response getUserWithParams(@QueryParam("name") @NotNull String name,
                                 @QueryParam("file") String file) {
        try {
            ExceptionUtil.checkArgument(name == null || name.isEmpty(),
                    "invalid user name.",
                    DemoError.INVALID_USER_NAME);
            User user = CSVProcessor.fetchUser(name, file);
            return Response.status(Response.Status.OK).entity(user.toJson()).build();
        } catch (Exception e) {
            return ExceptionUtil.toResponse(e);
        }
    }

    @GET
    @Produces("application/json")
    @Path("/upload")
    public Response getAllUploadedFiles() {
        try {
            return Response.status(Response.Status.OK).entity(CSVProcessor.fetchAllCSVFiles()).build();
        } catch (Exception e) {
            return ExceptionUtil.toResponse(e);
        }
    }

    @POST
    @Path("/uploadfile")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response upload(@FormDataParam("file") InputStream fileInputStream,
                           @FormDataParam("file") FormDataContentDisposition fileMetaData) {
        try {
            ExceptionUtil.checkArgument(fileInputStream == null || fileMetaData == null,
                    "Invalid file. Please uploade valid file.",
                    DemoError.INVALID_FILE); //TODO improve -> check file format, etc.
            String res = CSVProcessor.uploadSingleFile(fileInputStream, fileMetaData);
            return Response.status(Response.Status.OK).entity(res).build();
        } catch (Exception e) {
            return ExceptionUtil.toResponse(e);
        }
    }

//    @POST
//    @Path("/multiUpload")
//    @Produces("application/json")
//    public String multiUpload(HttpServletRequest request) {
//        //TODO
//        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
//        return CSVProcessor.uploadMultiFile(files);
//    }
}
