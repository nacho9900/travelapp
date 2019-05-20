<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <%@include file="head.jsp" %>
    <c:url value="/home/trip/${tripId}/create-activity" var="postURL"/>
    <c:url value="/resources/css/map.css" var="createTripCss"/>
    <c:url value="/resources/js/map.js" var="createTripJs"/>
    <link rel="stylesheet" href="${createTripCss}">
    <title>Create Activity</title>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <meta charset="utf-8">
</head>
<body>
<jsp:include page="header.jsp"/>

<div class="container-fluid" id="main-container">
    <div class="container box" style="margin-top: 20px;">
        <div class="row">
            <div class="col-1"></div>
            <div class="col-10">
                <div class="form-group">
                    <div class="display-4 text-white">Create Activity</div>
                </div>
                <form:form action="${postURL}" method="post" modelAttribute="activityForm">
                    <div class="form-group">
                        <form:errors path="name" cssClass="alert alert-warning" element="p"/>
                        <form:input type="text" placeholder="Enter activity name" path="name" cssClass="form-control"/>
                    </div>
                    <div class="form-group">
                        <form:errors path="category" cssClass="alert alert-warning" element="p"/>
                        <form:input type="text" placeholder="Category" path="category" name="category-input"
                                    cssClass="form-control"/>
                    </div>
                    <form:errors path="placeInput" cssClass="alert alert-warning" element="p"/>
                    <c:if test="${errorMap}">
                        <div class="alert alert-danger" role="alert">Error, invalid google place location</div>
                    </c:if>
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
    </div>
</div>
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDf5BlyQV8TN06oWY_U7Z_MnqWjIci2k2M&libraries=places&callback=initMap"
        async defer></script>
<script src="${createTripJs}"></script>
</body>
</html>
