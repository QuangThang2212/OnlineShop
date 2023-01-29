<%-- 
    Document   : index
    Created on : Sep 23, 2022, 1:45:24 PM
    Author     : PQT2212
--%>
<%@ page import = "java.io.*,java.util.*,java.sql.*"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/sql" prefix = "sql"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<c:if test="${Acc.account.role!=1 || Acc==null}">
    <c:import url="template/header.jsp" />
</c:if>
<c:if test="${Acc.account.role==1}">
    <c:import url="template/adminHeader.jsp" />
</c:if>
<div id="content-left">
    <sql:setDataSource var = "snapshot" driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver"
                       url = "jdbc:sqlserver://MYLAP2212\\SQLEXPRESS:1433;databaseName=PRJ301_DB"
                       user = "sa"  password = "123456"/>

    <sql:query dataSource = "${snapshot}" var = "categorys">
        SELECT * FROM Categories;
    </sql:query>
    <h3>CATEGORY</h3>
    <ul>
        <c:forEach var="category" items="${categorys.rows}">
            <c:choose>
                <c:when test="${categoryID == category.categoryID}">
                    <li class="choose">${category.categoryName}</li>
                    </c:when>            
                    <c:otherwise>
                        <c:url value="/home" var="categoryURL">
                            <c:param name="categoryId" value="${category.categoryID}"/>
                        </c:url>
                    <a href="${categoryURL}"><li>${category.categoryName}</li></a>
                        </c:otherwise>  
                    </c:choose>         
                </c:forEach>
    </ul>
