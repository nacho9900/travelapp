<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <c:url value="/resources/css/403.css" var="cssStyle"/>
    <c:url value="/home" var="homeURL"/>
    <link href="${cssStyle}" rel="stylesheet">
    <link rel="shortcut icon" href="<c:url value="/resources/icons/globe.ico"/>" type="image/x-icon"/>
    <title>Access Denied</title>
</head>
<body>
    <div id="app">
        <div>403</div>
        <div class="txt">
            <spring:message code="403.message"/><span class="blink">_</span>
        </div>
        <a id="homeButton" href="${homeURL}"><spring:message code="403.homeBtn"/></a>
    </div>
</body>
</html>