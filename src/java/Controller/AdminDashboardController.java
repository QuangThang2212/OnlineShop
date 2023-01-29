/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import DAL.Order;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.CustomersDao;
import models.OrderDao;

/**
 *
 * @author PQT2212
 */
public class AdminDashboardController extends HttpServlet {

    CustomersDao customerDao = new CustomersDao();
    OrderDao orderDao = new OrderDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        if(id==null){
            id = "";
        }

        if (!"".equals(id)) {
            int orderID = 0;
            try {
                orderID = Integer.parseInt(id);
            } catch (NumberFormatException e) {
                Logger.getLogger(AdminDashboardController.class.getName()).log(Level.WARNING, null, e);
                req.setAttribute("adminOrderMsgWarn", "Order id (" + req.getParameter("id") + ") isnt available");
            }
            Order check = orderDao.getOrderByOrderID(orderID);
            if (check == null) {
                Logger.getLogger(AdminDashboardController.class.getName()).log(Level.WARNING, null, "Order id (" + req.getParameter("id") + ") isnt exist in database");
                req.setAttribute("adminOrderMsgWarn", "Order id (" + req.getParameter("id") + ") isnt exist in database");
            } else {
                req.setAttribute("order", check);
            }
            req.getRequestDispatcher("./adminDetail.jsp").forward(req, resp);
        } else {
            int numberOfCustomer = customerDao.getNumberOfCustomer();
            int numberOfGuest = customerDao.getNumberOfGuest();
            int newCustomer30day = customerDao.getNumberOfNewCutomerBy30Date();

            double weeklySale = orderDao.getWeeklySale();
            double totalOrder = orderDao.getTotalOrder();
            double orderStatistic[] = orderDao.getMonthOrderStatistic();
            String result = Arrays.toString(orderStatistic).substring(1, Arrays.toString(orderStatistic).length() - 1);

            req.setAttribute("numberOfCustomer", numberOfCustomer);
            req.setAttribute("numberOfGuest", numberOfGuest);
            req.setAttribute("newCustomer30day", newCustomer30day);

            req.setAttribute("weeklySale", weeklySale);
            req.setAttribute("totalOrder", totalOrder);
            req.setAttribute("orderStatistic", result);
            req.getRequestDispatcher("./adminDashboard.jsp").forward(req, resp);
        }
    }

}
