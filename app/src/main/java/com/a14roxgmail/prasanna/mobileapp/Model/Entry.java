package com.a14roxgmail.prasanna.mobileapp.Model;

import java.util.ArrayList;

/**
 * Created by Prasanna Deshappriya on 1/22/2017.
 */
public class Entry {
    private int id;
    private String month;
    private String day;
    private ArrayList<String> events;

    public Entry(String day, String month) {
        this.day = day;
        this.month = month;
    }

    public Entry(String day, String month, ArrayList<String> events) {
        this.day = day;
        this.month = month;
        this.events = events;
    }

    public Entry(int id, String day, String month, ArrayList<String> events) {
        this.id = id;
        this.day = day;
        this.month = month;
        this.events = events;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public ArrayList<String> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<String> events) {
        this.events = events;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
