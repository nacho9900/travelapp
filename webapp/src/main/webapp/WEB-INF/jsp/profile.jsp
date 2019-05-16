<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <%@include file="head.jsp"%>
    <c:url value="/resources/icons/defaultPP.png" var="defaultPP"/>
    <link href="${bootstrapCss}" rel="stylesheet">
    <link rel="shortcut icon" href="${iconURL}" type="image/x-icon"/>
    <title>${userProfile.firstname}´s profile</title>
</head>
<body>
    <jsp:include page="header.jsp"/>
    <div class="container text-center">
        <div class= "jumbotron jumbotron-fluid">
            <%--TODO PROFILE PIC--%>
            <img class="img-fluid" src="${defaultPP}" width="410" height="430" style="margin: 20px">
            <div class="card car mx-auto" style="width: 18rem;">
                <div class="card-header">
                    <h5 class="card-title">${userProfile.firstname} ${userProfile.lastname}    ${userProfile.nationality}</h5>
                </div>
                <div class="card-body">
                    <p class="card-text">${userProfile.email}</p>
                    <p class="card-text">${birthday}</p>
                    <p class="card-text"><%--TODO BIO--%></p>
                    <%--TODO EDIT PROFILE--%>
                    <c:if test="${user.id == userProfile.id}">
                        <div class="card-footer">
                            <a href="#" class="btn btn-primary btn-success">Edit profile</a>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</body>
</html>