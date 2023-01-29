/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import DTO.AccountCustomer;
import DTO.AccountCustomerPagination;
import DTO.PaginationObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import models.CustomersDao;

/**
 *
 * @author PQT2212
 */
public class AdminCustomerController extends HttpServlet {

    CustomersDao customersDao = new CustomersDao();
    ;
    
    AccountCustomerPagination accCusPagi = new AccountCustomerPagination();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int currentPage = (int) req.getAttribute("currentPageFilter");
        
        String searchInput = req.getParameter("searchInput");
        if(searchInput==null){
            searchInput="";
        }

        List<AccountCustomer> accountCustomers = new ArrayList<>();
        if(searchInput.isEmpty()){
            accountCustomers = customersDao.getAccountCustomer();
        } else {
            accountCustomers = customersDao.getAccountCustomerByCompanyNameContactNameCustomerIDOrContacttitle(searchInput);
        }
        if (accountCustomers.isEmpty()) {
            req.setAttribute("actionCusMsg", "No customer found");
            req.getRequestDispatcher("../adminCustomer.jsp").forward(req, resp);
            return;
        }

        List<AccountCustomer> acs = accCusPagi.getPageOfResult(accountCustomers, currentPage, PaginationObject.getNumberOfRowEachPage());
        int numberOfPage = accCusPagi.getTotalPageOfResult(accountCustomers, PaginationObject.getNumberOfRowEachPage());

        if (acs == null) {
            req.getSession().setAttribute("msg", "Page " + req.getParameter("currentPage") + " not available");
            resp.sendRedirect("../account/signin");
            return;
        } else {
            req.setAttribute("accountCustomers", acs);
            req.setAttribute("numberOfPage", numberOfPage);
        }
        req.setAttribute("currentPage", currentPage);
        req.getRequestDispatcher("../adminCustomer.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int currentPage = 1;
        String search = req.getParameter("txtSearch");
        if ("".equals(search) || search == null) {
            req.getSession().setAttribute("searchProductMsg", "Not allow search empty");
            resp.sendRedirect("../admin/product");
            return;
        }
        req.setAttribute("searchInput", search);
        List<AccountCustomer> accountCustomers = customersDao.getAccountCustomerByCompanyNameContactNameCustomerIDOrContacttitle(search);
        if (accountCustomers.isEmpty()) {
            req.getSession().setAttribute("actionCusMsg", "No product for this search input");
            resp.sendRedirect("../admin/customer");
            return;
        }
        List<AccountCustomer> acs = accCusPagi.getPageOfResult(accountCustomers, currentPage, PaginationObject.getNumberOfRowEachPage());
        int numberOfPage = accCusPagi.getTotalPageOfResult(accountCustomers, PaginationObject.getNumberOfRowEachPage());

        if (acs == null) {
            req.getSession().setAttribute("msg", "Page " + req.getParameter("currentPage") + " not available");
            resp.sendRedirect("../account/signin");
            return;
        } else {
            req.setAttribute("accountCustomers", acs);
            req.setAttribute("numberOfPage", numberOfPage);
        }
        req.setAttribute("currentPage", currentPage);
        req.getRequestDispatcher("../adminCustomer.jsp").forward(req, resp);
    }

}
