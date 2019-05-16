<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
    <c:url value="/webjars/bootstrap/4.1.3/css/bootstrap.min.css" var="bootcss"/>
    <c:url value="/webjars/jquery/3.2.1/jquery.min.js" var="jquery" />
    <c:url value="/webjars/bootstrap/4.1.3/js/bootstrap.min.js" var="bootjs"/>
    <c:url value="/resources/icons/globe.ico" var="iconURL"/>
    <c:url value="/home" var="home"/>
    <c:url value="/about" var="about"/>
    <c:url value="/home/trips" var="trips"/>
    <c:url value="/" var="index"/>
    <c:url value="/home/create-trip" var="createTrip"/>
    <c:url value="/resources/icons/earth-globe.png" var="globeIMG"/>
    <c:url value="/resources/icons/uu.png" var="userIMG"/>
    <c:url value="/signin" var="signinURL"/>
    <c:url value="/home/profile/${user.id}" var="profile"/>
    <c:url value="/home/trip/${trip.id}/create-activity" var="createActivityURL"/>
    <c:url value="/home/trip/${trip.id}/join" var="joinTripURL"/>
    <c:url value="/resources/css/trip.css" var="tripCSS"/>
    <link rel="shortcut icon" href="${iconURL}" type="image/x-icon"/>
    <link href="${bootcss}" rel="stylesheet">
    <link href="${tripCSS}" rel="stylesheet">
    <title>${trip.name}</title>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <a class="navbar-brand" href="${index}">
        <img src="${globeIMG}" height="42" width="42" alt=""/>
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
        <c:choose>
            <c:when test="${user == null}">
                <a id="signinButton" class="btn btn-success my-2 my-sm-0" href="${signinURL}">Sign in</a>
            </c:when>
            <c:otherwise>
                <a href="${profile}">
                    <img alt="" style="margin-left: 10px" src="${userIMG}" height="32" width="32"/>
                </a>
            </c:otherwise>
        </c:choose>
    </div>
</nav>
<div class="container" style="margin-top: 30px">
    <div>
        <h1 class="display-4">${trip.name}</h1>
        <p>${trip.description}</p>
        <p>[${startDate} - ${endDate}]</p>
    </div>
    <h3 class="marginClass">Places</h3>
    <div class="list-group">
    <c:forEach items="${places}" var="place">
        <button type="button" class="list-group-item list-group-item-action">${place.address}</button>
    </c:forEach>
    </div>
    <h3 class="marginClass">Members</h3>
    <div class="list-group">
        <c:forEach items="${usersAndRoles}" var="ur">
            <a href="<c:url value='/home/profile/${ur.key.id}'/>" class="list-group-item list-group-item-action flex-column align-items-start">
                <div class="d-flex w-100 justify-content-between">
                    <h5 class="mb-1">${ur.key.firstname} ${ur.key.lastname}</h5>
                </div>
                <small>${ur.value}</small>
            </a>
        </c:forEach>
    </div>

    <%--TODO: SHOW PLACE ACTIVITY IN MAP WHEN CLICKED--%>
    <h3 class="marginClass">Activities</h3>
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
<script src="${jquery}" type="text/javascript"></script>
<script src="${bootjs}" type="text/javascript"></script>
</body>
</html>