<%--
  Created by IntelliJ IDEA.
  User: dingo
  Date: 13/12/2018
  Time: 17:27
  To change this template use File | Settings | File Templates.
--%>
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
						Register
				</span>
            <s:form action="signup" method="post">
                <s:textfield cssClass="input100" name = "username" placeholder="Username" required="true" />
                <s:div id="centering">
                    <p></p>
                </s:div>
                <s:password cssClass = "input100" name = "password" placeholder="Password" required="true" />
                <s:div id="centering">
                    <p></p>
                </s:div>
                <s:div cssClass="container-login100-form-btn">
                    <s:submit cssClass="loginVal" value="Register"/>
                    </s>
                    <s:div id="createAccountCenter">
                        <p></p>
                    </s:div>
                    <s:div class="text-center p-t-136">
                        <a class="corner" href="index.jsp">
                            Menu
                        </a>
                    </s:div>
                </s:div>
            </s:form>
        </s:div>
    </s:div>
</s:div>
</body>
</body>
</body>
</html>
