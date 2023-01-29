<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<c:import url="template/header.jsp"/>
<div id="form">   
    <c:if test="${Acc==null}">
        <c:set var="formCusValue" value="${customerSignUp}"/>
        <c:set var="formAccValue" value="${accountSignUp}"/>

        <c:url var="formUrl" value="/account/signup"/>
        <div id="form-title">
            <span><a href="<c:url value="/account/signup"/>" style="color: red;">SIGN UP</a></span>
            <span><a href="<c:url value="/account/signin"/>">SIGN IN</a></span>
        </div>        
    </c:if>
    <c:if test="${Acc!=null}">
        <c:set var="formCusValue" value="${Acc.customers}"/>
        <c:set var="formAccValue" value="${Acc.account}"/>

        <c:url var="formUrl" value="/profile">
            <c:param name="postAction" value="edit-infor"/>
        </c:url>
        <div id="form-title" style="font-size: x-large; font-weight: bold">Update information</div>
    </c:if>
    <div id="form-content" >       
        <form action="${formUrl}" method="post" onsubmit="return validationSignUp()">
            <c:if test="${adminMsg != null}">
                <div style="color: red; display: flex; justify-content: center;" id="adminMsg">${adminMsg}</div>
                <c:remove var="adminMsg"/>
            </c:if>
            <c:if test="${signUpMess != null}">
                <div style="color: red; display: flex; justify-content: center;" id="signUpMess">${signUpMess}</div>
            </c:if>
            <c:if test="${emailExistMess != null}">
                <div style="color: red; display: flex; justify-content: center;" id="emailExistMess">${emailExistMess}</div>
            </c:if>
            <c:if test="${customerExistMess != null}">
                <div style="color: red; display: flex; justify-content: center;" id="customerExistMess">${customerExistMess}</div>
            </c:if>           

            <label>Company name<span style="color: red;">*</span></label><br/>
            <input type="text" id="CompanyName" name="CompanyName" class="customerInput" value="${formCusValue.companyName}"/><br/>
            <div class="msg-error" id="CompanyNameMsg"></div>

            <label>Contact name<span style="color: red;">*</span></label><br/>
            <input type="text" id="ContactName" name="ContactName" class="customerInput" value="${formCusValue.contactName}"/><br/>
            <div class="msg-error" id="ContactNameMsg"></div>

            <label>Contact title<span style="color: red;">*</span></label><br/>
            <input type="text" id="ContactTitle" name="ContactTitle" class="customerInput" value="${formCusValue.contactTitle}"/><br/>
            <div class="msg-error" id="ContactTitleMsg"></div>

            <label>Address<span style="color: red;">*</span></label><br/>
            <input type="text" id="Address" name="Address" class="customerInput" value="${formCusValue.address}"/><br/>
            <div class="msg-error" id="AddressMsg"></div>

            <c:if test="${Acc==null}">
                <label>Email<span style="color: red;">*</span></label><br/>
                <input type="text" id="email" name="email" class="accountInput" value="${formAccValue.email}"/><br/>
                <div class="msg-error" id="EmailMsg"></div>

                <label>Password<span style="color: red;">*</span></label><br/>
                <input type="password" id="pass" name="pass" class="accountInput"><br/>
                <div class="msg-error" id="PassMsg"></div>

                <label>Re-Password<span style="color: red;">*</span></label><br/>
                <input type="password" id="re_pass" name="re_pass" /><br/>
                <div class="msg-error" id="re_passMsg"></div>

                <input type="submit" value="SIGN UP" style="margin-bottom: 30px;"/>
            </c:if>
            <c:if test="${Acc!=null}">               

                <label>Email<span style="color: red;">*</span></label><br/>
                <input type="text" id="email" name="email" class="accountInput" style="margin-bottom: 5%;" value="${Acc.account.email}" readonly="true"/><br/>

                <button type="submit" name="submitButton" style="margin-bottom: 20px;" onclick="return check()">Update information</button>

                <c:url value="/profile" var="back">
                    <c:param value="Personal-infor" name="profileFun"/>
                </c:url>
                <button type="button" style="margin-bottom: 30px;"><a href="${back}">Back</a></button>
            </c:if>           
        </form>
    </div>
</div>
<script>
    function validationSignUp() {
        var CompanyName = document.getElementById("CompanyName").value;
        var ContactName = document.getElementById("ContactName").value;
        var ContactTitle = document.getElementById("ContactTitle").value;
        var Address = document.getElementById("Address").value;

        var emailPattern = /[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$/;
        var addressPattern = /([1-9])+ ([A-Za-z -ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂ ưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ])+, ([A-Za-z ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂ ưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ])+, ([A-Za-z ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂ ưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ])+/;
        var check = true;

        if (CompanyName === "") {
            document.getElementById("CompanyNameMsg").innerHTML = "Company name is required";
            check = false;
        } else {
            document.getElementById("CompanyNameMsg").innerHTML = "";
        }
        if (ContactName === "") {
            document.getElementById("ContactNameMsg").innerHTML = "Contact name is required";
            check = false;
        } else {
            document.getElementById("ContactNameMsg").innerHTML = "";
        }
        if (ContactTitle === "") {
            document.getElementById("ContactTitleMsg").innerHTML = "Contact title required";
            check = false;
        } else {
            document.getElementById("ContactTitleMsg").innerHTML = "";
        }
        if (Address === "") {
            document.getElementById("AddressMsg").innerHTML = "Address is required";
            check = false;
        } else if (addressPattern.test(Address) === false) {
            document.getElementById("AddressMsg").innerHTML = "Address wrong format (number) address, city, country \n(Ex: 22 Hoang Quoc Viet-Cau giay, Ha Noi, Viet Name)";
            check = false;
        } else {
            document.getElementById("AddressMsg").innerHTML = "";
        }
        if ('${Acc==null}' === true) {
            var email = document.getElementById("email").value;
            var pass = document.getElementById("pass").value;
            var re_pass = document.getElementById("re_pass").value;
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
            if (re_pass === "") {
                document.getElementById("re_passMsg").innerHTML = "Re-Password is required";
                check = false;
            } else if (re_pass !== pass) {
                document.getElementById("re_passMsg").innerHTML = "Re-Password and password aren't the same";
                check = false;
            } else {
                document.getElementById("re_passMsg").innerHTML = "";
            }
        }
        return check;
    }
    function check() {
        if (confirm("Are you sure to update your information") === false) {
            return false;
        } else {
            return true;
        }
    }

    var customerExistMess = '${customerExistMess}';
    var listCustomer = document.getElementsByClassName("customerInput");

    if (customerExistMess !== "") {
        for (var i = 0; i < listCustomer.length; i++) {
            listCustomer[i].style.backgroundColor = "rgba(255,0,0,0.55)";
        }
    } else {
        for (var i = 0; i < listCustomer.length; i++) {
            listCustomer[i].style.backgroundColor = "white";
        }
    }

    var emailExistMess = '${emailExistMess}';
    var emailExist = document.getElementById("email");

    if (emailExistMess !== "") {
        emailExist.style.backgroundColor = "rgba(255,0,0,0.55)";
    } else {
        emailExist.style.backgroundColor = "white";
    }
</script> 
<c:import url="template/footer.jsp" />