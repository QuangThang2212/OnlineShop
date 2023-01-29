
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
    <c:if test="${product==null || productCUAction=='save'}">
        <div class="path-admin">CREATE A NEW PRODUCT</b></div>
    </c:if>
    <c:if test="${product!=null || productCUAction=='update'}">
        <div class="path-admin">UPDATE PRODUCT</b></div>
    </c:if> 

    <div class="content-main">
        <c:if test="${adminCUProductMsg!= null}">
            <div class="msg-error" style="margin: 1%">${adminCUProductMsg}</div>
            <c:remove var="adminCUProductMsg"/>
        </c:if> 

            <form id="content-main-product" method="post" action="<c:url value="/admin/cuProduct"/>" onsubmit="return validationProduct()">   
            <input type="text" name="productid" id="" hidden="true" value="${product.productID}"><br/>
            <div class="content-main-1">                
                <label>Product name (*):</label><br/>
                <input type="text" name="txtProductName" id="productName" value="${product.productName}"><br/>
                <div class="msg-error" id="productNameMsg"></div>

                <label>Unit price:</label><br/>
                <input type="" name="txtUnitPrice" id="unitPrice"  value="${product.unitPrice}"><br/>
                <div class="msg-error" id="unitPriceMsg"></div>


                <label>Quantity per unit:</label><br/>
                <input type="text" name="txtQuantityPerUnit" id="quantityPerUnit" value="${product.quantityPerUnit}"><br/>
                <div class="msg-error" id="quantityPerUnitMsg"></div>

                <label>Units in stock (*):</label><br/>
                <input type="number" name="txtUnitsInStock" id="unitsInStock" value="${product.unitsInStock}"><br/>
                <div class="msg-error" id="unitsInStockMsg"></div>
            </div>
            <div class="content-main-1">
                <label>Category (*):</label><br/>
                <select name="ddlCategory">
                    <sql:query dataSource = "${snapshot}" var = "categorys">
                        SELECT * FROM Categories;
                    </sql:query>
                    <c:forEach var="category" items="${categorys.rows}">  
                        <c:choose>
                            <c:when test="${product.categoryID==category.categoryID}">
                                <option value="${category.categoryID}" selected="true">${category.categoryName}</option>
                            </c:when>
                            <c:otherwise>
                                <option value="${category.categoryID}">${category.categoryName}</option>
                            </c:otherwise>
                        </c:choose>                               
                    </c:forEach>
                </select>
                <br/>
                <div class="msg-error" id="categoryMsg"></div>
                
                <label>Reorder level:</label><br/>
                <input type="text" name="txtReorderLevel" id="reorderLevel" disabled value="${product.reorderLevel}"><br/>
                <div class="msg-error" id="reorderLevelMsg"></div>
                
                <label>Units on order:</label><br/>
                <input type="text" name="txtUnitsOnOrder" id="unitsOnOrder" disabled value="${product.unitsOnOrder}"><br/>
                <div class="msg-error" id="unitsOnOrderMsg"></div>
                
                <label>Discontinued:</label> 
                <c:choose>
                    <c:when test="${product==null || productCUAction =='save'}">
                        <input type="checkbox" name="chkDiscontinued" id="discontinued"><br/>
                    </c:when>
                    <c:when test="${product!=null || productCUAction =='update'}">
                        <c:if test="${product.discontinued==true}">
                            <input type="checkbox" name="chkDiscontinued" id="discontinued" checked="true"><br/>
                        </c:if>
                        <c:if test="${product.discontinued==false}">
                            <input type="checkbox" name="chkDiscontinued" id="discontinued"><br/>
                        </c:if>
                    </c:when>
                </c:choose>
                <div class="msg-error" id="discontinuedkMsg"></div>

                <c:if test="${product==null  || productCUAction =='save'}">
                    <input type="submit" name="submitProductButton" value="Save"/>
                </c:if>
                <c:if test="${product!=null  || productCUAction =='update'}">
                    <input type="submit" name="submitProductButton" value="Update"/>
                </c:if>               
            </div>
        </form>
    </div>
</div>
<script>
    function validationProduct() {
        var productName = document.getElementById("productName").value;
        var unitPrice = document.getElementById("unitPrice").value;
        var unitsInStock = document.getElementById("unitsInStock").value;
        var check = true;
        if (productName === "" || productName === null) {
            document.getElementById("productNameMsg").innerHTML = "Product name is required";
            check = false;
        } else {
            document.getElementById("productNameMsg").innerHTML = "";
        }
        if (isNaN(unitPrice) || unitPrice < 0 || unitPrice ==="") {
            document.getElementById("unitPriceMsg").innerHTML = "Unit Price is required to be numberic and equal or greater than 0";
            check = false;
        } else {
            document.getElementById("unitPriceMsg").innerHTML = "";
        }
        if (unitsInStock < 0 || unitsInStock === "") {
            document.getElementById("unitsInStockMsg").innerHTML = "Units In Stock is required to be numberic and equal or greater than 0";
            check = false;
        } else {
            document.getElementById("unitsInStockMsg").innerHTML = "";
        }
        
        return check;
    }
</script>
<c:import url="template/adminFooter.jsp" />