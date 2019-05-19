<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <%@include file="head.jsp"%>
    <c:url value="/resources/icons/defaultPP.png" var="defaultPP"/>
    <c:url value="/home/profile/${user.id}/edit" var="editProfile"/>
    <link href="${bootstrapCss}" rel="stylesheet">
    <link rel="shortcut icon" href="${iconURL}" type="image/x-icon"/>
    <title>${userProfile.firstname}´s profile</title>
</head>
<body>
    <jsp:include page="header.jsp"/>
    <div class="container text-center">
        <div class= "jumbotron jumbotron-fluid">
            <c:choose>
                <c:when test="${hasProfilePicture}">
                    <c:url value="/home/profile/${userProfile.id}/image" var="profilePictureURL"/>
                    <img class="img-fluid" src="${profilePictureURL}" width="410" height="430" style="margin: 20px">
                </c:when>
                <c:otherwise>
                    <img class="img-fluid" src="${defaultPP}" width="410" height="430" style="margin: 20px">
                </c:otherwise>
            </c:choose>
            <div class="card car mx-auto" style="width: 18rem;">
                <div class="card-header">
                    <h5 class="card-title">${userProfile.firstname} ${userProfile.lastname}    ${userProfile.nationality}</h5>
                </div>
                <div class="card-body">
                    <p class="card-text">${userProfile.email}</p>
                    <p class="card-text">${birthday}</p>
                    <p class="card-text">BIOGRAPHY</p>
                    <c:if test="${user.id == userProfile.id}">
                        <div class="card-footer">
                            <a href="${editProfile}" class="btn btn-primary btn-success">Edit profile</a>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</body>
</html>