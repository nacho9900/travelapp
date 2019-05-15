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
    <c:url value="/resources/css/userTrips.css" var="customCSS"/>
    <c:url value="/resources/icons/earth-globe.png" var="globeIMG"/>
    <c:url value="/resources/icons/uu.png" var="userIMG"/>
    <c:url value="/signin" var="signinURL"/>
    <c:url value="/home/profile" var="profile"/>
    <link rel="shortcut icon" href="${iconURL}" type="image/x-icon"/>
    <link href="${bootcss}" rel="stylesheet">
    <link href="${customCSS}" rel="stylesheet">
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

<div class="container">
    <div>
        <h1 class="display-4">${trip.name}</h1>
        <p>${trip.description}</p>
        <p>[${startDate} - ${endDate}]</p>
    </div>
    <h3>Places</h3>
    <div class="list-group">
    <c:forEach items="${places}" var="place">
        <button type="button" class="list-group-item list-group-item-action">${place.address}</button>
    </c:forEach>
    </div>
    <h3>Members</h3>
    <div class="list-group">
        <c:forEach items="${usersAndRoles}" var="ur">
            <button type="button" class="list-group-item list-group-item-action">${ur.key.firstname} ${ur.key.lastname}
                    ${ur.value}</button>
        </c:forEach>
    </div>
    <h3>Activities</h3>
    <ul class="list-group">
        <c:forEach items="${actAndPlaces}" var="activity_places">
            <li class="list-group-item">${activity_places.key.name} ${activity_places.key.category} ${activity_places.value.address}</li>
        </c:forEach>
    </ul>
    <a class="btn btn-primary btn-lg" href="/home/trip/${trip.id}/create-activity" role="button">Add activity</a>
</div>

<script src="${jquery}" type="text/javascript"></script>
<script src="${bootjs}" type="text/javascript"></script>
</body>
</html>