package dataUnits;

import java.io.Serializable;

public class Request implements Serializable {
    private final RequestTypes typeOfRequest;
    private String fileName = null;
    private String newFileName = null;
    private Integer id = null;
    private FileTypes typeOfFile;
    private String responseText = null;
    private String nameOrID = null;
    private int state;
    public Request(RequestTypes typeOfRequest) {
        this.typeOfRequest = typeOfRequest;
    }
    public void setFilename(String fileName) {
        this.fileName = fileName;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public void setTypeOfFile(FileTypes typeOfFile) {
        this.typeOfFile = typeOfFile;
    }
    public void setNameOrID(String nameOrID) {
        this.nameOrID = nameOrID;
    }
    public void setState(int state) {
        this.state = state;
    }
    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }
    public RequestTypes getTypeOfRequest() {
        return typeOfRequest;
    }
    public String getFileName() {
        return fileName;
    }
    public Integer getId() {
        return id;
    }
    public FileTypes getTypeOfFile() {
        return typeOfFile;
    }
    public String getNameOrID() {
        return nameOrID;
    }
    public String getNewFileName() {
        return newFileName;
    }
    public void setNewFileName(String newFileName) {
        this.newFileName = newFileName;
    }
    public int getState() {
        return state;
    }
    public String getResponseText() {
        return responseText;
    }
}
