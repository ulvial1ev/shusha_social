package com.byulvi.shushasocial.ui;

public class gpimage {
    String id, publisher,image_id, timestamp,postid;
    public gpimage(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public gpimage(String id, String publisher, String image_id,String timestamp,String postid) {
        this.id = id;
        this.publisher = publisher;
        this.image_id = image_id;
        this.timestamp = timestamp;
        this.postid = postid;
    }
}
