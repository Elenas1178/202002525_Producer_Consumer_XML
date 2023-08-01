package main;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

public class ITStudent implements Serializable {

    private String name;
    private String id;
    private String programme;
    private List<String> courses;
    private List<Integer> marks;
    private double average;

    public ITStudent(String name, String id, String programme, List<String> courses, List<Integer> marks) {
        this.name = name;
        this.id = id;
        this.programme = programme;
        this.courses = courses;
        this.marks = marks;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getProgramme() {
        return programme;
    }

    public List<String> getCourses() {
        return courses;
    }

    public List<Integer> getMarks() {
        return marks;
    }

    public double getAverage() {
        return average;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setProgramme(String programme) {
        this.programme = programme;
    }

    public void setCourses(List<String> courses) {
        this.courses = courses;
    }

    public void setMarks(List<Integer> marks) {
        this.marks = marks;
    }

    public void calculateAverage() {
        int sum = 0;
        for (int mark : marks) {
            sum += mark;
        }
        average = (double) sum / marks.size();
    }

    public String toXML() {
        StringBuilder xml = new StringBuilder();
        xml.append("<student>\n");
        xml.append("\t<name>").append(name).append("</name>\n");
        xml.append("\t<id>").append(id).append("</id>\n");
        xml.append("\t<programme>").append(programme).append("</programme>\n");
        for (int i = 0; i < courses.size(); i++) {
            xml.append("\t<course>").append(courses.get(i)).append("</course>\n");
            xml.append("\t<mark>").append(marks.get(i)).append("</mark>\n");
        }
        xml.append("</student>");
        return xml.toString();
    }

   public static ITStudent fromXML(String xml) {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    try {
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        Document document = builder.parse(is);

        org.w3c.dom.Element root = document.getDocumentElement();

        String name = getElementValue(root, "name");
        String id = getElementValue(root, "id");
        String programme = getElementValue(root, "programme");

        List<String> courses = new ArrayList<>();
        List<Integer> marks = new ArrayList<>();

        NodeList courseElements = root.getElementsByTagName("course");
        NodeList markElements = root.getElementsByTagName("mark");

        for (int i = 0; i < courseElements.getLength(); i++) {
            String course = courseElements.item(i).getTextContent();
            int mark = Integer.parseInt(markElements.item(i).getTextContent());

            courses.add(course);
            marks.add(mark);
        }

        return new ITStudent(name, id, programme, courses, marks);
    } catch (Exception e) {
        e.printStackTrace();
    }

    return null;
}

private static String getElementValue(Element element, String tagName) {
    NodeList nodeList = element.getElementsByTagName(tagName);
    if (nodeList.getLength() > 0) {
        return nodeList.item(0).getTextContent();
    }
    return "";
}
}