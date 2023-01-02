package com.byulvi.shushasocial.ui;

public class groupclass {
    String gpname,pwd;

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getGpname() {
        return gpname;
    }

    public void setGpname(String gpname) {
        this.gpname = gpname;
    }

    public groupclass(String gpname, String pwd) {
        this.gpname = gpname;
        this.pwd = pwd;
    }
    public groupclass(){

    }
}
