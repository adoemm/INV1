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
        <%            String date_num = "Conteo4Departamento.xls";
            response.setContentType("application/vnd.ms-excel; encoding=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=\"" + date_num + "\"");
        %>
    </head>
    <body>
        <%
            LinkedList deptos = new LinkedList();
            for (int i = 0; i < request.getParameterValues("idDepartamento").length; i++) {
                deptos.add(WebUtil.decode(session, request.getParameterValues("idDepartamento")[i]));
            }
            LinkedList subcategorias = new LinkedList();
            for (int i = 0; i < request.getParameterValues("idSubcategoria").length; i++) {
                subcategorias.add(WebUtil.decode(session, request.getParameterValues("idSubcategoria")[i]));
            }
            String idPlantel = WebUtil.decode(session, request.getParameter("idPlantel"));
            LinkedList modelos = QUID.select_Modelo4Plantel(idPlantel, subcategorias);
            Iterator recordsConteo = QUID.select_ConteoXDeparatmentos(idPlantel, modelos, deptos, subcategorias).iterator();
            int[] totales = new int[modelos.size()];

        %>
        <table style="font-weight: bold;">
            <tr>
                <td><div class="logo"></div></td>
                <td colspan="9">COLEGIO DE ESTUDIOS CIENTÍFICOS Y TECNOLÓGICOS DEL ESTADO DE MÉXICO
                    <br>UNIDAD DE INFORMÁTICA
                    <br>INVENTARIO DE BIENES INFORMÁTICOS
                    <br>CONTEO POR DEPARTAMENTO
                </td>
            </tr>
        </table>
        <table>
            <tr style="background-color: #CCCCCC;font-weight: bold;">
                <td>PLANTEL</td>
                <td>DEPARTAMENTO</td>
                <%                for (int i = 0; i < modelos.size(); i++) {
                %>
                <td><%=modelos.get(i)%></td>
                <%
                    }
                %>
                <td style="background: #FFFF00;">TOTAL</td>
            </tr>
            <%
                while (recordsConteo.hasNext()) {
                    LinkedList datos = (LinkedList) recordsConteo.next();
                    int sumaFila = 0;
            %>
            <tr>
                <td><%=datos.get(0)%></td>
                <td><%=datos.get(1)%></td>
                <%
                    for (int i = 2; i < datos.size(); i++) {
                        totales[i - 2] += Integer.parseInt(datos.get(i).toString());
                        sumaFila += Integer.parseInt(datos.get(i).toString());
                %>
                <td <%=Integer.parseInt(datos.get(i).toString()) != 0 ? "style=\" background: #ECECEC;font-weight: bold;\"" : ""%>><%=datos.get(i)%></td>
                <%
                    }
                %>
                <td style="background: #FFFF00;"><%=sumaFila%></td>
            </tr>
            <%
                }
            %>
            <tr style="background: #FFFF00;">
                <td>TOTAL</td>
                <td></td>
                <%
                    int suma = 0;
                    for (int i = 0; i < totales.length; i++) {
                %>
                <td><%=totales[i]%></td>
                <%
                        suma += totales[i];
                    }
                %>
                <td><%=suma%></td>
            </tr>
            <tr></tr>
            <tr style="background: #CCCCCC;font-weight: bold;">
                <td colspan="5" style="text-align: center;">DETALLES DE LOS MODELOS</td>
            </tr>
            <tr style="background: #CCCCCC;font-weight: bold;">
                <td colspan="5" style="text-align: center;">Nota: Los detalles presentados dependen de la información capturada por los usuarios.</td>
            </tr>
            <%
                LinkedList idModelos = QUID.select_ID_Modelo4Plantel(idPlantel, subcategorias);
                LinkedList detalles = new LinkedList();
                detalles.add("DISCO DURO (HDD)");
                detalles.add("MEMORIA RAM");
                detalles.add("PROCESADOR");
                Iterator tr = QUID.select_Detalles4Modelo(idModelos, detalles, idPlantel, subcategorias).iterator();
            %>
            <tr style="background: #CCCCCC;font-weight: bold;">
                <td>Subcategoria</td>
                <td>Marca</td>
                <td>Modelo</td>
                <td>DISCO DURO (HDD)</td>
                <td>MEMORIA RAM</td>
                <td>PROCESADOR</td>
                <td>TOTALES</td>
            </tr>
            <%
                int i = 0;
                while (tr.hasNext()) {
                    LinkedList datos = (LinkedList) tr.next();
            %>
            <tr>
                <td><%=datos.get(0)%></td>
                <td><%=datos.get(1)%></td>
                <td><%=datos.get(2)%></td>
                <td><%=datos.get(3)%></td>
                <td><%=datos.get(4)%></td>
                <td><%=datos.get(5)%></td>
                <%
                    if (i < totales.length) {
                %>
                <td><%=totales[i]%></td>
                <%
                } else {
                %>
                <td>---</td>
                <%
                    }
                %>
            </tr>
            <%
                    i += 1;

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