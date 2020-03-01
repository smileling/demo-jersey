package com.example.demojersey.utils;

import com.example.demojersey.bean.User;
import com.example.demojersey.exception.DemoError;
import com.example.demojersey.exception.ExceptionUtil;
import com.google.gson.Gson;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.apache.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public abstract class CSVProcessorUtil {
//    @Value("${csv.root.path}")
//    public static String FILEROOTPATH;
    public static final Logger logger = Logger.getLogger(CSVProcessorUtil.class);

    public static String getDataPath() {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream("src/main/resources/config/config.properties"));
            return prop.getProperty("csv.files.path");
        } catch (Exception e) {
            logger.warn("Read property file failed, and will use default configurations.");
            return "/tmp/demo/";
        }
    }

    /***
     *  fetch all files under specified path
     * @return
     */
    public static List<String> fetchAllCSVFiles() {
        try {
            File dir = new File(getDataPath());
            List<String> fileList = new ArrayList<String>();
            for (File file : dir.listFiles()) {
                String fileName = file.getName();
                if (file.isFile() && fileName.endsWith(".csv"))
                    fileList.add(fileName);
            }

            if(logger.isInfoEnabled()) {
                logger.info(fileList.toString());
            }

            return fileList;
        } catch (Exception e) {
            throw e;
//            throw new SimpleException("upload file failed.", DemoError.INTERNAL_SERCICE_ERROR, e.getStackTrace()[0].toString());
        }
    }

    /**
     * upload one file to specified path
     * @param fileInputStream
     * @param fileMetaData
     *
     */
    public static void uploadSingleCSVFile(InputStream fileInputStream, FormDataContentDisposition fileMetaData) throws IOException {
        int read = 0;
        byte[] bytes = new byte[1024];

        try {
            String file = fileMetaData.getFileName();
            OutputStream out = new FileOutputStream(new File(getDataPath() + file));
            while ((read = fileInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();

            if(logger.isInfoEnabled()) {
                logger.info("Successfully upload file " + file + " to " + getDataPath());
            }
        } catch(IOException e) {
            throw e;
//            throw new SimpleException("upload file failed.", DemoError.FILE_UPLOAD_FAILED, e.getStackTrace()[0].toString());
        }
    }

//    /**
//     * upload one file to specified path via MultipartFile.
//     * @param file
//     * @return
//     */
//    public static void uploadSingleCSVFile(MultipartFile file) throws IOException {
//        String fileName = file.getOriginalFilename();
//        String filePath = getDataPath();
//        File dest = new File(filePath+fileName);
//
//        try {
//            file.transferTo(dest);
//            if(logger.isInfoEnabled()) {
//                logger.info("upload file to " + dest + "successfully!");
//            }
//
//        } catch (IOException e) {
//            throw e;
////            throw new SimpleException("upload file failed.", DemoError.INTERNAL_SERCICE_ERROR, e.getStackTrace()[0].toString());
//        }
//    }

//    /**
//     * add multiple files to specified path at once.
//     * @param files
//     * @return
//     */
//    public static void uploadMultiFile(List<MultipartFile> files) {
//        String filePath = getDataPath();
//        if(logger.isInfoEnabled()) {
//            logger.info(files.size() + " will be uploaded.");
//        }
//        for (int i = 0; i < files.size(); i++) {
//            MultipartFile file = files.get(i);
//            if (file.isEmpty()) {
//                logger.warn("upload the " + (i++) + " file failed."); //TODO
//            }
//            String fileName = file.getOriginalFilename();
//            File dest = new File(filePath+fileName);
//            try {
//                file.transferTo(dest);
//                if(logger.isInfoEnabled()) {
//                    logger.info("upload the " + (i + 1) +" file to " + dest + "successfully!");
//                }
//            } catch (IOException e) {
////                e.printStackTrace();
//                logger.error("upload the " + (i++) + " file failed.");
//                //TODO throw 403 error for part uploads failed?
//            }
//        }
//
//    }

    //TODO fetchUserFromDB

    //TODO updateUserToDB

    public static User fetchUserFromCSV(String userName, String csvFile) throws IOException {
        String srcPath = getDataPath() + csvFile;
        String charset = "utf-8";
        File file = new File(srcPath);

        ExceptionUtil.checkArgument(!file.exists(), file + " doesn't exist.", DemoError.File_NOT_FOUND);

        try {
            CSVReader csvReader = new CSVReaderBuilder(new BufferedReader(new InputStreamReader(new FileInputStream(new File(srcPath)), charset))).build();
            String[] user;
            user = csvReader.readNext();
            while(user != null) {
                if (user[0].equals(userName)) {
                    User u = new User();
                    String[] userFields = u.getClassFields();
                    int fieldLen = userFields.length;

                    String userStr = "";
                    int fieldNum = user.length;
                    for(int i = 0; i < fieldNum && i < fieldLen; i++) {
                        if (i < fieldNum - 1) {
                            userStr = userStr + userFields[i] + ":" + user[i] + ",";
                        } else {
                            userStr = userStr + userFields[i] + ":" + user[i];
                        }
                    }
                    userStr = "{" + userStr + "}";
                    if(logger.isInfoEnabled()) {
                        logger.info("Find user: " + userName + ". User info: " + userStr);
                    }
                    Gson gson = new Gson();
                    return gson.fromJson(userStr, User.class);
                }
                user = csvReader.readNext();
            }
        } catch (IOException e) {
            throw e;
//            throw new SimpleException("Query failed, please retry.", DemoError.INTERNAL_SERCICE_ERROR, e.getStackTrace()[0].toString());
        }

        return null;
    }

    public static User fetchFirstUserFromCSV(String csvFile) throws IOException {
        String srcPath = getDataPath() + csvFile;
        File file = new File(srcPath);

        ExceptionUtil.checkArgument(!file.exists(), file + " doesn't exist.", DemoError.File_NOT_FOUND);

        String charset = "utf-8";
        try {
            CSVReader csvReader = new CSVReaderBuilder(new BufferedReader(new InputStreamReader(new FileInputStream(new File(srcPath)), charset))).build();
            String[] user;
            user = csvReader.readNext();
            if (user != null) {
//                String[] userFields = {"name", "age", "emailAddress"};
                User u = new User();
                String[] userFields = u.getClassFields();
                int fieldLen = userFields.length;

                String userStr = "";
                int fieldNum = user.length;
                for(int i = 0; i < fieldNum && i < fieldLen; i++) {
                    if (i < fieldNum - 1) {
                        userStr = userStr + userFields[i] + ":" + user[i] + ",";
                    } else {
                        userStr = userStr + userFields[i] + ":" + user[i];
                    }
                }
                userStr = "{" + userStr + "}";
                if(logger.isInfoEnabled()) {
                    logger.info("First user: " + user[0] + ". User info: " + userStr);
                }
                Gson gson = new Gson();
                return gson.fromJson(userStr, User.class);
            }
        } catch (IOException e) {
            throw e;
//            throw new SimpleException("Query failed, please retry.", DemoError.INTERNAL_SERCICE_ERROR, e.getStackTrace()[0].toString());
        }
        return null;
    }

    public static void main(String[] args) {
//        String srcPath = userPathDir + "users.csv";
//        String charset = "utf-8";
//        try {
//            CSVReader csvReader = new CSVReaderBuilder(new BufferedReader(new InputStreamReader(new FileInputStream(new File(srcPath)), charset))).build();
//            Iterator<String[]> iterator = csvReader.iterator();
//            while (iterator.hasNext()) {
//                Arrays.stream(iterator.next()).forEach(System.out::print);
//                System.out.println();
//            }
//            List<String[]> list = new ArrayList<String[]>();
//            list = csvReader.readAll();
//            System.out.println(list.get(0)[0]);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        File dir = new File(getDataPath());
        List<String> fileList = new ArrayList<String>();
        for(File file: dir.listFiles()) {
            String fileName = file.getName();
            if(file.isFile() && fileName.endsWith(".csv"))
                fileList.add(fileName);
            System.out.println(file.toString());
        }
        System.out.println(fileList.toString());
    }
}
