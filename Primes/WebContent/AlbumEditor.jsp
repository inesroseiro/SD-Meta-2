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
						Album
				</span>
            <s:form action="login" method="post">
                <s:div cssClass="container-login100-form-btn">
                        <a href="insertalbum.jsp" class="selectVal">Insert Album</a>
                    <s:div id="centering2">
                        <p></p>
                    </s:div>
                        <a href="removealbum.jsp" class="selectVal">Remove Album</a>
                    <s:div id="centering2">
                        <p></p>
                    </s:div>
                        <a href="editalbum.jsp" class="selectVal">Edit Album</a>
                    <s:div id="centering2">
                        <p></p>
                    </s:div>
                        <a href="searchbbyalbumed.jsp" class="selectVal">Search by Album</a>
                    <s:div id="centering2">
                        <p></p>
                    </s:div>
                        <a href="viewalbumdetailseditor.jsp" class="selectVal">View Details</a>
                    <s:div id="centering2">
                        <p></p>
                    </s:div>
                        <a href="viewcriticsed.jsp" class="selectVal">View Critics</a>
                    <s:div id="createAccountCenter">
                        <p></p>
                    </s:div>
                    <s:form action="logout" method = "post">
                        <s:div class="text-center p-t-136">
                            <a class="corner" href="index.jsp">
                                Logout
                            </a>
                        </s:div>
                    </s:form>
                    <s:div class="text-center p-t-136">
                        <a class="cornerMenu" href="editor.jsp">
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
