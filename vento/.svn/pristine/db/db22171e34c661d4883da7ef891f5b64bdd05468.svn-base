<%@page import="jspread.core.util.SystemUtil"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("InsertOrdenSurtimiento");

                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                    Iterator it = null;
                    LinkedList listAux = null;
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Nueva Orden de Surtido</title>
        <jsp:include page='<%=PageParameters.getParameter("globalLibs")%>'/>        
        <jsp:include page='<%=PageParameters.getParameter("styleFormCorrections")%>'/>   
        <jsp:include page='<%=PageParameters.getParameter("ajaxFunctions") + "/tablaCapturaCSS.jsp"%>'/>
        <script type="text/javascript" language="javascript" charset="utf-8">
            var idTr = 0;
            function enviarInfo(form, estatus) {
                $.ajax({type: 'POST'
                    , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                    , cache: false
                    , async: false
                    , url: '<%=PageParameters.getParameter("mainController")%>'
                    , data: $(form).serialize() + '&estatus=' + estatus
                    , success: function (response) {
                        $('#wrapper').find('#divResult').html(response);
                    }});
            }

            function crearteInput(valor, nombre, texto) {
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
                            palomita = "<tr id=\"tr" + idTr + "\" ><td><input type=\"hidden\" name=\"" + nombre + "\" value=\"" + valor + "\">" + texto + "</td><td><input type=\"text\" name=\"" + valor + "\" ></td><td><a onclick=\"deleteInput(tr" + idTr + ")\">X</a></td></tr";
                            $("#tablaConsumibles").append(palomita);
                            idTr += 1;
                        }
                    }});

            }
            function deleteInput(idInput) {
                $(idInput).remove();
            }

            function buscarConsumible(keyCode) {
                if (keyCode === 32 || keyCode === 13 || keyCode === 8) {
                    searchText = document.getElementById("searchText").value;
                    if (searchText.length <= 3) {
                        $('#suggestions').hide();
                    } else {
                        var idplantel = "&idPlantel=" + document.getElementById('idPlantelSurte').value;
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
                                > Nueva Orden de Surtido
                            </td>
                            <td width="36" align="right" valign="top">
                                <script language="JavaScript" src="<%=PageParameters.getParameter("jsRcs")%>/funcionDate.js" type="text/javascript"></script>
                            </td>
                        </tr>                        
                    </table>

                    <form name="insertOrdenSurtimiento" method="post" action="" enctype="application/x-www-form-urlencoded" id="insertOrdenSurtimiento">
                        <input type="hidden" name="<%=WebUtil.encode(session, "imix")%>" value="<%=WebUtil.encode(session, UTime.getTimeMilis())%>"/>
                        <input type="hidden" name="FormFrom" value="insertOrdenSurtimiento"/>
                        <input type="hidden" name="idPlantelSolicita" value="<%=request.getParameter("idPlantel")%>"/>
                        <fieldset>
                            <legend>Nueva Orden de Surtido</legend>
                            <div>
                                <label for="idPlantelSurte">*Plantel Proveedor</label>
                                <%
                                    it = QUID.select_PlantelAlmacen4PlantelSolicita(WebUtil.decode(session, request.getParameter("idPlantel")), "1").iterator();
                                %>
                                <select name="idPlantelSurte" id="idPlantelSurte">
                                    <option value=""></option>
                                    <%
                                        while (it.hasNext()) {
                                            LinkedList datos = (LinkedList) it.next();
                                    %>
                                    <option value="<%=WebUtil.encode(session, datos.get(0))%>"><%=datos.get(2)%></option>
                                    <%
                                        }
                                    %>
                                </select>
                            </div>
                            <div>
                                <label for="asuntoGeneral">*Asunto</label>
                                <textarea name="asuntoGeneral" cols="30" rows="4" MAXLENGTH="150"></textarea>
                            </div>
                            <div>
                                <label for="fechaRequerida">*Fecha Requerida (aaaa-mm-dd)</label>
                                <input type="text" class="w8em format-y-m-d divider-dash highlight-days-67" id="fechaRequerida"   name="fechaRequerida" value="" size="10" MAXLENGTH="10">
                            </div>
                            <div>
                                <label for="justificacion">Justificación</label>
                                <textarea name="justificacion" cols="30" rows="4" ></textarea>
                            </div>
                            <div>
                                <label for="observaciones">Observaciones</label>
                                <textarea name="observaciones" cols="30" rows="4" ></textarea>
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
                                </table>
                            </div>
                        </fieldset>
                        <div id="botonEnviarDiv" >
                            <input type="button" value="Guardar Borrador" name="Guardar" onclick="enviarInfo(document.getElementById('insertOrdenSurtimiento'), '<%=WebUtil.encode(session, "Pendiente")%>');"/>
                            <input type="button" value="Enviar" name="Enviar" onclick="enviarInfo(document.getElementById('insertOrdenSurtimiento'), '<%=WebUtil.encode(session, "Proceso")%>');"/>
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