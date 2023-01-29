/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import DTO.AccountCustomer;
import DTO.StringHandling;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import models.AccountDAO;

/**
 *
 * @author PQT2212
 */
public class SignInController extends HttpServlet {

    AccountDAO accountDAO = new AccountDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().removeAttribute("Acc");
        req.getSession().removeAttribute("productsCart");
        req.getRequestDispatcher("../signin.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        boolean validationEmail = (boolean) req.getAttribute("validationEmail");
        boolean validationPass = (boolean) req.getAttribute("validationPass");
        if (validationEmail == false || validationPass == false) {
            req.getRequestDispatcher("../signin.jsp").forward(req, resp);
            return;
        }
        String email = req.getParameter("email");
        String pass = (String) req.getAttribute("encryptionPass");

        AccountCustomer accountCustomer = accountDAO.getAccountCustomer(email, pass);
        if (accountCustomer != null) {
            req.getSession().removeAttribute("productsCart");
            req.getSession().setAttribute("Acc", accountCustomer);
            if (accountCustomer.getAccount().getRole() == 2) {
                Cookie[] cookies = req.getCookies();
                if (cookies.length != 0) {                    
                    for (Cookie cookie : cookies) {
                        if (cookie.getName().equals(accountCustomer.getCustomers().getCustomerID())) {
                            HashMap<Integer, Integer>productsCart = StringHandling.StringToHashMap(cookie.getValue());
                            req.getSession().setAttribute("productsCart", productsCart);
                            break;
                        }
                    }
                }
            }

            if (accountCustomer.getAccount().getRole() == 1) {
                resp.sendRedirect("../admin");
            } else {
                req.getSession().setAttribute("homeMsg", "Welcome " + email);
                resp.sendRedirect("../home");
            }

        } else {
            req.setAttribute("msg", "Account not found");
            req.getRequestDispatcher("../signin.jsp").forward(req, resp);
        }
    }

}
