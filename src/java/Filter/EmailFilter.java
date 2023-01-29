/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Filter;

import DAL.Account;
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
import java.util.regex.Pattern;
import models.AccountDAO;

/**
 *
 * @author PQT2212
 */
public class EmailFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        String email = req.getParameter("email");

        if (req.getMethod().equalsIgnoreCase("POST")) {
            boolean check = true;
            if (email.equals("")) {
                check = false;
            } else if (!Pattern.matches("[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$", email)) {
                check = false;
            }
            request.setAttribute("validationEmail", check);
            if(check == true){
                request.setAttribute("emailFilter", email);
                AccountDAO accountDAO = new AccountDAO();
                Account checkEmail = accountDAO.getAccountByEmail(email);

                request.setAttribute("emailExist", checkEmail);
            }
        }

        try {
            chain.doFilter(request, response);
        } catch (ServletException | IOException t) {
            Logger.getLogger(EmailFilter.class.getName()).log(Level.WARNING, null, t);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
    }

}
