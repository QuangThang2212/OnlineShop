/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Filter.java to edit this template
 */
package Filter;

import DAL.Account;
import DTO.AccountCustomer;
import DAL.Customers;
import DTO.StringHandling;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import models.AccountDAO;
import models.CustomersDao;

/**
 *
 * @author PQT2212
 */
@WebFilter(filterName = "SignUpFormValidation", servletNames = {"EditPersonalInfor", "signUpController"})
public class CustomerInforFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        AccountCustomer accountCustomer = (AccountCustomer) req.getSession().getAttribute("Acc");

        if (req.getMethod().equalsIgnoreCase("POST")) {
            String CompanyNameCheck = req.getParameter("CompanyName");
            String ContactNameCheck = req.getParameter("ContactName");
            String ContactTitleCheck = req.getParameter("ContactTitle");
            String AddressCheck = req.getParameter("Address");

            boolean validation = true;
            if (CompanyNameCheck == null) {
                validation = false;
            }else if (CompanyNameCheck.equals("")) {
                validation = false;
            }
            if (ContactNameCheck == null) {
                validation = false;
            } else if (ContactNameCheck.equals("")) {
                validation = false;
            }
            if (ContactTitleCheck == null) {
                validation = false;
            }else if (ContactTitleCheck.equals("")) {
                validation = false;
            }
            if (AddressCheck == null) {
                validation = false;
            } else if (AddressCheck.equals("")) {
                validation = false;
            } else if(!Pattern.matches("([1-9])+ ([A-Za-z -ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂ ưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ])+, ([A-Za-z ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂ ưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ])+, ([A-Za-z ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂ ưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ])+", AddressCheck)){
                validation = false;
            }
            request.setAttribute("validationCus", validation);
            if (validation == true) {
                String CompanyName = StringHandling.upperFirstLetter(req.getParameter("CompanyName").trim());
                String ContactName = StringHandling.upperFirstLetter(req.getParameter("ContactName").trim());
                String ContactTitle = StringHandling.upperFirstLetter(req.getParameter("ContactTitle").trim());
                String Address = StringHandling.upperFirstLetter(req.getParameter("Address").trim());

                Customers customers = new Customers(CompanyName, ContactName, ContactTitle, Address);
                CustomersDao customersDao = new CustomersDao();
                List<Customers> check = customersDao.getCustomerByAllInfor(customers);
                if(check.isEmpty()){
                    request.setAttribute("customerExist", "");
                }else{
                    request.setAttribute("customerExist", check.get(0).getCustomerID());
                }

                req.setAttribute("customersFilter", customers);
            }
        }

        try {
            chain.doFilter(request, response);
        } catch (ServletException | IOException t) {
            Logger.getLogger(CustomerInforFilter.class.getName()).log(Level.WARNING, null, t);
        }

    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

}
