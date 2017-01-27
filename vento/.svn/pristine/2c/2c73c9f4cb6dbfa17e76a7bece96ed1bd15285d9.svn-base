<%@page import="jspread.core.util.SystemUtil"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("InsertConteoFisico");

                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                    Iterator it = null;
                    LinkedList listAux = null;
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Realizar Inventario</title>
        <jsp:include page='<%=PageParameters.getParameter("globalLibs")%>'/>        
        <jsp:include page='<%=PageParameters.getParameter("styleFormCorrections")%>'/>   
        <jsp:include page='<%=PageParameters.getParameter("ajaxFunctions") + "/tablaCapturaCSS.jsp"%>'/>
        <script type="text/javascript" language="javascript" charset="utf-8">

            function enviarInfo(form) {
                $('#loadingScreen').show();
                $('#contenent_info').hide();
                $.ajax({type: 'POST'
                    , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                    , cache: false
                    , async: true
                    , url: '<%=PageParameters.getParameter("mainController")%>'
                    , data: $(form).serialize()
                    , success: function (response) {
                        $('#loadingScreen').hide();
                        $('#contenent_info').show();
                        if (response.indexOf("title: \"Información\"") >= 0) {
                            $('#wrapper').find('#divConsumible').html('');
                        }
                        $('#wrapper').find('#divResult').html(response);
                    }});
            }
            function cerrarInventario(form) {
                $.msgBox({
                    title: "Confirmar"
                    , content: "Esta seguro que desea terminar el inventario?"
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
                                , data: $(form).serialize()
                                , success: function (response) {
                                    $('#wrapper').find('#divResult').html(response);
                                }});
                        }

                    }
                });
            }
            function getSubcategoria(idCategoria) {
                if (idCategoria !== null) {
                    $.ajax({type: 'POST'
                        , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                        , cache: false
                        , async: false
                        , url: "<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("ajaxFunctions")%>/getSubcategoria4Categoria.jsp"
                        , data: '<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idCategoria=' + idCategoria + '&onChange=getConsumible'
                        , success: function (response) {
                            $('#wrapper').find('#divSubcategoria').html(response);
                        }});
                }
            }
            function getConsumible() {
                $.ajax({type: 'POST'
                    , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                    , cache: false
                    , async: false
                    , url: "<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("ajaxFunctions")%>/getCosumible4Conteo4Subcategoria.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"
                                , data: $('#selectCategoriaForm').serialize()
                                , success: function (response) {
                                    $('#wrapper').find('#divConsumible').html(response);
                                }});
                        }



                        function validaFloat(value) {
                            return(value.match(/^[0-9]+(.[0-9]+)?$/))
                        }

                        function validaInputFloat(oInput) {
                            if (oInput.value !== '' && !validaFloat(oInput.value)) {
                                alert(oInput.value + " no es una cantidad válida!");
                                try {
                                    oInput.value = "";
                                    oInput.focus();
                                }
                                catch (ex) {
                                }
                                return(false);
                            }

                            return(true);
                        }
                        function calculaVariacion(oInput, target, value) {
                            if (validaInputFloat(oInput)) {
                                if (oInput.value === '') {
                                    oInput.value = 0;
                                }
                                document.getElementById(target).value = parseFloat(oInput.value) - value;
//            $('#'+target).val(parseFloat(oInput.value)-value);
                            }

                        }
        </script>
    </head>
    <body onload="$('#loadingScreen').hide();">
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
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaConteoFisico.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>">Toma de Inventario</a> 
                                > Realizar Inventario
                            </td>
                            <td width="36" align="right" valign="top">
                                <script language="JavaScript" src="<%=PageParameters.getParameter("jsRcs")%>/funcionDate.js" type="text/javascript"></script>
                            </td>
                        </tr>                        
                    </table>
                    <div id="loadingScreen" style="width:100%; height: 300px; font-size: 22px;">                                
                    </div>
                    <script type="text/javascript" language="javascript" charset="utf-8">
        $("#loadingScreen").Loadingdotdotdot({
            "speed": 400,
            "maxDots": 6
        });
                    </script>
                    <div id="contenent_info" >  
                        <div>
                            <%
                                LinkedList conteo = QUID.select_ConteoFisico(WebUtil.decode(session, request.getParameter("idConteoFisico")));
                            %>
                            <form name="terminarInventario" method="post" action="" enctype="application/x-www-form-urlencoded" id="terminarInventario">
                                <input type="hidden" name="<%=WebUtil.encode(session, "imix")%>" value="<%=WebUtil.encode(session, UTime.getTimeMilis())%>"/>
                                <input type="hidden" name="FormFrom" value="terminarInventario"/>
                                <input type="hidden" name="idPlantel" value="<%=request.getParameter("idPlantel")%>"/>
                                <input type="hidden" name="idConteoFisico" value="<%=request.getParameter("idConteoFisico")%>"/>
                                <fieldset>
                                    <legend>Cálculo Actual</legend>
                                    <div>
                                        <label>Plantel</label>
                                        <input type="text" value="<%=conteo.get(5)%>" disabled>
                                    </div>
                                    <div>
                                        <label>Fecha</label>
                                        <input type="text" value="<%=conteo.get(2)%>" disabled>
                                    </div>
                                    <div>
                                        <label>Estatus</label>
                                        <input type="text" value="<%=conteo.get(0)%>" disabled>
                                    </div>
                                </fieldset>
                                <div id="botonEnviarDiv" >
                                    <input type="button" value="Terminar Inventario" name="Terminar Inventario" onclick="cerrarInventario(document.getElementById('terminarInventario'));"/>
                                </div> 
                            </form>
                        </div>
                        <form name="selectCategoriaForm" method="post" action="" enctype="application/x-www-form-urlencoded" id="selectCategoriaForm">
                            <input type="hidden" name="<%=WebUtil.encode(session, "imix")%>" value="<%=WebUtil.encode(session, UTime.getTimeMilis())%>"/>
                            <input type="hidden" name="FormFrom" value="selectCategoriaForm"/>
                            <input type="hidden" name="idPlantel" value="<%=request.getParameter("idPlantel")%>"/>
                            <input type="hidden" name="idConteoFisico" value="<%=request.getParameter("idConteoFisico")%>"/>
                            <fieldset>
                                <legend>Realizar Inventario</legend>
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

                            </fieldset>
                        </form>
                        <form name="insertConteoFisicoConsumible" method="post" action="" enctype="application/x-www-form-urlencoded" id="insertConteoFisicoConsumible">
                            <input type="hidden" name="<%=WebUtil.encode(session, "imix")%>" value="<%=WebUtil.encode(session, UTime.getTimeMilis())%>"/>
                            <input type="hidden" name="FormFrom" value="insertConteoFisicoConsumible"/>
                            <input type="hidden" name="idPlantel" value="<%=request.getParameter("idPlantel")%>"/>
                            <input type="hidden" name="idConteoFisico" value="<%=request.getParameter("idConteoFisico")%>"/>
                            <div id="divConsumible"></div>    
                            <div id="botonEnviarDiv" >
                                <input type="button" value="Guardar" name="Enviar" onclick="enviarInfo(document.getElementById('insertConteoFisicoConsumible'));"/>
                            </div> 
                        </form>
                    </div>
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