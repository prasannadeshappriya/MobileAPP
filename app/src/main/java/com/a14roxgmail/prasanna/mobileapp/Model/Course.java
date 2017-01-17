package com.a14roxgmail.prasanna.mobileapp.Model;

/**
 * Created by Prasanna Deshappriya on 1/17/2017.
 */
public class Course {
    private String id;
    private String shortname;
    private String fullname;

    public Course(String id, String shortname, String fullname) {
        this.id = id;
        this.shortname = shortname;
        this.fullname = fullname;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }
}
