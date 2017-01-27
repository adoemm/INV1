<%@page import="jspread.core.util.SystemUtil"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("InsertSoftware");

                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                    Iterator it = null;
                    LinkedList listAux = null;
%>

<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Nuevo Software</title>
        <jsp:include page='<%=PageParameters.getParameter("globalLibs")%>'/>        
        <jsp:include page='<%=PageParameters.getParameter("styleFormCorrections")%>'/> 
        <script type="text/javascript" language="javascript" charset="utf-8">
            function getSoporte(opcionalSoporte) {

                if (opcionalSoporte === '<%=WebUtil.encode(session, "1")%>') {

                    document.getElementById('divdatosSoporte').innerHTML = '';

                    var response = $.ajax({
                        type: 'get',
                        contentType: 'application/x-www-form-urlencoded;charset=utf-8',
                        cache: false,
                        url: "<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("ajaxFunctions")%>/datosSoporte.jsp",
                        data: '<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, "" + UTime.getTimeMilis())%>&opcionalSoporte=' + opcionalSoporte,
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
                    , data: $('#insertSoftware').serialize()
                    , success: function(response) {
                        $('#wrapper').find('#divResult').html(response);
                    }});
            }
        </script>
        <script type="text/javascript" charset="utf-8">
            window.history.forward();
            function noBack() {
                window.history.forward();
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
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaSoftware.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>">Catálogo de Software</a> 
                                > Nuevo Software
                            </td>
                            <td width="36" align="right" valign="top">
                                <script language="JavaScript" src="<%=PageParameters.getParameter("jsRcs")%>/funcionDate.js" type="text/javascript"></script>
                            </td>
                        </tr>                        
                    </table>
                    <form name="insertSoftware" method="post" name="insertSoftware" action="" enctype="application/x-www-form-urlencoded" id="insertSoftware">
                        <input type="hidden" name="imix" value="<%=WebUtil.encode(session, UTime.getTimeMilis())%>"/>
                        <input type="hidden" name="FormFrom" value="insertSoftware"/>
                        <fieldset>
                            <legend>Nuevo Software</legend>
                            <div>
                                <label for="FK_ID_Plantel">*Plantel</label>
                                <select name="FK_ID_Plantel">
                                    <option value=""></option>
                                    <%
                                        it = null;
                                        it = QUID.select_NombrePlantelID(session.getAttribute("FK_ID_Plantel").toString(), SystemUtil.haveAcess("verTodo", userAccess)).iterator();
                                        while (it.hasNext()) {
                                            listAux = (LinkedList) it.next();
                                    %>
                                    <option value="<%=WebUtil.encode(session, "" + listAux.get(0))%>"><%=listAux.get(1)%></option>
                                    <%
                                        }
                                    %>
                                </select>
                            </div>
                            <div>
                                <label for="FK_ID_NombreSoftware">*Nombre de Software</label>
                                <select name="FK_ID_NombreSoftware">
                                    <option value=""></option>
                                    <%
                                        it = null;
                                        it = QUID.select_nombreSoftware().iterator();
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


                            <div>
                                <label for="version">*Version</label>          
                                <input name="version" type="text" id="version" value="" size="50" >
                            </div>
                            <div>
                                <label for="serial">Clave de activación o serial</label>          
                                <input name="serial" type="text" id="serial" value="" size="50" >
                            </div>
                            <div>
                                <label for="FK_ID_Tipo_Software">*Tipo de Software</label>
                                <select name="FK_ID_Tipo_Software">
                                    <option value=""></option>
                                    <%
                                        it = null;
                                        it = QUID.select_getRowsTipo_Software().iterator();
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
                            <div>
                                <label for="FK_ID_Tipo_Compra">*Tipo de compra</label>
                                <select name="FK_ID_Tipo_Compra">
                                    <option value=""></option>
                                    <%
                                        it = null;
                                        it = QUID.select_Tipo_Compra().iterator();
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
                            <div>
                                <label for="FK_ID_Proveedor">*Proveedor</label>
                                <select name="FK_ID_Proveedor">
                                    <option value=""></option>
                                    <%
                                        it = null;
                                        it = QUID.select_Proveedor().iterator();
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
                            <div>
                                <label for="noDictamen">No. de Dictamen</label>          
                                <input name="noDictamen" type="text" id="noDictamen" value="" size="50" >
                            </div>  
                            <div>
                                <label for="noFactura">*No de Factura</label>          
                                <input name="noFactura" type="text" id="noFactura" value="" size="50" >
                            </div>

                            <div>
                                <label for="noContrato">No. de Contrato</label>          
                                <input name="noContrato" type="text" id="noContrato" value="" size="50" >
                            </div>
                            <div>
                                <label for="noAutorizacion">No. de Autorización</label>          
                                <input name="noAutorizacion" type="text" id="noAutorizacion" value="" size="50" >
                            </div>
                            <div>
                                <label for="fechaAdquisicion">*Fecha de Adquisición</label>
                                <input type="text" class="w8em format-y-m-d divider-dash highlight-days-67" id="fechaAdquisicion" onmouseover="calendarField(id)" readonly="readonly" name="fechaAdquisicion" value="" size="10" MAXLENGTH="10">
                            </div>
                            <div>
                                <label for="FK_ID_Licencia">*Tipo de Licencia</label>
                                <select name="FK_ID_Licencia">
                                    <option value=""></option>
                                    <%
                                        it = null;
                                        it = QUID.select_getRowsLicencia().iterator();
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
                            <div>
                                <label for="noLicecencias">*Total de licencias adquiridas</label>
                                <input name="noLicencias" type="text" id="noLicencias" value="" size="50" >
                            </div>    
                            <div>
                                <label for="noLicenciasAsignadas">*Total licencias asignadas</label>
                                <input name="noLicenciasAsignadas" type="text" id="noLicenciasAsignadas" value="" size="50" >
                            </div>
                            <div>
                                <label for="aniosLicencia">Duración de la Licencia(meses)</label>
                                <input name="aniosLicencia" type="text" id="aniosLicencia" value="" size="50" >
                            </div>  
                            <div>
                                <label for="fechaVencimiento">Fecha de Vencimiento</label>
                                <input type="text" class="w8em format-y-m-d divider-dash highlight-days-67" id="fechaVencimiento" onmouseover="calendarField(id)" readonly="readonly" name="fechaVencimiento" value="" size="10" MAXLENGTH="10">
                            </div>   
                            <div>
                                <label for="noActualizacionesPermitidas">No. de Actualizaciones Permitidas</label>
                                <input name="noActualizacionesPermitidas" type="text" id="noActualizacionesPermitidas" value="" size="50" >
                            </div>    
                            <div>
                                <label for="upgrade">Upgrade</label>
                                <input type="checkbox" name="upgrade" value="" id="upgrade">
                            </div>
                            <div>
                                <label for="degrade">Degrade</label>
                                <input type="checkbox" name="degrade" value="1" id="degrade">
                            </div>   
                            <div>
                                <label for="SoporteTecnico">*Cuenta con Soporte Técnico</label>
                                <select name="SoporteTecnico" onchange="getSoporte(this.value);">                                            
                                    <option value=""></option>
                                    <option value=<%=WebUtil.encode(session, "1")%>>Sí</option>
                                    <option value=<%=WebUtil.encode(session, "0")%>>No</option>
                                </select>
                            </div>
                            <div id="divdatosSoporte" name="divdatosSoporte">
                            </div>
                            <div>
                                <label for="hddRequerido">Disco duro requerido(MB)</label>
                                <input name="hddRequerido" type="text" id="hddRequerido" value="" size="50" >
                            </div>
                            <div>
                                <label for="ramRequerida">Memoria RAM requerida(MB)</label>
                                <input name="ramRequerida" type="text" id="ramRequerida" value="" size="50" >
                            </div>
                            <div>
                                <label for="soRequerido">Sistema Operativo Requerido</label>          
                                <input name="soRequerido" type="text" id="soRequerido" value="" size="50" >
                            </div>
                            <div>
                                <label for="nombreResponsable">*Responsable del resguardo</label>
                                <input type="text" name="nombreResponsable" rows="4" cols="50" size="25">
                            </div>   
                            <div>
                                <label for="fechaInstalacion">Fecha de instalación</label>
                                <input type="text" class="w8em format-y-m-d divider-dash highlight-days-67" id="fechaInstalacion" onmouseover="calendarField(id)" readonly="readonly" name="fechaInstalacion" value="" size="10" MAXLENGTH="10">
                            </div>
                            <div>
                                <label for="status">*Estatus</label>
                                <select name="status">                                            
                                    <option value=""></option>
                                    <option value=<%=WebUtil.encode(session, "1")%>>Activo</option>
                                    <option value=<%=WebUtil.encode(session, "2")%>>Inactivo </option>
                                    <option value=<%=WebUtil.encode(session, "3")%>>Sin instalar</option>
                                    <option value=<%=WebUtil.encode(session, "4")%>>Instalado </option>
                                    <option value=<%=WebUtil.encode(session, "5")%>>Obsoleto</option>
                                </select>
                            </div> 
                            <div>
                                <label for="observaciones">Observaciones</label>
                                <textarea name="observaciones" rows="4" cols="50" size="25"></textarea> 
                            </div>
                            <div id="botonEnviarDiv" >
                                <input type="button" value="Guardar" name="Enviar" onclick="enviarInfo();"/>
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