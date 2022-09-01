package server;

import dataUnits.Request;
import dataUnits.RequestTypes;

import java.io.*;
import java.net.Socket;
import java.nio.file.Path;

import static fileTransfer.FileTransfer.getFile;
import static fileTransfer.FileTransfer.sendFile;
import static server.DatabaseRequestHandler.DATA_PATH;

public class Session extends Thread{
    private final Socket socket;

    public Session(Socket socket) {
        this.socket = socket;
    }
    public void run() {
        Request request;
        try (
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                InputStream fileInputStream = socket.getInputStream();
                DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(fileInputStream));
                OutputStream fileOutputStream = new DataOutputStream(
                        new BufferedOutputStream(socket.getOutputStream()))) {

            request = (Request) inputStream.readObject();

            if ("exit".equalsIgnoreCase(request.getTypeOfRequest().toString())) {
                ServerStop.stop = true;
                Thread.currentThread().interrupt();
                return;
            }

            Request response = DatabaseRequestHandler.handleRequest(request);
            outputStream.writeObject(response);

            if (response.getTypeOfRequest() == RequestTypes.GET && response.getState() == 200) {
                sendFile(fileOutputStream, response, Path.of(DATA_PATH));
            }
            if (response.getTypeOfRequest() == RequestTypes.PUT && response.getState() == 201) {
                getFile(dataInputStream, request,Path.of(DATA_PATH));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
