<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <%@include file="head.jsp"%>

    <c:url value="/home/trip/${tripId}/create-activity" var="postURL"/>
    <c:url value="/resources/css/map.css" var="createTripcss"/>
    <c:url value="/resources/js/map.js" var="createTripjs"/>
    <link rel="stylesheet" href="${createTripcss}">
    <title>Create Trip</title>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <meta charset="utf-8">
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="container">
    <form:form action="${postURL}" method="post" modelAttribute="activityForm">

        <form:errors path="name" cssClass = "alert alert-warning" element="p"/>
        <form:input type="text" placeholder="Enter activity name" path="name"/>

        <form:errors path="category" cssClass = "alert alert-warning" element="p"/>
        <form:label path="category" >Categories:</form:label><br>
        <form:input type="text" placeholder="Enter activity name" path="category"/>

        <form:errors path="placeInput" cssClass = "alert alert-warning" element="p"/>
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
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDf5BlyQV8TN06oWY_U7Z_MnqWjIci2k2M&libraries=places&callback=initMap"
        async defer></script>
<script src="${createTripjs}"></script>
</body>
</html>
