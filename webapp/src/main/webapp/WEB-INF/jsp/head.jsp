<%@ page contentType="text/html;charset=UTF-8"%>

<c:url value="/webjars/bootstrap/4.1.3/css/bootstrap.min.css" var="bootstrapCss"/>
<c:url value="/webjars/jquery/3.2.1/jquery.min.js" var="jquery" />
<c:url value="/webjars/bootstrap/4.1.3/js/bootstrap.min.js" var="bootstrapJs"/>
<c:url value="/resources/icons/globe.ico" var="iconURL"/>
<c:url value="/resources/css/site.css" var="siteCss"/>

<link href="${bootstrapCss}" rel="stylesheet">
<link rel="shortcut icon" href="${iconURL}" type="image/x-icon"/>
<link href="${siteCss}" rel="stylesheet">
<script src="${jquery}" type="text/javascript"></script>
<script src="${bootstrapJs}" type="text/javascript"></script>
