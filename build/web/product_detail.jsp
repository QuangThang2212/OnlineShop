<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<c:if test="${Acc.account.role!=1 || Acc==null}">
    <c:import url="template/header.jsp" />
</c:if>
<c:if test="${Acc.account.role==1}">
    <c:import url="template/adminHeader.jsp" />
</c:if>

<c:set var="product" value="${productCategory.product}"/>
<c:set var="category" value="${productCategory.category}"/>
<div id="content-detail">
    <div id="content-title">
        <c:url var="categoryURL" value="/home">
            <c:param value="${product.categoryID}" name="categoryId"/>
        </c:url>
        <c:url var="home" value="/home"/>
        <a href="${home}">Home</a> >        
        <a href="${categoryURL}">${category.categoryName}</a> > 
        Model: ${product.productName}
    </div>
    <div id="product">
        <div id="product-name">
            <h2>${product.productName}</h2>
            <div id="product-detail">
                <div id="product-detail-left">
                    <div id="product-img">
                        <img src="img/1.jpg"/>
                    </div>
                    <div id="product-img-items">
                        <div><a href="#"><img src="img/1.jpg"/></a></div>
                        <div><a href="#"><img src="img/1.jpg"/></a></div>
                        <div><a href="#"><img src="img/1.jpg"/></a></div>
                        <div><a href="#"><img src="img/1.jpg"/></a></div>
                    </div>
                </div>
                <div id="product-detail-right">
                    <div id="product-detail-right-content">
                        <div id="product-price">$ ${product.unitPrice}</div>
                        <c:choose>
                            <c:when test="${product.discontinued==false && product.unitsInStock!=0}">
                                <div id="product-status">In stock: ${product.unitsInStock}</div>
                            </c:when>
                            <c:when test="${product.unitsInStock==0}">
                                <div id="product-status-Warn">Out of stock</div>
                            </c:when>
                            <c:when test="${product.discontinued==true}">
                                <div id="product-status-Warn">No longer for sale</div>
                            </c:when>
                        </c:choose>                                       
                        <div id="product-detail-buttons">
                            <div id="product-detail-button"> 
                                <form action="<c:url value="/productDetail"/>" id="productDetailForm" method="post" hidden="true">
                                    <input type="text" name="productID" id="productID" value="${product.productID}" readonly="true"/>
                                </form>
                                <c:choose>
                                    <c:when test="${Acc.account.role==1}">
                                        <div>View only</div>
                                    </c:when>
                                    <c:when test="${product.discontinued==false && product.unitsInStock!=0}">
                                        <input type="submit" name="productDetailFormButton" form="productDetailForm" value="BUY NOW"/>
                                        <input type="submit" name="productDetailFormButton" form="productDetailForm" value="ADD TO CART" style="background-color: #fff; color:red;border: 1px solid gray;">
                                    </c:when>
                
                                    <c:otherwise>
                                        <div>Update soon</div>
                                    </c:otherwise>
                                </c:choose>                     
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div id="info-detail">
        <div id="info-detail-title">
            <h2>Information deltail</h2>
            <div style="margin:10px auto;">
                Lorem ipsum dolor sit amet consectetur adipisicing elit. Illum, debitis. Asperiores soluta eveniet eos accusantium doloremque cum suscipit ducimus enim at sapiente mollitia consequuntur dicta quaerat, sunt voluptates autem. Quam!
                Lorem ipsum dolor, sit amet consectetur adipisicing elit. Rem illum autem veritatis maxime corporis quod quibusdam nostrum eaque laborum numquam quos unde eveniet aut, exercitationem voluptatum veniam fugiat, debitis esse?
                Lorem ipsum dolor sit amet consectetur adipisicing elit. Distinctio eligendi ratione vitae nobis numquam dolorum assumenda saepe enim cumque blanditiis, deleniti neque voluptate vel ducimus in omnis harum aut nisi.
            </div>
        </div>
    </div>
</div>
<script>
    var msg = '${productDetailMsg}';
    console.log(msg);
    if (msg !== "") {
        alert(msg);
    <c:remove scope="session" var="productDetailMsg"/>
    }
</script>
<c:if test="${Acc.account.role!=1 || Acc==null}">
    <c:import url="template/footer.jsp" />
</c:if>
<c:if test="${Acc.account.role==1}">
    <c:import url="template/adminFooter.jsp" />
</c:if>
