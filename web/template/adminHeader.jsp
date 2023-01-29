<%-- 
    Document   : adminHeader
    Created on : Oct 26, 2022, 10:00:02 AM
    Author     : PQT2212
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Index</title>
        <c:url var="cssLink" value="/css/style.css"/>
        <link href="${cssLink}" rel="stylesheet"/>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.5.0/Chart.min.js"></script>
    </head>
    <body>
        <div id="container">
            <div id="header">
                <div id="logo-admin">
                    Ecommerce Admin
                </div>
                <div id="banner-admin">
                    <ul>
                        <c:url var="index" value="/home"/>
                        <li><a href="${index}">Home</a></li>
                        <c:url var="adminManage" value="/admin"/>
                        <li><a href="${adminManage}">Admin manage</a></li>
                        <c:url var="login" value="/account/signin"/>
                        <li><a href="${login}">SignOut</a></li>
                    </ul>
                </div>
            </div>
            <div id="content">
