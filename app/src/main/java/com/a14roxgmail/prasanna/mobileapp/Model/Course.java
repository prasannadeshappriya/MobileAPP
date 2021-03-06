package com.a14roxgmail.prasanna.mobileapp.Model;

/**
 * Created by Prasanna Deshappriya on 1/17/2017.
 */
public class Course {
    private String id;
    private String userIndex;
    private String courseName;
    private String courseCode;
    private String credits;
    private String semester;
    private String grade;

    public Course(String userIndex){
        this.userIndex = userIndex;
    }

    public Course(String userIndex, String courseName, String courseCode, String credits, String semester) {
        this.userIndex = userIndex;
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.credits = credits;
        this.semester = semester;
    }

    public Course(String id, String userIndex, String courseName, String courseCode, String credits, String semester) {
        this.id = id;
        this.userIndex = userIndex;
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.credits = credits;
        this.semester = semester;
    }

    public Course(String id, String userIndex, String courseName, String courseCode, String credits, String semester, String grade) {
        this.id = id;
        this.userIndex = userIndex;
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.credits = credits;
        this.semester = semester;
        this.grade = grade;
    }

    public String getGrade(){return grade;}

    public void setGrade(String grade){
        this.grade = grade;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getUserIndex() {
        return userIndex;
    }

    public void setUserIndex(String userIndex) {
        this.userIndex = userIndex;
    }
}
