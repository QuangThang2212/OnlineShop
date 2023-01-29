/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import DAL.Category;
import DTO.ProductCategory;
import DTO.ProductCategoryPagination;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.CategoryDAO;
import models.ProductDAO;

/**
 *
 * @author PQT2212
 */
public class AdminProductController extends HttpServlet {

    ProductDAO productDao = new ProductDAO();
    CategoryDAO categoryDAO = new CategoryDAO();
    ProductCategoryPagination pcPagi = new ProductCategoryPagination();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int currentPage = (int) req.getAttribute("currentPageFilter");
        
        List<ProductCategory> productCategorys = new ArrayList<>();
        String filterInput = req.getParameter("filterInput");
        String search = req.getParameter("searchInput");

        if (search != null) {
            req.setAttribute("searchInput", search);
            productCategorys = productDao.getProductCategoryByProductName(search);
        } else if (filterInput != null) {
            int filter = 0;
            try {
                filter = Integer.parseInt(filterInput);               
                Category category = categoryDAO.getCategoryByCategoryID(filter);
                if (category == null) {
                    req.getSession().setAttribute("msg", "CategoryID (" + filterInput + ") doest exist");
                    resp.sendRedirect("../account/signup");
                    return;
                }
            } catch (NumberFormatException e) {               
                req.getSession().setAttribute("msg", "CategoryID ID " + filterInput + " not available");
                resp.sendRedirect("../account/signup");
                return;
            }
            req.setAttribute("filterInput", filter);
            productCategorys = productDao.getProductCategoryByCategoryID(filter);
        }else{
            productCategorys = productDao.getProductCategory();
        }
        
        if (productCategorys.isEmpty()) {
            req.setAttribute("adminProductMsg", "No product show for now");
            req.getRequestDispatcher("../adminProduct.jsp").forward(req, resp);
            return;
        }
        List<ProductCategory> productCategoryPagination = pcPagi.getPageOfResult(productCategorys, currentPage, ProductCategoryPagination.numberOfRowEachPage);
        int numberOfPage = pcPagi.getTotalPageOfResult(productCategorys, ProductCategoryPagination.numberOfRowEachPage);
        if (productCategoryPagination == null) {
            req.getSession().setAttribute("adminMsg", "Page " + req.getParameter("currentPage") + " not available");                
            resp.sendRedirect("../account/signup");
            return;
        } else {
            req.setAttribute("adminProductPagination", productCategoryPagination);
            req.setAttribute("numberOfPage", numberOfPage);
        }

        req.setAttribute("currentPage", currentPage);
        req.getRequestDispatcher("../adminProduct.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String submitButton = req.getParameter("submitButton");
        List<ProductCategory> productCategorys = new ArrayList<>();
        int currentPage = 1;
        switch (submitButton) {
            case "Filter":
                int categoryID = 0;
                try {
                    categoryID = Integer.parseInt(req.getParameter("ddlCategory"));
                    req.setAttribute("filterInput", categoryID);
                } catch (NumberFormatException e) {
                    Logger.getLogger(SignUpController.class.getName()).log(Level.WARNING, null, e);
                    req.getSession().setAttribute("msg", "CategoryID " + req.getParameter("ddlCategory") + " not available");
                    resp.sendRedirect("../account/signup");
                    return;
                }
                if (categoryID == -1) {
                    resp.sendRedirect("../admin/product");
                    return;
                }
                productCategorys = productDao.getProductCategoryByCategoryID(categoryID);
                if (productCategorys.isEmpty()) {
                    req.getSession().setAttribute("adminProductMsg", "No product for this category");
                    resp.sendRedirect("../admin/product");
                    return;
                }
                break;

            case "Search":
                String search = req.getParameter("txtSearch");
                if ("".equals(search) || search == null) {
                    req.getSession().setAttribute("searchProductMsg", "Not allow search empty");
                    resp.sendRedirect("../admin/product");
                    return;
                }
                req.setAttribute("searchInput", search);
                productCategorys = productDao.getProductCategoryByProductName(search);
                if (productCategorys.isEmpty()) {
                    req.getSession().setAttribute("adminProductMsg", "No product for this search input");
                    resp.sendRedirect("../admin/product");
                    return;
                }
                break;
            default:
                req.getSession().setAttribute("adminProductMsg", "Invalid submit");
                resp.sendRedirect("../admin/product");
                break;
        }
        List<ProductCategory> productCategoryPagination = pcPagi.getPageOfResult(productCategorys, currentPage, ProductCategoryPagination.numberOfRowEachPage);
        int numberOfPage = pcPagi.getTotalPageOfResult(productCategorys, ProductCategoryPagination.numberOfRowEachPage);
        if (productCategoryPagination == null) {
            req.getSession().setAttribute("adminMsg", "Page " + req.getParameter("currentPage") + " not available");
            resp.sendRedirect("../account/signup");
            return;
        } else {
            req.setAttribute("adminProductPagination", productCategoryPagination);
            req.setAttribute("numberOfPage", numberOfPage);
        }
        req.setAttribute("currentPage", currentPage);
        req.getRequestDispatcher("../adminProduct.jsp").forward(req, resp);
    }

}
