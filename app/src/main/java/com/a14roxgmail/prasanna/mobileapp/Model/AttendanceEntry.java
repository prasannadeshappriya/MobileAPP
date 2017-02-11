package com.a14roxgmail.prasanna.mobileapp.Model;

/**
 * Created by prasanna on 2/10/17.
 */

public class AttendanceEntry {
    private String id;
    private String year;
    private String month;
    private String date;
    private String comment;
    private String value;
    private String userIndex;
    private String moduleName;

    public AttendanceEntry(String userIndex, String moduleName, String value, String date, String month, String year, String comment) {
        this.value = value;
        this.date = date;
        this.month = month;
        this.year = year;
        this.comment = comment;
        this.userIndex = userIndex;
        this.moduleName = moduleName;
    }

    public AttendanceEntry(String id, String userIndex, String moduleName, String value, String date, String month, String year, String comment) {
        this.id = id;
        this.value = value;
        this.date = date;
        this.month = month;
        this.year = year;
        this.comment = comment;
        this.userIndex = userIndex;
        this.moduleName = moduleName;
    }

    public String getId() {
        return id;
    }

    public String getUserIndex() {
        return userIndex;
    }

    public void setUserIndex(String userIndex) {
        this.userIndex = userIndex;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
