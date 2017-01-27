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
        <%
            
            int noModelos = 0;
            String[] modelos = request.getParameterValues("idModelo");
        %>
        <table style="font-weight: bold;">
            <tr>
                <td><div class="logo"></div></td>
                <td colspan="9">COLEGIO DE ESTUDIOS CIENTÍFICOS Y TECNOLÓGICOS DEL ESTADO DE MÉXICO
                    <br>UNIDAD DE INFORMÁTICA
                    <br>INVENTARIO DE BIENES INFORMÁTICOS
                    <br>TOTALES POR MODELO
                </td>
            </tr>
        </table>
        <table border="0" width="100%">
            <tr style="font-weight: bold;background-color: #CECECE;">
                <td>Plantel</td>
                <%
                    for (int i = 0; i < modelos.length; i++) {
                        LinkedList datosModelo = QUID.select_Modelo4ID(WebUtil.decode(session, modelos[i]));
                %>
                <td ><%=datosModelo.get(2) + " " + datosModelo.get(5)%><span style="font-size: 8px;">(<%=datosModelo.get(4)%>)</span></td>
                <%
                    }
                %>
                <td style="background: #CCCCCC;">Total</td>
            </tr>
            <%                Iterator t = null;
                int totalXmodelo = 0;
                int[] totales = new int[modelos.length + 1];
                if (WebUtil.decode(session, request.getParameter("idPlantel")).equalsIgnoreCase("todos")) {
                    t = QUID.select_Planteles("", true).iterator();
                } else {
                    t = QUID.select_Planteles(WebUtil.decode(session, request.getParameter("idPlantel")), false).iterator();
                }
                while (t.hasNext()) {
                    LinkedList plantel = (LinkedList) t.next();
                    int totalBienesPlantel = 0;
            %>
            <tr>
                <td><%=plantel.get(1)%></td>
                <%
                    noModelos = 0;
                    for (int i = 0; i < modelos.length; i++) {
                            totalXmodelo = QUID.select_Totales4Modelo(plantel.get(0).toString(),
                                    "Baja",
                                    WebUtil.decode(session, modelos[i]),
                                    SystemUtil.getDecodeArray(request.getParameterValues("idSubcategoria"), session),
                                    false);
                            totalBienesPlantel += totalXmodelo;
                            totales[noModelos] += totalXmodelo;
                            noModelos += 1;


                %>
                <td <%
                            if(totalXmodelo!=0){
                                %>
                                style="background: #FFBD69;"
                                <%
                            }
                %>><%=totalXmodelo%></td>
                <%

                        
                    }
                    totales[totales.length - 1] += totalBienesPlantel;
                %>
                <td style="background: #CCCCCC;"><%=totalBienesPlantel%></td>
            </tr>
            <%
                }
            %>
            <tr style="background: #CCCCCC;">
                <td>Totales</td>
                <%
                    for (int i = 0; i < totales.length; i++) {
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
            } else {
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