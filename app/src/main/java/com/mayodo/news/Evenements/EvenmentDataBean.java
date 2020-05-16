package com.mayodo.news.Evenements;

public class EvenmentDataBean {

    String id = "";
    String title = "";
    String date = "";
    String featured_image_link = "";
    String category_arr = "";

    public EvenmentDataBean() {
    }

    public EvenmentDataBean(String id, String title, String time, String img, String categories) {
        this.id = id;
        this.title = title;
        this.date = time;
        this.featured_image_link = img;
        this.category_arr = categories;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFeatured_image_link() {
        return featured_image_link;
    }

    public void setFeatured_image_link(String featured_image_link) {
        this.featured_image_link = featured_image_link;
    }

    public String getCategory_arr() {
        return category_arr;
    }

    public void setCategory_arr(String category_arr) {
        this.category_arr = category_arr;
    }
}
