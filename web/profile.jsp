
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<c:import url="template/header.jsp" />

<c:set var="customer" value="${Acc.customers}"/>
<c:set var="account" value="${Acc.account}"/>
<sql:setDataSource var = "snapshot" driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver"
                   url = "jdbc:sqlserver://MYLAP2212\\SQLEXPRESS:1433;databaseName=PRJ301_DB"
                   user = "sa"  password = "123456"/>
<div id="content-left">
    <h3 style="font-weight: normal;">Welcome, ${customer.contactName}</h3>
    <c:url var="profileCancelOrder" value="/profile">
        <c:param name="profileFun" value="Cancel-Order"/>
    </c:url>
    <c:url var="profileAllOrder" value="/profile">
        <c:param name="profileFun" value="All-Order"/>
    </c:url>
    <c:url var="profile" value="/profile">
        <c:param name="profileFun" value="Personal-infor"/>
    </c:url>
    <c:choose>
        <c:when test="${profileFunStatus eq 'Personal-infor'}">
            <h3>Account Management</h3>
            <ul><li  class="choose">Personal information</li></ul>
            <h3>My order</h3>
            <ul>
                <a href="${profileAllOrder}"><li>All orders</li></a>
                <a href="${profileCancelOrder}"><li>Canceled order</li></a>
            </ul>
        </c:when>
        <c:when test="${profileFunStatus eq 'All-Order'}">
            <h3>Account Management</h3>
            <ul><a href="${profile}"><li>Personal information</li></a></ul>
            <h3>My order</h3>
            <ul>
                <li  class="choose">All orders</li>
                <a href="${profileCancelOrder}"><li>Canceled order</li></a>
            </ul>
        </c:when>
        <c:when test="${profileFunStatus eq 'Cancel-Order'}">
            <h3>Account Management</h3>
            <ul>
                <a href="${profile}"><li>Personal information</li></a>
            </ul>
            <h3>My order</h3>
            <ul>
                <a href="${profileAllOrder}"><li>All orders</li></a>
                <li  class="choose">Canceled order</li>
            </ul>
        </c:when>
    </c:choose>
</div>
<div id="content-right">
    <c:if test="${profileFunStatus eq 'Personal-infor'}">
        <div class="path">Personal information</b></div>
        <div class="content-main">
            <div id="profile-content">
                <div class="profile-content-col">
                    <div>Company name: <br/>${customer.companyName}</div>
                    <div>Contact name: <br/>${customer.contactName}</div>
                    <c:url var="profileEdit" value="/profile">
                        <c:param name="profileFun" value="Edit-infor"/>
                    </c:url>
                    <button><a href="${profileEdit}"/>Edit</a></button>
                    <c:url var="profileChangePass" value="/profile">
                        <c:param name="profileFun" value="Change-pass"/>
                    </c:url>
                    <button><a href="${profileChangePass}"/>Password</a></button>
                    <c:if test="${editMsg!= null || editMsg!= ''}">
                        <div style=" color: green">${editMsg}</div>       
                        <c:remove var="editMsg"/>
                    </c:if>
                    <c:if test="${editMsgWarn!= null || editMsgWarn!= ''}">
                        <div style="color: red">${editMsgWarn}</div>       
                        <c:remove var="editMsgWarn"/>
                    </c:if>
                </div>
                <div class="profile-content-col">
                    <div>Company title: <br/>${customer.contactTitle}</div>
                    <div>Address: <br/>${customer.address}</div>                   
                </div>
                <div class="profile-content-col">
                    <div>Email: <br/>${account.email}</div>
                </div>
            </div>
        </div>
    </c:if>
    <c:if test="${profileFunStatus eq 'Cancel-Order' || profileFunStatus eq 'All-Order'}">
        <div class="path">LIST ORDERS</b></div>
        <div class="content-main">
            <div id="profile-content-order">
                <c:if test="${empty orderHistoryList || orderHistoryList==null}">
                    <div class="profile-order-title">No order history for now</div>
                </c:if>
                <c:if test="${not empty OrderMsgWarn || OrderMsgWarn!=null}">
                    <div style="margin-left: 2%; margin-bottom: 3%; color: red">${OrderMsgWarn}</div>  
                    <c:remove var="OrderMsgWarn"/>
                </c:if>
                <c:if test="${not empty OrderMsg || OrderMsg!=null}">
                    <div style="margin-left: 2%; margin-bottom: 3%; color: green">${OrderMsg}</div>   
                    <c:remove var="OrderMsg"/>
                </c:if>
                <c:if test="${not empty orderHistoryList}">
                    <c:forEach items="${orderHistoryList}" var="orderHistory">
                        <div>
                            <div class="profile-order-title">
                                <div class="profile-order-title-left">
                                    <div>Order creation date: ${orderHistory.orderDate}</div>
                                    <div>Order: #${orderHistory.orderID}</div>
                                    <div style="color: green">Total: ${orderHistory.freight} $</div>
                                </div>
                                <div class="profile-order-title-right">
                                    <c:if test="${orderHistory.status eq 'Order canceled'}">
                                        <span>${orderHistory.status}</span>
                                    </c:if> 
                                    <c:if test="${orderHistory.status eq 'Pending'}">
                                        <span style="margin-right: 4%">${orderHistory.status}</span>
                                        
                                        <c:url var="formUrl" value="/profile">
                                            <c:param value="cancel-order" name="postAction"/>
                                        </c:url>
                                        <form action="${formUrl}" method="post" onsubmit="return check()" hidden="true">
                                            <input type="text" value="${orderHistory.orderID}" name="id" readonly="true"/>
                                        </form>
                                        <span><a href="${CancelOrder}" onclick="return check()">Cancel order</a></span>
                                    </c:if>      
                                    <c:if test="${orderHistory.status eq 'Completed'}">
                                        <span style="color: blue;">${orderHistory.status}</span>
                                    </c:if> 
                                </div>
                            </div>
                            <sql:query dataSource = "${snapshot}" var = "orderDetailResult">
                                select * from [Order Details] a inner join Products b ON a.ProductID = b.ProductID  WHERE a.OrderID = '${orderHistory.orderID}';
                            </sql:query>
                            <c:forEach var="orderDetailProduct" items="${orderDetailResult.rows}">
                                <div class="profile-order-content">
                                    <div class="profile-order-content-col1">
                                        <a href="detail.html"><img src="img/2.jpg" width="100%"/></a>
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
                    </c:forEach>
                </c:if>
            </div>
        </div>
    </c:if>
</div>
<script>
    function check() {
        if (confirm("Are you sure to cancel this order") === false) {
            return false;
        } else {
            return true;
        }
    }
</script>
<c:import url="template/footer.jsp" />