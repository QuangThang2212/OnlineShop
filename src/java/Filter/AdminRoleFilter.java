/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Filter;

import DTO.AccountCustomer;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author PQT2212
 */
public class AdminRoleFilter implements Filter{

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig); 
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        
        AccountCustomer accountCustomer = (AccountCustomer) req.getSession().getAttribute("Acc");
        if (accountCustomer == null || accountCustomer.getAccount().getRole()!=1) {
            req.getSession().setAttribute("homeMsg", "Invalid login");
            resp.sendRedirect("http://localhost:9999/final_project_MVC/home");
            return;
        }   
        
        try {
            chain.doFilter(request, response);
        } catch (ServletException | IOException t) {
            Logger.getLogger(CustomerRolePage.class.getName()).log(Level.WARNING, null, t);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
    
}
