<%@page import="jspread.core.util.SystemUtil"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            //if (request.getParameter(WebUtil.encode(session, "imix")) == null) {
            LinkedList<String> access4ThisPage = new LinkedList();
            access4ThisPage.add("LoggedUser");

            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                if (PageParameters.getParameter("SiteOnMaintenance").equals("true")) {
                    String redirectURL = "" + PageParameters.getParameter("mainController") + "?exit=1";
                    response.sendRedirect(redirectURL);
                } else {
                    Iterator it = null;
                    LinkedList listAux = null;
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Menú Principal</title>
        <jsp:include page='<%=PageParameters.getParameter("globalLibs")%>'/>        

        <script type="text/javascript" language="javascript" charset="utf-8">
            window.history.forward();
            function noBack() {
                window.history.forward();
            }            
        </script>
    </head>
    <body>
        <div id="wrapper">
            <div id="divBody">
                <jsp:include page='<%=("" + PageParameters.getParameter("logo"))%>' />
                <div id="barMenu">
                    <jsp:include page='<%=(PageParameters.getParameter("barMenu"))%>' />
                </div>
                <%
                    if (PageParameters.getParameter("comunicado").equalsIgnoreCase("") == false) {
                %>
                <div class="errors">
                    <p>
                        <em>Comunicado: <%=PageParameters.getParameter("comunicado")%></em>
                    </p>
                </div> 
                <%}%>
                <p></p>
                <table width="100%" height="25px" border="0" align="center" cellpadding="0" cellspacing="0">
                    <tr>
                        <td width="64%" height="25" align="left" valign="top">
                            Menú Principal
                        </td>
                        <td width="36" align="right" valign="top">
                            <script language="JavaScript" src="<%=PageParameters.getParameter("jsRcs")%>/funcionDate.js" type="text/javascript"></script>
                        </td>
                    </tr>                        
                </table>
                <br>
                <br>
                <div id="overlay" class="overlay" ></div>
                <div id="floatBoxPuntuacion" class="floatBox" style=" overflow-y: scroll;height: 470px">
                    <div style="float:right;margin-right: 35px"><div class="closeButton2" onclick="closeDialogBox('floatBoxPuntuacion');" style="position:fixed;"></div></div>
                    <div style="text-align: left; font-size: x-small;">
                        <table class="tablaPuntuacion">
                            <tr>
                                <td colspan="4" style=" font-weight: bold; font-size: medium; text-align: center;"> Calificación del Plantel</td>
                            </tr>
                            <%
                                it = QUID.select_Puntuacion(session.getAttribute("FK_ID_Plantel").toString(), false).iterator();
                                Double puntuacionPromedio = -1.0;
                                int i = 0;
                                while (it.hasNext()) {
                                    LinkedList puntuacion = (LinkedList) it.next();
                            %>
                            <tr>
                                <td style="background: <%=SystemUtil.getColor4puntuacion(Double.parseDouble(puntuacion.get(4).toString()))%>; width:3%"></td>
                                <td><%=puntuacion.get(5)%></td>
                                <td><%=StringUtil.formatDouble1Decimals(Double.parseDouble(puntuacion.get(4).toString()))%></td>
                                <td><%=puntuacion.get(3)%></td>
                            </tr>
                            <%
                                    puntuacionPromedio += Double.parseDouble(puntuacion.get(4).toString());
                                    i += 1;
                                }

                                if (i > 0 && puntuacionPromedio != -1) {
                                    puntuacionPromedio = (puntuacionPromedio + 1) / i;
                                }
                            %>
                            <tr style=" font-weight: bold;">
                                <td style="background: <%=SystemUtil.getColor4puntuacion(puntuacionPromedio)%>;"></td>
                                <td>Puntuación General</td>
                                <td><%=puntuacionPromedio == -1 ? "Sin calificar" : StringUtil.formatDouble1Decimals(puntuacionPromedio)%></td>
                                <td></td>
                            </tr>
                        </table>
                                <div>
                                    <a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("excelReports")%>/CalificacionPlantel.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>" target="_blank"><input type="button" name="calificacionCompleto" value="Reporte Completo"></a>
                                </div>
                                <br>
                                <br>
                    </div>
                </div>
                <div class="puntuacionButton" style="background-color: <%=SystemUtil.getColor4puntuacion(puntuacionPromedio)%>;" onclick="openDialogBox('floatBoxPuntuacion');" title="Puntuación del Plantel">
                    <p style="text-align:center; vertical-align: middle; padding: 0px; margin-top: 9px; font-weight: bold; font-size: x-small;"><%=puntuacionPromedio == -1 ? "SC" : StringUtil.formatDouble1Decimals(puntuacionPromedio)%></p>
                </div>
                <br>
                <div>
                    <table width="200" border="0" align="center">
                        <tr>
                        </tr>
                        <tr>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaBien.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/consultaBienes.png" width="150" height="40"/></a></td>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaPersonal.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/catalogoPersonal.png" width="150" height="40"/></a></td>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/CentroOpcionesBien.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/CentroOpcionesBien.png" width="150" height="40"/></a></td>
                        </tr>
                        <tr>
                        </tr>
                        <tr>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaDepartamento_Plantel.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/catalogoDepartamento_Plantel.png" width="150" height="40"/></a></td>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaGrupo.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/catalogoGrupo.png" width="150" height="40"/></a></td>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaProveedor.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/catalogoProveedores.png" width="150" height="40"/></a></td>
                        </tr>
                        <tr>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaSoftware.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/catalogoSoftware.png" width="150" height="40"/></a></td>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaSolicitud.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/solicitud.png" width="150" height="40"/></a></td>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/MenuReportes.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/reportes.png" width="150" height="40"/></a></td>
                        </tr> 
                        <tr>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaBitacoraIncidente.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/bitacoraIncidente.png" width="150" height="40"/></a></td>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaResguardo.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/resguardo.png" width="150" height="40"/></a></td>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/MenuCatalogos.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/menuCatalogos.png" width="150" height="40"/></a></td>
                        </tr>
                        <tr>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/MenuConsumibles.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/MenuConsumibles.png" width="150" height="40"/></a></td>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaActividad.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/consultaActividades.png" width="150" height="40"/></a></td>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaSolicitudPlantel.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/consultaSolicitud.png" width="150" height="40"/></a></td>
                        </tr>
                        <tr>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaModelo.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/catalogoModelos.png" width="150" height="40"/></a></td>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaEnlace.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/conectividad.png" width="150" height="40"/></a></td>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaOrdenSurtimiento.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/ordenSurtido.png" width="150" height="40"/></a></td>
                        </tr>
                        <tr>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaOrdenSurtimientoPendiente.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/ordenSurtidoPendiente.png" width="150" height="40"/></a></td>
                        </tr>
                        <tr>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/UpdatePssUsuario.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/actualizarPass.png" width="150" height="40"/></a></td>
                            <td><a href="<%=PageParameters.getParameter("rsc")%>/downloads/manual.pdf" target="_blank"><img src="<%=PageParameters.getParameter("imgRsc")%>/guia.png" width="150" height="40"/></a></td>
                        </tr>
                        <%
                            if (session.getAttribute("tipoRol").toString().equalsIgnoreCase("root")
                                    || session.getAttribute("tipoRol").toString().equalsIgnoreCase("Administrador")) {
                        %>                        
                        <tr>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/UpdateComunicado.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/editarComunicado.png" width="150" height="40"/></a></td>                            
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/MenuSeguridad.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/opcionesSeguridad.png" width="150" height="40"/></a></td>
                            <td><a href="<%=PageParameters.getParameter("mainContext")%>/jspread/admin/SessionManager.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/sesionManger.png" width="150" height="40"/></a></td>                            
                        </tr>
                        <div id="divResult">
                        </div> 
                        <%}%>
                    </table>
                </div>
                <br>
                <br>                
                <div id="divFoot">
                    <jsp:include page='<%=(PageParameters.getParameter("footer"))%>' />
                </div> 
                <%
                    if (session.getAttribute("noticiaLeida") == null && PageParameters.getParameter("comunicado").equalsIgnoreCase("") == false) {
                        session.setAttribute("noticiaLeida", true);
                %>
                <jsp:include page='<%= PageParameters.getParameter("msgUtil") + "/msg.jsp"%>' flush = 'true'>
                    <jsp:param name='title' value='Comunicado - Noticia' />
                    <jsp:param name='msg' value='<%=PageParameters.getParameter("comunicado")%>' />
                    <jsp:param name='type' value='info' />
                </jsp:include>
                <%
                    }

                    int totalPendientes = QUID.select_CountActividad4Plantel(session.getAttribute("FK_ID_Plantel").toString(), "PENDIENTE");
                    if (totalPendientes > 0) {
                %>
                <jsp:include page='<%= PageParameters.getParameter("msgUtil") + "/msg.jsp"%>' flush = 'true'>
                    <jsp:param name='title' value='Atención' />
                    <jsp:param name='msg' value='<%="Usted tiene " + totalPendientes + " actividades pendientes"%>' />
                    <jsp:param name='type' value='alert' />
                </jsp:include>
                <%
                    }
                %>
            </div>            
        </div>
    </body>
</html>
<%
    }
} else {
    //System.out.println("Usuario No valido para esta pagina");
%>                
<%
    response.sendRedirect(PageParameters.getParameter("mainContext") + PageParameters.getParameter("LogInPage"));
%>
<%    }
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
