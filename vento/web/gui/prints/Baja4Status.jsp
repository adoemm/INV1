<%@page import="jspread.core.util.SystemUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Calendar"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("ReporteBaja4Estatus");
                Calendar cal = Calendar.getInstance();
                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                    Iterator it = null;
                    LinkedList listAux = null;
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Baja por Estatus</title>
        <jsp:include page='<%=PageParameters.getParameter("globalLibs")%>'/>
        <script type="text/javascript" charset="utf-8">
            window.history.forward();
            function noBack() {
                window.history.forward();
            }
        </script>
    </head>
    <body style="background-color:#ffffff;">
        <div align="center" style="width: 28cm; height: 25cm; margin: 0 auto;">
            <div>
                <div align="center">
                    <table width="92%" border="0" align="center" class="borde" rules="all" style="text-align: center;font-size: 13px;">
                        <tr>
                            <td width="15%">
                                <img src="<%=PageParameters.getParameter("rsc")%>/img/edomex.png"  align="left"  height="105px" width="160px">
                            </td>
                            <td ><strong>Colegio de Estudios Científicos y Tecnologicos del Estado de México</strong>
                                <br><strong>Unidad de Informática</strong>
                                <br><strong>Bienes/Artículos en Baja por Estatus</strong>
                            </td>
                            <td width="15%">
                                <img src="<%=PageParameters.getParameter("rsc")%>/img/cecytem2.jpg"  align="center"  height="60px" width="50px">    
                            </td>
                        </tr>
                    </table>
                    <table width="92%" border="0" align="center"  style="text-align: center;">
                        <tr >
                            <td width="85%" ></td>
                            <td width="15%" class="borde">FECHA:&nbsp;<%=UTime.calendar2SQLDateFormat(Calendar.getInstance())%></td>
                        </tr>
                    </table>
                    <%
                        String idPlantel=WebUtil.decode(session, request.getParameter("idPlantel"));
                        Iterator t = QUID.select_Bien4Subcategoria(
                                idPlantel,
                                idPlantel.equalsIgnoreCase("todos"),
                                "Baja",
                                true,
                                true,
                                SystemUtil.getDecodeArray(request.getParameterValues("idSubcategoria"), session)).iterator();
                    %>
                    <table width="92%" border="0" align="center" class="borde" rules="all" style="text-align: center;font-size: 10px;">
                        <tr class="celdaRellenoCenter">
                            <td>NP</td>
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
                            int fila = 1;
                            while (t.hasNext()) {
                                LinkedList datos = (LinkedList) t.next();
                                if (fila % 14 == 0) {
                        %>
                    </table>
                    <p class="break"></p>
                    <table width="92%" border="0" align="center" class="borde" rules="all" style="text-align: center;font-size: 13px;">
                        <tr>
                            <td width="15%">
                                <img src="<%=PageParameters.getParameter("rsc")%>/img/edomex.png"  align="left"  height="105px" width="160px">
                            </td>
                            <td ><strong>Colegio de Estudios Científicos y Tecnologicos del Estado de México</strong>
                                <br><strong>Unidad de Informática</strong>
                                <br><strong>Resumen de Bienes/Artículos</strong>
                            </td>
                            <td width="15%">
                                <img src="<%=PageParameters.getParameter("rsc")%>/img/cecytem2.jpg"  align="center"  height="60px" width="50px">    
                            </td>
                        </tr>
                    </table>
                    <table width="92%" border="0" align="center" style="text-align: right;">
                        <tr>
                            <td width="85%" >FECHA:</td>
                            <td width="15%" ><%=UTime.calendar2SQLDateFormat(Calendar.getInstance())%></td>
                        </tr>
                    </table>
                    <table width="92%" border="0" align="center" class="borde" rules="all" style="text-align: center;font-size: 10px;">
                        <tr class="celdaRellenoCenter">
                            <td>NP</td>
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
                            }
                        %>
                        <tr>
                            <td><%=fila%></td>
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
                                fila += 1;
                            }
                        %>
                    </table>
                    <p class="break"></p>
                </div>
            </div>
        </div>
    </body>
</html>
<%
} else {
    //System.out.println("Usuario No valido para esta pagina");
%>                
<%@ include file="/gui/pageComponents/invalidUser.jsp"%>
<%    }
} else {
    //System.out.println("No se ha encontrado a imix");
%>
<%@ include file="/gui/pageComponents/invalidParameter.jsp"%>
<%        }
    }
} catch (Exception ex) {
    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
%>
<%@ include file="/gui/pageComponents/handleUnExpectedError.jsp"%>
</body>
</html>
<%
        //response.sendRedirect(redirectURL);
    }
%>
