<%@page import="jspread.core.util.SystemUtil"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("ConsultaSolicitud");

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
                    , success: function(response) {
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
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaSolicitud.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>">Catálogo de Solicitudes</a> 
                                > Editar Solicitud
                            </td>
                            <td width="36" align="right" valign="top">
                                <script language="JavaScript" src="<%=PageParameters.getParameter("jsRcs")%>/funcionDate.js" type="text/javascript"></script>
                            </td>
                        </tr>                        
                    </table>
                    <form name="updateSolicitud" method="post" action="" enctype="application/x-www-form-urlencoded" id="updateSolicitud">
                        <input type="hidden" name="<%=WebUtil.encode(session, "imix")%>" value="<%=WebUtil.encode(session, UTime.getTimeMilis())%>"/>
                        <input type="hidden" name="FormFrom" value="updateSolicitud"/>
                        <input type="hidden" name="idSolicitud" value="<%=request.getParameter("idSolicitud")%>"/>
                        <fieldset>
                            <legend>Editar Solicitud</legend>
                            <%
                                LinkedList solicitud = QUID.select_Solicitud(WebUtil.decode(session, request.getParameter("idSolicitud")));
                            %>
                            <jsp:include page='<%=PageParameters.getParameter("ajaxFunctions") + "/getSelectPlantelSection.jsp"%>' flush = 'true'>
                                <jsp:param name='plantelActual' value='<%=WebUtil.encode(session, solicitud.get(8))%>' />
                            </jsp:include>
                            <div>
                                <label for="fechaSolicitud">*F. de solicitud (aaaa-mm-dd)</label>
                                <input type="text" class="w8em format-y-m-d divider-dash highlight-days-67" id="fechaSolicitud"   name="fechaSolicitud" value="<%=solicitud.get(2)%>" size="10" MAXLENGTH="10">
                            </div>
                            <div>
                                <label for="numeroOficio">*Número de oficio</label>
                                <input type="text" name="numeroOficio" id="numeroOficio" value="<%=solicitud.get(0)%>" size="50">
                            </div>
                            <div>
                                <label for="nombreSolicitante">*Nombre del Solicitante</label>
                                <input type="text" name="nombreSolicitante" id="nombreSolicitante" value="<%=solicitud.get(1)%>" size="50">
                            </div>
                            <div>
                                <label for="nombreResponsable">*Nombre del Responsable</label>
                                <input type="text" name="nombreResponsable" id="nombreResponsable" value="<%=solicitud.get(3)%>" size="50">
                            </div>
                            <div>
                                <label for="status">*Estatus</label>
                                <select name="status" id="status">
                                    <option value="<%=WebUtil.encode(session, solicitud.get(4))%>"><%=solicitud.get(4)%></option>
                                    <option value="<%=WebUtil.encode(session, "En espera")%>">En espera</option>
                                    <option value="<%=WebUtil.encode(session, "Atendida")%>">Atendido</option>
                                </select>
                            </div>
                            <div>
                                <label for="observaciones">Observaciones</label>
                                <textarea name="observaciones" id="observaciones" cols="25" rows="5"><%=solicitud.get(5).toString().trim()%></textarea>
                            </div>
                            <div id="botonEnviarDiv" >
                                <input type="button" value="Guardar" name="Enviar" onclick="enviarInfo(document.getElementById('updateSolicitud'));"/>
                            </div>      
                        </fieldset>
                    </form>
                    <a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaSolicitudBaja.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idSolicitud=<%=request.getParameter("idSolicitud")%>&idPlantel=<%=WebUtil.encode(session, solicitud.get(6))%>">
                        <button>Listado de Bienes</button>
                    </a>
                    <a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("prints")%>/solicitudBaja.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idSolicitud=<%=request.getParameter("idSolicitud")%>" target="_blank">
                        <button>Imprimir</button>
                    </a>
                    <%
                        if (!solicitud.get(4).toString().equalsIgnoreCase("Atendida")) {
                    %>
                    <a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/UpdateBaja.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idSolicitud=<%=request.getParameter("idSolicitud")%>&idPlantel=<%=WebUtil.encode(session, solicitud.get(6))%>">
                        <button>Confirmar Solicitud</button>
                    </a>
                    <%
                        }
                    %>

                    <div id="divResult"> 
                    </div>
                </div>   
                <div id="divFoot">
                    <jsp:include page='<%=(PageParameters.getParameter("footer"))%>' />
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