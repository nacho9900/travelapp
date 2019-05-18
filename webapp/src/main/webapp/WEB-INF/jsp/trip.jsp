<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>

    <%@include file="head.jsp" %>

    <c:url value="/home/trip/${trip.id}/create-activity" var="createActivityURL"/>
    <c:url value="/home/trip/${trip.id}/join" var="joinTripURL"/>
    <c:url value="/resources/css/trip.css" var="tripCSS"/>
    <link rel="shortcut icon" href="${iconURL}" type="image/x-icon"/>
    <link href="${bootstrapCss}" rel="stylesheet">
    <link href="${tripCSS}" rel="stylesheet">
    <title>${trip.name}</title>
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="container-fluid" id="main-container">
    <div class="container">
<div class="row">
    <div class="col-2"></div>
    <div class="col-8 box">
        <div>
            <h1 class="display-4 text-white">${trip.name}</h1>
            <p class="text-white">${trip.description}</p>
            <p>[${startDate} - ${endDate}]</p>
        </div>
        <h3 class="margin-class text-white">Places</h3>
        <div class="list-group">
            <c:forEach items="${places}" var="place">
                <button type="button" class="list-group-item list-group-item-action">${place.address}</button>
            </c:forEach>
        </div>
        <h3 class="margin-class text-white">Members</h3>
        <div class="list-group">
            <c:forEach items="${usersAndRoles}" var="ur">
                <a href="<c:url value='/home/profile/${ur.key.id}'/>"
                   class="list-group-item list-group-item-action flex-column align-items-start">
                    <div class="d-flex w-100 justify-content-between">
                        <h5 class="mb-1">${ur.key.firstname} ${ur.key.lastname}</h5>
                    </div>
                    <small>${ur.value}</small>
                </a>
            </c:forEach>
        </div>

        <%--TODO: SHOW PLACE ACTIVITY IN MAP WHEN CLICKED--%>
        <h3 class="margin-class text-white">Activities</h3>
        <c:if test="${actAndPlaces.size() == 0}">
            <div class="alert alert-primary" role="alert">This trip has no activities yet</div>
        </c:if>
        <ul class="list-group">
            <c:forEach items="${actAndPlaces}" var="activity_places">
                <li class="list-group-item">${activity_places.key.name} ${activity_places.key.category} ${activity_places.value.address}</li>
            </c:forEach>
        </ul>
        <c:if test="${isAdmin}">
            <a class="btn btn-primary btn-lg" href="${createActivityURL}" role="button">Add activity</a>
        </c:if>
        <c:if test="${!isTravelling}">
            <a class="btn btn-success" href="${joinTripURL}" role="button">Join trip</a>
        </c:if>
    </div>

    </div>
</div>








</div>

</body>
</html>