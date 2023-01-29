/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import DAL.Category;
import DTO.PaginationObject;
import DAL.Product;
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
public class HomeController extends HttpServlet {

    CategoryDAO categoryDAO = new CategoryDAO();
    ProductDAO productDao = new ProductDAO();
    ProductCategoryPagination productPagination = new ProductCategoryPagination();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Product> bestSaleProducts = new ArrayList<>();
        List<Product> newProducts = new ArrayList<>();
        List<Product> hotProducts = new ArrayList<>();
        List<ProductCategory> products;
        String searchInput = req.getParameter("searchInput");

        //get and validation current page
        int currentPage;
        if(req.getAttribute("currentPageFilter")==null){
            currentPage = 1;
        }else{
            currentPage = (int) req.getAttribute("currentPageFilter");
        }        
        
        //get categoryParam
        String categoryParam = req.getParameter("categoryId");
        if (categoryParam == null) {
            categoryParam = "";
        }
        // classifile case in home
        // search != null -> home show search result with pagination
        // search == null -> category == null -> show all hot, new, best sale and product by all products in database
        // search == null -> category != null -> show hot, new, best sale and product by all products which have category
        // note: hot, new, best only show in page 1;
        if (searchInput != null) {
            products = productDao.getProductCategoryByProductName(searchInput);
            req.setAttribute("searchInput", searchInput);
        } else if (categoryParam.isEmpty()) {
            if (currentPage == 1) {
                bestSaleProducts = productDao.getTop4BestSaleProduct();
                newProducts = productDao.getTop4NewProduct();
                hotProducts = productDao.getTop4HotProduct();
            }
            products = productDao.getProductCategory();
        } else {
            req.getSession().removeAttribute("searchInput");
            int categoryID;
            try {
                categoryID = Integer.parseInt(categoryParam);
                req.setAttribute("categoryID", categoryID);
                Category category = categoryDAO.getCategoryByCategoryID(categoryID);
                if (category == null) {
                    req.getSession().setAttribute("homeMsg", "CategoryID (" + req.getParameter("categoryId") + ") doest exist");
                    resp.sendRedirect("./home");
                    return;
                }
            } catch (IOException | NumberFormatException e) {
                Logger.getLogger(SignUpController.class.getName()).log(Level.WARNING, null, e);
                req.getSession().setAttribute("homeMsg", "Invalid categoryID (" + req.getParameter("categoryId") + ")");
                resp.sendRedirect("./home");
                return;
            }
            if (currentPage == 1) {
                bestSaleProducts = productDao.getTop4BestSaleProductByCategory(categoryID);
                newProducts = productDao.getTop4NewProductByCategory(categoryID);
                hotProducts = productDao.getTop4HotProductByCategory(categoryID);
            }
            products = productDao.getProductCategoryByCategoryID(categoryID);
        }

        //validation best sale, hot and new to check and decided to send msg or list of results
        if (bestSaleProducts.isEmpty()) {
            req.setAttribute("bestSaleZoneMsg", "No best sale product show for now");
        }else{
            req.setAttribute("bestSaleProducts", bestSaleProducts);
        }
        if (newProducts.isEmpty()) {
            req.setAttribute("newProductsMsg", "No new product show for now");
        }else{
            req.setAttribute("newProducts", newProducts);
        }
        if (hotProducts.isEmpty()) {
            req.setAttribute("hotProductsMsg", "No hot product show for now");
        }else{
            req.setAttribute("hotProducts", hotProducts);
        }

        //check and pagination for products
        if (products.isEmpty()) {
            req.setAttribute("productsMsg", "No product show for now");
            req.getRequestDispatcher("home.jsp").forward(req, resp);
            return;
        } 
        List<ProductCategory> productsPagiantion = productPagination.getPageOfResult(products, currentPage, PaginationObject.getNumberOfRowEachPage());
        int numberOfPage = productPagination.getTotalPageOfResult(products, PaginationObject.getNumberOfRowEachPage());

        //check value in product list pagination
        if (productsPagiantion == null) {
            req.getSession().setAttribute("homeMsg", "Page " + req.getParameter("currentPage") + " not available");
            req.getSession().setAttribute("currentPage", 1);
            resp.sendRedirect("./home");
            return;
        } else {
            req.setAttribute("productsPagiantion", productsPagiantion);
            req.setAttribute("numberOfPage", numberOfPage);
        }

        req.setAttribute("currentPage", currentPage);
        req.getRequestDispatcher("home.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<ProductCategory> productsPagiantion = null;
        int numberOfPage = 0;
        int currentPage = 1;

        //get search result from home
        String searchInput = req.getParameter("search");
        req.setAttribute("searchInput", searchInput);
        if (searchInput.equals("")) {
            req.getSession().setAttribute("homeMsg", "Search keyword not allow empty");
            resp.sendRedirect("./home");
            return;
        }

        //get all products which have name contain search result from database
        List<ProductCategory> products = productDao.getProductCategoryByProductName(searchInput);
        
        //validation dao result and pagination
        if (products.isEmpty()) {
            req.setAttribute("productsMsg", "No product show for this search keyword");
            req.getRequestDispatcher("home.jsp").forward(req, resp);
            return;
        }
        productsPagiantion = productPagination.getPageOfResult(products, currentPage, PaginationObject.getNumberOfRowEachPage());
        numberOfPage = productPagination.getTotalPageOfResult(products, PaginationObject.getNumberOfRowEachPage());

        req.setAttribute("productsPagiantion", productsPagiantion);
        req.setAttribute("numberOfPage", numberOfPage);
        req.setAttribute("currentPage", currentPage);
        req.getRequestDispatcher("home.jsp").forward(req, resp);
    }

}
