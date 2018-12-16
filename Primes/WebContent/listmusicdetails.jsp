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
	<h2> View music details </h2>
	<p> Music Name </p>
	${session.name}
	<p> Genre </p>
	${session.genre}
	<p> </p>
	<p> Duration </p>
	${session.duration}
	<p> </p>
	<p> Realease Date </p>
	${session.date}
	<p> </p>
	<p> Lyrics </p>
	${session.lyrics}
	<p> </p>


</s:div>
</body>
</html>
