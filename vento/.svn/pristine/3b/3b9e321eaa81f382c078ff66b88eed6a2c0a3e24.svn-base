<%-- 
    Document   : testMultiMap
    Created on : Apr 3, 2013, 8:46:27 PM
    Author     : Administrator
--%>

<%@page import="java.util.Iterator"%>
<%@page import="java.util.Set"%>
<%@page import="org.apache.commons.collections.map.MultiValueMap"%>
<%@page import="org.apache.commons.collections.MultiMap"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <%
            MultiValueMap mhm = new MultiValueMap();
            String key = "";

            key = "Group One";
            mhm.put(key, "Item One");
            mhm.put(key, "Item Two");
            mhm.put(key, "Item Three");

            key = "Group Two";
            mhm.put(key, "Item Four");
            mhm.put(key, "Item Five");

            Set keys = mhm.keySet();
            for (Object k : keys) {
                out.println("(" + k + " : " + mhm.get(k) + ")");
            }
            Iterator it = keys.iterator();
            while (it.hasNext()) {
                System.out.println("" + it.next());
            }

        %>
        <h1>Hello World!</h1>
    </body>
</html>
