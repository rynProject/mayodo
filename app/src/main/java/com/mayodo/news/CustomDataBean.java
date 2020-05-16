package com.mayodo.news;

/**
 * Created by cd on 27-02-2018.
 */

public class CustomDataBean {
    String id = "";
    String count = "";
    String post_title = "";

    public CustomDataBean() {
    }

    public CustomDataBean(String id, String count, String post_title) {
        this.id = id;
        this.count = count;
        this.post_title = post_title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPost_title() {
        return post_title;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
