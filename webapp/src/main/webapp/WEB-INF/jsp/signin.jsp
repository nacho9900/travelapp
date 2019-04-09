<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<!DOCTYPE html>
<html>
<head>
    <title>Sign in</title>
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.3.1/css/all.css" integrity="sha384-mzrmE5qonljUremFsqc01SB46JvROS7bZs3IO2EmfFsd15uHvIt+Y8vEf7N7fWAU" crossorigin="anonymous">
    <link rel="stylesheet" type="text/css" href="/resources/css/signin.css">
    <link rel="shortcut icon" href="<c:url value="/resources/icons/globe.ico"/>" type="image/x-icon"/>
</head>
<body>
    <a href="/" class="btn btn-primary">
        <span class="glyphicon glyphicon-chevron-left"></span> Back
    </a>
    <%--<a class="" href="/">Back</a>--%>
    <div class="container">
        <div class="d-flex justify-content-center h-100">
            <div class="card">
                <div class="card-header">
                    <h3>Sign In</h3>
                </div>
                <div class="card-body">
                    <form  method="post" action="/signin" method="post">
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
                        <div class="row align-items-center remember">
                            <input type="checkbox">Remember Me
                        </div>
                        <br>
                        <div class="form-group">
                            <input type="submit" value="Login" class="btn float-left login_btn">
                        </div>
                    </form>
                </div>
                <div class="card-footer" >
                    <div class="d-flex justify-content-center links">
                        Don't have an account?<a href="/signup">Sign Up</a>
                    </div>
                    <%--<div class="d-flex justify-content-center">
                        <a href="#">Forgot your password?</a>
                    </div>--%>
                </div>
            </div>
        </div>
    </div>
<script src="webjars/jquery/3.2.1/jquery.min.js"></script>
<script src="webjars/bootstrap/4.1.3/js/bootstrap.min.js"></script>
</body>
</html>