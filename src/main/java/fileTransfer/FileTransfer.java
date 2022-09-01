package fileTransfer;

import dataUnits.Request;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public abstract class FileTransfer {
    public static void getFile(DataInputStream dataInputStream, Request response, Path path) {

        byte[] fileToBytes = new byte[1024*10];
        File file = new File(path + "\\" + response.getNewFileName());
        try {
            int chunkSize = dataInputStream.read(fileToBytes);
            try (RandomAccessFile fileStream = new RandomAccessFile(file, "rw")) {
                while (chunkSize != -1) {
                    fileStream.write(fileToBytes, 0, chunkSize);
                    fileStream.skipBytes(chunkSize);
                    chunkSize = dataInputStream.read(fileToBytes);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response.getState() != 201){
            Scanner scanner = new Scanner(System.in);
            System.out.print("The file was downloaded! Specify a name for it: ");
            String fileName = scanner.nextLine();
            response.setFilename(fileName);
            try {
                Files.move(Path.of(file.getAbsolutePath()), Path.of(file.getAbsolutePath()).resolveSibling(response.getFileName()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void sendFile(OutputStream fileOutputStream, Request response, Path path) {
        File file = new File(path + "\\" + response.getFileName());
        byte[] fileToBytes = new byte[1024*10];
        try (InputStream fileInputStream = new FileInputStream(file)) {
            int chunkSize = fileInputStream.read(fileToBytes);
            while (chunkSize != -1) {
                fileOutputStream.write(fileToBytes);
                fileOutputStream.flush();
                chunkSize = fileInputStream.read(fileToBytes);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
