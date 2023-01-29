<%@ page import = "java.io.*,java.util.*,java.sql.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fn" uri = "http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/sql" prefix = "sql"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<sql:setDataSource var = "snapshot" driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver"
                   url = "jdbc:sqlserver://MYLAP2212\\SQLEXPRESS:1433;databaseName=PRJ301_DB"
                   user = "sa"  password = "123456"/>
<c:import url="template/header.jsp" />
<c:set var="total" scope="session" value="0"/>
<div id="cart">
    <div id="cart-title">
        <h3>SHOPPING CART</h3>
    </div>
    <c:if test="${cartMsg!= null || cartMsg!= ''}">
        <div style="margin-left: 2%; margin-bottom: 3%; color: green" id="cartMsg">${cartMsg}</div>      
        <c:remove var="cartMsg"/>
    </c:if>
    <c:if test="${cartMsgWarn!= null || cartMsgWarn!= ''}">
        <div style="margin-left: 2%; margin-bottom: 3%; color: red" id="cartMsgWarn">${cartMsgWarn}</div>  
        <c:remove var="cartMsgWarn"/>
    </c:if>
    <c:if test="${resultMessage!= null || resultMessage!= ''}">
        <div style="margin-left: 2%; margin-bottom: 3%; color: red" id="resultMessage">${resultMessage}</div> 
        <c:remove var="resultMessage"/>
    </c:if>
    <div id="cart-content">
        <c:if test="${productsCart==null || fn:length(productsCart)==0}">
            <div class="cart-item">
                <div style="margin-left: 2%">
                    <div>No product in cart for now</div>
                    <div>Enjoy your shopping time!</div>
                </div>
            </div>        
        </c:if>
        <c:forEach var="product" items="${productsCart}">
            <sql:query dataSource = "${snapshot}" var = "prod">
                select * from Products a  WHERE a.ProductID = '${product.key}';
            </sql:query>
            <c:forEach var="p" items="${prod.rows}">
                <div class="cart-item">
                    <div class="cart-item-infor">
                        <div class="cart-item-img">
                            <img src="img/1.jpg"/>
                        </div>
                        <div class="cart-item-name">
                            <c:url value="/productDetail" var="produdctDetailURL">
                                <c:param name="id" value="${p.productID}"/>
                            </c:url>
                            <a href="${produdctDetailURL}">${p.productName}</a>
                        </div>
                        <div class="cart-item-price">
                            <fmt:formatNumber type = "number" maxFractionDigits = "3" value = "${p.unitPrice}" /> $
                        </div>
                        <div class="cart-item-button">
                            <c:url var="remove" value="/Cart">
                                <c:param name="id" value="${p.productID}"/>
                            </c:url>
                            <a href="${remove}" onclick="return check()">Remove</a>
                        </div>
                    </div>
                    <div class="cart-item-function" style="display: flex">
                        <button class="cart-button" style="color: white" onclick="ajaxChangeQuantity('${p.productID}', 'minus', 'quantity${p.productID}')">-</button>

                        <input type="text" name="quantity" id="quantity${p.productID}" value="${product.value}" readonly="true"/>

                        <button class="cart-button" style="color: white" onclick="ajaxChangeQuantity('${p.productID}', 'plus', 'quantity${p.productID}')">+</button>

                        <button class="cart-button" style="width: 8%; color: white; font-size: medium" onclick="ajaxChangeQuantity('${p.productID}', 'setToOne', 'quantity${p.productID}')">Set to 1</button>
                    </div>
                    <c:set var="total" scope="session" value="${total+(p.unitPrice*product.value)}"/>
                </div>  
            </c:forEach>
        </c:forEach>
    </div>
    <div id="cart-summary">
        <div id="cart-summary-content">Total amount: 
            <span style="color:red">
                <fmt:formatNumber type = "number" maxFractionDigits = "3" value = "${total}" /> $
            </span>
        </div>
    </div>
    <c:if test="${Acc == null}">         
        <form method="post" id="orderCartForm" onsubmit="return cartValidation()">
        </c:if> 
        <c:if test="${Acc != null}">   
            <form method="post" id="orderCartForm" hidden="true">
            </c:if> 
            <div id="customer-info">
                <div id="customer-info-content">
                    <h3>CUSTOMER INFORMATION:</h3>
                    <div id="customer-info-detail">
                        <input type="text" name="total" id="total" value="${total}" hidden="true"/><br/>
                        <div id="customer-info-left">
                            <input type="text" name="CompanyName" id="CompanyName" placeholder="Company name *" value="${Acc.customers.companyName}"/><br/>
                            <div class="msg-error" id="CompanyNameMsg"></div>     

                            <input type="text" name="ContactName" id="ContactName" placeholder="Contact name *" value="${Acc.customers.contactName}"/><br/>
                            <div class="msg-error" id="ContactNameMsg"></div>
                        </div>
                        <div id="customer-info-right">
                            <input type="text" name="ContactTitle" id="ContactTitle" placeholder="Contact title *" value="${Acc.customers.contactTitle}"/><br/>
                            <div class="msg-error" id="ContactTitleMsg"></div>

                            <input type="text" name="Address" id="Address" placeholder="Address *" value="${Acc.customers.address}"/><br/>
                            <div class="msg-error" id="AddressMsg"></div>
                        </div>
                    </div>
                </div>
            </div>
            <div id="customer-info">
                <div id="customer-info-content">
                    <h3>PAYMENT METHODS:</h3>
                    <div id="customer-info-payment">
                        <div>
                            <input type="radio" name="rbPaymentMethod" value="Payment C.O.D - Payment on delivery" checked="true"/>
                            Payment C.O.D - Payment on delivery
                        </div>
                        <div>
                            <input type="radio" name="rbPaymentMethod" value="Payment via online payment gateway"/>
                            Payment via online payment gateway
                        </div>
                    </div>
                </div>
            </div>           
        </form>
        <c:if test="${total==0}">
            <div id="cart-order">
                <input type="button" class="cart-order-input" form="orderCartForm" style="background-color: gray" value="ORDER"/>
            </div>
        </c:if>
        <c:if test="${total>0}">
            <div id="cart-order">
                <input type="submit" class="cart-order-input" form="orderCartForm" value="ORDER"/>
            </div>
        </c:if>
