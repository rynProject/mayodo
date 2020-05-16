package com.mayodo.news.Home;

/**
 * Created by admin on 3/14/2018.
 */

public class CategoriesBean {
    private String id;
    private String name;
    private String parent;
    boolean isSelected = false;

    public CategoriesBean(String id, String name, String parent, boolean isSelected) {
        this.id = id;
        this.name = name;
        this.parent = parent;
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
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
