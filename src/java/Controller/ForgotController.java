/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import DAL.Account;
import DTO.EmailUtility;
import DTO.RandomString;
import DTO.StringHandling;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.mail.MessagingException;
import models.AccountDAO;

/**
 *
 * @author PQT2212
 */
public class ForgotController extends HttpServlet {

    private String host;
    private String port;
    private String user;
    private String pass;
    private final AccountDAO accountDao = new AccountDAO();

    @Override
    public void init() {
        // reads SMTP server setting from web.xml file
        ServletContext context = getServletContext();
        host = context.getInitParameter("host");
        port = context.getInitParameter("port");
        user = context.getInitParameter("user");
        pass = context.getInitParameter("pass");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().removeAttribute("Acc");
        req.getSession().removeAttribute("productsCart");
        req.getRequestDispatcher("forgot.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        boolean validation = (boolean) req.getAttribute("validationEmail");
        if (validation == false) {
            req.getRequestDispatcher("./forgot.jsp").forward(req, resp);
            return;
        }
        String email = req.getParameter("email");
        Account emailExist = (Account) req.getAttribute("emailExist");

        String randomPassword = new RandomString(10, new Random(), RandomString.alphanum).nextString();
        String encryptionPass = "";
        try {
            encryptionPass = StringHandling.toHexString(StringHandling.getSHA(randomPassword));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ForgotController.class.getName()).log(Level.SEVERE, null, ex);
            req.setAttribute("EmailMsg", "Get random email false");
            req.getRequestDispatcher("forgot.jsp").forward(req, resp);
            return;
        }

        if (emailExist == null) {
            req.setAttribute("EmailMsg", "This email doesn't sign up in system");
            req.setAttribute("email", email);
            req.getRequestDispatcher("forgot.jsp").forward(req, resp);
            return;
        } else {
            emailExist.setPassword(encryptionPass);
        }

        String recipient = email;
        String subject = "PRJ Final Project reset Password";
        String content = "This is your new account password: \n\n" + randomPassword + "\n\n"
                + "Using it to login into your account and change your password at Profile\n\n"
                + "Please check in Spam if you dont get mail after submit\n\n"
                + "Thank you";
        String resultMessage = "";
        try {
            EmailUtility.sendEmail(host, port, user, pass, recipient, subject, content);
            resultMessage = "The e-mail was sent successfully";
        } catch (MessagingException ex) {
            Logger.getLogger(SignUpController.class.getName()).log(Level.WARNING, null, ex);
            resultMessage = "There were an error: " + ex.getMessage();
            return;
        }
        try {
            int updateCheck = accountDao.updateAccountPass(emailExist);
            if (updateCheck == 0) {
                Logger.getLogger(ForgotController.class.getName()).log(Level.SEVERE, null, "Get random password fail");
                req.setAttribute("EmailMsg", "Update random password fail");
                req.getRequestDispatcher("forgot.jsp").forward(req, resp);
            } else {
                req.setAttribute("Message", resultMessage);
                req.getRequestDispatcher("forgot.jsp").forward(req, resp);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ForgotController.class.getName()).log(Level.SEVERE, null, ex);
            req.setAttribute("EmailMsg", "Update random password fail");
            req.getRequestDispatcher("forgot.jsp").forward(req, resp);
        }
    }

}
