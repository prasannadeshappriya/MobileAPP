package com.a14roxgmail.prasanna.mobileapp.Model;

/**
 * Created by Prasanna Deshappriya on 1/16/2017.
 */
public class User {
    private String ID;
    private String firstName;
    private String lastName;
    private String fullName;
    private String userIndex;
    private String token;

    public User(String ID, String firstName, String lastName, String fullName) {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = fullName;
    }

    public User(String firstName, String lastName, String fullName, String userIndex, String token) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = fullName;
        this.token = token;
        this.userIndex = userIndex;
    }

    public User(String ID, String firstName, String lastName, String fullName, String userIndex, String token) {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = fullName;
        this.token = token;
        this.userIndex = userIndex;
    }

    public void setUserIndex(String userIndex){
        this.userIndex = userIndex;
    }

    public void setToken(String token){
        this.token = token;
    }

    public String getToken(){return token;}
    public String getUserIndex(){return userIndex;}

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
