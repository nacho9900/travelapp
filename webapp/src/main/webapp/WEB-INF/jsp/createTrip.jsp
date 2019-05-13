<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
Autocomplete -> Cargar datos a la db (place id) -> Inverse geocoding -> place data
var a = autocomplete.getPlace().geometry.location
a.lng
a.lat
--%>
<!DOCTYPE html>
<html>
<head>
    <c:url value="/webjars/bootstrap/4.1.3/css/bootstrap.min.css" var="bootcss"/>
    <c:url value="/webjars/jquery/3.2.1/jquery.min.js" var="jquery" />
    <c:url value="/webjars/bootstrap/4.1.3/js/bootstrap.min.js" var="bootjs"/>
    <c:url value="/resources/icons/globe.ico" var="iconURL"/>
    <c:url value="/resources/css/createTrip.css" var="createTripcss"/>
    <c:url value="/resources/js/createTrip.js" var="createTripjs"/>
    <c:url value="/home/create-trip" var="createTripURL" />
    <link rel="stylesheet" href="${bootcss}" >
    <link rel="stylesheet" href="${createTripcss}">
    <link rel="shortcut icon" href="${iconURL}" type="image/x-icon"/>
    <title>Create Trip</title>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <meta charset="utf-8">
</head>
<body>
<h3 class="display-4 center-pill">Create Trip</h3>
<div class="container">
    <form:form action="${createTripURL}" method="post" modelAttribute="createTripForm">
        <hr>
        <div>
            <form:errors path="placeInput" cssClass = "alert alert-warning" element="p"/>

            <form:errors path="name" cssClass = "alert alert-warning" element="p"/>
            <form:input class="form-control" type="text" path="name"   name="trip_name" placeholder="Enter trip name"/>

            <form:errors path="description" cssClass = "alert alert-warning" element="p"/>
            <form:input class="form-control" type="text" path="description" name="trip_desc" placeholder="Enter trip description"/>

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
        <button type="submit" class="btn-primary" >Create</button>
    </form:form>
</div>
<script src="${createTripjs}"></script>
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDf5BlyQV8TN06oWY_U7Z_MnqWjIci2k2M&libraries=places&callback=initMap"
        async defer></script>
<script src="${jquery}" type="text/javascript"></script>
<script src="${bootjs}" type="text/javascript"></script>
</body>
</html>
