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
            access4ThisPage.add("ReporteMovimientosSalida");

            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
               String fechaInicio= request.getParameter("fechaInicio");
                String fechaFin= request.getParameter("fechaFin");
             
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%            String date_num = "MovimientosSalida.xls";
            response.setContentType("application/vnd.ms-excel; encoding=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=\"" + date_num + "\"");
        %>
    </head>
    <body>
        <%
            Iterator it = null;
            String auxPlantel = WebUtil.decode(session, request.getParameter("idPlantel"));
            if (auxPlantel.equalsIgnoreCase("Todos")) {
                it = QUID.select_MovimientoSalida4Fecha(
                        auxPlantel,
                        QUID.select_TipoMovimiento("SALIDA") + "",
                        true,
                        "",
                        false,
                        false,
                        request.getParameter("fechaInicio"),
                        request.getParameter("fechaFin")).iterator();
            } else {
                it = QUID.select_MovimientoSalida4Fecha(
                        auxPlantel,
                        QUID.select_TipoMovimiento("SALIDA") + "",
                        false,
                        "",
                        false,
                        false,
                        request.getParameter("fechaInicio"),
                        request.getParameter("fechaFin")).iterator();
            }
        %>
        <table>
            <tr style="background: #CCCCCC;font-weight: bold;">
                <td>No. Folio</td>
                <td>Plantel de Entrega</td>
                <td>Depto.</td>
                <td>No. Turno</td>
                <td>Fecha</td>
                <td>Estatus</td>
            </tr>
            <%                while (it.hasNext()) {
                    LinkedList datos = (LinkedList) it.next();
            %>
            <tr>
                <td><%=datos.get(2)%></td>
                <td><%=datos.get(22).toString().toUpperCase()%></td>
                <td><%=datos.get(18)%></td>
                <td><%=datos.get(24)%></td>
                <td><%=datos.get(3)%></td>
                <td><%=datos.get(6).toString().toUpperCase()%></td>
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