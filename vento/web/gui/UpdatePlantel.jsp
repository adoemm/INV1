<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("ConsultaPlanteles");

                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                    Iterator it = null;
                    LinkedList listAux = null;
                    LinkedList listAux2 = new LinkedList();
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Actualizar Plantel</title>
        <jsp:include page='<%=PageParameters.getParameter("globalLibs")%>'/>        
        <jsp:include page='<%=PageParameters.getParameter("styleFormCorrections")%>'/>
        <script type="text/javascript" language="javascript" charset="utf-8">
            function getMunicipio() {
                var estado = document.getElementById("estado").value;
                if (estado !== "") {
                    //document.getElementById('municipioDiv').innerHTML = '';
                    var response = $.ajax({
                        type: 'get',
                        contentType: 'application/x-www-form-urlencoded;charset=utf-8',
                        cache: false,
                        url: "<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("ajaxFunctions")%>/getMunicipiosXEstado.jsp",
                        data: '<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idEstado=' + estado,
                        async: false,
                        success: function(data) {
                            // Hagamos algo maravilloso con estos nuevos datos
                        }
                    }).responseText;
                    document.getElementById('municipioDiv').innerHTML = response;
                } else {
                    //document.getElementById('municipioDiv').innerHTML = '';
                }
            }
        </script>      
        <script type="text/javascript" language="javascript" charset="utf-8">
            function enviarInfo() {
                //alert("enviando info");
                $.ajax({type: 'POST'
                    , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                    , cache: false
                    , async: false
                    , url: '<%=PageParameters.getParameter("mainController")%>'
                    , data: $('#updatePlantel').serialize()
                    , success: function(response) {
                        $('#wrapper').find('#divResult').html(response);
                    }});
            }
        </script>
        <script type="text/javascript" language="javascript" charset="utf-8">
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
                                <a class="NVL" href="<%=PageParameters.getParameter("mainMenu")%>?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"> Menú Principal</a>
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/MenuCatalogos.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>">Catálogos del Sistema</a> 
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaPlanteles.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>">Catálogo de Planteles</a> 
                                > Actualizar Plantel
                            </td>
                            <td width="36" align="right" valign="top">
                                <script language="JavaScript" src="<%=PageParameters.getParameter("jsRcs")%>/funcionDate.js" type="text/javascript"></script>
                            </td>
                        </tr>                        
                    </table>
                    <%
                        it = null;
                        listAux = QUID.select_PlantelXID(Integer.parseInt(WebUtil.decode(session, request.getParameter("ID_Plantel"))));
                    %>
                    <form id="updatePlantel" name="updatePlantel" method="post" action="" enctype="application/x-www-form-urlencoded" id="form">
                        <input type="hidden" name="imix" value="<%=WebUtil.encode(session, UTime.getTimeMilis())%>"/>
                        <input type="hidden" name="ID_Plantel" value="<%=request.getParameter("ID_Plantel")%>"/>     
                        <input type="hidden" name="FormFrom" value="updatePlantel"/>                        
                        <fieldset>
                            <legend>Actualizar plantel</legend>
                            <div>
                                <label for="nombre">*Nombre del Plantel:</label>
                                <input name="nombre" type="text" id="nombre" value="<%=listAux.get(1)%>" size="40">
                            </div> 
                            <div>
                                <label for="direccion">*Dirección:</label>
                                <textarea name="direccion" type="text" id="direccion" cols="31" rows="5"><%=listAux.get(2)%></textarea>
                            </div> 
                            <div>
                                <label for="claveCentroTrabajo">*Clave del Centro de Trabajo:</label>
                                <input name="claveCentroTrabajo" type="text" id="claveCentroTrabajo" value="<%=listAux.get(3)%>" size="40">
                            </div> 
                            <div>
                                <label for="telefono">*Teléfono:</label>
                                <input name="telefono" type="text" id="telefono" value="<%=listAux.get(4)%>" size="40">
                            </div>
                            <div>
                                <label for="correo">*Correo Electrónico:</label>
                                <input name="correo" type="text" id="correo" value="<%=listAux.get(5)%>" size="40">
                            </div>
                        </fieldset>
                        <div id="botonEnviarDiv">
                            <input type="button" value="Actualizar" name="Enviar" onclick="enviarInfo();"/>
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
