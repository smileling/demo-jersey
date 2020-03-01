package com.example.demojersey.services;

import com.example.demojersey.bean.User;
import com.example.demojersey.exception.DemoError;
import com.example.demojersey.exception.ExceptionUtil;
import com.example.demojersey.utils.CSVProcessorUtil;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

@Service
@Path("/user")
public class UserService {

    //TODO use swagger to generate apis

    public static String CSVFILE = "users.csv"; //TODO read from property file

    @GET
    @Produces("application/json")
    @Path("/first")
    public Response getFirstUser(@QueryParam("file") String file) {
        try {
            if (file == null || file.equals("")) {
                file = CSVFILE;
            }

            User user = CSVProcessorUtil.fetchFirstUserFromCSV(file);
            if(user == null) {
                return Response.status(Response.Status.OK).entity("{}").build();
            }
            return Response.status(Response.Status.OK).entity(user.toJson()).build();
        } catch (Exception e) {
            return ExceptionUtil.toResponse(e);
//            return ExceptionUtil.toResponse(new SimpleException("Query failed, please retry.", DemoError.REQUEST_ERROR, new NullPointerException().getStackTrace()[0].toString()));
//            return Response.status(Response.Status.BAD_REQUEST).entity(ExceptionUtils.getStackTrace(e).toString()).build();
        }
    }

    @GET
    @Produces("application/json")
    @Path("/{name}")
    public Response getUser(@PathParam("name") @NotNull String name) {
        try {
            User user = CSVProcessorUtil.fetchUserFromCSV(name, CSVFILE);
            if (user == null) {
                return Response.status(Response.Status.OK).entity("{}").build();
            }
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

            if (file == null || file.equals("")) {
                file = CSVFILE;
            }

            User user = CSVProcessorUtil.fetchUserFromCSV(name, file);
            if (user == null) {
                return Response.status(Response.Status.OK).entity("{}").build();
            }

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
            return Response.status(Response.Status.OK).entity(CSVProcessorUtil.fetchAllCSVFiles()).build();
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
            CSVProcessorUtil.uploadSingleCSVFile(fileInputStream, fileMetaData);
            return Response.status(Response.Status.OK).entity("{}").build();
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
