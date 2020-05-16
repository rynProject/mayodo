package com.mayodo.news.Home;

public class NavigationDrawerModel {
    private int image;
    private String id;
    private String fchar;
    private String name;
    private String subId;

    String parentCatID;
    String SubCatID;


    String SubCatName;
    String SubCatFirstChar;

    public NavigationDrawerModel(String parentCatID, String subCatID, String subCatName, String subCatFirstChar) {
        this.parentCatID = parentCatID;
        SubCatID = subCatID;
        SubCatName = subCatName;
        SubCatFirstChar = subCatFirstChar;
    }


    public String getParentCatID() {
        return parentCatID;
    }

    public void setParentCatID(String parentCatID) {
        this.parentCatID = parentCatID;
    }

    public String getSubCatID() {
        return SubCatID;
    }

    public void setSubCatID(String subCatID) {
        SubCatID = subCatID;
    }

    public String getSubCatName() {
        return SubCatName;
    }

    public void setSubCatName(String subCatName) {
        SubCatName = subCatName;
    }

    public String getSubCatFirstChar() {
        return SubCatFirstChar;
    }

    public void setSubCatFirstChar(String subCatFirstChar) {
        SubCatFirstChar = subCatFirstChar;
    }

    public NavigationDrawerModel() {

    }

    public NavigationDrawerModel(String id, String subId) {
        this.id = id;
        this.subId = subId;
    }

    boolean isSelected = false;
    public NavigationDrawerModel(String id, String name, String fchar, boolean isSelected, String subId) {

        this.id = id;
        this.name = name;
        this.fchar = fchar;
        this.subId=subId;
        this.isSelected = isSelected ;
    }

    public String getSubId() {
        return subId;
    }
    public void setSubId(String subId) {
        this.subId = subId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFchar() {
        return fchar;
    }

    public void setFchar(String fchar) {
        this.fchar = fchar;
    }
}
