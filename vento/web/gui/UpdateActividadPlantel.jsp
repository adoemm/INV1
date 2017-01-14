<%@page import="jspread.core.util.SystemUtil"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("UpdateActividadPlantel");

                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                    Iterator it = null;
                    LinkedList listAux = null;
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Editar Actividad</title>
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
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaActividad.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%><%=request.getParameter("idPlantel") != null ? "&idPlantel=" + request.getParameter("idPlantel") : ""%>">Catálogo de Actividades</a> 
                                > Editar Actividad
                            </td>
                            <td width="36" align="right" valign="top">
                                <script language="JavaScript" src="<%=PageParameters.getParameter("jsRcs")%>/funcionDate.js" type="text/javascript"></script>
                            </td>
                        </tr>                        
                    </table>
                    <form name="updateActividad" method="post" action="" enctype="application/x-www-form-urlencoded" id="updateActividad">
                        <input type="hidden" name="<%=WebUtil.encode(session, "imix")%>" value="<%=WebUtil.encode(session, UTime.getTimeMilis())%>"/>
                        <input type="hidden" name="FormFrom" value="updateActividad"/>
                        <input type="hidden" name="idActividad" value="<%=request.getParameter("idActividad")%>"/>
                        <input type="hidden" name="idPlantel" value="<%=request.getParameter("idPlantel")%>"/>
                        <%
                            LinkedList actividad = QUID.select_ActividadPlantel(WebUtil.decode(session, request.getParameter("idActividadPlantel")));
                        %>
                        <fieldset>
                            <div>
                                <label for="idTipoActividad">*Tipo de Actividad</label>
                                <%
                                Iterator t=QUID.select_TipoActividad().iterator();
                                %>
                                <select name="idTipoActividad"> 
                                    <option value="<%=WebUtil.encode(session,actividad.get(22))%>"><%=actividad.get(23)%></option>
                                    <%
                                    while(t.hasNext()){
                                        LinkedList datos=(LinkedList) t.next();
                                        %>
                                        <option value="<%=WebUtil.encode(session,datos.get(0))%>"><%=datos.get(1)%></option>
                                        <%                                       
                                    }
                                    %>
                                </select>
                            </div>
                            <legend>Editar Actividad</legend>
                            <div>
                                <label>*Descripción</label>
                                <textarea  name="descripcion" cols="30" rows="5"><%=actividad.get(1)%></textarea>
                            </div>
                            <div>
                                <label>*Fecha de Inicio</label>
                                <input type="text" class="w8em format-y-m-d divider-dash highlight-days-67" id="fechaInicio"   name="fechaInicio" value="<%=actividad.get(3)%>" size="10" MAXLENGTH="10">
                            </div>
                            <div>
                                <label>Hora de Inicio</label>
                                <input type="time" name="horaInicio" id="horaInicio" value="<%=actividad.get(7)%>">    
                            </div>
                            <div>
                                <label>*Fecha de Termino</label>
                                <input type="text" class="w8em format-y-m-d divider-dash highlight-days-67" id="fechaFin"   name="fechaFin" value="<%=actividad.get(4)%>" size="10" MAXLENGTH="10">
                            </div>
                            <div>
                                <label>Hora de Termino</label>
                                <input type="time" name="horaFin" id="horaFin" value="<%=actividad.get(8)%>">    
                            </div>
                            <div>
                                <label>Fecha Limite</label>
                                <input type="text" class="w8em format-y-m-d divider-dash highlight-days-67" id="fechaLimite"   name="fechaLimite" value="<%=actividad.get(12)%>" size="10" MAXLENGTH="10">
                            </div>
                            <div>
                                <label>Hora Limite</label>
                                <input type="time" name="horaLimite" id="<%=actividad.get(13)%>">   
                            </div>
                            <div>
                                <label>*Estatus</label>
                                <select name="estatus">
                                    <option value="<%=WebUtil.encode(session,actividad.get(14))%>"><%=actividad.get(14)%></option>
                                    <option value="<%=WebUtil.encode(session,"PENDIENTE")%>">PENDIENTE</option>
                                    <option value="<%=WebUtil.encode(session,"COMPLETADA")%>">COMPLETADA</option>
                                </select>
                            </div>
                            <%
                                access4ThisPage.clear();
                                access4ThisPage.add("UpdateActividad");
                                if (SystemUtil.haveAcess("UpdateActividad", userAccess)) {
                            %>
                            <div id="botonEnviarDiv" >
                                <input type="button" value="Guardar" name="Enviar" onclick="enviarInfo(document.getElementById('updateActividad'));"/>
                            </div> 
                            <%
                                }
                            %>
                        </fieldset>
                    </form>
                    <form name="updateActividadPlantel" method="post" action="" enctype="application/x-www-form-urlencoded" id="updateActividadPlantel">
                        <input type="hidden" name="<%=WebUtil.encode(session, "imix")%>" value="<%=WebUtil.encode(session, UTime.getTimeMilis())%>"/>
                        <input type="hidden" name="FormFrom" value="updateActividadPlantel"/>
                        <input type="hidden" name="idActividadPlantel" value="<%=request.getParameter("idActividadPlantel")%>"/>
                        <input type="hidden" name="idPlantel" value="<%=request.getParameter("idPlantel")%>"/>
                        <fieldset>
                            <legend>Datos Actualizables</legend>
                            <div>
                                <label>*Porcentaje de Avance</label>
                                <input type="number" name="porcentajeCompleto" value="<%=actividad.get(18)%>" min="0" max="100">
                            </div>
                            <div>
                                <label for="observaciones">Observaciones</label>
                                <textarea  name="observaciones" cols="30" rows="5"><%=actividad.get(17)%></textarea>
                            </div>
                            <div id="botonEnviarDiv" >
                                <input type="button" value="Guardar" name="Enviar" onclick="enviarInfo(document.getElementById('updateActividadPlantel'));"/>
                            </div>      
                        </fieldset>
                    </form>
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