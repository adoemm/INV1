<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
        <%
        out.print("<p>"+request.getParameter("FormName"));
        out.print("<p>"+request.getParameter("FormName"));
        out.print("<p>"+request.getParameter("FormName"));
        out.print("<p>"+request.getParameter("FormName"));
        out.print("<p>"+request.getParameter("FormName"));
        
        %>
    </body>
</html>
