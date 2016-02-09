<%--
  Created by IntelliJ IDEA.
  User: ojrobert
  Date: 1/18/16
  Time: 2:35 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Upload</title>
</head>
<body>
<h1>Upload a File</h1>
<form action="/rest/fs/upload" method="post" enctype="multipart/form-data">
    <p>
        Select a file : <input type="file" name="file" size="50" />
    </p>
    <input type="submit" value="Upload It" />
</form>
</body>
</html>
