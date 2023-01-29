/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import DTO.AccountCustomer;
import DAL.Customers;
import DAL.Order;
import DAL.Product;
import DTO.EmailUtility;
import DTO.OrderDetailProduct;
import DTO.StringHandling;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import models.AccountDAO;
import models.CustomersDao;
import models.OrderDao;
import models.OrderDetailDAO;
import models.ProductDAO;

/**
 *
 * @author PQT2212
 */
public class CartController extends HttpServlet {

    private final ProductDAO productDao = new ProductDAO();
    private final CustomersDao customersDao = new CustomersDao();
    private final OrderDao orderDao = new OrderDao();
    private final OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
    private String host;
    private String port;
    private String user;
    private String pass;

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
        AccountCustomer accountCustomer = (AccountCustomer) req.getSession().getAttribute("Acc");
        String ID = req.getParameter("id");
        int id;
        Product product = new Product();

        HashMap<Integer, Integer> productsCart = (HashMap<Integer, Integer>) req.getSession().getAttribute("productsCart");
        if (productsCart == null) {
            productsCart = new HashMap<>();
        }

        if ("".equals(ID) || ID == null) {
            req.getRequestDispatcher("Cart.jsp").forward(req, resp);
            return;
        }

        try {
            id = Integer.parseInt(ID);
            product = productDao.getProductByProductID(id);
            if (product == null) {
                req.setAttribute("homeMsg", "Cant found product with ID " + req.getParameter("id"));
                resp.sendRedirect("./home");
                return;
            }
        } catch (IOException | NumberFormatException e) {
            Logger.getLogger(SignUpController.class.getName()).log(Level.WARNING, null, e);
            req.getSession().setAttribute("homeMsg", "ProductID " + req.getParameter("id") + " isnt available");
            resp.sendRedirect("./home");
            return;
        }

