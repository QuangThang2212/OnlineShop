/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import DTO.AccountCustomer;
import DTO.ProductCategory;
import DTO.StringHandling;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import models.ProductDAO;

/**
 *
 * @author PQT2212
 */
public class ProductController extends HttpServlet {

    ProductDAO productDao = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int productID;
        try {
            productID = Integer.parseInt(req.getParameter("id"));
        } catch (NumberFormatException e) {
            req.setAttribute("homeMsg", "Invalid productID (" + req.getParameter("id") + ")");
            req.getRequestDispatcher("/home").forward(req, resp);
            return;
        }
        ProductCategory productCategory = productDao.getProductCategoryByProductID(productID);
        if (productCategory == null) {
            req.setAttribute("homeMsg", "Cant found product with ID " + req.getParameter("id"));
            req.getRequestDispatcher("/home").forward(req, resp);
            return;
        }
        req.setAttribute("productCategory", productCategory);
        req.getRequestDispatcher("product_detail.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AccountCustomer accountCustomer = (AccountCustomer) req.getSession().getAttribute("Acc");

        String submitValue = req.getParameter("productDetailFormButton");
        int productID;
        try {
            productID = Integer.parseInt(req.getParameter("productID"));
        } catch (NumberFormatException e) {
            req.setAttribute("homeMsg", "Cant found product with ID " + req.getParameter("productID"));
            req.getRequestDispatcher("./home").forward(req, resp);
            return;
        }
        ProductCategory productCategory = productDao.getProductCategoryByProductID(productID);
        if (productCategory == null) {
            req.setAttribute("homeMsg", "Cant found product with ID " + req.getParameter("productID"));
            req.getRequestDispatcher("./home").forward(req, resp);
            return;
        }
        HashMap<Integer, Integer> productsCart = (HashMap<Integer, Integer>) req.getSession().getAttribute("productsCart");
        if (productsCart == null || productsCart.isEmpty()) {
            productsCart = new HashMap<>();
            productsCart.put(productID, 1);
        } else if (productsCart.containsKey(productID)) {
            if (productCategory.getProduct().getUnitsInStock() <= productsCart.get(productID)) {
                resp.sendRedirect("./productDetail?id=" + req.getParameter("productID"));
                return;
            } else {
                productsCart.replace(productID, productsCart.get(productID) + 1);
            }
        } else {
            productsCart.put(productID, 1);
        }
        if (accountCustomer != null && accountCustomer.getAccount().getRole() == 2) {
            String builder = StringHandling.HashMapToString(productsCart);
            Cookie[] cookies = req.getCookies();
            Cookie cookieNew = null;
            boolean cookieCheck = true;
            System.out.println(accountCustomer.getAccount().getEmail());
            if (cookies.length != 0) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(accountCustomer.getCustomers().getCustomerID())) {
                        cookieCheck = false;
                        cookie.setValue(builder);
                        resp.addCookie(cookie);
                        break;
                    }
                }
                if (cookieCheck) {
                    cookieNew = new Cookie(accountCustomer.getCustomers().getCustomerID(), builder);
                    resp.addCookie(cookieNew);
                }
            }
        }
        req.getSession().setAttribute("productsCart", productsCart);
        switch (submitValue) {
            case "BUY NOW":
                resp.sendRedirect("./Cart");
                break;
            case "ADD TO CART":
                req.getSession().setAttribute("productDetailMsg", req.getParameter("productID") + " has been added to cart");
                resp.sendRedirect("./productDetail?id=" + req.getParameter("productID"));
                break;
            default:
                req.setAttribute("homeMsg", "some error has happen");
                req.getRequestDispatcher("./home").forward(req, resp);
                break;
        }
    }

}
