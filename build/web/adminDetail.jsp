
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<c:import url="template/adminHeader.jsp" />
<sql:setDataSource var = "snapshot" driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver"
                   url = "jdbc:sqlserver://MYLAP2212\\SQLEXPRESS:1433;databaseName=PRJ301_DB"
                   user = "sa"  password = "123456"/>
<div id="content-left">
    <ul>
        <a href="<c:url value="/admin"/>"><li>Dashboard</li></a>
        <a href="<c:url value="/admin/order"/>"><li>Orders</li></a>
        <a href="<c:url value="/admin/product"/>"><li>Products</li></a>
        <a href="<c:url value="/admin/customer"/>"><li>Customers</li></a>
    </ul>
</div>
<div id="content-right">
    <div class="path-admin">ORDER DETAIL</b></div>
    <div class="content-main">
        <div id="content-main-dashboard">
            <c:if test="${not empty adminOrderMsgWarn || adminOrderMsgWarn!=null}">
                <div style="margin-left: 2%; margin-bottom: 3%; color: red">${adminOrderMsgWarn}</div>       
            </c:if>
            <c:if test="${order!=null}">
                <div>
                    <div class="profile-order-title">
                        <div class="profile-order-title-left">
                            <div>OrderID: #${order.orderID}</div>
                            <div>Order creation date: ${order.orderDate}</div>
                        </div>
                        <div class="profile-order-title-right">
                            <c:if test="${order.status eq 'Order canceled'}">
                                <span>${order.status}</span>
                            </c:if> 
                            <c:if test="${order.status eq 'Pending'}">
                                <span style="margin-right: 4%; color: blue;">${order.status}</span>
                            </c:if>      
                            <c:if test="${order.status eq 'Completed'}">
                                <span style="color: green;">${order.status}</span>
                            </c:if> 
                        </div>
                    </div>
                    <sql:query dataSource = "${snapshot}" var = "orderDetailResult">
                        select * from [Order Details] a inner join Products b ON a.ProductID = b.ProductID  WHERE a.OrderID = '${order.orderID}';
                    </sql:query>
                    <c:forEach var="orderDetailProduct" items="${orderDetailResult.rows}">
                        <div class="profile-order-content"  style="background-color: white;">
                            <div class="profile-order-content-col1">
                                <a href="detail.html"><img src="<c:url value="./img/2.jpg"/>" width="100%"/></a>
                            </div>
                            <div class="profile-order-content-col2">
                                <c:url var="productDetail" value="/productDetail">
                                    <c:param value="${orderDetailProduct.productID}" name="id"/>
                                </c:url>
                                <a href="${productDetail}">${orderDetailProduct.productName}</a>
                            </div>
                            <div class="profile-order-content-col3">Quantity: ${orderDetailProduct.quantity}</div>
                            <div class="profile-order-content-col4" style="color: green">Unit Price:
                                <fmt:formatNumber type = "number" maxFractionDigits = "3" value = "${orderDetailProduct.unitPrice}" /> $
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:if>
        </div>
    </div>
</div>
<c:import url="template/adminFooter.jsp" />