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
            access4ThisPage.add("DiferenciasConteoFisico");

            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {

%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%            String date_num = "DiferenciasConteoFisico.xls";
            response.setContentType("application/vnd.ms-excel; encoding=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=\"" + date_num + "\"");
        %>
    </head>
    <body>

        <%
            LinkedList conteoFisico = QUID.select_ConteoFisico(WebUtil.decode(session, request.getParameter("idConteoFisico")));

        %>
        <table>
            <tr style="background-color: #CECECE; font-weight: bold;">
                <td>PLANTEL</td>
                <td>FECHA DE REGISTRO</td>
                <td>ESTATUS</td>
                <td>ULTIMA MODIFICACIÓN</td>
            </tr>
            <tr>
                <td><%=conteoFisico.get(5)%></td>
                <td><%=conteoFisico.get(2)%></td>
                <td><%=conteoFisico.get(0)%></td>
                <td><%=conteoFisico.get(1)%></td>
            </tr>
            <tr><td colspan="4"></td></tr>
        </table>
        <%
            Iterator t = QUID.select_DiferenciaConteoFisico(WebUtil.decode(session, request.getParameter("idPlantel")),
                    WebUtil.decode(session, request.getParameter("idConteoFisico"))).iterator();
        %>
        <table>
            <tr style="background-color: #CECECE; font-weight: bold;">
                <td>NP</td>
                <td>CATEGORÍA</td>
                <td>CONTEO FISICO</td>
                <td>CONTEO VALORIZADO</td>
                <td>DIFERENCIA</td>
            </tr>
            <%
                int i = 1;
                double totalFisico = 0.0;
                double totalValorizado = 0.0;
                double totalDiferencia = 0.0;
                while (t.hasNext()) {
                    LinkedList datos = (LinkedList) t.next();
            %>
            <tr>
                <td><%=i%></td>
                <td><%=datos.get(0)%></td>
                <td>$ <%=datos.get(2)%></td>
                <td>$ <%=datos.get(1)%></td>
                <td>$ <%=datos.get(3)%></td>
            </tr>
            <%
                    i += 1;
                    totalFisico += Double.parseDouble(datos.get(2).toString());
                    totalValorizado += Double.parseDouble(datos.get(1).toString());
                    totalDiferencia += Double.parseDouble(datos.get(3).toString());
                }
            %>
            <tr style="background-color: #CECECE; font-weight: bold;">
                <td>TOTALES</td>
                <td></td>
                <td>$ <%=totalFisico%></td>
                <td>$ <%=totalValorizado%></td>
                <td>$ <%=totalDiferencia%></td>
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