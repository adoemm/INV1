<%@page import="jspread.core.util.SystemUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Calendar"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("printBitacoraincidente");
                Calendar cal = Calendar.getInstance();
                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                    Iterator it = null;
                    LinkedList listAux = null;
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Bitácora de Incidentes</title>
        <jsp:include page='<%=PageParameters.getParameter("globalLibs")%>'/>
        <script type="text/javascript" charset="utf-8">
            window.history.forward();
            function noBack() {
                window.history.forward();
            }
        </script>
    </head>
    <body style="background-color:#ffffff;">
        <div align="center" style="width: 28cm; height:25cm;  margin: 0 auto;">
            <div>
                <div align="center">
                    <%
                        String fechaInicio = request.getParameter("fechaInicio") != null && !request.getParameter("fechaInicio").equals("") ? request.getParameter("fechaInicio") : UTime.calendar2SQLDateFormat(Calendar.getInstance());
                        String fechaFin = request.getParameter("fechaFin") != null && !request.getParameter("fechaInicio").equals("") ? request.getParameter("fechaFin") : UTime.calendar2SQLDateFormat(Calendar.getInstance());
                        Iterator t = QUID.select_Bitacora_Incidente(WebUtil.decode(session, request.getParameter("idPlantel")), fechaInicio, fechaFin).iterator();
                    %>
                    <table width="92%" border="0" align="center" class="borde" rules="all" style="text-align: center;font-size: 13px;">
                        <tr>
                            <td width="15%">
                                <img src="<%=PageParameters.getParameter("rsc")%>/img/edomex.png"  align="left"  height="105px" width="160px">
                            </td>
                            <td ><strong>Colegio de Estudios Científicos y Tecnologicos del Estado de México</strong>
                                <br><strong>Unidad de Informática</strong>
                                <br><strong>Bitácora de Incidentes</strong>
                            </td>
                            <td width="15%">
                                <img src="<%=PageParameters.getParameter("rsc")%>/img/cecytem2.jpg"  align="center"  height="60px" width="50px">    
                            </td>
                        </tr>
                    </table>
                    <table width="92%" border="0" align="center" class="Borde" style="text-align: right;">
                        <tr>
                            <td width="85%" >FECHA:</td>
                            <td width="15%" ><%=UTime.calendar2SQLDateFormat(Calendar.getInstance())%></td>
                        </tr>
                    </table>
                    <br>
                    <table width="92%" class="onlyBordeOddChild" align="center" rules="all" style="font-size: 10px;">
                        <tr>
                            <td class="celdaRellenoCenter" style="width: 4%;">NP</td>
                            <td class="celdaRellenoCenter">Plantel</td>
                            <td class="celdaRellenoCenter">No. Reporte</td>
                            <td class="celdaRellenoCenter">Depto.</td>
                            <td class="celdaRellenoCenter">Marca</td>
                            <td class="celdaRellenoCenter">Modelo</td>
                            <td class="celdaRellenoCenter">No. Serie</td>
                            <td class="celdaRellenoCenter" style="width: 10%;">Incidente</td>
                            <td class="celdaRellenoCenter" style="width: 10%;">Acción</td>
                            <td class="celdaRellenoCenter" style="width: 10%;">Fecha Reporte</td>
                            <td class="celdaRellenoCenter" style="width: 10%;">Fecha Atención</td>
                            <td class="celdaRellenoCenter">Estatus</td>
                            <td class="celdaRellenoCenter" style="width: 13%;">Obs.</td>
                        </tr>
                        <%
                            int i = 1;
                            while (t.hasNext()) {
                                if (i % 10 == 0) {
                        %>
                    </table>
                    <br><br>
                    <table width="92%" border="0" align="center" style="text-align: center;">
                        <tr>
                            <td>&nbsp;</td>
                            <td width="50%">
                                _______________________________________________
                            </td>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td width="50%">
                                <input type="text" size="80" style="border: 0px; text-align: center;font-size: 12px;" value=" ">
                            </td>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td>
                                Nombre del Responsable
                            </td>
                            <td>&nbsp;</td>
                        </tr>
                    </table>
                    <p class="break"></p>
                    <table width="92%" border="0" align="center" class="borde" rules="all" style="text-align: center;font-size: 13px;">
                        <tr>
                            <td width="15%">
                                <img src="<%=PageParameters.getParameter("rsc")%>/img/edomex.png"  align="left"  height="105px" width="160px">
                            </td>
                            <td ><strong>Colegio de Estudios Científicos y Tecnologicos del Estado de México</strong>
                                <br><strong>Unidad de Informática</strong>
                                <br><strong>Bitácora de Incidentes</strong>
                            </td>
                            <td width="15%">
                                <img src="<%=PageParameters.getParameter("rsc")%>/img/cecytem2.jpg"  align="center"  height="60px" width="50px">    
                            </td>
                        </tr>
                    </table>
                    <table width="92%" border="0" align="center" class="Borde" style="text-align: right;">
                        <tr>
                            <td width="85%" >FECHA:</td>
                            <td width="15%" ><%=UTime.calendar2SQLDateFormat(Calendar.getInstance())%></td>
                        </tr>
                    </table>
                    <br>
                    <table width="92%" class="onlyBordeOddChild" align="center" rules="all" style="font-size: 10px;">
                        <tr>
                            <td class="celdaRellenoCenter" style="width: 4%;">NP</td>
                            <td class="celdaRellenoCenter">Plantel</td>
                            <td class="celdaRellenoCenter">No. Reporte</td>
                            <td class="celdaRellenoCenter">Depto.</td>
                            <td class="celdaRellenoCenter">Marca</td>
                            <td class="celdaRellenoCenter">Modelo</td>
                            <td class="celdaRellenoCenter">No. Serie</td>
                            <td class="celdaRellenoCenter" style="width: 10%;">Incidente</td>
                            <td class="celdaRellenoCenter" style="width: 10%;">Acción</td>
                            <td class="celdaRellenoCenter" style="width: 10%;">Fecha Reporte</td>
                            <td class="celdaRellenoCenter" style="width: 10%;">Fecha Atención</td>
                            <td class="celdaRellenoCenter">Estatus</td>
                            <td class="celdaRellenoCenter" style="width: 13%;">Obs.</td>
                        </tr>
                        <%
                            }
                            LinkedList datos = (LinkedList) t.next();
                        %>
                        <tr style="height: 50px;">
                            <td style="text-align: center;"><%=i%></td>
                            <td><%=datos.get(1)%></td>
                            <td><%=datos.get(8)%></td>
                            <td><%=datos.get(2)%></td>
                            <td><%=datos.get(6)%></td>
                            <td><%=datos.get(7)%></td>
                            <td><%=datos.get(4)%></td>
                            <td><%=datos.get(16)%></td>
                            <td><%=datos.get(12)%></td>
                            <td><%=datos.get(9)%></td>
                            <td><%=datos.get(10)%></td>
                            <td><%=datos.get(14)%></td>
                            <td><%=datos.get(11)%></td>
                        </tr>
                        <%
                                i += 1;

                            }
                        %>
                    </table>
                    <br><br>
                    <table width="92%" border="0" align="center" style="text-align: center;">
                        <tr>
                            <td>&nbsp;</td>
                            <td width="50%">
                                _______________________________________________
                            </td>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td width="50%">
                                <input type="text" size="80" style="border: 0px; text-align: center;font-size: 12px;" value=" ">
                            </td>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td>
                                Nombre del Responsable
                            </td>
                            <td>&nbsp;</td>
                        </tr>
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
