<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<html>
<head>
    <%@include file="head.jsp" %>
    <c:url value="/resources/icons/defaultPP.png" var="defaultPP"/>
    <c:url value="/home/profile/${user.id}/edit" var="editProfilePicture"/>
    <c:url value="/home/profile/${user.id}/editBio" var="editBiography"/>
    <link href="${bootstrapCss}" rel="stylesheet">
    <link rel="shortcut icon" href="${iconURL}" type="image/x-icon"/>
    <spring:message code="editP.title" arguments="${user.firstname}" var="title"/>
    <title><c:out value="${title}"/></title>
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="container-fluid text-white" id="main-container">
    <div class="container box">
        <h3 class="display-4" style="margin-top: 20px;">
            <spring:message code="editP.pageTitle"/>
        </h3>
        <br>

        <%-- BIO FORM --%>
        <form:form action="${editBiography}" method="post" modelAttribute="UserUpdateBioForm" enctype="multipart/form-data">
            <br>
            <spring:message code="editP.biographyPlaceholder" var="bPlaceholder"/>
            <form:errors path="biography" cssClass="alert alert-warning" element="p"/>
            <form:textarea rows="10" cols="100" placeholder="${bPlaceholder}" path="biography"
                           maxlength="500" cssClass="form-control"/>
            <br>

            <spring:message code="editP.editBio" var="inputValue"/>
            <input type="submit" value="${inputValue}" class="btn btn-success">
        </form:form>

        <%-- PICTURE FORM --%>
        <form:form action="${editProfilePicture}" method="post" modelAttribute="editProfileForm" enctype="multipart/form-data">
            <p>
                <spring:message code="editP.uploadProfilePicture"/>
            </p>
            <form:errors path="imageUpload" cssClass="alert alert-warning" element="p"/>
            <form:input type="file" path="imageUpload" accept="image/*" />
            <br>

            <button type="submit" class="btn btn-success text-center justify-content-center">
                <spring:message code="editP.applyChangesBtn"/>
            </button>
        </form:form>
        <c:if test="${fileSizeError}">
            <p class=" alert alert-warning">
                <spring:message code="editP.sizeTooBig"/>
            </p>
        </c:if>
        <c:if test="${invalidContentError}">
            <p class=" alert alert-warning">
                <spring:message code="editP.invalidContent"/>
            </p>
        </c:if>
        <c:if test="${generalError}">
            <p class=" alert alert-warning">
                <spring:message code="editP.errorUploading"/>
            </p>
        </c:if>
    </div>
</div>
</body>
</html>