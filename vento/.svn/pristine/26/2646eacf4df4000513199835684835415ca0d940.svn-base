<%@page import="jspread.core.util.SystemUtil"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("ConsultaBien");

                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                    Iterator it = null;
                    LinkedList listAux = null;
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Editar Bien</title>
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
                        , data: '<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idCategoria=' + idCategoria
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
            function popupArchivos() {
                newwindow = window.open(
                        '<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/Insert_ObjetoArchivo.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idObjeto=<%=request.getParameter("idBien")%>&nombreObjeto=<%=WebUtil.encode(session, "BIEN")%>'
                                        , 'Archivos'
                                        , 'width='+ (screen.availWidth - 10).toString()+',height='+ (screen.availHeight - 122).toString()+',toolbar=0,menubar=0,resizable=1,scrollbars=1,status=1,location=0,directories=0,top=50'
                                        );
                                if (window.focus) {
                                    newwindow.focus();
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
                                <a class="NVL" href="<%=PageParameters.getParameter("mainMenu")%>"> Menú Principal</a>
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaBien.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%><%=request.getParameter("idPlantel") != null ? "&idPlantel=" + request.getParameter("idPlantel") : ""%>">Catálogo de Bienes</a> 
                                > Editar Bien
                            </td>
                            <td width="36" align="right" valign="top">
                                <script language="JavaScript" src="<%=PageParameters.getParameter("jsRcs")%>/funcionDate.js" type="text/javascript"></script>
                            </td>
                        </tr>                        
                    </table>
                    <form name="updateBien" method="post" action="" enctype="application/x-www-form-urlencoded" id="updateBien">
                        <input type="hidden" name="<%=WebUtil.encode(session, "imix")%>" value="<%=WebUtil.encode(session, UTime.getTimeMilis())%>"/>
                        <input type="hidden" name="FormFrom" value="updateBien"/>
                        <input type="hidden" name="idBien" value="<%=request.getParameter("idBien")%>"/>
                        <fieldset>
                            <%
                                LinkedList datosBien = QUID.select_Bien(WebUtil.decode(session, request.getParameter("idBien")));
                            %>
                            <legend>Editar Bien</legend>
                            <jsp:include page='<%=PageParameters.getParameter("ajaxFunctions") + "/getSelectPlantelSection.jsp"%>' flush = 'true'>
                                <jsp:param name='plantelActual' value='<%=WebUtil.encode(session, datosBien.get(0))%>' />
                                <jsp:param name='onChange' value='getDepartamento' />
                            </jsp:include>
                            <div id="divDepartamento">
                                <label for="idDepartamento">*Departamento</label>
                                <select name="idDepartamento" onchange="getGrupos(this.value);">
                                    <option value="<%=WebUtil.encode(session, datosBien.get(20))%>"><%=datosBien.get(22)%></option>
                                    <%
                                        Iterator t = QUID.select_Departamento_PlantelXID(datosBien.get(0).toString()).iterator();
                                        while (t.hasNext()) {
                                            LinkedList datos = (LinkedList) t.next();
                                    %>
                                    <option value="<%=WebUtil.encode(session, datos.get(0))%>"><%=datos.get(3)%></option>
                                    <%
                                        }
                                    %>
                                </select>
                            </div>
                            <div id="divGrupos">
                                <%
                                    if (datosBien.get(24) != null) {
                                %>
                                <label for="idGrupo">Agrupación</label>
                                <select name="idGrupo">
                                    <option value="<%=WebUtil.encode(session, datosBien.get(24))%>"><%=datosBien.get(25)%></option>
                                    <%
                                        t = QUID.select_Grupo4DeptoPlantel(datosBien.get(20).toString()).iterator();
                                        while (t.hasNext()) {
                                            LinkedList datos = (LinkedList) t.next();
                                    %>
                                    <option value="<%=WebUtil.encode(session, datos.get(0))%>"><%=datos.get(1)%></option>
                                    <%
                                        }
                                    %>   
                                </select>
                                <%
                                    }
                                %>
                            </div>
                            <div>
                                <label for="idCategoria">*Categoria</label>
                                <%
                                    t = QUID.select_Categoria().iterator();
                                %>
                                <select name="idCategoria" onchange="getSubcategoria(this.value);">
                                    <option value=""></option>  
                                    <%
                                        while (t.hasNext()) {
                                            LinkedList datos = (LinkedList) t.next();
                                    %>
                                    <option value="<%=WebUtil.encode(session, datos.get(0))%>" <%=datos.get(0).toString().equals(datosBien.get(2).toString()) ? "selected" : ""%>><%=datos.get(1)%></option>
                                    <%
                                        }
                                    %>
                                </select>
                            </div>
                            <div id="divSubcategoria">
                                <label for="idSubcategoria">*Subcategoria</label>
                                <select name="idSubcategoria">
                                    <option value="<%=WebUtil.encode(session, datosBien.get(4))%>"><%=datosBien.get(5)%></option>
                                    <%
                                        t = QUID.select_SubCategoria4Categoria(datosBien.get(2).toString()).iterator();
                                        while (t.hasNext()) {
                                            LinkedList datos = (LinkedList) t.next();
                                    %>
                                    <option value="<%=WebUtil.encode(session, datos.get(0))%>"><%=datos.get(1)%></option>
                                    <%
                                        }
                                    %>
                                    %>
                                </select>
                            </div>
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
                                    <option value="<%=WebUtil.encode(session, datos.get(0))%>" <%=datos.get(0).toString().equals(datosBien.get(6).toString()) ? "selected" : ""%>><%=datos.get(1)%></option>
                                    <%
                                        }
                                    %>
                                </select>
                            </div>
                            <div id="divModelo">
                                <div class="divControlHelp">
                                    <div id="divAyudaModelo" class="div4HelpButton" >
                                        <img  id="botonAyuda"  src="<%=PageParameters.getParameter("imgRsc") + "/icons/help-browser.png"%>" width="24px" height="24px" onclick="showButton('divInfoModelo');
                                                hideButton('divAyudaModelo');" title="Abrir ayuda">
                                    </div>
                                    <label for="idModelo">*Modelo</label>
                                    <select name="idModelo" onchange="getDescripcionModelo(this.value)">
                                        <option value="<%=WebUtil.encode(session, datosBien.get(8))%>"><%=datosBien.get(9)%></option>
                                        <%
                                            t = QUID.select_Modelo4Marca(datosBien.get(6).toString()).iterator();
                                            while (t.hasNext()) {
                                                LinkedList datos = (LinkedList) t.next();
                                        %>
                                        <option value="<%=WebUtil.encode(session, datos.get(0))%>"><%=datos.get(1)%></option>
                                        <%
                                            }
                                        %>
                                    </select>
                                    <div id="divInfoModelo" class="msginfo div4HelpInfo"  onmouseover="showButton('botonCerrarInfoModelo');" onmouseout="hideButton('botonCerrarInfoModelo');">
                                        <div class="deleteBar div4HelpInfoCloseButton" >
                                            <img  class="div4HelpCloseButton" align="right" id="botonCerrarInfoModelo"  src="<%=PageParameters.getParameter("imgRsc") + "/icons/delete.png"%>" width="16px" height="16px" onclick="hideButton('divInfoModelo');
                                                    showButton('divAyudaModelo');" title="Cerrar ayuda">
                                        </div>
                                        <p id="divDescModelo"></p>
                                    </div>
                                </div>
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
                                    <option value="<%=WebUtil.encode(session, datos.get(0))%>" <%=datos.get(0).toString().equals(datosBien.get(23).toString()) ? "selected" : ""%>><%=datos.get(1)%></option>
                                    <%
                                        }
                                    %>
                                </select>
                            </div>
                            <div>
                                <label for="descripcion">Descripción</label>
                                <textarea  name="descripcion" cols="30" rows="5"><%=datosBien.get(11)%></textarea>
                            </div>
                            <div>
                                <label for="noDictamen">No. de Dictamen</label>
                                <input type="text" name="noDictamen" size="30" value="<%=datosBien.get(13).toString().trim()%>">
                            </div>
                            <div>
                                <label for="noFactura">No. de Factura</label>
                                <input type="text" name="noFactura" size="30" value="<%=datosBien.get(14).toString().trim()%>">
                            </div>
                            <div>
                                <label for="noSerie">*No. de Serie</label>
                                <input type="text" name="noSerie" size="30" value="<%=datosBien.get(12)%>">
                            </div>
                            <div class="divControlHelp">
                                <div id="divAyuda" class="div4HelpButton" >
                                    <img  id="botonAyuda"  src="<%=PageParameters.getParameter("imgRsc") + "/icons/help-browser.png"%>" width="24px" height="24px" onclick="document.getElementById('divInfo').style.display = 'block';
                                            document.getElementById('divAyuda').style.display = 'none'" title="Abrir ayuda">
                                </div>

                                <label for="noInventario">No. de Inventario</label>
                                <input type="text" name="noInventario" size="30" value="<%=datosBien.get(15)%>">

                                <div id="divInfo" class="msginfo div4HelpInfo" onmouseover="showButton('botonCerrar');" onmouseout="hideButton('botonCerrar');">
                                    <div class="deleteBar div4HelpInfoCloseButton" >
                                        <img  class="div4HelpCloseButton" align="right" id="botonCerrar"  src="<%=PageParameters.getParameter("imgRsc") + "/icons/delete.png"%>" width="16px" height="16px" onclick="document.getElementById('divInfo').style.display = 'none';
                                                document.getElementById('divAyuda').style.display = 'block'" title="Cerrar ayuda">
                                    </div>
                                    <p>
                                        &nbsp;Si desconoce el número de inventario el sistema guardará 00000.
                                    </p>
                                </div>

                            </div>
                            <div>
                                <label for="fechaCompra">Fecha de Compra (aaaa-mm-dd)</label>
                                <input type="text" class="w8em format-y-m-d divider-dash highlight-days-67" id="fechaCompra"   name="fechaCompra" value="<%=datosBien.get(16).toString().trim()%>" size="10" MAXLENGTH="10">
                            </div>
                            <div>
                                <label for="status">*Estatus</label>
                                <select name="status">
                                    <option value="<%=WebUtil.encode(session, datosBien.get(18))%>"><%=datosBien.get(18)%></option>
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
                                <textarea  name="observaciones" cols="30" rows="5"><%=datosBien.get(19).toString().trim()%></textarea>
                            </div>
                            <div id="botonEnviarDiv" >
                                <input type="hidden" name="idGrupoBien" value="<%=datosBien.get(26) != null ? WebUtil.encode(session, datosBien.get(26)) : ""%>"/>
                                <input type="hidden" name="idGrupoActual" value="<%=datosBien.get(24) != null ? WebUtil.encode(session, datosBien.get(24)) : ""%>"/>
                                <input type="button" value="Guardar" name="Enviar" onclick="enviarInfo(document.getElementById('updateBien'));"/>
                            </div>      
                        </fieldset>
                    </form>
                    <a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaBDS.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idBien=<%=request.getParameter("idBien")%>&idPlantel=<%=request.getParameter("idPlantel")%>" target="_blank">
                        <button>Ver Especificaciones</button>
                    </a>
                    <a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaPBD.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idBien=<%=request.getParameter("idBien")%>&idPlantel=<%=request.getParameter("idPlantel")%>">
                        <button>Datos del Proveedor</button>
                    </a>
                    <a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaGarantia.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idBien=<%=request.getParameter("idBien")%>&idPlantel=<%=request.getParameter("idPlantel")%>">
                        <button>Garantía</button>
                    </a>
                    <a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/InsertBaja.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idBien=<%=request.getParameter("idBien")%>&idPlantel=<%=request.getParameter("idPlantel")%>">
                        <button>Dar de Baja</button>
                    </a>
                    <a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaCRB.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idBien=<%=request.getParameter("idBien")%>&idPlantel=<%=request.getParameter("idPlantel")%>">
                        <button>CheckList</button>
                    </a>
                    <a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/InsertBitacoraIncidente.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idBien=<%=request.getParameter("idBien")%>&idPlantel=<%=request.getParameter("idPlantel")%>">
                        <button>Reportar Incidente</button>
                    </a>
                    <a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaResguardo4Bien.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idBien=<%=request.getParameter("idBien")%>&idPlantel=<%=request.getParameter("idPlantel")%>" target="_blank">
                        <button>Datos de Resguardo</button>
                    </a>
                    <a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("prints")%>/BarCode4Bien.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idBien=<%=request.getParameter("idBien")%>" target="_blank">
                        <button>Etiqueta</button>
                    </a>
                    
                        <button onclick="popupArchivos();">Archivos</button>
                    
                    <%
                        if (SystemUtil.haveAcess("bajaDirecta", userAccess)) {
                    %>

                    <button onclick="setEstatus('<%=request.getParameter("idBien")%>', '<%=WebUtil.encode(session, "BAJA")%>')">Baja Directa</button>

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