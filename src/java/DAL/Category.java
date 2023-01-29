/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAL;

/**
 *
 * @author PQT2212
 */
public class Category {
    private int categoryID;
    private String CategoryName;
    private String Description;
    private String Picture;

    public Category() {
    }

    public Category(int categoryID, String CategoryName, String Description, String Picture) {
        this.categoryID = categoryID;
        this.CategoryName = CategoryName;
        this.Description = Description;
        this.Picture = Picture;
    }

    public Category(int categoryID, String categoryName) {
        this.categoryID = categoryID;
        this.CategoryName = categoryName;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String CategoryName) {
        this.CategoryName = CategoryName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public String getPicture() {
        return Picture;
    }

    public void setPicture(String Picture) {
        this.Picture = Picture;
    }

    @Override
    public String toString() {
        return super.toString(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }
    
    
}