</div>
<script>
    <%--function quantityChange(status, quantityID) {
        var quantity = document.getElementById(quantityID);       
        if (status === 'plus') {
            quantity.value++;
        } else if (status === 'minus') {
            if (quantity.value > 0) {
                quantity.value--;
            } else {
                quantity.value = 0;
            }
        }
    }--%>
    function check() {
        console.log("aaaaaaaaaaa");
        return confirm("Are you sure to remove this product from cart");
    }

    console.log(document.getElementById("CompanyName").value);
    function cartValidation() {
        var CompanyName = document.getElementById("CompanyName").value;
        var ContactName = document.getElementById("ContactName").value;
        var ContactTitle = document.getElementById("ContactTitle").value;
        var Address = document.getElementById("Address").value;

        var addressPattern = /([1-9])+ ([A-Za-z -ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂ ưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ])+, ([A-Za-z ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂ ưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ])+, ([A-Za-z ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂ ưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ])+/;
        var check = true;

        if (CompanyName === "") {
            document.getElementById("CompanyNameMsg").innerHTML = "Company name is required";
            check = false;
        } else {
            document.getElementById("CompanyNameMsg").innerHTML = "";
        }
        if (ContactName === "") {
            document.getElementById("ContactNameMsg").innerHTML = "Contact name is required";
            check = false;
        } else {
            document.getElementById("ContactNameMsg").innerHTML = "";
        }
        if (ContactTitle === "") {
            document.getElementById("ContactTitleMsg").innerHTML = "Contact title required";
            check = false;
        } else {
            document.getElementById("ContactTitleMsg").innerHTML = "";
        }
        if (Address === "") {
            document.getElementById("AddressMsg").innerHTML = "Address is required";
            check = false;
        } else if (addressPattern.test(Address) === false) {
            document.getElementById("AddressMsg").innerHTML = "Address wrong format (number) address, city, country \n(Ex: 22 Hoang Quoc Viet-Cau giay, Ha Noi, Viet Name)";
            check = false;
        } else {
            document.getElementById("AddressMsg").innerHTML = "";
        }
        return check;
    }
    function ajaxChangeQuantity(id, changeQuantity, quantityID) {
        var quantity = document.getElementById(quantityID).value;
        var cartMsg = document.getElementById("cartMsg").textContent;
        var cartMsgWarn = document.getElementById("cartMsgWarn").textContent;
        var resultMessage = document.getElementById("resultMessage").textContent;       
        
        
        if (quantity <= 1 && changeQuantity==='minus') {
            var check = confirm("Are you sure to remove this product from cart");
            if(check===false){
                return;
            }
        }
        $.ajax({
            type: "get",
            url: '/final_project_MVC/Cart',
            data: {
                id: id,
                changeQuantity: changeQuantity
            },
            success: function (responseText) {
                if (isNaN(responseText)===true||responseText==="") {
                    location.reload();
                } else {
                    document.getElementById(quantityID).value = responseText;
                }
            },
            error: function (responseText) {

            }
        });
        if(cartMsg!=="" || cartMsgWarn!=="" || resultMessage!==""){
            location.reload();
        } 
    }
</script>
<c:import url="template/footer.jsp" />
