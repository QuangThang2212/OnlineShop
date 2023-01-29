/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Filter;

import DAL.Product;
import DTO.ProductCategory;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author PQT2212
 */
public class ProductCUFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        if (req.getMethod().equalsIgnoreCase("POST")) {

            String productNameCheck = req.getParameter("txtProductName");
            String categoryIDcheck = req.getParameter("ddlCategory");
            String unitPricecheck = req.getParameter("txtUnitPrice");
            String unitsInStockcheck = req.getParameter("txtUnitsInStock");
            String discontinuedcheck = req.getParameter("chkDiscontinued");

            boolean check = true;

            if (productNameCheck == null || "".equals(productNameCheck)) {
                request.setAttribute("productNameMsg", "Product name is required.");
                check = false;
            }
            try {
                int categoryID = Integer.parseInt(categoryIDcheck);
            } catch (NumberFormatException e) {
                Logger.getLogger(ProductCUFilter.class.getName()).log(Level.WARNING, null, e);
                check = false;
            }
            try {
                float unitPrice = Float.parseFloat(unitPricecheck);
                if (unitPrice < 0) {
                    check = false;
                }
            } catch (NumberFormatException e) {
                Logger.getLogger(ProductCUFilter.class.getName()).log(Level.WARNING, null, e);
            }
            try {
                int unitsInStock = Integer.parseInt(unitsInStockcheck);
                if (unitsInStock < 0) {
                    check = false;
                }
            } catch (NumberFormatException e) {
                Logger.getLogger(ProductCUFilter.class.getName()).log(Level.WARNING, null, e);
                check = false;
            }
            try {
                boolean discontinued = Boolean.parseBoolean(discontinuedcheck);
            } catch (Exception e) {
                Logger.getLogger(ProductCUFilter.class.getName()).log(Level.WARNING, null, e);
                check = false;
            }
            req.setAttribute("validation", check);
            if (check == true) {
                String productName = req.getParameter("txtProductName").trim();
                int categoryID = Integer.parseInt(req.getParameter("ddlCategory").trim());
                String quantityPerUnit = req.getParameter("txtQuantityPerUnit").trim();
                float unitPrice = Float.parseFloat(req.getParameter("txtUnitPrice").trim());
                int unitsInStock = Integer.parseInt(req.getParameter("txtUnitsInStock").trim());
                boolean discontinued = Boolean.parseBoolean(req.getParameter("chkDiscontinued"));

                Product product = new Product(categoryID, productName, quantityPerUnit, unitPrice, unitsInStock, discontinued);
                request.setAttribute("productValidation", product);            
            }
        }
        try {
            chain.doFilter(request, response);
        } catch (ServletException | IOException t) {
            Logger.getLogger(ProductCUFilter.class.getName()).log(Level.WARNING, null, t);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

}
