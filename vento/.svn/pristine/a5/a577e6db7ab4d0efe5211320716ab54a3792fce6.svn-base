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
            access4ThisPage.add("ReporteConteoBien");

            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {

%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%            String date_num = "ConteoBienes.xls";
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
                    <br>TOTALES POR SUBCATEGORIA
                </td>
            </tr>
        </table>
        <table border="0" width="100%">
            <tr style="font-weight: bold; background-color: #CECECE;">
                <td>Plantel</td>
                <%
            String[] subcategorias = request.getParameterValues("idSubcategoria");
            int[] totales=new int[subcategorias.length+1];        
                    for (int i = 0; i < subcategorias.length; i++) {
                %>
                <td><%=subcategorias[i]%></td>
                <%
                    }
                %>
                <td>Total</td>
            </tr>
            <%
                Iterator t = QUID.select_Totales4Subcategoria(WebUtil.decode(session, request.getParameter("idPlantel")),
                        "Baja", subcategorias, false).iterator();
                while (t.hasNext()) {
                    LinkedList datos = (LinkedList) t.next();
            %>
            <tr>
                <td><%=datos.get(0)%></td>
                <%
                    for (int i = 0; i < subcategorias.length; i++) {
                        totales[i]+=Integer.parseInt(datos.get(i + 2).toString());
                %>
                <td><%=datos.get(i + 2)%></td>
                <%
                    }
                    totales[subcategorias.length]+=Integer.parseInt(datos.get(1).toString());
                %>
                <td><%=datos.get(1)%></td>
            </tr>
            <%
                }
            %>
            <tr>
                <td>TOTALES</td>
                <%
                for(int i=0; i<totales.length;i++){
                    %>
                    <td><%=totales[i]%></td>
                    <%
                }
                %>
            </tr>
        </table>
    </body> 
</html>
<%
            }else{
            out.print("Usted NO tiene permios para realizar esta acción");
            }
        }else{
            out.print("Petición invalida");
        }
    } catch (Exception ex) {
        out.print("Ocurrio un error");
        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
    }
%>