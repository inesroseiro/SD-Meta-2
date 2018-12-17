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
						Search by Album
				</span>
            <s:form action="searchbyalbum" method="post">
                <s:textfield cssClass="insert100" name = "albumName" placeholder="Album Name" required="true" />
                <s:div id="centering3">
                    <p></p>
                </s:div>
                <s:div cssClass="container-confirm100-form-btn">
                    <a href="login.jsp" class="loginVal">Search</a>
                    <s:div id="createAccountCenter">
                        <p></p>
                    </s:div>
                    <s:div class="text-center p-t-136">
                        <a class="corner" href="index.jsp">
                            Logout
                        </a>
                    </s:div>
                    <s:div class="text-center p-t-136">
                        <a class="cornerMenu" href="user.jsp">
                            Menu
                        </a>
                    </s:div>
                </s:div>
                <a class="back" href="AlbumUser.jsp">
                    <
                </a>
            </s:form>
        </s:div>
    </s:div>
</s:div>
</body>
</html>