<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("InsertMovimientoBien");

                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                    Iterator it = null;
                    LinkedList listAux = null;
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    
    <head>
        <title>Agregar Bien a Movimiento</title>
        <jsp:include page='<%=PageParameters.getParameter("globalLibs")%>'/>        
        <jsp:include page='<%=PageParameters.getParameter("styleFormCorrections")%>'/>   
       <script>
        function enviarInfo() {
                //alert("enviando info");
                $.ajax({type: 'POST'
                    , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                    , cache: false
                    , async: false
                    , url: '<%=PageParameters.getParameter("mainController")%>'
                    , data: $('#insertTraspasoBien').serialize()
                    , success: function(response) {
                        $('#wrapper').find('#divResult').html(response);
                    }});
            }
            function buscarBien(keyCode) {
                document.getElementById('cajaDebotones').style.display = 'none';
                if (keyCode === 32 || keyCode === 13 || keyCode === 8) {
                    $('#idBien').attr('value', "");
                    searchText = document.getElementById("searchText").value;
                    if (searchText.length === 3) {
                        $('#suggestions').hide();
                        $('#idBien').attr('value', "");
                        document.getElementById('cajaDebotones').style.display = 'none';
                    } else {
                        $.ajax({type: 'POST'
                            , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                            , cache: false
                            , async: false
                            , url: '/vento/ajaxFunctions/buscarBien.jsp'
                            , data: '<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&busqueda=' + searchText
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
                    $('#searchText').val(thisValue);
                    $('#idBien').attr('value', id);
                    document.getElementById('cajaDebotones').style.display = 'block';
                    setTimeout("$('#suggestions').hide();", 200);
                }
            }
            function getPersonalPlantel() {
                var FK_ID_Plantel = document.getElementById("FK_ID_Plantel").value;
                console.log("FK_ID_Plantel" +FK_ID_Plantel );
                if (FK_ID_Plantel !== "") {
                    //document.getElementById('municipioDiv').innerHTML = '';
                    var response = $.ajax({
                        type: 'get',
                        contentType: 'application/x-www-form-urlencoded;charset=utf-8',
                        cache: false,
                        url: "<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("ajaxFunctions")%>/getPersonalXPlantel.jsp",
                        data: '<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&FK_ID_Plantel=' + FK_ID_Plantel,
                        async: false,
                        success: function(data) {
                            // Hagamos algo maravilloso con estos nuevos datos
                        }
                    }).responseText;
                    document.getElementById('personalDiv').innerHTML = response;
                } else {
                    //document.getElementById('municipioDiv').innerHTML = '';
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
                                <a class="NVL" href="<%=PageParameters.getParameter("mainMenu")%>?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"> Menú Principal</a>
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaTraspaso.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>">Catálogo de Movimientos</a> 
                                 > Agregar Bien a Movimiento
                            </td>
                            <td width="36" align="right" valign="top">
                                <script language="JavaScript" src="<%=PageParameters.getParameter("jsRcs")%>/funcionDate.js" type="text/javascript"></script>
                            </td>
                        </tr>                        
                    </table>
                    <form id="insertTraspasoBien" name="insertTraspasoBien" method="post" action="" enctype="application/x-www-form-urlencoded">
                        <input type="hidden" name="imix" value="<%=WebUtil.encode(session, UTime.getTimeMilis())%>"/>
                        <input type="hidden" name="FormFrom" value="insertTraspasoBien"/>
                        <input type="hidden" id='idBien' name="idBien" value=""/>
                         <input type="hidden" name="idNuevoMovimiento" id="idNuevoMovimiento" value="<%=request.getParameter("idNuevoMovimiento")%>"/>
                        <fieldset>   
                            <legend>Agregar Bien a Movimiento</legend>
                            <div>
                                <label>*Número de inventario</label>
                                <input type="text" name="searchText" id="searchText" onkeyup='buscarBien(event.keyCode);' onblur='fillSuggestions();' size="75">
                                <div class="suggestionsBox" id="suggestions" style="display: none;">
                                    <div class="suggestionList" id="autoSuggestionsList">
                                    </div>
                                </div>
                            </div>
                            <div id="cajaDebotones" style="display:none;">
                                <div>
                                    <label for="estatusTraspaso">*Estatus</label>
                                    <select name="estatusTraspaso">                                            
                                        <option value=""></option>
                                        <option value=<%=WebUtil.encode(session, "En proceso")%>>En proceso</option>
                                        <option value=<%=WebUtil.encode(session, "Aceptado")%>>Aceptado</option>
                                        <option value=<%=WebUtil.encode(session, "Rechazado")%>>Rechazado</option>
                                    </select>
                                </div>
                                <div>
                                    <label for="FK_ID_Plantel">*Plantel Destino</label>          
                                    <select name="FK_ID_Plantel" id="FK_ID_Plantel"  onkeyup="getPersonalPlantel();" onChange="getPersonalPlantel();">
                                        <option value=""></option>
                                        <%
                                            it = null;
                                            it = QUID.select_infoPlantel().iterator();
                                            while (it.hasNext()) {
                                                listAux = null;
                                                listAux = (LinkedList) it.next();
                                        %>
                                        <option value="<%=WebUtil.encode(session, "" + listAux.get(0))%>"><%=listAux.get(1)%></option>
                                        <%
                                            }
                                        %>
                                    </select>
                                </div>
                                <div id="personalDiv">   
                                </div>
                                <div>
                                    <label for="observaciones">*Observaciones</label>
                                    <textarea name="observaciones" rows="4" cols="50" size="25"></textarea> 
                                </div>
                                <div id="botonEnviarDiv" >
                                    <input type="button" value="Guardar" name="Enviar" onclick="enviarInfo();"/>
                                </div>      
                     </fieldset>
                    </form>
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