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
            access4ThisPage.add("ReporteBaja4Estatus");

            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {

%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%            String date_num = "BajaXEstatus.xls";
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
                    <br>BAJA POR ESTATUS
                </td>
            </tr>
        </table>
        <table border="0" width="100%">
            <tr style="font-weight: bold;background-color: #CECECE;">
                <td>Plantel</td>
                <td>Subcategoria</td>
                <td>Marca</td>
                <td>Modelo</td>
                <td>No. de Inventario</td>
                <td>No. de serie</td>
                <td>Fecha Compra</td>
                <td>Estatus</td>               
            </tr>
            <%
                String idPlantel = WebUtil.decode(session, request.getParameter("idPlantel"));
                Iterator t = QUID.select_Bien4Subcategoria(
                        idPlantel,
                        idPlantel.equalsIgnoreCase("todos"),
                        "Baja",
                        true,
                        true,
                        SystemUtil.getDecodeArray(request.getParameterValues("idSubcategoria"), session)).iterator();
                while (t.hasNext()) {
                    LinkedList datos = (LinkedList) t.next();
            %>
            <tr>
                <td><%=datos.get(1)%></td>
                <td><%=datos.get(5)%></td>
                <td><%=datos.get(7)%></td>
                <td><%=datos.get(9)%></td>
                <td><%=datos.get(15)%></td>
                <td><%=datos.get(12)%></td>
                <td><%=datos.get(16)%></td>
                <td><%=datos.get(18)%></td>
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