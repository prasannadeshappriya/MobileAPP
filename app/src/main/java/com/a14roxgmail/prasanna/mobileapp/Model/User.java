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
    private String loginStatus;
    private String password;

    public User(String ID, String firstName, String lastName, String fullName) {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = fullName;
    }

    public User(String firstName, String lastName, String fullName, String userIndex, String token, String loginStatus, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.loginStatus = loginStatus;
        this.fullName = fullName;
        this.token = token;
        this.userIndex = userIndex;
        this.password = password;
    }

    public User(String ID, String firstName, String lastName, String fullName, String userIndex, String token,String loginStatus,String password) {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = fullName;
        this.loginStatus = loginStatus;
        this.token = token;
        this.userIndex = userIndex;
        this.password = password;
    }

    public String getPassword(){return password;}

    public void setPassword(String password){
        this.password = password;
    }

    public void setLoginStatus(String loginStatus){
        this.loginStatus = loginStatus;
    }

    public String getLoginStatus(){return loginStatus;}

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
