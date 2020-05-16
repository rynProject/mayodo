package com.mayodo.news.comment;


public class CommentBean {

    String title;
    String author_name;
    String date;

    public CommentBean(String title, String author_name,String  date) {
        this.title = title;
        this.author_name = author_name;
        this.date = date;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
