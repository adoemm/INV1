<%@page import="jspread.core.util.SystemUtil"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("UpdateSoftware");

                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                    Iterator it = null;
                    LinkedList listAux = null;
                    LinkedList listAux1 = null;
                    LinkedList listAux2 = null;
                    LinkedList listAux3 = null;
                    LinkedList listAuxProveedor = null;
                    LinkedList listAuxPlantel = null;
                    LinkedList listAuxLicencia = null;
%>  
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Editar Software</title>
        <jsp:include page='<%=PageParameters.getParameter("globalLibs")%>'/>        
        <jsp:include page='<%=PageParameters.getParameter("styleFormCorrections")%>'/>   
        <jsp:include page='<%= PageParameters.getParameter("ajaxFunctions") + "/tablaCapturaCSS.jsp"%>'/>
        <script type="text/javascript" language="javascript" charset="utf-8">
            function getSoporte(opcionalSoporte) {

                var ID_Software = '<%=request.getParameter("ID_Software")%>';
                if (opcionalSoporte === '<%=WebUtil.encode(session, "1")%>') {

                    document.getElementById('divdatosSoporte').innerHTML = '';

                    var response = $.ajax({
                        type: 'get',
                        contentType: 'application/x-www-form-urlencoded;charset=utf-8',
                        cache: false,
                        url: "<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("ajaxFunctions")%>/datosSoporteActualizar.jsp",
                        data: '<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, "" + UTime.getTimeMilis())%>&opcionalSoporte=' + opcionalSoporte + '&ID_Software=' + ID_Software,
                        async: false,
                        success: function(data) {
                            // Hagamos algo maravilloso con estos nuevos datos
                        }
                    }).responseText;
                    $('#divdatosSoporte').fadeIn(0);
                    document.getElementById('divdatosSoporte').innerHTML = response;
                } else {
                    document.getElementById('divdatosSoporte').innerHTML = '';
                }
            }
            function enviarInfo() {
                $.ajax({type: 'POST'
                    , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                    , cache: false
                    , async: false
                    , url: '<%=PageParameters.getParameter("mainController")%>'
                    , data: $('#updateSoftware').serialize()
                    , success: function(response) {
                        $('#wrapper').find('#divResult').html(response);
                    }});
            }
            function getDocumentos(mainID) {
                $('#wrapper').find('#divDocumentos').html('');
                if (mainID !== null) {
                    $.ajax({type: 'POST'
                        , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                        , cache: false
                        , async: true
                        , url: "<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("ajaxFunctions")%>/getArchivos.jsp"
                        , data: '<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&FormFrom=file4Software&mainID=' + mainID+'&consulta=<%=WebUtil.encode(session,"si")%>'
                        , success: function(response) {
                            $('#wrapper').find('#divDocumentos').html(response);
                        }});
                }
            }
        </script>
    </head>
    <body onload="getSoporte(document.getElementById('SoporteTecnico').value);getDocumentos('<%=request.getParameter("ID_Software")%>')">
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
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaSoftware.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>">Catálogo de Software</a> 
                                > Editar Software
                            </td>
                            <td width="36" align="right" valign="top">
                                <script language="JavaScript" src="<%=PageParameters.getParameter("jsRcs")%>/funcionDate.js" type="text/javascript"></script>
                            </td>
                        </tr>                        
                    </table>
                    <form name="updateSoftware" method="post" action="" enctype="application/x-www-form-urlencoded" id="updateSoftware">
                        <input type="hidden" name="imix" value="<%=WebUtil.encode(session, UTime.getTimeMilis())%>"/>
                        <input type="hidden" name="ID_Software" value="<%=request.getParameter("ID_Software")%>"/> 
                        <input type="hidden" name="FormFrom" value="updateSoftware"/>

                        <%
                            it = null;
                            listAux = (LinkedList) QUID.select_SoftwareXID(WebUtil.decode(session, request.getParameter("ID_Software"))).get(0);
                        %>
                        <fieldset>
                            <legend>Editar Software</legend>
                            <div>
                                <label for="FK_ID_Plantel">*Plantel</label>
                                <select name="FK_ID_Plantel">
                                    <option selected value="<%=WebUtil.encode(session, "" + listAux.get(30))%>"><%=listAux.get(29)%></option>
                                    <%
                                        it = null;
                                        it = QUID.select_NombrePlantelID(session.getAttribute("FK_ID_Plantel").toString(), SystemUtil.haveAcess("verTodo", userAccess)).iterator();
                                        while (it.hasNext()) {
                                            listAuxPlantel = null;
                                            listAuxPlantel = (LinkedList) it.next();
                                    %>
                                    <option value="<%=WebUtil.encode(session, "" + listAuxPlantel.get(0))%>"><%=listAuxPlantel.get(1)%></option>
                                    <%
                                        }
                                    %>
                                </select>
                            </div>  
                            <div>
                                <label for="FK_ID_NombreSoftware">*Seleccione el nombre del software</label>
                                <select name="FK_ID_NombreSoftware">
                                    <option selected value="<%=WebUtil.encode(session, "" + listAux.get(26))%>"><%=listAux.get(25)%></option>
                                    <%
                                        it = null;
                                        it = QUID.select_getRowsNombreSoftware().iterator();
                                        while (it.hasNext()) {
                                            listAux3 = null;
                                            listAux3 = (LinkedList) it.next();
                                    %>
                                    <option value="<%=WebUtil.encode(session, "" + listAux3.get(0))%>"><%=listAux3.get(1)%></option>
                                    <%
                                        }

                                    %>
                                </select>
                            </div>
                            <div>
                                <label for="version">*Version</label>          
                                <input name="version" type="text" id="version" value="<%=listAux.get(2)%>" size="50" >
                            </div>
                            <div>
                                <label for="serial">Serial</label>          
                                <input name="serial" type="text" id="serial" value="<%=listAux.get(3)%>" size="50" >
                            </div>
                            <div>
                                <label for="FK_ID_Tipo_Compra">*Tipo de compra</label>
                                <select name="FK_ID_Tipo_Compra">
                                    <option selected value="<%=WebUtil.encode(session, "" + listAux.get(23))%>"><%=listAux.get(24)%></option>
                                    <%
                                        it = null;
                                        it = QUID.select_Tipo_Compra().iterator();
                                        while (it.hasNext()) {
                                            listAux2 = null;
                                            listAux2 = (LinkedList) it.next();
                                    %>
                                    <option value="<%=WebUtil.encode(session, "" + listAux2.get(0))%>"><%=listAux2.get(1)%></option>
                                    <%
                                        }
                                    %>
                                </select>
                            </div>
                            <div>
                                <label for="FK_ID_Proveedor">*Proveedor</label>
                                <select name="FK_ID_Proveedor">
                                    <option selected value="<%=WebUtil.encode(session, "" + listAux.get(34))%>"><%=listAux.get(35)%></option>
                                    <%
                                        it = null;
                                        it = QUID.select_Proveedor().iterator();
                                        while (it.hasNext()) {
                                            listAuxProveedor = null;
                                            listAuxProveedor = (LinkedList) it.next();
                                    %>
                                    <option value="<%=WebUtil.encode(session, "" + listAuxProveedor.get(0))%>"><%=listAuxProveedor.get(1)%></option>
                                    <%
                                        }
                                    %>
                                </select>
                            </div>
                            <div>
                                <label for="noDictamen">No. Dictamen</label>          
                                <input name="noDictamen" type="text" id="noDictamen" value="<%=listAux.get(6)%>" size="50" >
                            </div>
                            <div>
                                <label for="noFactura">*No. Factura</label>          
                                <input name="noFactura" type="text" id="noFactura" value="<%=listAux.get(36)%>" size="50" >
                            </div>
                            <div>
                                <label for="noContrato">No. de Contrato</label>          
                                <input name="noContrato" type="text" id="noContrato" value="<%=listAux.get(37)%>" size="50" >
                            </div>
                            <div>
                                <label for="noAutorizacion">No. de Autorización</label>          
                                <input name="noAutorizacion" type="text" id="noAutorizacion" value="<%=listAux.get(38)%>" size="50" >
                            </div>

                            <div>
                                <label for="FK_ID_Tipo_Software">*Tipo de Software</label>
                                <select name="FK_ID_Tipo_Software">
                                    <option selected value="<%=WebUtil.encode(session, "" + listAux.get(19))%>"><%=listAux.get(20)%></option>
                                    <%
                                        it = null;
                                        it = QUID.select_getRowsTipo_Software().iterator();
                                        while (it.hasNext()) {
                                            listAux1 = null;
                                            listAux1 = (LinkedList) it.next();
                                    %>
                                    <option value="<%=WebUtil.encode(session, "" + listAux1.get(0))%>"><%=listAux1.get(1)%></option>
                                    <%
                                        }
                                    %>
                                </select>
                            </div>
                            <div>
                                <label for="fechaAdquisicion">*Fecha de Adquisición</label>
                                <input type="text" class="w8em format-y-m-d divider-dash highlight-days-67" id="fechaAdquisicion" onmouseover="calendarField(id)" readonly="readonly" name="fechaAdquisicion" value="<%=listAux.get(5)%>" size="10" MAXLENGTH="10">
                            </div>
                            <div>
                                <label for="FK_ID_Licencia">*Tipo de Licencia</label>
                                <select name="FK_ID_Licencia">
                                    <option selected value="<%=WebUtil.encode(session, "" + listAux.get(21))%>"><%=listAux.get(22)%></option>
                                    <%
                                        it = null;
                                        it = QUID.select_getRowsLicencia().iterator();
                                        while (it.hasNext()) {
                                            listAuxLicencia = null;
                                            listAuxLicencia = (LinkedList) it.next();
                                    %>
                                    <option value="<%=WebUtil.encode(session, "" + listAuxLicencia.get(0))%>"><%=listAuxLicencia.get(1)%></option>
                                    <%
                                        }
                                    %>
                                </select>
                            </div>
                            <div>
                                <label for="noLicencias">*Número de licencias</label>
                                <input name="noLicencias" type="text" id="noLicencias" value="<%=listAux.get(7)%>" size="50" >
                            </div>  
                            <div>
                                <label for="noLicenciasAsignadas">*Licencias asignadas</label>
                                <input name="noLicenciasAsignadas" type="text" id="noLicenciasAsignadas" value="<%=listAux.get(28)%>" size="50" >
                            </div> 
                            <div>
                                <label for="aniosLicencia">Duración de la Licencia(meses)</label>
                                <input name="aniosLicencia" type="text" id="aniosLicencia" value="<%=listAux.get(40)%>" size="50" >
                            </div>
                            <div>
                                <label for="fechaVencimiento">Fecha de Vencimiento</label>
                                <input type="text" class="w8em format-y-m-d divider-dash highlight-days-67" id="fechaVencimiento" onmouseover="calendarField(id)" readonly="readonly" name="fechaVencimiento" value="<%=listAux.get(4)%>" size="10" MAXLENGTH="10">
                            </div>
                            <div>
                                <label for="noActualizacionesPermitidas">No. de Actialuzaciones Permitidas</label>
                                <input name="noActualizacionesPermitidas" type="text" id="noActualizacionesPermitidas" value="<%=listAux.get(41)%>" size="50" >
                            </div>
                            <div>
                                <label for="upgrade">Upgrade</label>
                                <%if (listAux.get(42).equals("1")) {%>
                                <input type="checkbox" name="upgrade" value="" id="upgrade" checked>
                                <%} else {%>
                                <input type="checkbox" name="upgrade" value="" id="upgrade">
                                <%}%>
                            </div>
                            <div>
                                <label for="degrade">Degrade</label>
                                <%if (listAux.get(43).equals("1")) {%>
                                <input type="checkbox" name="degrade" value="" id="degrade" checked>
                                <%} else {%>
                                <input type="checkbox" name="degrade" value="" id="degrade" >
                                <%}%>
                            </div>                    
                            <div>
                                <label for="SoporteTecnico">*Cuenta con Soporte Técnico</label>
                                <select name="SoporteTecnico" id="SoporteTecnico" onchange="getSoporte(this.value);" >  
                                    <%if (listAux.get(11).equals("1")) {%>
                                    <option selected value="<%=WebUtil.encode(session, listAux.get(11))%>">Sí</option>
                                    <%} else if (listAux.get(11).equals("0")) {%>
                                    <option selected value="<%=WebUtil.encode(session, listAux.get(11))%>">No</option>
                                    <%}%>
                                    <option value=<%=WebUtil.encode(session, "1")%>>Sí</option>
                                    <option value=<%=WebUtil.encode(session, "0")%>>No</option>
                                </select>
                            </div>     
                            <div id="divdatosSoporte" name="divdatosSoporte">
                            </div>
                            <div>
                                <label for="hddRequerido">Disco duro requerido</label>
                                <input name="hddRequerido" type="text" id="hddRequerido" value="<%=listAux.get(8)%>" size="50" >
                            </div>
                            <div>
                                <label for="soRequerido">Sistema Operativo Requerido</label>          
                                <input name="soRequerido" type="text" id="soRequerido" value="<%=listAux.get(10)%>" size="50" >
                            </div> 
                            <div>
                                <label for="ramRequerida">Memoria RAM requerida</label>
                                <input name="ramRequerida" type="text" id="ramRequerida" value="<%=listAux.get(9)%>" size="50" >
                            </div> 
                            <div>
                                <label for="nombreResponsable">*Responsable del resguardo</label>
                                <input type="text" name="nombreResponsable" value="<%=listAux.get(32)%>"  size="25">
                            </div>
                            <div>
                                <label for="fechaInstalacion">Fecha de instalación</label>
                                <input type="text" class="w8em format-y-m-d divider-dash highlight-days-67" id="fechaInstalacion" onmouseover="calendarField(id)" readonly="readonly" name="fechaInstalacion" value="<%=listAux.get(39)%>" size="10" MAXLENGTH="10">
                            </div>
                            <div>
                                <label for="status">*Estatus</label>
                                <select name="status" id="status">                                            

                                    <%if (listAux.get(27).equals("1")) {%>
                                    <option selected value="<%=WebUtil.encode(session, "" + listAux.get(27))%>">Activo</option>
                                    <%} else if (listAux.get(27).equals("2")) {%>
                                    <option selected value="<%=WebUtil.encode(session, "" + listAux.get(27))%>">Inactivo</option>
                                    <%} else if (listAux.get(27).equals("3")) {%>
                                    <option selected value="<%=WebUtil.encode(session, "" + listAux.get(27))%>">Sin instalar</option>
                                    <%} else if (listAux.get(27).equals("4")) {%>
                                    <option selected value="<%=WebUtil.encode(session, "" + listAux.get(27))%>">Instalado</option>
                                    <%} else if (listAux.get(27).equals("5")) {%>
                                    <option selected value="<%=WebUtil.encode(session, "" + listAux.get(27))%>">Obsoleto</option>
                                    <%}%>
                                    <option value=<%=WebUtil.encode(session, "1")%>>Activo</option>
                                    <option value=<%=WebUtil.encode(session, "2")%>>Inactivo </option>
                                    <option value=<%=WebUtil.encode(session, "3")%>>Sin instalar</option>
                                    <option value=<%=WebUtil.encode(session, "4")%>>Instalado</option>
                                    <option value=<%=WebUtil.encode(session, "5")%>>Obsoleto</option>

                                </select>
                            </div>
                            <div>
                                <label for="observaciones">Observaciones</label>
                                <textarea name="observaciones" rows="4" cols="50" size="25"><%=listAux.get(17)%></textarea> 
                            </div>      
                            <div id="botonEnviarDiv" >
                                <input type="button" value="Guardar" name="Enviar" onclick="enviarInfo();"/>
                                <input type="hidden" name="idSoftwarePlantel" value="<%=listAux.get(33) != null ? WebUtil.encode(session, listAux.get(33)) : ""%>"/>
                            </div>
                        </fieldset>
                    </form>
                    <div id="divDocumentos" align="center"></div>
                    <div id="divResult" >
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
%>                
<%@ include file="/gui/pageComponents/invalidUser.jsp"%>
<%    }
} else {
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
    }
%>