package main;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Consumer {
    private static final int BUFFER_SIZE = 10;
    private static final String BUFFER_DIRECTORY = "buffer/";
    private static final String RESULT_DIRECTORY = "result/";
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 1234;

    private static Semaphore mutex = new Semaphore(1);
    private static Semaphore empty = new Semaphore(BUFFER_SIZE);
    private static Semaphore full = new Semaphore(0);

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

                int fileNameIndex = objectInputStream.readInt();
                String fileName = "student" + fileNameIndex + ".xml";

                // Consume the file name from the buffer
                empty.acquire();
                mutex.acquire();

                // Read the XML file
                String xml = new String(Files.readAllBytes(Paths.get(fileName)));

                // Delete the XML file
                Files.deleteIfExists(Paths.get(fileName));

                mutex.release();
                full.release();

                objectInputStream.close();
                socket.close();

                // Process the XML file
                ITStudent student = ITStudent.fromXML(xml);
                student.calculateAverage();

                // Print student information
                System.out.println("Name: " + student.getName());
                System.out.println("ID: " + student.getId());
                System.out.println("Programme: " + student.getProgramme());
                System.out.println("Courses: " + student.getCourses());
                System.out.println("Marks: " + student.getMarks());
                System.out.println("Average: " + student.getAverage());
                System.out.println("Pass/Fail: " + (student.getAverage() >= 50 ? "Pass" : "Fail"));
                System.out.println();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}