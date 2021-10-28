package com.example.myapplication1.network.Info.entity;

public class User {
    private String name;
    private String password;

    public User(){
        super();
    }

    public User(String userName, String passWord) {
        this.name = userName;
        this.password = passWord;
    }

    public String getUserName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
