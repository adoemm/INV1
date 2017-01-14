<%@page import="jspread.core.util.SystemUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Calendar"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null ) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("ReporteResumenGeneral");
                Calendar cal = Calendar.getInstance();
                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                    Iterator it = null;
                    LinkedList listAux = null;
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Resumen</title>
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
                                <br><strong>Inventario de Bienes/Artículos</strong>
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
                        Iterator t = QUID.select_ResumenGeneral(WebUtil.decode(session, request.getParameter("idPlantel")),
                                SystemUtil.getDecodeArray(request.getParameterValues("idSubcategoria"), session),
                                "Baja",
                                false).iterator();
                    %>
                    <table width="92%" border="0" align="center" class="borde" rules="all" style="text-align: center;font-size: 10px;">
                        <tr class="celdaRellenoCenter">
                            <td>Plantel</td>
                            <td>Subcategoria</td>
                            <td>Marca</td>
                            <td>Modelo</td>
                            <td>No. de Inventario</td>
                            <td>No. de serie</td>
                            <td>Departamento</td>
                            <td>Tipo Compra</td>
                            <td>Vencimiento Garantía</td>
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
                            <td>Plantel</td>
                            <td>Subcategoria</td>
                            <td>Marca</td>
                            <td>Modelo</td>
                            <td>No. de Inventario</td>
                            <td>No. de serie</td>
                            <td>Departamento</td>
                            <td>Tipo Compra</td>
                            <td>Vencimiento Garantía</td>
                        </tr>
                        <%
                            }
                        %>
                        <tr>
                            <td><%=datos.get(0)%></td>
                            <td><%=datos.get(10)%></td>
                            <td><%=datos.get(3)%></td>
                            <td><%=datos.get(4)%></td>
                            <td><%=datos.get(6)%></td>
                            <td><%=datos.get(5)%></td>
                            <td><%=datos.get(7)%></td>
                            <td><%=datos.get(8)%></td>
                            <td><%=datos.get(9)%></td>
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
