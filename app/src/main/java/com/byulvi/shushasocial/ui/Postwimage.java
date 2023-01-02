package com.byulvi.shushasocial.ui;

public class Postwimage {
    public String id,title,post,image_id, publisher, uri, uiduser,timestamp;

    public String getUiduser() {
        return uiduser;
    }

    public void setUiduser(String uiduser) {
        this.uiduser = uiduser;
    }

    public Postwimage(){

    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Postwimage(String id, String title, String post, String image_id, String publisher, String uri, String uiduser, String timestamp) {
        this.id = id;
        this.title = title;
        this.post = post;
        this.image_id = image_id;
        this.publisher = publisher;
        this.uri = uri;
        this.uiduser = uiduser;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }
}
