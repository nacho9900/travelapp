<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <%@include file="head.jsp"%>
    <title><spring:message code="home.title"/></title>
</head>
<body>
    <%@include file="header.jsp"%>
    <div class="container-fluid" id="main-container">
        <div class="container">
            <spring:message code="home.greeting" arguments="${user.firstname}" var="welcome"/>
            <h3 class="text-center" > <c:out value="${welcome}"/></h3>
            <div class="row">
                <div class="col-2"></div>
                <div class="col-8">
                    <div class="container">
                        <div class="row">
                            <div class="col-12">
                                <c:forEach items = "${tripList}" var="trip">
                                    <div class="card" style="margin-top: 10px; margin-bottom: 10px;">
                                        <div class="card-body">
                                            <h4 class="card-title"><c:out value="${trip.name}"/></h4>
                                            <p class="card-text"><c:out value="${trip.description}"/></p>
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