<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <%@include file="head.jsp" %>
    <c:url value="/home/create-trip" var="createTripURL"/>
    <c:url value="/resources/css/map.css" var="createTripCss"/>
    <c:url value="/resources/js/map.js" var="createTripJs"/>
    <c:url value="/resources/js/preventEnterDefault.js" var="noEnterJs"/>
    <link rel="stylesheet" href="${createTripCss}">

    <title>
        <spring:message code="createTrip.title"/>
    </title>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <meta charset="utf-8">
</head>
<body>
<jsp:include page="header.jsp"/>

<div class="container-fluid" id="main-container">
    <div class="container box">
        <div class="row">
            <div class="col-12">
                <%--TODO: AGREGAR ALERT POR ERROR EN MAP INPUT--%>
                <div class="form-group">
                    <div id="headerID" class="display-4 text-white">
                        <spring:message code="createTrip.pageTitle"/>
                    </div>
                </div>
                <form:form action="${createTripURL}" method="post" modelAttribute="createTripForm">
                    <form:errors path="name" cssClass="alert alert-warning" element="p"/>
                    <spring:message code="createTrip.namePh" var="namePh"/>
                    <form:input class="form-control" cssStyle="margin-bottom: 10px" type="text" path="name"
                                name="trip_name"
                                placeholder="${namePh}"/>

                    <form:errors path="description" cssClass="alert alert-warning" element="p"/>
                    <spring:message code="createTrip.descriptionPh" var ="descriptionPh"/>
                    <form:textarea class="form-control" type="text" path="description" name="trip_desc"
                                   placeholder="${descriptionPh}"/>

                    <form:errors path="startDate" cssClass="alert alert-warning" element="p"/>
                    <form:label path="startDate" for="start_date" cssClass="text-white">
                        <spring:message code="createTrip.startDate"/>
                    </form:label>
                    <form:input class="form-control" type="date" path="startDate" name="start_date"/>

                    <form:errors path="endDate" cssClass="alert alert-warning" element="p"/>
                    <form:label path="endDate" for="start_date" cssClass="text-white">
                        <spring:message code="createTrip.endDate"/>
                    </form:label>
                    <form:input class="form-control" type="date" path="endDate" name="end_date"/>
                    <form:errors path="placeInput" cssClass="alert alert-warning" element="p"/>
                    <c:if test="${invalidPlace}">
                        <div class="alert alert-danger" role="alert">
                            <spring:message code="createTrip.invalidQuery"/>
                        </div>
                    </c:if>
                    <div class="pac-card" id="pac-card">
                        <div id="pac-container">
                            <spring:message code="createTrip.locationPh" var ="locationPh"/>
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
                            <spring:message code="createTrip.createBtn"/>
                        </button>
                    </div>
                </form:form>
            </div>
        </div>
    </div>
</div>
<script>

</script>
<script src="${noEnterJs}"></script>
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDf5BlyQV8TN06oWY_U7Z_MnqWjIci2k2M&libraries=places&callback=initMap"
        async defer></script>
<script src="${createTripJs}"></script>
</body>
</html>
