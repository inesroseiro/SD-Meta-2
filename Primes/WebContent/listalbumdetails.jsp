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
						View Album Details
				</span>
				<p class ="insert100">Artist Name</p>
				<p class = "insert100">${session.artist}</p>
				<p class = insert100> Description </p>
				<p class = "insert100">${session.description}</p>
				<p class = "insert100"> Genre </p>
				<p class = "insert100">${session.genre}</p>
				<p class = "insert100"> Date </p>
				<p class = "insert100>"${session.date}</p>
				<p></p>
				<p class = "insert100">Songs </p>
				<c:forEach items="${session.listaMusicas}">
					<p class = "insert100>"${session.listaMusicas} <br> </p>
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
				<a class="back" href="AlbumEditor.jsp">
					<
				</a>
			</s:form>
		</s:div>
	</s:div>
</s:div>
</body>
</body>
</body>
</html>