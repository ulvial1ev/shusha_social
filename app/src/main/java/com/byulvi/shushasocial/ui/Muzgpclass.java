package com.byulvi.shushasocial.ui;

public class Muzgpclass {
    String gp,id,publisher,comm;

    public Muzgpclass(String gp, String id, String publisher, String comm) {
        this.gp = gp;
        this.id = id;
        this.publisher = publisher;
        this.comm = comm;
    }
    public Muzgpclass(){

    }

    public String getGp() {
        return gp;
    }

    public void setGp(String gp) {
        this.gp = gp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getComm() {
        return comm;
    }

    public void setComm(String comm) {
        this.comm = comm;
    }
}
