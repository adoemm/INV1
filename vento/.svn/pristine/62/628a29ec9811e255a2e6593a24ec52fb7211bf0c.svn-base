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
            access4ThisPage.add("ReporteConsumoCantidad");

            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {

%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%            String date_num = "ConsumoCantidad.xls";
            response.setContentType("application/vnd.ms-excel; encoding=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=\"" + date_num + "\"");
        %>
    </head>
    <body>
        <%
            Iterator t = QUID.select_ConsumoCantidad4Plantel(
                    WebUtil.decode(session, request.getParameter("peDestino")),
                    WebUtil.decode(session, request.getParameter("idPlantel")),
                    request.getParameter("fechaInicio"),
                    request.getParameter("fechaFin"),
                    QUID.select_TipoMovimiento("SALIDA") + "").iterator();
            Double inventarioValorizado = 0.0;
            Double valorInventario=0.0;
        %>
        <table>
            <tr>
                <td>PLANTEL</td>
                <td>FOLIO</td>
                <td>FECHA MOVIMIENTO</td>
                <td>CLAVE</td>
                <td>DESC</td>
                <td>MEDIDA</td>
                <td>PRECIO</td>
                <td>CANTIDAD</td>
                <td>TOTAL</td>
            </tr>
            <%
                while (t.hasNext()) {
                    LinkedList datos = (LinkedList) t.next();
                    Double totalXConsumible = Double.parseDouble(datos.get(6).toString()) * Double.parseDouble(datos.get(3).toString());
            %>
            <tr>
                <td><%=datos.get(20)%></td>
                <td><%=datos.get(10)%></td>
                <td><%=datos.get(11)%></td>
                <td><%=datos.get(1)%></td>
                <td><%=datos.get(2)%></td>
                <td><%=datos.get(33)%></td>
                <td><%=datos.get(3)%></td>
                <td><%=datos.get(6)%></td>
                <td><%=totalXConsumible%></td>
            </tr>
            <%
                    valorInventario += totalXConsumible;
                }
            %>
            <tr style="background: #CCCCCC;font-size: 18px; text-align: right;">
                <td colspan="8">TOTAL</td>
                <td><%=valorInventario%></td>
            </tr>
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