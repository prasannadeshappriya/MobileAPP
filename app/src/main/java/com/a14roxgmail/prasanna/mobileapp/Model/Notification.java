package com.a14roxgmail.prasanna.mobileapp.Model;

/**
 * Created by Prasanna Deshappriya on 1/27/2017.
 */
public class Notification {
    private int ID;
    private String user_index;
    private String day;
    private String event;

    public Notification(String user_index, String day, String event) {
        this.user_index = user_index;
        this.day = day;
        this.event = event;
    }

    public Notification(int ID, String user_index, String day, String event) {
        this.ID = ID;
        this.user_index = user_index;
        this.day = day;
        this.event = event;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getUser_index() {
        return user_index;
    }

    public void setUser_index(String user_index) {
        this.user_index = user_index;
    }
}
