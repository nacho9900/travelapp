<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<html>
<head>
	<title>Meet & Travel</title>
	<link href="webjars/bootstrap/4.1.3/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
	<div class="container">
		<h1>Meet people and travel the world!</h1>
		<a href="<c:url value='/signup' />">Sign up</a>
		<a href="<c:url value='/signin' />" >Sign in</a>
		<a class="btn btn-success" href="#">Button</a>
	</div>

	<script src="webjars/jquery/3.2.1/jquery.min.js"></script>
	<script src="webjars/bootstrap/4.1.3/js/bootstrap.min.js"></script>
</body>
</html>