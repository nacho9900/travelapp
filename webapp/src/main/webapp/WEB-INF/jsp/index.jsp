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
	<c:url value="/home/profile" var="profile"/>
	<c:url value="/about" var="about"/>
	<c:url value="/resources/css/index.css" var="indexcss"/>
	<c:url value="/resources/icons/earth-globe.png" var="globeIMG"/>
	<c:url value="/resources/icons/uu.png" var="userIMG"/>

	<link href="${bootcss}" rel="stylesheet">
	<link href="${indexcss}" rel="stylesheet">
    <link rel="shortcut icon" href="${iconURL}" type="image/x-icon"/>
	<title>Meet and Travel</title>
</head>
<body>
	<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
		<a class="navbar-brand" href="${index}">
			<img src="${globeIMG}" height="42" width="42"/>
		</a>
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
			<c:choose>
				<c:when test="${user == null}">
					<a id="signinButton" class="btn btn-success my-2 my-sm-0" href="${signinURL}">Sign in</a>
				</c:when>
				<c:otherwise>
					<a href="${profile}">
						<img alt="" style="margin-left: 10px" src="${userIMG}" height="32" width="32"/>
					</a>
				</c:otherwise>
			</c:choose>

		</div>
	</nav>
	<div class="container">
		<h1 class="display-3 align-content-center " >Meet people and travel the world </h1>
		<p class="align-content-xl-center">If you are looking to meet people from all around the globe and go on adventures with them,
			you are in the right place.
		</p>
		<a class="btn btn-success align-content-center" href="${signupURL}">Sign up today</a>
	</div>
	<script src="${bootjs}" type="text/javascript"></script>
	<script src="${jquery}" type="text/javascript"></script>
</body>
</html>