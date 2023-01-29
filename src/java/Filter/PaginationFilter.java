/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Filter;

import Controller.SignUpController;
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
public class PaginationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        int currentPage;        
        String a = req.getParameter("currentPage");
        if ("".equals(a) || a == null) {
            request.setAttribute("currentPageFilter", 1);
        } else {
            try {
                currentPage = Integer.parseInt(a);
                request.setAttribute("currentPageFilter", currentPage);
            } catch (NumberFormatException e) {
                Logger.getLogger(SignUpController.class.getName()).log(Level.WARNING, null, e);
                req.getSession().setAttribute("msg", "Page " + req.getParameter("currentPage") + " not available");
                resp.sendRedirect("http://localhost:9999/final_project_MVC/account/signin");
                return;
            }
        }
        try {
            chain.doFilter(request, response);
        } catch (ServletException | IOException t) {
            Logger.getLogger(PasswordFilter.class.getName()).log(Level.WARNING, null, t);
        }
    }

    @Override
    public void destroy() {
    }

}
