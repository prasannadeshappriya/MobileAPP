package com.a14roxgmail.prasanna.mobileapp.Model;

/**
 * Created by Prasanna Deshappriya on 1/18/2017.
 */
public class GPA {
    private Long id;
    private String type;
    private String semester;
    private String gpa;

    public GPA(int id, String type, String semester, String gpa) {
        this.id = Long.parseLong(String.valueOf(id));
        this.type = type;
        this.semester = semester;
        this.gpa = gpa;
    }

    public void setId(Long id){this.id = id;}
    public Long getId(){return id;}

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
