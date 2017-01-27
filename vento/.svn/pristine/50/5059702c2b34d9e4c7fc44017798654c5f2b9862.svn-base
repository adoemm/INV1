<%@page import="java.util.Calendar"%>
<%@page import="jspread.core.util.SystemUtil"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("InsertMovimientoEntrada");

                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                    Iterator it = null;
                    LinkedList listAux = null;
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Nueva Entrada</title>
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
                    , success: function(response) {
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
                                <a class="NVL" href="<%=PageParameters.getParameter("mainMenu")%>"> Menú Principal</a>
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/MenuConsumibles.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>">Menú Consumibles</a> 
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaMovimientoEntrada.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idPlantel=<%=request.getParameter("idPlantel")%>">Movimientos de Entrada</a> 
                                > Nueva Entrada
                            </td>
                            <td width="36" align="right" valign="top">
                                <script language="JavaScript" src="<%=PageParameters.getParameter("jsRcs")%>/funcionDate.js" type="text/javascript"></script>
                            </td>
                        </tr>                        
                    </table>
                        <%
                        int idTipoMovimiento=QUID.select_TipoMovimiento("ENTRADA");
                        %>
                    <form name="insertMovimientoEntrada" method="post" action="" enctype="application/x-www-form-urlencoded" id="insertMovimientoEntrada">
                        <input type="hidden" name="<%=WebUtil.encode(session, "imix")%>" value="<%=WebUtil.encode(session, UTime.getTimeMilis())%>"/>
                        <input type="hidden" name="FormFrom" value="insertMovimientoEntrada"/>
                        <input type="hidden" name="idTipoMovimiento" value="<%=WebUtil.encode(session, idTipoMovimiento)%>"/>
                        
                        <fieldset>
                            <legend>Nueva Entrada</legend>
                            <jsp:include page='<%=PageParameters.getParameter("ajaxFunctions") + "/getSelectPlantelSection.jsp"%>' flush = 'true'>
                                <jsp:param name='plantelActual' value='<%=request.getParameter("idPlantel") != null ? request.getParameter("idPlantel") : WebUtil.encode(session, session.getAttribute("FK_ID_Plantel"))%>' />
                            </jsp:include>
                            
                            <div>
                                <%
                                   Iterator t = QUID.select_Proveedor().iterator();
                                %>
                                <label for="idProveedor">*Proveedor</label>          
                                <select name="idProveedor">
                                    <option value=""></option>
                                    <%
                                        while (t.hasNext()) {
                                            LinkedList datos = (LinkedList) t.next();
                                    %>
                                    <option value="<%=WebUtil.encode(session,datos.get(0))%>"><%=datos.get(1)%></option>
                                    <%
                                        }
                                    %>
                                </select>
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
                                <label for="fechaMovimiento">*Fecha de Registro</label>
                                <input type="text" class="w8em format-y-m-d divider-dash highlight-days-67" id="fechaMovimiento"   name="fechaMovimiento" value="<%=UTime.calendar2SQLDateFormat(Calendar.getInstance())%>" size="10" MAXLENGTH="10">
                            </div>
                            <div>
                                <label for="noFactura">*No. Factura</label>
                                <input type="text" name="noFactura" >
                            </div>
                                 <div>
                                <label for="fechaFactura">*Fecha de Factura</label>
                                <input type="text" class="w8em format-y-m-d divider-dash highlight-days-67" id="fechaFactura"   name="fechaFactura" value="" size="10" MAXLENGTH="10">
                            </div>
                            <div>
                                <label for="noReferencia">*No. Referencia</label>
                                <input type="text" name="noReferencia" >
                            </div>
                            <div>
                                <label for="iva">*Monto IVA</label>
                                <input type="text" name="iva" >
                            </div>
                            <div>
                                <label for="motivoMovimiento">Motivo</label>
                                <textarea  name="motivoMovimiento" cols="30" rows="5"></textarea>
                            </div>
                            <div>
                                <label for="estatus">*Estatus</label>
                                <select name="estatus">
                                    <option value=""></option>
                                    <option value="<%=WebUtil.encode(session,"Completo")%>">Completo</option>
                                    <option value="<%=WebUtil.encode(session,"Incompleto")%>"">Incompleto</option>
                                </select>
                            </div>
                            <div>
                                <label for="observaciones">Observaciones</label>
                                <textarea  name="observaciones" cols="30" rows="5" id="observaciones"></textarea>
                            </div>
                        </fieldset>
                        <div id="botonEnviarDiv" >
                            <input type="button" value="Guardar" name="Enviar" onclick="enviarInfo(document.getElementById('insertMovimientoEntrada'));">
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