package main;

import classes.Consumer;
import classes.functions;
import classes.Producer;
import classes.ReadWriteXML;
import classes.ITStudent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Ka-zakhali CAT
 */
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class ProducerConsumer {

    public static void main(String[] args) {
        Queue<Integer> queue = new LinkedList<>();
        Semaphore mutex = new Semaphore(1);  // Binary semaphore for mutual exclusion
        Semaphore empty = new Semaphore(10); // Semaphore for empty slots in the queue
        Semaphore full = new Semaphore(0);  // Semaphore for filled slots in the queue
        ITStudent stu = new ITStudent();

        Thread producer = new Thread(new Producer(queue, mutex, empty, full));

        functions fun = new functions();
        //put files in an array

        producer.start();

        ReadWriteXML readwrite = new ReadWriteXML();

        int main_choice = -1;

        //show main menu and read user input
        try {
            Scanner in = new Scanner(System.in);
            functions a = new functions();

            a.clearScreen();
            a.showMainMenu();
            main_choice = in.nextInt();

            // check if user input is valid
            //********************************************
            while (main_choice > 5 || main_choice < 1) {
                System.out.println("ERROR : Enter an integer digit between 1 and 5");

                //show main menu
                a.showMainMenu();

                main_choice = in.nextInt();
            }
            //********************************************

            while (main_choice != 5) // choice 5 to exit
            {
                switch (main_choice) {
                    case 1: { //manage student information
                        int submenu_choice = -1;

                        //show submenu and read user input
                        a.clearScreen();
                        a.showProducerSubMenu();
                        submenu_choice = in.nextInt();

                        // check if input is valid
                        //********************************************
                        while (main_choice > 5 || main_choice < 1) {
                            System.out.println("ERROR : Enter an integer digit between 1 and 3");

                            //show submenu again
                            a.showProducerSubMenu();

                            //read choice again
                            submenu_choice = in.nextInt();
                        }
                        //********************************************

                        while (submenu_choice != 3) //sub menu choice 3 returns to main menu
                        {
                            switch (submenu_choice) {
                                case 1: {  // add new student

                                    if (queue.size() == 10) {

                                        System.out.println("The buffer is full");
                                    } else {
                                        Scanner inAdd = new Scanner(System.in);
                                        int x = 0;
                                        String courseMark = "";

                                        System.out.println("Please enter student number:");
                                        stu.setStuID(Integer.parseInt(inAdd.nextLine()));

                                        System.out.println("Please enter student name in full starting with the surname:");
                                        stu.setStuName(inAdd.nextLine());

                                        System.out.println("Please enter student programme:");
                                        stu.setStuProgramme(inAdd.nextLine());

                                        System.out.println("How many courses is the student enrolled in? Enter number:");
                                        x = Integer.parseInt(inAdd.nextLine());

                                        for (int i = 0; i < x; i++) {
                                            String c = "";
                                            int m = 0;

                                            System.out.println("Please enter course name:");
                                            c = inAdd.nextLine();

                                            System.out.println("Please enter course mark:");
                                            m = (Integer.parseInt(inAdd.nextLine()));;

                                            courseMark = courseMark + c + ":" + String.valueOf(m) + "=";
                                            stu.setStuCourse(courseMark);
                                        }
                                        String filesList[] = fun.getFile("src\\files\\");
                                       
                                        ArrayList<Integer> fileNum = new ArrayList<>();
                                        for (String fileName : filesList) {

                                            System.out.println(fileName);
                                            fileName = fileName.replaceAll("[^0-9]", "");
                                            fileNum.add(Integer.parseInt(fileName));

                                            
                                        }

                                        

                                        ArrayList missingNum = new ArrayList();

                                        for (int j = 1; j <= 10; j++) {
                                            if (!fileNum.contains(j)) {
                                                missingNum.add(j);
                                            }
                                        }
                                        Collections.sort(missingNum);
                                        readwrite.WriteXML(stu, "src//files//student" + missingNum.get(0) + ".xml");
                                        System.out.println("Student ID: " + stu.getStuID());
                                        System.out.println("Student Name: " + stu.getStuName());
                                        System.out.println("Programme: " + stu.getStuProgramme());

                                        String[] courseandmark = courseMark.split("=");

                                        for (int i = 0; i < courseandmark.length; i++) {

                                            String[] splitMark = courseandmark[i].split(":");
                                            System.out.println("Course: " + splitMark[0]);
                                            System.out.println("Mark: " + splitMark[1]);
                                            System.out.println("");
                                        }
                                    }

                                    //show submenu again
                                    a.showProducerSubMenu();

                                    //read choice again
                                    submenu_choice = in.nextInt();

                                    break;
                                }
                                case 2: { //view student records

                                    //show submenu again
                                    a.showProducerSubMenu();

                                    //read choice again
                                    submenu_choice = in.nextInt();

                                    break;
                                }

                                case 3: {

                                    continue; //RETURN TO MAIN MENU
                                }
                                default: {
                                    System.out.println("INVALID SUBMENU CHOICE: Enter an integer digit between 1 and 3");

                                    //show submenu again
                                    a.showProducerSubMenu();

                                    //read choice again
                                    submenu_choice = in.nextInt();

                                }
                            }
                        }
                        a.clearScreen();
                        a.showMainMenu();
                        main_choice = in.nextInt();;

                        break;
                    } //end case main-choice = 1

                    case 2: { //consumer
                        int submenu_choice = -1;
                        Scanner inRead = new Scanner(System.in);
                        int x = 0;
                        //show submenu and read user input
                        if (queue.isEmpty()) {
                            System.out.println("Producer hasnt produced any files yet");
                            a.showMainMenu();
                            main_choice = in.nextInt();
                        } else {

                            a.showConsumerSubMenu();
                            submenu_choice = in.nextInt();

                        }
                        // check if input is valid
                        //********************************************
                        while (main_choice > 5 || main_choice < 1) {
                            System.out.println("ERROR : Enter an integer digit between 1 and 3");

                            //show submenu again
                            a.showConsumerSubMenu();

                            //read choice again
                            submenu_choice = in.nextInt();
                        }
                        while (submenu_choice != 2) // submenu choice 3 returns to main menu
                        {
                            switch (submenu_choice) {
                                case 1: { //add new enrollment record

                                    //show submenu again
                                    a.clearScreen();

                                    if (queue.size() == 1) {
                                        System.out.println("Please enter 1:");
                                    } else {
                                        System.out.println("Please enter a number between 1 and " + queue.size() + ":");
                                    }
                                    x = inRead.nextInt();

                                    if (x <= queue.size() && x > 0) {

                                        System.out.println("Here are the details for the selected student:");
                                        System.out.println("================================================");
                                        readwrite.ReadXML("src//files//student" + fun.get(queue, x - 1) + ".xml");
                                        queue.remove(fun.get(queue, x - 1));

                                        //Thread consumer = new Thread(new Consumer(queue, mutex, empty, full, x));
                                        //consumer.start();
                                        //consumer.interrupt();
                                    } else {
                                        System.out.println("Please enter a valid number");
                                    }
                                    a.showConsumerSubMenu();

                                    //read choice again
                                    submenu_choice = in.nextInt();

                                    break;
                                }

                                case 2: {
                                    continue; //RETURN TO MAIN MENU
                                }
                                default: {

                                    System.out.println("INVALID SUBMENU  CHOICE:  Enter an integer digit between 1 and 3");

                                    //show submenu again
                                    a.showConsumerSubMenu();

                                    //read choice again
                                    submenu_choice = in.nextInt();

                                }
                            }
                        }

                        //show main menu and read choice again
                        a.clearScreen();
                        a.showMainMenu();
                        main_choice = in.nextInt();;

                        break;
                    } //end case main choice = 2

                    case 0: {	 //exit program
                        System.exit(1);
                        break;
                    }

                    default: {
                        System.out.println("INCORRECT MAIN MENU CHOICE : Enter an integer digit between 1 and 5");

                        //show main menu again
                        a.showMainMenu();

                        //read choice again
                        main_choice = in.nextInt();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
