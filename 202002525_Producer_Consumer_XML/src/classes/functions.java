/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.io.File;
import java.io.FilenameFilter;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author LENOVO
 */
public class functions {

    public void showMainMenu() {

        System.out.println("\t MAIN MENU");
        System.out.println("\t ==========");
        System.out.println("\t 1. Producer menu");;
        System.out.println("\t 2. Consumer menu");
        System.out.println("\t 0. Exit");
        System.out.println("\t =====================================");

        System.out.println("\t ENTER YOUR CHOICE  :  ");
    }

    public void showProducerSubMenu() {

        System.out.println("\t PRODUCER SUB-MENU ");
        System.out.println("\t ==================================");
        System.out.println("\t 1. Add New Student Record");;
        System.out.println("\t 2. View Student Records");
        System.out.println("\t 3. Return to Main Menu");
        System.out.println("\t____________________________________________________");

        System.out.println("\t ENTER YOUR CHOICE  :  ");
    }

    public void showConsumerSubMenu() {

        System.out.println("\t CONSUMER SUB-MENU ");
        System.out.println("\t ==================================");
        System.out.println("\t 1. View student information");
        System.out.println("\t 2. Return to Main Menu");
        System.out.println("\t____________________________________________________");

        System.out.println("\t ENTER YOUR CHOICE  :  ");

    }
    
    public  <T> T get(Queue<T> queue, int index) {
     if(index>=queue.size()){
        throw new IndexOutOfBoundsException("index="+index+",size="+queue.size());
    }
    Queue<T> queueCopy = new LinkedList<T>(queue);
    for(int i=0; i<index; i++){
        queueCopy.remove();
    }
    return queueCopy.peek();    
    
}

    public String[] getFile(String folder) {
        String fileNames[] = null;
        
        
        //Creating a File object for directory
        File directoryPath = new File(folder);
        FilenameFilter textFilefilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                String lowercaseName = name.toLowerCase();
                if (lowercaseName.endsWith(".xml")) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        //List of all the text files
        fileNames = directoryPath.list(textFilefilter);
        
        return fileNames;
    }

    public void clearScreen() {

        System.out.print("\033[H\033[2J");
        System.out.flush();

    }
    
    public int removeString(String s) {
        int fileNumber = 0;
        String filename = s.replaceAll("[^0-9]", "");
        fileNumber=Integer.parseInt(filename);
        return fileNumber;
    }

}
