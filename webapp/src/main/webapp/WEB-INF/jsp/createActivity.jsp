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
    <title>
        <spring:message code="createActivity.title"/>
    </title>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <meta charset="utf-8">
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDf5BlyQV8TN06oWY_U7Z_MnqWjIci2k2M&libraries=places&callback=initMap"
            async defer></script>
    <script src="${createTripJs}"></script>
</head>
<body>
<jsp:include page="header.jsp"/>

<div class="container-fluid" id="main-container">
    <div class="container box" style="margin-top: 20px;">
        <div class="row">
            <div class="col-12">
                <div class="form-group">
                    <div class="display-4 text-white">
                        <spring:message code="createActivity.pageTitle"/>
                    </div>
                </div>
                <form:form action="${postURL}" method="post" modelAttribute="activityForm">
                    <div class="form-group">
                        <form:errors path="name" cssClass="alert alert-warning" element="p"/>
                        <spring:message code="createActivity.namePh" var="namePh"/>
                        <form:input type="text" placeholder="${namePh}" path="name" cssClass="form-control"/>
                    </div>
                    <div class="form-group">
                        <form:errors path="category" cssClass="alert alert-warning" element="p"/>
                        <spring:message code="createActivity.categoryPh" var="categoryPh"/>
                        <form:input type="text" placeholder="${categoryPh}" path="category" name="category-input"
                                    cssClass="form-control"/>
                    </div>
                    <form:errors path="placeInput" cssClass="alert alert-warning" element="p"/>
                    <c:if test="${errorMap}">
                        <div class="alert alert-danger" role="alert">
                            <spring:message code="createActivity.invalidPlace"/>
                        </div>
                    </c:if>
                    <div class="pac-card" id="pac-card">
                        <div id="pac-container">
                            <spring:message code="createActivity.locationPh" var="locationPh"/>
                            <form:input id="pac-input" type="text" path="placeInput" placeholder="${locationPh}"/>
                        </div>
                    </div>
                    <div class="container" id="map"></div>
                    <div id="infowindow-content">
                        <img src="" width="16" height="16" id="place-icon">
                        <span id="place-name" class="title"></span><br>
                        <span id="place-address"></span>
                    </div>
                    <div class="text-center flex-fill">
                        <button type="submit" class="btn-success btn btn-lg">
                            <spring:message code="createActivity.createBtn"/>
                        </button>
                    </div>
                </form:form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
