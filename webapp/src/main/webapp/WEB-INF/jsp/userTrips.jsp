<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <%@include file="head.jsp"%>
    <title>My Trips</title>
</head>
<body>
<jsp:include page="header.jsp"/>
    <%--TODO: change method call to model object--%>
    <h3 id="header" class="display-4 context-menu">My Trips</h3>
    <c:choose>
        <c:when test="${userTripsList.size() == 0}">
            <p class="alert alert-warning">You are not participating in any trips, try joining one!</p>
        </c:when>
        <c:otherwise>
            <div class="list-group">
                <c:forEach items="${userTripsList}" var="dataPair">
                    <c:url value="/home/trip/${dataPair.key.id}" var="tripUrl"/>
                    <a href="${tripUrl}" class="list-group-item list-group-item-action flex-column align-items-start">
                        <div class="d-flex w-100 justify-content-between">
                            <h5 class="mb-1">${dataPair.key.name}</h5>
                            <small>${dateFormat.format(dataPair.key.startDate.getTime())}</small>
                        </div>
                        <p class="mb-1">${dataPair.key.description}</p>
                        <small>${dataPair.value.address}</small>
                    </a>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</body>
</html>
