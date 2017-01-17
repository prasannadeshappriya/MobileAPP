package com.a14roxgmail.prasanna.mobileapp.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prasanna Deshappriya on 1/17/2017.
 */
public class Semester {
    private int id;
    private List<Course> courseList;

    public Semester(int id, List<Course> courseList) {
        this.id = id;
        this.courseList = courseList;
    }

    public List<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(ArrayList<Course> courseList) {
        this.courseList = courseList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
