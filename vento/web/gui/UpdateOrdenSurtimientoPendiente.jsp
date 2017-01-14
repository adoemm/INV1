<%@page import="jspread.core.util.SystemUtil"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("UpdateOrdenSurtimientoPendiente");

                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {

                    LinkedList orden = QUID.select_OrdenSurtimiento(WebUtil.decode(session, request.getParameter("idOrdenSurtimiento")));
                    Iterator it = null;
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Surtir</title>
        <jsp:include page='<%=PageParameters.getParameter("globalLibs")%>'/>        
        <jsp:include page='<%=PageParameters.getParameter("styleFormCorrections")%>'/>   
        <jsp:include page='<%=PageParameters.getParameter("ajaxFunctions") + "/tablaCapturaCSS.jsp"%>'/>
        <script type="text/javascript" language="javascript" charset="utf-8">
            function enviarInfo(form,estatus) {
                $.ajax({type: 'POST'
                    , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                    , cache: false
                    , async: false
                    , url: '<%=PageParameters.getParameter("mainController")%>'
                    , data: $(form).serialize()+'&estatus='+estatus
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
                                <a class="NVL" href="<%=PageParameters.getParameter("mainMenu")%>"> Menú Principal</a> >
                                <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaOrdenSurtimientoPendiente.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idPlantel=<%=request.getParameter("idPlantel")%>">Ordenes de Surtido Pendientes</a> 
                                > Surtir
                            </td>
                            <td width="36" align="right" valign="top">
                                <script language="JavaScript" src="<%=PageParameters.getParameter("jsRcs")%>/funcionDate.js" type="text/javascript"></script>
                            </td>
                        </tr>                        
                    </table>

                    <form name="updateOrdenSurtimientoPendiente" method="post" action="" enctype="application/x-www-form-urlencoded" id="updateOrdenSurtimientoPendiente">
                        <input type="hidden" name="<%=WebUtil.encode(session, "imix")%>" value="<%=WebUtil.encode(session, UTime.getTimeMilis())%>"/>
                        <input type="hidden" name="FormFrom" value="updateOrdenSurtimientoPendiente"/>
                        <input type="hidden" name="idOrdenSurtimiento" value="<%=request.getParameter("idOrdenSurtimiento")%>"/>
                        <input type="hidden" name="idPlantelSurte" value="<%=request.getParameter("idPlantel")%>"/>
                        <input type="hidden" name="idPlantelSolicita" value="<%=WebUtil.encode(session,orden.get(12))%>"/>
                        <fieldset>
                            <legend>Orden a Surtir</legend>
                            <div>
                                <label for="folio">Folio de Solicitud</label>
                                <input type="text" value="<%=orden.get(4)%>">
                            </div>
                            <div>
                                <label>Plantel Solcitante</label>
                                <input type="text" value="<%=orden.get(13)%>">
                            </div>
                            <div>
                                <label for="asuntoGeneral">Asunto</label>
                                <textarea name="asuntoGeneral" cols="30" rows="4" MAXLENGTH="150"><%=orden.get(14)%></textarea>
                            </div>
                            <div>
                                <label for="fechaRequerida">Fecha Requerida (aaaa-mm-dd)</label>
                                <input type="text" class="w8em format-y-m-d divider-dash highlight-days-67" id="fechaRequerida"   name="fechaRequerida" value="<%=orden.get(5)%>" size="10" MAXLENGTH="10">
                            </div>
                            <div>
                                <label for="justificacion">Justificación</label>
                                <textarea name="justificacion"><%=orden.get(6)%></textarea>
                            </div>
                            <div>
                                <label for="observaciones">Observaciones</label>
                                <textarea name="observaciones"><%=orden.get(7)%></textarea>
                            </div>
                        </fieldset>
                            <input type="hidden" name="idTipoMovimiento" value="<%=WebUtil.encode(session, QUID.select_TipoMovimiento("SALIDA"))%>"/>
                        <%
                            int folio=QUID.select_siguienteFolioXTipoMovimiento("SALIDA");
                        %>
                        <fieldset>
                            <legend>Registro de Salida</legend>
                            
                            <div>
                                <label>*Folio de Salida</label>
                                <input type="text" name="strFolio" value="<%=folio%>" disabled>
                                <input type="hidden" name="folio" value="<%=WebUtil.encode(session, folio)%>"/>
                            </div>
                            <div>
                                <label for="noTurno">*Número de Turno</label>
                                <input type="text" name="noTurno">
                            </div>
                            <div>
                                <label for="fechaMovimiento">*Fecha</label>
                                <input type="text" class="w8em format-y-m-d divider-dash highlight-days-67" id="fechaMovimiento"   name="fechaMovimiento" value="" size="10" MAXLENGTH="10">
                            </div>
                            
                            <div>
                                <label for="motivoMovimiento">Motivo</label>
                                <textarea  name="motivoMovimiento" cols="30" rows="5"></textarea>
                            </div>
                            <div>
                                <label for="estatus">*Estatus</label>
                                <select name="estatus">
                                    <option value=""></option>
                                    <option value="<%=WebUtil.encode(session, "Completo")%>">Completo</option>
                                    <option value="<%=WebUtil.encode(session, "Incompleto")%>"">Incompleto</option>
                                </select>
                            </div>
                            <div>
                                <label for="observacionesMovimiento">Observaciones</label>
                                <textarea  name="observacionesMovimiento" cols="30" rows="5" id="observacionesMovimiento"></textarea>
                            </div>
                        </fieldset>
                        <fieldset>
                            <legend>Materiales Requeridos</legend>
                            <div id="divConsumibles" align="center">
                                <table style="width:50%;" id="tablaConsumibles" class="cssLayout">
                                    <tr>
                                        <td></td>
                                        <td width="50%" >Consumible</td>
                                        <td>Cantidad Requerida</td>
                                        <td>Cantidad a Surtir</td>
                                    </tr>
                                    <%
                                        it = QUID.select_OrdenSurtimientoConsumible(WebUtil.decode(session, request.getParameter("idOrdenSurtimiento"))).iterator();
                                        int i = 0;
                                        while (it.hasNext()) {
                                            LinkedList datos = (LinkedList) it.next();
                                    %>
                                    <tr id="tr<%=i%>">
                                        <td><%=i+1%></td>
                                        <td><input type="hidden" name="idConsumible" value="<%=WebUtil.encode(session, datos.get(0))%>">
                                            <%=datos.get(5).toString() + " " + datos.get(6).toString() + "(" + datos.get(7).toString() + ")"%></td>
                                        <td>
                                            <input type="text" value="<%=datos.get(2)%>" style="padding:0px;margin: 0px;">
                                        </td>
                                        <td><input type="text" name="<%=WebUtil.encode(session, datos.get(0))%>" value="" style="padding:0px;margin: 0px;"></td>
                                    </tr>
                                    <%
                                            i += 1;
                                        }
                                    %>
                                </table>
                               
                            </div>
                        </fieldset>
                        <div id="botonEnviarDiv" >
                            <input type="button" value="Guardar" name="Guardar" onclick="enviarInfo(document.getElementById('updateOrdenSurtimientoPendiente'));"/>
                        </div> 
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