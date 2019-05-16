<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8"%>

<c:url value="/home" var="home"/>
<c:url value="/about" var="about"/>
<c:url value="/signup" var="signUpUrl"/>
<c:url value="/" var="index"/>
<c:url value="/resources/icons/earth-globe.png" var="globeIMG"/>

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
                <a class="nav-link" href="${home}">Home <span class="sr-only">(current)</span></a>
            </li>
            <li class="nav-item">
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${about}">About us</a>
            </li>
        </ul>
    </div>
    <a class="btn btn-success align-content-center" href="${signUpUrl}">Sign up</a>
</nav>