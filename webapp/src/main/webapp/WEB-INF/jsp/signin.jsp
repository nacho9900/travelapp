<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>
    <%@include file="head.jsp" %>
    <c:url value="/resources/css/signin.css" var="signinCss"/>
    <c:url value="/signin" var="signinURL"/>
    <c:url value="/signup" var="signupURL"/>
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.3.1/css/all.css"
          integrity="sha384-mzrmE5qonljUremFsqc01SB46JvROS7bZs3IO2EmfFsd15uHvIt+Y8vEf7N7fWAU" crossorigin="anonymous">
    <link rel="stylesheet" href="${signinCss}">
    <title><spring:message code="signIn.title"/></title>
</head>
<body>
<jsp:include page="headerEmpty.jsp"/>
<div class="container">
    <div class="d-flex justify-content-center h-100">
        <div class="card">
            <div class="card-header">
                <h3><spring:message code="signIn.pageTitle"/></h3>
            </div>
            <div class="card-body">
                <form method="post" action="${signinURL}" enctype="application/x-www-form-urlencoded">
                    <div class="input-group form-group">
                        <div class="input-group-prepend">
                            <span class="input-group-text"><i class="fas fa-user"></i></span>
                        </div>
                        <spring:message code="signIn.usernamePh" var="usernamePh"/>
                        <input type="text" class="form-control" placeholder="${usernamePh}" name="username">

                    </div>
                    <div class="input-group form-group">
                        <div class="input-group-prepend">
                            <span class="input-group-text"><i class="fas fa-key"></i></span>
                        </div>
                        <spring:message code="signIn.passwordPh" var="passwordPh"/>
                        <input type="password" class="form-control" placeholder="${passwordPh}" name="password">
                    </div>
                    <div class="row align-items-center remember">
                        <input type="checkbox" name="rememberme">
                        <spring:message code="signIn.rememberMeCb"/>
                    </div>
                    <br>
                    <div class="form-group">
                        <spring:message code="signIn.logInBtn" var="logInBtn"/>
                        <input type="submit" value="${logInBtn}" class="btn float-left login_btn">
                    </div>
                </form>
            </div>
            <div class="card-footer">
                <div class="d-flex justify-content-center links">
                    <spring:message code="signIn.dontHaveAcc"/>
                    <a class="myClass" href="${signupURL}">
                        <spring:message code="signIn.signUpBtn"/>
                    </a>
                </div>
                <div class="d-flex justify-content-center">
                    <a class="myClass" href="#">
                        <spring:message code="signIn.forgotPasswordBtn"/>
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>