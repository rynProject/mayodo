package com.mayodo.news.Home;

import java.util.ArrayList;

public class SubCategoriesModel {

    String parent;
    String CatID;
    String CatName;
    String CatFirstChar;
    ArrayList<SubCatModel> subCatArrList = new ArrayList<>();


    public SubCategoriesModel() {
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getCatID() {
        return CatID;
    }

    public void setCatID(String catID) {
        CatID = catID;
    }

    public String getCatName() {
        return CatName;
    }

    public void setCatName(String catName) {
        CatName = catName;
    }

    public String getCatFirstChar() {
        return CatFirstChar;
    }

    public void setCatFirstChar(String catFirstChar) {
        CatFirstChar = catFirstChar;
    }

    public ArrayList<SubCatModel> getSubCatArrList() {
        if (subCatArrList == null) {
            return new ArrayList<>();
        } else {
            return subCatArrList;
        }
    }

    public void setSubCatArrList(ArrayList<SubCatModel> subCatArrList) {
        this.subCatArrList = subCatArrList;
    }

    public class SubCatModel {
        String parentCatID;
        String SubCatID;
        String SubCatName;
        String SubCatFirstChar;


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
    }
}
