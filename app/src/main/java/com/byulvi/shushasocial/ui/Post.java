package com.byulvi.shushasocial.ui;

public class Post {
    public String id,title,post,publisher,uri, uiduser,timestamp;

    public Post(){

    }

    public String getUiduser() {
        return uiduser;
    }

    public void setUiduser(String uiduser) {
        this.uiduser = uiduser;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Post(String id, String title, String post, String publisher, String uri, String uiduser, String timestamp) {
        this.id = id;
        this.title = title;
        this.post = post;
        this.publisher = publisher;
        this.uri = uri;
        this.uiduser = uiduser;
        this.timestamp = timestamp;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
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
}
