<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
	<title>Home Page</title>
</head>
<body>
    <h3>Welcome ${user.firstname} ${user.lastname}! </h3>
    <a class="btn btn-primary" href="/home/create-trip">Create Trip</a>
    <a class="btn btn-primary" href="/home/trips">My trips</a>
</body>
</html>