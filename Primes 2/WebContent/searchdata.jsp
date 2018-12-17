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
	<h2> Search Data </h2>
	<a href="viewdata.jsp"> <s:submit value="View Data "/> </a>
	<p> </p>
	<a href="searchdata.jsp"> <s:submit value="Search Data"/> </a>
	<p> </p>
	<a href="writedata.jsp"> <s:submit value="Write Critic"/> </a>
	<p> </p>
	<a href="uploadmusic.jsp"> <s:submit value="Upload Music"/> </a>
	<p> </p>
	<a href="downloadmusic.jsp"> <s:submit value="Download Artist"/> </a>
	<p> </p>
</s:div>
</body>
</html>
