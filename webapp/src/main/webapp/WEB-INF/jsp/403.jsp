<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<html>
<head>
    <c:url value="/resources/css/404.css" var="cssStyle"/>
    <link href="${cssStyle}" rel="stylesheet">
    <link rel="shortcut icon" href="<c:url value="/resources/icons/globe.ico"/>" type="image/x-icon"/>
    <title>Access Denied</title>
</head>
<body>
    <div id="app">
        <div>403</div>
        <div class="txt">
            Forbidden<span class="blink">_</span>
        </div>
    </div>
</body>
</html>