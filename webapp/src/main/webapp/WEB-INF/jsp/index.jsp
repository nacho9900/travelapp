<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
	<c:url value="/webjars/bootstrap/4.1.3/css/bootstrap.min.css" var="bootcss"/>
	<c:url value="/webjars/jquery/3.2.1/jquery.min.js" var="jquery" />
	<c:url value="/webjars/bootstrap/4.1.3/js/bootstrap.min.js" var="bootjs"/>
	<c:url value="/resources/icons/globe.ico" var="iconURL"/>
	<c:url value="/signin" var="signinURL"/>
	<c:url value="/signup" var="signupURL"/>
	<c:url value="/" var="index"/>
	<c:url value="/home" var="home"/>
	<c:url value="/about" var="about"/>
	<c:url value="/resources/css/index.css" var="indexcss"/>
	<link href="${bootcss}" rel="stylesheet">
	<link href="${indexcss}" rel="stylesheet">
    <link rel="shortcut icon" href="${iconURL}" type="image/x-icon"/>
	<title>Meet and Travel</title>
</head>
<body>
<%--navbar navbar-dark bg-dark--%>
	<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
	<a class="navbar-brand" href="${index}"></a>
	<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
		<span class="navbar-toggler-icon"></span>
	</button>

	<div class="collapse navbar-collapse" id="navbarSupportedContent">
		<ul class="navbar-nav mr-auto">
			<li class="nav-item active">
				<a class="nav-link" href="${home}">Home <span class="sr-only">(current)</span></a>
			</li>
			<li class="nav-item">
			</li>
			<li class="nav-item">
				<a class="nav-link" href="${about}">About us</a>
			</li>
		</ul>
		<form class="form-inline my-2 my-lg-0">
			<input class="form-control mr-sm-2" type="search" placeholder="Search for trips" aria-label="Search">
			<button class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>
		</form>
		<a id="signinButton" class="btn btn-outline-success my-2 my-sm-0" href="${signinURL}">Signin</a>
	</div>
</nav>
	<%--<a class="btn btn-primary" href="${signinURL}">Sign in</a>--%>
<%--	<img src="<c:url value="/resources/icons/plane.svg"/>" width="100" height="100" />--%>
	<div class="container">
		<h1 class="display-3 align-content-center " >Meet people and travel the world </h1>
		<p class="align-content-xl-center">If you are looking to meet people from all around the globe and go on adventures with them,
			you are in the right place.
		</p>
		<a class="btn btn-success align-content-center" href="${signupURL}">Sign up today</a>
	</div>

	<%--TODO: navbar, background--%>

	<script src="${bootjs}" type="text/javascript"></script>
	<script src="${jquery}" type="text/javascript"></script>
</body>
</html>