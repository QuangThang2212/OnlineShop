
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<c:import url="template/header.jsp" />
<div id="form">
    <div id="form-title">
        <span><a href="<c:url value="/account/signup"/>">SIGN UP</a></span>
        <span><a href="<c:url value="/account/signin"/>"  style="color: red;">SIGN IN</a></span>
    </div>                   
    <div id="form-content">
        <c:if test="${msg!=null}">
            <div style="color: red; display: flex; justify-content: center;">${msg}</div>
            <c:remove var="msg"/>
        </c:if>
        <form action="<c:url value="/account/signin"/>" method="post" onsubmit="return validation()">
            <label>Email<span style="color: red;">*</span></label><br/>
            <input type="text" name="email" id="email"/><br/>
            <div class="msg-error" id="EmailMsg"></div>
            <label>Password<span style="color: red;">*</span></label><br/>
            <input type="password" name="pass" id="pass"/><br/>
            <div class="msg-error" id="PassMsg"></div>
            <div><a href="<c:url value="/forgot?"/>">Forgot password</a></div>
            <input type="submit" value="SIGN IN"/><br/>
            <input type="button" value="FACEBOOK LOGIN" style="background-color: #3b5998;"/><br/>
            <input type="button" value="ZALO LOGIN" style="background-color: #009dff;margin-bottom: 30px;"/>
        </form>
    </div>
</div>
<script>
    function validation() {
        var email = document.getElementById("email").value;
        var pass = document.getElementById("pass").value;

        var emailPattern = /[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$/;
        var check = true;

        if (email === "") {
            document.getElementById("EmailMsg").innerHTML = "Email is required";
            check = false;
        } else if (emailPattern.test(email) === false) {
            document.getElementById("EmailMsg").innerHTML = "Email wrong format";
            check = false;
        } else {
            document.getElementById("EmailMsg").innerHTML = "";
        }
        if (pass === "") {
            document.getElementById("PassMsg").innerHTML = "Password is required";
            check = false;
        } else {
            document.getElementById("PassMsg").innerHTML = "";
        }
        return check;
    }
</script>            
<c:import url="template/footer.jsp" />