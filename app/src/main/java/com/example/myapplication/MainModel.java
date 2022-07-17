package com.example.myapplication;


import android.net.Uri;

public class MainModel {

     String fname, email, location ,description,mobile;
     String image;

    public MainModel() {
    }

    public MainModel(String fname, String email, String location, String description, String mobile, String image) {
        this.fname = fname;
        this.email = email;
        this.location = location;
        this.description = description;
        this.mobile = mobile;
        this.image = image;
    }
    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
