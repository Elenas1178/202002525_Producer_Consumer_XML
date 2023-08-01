/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Producer {
    private static final int BUFFER_SIZE = 10;
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 1234;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

            Random random = new Random();
            int count = 1;

            while (true) {
                // Generate random student information
                ITStudent student = generateRandomStudent();

                // Create XML string from student object
                String xml = student.toXML();

                // Write XML string to file
                String fileName = "student" + count + ".xml";
                FileWriter fileWriter = new FileWriter(fileName);
                fileWriter.write(xml);
                fileWriter.close();

                // Send file name to buffer
                objectOutputStream.writeInt(count);
               // objectOutputStream.flush();

                System.out.println("Produced: " + fileName);

                // Sleep for random duration
                Thread.sleep(random.nextInt(2000) + 1000);

                count++;

                if (count > BUFFER_SIZE) {
                    break;
                }
            }

            objectOutputStream.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static ITStudent generateRandomStudent() {
        Random random = new Random();

        String[] names = {"John", "Jane", "Alex", "Emily", "Michael"};
        String name = names[random.nextInt(names.length)];

        String id = String.format("%08d", random.nextInt(100000000));

        String[] programmes = {"Computer Science", "Information Technology", "Software Engineering"};
        String programme = programmes[random.nextInt(programmes.length)];

        int numCourses = random.nextInt(5) + 1;
        List<String> courses = new ArrayList<>();
        List<Integer> marks = new ArrayList<>();

        for (int i = 0; i < numCourses; i++) {
            courses.add("Course " + (i + 1));
            marks.add(random.nextInt(100));
        }

        return new ITStudent(name, id, programme, courses, marks);
    }
}