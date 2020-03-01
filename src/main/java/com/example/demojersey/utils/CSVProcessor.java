package com.example.demojersey.utils;

import com.example.demojersey.bean.User;
import com.example.demojersey.exception.DemoError;
import com.example.demojersey.exception.SimpleException;
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

public class CSVProcessor {
    public static final String DATAPATH = getDataPath();
    final static Logger logger = Logger.getLogger(CSVProcessor.class);

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
    public static String fetchAllCSVFiles() {
        try {
            File dir = new File(DATAPATH);
            List<String> fileList = new ArrayList<String>();
            for (File file : dir.listFiles()) {
                String fileName = file.getName();
                if (file.isFile() && fileName.endsWith(".csv"))
                    fileList.add(fileName);
            }

            if(logger.isInfoEnabled()) {
                logger.info(fileList.toString());
            }

            return fileList.toString();
        } catch (Exception e) {
            logger.error("upload file failed.");
            throw new SimpleException("upload file failed.", DemoError.REQUEST_ERROR);
        }
    }

    /**
     * upload one file to specified path
     * @param fileInputStream
     * @param fileMetaData
     * @return
     */
    public static String uploadSingleFile(InputStream fileInputStream, FormDataContentDisposition fileMetaData) {

        int read = 0;
        byte[] bytes = new byte[1024];

        try {
            String file = fileMetaData.getFileName();
            OutputStream out = new FileOutputStream(new File(DATAPATH + file));
            while ((read = fileInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();

            if(logger.isInfoEnabled()) {
                logger.info("Successfully upload file " + file + " to " + DATAPATH);
            }
            return "Successfully upload file " + file + " to " + DATAPATH;
        } catch(IOException e) {
            logger.error("upload file failed.");
            throw new SimpleException("upload file failed.", DemoError.FILE_UPLOAD_FAILED);
        }
    }

    /**
     * upload one file to specified path via MultipartFile.
     * @param file
     * @return
     */
    public static String uploadSingleFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String filePath = DATAPATH;
        File dest = new File(filePath+fileName);

        try {
            file.transferTo(dest);
            if(logger.isInfoEnabled()) {
                logger.info("upload file to " + dest + "successfully!");
            }
            return "Upload successfully!";
        } catch (IOException e) {
            logger.error("upload file failed.");
            throw new SimpleException("upload file failed.", DemoError.REQUEST_ERROR);
        }
    }

    /**
     * add multiple files to specified path at once.
     * @param files
     * @return
     */
    public static String uploadMultiFile(List<MultipartFile> files) {
        String filePath = DATAPATH;
        if(logger.isInfoEnabled()) {
            logger.info(files.size() + " will be uploaded.");
        }
        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            if (file.isEmpty()) {
                return "upload the " + (i++) + " file failed.";
            }
            String fileName = file.getOriginalFilename();
            File dest = new File(filePath+fileName);
            try {
                file.transferTo(dest);
                if(logger.isInfoEnabled()) {
                    logger.info("upload the " + (i + 1) +" file to " + dest + "successfully!");
                }
            } catch (IOException e) {
//                e.printStackTrace();
                logger.error("upload the " + (i++) + " file failed.");
                //TODO throw 403 error for part uploads failed?
            }
        }

        return "Upload successfully!";
    }

//    //TODO update user in csv files - or DB?
//    public static boolean updUser(User user, String csvFile) {
//
//    }

    public static User fetchUser(String userName, String csvFile) {
        String srcPath = DATAPATH + csvFile;
        String charset = "utf-8";
        File file = new File(srcPath);

        if (!file.exists()) {
            logger.error(file + " doesn't exist.");
            throw new SimpleException(file + " doesn't exist.", DemoError.File_NOT_FOUND);
        }

        try {
            CSVReader csvReader = new CSVReaderBuilder(new BufferedReader(new InputStreamReader(new FileInputStream(new File(srcPath)), charset))).build();
            String[] user;
            user = csvReader.readNext();
            while(user != null) {
                if (user[0].equals(userName)) {
                    //TODO transfer the string[] to user and return user
//                    String[] userFields = {"name", "age", "emailAddress"};
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
        } catch (Exception e) {
            logger.error("Query failed, please retry.");
            throw new SimpleException("Query failed, please retry.", DemoError.REQUEST_ERROR);
        }

        logger.warn("Don't find user " + userName + " from file " + csvFile);
        throw new SimpleException("Don't find user - " + userName, DemoError.USER_NOT_FOUND);
    }

    public static User fetchFirstUser(String csvFile) {
        String srcPath = DATAPATH + csvFile;
        File file = new File(srcPath);

//        if (csvFile == null || csvFile.isEmpty() || !file.exists()) {
        if (!file.exists()) {
//            csvFile = "users.csv";
//            srcPath = DATAPATH + csvFile;
//            logger.warn(file + " doesn't exist, will fetch the user from default file: " + srcPath);
            logger.error(file + " doesn't exist.");
            throw new SimpleException(file + " doesn't exist.", DemoError.File_NOT_FOUND);
        }

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
        } catch (Exception e) {
            logger.error("Query failed, please retry.");
            throw new SimpleException("Query failed, please retry.", DemoError.REQUEST_ERROR);
        }

        logger.warn("No user found from file " + csvFile);
        throw new SimpleException("No user found!", DemoError.USER_NOT_FOUND);
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

//        fetchUser("Lynn","users.csv");
        User user = fetchUser("aaa","users.csv");
        System.out.println(user);

//        File file = new File("/tmp/demo/users.csv");
//        if(file.exists()) {
//            System.out.println("exist!");
//        } else {
//            System.out.println("don't exist!");
//        }

        File dir = new File(DATAPATH);
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
