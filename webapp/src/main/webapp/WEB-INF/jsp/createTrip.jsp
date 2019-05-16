<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <c:url value="/webjars/bootstrap/4.1.3/css/bootstrap.min.css" var="bootstrapCss"/>
    <c:url value="/webjars/jquery/3.2.1/jquery.min.js" var="jquery" />
    <c:url value="/webjars/bootstrap/4.1.3/js/bootstrap.min.js" var="bootjs"/>
    <c:url value="/resources/icons/globe.ico" var="iconURL"/>
    <c:url value="/resources/css/map.css" var="createTripcss"/>
    <c:url value="/resources/js/map.js" var="createTripjs"/>
    <c:url value="/home/create-trip" var="createTripURL" />
    <c:url value="/" var="index"/>
    <c:url value="/home" var="home"/>
    <c:url value="/about" var="about"/>
    <c:url value="/home/trips" var="trips"/>
    <c:url value="/home/create-trip" var="createTrip"/>
    <c:url value="/resources/icons/earth-globe.png" var="globeIMG"/>
    <c:url value="/resources/icons/uu.png" var="userIMG"/>
    <c:url value="/signin" var="signinURL"/>
    <c:url value="/home/profile/${user.id}" var="profile"/>
    <link rel="stylesheet" href="${bootstrapCss}" >
    <link rel="stylesheet" href="${createTripcss}">
    <link rel="shortcut icon" href="${iconURL}" type="image/x-icon"/>
    <title>Create Trip</title>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <meta charset="utf-8">
</head>
<body>
<%-- STOP ENTER KEY FROM SUBMITING FORM AND ONLY ALLOWING CREATE BUTTON TO DO IT
document.getElementById("YOURFORMNAMEHERE").onkeypress = function(e) {
var key = e.charCode || e.keyCode || 0;
if (key == 13) {
e.preventDefault();
}
}--%>
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
        <a href="${profile}">
            <img alt="" style="margin-left: 10px" src="${userIMG}" height="32" width="32"/>
        </a>
    </div>
</nav>
<div class="align-content-center">
    <div class="container">
        <%--TODO: AGREGAR ALERT POR ERROR EN MAP INPUT--%>
        <h3 id="headerID" class="display-4 ">Create Trip</h3>
        <form:form action="${createTripURL}" method="post" modelAttribute="createTripForm">
            <hr>
            <div class="container">
                <form:errors path="placeInput" cssClass = "alert alert-warning" element="p"/>

                <form:errors path="name" cssClass = "alert alert-warning" element="p"/>
                <form:input class="form-control" cssStyle="margin-bottom: 10px" type="text" path="name"   name="trip_name" placeholder="Enter trip name"/>

                <form:errors path="description" cssClass = "alert alert-warning" element="p"/>
                <form:textarea class="form-control" type="text" path="description" name="trip_desc" placeholder="Enter trip description"/>
                <%--<form:input class="form-control" type="text" path="description" name="trip_desc" placeholder="Enter trip description"/>--%>

                <form:errors path="startDate" cssClass = "alert alert-warning" element="p"/>
                <form:label path="startDate" for="start_date">Start date:</form:label>
                <form:input class="form-control" type="date" path="startDate"  name="start_date" />

                <form:errors path="endDate" cssClass = "alert alert-warning" element="p"/>
                <form:label path="endDate" for="start_date">End date:</form:label>
                <form:input class="form-control" type="date" path="endDate" name="end_date" />
            </div>
            <div class="pac-card" id="pac-card">
                <div id="pac-container">
                    <form:input id="pac-input" type="text" path="placeInput" placeholder="Enter a location"/>
                </div>
            </div>
            <div class="container" id="map"></div>
            <div id="infowindow-content">
                <img src="" width="16" height="16" id="place-icon">
                <span id="place-name"  class="title"></span><br>
                <span id="place-address"></span>
            </div>
            <div class="text-center flex-fill">
                <button type="submit" class="btn-success btn btn-lg" >Create</button>
            </div>
        </form:form>
    </div>
</div>
<script src="${createTripjs}"></script>
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDf5BlyQV8TN06oWY_U7Z_MnqWjIci2k2M&libraries=places&callback=initMap"
        async defer></script>
<script src="${jquery}" type="text/javascript"></script>
<script src="${bootjs}" type="text/javascript"></script>
</body>
</html>
