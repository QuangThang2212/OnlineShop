/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Filter;

import DTO.StringHandling;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author PQT2212
 */
public class PasswordFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String Password = req.getParameter("pass");
        String RePassword = req.getParameter("re_pass");
        String url = req.getServletPath();

        if (req.getMethod().equalsIgnoreCase("POST")) {
            boolean validation = true;
            if (Password==null) {
                validation = false;
            }else if(Password.equals("")){
                validation = false;
            }
            if (url.endsWith("/account/signup") || url.endsWith("/profile?postAction=edit-infor") || url.endsWith("/profile?postAction=change-pass")) {
                if (RePassword==null) {
                    validation = false;
                } else if (RePassword.equals("")) {
                    validation = false;
                } else if (!RePassword.equals(Password)) { 
                    validation = false;
                }
            }
            request.setAttribute("validationPass", validation);
            if (validation == true) {
                try {
                    String encryptionPass = StringHandling.toHexString(StringHandling.getSHA(Password));
                    request.setAttribute("encryptionPass", encryptionPass);
                } catch (NoSuchAlgorithmException | NullPointerException ex) {
                    Logger.getLogger(PasswordFilter.class.getName()).log(Level.SEVERE, null, ex);
                    resp.sendRedirect("./account/signin");
                }
            }
        }

        try {
            chain.doFilter(request, response);
        } catch (ServletException | IOException t) {
            Logger.getLogger(PasswordFilter.class.getName()).log(Level.WARNING, null, t);
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
