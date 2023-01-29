
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<c:import url="template/adminHeader.jsp" />

<div id="content-left">
    <ul>
        <a href="<c:url value="/admin"/>"><li>Dashboard</li></a>
        <li class="choose">Orders</li>
        <a href="<c:url value="/admin/product"/>"><li>Products</li></a>
        <a href="<c:url value="/admin/customer"/>"><li>Customers</li></a>
    </ul>
</div>
<div id="content-right">
    <div class="path-admin">ORDERS LIST</b></div>
    <div class="content-main">
        <div id="content-main-dashboard">
            <div id="order-title" style="display: flex">
                <div id="product-title-1" style="width: 50%; padding: 2% 0% ">
                    <b style="border-top: solid brown">Filter by Order date:</b>
                    <form action="<c:url value="/admin/order"/>" method="post">
                        <c:if test="${filterMsg!= null}">
                            <div class="msg-error" style="margin-bottom: 0px">${filterMsg}</div>
                            <c:remove var="filterMsg"/>
                        </c:if>                    
                        From: <input type="date" name="txtStartOrderDate" value="${startDate}"/>
                        To: <input type="date" name="txtEndOrderDate" value="${endDate}"/>
                        <input type="submit" name="submitButton" value="Filter">  
                    </form>
                    <a href="<c:url value="/admin/order"/>"> Show all</a>
                </div>
                <div id="product-title-2" style="width: 50%; padding: 2% 0% ">
                    <b style="border-top: solid brown">Search by Company name or custommer id</b>
                    <form action="<c:url value="/admin/order"/>" method="post">
                        <input type="text" name="txtSearch" placeholder="Enter Company name or custommer id to search" value='${searchInput}'/>
                        <input type="submit" name="submitButton" value="Search"/><br/>
                        <c:if test="${searchOrderMsg!= null}">
                            <span class="msg-error">${searchOrderMsg}</span><br/>
                            <c:remove var="searchOrderMsg"/>
                        </c:if> 
                    </form>                   
                </div>
            </div>
            <c:if test="${actionMsg!= null}">
                <div class="msg-error" style="margin: 1.5%">${actionMsg}</div>
                <c:remove var="actionMsg"/>
            </c:if>    
            <div id="order-table">
                <table id="orders">
                    <tr>
                        <th>OrderID</th>
                        <th>OrderDate</th>
                        <th>RequiredDate</th>
                        <th>ShippedDate</th>
                        <th>Employee</th>
                        <th>Customer</th>
                        <th>Freight($)</th>
                        <th>Status</th>
                    </tr>
                    <c:forEach var="order" items="${orderPagination}">
                        <tr>
                            <td>
                                <c:url var="orderDetail" value="/admin">
                                    <c:param name="id" value="${order.order.orderID}"/>
                                </c:url>
                                <a href="${orderDetail}">#${order.order.orderID}</a>
                            </td>
                            <td>${order.order.orderDate}</td>
                            <td>${order.order.requiredDate}</td>
                            <c:choose>
                                <c:when test="${order.order.shippedDate == null && order.order.status eq 'Pending'}">
                                    <td style="font-weight: bold">Doesn't shipped yet</td>
                                </c:when>  
                                <c:when test="${order.order.shippedDate == null && order.order.status eq 'Order canceled'}">
                                    <td style="font-weight: bold">Order canceled</td>
                                </c:when>  
                                <c:otherwise>
                                    <td>${order.order.shippedDate}</td>
                                </c:otherwise>                                                                     
                            </c:choose>
                            <c:choose>
                                <c:when test="${order.employee.employeeID == '' && order.order.status eq 'Pending'}">
                                    <td style="font-weight: bold">No confirm</td>
                                </c:when>  
                                <c:when test="${order.employee.employeeID == '' && order.order.status eq 'Order canceled'}">
                                    <td style="font-weight: bold">Order canceled</td>
                                </c:when> 
                                <c:otherwise>
                                    <td>${order.employee.firstName} ${order.employee.lastName}</td>
                                </c:otherwise>                                                                     
                            </c:choose> 

                            <td>${order.customers.contactName} - ${order.order.customerID}</td>

                            <td>${order.order.freight}</td>

                            <c:choose>
                                <c:when test="${order.order.status eq 'Completed'}">
                                    <td style="color: green;">${order.order.status}</td>
                                </c:when>  

                                <c:when test="${order.order.status eq 'Order canceled'}">
                                    <td style="color: red;">${order.order.status}</td>
                                </c:when> 

                                <c:when test="${order.order.status eq 'Pending'}">                           
                                    <c:url var="CancelOrder" value="/orderStatus">
                                        <c:param value="${order.order.orderID}" name="id"/>
                                        <c:param value="cancel" name="action"/>
                                    </c:url>
                                    <c:url var="CompletelOrder" value="/orderStatus">
                                        <c:param value="${order.order.orderID}" name="id"/>
                                        <c:param value="complete" name="action"/>
                                    </c:url>
                                    <td>                          
                                        <div style="color: blue; margin-bottom: 5px">${order.order.status}</div>
                                        <div><a href="${CompletelOrder}" onclick="return check('Are you sure to complete this order')">Complete</a> | <a href="${CancelOrder}" onclick="return check('Are you sure to cancel this order')">Cancel</a></div>
                                    </td>
                                </c:when>   
                            </c:choose>                           
                        </tr>
                    </c:forEach>
                </table>
            </div>
            <c:if test="${numberOfPage>0 || numberOfPage!=null}">
                <div class="pagination">
                    <c:if test="${currentPage>1}">
                        <c:if test="${submitButton=='Filter' && startDate != null && endDate != null}">
                            <c:url value="/admin/order" var="paginationPrevous">
                                <c:param name="currentPage" value="${currentPage-1}" />
                                <c:param name="submitButton" value="${submitButton}" />
                                <c:param name="startDate" value="${startDate}" />
                                <c:param name="endDate" value="${endDate}" />
                            </c:url> 
                        </c:if>
                        <c:if test="${submitButton=='Search' && searchInput != null && searchInput != ''}">
                            <c:url value="/admin/order" var="paginationPrevous">
                                <c:param name="currentPage" value="${currentPage-1}" />
                                <c:param name="submitButton" value="${submitButton}" />
                                <c:param name="searchInput" value="${searchInput}" />
                            </c:url> 
                        </c:if>
                        <c:if test="${submitButton == null || submitButton == ''}">
                            <c:url value="/admin/order" var="paginationPrevous">
                                <c:param name="currentPage" value="${currentPage-1}" />
                            </c:url> 
                        </c:if>
                        <a  href="${paginationPrevous}">Previous</a>
                    </c:if>

                    <c:forEach begin="1" end="${numberOfPage}" step="1" var="stepValue">
                        <c:choose>
                            <c:when test="${stepValue == currentPage}">
                                <a style="background-color: brown; color: white">${stepValue}</a>
                            </c:when>
                            <c:otherwise>
                                <c:if test="${submitButton=='Filter' && startDate != null && endDate != null}">
                                    <c:url value="/admin/order" var="pagination">
                                        <c:param name="currentPage" value="${stepValue}" />
                                        <c:param name="submitButton" value="${submitButton}" />
                                        <c:param name="startDate" value="${startDate}" />
                                        <c:param name="endDate" value="${endDate}" />
                                    </c:url>
                                </c:if>
                                <c:if test="${submitButton=='Search' && searchInput != null && searchInput != ''}">
                                    <c:url value="/admin/order" var="pagination">
                                        <c:param name="currentPage" value="${stepValue}" />
                                        <c:param name="submitButton" value="${submitButton}" />
                                        <c:param name="searchInput" value="${searchInput}" />
                                    </c:url> 
                                </c:if>
                                <c:if test="${submitButton == null || submitButton == ''}">
                                    <c:url value="/admin/order" var="pagination">
                                        <c:param name="currentPage" value="${stepValue}" />
                                    </c:url> 
                                </c:if>
                                <a href="${pagination}">${stepValue} </a>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                    <c:if test="${currentPage<numberOfPage}">
                        <c:if test="${submitButton=='Filter' && startDate != null && endDate != null}">
                            <c:url value="/admin/order" var="paginationNext">
                                <c:param name="currentPage" value="${currentPage+1}" />
                                <c:param name="submitButton" value="${submitButton}" />
                                <c:param name="startDate" value="${startDate}" />
                                <c:param name="endDate" value="${endDate}" />
                            </c:url>
                        </c:if>
                        <c:if test="${submitButton=='Search' && searchInput != null && searchInput != ''}">
                            <c:url value="/admin/order" var="paginationNext">
                                <c:param name="currentPage" value="${currentPage+1}" />
                                <c:param name="submitButton" value="${submitButton}" />
                                <c:param name="searchInput" value="${searchInput}" />
                            </c:url> 
                        </c:if>
                        <c:if test="${submitButton == null || submitButton == ''}">
                            <c:url value="/admin/order" var="paginationNext">
                                <c:param name="currentPage" value="${currentPage+1}" />
                            </c:url> 
                        </c:if>
                        <a  href="${paginationNext}">Next</a>
                    </c:if>
                </div>
            </c:if>
        </div>
    </div>
</div>
<script>
    function check(message) {
        if (confirm(message) === false) {
            return false;
        } else {
            return true;
        }
    }
</script>
<c:import url="template/adminFooter.jsp" />