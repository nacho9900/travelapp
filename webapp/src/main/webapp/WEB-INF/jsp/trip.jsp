<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="format" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <%@include file="head.jsp" %>
    <c:url value="/home/trip/${trip.id}/create-activity" var="createActivityURL"/>
    <c:url value="/home/trip/${trip.id}/join" var="joinTripURL"/>
    <c:url value="/home/trip/${trip.id}/exit" var="exitTripURL"/>
    <c:url value="/home/trip/${trip.id}/delete" var="deleteTripURL"/>
    <c:url value="/resources/css/trip.css" var="tripCSS"/>
    <c:url value="/resources/js/showMap.js" var="showMapJs"/>
    <c:url value="/home/trip/${trip.id}/edit" var="editTripURL"/>
    <c:url value="/home/trip/${trip.id}/image" var="tripImageURL"/>
    <c:url value="/home/trip/${trip.id}" var="commentUrl"/>
    <link rel="shortcut icon" href="${iconURL}" type="image/x-icon"/>
    <link href="${bootstrapCss}" rel="stylesheet">
    <link href="${tripCSS}" rel="stylesheet">
    <title><c:out value="${trip.name}"/></title>
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="container-fluid" id="main-container">
    <div class="container box">

                <c:if test="${hasTripPicture}">
                    <div class="img-container" style="background-image: url(${tripImageURL});">

                    </div>
                </c:if>
        <div class="row">
            <div class="col-1"></div>
            <div class="col-10">
                <div>
                    <div class="d-flex justify-content-between">
                        <c:if test="${user.id == admin.id}">
                            <div class="p-2">
                                <a href="${editTripURL}" class="btn btn-primary  align-middle" style="margin-top: 10px;">
                                    <i class="fas fa-edit"></i>
                                </a>
                            </div>
                        </c:if>
                    </div>
                    <div class="display-4 text-white p-2"><c:out value="${trip.name}"/></div>
                    <p class="text-white"><c:out value="${trip.description}"/></p>
                    <p class="text-white">
                        <spring:message code="trip.start"/>
                        <c:out value="${startDate.format(formatter)}"/>
                    </p>
                    <p class="text-white">
                        <spring:message code="trip.end"/>
                        <c:out value="${endDate.format(formatter)}"/>
                    </p>
                </div>

                <h3 class="margin-class text-white"></h3>
                <ul class="list-group">
                    <c:forEach items="${places}" var="place">
                        <li class="list-group-item">
                            <i class="fas fa-map-marker-alt"></i>
                            <c:out value="${place.name}"/></li>
                    </c:forEach>
                </ul>
                <h3 class="margin-class text-white">
                    <spring:message code="trip.members"/>
                </h3>
                <div class="list-group">
                    <a class="list-group-item list-group-item-action flex-column align-items-start"
                       href="<c:url value='/home/profile/${admin.id}'/>">
                        <div class="d-flex w-100 justify-content-between">
                            <h5 class="mb-1"><i class="fas fa-user" style="margin-right: 5px;"></i>
                                <c:out value="${admin.firstname}"/> <c:out value="${admin.lastname}"/></h5>
                            <small><spring:message code="trip.admin"/></small>
                        </div>
                    </a>
                    <c:forEach items="${users}" var="ur">
                        <a class="list-group-item list-group-item-action flex-column align-items-start"
                           href="<c:url value='/home/profile/${ur.id}'/>">
                            <div class="d-flex w-100 justify-content-between">
                                <h5 class="mb-1"><i class="fas fa-user" style="margin-right: 5px;"></i>
                                    <c:out value="${ur.firstname}"/> <c:out value="${ur.lastname}"/></h5>

                            </div>
                        </a>
                    </c:forEach>
                </div>
                <h3 class="margin-class text-white">
                    <spring:message code="trip.activities"/>
                </h3>

                <c:choose>
                    <c:when test="${isEmpty}">
                        <div class="alert alert-primary" role="alert">
                            <spring:message code="trip.noActivities"/>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="list-group" style="margin-bottom: 20px">
                            <c:forEach items="${actAndPlaces}" var="activity_places">
                                <button type="button"
                                        class="list-group-item list-group-item-action flex-column align-items-start"
                                        onclick="initMap(${activity_places.key.id},
                                            ${activity_places.value.latitude},${activity_places.value.longitude})">
                                    <div class="d-flex w-100 justify-content-between">
                                        <h5 class="mb-1"><c:out value="${activity_places.key.name}"/></h5>
                                    </div>
                                    <small><c:out value="${activity_places.key.category}"/></small>
                                    <br>
                                    <small><c:out value="${activity_places.key.startDate.format(formatter)}"/> -
                                        <c:out value="${activity_places.key.endDate.format(formatter)}"/>
                                    </small>
                                    <p class="mb-1"><c:out value="${activity_places.value.address}"/></p>
                                </button>
                                <c:url value="/home/trip/${trip.id}/${activity_places.key.id}/delete" var="deleteActURL"/>
                                <c:if test="${user.id == admin.id}">
                                    <form action="${deleteActURL}" method="post">
                                        <input type="submit" value="<spring:message code="trip.deleteActivity"/>"
                                               class="btn btn-danger" >
                                    </form>
                                </c:if>
                                <div id="map${activity_places.key.id}" style="height: 400px;width: 97%;display: none;"></div>
                            </c:forEach>
                        </div>

                    </c:otherwise>
                </c:choose>

                <c:if test="${user.id == admin.id}">
                    <a class="btn btn-success btn-lg" style="margin-top: 15px;" href="${createActivityURL}"
                       role="button">
                        <spring:message code="trip.addActivityBtn"/>
                    </a>
                </c:if>


                <h3 class="text-white">Comentarios</h3>

                <c:forEach items="${comments}" var="comment" >
                    <div class="form-group text-white">
                        <strong>${comment.user.firstname}:</strong> ${comment.comment}
                    </div>
                </c:forEach>

                <form:form action="${commentUrl}" method="post" modelAttribute="tripCommentForm" >
                    <div class="form-group">
                        <form:textarea path="comment" cssClass="text-area form-control"/>
                        <form:errors path="comment" cssClass="alert alert-warning" element="p"/>
                    </div>
                    <form:button type="submit" class="btn btn-primary">
                        <i class="fas fa-paper-plane"></i>
                    </form:button>
                </form:form>

                <c:if test="${user.id != admin.id}">
                    <c:choose>
                        <c:when test="${!isTravelling}">
                            <a class="btn btn-success" style="margin-top: 20px;" href="${joinTripURL}" role="button">
                                <spring:message code="trip.joinTripBtn"/>
                            </a>
                        </c:when>
                        <c:otherwise>
                            <a class="btn btn-danger" style="margin-top: 20px;" href="${exitTripURL}" role="button">
                                <spring:message code="trip.exitTripBtn"/>
                            </a>
                        </c:otherwise>
                    </c:choose>
                </c:if>
            </div>
        </div>
    </div>
</div>
</body>
<script src="${showMapJs}"></script>
<script async defer
        src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDf5BlyQV8TN06oWY_U7Z_MnqWjIci2k2M">
</script>
</html>