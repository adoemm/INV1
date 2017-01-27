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
            access4ThisPage.add("MismoModeloDifPatron");

            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {

%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%            String date_num = "mismoModeloDiferentePatron.xls";
            response.setContentType("application/vnd.ms-excel; encoding=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=\"" + date_num + "\"");
        %>
    </head>
    <body>
        <table style="font-weight: bold;">
            <tr>
                <td><div class="logo"></div></td>
                <td colspan="9">COLEGIO DE ESTUDIOS CIENTÍFICOS Y TECNOLÓGICOS DEL ESTADO DE MÉXICO
                    <br>UNIDAD DE INFORMÁTICA
                    <br>INVENTARIO DE BIENES INFORMÁTICOS
                    <br>BIENES MISMO MODELO DIFERENTE PATRON DE NÚMERO DE SERIE
                </td>
            </tr>
        </table>
        <%
            LinkedList patrones = QUID.select_PatronParticularXModelo(WebUtil.decode(session, request.getParameter("idModelo")));
            LinkedList datosModelo = QUID.select_Modelo(WebUtil.decode(session, request.getParameter("idModelo")));

        %>

        <table>
            <tr><td style="font-weight: bold;">MODELO SELECCIONADO</td></tr>
            <tr><td><%=datosModelo.get(2) + " " + datosModelo.get(5)%></td></tr>
            <tr><td  style="font-weight: bold;">PATRONES</td></tr>
            <%                for (int i = 0; i < patrones.size(); i++) {
            %>
            <tr><td><%=patrones.get(i)%></td></tr>
            <%
                }
            %>
        </table>
        <%
            if (!patrones.isEmpty()) {
                Iterator t = QUID.select_MismoModeloDiferentePatron(WebUtil.decode(session, request.getParameter("idModelo")),
                        patrones,
                        WebUtil.decode(session, request.getParameter("idPlantel"))).iterator();
        %>
        <table>
            <tr  style="font-weight: bold;">
                <td>ID Bien</td>
                <td>Plantel</td>
                <td>Marca</td>
                <td>Modelo</td>
                <td>Categoria</td>
                <td>SubCategoria</td>
                <td>No Inventario</td>
                <td>No Serie</td>

            </tr>
            <%                while (t.hasNext()) {
                    LinkedList datos = (LinkedList) t.next();

            %>
            <tr>
                <td><%=datos.get(0)%></td>
                <td><%=datos.get(1)%></td>
                <td><%=datos.get(2)%></td>
                <td><%=datos.get(3)%></td>
                <td><%=datos.get(4)%></td>
                <td><%=datos.get(5)%></td>
                <td><%=datos.get(6)%></td>
                <td><%=datos.get(7)%></td>
            </tr>
            <%
                }
            %>
        </table>
        <%
            }
        %>
    </body> 
</html>
<%
            }
        }
    } catch (Exception ex) {
        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
    }
%>