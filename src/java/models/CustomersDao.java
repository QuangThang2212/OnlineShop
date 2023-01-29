/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import DAL.Account;
import DAL.Customers;
import DTO.AccountCustomer;
import DTO.DBContext;
import DTO.RandomString;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author PQT2212
 */
public class CustomersDao {

    private Customers customer = null;
    private Connection connection;
    private PreparedStatement ps;
    private ResultSet rs;

    public Customers getCustomerByCustomerID(String CustomerID) throws SQLException {
        try {
            connection = DBContext.getInstance().getConnection();
            String sql = "SELECT * FROM Customers WHERE CustomerID=?";
            ps = connection.prepareStatement(sql);
            ps.setString(1, CustomerID);
            rs = ps.executeQuery();
            while (rs.next()) {
                String customerID = rs.getString("CustomerID");
                String CompanyName = rs.getString("CompanyName");
                String ContactName = rs.getString("ContactName");
                String ContactTitle = rs.getString("ContactTitle");
                String Address = rs.getString("Address");
                Date CreateDate = rs.getDate("CreateDate");
                customer = new Customers(customerID, CompanyName, ContactName, ContactTitle, Address, CreateDate);
            }
        } catch (SQLException e) {
            Logger.getLogger(CustomersDao.class.getName()).log(Level.WARNING, e.getMessage(), e);
        } finally {
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return customer;
    }

    public List<AccountCustomer> getAccountCustomer() {
        List<AccountCustomer> accountCustomer = new ArrayList<>();
        try {
            connection = DBContext.getInstance().getConnection();
            String sql = "SELECT * FROM Customers a LEFT JOIN Accounts b ON a.CustomerID=b.CustomerID";
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                int AccountID = rs.getInt("AccountID");
                String Email = rs.getString("Email");
                String Password = rs.getString("Password");
                int EmployeeID = rs.getInt("EmployeeID");
                int Role = rs.getInt("Role");                

                String customerID = rs.getString("CustomerID");
                String CompanyName = rs.getString("CompanyName");
                String ContactName = rs.getString("ContactName");
                String ContactTitle = rs.getString("ContactTitle");
                String Address = rs.getString("Address");
                Date CreateDate = rs.getDate("CreateDate");
                
                Account account = new Account(AccountID, Email, Password, customerID, EmployeeID, Role);

                customer = new Customers(customerID, CompanyName, ContactName, ContactTitle, Address, CreateDate);
                accountCustomer.add(new AccountCustomer(account, customer));

            }
        } catch (SQLException e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.WARNING, e.getMessage(), e);
        } finally {
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return accountCustomer;
    }
    public List<AccountCustomer> getAccountCustomerByCompanyNameContactNameCustomerIDOrContacttitle(String searchInput) {
        List<AccountCustomer> accountCustomer = new ArrayList<>();
        try {
            connection = DBContext.getInstance().getConnection();
            String sql = "SELECT * FROM Customers a LEFT JOIN Accounts b ON a.CustomerID=b.CustomerID WHERE a.CustomerID LIKE '%'+?+'%' OR CompanyName LIKE '%'+?+'%' OR ContactName LIKE '%'+?+'%' OR ContactTitle LIKE '%'+?+'%'";
            ps = connection.prepareStatement(sql);
            ps.setString(1, searchInput);
            ps.setString(2, searchInput);
            ps.setString(3, searchInput);
            ps.setString(4, searchInput);
            rs = ps.executeQuery();
            while (rs.next()) {
                int AccountID = rs.getInt("AccountID");
                String Email = rs.getString("Email");
                String Password = rs.getString("Password");
                int EmployeeID = rs.getInt("EmployeeID");
                int Role = rs.getInt("Role");                

                String customerID = rs.getString("CustomerID");
                String CompanyName = rs.getString("CompanyName");
                String ContactName = rs.getString("ContactName");
                String ContactTitle = rs.getString("ContactTitle");
                String Address = rs.getString("Address");
                Date CreateDate = rs.getDate("CreateDate");
                
                Account account = new Account(AccountID, Email, Password, customerID, EmployeeID, Role);

                customer = new Customers(customerID, CompanyName, ContactName, ContactTitle, Address, CreateDate);
                accountCustomer.add(new AccountCustomer(account, customer));

            }
        } catch (SQLException e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.WARNING, e.getMessage(), e);
        } finally {
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return accountCustomer;
    }
    public List<Customers> getCustomerByAllInfor(Customers customers) {
        List<Customers> list = new ArrayList<>();
        try {
            connection = DBContext.getInstance().getConnection();
            String sql = "SELECT * FROM Customers WHERE CompanyName=? AND ContactName=? AND ContactTitle=? AND Address=?";
            ps = connection.prepareStatement(sql);
            ps.setString(1, customers.getCompanyName());
            ps.setString(2, customers.getContactName());
            ps.setString(3, customers.getContactTitle());
            ps.setString(4, customers.getAddress());

            rs = ps.executeQuery();
            while (rs.next()) {
                String customerID = rs.getString("CustomerID");
                String CompanyName = rs.getString("CompanyName");
                String ContactName = rs.getString("ContactName");
                String ContactTitle = rs.getString("ContactTitle");
                String Address = rs.getString("Address");
                Date CreateDate = rs.getDate("CreateDate");
                list.add(new Customers(customerID, CompanyName, ContactName, ContactTitle, Address, CreateDate));
            }
        } catch (SQLException e) {
            Logger.getLogger(CustomersDao.class.getName()).log(Level.WARNING, e.getMessage(), e);
        } finally {
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return list;
    }

    public String insertCustomer(Customers customer) throws SQLException {
        Random random = new Random();
        RandomString randomString = new RandomString(5, random, RandomString.alphanum);
        String CustomerID = randomString.nextString();
        try {
            connection = DBContext.getInstance().getConnection();
            connection.setAutoCommit(false);

            String sql = "INSERT INTO Customers VALUES(?,?,?,?,?,?)";
            ps = connection.prepareStatement(sql);

            ps.setString(1, CustomerID);
            ps.setString(2, customer.getCompanyName());
            ps.setString(3, customer.getContactName());
            ps.setString(4, customer.getContactTitle());
            ps.setString(5, customer.getAddress());
            ps.setDate(6, Date.valueOf(LocalDate.now()));
            ps.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            Logger.getLogger(CustomersDao.class.getName()).log(Level.WARNING, e.getMessage(), e);
            connection.rollback();
            return null;
        } finally {
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return CustomerID;
    }

    public int updateCustomer(Customers customers) throws SQLException {
        int check = 0;
        try {
            connection = DBContext.getInstance().getConnection();
            connection.setAutoCommit(false);

            String sql = "UPDATE Customers SET CompanyName = ?,ContactName = ?,ContactTitle = ?,[Address] = ? WHERE CustomerID = ?";
            ps = connection.prepareStatement(sql);

            ps.setString(1, customers.getCompanyName());
            ps.setString(2, customers.getContactName());
            ps.setString(3, customers.getContactTitle());
            ps.setString(4, customers.getAddress());
            ps.setString(5, customers.getCustomerID());
            check = ps.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            Logger.getLogger(CustomersDao.class.getName()).log(Level.WARNING, e.getMessage(), e);
            connection.rollback();
            return 0;
        } finally {
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return check;
    }

    public int getNumberOfCustomer() {
        int numberOfCustomer = 0;
        try {
            connection = DBContext.getInstance().getConnection();
            String sql = "SELECT count(a.CustomerID) as NumberOfCustomer FROM Customers a";
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                numberOfCustomer = rs.getInt("NumberOfCustomer");
            }
        } catch (SQLException e) {
            Logger.getLogger(CustomersDao.class.getName()).log(Level.WARNING, e.getMessage(), e);
        } finally {
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return numberOfCustomer;
    }

    public int getNumberOfGuest(){
        int numberOfCustomer = 0;
        try {
            connection = DBContext.getInstance().getConnection();
            String sql = "select count(a.CustomerID)as NumberOfCustomer\n"
                    + "from Customers a left join Accounts b on a.CustomerID = b.CustomerID where b.AccountID IS NULL";
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                numberOfCustomer = rs.getInt("NumberOfCustomer");
            }
        } catch (SQLException e) {
            Logger.getLogger(CustomersDao.class.getName()).log(Level.WARNING, e.getMessage(), e);
        } finally {
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return numberOfCustomer;
    }

    public int getNumberOfNewCutomerBy30Date(){
        int numberOfCustomer = 0;
        try {
            connection = DBContext.getInstance().getConnection();
            String sql =  "select count(a.CustomerID) as NumberOfCustomer\n"
                        + "from Customers a where a.CreateDate>=? AND a.CreateDate<=?";
            ps = connection.prepareStatement(sql);
            ps.setDate(1, Date.valueOf(LocalDate.now().minusDays(30)));
            ps.setDate(2, Date.valueOf(LocalDate.now()));
            rs = ps.executeQuery();
            while (rs.next()) {
                numberOfCustomer = rs.getInt("NumberOfCustomer");
            }
        } catch (SQLException e) {
            Logger.getLogger(CustomersDao.class.getName()).log(Level.WARNING, e.getMessage(), e);
        } finally {
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return numberOfCustomer;
    }
}
