<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" %>

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
    <title>${trip.name}</title>
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="container-fluid">
    <div class="container">
                <div>
                    <h1 class="display-4" style="margin-top: 30px;">${trip.name}</h1>
                    <p>${trip.description}</p>
                    <p>[${startDate} - ${endDate}]</p>
                </div>
                <c:if test="${hasTripPicture}">
                    <img class="img-fluid" src="${tripImageURL}" width="205" height="215" style="margin: 20px">
                </c:if>
                <c:if test="${isAdmin}">
                    <c:if test="${fileSizeError}">
                        <p class=" alert alert-warning">File size too big</p>
                    </c:if>
                    <c:if test="${invalidContentError}">
                        <p class=" alert alert-warning">Invalid content. File must be jpg or png format</p>
                    </c:if>
                    <c:if test="${generalError}">
                        <p class=" alert alert-warning">Error uploading file</p>
                    </c:if>
                    <form:form action="${editTripURL}" method="post" modelAttribute="editTripForm" enctype="multipart/form-data" >
                        <form:errors path="imageUpload" cssClass = "alert alert-warning" element="p"/>
                        <form:input type="file" path="imageUpload" accept = "image/*"/>
                        <br>
                        <button type="submit" class="btn btn-success" style="margin-top: 10px;">Add image</button>
                    </form:form>
                </c:if>
                <h3 class="margin-class">Places</h3>
                <ul class="list-group">
                    <c:forEach items="${places}" var="place">
                        <li class="list-group-item">${place.name}</li>
                    </c:forEach>
                </ul>
                <h3 class="margin-class">Members</h3>
                <div class="list-group">
                    <c:forEach items="${usersAndRoles}" var="ur">
                        <a href="<c:url value='/home/profile/${ur.key.id}'/>"
                           class="list-group-item list-group-item-action flex-column align-items-start">
                            <div class="d-flex w-100 justify-content-between">
                                <h5 class="mb-1">${ur.key.firstname} ${ur.key.lastname}</h5>
                            </div>
                            <c:if test="${ur.value != 'MEMBER'}">
                                <small id="adminTag">${ur.value}</small>
                            </c:if>
                        </a>
                    </c:forEach>
                </div>
                <h3 class="margin-class">Activities</h3>
                <c:if test="${isEmpty}">
                    <div class="alert alert-primary" role="alert">This trip has no activities yet</div>
                </c:if>
                <div class="list-group" style="margin-bottom: 20px">
                    <c:forEach items="${actAndPlaces}" var="activity_places">
                        <button type="button"
                                class="list-group-item list-group-item-action flex-column align-items-start"
                                onclick="initMap(${activity_places.key.id},
                                    ${activity_places.value.latitude},${activity_places.value.longitude})">
                            <div class="d-flex w-100 justify-content-between">
                                <h5 class="mb-1">${activity_places.key.name}</h5>
                            </div>
                            <small>${activity_places.key.category}</small>
                            <p class="mb-1">${activity_places.value.address}</p>
                        </button>
                        <div id="map${activity_places.key.id}" style="height: 400px;width: 97%;display: none;"></div>
                    </c:forEach>
                </div>
                <c:if test="${isAdmin}">
                    <a class="btn btn-success btn-lg" style="margin-top: 15px;" href="${createActivityURL}"
                       role="button">Add activity</a>
                </c:if>
                <c:if test="${!isTravelling}">
                    <a class="btn btn-success" style="margin-top: 20px;" href="${joinTripURL}" role="button">Join
                        trip</a>
                </c:if>
    </div>
</div>
</body>
<script src="${showMapJs}"></script>
<script async defer
        src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDf5BlyQV8TN06oWY_U7Z_MnqWjIci2k2M">
</script>
</html>