<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <c:url value="/webjars/bootstrap/4.1.3/css/bootstrap.min.css" var="bootstrapCss"/>
    <c:url value="/webjars/jquery/3.2.1/jquery.min.js" var="jquery" />
    <c:url value="/webjars/bootstrap/4.1.3/js/bootstrap.min.js" var="bootjs"/>
    <c:url value="/resources/icons/globe.ico" var="iconURL"/>
    <c:url value="/" var="index"/>
    <c:url value="/home" var="home"/>
    <c:url value="/home/trips/1" var="trips"/>
    <c:url value="/home/create-trip" var="createTrip"/>
    <c:url value="/about" var="about"/>
    <c:url value="/resources/css/about.css" var="aboutCss"/>
    <c:url value="/resources/icons/earth-globe.png" var="globeIMG"/>
    <c:url value="/resources/icons/uu.png" var="userIMG"/>
    <c:url value="/signin" var="signinURL"/>
    <c:url value="/home/profile/${user.id}" var="profile"/>
    <c:url value="/logout" var="logoutUrl"/>
    <link href="${bootstrapCss}" rel="stylesheet">
    <link href="${aboutCss}" rel="stylesheet">
    <link rel="shortcut icon" href="${iconURL}" type="image/x-icon"/>
    <title>About us</title>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <a class="navbar-brand" href="${index}">
            <img src="${globeIMG}" height="42" width="42"/>
        </a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav mr-auto">
                <li class="nav-item active">
                    <a class="nav-link" href="${home}">Home <span class="sr-only">(current)</span></a>
                </li>
                <li class="nav-item">
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${trips}">My trips</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${createTrip}">Create trip</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#">About us</a>
                </li>
            </ul>
            <form class="form-inline my-2 my-lg-0">
                <input class="form-control mr-sm-2" type="search" placeholder="Search for trips" aria-label="Search">
                <button class="btn btn-success my-2 my-sm-0" type="submit">Search</button>
            </form>
            <c:choose>
                <c:when test="${user == null}">
                    <a id="signinButton" style="margin-left: 15px" class="btn btn-success my-2 my-sm-0" href="${signinURL}">Sign in</a>
                </c:when>
                <c:otherwise>
                    <a href="${profile}">
                        <img alt="" style="margin-left: 10px" src="${userIMG}" height="32" width="32"/>
                    </a>
                    <a style="margin-left: 15px" class="btn btn-dark my-2 my-sm-0" href="${logoutUrl}"><spring:message code="header.logout"/></a>
                </c:otherwise>
            </c:choose>
        </div>
    </nav>
    <div id="myContainer" class="container align-items-center">
        <h3 id="myHeader" class="display-4">PAW 2019 1er cuatrimestre grupo 4</h3>
        <ul class="list-group" >
            <li class="list-group-item">Felipe Gorostiaga - 57200</li>
            <li class="list-group-item">Ignacio Negro</li>
        </ul>
    </div>
    <script src="${bootjs}" type="text/javascript"></script>
    <script src="${jquery}" type="text/javascript"></script>
</body>
</html>
