<%@page import="com.bielu.gpw.GpwMonitor"%>
<%@page import="com.bielu.gpw.Util"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">
</head>
<body>
<form action="sharesDb" method="post">
<textarea width="100%" height="200px">
<%
out.print(Util.fileToString(GpwMonitor.SHARES_DB_FILE));
%>
</textarea>
<p>
<input type="submit" value="Update">
</form>
</body>
</html>