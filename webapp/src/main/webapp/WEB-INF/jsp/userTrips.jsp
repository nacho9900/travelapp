<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <%@include file="head.jsp"%>
    <title><spring:message code="trips.title"/></title>
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="container-fluid" id="main-container">
    <div class="container box">
        <h3 class="display-4 context-menu text-white"><spring:message code="trips.pageTitle"/></h3>
        <c:choose>
            <c:when test="${isEmpty}">
                <p class="alert alert-warning">
                    <spring:message code="trips.noTrips"/>
                </p>
            </c:when>
            <c:otherwise>
                <div class="list-group">
                    <c:forEach items="${userTripsList}" var="dataPair">
                        <c:url value="/home/trip/${dataPair.key.id}" var = "tripUrl"/>
                        <a href="${tripUrl}" class="list-group-item list-group-item-action flex-column align-items-start">
                            <div class="d-flex w-100 justify-content-between">
                                <h5 class="mb-1"><c:out value="${dataPair.key.name}"/></h5>
                                <small>${dataPair.key.startDate.format(formatter)}</small>
                            </div>
                            <c:if test="${dataPair.value.value}">
                                <c:url value="/home/trip/${dataPair.key.id}/image" var="tripImageURL"/>
                                <img class="img-fluid" src="${tripImageURL}" width="205" height="215" >
                            </c:if>
                            <p class="mb-1"><c:out value="${dataPair.key.description}"/></p>
                            <small><c:out value="${dataPair.value.key.address}"/></small>
                        </a>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>

    </div>
    <div class="d-flex justify-content-center align-bottom">
        <ul class="pagination">
            <c:forEach var = "i" begin = "1" end = "${pageQty}">
                <c:url value="/home/trips/${i}" var="tripsPageUrl"/>
                <li class="page-item"><a class="page-link" href="${tripsPageUrl}">${i}</a></li>
            </c:forEach>
        </ul>
    </div>
</div>
</body>
</html>
