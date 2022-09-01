package client;

import dataUnits.Request;
import dataUnits.RequestTypes;
import networkData.networkData;
import presentation.OptionsMenu;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

import static fileTransfer.FileTransfer.getFile;
import static fileTransfer.FileTransfer.sendFile;

public class Client {
    public final static String FILE_STORAGE_PATH = "C:\\Users\\cbrig\\IdeaProjects\\File Server\\File Server\\task\\src\\client\\data";

    public static void main(String[] args)  {

        OptionsMenu optionsMenu = new OptionsMenu();
        optionsMenu.printActionsMenu();
        Path path = Path.of(FILE_STORAGE_PATH);
        if (!Files.isDirectory(path)) {
            try {
                Files.createDirectory(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Request request = optionsMenu.getInteraction();

        try(Socket socket = new Socket(networkData.getIP(), networkData.getPORT());
        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
        InputStream fileInputStream = socket.getInputStream();
        DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(fileInputStream));
            OutputStream fileOutputStream = new DataOutputStream(
                    new BufferedOutputStream(socket.getOutputStream())))
        {

            requestHandling(request, outputStream, inputStream, dataInputStream, fileOutputStream);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void requestHandling(Request request, ObjectOutputStream outputStream, ObjectInputStream inputStream, DataInputStream dataInputStream, OutputStream fileOutputStream) throws IOException, ClassNotFoundException {
        outputStream.writeObject(request);
        if ("exit".equalsIgnoreCase(request.getTypeOfRequest().toString())) {
            System.out.println("The request was sent");
            return;
        }

        Request response = (Request) inputStream.readObject();

        Path fileStoragePath = Path.of(FILE_STORAGE_PATH);
        if (response.getTypeOfRequest() == RequestTypes.GET && response.getState() == 200) {
            getFile(dataInputStream, response, fileStoragePath);
            System.out.println("File saved on the hard drive!");
        }
        else if (response.getTypeOfRequest() == RequestTypes.PUT) {
            sendFile(fileOutputStream, request, fileStoragePath);
            System.out.print(response.getResponseText());
        }
        else {
            System.out.println(response.getResponseText());
        }
    }
}
