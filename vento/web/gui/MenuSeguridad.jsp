<%@page import="java.util.Calendar"%>
<%@page import="java.util.Calendar"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%
    try {
        if (fine) {

            LinkedList<String> access4ThisPage = new LinkedList();
            access4ThisPage.add("MenuSeguridad");

            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                Iterator it = null;
                LinkedList listAux = null;
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Seguridad</title>
        <jsp:include page='<%=PageParameters.getParameter("globalLibs")%>'/>        

        <script type="text/javascript" language="javascript" charset="utf-8">
            window.history.forward();
            function noBack() {
                window.history.forward();
            }
        </script>
    </head>
    <body>
        <div id="wrapper">
            <div id="divBody">
                <jsp:include page='<%=("" + PageParameters.getParameter("logo"))%>' />
                <div id="barMenu">
                    <jsp:include page='<%=(PageParameters.getParameter("barMenu"))%>' />
                </div>

                <p></p>
                <table width="100%" height="25px" border="0" align="center" cellpadding="0" cellspacing="0">
                    <tr>
                        <td width="64%" height="25" align="left" valign="top">
                            <a class="NVL" href="<%=PageParameters.getParameter("mainMenu")%>?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"> Menú Principal</a> > Seguridad
                        </td>
                        <td width="36" align="right" valign="top">
                            <script language="JavaScript" src="<%=PageParameters.getParameter("jsRcs")%>/funcionDate.js" type="text/javascript"></script>
                        </td>
                    </tr>                        
                </table>
                <br>
                <br>
                <br>
                <div>
                    <table width="200" border="0" align="center"  >
                        <tr>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaUsuarios.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/adminUsers.png" width="150" height="40"/></a></td>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaRol.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/adminRoles.png" width="150" height="40"/></a></td>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaPermisos.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/catalogoPermisos.png" width="150" height="40"/></a></td>
                        </tr>
                        <tr>
                            <td><a href="<%=PageParameters.getParameter("mainContext")%>/jspread/admin/MaintenanceOn.jsp"><img src="<%=PageParameters.getParameter("imgRsc")%>/setOffline.png" width="150" height="40"/></a></td>
                            <td><a href="<%=PageParameters.getParameter("mainContext")%>/jspread/admin/MaintenanceOff.jsp"><img src="<%=PageParameters.getParameter("imgRsc")%>/setOnline.png" width="150" height="40"/></a></td>
                        </tr>
                    </table>
                </div>
                <br>
                <br>
                <div id="divFoot">
                    <jsp:include page='<%=(PageParameters.getParameter("footer"))%>' />
                </div> 
            </div>            
        </div>
    </body>
</html>
<%
} else {

%>                
<%@ include file="/gui/pageComponents/invalidUser.jsp"%>
<%    }
    }
} catch (Exception ex) {
    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
%>
<%@ include file="/gui/pageComponents/handleUnExpectedError.jsp"%>
</body>
</html>
<%
    }
%>
