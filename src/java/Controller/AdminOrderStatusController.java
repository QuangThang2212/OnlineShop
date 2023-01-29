/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import DTO.AccountCustomer;
import DAL.Order;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.OrderDao;

/**
 *
 * @author PQT2212
 */
public class AdminOrderStatusController extends HttpServlet {

    OrderDao orderDao = new OrderDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AccountCustomer accountCustomer = (AccountCustomer) req.getSession().getAttribute("Acc");

        int orderID;
        try {
            orderID = Integer.parseInt(req.getParameter("id"));
        } catch (NumberFormatException e) {
            Logger.getLogger(AdminOrderStatusController.class.getName()).log(Level.WARNING, null, e);
            req.getSession().setAttribute("actionMsg", "Order id (" + req.getParameter("id") + ") isnt available");
            resp.sendRedirect("./admin/order");
            return;
        }
        Order check = orderDao.getOrderByOrderID(orderID);
        if (check == null) {
            Logger.getLogger(AdminOrderStatusController.class.getName()).log(Level.WARNING, null, "Order id (" + req.getParameter("id") + ") isnt exist in database");
            req.setAttribute("actionMsg", "Order id (" + req.getParameter("id") + ") isnt exist in database");
            resp.sendRedirect("./admin/order");
            return;
        } else if (check.getStatus().equals("Order canceled") || check.getStatus().equals("Completed")) {
            Logger.getLogger(AdminOrderStatusController.class.getName()).log(Level.WARNING, null, "Order id (" + req.getParameter("id") + ") had finished  and set to status: " + check.getStatus());
            req.getSession().setAttribute("actionMsg", "Order id (" + req.getParameter("id") + ") had finished  and set to status: " + check.getStatus());
            resp.sendRedirect("./admin/order");
            return;
        }

        String action = req.getParameter("action");
        if (action == null) {
            action = "";
        }
        switch (action) {
            case "cancel":
                try {
                int psUpdateOrder = orderDao.updateCancelOrderByOrderID(orderID, accountCustomer);
                if (psUpdateOrder == 0) {
                    Logger.getLogger(AdminOrderStatusController.class.getName()).log(Level.SEVERE, null, "Order id (" + req.getParameter("id") + ") cancel fail");
                    req.getSession().setAttribute("actionMsg", "Order id (" + req.getParameter("id") + ") cancel fail");
                } else {
                    req.getSession().setAttribute("actionMsg", "Order id (" + req.getParameter("id") + ") cancel successfully");
                }
            } catch (SQLException ex) {
                Logger.getLogger(AdminOrderStatusController.class.getName()).log(Level.SEVERE, null, ex);
                req.getSession().setAttribute("actionMsg", "Order id (" + req.getParameter("id") + ") cancel fail");
            }
            break;
            case "complete":
                try {
                int psUpdateOrder = orderDao.updateCompleteOrderByOrderID(orderID, accountCustomer.getEmployee().getEmployeeID());
                if (psUpdateOrder == 0) {
                    Logger.getLogger(AdminOrderStatusController.class.getName()).log(Level.SEVERE, null, "Order id (" + req.getParameter("id") + ") Compplete fail");
                    req.getSession().setAttribute("actionMsg", "Order id (" + req.getParameter("id") + ") Complete fail");
                } else {
                    req.getSession().setAttribute("actionMsg", "Order id (" + req.getParameter("id") + ") confirm successfully");
                }
            } catch (SQLException ex) {
                Logger.getLogger(AdminOrderStatusController.class.getName()).log(Level.SEVERE, null, ex);
                req.getSession().setAttribute("actionMsg", "Order id (" + req.getParameter("id") + ") Complete fail");
            }
            break;
            default:
                Logger.getLogger(AdminOrderStatusController.class.getName()).log(Level.SEVERE, null, "Action (" + action + ") not available");
                req.setAttribute("actionMsg", "Action (" + action + ") not available");
                break;
        }

        resp.sendRedirect("./admin/order");
    }

}
