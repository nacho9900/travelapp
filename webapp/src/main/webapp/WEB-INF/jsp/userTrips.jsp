<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <link rel="shortcut icon" href="<c:url value="/resources/icons/globe.ico"/>" type="image/x-icon"/>
    <link href="webjars/bootstrap/4.1.3/css/bootstrap.min.css" rel="stylesheet">
    <title>My Trips</title>
</head>
<body>
    <h3>My Trips</h3>
    <p>Hello ${user.firstname}, this are your trips</p>
    <c:forEach items="${userTrips}" var="trip">
        <a href="#">${trip.name}</a> <br>
        <p>${trip.description}</p> <br>
    </c:forEach>
    <script src="webjars/jquery/3.2.1/jquery.min.js"></script>
    <script src="webjars/bootstrap/4.1.3/js/bootstrap.min.js"></script>
</body>
</html>