        try {
            if (!productsCart.isEmpty() && productsCart.containsKey(id)) {
                String changeQuantity = req.getParameter("changeQuantity");
                if (changeQuantity == null) {
                    changeQuantity = "";
                }
                if (!"".equals(changeQuantity)) {
                    PrintWriter out = resp.getWriter();
                    switch (changeQuantity) {
                        case "minus":
                            if (productsCart.get(id) > 1) {
                                productsCart.replace(id, productsCart.get(id) - 1);
                            } else {
                                productsCart.remove(id, productsCart.get(id));
                                req.getSession().setAttribute("cartMsg", "Product " + id + " has been removed from cart");
                                return;
                            }
                            break;
                        case "plus":
                            if (product.getUnitsInStock() <= productsCart.get(id)) {
                                req.getSession().setAttribute("cartMsgWarn", "The quantity of Product " + id + " in your cart has reached the limit in stock");
                                return;
                            } else {
                                productsCart.replace(id, productsCart.get(id) + 1);
                            }
                            break;
                        case "setToOne":
                            productsCart.replace(id, 1);
                            break;
                        default:
                            Logger.getLogger(CartController.class.getName()).log(Level.SEVERE, null, "changeQuantity not set valid option");
                            resp.sendRedirect("./Cart");
                            return;
                    }
                    out.print(productsCart.get(id));
                } else {
                    productsCart.remove(id, productsCart.get(id));
                    req.getSession().setAttribute("cartMsg", "Product " + id + " has been removed from cart");
                    resp.sendRedirect("./Cart");
                }
            } else {
                req.getSession().setAttribute("cartMsgWarn", "Product with id=" + id + " arent in your cart");
                resp.sendRedirect("./Cart");
            }
        } finally {
            String builder = StringHandling.HashMapToString(productsCart);
            if (accountCustomer != null && accountCustomer.getAccount().getRole() == 2) {
                Cookie[] cookies = req.getCookies();
                if (cookies.length != 0) {
                    for (Cookie cookie : cookies) {
                        if (cookie.getName().equals(accountCustomer.getCustomers().getCustomerID())) {
                            cookie.setValue(builder);
                            cookie.setMaxAge(60 * 60 * 24 * 15);
                            resp.addCookie(cookie);
                            break;
                        }
                    }
                }
            }
            req.getSession().setAttribute("productsCart", productsCart);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        AccountCustomer accountCustomer = (AccountCustomer) req.getSession().getAttribute("Acc");

        float total = 0;
        try {
            String totalResult = req.getParameter("total");
            total = Float.parseFloat(totalResult);
        } catch (NumberFormatException e) {
            Logger.getLogger(CartController.class.getName()).log(Level.WARNING, null, e);
            resp.sendRedirect("./Cart");
            return;
        }
        String paymentMethod = req.getParameter("rbPaymentMethod");

        LocalDate date = LocalDate.now();
        String CustomerID;
        Order order;
        if (accountCustomer != null) {
            CustomerID = accountCustomer.getCustomers().getCustomerID();
            order = new Order(CustomerID, Date.valueOf(date), Date.valueOf(date.plusDays(20)), total, accountCustomer.getCustomers().getCompanyName(), accountCustomer.getCustomers().getAddress(), "Pending", paymentMethod);
        } else {
            boolean validation = (boolean) req.getAttribute("validationCus");
            if (validation == false) {
                req.getRequestDispatcher("Cart.jsp").forward(req, resp);
                return;
            }
            Customers customers = (Customers) req.getAttribute("customersFilter");
            String customerExist = (String) req.getAttribute("customerExist");
            try {
                if ("".equals(customerExist)) {
                    CustomerID = customersDao.insertCustomer(customers);
                } else {
                    CustomerID = customerExist;
                }
            } catch (SQLException ex) {
                Logger.getLogger(CartController.class.getName()).log(Level.SEVERE, null, ex);
                req.getSession().setAttribute("cartMsgWarn", "Create customer false, please try again");
                resp.sendRedirect("./Cart");
                return;
            }
            order = new Order(CustomerID, Date.valueOf(date), Date.valueOf(date.plusDays(20)), total, customers.getCompanyName(), customers.getAddress(), "Pending", paymentMethod);
        }

        HashMap<Integer, Integer> productsCart = (HashMap<Integer, Integer>) req.getSession().getAttribute("productsCart");
        if (productsCart == null) {
            Logger.getLogger(CartController.class.getName()).log(Level.SEVERE, null, "Cart list are null");
            req.getSession().setAttribute("cartMsgWarn", "Create order false, please contact for us for more detail");
            resp.sendRedirect("./Cart");
            return;
        }

        HashMap<Product, Integer> mapForInsert = new HashMap<>();
        Product product;
        for (Map.Entry<Integer, Integer> pC : productsCart.entrySet()) {
            product = productDao.getProductByProductID(pC.getKey());
            if (product == null) {
                resp.sendRedirect("./Cart");
                return;
            } else {
                mapForInsert.put(product, pC.getValue());
            }
        }
        int orderInsert = 0;
        try {
            orderInsert = orderDao.insertOrder(order, mapForInsert);
        } catch (SQLException ex) {
            Logger.getLogger(CartController.class.getName()).log(Level.SEVERE, null, ex);
            req.setAttribute("cartMsgWarn", "Create order false, please contact for us for more detail");
            req.getRequestDispatcher("Cart.jsp").forward(req, resp);
            return;
        }
        if (orderInsert == 0) {
            req.setAttribute("cartMsgWarn", "Create order false, please contact for us for more detail");
        } else {
            if (accountCustomer != null && accountCustomer.getAccount().getRole() == 2) {
                Cookie[] cookies = req.getCookies();
                if (cookies.length != 0) {
                    for (Cookie cookie : cookies) {
                        if (cookie.getName().equals(accountCustomer.getCustomers().getCustomerID())) {
                            cookie.setValue(null);
                            cookie.setMaxAge(0);
                            resp.addCookie(cookie);
                            break;
                        }
                    }
                }

                List<OrderDetailProduct> detailProducts = orderDetailDAO.getOrderDetailByOrderID(orderInsert);
                String recipient = accountCustomer.getAccount().getEmail();
                String subject = "PRJ Final Project confirm order";
                StringBuilder content = new StringBuilder();
                content.append("Dear ").append(accountCustomer.getCustomers().getContactName()).append(",<br/>");
                content.append("You have finish order on our sale website at ").append(LocalDateTime.now()).append(" <br/>");
                content.append("CustomerID: #").append(accountCustomer.getCustomers().getCustomerID()).append(" <br/>");
                content.append("Address: ").append(accountCustomer.getCustomers().getAddress()).append(" <br/>");
                content.append("Please check order detail list down below: <br/><br/>");
                content.append("    <table border='1'>"
                        + "<tr>\n"
                        + "<th>Product Name</th>\n"
                        + "<th>Quantity</th>\n"
                        + "<th>UnitPrice</th>\n"
                        + "</tr>");

                for (OrderDetailProduct detailProduct : detailProducts) {
                    content.append("<tr>\n"
                            + "<td>").append(detailProduct.getProduct().getProductName()).append("$</td>\n                        "
                            + "<td>").append(detailProduct.getOrderDetail().getQuantity()).append("$</td>\n                        "
                            + "<td>").append(detailProduct.getOrderDetail().getUnitPrice()).append("$</td>\n                    "
                            + "</tr>");
                }
                content.append("</table>");
                content.append("Total: ").append(total).append("$<br/><br/>");
                content.append("I hope you have the best shopping experience at our website<br/>");
                content.append("Thank you");

                String resultMessage = "";
                try {
                    EmailUtility.sendEmail(host, port, user, pass, recipient, subject, content.toString());
                    resultMessage = "The e-mail was sent successfully";
                } catch (MessagingException ex) {
                    Logger.getLogger(SignUpController.class.getName()).log(Level.WARNING, null, ex);
                    resultMessage = "There were an error: " + ex.getMessage();
                }
                req.setAttribute("resultMessage", resultMessage);
            }
            req.getSession().removeAttribute("productsCart");
            req.setAttribute("cartMsg", "Order successfully, please check your order in profile");
        }

        req.getRequestDispatcher("Cart.jsp").forward(req, resp);
    }

}
