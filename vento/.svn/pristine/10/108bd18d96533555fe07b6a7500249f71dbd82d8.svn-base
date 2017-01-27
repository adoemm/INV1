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
            access4ThisPage.add("ReporteMovimientosEntrada");

            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                String fechaInicio = request.getParameter("fechaInicio");
                String fechaFin = request.getParameter("fechaFin");
                String idplantel = WebUtil.decode(session, request.getParameter("idPlantel"));
                int plantel;
                if (idplantel.equalsIgnoreCase("Todos")) {
                    //para sacar reporte de todos los planteles.
                    plantel = 0;
                } else {
                    plantel = Integer.parseInt(idplantel);
                }

%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%  String date_num = "MovimientosEntrada.xls";
            response.setContentType("application/vnd.ms-excel; encoding=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=\"" + date_num + "\"");
        %>
    </head>
    <body>

        <table border="0" width="100%">
            <tr>
                <th>Plantel</th>
                <th>Folio</th>
                <th>Fecha de Entrada</th>
                <th>Proveedor</th>
                <th>No Factura</th>

                <th>Tipo Compra</th>
                <th>Estatus</th>   
            </tr>
            <%
                LinkedList movimientos = QUID.select_MovimientoxPlantelxFechasxTipo(1, fechaInicio, fechaFin, plantel);

                for (int i = 0; i < movimientos.size(); i++) {
                    String mov = movimientos.get(i).toString();
                    LinkedList datos = QUID.select_Movimiento4ID(mov, "", false, false);
                    String plantelaux = QUID.select_Plantel(Integer.parseInt(datos.get(1).toString()));

            %>
            <tr>     
                <td><%= plantelaux%></td>
                <td><%=datos.get(2)%></td>
                <td><%=datos.get(3)%></td>
                <td><%=datos.get(14)%></td>
                <td><%=datos.get(5)%></td>
                <td><%=datos.get(16)%></td>
                <td><%=datos.get(7)%></td>
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