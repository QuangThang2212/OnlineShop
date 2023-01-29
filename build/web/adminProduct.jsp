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
        <li class="choose">Products</li>
        <a href="<c:url value="/admin/customer"/>"><li>Customers</li></a>
    </ul>
</div>
<div id="content-right">
    <div class="path-admin">PRODUCTS LIST</b></div>
    <div class="content-main">
        <div id="content-main-dashboard">
            <div id="product-title-header">
                <div id="product-title-1" style="width: 25%;">
                    <b>Filter by Catetory:</b>
                    <form action="<c:url value="/admin/product"/>" method="post">
                        <select name="ddlCategory">
                            <option value="-1">--- No filter ---</option>
                            <sql:query dataSource = "${snapshot}" var = "categorys">
                                SELECT * FROM Categories;
                            </sql:query>
                            <c:forEach var="category" items="${categorys.rows}">  
                                <c:choose>
                                    <c:when test="${category.categoryID==filterInput}">
                                        <option value="${category.categoryID}" selected="true">${category.categoryName}</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="${category.categoryID}">${category.categoryName}</option>
                                    </c:otherwise>
                                </c:choose>                               
                            </c:forEach>
                        </select>
                        <input type="submit"  name="submitButton" value="Filter">
                    </form>
                </div>
                <div id="product-title-2" style="width: 55%;">
                    <form action="<c:url value="/admin/product"/>" method="post">
                        <input type="text" name="txtSearch" placeholder="Enter product name to search" value='${searchInput}'/>
                        <input type="submit" name="submitButton" value="Search"/><br/>
                        <c:if test="${searchProductMsg!= null}">
                            <div class="msg-error" style="margin-bottom: 1.5%">${searchProductMsg}</div>
                            <c:remove var="searchProductMsg"/>
                        </c:if> 
                    </form>
                </div>
                <div id="product-title-3" style="width: 20%;">
                    <div><a href="<c:url value="/admin/cuProduct"/>">Create a new Product</a></div>
                    <form action="" methed="post">
                        <label for="upload-file">Import .xls or .xlsx file</label>
                        <input type="file" name="submitButton" id="upload-file" />
                    </form>
                </div>
            </div>
            <c:if test="${adminProductMsg!= null}"> 
                <div style="padding: 0% 1.5% 3% 1.5%; color: green">${adminProductMsg}</div>
                <c:remove var="adminProductMsg"/>
            </c:if>
            <c:if test="${adminProductMsgWarn!= null}"> 
                <div class="msg-error" style="padding: 0% 1.5% 3% 1.5%">${adminProductMsgWarn}</div>
                <c:remove var="adminProductMsgWarn"/>
            </c:if>
            <c:if test="${ not empty adminProductPagination}">
                <div id="order-table-admin">
                    <table id="orders">
                        <tr>
                            <th>ID</th>
                            <th>ProductName</th>
                            <th>UnitPrice</th>
                            <th>Unit</th>
                            <th>UnitsInStock</th>
                            <th>Category</th>
                            <th>Discontinued</th>
                            <th></th>
                        </tr>
                        <c:forEach items="${adminProductPagination}" var="aPP">
                            <tr>
                                <td>
                                    <c:url var="productDetail" value="/productDetail">
                                        <c:param value="${aPP.product.productID}" name="id"/>
                                    </c:url>
                                    <a href="${productDetail}">#${aPP.product.productID}</a>
                                </td>
                                <td>${aPP.product.productName}</td>
                                <td>${aPP.product.unitPrice}</td>
                                <td>${aPP.product.quantityPerUnit}</td>
                                <td>${aPP.product.unitsInStock}</td>
                                <td>${aPP.category.categoryName}</td>
                                <td>${aPP.product.discontinued}</td>
                                <td>
                                    <c:url var="updateProduct" value="/admin/cuProduct">
                                        <c:param name="id" value="${aPP.product.productID}"/>
                                    </c:url>
                                    <c:url var="delateProduct" value="/admin/cuProduct">
                                        <c:param name="id" value="${aPP.product.productID}"/>
                                        <c:param name="action" value="delete"/>
                                    </c:url>
                                    <a href="${updateProduct}">Edit</a> &nbsp; | &nbsp; 
                                    <a href="${delateProduct}" onclick="return check()">Delete</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>
                </div>
            </c:if>
            <c:if test="${numberOfPage>0 || numberOfPage!=null}">
                <div class="pagination">
                    <c:if test="${currentPage>1}">
                        <c:choose>                                                    
                            <c:when test="${filterInput != null && filterInput !=''}">
                                <c:url value="/admin/product" var="paginationPrevous">
                                    <c:param name="currentPage" value="${currentPage-1}" />
                                    <c:param name="filterInput" value="${filterInput}" />
                                </c:url> 
                            </c:when>
                            <c:when test="${searchInput != null && searchInput !=''}">
                                <c:url value="/admin/product" var="paginationPrevous">
                                    <c:param name="currentPage" value="${currentPage-1}" />
                                    <c:param name="searchInput" value="${searchInput}" />
                                </c:url> 
                            </c:when>
                            <c:otherwise>
                                <c:url value="/admin/product" var="paginationPrevous">
                                    <c:param name="currentPage" value="${currentPage-1}" />
                                </c:url> 
                            </c:otherwise>
                        </c:choose>
                        <a  href="${paginationPrevous}">Previous</a>
                    </c:if>

                    <c:forEach begin="1" end="${numberOfPage}" step="1" var="stepValue">
                        <c:choose>
                            <c:when test="${stepValue == currentPage}">
                                <a style="background-color: brown; color: white">${stepValue}</a>
                            </c:when>
                            <c:otherwise>
                                <c:choose>
                                    <c:when test="${filterInput != null && filterInput !=''}">
                                        <c:url value="/admin/product" var="pagination">
                                            <c:param name="currentPage" value="${stepValue}" />
                                            <c:param name="filterInput" value="${filterInput}" />
                                        </c:url> 
                                    </c:when>
                                    <c:when test="${searchInput != null && searchInput !=''}">
                                        <c:url value="/admin/product" var="pagination">
                                            <c:param name="currentPage" value="${stepValue}" />
                                            <c:param name="searchInput" value="${searchInput}" />
                                        </c:url> 
                                    </c:when>
                                    <c:otherwise>
                                        <c:url value="/admin/product" var="pagination">
                                            <c:param name="currentPage" value="${stepValue}" />
                                        </c:url> 
                                    </c:otherwise>
                                </c:choose>
                                <a href="${pagination}">${stepValue} </a>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>                    
                    <c:if test="${currentPage<numberOfPage}">
                        <c:choose>
                            <c:when test="${filterInput != null && filterInput !=''}">
                                <c:url value="/admin/product" var="paginationNext">
                                    <c:param name="currentPage" value="${currentPage+1}" />
                                    <c:param name="filterInput" value="${filterInput}" />
                                </c:url> 
                            </c:when>
                            <c:when test="${searchInput != null && searchInput !=''}">
                                <c:url value="/admin/product" var="paginationNext">
                                    <c:param name="currentPage" value="${currentPage+1}" />
                                    <c:param name="searchInput" value="${searchInput}" />
                                </c:url> 
                            </c:when>
                            <c:otherwise>
                                <c:url value="/admin/product" var="paginationNext">
                                    <c:param name="currentPage" value="${currentPage+1}" />
                                </c:url> 
                            </c:otherwise>
                        </c:choose>                        
                        <a  href="${paginationNext}">Next</a>
                    </c:if>
                </div>
            </c:if>
        </div>
    </div>
</div>
<script>
    function check() {
        return confirm("Are you sure to delete this product from database");
    }
</script>
<c:import url="template/adminFooter.jsp" />