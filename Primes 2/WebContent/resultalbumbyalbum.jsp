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
				<span class="login100-form-title">
						Search Albums by Album Name
				</span>
			<p class ="insert101">Album Name:</p>
			<c:forEach items="${session.albuns}">
				<a class = "insert100view">${albuns} <br> </a>
			</c:forEach>
			<p class ="insert101">Artist Name:</p>
			<c:forEach items="${session.artistas}">
				<a class = "insert100view">${artistas} <br> </a>
			</c:forEach>
			<p></p>
		</s:div>
		<s:div cssClass="container-confirm100-form-btn">
			<s:div id="createAccountCenter">
				<p></p>
			</s:div>
			<s:div class="text-center p-t-136">
				<a class="corner" href="index.jsp">
					Logout
				</a>
			</s:div>
			<s:div class="text-center p-t-136">
				<a class="cornerMenu" href="editor.jsp">
					Menu
				</a>
			</s:div>
		</s:div>
		<s:if test="%{session.privilege == 'editor'}">
			<a class="backView" href="AlbumEditor.jsp">
				<
			</a>
		</s:if>
		<s:else>
			<a class="backView" href="AlbumUser.jsp">
				<
			</a>
		</s:else>
	</s:div>
</s:div>
</body>
</body>
</body>
</html>

