<%--
  Created by IntelliJ IDEA.
  User: nacho
  Date: 16/05/19
  Time: 11:10
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ page contentType="text/html;charset=UTF-8"%>

<c:url value="/home" var="home"/>
<c:url value="/" var="index"/>
<c:url value="/about" var="about"/>
<c:url value="/home/trips" var="trips"/>
<c:url value="/home/create-trip" var="createTrip"/>
<c:url value="/resources/icons/earth-globe.png" var="globeIMG"/>
<c:url value="/resources/icons/uu.png" var="userIMG"/>
<c:url value="/signin" var="signinURL"/>

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
                <a class="nav-link" href="${about}">About us</a>
            </li>
        </ul>
        <form class="form-inline my-2 my-lg-0">
            <input class="form-control mr-sm-2" type="search" placeholder="Search for trips" aria-label="Search">
            <button class="btn btn-success my-2 my-sm-0" type="submit">Search</button>
        </form>
        <c:choose>
            <c:when test="${user == null}">
                <a id="signinButton" class="btn btn-success my-2 my-sm-0" href="${signinURL}">Sign in</a>
            </c:when>
            <c:otherwise>
                <c:url value="/home/profile/${user.id}" var="profile"/>
                <a href="${profile}">
                    <img alt="" style="margin-left: 10px" src="${userIMG}" height="32" width="32"/>
                </a>
            </c:otherwise>
        </c:choose>
    </div>
</nav>