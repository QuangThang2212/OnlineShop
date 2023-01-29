<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="template/adminHeader.jsp" />
<div id="content-left">
    <ul>
        <a href="<c:url value="/admin"/>"><li>Dashboard</li></a>
        <a href="<c:url value="/admin/order"/>"><li>Orders</li></a>
        <a href="<c:url value="/admin/product"/>"><li>Products</li></a>
        <li class="choose">Customers</li>
    </ul>
</div>
<div id="content-right">
    <div class="path-admin">CUSTOMERS LIST</b></div>
    <div class="content-main">
        <div id="content-main-dashboard">
            <div id="product-title-header">
                <div id="product-title-2" style="width: 50%; padding: 1% 25% 4% 25%">
                    <form action="<c:url value="/admin/customer"/>" method="post">
                        <input type="text" name="txtSearch" placeholder="Enter Company name, contact title or contact name to search" value='${searchInput}'/>
                        <input type="submit" name="submitButton" value="Search"/><br/>
                        <c:if test="${searchCustomerMsg!= null}">
                            <div class="msg-error" style="margin-bottom: 1.5%">${searchCustomerMsg}</div>
                            <c:remove var="searchCustomerMsg"/>
                        </c:if> 
                    </form>
                </div>
            </div>
            <c:if test="${actionCusMsg!= null}">
                <div class="msg-error" style="padding: 0% 1.5% 3% 1.5%">${actionCusMsg}</div>
                <c:remove var="actionCusMsg"/>
            </c:if>  
            <c:if test="${not empty accountCustomers}">
                <div id="order-table-admin">
                    <table id="orders">
                        <tr>
                            <th>Customer ID</th>
                            <th>Company name</th>
                            <th>Contact name</th>
                            <th>Contact title</th>
                            <th>Address</th>
                            <th>Email</th>
                            <th>Create date</th>
                        </tr>
                        <c:forEach items="${accountCustomers}" var="accCus">
                            <tr>
                                <td>${accCus.customers.customerID}</td>
                                <td>${accCus.customers.companyName}</td>
                                <td>${accCus.customers.contactName}</td>
                                <td>${accCus.customers.contactTitle}</td>
                                <td>${accCus.customers.address}</td>
                                <c:if test="${accCus.account.email!=null}">
                                    <td>${accCus.account.email}</td>
                                </c:if>
                                <c:if test="${accCus.account.email==null}">
                                    <td style="font-weight: bold">Dont sign up yet</td>
                                </c:if>
                                <td>${accCus.customers.createDate}</td>
                            </tr> 
                        </c:forEach>
                    </table>
                </div>
            </c:if>
            <c:if test="${numberOfPage>0 || numberOfPage!=null}">
                <div class="pagination">
                    <c:if test="${currentPage>1}">                        
                        <c:if test="${searchInput != null}">
                            <c:url value="/admin/customer" var="paginationPrevous">
                                <c:param name="searchInput" value="${searchInput}" />
                                <c:param name="currentPage" value="${currentPage-1}" />
                            </c:url> 
                        </c:if>
                        <c:if test="${searchInput == null}">
                            <c:url value="/admin/customer" var="paginationPrevous">
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
                                <c:if test="${searchInput != null}">
                                    <c:url value="/admin/customer" var="pagination">
                                        <c:param name="searchInput" value="${searchInput}" />
                                        <c:param name="currentPage" value="${stepValue}" />
                                    </c:url> 
                                </c:if>
                                <c:if test="${searchInput == null}">
                                    <c:url value="/admin/customer" var="pagination">
                                        <c:param name="currentPage" value="${stepValue}" />
                                    </c:url>
                                </c:if>                                 
                                <a href="${pagination}">${stepValue} </a>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                    <c:if test="${currentPage<numberOfPage}">
                        <c:if test="${searchInput != null}">
                            <c:url value="/admin/customer" var="pagination">
                                <c:param name="searchInput" value="${searchInput}" />
                                <c:param name="currentPage" value="${currentPage+1}" />
                            </c:url> 
                        </c:if>
                        <c:if test="${searchInput == null}">
                            <c:url value="/admin/customer" var="paginationNext">
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
<c:import url="template/adminFooter.jsp" />
