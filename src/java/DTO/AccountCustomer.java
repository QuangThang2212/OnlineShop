/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

import DAL.Account;
import DAL.Customers;
import DAL.Employee;

/**
 *
 * @author PQT2212
 */
public class AccountCustomer {

    private Account account;
    private Customers customers;
    private Employee Employee;

    public AccountCustomer() {
    }

    public AccountCustomer(Account account, Customers customers) {
        this.account = account;
        this.customers = customers;
    }

    public AccountCustomer(Account account, Customers customers, Employee Employee) {
        this.account = account;
        this.customers = customers;
        this.Employee = Employee;
    }

    public AccountCustomer(Account account, Employee employee) {
        this.account = account;
        this.Employee = employee;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Customers getCustomers() {
        return customers;
    }

    public void setCustomers(Customers customers) {
        this.customers = customers;
    }

    public Employee getEmployee() {
        return Employee;
    }

    public void setEmployee(Employee Employee) {
        this.Employee = Employee;
    }

}
