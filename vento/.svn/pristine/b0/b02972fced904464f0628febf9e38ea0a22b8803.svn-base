<%@page import="jspread.core.util.SystemUtil"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("ConsultaSolicitudPlantel");

                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                    Iterator it = null;
                    LinkedList listAux = null;
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Editar Solicitud</title>
        <jsp:include page='<%=PageParameters.getParameter("globalLibs")%>'/>        
        <jsp:include page='<%=PageParameters.getParameter("styleFormCorrections")%>'/>   
        <jsp:include page='<%=PageParameters.getParameter("ajaxFunctions") + "/tablaCapturaCSS.jsp"%>'/>
        <script type="text/javascript" language="javascript" charset="utf-8">
            function enviarInfo(form) {
                $.ajax({type: 'POST'
                    , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                    , cache: false
                    , async: false
                    , url: '<%=PageParameters.getParameter("mainController")%>'
                    , data: $(form).serialize()
                    , success: function (response) {
                        $('#wrapper').find('#divResult').html(response);
                    }});
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
                <div class="errors">
                    <p>
                        <em>Los campos con  <strong>*</strong> son necesarios.</em>
                    </p>
                </div>
                <div class="form-container" width="100%">                    
                    <p></p>
                    <table width="100%" height="25px" border="0" align="center" cellpadding="0" cellspacing="0">
                        <tr>
                            <td width="64%" height="25" align="left" valign="top">
                                <a class="NVL" href="<%=PageParameters.getParameter("mainMenu")%>"> Menú Principal</a>
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaSolicitudPlantel.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%><%=request.getParameter("idPlantel") != null ? "&idPlantel=" + request.getParameter("idPlantel") : ""%>">Solicitudes</a> 
                                > Editar Solicitud
                            </td>
                            <td width="36" align="right" valign="top">
                                <script language="JavaScript" src="<%=PageParameters.getParameter("jsRcs")%>/funcionDate.js" type="text/javascript"></script>
                            </td>
                        </tr>                        
                    </table>
                    <form name="updateSolicitudPlantel" method="post" action="" enctype="application/x-www-form-urlencoded" id="updateSolicitudPlantel">
                        <input type="hidden" name="<%=WebUtil.encode(session, "imix")%>" value="<%=WebUtil.encode(session, UTime.getTimeMilis())%>"/>
                        <input type="hidden" name="FormFrom" value="updateSolicitudPlantel"/>
                        <input type="hidden" name="idSolicitud" value="<%=request.getParameter("idSolicitud")%>"/>
                        <fieldset>
                            <legend>Editar Solicitud</legend>
                            <%
                                if (session.getAttribute("tipoRol").toString().equalsIgnoreCase("Administrador")) {
                                    QUID.update_EstatusSolicitudPlantel("PENDIENTE", WebUtil.decode(session, request.getParameter("idSolicitud")));
                                }
                                LinkedList solicitud = QUID.select_SolicitudPlantel(WebUtil.decode(session, request.getParameter("idSolicitud")));
                            %>
                            <jsp:include page='<%=PageParameters.getParameter("ajaxFunctions") + "/getSelectPlantelSection.jsp"%>' flush = 'true'>
                                <jsp:param name='plantelActual' value='<%=WebUtil.encode(session, solicitud.get(0))%>' />
                            </jsp:include>
                            <div>
                                <label for="numeroOficio">*Número de Oficio</label>
                                <input type="text" name="numeroOficio" size="38" value="<%=solicitud.get(2)%>">
                            </div>
                            <div>
                                <label for="fechaSolicitud">*Fecha(aaaa-mm-dd)</label>
                                <input type="text" class="w8em format-y-m-d divider-dash highlight-days-67" id="fechaSolicitud"   name="fechaSolicitud" value="<%=solicitud.get(3)%>" size="10" MAXLENGTH="10">
                            </div>
                            <div>
                                <label for="asunto">*Asunto</label>
                                <input type="text" name="asunto" size="38" value="<%=solicitud.get(6)%>">
                            </div>
                            <div>
                                <label for="solicitud">*Descripción de la solicitud</label>
                                <textarea  id="descripcion" name="solicitud" cols="30" rows="5"><%=solicitud.get(7)%></textarea>
                            </div>
                            <div>
                                <label for="justificacion">Justificación</label>
                                <textarea  id="descripcion" name="justificacion" cols="30" rows="5"><%=solicitud.get(8)%></textarea>
                            </div>
                            <div>
                                <label for="observaciones">*Observaciones</label>
                                <textarea  id="descripcion" name="observaciones" cols="30" rows="5"><%=solicitud.get(5).toString().trim()%></textarea>
                            </div>
                            <div>
                                <label for="estatus">*Estatus</label>
                                <select name="estatus">
                                    <option value=""></option>
                                    <option value="<%=WebUtil.encode(session, solicitud.get(4))%>" selected><%=solicitud.get(4)%></option>
                                    <option value="<%=WebUtil.encode(session, "PENDIENTE")%>">PENDIENTE</option>
                                    <option value="<%=WebUtil.encode(session, "ATENDIDO")%>">ATENDIDO</option>
                                </select>
                            </div>
                            <div>
                                <label for="fechaCreacion">Fecha de Registro(aaaa-mm-dd)</label>
                                <input type="text" class="w8em format-y-m-d divider-dash highlight-days-67" id="fechaCreacion"   name="fechaCreacion" value="<%=solicitud.get(11)%>" size="10" MAXLENGTH="10">
                            </div>
                            <div>
                                <label for="horaFin">Hora de Registro (HH:MM:SS)</label>    
                                <input type="time" name="horaFin" id="horaFin" value="<%=solicitud.get(10)%>">    
                            </div>
                            <div id="botonEnviarDiv" >
                                <input type="button" value="Guardar" name="Enviar" onclick="enviarInfo(document.getElementById('updateSolicitudPlantel'));"/>
                            </div> 

                        </fieldset>
                    </form>
                    <div id="divResult"> 
                    </div>
                </div>   
                <div id="divFoot">
                    <jsp:include page='<%=(PageParameters.getParameter("footer"))%>' />
                </div> 
                <%
                int totalPendientes = QUID.select_CountActividad4Plantel(WebUtil.decode(session,request.getParameter("idPlantel")),"PENDIENTE");
                    if (totalPendientes > 0) {
                %>
                <jsp:include page='<%= PageParameters.getParameter("msgUtil") + "/msg.jsp"%>' flush = 'true'>
                    <jsp:param name='title' value='Atención' />
                    <jsp:param name='msg' value='<%="El plantel tiene " + totalPendientes + " actividades pendientes"%>' />
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