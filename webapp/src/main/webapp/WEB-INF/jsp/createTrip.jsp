<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <c:url value="/home/create-trip" var="createTripURL"/>

    <%@include file="head.jsp" %>
    <c:url value="/resources/css/map.css" var="createTripcss"/>
    <link rel="stylesheet" href="${createTripcss}">
    <c:url value="/resources/js/map.js" var="createTripjs"/>
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
<jsp:include page="header.jsp"/>
<div class="align-content-center">
    <div class="container">
        <%--TODO: AGREGAR ALERT POR ERROR EN MAP INPUT--%>
        <h3 id="headerID" class="display-4 ">Create Trip</h3>
        <form:form action="${createTripURL}" method="post" modelAttribute="createTripForm">
            <div class="container">
                <form:errors path="placeInput" cssClass="alert alert-warning" element="p"/>

                <form:errors path="name" cssClass="alert alert-warning" element="p"/>
                <form:input class="form-control" cssStyle="margin-bottom: 10px" type="text" path="name" name="trip_name"
                            placeholder="Enter trip name"/>

                <form:errors path="description" cssClass="alert alert-warning" element="p"/>
                <form:textarea class="form-control" type="text" path="description" name="trip_desc"
                               placeholder="Enter trip description"/>
                    <%--<form:input class="form-control" type="text" path="description" name="trip_desc" placeholder="Enter trip description"/>--%>

                <form:errors path="startDate" cssClass="alert alert-warning" element="p"/>
                <form:label path="startDate" for="start_date">Start date:</form:label>
                <form:input class="form-control" type="date" path="startDate" name="start_date"/>

                <form:errors path="endDate" cssClass="alert alert-warning" element="p"/>
                <form:label path="endDate" for="start_date">End date:</form:label>
                <form:input class="form-control" type="date" path="endDate" name="end_date"/>
            </div>
            <div class="pac-card" id="pac-card">
                <div id="pac-container">
                    <form:input id="pac-input" type="text" path="placeInput" placeholder="Enter a location"/>
                </div>
            </div>
            <div class="container" id="map"></div>
            <div id="infowindow-content">
                <img src="" width="16" height="16" id="place-icon">
                <span id="place-name" class="title"></span><br>
                <span id="place-address"></span>
            </div>
            <div class="text-center flex-fill">
                <button type="submit" class="btn-success btn btn-lg">Create</button>
            </div>
        </form:form>
    </div>
</div>
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDf5BlyQV8TN06oWY_U7Z_MnqWjIci2k2M&libraries=places&callback=initMap"
        async defer></script>
<script src="${createTripjs}"></script>
</body>
</html>
