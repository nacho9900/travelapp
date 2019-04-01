<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<html>
<head>
    <link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/signup.css'/>" >
	<title>Sign In</title>
</head>
<body>
    <h3>Sign in</h3>
    <div class="container">
        <form method="post" action="/signin" method="post">
            <label for="username"><b>Username</b></label></br>
            <input type="text" placeholder="Enter username" name="username" size="25"></br>
            <label for="password"><b>Password</b></label></br>
            <input type="password" placeholder="Enter password "name="password" size="25"></br>
            <input type="submit" value="Submit">
        </form>
    </div>
</body>
</html>
