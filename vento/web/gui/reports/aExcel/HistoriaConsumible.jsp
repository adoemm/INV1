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
            access4ThisPage.add("ReporteHistoriaConumible");

            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {

%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%            String date_num = "HistorialConsumible.xls";
            response.setContentType("application/vnd.ms-excel; encoding=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=\"" + date_num + "\"");
        %>
    </head>
    <body>
        <%
            Iterator t = QUID.select_HistoriaEntrada4Conumible(
                    WebUtil.decode(session, request.getParameter("idPlantel")),
                    WebUtil.decode(session, request.getParameter("idConsumible")),
                    "1",
                    request.getParameter("fechaInicio"),
                    request.getParameter("fechaFin")).iterator();

            LinkedList consumible = QUID.select_Consumible(WebUtil.decode(session, request.getParameter("idConsumible")));

        %>
        <table>
            <tr style="background: #CCCCCC; text-align: center; font-weight: bold;">
                <td colspan="8">
                    HISTORIAL DE MOVIMIENTOS
                </td>
            </tr>
            <tr>
                <td style="font-weight: bold;">Categoria</td>
                <td><%=consumible.get(17)%></td>
            </tr>
            <tr>
                <td style="font-weight: bold;">Subcategoria</td>
                <td><%=consumible.get(11)%></td>
            </tr>
            <tr>
                <td style="font-weight: bold;">Clave</td>
                <td><%=consumible.get(1)%></td>
            </tr>
            <tr>
                <td style="font-weight: bold;">Descripción</td>
                <td><%=consumible.get(2)%></td>
            </tr>
            <tr>
                <td style="font-weight: bold;">Precio Actual</td>
                <td><%=consumible.get(8)%></td>
            </tr>
            <tr>
                <td style="font-weight: bold;">Medida</td>
                <td><%=consumible.get(13)%></td>
            </tr>
            <tr>
                <td style="font-weight: bold;">Existencias</td>
                <td><%=consumible.get(9)%></td>
            </tr>
        </table>
        <table>
            <tr style="background: #CCCCCC; text-align: center; font-weight: bold;">
                <td colspan="8">MOVIMIENTOS DE ENTRADA</td>
            </tr>
            <tr style="background: #CCCCCC; text-align: center; font-weight: bold;">
                <td>NP</td>
                <td>Folio</td>
                <td>Fecha</td>
                <td>No. Factura</td>
                <td>Proveedor</td>
                <td>Tipo de compra</td>
                <td>Cantidad</td>
                <td>Precio</td>
            </tr>
            <%
                int i = 1;
                while (t.hasNext()) {
                    LinkedList datos = (LinkedList) t.next();
            %>
            <tr>
                <td><%=i%></td>
                <td><%=datos.get(2)%></td>
                <td><%=datos.get(3)%></td>
                <td><%=datos.get(5)%></td>
                <td><%=datos.get(15)%></td>
                <td><%=datos.get(17)%></td>
                <td><%=datos.get(20)%></td>
                <td><%=datos.get(21)%></td>
            </tr>
            <%
                    i += 1;
                }
            %>
        </table>
        <table>
            <tr style="background: #CCCCCC; text-align: center; font-weight: bold;">
                <td colspan="6">MOVIMIENTOS DE SALIDA</td>
            </tr>
            <tr style="background: #CCCCCC; text-align: center; font-weight: bold;">
                <td>NP</td>
                <td>Folio</td>
                <td>Fecha</td>
                <td>Plantel</td>
                <td>Cantidad</td>
                <td>Precio</td>
            </tr>
            <%
                t = QUID.select_HistoriaSalida4Conumible(
                        WebUtil.decode(session, request.getParameter("idPlantel")),
                        WebUtil.decode(session, request.getParameter("idConsumible")),
                        "2", request.getParameter("fechaInicio"),
                        request.getParameter("fechaFin")).iterator();
                i = 1;
                while (t.hasNext()) {
                    LinkedList datos = (LinkedList) t.next();
            %>
            <tr>
                <td><%=i%></td>
                <td><%=datos.get(2)%></td>
                <td><%=datos.get(3)%></td>
                <td><%=datos.get(22)%></td>
                <td><%=datos.get(27)%></td>
                <td><%=datos.get(28)%></td>
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