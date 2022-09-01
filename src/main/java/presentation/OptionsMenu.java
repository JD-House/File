package presentation;

import dataUnits.FileTypes;
import dataUnits.Request;
import dataUnits.RequestTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OptionsMenu {
    private final List<String> actions;
    private final Scanner scanner;

    public OptionsMenu() {
        this.scanner = new Scanner(System.in);
        this.actions = new ArrayList<>();
        actions.add("get a file"); //action 1
        actions.add("save a file"); //action 2
        actions.add("delete a file"); //action 3
    }
    public void printActionsMenu() {
        System.out.print("Enter action ");
        System.out.print("(");
        for (String action : actions) {
            System.out.printf("%s - %s", actions.indexOf(action) + 1, action);
            System.out.print(actions.indexOf(action) == actions.size() - 1 ? "" : ", ");
        }
        System.out.print("): ");
    }
    public Request getInteraction() {
        Request request;
        String requestType = this.scanner.nextLine();

        switch (requestType) {
            case "1" -> {
                request = new Request(RequestTypes.GET);
                getRequest(request);
            }
            case "2" -> {
                request = new Request(RequestTypes.PUT);
                putRequest(request);
            }
            case "3" -> {
                request = new Request(RequestTypes.DELETE);
                deleteRequest(request);
            }
            case "exit" -> request = new Request(RequestTypes.EXIT);
            default -> request = new Request(null);
        }
        return request;
    }
    private void deleteRequest(Request request) {
        if (request.getTypeOfRequest() == RequestTypes.DELETE) {
            System.out.print("Do you want to delete the file by name or by id (1 - name, 2 - id): ");
            String action = this.scanner.nextLine();
            switch (action) {
                case "1" -> {
                    request.setNameOrID("name");
                    setRequestedFileName(request);
                }
                case "2" -> {
                    request.setNameOrID("id");
                    setRequestedFileId(request);
                }
                default -> request.setNameOrID(null);
            }
        }
    }
    private void getRequest(Request request) {
        if (request.getTypeOfRequest() == RequestTypes.GET) {
            System.out.print("Do you want to get the file by name or by id (1 - name, 2 - id): ");
            String action = this.scanner.nextLine();

            switch (action) {
                case "1" -> {
                    request.setNameOrID("name");
                    setRequestedFileName(request);
                }
                case "2" -> {
                    request.setNameOrID("id");
                    setRequestedFileId(request);
                }
                default -> request.setNameOrID(null);
            }
            if(request.getTypeOfFile() != null){
                if (isTXT(request.getFileName())) {
                    request.setTypeOfFile(FileTypes.TXT);
                } else {
                    request.setTypeOfFile(FileTypes.JPG);
                }
            }
        }
    }
    private void putRequest(Request request) {
        setRequestedFileName(request);
        if (request.getFileName() != null){
            setRequestFileType(request);
        }
        if (request.getTypeOfRequest() == RequestTypes.PUT && request.getTypeOfFile() == FileTypes.TXT) {
            System.out.print("Enter name of the file to be saved on server: ");
            String fileOuterName = scanner.nextLine();
            request.setNewFileName(fileOuterName);
            if (request.getNewFileName() == null || "".equals(request.getNewFileName())) {
                request.setNewFileName(fileNameGenerator() + "." +
                        request.getTypeOfFile().toString().toLowerCase());
            }
        }
    }
    private boolean isJPG(String fileName) {
        return FileTypes.JPG.toString().equalsIgnoreCase
                (fileName.substring(fileName.length() - 3));
    }
    private boolean isTXT(String fileName) {
        return FileTypes.TXT.toString().equalsIgnoreCase
                (fileName.substring(fileName.length() - 3));
    }
    private void setRequestFileType(Request request) {
        if (isJPG(request.getFileName())) {
            request.setTypeOfFile(FileTypes.JPG);
        } else if (isTXT(request.getFileName())) {
            request.setTypeOfFile(FileTypes.TXT);
        }
    }
    private void setRequestedFileName (Request request){
        System.out.print("Enter file name: ");
        request.setFilename(scanner.nextLine());
    }
    private void setRequestedFileId (Request request){
        System.out.print("Enter id: ");
        request.setId(scanner.nextInt());
    }
    private String fileNameGenerator(){
        return Integer.valueOf((int) (Math.random() * 1000000 + 1)).toString();
    }
}
