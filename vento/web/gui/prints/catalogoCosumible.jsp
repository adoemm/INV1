<%@page import="jspread.core.util.SystemUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Calendar"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("ConsultaCatalogoConsumible");
                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Catálogo de Consumibles</title>
        <jsp:include page='<%=PageParameters.getParameter("globalLibs")%>'/>
        <script type="text/javascript" charset="utf-8">
            window.history.forward();
            function noBack() {
                window.history.forward();
            }
        </script>
    </head>
    <body style="background-color:#ffffff;">
        <div align="center" style="width: 25cm; height: 28cm; margin: 0 auto;">
            <div>
                <div align="center">
                    <%
                        String nombrePlantel = QUID.select_PlantelXCampo("nombre", WebUtil.decode(session, request.getParameter("idPlantel")));
                    %>
                    <table width="92%" border="0" align="center">
                        <tr>
                            <td align="left" width="7%">
                                <img src="<%=PageParameters.getParameter("rsc")%>/img/edomex.png"  align="left"  height="105px" width="160px">
                            </td>

                            <td class="estilo2" width="80%">
                                <p align="center">
                                    <strong>COLEGIO DE ESTUDIOS CIENTÍFICOS Y TECNOLÓGICOS DEL ESTADO DE MÉXICO</strong>
                                    <br><b><strong>PLANTEL <%=nombrePlantel.toUpperCase()%></strong></b>
                                    <br><b><strong>CATÁLOGO DE CONSUMIBLES</strong></b>
                                </p></td>
                            <td align="right" width="13%">
                                <img src="<%=PageParameters.getParameter("rsc")%>/img/cecytem2.jpg"  align="center"  height="60px" width="50px">    
                            </td>
                        </tr>
                    </table>
                    <br><br>
                    <%
                        Iterator it = it = QUID.select_Consumible(WebUtil.decode(session, request.getParameter("idPlantel")), false, "Baja", false, true).iterator();
                    %>
                    <div  class="firma" style="width:92%;text-align: right;">
                        Fecha de Consulta: <%=UTime.calendar2SQLDateFormat(Calendar.getInstance())%>
                    </div>
                    <table width="92%" border="0" align="center" class="onlyBorde" rules="all">
                        <tr  class="celdaRellenoCenter">
                            <td width="5%">NP</td>
                            <td width="15%">Plantel</td>
                            <td>Subcategoria</td>
                            <td style="text-align: left;" width="15%">Clave</td>
                            <td>Desc.</td>
                            <td>Unidad</td>
                            <td>Precio Actual</td>
                            <td>Estatus</td>
                        </tr>
                        <%
                            int i = 1;
                            while (it.hasNext()) {
                                LinkedList datos = (LinkedList) it.next();
                                if (i % 20 == 0) {
                        %>
                    </table>
                    <br><br>
                    <table border="0" width="92%" style="text-align: center;" class="firma">
                        <tr>
                            <td>_______________________________________</td>
                        </tr>
                        <tr>
                            <td>Elaboró</td>
                        </tr>
                    </table>
                    <p class="break"></p>
                    <table width="92%" border="0" align="center">
                        <tr>
                            <td align="left" width="7%">
                                <img src="<%=PageParameters.getParameter("rsc")%>/img/edomex.png"  align="left"  height="105px" width="160px">
                            </td>
                            <td class="estilo2" width="80%">
                                <p align="center">
                                    <strong>COLEGIO DE ESTUDIOS CIENTÍFICOS Y TECNOLÓGICOS DEL ESTADO DE MÉXICO</strong>
                                    <br><b><strong>PLANTEL <%=nombrePlantel.toUpperCase()%></strong></b>
                                    <br><b><strong>CATÁLOGO DE CONSUMIBLES</strong></b>
                                </p></td>
                            <td align="right" width="13%">
                                <img src="<%=PageParameters.getParameter("rsc")%>/img/cecytem2.jpg"  align="center"  height="60px" width="50px">    
                            </td>
                        </tr>
                    </table>
                    <br><br>
                    <div  class="firma" style="width:92%;text-align: right;">
                        Fecha de Consulta: <%=UTime.calendar2SQLDateFormat(Calendar.getInstance())%>
                    </div>
                    <table width="92%" border="0" align="center" class="onlyBorde" rules="all">
                        <tr  class="celdaRellenoCenter">
                            <td>NP</td>
                            <td>Plantel</td>
                            <td>Subcategoria</td>
                            <td style="text-align: left;">Clave</td>
                            <td>Desc.</td>
                            <td>Unidad</td>
                            <td>Precio Actual</td>
                            <td>Estatus</td>
                        </tr>
                        <%
                            }
                        %>
                        <tr>
                            <td style="text-align: center;"><%=i%></td>
                            <td><%=datos.get(15)%></td>
                            <td><%=datos.get(11)%></td>
                            <td><%=datos.get(1)%></td>
                            <td><%=datos.get(2)%></td>
                            <td><%=datos.get(13)%></td>
                            <td style="text-align: right;">$ <%=datos.get(8)%></td>
                            <td><%=datos.get(3)%></td>
                        </tr>
                        <%
                                i += 1;
                            }
                        %> 
                    </table>
                    <br><br>
                    <table border="0" width="92%" style="text-align: center;" class="firma">
                        <tr>
                            <td>_______________________________________</td>
                        </tr>
                        <tr>
                            <td>Elaboró</td>
                        </tr>
                    </table>
                    <p class="break"></p>
                </div>
            </div>
        </div>
    </body>
</html>
<%} else {
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
