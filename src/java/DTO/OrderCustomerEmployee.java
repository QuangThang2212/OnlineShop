/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

import DAL.Customers;
import DAL.Employee;
import DAL.Order;

/**
 *
 * @author PQT2212
 */
public class OrderCustomerEmployee {
    private Order order;
    private Customers customers;
    private Employee employee;

    public OrderCustomerEmployee() {
    }

    public OrderCustomerEmployee(Order order, Customers customers, Employee employee) {
        this.order = order;
        this.customers = customers;
        this.employee = employee;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Customers getCustomers() {
        return customers;
    }

    public void setCustomers(Customers customers) {
        this.customers = customers;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    
    
}
