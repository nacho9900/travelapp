<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <%@include file="head.jsp" %>
    <c:url value="/resources/icons/defaultPP.png" var="defaultPP"/>
    <c:url value="/home/profile/${user.id}/edit" var="editProfile"/>
    <link href="${bootstrapCss}" rel="stylesheet">
    <link rel="shortcut icon" href="${iconURL}" type="image/x-icon"/>
    <title><spring:message code="profile.title" arguments="${userProfile.firstname}"/></title>
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="container-fluid" id="main-container">
    <div class="container text-center">
        <div class="row">
            <div class="col-12">
                <div class="card car mx-auto" style="width: 18rem;">
                    <c:choose>
                        <c:when test="${hasProfilePicture}">
                            <c:url value="/home/profile/${userProfile.id}/image" var="profilePictureURL"/>
                            <img class="card-img-top" src="${profilePictureURL}">
                        </c:when>
                        <c:otherwise>
                            <img class="card-img-top" src="${defaultPP}">
                        </c:otherwise>
                    </c:choose>
                    <div class="card-header">
                        <h5 class="card-title"><c:out value="${userProfile.firstname}"/> <c:out
                                value="${userProfile.lastname}"/> <c:out value="${userProfile.nationality}"/></h5>
                    </div>
                    <div class="card-body">
                        <p class="card-text"><c:out value="${userProfile.biography}"/></p>
                        <p class="card-text"><c:out value="${userProfile.email}"/></p>
                        <p class="card-text"><c:out value="${birthday}"/></p>
                        <c:if test="${user.id == userProfile.id}">
                            <div class="card-footer">
                                <a href="${editProfile}" class="btn btn-primary btn-success">
                                    <spring:message code="profile.editProfileBtn"/>
                                </a>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>