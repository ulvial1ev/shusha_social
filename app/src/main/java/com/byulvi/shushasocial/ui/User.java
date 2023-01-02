package com.byulvi.shushasocial.ui;

public class User {
    public String uid,name,mail,school;

    public User(){

    }

    public User(String uid, String name, String mail, String school ) {
        this.uid = uid;
        this.name = name;
        this.mail = mail;
        this.school = school;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }
}
