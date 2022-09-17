package fr.endide.application.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "groups")
public class Group extends AbstractEntity{
    String SchoolEmail;
    String SchoolLevel;
    int StudentNumber;
    public String getSchoolEmail() {
        return SchoolEmail;
    }
    public void setSchoolEmail(String schoolEmail) {
        SchoolEmail = schoolEmail;
    }
    public String getSchoolLevel() {
        return SchoolLevel;
    }
    public void setSchoolLevel(String schoolLevel) {
        SchoolLevel = schoolLevel;
    }
    public int getStudentNumber() {
        return StudentNumber;
    }
    public void setStudentNumber(int studentNumber) {
        StudentNumber = studentNumber;
    }
}
