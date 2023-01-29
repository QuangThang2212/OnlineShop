/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

import DAL.Category;
import DAL.Product;

/**
 *
 * @author PQT2212
 */
public class ProductCategory {
    private Product product;
    private Category category;

    public ProductCategory() {
    }

    public ProductCategory(Product product, Category category) {
        this.product = product;
        this.category = category;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
    
    
}
