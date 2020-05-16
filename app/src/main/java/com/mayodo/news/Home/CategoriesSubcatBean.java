package com.mayodo.news.Home;

public class CategoriesSubcatBean {
    private String id;
    private String name;
    private String parent;

    public CategoriesSubcatBean(String id, String name, String parent) {
        this.id = id;
        this.name = name;
        this.parent = parent;

    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

}
