<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
	<%@include file="head.jsp"%>
	<c:url value="/signup" var="signUpUrl"/>
	<c:url value="/resources/css/index.css" var="indexCss"/>
	<link href="${indexCss}" rel="stylesheet">
	<title>Meet and Travel</title>
</head>
<body>
	<jsp:include page="header.jsp"/>
	<div class="container">
		<h1 class="display-3 align-content-center " >Meet people and travel the world </h1>
		<p class="align-content-xl-center">If you are looking to meet people from all around the globe and go on adventures with them,
			you are in the right place.
		</p>
		<a class="btn btn-success align-content-center" href="${signUpUrl}">Sign up today</a>
	</div>
</body>
</html>