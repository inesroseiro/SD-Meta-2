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
	<h2> View artist details </h2>
	<s:form action="viewartistdetails" method="post">
		<p> Username </p>
		<s:textfield name = "artistname" required="true" />
		<p> </p>
		<s:submit value="View artist details"/>
	</s:form>

</s:div>
</body>
</html>
