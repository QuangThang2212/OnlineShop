/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import DAL.Account;
import DAL.Customers;
import DTO.AccountCustomer;
import DAL.Order;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.AccountDAO;
import models.CustomersDao;
import models.OrderDao;

/**
 *
 * @author PQT2212
 */
public class ProfileController extends HttpServlet {

    OrderDao orderDao = new OrderDao();
    AccountDAO accountDAO = new AccountDAO();
    CustomersDao customersDao = new CustomersDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AccountCustomer accountCustomer = (AccountCustomer) req.getSession().getAttribute("Acc");
        List<Order> orders = new ArrayList<>();

        String profileFun = req.getParameter("profileFun");
        if (profileFun == null) {
            profileFun = "";
        }

        switch (profileFun) {
            case "Personal-infor":
                req.getSession().setAttribute("profileFunStatus", "Personal-infor");
                break;
            case "All-Order":
                orders.addAll(orderDao.getOrderByCustomerIDAndStatus(accountCustomer.getCustomers().getCustomerID(), "Pending"));
                orders.addAll(orderDao.getOrderByCustomerIDAndStatus(accountCustomer.getCustomers().getCustomerID(), "Completed"));
                req.getSession().setAttribute("profileFunStatus", "All-Order");
                break;
            case "Cancel-Order":
                orders = orderDao.getOrderByCustomerIDAndStatus(accountCustomer.getCustomers().getCustomerID(), "Order canceled");
                req.getSession().setAttribute("profileFunStatus", "Cancel-Order");
                break;
            case "Edit-infor":
                req.getRequestDispatcher("/signup.jsp").forward(req, resp);
                return;
            case "Change-pass":
                req.getRequestDispatcher("/changePassword.jsp").forward(req, resp);
                return;
            default:
                Logger.getLogger(SignUpController.class.getName()).log(Level.WARNING, null, "Invalid profile request (" + profileFun + ")");
                req.getSession().setAttribute("homeMsg", "Invalid profile request (" + profileFun + ")");
                resp.sendRedirect("./home");
                return;
        }

        //sort orders by DESC orderDate, use cmparator and write by lambra expression
        Collections.sort(orders, (Order o1, Order o2) -> {
            if (o1.getOrderID() > o2.getOrderID()) {
                return -1;
            } else {
                return 1;
            }
        });
        req.setAttribute("orderHistoryList", orders);

        req.getRequestDispatcher("profile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AccountCustomer accountCustomer = (AccountCustomer) req.getSession().getAttribute("Acc");
        String postAction = req.getParameter("postAction");
        if (postAction == null) {
            postAction = "";
        }
        switch (postAction) {
            case "edit-infor":
                boolean validation = (boolean) req.getAttribute("validationCus");
                if (validation == false) {
                    resp.sendRedirect("./profile?profileFun=Edit-infor");
                    return;
                }
                Customers customers = (Customers) req.getAttribute("customersFilter");
                String customerExist = (String) req.getAttribute("customerExist");

                customers.setCustomerID(accountCustomer.getCustomers().getCustomerID());

                try {
                    int customerCheck = 0;
                    if ("".equals(customerExist)) {
                        customerCheck = customersDao.updateCustomer(customers);
                    } else {
                        req.getSession().setAttribute("customerExistMess", "This customer information had been register in system");
                        resp.sendRedirect("./profile?profileFun=Edit-infor");
                        return;
                    }
                    if (customerCheck == 0) {
                        req.getSession().setAttribute("editMsgWarn", "Update fail");
                    } else {
                        AccountCustomer newAccountCustomer = accountDAO.getAccountCustomer(accountCustomer.getAccount().getEmail(), accountCustomer.getAccount().getPassword());
                        req.getSession().setAttribute("Acc", newAccountCustomer);
                        req.getSession().setAttribute("editMsg", "Update successfully");
                    }
                } catch (IOException | SQLException ex) {
                    Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, null, ex);
                    req.getSession().setAttribute("editMsgWarn", "Update fail, exeption logged");
                }

                resp.sendRedirect("./profile?profileFun=Personal-infor");

                break;
            case "change-pass":
                boolean validationPass = (boolean) req.getAttribute("validationPass");
                if (validationPass == false) {
                    resp.sendRedirect("./profile?profileFun=Change-pass");
                    return;
                }
                String pass = (String) req.getAttribute("encryptionPass");
                Account account = accountCustomer.getAccount();
                account.setPassword(pass);

                int updateCheck=0;
                try {
                    updateCheck = accountDAO.updateAccountPass(account);
                } catch (SQLException ex) {
                    Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, null, ex);
                    req.setAttribute("changePassMsg", "Update password fail, Exeption exist");
                    req.getRequestDispatcher("./changePassword.jsp").forward(req, resp);
                    return;
                }
                
                if (updateCheck == 0) {
                    Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, null, "Get random email fail");
                    req.setAttribute("changePassMsg", "Update password fail");
                    req.getRequestDispatcher("./changePassword.jsp").forward(req, resp);
                } else {
                    req.getSession().setAttribute("editMsg", "Update password successfully");
                    resp.sendRedirect("./profile?profileFun=Personal-infor");
                }                

                break;
            case "cancel-order":
                int orderID;
                try {
                    orderID = Integer.parseInt(req.getParameter("id"));
                } catch (NumberFormatException e) {
                    Logger.getLogger(ProfileController.class.getName()).log(Level.WARNING, null, e);
                    req.setAttribute("homeMsg", "Order id (" + req.getParameter("id") + ") isnt available");
                    req.getRequestDispatcher("./home").forward(req, resp);
                    return;
                }

                Order check = orderDao.getOrderByOrderID(orderID);
                boolean checkAction = false;
                if (check == null) {
                    Logger.getLogger(ProfileController.class.getName()).log(Level.WARNING, null, "Order id (" + orderID + ") isnt exist in database");
                    req.setAttribute("homeMsg", "Order id (" + req.getParameter("id") + ") isnt exist in database");
                    checkAction = true;
                } else if (check.getStatus().equals("Order canceled") || check.getStatus().equals("Completed")) {
                    Logger.getLogger(ProfileController.class.getName()).log(Level.WARNING, null, "Order id (" + orderID + ") had finished  and set to status: " + check.getStatus());
                    req.setAttribute("homeMsg", "Order id (" + req.getParameter("id") + ") had finished  and set to status: " + check.getStatus());
                    checkAction = true;
                }
                if (checkAction) {
                    req.getRequestDispatcher("./home").forward(req, resp);
                    return;
                }

                int psUpdateOrder;
                try {
                    psUpdateOrder = orderDao.updateCancelOrderByOrderID(orderID, accountCustomer);
                } catch (SQLException ex) {
                    Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, null, ex);
                    req.setAttribute("OrderMsgWarn", "Order id (" + req.getParameter("id") + ") cancel fail");
                    req.getRequestDispatcher("./profile").forward(req, resp);
                    return;
                }
                if (psUpdateOrder == 0) {
                    Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, null, "Order id (" + orderID + ") cancel fail");
                    req.getSession().setAttribute("OrderMsgWarn", "Order id (" + orderID + ") cancel fail");
                } else {
                    req.getSession().setAttribute("OrderMsg", "Order id (" + orderID + ") cancel successfully");
                }
                resp.sendRedirect("./profile?profileFun=All-Order");

                break;
            default:
                Logger.getLogger(SignUpController.class.getName()).log(Level.WARNING, null, "Invalid profile post request (" + postAction + ")");
                req.getSession().setAttribute("homeMsg", "Invalid profile post request (" + postAction + ")");
                resp.sendRedirect("/home");
                break;
        }
    }

}
