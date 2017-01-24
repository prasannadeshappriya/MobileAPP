package com.a14roxgmail.prasanna.mobileapp.Model;

/**
 * Created by Prasanna Deshappriya on 1/24/2017.
 */
public class Setting {
    private int ID;
    private String user_index;
    private String gpa_operation;
    private String last_edit_date;

    public Setting(String user_index, String gpa_operation, String last_edit_date) {
        this.user_index = user_index;
        this.gpa_operation = gpa_operation;
        this.last_edit_date = last_edit_date;
    }

    public Setting(int ID, String user_index, String gpa_operation, String last_edit_date) {
        this.ID = ID;
        this.user_index = user_index;
        this.gpa_operation = gpa_operation;
        this.last_edit_date = last_edit_date;
    }

    public String getGpa_operation() {
        return gpa_operation;
    }

    public void setGpa_operation(String gpa_operation) {
        this.gpa_operation = gpa_operation;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getLast_edit_date() {
        return last_edit_date;
    }

    public void setLast_edit_date(String last_edit_date) {
        this.last_edit_date = last_edit_date;
    }

    public String getUser_index() {
        return user_index;
    }

    public void setUser_index(String user_index) {
        this.user_index = user_index;
    }
}
