<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<html>
<head>
    <link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/signup.css' />" >
	<title>Sign Up</title>
</head>
<body>
    <form action="/signup" method="post">
      <div class="container">
        <h1>Register</h1>
        <p>Please fill in this form to create an account.</p>
        <hr>

        <label for="first_name"><b>First Name</b></label>
        <input type="text" placeholder="Enter First Name" name="first_name" required>

        <label for="last_name"><b>Last Name</b></label>
        <input type="text" placeholder="Enter Last Name" name="last_name" required>

        <label for="email"><b>Email</b></label>
        <input type="text" placeholder="Enter Email" name="email" required>

        <label for="password"><b>Password</b></label>
        <input type="password" placeholder="Enter Password" name="password" required>

        <label for="psw_repeat"><b>Repeat Password</b></label>
        <input type="password" placeholder="Repeat Password" name="psw_repeat" required>
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


