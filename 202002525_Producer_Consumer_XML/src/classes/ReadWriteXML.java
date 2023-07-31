/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.io.File;
import java.text.DecimalFormat;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Ka-zakhali CAT
 */
public class ReadWriteXML {

    private static final DecimalFormat decFormat = new DecimalFormat("0");

    public void WriteXML(ITStudent stu, String file) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
            // create the root element
            Element root = doc.createElement("students");
            doc.appendChild(root);
            Element student = doc.createElement("student");
            student.setAttribute("id", String.valueOf(stu.stuID));
            student.setAttribute("name", stu.stuName);
            student.setAttribute("programme", stu.stuProgramme);

            String[] courseandmark = stu.stuCourse.split("=");
            Element course1 = null;
            for (int i = 0; i < courseandmark.length; i++) {

                String[] splitMark = courseandmark[i].split(":");
                course1 = doc.createElement("course");
                course1.setAttribute("name", splitMark[0]);
                course1.setAttribute("mark", splitMark[1]);
                student.appendChild(course1);
            }

            root.appendChild(student);
            // write the updated document back to the file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(file));
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void ReadXML(String file) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
            NodeList studentList = doc.getElementsByTagName("student");
            for (int i = 0; i < studentList.getLength(); i++) {

                int count = 0;
                int total = 0;
                double average = 0.0;
                ITStudent stu = new ITStudent();

                Element student = (Element) studentList.item(i);
                String id = student.getAttribute("id");
                String name = student.getAttribute("name");
                String programme = student.getAttribute("programme");

                stu.stuID = Integer.parseInt(id);
                stu.stuName = name;
                stu.stuProgramme = programme;

                System.out.println("Student number: " + stu.getStuID());
                System.out.println("Student Name: " + stu.getStuName());
                System.out.println("Programme: " + stu.getStuProgramme());

                NodeList courseList = student.getElementsByTagName("course");
                System.out.println("=========================================================");
                System.out.println(String.format("%-45s", "Course") + "\t" + String.format("%-5s", "Mark"));
                System.out.println("=========================================================");

                for (int j = 0; j < courseList.getLength(); j++) {
                    Element course = (Element) courseList.item(j);
                    String courseName = course.getAttribute("name");
                    String mark = course.getAttribute("mark");
                    System.out.println(String.format("%-45s", courseName) + String.format("%5s", mark));

                    total = total + Integer.parseInt(mark);
                    count++;
                }
                average = (double) total / count;
                System.out.println("\n=========================================================");
                System.out.println(String.format("%-45s", "Average") + String.format("%5s", String.valueOf(decFormat.format(average))));
                System.out.println("==========================================================");
                if (average >= 50) {
                    System.out.println("Result: Pass");
                } else {
                    System.out.println("Result: Fail");
                }
                System.out.println("=======================================================");

                File xmlfile
                        = new File(file);

                if (xmlfile.delete()) {
                    System.out.println("File deleted successfully");
                } else {
                    System.out.println("Failed to delete the file");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
