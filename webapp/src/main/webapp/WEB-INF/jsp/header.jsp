<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<c:url value="/home" var="home"/>
<c:url value="/" var="index"/>
<c:url value="/about" var="about"/>
<c:url value="/home/trips/1" var="trips"/>
<c:url value="/home/create-trip" var="createTrip"/>
<c:url value="/resources/icons/earth-globe.png" var="globeIMG"/>
<c:url value="/resources/icons/uu.png" var="userIMG"/>
<c:url value="/signin" var="signinURL"/>
<c:url value="/resources/css/header.css" var="headerCss"/>
<c:url value="/home/search-trip/" var="searchURL"/>
<c:url value="/logout" var="logoutUrl"/>

<link href="${headerCss}" rel="stylesheet">
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <a class="navbar-brand" href="${index}">
        <img src="${globeIMG}" height="42" width="42"/>
    </a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
            aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item active">
                <a class="nav-link" href="${home}"><spring:message code="header.home"/> <span class="sr-only">(current)</span></a>
            </li>
            <li class="nav-item">
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${trips}"><spring:message code="header.myTrips"/></a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${createTrip}"><spring:message code="header.createTrip"/> </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${about}"><spring:message code="header.aboutUs"/> </a>
            </li>
        </ul>
        <ul class="navbar-nav ml-auto">
            <li class="nav-item">
                <form action="${searchURL}" method="get" class="form-inline my-2 my-lg-0 nav-link">
                    <spring:message code="header.searchMessage" var="searchMessage"/>
                    <input id="nameInput" name="nameInput" class="form-control mr-sm-2" type="search"
                           placeholder="${searchMessage}" aria-label="Search">
                    <button class="btn btn-success my-2 my-sm-0" type="submit"><spring:message code="header.searchBtn"/> </button>
                </form>
            </li>
            <c:choose>
                <c:when test="${user == null}">
                    <a id="signinButton" class="btn btn-success my-2 my-sm-0" href="${signinURL}"><spring:message code="header.signInBtn"/> </a>
                </c:when>
                <c:otherwise>
                    <c:url value="/home/profile/${user.id}" var="profile"/>
                    <li class="nav-item align-content-center">
                        <a href="${profile}" class="nav-link">
                            <img alt="" id="profile-nav" src="${userIMG}" height="32" width="32"/>
                        </a>
                    </li>
                    <li class="nav-item">
                    <form:form action="${logoutUrl}" method="post" cssClass="nav-link">
                            <button type="submit" class="btn btn-danger"><spring:message code="header.logout"/> </button>
                    </form:form>
                    </li>
                </c:otherwise>
            </c:choose>
        </ul>
    </div>
</nav>