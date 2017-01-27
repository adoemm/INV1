<%@page pageEncoding="utf-8" language="java"%>
<%@page import="jspread.core.util.security.EDP"%>
<%@page import="jspread.core.db.QUID"%>
<%@page import="jspread.core.util.PageParameters"%>
<%@page import="systemSettings.SystemSettings"%>
<%@page import="java.util.logging.Logger"%>
<%@page import="java.util.logging.Level"%>
<%@page import="java.util.LinkedList"%>
<%@page import="java.util.Iterator"%>
<%@page import="jspread.core.util.WebUtil"%>
<%@page import="jspread.core.util.StringUtil"%>
<%@page import="jspread.core.util.UTime"%>
<%@page import="jspread.core.util.UserUtil"%>


<jsp:useBean id="QUID" scope="page" class="jspread.core.db.QUID"/>

<%
    try {
        session = request.getSession(true);
        QUID.setRequest(request);
        Iterator it = null;

        if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
            LinkedList<String> access4ThisPage = new LinkedList();
            access4ThisPage.add("ConsultaMovimientoSalida");

            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {

%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%            String date_num = "ConsultaSalida4Plantel.xls";
            response.setContentType("application/vnd.ms-excel; encoding=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=\"" + date_num + "\"");
        %>
    </head>
    <body>
        <table border="0" width="100%">
            <tr>
                <th>Folio</th>
                <th>No. Turno</th>
                <th>Plantel</th>
                <th>Depto.</th>
                <th>Fecha</th>
                <th>Estatus</th>  
            </tr>
            <%
                it = QUID.select_MovimientoSalida(WebUtil.decode(session, request.getParameter("idPlantel")), QUID.select_TipoMovimiento("SALIDA") + "", false, "", false, false).iterator();
                while (it.hasNext()) {
                    LinkedList listAux = (LinkedList) it.next();
            %>
            <tr>
                <td><%=listAux.get(2)%></td>
                <td><%=listAux.get(24)%></td>
                <td><%=listAux.get(22)%></td>
                <td><%=listAux.get(18)%></td>
                <td><%=listAux.get(4)%></td>
                <td><%=listAux.get(6)%></td>                
            </tr>
            <%
                }
            %>
        </table>
    </body> 
</html>
<%
            }
        }
    } catch (Exception ex) {
        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
    }
%>