/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import DAL.Product;
import DTO.ProductCategory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.ProductDAO;

/**
 *
 * @author PQT2212
 */
public class AdminCUProductController extends HttpServlet {

    ProductDAO productDao = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idcheck = req.getParameter("id");
        String action = req.getParameter("action");
        
        //check if admin choose delete option of not
        boolean deleteCheck;
        if ("".equals(action) || action == null) {
            deleteCheck = false;
        } else {
            deleteCheck = action.equals("delete");
        }
        
        // id = null || id = "" -> go to create product
        // (id != null && id != "") && deleteCheck = true -> go to delete product
        // (id != null && id != "") && deleteCheck = false -> go to update product
        if (idcheck != null && !"".equals(idcheck)) {
            int productID;
            try {
                productID = Integer.parseInt(req.getParameter("id"));
            } catch (NumberFormatException e) {
                req.setAttribute("adminCUProductMsg", "Invalid product ID " + req.getParameter("productID"));
                req.getRequestDispatcher("../adminCreateProduct.jsp").forward(req, resp);
                return;
            }
            if (deleteCheck == true) {
                try {
                    int deleteDaoCheck = productDao.Delete(productID);
                    if (deleteDaoCheck == 0) {
                        req.getSession().setAttribute("adminProductMsgWarn", "Delete " + productID + " product fail, this product were on customer orders");
                    } else {
                        req.getSession().setAttribute("adminProductMsg", "Delete " + productID + " product successfully");
                    }
                    resp.sendRedirect("../admin/product");
                    return;
                } catch (SQLException ex) {
                    Logger.getLogger(AdminCUProductController.class.getName()).log(Level.SEVERE, null, ex);
                    req.getSession().setAttribute("adminProductMsgWarn", "Delete " + productID + " product fail, Exception exist");
                    resp.sendRedirect("../admin/product");
                    return;
                }
            } else {
                ProductCategory productCategory = productDao.getProductCategoryByProductID(productID);
                if (productCategory == null) {
                    req.setAttribute("adminCUProductMsg", "Cant found product with ID " + req.getParameter("productID"));
                    req.getRequestDispatcher("../adminCreateProduct.jsp").forward(req, resp);
                    return;
                } else {
                    req.setAttribute("product", productCategory.getProduct());
                }
            }
        }
        req.getRequestDispatcher("../adminCreateProduct.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Product product = new Product();
        boolean validation = (boolean) req.getAttribute("validation");
        if (validation == false) {
            req.getRequestDispatcher("../adminCreateProduct.jsp").forward(req, resp);
            return;
        } else {
            product = (Product) req.getAttribute("productValidation");
            req.setAttribute("product", product);
        }

        String submitProductButton = req.getParameter("submitProductButton");
        switch (submitProductButton) {
            case "Save":
                req.setAttribute("productCUAction", "save");
                try {
                    int checkCreate = productDao.CreateProduct(product);
                    if (checkCreate == 0) {
                        req.getSession().setAttribute("adminProductMsgWarn", "Create product " + product.getProductName() + " fail");
                    } else {
                        req.getSession().setAttribute("adminProductMsg", "Create product " + product.getProductName() + " successfully");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(AdminCUProductController.class.getName()).log(Level.SEVERE, null, ex);
                    req.getSession().setAttribute("adminProductMsgWarn", "Create product " + product.getProductName() + " fail, Exeption exist");
                }
                break;

            case "Update":
                req.setAttribute("productCUAction", "Update");
                int productID;
                try {
                    productID = Integer.parseInt(req.getParameter("productid"));
                } catch (NumberFormatException e) {
                    Logger.getLogger(AdminCUProductController.class.getName()).log(Level.SEVERE, null, e);
                    req.getSession().setAttribute("adminProductMsgWarn", "product id invalid");
                    break;
                }
                product.setProductID(productID);

                try {
                    int checkUpdate = productDao.update(product);
                    if (checkUpdate == 0) {
                        req.getSession().setAttribute("adminProductMsgWarn", "Update product " + product.getProductName() + " fail");
                    } else {
                        req.getSession().setAttribute("adminProductMsg", "Update product " + product.getProductName() + " successfully");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(AdminCUProductController.class.getName()).log(Level.SEVERE, null, ex);
                    req.getSession().setAttribute("adminProductMsgWarn", "Update product " + product.getProductName() + " fail, Exeption exist");
                }
                break;

            default:
                req.getSession().setAttribute("adminProductMsgWarn", "Invalid submit option, Exeption exist");
                break;
        }
        resp.sendRedirect("../admin/product");
    }

}
