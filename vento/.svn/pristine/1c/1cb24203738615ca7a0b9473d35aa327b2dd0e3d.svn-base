<%@page import="jspread.core.util.SystemUtil"%>
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
            access4ThisPage.add("ReporteNuloMovimiento");

            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {

%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%            String date_num = "NuloMovimiento.xls";
            response.setContentType("application/vnd.ms-excel; encoding=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=\"" + date_num + "\"");
        %>
    </head>
    <body>
        <table class="tablaPuntuacion">
            <tr style="font-weight: bold;">
                <td></td>
                <td>Plantel</td>
                <td>Rubro</td>
                <td>Calificación</td>
                <td>Observaciones</td>
            </tr>
            <%
                Iterator it = QUID.select_Puntuacion(session.getAttribute("FK_ID_Plantel").toString(), true).iterator();
                Double puntuacionPromedio = -1.0;
                int i = 0;
                int conteo=0;
                String idPlantel = "";
                while (it.hasNext()) {
                    LinkedList puntuacion = (LinkedList) it.next();
                    if (i > 0 && !idPlantel.equals(puntuacion.get(7))) {
                        if (conteo > 0 && puntuacionPromedio != -1) {
                            puntuacionPromedio = (puntuacionPromedio + 1) / conteo;
                            conteo=0;
                        }
            %>

            <tr style=" font-weight: bold; background-color:#CCCCCC;">
                <td style="background: <%=SystemUtil.getColor4puntuacion(puntuacionPromedio)%>;"></td>
                <td>Puntuación General</td>
                <td></td>
                <td><%=puntuacionPromedio == -1 ? "Sin calificar" : StringUtil.formatDouble1Decimals(puntuacionPromedio)%></td>
                <td></td>
            </tr>
            <tr></tr>
            <%
                }
            %>
            <tr>
                <td style="background: <%=SystemUtil.getColor4puntuacion(Double.parseDouble(puntuacion.get(4).toString()))%>; width:3%"></td>
                <td><%=puntuacion.get(8)%></td>
                <td><%=puntuacion.get(5)%></td>
                <td><%=StringUtil.formatDouble1Decimals(Double.parseDouble(puntuacion.get(4).toString()))%></td>
                <td><%=puntuacion.get(3)%></td>
            </tr>
            <%
                    puntuacionPromedio += Double.parseDouble(puntuacion.get(4).toString());
                    i += 1;
                    conteo+=1;
                    idPlantel = puntuacion.get(7).toString();
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