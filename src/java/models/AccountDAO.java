/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import DAL.Account;
import DTO.AccountCustomer;
import DAL.Customers;
import DTO.DBContext;
import DAL.Employee;
import DTO.RandomString;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
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
public class AccountDAO {

    private Connection connection;
    private PreparedStatement ps;
    private ResultSet rs;

    public AccountCustomer getAccountCustomer(String email, String pass) {
        Account account = null;
        AccountCustomer accountCustomer = null;
        try {
            connection = DBContext.getInstance().getConnection();
            String sql = "SELECT * FROM Accounts a LEFT JOIN Customers b ON a.CustomerID=b.CustomerID LEFT JOIN Employees c on a.employeeID = c.employeeID WHERE a.Email=? AND a.Password=?";
            ps = connection.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, pass);
            rs = ps.executeQuery();
            while (rs.next()) {
                int AccountID = rs.getInt("AccountID");
                String Email = rs.getString("Email");
                String Password = rs.getString("Password");
                String CustomerID = rs.getString("CustomerID");
                int EmployeeID = rs.getInt("EmployeeID");
                int Role = rs.getInt("Role");
                account = new Account(AccountID, Email, Password, CustomerID, EmployeeID, Role);
                if (Role == 2) {
                    String customerID = rs.getString("CustomerID");
                    String CompanyName = rs.getString("CompanyName");
                    String ContactName = rs.getString("ContactName");
                    String ContactTitle = rs.getString("ContactTitle");
                    String Address = rs.getString("Address");
                    Date CreateDate = rs.getDate("CreateDate");

                    Customers customer = new Customers(customerID, CompanyName, ContactName, ContactTitle, Address, CreateDate);
                    accountCustomer = new AccountCustomer(account, customer);
                } else {
                    int employeeID = rs.getInt("EmployeeID");
                    String firstName = rs.getString("FirstName");
                    String lastName = rs.getString("LastName");
                    int DepartmentID = rs.getInt("DepartmentID");
                    String title = rs.getString("Title");
                    String titleOfCourtesy = rs.getString("TitleOfCourtesy");
                    Date birthDate = rs.getDate("BirthDate");
                    Date hireDate = rs.getDate("HireDate");

                    Employee employee = new Employee(employeeID, firstName, lastName, DepartmentID, title, titleOfCourtesy, birthDate, hireDate);
                    accountCustomer = new AccountCustomer(account, employee);
                }

            }
        } catch (SQLException e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.WARNING, e.getMessage(), e);
        } finally {
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return accountCustomer;
    } 

    public int insertBothAccountAndCustomer(Account account, Customers customer) throws SQLException {
        int check = 0;
        Random random = new Random();

        try {
            int checkAccount = 0;
            int checkCustomer = 0;
            connection = DBContext.getInstance().getConnection();
            connection.setAutoCommit(false);

            String sqlCustomer = "INSERT INTO Customers VALUES(?,?,?,?,?,?)";
            ps = connection.prepareStatement(sqlCustomer);

            RandomString randomString = new RandomString(5, random, RandomString.alphanum);
            String CustomerID = randomString.nextString();
            ps.setString(1, CustomerID);
            ps.setString(2, customer.getCompanyName());
            ps.setString(3, customer.getContactName());
            ps.setString(4, customer.getContactTitle());
            ps.setString(5, customer.getAddress());
            ps.setDate(6, Date.valueOf(LocalDate.now()));
            checkAccount = ps.executeUpdate();

            String sqlAccount = "INSERT INTO Accounts VALUES(?,?,?,?,?)";
            ps = connection.prepareStatement(sqlAccount);
            ps.setString(1, account.getEmail());
            ps.setString(2, account.getPassword());
            ps.setString(3, CustomerID);
            if (account.getEmployeeID() == 0) {
                ps.setNull(4, Types.INTEGER);
            } else {
                ps.setInt(4, account.getEmployeeID());
            }
            ps.setInt(5, account.getRole());
            checkCustomer = ps.executeUpdate();

            if (checkAccount != 0 && checkCustomer != 0) {
                check = 1;
            } else {
                check = 0;
            }
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

    public int insertAccount(Account account) throws SQLException {
        int check = 0;
        try {
            connection = DBContext.getInstance().getConnection();
            connection.setAutoCommit(false);

            String sqlAccount = "INSERT INTO Accounts VALUES(?,?,?,?,?)";
            ps = connection.prepareStatement(sqlAccount);
            ps.setString(1, account.getEmail());
            ps.setString(2, account.getPassword());
            ps.setString(3, account.getCustomerID());
            if (account.getEmployeeID() == 0) {
                ps.setNull(4, Types.INTEGER);
            } else {
                ps.setInt(4, account.getEmployeeID());
            }
            ps.setInt(5, account.getRole());
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

    public int updateAccountPass(Account account) throws SQLException {
        int check = 0;
        try {
            connection = DBContext.getInstance().getConnection();
            connection.setAutoCommit(false);

            String sql = "UPDATE Accounts SET Password = ? WHERE AccountID=?";
            ps = connection.prepareStatement(sql);

            ps.setString(1, account.getPassword());
            ps.setInt(2, account.getAccountID());
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

    public Account getAccountByEmail(String email) {
        Account account = null;
        try {
            connection = DBContext.getInstance().getConnection();
            String sql = "SELECT * FROM Accounts WHERE Email=?";
            ps = connection.prepareStatement(sql);
            ps.setString(1, email);
            rs = ps.executeQuery();
            while (rs.next()) {
                int AccountID = rs.getInt("AccountID");
                String Email = rs.getString("Email");
                String Password = rs.getString("Password");
                String CustomerID = rs.getString("CustomerID");
                int EmployeeID = rs.getInt("EmployeeID");
                int Role = rs.getInt("Role");

                account = new Account(AccountID, Email, Password, CustomerID, EmployeeID, Role);
            }
        } catch (SQLException e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.WARNING, e.getMessage(), e);
        } finally {
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return account;
    }

    public List<Account> getAccountByCustomerID(String id) {
        List<Account> accounts = new ArrayList<>();
        try {
            connection = DBContext.getInstance().getConnection();
            String sql = "SELECT * FROM Accounts where CustomerID=?";
            ps = connection.prepareStatement(sql);
            ps.setString(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                int AccountID = rs.getInt("AccountID");
                String Email = rs.getString("Email");
                String Password = rs.getString("Password");
                String CustomerID = rs.getString("CustomerID");
                int EmployeeID = rs.getInt("EmployeeID");
                int Role = rs.getInt("Role");

                accounts.add(new Account(AccountID, Email, Password, CustomerID, EmployeeID, Role));
            }
        } catch (SQLException e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.WARNING, e.getMessage(), e);
        } finally {
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return accounts;
    }
}
