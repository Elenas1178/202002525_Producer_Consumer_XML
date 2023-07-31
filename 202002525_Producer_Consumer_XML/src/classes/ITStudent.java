package classes;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ka-zakhali CAT
 */
public class ITStudent {
    String stuName;
    int stuID;
    String stuProgramme;
    String stuCourse;   

    public ITStudent() {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public int getStuID() {
        return stuID;
    }

    public void setStuID(int stuID) {
        this.stuID = stuID;
    }

    public String getStuProgramme() {
        return stuProgramme;
    }

    public void setStuProgramme(String stuProgramme) {
        this.stuProgramme = stuProgramme;
    }

    public String getStuCourse() {
        return stuCourse;
    }

    public void setStuCourse(String stuCourse) {
        this.stuCourse = stuCourse;
    }

    public ITStudent(String stuName, int stuID, String stuProgramme, String stuCourse) {
        this.stuName = stuName;
        this.stuID = stuID;
        this.stuProgramme = stuProgramme;
        this.stuCourse = stuCourse;
    }
}
