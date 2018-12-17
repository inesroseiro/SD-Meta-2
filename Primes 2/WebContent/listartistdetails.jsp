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
						View Artist Details
				</span>
			<p class ="insert101">Artist Name:</p>
			<a class="insert100view">${session.name}</a>
			<p class = insert101> Biography: </p>
			<a class = "insert100view">${session.description}</a>
			<p></p>
			<p class = "insert101">Discography:</p>
			<c:forEach items="${listaInformacoes}">
				<a class = "insert100view">${listaInformacoes} <br> </a>
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
			<a class="backView" href="ArtistEditor.jsp">
				<
			</a>
		</s:if>
		<s:else>
			<a class="backView" href="ArtistUser.jsp">
				<
			</a>
		</s:else>
	</s:div>
</s:div>
</body>
</body>
</body>
</html>