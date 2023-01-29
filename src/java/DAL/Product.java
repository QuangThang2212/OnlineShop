/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAL;

/**
 *
 * @author PQT2212
 */
public class Product {

    private int productID;
    private String productName;
    private int categoryID;
    private String quantityPerUnit;
    private double unitPrice;
    private int unitsInStock;
    private int unitsOnOrder;
    private int reorderLevel;
    private boolean discontinued;

    public Product() {
    }

    public Product(int productID, String productName, int categoryID, String quantityPerUnit, double unitPrice, int unitsInStock, int unitsOnOrder, int reorderLevel, boolean discontinued) {
        this.productID = productID;
        this.productName = productName;
        this.categoryID = categoryID;
        this.quantityPerUnit = quantityPerUnit;
        this.unitPrice = unitPrice;
        this.unitsInStock = unitsInStock;
        this.unitsOnOrder = unitsOnOrder;
        this.reorderLevel = reorderLevel;
        this.discontinued = discontinued;
    }

    public Product(int productID, String productName, int categoryID, String quantityPerUnit, boolean discontinued) {
        this.productID = productID;
        this.productName = productName;
        this.categoryID = categoryID;
        this.quantityPerUnit = quantityPerUnit;
        this.discontinued = discontinued;
    }

    public Product(String productName, int categoryID, String quantityPerUnit, boolean discontinued) {
        this.productName = productName;
        this.categoryID = categoryID;
        this.quantityPerUnit = quantityPerUnit;
        this.discontinued = discontinued;
    }

    public Product(int productID, String productName, int categoryID, double unitPrice) {
        this.productID = productID;
        this.productName = productName;
        this.categoryID = categoryID;
        this.unitPrice = unitPrice;
    }

    public Product(int productID, String productName, double unitPrice) {
        this.productID = productID;
        this.productName = productName;
        this.unitPrice = unitPrice;
    }

    public Product(int unitsInStock, int unitsOnOrder) {
        this.unitsInStock = unitsInStock;
        this.unitsOnOrder = unitsOnOrder;
    }

    public Product(int categoryID, String productName, String quantityPerUnit, float unitPrice, int unitsInStock, boolean discontinued) {
        this.productName = productName;
        this.categoryID = categoryID;
        this.quantityPerUnit = quantityPerUnit;
        this.unitPrice = unitPrice;
        this.unitsInStock = unitsInStock;
        this.discontinued = discontinued;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getQuantityPerUnit() {
        return quantityPerUnit;
    }

    public void setQuantityPerUnit(String quantityPerUnit) {
        this.quantityPerUnit = quantityPerUnit;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getUnitsInStock() {
        return unitsInStock;
    }

    public void setUnitsInStock(int unitInStock) {
        this.unitsInStock = unitInStock;
    }

    public int getUnitsOnOrder() {
        return unitsOnOrder;
    }

    public void setUnitsOnOrder(int unitOnOrder) {
        this.unitsOnOrder = unitOnOrder;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public boolean isDiscontinued() {
        return discontinued;
    }

    public void setDiscontinued(boolean discontinued) {
        this.discontinued = discontinued;
    }

    @Override
    public String toString() {
        return "Product{" + "productID=" + productID + ", productName=" + productName + ", categoryID=" + categoryID + ", quantityPerUnit=" + quantityPerUnit + ", unitPrice=" + unitPrice + ", unitsInStock=" + unitsInStock + ", unitsOnOrder=" + unitsOnOrder + ", reorderLevel=" + reorderLevel + ", discontinued=" + discontinued + '}';
    }

}
