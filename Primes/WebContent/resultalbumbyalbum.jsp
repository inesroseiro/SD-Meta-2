<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
		 pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>WDropMusic</title>
</head>
<body>
<s:div class="WelcomeTitle">
	<h2> Search albums by albbum name </h2>
	<p> Album  Name </p>
	<c:forEach items="${session.albuns}">
		${albuns} <br>
	</c:forEach>
	<p> Artist  Name </p>
	<c:forEach items="${session.artistas}">
		${artistas} <br>
	</c:forEach>


</s:div>
</body>
</html>
