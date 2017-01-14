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
        Iterator it = null;

        if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
            LinkedList<String> access4ThisPage = new LinkedList();
            access4ThisPage.add("ConsultaConsumible");

            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {

%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%            String date_num = "ConsultaConsumible4Plantel.xls";
            response.setContentType("application/vnd.ms-excel; encoding=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=\"" + date_num + "\"");
        %>
    </head>
    <body>
        <table border="0" width="100%">
            <tr style="background: #CCCCCC; font-weight: bold; font-size: 14px;">
                <th>Plantel</th>
                <th>Categoría</th>
                <th>Subcategoría</th>
                <th>Clave</th>
                <th>Desc.</th>
                <th>Unidad</th>
                <th>Estatus</th>
                <th>Costo Promedio $</th>
                <th>Existencias</th>
                <th>Inventario Valorizado $</th>
            </tr>
            <%
                Double totalArticulos = 0.0;
                Double inventarioValorizado = 0.0;
                String categoriaActual = "";
                Double totalArticulosXCategoria = 0.0;
                Double inventarioValorizadoXCategoria = 0.0;
                it = QUID.select_Consumible(WebUtil.decode(session, request.getParameter("idPlantel")), false, "Baja", false, true).iterator();
                while (it.hasNext()) {
                    LinkedList listAux = (LinkedList) it.next();
                    Double costoTotal = Double.parseDouble(listAux.get(9).toString()) * Double.parseDouble(listAux.get(8).toString());
                    if (!categoriaActual.equalsIgnoreCase(listAux.get(16).toString())) {
                        if (!categoriaActual.equals("")) {
            %>
            <tr style="background: #CCCCCC; font-weight: bold; font-size: 14px;">
                <td>TOTALES CATEGORÍA</td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td><%=totalArticulosXCategoria%></td>
                <td><%=inventarioValorizadoXCategoria%></td>
            </tr>
            <%
                    }
                    categoriaActual = listAux.get(16).toString();
                    totalArticulosXCategoria = 0.0;
                    inventarioValorizadoXCategoria = 0.0;

                }

            %>
            <tr>
                <td><%=listAux.get(15)%></td>
                <td><%=listAux.get(17)%></td>
                <td><%=listAux.get(11)%></td>
                <td><%=listAux.get(1)%></td>
                <td><%=listAux.get(2)%></td>
                <td><%=listAux.get(13)%></td>
                <td><%=listAux.get(3)%></td>
                <td><%=listAux.get(8)%></td>
                <td><%=listAux.get(9)%></td>
                <td><%=costoTotal%></td>
            </tr>
            <%
                    inventarioValorizado += costoTotal;
                    totalArticulos += Double.parseDouble(listAux.get(9).toString());
                    
                    totalArticulosXCategoria+= Double.parseDouble(listAux.get(9).toString());
                    inventarioValorizadoXCategoria += Double.parseDouble(listAux.get(9).toString()) * Double.parseDouble(listAux.get(8).toString());
                }
            %>
             <tr style="background: #CCCCCC; font-weight: bold; font-size: 14px;">
                <td>TOTALES CATEGORÍA</td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td><%=totalArticulosXCategoria%></td>
                <td><%=inventarioValorizadoXCategoria%></td>
            </tr>
            <tr style="background: #CCCCCC; font-weight: bold; font-size: 14px;">
                <td>TOTALES</td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td><%=totalArticulos%></td>
                <td><%=inventarioValorizado%></td>
            </tr>
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