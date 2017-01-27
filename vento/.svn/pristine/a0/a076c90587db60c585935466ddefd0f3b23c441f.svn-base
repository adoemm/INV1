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
            access4ThisPage.add("DetalleDiferenciasConteoFisico");

            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {

%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%            String date_num = "DetalleDiferenciasConteoFisico.xls";
            response.setContentType("application/vnd.ms-excel; encoding=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=\"" + date_num + "\"");
        %>
    </head>
    <body>
        <%
            Iterator t = QUID.select_DetalleConteoFisicoConsumible(WebUtil.decode(session, request.getParameter("idConteoFisico"))).iterator();
        %>
        <table>
            <tr style="background-color: #CECECE; font-weight: bold;">
                <td>Plantel</td>
                <td>Categoría</td>
                <td>Subcategoría</td>
                <td>Clave</td>
                <td>Descripción</td>
                <td>Unidad</td>
                <td>Costo historico</td>
                <td>Conteo lógico</td>
                <td>Conteo fisico</td>
                <td>Valorizado lógico</td>
                <td>Valorizado fisico</td>
                <td>Diferencia</td>
                <td>Fecha toma de lectura</td>
            </tr>
            <%
                while (t.hasNext()) {
                    LinkedList datos = (LinkedList) t.next();
            %>
            <tr>
                <td><%=datos.get(12)%></td>
                <td><%=datos.get(0)%></td>
                <td><%=datos.get(1)%></td>
                <td><%=datos.get(2)%></td>
                <td><%=datos.get(3)%></td>
                <td><%=datos.get(4)%></td>
                <td><%=datos.get(5)%></td>
                <td><%=datos.get(6)%></td>
                <td><%=datos.get(7)%></td>
                <td><%=datos.get(8)%></td>
                <td><%=datos.get(9)%></td>
                <td><%=datos.get(10)%></td>
                <td><%=datos.get(11)%></td>
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