package com.platonso.yamify.data;

import com.google.firebase.Timestamp;


public class Favourites {
    String title;
    String content;

    public Favourites() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}