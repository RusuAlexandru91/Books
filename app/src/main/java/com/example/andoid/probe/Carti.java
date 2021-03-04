package com.example.andoid.probe;

public class Carti {

    private String title;
    private String author;
    private String date;
    private String Url;

    public Carti (String title, String author, String date, String url) {
        this.title = title;
        this.author = author;
        this.date = date;
        this.Url = url;
    }

    public String getAuthor() {
        return author;
    }

    public String getPages() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return Url;
    }
}
