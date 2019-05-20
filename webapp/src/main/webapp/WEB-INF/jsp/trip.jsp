<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <%@include file="head.jsp" %>
    <c:url value="/home/trip/${trip.id}/create-activity" var="createActivityURL"/>
    <c:url value="/home/trip/${trip.id}/join" var="joinTripURL"/>
    <c:url value="/resources/css/trip.css" var="tripCSS"/>
    <c:url value="/resources/js/showMap.js" var="showMapJs"/>
    <c:url value="/home/trip/${trip.id}" var="editTripURL"/>
    <c:url value="/home/trip/${trip.id}/image" var="tripImageURL"/>
    <link rel="shortcut icon" href="${iconURL}" type="image/x-icon"/>
    <link href="${bootstrapCss}" rel="stylesheet">
    <link href="${tripCSS}" rel="stylesheet">
    <title><c:out value="${trip.name}"/></title>
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="container-fluid">
    <div class="container">
        <div>
            <h1 class="display-4" style="margin-top: 30px;"><c:out value="${trip.name}"/></h1>
            <p><c:out value="${trip.description}"/></p>
            <p>[${startDate} - ${endDate}]</p>
        </div>
        <c:if test="${hasTripPicture}">
            <img class="img-fluid" src="${tripImageURL}" width="205" height="215" style="margin: 20px">
        </c:if>
        <c:if test="${isAdmin}">
            <c:if test="${fileSizeError}">
                <p class=" alert alert-warning">
                    <spring:message code="trip.sizeError"/>
                </p>
            </c:if>
            <c:if test="${invalidContentError}">
                <p class=" alert alert-warning">
                    <spring:message code="trip.contentError"/>
                </p>
            </c:if>
            <c:if test="${generalError}">
                <p class=" alert alert-warning">
                    <spring:message code="trip.generalError"/>
                </p>
            </c:if>
            <form:form action="${editTripURL}" method="post" modelAttribute="editTripForm"
                       enctype="multipart/form-data">
                <form:errors path="imageUpload" cssClass="alert alert-warning" element="p"/>
                <form:input type="file" path="imageUpload" accept="image/*"/>
                <br>
                <button type="submit" class="btn btn-success" style="margin-top: 10px;">
                    <spring:message code="trip.addImgBtn"/>
                </button>
            </form:form>
        </c:if>
        <h3 class="margin-class"><spring:message code="trip.places"/></h3>
        <ul class="list-group">
            <c:forEach items="${places}" var="place">
                <li class="list-group-item"><c:out value="${place.name}"/></li>
            </c:forEach>
        </ul>
        <h3 class="margin-class">
            <spring:message code="trip.members"/>
        </h3>
        <div class="list-group">
            <c:forEach items="${usersAndRoles}" var="ur">
                <a href="<c:url value='/home/profile/${ur.key.id}'/>"
                   class="list-group-item list-group-item-action flex-column align-items-start">
                    <div class="d-flex w-100 justify-content-between">
                        <h5 class="mb-1">
                            <c:out value="${ur.key.firstname}"/> <c:out value="${ur.key.lastname}"/>
                        </h5>
                    </div>
                    <c:if test="${ur.value != 'MEMBER'}">
                        <small id="adminTag"><c:out value="${ur.value}"/></small>
                    </c:if>
                </a>
            </c:forEach>
        </div>
        <h3 class="margin-class">
            <spring:message code="trip.activities"/>
        </h3>
        <c:if test="${isEmpty}">
            <div class="alert alert-primary" role="alert">
                <spring:message code="trip.noActivities"/>
            </div>
        </c:if>
        <div class="list-group" style="margin-bottom: 20px">
            <c:forEach items="${actAndPlaces}" var="activity_places">
                <button type="button"
                        class="list-group-item list-group-item-action flex-column align-items-start"
                        onclick="initMap(${activity_places.key.id},
                            ${activity_places.value.latitude},${activity_places.value.longitude})">
                    <div class="d-flex w-100 justify-content-between">
                        <h5 class="mb-1"><c:out value="${activity_places.key.name}"/></h5>
                    </div>
                    <small><c:out value="${activity_places.key.category}"/></small>
                    <p class="mb-1"><c:out value="${activity_places.value.address}"/></p>
                </button>
                <div id="map${activity_places.key.id}" style="height: 400px;width: 97%;display: none;"></div>
            </c:forEach>
        </div>
        <c:if test="${isAdmin}">
            <a class="btn btn-success btn-lg" style="margin-top: 15px;" href="${createActivityURL}"
               role="button">
                <spring:message code="trip.addActivityBtn"/>
            </a>
        </c:if>
        <c:if test="${!isTravelling}">
            <a class="btn btn-success" style="margin-top: 20px;" href="${joinTripURL}" role="button">
               <spring:message code="trip.joinTripBtn"/>
            </a>
        </c:if>
    </div>
</div>
</body>
<script src="${showMapJs}"></script>
<script async defer
        src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDf5BlyQV8TN06oWY_U7Z_MnqWjIci2k2M">
</script>
</html>