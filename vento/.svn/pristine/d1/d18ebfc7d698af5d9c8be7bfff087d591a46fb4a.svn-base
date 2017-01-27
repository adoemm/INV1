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
        <%
         LinkedList idSubcategoria=new LinkedList();
         String[] subcategorias=request.getParameterValues("idSubcategoria");
         String idPlantel=WebUtil.decode(session,request.getParameter("idPlantel"));
         Double valorInventario=0.0;
         for (int i = 0; i < subcategorias.length; i++) {
                 idSubcategoria.add(WebUtil.decode(session,subcategorias[i]));
             }
         Iterator t=QUID.select_ConsumibleNuloMovimiento(idPlantel, idSubcategoria).iterator();
         
        %>
        <table>
            <tr>
                <td>PLANTEL</td>
                <td>SUBCATEGORIA</td>
                <td>CLAVE</td>
                <td>DESC</td>
                <td>MEDIDA</td>
                <td>INVENTARIO INICIAL</td>
                <td>TOTAL ENTRADAS</td>
                <td>EXISTENCIAS + ENTRADAS</td>
                <td>TOTAL ENTREGADOS</td>
                <td>PRECIO</td>
                <td>INVENTARIO VALORIZADO</td>
            </tr>
            <%
            while(t.hasNext()){
                LinkedList datos=(LinkedList) t.next();
                Double totalXConsumible=Double.parseDouble(datos.get(8).toString())*Double.parseDouble(datos.get(9).toString());
                %>
                <tr>
                    <td><%=datos.get(15)%></td>
                <td><%=datos.get(11)%></td>
                <td><%=datos.get(1)%></td>
                <td><%=datos.get(2)%></td>
                <td><%=datos.get(13)%></td>
                <td><%=datos.get(9)%></td>
                <td>0</td>
                <td>0</td>
                <td>0</td>
                <td><%=datos.get(8)%></td>
                <td><%=totalXConsumible%></td>
                </tr>
                <%
                valorInventario+=totalXConsumible;
            }
            %>
            <tr style="background: #CCCCCC;font-size: 18px; text-align: right;">
                <td colspan="10">TOTAL</td>
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