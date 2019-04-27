package com.xon.mymidsemproject;

import android.database.sqlite.SQLiteDatabase;

public class User {
    private String email;
    private String name;
    private String phone;
    private String password;
    private byte[] image;

    public User(String email,String password) {
        this.email = email;
        this.password = password;


    }

    public User(String email, String name,String phone,String password) {
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.password = password;


    }



    public User(String email, String name,String phone,String password, byte[] image) {
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.image = image;

    }


    public String getEmail() {
        return email;
    }

    public void setPrice(String email) {
        this.email = email;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String price) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
