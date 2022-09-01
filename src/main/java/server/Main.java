package server;

import networkData.networkData;

import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Path;

import static server.DatabaseRequestHandler.DATA_PATH;

public class Main {

    public static void main(String[] args) {
        System.out.println("Server started!");
        Path path = Path.of(DATA_PATH);

        if (!Files.isDirectory(path)) {
            try {
                Files.createDirectory(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try(ServerSocket serverSocket = new ServerSocket(networkData.getPORT())) {
            while (!ServerStop.stop){
                Session session = new Session(serverSocket.accept());
                session.start();
                session.join();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}