/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import DAL.OrderDetail;
import DAL.Product;
import DTO.DBContext;
import DTO.OrderDetailProduct;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author PQT2212
 */
public class OrderDetailDAO {

    private Connection connection;
    private PreparedStatement ps;
    private ResultSet rs;

    public List<OrderDetailProduct> getOrderDetailByOrderID(int id) {
        List<OrderDetailProduct> orderDetailProducts = new ArrayList<>();
        try {
            connection = DBContext.getInstance().getConnection();
            String sql = "select * from [Order Details] a inner join Products b ON a.ProductID = b.ProductID  WHERE a.OrderID = ?";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                int orderID = rs.getInt("OrderID");
                int productID = rs.getInt("ProductID");
                double unitPrice = rs.getDouble("UnitPrice");
                int quantity = rs.getInt("Quantity");
                float discount = rs.getInt("Discount");
                
                String productName = rs.getString("ProductName");
                int categoryID = rs.getInt("CategoryID");
                String quantityPerUnit = rs.getString("QuantityPerUnit");
                int unitsInStock = rs.getInt("UnitsInStock");
                int unitsOnOrder = rs.getInt("UnitsOnOrder");
                int reorderLevel = rs.getInt("ReorderLevel");
                boolean discontinued = rs.getBoolean("Discontinued");
                
                OrderDetail detail = new OrderDetail(orderID, productID, unitPrice, quantity, discount);
                Product product = new Product(productID, productName, categoryID, quantityPerUnit, unitPrice, unitsInStock, unitsOnOrder, reorderLevel, discontinued);
                
                orderDetailProducts.add(new OrderDetailProduct(detail, product));
            }
        } catch (SQLException e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.WARNING, e.getMessage(), e);
        } finally {
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return orderDetailProducts;
    }
}
