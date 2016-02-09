<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%--<html>--%>
<%--<head>--%>
    <%--<title>Hello Facebook</title>--%>
<%--</head>--%>
<%--<body>--%>
<%--<h3>Connect to Facebook</h3>--%>
<%----%>
<%--<form action="/connect/facebook" method="POST">--%>
    <%--<input type="hidden" name="scope" value="read_stream" />--%>
    <%--<div class="formInfo">--%>
        <%--<p>You aren't connected to Facebook yet. Click the button to connect this application with your Facebook account.</p>--%>
    <%--</div>--%>
    <%--<p><button type="submit">Connect to Facebook</button></p>--%>
<%--</form>--%>
<%--</body>--%>
<%--</html>--%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Insert title here</title>
</head>
<body>
<div>

    ${methodHTTP}
    <jsp:forward page="main.jsp"/>
</div>
</body>
</html>