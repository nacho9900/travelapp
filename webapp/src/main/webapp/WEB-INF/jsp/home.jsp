<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="head.jsp"%>
    <title>Home</title>
</head>
<body>
    <%@include file="header.jsp"%>
    <div class="container-fluid" id="home-main-container">
        <div class="container">
            <h3 class="display-4" >Welcome ${user.firstname} ${user.lastname}! </h3>
            <div class="list-group">
                <c:forEach items = "${tripList}" var="trip">
                    <div style="margin: 20px">
                        <p class="list-group-item">${trip.name}</p>
                        <p class="list-group-item">${trip.description}</p>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</body>
</html>