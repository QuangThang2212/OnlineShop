/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAL;

import java.sql.Date;
import java.time.LocalDate;

/**
 *
 * @author PQT2212
 */
public class Customers {

    private String CustomerID;
    private String CompanyName;
    private String ContactName;
    private String ContactTitle;
    private String Address;
    private Date CreateDate;

    public Customers(String CustomerID, String CompanyName, String ContactName, String ContactTitle, String Address, Date CreateDate) {
        this.CustomerID = CustomerID;
        this.CompanyName = CompanyName;
        this.ContactName = ContactName;
        this.ContactTitle = ContactTitle;
        this.Address = Address;
        this.CreateDate = CreateDate;
    }

    public Customers(String CompanyName, String ContactName, String ContactTitle, String Address) {
        this.CompanyName = CompanyName;
        this.ContactName = ContactName;
        this.ContactTitle = ContactTitle;
        this.Address = Address;
    }

    public Customers(String customerID, String CompanyName, String ContactName, String ContactTitle, String Address) {
        this.CustomerID = customerID;
        this.CompanyName = CompanyName;
        this.ContactName = ContactName;
        this.ContactTitle = ContactTitle;
        this.Address = Address;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String CustomerID) {
        this.CustomerID = CustomerID;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String CompanyName) {
        this.CompanyName = CompanyName;
    }

    public String getContactName() {
        return ContactName;
    }

    public void setContactName(String ContactName) {
        this.ContactName = ContactName;
    }

    public String getContactTitle() {
        return ContactTitle;
    }

    public void setContactTitle(String ContactTitle) {
        this.ContactTitle = ContactTitle;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public Date getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(Date CreateDate) {
        this.CreateDate = CreateDate;
    }

    @Override
    public String toString() {
        return "Customers{" + "CustomerID=" + CustomerID + ", CompanyName=" + CompanyName + ", ContactName=" + ContactName + ", ContactTitle=" + ContactTitle + ", Address=" + Address + ", CreateDate=" + CreateDate + '}';
    }

}
