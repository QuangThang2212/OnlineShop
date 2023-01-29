/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import DAL.Account;
import DTO.AccountCustomer;
import DAL.Customers;
import DTO.StringHandling;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.AccountDAO;
import models.CustomersDao;

/**
 *
 * @author PQT2212
 */
public class SignUpController extends HttpServlet {

    AccountDAO accountDAO = new AccountDAO();
    CustomersDao customersDao = new CustomersDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().removeAttribute("Acc");
        req.getSession().removeAttribute("productsCart");
        req.getRequestDispatcher("../signup.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //validation
        boolean validationCus = (boolean) req.getAttribute("validationCus");
        boolean validationEmail = (boolean) req.getAttribute("validationEmail");
        boolean validationPass = (boolean) req.getAttribute("validationPass");
        if (validationCus == false || validationEmail == false || validationPass == false) {
            req.getRequestDispatcher("../signup.jsp").forward(req, resp);
            return;
        }
        
        String email = (String) req.getAttribute("emailFilter");
        String pass = (String) req.getAttribute("encryptionPass");
        Account account = new Account(email, pass, 0, 2);
        Customers customers = (Customers) req.getAttribute("customersFilter");

        req.setAttribute("accountSignUp", account);
        req.setAttribute("customerSignUp", customers);

        //check mail exist
        //mail exist in database -> stop sign up
        Account emailExist = (Account) req.getAttribute("emailExist");
        if (emailExist != null) {
            req.setAttribute("emailExistMess", "Email exist - please use different email");
            req.getRequestDispatcher("../signup.jsp").forward(req, resp);
            return;
        }

        //get list of customers by sign up information
        //list empty -> insert both account and customer in one action
        //list not empty -> customer infor had been assigned -> stop sign up
        //list not empty -> customer infor hadn't be assigned -> sign up new account with the customer
        int checkAccount = 0;
        String customerExist = (String) req.getAttribute("customerExist");
        try {
            if ("".equals(customerExist)) {
                checkAccount = accountDAO.insertBothAccountAndCustomer(account, customers);
            } else {
                List<Account> checkCustomerID = accountDAO.getAccountByCustomerID(customerExist);
                if (checkCustomerID.isEmpty()) {
                    account.setCustomerID(customerExist);
                    checkAccount = accountDAO.insertAccount(account);
                } else {
                    req.setAttribute("customerExistMess", "This customer information had been register in system");
                    req.getRequestDispatcher("../signup.jsp").forward(req, resp);
                    return;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(SignUpController.class.getName()).log(Level.WARNING, null, ex);
            req.setAttribute("signUpMess", "Sign up false");
            req.getRequestDispatcher("../signup.jsp").forward(req, resp);
            return;
        }

        //check sign up action after execute
        if (checkAccount == 0) {
            req.setAttribute("msg", "Sign up false");
        } else {
            req.getSession().removeAttribute("productsCart");
            req.setAttribute("msg", "Sign up success, please sign in for more service");            
        }
        req.getRequestDispatcher("../signin.jsp").forward(req, resp);
    }

}
