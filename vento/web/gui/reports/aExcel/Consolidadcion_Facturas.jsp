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

        if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
            LinkedList<String> access4ThisPage = new LinkedList();
            access4ThisPage.add("ReporteConsolidacionFacturas");

            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {

%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%            String date_num = "ConsolidacionFacturas.xls";
            response.setContentType("application/vnd.ms-excel; encoding=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=\"" + date_num + "\"");
        %>
    </head>
    <body>
        <%
            Iterator t = QUID.select_Consolidacion_Facturas(
                    WebUtil.decode(session, request.getParameter("idPlantel")),
                    request.getParameter("fechaInicio"),
                    request.getParameter("fechaFin")).iterator();
        %>
        <table>
            <tr style="background: #CCCCCC;font-weight: bold;">
                <td>Plantel</td>
                <td>Tipo Compra</td>
                <td>Folio</td>
                <td>No. Factura</td>
                <td>Fecha de Factura</td>
                <td>Proveedor</td>
                <td>Monto</td>
            </tr>
            <%
            while(t.hasNext()){
                LinkedList datos=(LinkedList)t.next();
                %>
                <tr>
                <td><%=datos.get(12)%></td>
                <td><%=datos.get(17)%></td>
                <td><%=datos.get(2)%></td>
                <td><%=datos.get(5)%></td>
                <td><%=datos.get(3)%></td>
                <td><%=datos.get(15)%></td>
                <td><%=datos.get(18)%></td>
            </tr>
                <%
            }
            %>
        </table>
    </body> 
</html>
<%            } else {
                out.print("Usted NO tiene permios para realizar esta acción");
            }
        } else {
            out.print("Petición invalida");
        }
    } catch (Exception ex) {
        out.print("Ocurrio un error");
        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
    }
%>