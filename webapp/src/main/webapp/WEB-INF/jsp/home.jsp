<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="head.jsp"%>
    <title>Home</title>
</head>
<body>
    <%@include file="header.jsp"%>
    <h3 class="display-4">Welcome ${user.firstname} ${user.lastname}! </h3>
</body>
</html>