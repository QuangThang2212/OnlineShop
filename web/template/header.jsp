<%-- 
    Document   : header
    Created on : Sep 23, 2022, 1:46:12 PM
    Author     : PQT2212
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fn" uri = "http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Index</title>
        <c:url var="cssLink" value="/css/style.css"/>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
        <link href="${cssLink}" rel="stylesheet"/>
    </head>
    <body>
        <div id="container">
            <div id="header">
                <div id="logo">
                    <c:url var="index" value="/home"/>
                    <c:url var="image" value="/img/logo.png"/>
                    <a href="${index}"><img src="${image}"/></a>
                </div>
                <div id="banner">
                    <ul>
                        <li><a href="<c:url value="/Cart"/>">Cart: ${fn:length(productsCart)}</a></li>
                        <c:url var="login" value="/account/signin"/>
                        <c:if test="${Acc==null}">
                            <li><a href="${login}">SignIn</a></li>
                            <li><a href="<c:url value="/account/signup"/>">SignUp</a></li>
                        </c:if>                       
                        <c:if test="${Acc!=null}">
                            <c:url var="profile" value="/profile">
                                <c:param name="profileFun" value="Personal-infor"/>
                            </c:url>
                            <li><a href="${profile}">Profile</a></li>
                            <li><a href="${login}">SignOut</a></li>
                        </c:if>                       
                    </ul>
                </div>
            </div>
            <div id="content">
