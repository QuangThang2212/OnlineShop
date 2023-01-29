<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<c:import url="template/header.jsp" />
<div id="form">
    <h3 style="padding: 20px;">Forgot your account password?</h3>
    <div style="padding: 0px 20px 10px;">
        Please enter the email address registered with us to create a new password. We will send an email to the email address provided and require verification before we can generate a new password
    </div>

    <div id="form-content">
        <form action="<c:url value="/forgot"/>" method="post" onsubmit="return validation()">
            <label>Enter your registered email address<span style="color: red;">*</span></label><br/>
            <input type="text" name="email" id="email" value="${email}"/><br/>
            <c:if test="${Message!=null}">
                <div class="msg-error" id="Message" style="padding-bottom: 5%; color: green">${Message}</div>
            </c:if>   
            <div class="msg-error" id="EmailMsg" style="padding-bottom: 5%">${EmailMsg}</div> 
            <c:remove var="EmailMsg"/>
            <input type="submit" value="GET PASSWORD" style="margin-bottom: 30px;"/><br/>
        </form>
    </div>
</div>
<script>
    function validation() {
        var email = document.getElementById("email").value;

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
        return check;
    }
</script>
<c:import url="template/footer.jsp" />
