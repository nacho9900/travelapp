<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <c:url value="webjars/bootstrap/4.1.3/css/bootstrap.min.css" var="bs-css"/>
    <c:url value="/webjars/jquery/3.2.1/jquery.min.js" var="jquery" />
    <c:url value="/webjars/bootstrap/4.1.3/js/bootstrap.min.js" var="bs-js"/>
    <c:url value="/resources/icons/globe.ico" var="iconURL"/>
    <link rel="shortcut icon" href="${iconURL}" type="image/x-icon"/>
    <link href="${bs-css}" rel="stylesheet">
    <title>My Trips</title>
</head>
<body>
    <h3>My Trips</h3>
    <p>Hello ${user.firstname}, this are your trips</p>
    <c:forEach items="${userTrips}" var="trip">
        <a href="#">${trip.name}</a> <br>
        <p>${trip.description}</p> <br>
    </c:forEach>
    <script src="${jquery}" type="text/javascript"></script>
    <script src="${bs-js}" type="text/javascript"></script>
</body>
</html>
