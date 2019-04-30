<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <c:url value="/resources/icons/globe.ico" var="iconURL"/>
    <c:url value="/home/trips" var="home-trips"/>
    <c:url value="/home/create-trip" var="home-create-trip"/>
    <link rel="shortcut icon" href="${iconURL}" type="image/x-icon"/>
	<title>Home Page</title>
</head>
<body>
    <h3>Welcome ${user.firstname} ${user.lastname}! </h3>
    <a class="btn btn-primary" href="${home-create-trip}">Create Trip</a>
    <a class="btn btn-primary" href="${home-trips}">My trips</a>
</body>
</html>