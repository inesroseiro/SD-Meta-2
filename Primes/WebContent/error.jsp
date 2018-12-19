<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
		 pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel="stylesheet" type="text/css" href="style.css">

	<title>Welcome to DropMusic</title>
</head>
<body>
<s:div cssClass="limiter">
	<s:div cssClass="container-login100">
		<s:div cssClass = "wrap-login100">
				<span class="select101-form-title">
						${username}
				</span>
			<a class="insert102"> Something went wrong!</a>
			<s:if test="%{session.privilege == 'editor'}">
				<a class="backView" href="editor.jsp">
					<
				</a>
			</s:if>
			<s:else>
			<a class="backView" href="user.jsp">
				<
			</a>
			</s:else>
			</s:div>
		</s:div>
	</s:div>
</body>
</html>