package com.example.spiritualtablet;

public class UserRegisterClass {

    private String fullName, userName, email, mobileNo, password;

    public UserRegisterClass() {
    }

    UserRegisterClass(String fullName, String userName, String email, String mobileNo, String password) {
        this.fullName = fullName;
        this.userName = userName;
        this.email = email;
        this.mobileNo = mobileNo;
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
