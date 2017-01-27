<%@page import="jspread.core.util.SystemUtil"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("UpdateOrdenSurtimiento");

                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {

                    LinkedList orden = QUID.select_OrdenSurtimiento(WebUtil.decode(session, request.getParameter("idOrdenSurtimiento")));

                    Iterator it = null;
                    LinkedList listAux = null;
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Editar Orden de Surtido</title>
        <jsp:include page='<%=PageParameters.getParameter("globalLibs")%>'/>        
        <jsp:include page='<%=PageParameters.getParameter("styleFormCorrections")%>'/>   
        <jsp:include page='<%=PageParameters.getParameter("ajaxFunctions") + "/tablaCapturaCSS.jsp"%>'/>
        <script type="text/javascript" language="javascript" charset="utf-8">
            var idTr = 0;
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

            function crearteInput(valor, nombre, texto, idInput) {
                $('#suggestions').hide();
                $.ajax({type: 'POST'
                    , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                    , cache: false
                    , async: false
                    , url: '<%=PageParameters.getParameter("mainController")%>'
                    , data: '<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idConsumible=' + valor + '&FormFrom=checkExistencia4Consumible'
                    , success: function (response) {

                        if (response.indexOf("title: \"Error\"") >= 0) {
                            $('#wrapper').find('#divResult').html(response);
                        } else {
                            palomita = "<tr id=\"tr" + idTr + "\" ><td><input type=\"hidden\" name=\"" + nombre + "\" value=\"" + valor + "\">(+) " + texto + "</td><td><input type=\"text\" name=\"" + valor + "\" ></td><td><a onclick=\"deleteInput(tr" + idTr + ")\">X</a></td></tr";
                            $("#tablaConsumibles").append(palomita);
                            idTr += 1;
                        }
                    }});

            }
            function deleteInput(idInput) {
                $(idInput).remove();
            }
            function deleteSavedInput(idInput, idOrdenConsumible) {
                $.ajax({type: 'POST'
                    , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                    , cache: false
                    , async: false
                    , url: '<%=PageParameters.getParameter("mainController")%>'
                    , data: '<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&FormFrom=deleteOrdenConsumible&idOrdenConsumible=' + idOrdenConsumible + '&idInput=' + idInput + '&idOrdenSurtimiento=<%=request.getParameter("idOrdenSurtimiento")%>'
                    , success: function (response) {
                        if (response.indexOf("title: \"Error\"") >= 0) {
                            $('#wrapper').find('#divResult').html(response);
                        } else {
                            $(idInput).remove();
                        }
                    }});
            }

            function buscarConsumible(keyCode) {
                if (keyCode === 32 || keyCode === 13 || keyCode === 8) {
                    searchText = document.getElementById("searchText").value;
                    if (searchText.length <= 3) {
                        $('#suggestions').hide();
                    } else {
                        var idplantel = "&idPlantel=<%=WebUtil.encode(session, orden.get(2))%>";
                        $.ajax({type: 'POST'
                            , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                            , cache: false
                            , async: true
                            , url: '/vento/ajaxFunctions/buscarConsumible.jsp'
                            , data: '<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&busqueda=' + searchText + idplantel + '&page=<%=WebUtil.encode(session, "ordensurtimiento")%>'
                            , success: function (response) {
                                if (response.length > 0) {
                                    $('#suggestions').show();
                                    $('#autoSuggestionsList').html(response);
                                }
                            }
                        });
                    }
                }
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
                                <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaOrdenSurtimiento.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idPlantel=<%=request.getParameter("idPlantel")%>">Ordenes de Surtido Realizadas</a> 
                                > Editar Orden de Surtido
                            </td>
                            <td width="36" align="right" valign="top">
                                <script language="JavaScript" src="<%=PageParameters.getParameter("jsRcs")%>/funcionDate.js" type="text/javascript"></script>
                            </td>
                        </tr>                        
                    </table>

                    <form name="updateOrdenSurtimiento" method="post" action="" enctype="application/x-www-form-urlencoded" id="updateOrdenSurtimiento">
                        <input type="hidden" name="<%=WebUtil.encode(session, "imix")%>" value="<%=WebUtil.encode(session, UTime.getTimeMilis())%>"/>
                        <input type="hidden" name="FormFrom" value="updateOrdenSurtimiento"/>
                        <input type="hidden" name="idOrdenSurtimiento" value="<%=request.getParameter("idOrdenSurtimiento")%>"/>
                        <input type="hidden" name="idPlantelSolicita" value="<%=request.getParameter("idPlantel")%>"/>
                        <fieldset>
                            <legend>Editar Orden de Surtido</legend>
                            <div>
                                <label for="folio">*Folio</label>
                                <input type="text" value="<%=orden.get(4)%>">
                            </div>
                            <div>
                                <label for="idPlantelSurte">*Plantel Proveedor</label>
                                <input type="text" value="<%=orden.get(1)%>">
                            </div>
                            <div>
                                <label for="asuntoGeneral">*Asunto</label>
                                <textarea name="asuntoGeneral" cols="30" rows="4" MAXLENGTH="150"><%=orden.get(14)%></textarea>
                            </div>
                            <div>
                                <label for="fechaRequerida">*Fecha Requerida (aaaa-mm-dd)</label>
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
                        <fieldset>
                            <legend>Materiales Requeridos</legend>
                            <div class="divControlHelp">
                                <div id="divAyuda" class="div4HelpButton" >
                                    <img  id="botonAyuda"  src="<%=PageParameters.getParameter("imgRsc") + "/icons/help-browser.png"%>" width="24px" height="24px" onclick="showButton('divInfo');
                hideButton('divAyuda');" title="Abrir ayuda">
                                </div>
                                <div>
                                    <label>*Buscar</label>
                                    <input type="text" name="searchText" id="searchText" onkeyup='buscarConsumible(event.keyCode);'  size="75">
                                    <div class="suggestionsBox" id="suggestions" style="display: none;">
                                        <div class="suggestionList" id="autoSuggestionsList" >
                                        </div>
                                    </div>
                                </div>
                                <div id="divInfo" class="msginfo div4HelpInfo" style="width:340px;" onmouseover="showButton('botonCerrarInfo');" onmouseout="hideButton('botonCerrarInfo');">
                                    <div class="deleteBar div4HelpInfoCloseButton" >
                                        <img  class="div4HelpCloseButton" align="right" id="botonCerrarInfo"  src="<%=PageParameters.getParameter("imgRsc") + "/icons/delete.png"%>" width="16px" height="16px" onclick="hideButton('divInfo');
                                                showButton('divAyuda');" title="Cerrar ayuda">
                                    </div>
                                    <p>
                                        &nbsp Busque consumibles por clave o descripción.
                                    </p>
                                </div>
                            </div>
                            <div id="divConsumibles" align="center">
                                <table style="width:50%;" id="tablaConsumibles" class="cssLayout">
                                    <tr>
                                        <td width="50%" >Consumible</td>
                                        <td>Cantidad</td>
                                        <td></td>
                                    </tr>
                                    <%
                                        it = QUID.select_OrdenSurtimientoConsumible(WebUtil.decode(session, request.getParameter("idOrdenSurtimiento"))).iterator();
                                        int i = 0;
                                        while (it.hasNext()) {
                                            LinkedList datos = (LinkedList) it.next();
                                    %>
                                    <tr id="tr<%=i%>">
                                        <td><input type="hidden" name="idConsumible" value="<%=WebUtil.encode(session, datos.get(1))%>">
                                            <%=datos.get(5).toString() + " " + datos.get(6).toString() + "(" + datos.get(7).toString() + ")"%></td>
                                        <td>
                                            <input type="text" name="<%=WebUtil.encode(session, datos.get(1))%>" value="<%=datos.get(2)%>">
                                        </td>
                                        <td><a onclick="deleteSavedInput(tr<%=i%>, '<%=WebUtil.encode(session, datos.get(0))%>')">X</a></td>
                                    </tr>
                                    <%
                                            i += 1;
                                        }
                                    %>
                                </table>
                                <script type="text/javascript" language="javascript" charset="utf-8">
                                    idTr =<%=i%>;
                                </script>
                            </div>
                        </fieldset>
                        <div id="botonEnviarDiv" >
                            <input type="button" value="Guardar" name="Guardar" onclick="enviarInfo(document.getElementById('updateOrdenSurtimiento'),'<%=WebUtil.encode(session,"Pendiente")%>');"/>
                            <input type="button" value="Enviar" name="Enviar" onclick="enviarInfo(document.getElementById('updateOrdenSurtimiento'),'<%=WebUtil.encode(session,"Proceso")%>');"/>
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