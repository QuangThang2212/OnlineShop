<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<c:import url="template/header.jsp" />
<div id="form">
    <h3 style="padding: 20px;">Change password</h3>

    <div id="form-content">
        <c:url var="formUrl" value="/profile">
            <c:param name="postAction" value="change-pass"/>
        </c:url>
        <form action="${formUrl}" method="post" onsubmit="return changePasswordValidation()">
            <label>Enter your new password<span style="color: red;">*</span></label><br/>
            <input type="password" name="pass" id="pass"/><br/>
            <div class="msg-error" id="passMsg"></div>
            
            <label>Re-enter your new password<span style="color: red;">*</span></label><br/>
            <input type="password" name="re_pass" id="re_pass"/><br/>
            <div class="msg-error" id="re_passMsg"></div>
            
            <c:if test="${changePassMsg!=null}">
                <span class="msg-error" id="Message" style="padding-bottom: 5%; color: green">${changePassMsg}</span><br/>
            </c:if>
            <input type="submit" value="CHANGE PASSWORD" style="margin-bottom: 20px;"/><br/>
            <c:url value="/profile" var="back">
                <c:param value="Personal-infor" name="profileFun"/>
            </c:url>
            <button type="button" style="margin-bottom: 30px;"><a href="${back}">BACK</a></button>
        </form>
    </div>
</div>
<script>
    function changePasswordValidation() {
        var pass = document.getElementById("pass").value;
        var re_pass = document.getElementById("re_pass").value;
        if (pass === "") {
            document.getElementById("passMsg").innerHTML = "Password is required";
            check = false;
        } else {
            document.getElementById("passMsg").innerHTML = "";
        }
        if (re_pass === "") {
            document.getElementById("re_passMsg").innerHTML = "Re-Password is required";
            check = false;
        } else if (re_pass !== pass) {
            document.getElementById("re_passMsg").innerHTML = "Re-Password and password aren't the same";
            check = false;
        } else {
            document.getElementById("re_passMsg").innerHTML = "";
        }
        return check;
    }
</script>
<c:import url="template/footer.jsp" />
