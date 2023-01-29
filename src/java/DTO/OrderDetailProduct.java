/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

import DAL.OrderDetail;
import DAL.Product;

/**
 *
 * @author PQT2212
 */
public class OrderDetailProduct {

    private OrderDetail orderDetail;
    private Product product;

    public OrderDetailProduct() {
    }

    public OrderDetailProduct(OrderDetail orderDetail, Product product) {
        this.orderDetail = orderDetail;
        this.product = product;
    }

    public OrderDetail getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(OrderDetail orderDetail) {
        this.orderDetail = orderDetail;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

}
