<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <%@include file="head.jsp" %>
    <title><spring:message code="home.title"/></title>
</head>
<body>
<%@include file="header.jsp" %>
<div class="container-fluid" id="main-container">
    <div class="container">
        <spring:message code="home.greeting" arguments="${user.firstname}" var="welcome"/>
        <h3 class="text-center"><c:out value="${welcome}"/></h3>
        <div class="list-group">
            <c:forEach items = "${tripList}" var="trip">
                <c:url value="/home/trip/${trip.key.id}" var = "tripUrl"/>
                <a href="${tripUrl}" class="list-group-item list-group-item-action flex-column align-items-start">
                    <div class="d-flex w-100 justify-content-between">
                        <h5 class="mb-1"><c:out value="${trip.key.name}"/></h5>
                        <small>${trip.key.startDate.format(dateFormat)}</small>
                    </div>
                    <c:if test="${trip.value}">
                        <c:url value="/home/trip/${trip.key.id}/image" var="tripImageURL"/>
                        <img class="img-fluid" src="${tripImageURL}" width="205" height="215" >
                    </c:if>
                    <p class="mb-1"><c:out value="${trip.key.description}"/></p>
                </a>
            </c:forEach>
        </div>
    </div>
</div>
</body>
</html>