</div>
<div id="content-right">
    <div class="path">
        <form action="<c:url value="/home"/>" id="searchForm" method="post">
            <label>Search product</label>
            <div class="product-search-form">
                <input type="text" name="search" id="search" placeholder="Enter search keyword" value="${searchInput}"/>
                <input type="submit" value="Search"/>
            </div>             
        </form>
    </div>
    <c:if test="${currentPage==1 && (searchInput==''||searchInput==null)}">
        <div class="path">Hot</b></div>
        <c:if test="${hotProductsMsg!=null}">
            <div style="margin: 10px">${hotProductsMsg}</div>
        </c:if>
        <div class="content-main">  
            <div class="home">
                <c:forEach items="${hotProducts}" var="hotProduct">
                    <div class="product">                   
                        <c:url var="productDetail" value="/productDetail">
                            <c:param value="${hotProduct.productID}" name="id"/>
                        </c:url>
                        <a href="${productDetail}"><img src="<c:url value="img/1.jpg"/>" width="100%"/></a>     
                        <div class="product-content">
                            <div class="name"><a href="${productDetail}">${hotProduct.productName}</a></div>
                            <div class="price">${hotProduct.unitPrice}</div>
                            <div><a href="${productDetail}" class="homeProductButton">Buy now</a></div> 
                        </div>
                    </div>
                </c:forEach>   
            </div>           
        </div>

        <div class="path">Best Sale</b></div>
        <c:if test="${bestSaleZoneMsg!=null}">
            <div style="margin: 10px">${bestSaleZoneMsg}</div>
        </c:if>
        <div class="content-main">  
            <div class="home">
                <c:forEach items="${bestSaleProducts}" var="bestSaleProduct">
                    <div class="product">
                        <c:url var="productDetail" value="/productDetail">
                            <c:param value="${bestSaleProduct.productID}" name="id"/>
                        </c:url>
                        <a href="${productDetail}"><img src="<c:url value="img/1.jpg"/>" width="100%"/></a>
                        <div class="product-content">
                            <div class="name"><a href="${productDetail}">${bestSaleProduct.productName}</a></div>
                            <div class="price">${bestSaleProduct.unitPrice}</div>
                            <div><a href="${productDetail}" class="homeProductButton">Buy now</a></div>
                        </div>
                    </div>
                </c:forEach>   
            </div>
        </div>
        <div class="path">New Product</b></div>
        <c:if test="${newProductsMsg!=null}">
            <div style="margin: 10px">${newProductsMsg}</div>
        </c:if>
        <div class="content-main">   
            <div class="home">
                <c:forEach items="${newProducts}" var="newProduct">
                    <div class="product">
                        <c:url var="productDetail" value="/productDetail">
                            <c:param value="${newProduct.productID}" name="id"/>
                        </c:url>
                        <a href="${productDetail}"><img src="<c:url value="img/1.jpg"/>" width="100%"/></a>
                        <div class="product-content">
                            <div class="name"><a href="${productDetail}">${newProduct.productName}</a></div>
                            <div class="price">${newProduct.unitPrice}</div>
                            <div><a href="${productDetail}" class="homeProductButton">Buy now</a></div>
                        </div>
                    </div>
                </c:forEach>              
            </div>
        </div>
    </c:if>
    <div class="path">Product</b></div>
    <c:if test="${productsMsg!=null}">
        <div style="margin: 10px">${productsMsg}</div>
    </c:if>
    <div class="content-main">   
        <div class="home">
            <c:forEach items="${productsPagiantion}" var="product">
                <div class="product">
                    <c:url var="productDetail" value="/productDetail">
                        <c:param value="${product.product.productID}" name="id"/>
                    </c:url>
                    <a href="${productDetail}"><img src="<c:url value="img/1.jpg"/>" width="100%"/></a>
                    <div class="product-content">
                        <div class="name"><a href="${productDetail}">${product.product.productName}</a></div>
                        <div class="price">${product.product.unitPrice}</div>
                        <div class="button"><a href="${productDetail}" class="homeProductButton">Buy now</a></div>
                    </div>
                </div>
            </c:forEach>    
        </div>          
    </div>
    <c:if test="${numberOfPage>0 || numberOfPage!=null}">
        <div class="pagination">
            <c:if test="${currentPage>1}">
                <c:url value="/home" var="paginationPrevous">
                    <c:param name="currentPage" value="${currentPage-1}" />
                </c:url>
                <a  href="${paginationPrevous}">Previous</a>
            </c:if>

            <c:forEach begin="1" end="${numberOfPage}" step="1" var="stepValue">
                <c:choose>
                    <c:when test="${stepValue == currentPage}">
                        <a style="background-color: brown; color: white">${stepValue}</a>
                    </c:when>
                    <c:otherwise>
                        <c:if test="${categoryID != null && searchInput == null}">
                            <c:url value="/home" var="pagination">
                                <c:param name="categoryId" value="${categoryID}" />
                                <c:param name="currentPage" value="${stepValue}" />
                            </c:url> 
                        </c:if>
                        <c:if test="${searchInput != null && categoryID == null}">
                            <c:url value="/home" var="pagination">
                                <c:param name="searchInput" value="${searchInput}" />
                                <c:param name="currentPage" value="${stepValue}" />
                            </c:url> 
                        </c:if>
                        <c:if test="${categoryID == null && searchInput == null}">
                            <c:url value="/home" var="pagination">
                                <c:param name="currentPage" value="${stepValue}" />
                            </c:url> 
                        </c:if>
                        <a href="${pagination}">${stepValue} </a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            <c:if test="${currentPage<numberOfPage}">
                <c:url value="/home" var="paginationNext">
                    <c:param name="currentPage" value="${currentPage+1}" />
                </c:url>
                <a  href="${paginationNext}">Next</a>
            </c:if>
        </div>
    </c:if>
</div>
<script>
    var msg = '${homeMsg}';
    console.log(msg);
    if (msg !== "") {
        alert(msg + "\n\nReturn to home");
    <c:remove var="homeMsg"/>
    }
    
</script>
<c:if test="${Acc.account.role!=1 || Acc==null}">
    <c:import url="template/footer.jsp" />
</c:if>
<c:if test="${Acc.account.role==1}">
    <c:import url="template/adminFooter.jsp" />
</c:if>