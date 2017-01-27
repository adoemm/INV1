<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script type = "text/javascript" >
            function changeHashOnLoad() {
                window.location.href += "#";
                setTimeout("changeHashAgain()", "50");
            }

            function changeHashAgain() {
                window.location.href += "1";
            }

            var storedHash = window.location.hash;
            window.setInterval(function() {
                if (window.location.hash != storedHash) {
                    window.location.hash = storedHash;
                }
            }, 50);
        </script>
    </head>
    <body onload="changeHashOnLoad();">
        Try to hit back!
        <p></p>
        <%
            if (session.getAttribute("encryptedKey") == null) {
                session.setAttribute("encryptedKey", EDP.encryptedKey(session.getId() + PageParameters.getParameter("encodeParametersSemilla")));
            }
            EDP.encrypt(session, "14");

            out.print(WebUtil.encode(session, "2"));
            //out.print(WebUtil.decode(session, WebUtil.encode(session, "14")));
            //Qv02F+YPNgk_jspread_
        %>
        <p></p>
        <%
            out.print(WebUtil.decode(session, WebUtil.encode(session, "2")));
            out.print(WebUtil.decode(session, "v4RQ7JSCy+Q_jspread_"));
        %>
    </body>
</html>
