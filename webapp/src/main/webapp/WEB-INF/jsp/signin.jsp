<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <%@include file="head.jsp"%>

    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.3.1/css/all.css" integrity="sha384-mzrmE5qonljUremFsqc01SB46JvROS7bZs3IO2EmfFsd15uHvIt+Y8vEf7N7fWAU" crossorigin="anonymous">

    <c:url value="/signin" var="signinURL"/>
    <c:url value="/signup" var="signUpUrl"/>
    <title>Sign in</title>
</head>
<body>
    <jsp:include page="headerEmpty.jsp"/>
    <div class="container-fluid" id="index-main-container">
<div class="row">
    <div class="col-4"></div>
    <div class="col-4">
        <div class="card">
            <div class="card-header">
                <h3>Sign In</h3>
            </div>
            <div class="card-body">
                <form  method="post" action="${signinURL}" enctype="application/x-www-form-urlencoded">
                    <div class="input-group form-group">
                        <div class="input-group-prepend">
                            <span class="input-group-text"><i class="fas fa-user"></i></span>
                        </div>
                        <input type="text" class="form-control" placeholder="username" name="username" >

                    </div>
                    <div class="input-group form-group">
                        <div class="input-group-prepend">
                            <span class="input-group-text"><i class="fas fa-key"></i></span>
                        </div>
                        <input type="password" class="form-control" placeholder="password" name="password">
                    </div>
                    <div class="form-check">
                        <input type="checkbox" name="rememberMe" class="form-check-input" id="remember-me-input"/>
                        <label class="form-check-label" for="remember-me-input">Remember Me</label>
                    </div>
                    <br>
                    <div class="form-group">
                        <input type="submit" value="Login" class="btn float-left login_btn">
                    </div>
                </form>
            </div>
            <div class="card-footer" >
                <div class="d-flex justify-content-center links">
                    Don't have an account?<a class="myClass" href="${signUpUrl}">Sign Up</a>
                </div>
                <div class="d-flex justify-content-center">
                    <a class="myClass" href="#">Forgot your password?</a>
                </div>
            </div>
        </div>

    </div>
</div>
    </div>
</body>
</html>