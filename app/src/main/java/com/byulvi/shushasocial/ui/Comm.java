package com.byulvi.shushasocial.ui;

public class Comm {

    public String id,comm,publisher,commid;

    public Comm(){

    }

    public String getCommid() {
        return commid;
    }

    public void setCommid(String commid) {
        this.commid = commid;
    }

    public Comm(String id, String comm, String publisher, String commid) {
        this.id = id;
        this.comm = comm;
        this.publisher =publisher;
        this.commid = commid;
    }

    public String getId() {
        return id;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComm() {
        return comm;
    }

    public void setComm(String comm) {
        this.comm = comm;
    }
}
