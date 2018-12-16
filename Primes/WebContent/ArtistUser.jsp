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
				<span class="artist100-form-title">
						Artist
				</span>
            <s:form action="login" method="post">
                <s:div cssClass="container-login100-form-btn">
                        <a href="searchByArtistUser.jsp" class="selectVal">Search by Artist</a>
                    <s:div id="centering2">
                        <p></p>
                    </s:div>
                        <a href="viewArtistDetailsUser.jsp" class="selectVal">View Details</a>
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
            </s:form>
        </s:div>
    </s:div>
</s:div>
</body>
</html>
