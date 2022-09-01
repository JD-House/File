package server;

import dataUnits.Request;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public abstract class DatabaseRequestHandler {
    final static String DATA_PATH = "C:\\Users\\cbrig\\IdeaProjects\\File Server\\File Server\\task\\src\\server\\data";
    final static String DATA_MAP_PATH = "C:\\Users\\cbrig\\IdeaProjects\\File Server\\File Server\\task\\src\\server\\";
    final static String DATA_BASE_NAME = "data_base.txt";
    public static Request handleRequest(Request request) {

        if (request.getTypeOfRequest() != null) {
            switch (request.getTypeOfRequest()) {
                case  PUT -> {
                    return putRequest(request);
                }
                case GET -> {
                    return getRequest(request);
                }
                case DELETE -> {
                    return deleteRequest(request);
                }
                default -> {
                    return request;
                }
            }
        } else {
            return request;
        }
    }
    private static Request putRequest(Request response) {

        final Path path = Paths.get(DATA_PATH, response.getNewFileName());
        if (addDataToBase(response) != 0) {
            try {
                Files.createFile(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        response.setState(201);
        return response;
    }
    private static Request deleteRequest(Request response) {
        Map<Integer, String> database = connectToDataBase();
        if("id".equals(response.getNameOrID())){
            response.setFilename(database.get(response.getId()));
        }
        if (deleteFromDatabase(response) && deleteFile(response)) {
            response.setState(200);
            response.setResponseText("The response says that this file was deleted successfully!");
        }
        else
        {
            response.setResponseText("The response says that this file is not found!");
            response.setState(403);
        }
        return response;
    }
    private static Request getRequest(Request response) {
        Map<Integer, String> database = connectToDataBase();
        if("id".equals(response.getNameOrID())){
            response.setFilename(database.get(response.getId()));
        }
        if(!isFileAlreadyExist(response)) {
            response.setResponseText("The response says that this file is not found!");
            response.setState(403);
            return response;
        }
        response.setState(200);
        return response;
    }
    private static int idGenerator() {
        return (int) (Math.random() * 1000 + 1);
    }
    private static void createDataBase(){
        try {
            Files.createFile(Path.of(DATA_MAP_PATH + DATA_BASE_NAME));
            try (FileOutputStream fileStream = new FileOutputStream(DATA_MAP_PATH + DATA_BASE_NAME);
            ObjectOutputStream objectStream = new ObjectOutputStream(fileStream)){
                Map<Integer, String> dataBase = new HashMap<>();
                objectStream.writeObject(dataBase);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @SuppressWarnings("unchecked")
    private static synchronized Map<Integer, String> connectToDataBase() {
        Map<Integer, String> dataBase = null;
        File file = new File(DATA_MAP_PATH + "\\" + DATA_BASE_NAME);
        try (FileInputStream inputStream = new FileInputStream(file);
             ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)){
            dataBase = (Map<Integer, String>) objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataBase;
    }
    private static int addDataToBase(Request response) {
        if (!Files.exists(Path.of(DATA_MAP_PATH + "\\" + DATA_BASE_NAME))) {
            createDataBase();
        }
        if (isFileAlreadyExist(response)) {
            response.setResponseText("The response says that creating the file was forbidden!");
            response.setState(403);
            return 0;
        }
        Map<Integer, String> database = connectToDataBase();
        int id = idGenerator();
        while (database.containsKey(id)) {
            id = idGenerator();
        }
        database.put(id, response.getNewFileName());
        dataBaseSaveChanges(database);
        response.setResponseText("Response says that file is saved! ID = " + id);
        response.setState(200);
        return id;
    }
    private static boolean deleteFromDatabase(Request response) {
        Map<Integer, String> dataBase = connectToDataBase();
        if (!isFileAlreadyExist(response)) {
            return false;
        }
        if ("name".equals(response.getNameOrID())) {
            dataBase.remove(response.getId());
        }
        if ("id".equals(response.getNameOrID())) {
            response.setFilename(dataBase.get(response.getId()));
            dataBase.remove(response.getId());
        }
        dataBaseSaveChanges(dataBase);
        return true;
    }
    private static void dataBaseSaveChanges(Map<Integer, String> dataBase) {
        File file = new File(DATA_MAP_PATH + "\\" + DATA_BASE_NAME);
        try (FileOutputStream fileStream = new FileOutputStream(file);
             ObjectOutputStream objectStream = new ObjectOutputStream(fileStream)){
            objectStream.writeObject(dataBase);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static boolean isFileAlreadyExist(Request response) {
        Map<Integer, String> dataBase = connectToDataBase();
        if (Files.exists(Path.of(DATA_PATH +"\\" + response.getFileName()))) {
            return dataBase.containsKey(response.getId()) || dataBase.containsValue(response.getFileName());
        }
        return false;
    }
    private static boolean deleteFile(Request response){
        try {
            Files.delete(Path.of(DATA_PATH + "\\" + response.getFileName()));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}




