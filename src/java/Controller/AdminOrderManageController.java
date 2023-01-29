/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import DTO.OrderCustomerEmployee;
import DTO.OrderCutomerEmployeePagination;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.OrderDao;

/**
 *
 * @author PQT2212
 */
public class AdminOrderManageController extends HttpServlet {

    OrderDao orderDaoAdmin = new OrderDao();
    OrderCutomerEmployeePagination orderPagination = new OrderCutomerEmployeePagination();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int currentPage = (int) req.getAttribute("currentPageFilter");
        String submitButton = req.getParameter("submitButton");
        if (submitButton == null) {
            submitButton = "";
        }
        List<OrderCustomerEmployee> oces = new ArrayList<>();
        switch (submitButton) {
            case "Filter":
                String startDate = req.getParameter("startDate");
                String endDate = req.getParameter("endDate");
                Date startDateValue = null;
                Date endDateValue = null;

                try {
                    startDateValue = Date.valueOf(startDate);
                    endDateValue = Date.valueOf(endDate);
                    oces = orderDaoAdmin.getOrderCustomerEmployeeByOrderDate(startDateValue, endDateValue);
                    System.out.println(oces.size());
                    req.setAttribute("startDate", startDate);
                    req.setAttribute("endDate", endDate);
                } catch (Exception e) {
                    oces = orderDaoAdmin.getOrderCustomerEmployee();
                }
                if (oces.isEmpty()) {
                    req.getSession().setAttribute("filterMsg", "No result for this filter");
                    req.setAttribute("currentPage", 1);
                    resp.sendRedirect("../admin/order");
                    return;
                }
                break;
            case "Search":
                String search = req.getParameter("searchInput");
                if ("".equals(search) || search == null) {
                    req.getSession().setAttribute("searchOrderMsg", "Not allow search empty");
                    resp.sendRedirect("../admin/order");
                    return;
                }
                req.setAttribute("searchInput", search);
                oces = orderDaoAdmin.getOrderCustomerEmployeeByCompanyNameOrCustomerID(search);
                if (oces.isEmpty()) {
                    req.getSession().setAttribute("filterMsg", "No result for this filter");
                    req.setAttribute("currentPage", 1);
                    resp.sendRedirect("../admin/order");
                    return;
                }
                break;
            default:
                oces = orderDaoAdmin.getOrderCustomerEmployee();
                break;
        }
        req.setAttribute("submitButton", submitButton);

        if (oces.isEmpty()) {
            req.setAttribute("actionMsg", "No order show for now");
            req.getRequestDispatcher("../adminOrder.jsp").forward(req, resp);
            return;
        }
        List<OrderCustomerEmployee> orders = orderPagination.getPageOfResult(oces, currentPage, OrderCutomerEmployeePagination.numberOfRowEachPage);
        int numberOfPage = orderPagination.getTotalPageOfResult(oces, OrderCutomerEmployeePagination.numberOfRowEachPage);

        if (orders == null) {
            req.getSession().setAttribute("msg", "Page " + req.getParameter("currentPage") + " not available");
            resp.sendRedirect("../account/signin");
            return;
        } else {
            req.setAttribute("orderPagination", orders);
            req.setAttribute("numberOfPage", numberOfPage);
        }
        req.setAttribute("currentPage", currentPage);
        req.getRequestDispatcher("../adminOrder.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String submitButton = req.getParameter("submitButton");
        int currentPage = 1;
        if (submitButton == null) {
            submitButton = "";
        }
        List<OrderCustomerEmployee> oces = new ArrayList<>();
        switch (submitButton) {
            case "Filter":
                String startDate = req.getParameter("txtStartOrderDate");
                String endDate = req.getParameter("txtEndOrderDate");
                Date startDateValue = null;
                Date endDateValue = null;

                try {
                    startDateValue = Date.valueOf(startDate);
                    endDateValue = Date.valueOf(endDate);
                    req.setAttribute("startDate", startDate);
                    req.setAttribute("endDate", endDate);
                } catch (IllegalArgumentException e) {
                    Logger.getLogger(SignUpController.class.getName()).log(Level.WARNING, null, e);
                    req.getSession().setAttribute("filterMsg", "Invalid startDate and endDate filter");
                    resp.sendRedirect("../admin/order");
                    return;
                }

                oces = orderDaoAdmin.getOrderCustomerEmployeeByOrderDate(startDateValue, endDateValue);
                if (oces.isEmpty()) {
                    req.getSession().setAttribute("filterMsg", "No result for this filter");
                    req.setAttribute("currentPage", 1);
                    resp.sendRedirect("../admin/order");
                    return;
                }
                break;
            case "Search":
                String search = req.getParameter("txtSearch");
                if ("".equals(search) || search == null) {
                    req.getSession().setAttribute("searchOrderMsg", "Not allow search empty");
                    resp.sendRedirect("../admin/order");
                    return;
                }
                req.setAttribute("searchInput", search);
                oces = orderDaoAdmin.getOrderCustomerEmployeeByCompanyNameOrCustomerID(search);
                if (oces.isEmpty()) {
                    req.getSession().setAttribute("filterMsg", "No result for this filter");
                    req.setAttribute("currentPage", 1);
                    resp.sendRedirect("../admin/order");
                    return;
                }
                break;
            default:
                req.getSession().setAttribute("actionMsg", "No order show for now");
                resp.sendRedirect("../admin/order");
                return;
        }
        req.setAttribute("submitButton", submitButton);

        List<OrderCustomerEmployee> ocesPagination = orderPagination.getPageOfResult(oces, currentPage, OrderCutomerEmployeePagination.numberOfRowEachPage);
        int numberOfPage = orderPagination.getTotalPageOfResult(oces, OrderCutomerEmployeePagination.numberOfRowEachPage);

        if (ocesPagination.isEmpty()) {
            req.getSession().setAttribute("msg", "Page " + req.getParameter("currentPage") + " not available");
            resp.sendRedirect("../account/signin");
            return;
        } else {
            req.setAttribute("orderPagination", ocesPagination);
            req.setAttribute("numberOfPage", numberOfPage);
        }
        req.setAttribute("currentPage", currentPage);
        req.getRequestDispatcher("../adminOrder.jsp").forward(req, resp);
    }

}
