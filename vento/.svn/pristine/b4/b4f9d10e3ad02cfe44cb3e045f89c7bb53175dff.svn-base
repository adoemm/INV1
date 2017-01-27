<%@page import="jspread.core.util.SystemUtil"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("InsertMovimientoConsumibleSalida");

                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                    Iterator it = null;
                    LinkedList listAux = null;
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Nueva Salida</title>
        <jsp:include page='<%=PageParameters.getParameter("globalLibs")%>'/>        
        <jsp:include page='<%=PageParameters.getParameter("styleFormCorrections")%>'/>   
        <jsp:include page='<%=PageParameters.getParameter("ajaxFunctions") + "/tablaCapturaCSS.jsp"%>'/>
        <script type="text/javascript" language="javascript" charset="utf-8">
            function resetForm() {
                document.getElementById("searchText").value = '';
                document.getElementById('cajaDebotones').style.display = 'none';
                $('#wrapper').find('#divInfoConsumible').html('');
                document.getElementById("cantidad").value = '';
            }
            function enviarInfo(form) {
                $.ajax({type: 'POST'
                    , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                    , cache: false
                    , async: false
                    , url: '<%=PageParameters.getParameter("mainController")%>'
                    , data: $(form).serialize()
                    , success: function(response) {
                        if (response.indexOf("title: \"Error\"") >= 0) {
                            $('#wrapper').find('#divResult').html(response);
                        } else {
                            $('#wrapper').find('#divConsumibleMovimiento').html(response);
                        }

                    }});
                resetForm();
            }

            function eliminarConsumible(idMovimientoConsumible, idMovimiento) {

                $.msgBox({
                    title: "Confirmar"
                    , content: "Esta seguro que desea eliminar el registro?"
                    , type: "confirm"
                    , buttons: [{value: "SI"}, {value: "NO"}]
                    , opacity: 0.75
                    , success: function(result) {
                        if (result === "SI") {
                            $.ajax({type: 'POST'
                                , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                                , cache: false
                                , async: false
                                , url: "<%=PageParameters.getParameter("mainController")%>"
                                , data: '<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idMovimientoConsumible=' + idMovimientoConsumible + '&FormFrom=deleteConsumibleSalida&idMovimiento=' + idMovimiento
                                , success: function(response) {
                                    if (response.indexOf("title: \"Error\"") >= 0) {
                                        $('#wrapper').find('#divResult').html(response);
                                    } else {
                                        $('#wrapper').find('#divConsumibleMovimiento').html(response);
                                    }
                                }});
                        }

                    }
                });


            }

            function getConsumibleMovimiento(idMovimiento) {
                $('#wrapper').find('#divConsumibleMovimiento').html('');
                if (idMovimiento !== null) {
                    $.ajax({type: 'POST'
                        , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                        , cache: false
                        , async: false
                        , url: "<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("ajaxFunctions")%>/getConsumible4Movimiento.jsp"
                        , data: '<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idMovimiento=' + idMovimiento
                        , success: function(response) {
                            $('#wrapper').find('#divConsumibleMovimiento').html(response);
                        }});
                }
            }
            function getInfoConsumible(idConsumible) {
                $('#wrapper').find('#divInfoConsumible').html('');
                if (idConsumible !== null) {
                    $.ajax({type: 'POST'
                        , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                        , cache: false
                        , async: false
                        , url: "<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("ajaxFunctions")%>/getInfoConsumible.jsp"
                        , data: '<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idConsumible=' + idConsumible
                        , success: function(response) {
                            $('#wrapper').find('#divInfoConsumible').html(response);
                        }});
                }
            }

            function buscarConsumible(keyCode) {
                document.getElementById('cajaDebotones').style.display = 'none';
                if (keyCode === 32 || keyCode === 13 || keyCode === 8) {
                    $('#idConsumible').attr('value', "");
                    searchText = document.getElementById("searchText").value;
                    if (searchText.length === 3) {
                        $('#suggestions').hide();
                        $('#idConsumible').attr('value', "");
                        document.getElementById('cajaDebotones').style.display = 'none';
                    } else {
                        var idplantel = "";
                        if (document.getElementById('buscarTodo') !== null && !document.getElementById('buscarTodo').checked) {
                            idplantel = "&idPlantel=<%=WebUtil.encode(session, session.getAttribute("FK_ID_Plantel").toString())%>";
                        }
                        $.ajax({type: 'POST'
                            , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                            , cache: false
                            , async: false
                            , url: '/vento/ajaxFunctions/buscarConsumible.jsp'
                            , data: '<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&busqueda=' + searchText + idplantel
                            , success: function(response) {
                                if (response.length > 0) {
                                    $('#suggestions').show();
                                    $('#autoSuggestionsList').html(response);
                                }
                            }
                        });
                    }
                }
            }
            function fillSuggestions(id, thisValue) {
                if (id.length === 0 || thisValue.length === 0) {
                    document.getElementById('cajaDebotones').style.display = 'none';
                } else {
                    $('#searchText').val('');
                    $('#idConsumible').attr('value', id);
                    getInfoConsumible(id);
                    document.getElementById('cajaDebotones').style.display = 'block';
                    setTimeout("$('#suggestions').hide();", 200);
                }
            }

        </script>
    </head>
    <body onload="getConsumibleMovimiento('<%=request.getParameter("idMovimiento")%>')">
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
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/MenuConsumibles.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>">Menú Consumibles</a> 
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaMovimientoSalida.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idPlantel=<%=request.getParameter("idPlantel")%>">Movimientos de Salida</a> 
                                > Nueva Salida
                            </td>
                            <td width="36" align="right" valign="top">
                                <script language="JavaScript" src="<%=PageParameters.getParameter("jsRcs")%>/funcionDate.js" type="text/javascript"></script>
                            </td>
                        </tr>                        
                    </table>
                    <form name="insertSalidaConsumible" method="post" action="" enctype="application/x-www-form-urlencoded" id="insertSalidaConsumible">
                        <input type="hidden" name="<%=WebUtil.encode(session, "imix")%>" value="<%=WebUtil.encode(session, UTime.getTimeMilis())%>"/>
                        <input type="hidden" name="FormFrom" value="insertSalidaConsumible"/>
                        <input type="hidden" name="idMovimiento" value="<%=request.getParameter("idMovimiento")%>"/>
                        <input type="hidden" id='idConsumible' name="idConsumible" value="" />
                        <%
                            LinkedList entrada = QUID.select_MovimientoSalida4ID(WebUtil.decode(session, request.getParameter("idMovimiento")));
                        %>
                        <fieldset>
                            <legend>Nueva Salida</legend>
                            <div>
                                <label>Folio de Salida</label>
                                <input type="text" name="folio" value="<%=entrada.get(2)%>" disabled>
                            </div>
                            <div>
                                <label>Número de Turno</label>
                                <input type="text" name="noTurno" value="<%=entrada.get(13)%>" disabled>
                            </div>
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
                                <%
                                    if (SystemUtil.haveAcess("VerTodo", userAccess)) {
                                %>
                                <div>
                                    <input type="checkbox" id="buscarTodo" value="<%=WebUtil.encode(session, "1")%>" checked>Buscar en todos los planteles
                                </div>
                                <%
                                    }
                                %>
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
                            <div id="cajaDebotones" style="display:none;">
                                <div id="divInfoConsumible" class="msginfo" style="padding-left: 15%; width: 55%;margin-left: 10%;">
                                </div>
                                <div>
                                    <label for="cantidad">*Cantidad</label>
                                    <input type="text" name="cantidad" id="cantidad">
                                </div>
                            </div> 
                        </fieldset>      
                    </form>

                    <a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaMovimientoSalida.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idPlantel=<%=request.getParameter("idPlantel")%>">
                        <button>Terminar</button>
                    </a>
                    <a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("prints")%>/movimientoSalida.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idMovimiento=<%=request.getParameter("idMovimiento")%>" target="_blank">
                        <button>Imprimir</button>
                    </a>
                    <button onclick="resetForm();">Nuevo</button>
                    <button onclick="enviarInfo(document.getElementById('insertSalidaConsumible'));">Guardar</button>
                    <br><br>
                    <div id="divConsumibleMovimiento" align="center"></div>
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