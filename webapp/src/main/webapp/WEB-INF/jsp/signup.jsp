<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
    <link href="webjars/bootstrap/4.1.3/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/signup.css"/>" >
    <link rel="shortcut icon" href="<c:url value="/resources/icons/globe.ico"/>" type="image/x-icon"/>
	<title>Create Account</title>
</head>
<body>
    <c:url value="/signup" var="post_path"/>
    <form:form action="${post_path}" method="post" modelAttribute="signupForm">
      <div class="container">
        <h1>Register</h1>
        <p>Please fill in this form to create an account.</p>
        <hr>
        <form:errors path="firstname" cssClass = "alert alert-warning" element="p"/>
        <form:input type="text" placeholder="Enter First Name" name="firstname"  path="firstname"/>

        <form:errors path="lastname" cssClass = "alert alert-warning" element="p"/>
        <form:input type="text" placeholder="Enter Last Name" name="lastname" path="lastname"/>

        <form:errors path="email" cssClass = "alert alert-warning" element="p"/>
        <form:input type="text" placeholder="Enter Email" name="email" path="email"/>

        <form:errors path="password" cssClass = "alert alert-warning" element="p"/>
        <form:input type="password" placeholder="Enter Password" name="password" path="password"/>

        <form:errors path="pswrepeat" cssClass = "alert alert-warning" role="alert" element="p"/>
        <form:input type="password" placeholder="Repeat Password" name="pswrepeat" path="pswrepeat"/>

        <hr>
        <p>By creating an account you agree to our <a href="#">Terms & Privacy</a>.</p>
        <button type="submit" class="registerbtn">Register</button>
      </div>
      <div class="container signin">
        <p>Already have an account? <a href="/signin">Sign in</a>.</p>
      </div>
    </form:form>
    <script src="webjars/jquery/3.2.1/jquery.min.js"></script>
    <script src="webjars/bootstrap/4.1.3/js/bootstrap.min.js"></script>
</body>
</html>


