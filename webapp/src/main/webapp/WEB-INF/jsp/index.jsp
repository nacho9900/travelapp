<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<html>
<head>
	<title>Meet and Travel</title>
	<link href="webjars/bootstrap/4.1.3/css/bootstrap.min.css" rel="stylesheet">
    <link rel="shortcut icon" href="<c:url value="/resources/icons/globe.ico"/>" type="image/x-icon"/>
</head>
<body>


	<div class="pos-f-t">
		<div class="collapse" id="navbarToggleExternalContent">
			<div class="bg-dark p-4">
				<div class="container">
					<h1>Meet people and travel the world! </h1>
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
	</div>
	<%--TODO: PONER UNA IMAGEN(de fondo) o algo mas lindo, no este cosito--%>
	<img src="<c:url value="/resources/icons/plane.svg"/>" width="100" height="100" />
	<script src="webjars/jquery/3.2.1/jquery.min.js"></script>
	<script src="webjars/bootstrap/4.1.3/js/bootstrap.min.js"></script>
</body>
</html>