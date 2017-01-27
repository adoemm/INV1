<%@page import="java.util.Calendar"%>
<%@page import="jspread.core.util.SystemUtil"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("CentroOpcionesBien");

                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                    Iterator it = null;
                    LinkedList listAux = null;
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Centro de búsqueda</title>
        <jsp:include page='<%=PageParameters.getParameter("globalLibs")%>'/>        
        <jsp:include page='<%=PageParameters.getParameter("styleFormCorrections")%>'/>   

        <style type="text/css">
            .botonOpcion{
                padding: 3px;
                margin: 0px;
                cursor: pointer;
            }
        </style>
        <script type="text/javascript" language="javascript" charset="utf-8">
            function enviarInfo(form) {
                $.ajax({type: 'POST'
                    , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                    , cache: false
                    , async: false
                    , url: '<%=PageParameters.getParameter("mainController")%>'
                    , data: $(form).serialize()
                    , success: function (response) {
                        if (response.indexOf("title: \"Error\"") >= 0) {
                            $('#wrapper').find('#divResult').html(response);
                        } else {
                            $('#wrapper').find('#divBienes').html(response);
                        }
                    }});
            }
            function setEstatus(idBien, status) {
                $.msgBox({
                    title: "Confirmar"
                    , content: "Esta seguro que desea cambiar el estatus del bien a BAJA?"
                    , type: "confirm"
                    , buttons: [{value: "SI"}, {value: "NO"}]
                    , opacity: 0.75
                    , success: function (result) {
                        if (result === "SI") {
                            $.ajax({type: 'POST'
                                , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                                , cache: false
                                , async: false
                                , url: '<%=PageParameters.getParameter("mainController")%>'
                                , data: '<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idBien=' + idBien + '&FormFrom=updateEstatusBien&status=' + status
                                , success: function (response) {
                                    $('#wrapper').find('#divResult').html(response);
                                }});
                        }

                    }
                });
            }
            function getInfoBien(idBien) {
                $('#wrapper').find('#divInfoBien').html('');
                if (idBien !== null) {
                    $.ajax({type: 'POST'
                        , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                        , cache: false
                        , async: false
                        , url: "<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("ajaxFunctions")%>/getInfoBien.jsp"
                        , data: '<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idBien=' + idBien
                        , success: function (response) {
                            $('#wrapper').find('#divInfoBien').html(response);
                        }});
                }
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
                        var idplantel = "";
                        if (document.getElementById('buscarTodo') !== null && !document.getElementById('buscarTodo').checked) {
                            idplantel = "&idPlantel=<%=WebUtil.encode(session, session.getAttribute("FK_ID_Plantel").toString())%>";
                        }
                        $.ajax({type: 'POST'
                            , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                            , cache: false
                            , async: false
                            , url: '/vento/ajaxFunctions/buscarBien.jsp'
                            , data: '<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&busqueda=' + searchText + idplantel
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
            function fillSuggestions(id, thisValue) {
                if (id.length === 0 || thisValue.length === 0) {
                    document.getElementById('cajaDebotones').style.display = 'none';
                } else {
                    $('#searchText').val('');
                    $('#idBien').attr('value', id);
                    getInfoBien(id);
                    document.getElementById('cajaDebotones').style.display = 'block';
                    setTimeout("$('#suggestions').hide();", 200);
                }
            }

            function goToOption(option) {
                var url = '';
                if (option === 'datosGenerales') {
                    url = '/UpdateBien.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idBien=' + document.getElementById('idBien').value + '&idPlantel=' + document.getElementById('idPlantel').value;
                } else if (option === 'verEspecificaciones') {
                    url = '/ConsultaBDS.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idBien=' + document.getElementById('idBien').value;
                } else if (option === 'datosProveedor') {
                    url = '/ConsultaPBD.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idBien=' + document.getElementById('idBien').value;
                } else if (option === 'garantia') {
                    url = '/ConsultaGarantia.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idBien=' + document.getElementById('idBien').value;
                } else if (option === 'insertBaja') {
                    url = '/InsertBaja.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idBien=' + document.getElementById('idBien').value;
                } else if (option === 'checkList') {
                    url = '/InsertCRB.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idBien=' + document.getElementById('idBien').value;
                } else if (option === 'incidente') {
                    url = '/InsertBitacoraIncidente.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idBien=' + document.getElementById('idBien').value;
                } else if (option === 'infoResguardo') {
                    url = '/ConsultaResguardo4Bien.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idBien=' + document.getElementById('idBien').value;
                } else if (option === 'barCode') {
                    url = '/BarCode4Bien.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idBien=' + document.getElementById('idBien').value;
                    window.open('<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("prints")%>' + url);
                    url = '';
                } else if (option === 'bajaDirecta') {
                    url = '?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idBien=' + document.getElementById('idBien').value + '&FormFrom=updateEstatusBien&status=<%=WebUtil.encode(session, "BAJA")%>';
                    window.open('<%=PageParameters.getParameter("mainController")%>' + url);
                    url = '';
                }

                if (url !== '') {
                    window.open('<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>' + url);
                }
            }

            function popupArchivos() {
                newwindow = window.open(
                        '<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/Insert_ObjetoArchivo.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idObjeto=' + document.getElementById('idBien').value + '&nombreObjeto=<%=WebUtil.encode(session, "BIEN")%>'
                        , 'Archivos'
                        , 'width=' + (screen.availWidth - 10).toString() + ',height=' + (screen.availHeight - 122).toString() + ',toolbar=0,menubar=0,resizable=1,scrollbars=1,status=1,location=0,directories=0,top=50'
                        );
                if (window.focus) {
                    newwindow.focus();
                }
            }

        </script>
    </head>
    <body >
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
                                > Centro de búsqueda
                            </td>
                            <td width="36" align="right" valign="top">
                                <script language="JavaScript" src="<%=PageParameters.getParameter("jsRcs")%>/funcionDate.js" type="text/javascript"></script>
                            </td>
                        </tr>                        
                    </table>
                    <form name="updateResguardo" method="post" action="" enctype="application/x-www-form-urlencoded" id="updateResguardo">
                        <input type="hidden" name="<%=WebUtil.encode(session, "imix")%>" value="<%=WebUtil.encode(session, UTime.getTimeMilis())%>"/>
                        <input type="hidden" name="FormFrom" value="searchBien"/>
                        <input type="hidden" id='idBien' name="idBien" value="" />
                        <fieldset>
                            <legend>Centro de búsqueda</legend>
                            <div class="divControlHelp">
                                <div id="divAyuda" class="div4HelpButton" >
                                    <img  id="botonAyuda"  src="<%=PageParameters.getParameter("imgRsc") + "/icons/help-browser.png"%>" width="24px" height="24px" onclick="showButton('divInfo');
                hideButton('divAyuda');" title="Abrir ayuda">
                                </div>
                                <div>
                                    <label>*Buscar</label>
                                    <input type="text" name="searchText" id="searchText" onkeyup='buscarBien(event.keyCode);'  size="75">
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
                                        &nbsp Busque bienes/artículos por modelo, no. de serie <br>&nbsp o no. de inventario.
                                    </p>
                                </div>
                            </div> 

                            <div id="cajaDebotones" style="display:none;">
                                <div style="display:none;">
                                    <input type="text">
                                </div>
                                <div id="divInfoBien" class="msginfo" style="padding-left: 15%; width: 55%;margin-left: 10%;">
                                </div>
                                <input type="button" value="Datos Generales" onclick="goToOption('datosGenerales')" class="botonOpcion"/>
                                <input type="button" value="Ver Especificaciones" onclick="goToOption('verEspecificaciones')" class="botonOpcion"/>
                                <input type="button" value="Datos del Proveedor" onclick="goToOption('datosProveedor')" class="botonOpcion"/>
                                <input type="button" value="Garantía" onclick="goToOption('garantia')" class="botonOpcion"/>
                                <input type="button" value="Dar de baja" onclick="goToOption('insertBaja')" class="botonOpcion"/>
                                <input type="button" value="Aplicar Checklist" onclick="goToOption('checkList')" class="botonOpcion"/>
                                <input type="button" value="Reportar Incidente" onclick="goToOption('incidente')" class="botonOpcion"/>
                                <input type="button" value="Información de Resguardo" onclick="goToOption('infoResguardo')" class="botonOpcion"/>
                                <input type="button" value="Código de Barras" onclick="goToOption('barCode')" class="botonOpcion"/>
                                <input type="button" value="Archivos" onclick="popupArchivos();" class="botonOpcion"/>
                                <%
                                    if (SystemUtil.haveAcess("bajaDirecta", userAccess)) {
                                %>
                                <input type="button" value="Baja Directa" onclick="setEstatus(document.getElementById('idBien').value, '<%=WebUtil.encode(session, "BAJA")%>')" class="botonOpcion"/>
                                <%
                                    }
                                %>
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