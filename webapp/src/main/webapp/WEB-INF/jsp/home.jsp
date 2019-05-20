<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="head.jsp"%>
    <title>Home</title>
</head>
<body>
    <%@include file="header.jsp"%>
    <div class="container-fluid" id="main-container">
        <div class="container">
            <h3 class="text-center" >Welcome back ${user.firstname}, this are some trips you might like </h3>
            <div class="row">
                <div class="col-2"></div>
                <div class="col-8">
                    <div class="container">
                        <div class="row">
                            <div class="col-12">
                                <c:forEach items = "${tripList}" var="trip">
                                    <div class="card" style="margin-top: 10px; margin-bottom: 10px;">
                                        <div class="card-body">
                                            <h4 class="card-title">${trip.name}</h4>
                                            <p class="card-text">${trip.description}</p>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>