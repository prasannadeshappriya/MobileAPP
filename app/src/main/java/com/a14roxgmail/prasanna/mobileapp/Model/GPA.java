package com.a14roxgmail.prasanna.mobileapp.Model;

/**
 * Created by Prasanna Deshappriya on 1/18/2017.
 */
public class GPA {
    private int id;
    private String type;
    private String semester;
    private String gpa;
    private String userIndex;
    private String total_credits;

    public GPA(int id, String type, String semester, String gpa, String userIndex) {
        this.id = id;
        this.type = type;
        this.semester = semester;
        this.gpa = gpa;
        this.userIndex = userIndex;
    }

    public GPA(String type, String semester, String gpa, String userIndex, String total_credits) {
        this.type = type;
        this.semester = semester;
        this.gpa = gpa;
        this.userIndex = userIndex;
        this.total_credits = total_credits;
    }

    public GPA(String type, String semester, String gpa, String userIndex) {
        this.type = type;
        this.semester = semester;
        this.gpa = gpa;
        this.userIndex = userIndex;
    }

    public void setTotal_credits(String total_credits){
        this.total_credits = total_credits;
    }

    public String getTotal_credits(){return total_credits;}

    public void setUserIndex(String userIndex){
        this.userIndex = userIndex;
    }
    public String getUserIndex(){return userIndex;}

    public void setId(int id){this.id = id;}
    public int getId(){return id;}

    public String getGpa() {
        return gpa;
    }

    public void setGpa(String gpa) {
        this.gpa = gpa;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
