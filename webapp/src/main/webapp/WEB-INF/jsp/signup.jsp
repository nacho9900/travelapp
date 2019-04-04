<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<html>
<head>
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/signup.css"/>" >
	<title>Create Account</title>
</head>
<body>
    <form action="/signup" method="post">
      <div class="container">
        <h1>Register</h1>
        <p>Please fill in this form to create an account.</p>
        <hr>
        <input type="text" placeholder="Enter First Name" name="firstname" required>
        <input type="text" placeholder="Enter Last Name" name="lastname" required>
        <input type="text" placeholder="Enter Email" name="email" required>
        <input type="password" placeholder="Enter Password" name="password" required>
        <input type="password" placeholder="Repeat Password" name="pswrepeat" required>
        <hr>
        <p>By creating an account you agree to our <a href="#">Terms & Privacy</a>.</p>
        <button type="submit" class="registerbtn">Register</button>
      </div>
      <div class="container signin">
        <p>Already have an account? <a href="/signin">Sign in</a>.</p>
      </div>
    </form>
</body>
</html>


