<%@page import="jspread.core.util.SystemUtil"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("InsertBien");

                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                    Iterator it = null;
                    LinkedList listAux = null;
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Nuevo Bien</title>
        <jsp:include page='<%=PageParameters.getParameter("globalLibs")%>'/>        
        <jsp:include page='<%=PageParameters.getParameter("styleFormCorrections")%>'/>   
        <jsp:include page='<%=PageParameters.getParameter("ajaxFunctions") + "/tablaCapturaCSS.jsp"%>'/>
        <script type="text/javascript" language="javascript" charset="utf-8">

            function resetForm() {
                document.getElementById("noDictamen").value = '';
                document.getElementById("descripcion").value = '';
                document.getElementById("noFactura").value = '';
                document.getElementById("noSerie").value = '';
                document.getElementById("noInventario").value = '';
                document.getElementById("fechaCompra").value = '';
                document.getElementById("observaciones").value = '';
                document.getElementById('status').selectedIndex = "0";
                getDepartamento(document.getElementById("idPlantel").value);
                getDetalle4Subcategoria(document.getElementById("idSubcategoria").value);
            }
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
            function sendSerial(data) {
                $.ajax({type: 'POST'
                    , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                    , cache: false
                    , async: false
                    , url: "<%=PageParameters.getParameter("mainController")%>"
                    , data: $('#' + data).find('select,input').serialize() + '&FormFrom=validaSerial'
                    , success: function (response) {
                        if (response.indexOf("title: \"Error\"") >= 0) {
                            $('#wrapper').find('#divResult').html(response);
                            $('#wrapper').find('#divModelosPosibles').html('<a href=\"<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("excelReports")%>/ModelosPosiblesXSerieXSubCategoria.jsp?' + $('#' + data).find('select,input').serialize() + '\" onclick=\"resetPosiblesPatrones()"><input type=\"button\" value=\"Ver Posibles Modelos\"></a>');

                        } else {
                            showButton('divFormData');
                            hideButton('divContinuar');
                            resetPosiblesPatrones();
                        }
                    }});
            }
            function resetPosiblesPatrones() {
                $('#wrapper').find('#divModelosPosibles').html('');
            }
            function getDepartamento(idPlantel) {
                if (idPlantel !== null) {
                    $.ajax({type: 'POST'
                        , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                        , cache: false
                        , async: false
                        , url: "<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("ajaxFunctions")%>/getDepartamento4Plantel.jsp"
                        , data: '<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idPlantel=' + idPlantel + '&onChange=getGrupos'
                        , success: function (response) {
                            $('#wrapper').find('#divDepartamento').html(response);
                        }});
                }
            }

            function getGrupos(idDeptoPlantel) {
                if (idDeptoPlantel !== null) {
                    $.ajax({type: 'POST'
                        , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                        , cache: false
                        , async: false
                        , url: "<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("ajaxFunctions")%>/getGrupos4DeptoPlantel.jsp"
                        , data: '<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idDeptoPlantel=' + idDeptoPlantel
                        , success: function (response) {
                            $('#wrapper').find('#divGrupos').html(response);
                        }});
                }
            }
            function getSubcategoria(idCategoria) {
                if (idCategoria !== null) {
                    $.ajax({type: 'POST'
                        , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                        , cache: false
                        , async: false
                        , url: "<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("ajaxFunctions")%>/getSubcategoria4Categoria.jsp"
                        , data: '<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idCategoria=' + idCategoria + '&onChange=getDetalle4Subcategoria'
                        , success: function (response) {
                            $('#wrapper').find('#divSubcategoria').html(response);
                        }});
                }
            }
            function getModelo(idMarca) {
                if (idMarca !== null) {
                    $.ajax({type: 'POST'
                        , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                        , cache: false
                        , async: false
                        , url: "<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("ajaxFunctions")%>/getModelo4Marca.jsp"
                        , data: '<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idMarca=' + idMarca
                        , success: function (response) {
                            $('#wrapper').find('#divModelo').html(response);
                        }});
                }
            }
            function getDetalle4Subcategoria(idSubcategoria) {
                if (idSubcategoria !== null) {
                    $.ajax({type: 'POST'
                        , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                        , cache: false
                        , async: false
                        , url: "<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("ajaxFunctions")%>/getDetalle4Subcategoria.jsp"
                        , data: '<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idSubcategoria=' + idSubcategoria
                        , success: function (response) {
                            $('#wrapper').find('#divDetalles').html(response);
                        }});
                }
            }
            function getDescripcionModelo(idModelo) {
                if (idModelo !== null) {
                    $.ajax({type: 'POST'
                        , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                        , cache: false
                        , async: false
                        , url: "<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("ajaxFunctions")%>/getDescripcion4Modelo.jsp"
                        , data: '<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idModelo=' + idModelo
                        , success: function (response) {
                            $('#wrapper').find('#divDescModelo').html(response);
                        }});
                }
            }

        </script>
    </head>
    <body 
        <%

            if (request.getParameter("idDepartamento") == null) {
        %>
        onload="getDepartamento(document.getElementById('idPlantel').value);"
        <%
            }
        %>  

        >
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
                                <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaBien.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idPlantel=<%=request.getParameter("idPlantel")%>">Catálogo de Bienes</a> 
                                > Nuevo Bien
                            </td>
                            <td width="36" align="right" valign="top">
                                <script language="JavaScript" src="<%=PageParameters.getParameter("jsRcs")%>/funcionDate.js" type="text/javascript"></script>
                            </td>
                        </tr>                        
                    </table>
                    <form name="insertBien" method="post" action="" enctype="application/x-www-form-urlencoded" id="insertBien">
                        <input type="hidden" name="FormFrom" value="insertBien"/>
                        <fieldset>
                            <legend>Nuevo Bien</legend>
                            <div id="divEsentialData">
                                <jsp:include page='<%=PageParameters.getParameter("ajaxFunctions") + "/getSelectPlantelSection.jsp"%>' flush = 'true'>
                                    <jsp:param name='plantelActual' value='<%=request.getParameter("idPlantel") != null ? request.getParameter("idPlantel") : WebUtil.encode(session, session.getAttribute("FK_ID_Plantel"))%>' />
                                    <jsp:param name='onChange' value='getDepartamento' />
                                </jsp:include>
                                <input type="hidden" name="<%=WebUtil.encode(session, "imix")%>" value="<%=WebUtil.encode(session, UTime.getTimeMilis())%>"/>
                                <div>
                                    <label for="idCategoria">*Categoría</label>
                                    <%
                                        Iterator t = QUID.select_Categoria().iterator();
                                    %>
                                    <select name="idCategoria" onchange="getSubcategoria(this.value);">
                                        <option value=""></option>  
                                        <%
                                            while (t.hasNext()) {
                                                LinkedList datos = (LinkedList) t.next();
                                        %>
                                        <option value="<%=WebUtil.encode(session, datos.get(0))%>"><%=datos.get(1)%></option>
                                        <%
                                            }
                                        %>
                                    </select>
                                </div>
                                <div id="divSubcategoria"></div>
                                <div>
                                    <label for="idMarca">*Marca</label>
                                    <%
                                        t = QUID.select_Marca().iterator();
                                    %>
                                    <select name="idMarca" onchange="getModelo(this.value);">
                                        <option value=""></option>  
                                        <%
                                            while (t.hasNext()) {
                                                LinkedList datos = (LinkedList) t.next();
                                        %>
                                        <option value="<%=WebUtil.encode(session, datos.get(0))%>"><%=datos.get(1)%></option>
                                        <%
                                            }
                                        %>
                                    </select>
                                </div>
                                <div id="divModelo"></div>
                                <div>
                                    <label for="noSerie">*No. de Serie</label>
                                    <input type="text" name="noSerie" size="30" id="noSerie">
                                </div>
                                <div id="divContinuar">
                                    <input type="button" value="Continuar" name="Continuar" onclick="sendSerial('divEsentialData')">
                                </div> 
                                <div id="divModelosPosibles"></div>
                            </div>
                            <div id="divFormData" class="divOculto">
                                <div id="divDepartamento">
                                    <%
                                        if (request.getParameter("idDepartamento") != null) {
                                            LinkedList idDepto = QUID.select_DepartamentoPlantel(WebUtil.decode(session, request.getParameter("idDepartamento")));

                                    %><label for="idDepartamento">*Departamento</label>
                                    <select name="idDepartamento"  onchange="getGrupos(this.value)" id="idDepartamento">
                                        <option value=""></option>
                                        <option value="<%=WebUtil.encode(session, idDepto.get(0))%>" selected><%=idDepto.get(3)%></option>
                                    </select>
                                    <%
                                        }
                                    %>
                                </div>
                                <div id="divGrupos">
                                    <%
                                        if (request.getParameter("idPdg") != null) {
                                            LinkedList grupo = QUID.select_Grupo4PDG(WebUtil.decode(session, request.getParameter("idPdg")));
                                    %>
                                    <label for="idGrupo">Agrupación</label>
                                    <select name="idGrupo">
                                        <option value=""></option>
                                        <option value="<%=WebUtil.encode(session, grupo.get(0))%>" selected><%=grupo.get(1)%></option>
                                    </select>
                                    <%
                                        }
                                    %>
                                </div>


                                <div>
                                    <label for="idTipoCompra">*Tipo de compra</label>
                                    <%
                                        t = QUID.select_Tipo_Compra().iterator();
                                    %>
                                    <select name="idTipoCompra" onchange="">
                                        <option value=""></option>  
                                        <%
                                            while (t.hasNext()) {
                                                LinkedList datos = (LinkedList) t.next();
                                        %>
                                        <option value="<%=WebUtil.encode(session, datos.get(0))%>"><%=datos.get(1)%></option>
                                        <%
                                            }
                                        %>
                                    </select>
                                </div>
                                <div>
                                    <label for="descripcion">Descripción del bien/artículo</label>
                                    <textarea  id="descripcion" name="descripcion" cols="30" rows="5"></textarea>
                                </div>
                                <div>
                                    <label for="noDictamen">No. de Dictamen</label>
                                    <input type="text" name="noDictamen" size="30" id="noDictamen">
                                </div>
                                <div>
                                    <label for="noFactura">No. de Factura</label>
                                    <input type="text" name="noFactura" size="30" id="noFactura">
                                </div>
                                <div class="divControlHelp">
                                    <div id="divAyudaNoSerie" class="div4HelpButton" >
                                        <img  id="botonAyuda"  src="<%=PageParameters.getParameter("imgRsc") + "/icons/help-browser.png"%>" width="24px" height="24px" onclick="showButton('divInfoNoSerie');
                                                hideButton('divAyudaNoSerie');" title="Abrir ayuda">
                                    </div>
                                    <label for="noInventario">No. de Inventario</label>
                                    <input type="text" name="noInventario" size="30" id="noInventario">
                                    <div id="divInfoNoSerie" class="msginfo div4HelpInfo" onmouseover="showButton('botonCerrarInfoSerie');" onmouseout="hideButton('botonCerrarInfoSerie');">
                                        <div class="deleteBar div4HelpInfoCloseButton" >
                                            <img  class="div4HelpCloseButton" align="right" id="botonCerrarInfoSerie"  src="<%=PageParameters.getParameter("imgRsc") + "/icons/delete.png"%>" width="16px" height="16px" onclick="hideButton('divInfoNoSerie');
                                                    showButton('divAyudaNoSerie');" title="Cerrar ayuda">
                                        </div>
                                        <p>
                                            &nbsp;Si desconoce el número de inventario el sistema guardará 00000.
                                        </p>
                                    </div>
                                </div>
                                <div>
                                    <label for="fechaCompra">Fecha de Compra (aaaa-mm-dd)</label>
                                    <input type="text" class="w8em format-y-m-d divider-dash highlight-days-67" id="fechaCompra"   name="fechaCompra" value="" size="10" MAXLENGTH="10">
                                </div>
                                <div>
                                    <label for="status">*Estatus</label>
                                    <select name="status" id="status">
                                        <option value=""></option>
                                        <option value="<%=WebUtil.encode(session, "Funcional")%>">Funcional</option>
                                        <option value="<%=WebUtil.encode(session, "Dañado")%>">Dañado</option>
                                        <option value="<%=WebUtil.encode(session, "Desuso")%>">Desuso</option>
                                        <option value="<%=WebUtil.encode(session, "En reparación")%>">En reparación</option>
                                        <option value="<%=WebUtil.encode(session, "Posible baja")%>">Posible baja</option>
                                        <option value="<%=WebUtil.encode(session, "No Funciona")%>">No Funciona</option>
                                        <option value="<%=WebUtil.encode(session, "No Funciona Reparable")%>">No Funciona Reparable</option>
                                        <!--<option value="<%=WebUtil.encode(session, "Baja")%>">Baja</option>-->
                                    </select>
                                </div>
                                <div>
                                    <label for="observaciones">Observaciones</label>
                                    <textarea  name="observaciones" cols="30" rows="5" id="observaciones"></textarea>
                                </div>
                                <div id="botonEnviarDiv" >
                                    <input type="button" value="Guardar" name="Enviar" onclick="enviarInfo(document.getElementById('insertBien'));"/>
                                    <input type="button" value="Nuevo" name="Nuevo" onclick="resetForm();"/>
                                </div>   
                            </div>
                        </fieldset>
                        <div id="divFormEspecificaciones" class="divOculto">
                            <fieldset>
                                <legend>Especificaciones</legend>
                                <div id="divDetalles" align="center"></div>
                            </fieldset>
                            <div id="botonEnviarDiv" >
                                <input type="button" value="Guardar" name="Enviar" onclick="enviarInfo(document.getElementById('insertBien'));">
                            </div> 
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