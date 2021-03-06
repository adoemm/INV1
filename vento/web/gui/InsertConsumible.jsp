<%@page import="jspread.core.util.SystemUtil"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("InsertConsumible");

                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                    Iterator it = null;
                    LinkedList listAux = null;
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Nuevo Consumible</title>
        <jsp:include page='<%=PageParameters.getParameter("globalLibs")%>'/>        
        <jsp:include page='<%=PageParameters.getParameter("styleFormCorrections")%>'/>   
        <jsp:include page='<%=PageParameters.getParameter("ajaxFunctions") + "/tablaCapturaCSS.jsp"%>'/>
        <script type="text/javascript" language="javascript" charset="utf-8">
            function resetForm() {
                document.getElementById("clave").value = '';
                document.getElementById("descripcion").value = '';
                document.getElementById("noReferencia").value = '';
                document.getElementById('idMedida').selectedIndex = "0";
                document.getElementById("precioActual").value = '';
                document.getElementById("total").value = '';
                document.getElementById('status').selectedIndex = "0";
                document.getElementById("observaciones").value = '';
            }
            function enviarInfo(form) {
               
                $.ajax({type: 'POST'
                    , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                    , cache: false
                    , async: false
                    , url: '<%=PageParameters.getParameter("mainController")%>'
                    , data: $(form).serialize()
                    , success: function(response) {
                        if (!(response.indexOf("title: \"Error\"") >= 0)) {
                            resetForm();
                        }
                        $('#wrapper').find('#divResult').html(response);


                    }});
            }



            function getSubcategoria(idCategoria) {
                if (idCategoria !== null) {
                    $.ajax({type: 'POST'
                        , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                        , cache: false
                        , async: false
                        , url: "<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("ajaxFunctions")%>/getSubcategoria4Categoria.jsp"
                        , data: '<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idCategoria=' + idCategoria
                        , success: function(response) {
                            $('#wrapper').find('#divSubcategoria').html(response);
                        }});
                }
            }
            function getMedida() {                
                    $.ajax({type: 'POST'
                        , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                        , cache: false
                        , async: false
                        , url: "<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("ajaxFunctions")%>/getMedida.jsp"
                        , data: '<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>'
                        , success: function(response) {
                            $('#wrapper').find('#divMedida').html(response);
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
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaConsumible.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idPlantel=<%=request.getParameter("idPlantel")%>">Inventario de Consumibles</a> 
                                > Nuevo Consumible
                            </td>
                            <td width="36" align="right" valign="top">
                                <script language="JavaScript" src="<%=PageParameters.getParameter("jsRcs")%>/funcionDate.js" type="text/javascript"></script>
                            </td>
                        </tr>                        
                    </table>
                    <form name="insertConsumible" method="post" action="" enctype="application/x-www-form-urlencoded" id="insertConsumible">
                        <input type="hidden" name="<%=WebUtil.encode(session, "imix")%>" value="<%=WebUtil.encode(session, UTime.getTimeMilis())%>"/>
                        <input type="hidden" name="FormFrom" value="insertConsumible"/>
                        <fieldset>
                            <legend>Nuevo Consumible</legend>
                            <jsp:include page='<%=PageParameters.getParameter("ajaxFunctions") + "/getSelectPlantelSection.jsp"%>' flush = 'true'>
                                <jsp:param name='plantelActual' value='<%=request.getParameter("idPlantel") != null ? request.getParameter("idPlantel") : WebUtil.encode(session, session.getAttribute("FK_ID_Plantel"))%>' />
                            </jsp:include>
                            <div>
                                <label for="idCategoria">*Categoria</label>
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
                                <label for="clave">*Clave</label>
                                <input type="text" name="clave" size="30" id="clave">
                            </div>
                            <div>
                                <label for="descripcion">*Descripción</label>
                                <textarea  id="descripcion" name="descripcion" cols="30" rows="5"></textarea>
                            </div>
                            <div>
                                <label for="noReferencia">No. de Parte/Referencia</label>
                                <input type="text" name="noReferencia" id="noReferencia">
                            </div>
                            
                               
                            <div id="divMedida">
                                <label for="idMedida" onclick="getMedida();" title="Recargar" class="labelAction">*Medida</label>
                                <select name="idMedida" id="idMedida">
                                    <option value=""></option>
                                    <%
                                        t = QUID.select_Medida().iterator();
                                        while (t.hasNext()) {
                                            LinkedList aux = (LinkedList) t.next();
                                    %>
                                    <option value="<%=WebUtil.encode(session, aux.get(0))%>"><%=aux.get(1)%></option>
                                    <%
                                        }
                                    %>
                                </select>
                            </div>
                                
                            <div>
                                <label for="precioActual">*Precio Actual</label>
                                <input type="text" name="precioActual" size="30" id="precioActual">
                            </div>
                            <div>
                                <label for="total">*Total/Existencias</label>
                                <input type="text" name="total" size="30" id="total">
                            </div>
                            <div>
                                <label for="status">*Estatus</label>
                                <select name="status" id="status">
                                    <option value=""></option>
                                    <option value="<%=WebUtil.encode(session, "Activo")%>">Activo</option>
                                    <option value="<%=WebUtil.encode(session, "Inactivo")%>">Inactivo</option>
                                    <option value="<%=WebUtil.encode(session, "Agotado")%>">Agotado</option>
                                    <option value="<%=WebUtil.encode(session, "Disponible")%>">Disponible</option>
                                </select>
                            </div>
                            <div>
                                <label for="observaciones">Observaciones</label>
                                <textarea  name="observaciones" cols="30" rows="5" id="observaciones"></textarea>
                            </div>
                            <div id="botonEnviarDiv" >
                                <input type="button" value="Guardar" name="Enviar" onclick="enviarInfo(document.getElementById('insertConsumible'));"/>
                                <input type="button" value="Nuevo" name="Nuevo" onclick="resetForm();"/>
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