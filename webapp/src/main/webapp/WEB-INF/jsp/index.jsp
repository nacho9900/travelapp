<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<html>
<head>
	<title>Meet and Travel</title>
	<link href="webjars/bootstrap/4.1.3/css/bootstrap.min.css" rel="stylesheet">
    <link rel="shortcut icon" href="<c:url value="/resources/icons/globe.ico"/>" type="image/x-icon"/>
	<link rel="stylesheet" href="/resources/css/index.css">
</head>
<body>
	<ul class="nav justify-content-end">
		<li class="nav-item">
			<a class="btn btn-primary" href="/signin">Sign in</a>
		</li>
	</ul>

	<div class="container">
		<h1>Meet people and travel the world! <img src="<c:url value="/resources/icons/plane.svg"/>" width="100" height="100" /> </h1>
		<p class="align-content-xl-center">If you are looking to meet people from all around the globe on go on adventures with them,
			you are in the right place. Sign up and start planning your trips, its free and easy to use.
		</p>
		<a class="btn btn-success" href="/signup">Sign up today</a>

	</div>

<%--
	<div class="pos-f-t">
		<div class="collapse" id="navbarToggleExternalContent">
			<div class="bg-dark p-4">
				<div class="container">

					<a href="<c:url value='/signup' />">Sign up</a>
					<a href="<c:url value='/signin' />" >Sign in</a>
				</div>
			</div>
		</div>
		<nav class="navbar navbar-dark bg-dark">
			<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarToggleExternalContent" aria-controls="navbarToggleExternalContent" aria-expanded="false" aria-label="Toggle navigation">
				<span class="navbar-toggler-icon"></span>
			</button>
		</nav>
	</div>--%>
	<%--TODO: PONER UNA IMAGEN(de fondo) o algo mas lindo, no este cosito--%>

	<script src="webjars/jquery/3.2.1/jquery.min.js"></script>
	<script src="webjars/bootstrap/4.1.3/js/bootstrap.min.js"></script>
</body>
</html>