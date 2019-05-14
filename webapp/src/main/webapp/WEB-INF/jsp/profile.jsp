<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <c:url value="/webjars/bootstrap/4.1.3/css/bootstrap.min.css" var="bootcss"/>
    <c:url value="/webjars/jquery/3.2.1/jquery.min.js" var="jquery" />
    <c:url value="/webjars/bootstrap/4.1.3/js/bootstrap.min.js" var="bootjs"/>
    <c:url value="/resources/icons/globe.ico" var="iconURL"/>
    <c:url value="/" var="index"/>
    <c:url value="/home/create-trip" var="createTrip"/>
    <c:url value="/home/trips" var="trips"/>
    <c:url value="/home" var="home"/>
    <c:url value="/resources/css/index.css" var="indexcss"/>
    <c:url value="/resources/icons/earth-globe.png" var="globeIMG"/>
    <link href="${bootcss}" rel="stylesheet">
    <link href="${indexcss}" rel="stylesheet">
    <link rel="shortcut icon" href="${iconURL}" type="image/x-icon"/>
    <title>Meet and Travel</title>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <a class="navbar-brand" href="${index}">
            <img src="${globeIMG}" height="42" width="42"/>
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
                    <a class="nav-link" href="${trips}">My Trips</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${createTrip}">Create Trip</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#">About us</a>
                </li>
            </ul>
            <form class="form-inline my-2 my-lg-0">
                <input class="form-control mr-sm-2" type="search" placeholder="Search for trips" aria-label="Search">
                <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>
            </form>
        </div>
    </nav>
    <h1 class="display-4">Profile</h1>
    <p>${user.firstname}</p>
    <p>${user.lastname}</p>
    <p>${user.email}</p>
    <p>Bio:</p>
    <script src="${bootjs}" type="text/javascript"></script>
<script src="${jquery}" type="text/javascript"></script>
</body>
</html>